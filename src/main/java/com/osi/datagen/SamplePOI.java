package com.osi.datagen;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.hssf.usermodel.HSSFObjectData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.extractor.ExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;

import javassist.bytecode.ByteArray;

public class SamplePOI {
	
	public static String getEmbeddedObject() throws IOException, OpenXML4JException, XmlException {
		ArrayList<String> sheetNames= new ArrayList<String>();
		FileInputStream excelFile = new FileInputStream(new File("E:/sample.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
		
		/*Using textExtractor Factory for old file types , ie: .doc, .xls*/
		
		/*FileInputStream fis = new FileInputStream(new File("E:/sample.xlsx"));
		POIFSFileSystem fileSystem = new POIFSFileSystem(fis);
		
		// Firstly, get an extractor for the Workbook
		POIOLE2TextExtractor oleTextExtractor = 
		   ExtractorFactory.createExtractor(fileSystem);
		// Then a List of extractors for any embedded Excel, Word, PowerPoint
		// or Visio objects embedded into it.
		POITextExtractor[] embeddedExtractors =
		   ExtractorFactory.getEmbededDocsTextExtractors(oleTextExtractor);
		for (POITextExtractor textExtractor : embeddedExtractors) {
		   // If the embedded object was an Excel spreadsheet.
		   if (textExtractor instanceof ExcelExtractor) {
		      ExcelExtractor excelExtractor = (ExcelExtractor) textExtractor;
		      System.out.println(excelExtractor.getText());
		   }
		   // A Word Document
		   else if (textExtractor instanceof WordExtractor) {
		      WordExtractor wordExtractor = (WordExtractor) textExtractor;
		      String[] paragraphText = wordExtractor.getParagraphText();
		      for (String paragraph : paragraphText) {
		         System.out.println(paragraph);
		      }
		   }*/
		
		for(PackagePart data : workbook.getAllEmbedds()){
			String contentType = data.getContentType();
			if(contentType.equals("application/vnd.openxmlformats-officedocument.oleObject")){
				//OPCPackage docPackage = OPCPackage.open(data.getInputStream());
				ByteArrayInputStream ba = (ByteArrayInputStream) data.getInputStream();
				XWPFDocument  embeddedWordDocument = new XWPFDocument (ba);
				//XWPFWordExtractor we = new XWPFWordExtractor(docPackage);
				//System.out.println(we.getText());
			}
		}
		/*	XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
		XWPFDocument document = null;
		try {
			for (PackagePart pPart : workbook.getAllEmbedds()) {
                String contentType = pPart.getContentType();
                if (contentType.equals("application/vnd.ms-excel")) { //This is to read xls workbook embedded to xlsx file
                    HSSFWorkbook embeddedWorkbook = new HSSFWorkbook(pPart.getInputStream());
                    int countOfSheetXls=embeddedWorkbook.getNumberOfSheets();

     }
                else if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) { //This is to read xlsx workbook embedded to xlsx file
                     if(pPart.getPartName().getName().equals("/xl/embeddings/Workbook1.xlsx")){
                     //"/xl/embeddings/Microsoft_Excel_Worksheet12.xlsx" - Can read an Excel from a particular sheet 
                    // This is the worksheet from the Parent Excel-sheet-12

                         XSSFWorkbook embeddedWorkbook = new XSSFWorkbook(pPart.getInputStream());
                         int countOfSheetXlsx=embeddedWorkbook.getNumberOfSheets();
                            for(int i=0;i<countOfSheetXlsx;i++){
                            String name=workbook.getSheetName(i);
                            sheetNames.add(name);
                            }
                    }
                }
             // Word Document - binary (OLE2CDF) file format
                else if (contentType.equals("application/msword")) {
                    HWPFDocument document = new HWPFDocument(pPart.getInputStream());
                }
             // Word Document - OpenXML file format
                else if (contentType.equals("application/vnd.openxmlformats-officedocument.oleObject")) {
                ByteArrayInputStream doc = 	(ByteArrayInputStream) pPart.getInputStream();
                document = new XWPFDocument(doc);
                }
    }
		 
		} catch (OpenXML4JException e) {
			e.printStackTrace();
		}*/
		
		return "Success";
	}
	
	public static void main(String[] args) throws IOException, OpenXML4JException, XmlException {
		String dc =	getEmbeddedObject();
	}

}
