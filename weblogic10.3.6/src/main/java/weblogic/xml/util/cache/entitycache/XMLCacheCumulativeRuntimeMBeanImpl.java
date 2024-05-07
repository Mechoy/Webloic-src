package weblogic.xml.util.cache.entitycache;

import java.util.Date;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.XMLCacheCumulativeRuntimeMBean;

public class XMLCacheCumulativeRuntimeMBeanImpl extends XMLCacheMonitorRuntimeMBeanImpl implements XMLCacheCumulativeRuntimeMBean {
   private static final boolean debug = true;
   private static final boolean verbose = true;
   private double AverageEntrySizeFlushed = 0.0;
   private Date MostRecentMemoryFlush = null;
   private double FlushesPerHour = 0.0;
   private long RejectionsCount = 0L;
   private double PercentRejected = 0.0;
   private long RenewalsCount = 0L;

   public XMLCacheCumulativeRuntimeMBeanImpl(String var1, ServerRuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public double getAverageEntrySizeFlushed() {
      return this.AverageEntrySizeFlushed;
   }

   public Date getMostRecentMemoryFlush() {
      return this.MostRecentMemoryFlush;
   }

   public double getFlushesPerHour() {
      return this.FlushesPerHour;
   }

   public long getRejectionsCount() {
      return this.RejectionsCount;
   }

   public double getPercentRejected() {
      return this.PercentRejected;
   }

   public long getRenewalsCount() {
      return this.RenewalsCount;
   }

   public void changeAverageEntrySizeFlushed(double var1) {
      this.AverageEntrySizeFlushed = var1;
   }

   public void changeMostRecentMemoryFlush(Date var1) {
      this.MostRecentMemoryFlush = var1;
   }

   public void changeFlushesPerHour(double var1) {
      this.FlushesPerHour = var1;
   }

   public void incrementRejectionsCount() {
      ++this.RejectionsCount;
   }

   public void changePercentRejected(double var1) {
      this.PercentRejected = var1;
   }

   public void incrementRenewalsCount() {
      ++this.RenewalsCount;
   }

   public static void main(String[] var0) {
   }
}
