package com.hhwy.iepip.cqdky.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;

import persistent.base.TCqdkyBaseClientmanageInfo;
import persistent.entm.TCqdkyEntmClientbillDel;

import com.hhwy.iepip.cqdky.detm.utils.ConvertUtils;
import com.hhwy.iepip.framework.container.BeanProvider;
import com.hhwy.iepip.framework.persistent.CommonJDBCDao;
import com.hhwy.iepip.framework.persistent.dao.CommonDao;
import com.hhwy.iepip.framework.persistent.dao.JDBCCommonDao;

public class WriteItOrPtDataToExcel {
	
	/** 接口数据源 **/
	private static CommonJDBCDao commonJdbcDao = BeanProvider
			.getCommonJDBCDao();
	
	/*通过hibernateDao层处理对象*/
	public CommonDao dao = BeanProvider.getCommonDao();
	/** 通用jdbcDao层处理对象 */
	private static JDBCCommonDao jdbcDao = BeanProvider.getJDBCCommonDao();

	public static void main(String[] args) {
		writeDataToExcel("","","D:/10kv电流互感器检定证书1.xls");
	}
	
	public void write(String sampleId,String clientBillId, String filePath){
		writeDataToExcel(sampleId,clientBillId,filePath);
		//writeCommonDataToExcel(sampleId,clientBillId,filePath);
	}

	/**
	 * 功能将向excel里面写数据入口 作者llj 创建时间 2016-11-24
	 * @param sampleId 试品ID
	 * @param slId 受理委托单ID
	 * @param filePath
	 *            文件路径
	 */
	public static void writeDataToExcel(String sampleId,String clientBillId, String filePath) {
		File inputFile = new File(filePath);
		int sheetNum = 0;// 第几个sheet，excel中sheet，行列下标均是从0开始
		// 先行后列然后数据区域
		List list = new ArrayList<Object>();
		list.add("rowCoordinate");
		list.add("colCoordinate");
		list.add("data");
		list.add("lxData");
		list.add("secondLoad");
		list.add("secondLoadCos");
		// 读取excel
		writeExcel(inputFile, sheetNum, list, filePath,clientBillId,sampleId,false);
		System.out.println("写数据成功");

	}
	
	/**
	 * 功能将向excel里面写公共数据入口
	 * 作者llj
	 * 创建时间 2016-12-8
	 * @param filePath 文件路径
	 */
	public static void writeCommonDataToExcel(String sampleId,String clientBillId,String filePath) {
		File inputFile = new File(filePath);
		int sheetNum = 0;// 第几个sheet，excel中sheet，行列下标均是从0开始
		// 先行后列然后数据区域
		List list = new ArrayList<Object>();
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
		// 读取excel
		writeExcel(inputFile, sheetNum, list, filePath,clientBillId,sampleId,true);
		System.out.println("写公共数据成功!");

	}

	/**
	 * 功能 封装要写的数据为Map,这里正式功能时，封装mds传过来的数据 作者llj 创建时间 2016-11-24
	 * @param clientBillId 受理ID
	 * @return map
	 */
	public static Map<String, Object> dataToMap(String clientBillId,String sampleId) {
		Map<String, Object> map = new HashMap<String, Object>();// 封裝所有的数据区域
		List<Object[]> data = new ArrayList<Object[]>();// 测试写数据data
		List<Object[]> colCoordinate = new ArrayList<Object[]>();// 测试写数据data
		List<Object[]> lxData = new ArrayList<Object[]>();// lxData
		List<Object[]> secondLoad = new ArrayList<Object[]>();// 二次負荷
		List<Map<String, Object>> listLx = null;
		List list = null;
		//clientBillId="123";
		String tableName="T_IB_ERROR_IT_CONC T";
		List sampleIds=new ArrayList();
		if(sampleIds.contains(sampleId)){//根据试品找数据库表
			tableName="T_IB_ERROR_PT_CONC T";
		}
		String sqlLx = "SELECT T.AMOUNT_RANGE FROM  "
				+tableName
				+ " WHERE T.TASK_ID = "+"'"+clientBillId+"'"+" AND T.DELETE_FLAG = '0' "
				+ " GROUP BY T.AMOUNT_RANGE";
		try {
			listLx = commonJdbcDao.query(sqlLx, null);
			for (int m = 0; m < listLx.size(); m++) {
				String sql = "SELECT T.TWO_MEET_VA  FROM  "
				+tableName
				+ " WHERE T.TASK_ID = "+"'"+clientBillId+"'"+" AND T.DELETE_FLAG = '0' "
				+ " AND T.AMOUNT_RANGE= " + "'"
				+ listLx.get(m).get("AMOUNT_RANGE").toString() + "'"
				+ " GROUP BY T.TWO_MEET_VA";// 再取出二次负荷va
				list = commonJdbcDao.query(sql, null);
			 if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
				Map mapCol = (Map) list.get(i);
				String loadCurrent = ConvertUtils.getStr(mapCol
								.get("TWO_MEET_VA"));
				String sqlData = "select distinct m.* from(  select GetDictName(t.error) error,t.two_meet_va,t.AMOUNT_RANGE,"
				+ " wmsys.wm_concat(decode(TO_CHAR(T.ERROR_DATA),null,'/',' ','/',TO_CHAR(T.ERROR_DATA))) over (partition by error) errorData from  "
				+tableName
				+ " WHERE T.TWO_MEET_VA =? AND T.TASK_ID=? AND T.Delete_Flag=0  and T.AMOUNT_RANGE="
				+ "'"
				+ listLx.get(m).get("AMOUNT_RANGE").toString()
				+ "'" + " order by to_number(t.sort) asc ) m";
				List listdata = commonJdbcDao.query(sqlData,
				new Object[] { loadCurrent,clientBillId });
				for (int j = 0; j < listdata.size(); j++) {
				Map mapData = (Map) listdata.get(j);
				String strData = ConvertUtils.getStr(mapData
									.get("ERRORDATA"));
				String[] str = strData.split(",");
				data.add(str);
				colCoordinate.add(new Object[] { ConvertUtils
									.getStr(mapData.get("ERROR")) });
				lxData.add(new Object[] { ConvertUtils
									.getStr(mapData.get("AMOUNT_RANGE")) });
				secondLoad.add(new Object[] { ConvertUtils
						.getStr(mapData.get("TWO_MEET_VA")) });
						}
					}
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("data", data);
		map.put("colCoordinate", colCoordinate);
		map.put("lxData", lxData);
		map.put("secondLoad", secondLoad);
		return map;
	}
	
	
	/**
	 * 功能 模板公共类数据的封装
	 * 作者: LiuYaoQiang
	 * 创建时间 2016-12-7
	 * @return map 
	 */
	public static Map<String,Object> dataToMapMultCon(List list,String sampleId,String taskId){
		Map<String,Object> map=new HashMap<String,Object>();//封裝所有的数据区域
		List<Object[]> at = new ArrayList<Object[]>();//温度：at
		List<Object[]> dampness = new ArrayList<Object[]>();//湿度：dampness
		List<Object[]> address = new ArrayList<Object[]>();//地址：address
		List<Object[]> inspectionUnit = new ArrayList<Object[]>();//送检单位：inspectionUnit 
		List<Object[]> jlqjName = new ArrayList<Object[]>();//计量器具名称：jlqjName  被校/检仪器名称
		List<Object[]> model = new ArrayList<Object[]>();//型号：model
		List<Object[]> factoryName = new ArrayList<Object[]>();//出厂编号：factoryName
		List<Object[]> VerificationBasis = new ArrayList<Object[]>();//检定依据：VerificationBasis
		List<Object[]> VerificationConclusion = new ArrayList<Object[]>();//检定结论：VerificationConclusion
		List<Object[]> VerificationDate = new ArrayList<Object[]>();//检定日期：VerificationDate
		List<Object[]> experiment_date = new ArrayList<Object[]>();//实验有效日期：experiment_date
		List listData = null;
		String sql = "SELECT ID,AMBIENT_TEMPERATURE,HUMIDITY,PLACE,INSPECTION_UNIT,INSTRUMENT_NAME,MODEL,FACTORY_NUMBER,CALIBRATION_BASIS, "
				+ "VERIFACTION_CONCLUSION,VERIFIED_DATE,VALIDITY_TERM FROM T_IB_MULTIPLE_CONCLUSION T  "
				+ "WHERE T.DELETE_FLAG='0' AND T.TASK_ID=?";
		/*try {
			listData = commonJdbcDao.query(sql, new Object[]{taskId});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//String cqdkySql="select t.* from T_CQDKY_ENTM_CLIENTBILL_DEL t where t.id=?";
		String cqdkySql="select"+ 
		"(select t.SAMPLE_NAME from t_cqdky_base_samples_manage t where t.id=n.sample_id ) SAMPLE_NAME,"+
		"(select t.CLIENT_NAME from  T_CQDKY_BASE_CLIENTMANAGE_INFO t where t.delete_flag=0 and t.id=n.parent_id) client_name,"+
		" n.factory_num,"+
		" n.EXPERIMENT_BASIS,"+
		" n.MODEL_STYLE,"+
		" n.SPECIFICATION_MODEL,"+
		"( select m.standard_name from T_CQDKY_BASE_TEST_STANDARD  m where m.id= n.EXPERIMENT_BASIS) EXPERIMENT_BASIS"+
		" from T_CQDKY_ENTM_CLIENTBILL_DEL n where n.id=?";
		String zjsql=" select t.factory_num,t.MODEL_STYLE,"
			   +"(select h.STANDARD_NAME from T_CQDKY_BASE_TEST_STANDARD h where h.id=t.experiment_basis) EXPERIMENT_BASIS ,"
               +" (select c.client_name "
               +" from T_CQDKY_BASE_CLIENTMANAGE_INFO c"
               +" where c.id =(select k.client_id from t_cqdky_entm_clientbill_info k where k.id = "
               +"  (select l.parent_id from t_cqdky_entm_clientbill_del l where l.id = ?)"
               +"   ) )client_name,"
               +"(select m.LAB_NAME from t_cqdky_base_lab m where m.id=t.laboratory_id) ADDRESS ,"
               +"(select q.sample_name from t_cqdky_base_samples_manage q where q.id=t.sample_id)SAMPLE_NAME"
               +" from t_cqdky_entm_clientbill_del t where t.id=?";
		/*String zjsql="select t.DAMPNESS ,t.address,t.environment_temperature, " 
				      +" (select h.client_name "
				      +"   from T_CQDKY_BASE_CLIENTMANAGE_INFO h "
				      +"   where h.id =(select n.client_id from t_cqdky_entm_clientbill_info n where n.id = ("
				      +"   (select m.parent_id from t_cqdky_entm_clientbill_del m where m.id = t.client_bill_id)"
				      +"    ))) client_name"
			      	  +" from T_CQDKY_DETM_WTBUSINESSRECORD t where t.sample_id=? and t.delete_flag=0 order by t.create_time desc";//复制公共数据，可将上一个同种试品的公共数据（如试验工器具、温度、湿度、地点等）直接写入原始数据中
*/		List<Map<String,Object>> cqdkyEntmClientbillDel= new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> tCqdkyDetmWtbusinessrecord= new ArrayList<Map<String,Object>>();//
		List<Map<String,Object>> baseSamplesManage= new ArrayList<Map<String,Object>>();
		String selectBaseDataMetSql="select t.base_data_method from T_CQDKY_BASE_SAMPLES_MANAGE t where t.id=? and t.delete_flag=0 ";
		String baseDataMethod="";
		try {
			baseSamplesManage =jdbcDao.query(selectBaseDataMetSql,new Object[]{sampleId});
			baseDataMethod=(String)baseSamplesManage.get(0).get("BASE_DATA_METHOD");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			cqdkyEntmClientbillDel =jdbcDao.query(cqdkySql,new Object[]{taskId});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			tCqdkyDetmWtbusinessrecord =jdbcDao.query(zjsql,new Object[]{taskId,taskId});
			if(tCqdkyDetmWtbusinessrecord!=null && tCqdkyDetmWtbusinessrecord.size()>0){
				tCqdkyDetmWtbusinessrecord=tCqdkyDetmWtbusinessrecord;
			}else{
				tCqdkyDetmWtbusinessrecord=null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(baseDataMethod.equals("9200009870657")){//公共数据从中间库取
			try {
				listData = commonJdbcDao.query(sql, new Object[]{taskId});
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map  mapData = (Map)listData.get(0);
			at.add(new Object[] {ConvertUtils.getStr(mapData.get("AMBIENT_TEMPERATURE"))});
			dampness.add(new Object[] {ConvertUtils.getStr(mapData.get("HUMIDITY"))});
			address.add(new Object[] {ConvertUtils.getStr(mapData.get("PLACE"))});
			inspectionUnit.add(new Object[] {ConvertUtils.getStr(mapData.get("INSPECTION_UNIT"))});
			jlqjName.add(new Object[] {ConvertUtils.getStr(mapData.get("INSTRUMENT_NAME"))});
			model.add(new Object[] {ConvertUtils.getStr(mapData.get("MODEL"))});
			factoryName.add(new Object[] {ConvertUtils.getStr(mapData.get("FACTORY_NUMBER"))});
			/*if(mapData.get("FACTORY_NUMBER")==null||mapData.get("FACTORY_NUMBER")==""){
				factoryName.add(new Object[] {CqdkyEntmClientbillDel.get(0).getFactoryNum()});
			}else{
				factoryName.add(new Object[] {ConvertUtils.getStr(mapData.get("FACTORY_NUMBER"))});
			}*/
			VerificationBasis.add(new Object[] {ConvertUtils.getStr(mapData.get("CALIBRATION_BASIS"))});
			VerificationConclusion.add(new Object[] {ConvertUtils.getStr(mapData.get("VERIFACTION_CONCLUSION"))});
			VerificationDate.add(new Object[] {ConvertUtils.getStr(mapData.get("VERIFIED_DATE"))});
			experiment_date.add(new Object[] {ConvertUtils.getStr(mapData.get("VALIDITY_TERM"))});
		}else{
			//String factoryNum=cqdkyEntmClientbillDel.get(0).getFactoryNum().toString();
			if(tCqdkyDetmWtbusinessrecord!=null){
			String DAMPNESS=(String)tCqdkyDetmWtbusinessrecord.get(0).get("DAMPNESS");//湿度
			dampness.add(new Object[] {ConvertUtils.getStr(DAMPNESS)});//湿度
			String CLIENT_NAME=(String)tCqdkyDetmWtbusinessrecord.get(0).get("CLIENT_NAME");//送检单位
			inspectionUnit.add(new Object[] {ConvertUtils.getStr(CLIENT_NAME)});//送检单位
			String et=(String)tCqdkyDetmWtbusinessrecord.get(0).get("ENVIRONMENT_TEMPERATURE");//环境温度
			at.add(new Object[] {ConvertUtils.getStr(et)});//环境温度
			String add=(String)tCqdkyDetmWtbusinessrecord.get(0).get("ADDRESS");//地点
			address.add(new Object[] {ConvertUtils.getStr(add)});//地点
			if(cqdkyEntmClientbillDel.size()>0){
			String factoryNum=(String)cqdkyEntmClientbillDel.get(0).get("FACTORY_NUM");//出厂编号
			String SAMPLE_NAME=(String)cqdkyEntmClientbillDel.get(0).get("SAMPLE_NAME");//试品名称
			String EXPERIMENT_BASIS=(String)cqdkyEntmClientbillDel.get(0).get("EXPERIMENT_BASIS");//标准
			String MODEL_STYLE=(String)cqdkyEntmClientbillDel.get(0).get("MODEL_STYLE");//型号
			model.add(new Object[] {ConvertUtils.getStr(MODEL_STYLE)});//型号
			factoryName.add(new Object[] {ConvertUtils.getStr(factoryNum)});//出厂编号
			jlqjName.add(new Object[] {ConvertUtils.getStr(SAMPLE_NAME)});//试品名称
			VerificationBasis.add(new Object[] {ConvertUtils.getStr(EXPERIMENT_BASIS)});//标准
			}
		 }
		}
		map.put(""+list.get(0)+"", at);
		map.put(""+list.get(1)+"", dampness);
		map.put(""+list.get(2)+"", address);
		map.put(""+list.get(3)+"", inspectionUnit);
		map.put(""+list.get(4)+"", jlqjName);
		map.put(""+list.get(5)+"", model);
		map.put(""+list.get(6)+"", factoryName);
		map.put(""+list.get(7)+"", VerificationBasis);
		map.put(""+list.get(8)+"", VerificationConclusion);
		map.put(""+list.get(9)+"", VerificationDate);
		map.put(""+list.get(10)+"", experiment_date);
		return map;
	}

	/**
	 * 功能将封装好的数据向excel里每个定义好的数据区域写数据 作者llj 创建时间 2016-11-24
	 */
	public static void writeExcel(File inputFile, int sheetNum, List list,
			String filePath,String clientBillId,String sampleId,boolean b) {
		Workbook wb = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(inputFile);
			if (inputFile.getName().endsWith("xls")) { // Excel 2003
				wb = new HSSFWorkbook(in);
			}
			int startRow = 0;
			int startCol = 0;
			int endRow = 0;
			int endCol = 0;
			List<Object[]> listValue = null;
			//Map<String, Object> map = dataToMap(clientBillId,sampleId);
			Map<String, Object> map = null;
			if(b){
				map=dataToMapMultCon(list,sampleId, clientBillId);
			}else{
				map=dataToMap(clientBillId,sampleId);
			}
			for (int nn = 0; nn < wb.getNumberOfNames(); nn++) {
				long k=System.currentTimeMillis();
				Name n = wb.getNameAt(nn);
				AreaReference[] arefs = AreaReference.generateContiguous(n
						.getRefersToFormula());
				String defineName = n.getNameName();// 获取自定义cell名称
				if (arefs.length > 0) {
					for (int i = 0; i < arefs.length; i++) {
						startRow = arefs[i].getFirstCell().getRow();// cell所在的开始行
						startCol = arefs[i].getFirstCell().getCol();// cell所在的开始列
						endRow = arefs[i].getLastCell().getRow();// cell所在的结束行
						endCol = arefs[i].getLastCell().getCol();// cell所在的结束行
					}
				}
				if (list.size() > 0) {
					Iterator it = list.iterator();
					while (it.hasNext()) {
						String key = (it.next()).toString();
						if (key.equals(defineName)) {// 当传入的自定义名称与当前的自定义名称
							List<Object[]> value = (List<Object[]>) map
									.get(defineName);// 此时获取跟数据区域相同的键所存的数组
							if (value != null && value.size() > 0) {
								writeListObject(map, value, wb, 0, startRow,
										endRow, startCol, endCol, filePath);
							}

						}
					}
				}
			}
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * @author llj
     * @exception 写数据
     * @param map 组装的数据集合
     * @param data 找出符合条件的数据
     * @param wb Workbook
     * @param sheetNum  要处理的sheet
     * @param startRowNum 开始行
     * @param endRowNum 结束行
     * @param startCellNum 开始列
     * @param endCellNum 结束列
     * @param filePath 文件路径
     */
	public static void writeListObject(Map<String, Object> map,
			List<Object[]> data, Workbook wb, int sheetNum, int startRowNum,
			int endRowNum, int startCellNum, int endCellNum, String filePath) {
		// List<Object[]> oList = new ArrayList<Object[]>();
		Sheet sheet = wb.getSheetAt(sheetNum);
		// endRowNum=data.size();//结束行应该是传过来的data数组行
		if (endRowNum == -1) {
			endRowNum = sheet.getLastRowNum() + 1;
		} else {
			endRowNum++;
		}
		for (int i = startRowNum; i < startRowNum + data.size(); i++) {// 此时循环应该是从开始行加上获取data数组的size否则会出现数组越界
			write(map, data, wb, sheet, startRowNum, i, startCellNum,
					endCellNum, filePath);
		}
		
		// return oList;
	}

	 
	/**
     * @author llj
     * @exception 写数据
     * @param map 组装的数据集合
     * @param data 找出符合条件的数据
     * @param wb Workbook
     * @param sheetNum  要处理的sheet
     * @param startRowNum 开始行
     * @param endRowNum 结束行
     * @param startCellNum 开始列
     * @param endCellNum 结束列
     * @param filePath 文件路径
     */
	private static void write(Map<String, Object> map, List<Object[]> data,
			Workbook wbe, Sheet sheet, int startRowNum, int rowNum,
			int startCellNum, int endCellNum, String filePath) {
		//List<Integer> notReadList = new ArrayList<Integer>();
		Row row = sheet.getRow(rowNum);
		OutputStream out = null;
		if (endCellNum == -1) {
			endCellNum = row.getLastCellNum();
		} else {
			endCellNum++;
		}
		for (int i = startCellNum; i < endCellNum; i++) {
			/*if (notReadList.contains(i)) {
				continue;
			}*/
			Cell cell = row.getCell(i);
			if (cell == null) {
				cell.setCellValue(data.get(rowNum - startRowNum)[i
						- startCellNum].toString());// 处理写坐标
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd",
							Locale.ENGLISH);
				} else {
					cell.setCellValue(data.get(rowNum - startRowNum)[i
							- startCellNum].toString());
				}
			} else if (cell.getCellType() == cell.CELL_TYPE_STRING) {
				cell.setCellValue(data.get(rowNum - startRowNum)[i
						- startCellNum].toString());// 处理写坐标

			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				try {
					cell.setCellValue(data.get(rowNum - startRowNum)[i
							- startCellNum].toString());
				} catch (Exception e) {
					try {
						cell.setCellValue(data.get(rowNum - startRowNum)[i
								- startCellNum].toString());
					} catch (Exception e1) {

					}
				}
			} else {
				cell.setCellValue(data.get(rowNum - startRowNum)[i
						- startCellNum].toString());// 当cellType为 BLANK的时候写数据
			}
		}
		try {
			out = new FileOutputStream(filePath);
			wbe.write(out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
