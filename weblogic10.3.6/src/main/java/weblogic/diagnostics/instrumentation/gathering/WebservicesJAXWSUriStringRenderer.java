package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;

public class WebservicesJAXWSUriStringRenderer implements ValueRenderer {
   public Object render(Object var1) {
      return var1 == null ? null : new WebservicesJAXWSEventInfoImpl(var1.toString());
   }
}
