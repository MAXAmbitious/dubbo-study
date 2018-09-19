package com.beidao.dubbo.cluster;

import java.util.List;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Directory;
import com.alibaba.dubbo.rpc.cluster.LoadBalance;
import com.alibaba.dubbo.rpc.cluster.support.AbstractClusterInvoker;
import com.alibaba.dubbo.rpc.cluster.support.FailfastClusterInvoker;
import com.alibaba.dubbo.rpc.cluster.support.FailoverClusterInvoker;

/**
 * 集群策略(根据判定读写分配不同的策略)
 * @author 0200759
 *
 */
public class BeiDaoDubboClusterInvoker<T> extends AbstractClusterInvoker<T>{
	//定义写操作方法前缀
	private final static String[] WRITE_PREFFIX_ARRAY = new String[] { "SAVE", "ADD", "INSERT", "DEL", "UPDATE" };
	private static final Logger logger = LoggerFactory.getLogger(BeiDaoDubboClusterInvoker.class);
	
	private Directory<T> directory;

	public BeiDaoDubboClusterInvoker(Directory<T> directory) {
		super(directory);
		this.directory = directory;
	}

	@Override
	protected Result doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance)
			throws RpcException {
		
		String methodName = invocation.getMethodName().toUpperCase();
		boolean write = checkMethod(methodName);
		if(write){
			logger.info(methodName + " method is excuting cluster for writing operation");
			return new FailfastClusterInvoker<T>(directory).doInvoke(invocation, invokers, loadbalance);
		}else{
			logger.info(methodName + " method is excuting cluster for reading operation");
			return new FailoverClusterInvoker<T>(directory).doInvoke(invocation, invokers, loadbalance);
		}
	}

	/**
	 * 检查是否为写操作
	 * @param methodName
	 * @return
	 */
	private boolean checkMethod(String methodName) {
		for(String writePreffix : WRITE_PREFFIX_ARRAY){
			if(methodName.startsWith(writePreffix)){
				return true;
			}
		}
		return false;
	}
	
}
