package weblogic.diagnostics.instrumentation.gathering;

import java.util.Iterator;
import java.util.Map;
import weblogic.diagnostics.instrumentation.ValueRenderer;

public class MapRenderer implements ValueRenderer {
   private static final String entryStart = "<\"";
   private static final String nullStr = "null";
   private static final String entryDelimiter = "\",\"";
   private static final String entryEnd = "\">";

   public Object render(Object var1) {
      if (var1 != null && var1 instanceof Map) {
         Map var2 = (Map)var1;
         StringBuffer var3 = new StringBuffer();
         Iterator var4 = var2.entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry var5 = (Map.Entry)var4.next();
            var3.append("<\"");
            var3.append(var5.getKey() == null ? "null" : var5.getKey().toString());
            var3.append("\",\"");
            var3.append(var5.getValue() == null ? "null" : var5.getValue().toString());
            var3.append("\">");
         }

         return var3.toString();
      } else {
         return null;
      }
   }
}
