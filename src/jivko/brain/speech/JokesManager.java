package jivko.brain.speech;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
  
  String activeJoke;

  public String getActiveJoke() {
    return activeJoke;
  }

  public void setActiveJoke(String activeJoke) {
    this.activeJoke = activeJoke;
  }
    
  List<String> jokes = new ArrayList<String>();
  
  public void initialize(String path) throws Exception {
    readJokes(path);
  }
  
  public void reset() {
    setActiveJoke(null);
  }
  
  void addJoke(String joke) {
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
    
    assert jokesNode.getNodeName() == XML_DOM_NODE_JOKES;
    
    Node jokeNode = jokesNode.getFirstChild();
    
    while (jokeNode != null) {
      if (jokeNode.getNodeType() != Document.ELEMENT_NODE) {
        jokeNode = jokeNode.getNextSibling();
        
        if (jokeNode != null)
          continue;
        else
          break;
      }
              
      addJoke(jokeNode.getTextContent());      
      
      jokeNode = jokeNode.getNextSibling();      
    }
  }
  
  public String findAnswer(String keyWords) {
    keyWords = LexicalUtils.stripServiceWords(keyWords);
    
    BestAnswerFinder baf = new BestAnswerFinder(keyWords);
    
    for (String j : jokes) {
      baf.testCandidate(j, j);
    }
    
    String result = (String)baf.getBestCandidate();
    
    return result;
  }
  
  public String findJoke(String keyWords) {
    
    setActiveJoke(findAnswer(keyWords));
    return getActiveJoke();
  }
}
