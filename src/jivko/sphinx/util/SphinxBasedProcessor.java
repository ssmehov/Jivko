package jivko.sphinx.util;

import edu.cmu.sphinx.frontend.Data;
import edu.cmu.sphinx.frontend.FrontEnd;
import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.frontend.util.WavWriter;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.ConfigurationManagerUtils;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class SphinxBasedProcessor {

  String configFile = null;

  public static void main(String[] argv) throws MalformedURLException,
          IOException {

    URL configURL = RecorderProcessor.class.getResource("frontend.config.xml");
    ConfigurationManager cm = new ConfigurationManager(configURL);

    process(cm);
  }

  static private void process(ConfigurationManager cm) throws IOException {

    FrontEnd frontend = (FrontEnd) cm.lookup("endpointer");
    frontend.initialize();

    final Microphone microphone = (Microphone) cm.lookup("microphone");
    microphone.startRecording();

    Thread stopper = new Thread(new Runnable() {
      public void run() {
        try {
          Thread.sleep(10000);
          microphone.stopRecording();          
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        }        
      }
    });
    stopper.start();
    
    Data data = null;
    do {
      data = frontend.getData();
    } while (data != null);    
  }
}
