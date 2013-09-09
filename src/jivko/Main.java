package jivko;

import java.io.IOException;
import java.net.MalformedURLException;

import jivko.sphinx.Engine;

import jivko.test.Commands;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Main {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws MalformedURLException, IOException, Exception {            
    
    //Commands.test();
                    
    Engine.run(args);       
  }
}
