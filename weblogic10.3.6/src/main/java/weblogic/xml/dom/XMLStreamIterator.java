package weblogic.xml.dom;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLStreamIterator implements Iterator {
   private static final int OPEN = 1;
   private static final int CLOSE = 2;
   private static final int VISIT = 3;
   private Node currentNode;
   private Node nextNode;
   private Node root;
   private int currenState;
   private int nextState;

   public XMLStreamIterator(Node var1) {
      if (var1.getNodeType() == 1) {
         this.nextNode = var1;
         this.root = var1;
         this.nextState = 1;
      } else {
         this.root = var1;
         this.nextNode = var1;
         this.nextState = 3;
      }

   }

   private void advance() {
      this.currentNode = this.nextNode;
      this.currenState = this.nextState;
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

   public boolean isOpen() {
      return this.currenState == 1;
   }

   public boolean isClosed() {
      return this.currenState == 2;
   }

   private Node findNext() {
      Node var1;
      switch (this.currenState) {
         case 1:
            var1 = this.currentNode.getFirstChild();
            if (var1 != null) {
               if (var1.getNodeType() == 1) {
                  this.nextState = 1;
               } else {
                  this.nextState = 3;
               }

               return var1;
            }

            var1 = this.currentNode;
            this.nextState = 2;
            return var1;
         case 2:
            if (this.currentNode != this.root) {
               var1 = this.currentNode.getNextSibling();
            } else {
               var1 = null;
            }

            if (var1 != null) {
               if (var1.getNodeType() == 1) {
                  this.nextState = 1;
               } else {
                  this.nextState = 3;
               }

               return var1;
            }

            if (this.currentNode != this.root) {
               var1 = this.currentNode.getParentNode();
            } else {
               var1 = null;
            }

            this.nextState = 2;
            return var1;
         case 3:
            if (this.currentNode != this.root) {
               var1 = this.currentNode.getNextSibling();
            } else {
               var1 = null;
            }

            if (var1 != null) {
               if (var1.getNodeType() == 1) {
                  this.nextState = 1;
               } else {
                  this.nextState = 3;
               }

               return var1;
            }

            if (this.currentNode != this.root) {
               var1 = this.currentNode.getParentNode();
            } else {
               var1 = null;
            }

            this.nextState = 2;
            return var1;
         default:
            return null;
      }
   }

   public boolean hasNext() {
      return this.nextNode != null;
   }

   public static void main(String[] var0) throws Exception {
      DocumentImpl var1 = new DocumentImpl();
      Element var2 = var1.createElement("parent");
      var1.appendChild(var2);
      System.out.println("----------------[ empty root node]");
      XMLStreamIterator var3 = new XMLStreamIterator(var2);

      while(var3.hasNext()) {
         System.out.println(var3.next());
      }

      DOMStreamReader var4 = new DOMStreamReader(var2);

      while(var4.hasNext()) {
         System.out.println(var4.toString());
         var4.next();
      }

      System.out.println("----------------[ dump]");
      var2.appendChild(var1.createComment(" this is a comment "));
      Element var5 = var1.createElement("child1");
      var2.appendChild(var5);
      Element var6 = var1.createElement("text");
      var6.appendChild(var1.createTextNode("sometext"));
      var2.appendChild(var6);
      Element var7 = var1.createElement("child2");
      var2.appendChild(var7);
      var7.appendChild(var1.createElement("a"));
      var7.appendChild(var1.createElement("b"));
      var7.appendChild(var1.createElement("c"));
      System.out.println(Util.printNode(var1));
      System.out.println("----------------[ root node]");
      var3 = new XMLStreamIterator(var2);

      while(var3.hasNext()) {
         System.out.println(var3.next());
      }

      System.out.println("----------------[ text node]");
      var3 = new XMLStreamIterator(var6);

      while(var3.hasNext()) {
         System.out.println(var3.next());
      }

      System.out.println("----------------[ child node]");
      var3 = new XMLStreamIterator(var7);

      while(var3.hasNext()) {
         System.out.println(var3.next());
      }

      System.out.println("----------------[ empty child node]");
      var3 = new XMLStreamIterator(var5);

      while(var3.hasNext()) {
         System.out.println(var3.next());
      }

      System.out.println("----------------[ root node]");
      var4 = new DOMStreamReader(var2);

      while(var4.hasNext()) {
         System.out.println(var4.toString());
         var4.next();
      }

      System.out.println("----------------[ text node]");
      var4 = new DOMStreamReader(var6);

      while(var4.hasNext()) {
         System.out.println(var4.toString());
         var4.next();
      }

      System.out.println("----------------[ child node]");
      var4 = new DOMStreamReader(var7);

      while(var4.hasNext()) {
         System.out.println(var4.toString());
         var4.next();
      }

      System.out.println("----------------[ empty child node]");
      var4 = new DOMStreamReader(var5);

      while(var4.hasNext()) {
         System.out.println(var4.toString());
         var4.next();
      }

   }
}
