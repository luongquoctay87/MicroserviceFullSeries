package vn.tayjava.service;


import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import vn.tayjava.grpcclient.VerifyRequest;
import vn.tayjava.grpcclient.VerifyResponse;
import vn.tayjava.grpcclient.VerifyTokenServiceGrpc;

@Service
@Slf4j
public class VerifyTokenService {

    @GrpcClient("verify-token-service")
    private VerifyTokenServiceGrpc.VerifyTokenServiceBlockingStub verifyTokenServiceBlockingStub;

    public boolean verifyToken(String token, String type) {
        log.info("-----[ verifyToken ]-----");
        VerifyRequest verifyRequest = VerifyRequest.newBuilder().setToken(token).setType(type).build();
        VerifyResponse response = verifyTokenServiceBlockingStub.isVerifyToken(verifyRequest);

        return response.getResult();
    }
}
