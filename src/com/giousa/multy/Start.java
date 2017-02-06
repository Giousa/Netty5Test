package com.giousa.multy;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Start {
	
	public static void main(String[] args) {
		
		MultyClient multyClient = new MultyClient();
		multyClient.init(5);
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			try {
				System.out.println("请输入：");
				String msg = bufferedReader.readLine();
				multyClient.nextChannel().writeAndFlush(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
