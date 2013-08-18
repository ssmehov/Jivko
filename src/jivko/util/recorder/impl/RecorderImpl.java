package jivko.util.recorder.impl;

import com.darkprograms.speech.microphone.Microphone;
import java.io.InputStream;
import javax.sound.sampled.AudioFileFormat;
import jivko.util.recorder.Recorder;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RecorderImpl implements Recorder {

  @Override
  public void record(int timeLen, final String fileName, FileType fileType) throws Exception {    
    
    Microphone microphone = new Microphone(AudioFileFormat.Type.WAVE);
    microphone.captureAudioToFile(fileName);
        
    Thread.sleep(timeLen);          
    microphone.close();      
  }

  @Override
  public InputStream record(int timeLen, FileType fileType) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
