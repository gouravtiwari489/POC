package com.osi.datagen.datageneration.service;

import org.springframework.util.StringUtils;

public class DataGenUtil {

  private static final String FORWARD_SLACE = "\"";
  private static final String COMMA = "'";

  public static String removeDoubleQuotes(String str) {
    return (str.startsWith(FORWARD_SLACE) && str.endsWith(FORWARD_SLACE)
        ? StringUtils.replace(str, FORWARD_SLACE, "")
        : str);
  }

  public static String removeSingleQuotes(String str) {
    return (str.startsWith(COMMA) && str.endsWith(COMMA)
        ? StringUtils.replace(str, COMMA, "")
        : str);
  }

  public static String singleQuote(String str) {
    return (str != null ? COMMA.concat(str).concat(COMMA) : null);
  }

  public static String doubleQuotes(String str) {
    return (str != null ? FORWARD_SLACE.concat(str).concat(FORWARD_SLACE) : null);
  }
}
