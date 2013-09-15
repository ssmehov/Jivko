package jivko.sphinx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import jivko.recognizer.impl.StdInRecognizerDecorator;
import jivko.util.OsUtils;

public class TextFrontend {
  BufferedReader br;
    
  public boolean hashData() throws IOException {
    return br.ready();
  }

  public TextFrontend() {    
    try {
      br = new BufferedReader(new InputStreamReader(System.in, OsUtils.getStdinEncoding()));
    } catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
      //Logger.getLogger(StdInRecognizerDecorator.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public UtteranceData getData() throws Exception {
    //if (!hashData())
    //  return null;
    
    UtteranceData result = null;
    String utteranceData = null;        
    try {
      utteranceData = br.readLine();
    } catch (IOException ex) {
      Logger.getLogger(TextFrontend.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    System.err.println("result = " + utteranceData);
    
    if (utteranceData != null && !"".equals(utteranceData)) {
      result = new UtteranceData(utteranceData);
    }
    return result;
  }
}
