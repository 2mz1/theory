# CHAPTER 4. 스웜을 이용한 실전 애플리케이션 개발


## 01. 웹 애플리케이션 구성

- TODO 등록, 수정, 삭제
- 등록된 TODO 목록 출력
- 브라우저에서 사용할 수 있는 웹 앱
- 브라우저 외에서도 사용할 수 있는 JSON API endpoint 제공


### 아키텍처

오케스트레이션 시스템 → 도커 스웜

목표: 컨테이너 중심의 애플리케이션 개발에 대한 기초적인 기술 습득

<br/><br/><img src="./image/image01.png" width="70%" /><br/>

**아키텍처 구성 요소**

| image | usage                       | service name               | stack name           |
|-------|-----------------------------|----------------------------|----------------------|
| MySQL | data store                  | mysql_master, mysql_slave  | MySQL                |
| API   | 데이터스토어를 다룰 API Server   | app_api                    | Application          |
| Web   | 뷰 생성을 담당하는 Web Server   | fronted_web                | Frontend             |
| Nginx | Proxy Server                | app_nginx, frontend_nginx  | Application Frontend |

<br/>

**✔️ MySQL**

Master-Slave 구성하여 데이터 스토어로 사용

마스터 컨테이너에는 레플리카 하나만 제작

<small>실제 운영 환경에서는 마스터가 SPOF 가 되지 않게 가용성 확보를 위한 추가적인 조치를 진행</small>

<br/>

**✔️ API**

TODO 앱에 특화된 마이크로서비스로 볼 수 있는 RESTful API

<br/>

**✔️ Web UI**

Node.js를 사용하여, 브라우저에 나타낼 웹 페이지를 렌더링하는 역할

<br/>

**✔️ Nginx**

앱 프론트엔드 서버 및 API 앞단에 리버스 프록시 역할

Caching, Routing, Access Logging 등의 역할도 수행

#### 배치 정략

스웜 환경은 3장에서 만든 것 드대로 사용

```bash
> docker container exec -it manager node ls

```

<br/>

### 스택 구성

<br/><img src="./image/image01.png" width="60%"/><br/>

overlay 네트워크 생성

```Bash
❯ docker container exec -it manager docker network create --driver=overlay --attachable todoapp
k5qfdm4rxaiqpr4hddz8735h7
```

<br/>

## 02. MySQL 서비스 구축

### MySQL 설정
- `etc/mysql/mysql.conf.d/mysqld.conf`
- Master-Slave 를 구성하기 위해선 MySQL 설정 파일인 `mysqld.conf`를 수정해야 함


실습 자료의 `mysqld.conf` 를 확인하면 아래와 같음

```Bash[mysqld]
character-set-server = utf8mb4
collation-server = utf8mb4_general_ci
pid-file	= /var/run/mysqld/mysqld.pid
socket		= /var/run/mysqld/mysqld.sock
datadir		= /var/lib/mysql
#log-error	= /var/log/mysql/error.log
# By default we only accept connections from localhost
#bind-address	= 127.0.0.1
# Disabling symbolic-links is recommended to prevent assorted security risks
symbolic-links=0
relay-log=mysqld-relay-bin 
relay-log-index=mysqld-relay-bin 

log-bin=/var/log/mysql/mysql-bin.log  # ①

# ②
```

<br/>

**✔️ ①: log-bin**

Replication을 사용하려면 바이너리 로그가 필요

`log-bin`을 통해 바이너리 로그를 출력할 경로를 설정

`log-bin`은 Master와 Slave 모두 사용

<br/>

**✔️ ②: server-id**

`server-id` 값은 서버를 식별하기 위한 유일한 값으로, 서로 중복 되어선 안됨

마스터와 슬레이브로 구성된 스택 안에서 중복이 없도록 아래와 같이 설정해야함

```Bash
server-id = 1
```

다중 MySQL 컨테이너를 띄우기 때문에, server-id 를 shell script 를 이용해 동적으로 작성

_add-server-id.sh_ :

```Bash
#!/bin/bash -e
OCTETS=(`hostname -i | tr -s '.' ' '`)

MYSQL_SERVER_ID=`expr ${OCTETS[2]} \* 256 + ${OCTETS[3]}`
echo "server-id=$MYSQL_SERVER_ID" >> /etc/mysql/mysql.conf.d/mysqld.cnf
```

위 스크립트는 컨테이너가 실행될 때 동적으로 `mysqld.conf` 하위에 `server-id = n`을 입력

<br/>

### Replication 설정

**prepare.sh 분석하기 🔍**

<table>
<tr>
<th>STEP</th>
<th>CODE</th>
<th>DESC</th>
</tr>
<tr><th>① 환경 변수로 마스터와 슬레이브 지정</th><td>

```Bash
if [ ! -z "$MYSQL_MASTER" ]; then
  echo "this container is master"
  exit 0
fi

echo "prepare as slave"
```

</td><td>

- `MYSQL_MASTER` 값에 따라 Master 혹은 Slave 동작 여부가 결정됨
- `prepare.sh` 파일은 대부분 Slave 설정

</td></tr>
<tr><th>② Slave와 Master 간의 통신 확인</th><td>

```Bash
if [ -z "$MYSQL_MASTER_HOST" ]; then
  echo "mysql_master_host is not specified" 1>&2
  exit 1
fi

while :
do
  if mysql -h $MYSQL_MASTER_HOST -u root -p$MYSQL_ROOT_PASSWORD -e "quit" > /dev/null 2>&1 ; then
    echo "MySQL master is ready!"
    break
  else
    echo "MySQL master is not ready"
  fi
  sleep 3
done
```

</td><td>

- 슬레이브가 마스터에 MySQL 명령을 실행하려면 마스터의 호스트 명을 알아야 하기 때문에 `MYSQL_MASTER_HOST` 변수 전달
- Slave에서 Master에 명령을 실행하려면 호스트명을 `-h` 옵션으로 주면 됨
- 3초 마다 Master와 통신을 시도하면서 통신이 가능한지 확인

</td></tr>
<tr><th>③ Master에 Replica 사용자 및 권한 추가</th><td>

```Bash
IP=`hostname -i`
IFS='.'
set -- $IP
SOURCE_IP="$1.$2.%.%"
mysql -h $MYSQL_MASTER_HOST -u root -p$MYSQL_ROOT_PASSWORD -e "CREATE USER IF NOT EXISTS '$MYSQL_REPL_USER'@'$SOURCE_IP' IDENTIFIED BY '$MYSQL_REPL_PASSWORD';"
mysql -h $MYSQL_MASTER_HOST -u root -p$MYSQL_ROOT_PASSWORD -e "GRANT REPLICATION SLAVE ON *.* TO '$MYSQL_REPL_USER'@'$SOURCE_IP';"
```

</td><td>

- Replication을 사용하려면 마스터에 레플리카 사용자 및 사용자 권한을 추가해야 함

```SQL
mysql> CREATE USER IF NOT EXISTS 'replication_user_name'@'slave_IP_address' IDENTIFIED BY 'replication_user_password';
mysql> GRANT REPLICATION SLAVE ON *.* TO 'replication_user_name'@'slave_IP_address';
```

- `GRANT REPLICATION SLAVE` 명령어에서 Slave의 IP 주소 값 (slave_IP_address)을 입력해야 하는데,
- Master에서는 Slave의 호스트명이나 IP를 알 수 없기 때문에 `hostname -i` 명령을 이용해 이 값을 받아옴
- IP 주소를 받아와 이 값을 x.x.%.% 포맷으로 `SOURCE_IP` 변수에 대입
    - 가령, IP가 `10.0.0.23` 이라면 `10.0.%.%` 가 변수에 저장됨
    - `10.0.0.0/16` 서브넷에 속하는 컨테이너에서 마스터에 접근할 수 있음
- `MYSQL_MASTER_HOST`, `MYSQL_REPL_USER`, `MYSQL_REPL_PASSWORD` 값으로 사용자 등록 및 권한 설정

</td></tr>
<tr><th>④ Master binlog의 위치 설정</th><td>

```Bash
MASTER_STATUS_FILE=/tmp/master-status
mysql -h $MYSQL_MASTER_HOST -u root -p$MYSQL_ROOT_PASSWORD -e "SHOW MASTER STATUS\G" > $MASTER_STATUS_FILE
BINLOG_FILE=`cat $MASTER_STATUS_FILE | grep File | xargs | cut -d' ' -f2`
BINLOG_POSITION=`cat $MASTER_STATUS_FILE | grep Position | xargs | cut -d' ' -f2`
echo "BINLOG_FILE=$BINLOG_FILE"
echo "BINLOG_POSITION=$BINLOG_POSITION"
```

</td><td>

- 애플리케이션 실행을 위해, Slave 가 Master의 host 및 binlog 파일명, binlog 위치를 알아야 함

<pre>해당 값을 직접 확인하고 싶다면, 아래 명령어를 입력하여 확인할 수 있음

<code>
mysql> show master status;
// TODO 실습 후 확인해서 기입하기
</code>
</pre>
</td></tr>
<tr><th>⑤ Replication 시작</th><td>

```Bash
mysql -u root -p$MYSQL_ROOT_PASSWORD -e "CHANGE MASTER TO MASTER_HOST='$MYSQL_MASTER_HOST', MASTER_USER='$MYSQL_REPL_USER', MASTER_PASSWORD='$MYSQL_REPL_PASSWORD', MASTER_LOG_FILE='$BINLOG_FILE', MASTER_LOG_POS=$BINLOG_POSITION;"
mysql -u root -p$MYSQL_ROOT_PASSWORD -e "START SLAVE;"

echo "slave started"
```

</td><td>

이후 `CHANGE MASTER TO MASTER_HOST='master', MASTER_USER='repl', MASTER_PASSWORD='gngsn', MASTER_LOG_FILE='mysql-bin.000003', MASTER_LOG_POS=605;"`

명령어를 입력하고 `START SLAVE` 명령을 실행 후, Replication 실행

</td></tr></table>

<br/>

### MySQL (mysql_master/mysql_slave) Dockerfile

<table>
<tr><th>STEP</th><th>CODE</th><th>DESC</th></tr>
<tr><th>① 패키지 업데이트 및 `wget` 설치</th><td>

```Bash
RUN apt-get update
RUN apt-get install -y wget
```

</td><td>

- 패키지 목록을 업데이트하고 `docker image build` 명령에서 사용하는 `entrykit`을 받아오기 위해 `wget`을 설치

</td></tr>
<tr><th>② entrykit 설치</th><td>

```Bash
RUN wget https://github.com/progrium/entrykit/releases/download/v0.4.0/entrykit_0.4.0_linux_x86_64.tgz
RUN tar -xvzf entrykit_0.4.0_linux_x86_64.tgz
RUN rm entrykit_0.4.0_linux_x86_64.tgz
RUN mv entrykit /usr/local/bin/
RUN entrykit --symlink
```

</td><td>

- entrykit 설치: 컨테이너 실행 시 처리할 내용을 기술하기 위한 도구
- **주 프로세스보다 먼저 실행할 명령이 있는 경우 유용**

</td></tr>
<tr><th>③ 스크립트 및 각종 설정 파일 복사</th><td>

```Bash
COPY add-server-id.sh /usr/local/bin/
COPY etc/mysql/mysql.conf.d/mysqld.cnf /etc/mysql/mysql.conf.d/
COPY etc/mysql/conf.d/mysql.cnf /etc/mysql/conf.d/
COPY prepare.sh /docker-entrypoint-initdb.d
COPY init-data.sh /usr/local/bin/
COPY sql /sql
```

</td><td>

- MySQL 컨테이너를 구성하기 위한 파일과 스크립트를 `tododb`에서 컨테이너로 복사

</td></tr>
<tr><th>④ 스크립트, `mysqld` 실행</th><td>

```Bash
ENTRYPOINT [ \
  "prehook", \
    "add-server-id.sh", \
    "--", \
  "docker-entrypoint.sh" \
]
```

</td><td>

- 컨테이너에서 실행할 내용 기술
- fyi. `docker-entrypoint.sh`는 `mysql:5.7` 에 포함된 파일

- 위에서 살펴봤다시피, `add-server-id.sh` 파일 (`server-id` 값을 `mysqld.cnf` 에 추가하는 파일)은 DB 시작 전에 실행되어야 함
- 이를 위해, entrykit의 prehook 명령 사용

</td></tr>
</table>

<br/>

### 빌드 및 스웜 클러스터에서 사용하기

1. image build

```Bash
docker image build -t gngsn/tododb:latest .
```

<table>
<tr>
<th>Error #1</th>
<td>

```Bash
❯ docker image build -t ch04/tododb:latest .
[+] Building 3.5s (3/3) FINISHED                                                                                                                                                                                               docker:desktop-linux
 => [internal] load .dockerignore                                                                                                                                                                                                              0.0s
 => => transferring context: 2B                                                                                                                                                                                                                0.0s
 => [internal] load build definition from Dockerfile                                                                                                                                                                                           0.0s
 => => transferring dockerfile: 945B                                                                                                                                                                                                           0.0s
 => ERROR [internal] load metadata for docker.io/library/mysql:5.7                                                                                                                                                                             3.5s
------
 > [internal] load metadata for docker.io/library/mysql:5.7:
------
Dockerfile:1
--------------------
   1 | >>> FROM mysql:5.7
   2 |     
   3 |     RUN apt-get update
--------------------
ERROR: failed to solve: failed to parse stage name "arm64v8/mysql:oracle:5.7": invalid reference format
```

#### Solution #1

add `--platform linux/x86_64` flag

</td>
</tr>
<tr>
<th>Error #2</th>
<td>

```Bash
❯ docker image build --platform linux/x86_64  -t ch04/tododb:latest .
[+] Building 30.7s (6/18) 
...
 => => extracting sha256:b0e9b86ed64c8df8320596d475d3bbc4927e1e8bdc9ea97473c7e38024ae9c82                                                                                                                                                      0.0s
 => => extracting sha256:bfef93045c96cfc909e0b6b4d373e5cb88f5a1c92c22754bf1a353220e24f02c                                                                                                                                                      0.0s
 => [internal] load build context                                                                                                                                                                                                              0.0s
 => => transferring context: 5.14kB                                                                                                                                                                                                            0.0s
 => ERROR [ 2/14] RUN apt-get update                                                                                                                                                                                                           0.4s
------
 > [ 2/14] RUN apt-get update:
0.116 /bin/sh: apt-get: command not found
------
Dockerfile:3
--------------------
   1 |     FROM mysql:5.7
   2 |     
   3 | >>> RUN apt-get update
   4 |     RUN apt-get install -y wget
   5 |     
--------------------
ERROR: failed to solve: process "/bin/sh -c apt-get update" did not complete successfully: exit code: 127
```

#### Debugging #2

add `--progress=plain` option: 빌드 내용들을 모두 출력해줌

<br/>

#### Reason #2

```Bash
 > [ 2/14] RUN apt-get -y update:
0.127 /bin/sh: apt-get: command not found
```

`apt-get` 명령어가 기존 이미지에 없음


#### Solution #2

Docker image 변경

`mysql:5.7` → `mysql:5.7-debian`


</td>
</tr><tr>
<th>Error #3</th>
<td>

```Bash
 > [ 8/14] RUN entrykit --symlink:
0.127 runtime: failed to create new OS thread (have 2 already; errno=22)
0.127 fatal error: newosproc
0.130 
0.130 runtime stack:
0.130 runtime.throw(0x84a820, 0x9)
```

#### Reason #3

Arm version의 컨테이너에서 발생하는 문제

해결법 없음: https://github.com/docker/for-mac/issues/6083

<br/>

#### Solution #3

Dockerfile 수정

entrykit 사용하지 않고 ENTRYPOINT 로만 사용

```Bash
FROM --platform=linux/amd64 mysql:5.7-debian

RUN apt-get -y update
RUN apt-get install -y wget

COPY add-server-id.sh /usr/local/bin/
COPY entrypoint.sh /usr/local/bin/
COPY etc/mysql/mysql.conf.d/mysqld.cnf /etc/mysql/mysql.conf.d/
COPY etc/mysql/conf.d/mysql.cnf /etc/mysql/conf.d/
COPY prepare.sh /docker-entrypoint-initdb.d
COPY init-data.sh /usr/local/bin/
COPY sql /sql

ENTRYPOINT ./entrypoint.sh

CMD ["mysqld"]
```

</td>
</tr>
</table>

<br/>

**build 명령어 실행 :**

```Bash
❯ docker image build --platform linux/amd64 --progress=plain --no-cache -t gngsn/tododb:latest . 
```

- 레지스트리에 등록할 태그 명령어 실행

```Bash
❯ docker image tag gngsn/tododb:latest localhost:5000/gngsn/tododb:latest
```

이전에 실행하던 `docker-compose.yml` 을 기반으로 `docker-compose up -d` 명령어 실행
그럼 registry 서버가 올라가고, 해당 registry 서버에 tododb 등록


```Bash
❯ docker image push localhost:5000/gngsn/tododb:latest
The push refers to repository [localhost:5000/gngsn/tododb]
1304545fd7b6: Preparing 
cd4663989e4f: Preparing
...
latest: digest: sha256:0106...621d size: 4493
```

<br/>

### 스웜에서 마스터 및 슬레이브 실행

스웜에서 MySQL Master & Slave 역할을 할 2개의 서비스 생성

- Master Service - Replica: 1
- Slave Service - Replica: 2

`/stack` directory에 todo-mysql.yml 파일을 만들고 해당 파일에 2개의 서비스를 각각 정의
앞에서 만든 overlay 네트워크 todoapp의 external 속성을 true로 정의해 각 서비스가 이 네트워크에 속하게 해야 함


```Bash
❯ docker container exec -it manager ls -al /stack
❯ docker container exec -it manager vi /stack/todo-mysql.yml
... 위 todo-mysql.yml 작성
❯ docker container exec -it manager docker stack deploy -c /stack/todo-mysql.yml todo_mysql
```

```yaml
version: "3"

services: 
  master:
    image: registry:5000/gngsn/tododb:latest
    deploy:
      replicas: 1
      placement:
        constraints: [node.role != manager]
    environment:
      MYSQL_ROOT_PASSWORD: gngsn
      MYSQL_DATABASE: tododb
      MYSQL_USER: gngsn
      MYSQL_PASSWORD: gngsn
      MYSQL_MASTER: "true"
    networks:
      - todoapp

  slave:
    image: registry:5000/gngsn/tododb:latest
    deploy:
      replicas: 2
      placement:
        constraints: [node.role != manager]
      depends_on:
        - master
      environment:
        MYSQL_MASTER_HOST: gngsn
        MYSQL_ROOT_PASSWORD: gngsn
        MYSQL_DATABASE: tododb
        MYSQL_USER: gngsn
        MYSQL_PASSWORD: gngsn
        MYSQL_REPL_USER: repl
        MYSQL_REPL_PASSWORD: gngsn
    networks:
      - todoapp

networks:
  todoapp:
    external: true
```

<table>
<tr>
<th>Error: <code>services.slave.deploy Additional property depends_on is not allowed</code></th>
</tr>
<tr><td>

오타나 Indentation 잘못된 경우 발생하는 경우 ...

`depends_on` 은 `slave.deploy.depends_on` 이 아니라 `slave.depends_on` 으로 설정

동일하게, `depends_on` 은 `slave.deploy.environment` 이 아니라 `slave.environment` 으로 설정

---

<code>{{services.slave.deploy}} Additional property depends_on is not allowed</code>

위와 같은 형식의 오류가 발생했다면, `{{ here }}` 에 위치한 property 에 오타나 포함 관계를 실수한 경우와 같이 오류가 있다는 의미

</td></tr><tr><td>

**TOBE:** 

```yaml
services:
  ...
  
  slave:
    image: registry:5000/gngsn/tododb:latest
    deploy:
      replicas: 2
      placement:
        constraints: [node.role != manager]
    depends_on:
      - master
    environment:
      MYSQL_MASTER_HOST: gngsn
      MYSQL_ROOT_PASSWORD: gngsn
      MYSQL_DATABASE: tododb
      MYSQL_USER: gngsn
      MYSQL_PASSWORD: gngsn
      MYSQL_REPL_USER: repl
      MYSQL_REPL_PASSWORD: gngsn
    networks:
      - todoapp
```

</td></tr>
</table>

```Bash
❯ docker container exec -it manager docker stack deploy -c /stack/todo-mysql.yml todo_mysql
Creating service todo_mysql_slave
Creating service todo_mysql_master
```

<br/>

#### 스웜으로 배포하기

todo-mysql.yml 에 정의된 서비스를 todo_mysql Stack으로 manager container에 배포

스택을 사용해서 여러 서비스를 배포하면 서비스명 앞에 스택명이 붙어서 Master는 todo_mysql_master, Slave는 todo_mysql_slave라는 이름이 됨

<br/>

```Bash
❯ docker container exec -it manager docker service ls
ID             NAME                MODE         REPLICAS   IMAGE                                      PORTS
k0yii1idlt71   todo_mysql_master   replicated   1/1        registry:5000/gngsn/tododb-master:latest   
un1yx2mdtcq6   todo_mysql_slave    replicated   2/2        registry:5000/gngsn/tododb-slave:latest   
```

<br/><img src="./image/image03.png" width="60%" /><br/>

<br/>

### MySQL 컨테이너 확인 및 초기 데이터 투입

초기 데이터를 넣기 전, 먼저 마스터 컨테이너가 Swarm 노드 중 어느 것에 배치됐는지 확 인해야 하는데,
다음과 같이 `docker service ps` 명령을 사용

```Bash
❯ docker container exec -it manager docker service ps todo_mysql_master --no-trunc --filter "desired-state=running"
ID                          NAME                  IMAGE                                                                                                              NODE           DESIRED STATE   CURRENT STATE           ERROR     PORTS
mojbtc46odudt4ho74v7dtm8a   todo_mysql_master.1   registry:5000/gngsn/tododb-master:latest@sha256:614c61b14ef19fb43d4c36bb0916171e2a352e6b2557bc1e15ee26af52c6e9c3   d5d52eeba974   Running         Running 2 minutes ago
```

노드의 ID와 태스크의 ID만 알면 다음과 같이 docker container exec 명령을 중첩 실행해 원하는 컨테이너에 데이터 삽입 가능


<table>
<tr>
<th>Master DB Data Init</th>
<td>

```Bash
❯ docker container exec -it manager docker container exec -it mojbtc46odudt4ho74v7dtm8a
"docker container exec" requires at least 2 arguments.
See 'docker container exec --help'.

Usage:  docker container exec [OPTIONS] CONTAINER COMMAND [ARG...]

Execute a command in a running container
/ # 
```

</td><td>

```Bash
❯ docker container exec -it manager docker service ps todo_mysql_master --no-trunc --filter "desired-state=running" --format "docker container exec -it {{.Node}} docker container exec -it {{.Name}}.{{.ID}} bash"
docker container exec -it d5d52eeba974 docker container exec -it todo_mysql_master.1.mojbtc46odudt4ho74v7dtm8a bash
```

```Bash
❯ docker container exec -it d5d52eeba974 docker container exec -it todo_mysql_master.1.mojbtc46odudt4ho74v7dtm8a init-data.sh
```

</td><td>

```Bash
❯ docker container exec -it d5d52eeba974 docker container exec -it todo_mysql_master.1.mojbtc46odudt4ho74v7dtm8a mysql -u gngsn -pgngsn tododb
mysql: [Warning] Using a password on the command line interface can be insecure.
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 10
Server version: 8.0.33 MySQL Community Server - GPL

Copyright (c) 2000, 2023, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> SELECT * FROM todo LIMIT 1\G
*************************** 1. row ***************************
     id: 1
  title: MySQL Docker 이미지 생성
content: MySQL Master, Slave를 환경 변수로 설정할 수 있는 MySQL 이미지 생성
 status: DONE
created: 2023-10-31 15:52:57
updated: 2023-10-31 15:52:57
1 row in set (0.00 sec)
```

</td></tr>
</table>


<table>
<tr>
<th>Slave DB Data Init</th>
<td>

```Bash
❯ docker container exec -it manager docker service ps todo_mysql_slave --no-trunc --filter "desired-state=running" --format "docker container exec -it {{.Node}} docker container exec -it {{.Name}}.{{.ID}} bash"
docker container exec -it 6c30c78dc985 docker container exec -it todo_mysql_slave.1.2mqwnco92qqwgm2b9x1iqzamk bash
docker container exec -it 3320c84927ee docker container exec -it todo_mysql_slave.2.t4ajy6v60fwo04s8pbcrmp5yc bash
```

```Bash
❯ docker container exec -it 6c30c78dc985 docker container exec -it todo_mysql_slave.1.2mqwnco92qqwgm2b9x1iqzamk init-data.sh
```

</td><td>

```Bash
❯ docker container exec -it 6c30c78dc985 docker container exec -it todo_mysql_slave.1.2mqwnco92qqwgm2b9x1iqzamk mysql -u gngsn -pgngsn tododb
mysql: [Warning] Using a password on the command line interface can be insecure.
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 10
Server version: 8.0.33 MySQL Community Server - GPL

Copyright (c) 2000, 2023, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> SELECT * FROM todo LIMIT 1\G
*************************** 1. row ***************************
     id: 1
  title: MySQL Docker 이미지 생성
content: MySQL Master, Slave를 환경 변수로 설정할 수 있는 MySQL 이미지 생성
 status: DONE
created: 2023-10-31 16:03:23
updated: 2023-10-31 16:03:23
1 row in set (0.00 sec)
```

</td></tr>
</table>












