package com.giousa.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String>{

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, String arg1) throws Exception {
		
		System.out.println("ServerReceived：服务端获取信息："+arg1);
;		
//		arg0.channel().writeAndFlush("服务端发送消息到客户端："+arg1);
		
		arg0.writeAndFlush("服务端发送消息到客户端："+arg1);
	}

	
}
