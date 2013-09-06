package jivko.brain.speech;

import java.util.List;
import jivko.brain.movement.Command;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Joke {

  List<Command> commands;
  String value; 

  public Joke(String value) {
    this.value = value;
  }
  
  public Joke(String value, List<Command> commands) {
    this.value = value;
    this.commands = commands;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public List<Command> getCommands() {
    return commands;
  }

  public void setCommands(List<Command> commandss) {
    this.commands = commands;
  } 
}
