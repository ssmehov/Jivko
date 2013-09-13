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
    this(v, commands);    
    this.musicCommand = musicCommand;
  }

  public RobotBoff(String v, List<Command> commands, MusicCommand musicCommand, Integer speachDelay) {
    this(v, commands, musicCommand);    
    this.speachDelay = speachDelay;
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
  
  private Integer speachDelay;
    
  public Integer getSpeachDelay() {
    return speachDelay;
  }

  public void setSpeachDelay(String speachDelay) {
    if (speachDelay != null && !"".equals(speachDelay)) {
      this.speachDelay = Integer.parseInt(speachDelay);
    }
  }
}
