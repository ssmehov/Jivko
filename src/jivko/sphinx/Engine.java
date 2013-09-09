package jivko.sphinx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.sphinx.frontend.Data;
import edu.cmu.sphinx.frontend.FrontEnd;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import jivko.brain.Brain;
import jivko.synthesizer.SynthesizerFactory;

//TODO: 
//2. Implement commands and transitions
//5. Jokes creator
//6. Speach vizualizator - face could be already drawn, we need to show only speech aplitude
//7. Speach syntezator for linux 
public class Engine {

  public static void run(String[] argv) throws MalformedURLException,
          IOException, Exception {
    
    URL configURL = Engine.class.getResource("frontend.config.xml");

    ConfigurationManager cm = new ConfigurationManager(configURL);

    FrontEnd frontend = (FrontEnd) cm.lookup("endpointer");

    Microphone microphone = (Microphone) cm.lookup("microphone");    

    frontend.initialize();

    if (!microphone.startRecording()) {
      System.out.println("Cannot start microphone.");
      System.exit(1);
    }

    Brain.getInstance().initialize();
    
    Data data = null;
    do {
      data = frontend.getData();

      try {
        if (data instanceof UtteranceData) {
          String utterance = ((UtteranceData) data).getUtterence();
          String answer = Brain.getInstance().findAnswer(utterance);
          SynthesizerFactory.getSynthesizer().talk(answer);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      
    } while (data != null);
  }
}
