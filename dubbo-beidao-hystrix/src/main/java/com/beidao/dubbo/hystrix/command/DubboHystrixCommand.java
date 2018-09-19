package com.beidao.dubbo.hystrix.command;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
 
/**
 * Hystrix����
 * @author 0200759
 *
 */
public class DubboHystrixCommand extends HystrixCommand<Result> {
 
    private static Logger    logger                       = LoggerFactory.getLogger(DubboHystrixCommand.class);
    private static final int DEFAULT_THREADPOOL_CORE_SIZE = 30;
    private Invoker<?>       invoker;
    private Invocation       invocation;
    
    public DubboHystrixCommand(Invoker<?> invoker,Invocation invocation){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getInterface().getName()))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(String.format("%s_%d", invocation.getMethodName(),
                                                                                 invocation.getArguments() == null ? 0 : invocation.getArguments().length)))
              .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                            .withCircuitBreakerRequestVolumeThreshold(20)//10����������19������ʧ�ܣ��۶����ŷ���������
                                            .withCircuitBreakerSleepWindowInMilliseconds(30000)//�۶����ж�����30���������״̬,�Ų���������ȥ����
                                            .withCircuitBreakerErrorThresholdPercentage(50)//�����ʴﵽ50�����۶ϱ���
                                            .withExecutionTimeoutEnabled(false))//ʹ��dubbo�ĳ�ʱ����������ĳ�ʱ
              .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(getThreadPoolCoreSize(invoker.getUrl()))));//�̳߳�Ϊ30
       
        
        this.invoker=invoker;
        this.invocation=invocation;
    }
    
    /**
     * ��ȡ�̳߳ش�С
     * 
     * @param url
     * @return
     */
    private static int getThreadPoolCoreSize(URL url) {
        if (url != null) {
            int size = url.getParameter("ThreadPoolCoreSize", DEFAULT_THREADPOOL_CORE_SIZE);
            if (logger.isDebugEnabled()) {
                logger.debug("ThreadPoolCoreSize:" + size);
            }
            return size;
        }
 
        return DEFAULT_THREADPOOL_CORE_SIZE;
 
    }
 
    @Override
    protected Result run() throws Exception {
        return invoker.invoke(invocation);
    }
    
}

