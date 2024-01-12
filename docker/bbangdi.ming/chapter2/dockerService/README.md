### 요약
 - 도커 스웜을 사용하는 경우 애플리케이션을 구성하는 일부 컨테이너를 제어하기 위한 단위를 서비스라고 함



```bash
docker container exec -it manager \
docker service create --replicas 1 --publish 8000:8000 --name helloserver registry:5000/helloserver:1.1
```
