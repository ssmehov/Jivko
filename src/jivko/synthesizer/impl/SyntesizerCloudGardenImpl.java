package jivko.synthesizer.impl;

import jivko.synthesizer.Synthesizer;

import javax.speech.*;
import javax.speech.synthesis.*;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class SyntesizerCloudGardenImpl extends BaseSynthesizer {

  private javax.speech.synthesis.Synthesizer synth = null;
  private Voice voice = null;

  public SyntesizerCloudGardenImpl() {
    try {

      EngineList list = Central.availableSynthesizers(null);
      SynthesizerModeDesc desc = null;
      boolean isFound = false;

      for (int i = 0; i < list.size(); i++) {
        if (!isFound) {
          desc = (SynthesizerModeDesc) list.elementAt(i);
          Voice[] voices = desc.getVoices();
          for (int j = 0; j < voices.length; j++) {
            if (voices[j].getName().indexOf("Aleksandr") > 0) {
              voice = voices[j];
              isFound = true;
              break;
            }
          }
        }
      }

      if (voice == null) {
        System.out.println("Unable to find a SAPI5 voice - quitting");
        System.exit(0);
      }

      synth = Central.createSynthesizer(desc);
      synth.allocate();
      synth.resume();
      synth.waitEngineState(javax.speech.synthesis.Synthesizer.ALLOCATED);
      System.out.println("Using voice " + voice);
      SynthesizerProperties props = synth.getSynthesizerProperties();
      props.setVoice(voice);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void talk(String text) {
    synth.speakPlainText(text, null);
  }
}
