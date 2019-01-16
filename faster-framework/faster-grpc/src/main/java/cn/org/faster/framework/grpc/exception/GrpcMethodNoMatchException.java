package cn.org.faster.framework.grpc.exception;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
public class GrpcMethodNoMatchException extends RuntimeException {
    public GrpcMethodNoMatchException(String className, String methodName, String grpcMethodType, String errorMessage) {
        super("GrpcService method does not match grpc type." +
                "[Class:" + className + "]" +
                "[Method:" + methodName + "]:" +
                "[Grpc method type is :" + grpcMethodType + "]." +
                errorMessage);
    }
}
