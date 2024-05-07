package weblogic.wsee.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import weblogic.store.PersistentMap;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.wsee.WseePersistLogger;

public class NetworkWLSStoreValuesMap<K extends Serializable, V extends Storable> extends WLSStoreValuesMap<K, V> {
   private ReentrantReadWriteLock _valuesLock = new ReentrantReadWriteLock(false);
   private PersistentMap _valuesMap;

   public NetworkWLSStoreValuesMap(PersistentStore var1, String var2) throws PersistentStoreException {
      super(var1, var2);
      this._valuesMap = ((PersistentStoreXA)var1).createPersistentMapXA(var2);
   }

   public int size() {
      int var1;
      try {
         this._valuesLock.readLock().lock();
         var1 = this._valuesMap.size();
      } catch (Exception var6) {
         WseePersistLogger.logUnexpectedException(var6.toString(), var6);
         throw new StoreRuntimeException(var6.toString(), var6);
      } finally {
         this._valuesLock.readLock().unlock();
      }

      return var1;
   }

   public boolean isEmpty() {
      boolean var1;
      try {
         this._valuesLock.readLock().lock();
         var1 = this._valuesMap.isEmpty();
      } catch (Exception var6) {
         WseePersistLogger.logUnexpectedException(var6.toString(), var6);
         throw new StoreRuntimeException(var6.toString(), var6);
      } finally {
         this._valuesLock.readLock().unlock();
      }

      return var1;
   }

   public boolean containsKey(Object var1) {
      boolean var2;
      try {
         this._valuesLock.readLock().lock();
         var2 = this._valuesMap.containsKey(var1);
      } catch (Exception var7) {
         WseePersistLogger.logUnexpectedException(var7.toString(), var7);
         throw new StoreRuntimeException(var7.toString(), var7);
      } finally {
         this._valuesLock.readLock().unlock();
      }

      return var2;
   }

   public boolean containsValue(Object var1) {
      boolean var6;
      try {
         this._valuesLock.readLock().lock();
         Set var2 = this._valuesMap.keySet();
         Iterator var3 = var2.iterator();

         Storable var5;
         do {
            if (!var3.hasNext()) {
               boolean var13 = false;
               return var13;
            }

            Serializable var4 = (Serializable)var3.next();
            var5 = (Storable)this._valuesMap.get(var4);
         } while(var5 == null || !var5.equals(var1));

         var6 = true;
      } catch (Exception var11) {
         WseePersistLogger.logUnexpectedException(var11.toString(), var11);
         throw new StoreRuntimeException(var11.toString(), var11);
      } finally {
         this._valuesLock.readLock().unlock();
      }

      return var6;
   }

   public V get(Object var1) {
      Storable var2;
      try {
         this._valuesLock.readLock().lock();
         var2 = (Storable)this._valuesMap.get(var1);
      } catch (Exception var7) {
         WseePersistLogger.logUnexpectedException(var7.toString(), var7);
         throw new StoreRuntimeException(var7.toString(), var7);
      } finally {
         this._valuesLock.readLock().unlock();
      }

      return var2;
   }

   public V put(K var1, V var2) {
      return null;
   }

   public V remove(Object var1) {
      Storable var3;
      try {
         this._valuesLock.writeLock().lock();
         Storable var2 = null;
         if (this._valuesMap.containsKey(var1)) {
            var2 = (Storable)this._valuesMap.get(var1);
         }

         this._valuesMap.remove(var1);
         var3 = var2;
      } catch (Exception var8) {
         WseePersistLogger.logUnexpectedException(var8.toString(), var8);
         throw new StoreRuntimeException(var8.toString(), var8);
      } finally {
         this._valuesLock.writeLock().unlock();
      }

      return var3;
   }

   public void putAll(Map<? extends K, ? extends V> var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         Serializable var3 = (Serializable)var2.next();
         Storable var4 = (Storable)var1.get(var3);
         this.put(var3, var4);
      }

   }

   public void clear() {
      try {
         this._valuesLock.writeLock().lock();
         HashSet var1 = new HashSet(this.keySet());
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Serializable var3 = (Serializable)var2.next();
            this.remove(var3);
         }
      } catch (Exception var8) {
         WseePersistLogger.logUnexpectedException(var8.toString(), var8);
         throw new StoreRuntimeException(var8.toString(), var8);
      } finally {
         this._valuesLock.writeLock().unlock();
      }

   }

   public Set<K> keySet() {
      HashSet var1;
      try {
         this._valuesLock.readLock().lock();
         var1 = new HashSet(this._valuesMap.keySet());
      } catch (Exception var6) {
         WseePersistLogger.logUnexpectedException(var6.toString(), var6);
         throw new StoreRuntimeException(var6.toString(), var6);
      } finally {
         this._valuesLock.readLock().unlock();
      }

      return var1;
   }

   public Collection<V> values() {
      try {
         this._valuesLock.readLock().lock();
         Set var1 = this._valuesMap.keySet();
         LinkedList var2 = new LinkedList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Serializable var4 = (Serializable)var3.next();
            Storable var5 = (Storable)this._valuesMap.get(var4);
            var2.add(var5);
         }

         LinkedList var12 = var2;
         return var12;
      } catch (Exception var10) {
         WseePersistLogger.logUnexpectedException(var10.toString(), var10);
         throw new StoreRuntimeException(var10.toString(), var10);
      } finally {
         this._valuesLock.readLock().unlock();
      }
   }

   public Set<Map.Entry<K, V>> entrySet() {
      throw new UnsupportedOperationException("entrySet()");
   }
}
