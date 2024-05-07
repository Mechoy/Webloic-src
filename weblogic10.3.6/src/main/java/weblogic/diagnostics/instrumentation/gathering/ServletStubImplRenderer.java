package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;
import weblogic.servlet.internal.ServletStubImpl;

public class ServletStubImplRenderer implements ValueRenderer {
   public Object render(Object var1) {
      if (var1 instanceof ServletStubImpl) {
         ServletStubImpl var2 = (ServletStubImpl)var1;
         return var2.getServletName();
      } else {
         return null;
      }
   }
}
