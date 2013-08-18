/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jivko.util.recorder;

import java.io.InputStream;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public interface Recorder {
  
  public enum FileType {
    FLAC
    ,WAV
    ,MP3
  };
  
  public void record (int timeLen, String fileName, FileType fileType) throws Exception;
  
  public InputStream record(int timeLen, FileType fileType) throws Exception;
  
}
