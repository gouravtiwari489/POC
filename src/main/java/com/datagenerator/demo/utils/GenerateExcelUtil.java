package com.datagenerator.demo.utils;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class GenerateExcelUtil {

	public static void createAndInsertDataIntoSheet(XSSFWorkbook workbook, String sheetName,
			List<List<String>> recordToAdd)  {
		try{
		int rownum = 0;
		workbook.createSheet(sheetName);
		XSSFSheet firstSheet = workbook.getSheet(sheetName);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		addContent(recordToAdd, firstSheet, rownum, style);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void addContent(List<List<String>> l1, XSSFSheet firstSheet, int rownum, XSSFCellStyle style)
			throws Exception {
		try {
			for (int j = 0; j < l1.size(); j++) {
				XSSFRow row = firstSheet.createRow(rownum);
				List<String> l2 = l1.get(j);
				for (int k = 0; k < l2.size(); k++) {
					Cell cell = row.createCell(k);
					cell.setCellValue(l2.get(k));
					if (j == 0) {
						cell.setCellStyle(style);
					}
				}
				rownum++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
