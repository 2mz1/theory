# Chapter2 도커 컨테이너 배포

생성일: 2023년 10월 18일 오후 4:15
태그: 2장

---

# 2-1 컨테이너로 애플리케이션 실행하기

**개념**

- 도커 이미지
    - 도커 컨테이너를 구성하는 파일 시스템과 실행할 애플리케이션 설정을 하나로 합친 것 컨테이너를 생성하는 템플릿 역할을 함
- 도커 컨테이너
    - 도커 이미지를 기반으로 생성되며, 파일 시스템과 애플리케이션이 구체화돼 실행되는 상태

## 도커 이미지와 도커 컨테이너

**간단한 이미지 학습**

```bash
# image pull
$ docker image pull gihyodocker/echo:latest

# docker run
$ docker container run -t -p 9000:8080 gihyodocker/echo:latest

# docker run (mac os)
$ docker container run -t -p 9000:8080 --platform linux/amd64 gihyodocker/echo:latest

# curl
$ curl http://localhost:9090/

```

## 간단한 애플리케이션과 도커 이미지 만들기

```go
package main

import (
	"fmt"
	"log"
	"net/http"
)

func main() {
	http.HandleFunc("/", func(w http.ResponseWriter, r * http.Request) {
		log.Println("received request")
		fmt.Fprintf(w, "Hello Docker!!")
	})

	log.Println("start server")
	server: = & http.Server {
		Addr: ":8080"
	}

	if err: = server.ListenAndServe(); err != nil {
		log.Println(err)
	}
}
```

- 모든 요청에 대해 `Heelo Docker!!` 라는 응답을 보냄
- 포트 8080로 요청을 받는 서버 애플리케이션으로 동작
- 클라이언트로부터 요청을 바등면 received request라는 메시지를 표준으로 출력

```docker
FROM golang:1.9

RUN mkdir /echo
COPY main.go /echo

CMD ["go", "run", "/echo/main.go"]
```

- 도커 전용 도메인언어로 이미지 구성
- FROM
    - 도커 이미지의 바탕이될 이미지를 지정
    - Dockerfile로 이미지를 빌드할 때 먼저 FROM에 지정된 이미지를 받음
- RUN
    - 도커를 실행할 때 컨테이너 안에서 실행할 명령을 정의함
- COPY
    - 도커가 동작중인 호스트 머신의 파일이나 디렉터리를 도커 컨테이너 안으로 복사하는 명령어
- CMD
    - 도커 컨테이너를 실행할 때 컨테이너 안에서 실행할 프로세스를 지정함
    - 애플리케이션 자체를 실행하는 명령어로 생각하면됨
    

## 도커 이미지 빌드하기

```bash
# build
$ docker image build -t 이미지명[:태그명] Dockerfile의 경로

$ docker image build -t example/echo:latest .

$ docker image ls
REPOSITORY         TAG       IMAGE ID       CREATED          SIZE
example/echo       latest    37a247b1d9e4   23 seconds ago   662MB
```

## 도커 컨테이너 실행

```bash
$ docker container run example/echo:latest

# 백그라운드 실행
$ docker container run -d example/echo:latest
```

## 포트 포워딩

```bash
# host 9000 , container 8080
$ docker container run -d -p 9000:8080 example/echo:latest
```

---

# 2.2 도커 이미지 다루기

## 이미지 빌드

```bash
# 기본
$ docker image build -t 이미지명:[:태그명] Dockerfile의 경로

# -f 옵션 Dockerfile이 아닌 다른 파일명을 사용할 경우
$ docker image build -f Dockerfile-test -t example/echo:latest .

# --pull 옵션 -> FROM 절 이미지를 매번 받아오려면(디폴트는 매번 안받음)
$ docker image build --pull=true -t example/echo:latest

```

## 이미지 검색

```bash
$ docker search [options] 검색키워드

$ docker search --limit 5 mysql
```

## 이미지 내려 받기

```bash
$ docker image pull [options] 리포지토리명[:태그명]

$ docker image pull jenkins:latest
```

---

# 2-3 도커 컨테이너 다루기

## 도커 컨테이너 생애주기

- 실행 중
    - docker container run 명령으로 실행 될 때
- 정지
    - 사용자가 정지하거나 애플리케이션이 정상/오류 여부를 막론하고 종료된 경우
- 파기
    - 정지 상태의 컨테이너를 명시적으로 파기하지 않는 이상 디스크에 그대로 남아있음
    - 해당 이미지를 파기하는 경우를 파기 상태라 부름

---

# 2-4 운영과 관리를 위한 명령어

- docker container prune
    - 실행되지 않는 모든 컨테이너를 삭제함
- docker image prune
    - 태그가 붙지 않은 모든 이미지 삭제
- docker system prune
    - 사용하지 ㅇ낳는 도커 이미지 및 컨테이너 볼륨 네트워크 등 모든 리소스를 삭제
- docker container stats
    - 사용 현황 확인 → 유닉스의 top
    

---

# 2-5 도커 컴포즈로 여러 컨테이너 실행하기

```yaml
version : "3"
services:
	echo:
		image: example/echo:latest
		ports:
			- 9000:8080
```

---

# 2-6 컴포즈로 여러 컨테이너 실행하기

```yaml
version: "3"
services:
	master:
		container_name: master
		image: jenkinsci/jenkins:2.142-slim
		ports:
			- 9090:9090
		volumes:
			- ./jenkins_home:/var/jenkins_home
```

**slave jenkins**

```yaml
version: "3"
services:
	master:
		container_name: master
		image: jenkinsci/jenkins:2.142-slim
		ports:
			- 9090:9090
		volumes:
			- ./jenkins_home:/var/jenkins_home
		links:
			- slave01

slave01:
	container_name: slave01
	image: jenkinsci/ssh-slave
	environment:
		- JENKINS_SLAVE_SSH-PUBKEY=ssh.....
```