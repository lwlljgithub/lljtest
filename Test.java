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
		File inputFile = new File("D:/10kv�����������춨֤��1.xls");
		int sheetNum = 0;//�ڼ���sheet��excel��sheet�������±���Ǵ�0��ʼ
		//���к���Ȼ����������
		List list = new ArrayList<Object>();
		list.add("RowCoordinate");
		list.add("ColCoordinate");
		list.add("data");
		list.add("lxData");
		list.add("towFh");
		list.add("cos");
		//����ȡ�������ݷ�װ��һ��list��
		List listData = new ArrayList<Object>();
		//��ȡexcel
		HashMap map = ReadExcel(inputFile,sheetNum,list);
		List<Object[]> data1=(List<Object[]>)map.get("data");
		List<Object[]> RowCoordinate=(List<Object[]>)map.get("RowCoordinate");
		List<Object[]> ColCoordinate=(List<Object[]>)map.get("ColCoordinate");
		List<Object[]> towFh=(List<Object[]>)map.get("towFh");
		List<Object[]> lxData=(List<Object[]>)map.get("lxData");
		List<Object[]> cos=(List<Object[]>)map.get("cos");
		List<Object[]> listLX=toNewList(lxData,4);//����n�е����ݽ���ת��
		List<Object[]> newtowFh=toNewList(towFh,2);//����n�е����ݽ���ת��
		List<Object[]> cosnew=toNewList(cos,16);//����n�е����ݽ���ת��
		for(int i=0;i<data1.size();i++){
			if(data1.size()>0){
				for(int j=0;j<data1.get(i).length;j++){
					System.out.println("cell={"+"data="+data1.get(i)[j]+"_"+RowCoordinate.get(0)[j]+"_"+ColCoordinate.get(i)[0]+"_"+listLX.get(i)[0]+"_"+newtowFh.get(i)[0]+
							"_"+cosnew.get(i)[0]+"}");
					/*System.out.println("cell={"+"data="+data1.get(i)[j]+"_"+RowCoordinate.get(0)[j]+"_"+ColCoordinate.get(i)[0]+"_"+listLX.get(i)[0]+"_"+newtowFh.get(i)[0]+"}");*/
				}
			}
		}
		
		//��װexcel�е�����
		if(map.size()>0){
			Iterator it = list.iterator();
			while(it.hasNext()){
				List<Object[]> obj = (List<Object[]>) map.get((it.next()).toString());
				listData.add(obj);
			}
		}
		AnalyzeData(listData);
		System.out.println("��ȡ������....");
	}
	
	/**
	 * ���ܽ�������excel������Ժϲ��е�,������������list���鼯��
	 * ����llj
	 * ����ʱ�� 2016-11-21
	 * @param list Ҫת����Ŀ�꼯�϶���
	 * @param kRowNum �缸��
	 * @return  list �����µļ��϶���
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
		if(list.size()==1){//���������е�ʱ�����kRowNum������ѭ����ֵÿ���������涼�ǵ�һ����ȡ������ֵ(��ʱ������kRowNum�������)
			for(int i=0;i<kRowNum;i++){
			     newList.add(i, list.get(0));
			}
		}
		return newList;
	}



	
	/**
	 * ���ܣ���������
	 * ���ߣ�WeiQingSong
	 * ����ʱ�䣺2016-11-11
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
	 * ���ܣ���ȡexcel
	 * ���ߣ�WeiQingSong
	 * ����ʱ�䣺2016-11-11
	 * @param inputFile
	 * @param sheetNum
	 */
	public static HashMap ReadExcel(File inputFile,int sheetNum,List list){
		Workbook wb = null;
		HashMap map = new HashMap();
		try {
			wb = WorkbookFactory.create(inputFile);//������ĵ�
			Sheet s = wb.getSheetAt(sheetNum);
			int startRow = 0;
			int startCol = 0;
			int endRow = 0;
			int endCol = 0;
			List<Object[]> listValue = null;
			for (int nn=0; nn<wb.getNumberOfNames(); nn++) {
				Name n = wb.getNameAt(nn);
				AreaReference[] arefs = AreaReference.generateContiguous(n.getRefersToFormula());
				String defineName = n.getNameName();//��ȡ�Զ���cell����
				System.out.println(defineName);
				if(arefs.length>0){
					for(int i=0;i<arefs.length;i++){
						startRow = arefs[i].getFirstCell().getRow();//cell���ڵĿ�ʼ��
						startCol = arefs[i].getFirstCell().getCol();//cell���ڵĿ�ʼ��
						endRow = arefs[i].getLastCell().getRow();//cell���ڵĽ�����
						endCol = arefs[i].getLastCell().getCol();//cell���ڵĽ�����
					}
				}
				if(list.size()>0){
					Iterator it = list.iterator();
					while(it.hasNext()){
						String key = (it.next()).toString();
						if(key.equals(defineName)){//��������Զ��������뵱ǰ���Զ�������
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
	 * ��һ��excel��ָ���к�ָ���У����ҷ���һ��list��װ��object����
	 * 
	 * @param sheetNum
	 *            sheet��λ�ú�
	 * @param startRowNum
	 *            ��ʼ���к�
	 * @param endRowNum
	 *            �������к�
	 * @param startCellNum
	 *            ��ʼ���к�
	 * @param endCellNum
	 *            �������к�
	 * @return list��װ��object����
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
				//object[j] = cell.getStringCellValue()+":"+rowNum+"-"+i;//���������Ķ���
				
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
	
	
	
	
	//��excel����д����
	public static void writeBegin() {
		File inputFile = new File("D:/10kv�����������춨֤��1.xls");
		int sheetNum = 0;//�ڼ���sheet��excel��sheet�������±���Ǵ�0��ʼ
		//���к���Ȼ����������
		List list = new ArrayList<Object>();
		list.add("data");
		/*list.add("RowCoordinate");
		list.add("ColCoordinate");
		list.add("data");
		list.add("lxData");
		list.add("towFh");
		list.add("cos");*/
		//����ȡ�������ݷ�װ��һ��list��
		List listData = new ArrayList<Object>();
		//��ȡexcel
		writeExcel(inputFile,sheetNum,list);

	}
	
	public static void writeExcel(File inputFile,int sheetNum,List list){
		Workbook wb = null;
		//HashMap map = new HashMap();
		try {
			wb = WorkbookFactory.create(inputFile);//������ĵ�
			Sheet s = wb.getSheetAt(sheetNum);
			int startRow = 0;
			int startCol = 0;
			int endRow = 0;
			int endCol = 0;
			List<Object[]> listValue = null;
			List<Object[]> data=new ArrayList<Object[]>();//����д����data
			data.add(new Object[]{"Sun Jianjing", "Xiandaiyinxiang", "Renminyoudian", "re","sas"});
			data.add(new Object[]{"Wang Aiping", "Ruanjianceshi", "Qinghuadaxue", "tt","sds"}); 
			data.add(new Object[]{"Zhang Yihe", "51DanPianJi", "Renminyoudian", "gt","ddd"});
			for (int nn=0; nn<wb.getNumberOfNames(); nn++) {
				Name n = wb.getNameAt(nn);
				AreaReference[] arefs = AreaReference.generateContiguous(n.getRefersToFormula());
				String defineName = n.getNameName();//��ȡ�Զ���cell����
				System.out.println(defineName);
				if(arefs.length>0){
					for(int i=0;i<arefs.length;i++){
						startRow = arefs[i].getFirstCell().getRow();//cell���ڵĿ�ʼ��
						startCol = arefs[i].getFirstCell().getCol();//cell���ڵĿ�ʼ��
						endRow = arefs[i].getLastCell().getRow();//cell���ڵĽ�����
						endCol = arefs[i].getLastCell().getCol();//cell���ڵĽ�����
					}
				}
				if(list.size()>0){
					Iterator it = list.iterator();
					while(it.hasNext()){
						String key = (it.next()).toString();
						if(key.equals(defineName)){//��������Զ��������뵱ǰ���Զ�������
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
		//endRowNum=data.size();//������Ӧ���Ǵ�������data������
		if (endRowNum == -1) {
			endRowNum = sheet.getLastRowNum() + 1;
		} else {
			endRowNum++;
		}
		for (int i = startRowNum; i < startRowNum+data.size(); i++) {//��ʱѭ��Ӧ���Ǵӿ�ʼ�м��ϻ�ȡdata�����size������������Խ��
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
				cell.setCellValue(data.get(rowNum-startRowNum)[i-startCellNum].toString());//����д����
				//object[j] = cell.getStringCellValue()+":"+rowNum+"-"+i;//���������Ķ���
				
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
			out =  new FileOutputStream("D:/10kv�����������춨֤��11.xls");
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
