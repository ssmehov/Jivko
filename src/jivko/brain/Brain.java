package jivko.brain;

import jivko.brain.state.CommandRecognizer;
import jivko.brain.state.StateMachineHolder;
import jivko.brain.speech.sketch.SketchesManager;
import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jivko.brain.speech.JokesManager;
import jivko.brain.speech.UtterancesManager;
import jivko.brain.state.Command;
import jivko.config.ConfigurationManager;
import jivko.util.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Brain {

  static private final String DATABASE_PATH = "D:/work/Jivko/db";
  StateMachineHolder stateMachineHolder = new StateMachineHolder();
  UtterancesManager utterancesManager = new UtterancesManager();
  JokesManager jokesManager = new JokesManager();
  SketchesManager sketchesManager = new SketchesManager();

  private Brain() {
  }

  public void initialize() throws Exception {
    ConfigurationManager cm = ConfigurationManager.getInstance();

    utterancesManager.initialize(cm.getDialogsDBPath());
    jokesManager.initialize(cm.getJokesDBPath());
    sketchesManager.initialize(cm.getScetchesDBPath());
  }

  public void reset() {
    utterancesManager.reset();
    sketchesManager.reset();
    jokesManager.reset();
  }

  public String findAnswer(String question) {
    
    if (question == null) 
      return "";
    
    Command command = CommandRecognizer.getCommand(question);

    if (command != Command.UNKNOWN_COMMAND) {
      stateMachineHolder.execute(command);
    }

    StateMachineHolder.State state = stateMachineHolder.getState();

    if (state == StateMachineHolder.State.PAUSED) {
      if (stateMachineHolder.getPrevState() != StateMachineHolder.State.PAUSED) {
        return "";
      }
      
      stateMachineHolder.setPrevState();
    }

    if (state == StateMachineHolder.State.SKETCH_CHOICE) {
      if (stateMachineHolder.getPrevState() != StateMachineHolder.State.SKETCH_CHOICE) {
        stateMachineHolder.execute(Command.START);
        return "Какую миниатюру?";
      } else {
        sketchesManager.setActiveSketch(question);
        stateMachineHolder.execute(Command.SKETCH);

        return sketchesManager.findAnswer(question);
      }
    }

    if (state == StateMachineHolder.State.SKETCHING) {
      String result = sketchesManager.findAnswer(question);
      if (result == null) {
        result = "спасибо за внимание";
        stateMachineHolder.execute(Command.RESET);
      }
      return result;
    }

    if (state == StateMachineHolder.State.JOKE_CHOICE) {      
      if (stateMachineHolder.getPrevState() != StateMachineHolder.State.JOKE_CHOICE) {
        stateMachineHolder.execute(Command.START);
        return "Шутку про что?";//add question: more joke?
      } else {
        String joke = jokesManager.findJoke(question);
        stateMachineHolder.execute(Command.RESET);
        return joke;
      }
    }   
    
    if (state == StateMachineHolder.State.JOKING) {            
      String joke = jokesManager.findJoke(question);
      stateMachineHolder.execute(Command.RESET);
      return "хорошо. слушайте." + joke;   
    }

    return utterancesManager.findAnswer(question);
  }
  private static Brain instance = null;

  public static Brain getInstance() {
    if (instance == null) {
      instance = new Brain();
    }

    return instance;
  }
}
