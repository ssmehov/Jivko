package jivko.synthesizer.impl;

import jivko.synthesizer.Synthesizer;


public class TextSynthesizer extends BaseSynthesizer {

  @Override
  public void talk(String text) {
    System.err.println("ROBOT: " + text);
  }
    
}
