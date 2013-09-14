package jivko.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import jivko.util.OsUtils;

public class StdIn implements Testable {

  BufferedReader br;

  public StdIn() {
    try {
      br = new BufferedReader(new InputStreamReader(System.in, OsUtils.getStdinEncoding()));
    } catch (UnsupportedEncodingException ex) {
      Logger.getLogger(StdIn.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public void test() throws Exception {       
    String result = br.readLine();    

    for (int i = 0; i < 10; ++i) {
      while (br.ready()) {
        result = br.readLine();
        System.err.println("result = " + result.getBytes("UTF-8"));

        if (!result.equals("")) {
          break;
        }
      }
    }
  }
}
