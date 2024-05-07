package weblogic.xml.util.cache.entitycache;

public class CX {
   public static class EntityCacheInternalException extends EntityCacheException {
      private static final long serialVersionUID = -2418110382655656920L;

      public EntityCacheInternalException() {
      }

      public EntityCacheInternalException(String var1) {
         super("Property Exception: " + var1);
      }

      public EntityCacheInternalException(Throwable var1) {
         this.previous = var1;
      }

      public EntityCacheInternalException(String var1, Throwable var2) {
         super(var1);
         this.previous = var2;
      }

      public Throwable getPrevious() {
         return this.previous;
      }
   }

   public static class FileLoadOutOfMemory extends FileLoad {
      private static final long serialVersionUID = 4798316024393810013L;

      public FileLoadOutOfMemory(String var1, Throwable var2) {
         super("Error loading file: " + var1, var2);
      }
   }

   public static class FileLoad extends EntityCacheException {
      private static final long serialVersionUID = -6088499391731515245L;

      public FileLoad(String var1, Throwable var2) {
         super("Error loading file: " + var1, var2);
      }
   }

   public static class UnnamedCache extends EntityCacheException {
      private static final long serialVersionUID = -7722783160384285313L;

      public UnnamedCache() {
         this((Throwable)null);
      }

      public UnnamedCache(Throwable var1) {
         super("New cache must be provided with a name.", var1);
      }
   }

   public static class EntryTooLargeDisk extends EntryTooLarge {
      private static final long serialVersionUID = 8758558572896661571L;

      public EntryTooLargeDisk(CacheEntry var1, long var2) {
         this(var1, var2, (Throwable)null);
      }

      public EntryTooLargeDisk(CacheEntry var1, long var2, Throwable var4) {
         super(var1, var1.getDiskSize(), var2, "disk", var4);
      }
   }

   public static class EntryTooLargeMemory extends EntryTooLarge {
      private static final long serialVersionUID = 1895054273049911015L;

      public EntryTooLargeMemory(CacheEntry var1, long var2) {
         this(var1, var2, (Throwable)null);
      }

      public EntryTooLargeMemory(CacheEntry var1, long var2, Throwable var4) {
         super(var1, var1.getSize(), var2, "memory", var4);
      }
   }

   public abstract static class EntryTooLarge extends EntityCacheException {
      private static final long serialVersionUID = -1455918258626459255L;
      CacheEntry cacheEntry;

      EntryTooLarge(CacheEntry var1, long var2, long var4, String var6, Throwable var7) {
         super("The " + var6 + " size (" + var2 + ") of CacheEntry " + var1.getKey() + " is larger than the maximum cache size: " + var4, var7);
      }
   }

   public static class EntryExpired extends EntityCacheException {
      private static final long serialVersionUID = 6195032839751825607L;
      public CacheEntry cacheEntry;

      public void renewLease() throws EntityCacheException {
         this.cacheEntry.renewLease();
      }

      public void renewLease(long var1) throws EntityCacheException {
         this.cacheEntry.renewLease(var1);
      }

      public EntryExpired(CacheEntry var1) {
         this(var1, (Throwable)null);
      }

      public EntryExpired(CacheEntry var1, Throwable var2) {
         super("CacheEntry " + var1.getKey() + " has expired.", var2);
         this.cacheEntry = var1;
      }
   }

   public static class EntityCacheException extends Exception implements Tools.ILinkableException {
      private static final long serialVersionUID = -2146832826767767621L;
      Throwable previous = null;

      public EntityCacheException() {
      }

      public EntityCacheException(String var1) {
         super("Property Exception: " + var1);
      }

      public EntityCacheException(Throwable var1) {
         this.previous = var1;
      }

      public EntityCacheException(String var1, Throwable var2) {
         super(var1);
         this.previous = var2;
      }

      public Throwable getPrevious() {
         return this.previous;
      }
   }
}
