package jivko.test;

import jivko.brain.command.Command;
import jivko.brain.command.CommandsCenter;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Commands {

  private static String[] commandsToTest = {
    "HEAD_ROTATE"
    ,"HEAD_ROTATE_CENTER"
    ,"HEAD_TILT"
    ,"HEAD_TILT_CENTER"
  };
  
  public static void test() throws Exception {
    Command command;
    
    for (String s : commandsToTest) {
      command = CommandsCenter.getInstance().getCommand(s);
      command.execute();
    }
  }
}
