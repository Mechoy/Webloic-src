package weblogic.kodo.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.KodoDataCacheRuntimeMBean;
import weblogic.management.runtime.KodoPersistenceUnitRuntimeMBean;
import weblogic.management.runtime.KodoQueryCacheRuntimeMBean;
import weblogic.management.runtime.KodoQueryCompilationCacheRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class KodoPersistenceUnitRuntimeMBeanImpl extends RuntimeMBeanDelegate implements KodoPersistenceUnitRuntimeMBean {
   KodoDataCacheRuntimeMBean[] dataCacheRuntimes = null;
   KodoQueryCacheRuntimeMBean[] queryCacheRuntimes = null;
   KodoQueryCompilationCacheRuntimeMBean queryCompilationCacheRuntimes = null;
   String persistenceUnitname = null;

   public KodoPersistenceUnitRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2, true, "PersistenceUnitRuntime");
      this.persistenceUnitname = var1;
   }

   public void setDataCacheRuntimes(KodoDataCacheRuntimeMBean[] var1) {
      this.dataCacheRuntimes = var1;
   }

   public void setQueryCacheRuntimes(KodoQueryCacheRuntimeMBean[] var1) {
      this.queryCacheRuntimes = var1;
   }

   public void setQueryCompilationCacheRuntime(KodoQueryCompilationCacheRuntimeMBean var1) {
      this.queryCompilationCacheRuntimes = var1;
   }

   public KodoDataCacheRuntimeMBean[] getDataCacheRuntimes() {
      return this.dataCacheRuntimes;
   }

   public KodoQueryCacheRuntimeMBean[] getQueryCacheRuntimes() {
      return this.queryCacheRuntimes;
   }

   public KodoQueryCompilationCacheRuntimeMBean getQueryCompilationCacheRuntime() {
      return this.queryCompilationCacheRuntimes;
   }

   public String getPersistenceUnitName() {
      return this.persistenceUnitname;
   }
}
