package com.osi.datagen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;


public class TikaExtractorSample {
	
	public static void main(String[] args) throws IOException {
		FileInputStream excelFile = new FileInputStream(new File("E:/sample.xlsx"));
		Detector dc = new DefaultDetector();
		Parser ps = new AutoDetectParser(dc);
		Metadata md = new Metadata();
		Tika tk = new Tika();
		tk.parse(excelFile, md);
		System.out.println(md.toString());
	}

}
