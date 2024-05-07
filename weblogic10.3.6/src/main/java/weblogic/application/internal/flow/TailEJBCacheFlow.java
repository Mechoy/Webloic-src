package weblogic.application.internal.flow;

import java.util.Iterator;
import java.util.Map;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.EarUtils;
import weblogic.ejb.spi.ScrubbedCache;

public final class TailEJBCacheFlow extends BaseFlow {
   public TailEJBCacheFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void activate() {
      this.startEJBCache();
   }

   public void deactivate() {
      this.stopEJBCache();
   }

   private void startEJBCache() {
      if (EarUtils.isDebugOn()) {
         EarUtils.debug(" starting cache scrubbers for app: " + this.appCtx.getApplicationId());
      }

      Map var1 = this.appCtx.getEJBCacheMap();
      if (var1 != null) {
         Iterator var2 = var1.values().iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 instanceof ScrubbedCache) {
               ((ScrubbedCache)var3).startScrubber();
            }
         }
      }

   }

   private void stopEJBCache() {
      if (EarUtils.isDebugOn()) {
         EarUtils.debug(" stopping cache scrubbers for app: " + this.appCtx.getApplicationId());
      }

      Map var1 = this.appCtx.getEJBCacheMap();
      if (var1 != null) {
         Iterator var2 = var1.values().iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 instanceof ScrubbedCache) {
               ((ScrubbedCache)var3).stopScrubber();
            }
         }
      }

   }
}
