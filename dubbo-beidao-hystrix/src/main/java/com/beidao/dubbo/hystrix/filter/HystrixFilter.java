package com.beidao.dubbo.hystrix.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.beidao.dubbo.hystrix.command.DubboHystrixCommand;

/**
 *  Hystrix������
 * @author 0200759
 *
 */
@Activate(group = Constants.CONSUMER)
public class HystrixFilter implements Filter {

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		DubboHystrixCommand command = new DubboHystrixCommand(invoker, invocation);
        return (Result) command.execute();
	}

}