package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;
import weblogic.jdbc.common.internal.ConnectionEnv;
import weblogic.jdbc.wrapper.Statement;

public class JDBCStatementRenderer implements ValueRenderer {
   public Object render(Object var1) {
      if (var1 != null && var1 instanceof Statement) {
         Statement var2 = (Statement)var1;
         ConnectionEnv var3 = var2.getConnectionEnv();
         if (var2.sql == null && var3 == null) {
            return null;
         } else {
            return var3 == null ? new JDBCEventInfoImpl(var2.sql, (String)null) : new JDBCEventInfoImpl(var2.sql, var3.getPoolName(), var3.isInfected());
         }
      } else {
         return null;
      }
   }
}
