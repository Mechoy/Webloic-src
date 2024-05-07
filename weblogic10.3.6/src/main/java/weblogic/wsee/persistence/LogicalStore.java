package weblogic.wsee.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreManager;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.wsee.WseePersistLogger;
import weblogic.wsee.jaxws.framework.ConfigUtil;
import weblogic.wsee.runtime.WebServicesRuntime;

public class LogicalStore<K extends Serializable, V extends Storable> implements Map<K, V> {
   private static boolean _closeAllCalledExplicitly = false;
   private static final Logger LOGGER;
   private static final Map<String, LogicalStore> _storeMap;
   private static final Map<String, List<LogicalStore>> _storeNameToStoreMap;
   private static final ReentrantReadWriteLock _storeMapLock;
   private static final List<LogicalStoreListChangeListener> _listeners;
   public static final String LOGICAL_STORE_LIST = "LogicalStoreList";
   private String _name;
   private String _connectionName;
   private PersistenceStrategy _persistStrategy;
   private long _maxObjectLifetimeMillis;
   private long _cleanerIntervalMillis;
   private ReentrantReadWriteLock _storeConnListLock;
   private List<StoreConnection<K, V>> _storeConnList;
   private int _lastUsedStoreConnIndex;
   private final String _lastUsedStoreConnIndexMonitor;

   public static boolean storeExists(String var0, String var1) {
      boolean var2;
      try {
         _storeMapLock.readLock().lock();
         var2 = _storeMap.containsKey(var0 + var1);
      } finally {
         _storeMapLock.readLock().unlock();
      }

      return var2;
   }

   public static boolean addStore(String var0) {
      try {
         _storeMapLock.writeLock().lock();
         List var1 = (List)_storeNameToStoreMap.get(var0);
         if (var1 != null) {
            boolean var2 = false;
            return var2;
         }

         ArrayList var11 = new ArrayList();
         _storeNameToStoreMap.put(var0, var11);
      } finally {
         _storeMapLock.writeLock().unlock();
      }

      LogicalStoreListChangeListener[] var12 = (LogicalStoreListChangeListener[])_listeners.toArray(new LogicalStoreListChangeListener[_listeners.size()]);
      LogicalStoreListChangeListener[] var13 = var12;
      int var3 = var12.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         LogicalStoreListChangeListener var5 = var13[var4];

         try {
            var5.logicalStoreAdded(var0);
         } catch (Exception var9) {
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.log(Level.WARNING, var9.toString(), var9);
            }

            WseePersistLogger.logUnexpectedException(var9.toString(), var9);
         }
      }

      return true;
   }

   protected static boolean addStore(String var0, String var1, LogicalStore var2) {
      boolean var4;
      try {
         _storeMapLock.writeLock().lock();
         String var3 = var0 + var1;
         if (!_storeMap.containsKey(var3)) {
            _storeMap.put(var3, var2);
            Object var8 = (List)_storeNameToStoreMap.get(var0);
            if (var8 == null) {
               var8 = new ArrayList();
               _storeNameToStoreMap.put(var0, var8);
            }

            ((List)var8).add(var2);
            return true;
         }

         var4 = false;
      } finally {
         _storeMapLock.writeLock().unlock();
      }

      return var4;
   }

   public static LogicalStore<? extends Serializable, ? extends Storable> getStore(String var0, String var1) throws StoreException {
      LogicalStore var3;
      try {
         _storeMapLock.readLock().lock();
         String var2 = var0 + var1;
         var3 = (LogicalStore)_storeMap.get(var2);
      } finally {
         _storeMapLock.readLock().unlock();
      }

      return var3;
   }

   public static boolean removeStore(String var0) {
      LogicalStoreListChangeListener[] var1 = (LogicalStoreListChangeListener[])_listeners.toArray(new LogicalStoreListChangeListener[_listeners.size()]);
      LogicalStoreListChangeListener[] var2 = var1;
      int var3 = var1.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         LogicalStoreListChangeListener var5 = var2[var4];

         try {
            var5.logicalStorePreRemoval(var0);
         } catch (Exception var13) {
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.log(Level.WARNING, var13.toString(), var13);
            }

            WseePersistLogger.logUnexpectedException(var13.toString(), var13);
         }
      }

      boolean var14;
      try {
         _storeMapLock.writeLock().lock();
         List var15 = (List)_storeNameToStoreMap.remove(var0);
         var14 = var15 != null;
         if (var15 != null) {
            closeLogicalStores(var15, var0);
         }
      } finally {
         _storeMapLock.writeLock().unlock();
      }

      var1 = (LogicalStoreListChangeListener[])_listeners.toArray(new LogicalStoreListChangeListener[_listeners.size()]);
      LogicalStoreListChangeListener[] var16 = var1;
      var4 = var1.length;

      for(int var17 = 0; var17 < var4; ++var17) {
         LogicalStoreListChangeListener var6 = var16[var17];

         try {
            var6.logicalStoreRemoved(var0);
         } catch (Exception var11) {
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.log(Level.WARNING, var11.toString(), var11);
            }

            WseePersistLogger.logUnexpectedException(var11.toString(), var11);
         }
      }

      return var14;
   }

   public static void closeAllStores() {
      try {
         _closeAllCalledExplicitly = true;
         _storeMapLock.writeLock().lock();
         Set var0 = _storeNameToStoreMap.keySet();
         Iterator var1 = var0.iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            closeStore(var2);
         }
      } finally {
         _storeMapLock.writeLock().unlock();
      }

   }

   public static boolean closeStore(String var0) {
      boolean var2;
      try {
         _storeMapLock.writeLock().lock();
         List var1 = (List)_storeNameToStoreMap.get(var0);
         if (var1 != null) {
            closeLogicalStores(var1, var0);
            return true;
         }

         var2 = false;
      } finally {
         _storeMapLock.writeLock().unlock();
      }

      return var2;
   }

   private static void closeLogicalStores(List<LogicalStore> var0, String var1) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         try {
            LogicalStore var3 = (LogicalStore)var2.next();
            _storeMap.remove(var1 + var3.getConnectionName());
            var3.close();
            var2.remove();
         } catch (Exception var4) {
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.log(Level.WARNING, var4.toString(), var4);
            }

            WseePersistLogger.logUnexpectedException(var4.toString(), var4);
         }
      }

   }

   public static boolean closeStore(String var0, String var1) throws StoreException {
      boolean var4;
      try {
         _storeMapLock.writeLock().lock();
         LogicalStore var2 = (LogicalStore)_storeMap.remove(var0 + var1);
         List var3 = (List)_storeNameToStoreMap.get(var0);
         if (var3 != null) {
            var3.remove(var2);
         }

         if (var2 != null) {
            var2.close();
         }

         var4 = var2 != null;
      } finally {
         _storeMapLock.writeLock().unlock();
      }

      return var4;
   }

   public static List<LogicalStore> getLogicalStores(String var0) {
      ArrayList var2;
      try {
         _storeMapLock.readLock().lock();
         List var1 = (List)_storeNameToStoreMap.get(var0);
         ArrayList var6;
         if (var1 == null) {
            var6 = new ArrayList();
         } else {
            var6 = new ArrayList(var1);
         }

         var2 = var6;
      } finally {
         _storeMapLock.readLock().unlock();
      }

      return var2;
   }

   public static void addLogicalStoreListChangeListener(LogicalStoreListChangeListener var0) {
      if (!_listeners.contains(var0)) {
         _listeners.add(var0);
      }

   }

   public static void removeLogicalStoreListChangeListener(LogicalStoreListChangeListener var0) {
      if (_listeners.contains(var0)) {
         _listeners.remove(var0);
      }

   }

   protected LogicalStore() {
      this._storeConnListLock = new ReentrantReadWriteLock(false);
      this._lastUsedStoreConnIndexMonitor = "_lastUsedStoreConnIndexMonitor";
      this._storeConnList = new ArrayList();
   }

   public LogicalStore(String var1, String var2) throws StoreException {
      this(var1, var2, true);
   }

   public LogicalStore(String var1, String var2, boolean var3) throws StoreException {
      this._storeConnListLock = new ReentrantReadWriteLock(false);
      this._lastUsedStoreConnIndexMonitor = "_lastUsedStoreConnIndexMonitor";
      this._name = var1;
      this._connectionName = var2;
      if (var3) {
         this.addAvailablePhysicalStores();
      }

   }

   protected void addAvailablePhysicalStores() throws StoreException {
      StoreInfo var1 = getStoreInfo(this._name);
      this._persistStrategy = var1.persistStrategy;
      this._maxObjectLifetimeMillis = var1.maxObjectLifetimeMillis;
      this._cleanerIntervalMillis = var1.cleanerIntervalMillis;

      try {
         this._storeConnListLock.writeLock().lock();
         this._storeConnList = new ArrayList();
         this._lastUsedStoreConnIndex = -1;
         Iterator var2 = var1.physicalStoreNames.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (LogicalStore.PersistenceStrategy.IN_MEMORY != var1.persistStrategy) {
               String var4 = this.preConnectStoreCheck(var3);
               if (!var4.equals(var3)) {
                  var3 = var4;
               }
            }

            this.addPhysicalStore(var3);
            this.postConnectStoreCheck();
         }
      } finally {
         this._storeConnListLock.writeLock().unlock();
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(" == LogicalStore Created for " + this._name);
      }

   }

   protected LogicalStore<K, V> getOrCreateLogicalStore(String var1, String var2) throws StoreException {
      LogicalStore var3;
      try {
         _storeMapLock.writeLock().lock();
         if (!storeExists(var1, var2)) {
            var3 = this.createLogicalStore(var1, var2);
            addStore(var1, var2, var3);
            LogicalStore var4 = var3;
            return var4;
         }

         var3 = getStore(var1, var2);
      } finally {
         _storeMapLock.writeLock().unlock();
      }

      return var3;
   }

   private static StoreInfo getStoreInfo(String var0) {
      StoreInfo var1 = new StoreInfo();
      WebServiceLogicalStoreMBean var2 = ConfigUtil.getLogicalStoreMBean(var0);
      String var3 = var2.getPersistenceStrategy();
      if (var3 == null) {
         var3 = "LOCAL_ACCESS_ONLY";
      }

      String var4 = var2.getDefaultMaximumObjectLifetime();
      if (var4 == null) {
         var4 = "P1D";
      }

      Duration var5;
      try {
         var5 = DatatypeFactory.newInstance().newDuration(var4);
      } catch (Exception var10) {
         throw new RuntimeException(var10.toString(), var10);
      }

      var1.maxObjectLifetimeMillis = var5.getTimeInMillis(new Date());
      String var6 = var2.getCleanerInterval();
      if (var6 == null) {
         var6 = "PT10M";
      }

      Duration var7;
      try {
         var7 = DatatypeFactory.newInstance().newDuration(var6);
      } catch (Exception var9) {
         throw new RuntimeException(var9.toString(), var9);
      }

      var1.cleanerIntervalMillis = var7.getTimeInMillis(new Date());
      if (var1.cleanerIntervalMillis > var1.maxObjectLifetimeMillis / 2L) {
         var1.cleanerIntervalMillis = var1.maxObjectLifetimeMillis / 2L;
      }

      var1.physicalStoreNames = new ArrayList();
      if (var3.equals("IN_MEMORY")) {
         var1.physicalStoreNames.add(InMemoryValuesMap.PHYSICAL_STORE_NAME);
      } else if (var2.getRequestBufferingQueueJndiName() != null) {
         var1.physicalStoreNames = WebServicesRuntime.getInstance().getLocalPhysicalStoresForLogicalStore(var0);
      } else {
         String var8 = var2.getPhysicalStoreName();
         if (var8 == null) {
            var8 = "";
         }

         var1.physicalStoreNames.add(var8);
      }

      var1.persistStrategy = LogicalStore.PersistenceStrategy.valueOf(var3);
      return var1;
   }

   protected LogicalStore<K, V> createLogicalStore(String var1, String var2) throws StoreException {
      return new LogicalStore(var1, var2);
   }

   protected StoreConnection<K, V> createStoreConnection(String var1, String var2) throws StoreException {
      return new StoreConnection(var1, var2);
   }

   public boolean addPhysicalStore(String var1) throws StoreException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      boolean var3 = false;

      StoreConnection var4;
      try {
         ClassLoader var5 = this.getClass().getClassLoader();
         if (var5 != var2) {
            Thread.currentThread().setContextClassLoader(var5);
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("LogicalStore " + this + " is being asked to add a new member physical store: " + var1);
         }

         try {
            StoreConnection.connectionMapWriteLock();
            if (StoreConnection.storeConnectionExists(var1, this._connectionName)) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("LogicalStore " + this + " is reusing an existing StoreConnection to physical store: " + var1);
               }

               var4 = StoreConnection.getStoreConnection(var1, this._connectionName);
            } else {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("LogicalStore " + this + " is adding a new StoreConnection to physical store: " + var1);
               }

               var4 = this.createStoreConnection(var1, this._connectionName);
               var4.setPhysicalStoreMustExist(KernelStatus.isServer() || this._persistStrategy != LogicalStore.PersistenceStrategy.LOCAL_ACCESS_ONLY);
               var4.setCleanerIntervalMillis(this._cleanerIntervalMillis);
               var4.setMaxObjectLifetimeMillis(this._maxObjectLifetimeMillis);
               var4.open();
               StoreConnection.addStoreConnection(var1, this._connectionName, var4);
            }
         } finally {
            StoreConnection.connectionMapWriteUnlock();
         }
      } finally {
         Thread.currentThread().setContextClassLoader(var2);
      }

      try {
         this._storeConnListLock.writeLock().lock();
         if (!this._storeConnList.contains(var4)) {
            this._storeConnList.add(var4);
            var3 = true;
         }

         this.dumpStoreConnections();
      } finally {
         this._storeConnListLock.writeLock().unlock();
      }

      return var3;
   }

   public StoreConnection<K, V> removePhysicalStore(String var1) throws StoreException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("LogicalStore " + this + " is removing the StoreConnection for physical store: " + var1);
      }

      try {
         StoreConnection.connectionMapWriteLock();
         if (StoreConnection.storeConnectionExists(var1, this._connectionName)) {
            StoreConnection.closeStoreConnection(var1, this._connectionName);
         }
      } finally {
         StoreConnection.connectionMapWriteUnlock();
      }

      StoreConnection var2;
      try {
         this._storeConnListLock.writeLock().lock();
         if (!this._storeConnList.isEmpty()) {
            var2 = null;

            for(int var3 = 0; var3 < this._storeConnList.size(); ++var3) {
               StoreConnection var4 = (StoreConnection)this._storeConnList.get(var3);
               if (var4.getPhysicalStoreName().equals(var1)) {
                  this._storeConnList.remove(var4);
                  var2 = var4;
                  String var5 = "_lastUsedStoreConnIndexMonitor";
                  synchronized("_lastUsedStoreConnIndexMonitor") {
                     if (this._lastUsedStoreConnIndex >= var3) {
                        --this._lastUsedStoreConnIndex;
                     }
                     break;
                  }
               }
            }

            this.dumpStoreConnections();
            StoreConnection var17 = var2;
            return var17;
         }

         var2 = null;
      } finally {
         this._storeConnListLock.writeLock().unlock();
      }

      return var2;
   }

   private void dumpStoreConnections() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Store connections for logical store: ").append(this).append("\n");
      Iterator var2 = this._storeConnList.iterator();

      while(var2.hasNext()) {
         StoreConnection var3 = (StoreConnection)var2.next();
         var1.append("   ");
         var1.append(var3.getPhysicalStoreName());
         var1.append("/");
         var1.append(var3.getConnectionName());
         var1.append("\n");
      }

      LOGGER.fine(var1.toString());
   }

   protected List<StoreConnection<K, V>> getStoreConnections() {
      ArrayList var1;
      try {
         WebServicesRuntime.getInstance().ensureReady();
         this._storeConnListLock.readLock().lock();
         var1 = new ArrayList(this._storeConnList);
      } finally {
         this._storeConnListLock.readLock().unlock();
      }

      return var1;
   }

   private String preConnectStoreCheck(String var1) throws StoreException {
      PersistentStore var2;
      if (var1 != null && !var1.isEmpty()) {
         var2 = PersistentStoreManager.getManager().getStore(var1);
         if (var2 == null) {
            PersistentStore var3 = PersistentStoreManager.getManager().getDefaultStore();
            if (var3 != null && var3.getName().equals(var1)) {
               var2 = var3;
            }
         }
      } else {
         var2 = PersistentStoreManager.getManager().getDefaultStore();
      }

      if (var2 != null || !KernelStatus.isServer() && this._persistStrategy == LogicalStore.PersistenceStrategy.LOCAL_ACCESS_ONLY) {
         return var1;
      } else {
         throw new StoreException(WseePersistLogger.logStoreNameNotSetLoggable().getMessage());
      }
   }

   private void postConnectStoreCheck() throws StoreException {
      if (this._persistStrategy == LogicalStore.PersistenceStrategy.NETWORK_ACCESSIBLE && !(this._storeConnList instanceof PersistentStoreXA)) {
         throw new StoreException(WseePersistLogger.logNetworkStoreNotXALoggable(this._name).getMessage());
      }
   }

   public String getName() {
      return this._name;
   }

   public String getConnectionName() {
      return this._connectionName;
   }

   public void close() throws StoreException {
      ArrayList var1;
      try {
         this._storeConnListLock.readLock().lock();
         var1 = new ArrayList();
         Iterator var2 = this._storeConnList.iterator();

         while(var2.hasNext()) {
            StoreConnection var3 = (StoreConnection)var2.next();
            var1.add(var3.getPhysicalStoreName());
         }
      } finally {
         this._storeConnListLock.readLock().unlock();
      }

      StoreException var13 = null;
      Iterator var14 = var1.iterator();

      while(var14.hasNext()) {
         String var4 = (String)var14.next();

         try {
            StoreConnection var5 = this.removePhysicalStore(var4);
            if (var5 != null) {
               try {
                  var5.close();
               } catch (StoreException var10) {
                  if (LOGGER.isLoggable(Level.WARNING)) {
                     LOGGER.log(Level.WARNING, "Error closing physical store '" + var4 + "': " + var10.toString(), var10);
                  }

                  throw var10;
               }
            }
         } catch (StoreException var11) {
            var13 = var11;
            WseePersistLogger.logUnexpectedException(var11.toString(), var11);
         }
      }

      if (var13 != null) {
         throw var13;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString());
      var1.append(" (").append(this.getName()).append(")");
      return var1.toString();
   }

   public int size() {
      this.ensureLogicalStoreReady();

      try {
         this._storeConnListLock.readLock().lock();
         int var1 = 0;

         StoreConnection var3;
         for(Iterator var2 = this._storeConnList.iterator(); var2.hasNext(); var1 += var3.size()) {
            var3 = (StoreConnection)var2.next();
         }

         int var7 = var1;
         return var7;
      } finally {
         this._storeConnListLock.readLock().unlock();
      }
   }

   public boolean isEmpty() {
      this.ensureLogicalStoreReady();

      boolean var7;
      try {
         this._storeConnListLock.readLock().lock();
         Iterator var1 = this._storeConnList.iterator();

         while(var1.hasNext()) {
            StoreConnection var2 = (StoreConnection)var1.next();
            if (var2.isEmpty()) {
               boolean var3 = true;
               return var3;
            }
         }

         var7 = false;
      } finally {
         this._storeConnListLock.readLock().unlock();
      }

      return var7;
   }

   public boolean containsKey(Object var1) {
      this.ensureLogicalStoreReady();

      boolean var8;
      try {
         this._storeConnListLock.readLock().lock();
         Iterator var2 = this._storeConnList.iterator();

         while(var2.hasNext()) {
            StoreConnection var3 = (StoreConnection)var2.next();
            if (var3.containsKey(var1)) {
               boolean var4 = true;
               return var4;
            }
         }

         var8 = false;
      } finally {
         this._storeConnListLock.readLock().unlock();
      }

      return var8;
   }

   public boolean containsValue(Object var1) {
      this.ensureLogicalStoreReady();

      boolean var8;
      try {
         this._storeConnListLock.readLock().lock();
         Iterator var2 = this._storeConnList.iterator();

         while(var2.hasNext()) {
            StoreConnection var3 = (StoreConnection)var2.next();
            if (var3.containsValue(var1)) {
               boolean var4 = true;
               return var4;
            }
         }

         var8 = false;
      } finally {
         this._storeConnListLock.readLock().unlock();
      }

      return var8;
   }

   public V get(Object var1) {
      this.ensureLogicalStoreReady();

      Storable var5;
      try {
         this._storeConnListLock.readLock().lock();
         Iterator var2 = this._storeConnList.iterator();

         StoreConnection var3;
         Serializable var4;
         do {
            if (!var2.hasNext()) {
               var2 = null;
               return var2;
            }

            var3 = (StoreConnection)var2.next();
            var4 = (Serializable)var1;
         } while(!var3.containsKey(var4));

         var5 = var3.get(var1);
      } finally {
         this._storeConnListLock.readLock().unlock();
      }

      return var5;
   }

   public V put(K var1, V var2) {
      this.ensureLogicalStoreReady();

      Storable var6;
      try {
         this._storeConnListLock.readLock().lock();
         Storable var4 = this.get(var1);
         String var3;
         if (var4 != null) {
            var3 = var4.getPhysicalStoreName();
         } else {
            var3 = var2.getPhysicalStoreName();
         }

         StoreConnection var5;
         if (var3 == null) {
            var5 = this.getNextPhysicalStore();
            var2.setPhysicalStoreName(var5.getPhysicalStoreName());
         } else {
            var5 = this.getStoreConnection(var3);
         }

         var6 = var5.put(var1, var2);
      } finally {
         this._storeConnListLock.readLock().unlock();
      }

      return var6;
   }

   public String getNextPhysicalStoreName() {
      return this.getNextPhysicalStore().getPhysicalStoreName();
   }

   private StoreConnection<K, V> getNextPhysicalStore() {
      String var1 = "_lastUsedStoreConnIndexMonitor";
      synchronized("_lastUsedStoreConnIndexMonitor") {
         this.ensureLogicalStoreReady();

         StoreConnection var2;
         try {
            this._storeConnListLock.readLock().lock();
            this._lastUsedStoreConnIndex = 0;
            var2 = (StoreConnection)this._storeConnList.get(this._lastUsedStoreConnIndex);
         } finally {
            this._storeConnListLock.readLock().unlock();
         }

         return var2;
      }
   }

   private void ensureLogicalStoreReady() {
      if (WebServicesRuntime.getInstance().ensureReady()) {
         boolean var1 = false;

         try {
            this._storeConnListLock.readLock().lock();
            var1 = true;
            if (this._storeConnList.size() < 1) {
               this._storeConnListLock.readLock().unlock();
               var1 = false;
               Exception var2 = null;

               try {
                  this.addAvailablePhysicalStores();
               } catch (Exception var7) {
                  var2 = var7;
               }

               this._storeConnListLock.readLock().lock();
               var1 = true;
               if (this._storeConnList.size() < 1 || var2 != null) {
                  String var3 = "No store connections in LogicalStore " + this + ", cannot put to this logical store.";
                  if (var2 != null) {
                     var3 = var3 + " Tried to add available physical stores to correct this, but this failed with: " + var2;
                  }

                  throw new RuntimeException(var3);
               }
            }
         } finally {
            if (var1) {
               this._storeConnListLock.readLock().unlock();
            }

         }

      }
   }

   protected StoreConnection<K, V> getStoreConnection(String var1) {
      return this.doGetStoreConnection(var1, true);
   }

   protected StoreConnection<K, V> getStoreConnectionInternal(String var1) {
      return this.doGetStoreConnection(var1, false);
   }

   private StoreConnection<K, V> doGetStoreConnection(String var1, boolean var2) {
      try {
         if (var2) {
            this.ensureLogicalStoreReady();
         }

         this._storeConnListLock.readLock().lock();
         Iterator var3 = this._storeConnList.iterator();

         StoreConnection var4;
         do {
            if (!var3.hasNext()) {
               throw new RuntimeException("No physical store with name " + var1 + " is associated with logical store " + this._name);
            }

            var4 = (StoreConnection)var3.next();
         } while(!var4.getPhysicalStoreName().equals(var1));

         StoreConnection var5 = var4;
         return var5;
      } finally {
         this._storeConnListLock.readLock().unlock();
      }
   }

   public V remove(Object var1) {
      this.ensureLogicalStoreReady();

      Storable var5;
      try {
         this._storeConnListLock.readLock().lock();
         Serializable var2 = (Serializable)var1;
         Iterator var3 = this._storeConnList.iterator();

         StoreConnection var4;
         do {
            if (!var3.hasNext()) {
               var3 = null;
               return var3;
            }

            var4 = (StoreConnection)var3.next();
         } while(!var4.containsKey(var2));

         var5 = var4.remove(var2);
      } finally {
         this._storeConnListLock.readLock().unlock();
      }

      return var5;
   }

   public void putAll(Map<? extends K, ? extends V> var1) {
      try {
         this._storeConnListLock.readLock().lock();
         Iterator var2 = var1.keySet().iterator();

         while(var2.hasNext()) {
            Serializable var3 = (Serializable)var2.next();
            Storable var4 = (Storable)var1.get(var3);
            this.put(var3, var4);
         }
      } finally {
         this._storeConnListLock.readLock().unlock();
      }

   }

   public void clear() {
      this.ensureLogicalStoreReady();

      try {
         this._storeConnListLock.readLock().lock();
         Iterator var1 = this._storeConnList.iterator();

         while(var1.hasNext()) {
            StoreConnection var2 = (StoreConnection)var1.next();
            var2.clear();
         }
      } finally {
         this._storeConnListLock.readLock().unlock();
      }

   }

   public Set<K> keySet() {
      this.ensureLogicalStoreReady();

      try {
         this._storeConnListLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._storeConnList.iterator();

         while(var2.hasNext()) {
            StoreConnection var3 = (StoreConnection)var2.next();
            var1.addAll(var3.keySet());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storeConnListLock.readLock().unlock();
      }
   }

   public Collection<V> values() {
      this.ensureLogicalStoreReady();

      try {
         this._storeConnListLock.readLock().lock();
         LinkedList var1 = new LinkedList();
         Iterator var2 = this._storeConnList.iterator();

         while(var2.hasNext()) {
            StoreConnection var3 = (StoreConnection)var2.next();
            var1.addAll(var3.values());
         }

         LinkedList var7 = var1;
         return var7;
      } finally {
         this._storeConnListLock.readLock().unlock();
      }
   }

   public Set<Map.Entry<K, V>> entrySet() {
      this.ensureLogicalStoreReady();

      try {
         this._storeConnListLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._storeConnList.iterator();

         while(var2.hasNext()) {
            StoreConnection var3 = (StoreConnection)var2.next();
            var1.addAll(var3.entrySet());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storeConnListLock.readLock().unlock();
      }
   }

   static {
      Runtime.getRuntime().addShutdownHook(new Thread() {
         public void run() {
            String var1 = LogicalStore._closeAllCalledExplicitly ? "All LogicalStores already closed. Quitting" : "Closing all logical stores";
            if (LogicalStore.LOGGER.isLoggable(Level.FINE)) {
               LogicalStore.LOGGER.fine("SHUTDOWN HOOK FIRED: " + var1);
            }

            LogicalStore.closeAllStores();
         }
      });
      LOGGER = Logger.getLogger(LogicalStore.class.getName());
      _storeMap = new HashMap();
      _storeNameToStoreMap = new HashMap();
      _storeMapLock = new ReentrantReadWriteLock(false);
      _listeners = new ArrayList();
   }

   private static class StoreInfo {
      public List<String> physicalStoreNames;
      public PersistenceStrategy persistStrategy;
      public long cleanerIntervalMillis;
      public long maxObjectLifetimeMillis;

      private StoreInfo() {
      }

      // $FF: synthetic method
      StoreInfo(Object var1) {
         this();
      }
   }

   public static enum PersistenceStrategy {
      LOCAL_ACCESS_ONLY,
      NETWORK_ACCESSIBLE,
      IN_MEMORY;
   }
}
