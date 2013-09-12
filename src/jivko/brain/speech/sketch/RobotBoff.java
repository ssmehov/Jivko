package jivko.brain.speech.sketch;

import java.util.List;
import jivko.brain.movement.Command;
import jivko.brain.music.MusicCommand;

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
  
  public RobotBoff(String v, List<Command> commands, MusicCommand musicCommand) {
    super(v);
    this.commands = commands;
    this.musicCommand = musicCommand;
  }    
  
  MusicCommand musicCommand;

  public MusicCommand getMusicCommand() {
    return musicCommand;
  }

  public void setMusicCommand(MusicCommand musicCommand) {
    this.musicCommand = musicCommand;
  }
  
  List<Command> commands;
  
  public List<Command> getCommands() {
    return commands;
  }

  public void setCommands(List<Command> commands) {
    this.commands = commands;
  }
}
