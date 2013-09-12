package jivko.brain.improvisation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import jivko.config.ConfigurationManager;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Dictionary {

  public static final String TAIL = "tail";
  public static final String VERB = "verb";
  public static final String NOUN = "noun";
  public static final String ADJECTIVE = "adjective";
  public static final String PERSONA = "persona";
  public static final String PLACE = "place";  
  
  private static Random rand = new Random();

  static public class Component {

    private String contentFileName;
    private String value;
    private List<String> values;

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
        
    public Component(String contentFileName) {
      this.contentFileName = contentFileName;
    }

    public void initializeFromDb() throws Exception {
      String dbPath = ConfigurationManager.getInstance().getDictionariesDBPath();
      dbPath += "/";
      values = readTextFile(dbPath + contentFileName);
    }

    public List<String> getValues() {
      return values;
    }

    public String getRandValueAndRemove() throws Exception {
      int idx = rand.nextInt(values.size());
      String result = values.get(idx);
      values.remove(idx);
      if (values.size() == 0) {
        initializeFromDb();
      }
      return result;      
    }
    
    public String getRandValue() {
      int idx = rand.nextInt(values.size());
      return values.get(idx);
    }

    private List<String> readTextFile(String fileName) throws Exception {
      List<String> out = new ArrayList<>();
      BufferedReader br = new BufferedReader(new FileReader(fileName));

      String line;
      while ((line = br.readLine()) != null) {
        out.add(line);
      }
      br.close();

      return out;
    }
  }

  static public class Tails extends Component {

    private static final String CONTENT_FILE = "tails.txt";

    public Tails() throws Exception {
      super(CONTENT_FILE);
    }
  }
  
  static public class Verbs extends Component {

    private static final String CONTENT_FILE = "verbs.txt";

    public Verbs() throws Exception {
      super(CONTENT_FILE);
    }
  }

  static public class Nouns extends Component {

    private static final String CONTENT_FILE = "nouns.txt";

    public Nouns() throws Exception {
      super(CONTENT_FILE);
    }
  }

  static public class Adjectives extends Component {

    private static final String CONTENT_FILE = "adjectives.txt";

    public Adjectives() throws Exception {
      super(CONTENT_FILE);
    }
  }

  static public class Personas extends Component {

    private static final String CONTENT_FILE = "personas.txt";

    public Personas() throws Exception {
      super(CONTENT_FILE);
    }
  }

  static public class Places extends Component {

    private static final String CONTENT_FILE = "places.txt";

    public Places() throws Exception {
      super(CONTENT_FILE);
    }
  }
  
  private Tails tails;
  private Verbs verbs;
  private Nouns nouns;
  private Adjectives adjectives;
  private Personas personas;
  private Places places;

  public Dictionary() throws Exception {
    tails = new Tails();
    tails.initializeFromDb();
    
    verbs = new Verbs();
    verbs.initializeFromDb();
    
    nouns = new Nouns();
    nouns.initializeFromDb();
    
    adjectives = new Adjectives();
    adjectives.initializeFromDb();
    
    personas = new Personas();
    personas.initializeFromDb();
    
    places = new Places();
    places.initializeFromDb();
  }

  public Tails getTails() {
    return tails;
  }
  
  public Verbs getVerbs() {
    return verbs;
  }

  public Nouns getNouns() {
    return nouns;
  }

  public Adjectives getAdjectives() {
    return adjectives;
  }

  public Personas getPersonas() {
    return personas;
  }

  public Places getPlaces() {
    return places;
  }

  
  private static Dictionary instance = null;

  public static Dictionary getInstance() throws Exception {
    if (instance == null) {
      instance = new Dictionary();
    }

    return instance;
  }
}
