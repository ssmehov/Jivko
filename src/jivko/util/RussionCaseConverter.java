package jivko.util;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RussionCaseConverter {

  public String getFromRoditelniy(String in) {
    String result = "";
    
    if (in.length() < 1)
      return result;
    
    int lastChIdx = in.length() - 1;
    char ch = in.charAt(lastChIdx);
    
    if (ch == 'у') {
      result = in.substring(0, lastChIdx);
      result += 'а';
    }
    
    return result;
  }
}
