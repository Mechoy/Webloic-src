package weblogic.diagnostics.type;

import java.util.Iterator;

public class ImmutableIterator implements Iterator {
   private Iterator iterator;

   public ImmutableIterator(Iterator var1) {
      this.iterator = var1;
   }

   public boolean hasNext() {
      return this.iterator.hasNext();
   }

   public Object next() {
      return this.iterator.next();
   }

   public void remove() {
      throw new UnsupportedOperationException("Remove not allowed on this iterator.");
   }
}
