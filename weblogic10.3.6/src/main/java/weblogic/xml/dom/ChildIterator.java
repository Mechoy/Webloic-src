package weblogic.xml.dom;

import java.util.Iterator;

public class ChildIterator implements Iterator {
   private int current = 0;
   private NodeImpl parent;

   public ChildIterator(NodeImpl var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Parent may not be null");
      } else {
         this.parent = var1;
      }
   }

   public Object next() {
      return this.parent.item(this.current++);
   }

   public boolean hasNext() {
      return this.current < this.parent.getLength();
   }

   public void remove() {
      throw new UnsupportedOperationException("Not Supported");
   }
}
