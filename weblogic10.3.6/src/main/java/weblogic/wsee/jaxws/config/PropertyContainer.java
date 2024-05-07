package weblogic.wsee.jaxws.config;

import java.util.Iterator;
import java.util.List;

public abstract class PropertyContainer {
   protected abstract List<Property> getPropertyFields();

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      String var2 = this.getClass().getName();
      int var3 = var2.lastIndexOf(".");
      if (var3 > 0) {
         var2 = var2.substring(var3 + 1);
      }

      var1.append("### ").append(var2).append(" ###\n");
      List var4 = this.getPropertyFields();

      for(Iterator var5 = var4.iterator(); var5.hasNext(); var1.append("\n")) {
         Property var6 = (Property)var5.next();
         var1.append("  ");

         try {
            var1.append(var6);
         } catch (Throwable var8) {
            var1.append(var6.getName()).append(": ");
            var1.append(var1.append(var8.toString()));
         }
      }

      return var1.toString();
   }
}
