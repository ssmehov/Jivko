package jivko.util;

import java.util.LinkedHashSet;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class BestAnswerFinder {

  final String DELIMITER = " ";
  String[] questionSplitted;
  int cntOfQuestionUniqueWords = 0;
  
  Object bestCandidate = null;
  int bestCandidateCntOfSimilar = 0;

  public BestAnswerFinder(String question) {
    questionSplitted = question.split(DELIMITER);
    
    LinkedHashSet<String> lhl = new LinkedHashSet<String>();
    for (String s : questionSplitted) {
      lhl.add(s.toLowerCase());
    }
    
    cntOfQuestionUniqueWords = lhl.size();
  }

  public void testCandidate(Object candidate, String candidateAnswer) {    
    String[] candidateAnswerSplitted = candidateAnswer.split(DELIMITER);

    LinkedHashSet<String> lhl = new LinkedHashSet<String>();

    for (String s : candidateAnswerSplitted) {
      lhl.add(s.toLowerCase());
    }
    
    int cntOfCandidateUniqueWords = lhl.size();
    
    for (String s : questionSplitted) {
      lhl.add(s.toLowerCase());
    }    

    int cntOfSimilar = cntOfQuestionUniqueWords + cntOfCandidateUniqueWords - lhl.size();

    if (cntOfSimilar > bestCandidateCntOfSimilar) {
      bestCandidateCntOfSimilar = cntOfSimilar;
      bestCandidate = candidate;      
    }
  }
  
  public Object getBestCandidate() {
    return bestCandidate;
  }
}
