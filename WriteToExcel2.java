package com.llj.test;
import java.io.File;
import java.io.FileInputStream;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;

public class WriteToExcel2 {

	
	
	public static void main(String[] args) {
		writeDataToExcel("D:/10kv�����������춨֤��1.xls");
	}

	/**
	 * ���ܽ���excel����д�������
	 * ����llj
	 * ����ʱ�� 2016-11-24
	 * @param filePath �ļ�·��
	 */
	public static void writeDataToExcel(String filePath) {
		File inputFile = new File(filePath);
		int sheetNum = 0;// �ڼ���sheet��excel��sheet�������±���Ǵ�0��ʼ
		// ���к���Ȼ����������
		List list = new ArrayList<Object>();
		list.add("data");
		list.add("lxData");
		// ��ȡexcel
		writeExcel(inputFile, sheetNum, list,filePath);
		System.out.println("д���ݳɹ�!");

	}
	
	/**
	 * ���� ��װҪд������ΪMap,������ʽ����ʱ����װmds������������
	 * ����llj
	 * ����ʱ�� 2016-11-24
	 * @return map 
	 */
	public static Map<String,Object> dataToMap(){
		Map<String,Object> map=new HashMap<String,Object>();//���b���е���������
		List<Object[]> data = new ArrayList<Object[]>();// ����д����data
		data.add(new Object[] { "wq", "����", "s", "re", "sas" });
		data.add(new Object[] { "llj", "sa", "ca", "tt", "sds" });
		data.add(new Object[] { "sa", "ssss", "��໰�", "gt", "ddd" });
		data.add(new Object[] { "ssa", "ssssas", "���sas��", "gst", "dsdd" });
		data.add(new Object[] { "ssa", "ssssas", "���sas��", "gst", "dsdd" });
		
		List<Object[]> lxData = new ArrayList<Object[]>();// ����д����data
		lxData.add(new Object[] { "300/400" });
		lxData.add(new Object[] { "300/500"});
		lxData.add(new Object[] { "200/400"});
		lxData.add(new Object[] { "600/400"});
		lxData.add(new Object[] { "300/700" });
		lxData.add(new Object[] { "300/800"});
		lxData.add(new Object[] { "300/900"});
		lxData.add(new Object[] { "500/400"});
		map.put("data", data);
		map.put("lxData", lxData);
		return map;
	}
    
	/**
	 * ���ܽ���װ�õ�������excel��ÿ������õ���������д����
	 * ����llj
	 * ����ʱ�� 2016-11-24
	 */
	public static void writeExcel(File inputFile, int sheetNum, List list,String filePath) {
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
			Map<String,Object> map=dataToMap();
			for (int nn = 0; nn < wb.getNumberOfNames(); nn++) {
				Name n = wb.getNameAt(nn);
				AreaReference[] arefs = AreaReference.generateContiguous(n
						.getRefersToFormula());
				String defineName = n.getNameName();// ��ȡ�Զ���cell����
				if (arefs.length > 0) {
					for (int i = 0; i < arefs.length; i++) {
						startRow = arefs[i].getFirstCell().getRow();// cell���ڵĿ�ʼ��
						startCol = arefs[i].getFirstCell().getCol();// cell���ڵĿ�ʼ��
						endRow = arefs[i].getLastCell().getRow();// cell���ڵĽ�����
						endCol = arefs[i].getLastCell().getCol();// cell���ڵĽ�����
					}
				}
				if (list.size() > 0) {
					Iterator it = list.iterator();
					while (it.hasNext()) {
						String key = (it.next()).toString();
						if (key.equals(defineName)) {// ��������Զ��������뵱ǰ���Զ�������
							List<Object[]> value
							=(List<Object[]>)map.get(defineName);//��ʱ��ȡ������������ͬ�ļ����������
							if(value.size()>0){
								listValue = writeListObject(map,value, wb, 0, startRow,
										endRow, startCol, endCol,filePath);
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

	public static List<Object[]> writeListObject(Map<String,Object> map,List<Object[]> data,
			Workbook wb, int sheetNum, int startRowNum, int endRowNum,
			int startCellNum, int endCellNum,String filePath) {
		List<Object[]> oList = new ArrayList<Object[]>();
		Sheet sheet = wb.getSheetAt(sheetNum);
		// endRowNum=data.size();//������Ӧ���Ǵ�������data������
		if (endRowNum == -1) {
			endRowNum = sheet.getLastRowNum() + 1;
		} else {
			endRowNum++;
		}
		for (int i = startRowNum; i < startRowNum + data.size(); i++) {// ��ʱѭ��Ӧ���Ǵӿ�ʼ�м��ϻ�ȡdata�����size������������Խ��
			write(map,data, wb, sheet, startRowNum, i, startCellNum, endCellNum,filePath);
		}
		return oList;
	}

	private static void write(Map<String, Object> map, List<Object[]> data,
			Workbook wbe, Sheet sheet, int startRowNum, int rowNum,
			int startCellNum, int endCellNum,String filePath) {
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
			if (cell == null) {
				cell.setCellValue(data.get(rowNum - startRowNum)[i
						- startCellNum].toString());// ����д����
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
						- startCellNum].toString());// ����д����

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
						- startCellNum].toString());// ��cellTypeΪ BLANK��ʱ��д����
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
