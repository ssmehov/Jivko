package jivko.brain.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class StateMachine {

  public static final int DIALOG_STATE = 0;
  public static final int PAUSE_STATE = 1;
  public static final int SKETCH_SHOW_STATE = 2;
  public static final int JOKES_TELL_STATE = 3;
  public static final int ANY_STATE = 4;  
  
  int transitionsTableSize = ANY_STATE;
  private int[][] transitions = new int[transitionsTableSize][transitionsTableSize];  
  {
    //Arrays.fill(transitions, 0);   
    transitions[DIALOG_STATE][PAUSE_STATE] = 1;
    transitions[DIALOG_STATE][SKETCH_SHOW_STATE] = 1;
    
    transitions[PAUSE_STATE][ANY_STATE] = 1;    
  }
  
  Map<Command, Integer> commandsToStatesMap = new HashMap<Command, Integer>();
  {
    commandsToStatesMap.put(Command.PAUSE, PAUSE_STATE);
    commandsToStatesMap.put(Command.RELOAD, DIALOG_STATE);
    commandsToStatesMap.put(Command.TELL_JOKE, JOKES_TELL_STATE);    
  }  
                          
  
  private List<Integer> prevStatesQueue = new ArrayList<Integer>();
  private int curState;
    
  public void execute(Command command) {
    Integer state = commandsToStatesMap.get(command);
    
    if (state != null && transitions[curState][state] != 0) {
      curState = state;
    }
  }

  public int getCurState() {
    return curState;
  }    
}
