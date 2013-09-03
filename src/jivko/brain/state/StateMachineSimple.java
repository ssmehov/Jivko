package jivko.brain.state;

//import com.tecacet.math.fsm.InvalidTransitionException;
//import com.tecacet.math.fsm.StateTransitionTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class StateMachineSimple {

  /*enum State {

    INITIAL, DIALOG, PAUSED, SKETCHING, JOKING
  }
  StateTransitionTable<State, Command> table =
          new StateTransitionTable<State, Command>();

  {    
    table.addTransition(State.DIALOG, Command.RESET, State.INITIAL);
    table.addTransition(State.DIALOG, Command.PAUSE, State.PAUSED);
    table.addTransition(State.DIALOG, Command.SKETCH, State.SKETCHING);
    table.addTransition(State.DIALOG, Command.JOKE, State.JOKING);

    table.addTransition(State.SKETCHING, Command.RESET, State.INITIAL);
    table.addTransition(State.SKETCHING, Command.PAUSE, State.PAUSED);

    table.addTransition(State.JOKING, Command.RESET, State.INITIAL);
    table.addTransition(State.JOKING, Command.PAUSE, State.PAUSED);
  }
  private State prevState = State.INITIAL;
  private State curState = State.INITIAL;

  public void execute(Command command) {
    try {
      State newState = table.getNextState(curState, command);      
      prevState = curState;
      curState = newState;
    } catch (InvalidTransitionException ite) {      
    }    
  }

  public State getCurState() {
    return curState;
  }*/
}
