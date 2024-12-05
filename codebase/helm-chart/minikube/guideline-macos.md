# Deploy application by kubernetes on local

**Prerequisites:**
- Container or virtual machine manager, such as: Docker, QEMU, Hyperkit, Hyper-V, KVM, Parallels, Podman, VirtualBox, or VMware Fusion/Workstation
- Install minikube
- Install Helm with a package manager (Optional)

## 1. Install docker:

[install docker](https://minikube.sigs.k8s.io/docs/drivers/docker/)

- ***Requirements***
    - Install Docker 18.09 or higher (20.10 or higher is recommended)
    - amd64 or arm64 system.
    - If using WSL complete these steps first

- ***Usage***

    Start a cluster using the docker driver:

    ```
    $ minikube start --driver=docker
    ```

    To make docker the default driver:
    ```
    $ minikube config set driver docker
    ```


## 2. Install minikube:

[minikube start](https://minikube.sigs.k8s.io/docs/start/)

### 2.1 Install minikube on MacOS

- minikube
    ```
    $ curl -LO https://github.com/kubernetes/minikube/releases/download/v1.31.2/minikube-darwin-arm64
    $ sudo install minikube-darwin-arm64 /usr/local/bin/minikube
    ```

- kubectl

    Use kubectl inside minikube, 
    By default, [kubectl](https://kubernetes.io/docs/tasks/tools/) gets configured to access the kubernetes cluster control plane inside minikube when the minikube start command is executed.

### 2.2 Start your cluster

- Start minikube 

    ```
    $ minikube start
    ```

- To allow Kubernetes to read your docker repository you need to run below command , so that both will be in sync

    ```
    $ minikube docker-env
    $ eval $(minikube -p minikube docker-env)
    ```

- Check version kubectl

    ```
    $ kubectl version --client
    ```

- Check that kubectl is properly configured by getting the cluster state

    ```
    $ kubectl cluster-info
    ```

### 2.3 Interact with your cluster
```
$ kubectl get po -A
$ minikube dashboard
```

### 2.4 Deploy applications

#### 2.4.1 Deploy Service

- Create file `Dockerfile`

    ```
    FROM public.ecr.aws/docker/library/openjdk:17
    ARG JAR_FILE=target/*.jar
    COPY ${JAR_FILE} cloud-gateway.jar
    ENTRYPOINT ["java", "-jar","cloud-gateway.jar"]
    EXPOSE 4953
    ```

- Docker build application
    ```
    $ cd ~/cloud-gateway
    $ docker build -t gateway:1.0 .
    $ docker images
    ```

- Create deployment and expose it on port 4953

    ```
    $ kubectl create deployment gateway --image=gateway:1.0
    $ kubectl expose deployment gateway --type=NodePort --port=4953
    ```

- Get services list / Check service available

    ```
    $ kubectl get services gateway
    ```

- Publish service to web browser

    ```
    $ minikube service gateway
    ```

- Alternatively, use kubectl to forward the port:

    ```
    $ kubectl port-forward service/gateway 8181:4953
    ```

#### 2.4.2 Deploy LoadBalancer


#### 2.4.3 Ingress


### 2.5. Manage cluster

- Pause Kubernetes without impacting deployed applications:

    ```
    $ minikube pause
    ```

- Unpause a paused instance:

    ```
    $ minikube unpause
    ```

- Halt the cluster:

    ```
    $ minikube stop
    ```

- Change the default memory limit (requires a restart):

    ```
    $ minikube config set memory 9001
    ```

- Browse the catalog of easily installed Kubernetes services:

    ```
    $ minikube addons list
    ```

- Create a second cluster running an older Kubernetes release:

    ```
    $ minikube start -p aged --kubernetes-version=v1.16.1
    ```

- Delete all of the minikube clusters:

    ```
    $ minikube delete --all
    ```
