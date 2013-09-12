package jivko.brain.improvisation;

import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jivko.brain.morpher.Morpher;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class JokeCreator {

  public class Scheme {
    List<Dictionary.Component> components = new ArrayList<Dictionary.Component>();
    
    public void addComponent(Dictionary.Component c) {
      components.add(c);
    }

    public List<Dictionary.Component> getComponents() {
      return components;
    }        
  }

  private Scheme commonScheme;
  
  private Scheme getCommonScheme() {
    return commonScheme;
  }  
  
  public JokeCreator() throws Exception {
    commonScheme = new Scheme();
    commonScheme.addComponent(Dictionary.getInstance().getAdjectives());
    commonScheme.addComponent(Dictionary.getInstance().getPersonas());
    commonScheme.addComponent(Dictionary.getInstance().getVerbs());
    commonScheme.addComponent(Dictionary.getInstance().getNouns());
    commonScheme.addComponent(Dictionary.getInstance().getPlaces());
  }
  
  public String createJoke() throws Exception {
    return createJoke(commonScheme);
  }
  
  public String createJokeWHoWhere(String whowhere) throws Exception {
    String result = null;
    
    String[] whowhereSplitted = whowhere.split("где ");
    
    if (whowhereSplitted.length == 2) {
      String who = whowhereSplitted[0];
      String where = whowhereSplitted[1];
      
      result = createJokeWHoWhere(who, where);
    }
    
    return result;
  }
  
  public String createJokeWHoWhere(String who, String where) throws Exception {
    Dictionary.Personas persona = new Dictionary.Personas();
    persona.setValue(who);
            
    Dictionary.Places place = new Dictionary.Places();
    place.setValue(where);
    
    return createJoke(persona, place);
  }
  
  public String createJoke(Dictionary.Personas personas, Dictionary.Places places) throws Exception {
    List<Dictionary.Component> presetComponents = new ArrayList<>();
    presetComponents.add(personas);
    presetComponents.add(places);
    
    return createJoke(presetComponents);
  }
  
  public String createJoke(List<Dictionary.Component> presetComponents) throws Exception {
    
    Scheme scheme = new Scheme();
    
    for (Dictionary.Component schemeComponent : commonScheme.getComponents()) {      
      
      int idx = 0;
      boolean isAdded = false;
      
      while (idx < presetComponents.size()) {        
        
        Dictionary.Component presetComponent = presetComponents.get(idx);
                
        if (schemeComponent.getClass().equals(presetComponent.getClass())) {
          scheme.addComponent(presetComponent);
          presetComponents.remove(presetComponent);
          isAdded = true;
          break;
        } else {          
          ++idx;
        }
      }
      
      if (!isAdded) 
        scheme.addComponent(schemeComponent);
    }
    
    return createJoke(scheme);
  }
          
  public String createJoke(Scheme scheme) throws Exception {
    String joke = "";
    
    Dictionary.Component prev = null;
    for (Dictionary.Component component : scheme.getComponents()) {
      
      String word;
      if (component.getValue() != null) {
        word = component.getValue();
      } else {
        word = component.getRandValueAndRemove();

        if (prev instanceof Dictionary.Verbs) {
          Morpher m = new Morpher(word);
          word = m.getMorph("Р");
        }
      }
      
      joke += word;
      joke += " ";
      
      prev = component;
    }
    
    return joke;
  }
  
  private static JokeCreator instance = null;
  
  public static JokeCreator getInstance() throws Exception {
    if (instance == null) {
      instance = new JokeCreator();
    }

    return instance;
  }
}
