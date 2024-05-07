package weblogic.cache.webapp;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import weblogic.servlet.internal.ServletResponseImpl;

public class WebAppCacheSystem extends CacheSystem {
   private ServletRequestParameterScope srps;
   private ServletRequestAttributeScope sras;
   private ServletRequestCookieScope srcs;
   private ServletSessionAttributeScope ssas;
   private static ServletContextAttributeScope scas = new ServletContextAttributeScope();
   private ServletRequestHeaderScope sqhs;
   private ServletResponseHeaderScope sshs;
   private WebAppFileScope wafs;

   public WebAppCacheSystem() {
      this.registerScope("parameter", this.srps = new ServletRequestParameterScope());
      this.registerScope("request", this.sras = new ServletRequestAttributeScope());
      this.registerScope("cookie", this.srcs = new ServletRequestCookieScope());
      this.registerScope("requestHeader", this.sqhs = new ServletRequestHeaderScope());
      this.registerScope("responseHeader", this.sshs = new ServletResponseHeaderScope());
      this.registerScope("session", this.ssas = new ServletSessionAttributeScope());
      this.registerScope("application", scas);
      this.registerScope("file", this.wafs = new WebAppFileScope());
      this.registerScope("cluster", scas);
   }

   public void setRequest(HttpServletRequest var1) {
      this.srps.setRequest(var1);
      this.sras.setRequest(var1);
      this.srcs.setRequest(var1);
      this.sqhs.setRequest(var1);
      this.setSession(var1.getSession(false));
   }

   public void setResponse(HttpServletResponse var1) {
      if (var1 instanceof ServletResponseImpl) {
         ServletResponseImpl var2 = (ServletResponseImpl)var1;
         this.sshs.setResponse(var2);
      }

   }

   public void setPageContext(PageContext var1) {
      this.setRequest((HttpServletRequest)var1.getRequest());
      this.setResponse((HttpServletResponse)var1.getResponse());
      this.setContext(var1.getServletContext());
   }

   public void setSession(HttpSession var1) {
      this.ssas.setSession(var1);
   }

   public void setContext(ServletContext var1) {
      scas.setContext(var1);
      this.wafs.setContext(var1);
   }
}
