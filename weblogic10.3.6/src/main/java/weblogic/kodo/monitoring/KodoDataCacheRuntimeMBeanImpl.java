package weblogic.kodo.monitoring;

import kodo.datacache.KodoDataCacheManager;
import kodo.datacache.MonitoringDataCache;
import weblogic.management.ManagementException;
import weblogic.management.runtime.KodoDataCacheRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class KodoDataCacheRuntimeMBeanImpl extends RuntimeMBeanDelegate implements KodoDataCacheRuntimeMBean {
   private MonitoringDataCache dataCache = null;
   private KodoDataCacheManager manager = null;

   public KodoDataCacheRuntimeMBeanImpl(String var1, RuntimeMBean var2, KodoDataCacheManager var3, MonitoringDataCache var4) throws ManagementException {
      super(var1, var2, true, "DataCacheRuntimes");
      this.dataCache = var4;
      this.manager = var3;
   }

   public void clear() {
      this.dataCache.clear();
   }

   public int getCacheHitCount() {
      return this.dataCache.getHits();
   }

   public double getCacheHitRatio() {
      return this.dataCache.getHitRate();
   }

   public int getCacheMissCount() {
      return this.dataCache.getMisses();
   }

   public int getTotalCurrentEntries() {
      return this.manager.size();
   }

   public String getStatistics() {
      return this.dataCache.getStatisticsString();
   }
}
