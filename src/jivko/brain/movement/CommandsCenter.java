package jivko.brain.movement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
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
  
  public Command getCommand(String commandName) {
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
      addCommandWithDifDelay(s);
      commandsFromXml.add(getCommand(s));
    }
    
    return commandsFromXml;
  }
  
  //adds commads with different to default delay. If command with such delay exists nothing changes
  public void addCommandWithDifDelay(String command) throws Exception {
    if (getCommand(command) != null)
      return;
    
    int idx = command.indexOf("=");
    
    if (idx > 0) {
      String durationStr = command.substring(idx);
      Integer duration = Integer.parseInt(durationStr);    
      String commandName = command.substring(0, idx-1);      
      
      Command c = getCommand(commandName);
      
      if (c == null)
        throw new Exception("Command " + commandName + " not found!!");
      
      if (c.getDuration() != duration) {
        Command newCommand = (Command)c.clone();
        
        //set full comand name for future
        newCommand.setName(command);        
        newCommand.setDuration(durationStr);
        commands.put(newCommand.getName(), newCommand);        
      }
    }    
  }
  
  public void print() {
    String identity = "";
    
    for (Entry<String, Command> e : commands.entrySet()) {
      printNode(e.getValue(), identity);  
    }    
  }
  
  public void printNode(Command c, String identity) {        
    System.out.println(identity + XML_DOM_ATTRIBUTE_NAME + ": "+ c.getName());
    System.out.println(identity + XML_DOM_ATTRIBUTE_MAX + ": "+ c.getMax());
    System.out.println(identity + XML_DOM_ATTRIBUTE_MIN + ": "+ c.getMin());
    System.out.println(identity + XML_DOM_ATTRIBUTE_PORT + ": "+ c.getPort());
    System.out.println(identity + XML_DOM_ATTRIBUTE_VAL + ": "+ c.getValue());
    System.out.println(identity + XML_DOM_NODE_COMMAND + ": "+ c.getCommand());
                
    identity = identity + "  ";        
    List<Tree> chNodes = c.getNodes();
    for (Tree t : chNodes) {
      printNode((Command)t, identity);
    }        
  }
  
  private CommandsExecutor commandsExecutor = new CommandsExecutor();  
  
  public void executeCommandList(List<Command> commandsToExecute) throws Exception {
    if (commandsToExecute != null) {
      for (Command c : commandsToExecute) {
        //c.execute();
        commandsExecutor.addCommand(c);
      }
    }
  }
  
  private class CommandsExecutor implements Runnable {
    public BlockingQueue<Command> queue;    

    public void addCommand(Command c) {
      queue.offer(c);
    }
            
    @Override
    public void run() {
      Command command = queue.poll();
      try {
        command.execute();
        
        Command nextCommand = queue.peek();
        Thread.sleep(nextCommand.getDuration());
      } catch (Exception ex) {
        Logger.getLogger(CommandsCenter.class.getName()).log(Level.SEVERE, null, ex);
      }            
    }
  }
}
