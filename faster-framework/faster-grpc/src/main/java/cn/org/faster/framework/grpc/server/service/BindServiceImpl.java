//package cn.org.faster.framework.grpc.server.service;
//
//import cn.org.faster.framework.grpc.core.marshaller.FastJsonMarshaller;
//import io.grpc.BindableService;
//import io.grpc.MethodDescriptor;
//import io.grpc.ServerServiceDefinition;
//import io.grpc.stub.ServerCalls;
//import io.grpc.stub.StreamObserver;
//import org.springframework.beans.factory.BeanFactory;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author zhangbowen
// * @since 2019/1/16
// */
//public class BindServiceImpl implements BindableService {
//    private static String scheme;
//    private static Map<String,Map<Method,MethodDescriptor<Object, Object>>> methodMethodDescriptorMap = new HashMap<>();
//    private BeanFactory beanFactory;
//
//    static {
//        scheme = "io.grpc.KeyValueService";
//        methodMethodDescriptorMap.put("class",MethodDescriptor.newBuilder(
//                new FastJsonMarshaller(),
//                new FastJsonMarshaller(Object.class))
//                .setFullMethodName(
//                        MethodDescriptor.generateFullMethodName("io.grpc.KeyValueService", "create"))
//                .setType(MethodDescriptor.MethodType.SERVER_STREAMING)
//                .build());
//    }
//
//    @Override
//    public ServerServiceDefinition bindService() {
//        ServerServiceDefinition.Builder ssd = ServerServiceDefinition.builder(scheme);
//        methodMethodDescriptorMap.forEach((className,methodDescriptor)->{
//            ssd.addMethod(methodDescriptor, ServerCalls.asyncUnaryCall(new ServerCalls.UnaryMethod<Object, Object>() {
//                @Override
//                public void invoke(Object request, StreamObserver<Object> responseObserver) {
//
//                }
//            }));
//            ssd.addMethod(methodDescriptor,ServerCalls.asyncServerStreamingCall(new ServerCalls.ServerStreamingMethod<Object, Object>() {
//                @Override
//                public void invoke(Object request, StreamObserver<Object> responseObserver) {
//
//                }
//            }));
//            ssd.addMethod(methodDescriptor,ServerCalls.asyncClientStreamingCall(new ServerCalls.ClientStreamingMethod<Object, Object>() {
//                @Override
//                public StreamObserver<Object> invoke(StreamObserver<Object> responseObserver) {
//                    return null;
//                }
//            }));
//            ssd.addMethod(methodDescriptor,ServerCalls.asyncBidiStreamingCall(new ServerCalls.BidiStreamingMethod<Object, Object>() {
//                @Override
//                public StreamObserver<Object> invoke(StreamObserver<Object> responseObserver) {
//                    return null;
//                }
//            }));
//
//        });
//
//
//        return ssd.build();
//    }
//}
