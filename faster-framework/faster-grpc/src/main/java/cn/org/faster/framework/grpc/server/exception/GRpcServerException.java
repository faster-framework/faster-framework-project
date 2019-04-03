package cn.org.faster.framework.grpc.server.exception;

/**
 * @author zhangbowen
 * @since 2019/4/3
 */
public class GRpcServerException extends RuntimeException {
    public GRpcServerException(String message) {
        super(message);
    }
}
