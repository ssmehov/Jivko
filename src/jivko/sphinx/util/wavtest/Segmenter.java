package jivko.sphinx.util.wavtest;

import jivko.sphinx.util.wavtest.Segmenter;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import edu.cmu.sphinx.frontend.Data;
import edu.cmu.sphinx.frontend.FrontEnd;
import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.ConfigurationManagerUtils;
import jivko.brain.speech.DialogsMemory;
import jivko.sphinx.util.UtteranceData;
import jivko.synthesizer.SynthesizerFactory;

//TODO: 
//0. Simplify interface + add cmu sphinx recognition!
//Add scetches with autotranmittion
//Add jokes logic!
//1. Split into different parts like jokes, sketches, utterances. 
//2. Implement commands and transitions
//3. Implemet sphinx recognizer part
//4. Simplify libraries and interfaces
//5. Jokes creator
//6. Speach vizualizator - face could be already drawn, we need to show only speech aplitude
//7. Speach syntezator for linux 
public class Segmenter {

  public static void main(String[] argv) throws MalformedURLException,
          IOException {
    processFile();
  }

  static private void processFile() throws MalformedURLException, IOException {

    URL configURL = Segmenter.class.getResource("frontend.config2.xml");

    ConfigurationManager cm = new ConfigurationManager(configURL);

    FrontEnd frontend = (FrontEnd) cm.lookup("endpointer");

    Microphone microphone = (Microphone) cm.lookup("microphone");

    WavWriter wavWriter = (WavWriter) cm.lookup("wavWriter");

    frontend.initialize();

    if (!microphone.startRecording()) {
      System.out.println("Cannot start microphone.");
      System.exit(1);
    }

    Data data = null;
    do {
      data = frontend.getData();

      try {
        if (data instanceof UtteranceData) {
          String utterance = ((UtteranceData) data).getUtterence();
          String answer = DialogsMemory.getInstance().findAnswer(utterance);
          SynthesizerFactory.getSynthesizer().talk(answer);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      
    } while (data != null);
  }
}
