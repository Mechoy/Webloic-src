package weblogic.xml.util.cache.entitycache;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class Statistics implements Serializable, Cloneable {
   static final long serialVersionUID = 1L;
   transient boolean isPersistent = false;
   transient EntityCache cache = null;
   transient String path = null;
   StatsData statsData = new StatsData();

   Statistics(EntityCache var1) {
      this.cache = var1;
   }

   private void touch() {
      this.cache.statsCumulativeModification = true;
   }

   static Statistics initialize(EntityCache var0, String var1) {
      Statistics var2 = null;
      if ((new File(var1)).exists()) {
         try {
            var2 = (Statistics)EntityCache.loadFile(var1);
            var2.statsData.sessionStartClock = (double)(new Date()).getTime();
            var2.statsData.lastOngoingClockUpdate = var2.statsData.sessionStartClock;
         } catch (CX.FileLoadOutOfMemory var8) {
            try {
               var0.notifyListener(new Event.OutOfMemoryLoadingStatisticsEvent(var0, var1));
            } catch (Exception var7) {
            }

            var2 = null;
         } catch (Exception var9) {
            if (EntityCache.canRead(var1)) {
               try {
                  var0.notifyListener(new Event.StatisticsCorruptionEvent(var0, var1));
               } catch (Exception var6) {
               }
            } else {
               try {
                  var0.notifyListener(new Event.FileAccessErrorForStatisticsEvent(var0, var1, false));
               } catch (Exception var5) {
               }
            }

            var2 = null;
         }
      }

      if (var2 == null) {
         var2 = new Statistics(var0);
         var2.touch();
      }

      var2.cache = var0;
      var2.isPersistent = true;
      var2.path = var1;
      return var2;
   }

   synchronized Statistics copy() {
      try {
         Statistics var1 = (Statistics)this.clone();
         var1.statsData = this.statsData.copy();
         return var1;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   void clear() {
      this.statsData = new StatsData();
   }

   public Date getStartOfSampleCollection() {
      return this.statsData.startOfSampleCollection;
   }

   public synchronized double getHoursUpSinceSampleStart() {
      this.updateClock();
      return this.statsData.ongoingClock / 3600000.0;
   }

   public synchronized double getHoursThisSession() {
      return ((double)(new Date()).getTime() - this.statsData.sessionStartClock) / 3600000.0;
   }

   public long getTotalEntries() {
      return this.statsData.totalEntries;
   }

   public long getTotalTransientEntries() {
      return this.statsData.totalTransientEntries;
   }

   public long getTotalPersistentEntries() {
      return this.statsData.totalEntries - this.statsData.totalTransientEntries;
   }

   public long getTotalPersistedEntries() {
      return this.statsData.totalPersistedEntries;
   }

   public double getAvgPercentTransient() {
      return this.statsData.totalEntries != 0L ? (double)this.statsData.totalTransientEntries / (double)this.statsData.totalEntries * 100.0 : 0.0;
   }

   public double getAvgPercentPersistent() {
      return this.statsData.totalEntries != 0L ? ((double)this.statsData.totalEntries - (double)this.statsData.totalTransientEntries) / (double)this.statsData.totalEntries * 100.0 : 0.0;
   }

   public double getAvgTimout() {
      return this.statsData.avgTimout;
   }

   public double getMinEntryTimeout() {
      return this.statsData.minEntryTimeout;
   }

   public double getMaxEntryTimeout() {
      return this.statsData.maxEntryTimeout;
   }

   public double getAvgPerEntryMemorySize() {
      return this.statsData.avgPerEntryMemorySize;
   }

   public long getMaxEntryMemorySize() {
      return this.statsData.maxEntryMemorySize;
   }

   public long getMinEntryMemorySize() {
      return this.statsData.minEntryMemorySize;
   }

   public long getMaxEntryMemorySizeRequested() {
      return this.statsData.maxEntryMemorySizeRequested;
   }

   public double getAvgPerEntryDiskSize() {
      return this.statsData.avgPerEntryDiskSize;
   }

   public long getTotalNumberMemoryPurges() {
      return this.statsData.totalNumberMemoryPurges;
   }

   public long getTotalItemsMemoryPurged() {
      return this.statsData.totalItemsMemoryPurged;
   }

   public double getAvgEntrySizeMemoryPurged() {
      return this.statsData.avgEntrySizeMemoryPurged;
   }

   public Date getMostRecentMemoryPurge() {
      return this.statsData.mostRecentMemoryPurge;
   }

   public synchronized double getMemoryPurgesPerHour() {
      this.updateClock();
      double var1 = this.statsData.ongoingClock;
      if (var1 == 0.0) {
         var1 = 1.0;
      }

      double var3 = var1 / 3600000.0;
      double var5 = (double)this.statsData.totalNumberMemoryPurges / var3;
      return var5;
   }

   public long getTotalNumberDiskPurges() {
      return this.statsData.totalNumberDiskPurges;
   }

   public long getTotalItemsDiskPurged() {
      return this.statsData.totalItemsDiskPurged;
   }

   public double getAvgEntrySizeDiskPurged() {
      return this.statsData.avgEntrySizeDiskPurged;
   }

   public Date getMostRecentDiskPurge() {
      return this.statsData.mostRecentDiskPurge;
   }

   public synchronized double getDiskPurgesPerHour() {
      this.updateClock();
      double var1 = this.statsData.ongoingClock;
      if (var1 == 0.0) {
         var1 = 1.0;
      }

      double var3 = var1 / 3600000.0;
      double var5 = (double)this.statsData.totalNumberDiskPurges / var3;
      return var5;
   }

   public long getTotalNumberOfRejections() {
      return this.statsData.totalNumberOfRejections;
   }

   public long getTotalSizeOfRejections() {
      return this.statsData.totalSizeOfRejections;
   }

   public synchronized double getPercentRejected() {
      if (this.statsData.totalEntries == 0L) {
         return 0.0;
      } else {
         double var1 = (double)this.statsData.totalNumberOfRejections + (double)this.statsData.totalEntries;
         return (double)this.statsData.totalNumberOfRejections / var1 * 100.0;
      }
   }

   public long getTotalNumberOfRenewals() {
      return this.statsData.totalNumberOfRenewals;
   }

   synchronized void addEntry(CacheEntry var1) {
      long var2 = var1.getSize();
      if (this.statsData.minEntryMemorySize == 0L) {
         this.statsData.minEntryMemorySize = var2;
      } else if (var2 < this.statsData.minEntryMemorySize) {
         this.statsData.minEntryMemorySize = var2;
      }

      if (var2 > this.statsData.maxEntryMemorySize) {
         this.statsData.maxEntryMemorySize = var2;
      }

      this.statsData.avgPerEntryMemorySize = ((double)this.statsData.totalEntries * this.statsData.avgPerEntryMemorySize + (double)var2) / (double)(this.statsData.totalEntries + 1L);
      long var4 = var1.getLeaseInterval();
      if (this.statsData.minEntryTimeout == 0.0) {
         this.statsData.minEntryTimeout = (double)var4;
      } else if ((double)var4 < this.statsData.minEntryTimeout) {
         this.statsData.minEntryTimeout = (double)var4;
      }

      if ((double)var4 > this.statsData.maxEntryTimeout) {
         this.statsData.maxEntryTimeout = (double)var4;
      }

      this.statsData.avgTimout = ((double)this.statsData.totalEntries * this.statsData.avgTimout + (double)var4) / (double)(this.statsData.totalEntries + 1L);
      this.statsData.totalEntries++;
      if (!var1.isPersistentNoStupidException()) {
         this.statsData.totalTransientEntries++;
      }

      this.touch();
   }

   synchronized void writeEntry(CacheEntry var1) {
      this.statsData.totalPersistedEntries++;
      this.statsData.avgPerEntryDiskSize = ((double)(this.statsData.totalPersistedEntries - 1L) * this.statsData.avgPerEntryDiskSize + (double)var1.getDiskSizeNoStupidException()) / (double)this.statsData.totalPersistedEntries;
      this.touch();
   }

   synchronized void memoryPurge(long var1, long var3) {
      this.statsData.avgEntrySizeMemoryPurged = ((double)this.statsData.totalItemsMemoryPurged * this.statsData.avgEntrySizeMemoryPurged + (double)var3) / (double)(this.statsData.totalItemsMemoryPurged + var1);
      this.statsData.totalNumberMemoryPurges++;
      this.statsData.totalItemsMemoryPurged = var1;
      this.statsData.mostRecentMemoryPurge = new Date();
      this.touch();
   }

   synchronized void diskPurge(long var1, long var3) {
      this.statsData.avgEntrySizeDiskPurged = ((double)this.statsData.totalItemsDiskPurged * this.statsData.avgEntrySizeDiskPurged + (double)var3) / (double)(this.statsData.totalItemsDiskPurged + var1);
      this.statsData.totalNumberDiskPurges++;
      this.statsData.totalItemsDiskPurged = var1;
      this.statsData.mostRecentDiskPurge = new Date();
      this.touch();
   }

   synchronized void rejection(CacheEntry var1) {
      this.statsData.totalNumberOfRejections++;
      this.statsData.totalSizeOfRejections = var1.getSize();
      this.touch();
   }

   void renewal(CacheEntry var1) {
      this.statsData.totalNumberOfRenewals++;
      this.touch();
   }

   private void updateClock() {
      this.statsData.ongoingClock = (double)(new Date()).getTime() - this.statsData.lastOngoingClockUpdate;
      this.statsData.lastOngoingClockUpdate = (double)(new Date()).getTime();
   }

   synchronized void save() throws CX.EntityCacheException {
      if (this.isPersistent) {
         if (this.cache.statsCumulativeModification) {
            this.updateClock();

            try {
               EntityCache.saveFile(this, this.path);
            } catch (Exception var2) {
               this.cache.notifyListener(new Event.FileAccessErrorForStatisticsEvent(this.cache, this.path, true));
            }

         }
      }
   }

   class StatsData implements Serializable, Cloneable {
      static final long serialVersionUID = 1L;
      Date startOfSampleCollection = new Date();
      volatile long totalDiskUpdates = 0L;
      volatile long totalLostUpdates = 0L;
      private volatile double ongoingClock = 0.0;
      private transient volatile double sessionStartClock = (double)System.currentTimeMillis();
      private transient volatile double lastOngoingClockUpdate;
      private volatile long totalEntries;
      private volatile long totalPersistedEntries;
      private volatile long totalTransientEntries;
      private volatile double avgTimout;
      private volatile double minEntryTimeout;
      private volatile double maxEntryTimeout;
      private volatile double avgPerEntryMemorySize;
      private volatile long maxEntryMemorySize;
      private volatile long minEntryMemorySize;
      private volatile long maxEntryMemorySizeRequested;
      private volatile double avgPerEntryDiskSize;
      private volatile long totalNumberMemoryPurges;
      private volatile long totalItemsMemoryPurged;
      private volatile double avgEntrySizeMemoryPurged;
      private Date mostRecentMemoryPurge;
      private volatile long totalNumberDiskPurges;
      private volatile long totalItemsDiskPurged;
      private volatile double avgEntrySizeDiskPurged;
      private Date mostRecentDiskPurge;
      private volatile long totalNumberOfRejections;
      private volatile long totalSizeOfRejections;
      private volatile long totalNumberOfRenewals;

      StatsData() {
         this.lastOngoingClockUpdate = this.sessionStartClock;
         this.totalEntries = 0L;
         this.totalPersistedEntries = 0L;
         this.totalTransientEntries = 0L;
         this.avgTimout = 0.0;
         this.minEntryTimeout = 0.0;
         this.maxEntryTimeout = 0.0;
         this.avgPerEntryMemorySize = 0.0;
         this.maxEntryMemorySize = 0L;
         this.minEntryMemorySize = 0L;
         this.maxEntryMemorySizeRequested = 0L;
         this.avgPerEntryDiskSize = 0.0;
         this.totalNumberMemoryPurges = 0L;
         this.totalItemsMemoryPurged = 0L;
         this.avgEntrySizeMemoryPurged = 0.0;
         this.mostRecentMemoryPurge = null;
         this.totalNumberDiskPurges = 0L;
         this.totalItemsDiskPurged = 0L;
         this.avgEntrySizeDiskPurged = 0.0;
         this.mostRecentDiskPurge = null;
         this.totalNumberOfRejections = 0L;
         this.totalSizeOfRejections = 0L;
         this.totalNumberOfRenewals = 0L;
      }

      synchronized StatsData copy() {
         try {
            StatsData var1 = (StatsData)this.clone();
            return var1;
         } catch (CloneNotSupportedException var2) {
            return null;
         }
      }
   }
}
