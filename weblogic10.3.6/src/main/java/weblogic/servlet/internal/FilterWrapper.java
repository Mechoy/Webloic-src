package weblogic.servlet.internal;

import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import weblogic.management.DeploymentException;
import weblogic.utils.classloaders.ChangeAwareClassLoader;

public class FilterWrapper {
   private static final String HEAD_FILTER = "weblogic.servlet.Filter.Head";
   private final WebAppServletContext context;
   private final String filtername;
   private final String filterclass;
   private final Map filterparams;
   private Filter filter;
   private ClassLoader contextLoader;
   private boolean reloadable = false;
   private boolean headFilter = false;

   public FilterWrapper(Filter var1, String var2, String var3, Map var4, WebAppServletContext var5) {
      this.filter = var1;
      this.filtername = var2;
      this.filterclass = var3;
      this.filterparams = var4;
      this.context = var5;
      this.contextLoader = var5.getServletClassLoader();
      this.reloadable = this.contextLoader instanceof ChangeAwareClassLoader;
      this.headFilter = var4 != null && "true".equalsIgnoreCase((String)var4.get("weblogic.servlet.Filter.Head"));
   }

   Filter getFilter() {
      return this.filter;
   }

   Filter getFilter(boolean var1) throws ServletException {
      try {
         if (var1 && this.reloadable) {
            this.checkForReload();
         }
      } catch (DeploymentException var3) {
         throw new ServletException(var3);
      }

      return this.filter;
   }

   boolean isHeadFilter() {
      return this.headFilter;
   }

   void setHeadFilter(boolean var1) {
      this.headFilter = var1;
   }

   private void reloadFilter(boolean var1) throws DeploymentException {
      Thread var2 = Thread.currentThread();

      try {
         var2.setContextClassLoader(this.contextLoader);
         this.context.getFilterManager().destroyFilter(this.filter, this.filtername);
         if (var1) {
            this.context.reloadServletClassLoader();
         }
      } finally {
         this.contextLoader = this.context.getServletClassLoader();
         this.reloadable = this.contextLoader instanceof ChangeAwareClassLoader;
         var2.setContextClassLoader(this.contextLoader);
      }

      this.filter = this.context.getFilterManager().loadFilter(this.filtername, this.filterclass, this.filterparams);
   }

   private void checkForReload() throws DeploymentException {
      if (this.context.getServletClassLoader() != this.contextLoader) {
         synchronized(this) {
            if (this.context.getServletClassLoader() != this.contextLoader) {
               this.reloadFilter(false);
               return;
            }
         }
      }

      long var1 = (long)this.context.getConfigManager().getServletReloadCheckSecs();
      if (var1 >= 0L && this.checkReloadTimeout(var1)) {
         synchronized(this) {
            if (this.context.getServletClassLoader() != this.contextLoader) {
               this.reloadFilter(false);
               return;
            }

            if (this.checkReloadTimeout(var1) && this.needToReload()) {
               this.reloadFilter(true);
            }
         }
      }

   }

   private final boolean checkReloadTimeout(long var1) {
      ChangeAwareClassLoader var3 = (ChangeAwareClassLoader)this.context.getServletClassLoader();
      long var4 = System.currentTimeMillis();
      return var4 - var1 * 1000L > var3.getLastChecked();
   }

   private boolean needToReload() {
      ChangeAwareClassLoader var1 = (ChangeAwareClassLoader)this.context.getServletClassLoader();
      return !var1.upToDate();
   }
}
