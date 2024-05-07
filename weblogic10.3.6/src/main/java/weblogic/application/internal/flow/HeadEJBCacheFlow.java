package weblogic.application.internal.flow;

import java.util.HashMap;
import java.util.Map;
import weblogic.application.ApplicationContextInternal;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.EJBCacheFactory;
import weblogic.ejb.spi.ReInitializableCache;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.wl.ApplicationEntityCacheBean;
import weblogic.j2ee.descriptor.wl.EjbBean;
import weblogic.j2ee.descriptor.wl.MaxCacheSizeBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;

public final class HeadEJBCacheFlow extends BaseFlow {
   public HeadEJBCacheFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      this.setupEJB();
   }

   public void unprepare() {
      this.unSetupEJB();
   }

   private boolean isExclusiveCache(ApplicationEntityCacheBean var1) {
      return var1.getCachingStrategy().equalsIgnoreCase("Exclusive");
   }

   private boolean isMultiVersionCache(ApplicationEntityCacheBean var1) {
      return var1.getCachingStrategy().equalsIgnoreCase("MultiVersion");
   }

   private void setupEJB() throws DeploymentException {
      WeblogicApplicationBean var1 = null;
      EjbBean var2 = null;
      ApplicationEntityCacheBean[] var3 = null;
      HashMap var4 = new HashMap();
      HashMap var5 = new HashMap();
      this.appCtx.setEJBCacheMap(var4);
      this.appCtx.setEJBQueryCacheMap(var5);
      var1 = this.appCtx.getWLApplicationDD();
      if (var1 != null) {
         var2 = var1.getEjb();
         if (var2 != null) {
            var3 = var2.getEntityCaches();

            for(int var6 = 0; var6 < var3.length; ++var6) {
               ApplicationEntityCacheBean var7 = var3[var6];
               MaxCacheSizeBean var8 = var7.getMaxCacheSize();
               boolean var9 = true;
               int var10 = -1;
               long var11 = -1L;
               if (var8 == null) {
                  var10 = var7.getMaxBeansInCache();
               } else {
                  var9 = false;
                  if (var8.getBytes() == -1) {
                     var11 = (long)var8.getMegabytes() * 1048576L;
                  } else {
                     var11 = (long)var8.getBytes();
                  }
               }

               EJBCache var13 = null;
               if (this.isExclusiveCache(var7)) {
                  if (var9) {
                     var13 = EJBCacheFactory.createNRUCache(var7.getEntityCacheName(), var10);
                  } else {
                     var13 = EJBCacheFactory.createNRUCache(var7.getEntityCacheName(), var11);
                  }
               } else {
                  if (!this.isMultiVersionCache(var7)) {
                     throw new AssertionError("illegal caching scheme: " + var7.getCachingStrategy());
                  }

                  if (var9) {
                     var13 = EJBCacheFactory.createEntityCache(var7.getEntityCacheName(), var10);
                  } else {
                     var13 = EJBCacheFactory.createEntityCache(var7.getEntityCacheName(), var11);
                  }
               }

               var4.put(var7.getEntityCacheName(), var13);
               int var14 = var7.getMaxQueriesInCache();
               var5.put(var7.getEntityCacheName(), EJBCacheFactory.createQueryCache(var7.getEntityCacheName(), var14));
            }
         }
      }

      J2EEApplicationRuntimeMBeanImpl var16 = this.appCtx.getRuntime();
      ReInitializableCache[] var17 = (ReInitializableCache[])((ReInitializableCache[])var4.values().toArray(new ReInitializableCache[0]));
      var16.setApplicationCaches(var17);

      try {
         var16.setQueryCacheRuntimes(var5);
      } catch (ManagementException var15) {
         throw new DeploymentException("Error setting up query-caching runtimes", var15);
      }

      if (!var4.containsKey("ExclusiveCache")) {
         var4.put("ExclusiveCache", EJBCacheFactory.createNRUCache("ExclusiveCache", 1000));
      }

      if (!var4.containsKey("MultiVersionCache")) {
         var4.put("MultiVersionCache", EJBCacheFactory.createEntityCache("MultiVersionCache", 1000));
      }

   }

   private void unSetupEJB() {
      J2EEApplicationRuntimeMBeanImpl var1 = this.appCtx.getRuntime();
      var1.setApplicationCaches(new ReInitializableCache[0]);
      Map var2 = this.appCtx.getEJBCacheMap();
      if (var2 != null) {
         var2.clear();
      }

      Map var3 = this.appCtx.getEJBQueryCacheMap();
      if (var3 != null) {
         var3.clear();
      }

   }
}
