package com.datagenerator.das.utils;

public class DataGenUtil {

  public static String removeDoubleQuotes(String str) {
    return (str.startsWith("\"") && str.endsWith("\"") ? str.replaceAll("\"", "") : str);
  }

  public static String removeSingleQuotes(String str) {
    return (str.startsWith("'") && str.endsWith("'") ? str.replaceAll("'", "") : str);
  }

  public static String singleQuote(String str) {
    return (str != null ? "'" + str + "'" : null);
  }

  public static String doubleQuotes(String str) {
    return (str != null ? "\"" + str + "\"" : null);
  }
}
