package weblogic.wsee.util;

import java.util.Iterator;
import java.util.Map;

public class ToStringWriter {
   StringBuffer sb = new StringBuffer();
   int level = 0;

   public void start(Object var1) {
      this.sb.append("(");
      this.sb.append(this.shortName(var1.getClass().getName()));
      this.sb.append("@");
      this.sb.append(System.identityHashCode(var1));
   }

   private String shortName(String var1) {
      int var2 = var1.lastIndexOf(".");
      return var2 == -1 ? var1 : var1.substring(var2 + 1, var1.length());
   }

   public void end() {
      this.sb.append(")");
   }

   public void writeField(String var1, int var2) {
      this.writeField(var1, "" + var2);
   }

   public void writeField(String var1, Object var2) {
      this.sb.append(" <");
      this.sb.append(var1);
      this.sb.append("=");
      if (var2 != null) {
         this.sb.append(var2);
      } else {
         this.sb.append("null");
      }

      this.sb.append(">");
   }

   public String toString() {
      return this.sb.toString();
   }

   public void writeArray(String var1, Iterator var2) {
      this.sb.append(" <");
      this.sb.append(var1);
      this.sb.append("[]{");

      while(var2.hasNext()) {
         this.sb.append(var2.next());
         if (var2.hasNext()) {
            this.sb.append(",");
         }
      }

      this.sb.append("}");
      this.sb.append(">");
   }

   public void writeMap(String var1, Map var2) {
      this.sb.append(" <");
      this.sb.append(var1);
      this.sb.append("{");
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         String var5 = var4.getKey() == null ? "" : var4.getKey().toString();
         this.writeField(var5, var4.getValue());
      }

      this.sb.append("}");
   }
}
