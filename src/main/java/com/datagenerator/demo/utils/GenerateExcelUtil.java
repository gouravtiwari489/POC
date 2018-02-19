package com.datagenerator.demo.utils;

import java.io.IOException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.stereotype.Service;

@Service
public class GenerateExcelUtil {

	public static void createAndInsertDataIntoSheet(HSSFWorkbook workbook, String sheetName,
			List<List<String>> recordToAdd) throws Exception {
		int rownum = 0;
		workbook = createExcel(workbook, sheetName);
		HSSFSheet firstSheet = workbook.getSheet(sheetName);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont font = workbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		addContent(recordToAdd, firstSheet, rownum, style);
	}
	public static void addContent(List<List<String>> l1, HSSFSheet firstSheet, int rownum, HSSFCellStyle style)
			throws Exception {
		try {
			for (int j = 0; j < l1.size(); j++) {
				HSSFRow row = firstSheet.createRow(rownum);
				List<String> l2 = l1.get(j);
				for (int k = 0; k < l2.size(); k++) {
					HSSFCell cell = row.createCell(k);
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
	public static HSSFWorkbook createExcel(HSSFWorkbook workbook, String sheetName)
			throws IOException {
		try {
			if (workbook.getSheet(sheetName) == null) {
				synchronized (workbook) {
						if (workbook.getSheet(sheetName) == null) {
							workbook.createSheet(sheetName);
						}
					}
				} 
			}catch (Exception e) {
				throw e;
			}
		return workbook;
	}
}
