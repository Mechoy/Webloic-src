package weblogic.cache.webapp;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import weblogic.cache.CacheException;
import weblogic.cache.CacheValue;
import weblogic.cache.RefWrapper;
import weblogic.cache.utils.BubblingCache;
import weblogic.cluster.GroupMessage;
import weblogic.rmi.spi.HostID;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebService;

class CacheMessage implements GroupMessage, Serializable {
   private static final long serialVersionUID = -8369932656337893639L;
   private String httpServerName;
   private String contextName;
   private CacheListener.CacheEvent ce;

   public CacheMessage(String var1, String var2, CacheListener.CacheEvent var3) {
      this.httpServerName = var1;
      this.contextName = var2;
      this.ce = var3;
   }

   public void execute(HostID var1) {
      WebAppServletContext var2;
      if (this.httpServerName == null) {
         var2 = WebService.defaultHttpServer().getServletContextManager().getContextForContextPath(this.contextName);
      } else {
         var2 = WebService.getHttpServer(this.httpServerName).getServletContextManager().getContextForContextPath(this.contextName);
      }

      try {
         CacheSystem var3 = new CacheSystem();
         ServletContextAttributeScope var4 = new ServletContextAttributeScope();
         var4.setContext(var2);
         var3.registerScope("cluster", var4);
         CacheValue var5 = this.ce.getValue();
         String var6 = this.ce.getScope();
         String var7 = this.ce.getName();
         KeySet var8 = this.ce.getKeySet();
         String var9 = var8 == null ? null : var8.getKey();
         if (var9 == null) {
            if (var5 == null) {
               var3.removeValueInScope(var6, var7);
            } else {
               var3.waitOnLock(var6, var7);
               var3.setValueInScope(var6, var7, new RefWrapper(var5));
               var3.releaseLock(var6, var7);
            }
         } else {
            var3.waitOnLock(var6, var7);

            Object var10;
            try {
               var10 = (Map)var3.getValueFromScope(var6, var7);
            } catch (ClassCastException var12) {
               var10 = null;
            }

            if (var10 == null) {
               int var11 = this.ce.getSize();
               if (var11 == -1) {
                  var10 = Collections.synchronizedMap(new HashMap());
               } else {
                  var10 = new BubblingCache(var11);
               }

               var3.setValueInScope(var6, var7, var10);
            }

            var3.releaseLock(var6, var7);
            if (var5 == null) {
               ((Map)var10).remove(var9);
            } else {
               String var14 = var7 + '\u0000' + var9;
               var3.waitOnLock(var6, var14);
               ((Map)var10).put(var9, new RefWrapper(var5));
               var3.releaseLock(var6, var14);
            }
         }
      } catch (CacheException var13) {
         if (var2 != null) {
            var2.log("Could not set cache value", var13);
         }
      }

   }

   public boolean runInSameThread() {
      return false;
   }
}
