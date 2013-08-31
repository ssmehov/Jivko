package jivko.brain.speech.sketch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jivko.brain.speech.Utterance;
import static jivko.brain.speech.UtterancesManager.getFailAnswer;
import jivko.util.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Sketch extends Utterance {
  private String name;  
  private int curBufIdx = 0;  
  private List<Boff> boffs = new ArrayList<Boff>();

  public Sketch() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
      
  public void addHumanBoff(String value) {
    boffs.add(new HumanBoff(value));
  }

  public void addRobotBoff(String value) {
    boffs.add(new RobotBoff(value));
  }

  public List<Boff> getBoffs() {
    return boffs;
  }
  
  public void reset() {
    curBufIdx = 0;
  }
  
  public Boff nextBoff() {
    Boff result = null;
    
    if (curBufIdx < boffs.size())
      result = boffs.get(curBufIdx++);
    
    return result;
  }
  
  public RobotBoff nextRobotBoff() {
    Boff result = null;
    
    while (curBufIdx < boffs.size()) {
      result = boffs.get(curBufIdx++);
      
      if (result instanceof RobotBoff)
        break;
    }
          
    return (RobotBoff)result;
  }
}




