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
> docker container exec -it manager \
   docker network create --drive=overlay --attachable todoapp
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
















