package weblogic.kodo.monitoring;

import java.util.Map;
import weblogic.management.ManagementException;
import weblogic.management.runtime.KodoQueryCompilationCacheRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class KodoQueryCompilationCacheRuntimeMBeanImpl extends RuntimeMBeanDelegate implements KodoQueryCompilationCacheRuntimeMBean {
   Map cache = null;

   public KodoQueryCompilationCacheRuntimeMBeanImpl(String var1, RuntimeMBean var2, Map var3) throws ManagementException {
      super(var1, var2, true, "QueryCompilationCacheRuntime");
      this.cache = var3;
   }

   public void clear() {
      this.cache.clear();
   }

   public int getTotalCurrentEntries() {
      return this.cache.size();
   }
}
