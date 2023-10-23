# CHAPTER 3. 컨테이너 실정 구축 및 배포

**목표**
- 시스템에서 단일 컨테이너의 비중, 이식성을 고려해 도커 친화적인 애플리케이션을 개발하는 방법
- 퍼시스턴스(persistence) 데이터를 다루는 방법
- 도커 스웜이나 스택(Stack)을 이용한 컨테이너 배포 전략


<br/>

## 01. 애플리케이션과 시스템 내 단일 컨테이너의 적정 비중


실제운영에서는 애플리케이션을 컨테이너 안에 어떻게 배치하는지가 매우 중요
- 컨테이너 하나가 맡을 수 있는 적정 수준의 책임은 어느정도일까?
- 세세하게 역할을 나누다가 시스템 전체의 복잡도가 올라가지는 않을까?

👉🏻 애플리케이션 내 단일 컨테이너의 적정 비중은 이런 다양한 측면을 고려할 필요가 있음

<br/>

### 컨테이너 1개 = 프로세스 1개?

**✔️ 정기적으로 작업을 실행하는 애플리케이션**

- 스케줄러 기능이 있는 애플리케이션
  - 컨테이너 1개 = 프로세스 1개 가능
- 스케줄러 기능이 없는 애플리케이션
  - 대부분 cron을 사용
  - cron은 하나의 프로세스


```dockerfile
RUM ubuntu: 16.04         # ①

RUN apt update            # ②
RUN apt install -y cron   # ②

COPY task.sh /usr/local/bin/    # ③
COPY cron-example /etc/cron.d/  # ③
RUN chmod 0644 /etc/cron.d/cron-example   # ④

CMD ["cron" "-f"]   # ⑤
```

- ① `ubuntu: 16.04` 사용
- ② 패키지 관리자 apt 사용해서 cron 설치
- ③ task.sh 과 cron-example을 컨테이너로 복사
- ④ 실행을 위해 0644 로 권한 조정
- ⑤ cron 을 foreground 실행 
  - cron 은 background 에서 실행하는 프로세스라서 CMD 명령 후 바로 종료 되기 때문에 실행 상태로 남겨둠

```shell
❯ docker image build -t gngsn/cronjob:latest .
[+] Building 18.7s (12/12) FINISHED                                                                docker:desktop-linux
 => [internal] load .dockerignore                                                                                  0.0s
 => => transferring context: 2B                                                                                    0.0s
 => [internal] load build definition from Dockerfile                                                               0.0s
 => => transferring dockerfile: 253B                                                                               0.0s
 => [internal] load metadata for docker.io/library/ubuntu:16.04                                                    3.3s
 => [auth] library/ubuntu:pull token for registry-1.docker.io                                                      0.0s
 => CACHED [1/6] FROM docker.io/library/ubuntu:16.04@sha256:1f1a2d56de1d604801a9671f301190704c25d604a416f59e03c04  0.0s
 => [internal] load build context                                                                                  0.0s
 => => transferring context: 244B                                                                                  0.0s
 => [2/6] RUN apt update                                                                                          11.8s
 => [3/6] RUN apt install -y cron                                                                                  3.3s
 => [4/6] COPY task.sh /usr/local/bin/                                                                             0.0s 
 => [5/6] COPY cronfile /etc/cron.d/                                                                           0.0s 
 => [6/6] RUN chmod 0644 /etc/cron.d/cronfile                                                                  0.2s 
 => exporting to image                                                                                             0.1s 
 => => exporting layers                                                                                            0.1s 
 => => writing image sha256:7292182058857c96677b99b4934c9d2bb0ad59ca2461d17d7b3a9c9773d6e93a                       0.0s 
 => => naming to docker.io/gngsn/cronjob:latest
```

- docker 실행

```shell
❯ docker container run -d --rm --name cronjob gngsn/cronjob:latest
0a081727212cee7fcb77779c0e7d68a511afb7152f073c93716fa8b2903f6f7b
```

- 실행중인 컨테이너 로그 확인

실행중인 컨테이너의 `/var/log/cron.log` 파일의 내용을 보면 `task.sh` 에서 출력한 문자열이 1분마다 추가 되고 있음을 알 수 있음

```shell
❯ docker container exec -it cronjob tail -f /var/log/cron.log
```

- 컨테이너 하나에서 cron 작업까지 총 2개 프로세스를 모두 실행하는 형태
- 억지로 별도의 컨테이너로 분리하는 것보다 훨씬 깔끔

<br/>

### 컨테이너 1개에 하나의 관심사


> Each container should have only one concern.
> 
> 컨테이너는 하나의 관심사에만 집중해야 한다.
> 
> - 'Best Practices for writing Dockerfiles', 도커 공식문서

애플리케이션과 컨테이너가 전체 시스템에서 차지해야 하는 적정 비중을 고려하면, "각 컨테이너가 맡은 역할을 적절히 나누고, 그 역할에 따라 배치한 컨테이너를 복제해도 전체 구조에서 부작용이 일어나지 않는가?" 를 따져가며 시스템을 설계해야 함


<br/>

## 02. 컨테이너 이식성

- 도커를 사용하면 애플리케이션과 인프라를 컨테이너라는 단위로 분리할 수 있으며, 도커가 설치된 환경이라면 어떤 호스트 운영체제와 플랫폼, 온프레미스 및 클라우드 환경에서도 그대로 동작
- 도커의 이식성은 완벽하지 않아서 몇 가지 예외가 존재

<br/>

### 커널 및 아키텍처의 차이


