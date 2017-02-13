package com.llj.test;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.AreaReference;

public class Test {
	public static void main1(String[] args) {
		File inputFile = new File("D:/10kv电流互感器检定证书1.xls");
		int sheetNum = 0;//第几个sheet，excel中sheet，行列下标均是从0开始
		//先行后列然后数据区域
		List list = new ArrayList<Object>();
		list.add("RowCoordinate");
		list.add("ColCoordinate");
		list.add("data");
		list.add("lxData");
		list.add("towFh");
		list.add("cos");
		//将读取到的数据分装到一个list中
		List listData = new ArrayList<Object>();
		//读取excel
		HashMap map = ReadExcel(inputFile,sheetNum,list);
		List<Object[]> data1=(List<Object[]>)map.get("data");
		List<Object[]> RowCoordinate=(List<Object[]>)map.get("RowCoordinate");
		List<Object[]> ColCoordinate=(List<Object[]>)map.get("ColCoordinate");
		List<Object[]> towFh=(List<Object[]>)map.get("towFh");
		List<Object[]> lxData=(List<Object[]>)map.get("lxData");
		List<Object[]> cos=(List<Object[]>)map.get("cos");
		List<Object[]> listLX=toNewList(lxData,4);//将夸n行的数据进行转换
		List<Object[]> newtowFh=toNewList(towFh,2);//将夸n行的数据进行转换
		List<Object[]> cosnew=toNewList(cos,16);//将夸n行的数据进行转换
		for(int i=0;i<data1.size();i++){
			if(data1.size()>0){
				for(int j=0;j<data1.get(i).length;j++){
					System.out.println("cell={"+"data="+data1.get(i)[j]+"_"+RowCoordinate.get(0)[j]+"_"+ColCoordinate.get(i)[0]+"_"+listLX.get(i)[0]+"_"+newtowFh.get(i)[0]+
							"_"+cosnew.get(i)[0]+"}");
					/*System.out.println("cell={"+"data="+data1.get(i)[j]+"_"+RowCoordinate.get(0)[j]+"_"+ColCoordinate.get(i)[0]+"_"+listLX.get(i)[0]+"_"+newtowFh.get(i)[0]+"}");*/
				}
			}
		}
		
		//封装excel中的数据
		if(map.size()>0){
			Iterator it = list.iterator();
			while(it.hasNext()){
				List<Object[]> obj = (List<Object[]>) map.get((it.next()).toString());
				listData.add(obj);
			}
		}
		AnalyzeData(listData);
		System.out.println("读取结束了....");
	}
	
	/**
	 * 功能将单独的excel块区域对合并行的,进行重新生成list数组集合
	 * 作者llj
	 * 创建时间 2016-11-21
	 * @param list 要转换的目标集合对象
	 * @param kRowNum 跨几行
	 * @return  list 返回新的集合对象
	 */
	public static List<Object[]> toNewList(List<Object[]> list, int kRowNum){
		List<Object[]> newList=new ArrayList<Object[]>();
		if(list.size()>1){
			for(int i=0;i<list.size();i++){
				if(i<kRowNum*1&&i>=(i/kRowNum)*kRowNum){
					newList.add(i, list.get((i/kRowNum)*kRowNum));
					continue;
				}
				if(i<kRowNum*2&&i>=(i/kRowNum)*kRowNum){
					newList.add(i, list.get((i/kRowNum)*kRowNum));
					continue;
				}
				if(i<kRowNum*3&&i>=(i/kRowNum)*kRowNum){
					newList.add(i, list.get((i/kRowNum)*kRowNum));
					continue;
				}
				if(i<kRowNum*4&&i>=(i/kRowNum)*kRowNum){
					newList.add(i, list.get((i/kRowNum)*kRowNum));
					continue;
				}
				if(i<kRowNum*5&&i>=(i/kRowNum)*kRowNum){
					newList.add(i, list.get((i/kRowNum)*kRowNum));
					continue;
				}
				if(i<kRowNum*6&&i>=(i/kRowNum)*kRowNum){
					newList.add(i, list.get((i/kRowNum)*kRowNum));
					continue;
				}
				if(i<kRowNum*7&&i>=(i/kRowNum)*kRowNum){
					newList.add(i, list.get((i/kRowNum)*kRowNum));
					continue;
				}
				if(i<kRowNum*8&&i>=(i/kRowNum)*kRowNum){
					newList.add(i, list.get((i/kRowNum)*kRowNum));
					continue;
				}
			}
		}
		if(list.size()==1){//当跨所有行的时候根据kRowNum跨行数循环设值每隔数组里面都是第一个读取出来的值(此时不考虑kRowNum跨多少行)
			for(int i=0;i<kRowNum;i++){
			     newList.add(i, list.get(0));
			}
		}
		return newList;
	}



	
	/**
	 * 功能：解析数据
	 * 作者：WeiQingSong
	 * 创建时间：2016-11-11
	 * @param list
	 */
	public static void AnalyzeData(List list){
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				List<Object[]> obj = (List<Object[]>) list.get(i);
				if(obj!=null){
				for(Object[] values:obj){
					if(values.length>0){
						for(Object value:values){
							System.out.println("value:"+value);
						}
					}
				}	
			}
			}		
		}
	}




	/**
	 * 功能：读取excel
	 * 作者：WeiQingSong
	 * 创建时间：2016-11-11
	 * @param inputFile
	 * @param sheetNum
	 */
	public static HashMap ReadExcel(File inputFile,int sheetNum,List list){
		Workbook wb = null;
		HashMap map = new HashMap();
		try {
			wb = WorkbookFactory.create(inputFile);//输入的文档
			Sheet s = wb.getSheetAt(sheetNum);
			int startRow = 0;
			int startCol = 0;
			int endRow = 0;
			int endCol = 0;
			List<Object[]> listValue = null;
			for (int nn=0; nn<wb.getNumberOfNames(); nn++) {
				Name n = wb.getNameAt(nn);
				AreaReference[] arefs = AreaReference.generateContiguous(n.getRefersToFormula());
				String defineName = n.getNameName();//获取自定义cell名称
				System.out.println(defineName);
				if(arefs.length>0){
					for(int i=0;i<arefs.length;i++){
						startRow = arefs[i].getFirstCell().getRow();//cell所在的开始行
						startCol = arefs[i].getFirstCell().getCol();//cell所在的开始列
						endRow = arefs[i].getLastCell().getRow();//cell所在的结束行
						endCol = arefs[i].getLastCell().getCol();//cell所在的结束行
					}
				}
				if(list.size()>0){
					Iterator it = list.iterator();
					while(it.hasNext()){
						String key = (it.next()).toString();
						if(key.equals(defineName)){//当传入的自定义名称与当前的自定义名称
							listValue = readListObject(wb,0, startRow,
									endRow, startCol, endCol);
							if(listValue.size()>0){
								map.put(key, listValue);
							}
						}
					}
				}
			}
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}





	/**
	 * 读一个excel的指定行和指定列，并且返回一个list包装的object数组
	 * 
	 * @param sheetNum
	 *            sheet的位置号
	 * @param startRowNum
	 *            开始的行号
	 * @param endRowNum
	 *            结束的行号
	 * @param startCellNum
	 *            开始的列号
	 * @param endCellNum
	 *            结束的列号
	 * @return list包装的object数组
	 */
	public static List<Object[]> readListObject(Workbook wb,int sheetNum, int startRowNum,
			int endRowNum, int startCellNum, int endCellNum) {
		List<Object[]> oList = new ArrayList<Object[]>();
		Sheet sheet = wb.getSheetAt(sheetNum);
		if (endRowNum == -1) {
			endRowNum = sheet.getLastRowNum() + 1;
		} else {
			endRowNum++;
		}
		for (int i = startRowNum; i < endRowNum; i++) {
			Object[] objects = readOneline(sheet, i, startCellNum, endCellNum);
			oList.add(objects);
		}
		return oList;
	}


	/**
	 * 
	 * @param sheet
	 * @param rowNum
	 * @param startCellNum
	 * @param endCellNum
	 * @return
	 */
	private static Object[] readOneline(Sheet sheet, int rowNum, int startCellNum,int endCellNum) {
		List<Integer> notReadList = new ArrayList<Integer>();
		Map<String,String> map=new HashMap<String,String>();
		Row row = sheet.getRow(rowNum);
		if (row == null) {
			return null;
		}
		if (endCellNum == -1) {
			endCellNum = row.getLastCellNum();
			
		} else {
			endCellNum++;
		}
		int objectNum = endCellNum - startCellNum;
		Object[] object = new Object[objectNum];
		
		int j = 0;
		for (int i = startCellNum; i < endCellNum; i++) {
			if (notReadList.contains(i)) {
				continue;
			}
			Cell cell = row.getCell(i);
			if(cell==null){
				object[j] = "";
			}else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd",
							Locale.ENGLISH);
					object[j] = sdf.format(HSSFDateUtil.getJavaDate(cell
							.getNumericCellValue()));
				} else {
					object[j] = cell.getNumericCellValue();
				}
			} else if (cell.getCellType() == cell.CELL_TYPE_STRING) {
				object[j] = cell.getStringCellValue();	
				//object[j] = cell.getStringCellValue()+":"+rowNum+"-"+i;//放入带坐标的对象
				
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				try {
					object[j] = cell.getNumericCellValue();
				} catch (Exception e) {
					try {
						object[j] = cell.getStringCellValue();
					} catch (Exception e1) {
						object[j] = "";
					}
				}
			} else {
				object[j] = "";
			}
			j++;
		}
		return object;
	}
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		writeBegin();
	}
	
	
	
	
	//向excel里面写数据
	public static void writeBegin() {
		File inputFile = new File("D:/10kv电流互感器检定证书1.xls");
		int sheetNum = 0;//第几个sheet，excel中sheet，行列下标均是从0开始
		//先行后列然后数据区域
		List list = new ArrayList<Object>();
		list.add("data");
		/*list.add("RowCoordinate");
		list.add("ColCoordinate");
		list.add("data");
		list.add("lxData");
		list.add("towFh");
		list.add("cos");*/
		//将读取到的数据分装到一个list中
		List listData = new ArrayList<Object>();
		//读取excel
		writeExcel(inputFile,sheetNum,list);

	}
	
	public static void writeExcel(File inputFile,int sheetNum,List list){
		Workbook wb = null;
		//HashMap map = new HashMap();
		try {
			wb = WorkbookFactory.create(inputFile);//输入的文档
			Sheet s = wb.getSheetAt(sheetNum);
			int startRow = 0;
			int startCol = 0;
			int endRow = 0;
			int endCol = 0;
			List<Object[]> listValue = null;
			List<Object[]> data=new ArrayList<Object[]>();//测试写数据data
			data.add(new Object[]{"Sun Jianjing", "Xiandaiyinxiang", "Renminyoudian", "re","sas"});
			data.add(new Object[]{"Wang Aiping", "Ruanjianceshi", "Qinghuadaxue", "tt","sds"}); 
			data.add(new Object[]{"Zhang Yihe", "51DanPianJi", "Renminyoudian", "gt","ddd"});
			for (int nn=0; nn<wb.getNumberOfNames(); nn++) {
				Name n = wb.getNameAt(nn);
				AreaReference[] arefs = AreaReference.generateContiguous(n.getRefersToFormula());
				String defineName = n.getNameName();//获取自定义cell名称
				System.out.println(defineName);
				if(arefs.length>0){
					for(int i=0;i<arefs.length;i++){
						startRow = arefs[i].getFirstCell().getRow();//cell所在的开始行
						startCol = arefs[i].getFirstCell().getCol();//cell所在的开始列
						endRow = arefs[i].getLastCell().getRow();//cell所在的结束行
						endCol = arefs[i].getLastCell().getCol();//cell所在的结束行
					}
				}
				if(list.size()>0){
					Iterator it = list.iterator();
					while(it.hasNext()){
						String key = (it.next()).toString();
						if(key.equals(defineName)){//当传入的自定义名称与当前的自定义名称
							listValue = writeListObject(data,wb,0, startRow,
									endRow, startCol, endCol);
						}
					}
				}
			}
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<Object[]> writeListObject(List<Object[]> data,Workbook wb,int sheetNum, int startRowNum,
			int endRowNum, int startCellNum, int endCellNum) {
		List<Object[]> oList = new ArrayList<Object[]>();
		Sheet sheet = wb.getSheetAt(sheetNum);
		//endRowNum=data.size();//结束行应该是传过来的data数组行
		if (endRowNum == -1) {
			endRowNum = sheet.getLastRowNum() + 1;
		} else {
			endRowNum++;
		}
		for (int i = startRowNum; i < startRowNum+data.size(); i++) {//此时循环应该是从开始行加上获取data数组的size否则会出现数组越界
			//while((endRowNum-startRowNum)<=data.size()){
				write(data,wb,sheet, startRowNum,i, startCellNum, endCellNum);
			//}
			
		}
		return oList;
	}
	
	private static void write(List<Object[]> data,Workbook wbe,Sheet sheet,int startRowNum ,int rowNum, int startCellNum,int endCellNum) {
		List<Integer> notReadList = new ArrayList<Integer>();
		Row row = sheet.getRow(rowNum);
		OutputStream out = null;
		if (endCellNum == -1) {
			endCellNum = row.getLastCellNum();
		} else {
			endCellNum++;
		}
		for (int i = startCellNum; i < endCellNum; i++) {
			if (notReadList.contains(i)) {
				continue;
			}
			Cell cell = row.getCell(i);
			if(cell==null){
				//cell.setCellValue("cao");
			}else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd",
							Locale.ENGLISH);
					//cell.setCellValue("cao");
				} else {
					cell.setCellValue(data.get(rowNum-startRowNum)[i-startCellNum].toString());
				}
			} else if (cell.getCellType() == cell.CELL_TYPE_STRING) {
				cell.setCellValue(data.get(rowNum-startRowNum)[i-startCellNum].toString());//处理写坐标
				//object[j] = cell.getStringCellValue()+":"+rowNum+"-"+i;//放入带坐标的对象
				
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				try {
					cell.setCellValue(data.get(rowNum-startRowNum)[i-startCellNum].toString());
				} catch (Exception e) {
					try {
						cell.setCellValue(data.get(rowNum-startRowNum)[i-startCellNum].toString());
					} catch (Exception e1) {
						
					}
				}
			} else {
				
			}
		}
		try {
			out =  new FileOutputStream("D:/10kv电流互感器检定证书11.xls");
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
