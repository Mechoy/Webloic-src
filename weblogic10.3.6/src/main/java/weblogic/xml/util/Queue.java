package weblogic.xml.util;

import java.util.Vector;

public class Queue extends Vector {
   public boolean empty() {
      return this.isEmpty();
   }

   public Object peek() {
      return this.firstElement();
   }

   public Object pull() {
      if (this.isEmpty()) {
         return null;
      } else {
         Object var1 = this.firstElement();
         this.removeElementAt(0);
         return var1;
      }
   }

   public Object push(Object var1) {
      this.addElement(var1);
      return var1;
   }

   public int search(Object var1) {
      return this.lastIndexOf(var1);
   }
}
