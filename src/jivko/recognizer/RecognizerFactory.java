package jivko.recognizer;

import jivko.recognizer.impl.RecognizerGoogleImpl;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RecognizerFactory {
  private static Recognizer recognizer = new RecognizerGoogleImpl();
  
  public static Recognizer getRecognizer() {
    return recognizer;
  }
}
