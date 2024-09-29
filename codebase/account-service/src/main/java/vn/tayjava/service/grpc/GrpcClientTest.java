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
