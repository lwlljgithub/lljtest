package com.hhwy.iepip.report.hisdata.action;

import java.awt.BasicStroke;
import javax.print.attribute.*;  

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import com.hhwy.iepip.controls.util.FileFunction;

public class PrintReportUtils implements Printable{

	@Override
	public int print(Graphics gra, PageFormat pf, int pageIndex)
			throws PrinterException {
		  System.out.println("pageIndex="+pageIndex);   
	       Component c = null;  
	       
	      //print string    
	      String str = "中华民族是勤劳、勇敢和富有智慧的伟大民族。";  
	  
	      //转换成Graphics2D  
	      Graphics2D g2 = (Graphics2D) gra;  
	  
	      //设置打印颜色为黑色  
	      g2.setColor(Color.black);  
	  
	      //打印起点坐标  
	      double x = pf.getImageableX();  
	      double y = pf.getImageableY();  
	   
	      switch(pageIndex){  
	         case 0:  
	           //设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）  
	           //Java平台所定义的五种字体系列：Serif、SansSerif、Monospaced、Dialog 和 DialogInput  
	           Font font = new Font("新宋体", Font.PLAIN, 9);  
	           g2.setFont(font);//设置字体  
	  
	           //BasicStroke bs_3=new BasicStroke(0.5f);     
	  
	           float[] dash1 = {2.0f};   
	  
	           //设置打印线的属性。   
	           //1.线宽 2、3、不知道，4、空白的宽度，5、虚线的宽度，6、偏移量  
	           g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f));    
	           //g2.setStroke(bs_3);//设置线宽 
	           float heigth = font.getSize2D();//字体高度   
	           System.out.println("x="+x);  
	           // -1- 用Graphics2D直接输出   
	           //首字符的基线(右下部)位于用户空间中的 (x, y) 位置处   
	           //g2.drawLine(10,10,200,300);   
	           Image src = Toolkit.getDefaultToolkit().getImage("E:\\test.pdf");  
	           g2.drawImage(src,(int)x,(int)y,c);    
	           int img_Height=src.getHeight(c);  
	           int img_width=src.getWidth(c);  
	           //System.out.println("img_Height="+img_Height+"img_width="+img_width) ;  
	  
	           g2.drawString(str, (float)x, (float)y+1*heigth+img_Height);  
	           g2.drawLine((int)x,(int)(y+1*heigth+img_Height+10),(int)x+200,(int)(y+1*heigth+img_Height+10));             
	           g2.drawImage(src,(int)x,(int)(y+1*heigth+img_Height+11),c);  
	  
	                return PAGE_EXISTS;  
	  
	         default:  
	  
	             return NO_SUCH_PAGE;  
	  
	      }  

	}
	
	
	

	/**
	 * 
	 * 功能: 指定文件打印
	 * 作者: llj
	 * 创建日期:2017年3月21日
	 */
	public void print(String fileName) {  
		FileInputStream psStream = null;
		try {
			String[] fileNameArray = fileName.split("\\.");
			fileName = fileNameArray[0] + "NoGz.PDF";
			FileFunction filef = new FileFunction();
			String filePath = filef.getFileSavePathRoot();
			filePath = filePath + fileName;
			psStream = new FileInputStream(filePath);
		} catch (FileNotFoundException ffne) {
			ffne.printStackTrace();
		}
		if (psStream == null) {
			return;
		}
		// 设置打印数据的格式，此处为pdf格式
		DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		// 创建打印数据
		// DocAttributeSet docAttr = new HashDocAttributeSet();//设置文档属性
		// Doc myDoc = new SimpleDoc(psStream, psInFormat, docAttr);
		Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
		// 设置打印属性
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(new Copies(1));// 打印份数，3份
		// 查找所有打印服务
		PrintService[] services = PrintServiceLookup.lookupPrintServices(
				psInFormat, aset);
		// this step is necessary because I have several printers configured
		// 将所有查找出来的打印机与自己想要的打印机进行匹配，找出自己想要的打印机
		PrintService myPrinter = null;
		myPrinter = PrintServiceLookup.lookupDefaultPrintService(); // 默认的打印机
		/*
		 * for (int i = 0; i < services.length; i++) {
		 * System.out.println("service found: " + services[i]); String svcName =
		 * services[i].toString(); if (svcName.contains("Snagit 11")) {
		 * myPrinter = services[i]; System.out.println("my printer found: " +
		 * svcName); System.out.println("my printer found: " + myPrinter);
		 * break; } }
		 */

		// 可以输出打印机的各项属性
		AttributeSet att = myPrinter.getAttributes();
		/*
		 * for (Attribute a : att.toArray()) {
		 * 
		 * String attributeName; String attributeValue;
		 * 
		 * attributeName = a.getName(); attributeValue =
		 * att.get(a.getClass()).toString();
		 * 
		 * System.out.println(attributeName + " : " + attributeValue); }
		 */
		if (myPrinter != null) {
			DocPrintJob job = myPrinter.createPrintJob();// 创建文档打印作业
			try {
				job.print(myDoc, aset);// 打印文档
			} catch (Exception pe) {
				pe.printStackTrace();
			}
		} else {
			System.out.println("no printer services found");
		}
	}  

	
	/**
	 * 
	 * 功能: 静默打印
	 * 作者: llj
	 * 创建日期:2017年3月21日
	 */
	public void print11(String fileName){
		String [] fileNameArray=fileName.split("\\.");
		fileName=fileNameArray[0]+"NoGz.PDF";
		FileFunction filef=new FileFunction();
		String filePath=filef.getFileSavePathRoot();
		filePath=filePath+fileName;
		//String filePath="E:\\Fileupload\\"+fileName;
		try {
			Runtime.getRuntime().exec(
					"cmd.exe /C start acrord32 /h /p "
					+ filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	
	

}
