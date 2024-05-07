package weblogic.servlet.utils;

import com.bea.wls.redef.ClassRedefinitionRuntime;
import com.bea.wls.redef.RedefiningClassLoader;
import com.bea.wls.redef.RedefinitionTask;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.management.DeploymentException;
import weblogic.servlet.internal.WebAppServletContext;

public class FastSwapFilter implements Filter {
   private FilterConfig config;
   private long refreshInterval;
   private long lastRefreshTime;

   public void init(FilterConfig var1) throws ServletException {
      this.config = var1;
      this.initRefreshInterval(var1);
   }

   public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException {
      if (!(var1 instanceof HttpServletRequest) && !(var2 instanceof HttpServletResponse)) {
         var3.doFilter(var1, var2);
      } else {
         this.redefineClasses();
         var3.doFilter(var1, var2);
      }
   }

   private void redefineClasses() throws ServletException {
      long var1 = System.currentTimeMillis();
      if (var1 - this.lastRefreshTime >= this.refreshInterval) {
         try {
            WebAppServletContext var3 = (WebAppServletContext)this.config.getServletContext();
            Object var4 = var3.getApplicationContext().getAppClassLoader();
            if (!(var4 instanceof RedefiningClassLoader)) {
               var4 = var3.getServletClassLoader();
            }

            if (var4 instanceof RedefiningClassLoader) {
               RedefiningClassLoader var5 = (RedefiningClassLoader)var4;
               ClassRedefinitionRuntime var6 = var5.getRedefinitionRuntime();
               RedefinitionTask var7 = new RedefinitionTask(var6, (String)null, (String[])null);
               var6.redefineClasses((String)null, (Set)null, var7);
            }
         } catch (Exception var8) {
            throw new ServletException(var8.getMessage(), var8);
         }

         this.lastRefreshTime = var1;
      }
   }

   public void destroy() {
   }

   private void initRefreshInterval(FilterConfig var1) {
      WebAppServletContext var2 = (WebAppServletContext)var1.getServletContext();
      this.refreshInterval = 1000L * (long)var2.getWebAppModule().getFastSwapRefreshInterval();
   }

   public static void registerFastSwapFilter(WebAppServletContext var0) throws DeploymentException {
      var0.registerFilter("FastSwapFilter", FastSwapFilter.class.getName(), new String[]{"/*"}, (String[])null, Collections.emptyMap());
   }
}
