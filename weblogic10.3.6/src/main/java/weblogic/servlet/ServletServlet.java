package weblogic.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.management.ManagementException;
import weblogic.servlet.internal.ServletStubImpl;
import weblogic.servlet.internal.WebAppServletContext;

public final class ServletServlet extends HttpServlet {
   private static final boolean DEBUG = false;
   private Map servletStubs = new HashMap();
   private WebAppServletContext context;

   public void init(ServletConfig var1) throws ServletException {
      super.init(var1);
      this.context = (WebAppServletContext)var1.getServletContext();
   }

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = (String)var1.getAttribute("javax.servlet.include.path_info");
      if (var3 == null) {
         var3 = var1.getPathInfo();
      }

      ServletStubImpl var4 = this.resolveServlet(var3);
      if (var4 != null) {
         var4.execute(var1, var2);
      } else {
         throw new ServletException("Failed to resolve PATH_INFO to a servlet: '" + var3 + "'");
      }
   }

   private void print(String var1) {
   }

   private ServletStubImpl resolveServlet(String var1) {
      ServletStubImpl var2 = null;
      var1 = var1.substring(1);
      String var3 = var1.replace('/', '.');

      while(true) {
         try {
            this.context.getServletClassLoader().loadClass(var3);
            break;
         } catch (ClassNotFoundException var6) {
            int var5 = var3.lastIndexOf(".");
            if (var5 == -1) {
               return null;
            }

            var3 = var3.substring(0, var5);
         }
      }

      String var4 = var3.replace('.', '/');
      var2 = (ServletStubImpl)this.servletStubs.get(var4);
      if (var2 == null) {
         var2 = this.registerServlet(var4, var3);
      }

      return var2;
   }

   private ServletStubImpl registerServlet(String var1, String var2) {
      ServletStubImpl var3 = null;

      try {
         var3 = new ServletStubImpl(var1, var2, this.context, (Map)null);
         var3.initRuntime();
      } catch (ManagementException var8) {
         HTTPLogger.logErrorCreatingServletStub((String)null, var1, var2, (Object)null, var8);
         return null;
      }

      synchronized(this.servletStubs) {
         Map var5 = (Map)((HashMap)this.servletStubs).clone();
         var5.put(var1, var3);
         this.servletStubs = var5;
         return var3;
      }
   }
}
