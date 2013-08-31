package jivko.brain.state;

import java.awt.Desktop;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jivko.brain.Brain;
import org.customsoft.stateless4j.StateMachine;
import org.customsoft.stateless4j.delegates.Action;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class StateMachineHolder {
  
  public enum State {
    INITIAL, DIALOG, PAUSED, SKETCH_CHOICE, SKETCHING, JOKE_CHOICE, JOKING
  }
  
  Action resetAction = new Action() {
    @Override
    public void doIt() {
      Brain.getInstance().reset();
      
      try {
        stateMachine.Fire(Command.DIALOG);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
    
  StateMachine<State, Command> stateMachine = new StateMachine<State, Command>(State.DIALOG);
  {
    try {
      stateMachine.Configure(State.INITIAL)
          .Permit(Command.DIALOG, State.DIALOG)
          .OnEntry(resetAction);

      stateMachine.Configure(State.DIALOG)
          .Permit(Command.RESET, State.INITIAL)
          .Permit(Command.PAUSE, State.PAUSED)
          .Permit(Command.SKETCH, State.SKETCH_CHOICE)
          .Permit(Command.JOKE, State.JOKE_CHOICE);

      stateMachine.Configure(State.SKETCH_CHOICE)
          .Permit(Command.RESET, State.INITIAL)
          .Permit(Command.START, State.SKETCHING);
      
      stateMachine.Configure(State.SKETCHING)
          .Permit(Command.RESET, State.INITIAL)
          .Permit(Command.PAUSE, State.PAUSED);

      stateMachine.Configure(State.JOKE_CHOICE)
          .Permit(Command.RESET, State.INITIAL)
          .Permit(Command.START, State.JOKING);          
      
      stateMachine.Configure(State.JOKING)
          .Permit(Command.RESET, State.INITIAL)
          .Permit(Command.PAUSE, State.PAUSED);          

    } catch (Exception e) {
      e.printStackTrace();
    }   
  }
  
  private State prevState = State.INITIAL;  

  public void execute(Command command) {
    
    try {
      State curState = stateMachine.getState();
      stateMachine.Fire(command);
      prevState = curState;            
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public State getState() {
    return stateMachine.getState();
  }
  
  public State getPrevState() {
    return prevState;
  }
  
  public void setPrevState() {
    
  }
}
