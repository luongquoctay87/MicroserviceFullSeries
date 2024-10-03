package vn.tayjava.service.grpc;


import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import vn.tayjava.grpcserver.*;

@Service
@Slf4j
public class GrpcClientTest {

    @GrpcClient("hello-service")
    private HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

    @GrpcClient("verify-token-service")
    private VerifyTokenServiceGrpc.VerifyTokenServiceBlockingStub verifyTokenServiceBlockingStub;

    public String testingGRPC() {
        HelloRequest helloRequest = HelloRequest.newBuilder().setFirstName("Ong Tay").setLastName("Viet Nam").build();
        HelloResponse response = helloServiceBlockingStub.hello(helloRequest);
        log.info(response.getGreeting());

        return response.getGreeting();
    }

    public boolean verifyToken(String token, String type) {
        log.info("-----[ verifyToken ]-----");
        VerifyRequest verifyRequest = VerifyRequest.newBuilder().setToken(token).setType(type).build();
        VerifyResponse response = verifyTokenServiceBlockingStub.isVerifyToken(verifyRequest);
log.info("R------------> {}", response.getResult());
        return response.getResult();
    }
}
