package jivko.sphinx.util;

import jivko.recognizer.RecognizerFactory;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class UtteranceFromWavData extends UtteranceData {

  public UtteranceFromWavData(String wavName) throws Exception {
    super(null);
    
    String response = RecognizerFactory.getRecognizer().recognize(wavName);
    System.out.println(response);    
    setUtterence(response);
  }

}
