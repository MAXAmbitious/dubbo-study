package com.beidao.dubbo.cluster;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Cluster;
import com.alibaba.dubbo.rpc.cluster.Directory;

/**
 * @author 0200759
 * 自定义集群
 *
 */
public class BeiDaoDubboCluster implements Cluster{

	public final static String Name = "beidaoCluster";

	public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
		return new BeiDaoDubboClusterInvoker<T>(directory);
	}
	
	
}
