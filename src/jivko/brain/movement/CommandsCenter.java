package jivko.brain.movement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jivko.config.ConfigurationManager;
import jivko.util.Tree;
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
  static public final String XML_DOM_ATTRIBUTE_VAL = "val";
  static public final String XML_DOM_ATTRIBUTE_MIN = "min";
  static public final String XML_DOM_ATTRIBUTE_MAX = "max";
  static public final String XML_DOM_ATTRIBUTE_PORT = "port";
  static public final String XML_DOM_ATTRIBUTE_SPEED = "speed";
  static public final String XML_DOM_ATTRIBUTE_DURATION = "duration";  
  static public final String XML_DOM_ATTRIBUTE_COMMAND = "command";  
  
  private Map<String, Command> commands = new HashMap<>();   
      
  public CommandsCenter() throws Exception {
    ConfigurationManager cm = ConfigurationManager.getInstance();
    initialize(cm.getCommandsDBPath());
  }
     
  private void initialize(String path) throws Exception {    
    readCommands(path);
    print();    
  }
  
  private Command getCommand(String commandName) {
    return commands.get(commandName);
  }
      
  public void execute(String commandName) throws Exception {
    Command c = commands.get(commandName);
    if (c == null) {
      throw new Exception("Command not found!");
    }
    
    c.execute();
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
    Command command = new Command();
    
    assert XML_DOM_NODE_COMMAND.equals(node.getNodeName());
    
    String name = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_NAME);
    command.setName(name);
    
    Command commandToCheck = commands.get(name);
    if (commandToCheck != null) {
      return commandToCheck;
    }
    
    String max = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_MAX);
    command.setMax(max);
    
    String min = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_MIN);
    command.setMin(min);
    
    String port = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_PORT);
    command.setPort(port);
    
    String value = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_VAL);
    command.setValue(value);
    
    String speed = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_SPEED);
    command.setValue(speed);
    
    String duration = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_DURATION);
    command.setValue(duration);
    
    String cmd = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_COMMAND);
    command.setCommand(cmd);
    
    NodeList nl = node.getChildNodes();
    for (int i = 0; i < nl.getLength(); ++i) {
      Node n = nl.item(i);
      
      if (n.getNodeType() != Document.ELEMENT_NODE) 
        continue;
        
      String nodeName = n.getNodeName();            
      
      if (XML_DOM_NODE_COMMAND.equals(nodeName)) {        
        NodeList nnl = n.getChildNodes();
        for (int j = 0; j < nnl.getLength(); ++j) {
          Node chNode = nnl.item(j);
          
          if (chNode.getNodeType() != Document.ELEMENT_NODE 
                  || !XML_DOM_NODE_COMMAND.equals(chNode.getNodeName()))
            continue;
          
          Command chCommand = readCommand(chNode);
          
          command.addNode(chCommand);
        }                        
      }      
    }
    
    command.compile();//NB!!!
    return command;
  }
  
  public List<Command> getCommandsFromXmlElement(Node element) throws Exception {
    List<Command> commandsFromXml = new ArrayList<>();
    
    String commandsStr = ((Element)element).getAttribute(CommandsCenter.XML_DOM_NODE_COMMANDS);
    String[] commandsVals = commandsStr.split(",");
    
    for (String s : commandsVals) {      
      commandsFromXml.add(getCommandEx(s));
    }
    
    return commandsFromXml;
  }
  
  public Command getCommandEx(String commandName) throws Exception {
    commandName = addCommandWithDifDelay(commandName);
    return commands.get(commandName);
  }
  
  //adds commads with different to default delay. If command with such delay exists nothing changes
  public String addCommandWithDifDelay(String commandName) throws Exception {        
    String result = commandName;
    
    Command command = getCommand(commandName);
    if (command != null)
      return command.getName();
    
    int idx = commandName.indexOf("=");
    
    if (idx > 0) {
      String durationStr = commandName.substring(idx + 1);
      Integer duration = Integer.parseInt(durationStr);    
      String commandBaseName = commandName.substring(0, idx);      
      
      command = getCommand(commandBaseName);
      
      if (command == null)
        throw new Exception("Command " + commandBaseName + " not found!!");
      
      if (!command.getDuration().equals(duration)) {
        Command newCommand = (Command)command.clone();
        
        //set full comand name for future
        newCommand.setName(commandName);        
        newCommand.setDuration(durationStr);
        commands.put(newCommand.getName(), newCommand);        
      } else {
        result = commandBaseName;
      }            
    }  
    
    return result;
  }
  
  public void print() {
    String identity = "";
    
    for (Entry<String, Command> e : commands.entrySet()) {
      printNode(e.getValue(), identity);  
    }    
  }
  
  public void printNode(Command c, String identity) {        
    c.print(identity);
  }
  
  private CommandsExecutor commandsExecutor = new CommandsExecutor();
  {
      commandsExecutor.start();
  }
  
  public void executeCommandList(List<Command> commandsToExecute) throws Exception {
    if (commandsToExecute != null) {
      for (Command c : commandsToExecute) {
        //c.execute();
        commandsExecutor.addCommand(c);
      }
    }
  }
  
  private class CommandsExecutor extends Thread implements Runnable {
    public BlockingQueue<Command> queue = new LinkedBlockingQueue<>();    

    public void addCommand(Command c) throws InterruptedException {
      queue.put(c);
    }
            
    @Override
    public void run() {
      while (true) {        
        try {
          Command command = queue.take();        
          command.execute();
          
          Integer duration = command.getDuration();
          System.out.println("duration = " + duration);
          Thread.sleep(duration);
          
        } catch (Exception ex) {
          ex.printStackTrace();
          Logger.getLogger(CommandsCenter.class.getName()).log(Level.SEVERE, null, ex);
        }          
      }
    }
  }
}
