package jivko.brain.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CommandSpeedDeterminator {
  
  private static final String TILT_MARKER = "HEAD_TILT";
  private static final String PAN_MARKER = "HEAD_PAN";
  private static final String TIP_MARKER = "HEAD_TIP";    
  
  private static Map<String, Integer> states = new HashMap<>();

  static {
    states.put(TILT_MARKER, 0);
    states.put(PAN_MARKER, 0);
    states.put(TIP_MARKER, 0);
  }    
  
  private static Integer computeNewSpeed(int curVal, int newVal, Integer min, Integer max, int defaultSpeed) {
    if (curVal == 0) {      
      if (min != null && max != null) {
        curVal = (min + max) / 2;
      }
    }
    
    int step = Math.abs(curVal - newVal);
    //double relStep = ((double)step) / totalInterval;        
    //return new Integer((int) (defauletSpeed * relStep));
    
    //return new Integer((int) (defaultSpeed * step));
                
    int sp = (int) Math.round((double)step / Command.DEFAULT_COMMAND_SPEED_KOEF);    
    return sp;
  }
  
  private static Integer update(Command command, int newVal) {
    for (Entry<String, Integer> e : states.entrySet()) {
      if (command.getName().contains(e.getKey())) {
        Integer newSpeed = computeNewSpeed(
                e.getValue(), 
                newVal, 
                command.getMin(),
                command.getMax(), 
                Command.DEFAULT_COMMAND_SPEED);        
        states.put(e.getKey(),newSpeed);
        return newSpeed;
      }
    }  
    
    return Command.DEFAULT_COMMAND_SPEED * newVal;
  }
  
  public static int getReccomendSpeed(Command command, int newVal) {
    return update(command, newVal);
  }          
}

