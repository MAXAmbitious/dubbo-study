package com.beidao.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

@Activate(group = Constants.PROVIDER, order = -1000000)
public class MyDubboFilterProvider implements Filter {

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		System.out.println("add MyDubboFilterProvider successfully!");
		//to do what you want
		return invoker.invoke(invocation);
	}
}
