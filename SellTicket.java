package com.mq;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellTicket extends Thread{
	
	
	private static int tickets=20;
	
	static Object ob="ob";
    
	public SellTicket(String name){
		super(name);
		
	}
	static {
		System.out.println("aaa");
	}
	{System.out.println("ss");}
	
	@Override
	public void run() {
		
		while(tickets>0){
			synchronized (ob) {
				if(tickets>0){
					System.out.println(getName()+"卖了第"+tickets+" 张票");
					tickets--;
				}else{
					System.out.println("没票了！");
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		SellTicket SellTicket1=new SellTicket("窗口1");
		SellTicket SellTicket2=new SellTicket("窗口2");
		SellTicket SellTicket3=new SellTicket("窗口3");
		SellTicket1.start();
		SellTicket2.start();
		SellTicket3.start();
		
		Map ss=new HashMap();
		List ss1=new ArrayList();
		FileNotFoundException ss11 =new FileNotFoundException();
		String str=new String("goor");
		SellTicket sell=new SellTicket(str);
		sell.change(str);
		System.out.println(str);

	}
	
	public void change(String str){
		//String str=new String("goor");
		str="good";
	}
	
	

}
