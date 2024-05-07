package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;

public class ToStringRenderer implements ValueRenderer {
   public Object render(Object var1) {
      return var1 == null ? null : var1.toString();
   }
}
