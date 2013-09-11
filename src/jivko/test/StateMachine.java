package jivko.test;

import jivko.brain.Brain;
import jivko.brain.state.CommandsCenter;


public class StateMachine implements Testable {

  private void next(String question) throws Exception {    
    String answer = Brain.getInstance().findAnswer(question);
    System.out.println("question: " + question);
    System.out.println("answer: " + answer);
  }
  
  @Override
  public void test() throws Exception {    
    next("Привет, как тебя зовут");
    next("Что ты умеешь делать");
    next("Расскажи шутку");
    next("Про поезд");
    next("Расскажи шутку");
    next("блвыв");
    next("Про пезд");
    next("Придумай шутку");
    next("Павел воля где в гостиннице");
    next("Давай покажем миниатюру");
    next("музыкант");
    next("бла бла");
    next("бла бла");
    next("бла бла");
    next("стоп");
  }
  
}
