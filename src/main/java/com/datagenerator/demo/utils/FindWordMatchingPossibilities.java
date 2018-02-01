package com.datagenerator.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class FindWordMatchingPossibilities {

	/*public static void main(String[] args) throws FileNotFoundException {
		String wordToFind = "dId";
		findMathingWord(wordToFind);
	}*/

	public Map<String,String> findMatchingWord(String wordToFind) throws FileNotFoundException {
		String wordArr[] = null;
		Map<String,String> matchingMap = null;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("emp-dataset.txt").getFile());
		Scanner scanner = new Scanner(file);
		
		wordArr = splitWordToFind(wordToFind, wordArr, 2);
		wordArr = removeNullValues(wordArr);
		
	    matchingMap = findMatchingWords(wordArr, matchingMap, scanner);
		System.out.println("First time match #################################");
	    for(Map.Entry<String, String> elem : matchingMap.entrySet()){
			System.out.println(elem.getKey()+" :: "+elem.getValue());
		}
	    
	    int splitLength = 3, len=wordArr.length;
		while(matchingMap.size() >1){
			wordArr = splitWordToFind(wordToFind, wordArr, splitLength);
			wordArr = removeNullValues(wordArr);
			scanner = new Scanner(file);
			matchingMap = findMatchingWords(wordArr, matchingMap, scanner);
			splitLength++;
			if(len == wordArr.length){
				break;
			}
		}
		System.out.println("Second time match #################################");
		for(Map.Entry<String, String> elem : matchingMap.entrySet()){
			System.out.println(elem.getKey()+" :: "+elem.getValue());
		}
		scanner.close();
		return matchingMap;
	}

	private static String[] removeNullValues(String[] wordArr) {
		List<String> list = new ArrayList<String>();

	    for(String s : wordArr) {
	       if(s != null && s.length() > 0) {
	          list.add(s);
	       }
	    }

	    wordArr = list.toArray(new String[list.size()]);
		return wordArr;
	}

	private static Map<String, String> findMatchingWords(String[] wordArr, Map<String, String> matchingMap, final Scanner scanner) {
		matchingMap = new HashMap<>();
		while(scanner.hasNextLine()){
			final String lineFromFile = scanner.nextLine();
			
			for(int i=0; i<wordArr.length; i++){
				if (lineFromFile.toLowerCase().contains(wordArr[i].toLowerCase())) {
					String arr[]= lineFromFile.split(" ");
					for(int a=0; a<arr.length; a++){
						//System.out.println(arr[a]+" :: "+wordArr[i]);
						if(arr[a].toLowerCase().contains(wordArr[i].toLowerCase())){
							matchingMap.put(arr[0],arr[a]);
						}	
					}	
				}
			}
		}
		return matchingMap;
	}

	private static String[] splitWordToFind(String wordToFind, String[] wordArr, int splitLength) {
		wordArr = new String[50];
		int j=0;
		if(wordToFind.length() <= splitLength){
			wordArr[j] = wordToFind;
			return wordArr;
		}	
		
		for(int m=0; m<wordToFind.length(); m++){
			int len = m+splitLength>wordToFind.length()?wordToFind.length():m+splitLength;
			String word = wordToFind.substring(m, len);
			if(word.length()>splitLength-1){
				wordArr[j] = wordToFind.substring(m, len);
				j++;
			}	
		}
		return wordArr;
	}
}
