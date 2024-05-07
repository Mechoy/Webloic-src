package weblogic.wsee.runtime;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.management.configuration.WebServicePersistenceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.cluster.spi.StoreRoutableIDMapService;
import weblogic.wsee.persistence.InMemoryValuesMap;
import weblogic.wsee.persistence.LogicalStore;

public class InMemoryStoreRoutableIDMapper implements StoreRoutableIDMapService {
   private static final Logger LOGGER = Logger.getLogger(InMemoryStoreRoutableIDMapper.class.getName());
   private static RuntimeAccess _runtimeAccess;
   private ReentrantReadWriteLock _storeListMapLock = new ReentrantReadWriteLock(false);
   private Set<String> _logicalStoreNameSet = new HashSet();
   private Map<String, String> _physicalStoreToServerMap = new HashMap(1);

   InMemoryStoreRoutableIDMapper() {
   }

   public void startup() {
      WebServicePersistenceMBean var1 = WebServiceMBeanFactory.getInstance().getWebServicePersistence();
      var1.addPropertyChangeListener(new LogicalStoreListPropertyChangeListener());

      try {
         this._storeListMapLock.writeLock().lock();
         this._physicalStoreToServerMap.put(InMemoryValuesMap.PHYSICAL_STORE_NAME, _runtimeAccess.getServerName());
         WebServiceLogicalStoreMBean[] var2 = var1.getWebServiceLogicalStores();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WebServiceLogicalStoreMBean var5 = var2[var4];
            if (var5.getPersistenceStrategy().equals("IN_MEMORY")) {
               this.handleLogicalStoreAdded(var5);
            }
         }
      } finally {
         this._storeListMapLock.writeLock().unlock();
      }

   }

   private void handleLogicalStoreRemoved(String var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Web Services runtime removing LogicalStore " + var1);
      }

      if (this._logicalStoreNameSet.contains(var1)) {
         this.handlePhysicalStoreRemoved(var1);
         this._logicalStoreNameSet.remove(var1);
         LogicalStore.removeStore(var1);
      }
   }

   private void handleLogicalStoreAdded(WebServiceLogicalStoreMBean var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Web Services runtime adding LogicalStore " + var1.getName());
      }

      this._logicalStoreNameSet.add(var1.getName());
      LogicalStore.addStore(var1.getName());
      this.handlePhysicalStoreAdded(var1.getName());
   }

   public void shutdown() {
      LogicalStore.closeAllStores();
   }

   public List<String> getLocalPhysicalStoresForLogicalStore(String var1) {
      ArrayList var3;
      try {
         this._storeListMapLock.readLock().lock();
         ArrayList var2 = new ArrayList();
         if (this._logicalStoreNameSet.contains(var1)) {
            var2.add(InMemoryValuesMap.PHYSICAL_STORE_NAME);
         }

         var3 = var2;
      } finally {
         this._storeListMapLock.readLock().unlock();
      }

      return var3;
   }

   public String getServerNameForPhysicalStore(String var1) {
      return (String)this._physicalStoreToServerMap.get(var1);
   }

   private void handlePhysicalStoreAdded(String var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("WSEE runtime just detected new physical store added for logical store " + var1 + ": " + InMemoryValuesMap.PHYSICAL_STORE_NAME);
      }

      List var2 = LogicalStore.getLogicalStores(var1);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         LogicalStore var4 = (LogicalStore)var3.next();

         try {
            var4.addPhysicalStore(InMemoryValuesMap.PHYSICAL_STORE_NAME);
         } catch (Exception var6) {
            WseeCoreLogger.logUnexpectedException(var6.toString(), var6);
         }
      }

      this.notifyServerAddressChange((String)null);
   }

   private void handlePhysicalStoreRemoved(String var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("WSEE runtime just detected physical store removed for logical store " + var1 + ": " + InMemoryValuesMap.PHYSICAL_STORE_NAME);
      }

      List var2 = LogicalStore.getLogicalStores(var1);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         LogicalStore var4 = (LogicalStore)var3.next();

         try {
            var4.removePhysicalStore(InMemoryValuesMap.PHYSICAL_STORE_NAME);
         } catch (Exception var6) {
            WseeCoreLogger.logUnexpectedException(var6.toString(), var6);
         }
      }

   }

   public Map<String, String> getCurrentRoutableIDToServerMap() throws Exception {
      HashMap var1;
      try {
         this._storeListMapLock.readLock().lock();
         var1 = new HashMap(this._physicalStoreToServerMap);
      } finally {
         this._storeListMapLock.readLock().unlock();
      }

      return var1;
   }

   private void notifyServerAddressChange(String var1) {
   }

   static {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      _runtimeAccess = ManagementService.getRuntimeAccess(var0);
   }

   private class LogicalStoreListPropertyChangeListener implements PropertyChangeListener {
      private LogicalStoreListPropertyChangeListener() {
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if ("WebServiceLogicalStores".equals(var1.getPropertyName())) {
            if (InMemoryStoreRoutableIDMapper.LOGGER.isLoggable(Level.FINE)) {
               InMemoryStoreRoutableIDMapper.LOGGER.fine("InMemoryStoreRoutableIDMapper detected a change to the logical stores configured on the local server");
            }

            WebServiceLogicalStoreMBean[] var2 = (WebServiceLogicalStoreMBean[])((WebServiceLogicalStoreMBean[])var1.getNewValue());

            try {
               InMemoryStoreRoutableIDMapper.this._storeListMapLock.writeLock().lock();
               ArrayList var3 = new ArrayList();
               ArrayList var4 = new ArrayList(InMemoryStoreRoutableIDMapper.this._logicalStoreNameSet);
               WebServiceLogicalStoreMBean[] var5 = var2;
               int var6 = var2.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  WebServiceLogicalStoreMBean var8 = var5[var7];
                  if (var8.getPersistenceStrategy().equals("IN_MEMORY")) {
                     var4.remove(var8.getName());
                     if (!InMemoryStoreRoutableIDMapper.this._logicalStoreNameSet.contains(var8.getName())) {
                        var3.add(var8);
                     }
                  }
               }

               Iterator var13 = var4.iterator();

               while(var13.hasNext()) {
                  String var14 = (String)var13.next();
                  InMemoryStoreRoutableIDMapper.this.handleLogicalStoreRemoved(var14);
               }

               var13 = var3.iterator();

               while(var13.hasNext()) {
                  WebServiceLogicalStoreMBean var15 = (WebServiceLogicalStoreMBean)var13.next();
                  InMemoryStoreRoutableIDMapper.this.handleLogicalStoreAdded(var15);
               }
            } finally {
               InMemoryStoreRoutableIDMapper.this._storeListMapLock.writeLock().unlock();
            }

         }
      }

      // $FF: synthetic method
      LogicalStoreListPropertyChangeListener(Object var2) {
         this();
      }
   }
}
