package jivko.brain.speech;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class CommandsList {

  enum Command {
    START_FROM_BEGIN
    ,SEARCH_INTERNET
    ,IMPROVISE
  }
  
  Map<String, Command> commands;
  
  {
    commands = new HashMap<String, Command>();
    
    commands.put("Давай сначала", Command.START_FROM_BEGIN);
    commands.put("Придумай шутку", Command.IMPROVISE);
    commands.put("Найди в интернете", Command.SEARCH_INTERNET);        
  }

  public CommandsList() {
    
  }  
}
