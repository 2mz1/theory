# Chapter4 스웜을 이용한 실전 애플리케이션 개발

생성일: 2023년 11월 1일 오후 1:17
태그: 4장

---

# 4-1 웹 애플리케이션 구축

```bash
# TODO network 구축
$ docker container exec -it manager \                                                                                                                                                                                            4584  13:49:19  
> docker network create --driver=overlay --attachable todoapp
```

## TODO 앱 구조

- 데이터 스토어 mysql → master / slave
- Mysql 과 데이터를 주고받을 API
- nginx 를 이용한 웹 어플리케이션과 API 사이에서 리버스 프록시 역할을 하도록 설정
- API를 이용해 서버 사이드 렌더링을 수행할 웹 어플리케이션 구현
- 프론트엔드 쪽에 리버스 프록시(nginx) 배치

# 4-2 데이터 베이스 컨테이너 구성

- mysql container version mysql:5.7
- 마스터 - 슬레이브 컨테이너 두 역할을 모두 수행할 수 있는 하나의 이미지로 생성
- MYSQL_MASTER 환경 변수로 마스터가 될지 슬레이브가 될지 결정
- replicas 값을 조절해 슬레이브를 늘릴 수 있께 함

## mysq 구성

```yaml
version: "3"

services:
  master:
    image: registry:5000/ch04/tododb:latest
    deploy:
      replicas: 1
      placement:
        constraints: [node.role != manager]
    environment:
      MYSQL_ROOT_PASSWORD: gihyo
      MYSQL_DATABASE: tododb
      MYSQL_USER: gihyo
      MYSQL_PASSWORD: gihyo
      MYSQL_MASTER: "true"
    networks:
      - todoapp
  
  slave:
    image: registry:5000/ch04/tododb:latest
    deploy:
      replicas: 2
      placement:
        constraints: [node.role != manager]
    depends_on:
      - master
    environment:
      MYSQL_MASTER_HOST: master
      MYSQL_ROOT_PASSWORD: gihyo
      MYSQL_DATABASE: tododb
      MYSQL_USER: gihyo
      MYSQL_PASSWORD: gihyo
      MYSQL_REPL_USER: repl
      MYSQL_REPL_PASSWORD: gihyo
    networks:
      - todoapp

networks:
  todoapp:
    external: true
```

**실행**

```bash
$ docker container exec -it manager docker stack deploy -c /stack/todo-mysql.yml todo_mysql

$ docker container exec -it manager docker service ls
ID                  NAME                MODE                REPLICAS            IMAGE                              PORTS
ml5mv9fgiz0u        todo_mysql_master   replicated          0/1                 registry:5000/ch04/tododb:latest
ancjyfk1uxnk        todo_mysql_slave    replicated          0/2                 registry:5000/ch04/tododb:latest
```

# 4-3 API 서비스 구축

**image push**

```bash
# git clone
$ git clone https://github.com/gihyodocker/todoapi

# api server docker build
$ docker image build -t ch04/todoapi:latest .

# push
$ docker image push localhost:5000/ch04/todoapi:latest
```

**stack.yml**

```yaml
version: "3"

services:
  api:
    image: registry:5000/ch04/todoapi:latest
    deploy:
      replicas: 2
    environment:
      TODO_BIND: ":8080"
      TODO_MASTER_URL: "gihyo:gihyo@tcp(todo_mysql_master:3306)/tododb?parseTime=true"
      TODO_SLAVE_URL: "gihyo:gihyo@tcp(todo_mysql_slave:3306)/tododb?parseTime=true"
    networks:
      - todoapp

networks:
  todoapp:
    external: true
```

# 4-4 nginx 서비스 구축

```bash
$ docker image build -t ch04/nginx:latest .
```

```yaml
version: "3"

services:
  nginx:
    image: registry:5000/ch04/nginx:latest
    deploy:
      replicas: 2
      placement:
        constraints: [node.reole != manager]
    depends_on:
      - api
    environment:
      WORKER_PROCESSES: 2
      WORKER_CONNECTIONS: 1024
      KEEPALIVE_TIMEOUT: 65
      GZIP: "on"
      BACKEND_HOST: todo_app_api:8080
      BACKEND_MAX_FAILS: 3
      BACKEND_FAIL_TIMEOUT: 10s
      SERVER_PORT: 80
      SERVER_NAME: todo_app_nginx
      LOG_STDOUT: "true"
    networks:
      - todoapp

  api:
    image: registry:5000/ch04/todoapi:latest
    deploy:
      replicas: 2
    environment:
      TODO_BIND: ":8080"
      TODO_MASTER_URL: "gihyo:gihyo@tcp(todo_mysql_master:3306)/tododb?parseTime=true"
      TODO_SLAVE_URL: "gihyo:gihyo@tcp(todo_mysql_slave:3306)/tododb?parseTime=true"
    networks:
      - todoapp

networks:
  todoapp:
    external: true
```

---

# 4-5 web 구축

```bash
# front clone 
$ git clone https://github.com/gihyodocker/todoweb

# 의존 모듈 다운
$ npm install
$ npm run build
$ npm run start

$ docker image build -t ch04/todoweb:latest .

$ docker image build -f Dockerfile-nuxt -t ch04/nginx-nuxt:latest .
$ docker image tag ch04/nginx-nuxt:latest localhost:5000/ch04/nginx-nuxt:latest
$ docker image push localhost:5000/ch04/nginx-nuxt:latest
```

```yaml
version: "3"
services:
  nginx:
    image: registry:5000/ch04/nginx-nuxt:latest
    deploy:
      replicas: 2
    placement:
      constraints: [node.role != manager]
    depends_on:
      - web
    environment:
      SERVICE_PORTS: 80
      WORKER_PROCESSES: 2
      WORKER_CONNECTIONS: 1024
      KEEPALIVE_TIMEOUT: 65
      GZIP: "on"
      BACKEND_HOST: todo_frontend_web:3000
      BACKEND_MAX_FAILS: 3
      BACKEND_FAIL_TIMEOUT: 10s
      SERVER_PORT: 80
      SERVER_NAME: localhost
      LOG_STDOUT: "true"
    networks:
      - todoapp
    volumes:
      - assets:/var/www/_nuxt
  web:
    image: registry:5000/ch04/todoweb:latest
    deploy:
      replicas: 2
      placement:
        constraints: [node.role != manager]
      environment:
        TODO_API_URL: http://todo_app_nginx
      networks:
        - todoapp
      volumne:
        - assets:/todoweb/.nuxt/dis

networks:
  todoapp:
    external: true

volumes:
  assets:
    driver: local:
```

**ingress**

```yaml
version: "3"

services:
  haproxy:
    image: dockercloud/haproxy
    networks:
      - todoapp
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    deploy:
      mode: global
      placement:
        constraints:
          - node.role == manager
    ports:
      - 80:80
      - 1936:1936

networks:
  todoapp:
    external: true
```