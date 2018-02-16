package com.datagenerator.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.stereotype.Service;

@Service
public class GenerateExcelUtil {

	static HSSFWorkbook workbook = null;
	static CellStyle style;

	public  static void createAndInsertDataIntoSheet(String excelFilePath, String sheetName, List<List<String>> recordToAdd)
			throws Exception {
		int rownum = 0;
		HSSFWorkbook workbook = createExcel(excelFilePath, sheetName);
		HSSFSheet firstSheet = workbook.getSheet(sheetName);
		style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		addContent(recordToAdd, firstSheet, rownum);
		FileOutputStream outputStream = new FileOutputStream(excelFilePath);
		workbook.write(outputStream);
		outputStream.close();
	}

	public static void addContent(List<List<String>> l1, HSSFSheet firstSheet, int rownum) throws Exception {
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

	public static HSSFWorkbook createExcel(String excelFilePath, String sheetName) throws IOException {
		FileOutputStream fos = null;
		if (workbook == null || workbook.getSheet(sheetName) == null) {
			try {
				synchronized (GenerateExcelUtil.class) {
					if (workbook == null) {
						if (new File(excelFilePath).createNewFile()) {
							workbook = new HSSFWorkbook();
						} else {
							POIFSFileSystem pfs = new POIFSFileSystem(new FileInputStream(excelFilePath));
							workbook = new HSSFWorkbook(pfs);
						}
						if (workbook.getSheet(sheetName) == null) {
							fos = new FileOutputStream(excelFilePath);
							workbook.createSheet(sheetName);
							workbook.write(fos);
						}
					} else {
						if (workbook.getSheet(sheetName) == null) {
							fos = new FileOutputStream(excelFilePath);
							workbook.createSheet(sheetName);
							workbook.write(fos);
						}
					}
				}

			} catch (IOException e) {
				throw e;
			} finally {
				if (fos != null) {
					fos.close();
				}
			}
		}
		return workbook;
	}

}
