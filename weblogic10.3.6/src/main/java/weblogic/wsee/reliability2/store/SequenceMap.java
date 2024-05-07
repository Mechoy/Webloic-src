package weblogic.wsee.reliability2.store;

import java.beans.PropertyChangeListener;
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
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.persistence.LogicalStore;
import weblogic.wsee.persistence.LogicalStoreListChangeListener;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.reliability2.sequence.Sequence;

public abstract class SequenceMap<S extends Sequence> implements Map<String, S> {
   private static final Logger LOGGER = Logger.getLogger(SequenceMap.class.getName());
   private ReentrantReadWriteLock _storesLock = new ReentrantReadWriteLock(false);
   private List<SequenceStore<S>> _stores = new ArrayList();
   private PropertyChangeListener _sequenceListener;

   public void setSequenceListener(PropertyChangeListener var1) {
      this._sequenceListener = var1;
   }

   public Set<String> getLogicalStoreNames() {
      try {
         this._storesLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            SequenceStore var3 = (SequenceStore)var2.next();
            var1.add(var3.getName());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   protected abstract SequenceStore<S> getOrCreateSequenceStore(String var1) throws StoreException;

   public void recover() throws StoreException {
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
               SequenceMap.this.handleLogicalStoreAdded(var1);
            } catch (Exception var3) {
               WseeRmLogger.logUnexpectedException(var3.toString(), var3);
            }

         }

         public void logicalStorePreRemoval(String var1) {
            try {
               SequenceMap.this.handleLogicalStorePreRemoval(var1);
            } catch (Exception var3) {
               WseeRmLogger.logUnexpectedException(var3.toString(), var3);
            }

         }

         public void logicalStoreRemoved(String var1) {
            try {
               SequenceMap.this.handleLogicalStoreRemoved(var1);
            } catch (Exception var3) {
               WseeRmLogger.logUnexpectedException(var3.toString(), var3);
            }

         }
      });
   }

   protected void handleLogicalStoreAdded(String var1) throws StoreException {
      try {
         this._storesLock.writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SequenceMap handling newly added logical store: " + var1);
         }

         SequenceStore var2 = this.getOrCreateSequenceStore(var1);
         this._stores.add(var2);
         if (LOGGER.isLoggable(Level.FINE)) {
            this.dumpLogicalStoreNames("Added");
            this.dumpSequences();
         }
      } finally {
         this._storesLock.writeLock().unlock();
      }

   }

   protected void handleLogicalStorePreRemoval(String var1) throws StoreException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Handling pre-removal tasks for logical store: " + var1);
      }

      SequenceStore var2 = this.getOrCreateSequenceStore(var1);
      if (var2 != null) {
         Set var3 = var2.keySet();
         String[] var4 = (String[])var3.toArray(new String[var3.size()]);
         String[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            var2.remove(var8);
         }
      }

   }

   protected void handleLogicalStoreRemoved(String var1) throws StoreException {
      try {
         this._storesLock.writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SequenceMap handling newly removed logical store: " + var1);
         }

         SequenceStore var2 = null;
         Iterator var3 = this._stores.iterator();

         while(var3.hasNext()) {
            SequenceStore var4 = (SequenceStore)var3.next();
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
            this.dumpSequences();
         }
      } finally {
         this._storesLock.writeLock().unlock();
      }

   }

   private void dumpLogicalStoreNames(String var1) {
      Set var2 = this.getLogicalStoreNames();
      StringBuffer var3 = new StringBuffer();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var3.append(var5).append(", ");
      }

      LOGGER.fine("SequenceMap " + this + " has these logical stores after one was " + var1 + ": " + var3.toString());
   }

   private void dumpSequences() {
      if (LOGGER.isLoggable(Level.FINER)) {
         Set var1 = this.keySet();
         StringBuffer var2 = new StringBuffer();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Sequence var5 = this.get(var4);
            if (var5 != null) {
               var2.append("   ").append(var5.getClass().getSimpleName()).append(": ").append(var4).append(" - ").append(var5.getLogicalStoreName()).append("\n");
            }
         }

         LOGGER.finer("Current " + this + " Sequences:\n" + var2.toString());
      }

   }

   public void finalize() throws Throwable {
      Exception var1 = null;

      try {
         this._storesLock.writeLock().lock();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            SequenceStore var3 = (SequenceStore)var2.next();

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

   public SequenceStore<S> getSequenceStoreForSequence(S var1) {
      String var2 = var1.getLogicalStoreName();
      if (var2 == null) {
         throw new IllegalStateException("Sequence with ID " + var1.getId() + " didn't have a logical store name assigned to it!");
      } else {
         try {
            this._storesLock.readLock().lock();
            Iterator var3 = this._stores.iterator();

            SequenceStore var4;
            do {
               if (!var3.hasNext()) {
                  throw new IllegalStateException("Sequence with ID " + var1.getId() + " refers to a logical store name for which we have no associated physical store: " + var2);
               }

               var4 = (SequenceStore)var3.next();
            } while(!var4.getName().equals(var2));

            SequenceStore var5 = var4;
            return var5;
         } finally {
            this._storesLock.readLock().unlock();
         }
      }
   }

   public int size() {
      int var1 = 0;

      try {
         this._storesLock.readLock().lock();

         SequenceStore var3;
         for(Iterator var2 = this._stores.iterator(); var2.hasNext(); var1 += var3.size()) {
            var3 = (SequenceStore)var2.next();
         }

         int var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   public boolean isEmpty() {
      boolean var3;
      try {
         this._storesLock.readLock().lock();
         Iterator var1 = this._stores.iterator();

         SequenceStore var2;
         do {
            if (!var1.hasNext()) {
               boolean var7 = false;
               return var7;
            }

            var2 = (SequenceStore)var1.next();
         } while(!var2.isEmpty());

         var3 = true;
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var3;
   }

   public boolean containsKey(Object var1) {
      boolean var8;
      try {
         this._storesLock.readLock().lock();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            SequenceStore var3 = (SequenceStore)var2.next();
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

         SequenceStore var3;
         do {
            if (!var2.hasNext()) {
               boolean var8 = false;
               return var8;
            }

            var3 = (SequenceStore)var2.next();
         } while(!var3.containsValue(var1));

         var4 = true;
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var4;
   }

   public S get(Object var1) {
      Sequence var5;
      try {
         this._storesLock.readLock().lock();
         Iterator var2 = this._stores.iterator();

         SequenceStore var3;
         String var4;
         do {
            if (!var2.hasNext()) {
               var2 = null;
               return var2;
            }

            var3 = (SequenceStore)var2.next();
            var4 = (String)var1;
         } while(!var3.containsKey(var4));

         var5 = var3.get(var1);
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var5;
   }

   public S put(String var1, S var2) {
      SequenceStore var3 = this.getSequenceStoreForSequence(var2);
      Sequence var4 = this.get(var1);
      boolean var5 = var4 == null;
      if (var5) {
         this.adding(var2);
      } else {
         this.updating(var2);
      }

      this.internalPut(var1, var2, var3);
      if (var5) {
         this.added(var2);
      } else {
         this.updated(var2, var4);
      }

      if (var2.isNonBuffered()) {
         var2.getAndClearPendingRequests();
      }

      return var4;
   }

   protected S internalPut(String var1, S var2, SequenceStore<S> var3) {
      return var3.put(var1, var2);
   }

   protected void adding(S var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Adding " + var1);
      }

   }

   protected void added(S var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Added " + var1);
      }

      this.startupSequence(var1);
   }

   protected void updating(S var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Updating " + var1);
      }

   }

   protected void updated(S var1, S var2) {
      if (var2 != var1) {
         var2.setMasterInstance(false);
         var1.setMasterInstance(true);
         var1.copyPropertyChangeListeners(var2);
      } else if (!var1.isMasterInstance()) {
         var1.setMasterInstance(true);
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Updated " + var1);
      }

   }

   public S remove(Object var1) {
      Sequence var2 = this.get(var1);
      if (var2 == null) {
         return var2;
      } else {
         this.removing(var2);

         try {
            this._storesLock.readLock().lock();
            Iterator var3 = this._stores.iterator();

            while(var3.hasNext()) {
               SequenceStore var4 = (SequenceStore)var3.next();
               String var5 = (String)var1;
               if (var4.containsKey(var5)) {
                  var4.remove(var1);
                  break;
               }
            }
         } finally {
            this._storesLock.readLock().unlock();
         }

         this.removed(var2);
         return var2;
      }
   }

   protected void removing(S var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Removing " + var1);
      }

      this.shutdownSequence(var1);
      var1.destroy();
   }

   protected void removed(S var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Removed " + var1);
      }

   }

   boolean startupSequence(S var1) {
      if (var1 != null) {
         var1.setMasterInstance(true);
         if (this._sequenceListener != null) {
            var1.addPropertyChangeListener(this._sequenceListener);
         }

         var1.startup();
         return true;
      } else {
         return false;
      }
   }

   boolean shutdownSequence(S var1) {
      if (var1 != null) {
         var1.shutdown();
         if (this._sequenceListener != null) {
            var1.removePropertyChangeListener(this._sequenceListener);
         }

         var1.setMasterInstance(false);
         return true;
      } else {
         return false;
      }
   }

   public void putAll(Map<? extends String, ? extends S> var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Sequence var4 = (Sequence)var1.get(var3);
         this.put(var3, var4);
      }

   }

   public void clear() {
      try {
         this._storesLock.readLock().lock();
         Iterator var1 = this._stores.iterator();

         while(var1.hasNext()) {
            SequenceStore var2 = (SequenceStore)var1.next();
            var2.clear();
         }
      } finally {
         this._storesLock.readLock().unlock();
      }

   }

   public Set<String> keySet() {
      try {
         this._storesLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            SequenceStore var3 = (SequenceStore)var2.next();
            var1.addAll(var3.keySet());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   public Collection<S> values() {
      try {
         this._storesLock.readLock().lock();
         LinkedList var1 = new LinkedList();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            SequenceStore var3 = (SequenceStore)var2.next();
            var1.addAll(var3.values());
         }

         LinkedList var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   public Set<Map.Entry<String, S>> entrySet() {
      try {
         this._storesLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            SequenceStore var3 = (SequenceStore)var2.next();
            var1.addAll(var3.entrySet());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }
}
