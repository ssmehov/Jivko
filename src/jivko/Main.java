package jivko;

import java.io.IOException;
import java.net.MalformedURLException;

import jivko.sphinx.Engine;
import jivko.sphinx.TextEngine;

import jivko.test.TestManager;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Main {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws MalformedURLException, IOException, Exception {            
    
    TestManager.run();
    TextEngine.getInstance().start();              
    Engine.run(args);   
  }
}
