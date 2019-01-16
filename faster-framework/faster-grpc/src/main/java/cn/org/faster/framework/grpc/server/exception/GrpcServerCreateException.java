package cn.org.faster.framework.grpc.server.exception;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
public class GrpcServerCreateException extends RuntimeException{
    public GrpcServerCreateException(String message) {
        super(message);
    }
}
