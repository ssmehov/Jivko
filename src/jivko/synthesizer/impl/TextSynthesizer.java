package jivko.synthesizer.impl;

import jivko.synthesizer.Synthesizer;


public class TextSynthesizer implements Synthesizer  {

  @Override
  public void talk(String text) {
    System.err.println("ROBOT: " + text);
  }
  
}
