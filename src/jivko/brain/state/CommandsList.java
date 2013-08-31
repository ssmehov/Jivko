package jivko.brain.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class CommandsList { 
  
  private static Map<String, Command> commands;
  
  static {
    commands = new HashMap<String, Command>();
    
    commands.put("Давай сначала", Command.START_FROM_BEGIN);
    commands.put("Ладно, стоп", Command.START_FROM_BEGIN);
    
    commands.put("Пауза", Command.PAUSE);
    commands.put("Остановись", Command.PAUSE);
    
    commands.put("Перезагрузка", Command.RELOAD);
    
    commands.put("Расскажи шутку", Command.TELL_JOKE);
    commands.put("Давай покажем миниатюру", Command.SHOW_SCETCH);    
    
    //TODO: implement in future
    //commands.put("Придумай шутку", Command.IMPROVISE);
    //commands.put("Найди в интернете", Command.SEARCH_INTERNET);        
  } 
  
  public static Command getCommand(String utterance) {
    Command result = Command.UNKNOWN_COMMAND;
    
    for (Entry <String, Command> e : commands.entrySet()) {
      if (e.getKey().contains(utterance)) {
        result = e.getValue();
      }        
    }
    
    return result;
  }
}
