package com.beidao.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

@Activate(group = Constants.CONSUMER)
public class MyDubboFilterConsumer implements Filter {

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		System.out.println("add MyDubboFilterConsumer successfully!");
		//to do what you want
		return invoker.invoke(invocation);
	}

}
