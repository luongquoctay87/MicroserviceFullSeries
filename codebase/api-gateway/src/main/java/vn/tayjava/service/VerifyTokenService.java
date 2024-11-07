package vn.tayjava.service;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @CircuitBreaker(name = "authenticationServiceCircuitBreaker", fallbackMethod = "errorMessage")
    public VerifyResponse verifyToken(String token) {
        log.info("-----[ verifyToken ]-----");
        VerifyRequest verifyRequest = VerifyRequest.newBuilder().setToken(token).build();
        VerifyResponse response = verifyTokenServiceBlockingStub.verifyAccessToken(verifyRequest);

        return response;
    }


    /**
     *
     * @param throwable
     * @return
     */
    public VerifyResponse errorMessage(Throwable throwable) {
        return VerifyResponse.newBuilder().setIsVerified(false).setMessage("Service unavailable, please try again!").build();
    }


}
