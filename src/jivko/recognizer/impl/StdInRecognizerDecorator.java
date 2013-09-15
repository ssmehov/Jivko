package jivko.recognizer.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jivko.recognizer.Recognizer;
import static jivko.recognizer.Recognizer.MUTED;
import jivko.util.OsUtils;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class StdInRecognizerDecorator implements Recognizer {

  Recognizer recognizer;
  BufferedReader br;

  public StdInRecognizerDecorator(Recognizer recognizer) {
    this.recognizer = recognizer;
    try {
      br = new BufferedReader(new InputStreamReader(System.in, OsUtils.getStdinEncoding()));
    } catch (UnsupportedEncodingException ex) {
      Logger.getLogger(StdInRecognizerDecorator.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public String recognize(String fileName) throws Exception {
    String result = null;
    
    if (isMuteNext()) {
      System.err.println("Utterence is mutted to be recognized");
      unmuteNext();
      return MUTED;
    }
    
    try {
      result = "";
      
      //Scanner s = new Scanner(System.in);            
      while (br.ready()) {
        result = br.readLine();        
        System.err.println("result = " + result);
        
        if (!result.equals(""))
          break;
      }    
      
      if (result.equals("")) {
        result = recognizer.recognize(fileName);      
      }
    } catch (IOException ioe) {
      System.out.println("IO error");      
    }
    
    return result;
  }

  @Override
  public String recognize(InputStream inputStream) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void muteNext() {
    recognizer.muteNext();
  }
  
  @Override
  public void unmuteNext() {
    recognizer.unmuteNext();
  }

  @Override
  public boolean isMuteNext() {
    return recognizer.isMuteNext();
  }
}
