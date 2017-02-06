package com.giousa.multy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.giousa.client.ClientHandler;

import io.netty.bootstrap.Bootstrap;

/**
 * 单客户端多连接程序
 * @author zhangmengmeng
 *
 */
public class MultyClient {
	
	
	/**
	 * 服务类
	 */
	private Bootstrap bootstrap = new Bootstrap();
	
	/**
	 * 会话
	 */
	private List<Channel> channelList = new ArrayList<>();
	
	/**
	 * 引用计数
	 */
	private final AtomicInteger index = new AtomicInteger();
	
	/**
	 * 初始化
	 * @param count
	 */
	public void init(int count){
		NioEventLoopGroup worker = new NioEventLoopGroup();
		// 设置线程池
		bootstrap.group(worker);

		// 设置socket工厂
		bootstrap.channel(NioSocketChannel.class);

		// 设置管道
		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new StringDecoder());
				ch.pipeline().addLast(new StringEncoder());
				ch.pipeline().addLast(new ClientHandler());

			}
		});
		
		// 连接服务器
		for (int i = 0; i < count; i++) {
			ChannelFuture future = bootstrap.connect("192.168.1.102", 7788);
			channelList.add(future.channel());
		}
		
	}
	
	
	/**
	 * 获取会话
	 * @return
	 */
	public Channel nextChannel(){
		return getFirstActiveChannel(0);
	}
	
	private Channel getFirstActiveChannel(int count){
		Channel channel = channelList.get(Math.abs(index.getAndIncrement() % channelList.size()));
		if(!channel.isActive()){
			//重连
			reconnect(channel);
			
			if(count >= channelList.size()){
				throw new RuntimeException("no can use channel");
			}
			return getFirstActiveChannel(count++);
		}
		return channel;
	}
	
	private void reconnect(Channel channel){
		synchronized (channel) {
			if(channelList.indexOf(channel) == -1){
				return;
			}
			Channel newChannel = bootstrap.connect("127.0.0.1", 7788).channel();
			channelList.set(channelList.indexOf(channel), newChannel);
		}
	}
}
