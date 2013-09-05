package jivko.brain.improvisation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class JokeCreator {

  private class Scheme {
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
  
  public String createJoke() {
    return createJoke(commonScheme);
  }
  
  public String createJoke(Scheme scheme) {
    String joke = "";
    
    for (Dictionary.Component component : scheme.getComponents()) {
      
      joke += component.getRandValue();
      joke += " ";
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
