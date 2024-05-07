package weblogic.wsee.server;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.kernel.KernelStatus;
import weblogic.store.ObjectHandler;
import weblogic.store.PersistentMap;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.store.PersistentStoreTransaction;
import weblogic.store.StoreWritePolicy;
import weblogic.store.internal.PersistentStoreImpl;
import weblogic.store.io.file.FileStoreIO;
import weblogic.wsee.util.Verbose;

public class WsStorage {
   private static final boolean verbose = Verbose.isVerbose(WsStorage.class);
   private Map inMemoryStore;
   private PersistentStore store;
   private boolean needToCloseStore;
   private PersistentMap storeMap;
   private String name;
   private ObjectHandler handler;
   private StoreCleaner cleaner;
   private boolean inMemoryOnly = false;
   private static final String WEBLOGIC_WSEE_SERVER_STORE = "weblogic.wsee.server.store.";

   WsStorage(String var1, ObjectHandler var2, boolean var3, boolean var4) {
      this.name = var1;
      this.inMemoryStore = Collections.synchronizedMap(new HashMap());
      if (var3) {
         this.inMemoryOnly = true;
      } else {
         this.handler = var2;
         if (!var4) {
            this.initCleaner();
         }
      }

   }

   private void initCleaner() {
      this.cleaner = new StoreCleaner(this);
      this.cleaner.startCleanup();
   }

   public void close() throws PersistentStoreException {
      if (this.needToCloseStore) {
         this.store.close();
      }

   }

   public String getName() {
      return this.name;
   }

   public void setCleanInterval(int var1) {
      if (this.cleaner != null) {
         this.cleaner.setInterval(var1);
      }

   }

   public int getCleanInterval() {
      return this.cleaner != null ? this.cleaner.getInterval() : -1;
   }

   private synchronized void initStore() throws PersistentStoreException {
      if (this.store == null) {
         this.needToCloseStore = false;
         String var1;
         if (KernelStatus.isServer()) {
            var1 = System.getProperty("weblogic.wsee.internal.store");
            if (var1 != null) {
               this.store = PersistentStoreManager.getManager().getStore(var1);
            } else {
               this.store = PersistentStoreManager.getManager().getDefaultStore();
            }
         } else {
            this.store = PersistentStoreManager.getManager().getDefaultStore();
            if (this.store == null) {
               var1 = "WseeFileStore";
               String var2 = System.getProperty("user.home");
               FileStoreIO var3 = new FileStoreIO("weblogic.wsee.persistence." + var1, var2);
               PersistentStoreImpl var4 = new PersistentStoreImpl(this.getClass().getName(), var3);
               this.needToCloseStore = true;
               HashMap var5 = new HashMap();
               var5.put("DaemonThreadInClientJVM", this.needToCloseStore);
               var5.put("SynchronousWritePolicy", StoreWritePolicy.CACHE_FLUSH);
               var4.open(var5);
               PersistentStoreManager.getManager().setDefaultStore(var4);
               this.store = PersistentStoreManager.getManager().getDefaultStore();
            }
         }

         if (this.store == null) {
            throw new PersistentStoreException("Persistent store not found");
         }

         if (this.handler != null) {
            this.storeMap = this.store.createPersistentMap("weblogic.wsee.server.store." + this.name, this.handler);
         } else {
            this.storeMap = this.store.createPersistentMap("weblogic.wsee.server.store." + this.name);
         }
      }

      if (this.storeMap == null) {
         if (this.handler != null) {
            this.storeMap = this.store.createPersistentMap("weblogic.wsee.server.store." + this.name, this.handler);
         } else {
            this.storeMap = this.store.createPersistentMap("weblogic.wsee.server.store." + this.name);
         }
      }

   }

   public synchronized boolean put(Object var1, Object var2) {
      assert var2 != null;

      Object var3 = this.inMemoryStore.put(var1, var2);
      return var3 != null;
   }

   public synchronized boolean persistentPut(Object var1, Object var2) throws PersistentStoreException {
      assert var2 != null;

      if (verbose) {
         Verbose.say("^^^ Putting object in persistent store " + this.name + " with key: " + var1);
      }

      Object var3 = this.inMemoryStore.put(var1, var2);
      if (this.inMemoryOnly) {
         return var3 != null;
      } else {
         this.initStore();
         boolean var4 = this.storeMap.put(var1, var2);
         return var3 != null || var4;
      }
   }

   public synchronized Object get(Object var1) {
      return this.inMemoryStore.get(var1);
   }

   public synchronized void stopCleaner() {
      if (this.cleaner != null) {
         this.cleaner.stopCleanup();
      }

   }

   public synchronized Object persistentGet(Object var1) throws PersistentStoreException {
      Object var2 = this.inMemoryStore.get(var1);
      if (!this.inMemoryOnly && var2 == null) {
         this.initStore();
         var2 = this.storeMap.get(var1);
         if (var2 != null) {
            this.inMemoryStore.put(var1, var2);
         }

         return var2;
      } else {
         return var2;
      }
   }

   public synchronized boolean remove(Object var1) {
      return this.inMemoryStore.remove(var1) != null;
   }

   public synchronized boolean persistentRemove(Object var1) throws PersistentStoreException {
      boolean var2 = this.inMemoryStore.remove(var1) != null;
      if (verbose && var2) {
         Verbose.say("^^^ Removing object in persistent store " + this.name + " with key: " + var1);
      }

      if (this.inMemoryOnly) {
         return var2;
      } else {
         this.initStore();

         boolean var3;
         try {
            var3 = this.storeMap.remove(var1);
         } catch (Exception var5) {
            var3 = false;
         }

         return var3 || var2;
      }
   }

   Set storeKeys() {
      try {
         return this.storeMap != null ? this.storeMap.keySet() : null;
      } catch (PersistentStoreException var2) {
         return null;
      }
   }

   Set memKeys() {
      return this.inMemoryStore.keySet();
   }

   Map memMap() {
      return this.inMemoryStore;
   }

   public PersistentStoreTransaction begin() {
      return this.store.begin();
   }

   public <String, V extends Serializable> TxMap<String, V> getMap() {
      return new MapImpl();
   }

   private class MapImpl<K, V extends Serializable> implements TxMap<K, V> {
      public MapImpl() {
      }

      public PersistentStoreTransaction beginTransaction() {
         return WsStorage.this.begin();
      }

      public int size() {
         try {
            WsStorage.this.initStore();
            return WsStorage.this.inMemoryStore.size();
         } catch (Exception var2) {
            throw new RuntimeException(var2.toString(), var2);
         }
      }

      public boolean isEmpty() {
         try {
            WsStorage.this.initStore();
            return WsStorage.this.inMemoryStore.isEmpty();
         } catch (Exception var2) {
            throw new RuntimeException(var2.toString(), var2);
         }
      }

      public boolean containsKey(Object var1) {
         try {
            WsStorage.this.initStore();
            return WsStorage.this.inMemoryStore.containsKey(var1);
         } catch (Exception var3) {
            throw new RuntimeException(var3.toString(), var3);
         }
      }

      public boolean containsValue(Object var1) {
         try {
            Set var2 = WsStorage.this.inMemoryStore.keySet();
            Iterator var3 = var2.iterator();

            Serializable var5;
            do {
               if (!var3.hasNext()) {
                  return false;
               }

               Object var4 = var3.next();
               var5 = (Serializable)WsStorage.this.inMemoryStore.get(var4);
            } while(var5 == null || !var5.equals(var1));

            return true;
         } catch (Exception var6) {
            throw new RuntimeException(var6.toString(), var6);
         }
      }

      public V get(Object var1) {
         try {
            return (Serializable)WsStorage.this.persistentGet(var1);
         } catch (Exception var3) {
            throw new RuntimeException(var3.toString(), var3);
         }
      }

      public V put(K var1, V var2) {
         Serializable var3 = this.get(var1);

         try {
            return WsStorage.this.persistentPut(var1, var2) ? var3 : null;
         } catch (Exception var5) {
            throw new RuntimeException(var5.toString(), var5);
         }
      }

      public V remove(Object var1) {
         Serializable var2 = this.get(var1);

         try {
            return WsStorage.this.persistentRemove(var1) ? var2 : null;
         } catch (Exception var4) {
            throw new RuntimeException(var4.toString(), var4);
         }
      }

      public void putAll(Map<? extends K, ? extends V> var1) {
         Iterator var2 = var1.keySet().iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            Serializable var4 = (Serializable)var1.get(var3);
            this.put(var3, var4);
         }

      }

      public void clear() {
         try {
            HashSet var1 = new HashSet(WsStorage.this.inMemoryStore.keySet());
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               Object var3 = var2.next();
               WsStorage.this.persistentRemove(var3);
            }

         } catch (Exception var4) {
            throw new RuntimeException(var4.toString(), var4);
         }
      }

      public Set<K> keySet() {
         return WsStorage.this.inMemoryStore.keySet();
      }

      public Collection<V> values() {
         return WsStorage.this.inMemoryStore.values();
      }

      public Set<Map.Entry<K, V>> entrySet() {
         return WsStorage.this.inMemoryStore.entrySet();
      }
   }

   public interface TxMap<K, V extends Serializable> extends Map<K, V> {
      PersistentStoreTransaction beginTransaction();
   }
}
