package jivko.util;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class LexicalUtils {

  private static String[] serviceWords = {"про", "об", "лучше", "или"};
  
  public static String stripServiceWords(String in) {
    for (String s : serviceWords) {
      String regexp = "\\s*\\b" + s + "\\b\\s*";
      in = in.replaceAll(regexp, "");
    }
    
    return in;
  }
}
