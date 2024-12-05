minikube start --cpus 8 --memory 8192

# macOS
eval $(minikube -p minikube docker-env)

# Maven build
mvn clean package -P local

# Docker build
docker build -t cloud-gateway:latest cloud-gateway
docker build -t authz:latest authentication
docker build -t account:latest account
docker build -t common:latest common
docker build -t calendar:latest calendar
docker build -t call-list:latest call-list
docker build -t contact:latest contact
docker build -t company:latest company
docker build -t case-desk:latest case-desk
docker build -t inventory:latest central-inventory
docker build -t fund-transfer:latest fund-transfer
docker build -t notification:latest notification
docker build -t payment:latest payment
docker build -t saldo:latest saldo
docker build -t sale:latest sale
docker build -t self-service:latest self-service
docker build -t service-portal:latest service-portal

cd buildScripts

# Deploy on minikube
kubectl apply -f configMap.yaml
kubectl apply -f secretKey.yaml
kubectl apply -f zookeeper.yaml
kubectl apply -f kafka.yaml
kubectl apply -f redis.yaml
kubectl apply -f ingress.yaml
kubectl apply -f cloud-gateway.yaml
kubectl apply -f authz.yaml
kubectl apply -f account.yaml
kubectl apply -f common.yaml
kubectl apply -f company.yaml
kubectl apply -f contact.yaml
kubectl apply -f calendar.yaml
kubectl apply -f call-list.yaml
kubectl apply -f case-desk.yaml
kubectl apply -f sale.yaml
kubectl apply -f payment.yaml
kubectl apply -f inventory.yaml
kubectl apply -f fund-transfer.yaml
kubectl apply -f saldo.yaml
kubectl apply -f self-service.yaml
kubectl apply -f service-portal.yaml
kubectl apply -f notification.yaml

# Don't forget run this command while build LoadBalancer
minikube addons enable ingress

echo "=========================> ENTER YOUR PASSWORD FOR USING HOSTNAME api-local.k8s"
minikube tunnel
