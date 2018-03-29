package com.osi.datagen.filegeneration.util;

import static com.osi.datagen.datageneration.service.DataGenUtil.removeSingleQuotes;

import com.osi.datagen.domain.CustomUserDetails;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

@Slf4j
public enum ExcelGenerationUtil implements GenerateDataInterface {
  INSTANCE;

  public void addContent(List<List<String>> l1, Sheet firstSheet, int rownum, CellStyle style)
      throws Exception {
    try {
      for (int j = 0; j < l1.size(); j++) {
        Row row = firstSheet.createRow(rownum);
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
      log.info(e.getMessage());
      throw new Exception();
    }
  }

  @Override
  public void generateData(
      String tableName, List<List<String>> excelData, String fileType, CustomUserDetails user) {
    log.info(Thread.currentThread().getName() + " " + tableName + " generateData started");
    int rownum = 0;
    try {
      SXSSFWorkbook workbook = new SXSSFWorkbook();
      workbook.createSheet(tableName);
      CellStyle style = workbook.createCellStyle();
      style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
      Font font = workbook.createFont();
      font.setColor(IndexedColors.BLACK.getIndex());
      font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
      style.setFont(font);
      Sheet firstSheet = workbook.getSheet(tableName);
      addContent(excelData, firstSheet, rownum, style);
      excelData.clear();
      log.info(Thread.currentThread().getName() + " " + tableName + " generateData ended");
      writeToFile(workbook, tableName, fileType, user);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user)
      throws IOException, FileNotFoundException {
    log.info(Thread.currentThread().getName() + " " + tableName + " excel write started");
    String filePath;
    File resource = new File(fileDownloadPath + user.getUsername());
    if (!resource.exists()) {
      new File(fileDownloadPath + user.getUsername()).mkdir();
    }
    File file = new File(resource.getAbsoluteFile().getPath() + "/" + fileType);
    if (!file.exists()) {
      file.mkdir();
    }
    filePath =
        String.format(
            "%s\\%s.%s",
            resource.getAbsoluteFile().getPath() + "\\" + fileType, tableName, fileType);
    OutputStream excelFileToCreate = new FileOutputStream(new File(filePath));
    SXSSFWorkbook workbook = (SXSSFWorkbook) obj;
    workbook.write(excelFileToCreate);
    excelFileToCreate.flush();
    excelFileToCreate.close();
    log.info(Thread.currentThread().getName() + " " + tableName + " excel write ended");
  }
}
