package weblogic.xml.dom;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Node;

public class NodeIterator implements Iterator {
   private final Node startNode;
   private Node currentNode;
   private Node nextNode;

   public NodeIterator(Node var1) {
      this.startNode = this.nextNode = var1;
   }

   private void advance() {
      this.currentNode = this.nextNode;
      this.nextNode = this.findNext();
   }

   public Object next() {
      return this.nextNode();
   }

   public void remove() {
      this.current().getParentNode().removeChild(this.current());
      this.advance();
   }

   public Node current() {
      if (this.currentNode == null) {
         throw new NoSuchElementException("The current node is null please call next() before using accessor methods");
      } else {
         return this.currentNode;
      }
   }

   public Node nextNode() {
      if (this.nextNode == null) {
         throw new NoSuchElementException("Unable to advance the node iterator");
      } else {
         this.advance();
         return this.current();
      }
   }

   private Node findNext() {
      Node var1 = this.currentNode.getFirstChild();
      if (var1 != null) {
         return var1;
      } else {
         var1 = this.currentNode.getNextSibling();
         if (var1 != null) {
            return var1;
         } else if (this.currentNode == this.startNode) {
            return null;
         } else {
            for(var1 = this.currentNode.getParentNode(); var1 != null && var1 != this.startNode; var1 = var1.getParentNode()) {
               if (var1.getNextSibling() != null) {
                  return var1.getNextSibling();
               }
            }

            return null;
         }
      }
   }

   public boolean hasNext() {
      return this.nextNode != null;
   }
}
