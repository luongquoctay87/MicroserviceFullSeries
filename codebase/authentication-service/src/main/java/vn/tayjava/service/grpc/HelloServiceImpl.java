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
