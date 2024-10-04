package vn.tayjava.service.grpc;

import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;
import vn.tayjava.common.TokenType;
import vn.tayjava.exception.UnauthorizedException;
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
    public void verifyAccessToken(VerifyRequest request, StreamObserver<VerifyResponse> responseObserver) {
        log.info("-----[ verifyToken ]-----");
        VerifyResponse response;
        try {
            jwtService.extractUsername(request.getToken(), TokenType.ACCESS_TOKEN);
            response = VerifyResponse.newBuilder().setIsVerified(true).setMessage("Token is valid").build();
        } catch (IllegalArgumentException e) {
            response = VerifyResponse.newBuilder().setIsVerified(false).setMessage(e.getMessage()).build();
        } catch (ExpiredJwtException | SignatureException ex) {
            response = VerifyResponse.newBuilder().setIsVerified(false).setMessage(ex.getMessage()).build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

