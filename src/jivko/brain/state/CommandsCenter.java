package jivko.brain.state;

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
public class CommandsCenter { 
  
  static public final String XML_DOM_NODE_COMMANDS = "commands";
  static public final String XML_DOM_NODE_COMMAND = "command";
  
  static public final String XML_DOM_ATTRIBUTE_NAME = "name";
  static public final String XML_DOM_ATTRIBUTE_COMMAND = "command";
    
  private Map<String, Command> commands = new HashMap<>();
  
  public CommandsCenter() throws Exception {
    ConfigurationManager cm = ConfigurationManager.getInstance();
    initialize(cm.getVoiceCommandsDBPath());
  }
     
  private void initialize(String path) throws Exception {    
    readCommands(path);
    print();
  }    
  
  private static CommandsCenter instance = null;
  
  public static CommandsCenter getInstance() throws Exception {    
    if (instance == null)
      instance = new CommandsCenter();
    
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

      Command newCommand = readCommand(commandNode);
      
      commands.put(newCommand.getName(), newCommand);

      commandNode = commandNode.getNextSibling();
    }        
  }
  
  private Command readCommand(Node node) throws Exception {
    Command resultCommand = new Command();
    
    assert XML_DOM_NODE_COMMAND.equals(node.getNodeName());
    
    String name = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_NAME);
    resultCommand.setName(name);
                        
    Command existedCommand = commands.get(name);
    if (existedCommand != null) {
      resultCommand = existedCommand;      
    } 
    
    String value = ((Element)node).getTextContent();
    resultCommand.addPossibleValue(value);
        
    return resultCommand;
  }
  
  public void print() {
    String identity = "";
    
    for (Entry<String, Command> e : commands.entrySet()) {
      e.getValue().print();
    }    
  }
    
  public String getCommand(String utterance) {
    String result = Command.UNKNOWN_COMMAND;
        
    for (Entry <String, Command> e : commands.entrySet()) {
      if (e.getValue().isContainValue(utterance)) {
        result = e.getKey();
      }
    }
    
    return result;
  }
}
