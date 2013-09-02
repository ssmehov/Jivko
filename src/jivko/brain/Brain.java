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
  private StateMachineHolder stateMachineHolder = new StateMachineHolder();
  private UtterancesManager utterancesManager = new UtterancesManager();
  private JokesManager jokesManager = new JokesManager();
  private SketchesManager sketchesManager = new SketchesManager();
  
  private String question = "";
  private Command activeCommand = Command.DIALOG;

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }  

  public UtterancesManager getUtterancesManager() {
    return utterancesManager;
  }

  public JokesManager getJokesManager() {
    return jokesManager;
  }

  public SketchesManager getSketchesManager() {
    return sketchesManager;
  }
    
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
    String result = "";
    
    if (question == null) 
      return "";
    
    setQuestion(question);
    
    Command command = CommandRecognizer.getCommand(question);
    if (command != Command.UNKNOWN_COMMAND) {
      activeCommand = command;      
    }

    result = stateMachineHolder.execute(activeCommand);

    return result;
  }
  
  private static Brain instance = null;

  public static Brain getInstance() {
    if (instance == null) {
      instance = new Brain();
    }

    return instance;
  }
}
