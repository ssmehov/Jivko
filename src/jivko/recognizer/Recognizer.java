/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jivko.recognizer;

import java.io.InputStream;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public interface Recognizer {
  
  public static final String MUTED = "muted";
  
  public String recognize(final String fileName) throws Exception;
  
  public String recognize(InputStream inputStream) throws Exception;
  
  public void muteNext();
  public void unmuteNext();
  
  public boolean isMuteNext();
}
