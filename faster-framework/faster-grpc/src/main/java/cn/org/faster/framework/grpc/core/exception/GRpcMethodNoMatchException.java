package cn.org.faster.framework.grpc.core.exception;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
public class GRpcMethodNoMatchException extends RuntimeException {
    public GRpcMethodNoMatchException(String className, String methodName, String grpcMethodType, String errorMessage) {
        super("GRpcService method does not match GRpc type." +
                "[Class:" + className + "]" +
                "[Method:" + methodName + "]" +
                "[GRpc method type is:" + grpcMethodType + "]." +
                errorMessage);
    }
}
