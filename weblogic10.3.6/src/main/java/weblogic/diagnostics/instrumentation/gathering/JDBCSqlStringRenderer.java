package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;

public class JDBCSqlStringRenderer implements ValueRenderer {
   public Object render(Object var1) {
      return var1 == null ? null : new JDBCEventInfoImpl(var1.toString(), (String)null);
   }
}
