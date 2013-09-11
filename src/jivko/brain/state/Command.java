package jivko.brain.state;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Command {
  public static final String UNKNOWN_COMMAND = "UNKNOWN_COMMAND";
  public static final String RESET = "RESET";
  public static final String START = "START";
  public static final String DIALOG = "DIALOG";
  public static final String JOKE = "JOKE";
  public static final String SKETCH = "SKETCH";
  public static final String PAUSE = "PAUSE";
  public static final String IMPROVISE = "IMPROVISE";
  public static final String SEARCH_INTERNET = "SEARCH_INTERNET";  

  private String name;
  private List<String> possibleValues = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }  
  
  public Command() {
  }
  
  public void addPossibleValue(String v) {
    if (!possibleValues.contains(v)) {
      possibleValues.add(v);
    }
  }
      
  public void print() {
    System.out.println("name = " + getName());
    System.out.println("possible values:");
    for (String s : possibleValues) {
      System.out.println("\t" + s);
    }
  }
  
  public boolean isContainValue(String value) {
    boolean result = false;
    for (String s : possibleValues) {
      if (s.toLowerCase().contains(value.toLowerCase())) {
        result = true;
        break;
      }
    }
    
    return result;
  }
}

