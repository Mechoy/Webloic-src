package weblogic.servlet;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.servlet.internal.ServletStubImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.jsp.AddToMapException;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.io.FilenameEncoder;

public class JSPClassServlet extends HttpServlet {
   protected String packagePrefix;
   protected WebAppServletContext context;
   private boolean internalApp = false;

   public synchronized void init(ServletConfig var1) throws ServletException {
      super.init(var1);
      this.context = (WebAppServletContext)var1.getServletContext();
      this.packagePrefix = this.context.getJSPManager().createJspConfig().getPackagePrefix();
      this.internalApp = this.context.isInternalApp();
   }

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws IOException, ServletException {
      StringBuilder var3 = new StringBuilder(30);
      String var4 = (String)var1.getAttribute("javax.servlet.include.servlet_path");
      String var5 = (String)var1.getAttribute("javax.servlet.include.path_info");
      if (var4 == null && var5 == null) {
         var4 = var1.getServletPath();
         var5 = var1.getPathInfo();
      }

      if (var4 != null) {
         var3.append(var4);
      }

      if (var5 != null) {
         var3.append(var5);
      }

      int var6 = var3.length();
      if (var6 != 0 && var3.charAt(var6 - 1) != '/') {
         String var7 = FilenameEncoder.resolveRelativeURIPath(var3.toString());
         String var8 = JSPServlet.uri2classname(this.packagePrefix, var7);
         if (this.internalApp) {
            var8 = var8.toLowerCase();
         }

         ClassFinder var9 = this.context.getResourceFinder("/");
         if (var9.getClassSource(var8) == null) {
            var2.sendError(404);
         } else {
            ServletStubImpl var10 = new ServletStubImpl(var7, var8, (WebAppServletContext)this.getServletContext(), (Map)null);
            throw new AddToMapException(var7, var10);
         }
      } else {
         var2.sendError(404);
      }
   }
}
