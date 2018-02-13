package com.datagenerator.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.datagenerator.demo.serviceImpl.Dataset;


public class CreateFileAndWrite {
	  
	  static HSSFWorkbook workbook = null;
	  
	  public static void insertDataIntoSheet(Map<String, String> fieldMap,String excelFilePath, String sheetName)
			  throws Exception {
		int rownum = 0;
		HSSFWorkbook workbook=createExcel(excelFilePath,sheetName); 
		HSSFSheet firstSheet = workbook.getSheet(sheetName);
		HSSFRow headerRow = firstSheet.createRow(rownum);
		//headerRow.setRowStyle(style);
	    headerRow.setHeightInPoints(40);
	    List<String> headerRow1 = new ArrayList<String>();
	    for(Map.Entry<String, String> entry : fieldMap.entrySet()) {
	    headerRow1.add(entry.getKey());
	    }
	    List<List<String>> recordToAdd = new ArrayList<List<String>>();
	    recordToAdd.add(headerRow1);
	    for(int i=0;i<1000;i++){
	    	List<String> firstRow = new ArrayList<String>();
	    	for(Map.Entry<String, String> entry : fieldMap.entrySet()) {
	    		if(entry.getValue().contains("int")){
	    			firstRow.add(i+"");
	    		}else if(entry.getValue().contains("decimal")){
	    			firstRow.add(i+new Random().nextInt(23456)*0.2+"");
	    		}
	    		firstRow.add(Dataset.getRandomData(entry.getKey()));
	    		
	    	    }
	    	recordToAdd.add(firstRow);
	    }
	   
	    addContent(recordToAdd,firstSheet,rownum);
	    FileOutputStream outputStream = new FileOutputStream(
	    		excelFilePath);
        workbook.write(outputStream);
        outputStream.close();
	  }
	 
	   static void addContent(List<List<String>> l1, HSSFSheet firstSheet,int rownum) throws Exception {
	    try {
	      for (int j = 0; j < l1.size(); j++) {
	        HSSFRow row = firstSheet.createRow(rownum);
	        List<String> l2= l1.get(j);
	        for(int k=0; k<l2.size(); k++)
	        {
	          HSSFCell cell = row.createCell(k);
	          cell.setCellValue(l2.get(k));
	        }
	        rownum++;
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	    }
	  }
	   public static HSSFWorkbook createExcel(String excelFilePath, String sheetName)
		        throws IOException {
		    FileOutputStream fos = null;
		    if(workbook==null || workbook.getSheet(sheetName) == null)
		    {
			    try {
			        synchronized (CreateFileAndWrite.class) {
			        	if(workbook==null)
			        	{
					        if (new File(excelFilePath).createNewFile()) {
					            workbook = new HSSFWorkbook();
					        } else {
					            POIFSFileSystem pfs = new POIFSFileSystem(new FileInputStream(
					            		excelFilePath));
					            workbook = new HSSFWorkbook(pfs);
					        }
					        if (workbook.getSheet(sheetName) == null) {
					            fos = new FileOutputStream(excelFilePath);
					            workbook.createSheet(sheetName);
					            workbook.write(fos);
					        }
			        	}
			        	else
			        	{
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