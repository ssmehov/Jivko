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

  private String actionResult = "";

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
  
  Action sketchChooseAction = new Action() {
    @Override
    public void doIt() {

      if (Brain.getInstance().getSketchesManager().getActiveSketch() == null) {
        actionResult = "Какую миниатюру бля?";
        return;
      }

      try {
        stateMachine.Fire(Command.START);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
  
  Action sketchTellAction = new Action() {
    @Override
    public void doIt() {
      actionResult = Brain.getInstance().getSketchesManager().findAnswer(
              Brain.getInstance().getQuestion());

      if (actionResult == null) {
        actionResult = "спасибо за внимание";

        try {
          stateMachine.Fire(Command.RESET);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  };
  
  Action jokeChooseAction = new Action() {
    @Override
    public void doIt() {     
      
      if (Brain.getInstance().getJokesManager().getActiveJoke() == null) {
        actionResult = "Шутку про что?";
        return;
      }

      try {
        stateMachine.Fire(Command.START);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
  
  Action jokeTellAction = new Action() {
    @Override
    public void doIt() {
      actionResult = Brain.getInstance().getJokesManager().findAnswer(
              Brain.getInstance().getQuestion());

      if (actionResult == null) {
        actionResult = "повторите, про что шутку?";
      } else {
        actionResult = "слушайте. " + actionResult;
        try {
          stateMachine.Fire(Command.RESET);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  };
      
  Action dialogTellAction = new Action() {
    @Override
    public void doIt() {
      actionResult = Brain.getInstance().getUtterancesManager().findAnswer(
              Brain.getInstance().getQuestion());     
      }
  };
  
  StateMachine<State, Command> stateMachine = new StateMachine<State, Command>(State.DIALOG);
  {
    try {
      stateMachine.Configure(State.INITIAL)
              .Permit(Command.DIALOG, State.DIALOG)
              .OnEntry(resetAction);

      stateMachine.Configure(State.DIALOG)
              .PermitReentry(Command.DIALOG)
              .Permit(Command.RESET, State.INITIAL)
              .Permit(Command.PAUSE, State.PAUSED)
              .Permit(Command.SKETCH, State.SKETCH_CHOICE)
              .Permit(Command.JOKE, State.JOKE_CHOICE)
              .OnEntry(dialogTellAction);

      stateMachine.Configure(State.SKETCH_CHOICE)
              .PermitReentry(Command.SKETCH)
              .Permit(Command.RESET, State.INITIAL)
              .Permit(Command.START, State.SKETCHING)
              .OnEntry(sketchChooseAction);

      stateMachine.Configure(State.SKETCHING)
              .PermitReentry(Command.SKETCH)
              .Permit(Command.RESET, State.INITIAL)
              .Permit(Command.PAUSE, State.PAUSED)
              .OnEntry(sketchTellAction);

      stateMachine.Configure(State.JOKE_CHOICE)
              .Permit(Command.RESET, State.INITIAL)
              .Permit(Command.START, State.JOKING)
              .OnEntry(jokeChooseAction);

      stateMachine.Configure(State.JOKING)
              .Permit(Command.RESET, State.INITIAL)
              .Permit(Command.PAUSE, State.PAUSED)
              .OnEntry(jokeTellAction);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private State prevState = State.INITIAL;

  public String execute(Command command) {

    try {
      State curState = stateMachine.getState();
      stateMachine.Fire(command);
      prevState = curState;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return actionResult;
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
