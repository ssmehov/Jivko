package jivko.sphinx.util;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class UtteranceData implements edu.cmu.sphinx.frontend.Data {

  public UtteranceData(String utterence) throws Exception {
    this.utterence = utterence;
  }
  
  private String utterence;

  public String getUtterence() {
    return utterence;
  }

  public void setUtterence(String utterence) {
    this.utterence = utterence;
  }
    
}
