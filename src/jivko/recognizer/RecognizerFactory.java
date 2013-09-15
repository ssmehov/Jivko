package jivko.recognizer;

import jivko.recognizer.impl.RecognizerGoogleImpl;
//import jivko.recognizer.impl.StdInRecognizerDecorator;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RecognizerFactory {
  //private static Recognizer recognizer = new StdInRecognizerDecorator(new RecognizerGoogleImpl());
  private static Recognizer recognizer = new RecognizerGoogleImpl();
  
  public static Recognizer getRecognizer() {
    return recognizer;
  }
}
