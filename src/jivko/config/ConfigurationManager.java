package jivko.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class ConfigurationManager {
  private final String JIVKO_HOME_ENV = "JIVKO_HOME";
  private final String CFG_NAME = "/config/config.xml";
  
  private final String XML_DOM_NODE_CONFIGURATION = "configuraion";
  private final String XML_DOM_NODE_ITEM = "item";
  private final String XML_DOM_NODE_ITEM_NAME_ATTR = "name";  
  
  private final String XML_DOM_DICTIONARIES_DB_PATH_MARKER = "DictionariesDB";  
  private final String XML_DOM_DIALOGS_DB_PATH_MARKER = "DialogsDB";  
  private final String XML_DOM_JOKES_DB_PATH_MARKER = "JokesDB";
  private final String XML_DOM_SCETCHES_DB_PATH_MARKER = "SketchesDB";

  private Map<String, String> properties = new HashMap<String, String>();
  
  public String getCfgPath() throws Exception {
    String path = System.getenv(JIVKO_HOME_ENV);
    if (path == null) {
      throw new Exception(JIVKO_HOME_ENV + " is not set");
    }
    
    return path + CFG_NAME;
  }
    
  public String getDictionariesDBPath() {
    return properties.get(XML_DOM_DICTIONARIES_DB_PATH_MARKER);    
  }
          
  public String getDialogsDBPath() {
    return properties.get(XML_DOM_DIALOGS_DB_PATH_MARKER);    
  }  
  
  public String getJokesDBPath() {
    return properties.get(XML_DOM_JOKES_DB_PATH_MARKER);    
  }
  
  public String getScetchesDBPath() {
    return properties.get(XML_DOM_SCETCHES_DB_PATH_MARKER);    
  }  

  private ConfigurationManager() throws Exception {
    readConfiguration();
  }

  public void readConfiguration() throws Exception {
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    f.setValidating(false);
    DocumentBuilder builder = f.newDocumentBuilder();
    Document doc = builder.parse(new File(getCfgPath()));
    
    Node root = doc.getFirstChild();
    assert root.getNodeName() == XML_DOM_NODE_CONFIGURATION;
    
    NodeList nl = root.getChildNodes();
    for (int i = 0; i < nl.getLength(); ++i) {      
      Node n = nl.item(i);
      
      if (n.getNodeType() != Document.ELEMENT_NODE)
        continue;
      
      assert n.getNodeName() == XML_DOM_NODE_ITEM;
            
      NamedNodeMap map = n.getAttributes();
      Node nameAttrib = map.getNamedItem(XML_DOM_NODE_ITEM_NAME_ATTR);
      properties.put(nameAttrib.getNodeValue(), n.getTextContent());
    }
  }
  
  private static ConfigurationManager instance = null;

  public static ConfigurationManager getInstance() throws Exception {
    if (instance == null) {
      instance = new ConfigurationManager();  
    }
    
    return instance;
  }
}
