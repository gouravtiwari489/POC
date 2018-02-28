package com.datagenerator.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FindWordMatchingPossibilities {

  @Value("${match.threshold}")
  String threshold;

  public Map<String, List<String>> findMatchingWord(String wordToFind, String domainType)
      throws FileNotFoundException {
    String wordArr[] = null;
    Map<String, List<String>> matchingMap = null;
    ClassLoader classLoader = getClass().getClassLoader();

    String datasetFileName =
        domainType.equalsIgnoreCase("HRMS") ? "HRMS-dataset.txt" : "SupplyChain-dataset.txt";

    File file = new File(classLoader.getResource(datasetFileName).getFile());
    Scanner scanner = new Scanner(file);

    wordArr = splitWordToFind(wordToFind, wordArr, 2);
    wordArr = removeNullValues(wordArr);
    matchingMap = findMatchingWords(wordArr, matchingMap, scanner);

    int splitLength = 3, len = wordArr.length;
    while (matchingMap.size() > 1) {
      wordArr = splitWordToFind(wordToFind, wordArr, splitLength);
      wordArr = removeNullValues(wordArr);
      scanner = new Scanner(file);
      matchingMap = findMatchingWords(wordArr, matchingMap, scanner);
      splitLength++;
      if (len >= wordArr.length && splitLength > 5) {
        break;
      }
    }
    Map<String, List<String>> matchingProbMap = new HashMap<>();
    for (Map.Entry<String, List<String>> elem : matchingMap.entrySet()) {
      List<String> list = elem.getValue();
      List<String> matchList = new ArrayList<>();
      list.forEach(
          new Consumer<String>() {
            @Override
            public void accept(String matchField) {
              Float matchProb = computeProbability(wordToFind, matchField);
              if (matchProb.compareTo(Float.valueOf(threshold)) >= 0.0f) {
                matchList.add(matchField);
              }
            }
          });
      if (!matchList.isEmpty() && null != matchList) matchingProbMap.put(elem.getKey(), matchList);
    }
    scanner.close();
    return matchingProbMap;
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
      String[] wordArr, Map<String, List<String>> matchingMap, final Scanner scanner) {
    matchingMap = new HashMap<>();
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
}
