package weblogic.wsee.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.management.configuration.WebServicePhysicalStoreMBean;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreManager;
import weblogic.store.StoreWritePolicy;
import weblogic.store.internal.PersistentStoreImpl;
import weblogic.store.io.file.FileStoreIO;
import weblogic.wsee.WseePersistLogger;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.framework.ConfigUtil;

public class StoreConnection<K extends Serializable, V extends Storable> implements Map<K, V> {
   private static final Logger LOGGER = Logger.getLogger(StoreConnection.class.getName());
   private static final Map<String, StoreConnection> _nameConnToStoreMap = new HashMap();
   private static final ReentrantReadWriteLock _nameConnToStoreMapLock = new ReentrantReadWriteLock(false);
   private ValuesMap<K, V> _valuesMap;
   private String _physicalStoreName;
   private boolean _physicalStoreMustExist = KernelStatus.isServer();
   private String _connectionName;
   private long _cleanerIntervalMillis;
   private long _maxObjectLifetimeMillis;
   private long _maxIdleTimeMillis;
   private StoreCleaner _cleaner;

   public static boolean storeConnectionExists(String var0, String var1) {
      boolean var2;
      try {
         _nameConnToStoreMapLock.readLock().lock();
         var2 = _nameConnToStoreMap.containsKey(var0 + var1);
      } finally {
         _nameConnToStoreMapLock.readLock().unlock();
      }

      return var2;
   }

   protected static void connectionMapWriteLock() {
      _nameConnToStoreMapLock.writeLock().lock();
   }

   protected static void connectionMapWriteUnlock() {
      _nameConnToStoreMapLock.writeLock().unlock();
   }

   protected static boolean addStoreConnection(String var0, String var1, StoreConnection var2) {
      boolean var4;
      try {
         _nameConnToStoreMapLock.writeLock().lock();
         String var3 = var0 + var1;
         if (_nameConnToStoreMap.containsKey(var3)) {
            var4 = false;
            return var4;
         }

         _nameConnToStoreMap.put(var3, var2);
         var4 = true;
      } finally {
         _nameConnToStoreMapLock.writeLock().unlock();
      }

      return var4;
   }

   public static StoreConnection getStoreConnection(String var0, String var1) throws StoreException {
      StoreConnection var3;
      try {
         _nameConnToStoreMapLock.readLock().lock();
         String var2 = var0 + var1;
         var3 = (StoreConnection)_nameConnToStoreMap.get(var2);
      } finally {
         _nameConnToStoreMapLock.readLock().unlock();
      }

      return var3;
   }

   public static boolean closeStoreConnection(String var0, String var1) throws StoreException {
      boolean var3;
      try {
         _nameConnToStoreMapLock.writeLock().lock();
         StoreConnection var2 = (StoreConnection)_nameConnToStoreMap.remove(var0 + var1);
         if (var2 != null) {
            var2.close();
            var3 = true;
            return var3;
         }

         var3 = false;
      } finally {
         _nameConnToStoreMapLock.writeLock().unlock();
      }

      return var3;
   }

   public static void closeAllStoreConnections() {
      try {
         _nameConnToStoreMapLock.writeLock().lock();
         Iterator var0 = _nameConnToStoreMap.values().iterator();

         while(var0.hasNext()) {
            StoreConnection var1 = (StoreConnection)var0.next();

            try {
               var1.close();
            } catch (Exception var6) {
               WseePersistLogger.logUnexpectedException(var6.toString(), var6);
            }
         }
      } finally {
         _nameConnToStoreMapLock.writeLock().unlock();
      }

   }

   public StoreConnection(String var1, String var2) throws StoreException {
      if (storeConnectionExists(var1, var2)) {
         throw new StoreException(WseePersistLogger.logStoreExistsLoggable(var1, var2).getMessage());
      } else {
         this._physicalStoreName = var1;
         this._connectionName = var2;
         this._cleanerIntervalMillis = -1L;
         this._maxObjectLifetimeMillis = -1L;
         this._maxIdleTimeMillis = -1L;
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(" == StoreConnection Created for physical store '" + var1 + "' and connection name '" + var2 + "'");
         }

      }
   }

   public String getPhysicalStoreName() {
      return this._physicalStoreName;
   }

   public String getEffectiveStoreName() {
      String var1 = this.getPhysicalStoreName();
      if (var1 == null) {
         var1 = this._valuesMap.getStoreName();
      }

      return var1;
   }

   public long getCleanerIntervalMillis() {
      return this._cleanerIntervalMillis;
   }

   public void setCleanerIntervalMillis(long var1) {
      this._cleanerIntervalMillis = var1;
   }

   public long getMaxObjectLifetimeMillis() {
      return this._maxObjectLifetimeMillis;
   }

   public void setMaxObjectLifetimeMillis(long var1) {
      this._maxObjectLifetimeMillis = var1;
   }

   public long getMaxIdleTimeMillis() {
      return this._maxIdleTimeMillis;
   }

   public void setMaxIdleTimeMillis(long var1) {
      this._maxIdleTimeMillis = var1;
   }

   public boolean isPhysicalStoreMustExist() {
      return this._physicalStoreMustExist;
   }

   public void setPhysicalStoreMustExist(boolean var1) {
      this._physicalStoreMustExist = var1;
   }

   public String getConnectionName() {
      return this._connectionName;
   }

   public String toString() {
      return "<StoreConnection> : storeName = " + this._physicalStoreName + " connectionName = " + this._connectionName;
   }

   ValuesMap createValuesMap() throws StoreException {
      return (ValuesMap)(InMemoryValuesMap.PHYSICAL_STORE_NAME.equals(this._physicalStoreName) ? new InMemoryValuesMap(this._connectionName) : this.createWLSStoreValuesMap());
   }

   private WLSStoreValuesMap createWLSStoreValuesMap() throws StoreException {
      InitInfo var1 = this.initPersistentStore();
      boolean var2 = var1.store instanceof PersistentStoreImpl;
      return var2 ? this.createLocalMap(var1) : this.createNetworkMap(var1);
   }

   private StoreConnection<K, V>.InitInfo initPersistentStore() throws StoreException {
      InitInfo var1 = new InitInfo();
      if (this.getPhysicalStoreName() != null && !this.getPhysicalStoreName().isEmpty()) {
         var1.store = PersistentStoreManager.getManager().getStore(this.getPhysicalStoreName());
         if (var1.store == null) {
            PersistentStore var2 = PersistentStoreManager.getManager().getDefaultStore();
            if (var2 != null && var2.getName().equals(this.getPhysicalStoreName())) {
               var1.store = var2;
            }
         }
      } else {
         var1.store = PersistentStoreManager.getManager().getDefaultStore();
      }

      if (var1.store == null) {
         if (this._physicalStoreMustExist) {
            throw new StoreException(WseePersistLogger.logStoreNameNotSetLoggable().getMessage());
         }

         var1.store = this.createLocalFileStore();
      }

      return var1;
   }

   private PersistentStore createLocalFileStore() throws StoreException {
      WebServiceMBean var1 = WebServiceMBeanFactory.getInstance();
      WebServiceLogicalStoreMBean var2 = ConfigUtil.getLogicalStoreMBean(var1.getWebServicePersistence().getDefaultLogicalStoreName());
      String var3 = var2.getPhysicalStoreName();
      WebServicePhysicalStoreMBean var4 = null;
      WebServicePhysicalStoreMBean[] var5 = var1.getWebServicePersistence().getWebServicePhysicalStores();
      WebServicePhysicalStoreMBean[] var6 = var5;
      int var7 = var5.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         WebServicePhysicalStoreMBean var9 = var6[var8];
         if (var9.getName().equals(var3)) {
            var4 = var9;
            break;
         }
      }

      if (var4 == null) {
         throw new RuntimeException("Couldn't find physical store named: " + var2.getPhysicalStoreName());
      } else {
         String var11 = var4.getLocation();

         try {
            FileStoreIO var12 = new FileStoreIO("weblogic.wsee.persistence." + var3, var11);
            PersistentStoreImpl var13 = new PersistentStoreImpl(var3, var12);
            HashMap var14 = new HashMap();
            var14.put("SynchronousWritePolicy", StoreWritePolicy.CACHE_FLUSH);
            var14.put("DaemonThreadInClientJVM", true);
            var13.open(var14);
            PersistentStoreManager.getManager().addStore(var3, var13);
            return var13;
         } catch (Exception var10) {
            throw new StoreException(var10.toString(), var10);
         }
      }
   }

   private WLSStoreValuesMap createNetworkMap(StoreConnection<K, V>.InitInfo var1) throws StoreException {
      try {
         return new NetworkWLSStoreValuesMap(var1.store, this.getConnectionName());
      } catch (Exception var3) {
         throw new StoreException(var3.toString(), var3);
      }
   }

   private WLSStoreValuesMap createLocalMap(StoreConnection<K, V>.InitInfo var1) throws StoreException {
      return new LocalWLSStoreValuesMap(var1.store, this.getConnectionName());
   }

   protected void open() throws StoreException {
      if (this._valuesMap != null) {
         throw new StoreException("Already opened this StoreConnection: " + this._physicalStoreName + "/" + this._connectionName);
      } else {
         try {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine(" == StoreConnection being Opened for physical store '" + this._physicalStoreName + "' and connection name '" + this._connectionName + "'");
            }

            this._valuesMap = this.createValuesMap();
            this.recoveryStarting();
            Set var1 = this._valuesMap.keySet();

            Storable var5;
            for(Iterator var2 = var1.iterator(); var2.hasNext(); this.recoverValue(var5)) {
               Object var3 = var2.next();
               Serializable var4 = (Serializable)var3;
               var5 = (Storable)this._valuesMap.get(var4);
               var5.setPhysicalStoreName(this.getPhysicalStoreName());
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine(this + " recovering: " + var5);
               }
            }

            this.recoveryComplete();
            long var8 = Long.MAX_VALUE;
            if (this._maxObjectLifetimeMillis > 0L && this._maxObjectLifetimeMillis < var8) {
               var8 = this._maxObjectLifetimeMillis;
            }

            if (this._maxIdleTimeMillis > 0L && this._maxIdleTimeMillis < var8) {
               var8 = this._maxIdleTimeMillis;
            }

            if (this._cleanerIntervalMillis > var8 / 2L) {
               this._cleanerIntervalMillis = var8 / 2L;
            }

            this._cleaner = new StoreCleaner(this, this._cleanerIntervalMillis, this._maxObjectLifetimeMillis, this._maxIdleTimeMillis);
            this._cleaner.startCleanup();
         } catch (StoreException var6) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
               LOGGER.log(Level.SEVERE, var6.toString(), var6);
            }

            throw var6;
         } catch (Throwable var7) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
               LOGGER.log(Level.SEVERE, var7.toString(), var7);
            }

            throw new RuntimeException(var7.toString(), var7);
         }
      }
   }

   protected void recoveryStarting() {
   }

   protected void recoverValue(V var1) {
   }

   protected void recoveryComplete() {
   }

   public void close() throws StoreException {
      this._cleaner.stopCleanup();
      this._valuesMap.close();
   }

   public Set<K> keySet() {
      return this._valuesMap.keySet();
   }

   public Collection<V> values() {
      return this._valuesMap.values();
   }

   public Set<Map.Entry<K, V>> entrySet() {
      return this._valuesMap.entrySet();
   }

   public boolean containsKey(Object var1) {
      return this._valuesMap.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      return this._valuesMap.containsValue(var1);
   }

   public boolean isEmpty() {
      return this._valuesMap.isEmpty();
   }

   public int size() {
      return this._valuesMap.size();
   }

   public V put(K var1, V var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Attempt to store null key with value: " + var2);
      } else if (var2 == null) {
         throw new IllegalArgumentException("Attempt to store null value with key: " + var1);
      } else {
         if (var2.getPhysicalStoreName() == null) {
            var2.setPhysicalStoreName(this.getPhysicalStoreName());
         }

         return (Storable)this._valuesMap.put(var1, var2);
      }
   }

   public void putAll(Map<? extends K, ? extends V> var1) {
      this._valuesMap.putAll(var1);
   }

   public void clear() {
      this._valuesMap.clear();
   }

   public V get(Object var1) {
      return (Storable)this._valuesMap.get(var1);
   }

   public V remove(Object var1) {
      return (Storable)this._valuesMap.remove(var1);
   }

   static {
      Runtime.getRuntime().addShutdownHook(new Thread() {
         public void run() {
            StoreConnection.closeAllStoreConnections();
         }
      });
   }

   private class InitInfo {
      public PersistentStore store;

      private InitInfo() {
      }

      // $FF: synthetic method
      InitInfo(Object var2) {
         this();
      }
   }
}
