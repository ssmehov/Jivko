package jivko.recognizer.impl;

import com.darkprograms.speech.recognizer.GoogleResponse;
import java.io.InputStream;
import jivko.recognizer.Recognizer;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RecognizerGoogleImpl implements Recognizer {

  private boolean isMuteNext = false;
  
  @Override
  public String recognize(String fileName) throws Exception { 
    System.out.println("recognize start");
    long startTime = System.currentTimeMillis();
            
    if (isMuteNext) {
      System.err.println("Utterence is mutted to be recognized");
      isMuteNext = false;
      return MUTED;
    }
    
    try {
      com.darkprograms.speech.recognizer.Recognizer recognizer = 
            new com.darkprograms.speech.recognizer.Recognizer();
    
      recognizer.setLanguage("ru-RU");

      GoogleResponse googleResponse = recognizer.getRecognizedDataForWave(fileName);

      long stopTime = System.currentTimeMillis();
      long elapsedTime = stopTime - startTime;    
      System.out.println("recognize end. total time = " + elapsedTime);

      return googleResponse.getResponse();
    } catch (Exception e) {
      e.printStackTrace();;
    }
    
    return null;
  }

  @Override
  public String recognize(InputStream inputStream) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void muteNext() {
    isMuteNext = true;
  }
  
  @Override
  public void unmuteNext() {
    isMuteNext = false;
  }

  @Override
  public boolean isMuteNext() {
    return isMuteNext;
  }
}
