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
    ,"HEAD_PAN=500"
    ,"HEAD_PAN=200"
    ,"HEAD_PAN=300"
    ,"HEAD_PAN=300"
    ,"HEAD_PAN_CENTER"
    ,"HEAD_TILT"
    ,"HEAD_TILT_CENTER"
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
