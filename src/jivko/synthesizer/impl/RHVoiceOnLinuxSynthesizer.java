package jivko.synthesizer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import jivko.synthesizer.Synthesizer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RHVoiceOnLinuxSynthesizer implements Synthesizer {

  @Override
  public void talk(String text) {
    
    if (text == null)
      return;
    
    try {
      String TEXT_TO_SAY_FILENAME = "textToSay.txt";
      String RESULT_WAV_FILENAME = "out.wav";
      
      PrintWriter writer = new PrintWriter(TEXT_TO_SAY_FILENAME, "UTF-8");
      writer.println(text);
      writer.close();
      
      Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "RHVoice -i "+ TEXT_TO_SAY_FILENAME + " -o " + RESULT_WAV_FILENAME});
      
      playWav(RESULT_WAV_FILENAME);
      
    } catch (Exception ex) {
      Logger.getLogger(RHVoiceOnLinuxSynthesizer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public static void playWav(String fileName) throws Exception {        
    InputStream in = new FileInputStream(fileName);
    
    AudioStream audioStream = new AudioStream(in);

    AudioPlayer.player.start(audioStream);
  }
}
