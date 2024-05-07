package weblogic.corba.rmic;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

final class Structure {
   public String name;
   public Hashtable elements;
   int indentLevel = 0;
   final String SPACES = "                                                     ";

   public Structure() {
   }

   public String spaces() {
      return "                                                     ".substring(0, this.indentLevel * 2);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(100);
      var1.append(this.spaces() + "(" + this.name + "\n");

      for(Enumeration var3 = this.elements.keys(); var3.hasMoreElements(); --this.indentLevel) {
         ++this.indentLevel;
         String var4 = (String)var3.nextElement();
         Object var5 = this.elements.get(var4);
         if (var5 instanceof Structure) {
            var1.append(var5);
         } else if (var5 instanceof String) {
            var1.append(this.spaces() + var4 + ": " + (String)var5 + "\n");
         } else if (var5 instanceof Vector) {
            var1.append(this.spaces() + var4 + ": [ ");
            Vector var6 = (Vector)var5;
            int var7 = var6.size();

            for(int var8 = 0; var8 < var7; ++var8) {
               var1.append((String)var6.elementAt(var8) + " ");
            }

            var1.append("]\n");
         }
      }

      var1.append(this.spaces() + ")\n");
      return var1.toString();
   }
}
