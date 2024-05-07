package weblogic.diagnostics.instrumentation.gathering;

import weblogic.connector.outbound.ConnectionHandlerBaseImpl;
import weblogic.diagnostics.instrumentation.ValueRenderer;

public class JCAConnectionHandlerPoolRenderer implements ValueRenderer {
   public Object render(Object var1) {
      return var1 != null && var1 instanceof ConnectionHandlerBaseImpl ? ((ConnectionHandlerBaseImpl)var1).getPoolName() : null;
   }
}
