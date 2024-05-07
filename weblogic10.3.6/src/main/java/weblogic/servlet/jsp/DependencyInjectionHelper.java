package weblogic.servlet.jsp;

import javax.servlet.jsp.PageContext;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebComponentCreator;

public class DependencyInjectionHelper {
   public static void inject(PageContext var0, Object var1) {
      if (var1 != null) {
         getWebComponentCreator(var0).inject(var1);
         getWebComponentCreator(var0).notifyPostConstruct(var1);
      }
   }

   public static void preDestroy(PageContext var0, Object var1) {
      if (var1 != null) {
         getWebComponentCreator(var0).notifyPreDestroy(var1);
      }
   }

   private static WebComponentCreator getWebComponentCreator(PageContext var0) {
      WebAppServletContext var1 = (WebAppServletContext)var0.getServletContext();
      return var1.getComponentCreator();
   }
}
