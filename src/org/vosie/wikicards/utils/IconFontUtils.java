package org.vosie.wikicards.utils;

public class IconFontUtils {
  public static final int WIKIPEDIA = 0xe600;
  public static final int DOWNLOAD = 0xe601;
  public static final int WRENCH = 0xe602;
  public static final int REMOVE = 0xe603;
  public static final int ARROW_RIGHT = 0xe604;
  public static final int ARROW_LEFT = 0xe605;

  public static String get(int code) {
    return String.valueOf((char) code);
  }

  public static String get(int code, String concat) {
    return get(code) + " " + concat;
  }
}
