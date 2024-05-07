package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;

public class JDBCPoolStringRenderer implements ValueRenderer {
   public Object render(Object var1) {
      return var1 == null ? null : new JDBCEventInfoImpl((String)null, var1.toString());
   }
}
