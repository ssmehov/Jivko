package jivko.brain.speech.sketch;

import java.util.List;
import jivko.brain.command.Command;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class RobotBoff extends Boff {

  public RobotBoff(String v) {
    super(v);
  }

  public RobotBoff(String v, List<Command> commands) {
    super(v);
    this.commands = commands;
  }    
  
  List<Command> commands;
  
  public List<Command> getCommands() {
    return commands;
  }

  public void setCommands(List<Command> commandss) {
    this.commands = commands;
  }
}
