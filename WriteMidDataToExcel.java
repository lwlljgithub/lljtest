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
	
	/**接口数据源 **/
	private static CommonJDBCDao commonJdbcDao = BeanProvider.getCommonJDBCDao();
	
	/**
	 * @author llj
	 * 时间   2016-12-7
	 * 功能  将模板数据写入excel
	 * @param sampleId 试品ID
	 * @param clientBillId 受理ID
	 * @param filePath 文件路径全名
	 */
	public void writeMidDataToExcel(String sampleId,String clientBillId,String filePath){
		String hgqSimpleIds=Message.getMessage("hgqSimpleIds");//获取互感器试品ID集合
		String hgqSimpleIdsArray[]=hgqSimpleIds.split(",");
		List<String> hgqlist=Arrays.asList(hgqSimpleIdsArray);//数组转Li
		String dnbSimpleIds=Message.getMessage("dnbSimpleIds");//获取电能表试品ID集合
		String dnbSimpleIdsArray[]=dnbSimpleIds.split(",");
		List<String> dnblist=Arrays.asList(dnbSimpleIdsArray);//数组转Li
		WriteItOrPtDataToExcel WriteItOrPtDataToExcel=new WriteItOrPtDataToExcel();
		WriteItOrPtDataToExcel.writeCommonDataToExcel(sampleId,clientBillId,filePath);//写公共数据的提出来对每个试品都去执行写公共数据方法
		if(hgqlist.contains(sampleId)){       //处理互感器写Excel
			String oneTableToExcelList=Message.getMessage("oneTableToExcelList");//互感器向excel写一个表数据的试品ID集合
			String oneTableToExcelListArray[]=oneTableToExcelList.split(",");
			List<String> oneExcelList=Arrays.asList(oneTableToExcelListArray);//数组转Li
			String twoTableToExcelList=Message.getMessage("twoTableToExcelList");//互感器向excel写两个表数据的试品ID集合
			String twoTableToExcelListArray[]=twoTableToExcelList.split(",");
			List<String> twoExcelList=Arrays.asList(twoTableToExcelListArray);//数组转Li
			if(oneExcelList.contains(sampleId)){//向excel里面写单个表数据
				WriteItOrPtDataToExcel w=new WriteItOrPtDataToExcel();
				w.write(sampleId, clientBillId, filePath);
				//w.writeDataToExcel(sampleId,clientBillId,filePath);
			}
			if(twoExcelList.contains(sampleId)){//向excel里面写多个表数据
				WriteTwoDataToExcel writeTwoDataToExcel=new WriteTwoDataToExcel();
				//writeTwoDataToExcel.writeDataToExcel(sampleId,clientBillId,filePath);
				writeTwoDataToExcel.writeTwoExcel(sampleId, clientBillId, filePath);
			}
		}
		if(dnblist.contains(sampleId)){     //处理电能表写excel
			PfWriteToExcel.getDataWriteExcel(filePath, sampleId, clientBillId);
		}
		
		
	}
	
	
	/**
	 * @author llj
	 * 时间   2016-12-7
	 * 功能  将模板excel数据写入到中间库
	 * @param sampleId 试品ID
	 * @param clientBillId 受理ID
	 * @param filePath 文件路径全名
	 */
	public void writeMidExcelToData(String sampleId,String clientBillId,String filePath){
		String hgqSimpleIds=Message.getMessage("hgqSimpleIds");//获取互感器试品ID集合
		String hgqSimpleIdsArray[]=hgqSimpleIds.split(",");
		List<String> hgqlist=Arrays.asList(hgqSimpleIdsArray);//数组转Li
		
		String dnbSimpleIds=Message.getMessage("dnbSimpleIds");//获取电能表试品ID集合
		String dnbSimpleIdsArray[]=dnbSimpleIds.split(",");
		List<String> dnblist=Arrays.asList(dnbSimpleIdsArray);//数组转Li	
		if(hgqlist.contains(sampleId)){       //处理互感器Excel到数据库
			String oneTableToExcelList=Message.getMessage("oneTableToExcelList");//互感器向excel写一个表数据的试品ID集合
			String oneTableToExcelListArray[]=oneTableToExcelList.split(",");
			List<String> oneExcelList=Arrays.asList(oneTableToExcelListArray);//数组转Li
			String twoTableToExcelList=Message.getMessage("twoTableToExcelList");//互感器向excel写两个表数据的试品ID集合
			String twoTableToExcelListArray[]=twoTableToExcelList.split(",");
			List<String> twoExcelList=Arrays.asList(twoTableToExcelListArray);//数组转Li
			if(oneExcelList.contains(sampleId)){//向excel里面写单个表数据
				MidWriteExcelToData m=new MidWriteExcelToData();
				m.WriteHgqExcelToData(filePath,sampleId,clientBillId);
			}
			if(twoExcelList.contains(sampleId)){//向excel里面写多个表数据
				MidWriteTwoExcelToData mtt=new MidWriteTwoExcelToData();
				mtt.WriteHgqExcelToData(filePath,sampleId,clientBillId);
			}
		}
		if(dnblist.contains(sampleId)){     //处理电能表写excel
			InterUtils.getReadExcel(filePath, sampleId, clientBillId);
		}
		
		
	}
	
	
	 /** 
	 * 功能: 综合结论公共表数据保存
	 * 作者: llj
	 * 创建日期:2016年12月8日
	 * @param map 读取excel整个数据集合
	 * @param taskId 受理ID
	 *//*
	public  void writeCommonExcelToData(HashMap map,String taskId){
		List<String> list = new ArrayList<String>();//公共部分数据区域集合
		list.add("at");//温度：at
		list.add("dampness");//湿度：dampness
		list.add("address");//地址：address
		list.add("inspectionUnit");//送检单位：inspectionUnit 
		list.add("jlqjName");//计量器具名称：jlqjName  被校/检仪器名称
		list.add("model");//型号：model
		list.add("factoryName");//出厂编号：factoryName
		list.add("VerificationBasis");//检定依据：VerificationBasis
		list.add("VerificationConclusion");//检定结论：VerificationConclusion
		list.add("VerificationDate");//检定日期：VerificationDate
		list.add("experiment_date");//实验有效日期：experiment_date
		//将读取到的数据分装到一个list中
		List<List<Object[]>> listData = new ArrayList<List<Object[]>>();
		//读取excel
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
	 * 功能：Excel模板工具数据提取
	 * 作者：LiuYaoQiang
	 * 创建日期:2016年12月7日
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
