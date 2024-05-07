package weblogic.kodo.monitoring;

import java.util.Map;
import javax.persistence.EntityManagerFactory;
import kodo.datacache.KodoDataCacheManager;
import kodo.datacache.MonitoringDataCache;
import kodo.datacache.MonitoringQueryCache;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.datacache.DataCacheManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import weblogic.management.ManagementException;
import weblogic.management.runtime.KodoDataCacheRuntimeMBean;
import weblogic.management.runtime.KodoPersistenceUnitRuntimeMBean;
import weblogic.management.runtime.KodoQueryCacheRuntimeMBean;
import weblogic.management.runtime.KodoQueryCompilationCacheRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;

public class KodoPersistenceUnitRuntimeMBeanFactory {
   private static KodoPersistenceUnitRuntimeMBeanFactory factory = new KodoPersistenceUnitRuntimeMBeanFactory();

   public static KodoPersistenceUnitRuntimeMBeanFactory getInstance() {
      return factory;
   }

   public KodoPersistenceUnitRuntimeMBean createKodoPersistenceUnitRuntimeMBean(String var1, RuntimeMBean var2, EntityManagerFactory var3) throws ManagementException {
      if (!(var3 instanceof OpenJPAEntityManagerFactorySPI)) {
         return null;
      } else {
         OpenJPAConfiguration var4 = ((OpenJPAEntityManagerFactorySPI)var3).getConfiguration();
         Map var5 = var4.getQueryCompilationCacheInstance();
         DataCacheManager var6 = var4.getDataCacheManagerInstance();
         if (var6 != null && var6 instanceof KodoDataCacheManager) {
            KodoPersistenceUnitRuntimeMBeanImpl var7 = new KodoPersistenceUnitRuntimeMBeanImpl(var1, var2);
            KodoDataCacheManager var8 = (KodoDataCacheManager)var6;
            KodoDataCacheRuntimeMBean[] var9 = this.getDataCacheRuntimeMBean(var7, var8);
            KodoQueryCacheRuntimeMBean[] var10 = this.getQueryCacheRuntimeMBean(var7, var8);
            KodoQueryCompilationCacheRuntimeMBean var11 = this.getQueryCompilationCacheRuntimeMBean(var7, var5);
            var7.setDataCacheRuntimes(var9);
            var7.setQueryCacheRuntimes(var10);
            var7.setQueryCompilationCacheRuntime(var11);
            return var7;
         } else {
            return null;
         }
      }
   }

   private KodoDataCacheRuntimeMBean[] getDataCacheRuntimeMBean(RuntimeMBean var1, KodoDataCacheManager var2) throws ManagementException {
      String[] var3 = var2.getDataCacheNames();
      if (var3 != null && var3.length != 0) {
         KodoDataCacheRuntimeMBean[] var4 = new KodoDataCacheRuntimeMBean[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            MonitoringDataCache var6 = (MonitoringDataCache)var2.getDataCache(var3[var5]);
            var4[var5] = new KodoDataCacheRuntimeMBeanImpl(var6.getName(), var1, var2, var6);
         }

         return var4;
      } else {
         return null;
      }
   }

   private KodoQueryCacheRuntimeMBean[] getQueryCacheRuntimeMBean(RuntimeMBean var1, KodoDataCacheManager var2) throws ManagementException {
      MonitoringQueryCache var3 = (MonitoringQueryCache)var2.getSystemQueryCache();
      return var3 != null ? new KodoQueryCacheRuntimeMBean[]{new KodoQueryCacheRuntimeMBeanImpl(var1.getName(), var1, var3)} : null;
   }

   private KodoQueryCompilationCacheRuntimeMBean getQueryCompilationCacheRuntimeMBean(RuntimeMBean var1, Map var2) throws ManagementException {
      return var2 != null ? new KodoQueryCompilationCacheRuntimeMBeanImpl(var1.getName(), var1, var2) : null;
   }
}
