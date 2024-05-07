package weblogic.diagnostics.instrumentation;

import com.bea.adaptive.harvester.WatchedValues;
import java.util.HashMap;
import java.util.Map;
import weblogic.diagnostics.harvester.WLDFHarvesterUtils;
import weblogic.diagnostics.harvester.internal.TreeBeanHarvestableDataProviderHelper;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WLDFInstrumentationRuntimeMBean;

public class InstrumentationRuntimeMBeanImpl extends RuntimeMBeanDelegate implements WLDFInstrumentationRuntimeMBean {
   private static final long NANOS_PER_MILLI = 1000000L;
   private InstrumentationScope scope;
   private HarvesterAttributeNormalizer attrNormalizer = new HarvesterAttributeNormalizer();

   public InstrumentationRuntimeMBeanImpl(InstrumentationScope var1, RuntimeMBean var2) throws ManagementException {
      super(var1.getName(), var2);
      this.scope = var1;
   }

   private InstrumentationStatistics getInstrumentationStatistics() {
      return this.scope != null ? this.scope.getInstrumentationStatistics() : null;
   }

   public int getInspectedClassesCount() {
      InstrumentationStatistics var1 = this.getInstrumentationStatistics();
      return var1 != null ? var1.getInspectedClassesCount() : 0;
   }

   public int getModifiedClassesCount() {
      InstrumentationStatistics var1 = this.getInstrumentationStatistics();
      return var1 != null ? var1.getModifiedClassesCount() : 0;
   }

   public long getMinWeavingTime() {
      InstrumentationStatistics var1 = this.getInstrumentationStatistics();
      return var1 != null ? var1.getMinWeavingTime() / 1000000L : 0L;
   }

   public long getMaxWeavingTime() {
      InstrumentationStatistics var1 = this.getInstrumentationStatistics();
      return var1 != null ? var1.getMaxWeavingTime() / 1000000L : 0L;
   }

   public long getTotalWeavingTime() {
      InstrumentationStatistics var1 = this.getInstrumentationStatistics();
      return var1 != null ? var1.getTotalWeavingTime() / 1000000L : 0L;
   }

   public int getExecutionJoinpointCount() {
      InstrumentationStatistics var1 = this.getInstrumentationStatistics();
      return var1 != null ? var1.getExecutionJoinpointCount() : 0;
   }

   public int getCallJoinpointCount() {
      InstrumentationStatistics var1 = this.getInstrumentationStatistics();
      return var1 != null ? var1.getCallJoinpointCount() : 0;
   }

   public int getClassweaveAbortCount() {
      InstrumentationStatistics var1 = this.getInstrumentationStatistics();
      return var1 != null ? var1.getClassweaveAbortCount() : 0;
   }

   public Map getMethodInvocationStatistics() {
      InstrumentationStatistics var1 = this.getInstrumentationStatistics();
      return (Map)(var1 != null ? var1.getMethodInvocationStatistics() : new HashMap());
   }

   public Object getMethodInvocationStatisticsData(String var1) throws ManagementException {
      var1 = this.attrNormalizer.getPartiallyNormalizedInvocationAttributeName(var1);

      try {
         String var2 = TreeBeanHarvestableDataProviderHelper.getObjectNameForBean(this);
         Object var5 = WLDFHarvesterUtils.getValue("ServerRuntime", WLDFInstrumentationRuntimeMBean.class.getName(), var2, var1);
         if (var5 != null && var5 instanceof WatchedValues.AttributeTrackedDataItem) {
            var5 = ((WatchedValues.AttributeTrackedDataItem)var5).getData();
         }

         if (var5 != null && var5.getClass().isArray()) {
            var5 = WLDFHarvesterUtils.getLeafValues((Object[])((Object[])var5));
         }

         return var5;
      } catch (Exception var4) {
         ManagementException var3 = new ManagementException(var4.getMessage());
         var3.setStackTrace(var4.getStackTrace());
         throw var3;
      }
   }

   public void resetMethodInvocationStatisticsData(String var1) throws ManagementException {
      Object var2 = this.getMethodInvocationStatisticsData(var1);
      if (var2 != null) {
         if (var2 instanceof Map) {
            synchronized(var2) {
               Map var4 = (Map)var2;
               var4.clear();
            }
         }

      }
   }

   public Map getMethodMemoryAllocationStatistics() {
      InstrumentationStatistics var1 = this.getInstrumentationStatistics();
      return (Map)(var1 != null ? var1.getMethodMemoryAllocationStatistics() : new HashMap());
   }

   public Object getMethodMemoryAllocationStatisticsData(String var1) throws ManagementException {
      var1 = this.attrNormalizer.getPartiallyNormalizedAllocationAttributeName(var1);

      try {
         String var2 = TreeBeanHarvestableDataProviderHelper.getObjectNameForBean(this);
         Object var5 = WLDFHarvesterUtils.getValue("ServerRuntime", WLDFInstrumentationRuntimeMBean.class.getName(), var2, var1);
         if (var5 != null && var5 instanceof WatchedValues.AttributeTrackedDataItem) {
            var5 = ((WatchedValues.AttributeTrackedDataItem)var5).getData();
         }

         if (var5 != null && var5.getClass().isArray()) {
            var5 = WLDFHarvesterUtils.getLeafValues((Object[])((Object[])var5));
         }

         return var5;
      } catch (Exception var4) {
         ManagementException var3 = new ManagementException(var4.getMessage());
         var3.setStackTrace(var4.getStackTrace());
         throw var3;
      }
   }

   public void resetMethodMemoryAllocationStatisticsData(String var1) throws ManagementException {
      Object var2 = this.getMethodMemoryAllocationStatisticsData(var1);
      if (var2 != null) {
         if (var2 instanceof Map) {
            synchronized(var2) {
               Map var4 = (Map)var2;
               var4.clear();
            }
         }

      }
   }
}
