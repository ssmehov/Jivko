package jivko.brain.music;

import java.util.ArrayList;
import java.util.List;
import jivko.util.MakeSound;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class MusicCommand {  
  private String name;
  private String file;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }  

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }    
  
  public MusicCommand() {
  }    
      
  public void print() {
    System.out.println("name = " + getName());
    System.out.println("file = " + getFile());   
  }
  
  public void execute() {
    MakeSound.playWav(file);
  }
}

