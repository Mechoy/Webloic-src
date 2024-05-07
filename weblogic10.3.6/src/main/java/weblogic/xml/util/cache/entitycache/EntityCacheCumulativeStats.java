package weblogic.xml.util.cache.entitycache;

import java.util.Date;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EntityCacheCumulativeRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;

public class EntityCacheCumulativeStats extends EntityCacheStats implements EntityCacheCumulativeRuntimeMBean {
   private Statistics stats;

   public EntityCacheCumulativeStats(String var1, RuntimeMBean var2, EntityCache var3, Statistics var4) throws ManagementException {
      super(var1, var2, var3);
      this.stats = var4;
   }

   Statistics getStats() {
      return this.stats;
   }

   boolean changesMade() {
      return this.cache.statsCumulativeModification;
   }

   public synchronized long getTotalNumberMemoryPurges() {
      return this.stats.getTotalNumberMemoryPurges();
   }

   public synchronized long getTotalItemsMemoryPurged() {
      return this.stats.getTotalItemsMemoryPurged();
   }

   public synchronized double getAvgEntrySizeMemoryPurged() {
      return this.stats.getAvgEntrySizeMemoryPurged();
   }

   public synchronized Date getMostRecentMemoryPurge() {
      return this.stats.getMostRecentMemoryPurge();
   }

   public synchronized double getMemoryPurgesPerHour() {
      return this.stats.getMemoryPurgesPerHour();
   }

   public synchronized long getTotalNumberDiskPurges() {
      return this.stats.getTotalNumberDiskPurges();
   }

   public synchronized long getTotalItemsDiskPurged() {
      return this.stats.getTotalItemsDiskPurged();
   }

   public synchronized double getAvgEntrySizeDiskPurged() {
      return this.stats.getAvgEntrySizeDiskPurged();
   }

   public synchronized Date getMostRecentDiskPurge() {
      return this.stats.getMostRecentDiskPurge();
   }

   public synchronized double getDiskPurgesPerHour() {
      return this.stats.getDiskPurgesPerHour();
   }

   public synchronized long getTotalNumberOfRejections() {
      return this.stats.getTotalNumberOfRejections();
   }

   public synchronized long getTotalSizeOfRejections() {
      return this.stats.getTotalSizeOfRejections();
   }

   public synchronized double getPercentRejected() {
      return this.stats.getPercentRejected();
   }

   public synchronized long getTotalNumberOfRenewals() {
      return this.stats.getTotalNumberOfRenewals();
   }
}
