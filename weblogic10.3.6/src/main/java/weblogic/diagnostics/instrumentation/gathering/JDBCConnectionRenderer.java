package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;
import weblogic.jdbc.common.internal.ConnectionEnv;
import weblogic.jdbc.wrapper.Connection;

public class JDBCConnectionRenderer implements ValueRenderer {
   public Object render(Object var1) {
      if (var1 != null && var1 instanceof Connection) {
         Connection var2 = (Connection)var1;
         ConnectionEnv var3 = var2.getConnectionEnv();
         return var3 == null ? new JDBCEventInfoImpl((String)null, var2.getPoolName()) : new JDBCEventInfoImpl((String)null, var2.getPoolName(), var3.isInfected());
      } else {
         return null;
      }
   }
}
