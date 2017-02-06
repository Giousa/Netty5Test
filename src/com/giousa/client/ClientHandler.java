package com.giousa.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, String arg1) throws Exception {
		System.out.println("Client收到消息:"+arg1);
		
	}

}
