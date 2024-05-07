package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;
import weblogic.jdbc.common.internal.ConnectionEnv;

public class JDBCConnectionEnvRenderer implements ValueRenderer {
   public Object render(Object var1) {
      if (var1 != null && var1 instanceof ConnectionEnv) {
         ConnectionEnv var2 = (ConnectionEnv)var1;
         return new JDBCEventInfoImpl((String)null, var2.getPoolName(), var2.isInfected());
      } else {
         return null;
      }
   }
}
