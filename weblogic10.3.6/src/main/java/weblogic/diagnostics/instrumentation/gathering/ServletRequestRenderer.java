package weblogic.diagnostics.instrumentation.gathering;

import javax.servlet.http.HttpServletRequestWrapper;
import weblogic.diagnostics.instrumentation.ValueRenderer;
import weblogic.servlet.internal.ServletRequestImpl;

public final class ServletRequestRenderer implements ValueRenderer {
   public Object render(Object var1) {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof ServletRequestImpl) {
         ServletRequestImpl var3 = (ServletRequestImpl)var1;
         return new ServletEventInfoImpl(var3.getRequestURI());
      } else if (var1 instanceof HttpServletRequestWrapper) {
         HttpServletRequestWrapper var2 = (HttpServletRequestWrapper)var1;
         return new ServletEventInfoImpl(var2.getRequestURI());
      } else {
         return null;
      }
   }
}
