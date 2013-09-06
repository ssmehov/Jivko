package jivko.brain.speech;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import jivko.brain.movement.Command;
import jivko.util.Tree;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Utterance extends jivko.util.Tree {  
  List<Command> commands;
  
  public List<Command> getCommands() {
    return commands;
  }

  public void setCommands(List<Command> commandss) {
    this.commands = commands;
  } 
  //quesion associated with answers set  
  private String question;

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }
  
  public void setAnswer(String answer) {
    addValue(answer);
  }
  
  
  private Random generator = new Random();
  
  public Utterance() {    
  }
  
  public String getRandomValue() {
    List<Object> values = (List<Object>)getValues();    
    
    if (values.isEmpty())
      return "";
    
    return (String)values.get(generator.nextInt(values.size()));
  }
  
  public Utterance findUtteranceWithAnswer(String question) {
    return findUtteranceWithAnswer(this, question);
  }
  
  public Utterance findUtteranceWithAnswer(Utterance utterance, String question) {
    Utterance answer = null;
    
    String delimiter = " ";
    
    String[] questionSplitted;
    questionSplitted = question.split(delimiter);    
    
    int cntOfSimilarMax = 0;

    LinkedList<Tree> processingNodes = new LinkedList<>();
    processingNodes.addAll(utterance.getNodes());
    
    //for (Tree t : utterance.getNodes()) {      
    while(!processingNodes.isEmpty()) {
      
      Utterance u = (Utterance)processingNodes.pollFirst();
      
      String[] utteranceQuestionSplitted;
      utteranceQuestionSplitted = u.getQuestion().split(delimiter);

      LinkedHashSet<String> lhl = new LinkedHashSet<String>();

      for (String s : questionSplitted) {
        lhl.add(s.toLowerCase());
      }

      for (String s : utteranceQuestionSplitted) {
        lhl.add(s.toLowerCase());
      }

      int duplicateCnt = questionSplitted.length + utteranceQuestionSplitted.length - lhl.size();

      if (duplicateCnt > cntOfSimilarMax) {
        cntOfSimilarMax = duplicateCnt;
                
        answer = u;            
      }
      
      processingNodes.addAll(u.getNodes());
    }
        
    return answer;
  }

}
