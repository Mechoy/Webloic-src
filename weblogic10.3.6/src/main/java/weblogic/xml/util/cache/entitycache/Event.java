package weblogic.xml.util.cache.entitycache;

import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

public class Event {
   public static class CacheFailureEvent extends CacheEvent {
      public String path;
      public String message;

      CacheFailureEvent(EntityCache var1, String var2, String var3) throws CX.EntityCacheException {
         super(var1);
         this.path = this.path;
         this.message = var3;
      }
   }

   public static class OutOfMemoryLoadingStatisticsEvent extends OutOfMemoryOnLoadEvent {
      OutOfMemoryLoadingStatisticsEvent(EntityCache var1, String var2) throws CX.EntityCacheException {
         super(var1, var2);
      }
   }

   public static class OutOfMemoryLoadingCacheEvent extends OutOfMemoryOnLoadEvent {
      OutOfMemoryLoadingCacheEvent(String var1) throws CX.EntityCacheException {
         super((EntityCache)null, var1);
      }
   }

   public static class OutOfMemoryLoadingEntryEvent extends OutOfMemoryOnLoadEvent {
      public Serializable key;

      OutOfMemoryLoadingEntryEvent(EntityCache var1, Serializable var2, String var3) throws CX.EntityCacheException {
         super(var1, var3);
         this.key = var2;
      }
   }

   public abstract static class OutOfMemoryOnLoadEvent extends OutOfMemoryEvent {
      public String path;

      OutOfMemoryOnLoadEvent(EntityCache var1, String var2) throws CX.EntityCacheException {
         super(var1);
         this.path = var2;
      }
   }

   public abstract static class OutOfMemoryEvent extends CacheEvent {
      OutOfMemoryEvent(EntityCache var1) throws CX.EntityCacheException {
         super(var1);
      }
   }

   public static class FileAccessErrorForStatisticsEvent extends FileAccessErrorEvent {
      FileAccessErrorForStatisticsEvent(EntityCache var1, String var2, boolean var3) throws CX.EntityCacheException {
         super(var1, var2, var3);
      }
   }

   public static class FileAccessErrorForCacheEvent extends FileAccessErrorEvent {
      FileAccessErrorForCacheEvent(EntityCache var1, String var2, boolean var3) throws CX.EntityCacheException {
         super(var1, var2, var3);
      }
   }

   public static class FileAccessErrorForEntryEvent extends FileAccessErrorEvent {
      public CacheEntry cacheEntry;

      FileAccessErrorForEntryEvent(EntityCache var1, CacheEntry var2, String var3, boolean var4) throws CX.EntityCacheException {
         super(var1, var3, var4);
         this.cacheEntry = this.cacheEntry;
      }
   }

   public abstract static class FileAccessErrorEvent extends FileErrorEvent {
      public boolean onRead;
      public boolean onWrite;

      FileAccessErrorEvent(EntityCache var1, String var2, boolean var3) throws CX.EntityCacheException {
         super(var1, var2);
         this.onWrite = var3;
         this.onRead = !var3;
      }
   }

   public abstract static class FileErrorEvent extends CacheEvent {
      public String path;

      FileErrorEvent(EntityCache var1, String var2) throws CX.EntityCacheException {
         super(var1);
         this.path = var2;
      }
   }

   public static class StatisticsCorruptionEvent extends CorruptionEvent {
      StatisticsCorruptionEvent(EntityCache var1, String var2) throws CX.EntityCacheException {
         super(var1, var2);
         this.cache = this.cache;
      }
   }

   public static class EntryCorruptionEvent extends CorruptionEvent {
      public Serializable key;

      EntryCorruptionEvent(EntityCache var1, Serializable var2, String var3) throws CX.EntityCacheException {
         super(var1, var3);
         this.key = var2;
         this.cache = this.cache;
      }
   }

   public static class CacheCorruptionEvent extends CorruptionEvent {
      CacheCorruptionEvent(String var1) throws CX.EntityCacheException {
         super((EntityCache)null, var1);
      }
   }

   public abstract static class CorruptionEvent extends CacheEvent {
      public String path;

      CorruptionEvent(EntityCache var1, String var2) throws CX.EntityCacheException {
         super(var1);
         this.path = var2;
      }
   }

   public static class CacheCloseEvent extends CacheEvent {
      CacheCloseEvent(EntityCache var1) throws CX.EntityCacheException {
         super(var1);
      }
   }

   public static class CacheLoadEvent extends CacheEvent {
      CacheLoadEvent(EntityCache var1) throws CX.EntityCacheException {
         super(var1);
      }
   }

   public static class CacheCreationEvent extends CacheEvent {
      CacheCreationEvent(EntityCache var1) throws CX.EntityCacheException {
         super(var1);
      }
   }

   public static class StatCheckpointEvent extends CacheEvent {
      StatCheckpointEvent(EntityCache var1) throws CX.EntityCacheException {
         super(var1);
      }
   }

   public static class EntryLoadEvent extends SingleEntryEvent {
      EntryLoadEvent(EntityCache var1, CacheEntry var2) throws CX.EntityCacheException {
         super(var1, var2);
      }
   }

   public static class EntryPersistEvent extends SingleEntryEvent {
      EntryPersistEvent(EntityCache var1, CacheEntry var2) throws CX.EntityCacheException {
         super(var1, var2);
      }
   }

   public static class EntryDeleteEvent extends SingleEntryEvent {
      EntryDeleteEvent(EntityCache var1, CacheEntry var2) throws CX.EntityCacheException {
         super(var1, var2);
      }
   }

   public static class EntryAddEvent extends SingleEntryEvent {
      EntryAddEvent(EntityCache var1, CacheEntry var2) throws CX.EntityCacheException {
         super(var1, var2);
      }
   }

   public static class EntryDiskRejectionEvent extends SingleEntryEvent {
      EntryDiskRejectionEvent(EntityCache var1, CacheEntry var2) throws CX.EntityCacheException {
         super(var1, var2);
      }
   }

   public static class EntryRejectionEvent extends SingleEntryEvent {
      EntryRejectionEvent(EntityCache var1, CacheEntry var2) throws CX.EntityCacheException {
         super(var1, var2);
      }
   }

   public abstract static class SingleEntryEvent extends CacheEvent {
      public CacheEntry cacheEntry = null;
      public long memorySize = -1L;
      public long diskSize = -1L;
      public long secondsUntilExpiration = -1L;

      SingleEntryEvent(EntityCache var1, CacheEntry var2) throws CX.EntityCacheException {
         super(var1);
         this.cacheEntry = var2;
         this.memorySize = var2.getMemorySize();
         this.diskSize = var2.getDiskSize();
         this.secondsUntilExpiration = var2.getSecondsUntilExpiration();
      }
   }

   public static class DiskPurgeEvent extends MultipleEntryEvent {
      DiskPurgeEvent(EntityCache var1, Vector var2) throws CX.EntityCacheException {
         super(var1, var2);
      }
   }

   public static class MemoryPurgeEvent extends MultipleEntryEvent {
      MemoryPurgeEvent(EntityCache var1, Vector var2) throws CX.EntityCacheException {
         super(var1, var2);
      }
   }

   public abstract static class MultipleEntryEvent extends CacheEvent {
      public Vector cacheEntries;
      public long combinedMemorySize = 0L;
      public long combinedDiskSize = 0L;

      MultipleEntryEvent(EntityCache var1, Vector var2) throws CX.EntityCacheException {
         super(var1);
         this.cacheEntries = var2;

         CacheEntry var4;
         for(Enumeration var3 = var2.elements(); var3.hasMoreElements(); this.combinedDiskSize += var4.getDiskSize()) {
            var4 = (CacheEntry)var3.nextElement();
            this.combinedMemorySize += var4.getMemorySize();
         }

      }
   }

   public abstract static class CacheEvent extends CacheUtilityEvent {
      public EntityCache cache = null;
      public long currentMemorySize;
      public long currentDiskSize;

      CacheEvent(EntityCache var1) throws CX.EntityCacheException {
         this.cache = var1;
         if (var1 != null) {
            this.currentMemorySize = var1.memoryUsed;
            this.currentDiskSize = var1.diskUsed;
         }

      }
   }

   public abstract static class CacheUtilityEvent {
      public Date timeStamp = new Date();

      CacheUtilityEvent() throws CX.EntityCacheException {
      }
   }
}
