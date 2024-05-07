package weblogic.wsee.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.common.CompletionRequest;
import weblogic.store.PersistentHandle;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreRecord;
import weblogic.store.PersistentStoreTransaction;
import weblogic.wsee.WseePersistLogger;

public class LocalWLSStoreValuesMap<K extends Serializable, V extends Storable> extends WLSStoreValuesMap<K, V> {
   private static final Logger LOGGER = Logger.getLogger(LocalWLSStoreValuesMap.class.getName());
   private Map<K, PersistentHandle> _storeHandles;
   private Map<K, V> _valuesMap;
   private final ReentrantReadWriteLock _valueLock = new ReentrantReadWriteLock(false);
   private PersistentStoreConnection _storeConnection;
   private boolean _recovered;
   private static final int NO_FLAGS = 0;

   public LocalWLSStoreValuesMap(PersistentStore var1, String var2) throws StoreException {
      super(var1, var2);
      this.open();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(" == Store Created for " + this);
      }

   }

   public String toString() {
      return "storeName = " + this.getStoreName() + " connectionName = " + this._connectionName;
   }

   private void open() throws StoreException {
      this._storeHandles = null;
      this._valuesMap = null;

      try {
         this._storeHandles = new HashMap();
         this._valuesMap = new HashMap();
         this._storeConnection = this._store.createConnection(this._connectionName);
      } catch (PersistentStoreException var10) {
         throw new StoreException(var10.toString(), var10);
      }

      StoreRecord var1 = this.recover();

      try {
         this._valueLock.writeLock().lock();

         StoreRecord var2;
         for(; var1 != null; var1 = var2) {
            var2 = var1.getNext();
            var1.setNext((StoreRecord)null);

            Storable var3;
            try {
               var3 = (Storable)var1.getStoreObject();
            } catch (PersistentStoreException var11) {
               WseePersistLogger.logUnexpectedException(var11.toString(), var11);
               continue;
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("*********** ***** <StoreRecord> state= class=" + var3.getClass().getName() + " obj=" + var3);
            }

            this.recoverValue(var3);
            this._storeHandles.put(var3.getObjectId(), var1.getHandle());
         }
      } finally {
         this._valueLock.writeLock().unlock();
      }

   }

   protected void recoverValue(V var1) {
      this._valuesMap.put(var1.getObjectId(), var1);
   }

   private StoreRecord recover() throws StoreException {
      if (this._recovered) {
         return null;
      } else {
         this._recovered = true;
         StoreRecord var1 = null;
         StoreRecord var2 = null;

         try {
            PersistentStoreConnection.Cursor var3 = this._storeConnection.createCursor(0);

            PersistentStoreRecord var7;
            while((var7 = var3.next()) != null) {
               StoreRecord var5 = new StoreRecord(var7);
               if (var2 == null) {
                  var2 = var5;
                  var1 = var5;
               } else {
                  var2.setNext(var5);
                  var2 = var5;
               }
            }

            return var1;
         } catch (PersistentStoreException var6) {
            StoreException var4 = new StoreException(var6.toString(), var6);
            throw var4;
         }
      }
   }

   public void close() throws StoreException {
      try {
         this._valueLock.writeLock().lock();
         this._valuesMap.clear();
         this._storeHandles.clear();
      } finally {
         this._valueLock.writeLock().unlock();
      }

      if (this._storeConnection != null) {
         this._storeConnection.close();
      }

      super.close();
   }

   public void clean() throws StoreException {
      try {
         this._valueLock.writeLock().lock();
         Iterator var1 = this._storeHandles.values().iterator();

         while(var1.hasNext()) {
            PersistentHandle var2 = (PersistentHandle)var1.next();
            this.delete(var2);
         }
      } finally {
         this._valueLock.writeLock().unlock();
      }

   }

   public void runWhileLocked(Runnable var1, boolean var2) {
      try {
         if (var2) {
            this._valueLock.readLock().lock();
         } else {
            this._valueLock.writeLock().lock();
         }

         var1.run();
      } finally {
         if (var2) {
            this._valueLock.readLock().unlock();
         } else {
            this._valueLock.writeLock().unlock();
         }

      }

   }

   public Set<K> keySet() {
      TreeSet var1;
      try {
         this._valueLock.readLock().lock();
         var1 = new TreeSet(this._valuesMap.keySet());
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var1;
   }

   public Collection<V> values() {
      try {
         this._valueLock.readLock().lock();
         LinkedList var1 = new LinkedList();
         Iterator var2 = this._valuesMap.keySet().iterator();

         while(var2.hasNext()) {
            Serializable var3 = (Serializable)var2.next();
            var1.add(this._valuesMap.get(var3));
         }

         LinkedList var8 = var1;
         return var8;
      } finally {
         this._valueLock.readLock().unlock();
      }
   }

   public Set<Map.Entry<K, V>> entrySet() {
      HashSet var1;
      try {
         this._valueLock.readLock().lock();
         var1 = new HashSet(this._valuesMap.entrySet());
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var1;
   }

   public boolean containsKey(Object var1) {
      boolean var2;
      try {
         this._valueLock.readLock().lock();
         var2 = this._valuesMap.containsKey(var1);
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var2;
   }

   public boolean containsValue(Object var1) {
      boolean var2;
      try {
         this._valueLock.readLock().lock();
         var2 = this._valuesMap.containsValue(var1);
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var2;
   }

   public boolean isEmpty() {
      boolean var1;
      try {
         this._valueLock.readLock().lock();
         var1 = this._valuesMap.isEmpty();
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var1;
   }

   public int size() {
      int var1;
      try {
         this._valueLock.readLock().lock();
         var1 = this._valuesMap.size();
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var1;
   }

   public V put(K var1, V var2) throws StoreRuntimeException {
      Storable var5;
      try {
         this._valueLock.writeLock().lock();
         Storable var4 = null;
         PersistentHandle var3;
         if (!this._valuesMap.containsKey(var1)) {
            var3 = this.storeSync(var2);
            this._valuesMap.put(var1, var2);
            this._storeHandles.put(var1, var3);
         } else {
            var4 = this.get(var1);
            var3 = (PersistentHandle)this._storeHandles.get(var1);
            this.updateSync(var3, var2);
         }

         var5 = var4;
      } catch (StoreRuntimeException var10) {
         WseePersistLogger.logUnexpectedException(var10.toString(), var10);
         throw var10;
      } finally {
         this._valueLock.writeLock().unlock();
      }

      return var5;
   }

   public void putAll(Map<? extends K, ? extends V> var1) throws StoreRuntimeException {
      try {
         Iterator var2 = var1.keySet().iterator();

         while(var2.hasNext()) {
            Serializable var3 = (Serializable)var2.next();
            Storable var4 = (Storable)var1.get(var3);
            this.put(var3, var4);
         }

      } catch (StoreRuntimeException var5) {
         WseePersistLogger.logUnexpectedException(var5.toString(), var5);
         throw var5;
      }
   }

   public void clear() throws StoreRuntimeException {
      try {
         HashSet var1 = new HashSet(this.keySet());
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Serializable var3 = (Serializable)var2.next();
            this.remove(var3);
         }

      } catch (StoreRuntimeException var4) {
         WseePersistLogger.logUnexpectedException(var4.toString(), var4);
         throw var4;
      }
   }

   public V get(Object var1) throws StoreRuntimeException {
      Storable var2;
      try {
         this._valueLock.readLock().lock();
         var2 = (Storable)this._valuesMap.get(var1);
      } catch (StoreRuntimeException var7) {
         WseePersistLogger.logUnexpectedException(var7.toString(), var7);
         throw var7;
      } finally {
         this._valueLock.readLock().unlock();
      }

      return var2;
   }

   public V remove(Object var1) throws StoreRuntimeException {
      Storable var2;
      try {
         this._valueLock.writeLock().lock();
         if (this._valuesMap.containsKey(var1)) {
            var2 = (Storable)this._valuesMap.remove(var1);
            this.delete((PersistentHandle)this._storeHandles.remove(var1));
            Storable var3 = var2;
            return var3;
         }

         var2 = null;
      } catch (StoreRuntimeException var8) {
         WseePersistLogger.logUnexpectedException(var8.toString(), var8);
         throw var8;
      } finally {
         this._valueLock.writeLock().unlock();
      }

      return var2;
   }

   private PersistentHandle storeInternal(Object var1, CompletionRequest var2) {
      PersistentStoreTransaction var3 = this._store.begin();
      PersistentHandle var4 = this._storeConnection.create(var3, var1, 0);
      var3.commit(var2);
      return var4;
   }

   private void updateInternal(PersistentHandle var1, Object var2, CompletionRequest var3) {
      PersistentStoreTransaction var4 = this._store.begin();
      this._storeConnection.update(var4, var1, var2, 0);
      var4.commit(var3);
   }

   private PersistentHandle storeSync(Object var1) throws StoreRuntimeException {
      CompletionRequest var2 = new CompletionRequest();
      PersistentHandle var3 = this.storeInternal(var1, var2);

      try {
         var2.getResult();
         return var3;
      } catch (Throwable var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof Error) {
            throw (Error)var5;
         } else {
            throw new StoreRuntimeException(var5.toString(), var5);
         }
      }
   }

   private void updateSync(PersistentHandle var1, Object var2) throws StoreRuntimeException {
      CompletionRequest var3 = new CompletionRequest();
      this.updateInternal(var1, var2, var3);

      try {
         var3.getResult();
      } catch (Throwable var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof Error) {
            throw (Error)var5;
         } else {
            throw new StoreRuntimeException(var5.toString(), var5);
         }
      }
   }

   private void delete(PersistentHandle var1) throws StoreRuntimeException {
      PersistentStoreTransaction var2 = this._store.begin();
      this._storeConnection.delete(var2, var1, 0);

      try {
         var2.commit();
      } catch (PersistentStoreException var4) {
         throw new StoreRuntimeException(var4.toString(), var4);
      }
   }
}
