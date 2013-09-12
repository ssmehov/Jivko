package jivko.brain.music;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jivko.config.ConfigurationManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class MusicCommandsCenter { 
  
  static public final String MUSIC_COMMANDS_FILE = "music_commands.xml";
  
  static public final String XML_DOM_NODE_COMMANDS = "commands";
  static public final String XML_DOM_NODE_COMMAND = "command";
  
  static public final String XML_DOM_ATTRIBUTE_MUSIC = "music";
  
  static public final String XML_DOM_ATTRIBUTE_NAME = "name";
  static public final String XML_DOM_ATTRIBUTE_FILE = "file";
    
  private Map<String, MusicCommand> commands = new HashMap<>();
  
  public MusicCommandsCenter() throws Exception {
    ConfigurationManager cm = ConfigurationManager.getInstance();
    initialize(cm.getMusicCommandsDBPath());
  }
     
  private void initialize(String path) throws Exception {    
    readCommands(path + MUSIC_COMMANDS_FILE);
    print();
  }    
  
  private static MusicCommandsCenter instance = null;
  
  public static MusicCommandsCenter getInstance() throws Exception {    
    if (instance == null)
      instance = new MusicCommandsCenter();
    
    return instance;
  }
  
  private void readCommands(String path) throws Exception {
    
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    f.setValidating(false);
    DocumentBuilder builder = f.newDocumentBuilder();
    Document doc = builder.parse(new File(path));
    
    Node commandsNode = doc.getFirstChild();

    while (commandsNode.getNodeType() != Document.ELEMENT_NODE 
            && commandsNode.getNextSibling() != null)
      commandsNode = commandsNode.getNextSibling();
    
    assert XML_DOM_NODE_COMMANDS.equals(commandsNode.getNodeName());
    
    Node commandNode = commandsNode.getFirstChild();
    while (commandNode != null) {
      
      if (commandNode.getNodeType() != Document.ELEMENT_NODE) {
        commandNode = commandNode.getNextSibling();

        if (commandNode != null) {
          continue;
        } else {
          break;
        }
      }

      MusicCommand newCommand = readCommand(commandNode, path);
      
      commands.put(newCommand.getName(), newCommand);

      commandNode = commandNode.getNextSibling();
    }        
  }
  
  private MusicCommand readCommand(Node node, String path) throws Exception {
    MusicCommand resultCommand = new MusicCommand();
    
    assert XML_DOM_NODE_COMMAND.equals(node.getNodeName());
    
    String name = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_NAME);
    resultCommand.setName(name);
                        
    MusicCommand existedCommand = commands.get(name);
    if (existedCommand != null) {
      resultCommand = existedCommand;      
    }
    
    String file = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_FILE);
    
    String musicDir = ConfigurationManager.getInstance().getMusicCommandsDBPath();
    resultCommand.setFile(musicDir + file);
        
    return resultCommand;
  }
  
  public void print() {
    String identity = "";
    
    for (Entry<String, MusicCommand> e : commands.entrySet()) {
      e.getValue().print();
    }    
  }
    
  public MusicCommand getCommand(String key) {   
    return commands.get(key);
  }
  
  public MusicCommand getCommandFromXmlElement(Node element) throws Exception {        
    String musicCommandName = ((Element)element).getAttribute(XML_DOM_ATTRIBUTE_MUSIC);    
    
    return getCommand(musicCommandName);
  }
  
  public void executeCommand(MusicCommand musicCommand) throws Exception {
    if (musicCommand != null)
      musicCommand.execute();
  }
}
