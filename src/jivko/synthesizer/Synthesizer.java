/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jivko.synthesizer;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public interface Synthesizer {
  
  public void talk(final String text);
  public void talkWithPause(final String text, int pauseMs);
  public void talkWithDelay(final String text, int delayMs);
}
