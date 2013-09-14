package jivko.sphinx;

import jivko.brain.Brain;
import jivko.synthesizer.SynthesizerFactory;


public class TextEngine extends Thread implements Runnable {
    //public BlockingQueue<Command> queue = new LinkedBlockingQueue<>();    
            
    @Override
    public void run() {
      TextFrontend textFrontend = new TextFrontend();
      while (true) {        
        try {
          UtteranceData utterance = textFrontend.getData();
          String utteranceStr = utterance.getUtterence();
          String answer = Brain.getInstance().findAnswer(utteranceStr);
          SynthesizerFactory.getSynthesizer().talk(answer);
        } catch (Exception ex) {
          ex.printStackTrace();          
        }          
      }
    }

  static TextEngine textEngine = new TextEngine();
  public static TextEngine getInstance() {
    return textEngine;
  }
}
