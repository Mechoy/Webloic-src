package weblogic.wsee.deploy;

import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;
import weblogic.wsee.wsdl.RelativeResourceResolver;

public class ServletContextRelativeResourceResolver implements RelativeResourceResolver {
   private final ServletContext servletContext;

   public ServletContextRelativeResourceResolver(ServletContext var1) {
      this.servletContext = var1;
   }

   public URL getResource(String var1) {
      if (var1.charAt(0) != '/') {
         var1 = '/' + var1;
      }

      try {
         return this.servletContext.getResource(var1);
      } catch (MalformedURLException var3) {
         return null;
      }
   }
}
