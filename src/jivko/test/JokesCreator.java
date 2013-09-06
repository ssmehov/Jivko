package jivko.test;

import jivko.brain.improvisation.JokeCreator;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class JokesCreator {

  public void test() throws Exception {
    String joke;
    for (int i = 0; i < 10; ++i) {
      joke = JokeCreator.getInstance().createJoke();
      System.err.println(joke);
    }
    
    joke = JokeCreator.getInstance().createJokeWHoWhere("Янукович где во дворе");    
    System.err.println(joke);    
  }
}
