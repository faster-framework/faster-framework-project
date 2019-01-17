package cn.org.faster.framework.grpc.server.exception;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
public class GRpcServerCreateException extends RuntimeException{
    public GRpcServerCreateException(String message) {
        super(message);
    }
}
