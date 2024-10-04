package vn.tayjava.service;


import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import vn.tayjava.grpcserver.VerifyRequest;
import vn.tayjava.grpcserver.VerifyResponse;
import vn.tayjava.grpcserver.VerifyTokenServiceGrpc;

@Service
@Slf4j
public class VerifyTokenService {

    @GrpcClient("verify-token-service")
    private VerifyTokenServiceGrpc.VerifyTokenServiceBlockingStub verifyTokenServiceBlockingStub;

    public VerifyResponse verifyToken(String token) {
        log.info("-----[ verifyToken ]-----");
        VerifyRequest verifyRequest = VerifyRequest.newBuilder().setToken(token).build();
        VerifyResponse response = verifyTokenServiceBlockingStub.verifyAccessToken(verifyRequest);

        return response;
    }
}
