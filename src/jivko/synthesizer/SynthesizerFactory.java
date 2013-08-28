package jivko.synthesizer;

import jivko.synthesizer.impl.SyntesizerCloudGardenImpl;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class SynthesizerFactory {
  private static Synthesizer synthesizer = new SyntesizerCloudGardenImpl();
  
  public static Synthesizer getSynthesizer() {
    return synthesizer;
  }
}
