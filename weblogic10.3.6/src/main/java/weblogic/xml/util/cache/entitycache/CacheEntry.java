package weblogic.xml.util.cache.entitycache;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class CacheEntry implements Serializable {
   static final long serialVersionUID = 1L;
   Object description = null;
   Object sourceIdentificationData = null;
   private long whenCreated;
   private volatile long whenLastAccessed;
   private long size;
   private long leaseInterval = 0L;
   private Serializable key;
   private boolean hasReader;
   transient volatile long expirationTime;
   transient String fileName;
   transient EntityCache cache;
   private transient AtomicReference<Serializable> theData = new AtomicReference();
   private transient long diskSize = 0L;
   transient CacheEntry prevAccess = null;
   transient CacheEntry nextAccess = null;
   private transient volatile boolean isPersistent = false;
   private transient volatile boolean isPersisted = false;

   public CacheEntry(EntityCache var1, Serializable var2, Serializable var3, long var4, Object var6, long var7) throws CX.EntityCacheException {
      this.hasReader = false;
      this.cache = var1;
      this.key = var2;
      this.sourceIdentificationData = var6;
      long var9 = System.currentTimeMillis();
      this.whenLastAccessed = this.whenCreated = var9;
      this.expirationTime = var9 + var7;
      this.size = var4;
      this.leaseInterval = var7;
      this.theData.set(var3);
   }

   public CacheEntry(EntityCache var1, Serializable var2, Reader var3, Object var4, long var5) throws CX.EntityCacheException {
      this.hasReader = true;
      this.cache = var1;
      this.key = var2;
      this.sourceIdentificationData = var4;
      long var7 = System.currentTimeMillis();
      this.whenLastAccessed = this.whenCreated = var7;
      this.expirationTime = var7 + var5;
      this.leaseInterval = var5;
      char[] var9 = this.readData(var3);
      this.theData.set(var9);
      this.size = (long)var9.length;
   }

   protected char[] readData(Reader var1) throws CX.EntityCacheException {
      Object var2 = null;
      CharArrayWriter var3 = null;

      try {
         var3 = new CharArrayWriter();
         char[] var4 = new char[1024];
         boolean var5 = false;

         int var15;
         while((var15 = var1.read(var4)) != -1) {
            var3.write(var4, 0, var15);
         }

         char[] var14 = var3.toCharArray();
         return var14;
      } catch (OutOfMemoryError var11) {
         throw var11;
      } catch (Exception var12) {
         throw new CX.EntityCacheException(var12);
      } finally {
         if (var3 != null) {
            var3.flush();
            var3.close();
         }

      }
   }

   void renewLease() throws CX.EntityCacheException {
      this.renewLease(this.leaseInterval);
   }

   void renewLease(long var1) throws CX.EntityCacheException {
      synchronized(this.cache) {
         long var4 = System.currentTimeMillis();
         this.leaseInterval = var1;
         this.expirationTime = var4 + var1;
         this.whenCreated = var4;
         this.cache.makeMostRecent(this);
         if (this.isPersistent) {
            this.saveIndex();
         }
      }

      if (this.cache.stats != null) {
         this.cache.stats.renewal(this);
      }

      if (this.cache.sessionStats != null) {
         this.cache.sessionStats.renewal(this);
      }

   }

   void stopLease() throws CX.EntityCacheException {
      synchronized(this.cache) {
         long var2 = System.currentTimeMillis();
         this.leaseInterval = 0L;
         this.whenCreated = this.expirationTime = var2;
         if (this.isPersistent) {
            this.saveIndex();
         }
      }

      if (this.cache.stats != null) {
         this.cache.stats.renewal(this);
      }

      if (this.cache.sessionStats != null) {
         this.cache.sessionStats.renewal(this);
      }

   }

   void updateAccessed() {
      this.whenLastAccessed = System.currentTimeMillis();
   }

   public Date getWhenLastAccessed() {
      return new Date(this.whenLastAccessed);
   }

   static char[] loadFilePerReader(String var0) throws CX.EntityCacheException {
      FileReader var1 = null;
      CharArrayWriter var2 = null;
      Object var3 = null;

      try {
         var1 = new FileReader(var0);
         var2 = new CharArrayWriter();
         char[] var4 = new char[1024];
         boolean var5 = false;

         int var19;
         while((var19 = var1.read(var4)) != -1) {
            var2.write(var4, 0, var19);
         }

         char[] var18 = var2.toCharArray();
         return var18;
      } catch (OutOfMemoryError var15) {
         throw new CX.FileLoadOutOfMemory(var0, var15);
      } catch (Exception var16) {
         Tools.px(var16);
         throw new CX.FileLoad(var0, var16);
      } finally {
         try {
            if (var1 != null) {
               var1.close();
            }

            if (var2 != null) {
               var2.close();
            }
         } catch (Exception var14) {
         }

      }
   }

   private Serializable loadData() throws CX.EntityCacheException {
      synchronized(this.cache) {
         Serializable var2 = (Serializable)this.theData.get();
         if (var2 != null) {
            return var2;
         } else {
            this.cache.loadCacheEntry(this);
            return (Serializable)this.theData.get();
         }
      }
   }

   synchronized void loadBytesCallback(String var1) throws CX.EntityCacheException {
      if (this.theData.get() == null) {
         Object var2;
         if (!this.hasReader) {
            var2 = EntityCache.loadFile(var1);
         } else {
            var2 = loadFilePerReader(var1);
         }

         this.theData.set(var2);
      }
   }

   protected void saveEntry() throws CX.EntityCacheException {
      if (this.isPersistent) {
         this.saveData();
         if (this.isPersistent) {
            this.saveIndex();
         }
      }
   }

   protected void saveIndex() throws CX.EntityCacheException {
      try {
         if (this.isPersistent) {
            String var1 = EntityCache.getIndexFilePath(this.cache.getRootPath(), this.fileName);

            try {
               EntityCache.saveFile(this, var1);
            } catch (Exception var3) {
               this.isPersistent = false;
               this.cache.notifyListener(new Event.FileAccessErrorForEntryEvent(this.cache, this, var1, true));
            }

         }
      } catch (Exception var4) {
         throw new CX.EntityCacheException(var4);
      }
   }

   void saveFilePerReader(char[] var1, String var2) throws CX.EntityCacheException {
      FileWriter var3 = null;

      try {
         var3 = new FileWriter(var2);
         var3.write(var1);
      } catch (Exception var13) {
         throw new CX.EntityCacheException(var13);
      } finally {
         if (var3 != null) {
            try {
               var3.flush();
               var3.close();
            } catch (Exception var12) {
            }
         }

      }

   }

   protected synchronized void saveData() throws CX.EntityCacheException {
      try {
         if (this.isPersistent) {
            if (!this.isPersisted) {
               this.isPersisted = true;
               long var1 = 0L;
               if (this.theData.get() != null) {
                  var1 = this.diskSize;
               }

               String var3 = EntityCache.getDataFilePath(this.cache.getRootPath(), this.fileName);

               try {
                  if (!this.hasReader) {
                     EntityCache.saveFile((Serializable)this.theData.get(), var3);
                  } else {
                     this.saveFilePerReader((char[])((char[])this.theData.get()), var3);
                  }
               } catch (Exception var8) {
                  this.isPersistent = false;
                  this.cache.notifyListener(new Event.FileAccessErrorForEntryEvent(this.cache, this, var3, true));
                  return;
               }

               long var4 = (new File(var3)).length();
               long var6 = this.cache.diskUsed - var1 + var4;
               if (var6 > this.cache.maxDisk) {
                  if (var4 > this.cache.maxDisk) {
                     throw new CX.EntryTooLargeDisk(this, this.cache.maxDisk);
                  }

                  this.cache.purgeDisk(var6 - this.cache.maxDisk, this);
               }

               this.cache.diskUsed = var6;
               this.diskSize = var4;
               if (this.cache.stats != null) {
                  this.cache.stats.writeEntry(this);
               }

               if (this.cache.stats != null) {
                  this.cache.sessionStats.writeEntry(this);
               }

            }
         }
      } catch (CX.EntityCacheException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new CX.EntityCacheException(var10);
      }
   }

   protected void purge() throws CX.EntityCacheException {
      if (this.theData.get() != null) {
         if (this.isPersistent) {
            try {
               this.saveEntry();
            } catch (Exception var2) {
               this.cache.removeEntry(this.key);
               return;
            }

            this.theData.set((Object)null);
            EntityCache var10000 = this.cache;
            var10000.memoryUsed -= this.size;
         } else {
            this.cache.removeEntry(this.key);
         }
      }

   }

   protected boolean isLoaded() {
      return this.theData.get() != null;
   }

   protected boolean isPersisted() {
      return this.isPersisted;
   }

   Object getData() throws CX.EntityCacheException {
      Object var1 = this.theData.get();
      if (var1 == null) {
         var1 = this.loadData();
      }

      this.cache.makeMostRecent(this);
      return !this.hasReader ? var1 : new CharArrayReader((char[])((char[])var1));
   }

   protected void delete() {
      if (this.isPersistent) {
         this.deleteEntryData(this.fileName, false);
      }
   }

   void makeTransient(boolean var1) {
      if (this.isPersistent) {
         try {
            this.deleteEntryData(this.fileName, var1);
         } catch (Exception var3) {
         }

      }
   }

   synchronized void deleteEntryData(String var1, boolean var2) {
      this.cache.deleteEntryData(var1, var2);
      this.isPersistent = false;
      this.isPersisted = false;
   }

   public boolean isPersistent() {
      return this.isPersistent;
   }

   public void setPersistent(boolean var1) {
      this.isPersistent = var1;
   }

   public boolean isPersistentNoStupidException() {
      return this.isPersistent;
   }

   public boolean isExpired() throws CX.EntityCacheException {
      return System.currentTimeMillis() > this.expirationTime;
   }

   public boolean olderThan(CacheEntry var1) {
      return this.whenLastAccessed > var1.whenLastAccessed;
   }

   public long getSecondsUntilExpiration() throws CX.EntityCacheException {
      return (System.currentTimeMillis() - this.expirationTime) / 1000L;
   }

   public long getMemorySize() {
      return this.theData.get() != null ? this.size : 0L;
   }

   synchronized void reactivateFromDisk(EntityCache var1, String var2, long var3) {
      this.cache = var1;
      this.fileName = var2;
      this.diskSize = var3;
      this.isPersistent = true;
      this.isPersisted = true;
   }

   public long getDiskSize() {
      return this.diskSize;
   }

   public long getDiskSizeNoStupidException() {
      return this.diskSize;
   }

   public Serializable getCacheKey() throws CX.EntityCacheException {
      return this.key;
   }

   public Object getDescription() throws CX.EntityCacheException {
      return this.description;
   }

   public void setDescription(Object var1) throws CX.EntityCacheException {
      this.description = var1;
   }

   public Object getSourceIdentificationData() throws CX.EntityCacheException {
      return this.sourceIdentificationData;
   }

   public void setSourceIdentificationData(Object var1) throws CX.EntityCacheException {
      this.sourceIdentificationData = var1;
   }

   public String toString() {
      return this.key.toString();
   }

   public long getSize() {
      return this.size;
   }

   public long getLeaseInterval() {
      return this.leaseInterval;
   }

   public String getFileName() {
      return this.fileName;
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject(this.description);
      var1.writeObject(this.sourceIdentificationData);
      var1.writeObject(new Date(this.whenCreated));
      var1.writeObject(new Date(this.whenLastAccessed));
      var1.writeLong(this.size);
      var1.writeLong(this.leaseInterval);
      var1.writeObject(this.key);
      var1.writeBoolean(this.hasReader);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.description = var1.readObject();
      this.sourceIdentificationData = var1.readObject();
      Date var2 = (Date)var1.readObject();
      if (var2 != null) {
         this.whenCreated = var2.getTime();
      }

      Date var3 = (Date)var1.readObject();
      if (var3 != null) {
         this.whenLastAccessed = var3.getTime();
      }

      this.size = var1.readLong();
      this.leaseInterval = var1.readLong();
      this.key = (Serializable)var1.readObject();
      this.hasReader = var1.readBoolean();
   }

   public Serializable getKey() {
      return this.key;
   }
}
