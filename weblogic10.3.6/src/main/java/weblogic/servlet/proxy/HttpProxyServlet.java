package weblogic.servlet.proxy;

import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public final class HttpProxyServlet extends GenericProxyServlet {
   public void init(ServletConfig var1) throws ServletException {
      super.init(var1);
      String var2 = this.getInitParameter("redirectURL");
      if (var2 != null) {
         try {
            URL var3 = new URL(var2);
            this.destHost = var3.getHost();
            this.destPort = var3.getPort();
            if (this.destPort == -1) {
               this.destPort = 80;
            }
         } catch (MalformedURLException var4) {
            throw new ServletException("Bad redirectURL - " + var4.getMessage());
         }
      }

      if (this.destHost != null && this.destPort != 0) {
         this.httpVersion = this.getInitParameter("HttpVersion");
      } else {
         throw new ServletException("WebLogicHost/WebLogicPort is not defined.");
      }
   }
}
