## CHAPTER 2. 도커 컨테이너 배포

### Docker Image
: Docker Container 를 구성하는 파일 시스템과 실행할 애플리케이션 설정의 템플릿

$ docker image --help
: 이미지를 다루는 명령어 도움말 확인

혹은 docker image COMMAND --help 와 같이 하위 명령어의 도움말을 확인할 수도 있습니다.


$ docker image build

: Dockerfile 명세에 따라 도커 이미지 생성

```bash
$ docker image build -t IMAGE_NAME[:TAG] DOCKERFILE_PATH
# example. docker build -t vieux/apache:2.0 .
```

DOCKERFILE_PATH 는 이미지를 생성할 스펙을 담고 있는 Dockerfile의 위치를 입력하면 됩니다.

바로 아래의 예시를 보면, vieux/apache 라는 image 명의 2.0 태그를 달아 . 에 위치해 있는 Dockerfile로 새로운 이미지를 빌드하는 것입니다.



Option
-t : 태그 명을 붙임. 실제 상황에서 거의 필수적으로 사용되는 옵션.

--pull : 캐시 값을 사용하지 않고 베이스 이미지를 강제로 새로 받아오는 옵션.

Dockerfile에 작성한 FROM instruction에 지정한 이미지는,

호스트 운영체제에 캐시되어 해당 이미지를 사용할 때 캐시 값을 사용합니다.

이를 원치 않고, 베이스 이미지가 새롭게 다운 받아져와서 그 위에 본인의 이미지를 구축하고 싶을 때 사용합니다.

가령, latest 태그를 달고 있을 때 유용하게 사용할 수 있습니다.







$ docker search
: 저장소에 있는 이미지 검색



$ docker search [OPTIONS] TERM


공식 레포지토리의의 네임스페이스는 library 입니다.



Docker Hub 에는 이미지들의 기반이 되는 운영체제 (CentOS, Ubuntu, etc) 나,

언어의 런타임이나 미들웨어 이미지 등 이미 완전한 제품으로 관리되어 지는 이미지들이 많습니다.

덕분에 다른 사람들이 만든 이미지를 사용할 수 있는데, 이 때 어떤 이미지가 존재하는지 확인할 수 있는 명령어입니다.



$ docker search --limit 5 mysql
NAME            DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
mysql           MySQL is a widely used, open-source relation…   14517     [OK]       
mariadb         MariaDB Server is a high performing open sou…   5543      [OK]       
percona         Percona Server is a fork of the MySQL relati…   621       [OK]       
phpmyadmin      phpMyAdmin - A web interface for MySQL and M…   876       [OK]       
bitnami/mysql   Bitnami MySQL Docker Image                      98                   [OK]


Option
--limit : 검색 건수 제한









$ docker image pull
: 저장소에 있는 이미지 다운로드

Download an image from a registry



$ docker image pull [OPTIONS] NAME[:TAG]




태그를 생략하면 기본값 (보통 latest)으로 지정된 태그로 적용됩니다.



대부분 이미지를 생성할 때, Docker Hub 에 이미 사전 정의되어 등록된 이미지를 불러와서 그 위에서 만들텐데요.
Docker Hub는 사전에 구축된 많은 이미지pre-built image들이 등록되어 있어,

직접 정의하고 구성할 필요 없이 바로 pull 받아 사용할 수 있습니다.
특정 image 또는 repository 를 다운로드하려면 docker pull 명령어를 사용합니다.







$ docker image ls
: 다운받은 도커 이미지 목록 확인



$ docker image ls [OPTIONS] [REPOSITORY[:TAG]]


현재 호스트 운영 체제 (docker demon이 실행중인 호스트 환경)에 저장된 도커 이미지의 목록을 보여줍니다.



$ docker image ls
REPOSITORY       TAG       IMAGE ID       CREATED        SIZE
redis            latest    6f3eb785c21d   3 weeks ago    157MB
apache/airflow   2.7.1     f08724944476   5 weeks ago    1.37GB
postgres         latest    ee56d70bcdf1   2 months ago   433MB






$ docker image tag
: 다운받은 도커 이미지 목록 확인



도커 이미지에 붙은 태그는 이미지의 특정 버전을 구별하기 위해 사용합니다.



$ docker image tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]


기본적으로, 한 이미지에 같은 태그가 여러 개 할당될 수 없습니다.

즉, 이미지 고유의 IMAGE ID 이외의, 한 이미지 당 태그는 식별될 수 있는 단위인 것입니다.

동일한 이미지를 조금씩 수정하고 빌드하다 보면 아래와 같이 <none> 값들을 확인할 수 있게 됩니다.



$ docker image ls -a
REPOSITORY       TAG       IMAGE ID       CREATED         SIZE
example/image    latest    ef864d623b3b   11 months ago   119MB
<none>           <none>    c0f8d921704c   11 months ago   119MB
<none>           <none>    7caafbde4e75   11 months ago   119MB


example/image라는 동일한 이미지를 수정했는데, 매번 latest가 새로운 이미지에 할당되면서 이전의 태그는 사라지게 됩니다.

태그는 이미지를 쉽게 참조할 수 있는 별명과도 같습니다.

버전 관리를 위해 태그를 통해 latest 의 버전을 남겨두고, latest 태그를 붙일 새로운 이미지를 붙이는 것이 좋습니다.





$ docker tag example/image example/image:1.0


이후 이미지를 확인하면 아래와 같이 태그는 다르지만 해시 값이 같은, 즉 동일한 이미지임을 확인할 수 있습니다.



$ docker image ls -a
REPOSITORY       TAG       IMAGE ID       CREATED         SIZ
example/image    1.0       ef864d623b3b   11 months ago   119MB
example/image    latest    ef864d623b3b   11 months ago   119MB


이미지 명이 아닌 아래와 같이 IMAGE ID 로도 수정할 수 있습니다.



# Tag an image referenced by ID
$ docker tag 0e5574283393 fedora/httpd:version1.0




$ docker image push
: 이미지를 저장소에 업로드



$ docker push [OPTIONS] NAME[:TAG]


생성한 이미지를 Docker Hub 와 같은 public registry에 업로드하기 위한 명령어입니다.

아래와 같은 예시를 확인할 수 있습니다.

이 때, 네임스페이스는 개인의 계정 혹은 특정 소속을 의미하기 때문에, tag 명을 변경해서 업로드해야 합니다.

가령, example은 필자의 계정/소속이 아니며, 필자의 아이디는 gngsn이기 때문에 아래와 같이 tag 명을 변경합니다.



$ docker image tag example/image gngsn/example:latest
$ docker image push gngsn/example:latest


이후, docker hub 에 push 명령어로 업로드할 수 있습니다.

공개 레포지토리에 Dockerfile을 업로드할 때에는 패스워드나 API 키 값처럼 민감한 정보가 포함되지 않게 주의하세요.









Docker Container


도커 컨테이너는  위에서 살펴본 Lifecycle 중 Running, Stopped, Deleted 의 3가지 상태를 가지며,

이를 컨테이너의 생애주기라고 합니다.




https://linuxhandbook.com/container-lifecycle-docker-commands/




✔️ 실행 중 :: RUNNING

docker container run 명령의 인자로 지정된 도커 이미지를 기반으로 커테이너가 생성되면,

이 이미지를 생성했던 Dockerfile에 포함된 CMD 및 ENTRYPOINT 인스트럭션에 정의된 애플리케이션이 실행됩니다.

이 때, 컨테이너의 상태가 실행 중이 됩니다.



✔️ 정지 상태  :: STOPPED

실행 중인 상태에 있는 컨테이너를 사용자가 명시적으로 정지하거나,

컨테이너에서 실행된 애플리케이션이 정상/오류 여부와 관계없이 종료된 경우,

컨테이너가 자동으로 정지 상태가 됩니다.



정지 상태에는 컨테이너가 가상 환경으로서 동작하진 않지만,

디스크에 컨테이너가 종료되던 시점의 상태가 저장되어 있습니다.

정지시킨 컨테이너를 다시 실행할 수 있습니다.



✔️ 파기 상태 :: DELETED

정지 상태인 컨테이너는 명시적으로 제거하지 않는 이상 디스크에 그대로 남아있습니다.

컨테이너를 빈번하게 생성하고 없애야 한다면 디스크를 차지하는 용량을 고려하여, 이를 완전히 삭제하는 것이 바람직합니다.







$ docker contianer run
: 컨테이너 생성 및 실행



도커 컨테이너를 실행 중 상태로 만들기 위해 사용합니다.

아래 인자 중 IMAGE는 IMAGE_NAME[:TAG] 혹은 IMAGE_ID 로 표현할 수 있습니다.



$ docker container run [OPTIONS] IMAGE [COMMAND] [ARG...]




가령, 아래와 같은 방식으로 컨테이너를 백그라운드에서 실행할 수 있습니다.



$ docker container run -d -p 9000:8080 gngns/example:latest


위 예시에서 -p 옵션을 통해 호스트 쪽 포트 9000을 컨테이너 쪽 포트 8080으로 포트 포워딩한 것을 확인할 수 있습니다.

서버 에플리케이션을 위해 도커를 사용하는 경우가 많기 때문에, -d 이나 -p 옵션을 사용할 일이 많이 있습니다.



option
-p: --port 의 축약형입니다.

위의 예시처럼 포트 포워딩을 원할 때 -p HOST_PORT:CONTAINER_PORT 와 같이 적용할 수 있습니다.



-d: --detach 의 축약형입니다.

container 실행을 background 에서 실행한 후 Terminal 제어권을 다시 돌려받습니다.



-it: docker container run 명령에 -it 인자를 전달하면 Dockerfile 에 정의했던 CMD 인스트럭션을 오버라이드할 수 있습니다.



-i옵션은 --interactive의 축약형입니다.

컨테이너를 실행할 때 컨테이너 쪽 표준 입력과의 연결을 그대로 유지하여,

컨테이너 쪽 셸에 들어가서 명령을 실행할 수 있다.



-t 옵션은 --tty 의 축약형입니다.

유사 터미널 기능을 활성화 하는 옵션인데,

-i 옵션을 사용하지 않으면 유사 터미널을 실행해도 입력할 수가 없으므로,

이를 같이 사용하기 위해 -it 의 형태로 자주 사용됩니다.



예를 들어 library/aipine:3.7의 CMD 인스트럭션은 /bin/sh 로, 셸을 실행하는데,

아래와 같이 다른 명령으로 오버라이드할 수 있습니다.



$ docker image pull alpine:3.7
# container 내 실행 중인 셸에 접속
§ docker container run -it alpine:3.7 uname -a


--name: container에 원하는 이름을 붙일 수 있습니다.

container 에 운영할 서비스 개발 중에는 같은 docker 명령을 반복 실행하는 등 container를 다룰 일이 많습니다.



기본적으로 container는 ID, NAME으로 식별할 수 있는데,

ID가 자동 생성되며, NAME은 container 가 실행되면 무작위의 단어로 이름 지어집니다.

하지만, docker container ls 명령으로 빈번하게 ID 나 이름을 확인하기가 번거롭습니다.

또, container ID와 자동 부여된 컨테이너 이름 모두 컨테이너가 실행되고 나야 알 수 있습니다.



이 문제를 해결할 수 있는 방법이 컨테이너에 이름을 붙이는 기능입니다.



$ docker container run --name CONTAINER_NAME IMAGE
# example. docker container run --name alpine alpine:latest


-v: --volume 의 축약형입니다.

Container 동작 시 데이터 볼륨을 지정할 수 있습니다.

데이터 볼륨은 호스트와 컨테이너 간에 디렉터리나 파일을 공유하기 위해 사용합니다.







$ docker contianer ls
: 실행 중이거나 종료된 컨테이너의 목록을 보여주는 명령



$ docker container ls [OPTIONS]


실제 명령을 실행해서 확인해보면 아래와 같은 필드를 확인할 수 있습니다.



$ docker container ls
CONTAINER ID   IMAGE          COMMAND                  CREATED       STATUS                 PORTS      NAMES
8b36b2e62616   postgres:13    "docker-entrypoint.s…"   2 weeks ago   Up 2 weeks (healthy)   5432/tcp   airflow-local-postgres-1
ca1d9ab8f694   redis:latest   "docker-entrypoint.s…"   2 weeks ago   Up 2 weeks (healthy)   6379/tcp   airflow-local-redis-1




CONTAINER ID	컨테이너 식별자
IMAGE	해당 컨테이너를 만드는 데 사용된 도커 이미지
COMMAND	컨테이너에서 실행되는 애플리케이션 프로세스
CREATED	컨테이너 생성 후 경과된 시간
STATUS	Up 이나 Exited 등 컨테이너의 실행 상태
PORTS	포트 포워딩: 호스트 포트와 컨테이너 포트의 연결 관계
NAMES	컨테이너의 이름




option
-q: 컨테이너의 ID 만을 추출합니다.

--filter: 컨테이너 목록을 필터링합니다.



$ docker container ls --filter "FILTER_NAME=VALUE"


가령, 원하는 이름과 컨테이너 명이 일치하는 컨테이너의 목록을 보고 싶으면 name 필터를 사용합니다.



$ docker container ls --filter "name=redis"


-a: 종료된 컨테이너를 포함한 컨테이너 목록을 확인할 수 있습니다.







$ docker contianer stop
: 실행 중인 컨테이너 정지



$ docker container stop [OPTIONS] CONTAINER [CONTAINER...]


가령 아래와 같이 사용할 수 있습니다.



$ docker container run -t -d --name echo example/echo:latest
$ docker container stop echo
echo


echo 라고 이름붙인 컨테이너를 정지시킵니다.









$ docker contianer restart
: 실행 중인 컨테이너 재시작



정지 상태의 컨테이너를 재시작할 수 있습니다.



$ docker container restart [OPTIONS] CONTAINER [CONTAINER...]


가령, 정지해둔 echo 컨테이너를 아래와 같이 재시작할 수 있습니다.



$ docker container restart echo




$ docker contianer rm
: 정지시킨 컨테이너를 완전히 제거



$ docker container rm [OPTIONS] CONTAINER [CONTAINER...]


단, 현재 실행 중인 컨테이너는 제거할 수 없습니다.

만약 강제로 제거하고 싶다면 아래에 명세한 --force 옵션을 사용합니다.



실 업무 중 컨테이너 실행 및 정지를 반복하다 보면 아래와 같이 정지된 컨테이너가 여러개 생깁니다.



$ docker container ls --filter "status=exited"
CONTAINER ID   IMAGE          COMMAND                  CREATED       STATUS                   PORTS      NAMES
8b36b2e62616   postgres:13    "docker-entrypoint.s…"   2 weeks ago   Exited (2) 2 weeks ago   5432/tcp   airflow-local-postgres-1
ca1d9ab8f694   redis:latest   "docker-entrypoint.s…"   2 weeks ago   Exited (2) 2 weeks ago   6379/tcp   airflow-local-redis-1


위의 명령어를 통해 종료된 컨테이너를 확인해볼 수 있습니다.

실제로 많이 사용될 수 있는 알아두면 유용한 옵션입니다.



이 때 종료된 docker 를 제거하기 위해 아래와 같은 명령어를 사용할 수 있습니다.



$ docker container rm 8b36b2e62616




Option
Option	Short	Description
--force	-f	Force the removal of a running container (uses SIGKILL)
--volumes	-v	Remove anonymous volumes associated with the container




$ docker contianer logs
: 표준 출력 연결하기



현재 실행 중인 특정 도커 컨테이너의 표준 풀력 내용을 확인할 수 있습니다.

단, 컨테이너의 출력 내용 중 표준 출력으로 출력된 내용만 확인할 수 있기 때문에,

파일 등에 출력된 로그는 볼 수 없습니다.



$ docker container logs [OPTIONS] CONTAINER


가령, 컨테이너 리스트에서 postgres 명을 찾아 로그를 실행시키고 싶다면 아래와 같은 명령어를 입력할 수 있습니다.



$ docker container logs -f $(docker container ls --filter "name=postgres" -q)
The files belonging to this database system will be owned by user "postgres".
This user must also own the server process.

The database cluster will be initialized with locale "en_US.utf8".
The default database encoding has accordingly been set to "UTF8".
The default text search configuration will be set to "english".






$ docker contianer exec
: 실행 중인 컨테이너에서 명령 실행



$ docker exec [OPTIONS] CONTAINER COMMAND [ARG...]


예를 들어, 실행 중인 Docker Container의 bash 명령어를 실행하고 싶다면 아래와 같이 접근할 수 있습니다.



$ docker exec -it 8b36b2e62616 /bin/bash
root@8b36b2e62616:/#


Options
Option	Short	Description
--detach	-d	Detached mode: run command in the background
--env list	-e	Set environment variables
--interactive	-i	Keep STDIN open even if not attached
--privileged	 	Give extended privileges to the command
--tty	-t	Allocate a pseudo-TTY
--user	-u	Username or UID (format: <name
--workdir	-w	Working directory inside the container




$ docker contianer cp
: 파일 복사하기



$ docker container cp [OPTIONS] CONTAINER:SRC_PATH DEST_PATH|-
# or
$ docker cp [OPTIONS] SRC_PATH|- CONTAINER:DEST_PATH


docker comainer cp 명령은 컨테이너끼리 혹은 컨테이너와 호스트 간에 파일을 복사하기 위한 명령입니다.

Dockerfile에 포함된 COPY 인스트럭션은 이미지를 빌드할 때 호스트에서 복사해 올 파일을 정의하기 위한 것이고,

docker container cp 명령은 실행 중인 컨테이너와 파일을 주고받기 위한 명령입니다.



docker container cp 명령은 디버깅 중 컨테이너 안에서 생성된 파일을 호스트로 옮겨 확인할 목적으로 사용하는 경우가 대부분입니다.

또한, 아직 삭제되지 않은 정지 상태의 컨테이너에 대해서도 실행할 수 있습니다.









$ docker contianer prune
: 컨테이너 및 이미지 제거



docker container prune 명령은 실행 중이 아닌 모든 컨테이너를 삭제하는 명령입니다.



$ docker container prune [OPTIONS]


docker container prune 명령은 도커 운영이나 관리 시 사용할 수 있는 명령어입니다.

도커를 오랜 기간 사용하다 보면 디스크에 저장된 컨테이너와 이미지가 점점 늘어나곤 합니다.

이런 경우에 prune 명령을 사용해 필요 없는 이미지나 컨테이너를 일괄 삭제할 수 있습니다.



정지시킨 대부분의 컨테이너는 그리 쓸모가 없기 때문에,

정기적으로 이들을 삭제하는 것이 좋습니다.



$ docker container prune
WARNING! This will remove all stopped containers.
Are you sure you want to continue? [y/N] y
Deleted Containers:
74a18a7eda29cbd62876ec2ad2cca73069a1f3647df67ba07cb47e840fb221b1
3174c544e7d7a95113139e4a7f04eef42739414806d092aa7e99dd959a450e9a
9afc781f43879b0566319090c06d5b54e0ce69cb8edcf2d1b5adf4fc78650823
5435e63da04d83db9cfc170623e63ba11e7e74c58e3061f0ba092f1797399a98
43221ba1280d9ebdae7fd541b637e6af8bb75188213058b7d0e091bd6048d87e

Total reclaimed space: 87.75MB








$ docker contianer stats
: 사용 현황 확인하기



docker container stats 명령은 시스템 리소스 사용 현황을 컨테이너 단위로 확인할 수 있습니다.

유닉스 계열 운영 체제의
⊤
명령과 같은 역할을 한다고 볼 수 있습니다.



$ docker container stats
CONTAINER ID   NAME                       CPU %     MEM USAGE / LIMIT     MEM %     NET I/O       BLOCK I/O        PIDS
8b36b2e62616   airflow-local-postgres-1   0.35%     40.99MiB / 7.667GiB   0.52%     5.8kB / 0B    37.6MB / 305MB   7
ca1d9ab8f694   airflow-local-redis-1      0.29%     8.359MiB / 7.667GiB   0.11%     5.87kB / 0B   14.8MB / 0B      6
 

 