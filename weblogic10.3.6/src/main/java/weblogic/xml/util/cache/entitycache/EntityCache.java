package weblogic.xml.util.cache.entitycache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.XMLEntityCacheMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class EntityCache implements Serializable {
   static final long serialVersionUID = 1L;
   static ServerMBean serverConfigMBean = null;
   static final long DefaultCacheSize = 2000000L;
   static final long DefaultCacheDiskSize = 5000000L;
   static final int DefaultSizeBias = 5;
   static final String DefaultCachePath = "EntityCache";
   long maxMemory;
   long maxDisk;
   long memoryUsed;
   long fileNameCounter;
   double sizeBias;
   String name;
   transient StatisticsMonitor statisticsMonitor;
   transient Statistics stats;
   transient Statistics sessionStats;
   transient Statistics currentStats;
   private String rootPath;
   static transient Hashtable<String, EntityCache> caches = new Hashtable();
   transient ConcurrentHashMap<Serializable, CacheEntry> entries;
   transient long diskUsed;
   transient AccessList accessList;
   static transient Persister persister = null;
   transient CacheListener listener;
   transient volatile boolean statsCumulativeModification;
   transient volatile boolean statsCurrentModification;
   transient long memLossTotal;
   transient long diskLossTotal;

   static ServerMBean getServerConfigMBean() throws CX.EntityCacheException {
      if (serverConfigMBean == null) {
         AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         serverConfigMBean = ManagementService.getRuntimeAccess(var0).getServer();
         if (serverConfigMBean == null) {
            throw new CX.EntityCacheException("ServerConfigMBean can't be null!");
         }
      }

      return serverConfigMBean;
   }

   public static EntityCache getCache(CacheSpec var0, XMLEntityCacheMBean var1) throws CX.EntityCacheException {
      boolean var2 = false;
      String var3 = var0.path;
      if (!var3.endsWith(String.valueOf(File.separatorChar))) {
         var3 = var3 + File.separatorChar;
      }

      EntityCache var4 = (EntityCache)caches.get(var3);
      if (var4 == null) {
         var2 = true;
         if ((new File(getCacheFilePath(var3))).exists()) {
            var4 = open(var3, var0);
         } else {
            var4 = create(var3, var0);
         }
      }

      if (var4 == null) {
         return null;
      } else {
         var4.name = var0.name;
         var4.listener = var0.cacheListener;
         var4.setMemoryFootprint(var0.memSize);
         var4.setDiskFootprint(var0.diskSize);
         File var5 = new File(var3);
         if (!var5.exists()) {
            var5.mkdirs();
         }

         var5 = new File(getIndexDirectory(var3));
         if (!var5.exists()) {
            var5.mkdirs();
         }

         var5 = new File(getDataDirectory(var3));
         if (!var5.exists()) {
            var5.mkdirs();
         }

         try {
            saveFile(var4, getCacheFilePath(var3));
         } catch (Exception var12) {
            if (var0.cacheListener != null) {
               var0.cacheListener.notify(new Event.FileAccessErrorForCacheEvent(var4, var3, true));
            }
         }

         if (var2) {
            caches.put(var3, var4);

            try {
               AuthenticatedSubject var6 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
               ServerRuntimeMBean var7 = ManagementService.getRuntimeAccess(var6).getServerRuntime();
               var4.statisticsMonitor = new StatisticsMonitor(var4, 300000L);
               var4.stats = Statistics.initialize(var4, getStatsFilePath(var4.rootPath));
               EntityCacheCumulativeStats var8 = var4.registerCumulativeStatsMBean("EntityCacheHistorical_", var4.stats);
               var4.statisticsMonitor.addSubject(var4.sessionStats, var8);
               var7.setEntityCacheHistoricalRuntime(var8);
               var4.sessionStats = new Statistics(var4);
               EntityCacheCumulativeStats var9 = var4.registerCumulativeStatsMBean("EntityCacheCumulative_", var4.sessionStats);
               var7.setEntityCacheCumulativeRuntime(var9);
               var4.statisticsMonitor.addSubject(var4.sessionStats, var9);
               var4.currentStats = new Statistics(var4);
               EntityCacheCurrentStats var10 = new EntityCacheCurrentStats(var4);
               var4.statisticsMonitor.addSubject(var4.currentStats, var10);
               var7.setEntityCacheCurrentStateRuntime(var10);
               if (var1 != null) {
                  var1.setEntityCacheHistoricalRuntime(var8);
                  var1.setEntityCacheSessionRuntime(var9);
                  var1.setEntityCacheCurrentRuntime(var10);
               }

               var4.statisticsMonitor.start();
            } catch (ManagementException var11) {
               throw new CX.EntityCacheException(var11);
            }
         }

         return var4;
      }
   }

   public String getName() {
      return this.name;
   }

   public void addEntry(Serializable var1, CacheEntry var2) throws CX.EntityCacheException {
      this.addEntry(var1, var2, true);
   }

   public void addEntry(Serializable var1, CacheEntry var2, boolean var3) throws CX.EntityCacheException {
      synchronized(this) {
         this.removeEntry(var1);
         var2.setPersistent(var3);
         this.findSpace(var2);
         this.put(var1, var2);
         this.accessList.addMostRecent(var2);
         this.memoryUsed += var2.getSize();
         var2.setFileName(this.getNewFileName());
      }

      if (var3) {
         Persister.get().add(var2);
      }

      if (this.stats != null) {
         this.stats.addEntry(var2);
      }

      if (this.sessionStats != null) {
         this.sessionStats.addEntry(var2);
      }

      this.notifyListener(new Event.EntryAddEvent(this, var2));
      this.statsCurrentModification = true;
   }

   public Object getData(Serializable var1) throws CX.EntityCacheException {
      Object var2 = null;
      CacheEntry var3 = this.get(var1);
      if (var3 != null) {
         if (var3.isExpired()) {
            throw new CX.EntryExpired(var3);
         }

         var2 = var3.getData();
      }

      return var2;
   }

   public synchronized CacheEntry renewLease(Serializable var1) throws CX.EntityCacheException {
      CacheEntry var2 = this.get(var1);
      if (var2 != null) {
         var2.renewLease();
      }

      return var2;
   }

   public synchronized CacheEntry putrify(Serializable var1) throws CX.EntityCacheException {
      CacheEntry var2 = this.get(var1);
      if (var2 != null) {
         var2.stopLease();
      }

      return var2;
   }

   public synchronized CacheEntry renewLease(Serializable var1, long var2) throws CX.EntityCacheException {
      CacheEntry var4 = this.get(var1);
      if (var4 != null) {
         var4.renewLease(var2);
         this.accessList.moveMostRecent(var4);
      }

      return var4;
   }

   public synchronized CacheEntry removeEntry(Serializable var1) throws CX.EntityCacheException {
      CacheEntry var2 = this.get(var1);
      if (var2 != null) {
         this.remove(var1);
         this.accessList.remove(var2);
         this.reduceMemoryUsed(var2.getSize());
         var2.delete();
         this.notifyListener(new Event.EntryDeleteEvent(this, var2));
      }

      this.statsCurrentModification = true;
      return var2;
   }

   public synchronized long getMemoryFootprint() {
      return this.maxMemory;
   }

   public synchronized void setMemoryFootprint(long var1) throws CX.EntityCacheException {
      long var3 = this.memoryUsed - var1;
      if (var3 != 0L) {
         if (var3 > 0L) {
            this.purge(var3);
         }

         this.maxMemory = var1;
      }
   }

   public synchronized long getDiskFootprint() {
      return this.maxDisk;
   }

   public synchronized void setDiskFootprint(long var1) throws CX.EntityCacheException {
      long var3 = this.diskUsed - var1;
      if (var3 != 0L) {
         if (var3 > 0L) {
            this.purgeDisk(var3);
         }

         this.maxDisk = var1;
      }
   }

   public synchronized void close() throws CX.EntityCacheException {
      caches.remove(this.rootPath);
      if (this.statisticsMonitor != null) {
         this.statisticsMonitor.finish();
      }

      this.notifyListener(new Event.CacheCloseEvent(this));
      this.listener = null;
   }

   synchronized void notifyListener(Event.CacheUtilityEvent var1) {
      if (this.listener != null) {
         this.listener.notify(var1);
      }

   }

   public void makeMostRecent(CacheEntry var1) {
      var1.updateAccessed();
      this.accessList.moveMostRecent(var1);
   }

   protected EntityCache(String var1, String var2) {
      this(var1, 2000000L, 5000000L, 5, var2);
   }

   protected EntityCache(String var1, long var2, long var4, int var6, String var7) {
      this.maxMemory = 0L;
      this.maxDisk = 0L;
      this.memoryUsed = 0L;
      this.fileNameCounter = -1L;
      this.sizeBias = 0.0;
      this.name = null;
      this.statisticsMonitor = null;
      this.stats = null;
      this.sessionStats = null;
      this.currentStats = null;
      this.entries = null;
      this.diskUsed = 0L;
      this.accessList = null;
      this.listener = null;
      this.statsCumulativeModification = false;
      this.statsCurrentModification = false;
      this.memLossTotal = 0L;
      this.diskLossTotal = 0L;
      this.name = var1;
      this.maxMemory = var2;
      this.maxDisk = var4;
      this.sizeBias = (double)var6 / 100.0;
      this.rootPath = var7;
   }

   void put(Serializable var1, CacheEntry var2) {
      this.entries.put(var1, var2);
   }

   CacheEntry get(Serializable var1) {
      CacheEntry var2 = (CacheEntry)this.entries.get(var1);
      return var2;
   }

   CacheEntry remove(Serializable var1) {
      CacheEntry var2 = (CacheEntry)this.entries.remove(var1);
      return var2;
   }

   public String getIndexFilePath(String var1) {
      return getIndexFilePath(this.rootPath, var1);
   }

   public String getDataFilePath(String var1) {
      return getDataFilePath(this.rootPath, var1);
   }

   static String getIndexFilePath(String var0, String var1) {
      return getIndexDirectory(var0) + var1;
   }

   static String getIndexDirectory(String var0) {
      return var0 + "index" + File.separatorChar;
   }

   static String getDataFilePath(String var0, String var1) {
      return getDataDirectory(var0) + var1;
   }

   static String getDataDirectory(String var0) {
      return var0 + "data" + File.separatorChar;
   }

   static String getCacheFilePath(String var0) {
      return var0 + File.separatorChar + "cache";
   }

   static String getStatsFilePath(String var0) {
      return var0 + File.separatorChar + "stats";
   }

   protected static EntityCache open(String var0, CacheSpec var1) throws CX.EntityCacheException {
      EntityCache var2 = null;

      try {
         try {
            var2 = (EntityCache)loadFile(getCacheFilePath(var0));
            var2.initTransientInfo(var0, var1);
            if (var1.cacheListener != null) {
               var1.cacheListener.notify(new Event.CacheLoadEvent(var2));
            }
         } catch (CX.FileLoadOutOfMemory var16) {
            Tools.log("Unable to load cache data record from disk (too big!): " + var0 + ". Rebuilding from index...");
            if (var1.cacheListener != null) {
               var1.cacheListener.notify(new Event.OutOfMemoryLoadingCacheEvent(var0));
            }
         } catch (CX.FileLoad var17) {
            Tools.log("Unable to load cache data record from disk (corrupted?): " + var0 + ". Rebuilding from index...");
            if (canRead(getCacheFilePath(var0))) {
               if (var1.cacheListener != null) {
                  var1.cacheListener.notify(new Event.CacheCorruptionEvent(var0));
               }
            } else if (var1.cacheListener != null) {
               var1.cacheListener.notify(new Event.FileAccessErrorForCacheEvent((EntityCache)null, var0, false));
            }
         }

         if (var2 == null) {
            var2 = create(var0, var1);
         } else {
            var2.name = var1.name;
            var2.setMemoryFootprint(var1.memSize);
            var2.setDiskFootprint(var1.diskSize);
         }

         String[] var3 = (new File(getIndexDirectory(var0))).list();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            long var5 = 0L;

            try {
               Long.parseLong(var3[var4]);
            } catch (NumberFormatException var13) {
            }

            CacheEntry var7 = null;
            String var8 = getIndexFilePath(var0, var3[var4]);
            String var9 = getDataFilePath(var0, var3[var4]);

            try {
               var7 = (CacheEntry)loadFile(var8);
            } catch (CX.FileLoadOutOfMemory var14) {
               Tools.log("Unable to load cache index data record from disk (too big!). Deleting entry...");
               var2.notifyListener(new Event.OutOfMemoryLoadingEntryEvent(var2, (Serializable)null, var0));
               continue;
            } catch (CX.FileLoad var15) {
               if (canRead(var8)) {
                  var2.notifyListener(new Event.EntryCorruptionEvent(var2, (Serializable)null, var0));
               } else {
                  var2.notifyListener(new Event.FileAccessErrorForEntryEvent(var2, (CacheEntry)null, var0, false));
               }

               Tools.log("Unable to load cache index data record from disk (corrupted?). Deleting entry...");
               var2.deleteEntryData(var3[var4], true);
               continue;
            }

            if (var7 != null) {
               long var10 = (new File(var9)).length();
               var7.reactivateFromDisk(var2, var3[var4], var10);
               var2.put(var7.getKey(), var7);
               var2.accessList.addSorted(var7);
               var2.diskUsed += var10;
               if (var5 > var2.fileNameCounter) {
                  var2.fileNameCounter = var5;
               }
            }
         }

         try {
            saveFile(var2, getCacheFilePath(var2.rootPath));
         } catch (Exception var12) {
            var2.notifyListener(new Event.FileAccessErrorForCacheEvent(var2, var0, true));
         }
      } catch (Exception var18) {
         if (var1.cacheListener != null) {
            var1.cacheListener.notify(new Event.CacheFailureEvent(var2, var0, "Exception while opening cache."));
         }

         var2 = null;
      }

      return var2;
   }

   protected void initTransientInfo(String var1, CacheSpec var2) {
      this.entries = new ConcurrentHashMap();
      this.accessList = new AccessList();
      this.rootPath = var1;
      this.memoryUsed = 0L;
      this.diskUsed = 0L;
      this.listener = var2.cacheListener;
   }

   protected static EntityCache create(String var0, CacheSpec var1) throws CX.EntityCacheException {
      EntityCache var2 = null;

      try {
         var2 = new EntityCache(var1.name, var1.memSize, var1.diskSize, var1.sizeBias, var0);
         var2.initTransientInfo(var0, var1);
         if (var1.cacheListener != null) {
            var1.cacheListener.notify(new Event.CacheCreationEvent(var2));
         }
      } catch (Exception var4) {
         if (var1.cacheListener != null) {
            var1.cacheListener.notify(new Event.CacheFailureEvent(var2, var0, "Exception while creating cache."));
         }

         var2 = null;
      }

      return var2;
   }

   String getRootPath() {
      return this.rootPath;
   }

   String getNewFileName() throws CX.EntityCacheException {
      try {
         ++this.fileNameCounter;

         while((new File(getIndexFilePath(this.rootPath, "" + this.fileNameCounter))).exists()) {
            ++this.fileNameCounter;
         }

         try {
            saveFile(this, getCacheFilePath(this.rootPath));
         } catch (Exception var2) {
            this.notifyListener(new Event.FileAccessErrorForCacheEvent(this, getCacheFilePath(this.rootPath), true));
            throw var2;
         }
      } catch (Exception var3) {
         throw new CX.EntityCacheException(var3);
      }

      return "" + this.fileNameCounter;
   }

   protected synchronized void findSpace(CacheEntry var1) throws CX.EntityCacheException {
      if (var1.getSize() > this.maxMemory) {
         if (this.stats != null) {
            this.stats.rejection(var1);
         }

         if (this.sessionStats != null) {
            this.sessionStats.rejection(var1);
         }

         this.notifyListener(new Event.EntryRejectionEvent(this, var1));
         throw new CX.EntryTooLargeMemory(var1, this.maxMemory);
      } else {
         if (this.memoryUsed + var1.getSize() > this.maxMemory) {
            this.purge(var1.getSize() - (this.maxMemory - this.memoryUsed));
         }

      }
   }

   synchronized void purge(long var1) throws CX.EntityCacheException {
      Vector var3 = this.findEntitiesToPurge(var1);
      long var4 = this.memoryUsed;
      Enumeration var6 = var3.elements();

      while(var6.hasMoreElements()) {
         CacheEntry var7 = (CacheEntry)var6.nextElement();
         var7.purge();
      }

      long var10 = var4 - this.memoryUsed;
      long var8 = var10 - var1;
      if (var8 < 0L) {
         var8 = 0L;
      }

      this.memLossTotal += var8;
      if (this.stats != null) {
         this.stats.memoryPurge((long)var3.size(), var10);
      }

      if (this.sessionStats != null) {
         this.sessionStats.memoryPurge((long)var3.size(), var10);
      }

      this.statsCurrentModification = true;
      this.notifyListener(new Event.MemoryPurgeEvent(this, var3));
   }

   protected Vector findEntitiesToPurge(long var1) {
      int var3 = 0;
      Vector var4 = new Vector();

      for(CacheEntry var5 = this.accessList.oldestEntry; var5 != null && (long)var3 < var1; var5 = var5.prevAccess) {
         long var6 = var5.getMemorySize();
         if (var6 != 0L) {
            var4.addElement(var5);
            var3 = (int)((long)var3 + var6);
         }
      }

      return var4;
   }

   protected void purgeDisk(long var1) throws CX.EntityCacheException {
      this.purgeDisk(var1, (CacheEntry)null);
   }

   protected synchronized void purgeDisk(long var1, CacheEntry var3) throws CX.EntityCacheException {
      Vector var4 = this.findDiskEntitiesToPurge(var1, var3);
      long var5 = this.diskUsed;
      Enumeration var7 = var4.elements();

      while(var7.hasMoreElements()) {
         CacheEntry var8 = (CacheEntry)var7.nextElement();
         var8.makeTransient(false);
      }

      long var9 = var5 - this.diskUsed - var1;
      this.diskLossTotal += var5 - this.diskUsed - var1;
      if (this.stats != null) {
         this.stats.diskPurge((long)var4.size(), var5 - this.diskUsed);
      }

      if (this.sessionStats != null) {
         this.sessionStats.diskPurge((long)var4.size(), var5 - this.diskUsed);
      }

      this.notifyListener(new Event.DiskPurgeEvent(this, var4));
      this.statsCurrentModification = true;
   }

   synchronized void reduceDiskUsed(long var1) {
      this.diskUsed -= var1;
   }

   synchronized void reduceMemoryUsed(long var1) {
      this.memoryUsed -= var1;
   }

   synchronized void loadCacheEntry(CacheEntry var1) throws CX.EntityCacheException {
      this.findSpace(var1);
      Serializable var2 = var1.getKey();
      String var4 = getDataFilePath(this.rootPath, var1.getFileName());

      try {
         var1.loadBytesCallback(var4);
         this.memoryUsed += var1.getMemorySize();
      } catch (CX.FileLoadOutOfMemory var6) {
         Tools.log("Unable to load cache entry data record from disk (too big!): " + var2 + ". Deleting entry...");
         this.removeEntry(var2);
         this.notifyListener(new Event.OutOfMemoryLoadingEntryEvent(this, var2, var4));
         throw var6;
      } catch (CX.FileLoad var7) {
         Tools.log("Unable to load cache entry data record from disk (corrupted?): " + var2 + ". Deleting entry...");
         if (canRead(var4)) {
            this.notifyListener(new Event.EntryCorruptionEvent(this, var2, var4));
         } else {
            this.notifyListener(new Event.FileAccessErrorForEntryEvent(this, (CacheEntry)null, var4, false));
         }

         this.removeEntry(var2);
         throw var7;
      }

      this.notifyListener(new Event.EntryLoadEvent(this, var1));
   }

   protected synchronized Vector findDiskEntitiesToPurge(long var1, CacheEntry var3) {
      int var4 = 0;
      Vector var5 = new Vector();

      for(CacheEntry var6 = this.accessList.oldestEntry; var6 != null; var6 = var6.prevAccess) {
         if (var6 != var3 && var6.isPersisted()) {
            if ((long)var4 >= var1) {
               break;
            }

            var5.addElement(var6);
            var4 = (int)((long)var4 + var6.getDiskSize());
         }
      }

      return var5;
   }

   long getBestFitRetryCount() {
      double var1 = this.factorial((long)this.entries.size()) - 1.0;
      long var3 = (long)(var1 * this.sizeBias);
      return var3;
   }

   double factorial(long var1) {
      long var3 = 1L;

      for(long var5 = 2L; var5 < var1 + 1L; ++var5) {
         var3 *= var1;
      }

      return (double)var3;
   }

   static boolean canRead(String var0) {
      try {
         return (new File(var0)).canRead();
      } catch (SecurityException var2) {
         return false;
      }
   }

   static void saveFile(Serializable var0, String var1) throws IOException {
      FileOutputStream var2 = new FileOutputStream(var1);
      ObjectOutputStream var3 = new ObjectOutputStream(var2);
      var3.writeObject(var0);
      var3.flush();
      var3.close();
      var2.close();
   }

   synchronized void deleteEntryData(String var1, boolean var2) {
      String var3 = null;
      File var4 = null;
      var3 = this.getIndexFilePath(var1);
      var4 = new File(var3);
      if (var2) {
         try {
            var4.delete();
         } catch (Throwable var7) {
         }
      } else {
         var4.delete();
      }

      var3 = this.getDataFilePath(var1);
      var4 = new File(var3);
      this.reduceDiskUsed(var4.length());
      if (var2) {
         try {
            var4.delete();
         } catch (Throwable var6) {
         }
      } else {
         var4.delete();
      }

   }

   static final Serializable loadFile(String var0) throws CX.FileLoadOutOfMemory, CX.FileLoad {
      Serializable var1 = null;

      try {
         FileInputStream var2 = new FileInputStream(var0);
         ObjectInputStream var3 = new ObjectInputStream(var2);
         var1 = (Serializable)var3.readObject();
         var3.close();
         var2.close();
         return var1;
      } catch (OutOfMemoryError var4) {
         throw new CX.FileLoadOutOfMemory(var0, var4);
      } catch (Exception var5) {
         Tools.px(var5);
         throw new CX.FileLoad(var0, var5);
      }
   }

   public EntityCacheCumulativeStats registerCumulativeStatsMBean(String var1, Statistics var2) throws ManagementException {
      AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      ServerRuntimeMBean var4 = ManagementService.getRuntimeAccess(var3).getServerRuntime();
      String var5 = var1 + var4.getName();
      EntityCacheCumulativeStats var6 = new EntityCacheCumulativeStats(var5, var4, this, var2);
      return var6;
   }

   public void initializeStatisticsMBeans() {
   }

   class AccessList {
      transient AtomicReference<CacheEntry> mostRecentlyAccessed = new AtomicReference();
      CacheEntry oldestEntry = null;

      boolean isMostRecent(CacheEntry var1) {
         return this.mostRecentlyAccessed.get() == var1;
      }

      synchronized void addMostRecent(CacheEntry var1) {
         CacheEntry var2 = (CacheEntry)this.mostRecentlyAccessed.get();
         this.mostRecentlyAccessed.set(var1);
         var1.nextAccess = var2;
         var1.prevAccess = null;
         if (var2 != null) {
            var2.prevAccess = var1;
         }

         if (this.oldestEntry == null) {
            this.oldestEntry = var1;
         }

      }

      void moveMostRecent(CacheEntry var1) {
         if (this.mostRecentlyAccessed.get() != var1) {
            synchronized(this) {
               if (this.mostRecentlyAccessed.get() != var1) {
                  this.remove(var1);
                  this.addMostRecent(var1);
               }
            }
         }

      }

      synchronized void remove(CacheEntry var1) {
         if (var1.prevAccess != null) {
            var1.prevAccess.nextAccess = var1.nextAccess;
         } else {
            this.mostRecentlyAccessed.set(var1.nextAccess);
         }

         if (var1.nextAccess != null) {
            var1.nextAccess.prevAccess = var1.prevAccess;
         } else {
            this.oldestEntry = var1.prevAccess;
         }

      }

      synchronized void addSorted(CacheEntry var1) {
         for(CacheEntry var2 = (CacheEntry)this.mostRecentlyAccessed.get(); var2 != null; var2 = var2.nextAccess) {
            if (var1.olderThan(var2)) {
               var1.prevAccess = var2.prevAccess;
               var2.prevAccess.nextAccess = var1;
               var2.prevAccess = var1;
               if (var2.nextAccess == null) {
                  this.oldestEntry = var1;
               }

               return;
            }
         }

         if (this.oldestEntry != null) {
            this.oldestEntry.nextAccess = var1;
         }

         var1.prevAccess = this.oldestEntry;
         this.oldestEntry = var1;
      }
   }

   public static class CacheSpec {
      public String name = null;
      public String path = "EntityCache";
      public long memSize = 2000000L;
      public long diskSize = 5000000L;
      int sizeBias = 5;
      public CacheListener cacheListener = null;
      Vector eventsOfInterest = null;
   }
}
