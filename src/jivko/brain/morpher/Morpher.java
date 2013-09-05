package jivko.brain.morpher;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
 

public class Morpher {
  private Document doc = null;
 
  public Morpher(String word) throws IOException, ParserConfigurationException, SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    doc = builder.parse("http://morpher.ru/WebService.asmx/GetXml?s="+word);
  }
 
 
  public String getRootElement() {
    String result = "";
    if (null != doc) {
      Element el = doc.getDocumentElement();
      result = el.getTagName();
    }
    return result;
  }
 
  public String getMorph(String p) {
    String result = "";
    if (null != doc) {
      Element root = doc.getDocumentElement();
      NodeList nodes = root.getChildNodes();
      for (int x = 0; x < nodes.getLength(); x++) {
        Node item = nodes.item(x);
        if (item instanceof Element) {
          Element el = ((Element)item);
          if (el.getTagName().equals(p)) {
            result = ((Text)el.getFirstChild()).getData().trim();
          }
        }
      }
    }
    return result;
  }    
}

