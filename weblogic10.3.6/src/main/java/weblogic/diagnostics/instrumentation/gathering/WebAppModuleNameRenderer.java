package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;
import weblogic.servlet.internal.WebAppModule;
import weblogic.servlet.internal.WebAppServletContext;

public final class WebAppModuleNameRenderer implements ValueRenderer {
   private static final String noModule = "<internal>";

   public Object render(Object var1) {
      String var2 = null;
      if (var1 == null) {
         var2 = "<internal>";
      } else if (var1 instanceof WebAppModule) {
         WebAppModule var3 = (WebAppModule)var1;
         var2 = var3.getName();
      } else if (var1 instanceof WebAppServletContext) {
         WebAppServletContext var5 = (WebAppServletContext)var1;
         WebAppModule var4 = var5.getWebAppModule();
         if (var4 != null) {
            var2 = var4.getName();
         }
      }

      return new WebApplicationEventInfoImpl(var2);
   }
}
