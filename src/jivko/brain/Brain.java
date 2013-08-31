package jivko.brain;

import jivko.brain.state.CommandsList;
import jivko.brain.state.StateMachine;
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
  
  StateMachine stateMachine = new StateMachine();
  
  UtterancesManager utterancesManager = new UtterancesManager();
  JokesManager jokesManager = new JokesManager();
  SketchesManager sketchesManager = new SketchesManager();  

  private Brain() throws Exception {
    initialize();
  }
  
  private void initialize() throws Exception {
    ConfigurationManager cm = ConfigurationManager.getInstance();            
    
    utterancesManager.initialize(cm.getDialogsDBPath());
    jokesManager.initialize(cm.getJokesDBPath());
    sketchesManager.initialize(cm.getScetchesDBPath());
  }
  
  public String findAnswer(String question) {
    Command command = CommandsList.getCommand(question);
    
    if (command != Command.UNKNOWN_COMMAND) {
      stateMachine.execute(command);
    }
    
    int state = stateMachine.getCurState();        
    
    if (state == StateMachine.SKETCH_SHOW_STATE) {
      return sketchesManager.findAnswer(question);
    }
    
    if (state == StateMachine.JOKES_TELL_STATE) {
      return jokesManager.findJoke(question);
    }
    
    return utterancesManager.findAnswer(question);
  }
  
  private static Brain instance = null;

  public static Brain getInstance() throws Exception {
    if (instance == null) {
      instance = new Brain();  
    }
    
    return instance;
  }
}
