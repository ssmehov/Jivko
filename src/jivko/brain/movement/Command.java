package jivko.brain.movement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import static jivko.brain.movement.CommandsCenter.XML_DOM_ATTRIBUTE_MAX;
import static jivko.brain.movement.CommandsCenter.XML_DOM_ATTRIBUTE_MIN;
import static jivko.brain.movement.CommandsCenter.XML_DOM_ATTRIBUTE_NAME;
import static jivko.brain.movement.CommandsCenter.XML_DOM_ATTRIBUTE_PORT;
import static jivko.brain.movement.CommandsCenter.XML_DOM_ATTRIBUTE_VAL;
import static jivko.brain.movement.CommandsCenter.XML_DOM_NODE_COMMAND;
import jivko.util.ComPort;
import jivko.util.OsUtils;
import jivko.util.Tree;


/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Command extends jivko.util.Tree implements Cloneable {
  
  private static final String DEFAULT_PORT_NAME = "/dev/ttyACM0";
  private static final int DEFAULT_PORT_SPEED = 9600;
  
  
  private static final String COMMAND_SPEED_PREFIX = "T";
  private static final int DEFAULT_COMMAND_SPEED = 100;
  
  private static final int DEFAULT_COMMAND_DURATION = 1500;
  
  private static Random rand = new Random();    
                
  public Command() {
  }
  
  public Command(String name) {
    this.name = name;
  }
  
  public Command(String name, String value) {
    this(name);
    setValue(value);
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone(); //To change body of generated methods, choose Tools | Templates.
  }  
  
  private String name;
  private Integer min;
  private Integer max;  
  private Integer value;
  private String port;
  private Integer duration = DEFAULT_COMMAND_DURATION;
  private Integer speed = DEFAULT_COMMAND_SPEED;
  
  private String command;  
  
  private static Map<String, ComPort> openedPorts = new HashMap<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }    

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public Integer getMin() {
    return min;
  }

  public void setMin(String min) {
    if (min != null && !"".equals(min)) {
      this.min = Integer.parseInt(min);
    }
  }

  public Integer getMax() {
    return max;
  }

  public void setMax(String max) {    
    if (max != null && !"".equals(max)) {
      this.max = Integer.parseInt(max);
    }
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(String value) {
    if (value != null && !"".equals(value)) {
      this.value = Integer.parseInt(value);
    }
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    if (duration != null && !"".equals(duration)) {
      this.duration = Integer.parseInt(duration);
    }
  }

  public Integer getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {    
    if (speed != null && !"".equals(speed)) {
      this.speed = Integer.parseInt(speed);
    }
  }
  
  public void addPort(String portName) throws Exception {
    //this work only for unix
    if (OsUtils.isUnix()) {
      //if port is not opened yet
      if (openedPorts.get(port) == null) {
        ComPort newPort = new ComPort(portName, DEFAULT_PORT_SPEED);
        openedPorts.put(port, newPort);
      }
    }
  }
  
  public void compile() throws Exception {
    //System.err.println("before:" + command);
    
    if (command == null || "".equals(command))
      return;
    
    Integer val;
    if (value != null) {
      val = value;
    } else {
      if (min == null || max == null)
        throw new Exception("Command: " + name + ": if now val preset at least min or max should be!");
      
      val = Math.max(min, rand.nextInt(max));
    } 
    
    command = command.replaceAll("xxx", val.toString());
    command += COMMAND_SPEED_PREFIX + speed.toString();
    command += "\r\n";
    
    if (port == null || "".equals(port))
      port = DEFAULT_PORT_NAME;
    
    addPort(port);
    
    //System.err.println("after:" + command);
  }
  
  public void print() {
    print("");  
  }
  
  public void print(String identity) {
    System.out.println(identity + XML_DOM_ATTRIBUTE_NAME + ": "+ getName());
    System.out.println(identity + XML_DOM_ATTRIBUTE_MAX + ": "+ getMax());
    System.out.println(identity + XML_DOM_ATTRIBUTE_MIN + ": "+ getMin());
    System.out.println(identity + XML_DOM_ATTRIBUTE_PORT + ": "+ getPort());
    System.out.println(identity + XML_DOM_ATTRIBUTE_VAL + ": "+ getValue());
    System.out.println(identity + XML_DOM_NODE_COMMAND + ": "+ getCommand());
                
    identity = identity + "  ";        
    List<Tree> chNodes = getNodes();
    for (Tree t : chNodes) {
      ((Command)t).print(identity);
    }  
  }
  
  public void execute() throws  Exception {
    System.out.println("Executing command: " + getName());
    print();
    
    //this work only for unix
    if (command != null && !"".equals(command)) {
      if (OsUtils.isUnix()) {
        ComPort comPort = openedPorts.get(port);
        comPort.getOut().write(command.getBytes());
      }
    }
    
    for (Tree t : getNodes()) {
      ((Command)t).execute();
    }
  }          
}

