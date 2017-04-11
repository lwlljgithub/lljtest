package com.hhwy.iepip.cqdky.mid;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.hhwy.iepip.cqdky.inter.utils.InterUtils;
import com.hhwy.iepip.cqdky.inter.utils.PfWriteToExcel;
import com.hhwy.iepip.cqdky.util.MidWriteExcelToData;
import com.hhwy.iepip.cqdky.util.MidWriteTwoExcelToData;
import com.hhwy.iepip.cqdky.util.WriteItOrPtDataToExcel;
import com.hhwy.iepip.cqdky.util.WriteTwoDataToExcel;
import com.hhwy.iepip.framework.container.BeanProvider;
import com.hhwy.iepip.framework.message.Message;
import com.hhwy.iepip.framework.persistent.CommonJDBCDao;

public class WriteMidDataToExcel {
	
	/**�ӿ�����Դ **/
	private static CommonJDBCDao commonJdbcDao = BeanProvider.getCommonJDBCDao();
	
	/**
	 * @author llj
	 * ʱ��   2016-12-7
	 * ����  ��ģ������д��excel
	 * @param sampleId ��ƷID
	 * @param clientBillId ����ID
	 * @param filePath �ļ�·��ȫ��
	 */
	public void writeMidDataToExcel(String sampleId,String clientBillId,String filePath){
		String hgqSimpleIds=Message.getMessage("hgqSimpleIds");//��ȡ��������ƷID����
		String hgqSimpleIdsArray[]=hgqSimpleIds.split(",");
		List<String> hgqlist=Arrays.asList(hgqSimpleIdsArray);//����תLi
		String dnbSimpleIds=Message.getMessage("dnbSimpleIds");//��ȡ���ܱ���ƷID����
		String dnbSimpleIdsArray[]=dnbSimpleIds.split(",");
		List<String> dnblist=Arrays.asList(dnbSimpleIdsArray);//����תLi
		WriteItOrPtDataToExcel WriteItOrPtDataToExcel=new WriteItOrPtDataToExcel();
		WriteItOrPtDataToExcel.writeCommonDataToExcel(sampleId,clientBillId,filePath);//д�������ݵ��������ÿ����Ʒ��ȥִ��д�������ݷ���
		if(hgqlist.contains(sampleId)){       //��������дExcel
			String oneTableToExcelList=Message.getMessage("oneTableToExcelList");//��������excelдһ�������ݵ���ƷID����
			String oneTableToExcelListArray[]=oneTableToExcelList.split(",");
			List<String> oneExcelList=Arrays.asList(oneTableToExcelListArray);//����תLi
			String twoTableToExcelList=Message.getMessage("twoTableToExcelList");//��������excelд���������ݵ���ƷID����
			String twoTableToExcelListArray[]=twoTableToExcelList.split(",");
			List<String> twoExcelList=Arrays.asList(twoTableToExcelListArray);//����תLi
			if(oneExcelList.contains(sampleId)){//��excel����д����������
				WriteItOrPtDataToExcel w=new WriteItOrPtDataToExcel();
				w.write(sampleId, clientBillId, filePath);
				//w.writeDataToExcel(sampleId,clientBillId,filePath);
			}
			if(twoExcelList.contains(sampleId)){//��excel����д���������
				WriteTwoDataToExcel writeTwoDataToExcel=new WriteTwoDataToExcel();
				//writeTwoDataToExcel.writeDataToExcel(sampleId,clientBillId,filePath);
				writeTwoDataToExcel.writeTwoExcel(sampleId, clientBillId, filePath);
			}
		}
		if(dnblist.contains(sampleId)){     //������ܱ�дexcel
			PfWriteToExcel.getDataWriteExcel(filePath, sampleId, clientBillId);
		}
		
		
	}
	
	
	/**
	 * @author llj
	 * ʱ��   2016-12-7
	 * ����  ��ģ��excel����д�뵽�м��
	 * @param sampleId ��ƷID
	 * @param clientBillId ����ID
	 * @param filePath �ļ�·��ȫ��
	 */
	public void writeMidExcelToData(String sampleId,String clientBillId,String filePath){
		String hgqSimpleIds=Message.getMessage("hgqSimpleIds");//��ȡ��������ƷID����
		String hgqSimpleIdsArray[]=hgqSimpleIds.split(",");
		List<String> hgqlist=Arrays.asList(hgqSimpleIdsArray);//����תLi
		
		String dnbSimpleIds=Message.getMessage("dnbSimpleIds");//��ȡ���ܱ���ƷID����
		String dnbSimpleIdsArray[]=dnbSimpleIds.split(",");
		List<String> dnblist=Arrays.asList(dnbSimpleIdsArray);//����תLi	
		if(hgqlist.contains(sampleId)){       //��������Excel�����ݿ�
			String oneTableToExcelList=Message.getMessage("oneTableToExcelList");//��������excelдһ�������ݵ���ƷID����
			String oneTableToExcelListArray[]=oneTableToExcelList.split(",");
			List<String> oneExcelList=Arrays.asList(oneTableToExcelListArray);//����תLi
			String twoTableToExcelList=Message.getMessage("twoTableToExcelList");//��������excelд���������ݵ���ƷID����
			String twoTableToExcelListArray[]=twoTableToExcelList.split(",");
			List<String> twoExcelList=Arrays.asList(twoTableToExcelListArray);//����תLi
			if(oneExcelList.contains(sampleId)){//��excel����д����������
				MidWriteExcelToData m=new MidWriteExcelToData();
				m.WriteHgqExcelToData(filePath,sampleId,clientBillId);
			}
			if(twoExcelList.contains(sampleId)){//��excel����д���������
				MidWriteTwoExcelToData mtt=new MidWriteTwoExcelToData();
				mtt.WriteHgqExcelToData(filePath,sampleId,clientBillId);
			}
		}
		if(dnblist.contains(sampleId)){     //������ܱ�дexcel
			InterUtils.getReadExcel(filePath, sampleId, clientBillId);
		}
		
		
	}
	
	
	 /** 
	 * ����: �ۺϽ��۹��������ݱ���
	 * ����: llj
	 * ��������:2016��12��8��
	 * @param map ��ȡexcel�������ݼ���
	 * @param taskId ����ID
	 *//*
	public  void writeCommonExcelToData(HashMap map,String taskId){
		List<String> list = new ArrayList<String>();//���������������򼯺�
		list.add("at");//�¶ȣ�at
		list.add("dampness");//ʪ�ȣ�dampness
		list.add("address");//��ַ��address
		list.add("inspectionUnit");//�ͼ쵥λ��inspectionUnit 
		list.add("jlqjName");//�����������ƣ�jlqjName  ��У/����������
		list.add("model");//�ͺţ�model
		list.add("factoryName");//������ţ�factoryName
		list.add("VerificationBasis");//�춨���ݣ�VerificationBasis
		list.add("VerificationConclusion");//�춨���ۣ�VerificationConclusion
		list.add("VerificationDate");//�춨���ڣ�VerificationDate
		list.add("experiment_date");//ʵ����Ч���ڣ�experiment_date
		//����ȡ�������ݷ�װ��һ��list��
		List<List<Object[]>> listData = new ArrayList<List<Object[]>>();
		//��ȡexcel
		if(map.size()>0){
			Iterator it = list.iterator();
			while(it.hasNext()){
				List<Object[]> obj = (List<Object[]>) map.get((it.next()).toString());
				listData.add(obj);				
			}
		}
		AnalyzeData(listData,taskId);
	}
	
	*//**
	 * ���ܣ�Excelģ�幤��������ȡ
	 * ���ߣ�LiuYaoQiang
	 * ��������:2016��12��7��
	 * @param list
	 *//*
	public static void AnalyzeData(List list,String taskId){
		String str = "";
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List<Object[]> obj = (List<Object[]>) list.get(i);
				for (int j = 0; j < obj.size(); j++) {
					Object[] object = obj.get(j);
					for (int h = 0; h < object.length; h++) {
						if (object[h] == "") {
							object[h] = "-";
						}
						str = str + "," + object[h];
					}
				}
			}
		}
		String strExcel = str.substring(1, str.length());
		String[] strs = strExcel.split(",");
		String sql = "INSERT INTO t_ib_multiple_conclusion(ID, AMBIENT_TEMPERATURE, HUMIDITY, PLACE, INSPECTION_UNIT, "
				+ "INSTRUMENT_NAME, MODEL, FACTORY_NUMBER,CALIBRATION_BASIS, VERIFACTION_CONCLUSION, VERIFIED_DATE, VALIDITY_TERM,TASK_ID) "
				+ "VALUES(INTER_ID_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(strs[9]);
			date2 = sdf.parse(strs[10]);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			commonJdbcDao.execute(sql, new Object[] { strs[0], strs[1],
					strs[2], strs[3], strs[4], strs[5], strs[6], strs[7],
					strs[8], date1, date2, taskId });
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
