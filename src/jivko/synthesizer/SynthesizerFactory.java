package jivko.synthesizer;

import jivko.synthesizer.impl.RHVoiceOnLinuxSynthesizer;
import jivko.synthesizer.impl.SyntesizerCloudGardenImpl;
import jivko.synthesizer.impl.TextSynthesizer;
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
        //synthesizer = new SyntesizerCloudGardenImpl();
        synthesizer = new TextSynthesizer();
      } else {
        synthesizer = new RHVoiceOnLinuxSynthesizer();
      }
    }
    
    return synthesizer;
  }
}
