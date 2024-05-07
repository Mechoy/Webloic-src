package weblogic.servlet.utils;

import java.util.Hashtable;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import weblogic.common.T3ServicesDef;
import weblogic.common.T3StartupDef;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebService;
import weblogic.utils.StackTraceUtils;

public final class ServletStartup implements T3StartupDef {
   public void setServices(T3ServicesDef var1) {
   }

   public String startup(String var1, Hashtable var2) throws Exception {
      WebAppServletContext var3 = WebService.defaultHttpServer().getServletContextManager().getDefaultContext();
      String var4 = (String)var2.get("servlet");
      if (var4 == null) {
         return "no servlet argument given";
      } else {
         Servlet var5 = null;

         try {
            var5 = var3.getServlet(var4);
         } catch (ServletException var7) {
            return "servlet: " + var4 + " threw ServletException " + StackTraceUtils.throwable2StackTrace(var7);
         }

         return var5 == null ? "servlet: " + var4 + " not found" : "servlet: " + var4 + " initialized";
      }
   }
}
