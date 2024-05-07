package weblogic.diagnostics.instrumentation.gathering;

import weblogic.connector.outbound.ConnectionPool;
import weblogic.diagnostics.instrumentation.ValueRenderer;

public class JCAConnectionPoolRenderer implements ValueRenderer {
   public Object render(Object var1) {
      return var1 != null && var1 instanceof ConnectionPool ? ((ConnectionPool)var1).getName() : null;
   }
}
