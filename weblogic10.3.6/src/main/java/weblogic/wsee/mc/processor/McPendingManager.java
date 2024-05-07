package weblogic.wsee.mc.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.management.configuration.WebServicePersistenceMBean;
import weblogic.wsee.WseeMCLogger;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.mc.utils.McProtocolUtils;
import weblogic.wsee.persistence.LogicalStore;
import weblogic.wsee.persistence.LogicalStoreListChangeListener;
import weblogic.wsee.persistence.StoreException;

public class McPendingManager {
   private static final Logger LOGGER = Logger.getLogger(McPendingManager.class.getName());
   private static McPendingManager _instance;
   private final List<PendingStore> _stores = new ArrayList();
   private final ReentrantReadWriteLock _storesLock = new ReentrantReadWriteLock(false);

   public static McPendingManager getInstance() {
      return _instance;
   }

   public McPendingManager() throws StoreException {
      this.recover();
   }

   private void recover() throws StoreException {
      WebServiceMBean var1 = WebServiceMBeanFactory.getInstance();
      WebServicePersistenceMBean var2 = var1.getWebServicePersistence();
      WebServiceLogicalStoreMBean[] var3 = var2.getWebServiceLogicalStores();
      WebServiceLogicalStoreMBean[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         WebServiceLogicalStoreMBean var7 = var4[var6];
         this.handleLogicalStoreAdded(var7.getName());
      }

      LogicalStore.addLogicalStoreListChangeListener(new LogicalStoreListChangeListener() {
         public void logicalStoreAdded(String var1) {
            try {
               McPendingManager.this.handleLogicalStoreAdded(var1);
            } catch (Exception var3) {
               WseeMCLogger.logUnexpectedException(var3.toString(), var3);
            }

         }

         public void logicalStorePreRemoval(String var1) {
            try {
               McPendingManager.this.handleLogicalStorePreRemoval(var1);
            } catch (Exception var3) {
               WseeMCLogger.logUnexpectedException(var3.toString(), var3);
            }

         }

         public void logicalStoreRemoved(String var1) {
            try {
               McPendingManager.this.handleLogicalStoreRemoved(var1);
            } catch (Exception var3) {
               WseeMCLogger.logUnexpectedException(var3.toString(), var3);
            }

         }
      });
   }

   private void handleLogicalStoreAdded(String var1) throws StoreException {
      try {
         this._storesLock.writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Handling newly added logical store: " + var1);
         }

         PendingStore var2 = this.getPendingStore(var1);
         this._stores.add(var2);
         if (LOGGER.isLoggable(Level.FINE)) {
            this.dumpLogicalStoreNames("Added");
            this.dumpPendings();
         }
      } finally {
         this._storesLock.writeLock().unlock();
      }

   }

   protected void handleLogicalStorePreRemoval(String var1) throws StoreException {
   }

   protected void handleLogicalStoreRemoved(String var1) throws StoreException {
      try {
         this._storesLock.writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Handling newly removed logical store: " + var1);
         }

         PendingStore var2 = null;
         Iterator var3 = this._stores.iterator();

         while(var3.hasNext()) {
            PendingStore var4 = (PendingStore)var3.next();
            if (var4.getName().equals(var1)) {
               var2 = var4;
               break;
            }
         }

         if (var2 != null) {
            this._stores.remove(var2);
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            this.dumpLogicalStoreNames("Removed");
            this.dumpPendings();
         }
      } finally {
         this._storesLock.writeLock().unlock();
      }

   }

   private void dumpLogicalStoreNames(String var1) {
      Set var2 = this.getPendingStoreNames();
      StringBuffer var3 = new StringBuffer();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var3.append(var5).append(", ");
      }

      LOGGER.fine("These logical stores exist after one was " + var1 + ": " + var3.toString());
   }

   private void dumpPendings() {
      if (LOGGER.isLoggable(Level.FINER)) {
         Set var1 = this.keySet();
         StringBuffer var2 = new StringBuffer();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            McPending var5 = this.get(var4);
            if (var5 != null) {
               var2.append("   ").append(var5.getClass().getSimpleName()).append(": ").append(McProtocolUtils.decodeId(var4)).append(" - ").append(var5.getLogicalStoreName()).append("\n");
            }
         }

         LOGGER.finer("Current Pending Lists:\n" + var2.toString());
      }

   }

   public void finalize() throws Throwable {
      Exception var1 = null;

      try {
         this._storesLock.writeLock().lock();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PendingStore var3 = (PendingStore)var2.next();

            try {
               var3.close();
            } catch (Exception var8) {
               var1 = var8;
            }
         }
      } finally {
         this._storesLock.writeLock().unlock();
      }

      super.finalize();
      if (var1 != null) {
         throw var1;
      }
   }

   private PendingStore getPendingStore(String var1) throws StoreException {
      return PendingStore.getStore(var1);
   }

   public Set<String> getPendingStoreNames() {
      try {
         this._storesLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PendingStore var3 = (PendingStore)var2.next();
            var1.add(var3.getName());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   public PendingStore getPendingStoreForPending(McPending var1) {
      String var2 = var1.getLogicalStoreName();
      if (var2 == null) {
         throw new IllegalStateException("Pending List " + var1.getId() + " doesn't have a logical store name assigned to it!");
      } else {
         try {
            this._storesLock.readLock().lock();
            Iterator var3 = this._stores.iterator();

            PendingStore var4;
            do {
               if (!var3.hasNext()) {
                  throw new IllegalStateException("Pending list " + var1.getId() + " refers to a logical store name for which there is no associated physical store: " + var2);
               }

               var4 = (PendingStore)var3.next();
            } while(!var4.getName().equals(var2));

            PendingStore var5 = var4;
            return var5;
         } finally {
            this._storesLock.readLock().unlock();
         }
      }
   }

   public void addPending(McPending var1) {
      String var2 = var1.getId();
      if (!this.containsKey(var2)) {
         this.put(var2, var1);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Added management of pending list " + McProtocolUtils.decodeId(var2) + ". Current count of lists: " + this.size());
         }
      }

   }

   public void updatePending(McPending var1) {
      this.put(var1.getId(), var1);
   }

   public McPending getPending(String var1) {
      return this.get(var1);
   }

   public Collection<McPending> values() {
      try {
         this._storesLock.readLock().lock();
         LinkedList var1 = new LinkedList();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PendingStore var3 = (PendingStore)var2.next();
            var1.addAll(var3.values());
         }

         LinkedList var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   public int size() {
      int var1 = 0;

      try {
         this._storesLock.readLock().lock();

         PendingStore var3;
         for(Iterator var2 = this._stores.iterator(); var2.hasNext(); var1 += var3.size()) {
            var3 = (PendingStore)var2.next();
         }

         int var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   public boolean isEmpty() {
      boolean var7;
      try {
         this._storesLock.readLock().lock();
         Iterator var1 = this._stores.iterator();

         while(var1.hasNext()) {
            PendingStore var2 = (PendingStore)var1.next();
            if (var2.isEmpty()) {
               boolean var3 = true;
               return var3;
            }
         }

         var7 = false;
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var7;
   }

   public boolean containsKey(Object var1) {
      boolean var8;
      try {
         this._storesLock.readLock().lock();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PendingStore var3 = (PendingStore)var2.next();
            if (var3.containsKey(var1)) {
               boolean var4 = true;
               return var4;
            }
         }

         var8 = false;
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var8;
   }

   public boolean containsValue(Object var1) {
      boolean var4;
      try {
         this._storesLock.readLock().lock();
         Iterator var2 = this._stores.iterator();

         PendingStore var3;
         do {
            if (!var2.hasNext()) {
               boolean var8 = false;
               return var8;
            }

            var3 = (PendingStore)var2.next();
         } while(!var3.containsValue(var1));

         var4 = true;
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var4;
   }

   public McPending get(String var1) {
      Iterator var2;
      try {
         this._storesLock.readLock().lock();
         var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PendingStore var3 = (PendingStore)var2.next();
            if (var3.containsKey(var1)) {
               McPending var4 = (McPending)var3.get(var1);
               return var4;
            }
         }

         var2 = null;
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var2;
   }

   public McPending put(String var1, McPending var2) {
      PendingStore var3 = this.getPendingStoreForPending(var2);
      return (McPending)var3.put(var1, var2);
   }

   public McPending remove(String var1) {
      McPending var4;
      try {
         this._storesLock.writeLock().lock();
         Iterator var2 = this._stores.iterator();

         PendingStore var3;
         do {
            if (!var2.hasNext()) {
               var2 = null;
               return var2;
            }

            var3 = (PendingStore)var2.next();
         } while(!var3.containsKey(var1));

         var4 = (McPending)var3.remove(var1);
      } finally {
         this._storesLock.writeLock().unlock();
      }

      return var4;
   }

   public void putAll(Map<String, McPending> var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         McPending var4 = (McPending)var1.get(var3);
         this.put(var3, var4);
      }

   }

   public void clear() {
      try {
         this._storesLock.writeLock().lock();
         Iterator var1 = this._stores.iterator();

         while(var1.hasNext()) {
            PendingStore var2 = (PendingStore)var1.next();
            var2.clear();
         }
      } finally {
         this._storesLock.writeLock().unlock();
      }

   }

   public Set<String> keySet() {
      try {
         this._storesLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PendingStore var3 = (PendingStore)var2.next();
            var1.addAll(var3.keySet());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   public Set<Map.Entry<String, McPending>> entrySet() {
      try {
         this._storesLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PendingStore var3 = (PendingStore)var2.next();
            var1.addAll(var3.entrySet());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   static {
      try {
         _instance = new McPendingManager();
      } catch (Exception var1) {
         WseeMCLogger.logUnexpectedException(var1.toString(), var1);
         throw new RuntimeException(var1.toString(), var1);
      }
   }
}
