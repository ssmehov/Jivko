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
    ,TELL_JOKE
    ,SHOW_SCETCH    
    ,IMPROVISE
    ,SEARCH_INTERNET
  }
  
  Map<String, Command> commands;
  
  {
    commands = new HashMap<String, Command>();
    
    commands.put("Давай сначала", Command.START_FROM_BEGIN);
    commands.put("Расскажи шутку", Command.TELL_JOKE);
    commands.put("Давай сначала", Command.SHOW_SCETCH);    
    commands.put("Придумай шутку", Command.IMPROVISE);
    commands.put("Найди в интернете", Command.SEARCH_INTERNET);        
  }

  public CommandsList() {
    
  }  
}
