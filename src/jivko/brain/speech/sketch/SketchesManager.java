package jivko.brain.speech.sketch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jivko.brain.movement.Command;
import jivko.brain.movement.CommandsCenter;
import jivko.brain.music.MusicCommand;
import jivko.brain.music.MusicCommandsCenter;
import jivko.util.BestAnswerFinder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class SketchesManager {

  static private final String XML_DOM_NODE_SKETCHES = "sketches";
  static private final String XML_DOM_NODE_SKETCH = "sketch";
  static private final String XML_DOM_NODE_HUMAN = "human";
  static private final String XML_DOM_NODE_ROBOT = "robot";
  static private final String XML_DOM_ATTRIBUTE_NAME = "name";
  static private final String XML_DOM_ATTRIBUTE_SPEACH_DELAY = "speachdelay";
  
  static private final Integer MAX_SPEACH_DELAY = 4000;
  
  private List<Sketch> sketches = new ArrayList<>();
  private Sketch activeSketch = null;

  public void addSketch(Sketch sketch) {
    sketches.add(sketch);
  }

  public void initialize(String path) throws Exception {
    readSketchList(path);
    print();
  }
  
  public void reset() {
    activeSketch = null;
  }

  private void readSketchList(String path) throws Exception {

    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    f.setValidating(false);
    DocumentBuilder builder = f.newDocumentBuilder();
    Document doc = builder.parse(new File(path));

    Node sketchesNode = doc.getFirstChild();

    while (sketchesNode.getNodeType() != Document.ELEMENT_NODE
            && sketchesNode.getNextSibling() != null) {
      sketchesNode = sketchesNode.getNextSibling();
    }

    assert XML_DOM_NODE_SKETCHES.equals(sketchesNode.getNodeName());

    Node sketchNode = sketchesNode.getFirstChild();

    while (sketchNode != null) {
      if (sketchNode.getNodeType() != Document.ELEMENT_NODE) {
        sketchNode = sketchNode.getNextSibling();

        if (sketchNode != null) {
          continue;
        } else {
          break;
        }
      }

      Sketch sketch = new Sketch();
      readSketch(sketchNode, sketch);

      addSketch(sketch);

      sketchNode = sketchNode.getNextSibling();
    }
  }

  private void readSketch(Node node, Sketch sketch) throws Exception {
    assert XML_DOM_NODE_SKETCH.equals(node.getNodeName());
            
    String name = ((Element)node).getAttribute(XML_DOM_ATTRIBUTE_NAME);
    sketch.setName(name);

    NodeList nl = node.getChildNodes();
    for (int i = 0; i < nl.getLength(); ++i) {
      Node n = nl.item(i);

      if (n.getNodeType() != Document.ELEMENT_NODE) {
        continue;
      }

      String nodeName = n.getNodeName();

      if (nodeName.contains(XML_DOM_NODE_HUMAN)) {
        String nodeVal = n.getTextContent();
        sketch.addHumanBoff(nodeVal);
      } else if (nodeName.contains(XML_DOM_NODE_ROBOT)) {
        
        MusicCommand musicCommand = MusicCommandsCenter.getInstance().getCommandFromXmlElement(n);            
        List<Command> commands = CommandsCenter.getInstance().getCommandsFromXmlElement(n);        
      
        String nodeVal = n.getTextContent();
        
        String speachDelay = ((Element)n).getAttribute(XML_DOM_ATTRIBUTE_SPEACH_DELAY);
        
        RobotBoff robotBoff = new RobotBoff(nodeVal, commands, musicCommand);
        robotBoff.setSpeachDelay(speachDelay);
        
        sketch.addRobotBoff(robotBoff);
      }
    }
    
  }

  public Sketch getActiveSketch() {
    return activeSketch;
  }    
  
  
  public void setActiveSketch(Sketch s) {
    activeSketch = s;
  }
  
  public Sketch findDefaultSketch() {
    return findSketch("default");
  }
  
  public Sketch findSketch(String name) {
    BestAnswerFinder baf = new BestAnswerFinder(name);
    
    for (Sketch s : sketches) {
      baf.testCandidate(s, s.getName());
    }
    
    Sketch bestCandidate = (Sketch)baf.getBestCandidate();
    return bestCandidate;
  }    
  
  public String findAnswer(String question) throws Exception {
    String result = null;
    
    if (activeSketch != null) {
      RobotBoff answer = activeSketch.nextRobotBoff();
      
      if (answer == null) { 
        setActiveSketch(null);
      } else {
        result = answer.getValue();
        CommandsCenter.getInstance().executeCommandList(answer.getCommands());
        MusicCommandsCenter.getInstance().executeCommand(answer.getMusicCommand());
        if (answer.getSpeachDelay() != null) {
          int delay = answer.getSpeachDelay();
          Thread.sleep(Math.min(delay, MAX_SPEACH_DELAY));
        }
      }
    }

    return result;
  }
  
  public void print() {
    String identity = "";
    
    for (Sketch s : sketches) {
      printSketch(s, identity);  
    }    
  }
  
  public void printSketch(Sketch s, String identity) {
    
    System.out.println("Sketch: " + s.getName());
    
    for (Boff b : s.getBoffs()) {
      String prefix = "";
      if (b instanceof HumanBoff) {
        prefix = "human: ";
      } else if (b instanceof RobotBoff){
        prefix = "robot: ";
      }
      
      System.out.println(identity + prefix + b.getValue());
    }
    
    System.out.println("\n");
  }
}
