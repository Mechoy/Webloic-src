package weblogic.diagnostics.instrumentation.gathering;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import weblogic.diagnostics.instrumentation.ValueRenderer;

public class ServletRenderer implements ValueRenderer {
   public Object render(Object var1) {
      if (var1 instanceof Servlet) {
         ServletConfig var2 = ((Servlet)var1).getServletConfig();
         if (var2 != null) {
            return var2.getServletName();
         }
      }

      return null;
   }
}
