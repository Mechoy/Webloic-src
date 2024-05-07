package weblogic.cache.webapp;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import weblogic.cluster.ClusterService;
import weblogic.cluster.ClusterServices;
import weblogic.cluster.MulticastSession;
import weblogic.cluster.RecoverListener;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebService;

public class ClusterListener implements CacheListener, ServletContextListener {
   private MulticastSession multicastSession;
   private ServletContext sc;
   private String httpServerName;
   private String contextName;

   public void contextInitialized(ServletContextEvent var1) {
      this.sc = var1.getServletContext();
      WebAppServletContext var2 = (WebAppServletContext)this.sc;
      if (var2.getServer() == WebService.defaultHttpServer()) {
         this.httpServerName = null;
      } else {
         this.httpServerName = var2.getServer().getName();
      }

      this.contextName = var2.getContextPath();

      try {
         ClusterServices var3 = ClusterService.getServices();
         if (var3 == null) {
            this.sc.log("This server is not in a cluster.  Cluster caching disabled");
            return;
         }

         this.sc.log("Cluster caching enabled");
         this.multicastSession = var3.createMulticastSession((RecoverListener)null, -1);
         ServletCacheUtils.addCacheListener(this.sc, this);
      } catch (Exception var4) {
         this.sc.log("Could not register", var4);
      }

   }

   public void contextDestroyed(ServletContextEvent var1) {
      ServletCacheUtils.removeCacheListener(this.sc, this);
   }

   public void cacheUpdateOccurred(CacheListener.CacheEvent var1) {
      if (var1.getScope().equals("cluster")) {
         try {
            CacheMessage var2 = new CacheMessage(this.httpServerName, this.contextName, var1);
            this.multicastSession.send(var2);
         } catch (IOException var3) {
            this.sc.log("Could not send cache message", var3);
         }
      }

   }

   public void cacheFlushOccurred(CacheListener.CacheEvent var1) {
      this.cacheUpdateOccurred(var1);
   }

   public void cacheAccessOccurred(CacheListener.CacheEvent var1) {
   }
}
