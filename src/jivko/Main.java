package jivko;

import java.io.IOException;
import java.net.MalformedURLException;
import jivko.brain.improvisation.JokeCreator;

import jivko.sphinx.Engine;
import jivko.synthesizer.impl.RHVoiceOnLinuxSynthesizer;
import jivko.util.Morpher;


/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Main {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws MalformedURLException, IOException, Exception {            
    
    for (int i = 0; i < 10; ++i) {
      String joke = JokeCreator.getInstance().createJoke();
      System.err.println(joke);
    }
    
    Engine.run(args);       
  }
}
