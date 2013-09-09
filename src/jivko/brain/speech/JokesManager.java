package jivko.brain.speech;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jivko.brain.movement.Command;
import jivko.brain.movement.CommandsCenter;
import jivko.util.BestAnswerFinder;
import jivko.util.LexicalUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class JokesManager {
  static private final String XML_DOM_NODE_JOKES = "jokes";
  static private final String XML_DOM_NODE_JOKE = "joke";
  
  Joke activeJoke;

  public Joke getActiveJoke() {
    return activeJoke;
  }

  public void setActiveJoke(Joke activeJoke) {
    this.activeJoke = activeJoke;
  }
    
  List<Joke> jokes = new ArrayList<>();
  
  public void initialize(String path) throws Exception {
    readJokes(path);
  }
  
  public void reset() {
    setActiveJoke(null);
  }
  
  void addJoke(Joke joke) {
    jokes.add(joke);
  }
  
  private void readJokes(String path) throws Exception {
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    f.setValidating(false);
    DocumentBuilder builder = f.newDocumentBuilder();
    Document doc = builder.parse(new File(path));
    
    Node jokesNode = doc.getFirstChild();

    while (jokesNode.getNodeType() != Document.ELEMENT_NODE 
            && jokesNode.getNextSibling() != null)
      jokesNode = jokesNode.getNextSibling();
    
    assert XML_DOM_NODE_JOKES.equals(jokesNode.getNodeName());
    
    Node jokeNode = jokesNode.getFirstChild();
    
    while (jokeNode != null) {
      if (jokeNode.getNodeType() != Document.ELEMENT_NODE) {
        jokeNode = jokeNode.getNextSibling();
        
        if (jokeNode != null)
          continue;
        else
          break;
      }
      
      List<Command> jokeCommands = CommandsCenter.getInstance().getCommandsFromXmlElement(jokeNode);      
      String jokeContent = jokeNode.getTextContent();
      Joke joke = new Joke(jokeContent, jokeCommands);
              
      addJoke(joke);
      
      jokeNode = jokeNode.getNextSibling();      
    }
  }
  
  public String findAnswer(String keyWords) throws Exception {
    String answer = null;
    
    keyWords = LexicalUtils.stripServiceWords(keyWords);
    
    BestAnswerFinder baf = new BestAnswerFinder(keyWords);
    
    for (Joke j : jokes) {
      baf.testCandidate(j, j.getValue());
    }
    
    Joke result = (Joke)baf.getBestCandidate();
    
    if (result != null) {
      answer = result.getValue();
      CommandsCenter.getInstance().executeCommandList(result.getCommands());
    }
    
    return answer;
  }   
}
