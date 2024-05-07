package weblogic.servlet.jsp;

import java.lang.reflect.Method;

class MethodEntry {
   Method m;
   Class[] paramTypes;
   String paramPart;

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof MethodEntry) {
         MethodEntry var2 = (MethodEntry)var1;
         return this.paramPart.equals(var2.paramPart);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.paramPart.hashCode();
   }
}
