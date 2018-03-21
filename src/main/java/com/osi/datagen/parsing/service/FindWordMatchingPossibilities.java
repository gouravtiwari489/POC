package com.osi.datagen.parsing.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FindWordMatchingPossibilities {

  @Value("${match.threshold}")
  String threshold;

  private static final String HRMS = "HRMS";
  private static final String SUPPLYCHAIN = "SupplyChain";
  private static final String MANUFACTURING = "Manufacturing";
  private static final String HRMSCATEGORY = "HRMS-dataset.txt";
  private static final String SUPPLYCHAINCATEGORY = "SupplyChain-dataset.txt";
  private static final String MANUFACTURINGCATEGORY = "Manufacturing-Categories.txt";

  public Map<String, List<String>> findMatchingWord(String wordToFind, String domainType,String dataType)
      throws FileNotFoundException {
    String wordArr[] = null;
    Map<String, List<String>> matchingMap = null;
    ClassLoader classLoader = getClass().getClassLoader();

    String datasetFileName = null;
    if (domainType.equalsIgnoreCase(HRMS)) datasetFileName = HRMSCATEGORY;
    else if (domainType.equalsIgnoreCase(SUPPLYCHAIN)) datasetFileName = SUPPLYCHAINCATEGORY;
    else if (domainType.equalsIgnoreCase(MANUFACTURING)) datasetFileName = MANUFACTURINGCATEGORY;

    File file = new File(classLoader.getResource(datasetFileName).getFile());

    matchingMap = findForExactWordMatch(wordToFind, matchingMap, file);

    if (null == matchingMap) {
      wordArr = splitWordToFind(wordToFind, wordArr, 2);
      wordArr = removeNullValues(wordArr);
      matchingMap = findMatchingWords(wordArr, matchingMap, file);

      int splitLength = 3, len = wordArr.length;
      while (matchingMap.size() > 1) {
        wordArr = splitWordToFind(wordToFind, wordArr, splitLength);
        wordArr = removeNullValues(wordArr);
        matchingMap = findMatchingWords(wordArr, matchingMap, file);
        splitLength++;
        if (len >= wordArr.length && splitLength > 5) {
          break;
        }
      }
    }
    Map<String, List<String>> matchingProbMap = new HashMap<>();
    for (Map.Entry<String, List<String>> elem : matchingMap.entrySet()) {
      List<String> list = elem.getValue();
      List<String> matchList = new ArrayList<>();
      boolean isAdd = false;
      for (String matchField : list) {
        Float matchProb = computeProbability(wordToFind, matchField);
        if (matchProb == 1.0f) {
        	System.out.println("matchingProbMap is:::"+matchingProbMap.toString());
          matchingProbMap.clear();
          matchList.add(matchField);
          matchingProbMap.put(elem.getKey(), matchList);
          isAdd = true;
          break;
        } else if(!isAdd && matchProb.compareTo(Float.valueOf(threshold)) >= 0.0f) {
             matchList.add(matchField);
        }
      }
      if(isAdd){
    	  break;
      }
      if (!matchList.isEmpty() && null != matchList) {
        if(!matchingProbMap.containsKey(elem.getKey()))
             matchingProbMap.put(elem.getKey(), matchList);
      }
     // System.out.println("matchField = "+elem.getKey()+" :: matchList = "+matchList.size());
    }

    Map<String, List<String>> finalmatchingMap = new LinkedHashMap<>();
		for (Map.Entry<String, List<String>> element : matchingProbMap.entrySet()) {
			String[] split = element.getKey().split("-");
			String[] split2 = split[1].split("\\^");
			String[] inputTypeSplit = dataType.split("\\(");
			List<String> dataTypes = new ArrayList<>(Arrays.asList(split2));
			for (String type : dataTypes) {
				if (type.equalsIgnoreCase(inputTypeSplit[0])) {
					finalmatchingMap.put(split[0]+"-"+type, element.getValue());
				}else {
					finalmatchingMap.put(split[0]+"-"+inputTypeSplit[0], element.getValue());
				}
			}

		}
  //  return matchingProbMap;
  	return finalmatchingMap;
    
  }

  private String[] removeNullValues(String[] wordArr) {
    List<String> list = new ArrayList<String>();

    for (String s : wordArr) {
      if (s != null && s.length() > 0) {
        list.add(s);
      }
    }

    wordArr = list.toArray(new String[list.size()]);
    return wordArr;
  }

  private Map<String, List<String>> findMatchingWords(
      String[] wordArr, Map<String, List<String>> matchingMap, File file) throws FileNotFoundException{
    matchingMap = new HashMap<>();
    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine()) {
      final String lineFromFile = scanner.nextLine();
      for (int i = 0; i < wordArr.length; i++) {
        if (lineFromFile.toLowerCase().contains(wordArr[i].toLowerCase())) {
          List<String> list = new ArrayList<>();
          String arr[] = lineFromFile.split(" ");
          for (int a = 1; a < arr.length; a++) {
            if (arr[a].toLowerCase().contains(wordArr[i].toLowerCase())) {
              list.add(arr[a]);
            }
          }
          matchingMap.put(arr[0], list);
        }
      }
    }
    scanner.close();
    return matchingMap;
  }

  private float computeProbability(String word1, String word2) {
    float match = 0;
    for (int i = 0; i < word1.length(); i++) {
      for (int j = 0 + i; j < word2.length(); j++) {
        if (word1.charAt(i) == word2.charAt(j)) {
          match++;
          if (i < word1.length() - 1) i++;
        }
      }
    }
    System.out.println(
        "Probability for " + word1 + " = " + word2 + " is : " + Math.abs(match / word2.length()));
    return Math.abs(match / word2.length());
  }

  private String[] splitWordToFind(String wordToFind, String[] wordArr, int splitLength) {
    wordArr = new String[50];
    int j = 0;
    if (wordToFind.length() <= splitLength) {
      wordArr[j] = wordToFind;
      return wordArr;
    }

    for (int m = 0; m < wordToFind.length(); m++) {
      int len = m + splitLength > wordToFind.length() ? wordToFind.length() : m + splitLength;
      String word = wordToFind.substring(m, len);
      if (word.length() > splitLength - 1) {
        wordArr[j] = wordToFind.substring(m, len);
        j++;
      }
    }
    return wordArr;
  }
  
  private Map<String, List<String>> findForExactWordMatch(
	      String wordToFind, Map<String, List<String>> matchingMap, File file)
	      throws FileNotFoundException {
	    Scanner scanner = new Scanner(file);
	    matchingMap = new HashMap<>();
	    while (scanner.hasNextLine()) {
	      final String lineFromFile = scanner.nextLine();
	      if (lineFromFile.contains(wordToFind)) {
	        List<String> list = new ArrayList<>();
	        String arr[] = lineFromFile.split(" ");
	        for (int a = 1; a < arr.length; a++) {
	          if (arr[a].equals(wordToFind)) {
	            list.add(arr[a]);
	            break;
	          }
	        }
	        scanner.close();
	        matchingMap.put(arr[0], list);
	        return matchingMap;
	      }
	    }
	    scanner.close();
	    return null;
	  }
}