package cn.org.faster.framework.grpc.client.proxy;

import cn.org.faster.framework.core.utils.Utils;
import cn.org.faster.framework.grpc.annotation.GrpcMethod;
import cn.org.faster.framework.grpc.client.model.ChannelProperty;
import cn.org.faster.framework.grpc.client.model.MethodCallProperty;
import cn.org.faster.framework.grpc.exception.GrpcMethodNoMatchException;
import cn.org.faster.framework.grpc.marshaller.FastJsonMarshaller;
import io.grpc.*;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author zhangbowen
 * @since 2019/1/14
 */
public class ManageChannelProxy implements InvocationHandler {
    private ManagedChannel channel;
    private Map<String, ClientCall<Object, Object>> clientCallMap = new HashMap<>();

    public ManageChannelProxy(ChannelProperty channelProperty) {
        this.channel = ManagedChannelBuilder.forAddress(channelProperty.getHost(), channelProperty.getPort())
                .usePlaintext()
                .build();
    }


    private FastJsonMarshaller parseReturnMarshaller(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        Type[] types;
        switch (methodCallProperty.getMethodType()) {
            case UNARY:
                if (method.getReturnType() == ListenableFuture.class) {
                    //等于ClientCalls.futureUnaryCall()
                    //获取ListenableFuture的泛型
                    types = Utils.reflectMethodReturnTypes(method);
                    return new FastJsonMarshaller(types[0]);
                } else if (method.getReturnType() == Void.class) {
                    //等于ClientCalls.asyncUnaryCall();
                    //参数中为StreamObserver的泛型
                    types = Utils.reflectMethodParameterTypes(method, StreamObserver.class);
                    //说明参数中不含有StreamObserver参数
                    if (types == null) {
                        throw new GrpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                                "You should use [StreamObserver] for your param value.Please check it.");
                    }
                    return new FastJsonMarshaller(types[0]);
                }
                //直接返回方法的返回类型，等于ClientCalls.blockingUnaryCall
                return new FastJsonMarshaller(method.getGenericReturnType());
            case BIDI_STREAMING://双向流，相当于asyncBidiStreamingCall
                //判断方法返回类型是否为StreamObserver（此为客户端传输数据所用，服务端响应在参数的StreamObserver中）
                if (method.getReturnType() != StreamObserver.class) {
                    throw new GrpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your return value.Please check it.");
                }
                //检验参数是否为StreamObserver，获取服务端响应泛型
                types = Utils.reflectMethodParameterTypes(method, StreamObserver.class);
                if (types == null) {
                    throw new GrpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your param value.Please check it.");
                }
                //获取返回类型的泛型
                types = Utils.reflectMethodReturnTypes(method);
                return new FastJsonMarshaller(types[0]);
            case CLIENT_STREAMING: //客户端流。等于ClientCalls.asyncClientStreamingCall()
                //判断方法返回类型是否为StreamObserver（此为客户端传输数据所用，服务端响应在参数的StreamObserver中）
                if (method.getReturnType() != StreamObserver.class) {
                    throw new GrpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your return value.Please check it.");
                }
                //检验参数是否为StreamObserver，获取服务端响应泛型
                types = Utils.reflectMethodParameterTypes(method, StreamObserver.class);
                if (types == null) {
                    throw new GrpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your param value.Please check it.");
                }
                //获取返回类型的泛型
                types = Utils.reflectMethodReturnTypes(method);
                return new FastJsonMarshaller(types[0]);
            case SERVER_STREAMING://等于ClientCalls.blockingServerStreamingCall
                if (method.getReturnType() != Iterator.class) {
                    throw new GrpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [Iterator] for your return value.Please check it.");
                }
                //获取返回类型的泛型
                types = Utils.reflectMethodReturnTypes(method);
                return new FastJsonMarshaller(types[0]);
        }
        throw new GrpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                "Not found return value type.Please check your configuration.");
    }


    public void addCall(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        MethodDescriptor.Builder<Object, Object> builder = MethodDescriptor.newBuilder(
                new FastJsonMarshaller(),
                parseReturnMarshaller(methodCallProperty))
                .setType(methodCallProperty.getMethodType())
                .setFullMethodName(MethodDescriptor.generateFullMethodName(methodCallProperty.getScheme(), methodCallProperty.getMethodName()));
        clientCallMap.put(method.getName(), channel.newCall(builder.build(), CallOptions.DEFAULT));
    }

    /**
     * 从多个参数中获取StreamObserver
     *
     * @param args 参数
     * @return StreamObserver
     */
    @SuppressWarnings("unchecked")
    private StreamObserver<Object> getStreamObserverFromArgs(List<Object> args) {
        for (int i = args.size() - 1; i > -1; i--) {
            Object streamObserver = args.get(i);
            if (streamObserver.getClass() == StreamObserver.class) {
                args.remove(i);
                return (StreamObserver) streamObserver;
            }
        }
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        GrpcMethod grpcMethod = method.getAnnotation(GrpcMethod.class);
        String annotationMethodName = grpcMethod.value();
        MethodCallProperty methodCallProperty = new MethodCallProperty();
        methodCallProperty.setMethodName(StringUtils.isEmpty(annotationMethodName) ? method.getName() : annotationMethodName);
        methodCallProperty.setMethodType(grpcMethod.type());
        ClientCall<Object, Object> clientCall = this.clientCallMap.get(method.getName());
        List<Object> argsList = Arrays.asList(args);
        switch (methodCallProperty.getMethodType()) {
            case UNARY:
                if (method.getReturnType() == ListenableFuture.class) { //等于ClientCalls.futureUnaryCall()
                    return ClientCalls.futureUnaryCall(clientCall, args[0]);
                } else if (method.getReturnType() == Void.class) { //等于ClientCalls.asyncUnaryCall();
                    StreamObserver<Object> streamObserver = getStreamObserverFromArgs(argsList);
                    ClientCalls.asyncUnaryCall(clientCall, argsList.get(0), streamObserver);
                    return null;
                }
                return ClientCalls.blockingUnaryCall(clientCall, args[0]);
            case BIDI_STREAMING://双向流，相当于asyncBidiStreamingCall
                //获取返回类型的泛型
                return ClientCalls.asyncBidiStreamingCall(clientCall, getStreamObserverFromArgs(argsList));
            case CLIENT_STREAMING: //客户端流。等于ClientCalls.asyncClientStreamingCall()
                return ClientCalls.asyncClientStreamingCall(clientCall, getStreamObserverFromArgs(argsList));
            case SERVER_STREAMING://等于ClientCalls.blockingServerStreamingCall
                return ClientCalls.blockingServerStreamingCall(clientCall, args[0]);
        }
        return null;
    }

}
