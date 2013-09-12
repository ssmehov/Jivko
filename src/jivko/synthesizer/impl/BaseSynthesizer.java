package jivko.synthesizer.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import jivko.synthesizer.Synthesizer;

public abstract class BaseSynthesizer implements Synthesizer {
              
  @Override
  public void talkWithPause(String text, int pauseMs) {
    talk(text);
    try {
      Thread.sleep(pauseMs);
    } catch (InterruptedException ex) {
      Logger.getLogger(BaseSynthesizer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public void talkWithDelay(String text, int delayMs) {
    try {
      Thread.sleep(delayMs);
    } catch (InterruptedException ex) {
      Logger.getLogger(BaseSynthesizer.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    talk(text);
  }
  
}
