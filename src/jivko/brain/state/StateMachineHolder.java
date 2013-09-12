package jivko.brain.state;

import jivko.brain.Brain;
import jivko.brain.improvisation.Dictionary;
import jivko.brain.improvisation.JokeCreator;
import jivko.brain.speech.sketch.Sketch;
import jivko.recognizer.RecognizerFactory;
import jivko.synthesizer.SynthesizerFactory;
import org.customsoft.stateless4j.StateMachine;
import org.customsoft.stateless4j.delegates.Action;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class StateMachineHolder {

  private String actionResult = "";

  public enum State {
    INITIAL, DIALOG, PAUSED, SKETCH_CHOICE, SKETCHING, JOKE_CHOICE, JOKING, IMPROVISE_CHOICE, IMPROVISE
  }
  
  Action resetAction = new Action() {
    @Override
    public void doIt() {
      Brain.getInstance().reset();

      try {
        //stateMachine.Fire(Command.DIALOG);
        setNextCommand(Command.DIALOG);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
  
  Action sketchChooseAction = new Action() {
    @Override
    public void doIt() {

      if (Brain.getInstance().getSketchesManager().getActiveSketch() == null) {
        actionResult = "Какую миниатюру?";        
      }
     
      setNextCommand(Command.START);
    }
  };
  
  Action sketchTellAction = new Action() {
    @Override
    public void doIt() {            
      actionResult = "";
      
      setNextCommand(Command.SKETCH);
              
      if (Brain.getInstance().getSketchesManager().getActiveSketch() == null) {
        
        Sketch sketch = Brain.getInstance().getSketchesManager().findSketch(
                Brain.getInstance().getQuestion());
        
        if (sketch == null) {
          actionResult = "повтори еще раз, какую миниатюру?";
          return;
        }
                
        Brain.getInstance().getSketchesManager().setActiveSketch(sketch);

        if (sketch.doesHumanStart()) {
          //actionResult = "ок! начинай";
          actionResult = "окей. погнали.  ";
          return;
        } else {
          actionResult = "окей. погнали.  ";
        }
        
        RecognizerFactory.getRecognizer().muteNext();
      }
      
      String answer = null;
      try {
        answer = Brain.getInstance().getSketchesManager().findAnswer(
              Brain.getInstance().getQuestion());
        
        if (answer == null) {
          actionResult = "спасибо за внимание";                 
          stateMachine.Fire(Command.RESET);
        } else {
          actionResult += answer;
          RecognizerFactory.getRecognizer().muteNext();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }     
    }    
  };
  
  Action sketchTellDefaultAction = new Action() {
    @Override
    public void doIt() {          
      actionResult = "";
      
      setNextCommand(Command.SKETCH);
              
      if (Brain.getInstance().getSketchesManager().getActiveSketch() == null) {
        
        Sketch sketch = Brain.getInstance().getSketchesManager().findDefaultSketch();        
        if (sketch == null) {
          actionResult = "Я обосрасля. Пойду менять белье";
          return;
        }
                
        Brain.getInstance().getSketchesManager().setActiveSketch(sketch);

        //don't need to recognize next utterance
        RecognizerFactory.getRecognizer().muteNext();
        
        if (sketch.doesHumanStart()) {
          //actionResult = "ок! начинай";
          actionResult = "окей. погнали.  ";
          return;
        } else {
          actionResult = "окей. погнали.  ";
        }                
      }
      
      String answer = null;
      try {
        answer = Brain.getInstance().getSketchesManager().findAnswer(
              Brain.getInstance().getQuestion());
        
        if (answer == null) {
          actionResult = "спасибо за внимание";                 
          stateMachine.Fire(Command.RESET);
        } else {
          actionResult += answer;
          RecognizerFactory.getRecognizer().muteNext();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }     
    }    
  };
  
  Action jokeChooseAction = new Action() {
    @Override
    public void doIt() {     
      
      if (Brain.getInstance().getJokesManager().getActiveJoke() == null) {
        actionResult = "Шутку про что?";        
      }

      try {
        setNextCommand(Command.START);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
  
  Action jokeTellAction = new Action() {
    @Override
    public void doIt() {
      try {
        actionResult = Brain.getInstance().getJokesManager().findAnswer(
              Brain.getInstance().getQuestion());
        
        if (actionResult == null) {
          actionResult = "повторите, про что шутку?";
        } else {
          actionResult = "слушайте. " + actionResult;                
          stateMachine.Fire(Command.RESET);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }      
    }
  };     
  
  Action improviseTellAction = new Action() {
    @Override
    public void doIt() {
      
      try {
        actionResult = JokeCreator.getInstance().createJokeWHoWhere(Brain.getInstance().getQuestion());
        
        if (actionResult == null) {
          actionResult = "повторите, про что шутку?";
        } else {
          actionResult = "слушайте. " + actionResult;
          SynthesizerFactory.getSynthesizer().talkWithPause(actionResult, 1000);
          actionResult = Dictionary.getInstance().getTails().getRandValue();
          stateMachine.Fire(Command.RESET);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }      
    }
  };
      
  Action dialogTellAction = new Action() {
    @Override
    public void doIt() {      
        try {
          actionResult = Brain.getInstance().getUtterancesManager().findAnswer(
                Brain.getInstance().getQuestion());     
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
  };
  
  StateMachine<State, String> stateMachine = new StateMachine<>(State.DIALOG);
  {
    try {
      stateMachine.Configure(State.INITIAL)
              .Permit(Command.DIALOG, State.DIALOG)
              .Permit(Command.JOKE, State.JOKE_CHOICE)
              .Permit(Command.SKETCH, State.SKETCHING)
              .Permit(Command.IMPROVISE, State.IMPROVISE_CHOICE)
              .OnEntry(resetAction);

      stateMachine.Configure(State.DIALOG)
              .PermitReentry(Command.DIALOG)
              .Permit(Command.RESET, State.INITIAL)
              .Permit(Command.PAUSE, State.PAUSED)
              .Permit(Command.SKETCH, State.SKETCHING)
              .Permit(Command.JOKE, State.JOKE_CHOICE)
              .Permit(Command.IMPROVISE, State.IMPROVISE_CHOICE)
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
              .OnEntry(sketchTellDefaultAction);

      stateMachine.Configure(State.JOKE_CHOICE)
              .Permit(Command.RESET, State.INITIAL)
              .Permit(Command.START, State.JOKING)
              .OnEntry(jokeChooseAction);

      stateMachine.Configure(State.JOKING)
              .PermitReentry(Command.START)
              .Permit(Command.JOKE, State.JOKE_CHOICE)
              .Permit(Command.RESET, State.INITIAL)
              .Permit(Command.PAUSE, State.PAUSED)
              .OnEntry(jokeTellAction);
      
      stateMachine.Configure(State.IMPROVISE_CHOICE)
              .Permit(Command.RESET, State.INITIAL)
              .Permit(Command.START, State.IMPROVISE)
              .OnEntry(jokeChooseAction);

      stateMachine.Configure(State.IMPROVISE)
              .Permit(Command.IMPROVISE, State.IMPROVISE_CHOICE)
              .PermitReentry(Command.START)
              .Permit(Command.RESET, State.INITIAL)
              .Permit(Command.PAUSE, State.PAUSED)
              .OnEntry(improviseTellAction);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private State prevState = State.INITIAL;

  private String nextCommand = Command.DIALOG;

  public void setNextCommand(String nextCommand) {
    this.nextCommand = nextCommand;
  }  

  public String getNextCommand() {
    return nextCommand;
  }
        
  public String execute(String command) {

    if (!Command.UNKNOWN_COMMAND.equals(command)) {
      setNextCommand(command);
    }      
    
    try {
      State curState = stateMachine.getState();
      stateMachine.Fire(getNextCommand());
      prevState = curState;
    } catch (Exception e) {
      e.printStackTrace();
    }

    String result = actionResult;
    actionResult = "";
    return result;
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
