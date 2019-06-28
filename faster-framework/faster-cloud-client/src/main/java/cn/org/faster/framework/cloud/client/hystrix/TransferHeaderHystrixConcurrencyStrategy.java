package cn.org.faster.framework.cloud.client.hystrix;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangbowen
 * @since 2019-06-20
 */
@Slf4j
public class TransferHeaderHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {
    private HystrixConcurrencyStrategy delegate;

    public TransferHeaderHystrixConcurrencyStrategy() {
        try {
            this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();
            if (this.delegate instanceof TransferHeaderHystrixConcurrencyStrategy) {
                return;
            }
            HystrixCommandExecutionHook commandExecutionHook =
                    HystrixPlugins.getInstance().getCommandExecutionHook();
            HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
            HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
            HystrixPropertiesStrategy propertiesStrategy =
                    HystrixPlugins.getInstance().getPropertiesStrategy();
            this.logCurrentStateOfHystrixPlugins(eventNotifier, metricsPublisher, propertiesStrategy);
            HystrixPlugins.reset();
            HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
            HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
        } catch (Exception e) {
            log.error("Failed to register Sleuth Hystrix Concurrency Strategy", e);
        }
    }

    private void logCurrentStateOfHystrixPlugins(HystrixEventNotifier eventNotifier,
                                                 HystrixMetricsPublisher metricsPublisher, HystrixPropertiesStrategy propertiesStrategy) {
        if (log.isDebugEnabled()) {
            log.debug("Current Hystrix plugins configuration is [" + "concurrencyStrategy ["
                    + this.delegate + "]," + "eventNotifier [" + eventNotifier + "]," + "metricPublisher ["
                    + metricsPublisher + "]," + "propertiesStrategy [" + propertiesStrategy + "]," + "]");
            log.debug("Registering Sleuth Hystrix Concurrency Strategy.");
        }
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return delegate != null
                ? delegate.getBlockingQueue(maxQueueSize)
                : super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(
            HystrixRequestVariableLifecycle<T> rv) {
        return delegate != null
                ? delegate.getRequestVariable(rv)
                : super.getRequestVariable(rv);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return delegate != null
                ? delegate.getThreadPool(threadPoolKey, corePoolSize,
                maximumPoolSize, keepAliveTime, unit, workQueue)
                : super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
                keepAliveTime, unit, workQueue);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixThreadPoolProperties threadPoolProperties) {
        return delegate != null
                ? delegate.getThreadPool(threadPoolKey,
                threadPoolProperties)
                : super.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {

        if (callable instanceof TransferCallable) {
            return callable;
        }
        Callable<T> wrappedCallable = this.delegate != null
                ? this.delegate.wrapCallable(callable) : callable;
        if (wrappedCallable instanceof TransferCallable) {
            return wrappedCallable;
        }
        //将当前请求信息拿到，放入包装类所在线程中
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return new TransferCallable<>(wrappedCallable, requestAttributes);
    }


    static class TransferCallable<T> implements Callable<T> {
        private final Callable<T> delegate;

        private final RequestAttributes requestAttributes;


        public TransferCallable(Callable<T> delegate, RequestAttributes requestAttributes) {
            this.requestAttributes = requestAttributes;
            this.delegate = delegate;
        }

        @Override
        public T call() throws Exception {
            try {
                //将当前请求信息拿到，放入包装类所在线程中
                RequestContextHolder.setRequestAttributes(requestAttributes);
                return delegate.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }

}
