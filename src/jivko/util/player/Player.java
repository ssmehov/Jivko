/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jivko.util.player;

import java.io.InputStream;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public interface Player {
  
  public void play(final String fileName, jivko.util.recorder.Recorder.FileType fileType);
  
  public void play(final String fileName, InputStream inputStream);
}
