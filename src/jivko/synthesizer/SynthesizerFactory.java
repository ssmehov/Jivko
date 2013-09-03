package jivko.synthesizer;

import jivko.synthesizer.impl.RHVoiceOnLinuxSynthesizer;
import jivko.synthesizer.impl.SyntesizerCloudGardenImpl;
import jivko.util.OsUtils;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class SynthesizerFactory {
  private static Synthesizer synthesizer = null;
  
  public static Synthesizer getSynthesizer() {
    if (synthesizer == null) {
      if (OsUtils.isWindows()) {
        synthesizer = new SyntesizerCloudGardenImpl();
      } else {
        synthesizer = new RHVoiceOnLinuxSynthesizer();
      }
    }
    
    return synthesizer;
  }
}
