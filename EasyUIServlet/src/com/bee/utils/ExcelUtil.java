package com.bee.utils;

import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil {

	public static void fillExcelData(ResultSet rs, Workbook wb, String[] headers) throws Exception {
		int rowIndex = 0;
		Sheet sheet = wb.createSheet();
		Row row = sheet.createRow(rowIndex++);
		for (int i = 0; i < headers.length; i++) {
			row.createCell(i).setCellValue(headers[i]);
		}
		while (rs.next()) {
			row = sheet.createRow(rowIndex++);
			for (int i = 0; i < headers.length; i++) {
				row.createCell(i).setCellValue(rs.getObject(i + 1).toString());
			}
		}
	}

	public static Workbook fillExcelData(ResultSet rs, String templateFileName) throws Exception {
		InputStream is = ExcelUtil.class.getResourceAsStream("/com/bee/template/" + templateFileName);
		POIFSFileSystem pfs = new POIFSFileSystem(is);
		Workbook wb = new HSSFWorkbook(pfs);
		Sheet sheet = wb.getSheetAt(0); // 取得第一个sheet
		int cellNums = sheet.getRow(0).getLastCellNum(); // 获取列数（getRow(0)是第一行）
		int rowIndex = 1; // 第二行开始
		while (rs.next()) {
			Row row = sheet.createRow(rowIndex++);
			for (int i = 0; i < cellNums; i++) {
				row.createCell(i).setCellValue(rs.getObject(i + 1).toString());
			}
		}
		return wb;
	}

	public static String formatCell(HSSFCell hssfCell) {
		if (hssfCell == null) {
			return "";
		}

		switch (hssfCell.getCellType()) {
		case BOOLEAN:
			return String.valueOf(hssfCell.getBooleanCellValue());
		case NUMERIC:
			DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
			String str = decimalFormat.format(hssfCell.getNumericCellValue());
			System.out.println(str);
			return str;
		default:
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
}