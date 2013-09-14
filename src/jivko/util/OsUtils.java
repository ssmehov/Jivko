package jivko.util;

public class OsUtils {

  private static String OS = null;

  public static String getOsName() {
    if (OS == null) {
      OS = System.getProperty("os.name");
    }
    return OS;
  }

  public static boolean isWindows() {
    return getOsName().startsWith("Windows");
  }

  public static boolean isUnix() {
    return !isWindows();
  }
  
  public static String getStdinEncoding() {
    if (isWindows()) {
      return "windows-1251";
    } else {
      return "UTF-8";
    }
  }
}
