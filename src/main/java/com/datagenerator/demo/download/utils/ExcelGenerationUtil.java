package com.datagenerator.demo.download.utils;

import static com.datagenerator.demo.utils.DataGenUtil.removeSingleQuotes;

import com.datagenerator.demo.domain.CustomUserDetails;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public enum ExcelGenerationUtil implements GenerateDataInterface {
  INSTANCE;

  public void addContent(
      List<List<String>> l1, XSSFSheet firstSheet, int rownum, XSSFCellStyle style)
      throws Exception {
    try {
      for (int j = 0; j < l1.size(); j++) {
        XSSFRow row = firstSheet.createRow(rownum);
        List<String> l2 = l1.get(j);
        for (int k = 0; k < l2.size(); k++) {
          Cell cell = row.createCell(k);
          cell.setCellValue(removeSingleQuotes(l2.get(k)));
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

  @Override
  public void generateData(
      String tableName, List<List<String>> excelData, String fileType, CustomUserDetails user) {
    int rownum = 0;
    try {
      XSSFWorkbook workbook = new XSSFWorkbook();
      workbook.createSheet(tableName);
      XSSFSheet firstSheet = workbook.getSheet(tableName);
      XSSFCellStyle style = workbook.createCellStyle();
      style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
      XSSFFont font = workbook.createFont();
      font.setColor(IndexedColors.BLACK.getIndex());
      font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
      style.setFont(font);
      addContent(excelData, firstSheet, rownum, style);
      writeToFile(workbook, tableName, fileType, user);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user)
      throws IOException, FileNotFoundException {
    String filePath;
    Resource resource = new ClassPathResource("output\\" + user.getUsername());
    if (!resource.exists()) {
      new File("bin\\output\\" + user.getUsername()).mkdir();
    }
    File file = new File(resource.getFile().getPath() + "/" + fileType);
    if (!file.exists()) {
      file.mkdir();
    }
    filePath =
        String.format(
            "%s\\%s.%s", resource.getFile().getPath() + "\\" + fileType, tableName, fileType);
    OutputStream excelFileToCreate = new FileOutputStream(new File(filePath));
    XSSFWorkbook workbook = (XSSFWorkbook) obj;
    workbook.write(excelFileToCreate);
    excelFileToCreate.close();
  }
}
