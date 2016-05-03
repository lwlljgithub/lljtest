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
	      String str = "�л����������͡��¸Һ͸����ǻ۵�ΰ�����塣";  
	  
	      //ת����Graphics2D  
	      Graphics2D g2 = (Graphics2D) gra;  
	  
	      //���ô�ӡ��ɫΪ��ɫ  
	      g2.setColor(Color.black);  
	  
	      //��ӡ�������  
	      double x = pf.getImageableX();  
	      double y = pf.getImageableY();  
	   
	      switch(pageIndex){  
	         case 0:  
	           //���ô�ӡ���壨�������ơ���ʽ�͵��С�����������ƿ�������������߼����ƣ�  
	           //Javaƽ̨���������������ϵ�У�Serif��SansSerif��Monospaced��Dialog �� DialogInput  
	           Font font = new Font("������", Font.PLAIN, 9);  
	           g2.setFont(font);//��������  
	  
	           //BasicStroke bs_3=new BasicStroke(0.5f);     
	  
	           float[] dash1 = {2.0f};   
	  
	           //���ô�ӡ�ߵ����ԡ�   
	           //1.�߿� 2��3����֪����4���հ׵Ŀ�ȣ�5�����ߵĿ�ȣ�6��ƫ����  
	           g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f));    
	           //g2.setStroke(bs_3);//�����߿� 
	           float heigth = font.getSize2D();//����߶�   
	           System.out.println("x="+x);  
	           // -1- ��Graphics2Dֱ�����   
	           //���ַ��Ļ���(���²�)λ���û��ռ��е� (x, y) λ�ô�   
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
	 * ����: ָ���ļ���ӡ
	 * ����: llj
	 * ��������:2017��3��21��
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
		// ���ô�ӡ���ݵĸ�ʽ���˴�Ϊpdf��ʽ
		DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		// ������ӡ����
		// DocAttributeSet docAttr = new HashDocAttributeSet();//�����ĵ�����
		// Doc myDoc = new SimpleDoc(psStream, psInFormat, docAttr);
		Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
		// ���ô�ӡ����
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(new Copies(1));// ��ӡ������3��
		// �������д�ӡ����
		PrintService[] services = PrintServiceLookup.lookupPrintServices(
				psInFormat, aset);
		// this step is necessary because I have several printers configured
		// �����в��ҳ����Ĵ�ӡ�����Լ���Ҫ�Ĵ�ӡ������ƥ�䣬�ҳ��Լ���Ҫ�Ĵ�ӡ��
		PrintService myPrinter = null;
		myPrinter = PrintServiceLookup.lookupDefaultPrintService(); // Ĭ�ϵĴ�ӡ��
		/*
		 * for (int i = 0; i < services.length; i++) {
		 * System.out.println("service found: " + services[i]); String svcName =
		 * services[i].toString(); if (svcName.contains("Snagit 11")) {
		 * myPrinter = services[i]; System.out.println("my printer found: " +
		 * svcName); System.out.println("my printer found: " + myPrinter);
		 * break; } }
		 */

		// ���������ӡ���ĸ�������
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
			DocPrintJob job = myPrinter.createPrintJob();// �����ĵ���ӡ��ҵ
			try {
				job.print(myDoc, aset);// ��ӡ�ĵ�
			} catch (Exception pe) {
				pe.printStackTrace();
			}
		} else {
			System.out.println("no printer services found");
		}
	}  

	
	/**
	 * 
	 * ����: ��Ĭ��ӡ
	 * ����: llj
	 * ��������:2017��3��21��
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
