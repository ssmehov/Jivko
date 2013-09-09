package jivko.brain.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class CommandRecognizer { 
  
  private static Map<String, Command> commands;
  
  static {
    commands = new HashMap<>();
    
    commands.put("Давай сначала", Command.RESET);
    commands.put("Ладно, стоп", Command.RESET);
    commands.put("Перезагрузка", Command.RESET);
    
    commands.put("Пауза", Command.PAUSE);
    commands.put("Остановись", Command.PAUSE);
            
    commands.put("Расскажи шутку", Command.JOKE);
    commands.put("Расскажи шутка", Command.JOKE);
    
    commands.put("Придумай шутку", Command.IMPROVISE);
    commands.put("Придумай шутка", Command.IMPROVISE);
    
    commands.put("Давай покажем миниатюру", Command.SKETCH);    
    commands.put("Давай покажем миниатюра", Command.SKETCH);
    commands.put("Давай покажем", Command.SKETCH);
    
    commands.put("поехали", Command.START);    
    
    //TODO: implement in future
    //commands.put("Придумай шутку", Command.IMPROVISE);
    //commands.put("Найди в интернете", Command.SEARCH_INTERNET);                
  }     
  
  public static Command getCommand(String utterance) {
    Command result = Command.UNKNOWN_COMMAND;
    
    for (Entry <String, Command> e : commands.entrySet()) {
      if (e.getKey().toLowerCase().contains(utterance)) {
        result = e.getValue();
      }        
    }
    
    return result;
  }
}
