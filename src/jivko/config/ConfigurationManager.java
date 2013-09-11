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
    
  private final String XML_DOM_VOICE_COMMANDS_DB_PATH_MARKER = "VoiceCommandsDB";
  private final String XML_DOM_COMMANDS_DB_PATH_MARKER = "CommandsDB";  
  private final String XML_DOM_DICTIONARIES_DB_PATH_MARKER = "DictionariesDB";  
  private final String XML_DOM_DIALOGS_DB_PATH_MARKER = "DialogsDB";  
  private final String XML_DOM_JOKES_DB_PATH_MARKER = "JokesDB";
  private final String XML_DOM_SCETCHES_DB_PATH_MARKER = "SketchesDB";

  private Map<String, String> properties = new HashMap<>();
  
  
  public String getHome() throws Exception {
    String path = System.getenv(JIVKO_HOME_ENV);
    if (path == null) {
      throw new Exception(JIVKO_HOME_ENV + " is not set");
    }    
    return path + "/";
  }
  
  public String getCfgPath() throws Exception {    
    return getHome() + CFG_NAME;
  }
     
  public String getVoiceCommandsDBPath() throws Exception {
    return getHome() + properties.get(XML_DOM_VOICE_COMMANDS_DB_PATH_MARKER);    
  }
          
  public String getCommandsDBPath() throws Exception {
    return getHome() + properties.get(XML_DOM_COMMANDS_DB_PATH_MARKER);    
  }
  
  public String getDictionariesDBPath() throws Exception {
    return getHome() + properties.get(XML_DOM_DICTIONARIES_DB_PATH_MARKER);    
  }
          
  public String getDialogsDBPath() throws Exception {
    return getHome() + properties.get(XML_DOM_DIALOGS_DB_PATH_MARKER);    
  }  
  
  public String getJokesDBPath() throws Exception {
    return getHome() + properties.get(XML_DOM_JOKES_DB_PATH_MARKER);    
  }
  
  public String getScetchesDBPath() throws Exception {
    return getHome() + properties.get(XML_DOM_SCETCHES_DB_PATH_MARKER);    
  }
  
  private static final String TMP_DIR ="tmp/";
  private static final String RHVOICE_TEXT_TO_SAY_FILENAME = "textToSay.txt";
  private static final String RHVOICE_RESULT_WAV_FILENAME = "out.wav";
  private static final String SPHINX_RECOGNIZER_WAV_FILENAME = "sphinx_recog.wav";
  
  
  public String getTmpDir() {
    File theDir = new File(TMP_DIR);    
    if (!theDir.exists())
      theDir.mkdir();  

    return TMP_DIR;
  }
  
  public String getRhVoiceTextToSayFileName() {
    return getTmpDir() + RHVOICE_TEXT_TO_SAY_FILENAME;    
  }
  
  public String getRhVoiceResultWavFileName() {
    return getTmpDir() + RHVOICE_RESULT_WAV_FILENAME;    
  }
  
  public String getSpinxUtteranceRecognizerWavFileName() {
    return getTmpDir() + SPHINX_RECOGNIZER_WAV_FILENAME;    
  }    

  private ConfigurationManager() throws Exception {
    readConfiguration();
  }

  private void readConfiguration() throws Exception {
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    f.setValidating(false);
    DocumentBuilder builder = f.newDocumentBuilder();
    Document doc = builder.parse(new File(getCfgPath()));
    
    Node root = doc.getFirstChild();
    assert XML_DOM_NODE_CONFIGURATION.equals(root.getNodeName());
    
    NodeList nl = root.getChildNodes();
    for (int i = 0; i < nl.getLength(); ++i) {      
      Node n = nl.item(i);
      
      if (n.getNodeType() != Document.ELEMENT_NODE)
        continue;
      
      assert XML_DOM_NODE_ITEM.equals(n.getNodeName());
            
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
