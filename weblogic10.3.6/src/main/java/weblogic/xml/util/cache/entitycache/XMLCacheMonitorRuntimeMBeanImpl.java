package weblogic.xml.util.cache.entitycache;

import weblogic.cache.management.CacheMonitorRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.XMLCacheMonitorRuntimeMBean;

public class XMLCacheMonitorRuntimeMBeanImpl extends CacheMonitorRuntimeMBeanImpl implements XMLCacheMonitorRuntimeMBean {
   static final long serialVersionUID = -3112329298270789642L;
   private double AverageTimeout = 0.0;
   private double MinEntryTimeout = 0.0;
   private double MaxEntryTimeout = 0.0;
   private double AveragePerEntryMemorySize = 0.0;
   private double MinEntryMemorySize = 0.0;
   private double MaxEntryMemorySize = 0.0;

   public XMLCacheMonitorRuntimeMBeanImpl(String var1, ServerRuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public double getAverageTimeout() {
      return this.AverageTimeout;
   }

   public double getMinEntryTimeout() {
      return this.MinEntryTimeout;
   }

   public double getMaxEntryTimeout() {
      return this.MaxEntryTimeout;
   }

   public double getAveragePerEntryMemorySize() {
      return this.AveragePerEntryMemorySize;
   }

   public double getMinEntryMemorySize() {
      return this.MinEntryMemorySize;
   }

   public double getMaxEntryMemorySize() {
      return this.MaxEntryMemorySize;
   }

   public void changeAverageTimeout(double var1) {
      this.AverageTimeout = var1;
   }

   public void changeMinEntryTimeout(double var1) {
      this.MinEntryTimeout = var1;
   }

   public void changeMaxEntryTimeout(double var1) {
      this.MaxEntryTimeout = var1;
   }

   public void changeAveragePerEntryMemorySize(double var1) {
      this.AveragePerEntryMemorySize = var1;
   }

   public void changeMinEntryMemorySize(double var1) {
      this.MinEntryMemorySize = var1;
   }

   public void changeMaxEntryMemorySize(double var1) {
      this.MaxEntryMemorySize = var1;
   }
}
