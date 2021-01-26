
### Introcution


### Deploy  with prebuild images

#### Deploy to Kubernetes cluster

This project can be deployed to Kubernetes cluster with the following command:

```sh
cd prebuild/
for i in *.yaml; do kubectl apply -f $i; done
```

If you want to delete the deployment, please use the following command:

```sh
for i in *.yaml; do kubectl delete -f $i; done
```

### Build manually

First, in root directory
```sh
mvn clean install
```

Then, in each path of submodule
```
./build.sh
``` 


### Credit

This project is forked from [aliyun/alibabacloud-microservice-demo](https://github.com/aliyun/alibabacloud-microservice-demo)
