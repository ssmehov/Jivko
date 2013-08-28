package jivko.brain.speech;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class JokesManager {
  static private final String XML_DOM_NODE_JOKES = "jokes";
  static private final String XML_DOM_NODE_JOKE = "joke";
  
  List<String> jokes = new ArrayList<String>();
  
  public void init(String path) throws Exception {
    readJokes(path);
  }
  
  void addJoke(String joke) {
    jokes.add(joke);
  }
  
  public void readJokes(String path) throws Exception {
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
    
    do {
      if (jokesNode.getNodeType() != Document.ELEMENT_NODE)
        continue;            
      
      addJoke(jokeNode.getTextContent());      
      
      jokesNode = jokesNode.getNextSibling();
      
    } while (jokesNode.getNextSibling() != null);              
  }
  
  public String findJoke(List<String> keyWords) {
    return "";
  }
}
