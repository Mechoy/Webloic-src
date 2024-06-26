package weblogic.cache;

import java.util.Enumeration;
import java.util.Iterator;

public class EnumerationIterator implements Iterator {
   private Enumeration enum_;

   public EnumerationIterator(Enumeration var1) {
      this.enum_ = var1;
   }

   public boolean hasNext() {
      return this.enum_.hasMoreElements();
   }

   public Object next() {
      return this.enum_.nextElement();
   }

   public void remove() {
      throw new UnsupportedOperationException("Cannot remove from an enumeration");
   }
}
