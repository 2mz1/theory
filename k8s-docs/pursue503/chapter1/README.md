# Chapter1 OverView(Object)

생성일: 2023년 11월 22일 오후 3:14
태그: Chapter1

---

# 쿠버네티스 오브젝트란?

- 쿠버네티스 오브젝트는 쿠버네티스 시스템에서 영속성을 가지는 오브젝트
- 클러스터의 상태를 나타내기 위해 오브젝트를 활용
- 어떤 컨테이너화된 애플리케이션이 동작 중인지 (어느 노드에서 동작 중인디)
- 그 애플리케이션이 이용할 수 있는 리소스
- 그 애필리케이션이 어떻게 재구동 정책, 업그레이드, 그리고 내고장성과 같은 것에 동작해야 하는지에 대한 정책

# 오브젝트 명세(spec)와 상태(status)

- 거의 모든 쿠버네티스가 두 개의 중첩 오브젝트 필드를 포함함
- status
    - 쿠버네티스 시스템과 컴포넌트에 의해 제공되고 업데이트된 오브젝트의 `현재 상태` 를 설명함

> 쿠버네티스 디플로이먼트가 동작하는 애플리케이션을 표현해 줄 수 있는데
디플로이먼트를 생성할 때 디플로이먼트 spec에 3개의 애플리케이션으 레플리카가 동작되도록 설정
쿠버네티스 시스템이 그 spec를 읽어 spec에 일치하도록 `상태` 를 꾸준히 업데이트 하여
3개의 애플리케이션 인스턴스를 구동하게함
> 

# 쿠버네티스 오브젝트 기술하기

- 대부분의 정보를 yaml 파일로 kubectl에 제공하여 yaml으로 작성함

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 2 # tells deployment to run 2 pods matching the template
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.14.2
        ports:
        - containerPort: 80
```

# 오브젝트 종류

## Pod

- Pod는 쿠버네티스에서 컨테이너의 기본 단위로, 가장 기본적인 배포 단위
- pod는 1개 이상의 컨테이너로 구성된 컨테이너의 집합
- 각 pod는 고유한 ip 주소가 할당됨
- 같은 pod내의 컨테이너는 IP와 Port 공간을 공유함

## Volume

- pod가 생성될 때 로컬 디스크도 생성되는데 컨테이너가 재 실행되거나 종료되면 디스크 파일도 손실됨
이를 막기 위해 나온 스토리지
- 쿠버네티스는 영구적인 스토리지를 제공하는데 이를 볼륨이라고 함ㅏ
- 볼륨은 pod가 실행될 때 컨테이너에 마운트됨
- 네트워크 스토리지부터 aws ebs, google pd, azure 까지 다양하게 지원함

## Service

- 라벨링을 통해 같은 라벨을 가진 pod를 묶어 단일 엔드포인트(ClusterIp, NodePort, LoadBalancer 등)을 제공해주는 기능
- Service는 클러스터 내부에서 고정적인 IP를 가지고 있으며 Pod가 변경되어도 서비스의 IP는 변하지 않기 떄문에 클러스터 내/외부 통신 유지가 가능
- Pod간의 로드 밸런싱도 지원함

## Label

- 라벨은 쿠버네티스 리소스르 ㄹ선택하는데 사용됨
- 모든 리소스는 라벨을 가질 수 있음
- 라벨은 Key:Value 형태로 정의 할 수 있음
- 하나의 리소스에 여러개의 라벨을 동시에 적용할 수도 있음

## NameSpace

- NameSpace는 쿠버네팃 클러스터 내의 리소스들을 논리적으로 구분할 수 있게 해주는 하나의 단위
- Monitoring Resource , Application Resource, Test Resource 등으로 각각 네임스페이스로 가능
- 네임스페이스 별로 리소스(cpu, gpu, memory) 등을 할등해 줄 수 있음

## Controller

- 기본 오브젝트만으로도 가능하지만 좀 더 편리하게 사용하기 위해 `high-level` 의 오브젝트인 컨트롤러를 제공
- ReplicationController
    - Selector는 라벨을 기반으로 하며 라벨을 지정하면 해당 라벨을 가진 모든 pod를 관리합니다
    - Replica는 ReplicationController에 의해 관리되는 pod의 수를 지정함
    - Template는 새로운 pod를 추가할 때 해당 pod에 정의해야 하는데 이를 Template에다가 정의함
- ReplicaSet
    - ReplicationController의 상위 버전임
    - 공식문서에서도 ReplicationController보다 ReplicaSet을 이용하라함
    - Selector 여기는 Set기반의 집합 Selector을 사용함 따라서 in notin exists같은 연산자를 지원함
    - rolling-update Deployment를 통해 사용 가능함
- Deployment
    - ReplicaSet의 상위 개념 Pod와 ReplicaSet에 대한 배포를 관리함
    - 보통 deployment단위로 Pod와 ReplicaSet을 관리하고 운영함teFulSet
- Job
    - Job은 하나 이상의 pod를 지정하고 지정된 수의 pod를 실행함
    - 주로 백업이나 배치 작업에 사용됨
- CronJob
    - 리눅스 크론과 유사함