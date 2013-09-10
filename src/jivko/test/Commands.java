package jivko.test;

import java.util.ArrayList;
import java.util.List;
import jivko.brain.movement.Command;
import jivko.brain.movement.CommandsCenter;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Commands {

  private static String[] commandsToTest = {
    "HEAD_PAN_CENTER"      
    ,"HEAD_PAN"
    ,"HEAD_PAN"
    ,"HEAD_PAN"
    ,"HEAD_PAN=100"
    ,"HEAD_PAN_CENTER=3000"
    ,"HEAD_TILT"
    ,"HEAD_TIP=400"
    ,"HEAD_TIP=400"
    ,"HEAD_TIP=400"          
    ,"HEAD_TILT_CENTER"        
    ,"HEAD_TIP_CENTER=400"
  };
  
  public static void test() throws Exception {
    List<Command> commands = new ArrayList<>();
    
    for (String s : commandsToTest) {
      Command command = CommandsCenter.getInstance().getCommandEx(s);
      commands.add(command);
    }
    
    CommandsCenter.getInstance().executeCommandList(commands);
  }
}
