package vn.tayjava.service.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;
import vn.tayjava.common.TokenType;
import vn.tayjava.grpcserver.VerifyRequest;
import vn.tayjava.grpcserver.VerifyResponse;
import vn.tayjava.grpcserver.VerifyTokenServiceGrpc;
import vn.tayjava.repository.UserRepository;
import vn.tayjava.service.JwtService;

import static vn.tayjava.common.TokenType.ACCESS_TOKEN;
import static vn.tayjava.common.TokenType.REFRESH_TOKEN;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class VerifyServiceImpl extends VerifyTokenServiceGrpc.VerifyTokenServiceImplBase {

    private final JwtService jwtService;

    @Override
    public void isVerifyToken(VerifyRequest request, StreamObserver<VerifyResponse> responseObserver) {
        log.info("-----[ isVerifyToken ]-----");

        boolean result = false;
        String message = "";
        try {
            TokenType type = TokenType.valueOf(request.getType());
            result = jwtService.isVerifyToken(request.getToken(), type);
            if (!result){
                message = "Token is invalid";
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            message = e.getMessage();
        }
        VerifyResponse response = VerifyResponse.newBuilder().setResult(result).setMessage(message).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
