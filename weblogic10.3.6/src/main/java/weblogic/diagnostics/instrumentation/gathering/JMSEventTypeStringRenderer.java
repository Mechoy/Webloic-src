package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;

public class JMSEventTypeStringRenderer implements ValueRenderer {
   private static final int LOG_CONSUMERCREATE = 1;
   private static final int LOG_CONSUMERDESTROY = 2;
   private static final String CONSUMERCREATE = "CREATE";
   private static final String CONSUMERDESTROY = "DESTROY";

   public Object render(Object var1) {
      if (var1 != null && var1 instanceof Integer) {
         int var2 = (Integer)var1;
         if (var2 == 1) {
            return "CREATE";
         } else {
            return var2 == 2 ? "DESTROY" : null;
         }
      } else {
         return null;
      }
   }
}
