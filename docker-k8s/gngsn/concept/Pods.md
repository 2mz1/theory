### 파드

Pod: 컨테이너의 집합 중 가장 작은 단위, 컨테이너의 실행 방법 정의

파드 (고래 떼(pod of whales)나 콩꼬투리(pea pod)와 마찬가지로)는 하나 이상의 컨테이너의 그룹이다. 
- 스토리지 및 네트워크를 공유하고, 해당 컨테이너를 구동하는 방식에 대한 명세를 갖음
- 파드의 콘텐츠는 항상 함께 배치되고, 함께 스케줄되며, 공유 콘텍스트에서 실행
- 파드는 애플리케이션 별 "논리 호스트"를 모델링
  - 여기에는 상대적으로 밀접하게 결합된 하나 이상의 애플리케이션 컨테이너가 포함
- 클라우드가 아닌 콘텍스트에서, 동일한 물리 또는 가상 머신에서 실행되는 애플리케이션은 동일한 논리 호스트에서 실행되는 클라우드 애플리케이션과 비슷하다.

애플리케이션 컨테이너와 마찬가지로, 파드에는 파드 시작 중에 실행되는 초기화 컨테이너가 포함될 수 있다. 클러스터가 제공하는 경우, 디버깅을 위해 임시 컨테이너를 삽입할 수도 있다.

파드의 공유 콘텍스트는 리눅스 네임스페이스, 컨트롤 그룹(cgroup) 및 컨테이너를 격리하는 것과 같이 잠재적으로 다른 격리 요소들이다. 파드의 콘텍스트 내에서 개별 애플리케이션은 추가적으로 하위 격리가 적용된다.

파드는 공유 네임스페이스와 공유 파일시스템 볼륨이 있는 컨테이너들의 집합과 비슷하다.

<br/><br/>

### Pod 예시

```Bash
apiVersion: v1
kind: Pod
metadata:
  name: nginx
spec:
  containers:
  - name: nginx
    image: nginx:1.14.2
    ports:
    - containerPort: 80
```

<br/><br/>

## Workload resources for managing pods

- 주로, 디플로이먼트(Deployment) 또는 잡(Job)과 같은 워크로드 리소스를 사용하여 생성
- 파드가 상태를 추적해야 한다면, 스테이트풀셋(StatefulSet) 리소스를 고려

<small>일반적으로 싱글톤(singleton) 파드를 포함하여 파드를 직접 만들 필요가 없음 (거의 테스트 용)</small>

- 쿠버네티스 클러스터의 파드는 두 가지 주요 방식으로 사용
  - **✔️ 단일 컨테이너를 실행하는 파드**, Pods that run a single container
    - "파드 당 하나의 컨테이너" 모델은 가장 일반적인 쿠버네티스 유스케이스
    - 이 경우, 파드를 단일 컨테이너를 둘러싼 래퍼(wrapper)로 생각할 수 있음
    - 쿠버네티스는 컨테이너를 직접 관리하는 대신 파드를 관리
  - **✔️ 함께 작동해야 하는 여러 컨테이너를 실행하는 파드**, Pods that run multiple containers that need to work together
    - 파드는 밀접하게 결합되어 있고 리소스를 공유해야 하는 함께 배치된 여러 개의 컨테이너로 구성된 애플리케이션을 캡슐화할 수 있음
    - 이런 함께 배치된 컨테이너는 하나의 결합된 서비스 단위를 형성
    - 예를 들어, 하나의 컨테이너는 공유 볼륨에 저장된 데이터를 퍼블릭에 제공하는 반면, 별도의 사이드카 컨테이너는 해당 파일을 새로 고치거나 업데이트
    - 파드는 이러한 컨테이너, 스토리지 리소스, 임시 네트워크 ID를 단일 단위로 함께 래핑

<br/><br/>

## How Pods manage multiple containers

- 파드는 응집력있는 서비스 단위의 여러 프로세스(컨테이너)를 지원하도록 설계됨
- 파드의 컨테이너는 클러스터의 동일한 물리 또는 가상 머신에서 자동으로 같은 위치에 배치되고 함께 스케줄됨
- 컨테이너는 리소스와 의존성을 공유하고, 서로 통신하고, 종료 시기와 방법을 조정할 수 있음

<br/><br/>

## Pod Templates
: 파드를 생성하기 위한 명세이며, Deployments, Jobs, 그리고 DaemonSets과 같은 워크로드 리소스에 포함

- 워크로드 리소스에 위한 컨트롤러들이 **파드 템플릿**에서 파드를 생성하고, 사용자 대신 해당 파드를 관리

```Bash
apiVersion: batch/v1
kind: Job
metadata:
  name: hello
spec:
  template:
    # This is the pod template
    spec:
      containers:
      - name: hello
        image: busybox:1.28
        command: ['sh', '-c', 'echo "Hello, Kubernetes!" && sleep 3600']
      restartPolicy: OnFailure
    # The pod template ends here
```

위 파드의 container는 메시지를 출력한 다음 일시 중지함

<br/><br/>

## Pod update and replacement

- 워크로드 리소스의 파드 템플릿이 바뀌면, 컨트롤러는 기존의 파드를 갱신하거나 패치하는 대신 갱신된 템플릿을 기반으로 신규 파드를 생성
- 쿠버네티스는 사용자가 파드를 직접 관리하는 것을 막지는 않고, 동작 중인 파드의 필드를 갱신하는 것도 가능. 
- 그러나, `patch` 및 `replace`와 같은 파드 갱신 작업에는 다음과 같은 제약이 있음 
  - 1.파드에 대한 대부분의 메타데이터는 불변(immutable)
    - 가령, 사용자는 namespace, name, uid, 또는 creationTimestamp 필드를 변경할 수 없음.
    - `generation` 필드는 고유. 이 필드는 필드의 현재 값을 증가시키는 갱신만 허용.
  - 2.`metadata.deletionTimestamp` 가 설정된 경우, `metadata.finalizers` 리스트에 새로운 항목이 추가될 수 없음
    - 파드 업데이트는 `spec.containers[*].image`, `spec.initContainers[*].image`, `spec.activeDeadlineSeconds`, 또는 `spec.tolerations` 이외의 필드는 변경하지 않음. `spec.tolerations`만 새로운 항목 추가 가능.
  - 3.`spec.activeDeadlineSeconds` 필드를 추가할 때는, 아래 두 가지 형태의 갱신만 허용
    - 할당되지 않은 필드를 양수로 설정
    - 필드의 양수를 음수가 아닌 더 작은 숫자로 갱신

<br/><br/>

## Container probes
*probe*는 컨테이너의 kubelet에 의해 주기적으로 실행되는 진찰
진찰을 수행하기 위하여 kubelet은 다음과 같은 작업을 호출할 수 있다.

- `ExecAction`: 컨테이너 런타임의 도움을 받아 수행
- `TCPSocketAction`: kubelet에 의해 직접 검사
- `HTTPGetAction`: kubelet에 의해 직접 검사

[LifeCycle Docs 참고](https://kubernetes.io/ko/docs/concepts/workloads/pods/pod-lifecycle/)


