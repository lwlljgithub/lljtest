package com.px;

public class Sell implements Runnable{
	
	private static int m=20;
	
	private Object o="obj";

	@Override
	public void run() {
		while(m>0){
			synchronized (o) {
				if(m>0){
					System.out.println(Thread.currentThread().getName()+"卖了第"+m+"张票 ");
					m--;
				}else{
					System.out.println("没票了");
				}
				
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Sell se1=new Sell();
		Sell se2=new Sell();
		Thread t1=new Thread(se1);
		Thread t2=new Thread(se2);
		t1.start();
		t2.start();
		
	}
	
	
	

}
