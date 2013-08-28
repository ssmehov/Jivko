package jivko.recognizer.impl;

import com.darkprograms.speech.recognizer.GoogleResponse;
import java.io.InputStream;
import jivko.recognizer.Recognizer;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RecognizerGoogleImpl implements Recognizer {

  @Override
  public String recognize(String fileName) throws Exception {    
            
    com.darkprograms.speech.recognizer.Recognizer recognizer = 
            new com.darkprograms.speech.recognizer.Recognizer();
    
    recognizer.setLanguage("ru-RU");

    GoogleResponse googleResponse = recognizer.getRecognizedDataForWave(fileName);
    
    return googleResponse.getResponse();
  }

  @Override
  public String recognize(InputStream inputStream) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
