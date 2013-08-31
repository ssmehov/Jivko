package jivko;

import java.io.IOException;
import java.net.MalformedURLException;

import jivko.sphinx.Engine;


/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Main {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws MalformedURLException, IOException, Exception {            
        
    Engine.run(args);
  }
}
