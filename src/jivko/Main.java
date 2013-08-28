package jivko;

import jivko.unused.dictionary.Dictionary;
import jivko.recognizer.Recognizer;
import jivko.recognizer.impl.RecognizerGoogleImpl;
import jivko.synthesizer.Synthesizer;
import jivko.synthesizer.impl.SyntesizerCloudGardenImpl;
import jivko.util.recorder.Recorder;
import jivko.util.recorder.impl.RecorderImpl;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Main {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    final int recordTime = 4000;
    
    Recorder recorder = new RecorderImpl();
    Recognizer recognizer = new RecognizerGoogleImpl();
    Synthesizer synthesizer = new SyntesizerCloudGardenImpl();
    
    Dictionary dictionary = new Dictionary();
    
    try {
      String fileName = "D:/tmp/tmp777.wav";
      
      while (true) { 
        recorder.record(recordTime, fileName, Recorder.FileType.FLAC);      
        String response = recognizer.recognize(fileName);
        String answer = dictionary.findAnswer(response);
        synthesizer.talk(answer);
      }
    } catch (Exception ex) {
      System.out.println(ex.toString());
      ex.printStackTrace();
    }
  }
}
