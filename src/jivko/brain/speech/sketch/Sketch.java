package jivko.brain.speech.sketch;

import java.util.ArrayList;
import java.util.List;
import jivko.brain.movement.Command;
import jivko.brain.music.MusicCommand;
import jivko.brain.speech.Utterance;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Sketch extends Utterance {
  private String name;  
  private int curBufIdx = 0;  
  private List<Boff> boffs = new ArrayList<>();

  public Sketch() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
      
  public void addHumanBoff(String value) {
    boffs.add(new HumanBoff(value));
  }

  public void addRobotBoff(String value) {
    boffs.add(new RobotBoff(value, null));
  }
  
  public void addRobotBoff(String value, List<Command> commands) {
    boffs.add(new RobotBoff(value, commands));
  }
  
  public void addRobotBoff(RobotBoff robotBoff) {
    boffs.add(robotBoff);
  }
  
  public void addRobotBoff(String value, List<Command> commands, MusicCommand musicCommand) {
    addRobotBoff(new RobotBoff(value, commands, musicCommand));
  }

  public List<Boff> getBoffs() {
    return boffs;
  }
  
  public void reset() {
    curBufIdx = 0;
  }
  
  public Boff nextBoff() {
    Boff result = null;
    
    if (curBufIdx < boffs.size())
      result = boffs.get(curBufIdx++);
    
    return result;
  }
  
  public boolean isNextRobotBoff() {
    return boffs.get(curBufIdx) instanceof RobotBoff;
  }
  
  public RobotBoff nextRobotBoff() {
    Boff result = null;
    
    while (curBufIdx < boffs.size()) {
      result = boffs.get(curBufIdx++);
      
      if (result instanceof RobotBoff)
        break;
    }
          
    return (RobotBoff)result;
  }
  
  public boolean doesHumanStart() {
    return (boffs.get(0) instanceof HumanBoff);
  }
}




