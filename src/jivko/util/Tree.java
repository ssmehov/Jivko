package jivko.util;

/**
 *
 * @author Sergii Smehov (smehov.com)
 */
import java.util.*;

public class Tree {

  List<Tree> nodes = new ArrayList<Tree>();
  
  List<Object> values = new ArrayList<Object>();

  public void addNode(Tree t) {
    nodes.add(t);
  }

  public List<Tree> getNodes () {
    return nodes;  
  }
  
  public Iterator<Tree> enumerateNodes() {
    return nodes.iterator();
  }

  public void addValue(Object o) {
    values.add(o);
  }

  public List<Object> getValues() {
    return values;
  }
  
  public Iterator<Object> enumerateValues() {
    return values.iterator();
  }

  public void print(int level) {
    char[] chars = new char[level];
    Arrays.fill(chars, ' ');

    for (Object val : values) {
      System.out.print(chars);
      System.out.println(val);
    }
    for (Tree node : nodes) {
      node.print(level + 1);
    }
  }

  public static void main(String[] args) {
    Tree root = new Tree();
    
    for (int i = 0; i < 5; i++) {
      final Tree node1 = new Tree();
      node1.addValue("level 1:value = " + i);
      root.addNode(node1);
      
      for (int j = 0; j < 5; j++) {
        final Tree node2 = new Tree();
        node2.addValue("level 2:value = " + j);
        node1.addNode(node2);
        for (int k = 0; k < 2; k++) {
          final Tree node3 = new Tree();
          node3.addValue("level 3:value = " + k);
          node2.addNode(node3);
        }

      }
    }
    root.print(0);
  }
  
}
