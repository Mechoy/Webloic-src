package weblogic.ejb.container.utils.ddconverter;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Structure {
   public String name;
   public Hashtable elements;
   static int indentLevel = 0;
   static final String SPACES = "                                                     ";

   public String spaces() {
      return "                                                     ".substring(0, indentLevel * 2);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(100);
      var1.append(this.spaces() + "(" + this.name + "\n");

      for(Enumeration var3 = this.elements.keys(); var3.hasMoreElements(); --indentLevel) {
         ++indentLevel;
         String var4 = (String)var3.nextElement();
         Object var5 = this.elements.get(var4);
         if (var5 instanceof Structure) {
            var1.append(var5);
         } else if (var5 instanceof String) {
            var1.append(this.spaces() + var4 + ": " + (String)var5 + "\n");
         } else if (var5 instanceof Vector) {
            var1.append(this.spaces() + var4 + ": [ ");
            Vector var10 = (Vector)var5;
            int var11 = var10.size();

            for(int var12 = 0; var12 < var11; ++var12) {
               var1.append((String)var10.elementAt(var12) + " ");
            }

            var1.append("]\n");
         } else if (var5 instanceof Hashtable) {
            var1.append(this.spaces() + var4 + ": { ");
            Hashtable var6 = (Hashtable)var5;
            Enumeration var7 = var6.keys();

            while(var7.hasMoreElements()) {
               String var8 = (String)var7.nextElement();
               Object var9 = var6.get(var8);
               var1.append("\n" + this.spaces() + this.spaces() + var8 + ": " + var9 + "\n");
            }

            var1.append("}\n");
         }
      }

      var1.append(this.spaces() + ")\n");
      return var1.toString();
   }
}
