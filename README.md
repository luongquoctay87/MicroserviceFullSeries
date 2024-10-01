# gRPC in Microservice

### 1. gRPC là gì ?
gRPC là một framework mã nguồn mở được phát triển bởi Google dựa trên mô hình gọi thủ tục từ xa (Remote Procedure Call - RPC). Nó được thiết kế để hỗ trợ giao tiếp giữa các ứng dụng phân tán trong môi trường mạng.

#### 1.1 Giao thức nền tảng: HTTP/2
gRPC sử dụng HTTP/2 thay vì HTTP/1.1 để truyền dữ liệu giữa client và `server`. HTTP/2 mang lại nhiều lợi thế
- **Multiplexing**: HTTP/2 cho phép nhiều luồng dữ liệu được truyền đồng thời qua một kết nối TCP duy nhất, giúp giảm độ trễ và tăng hiệu suất.
- **Header compression**: HTTP/2 hỗ trợ nén các tiêu đề HTTP, giúp giảm kích thước dữ liệu truyền đi.
- **Streaming**: gRPC sử dụng tính năng này của HTTP/2 để truyền tải dữ liệu liên tục (stream) giữa client và `server`.
- **Bảo mật**: HTTP/2 thường yêu cầu bảo mật thông qua TLS, đảm bảo rằng dữ liệu được truyền qua mạng an toàn.

#### 1.2 ProtoBuf (Protocol Buffers)
ProtoBuf là ngôn ngữ định nghĩa giao thức được sử dụng trong gRPC để serialization và deserialization. 

ProtoBuf có các ưu điểm sau:
- **Tối ưu về kích thước và hiệu suất**: Dữ liệu được mã hóa dưới dạng nhị phân nhỏ gọn, nhanh hơn so với các định dạng như JSON hay XML.
- **Ngôn ngữ định nghĩa dịch vụ**: Bạn sử dụng một tệp .proto để định nghĩa các phương thức dịch vụ và các message dữ liệu được trao đổi giữa client và `server`.
- **Hỗ trợ nhiều ngôn ngữ**: ProtoBuf có thể tạo mã cho nhiều ngôn ngữ lập trình khác nhau (Java, C++, Go, Python, Ruby, C#, v.v.), giúp dễ dàng tích hợp trong môi trường đa nền tảng.

Ví dụ định nghĩa file `.proto`
```text
syntax = "proto3";

service HelloService {
  rpc SayHello (HelloRequest) returns (HelloResponse);
}

message HelloRequest {
  string firstName = 1;
  string lastName = 2;
}

message HelloResponse {
  string message = 1;
}
```

#### 1.3 Các phương pháp gọi gRPC
gRPC hỗ trợ nhiều cách gọi thủ tục khác nhau phù hợp với các kịch bản sử dụng đa dạng

- Unary RPC: `Client` gửi một yêu cầu duy nhất và nhận lại một phản hồi duy nhất từ `server`, Đây là kiểu đơn giản nhất giống như hàm đồng bộ.

![unary-rpc.png](gallery/unary-rpc.png)

- Server Streaming RPC: `Client` gửi một yêu cầu và `server` trả về một luồng nhiều phản hồi (stream) mà không cần yêu cầu bổ sung từ `client`.

![server-streaming-rpc.png](gallery/server-streaming-rpc.png)

- Client Streaming RPC: `Client` gửi nhiều yêu cầu (stream) và `server` trả về một phản hồi sau khi nhận xong tất cả yêu cầu

![client-streaming-rpc.png](gallery/client-streaming-rpc.png)

- Bidirectional Streaming RPC: Cả client và server đều có thể gửi nhiều message qua lại trong luồng. Hai bên có thể truyền và nhận dữ liệu liên tục mà không cần chờ đợi toàn bộ yêu cầu hoặc phản hồi.

![bidirectional-streaming-rpc.png](gallery/bidirectional-streaming-rpc.png)

#### 1.4. Kiến trúc gRPC

![grpc-architecture.png](gallery/grpc-architecture.png)

- **Stub (Client)**: Phía `client` sử dụng một đối tượng gọi là stub để thực hiện các cuộc gọi RPC. Stub là một đại diện của dịch vụ trên `server` giúp `client` gửi các yêu cầu tới `server` thông qua giao diện `gRPC`.
- **Server**: `Server` triển khai dịch vụ được định nghĩa trong file `.proto`. Khi nhận yêu cầu từ `client` thì `server` thực hiện các hành động tương ứng và trả về kết quả.
- **Channel**: Là kênh kết nối giữa `client` và `server` thực hiện giao tiếp thông qua `HTTP/2.` Channel được bảo mật bởi TLS nếu cần.

#### 1.5 Streaming trong gRPC
`Streaming` là một trong những tính năng mạnh mẽ nhất của `gRPC`. Nó cho phép truyền dữ liệu lớn hoặc liên tục giữa máy khách và máy chủ mà không cần đóng kết nối. Các kiểu stream phổ biến:
- **Server streaming**: Máy chủ trả về một chuỗi dữ liệu liên tục cho một yêu cầu đơn lẻ.
- **Client streaming**: Máy khách gửi dữ liệu liên tục đến máy chủ trong một kết nối duy nhất.
- **Bidirectional streaming**: Cả hai bên gửi và nhận dữ liệu đồng thời qua một kết nối duy nhất. Streaming giúp gRPC thích hợp cho các ứng dụng như chat, video streaming hoặc bất kỳ dịch vụ nào yêu cầu trao đổi dữ liệu liên tục trong thời gian thực.

#### 1.6 Bảo mật trong gRPC
gRPC tích hợp chặt chẽ với TLS để cung cấp bảo mật giao tiếp giữa `client` và `server`. Nó có một số điểm nổi bật về bảo mật của gRPC:

- **TLS/SSL**: gRPC hỗ trợ mã hóa dữ liệu truyền tải qua TLS, đảm bảo tính bảo mật và riêng tư.
- **Xác thực (Authentication)**: gRPC cung cấp nhiều cơ chế xác thực `client` và `server` từ cơ chế dựa trên chứng chỉ TLS đến xác thực thông qua token JWT hoặc OAuth2.

#### 1.7 Ưu điểm của gRPC
- **gRPC** rất phù hợp với kiến trúc microservices vì hiệu suất cao, khả năng tương thích đa ngôn ngữ và các tính năng như streaming và bảo mật. Trong các hệ thống microservices các microservice giao tiếp với nhau thông qua gRPC để giảm độ trễ và tăng hiệu quả xử lý.
- **gRPC** dễ dàng tích hợp với các công nghệ quản lý hệ thống như Kubernetes hoặc Docker. Trong môi trường sản xuất, các công cụ như Envoy hoặc Istio có thể được sử dụng để quản lý và tối ưu hóa giao tiếp gRPC giữa các dịch vụ.

### 2. Demo sample call-gRPC trong microservice
![sample-call-grpc.png](gallery/sample-call-grpc.png)

#### 2.1 Build gRPC server tại Authentication-service
- Bước 1: thêm các dependency sau vào `pom.xml`
```xml
<properties>
  <grpc.version>1.68.0</grpc.version>
  <protoc.version>3.21.12</protoc.version>
</properties>

<dependencies>
  <dependency>
      <groupId>io.grpc</groupId>
      <artifactId>grpc-netty</artifactId>
      <version>${grpc.version}</version>
  </dependency>
  <dependency>
      <groupId>io.grpc</groupId>
      <artifactId>grpc-protobuf</artifactId>
      <version>${grpc.version}</version>
  </dependency>
  <dependency>
      <groupId>io.grpc</groupId>
      <artifactId>grpc-stub</artifactId>
      <version>${grpc.version}</version>
  </dependency>
  <dependency>
      <groupId>net.devh</groupId>
      <artifactId>grpc-server-spring-boot-starter</artifactId>
      <version>3.1.0.RELEASE</version>
  </dependency>
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.17.2</version>
  </dependency>
  <dependency>
      <groupId>javax.annotation</groupId>
      <artifactId>javax.annotation-api</artifactId>
      <version>1.3.2</version>
  </dependency>
    <!-- others -->
</dependencies>
```
- Bước 2: cấu hình gRPC server
  - application.yml
  ```yml
  grpc:
    server:
      port: 9091
  ```

  - AppConfig.java
  ```java
  @Bean
  public GrpcAuthenticationReader grpcAuthenticationReader(){
    return new BasicGrpcAuthenticationReader();
  }
  ```
- Bước 3: tạo file `.proto`

    - Tạo folder `proto` tại package `src.main`
    - Tạo file `HelloService.proto` tạo folder `proto`
    ```text
    syntax = "proto3";
    option java_multiple_files = true;
    package vn.tayjava.grpcserver;
    
    
    service HelloService {
    rpc hello(HelloRequest) returns (HelloResponse);
    }
    
    message HelloRequest {
    string firstName = 1;
    string lastName = 2;
    }
    
    message HelloResponse {
    string greeting = 1;
    }
    ```
  
- Bước 4: Generate ra gRPC service từ file HelloService.proto
> Để compile thành công chúng ta cần thêm 1 số dependency để biên dịch file .proto ra file .class
```xml
<build>
  <!-- Extension for build file .proto -->
  <extensions>
    <extension>
      <groupId>kr.motd.maven</groupId>
      <artifactId>os-maven-plugin</artifactId>
      <version>1.6.1</version>
    </extension>
  </extensions>
  <plugins>
    <!-- other plugin -->
    
    <!-- Protocol compiler for build .proto -->
    <plugin>
      <groupId>org.xolstice.maven.plugins</groupId>
      <artifactId>protobuf-maven-plugin</artifactId>
      <version>0.6.1</version>
      <configuration>
        <protocArtifact>
          com.google.protobuf:protoc:${protoc.version}:exe:${os.detected.classifier}
        </protocArtifact>
        <pluginId>grpc-java</pluginId>
        <pluginArtifact>
          io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}
        </pluginArtifact>
        <protoSourceRoot>
          src/main/proto
        </protoSourceRoot>
      </configuration>
      <executions>
        <execution>
          <goals>
            <goal>compile</goal>
            <goal>compile-custom</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

Biên dịch với maven extension
```bash
$ mvn clean compile 
```

Kết quả sau khi compile

![protoc-generate.png](gallery/protoc-generate.png)

- Bước 5: Tạo gRPC service tại package `vn.tayjava.service.grpc`
```java
package vn.tayjava.service.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.tayjava.grpcserver.HelloRequest;
import vn.tayjava.grpcserver.HelloResponse;
import vn.tayjava.grpcserver.HelloServiceGrpc;

@GrpcService
@Slf4j
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        log.info(request.toString());

        HelloResponse response = HelloResponse.newBuilder().setGreeting("Hello " + request.getFirstName() + " " + request.getLastName()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
```
- Bước 6: Chạy authentication service
```bash
$ ./mvnw spring-boot:run
2024-10-01T11:01:32.747+07:00  INFO 48850 --- [authentication-service] [  restartedMain] n.d.b.g.s.s.AbstractGrpcServerFactory    : Registered gRPC service: vn.tayjava.grpcserver.HelloService, bean: helloServiceImpl, class: vn.tayjava.service.grpc.HelloServiceImpl
2024-10-01T11:01:32.747+07:00  INFO 48850 --- [authentication-service] [  restartedMain] n.d.b.g.s.s.AbstractGrpcServerFactory    : Registered gRPC service: grpc.health.v1.Health, bean: grpcHealthService, class: io.grpc.protobuf.services.HealthServiceImpl
2024-10-01T11:01:32.747+07:00  INFO 48850 --- [authentication-service] [  restartedMain] n.d.b.g.s.s.AbstractGrpcServerFactory    : Registered gRPC service: grpc.reflection.v1alpha.ServerReflection, bean: protoReflectionService, class: io.grpc.protobuf.services.ProtoReflectionService
2024-10-01T11:01:32.748+07:00  INFO 48850 --- [authentication-service] [  restartedMain] n.d.b.g.s.s.GrpcServerLifecycle          : gRPC Server started, listening on address: in-process:auth-server, port: -1
2024-10-01T11:01:32.758+07:00  INFO 48850 --- [authentication-service] [  restartedMain] v.t.AuthenticationServiceApplication     : Started AuthenticationServiceApplication in 2.556 seconds (process running for 2.704)
```

- Bước 7: Test gRPC service bằng bloomRPC
![bloomRPC-test.png](gallery/bloomRPC-test.png)


#### 2.2 Build gRPC client tại Account-service
- Bước 1: thêm các dependency sau vào `pom.xml`
```xml
<properties>
  <grpc.version>1.68.0</grpc.version>
  <protoc.version>3.21.12</protoc.version>
</properties>

<dependencies>
<dependency>
  <groupId>io.grpc</groupId>
  <artifactId>grpc-netty</artifactId>
  <version>${grpc.version}</version>
</dependency>
<dependency>
  <groupId>io.grpc</groupId>
  <artifactId>grpc-protobuf</artifactId>
  <version>${grpc.version}</version>
</dependency>
<dependency>
  <groupId>io.grpc</groupId>
  <artifactId>grpc-stub</artifactId>
  <version>${grpc.version}</version>
</dependency>
<dependency>
  <groupId>net.devh</groupId>
  <artifactId>grpc-client-spring-boot-starter</artifactId>
  <version>3.1.0.RELEASE</version>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.17.2</version>
</dependency>
<dependency>
  <groupId>javax.annotation</groupId>
  <artifactId>javax.annotation-api</artifactId>
  <version>1.3.2</version>
</dependency>
<!-- others -->
</dependencies>
```

- Bước 2: cấu hình gRPC client
  - application.yml
  ```yml
  grpc:
    client:
      hello-service:
        address: localhost:9091
        enableKeepAlive: true
        keepAliveWithoutCalls: true
        negotiationType: plaintext
  ```
  
- Bước 3: tạo file `.proto`

  - Tạo folder `proto` tại package `src.main`
  - Tạo file `HelloService.proto` tạo folder `proto`
    ```text
    syntax = "proto3";
    option java_multiple_files = true;
    package vn.tayjava.grpcserver;
    
    
    service HelloService {
    rpc hello(HelloRequest) returns (HelloResponse);
    }
    
    message HelloRequest {
    string firstName = 1;
    string lastName = 2;
    }
    
    message HelloResponse {
    string greeting = 1;
    }
    ```

- Bước 4: Generate ra gRPC service từ file HelloService.proto
> Để compile thành công chúng ta cần thêm 1 số dependency để biên dịch file .proto ra file .class
```xml
<build>
  <!-- Extension for build file .proto -->
  <extensions>
    <extension>
      <groupId>kr.motd.maven</groupId>
      <artifactId>os-maven-plugin</artifactId>
      <version>1.6.1</version>
    </extension>
  </extensions>
  <plugins>
    <!-- other plugin -->
    
    <!-- Protocol compiler for build .proto -->
    <plugin>
      <groupId>org.xolstice.maven.plugins</groupId>
      <artifactId>protobuf-maven-plugin</artifactId>
      <version>0.6.1</version>
      <configuration>
        <protocArtifact>
          com.google.protobuf:protoc:${protoc.version}:exe:${os.detected.classifier}
        </protocArtifact>
        <pluginId>grpc-java</pluginId>
        <pluginArtifact>
          io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}
        </pluginArtifact>
        <protoSourceRoot>
          src/main/proto
        </protoSourceRoot>
      </configuration>
      <executions>
        <execution>
          <goals>
            <goal>compile</goal>
            <goal>compile-custom</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

Biên dịch với maven extension
```bash
$ mvn clean compile 
```

Kết quả sau khi compile

![protoc-generate.png](gallery/protoc-generate.png)

- Bước 5: Tạo kết nối đến Server với `@GrpcClient`
```java
package vn.tayjava.service.grpc;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import vn.tayjava.grpcserver.HelloRequest;
import vn.tayjava.grpcserver.HelloResponse;
import vn.tayjava.grpcserver.HelloServiceGrpc;

@Service
@Slf4j
public class GrpcClientTest {

  @GrpcClient("hello-service")
  private HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

  public String testingGRPC() {
    HelloRequest helloRequest = HelloRequest.newBuilder().setFirstName("Ong Tay").setLastName("Viet Nam").build();
    HelloResponse response = helloServiceBlockingStub.hello(helloRequest);
    log.info(response.getGreeting());

    return response.getGreeting();
  }
}
```

- Bước 6: Chạy account service
```bash
$ ./mvnw spring-boot:run
2024-10-01T11:15:37.868+07:00  INFO 50294 --- [account-service] [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8082 (http) with context path '/'
2024-10-01T11:15:37.877+07:00  INFO 50294 --- [account-service] [  restartedMain] vn.tayjava.AccountServiceApplication     : Started AccountServiceApplication in 1.062 seconds (process running for 1.203)
```

- Bước 7: Test gRPC service bằng call REST API
```bash
$ curl --location 'http://localhost:8082/user/test-grpc'
Hello Ong Tay Viet Nam
```

### 3. Bài tập về nhà
Call gRPC từ api-gateway đến authentication-service để verify-token