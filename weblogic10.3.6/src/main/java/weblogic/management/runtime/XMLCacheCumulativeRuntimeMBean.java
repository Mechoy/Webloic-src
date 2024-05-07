package weblogic.management.runtime;

import java.util.Date;

/** @deprecated */
public interface XMLCacheCumulativeRuntimeMBean extends XMLCacheMonitorRuntimeMBean {
   double getAverageEntrySizeFlushed();

   Date getMostRecentMemoryFlush();

   double getFlushesPerHour();

   long getRejectionsCount();

   double getPercentRejected();

   long getRenewalsCount();
}
