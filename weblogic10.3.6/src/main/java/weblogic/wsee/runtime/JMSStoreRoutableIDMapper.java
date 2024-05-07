package weblogic.wsee.runtime;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.extensions.DestinationAvailabilityListener;
import weblogic.jms.extensions.DestinationDetail;
import weblogic.jms.extensions.JMSDestinationAvailabilityHelper;
import weblogic.jms.extensions.RegistrationHandle;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.management.configuration.WebServicePersistenceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreManager;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.WseePersistLogger;
import weblogic.wsee.buffer2.api.wls.BufferingManager_WLS;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.cluster.spi.ServerNameMapService;
import weblogic.wsee.jaxws.cluster.spi.StoreRoutableIDMapService;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.persistence.LogicalStore;

public class JMSStoreRoutableIDMapper implements StoreRoutableIDMapService, DestinationAvailabilityListener, ServerNameMapService, BufferingManager_WLS.UnitOfOrderQueueFinder {
   private static final Logger LOGGER = Logger.getLogger(JMSStoreRoutableIDMapper.class.getName());
   private static final Protocol HTTP_PROTOCOL;
   private static final Protocol HTTPS_PROTOCOL;
   private List<RegistrationHandle> _handles;
   private ReentrantReadWriteLock _storeListMapLock = new ReentrantReadWriteLock(false);
   private Condition _physicalStoreToServerMapInitializedCondition;
   private long _lastNotificationTimestamp;
   private Map<String, LogicalStoreInfo> _logicalStoreNameToInfoMap;
   private Map<String, List<String>> _ddJndiNameToLogicalStoreNameListMap;
   private Map<String, DDInfo> _ddJndiNameToDDInfoMap;
   private Map<String, String> _physicalStoreToServerMap;
   private List<ServerNameMapService.ServerAddressChangeListener> _serverAddressChangeListeners;
   private static RuntimeAccess _runtimeAccess;

   JMSStoreRoutableIDMapper() {
      this._physicalStoreToServerMapInitializedCondition = this._storeListMapLock.writeLock().newCondition();
      this._lastNotificationTimestamp = -1L;
      this._serverAddressChangeListeners = new ArrayList();
      this._handles = new ArrayList();
      this._logicalStoreNameToInfoMap = new HashMap();
      this._physicalStoreToServerMap = new HashMap();
      this._ddJndiNameToLogicalStoreNameListMap = new HashMap();
      this._ddJndiNameToDDInfoMap = new HashMap();
   }

   public ServerNameMapService.ServerAddress getServerAddress(String var1) {
      URL var3 = null;

      URL var2;
      String var4;
      try {
         var4 = URLManager.findURL(var1, HTTP_PROTOCOL);
         var2 = new URL(var4);
      } catch (Exception var6) {
         WseeCoreLogger.logUnexpectedException(var6.toString(), var6);
         throw new RuntimeException(var6.toString(), var6);
      }

      try {
         var4 = URLManager.findURL(var1, HTTPS_PROTOCOL);
         if (var4 == null) {
            var3 = new URL(var4);
         }
      } catch (Exception var5) {
      }

      ServerNameMapService.ServerAddress var7 = new ServerNameMapService.ServerAddress();
      var7.serverName = var1;
      var7.host = var2.getHost();
      var7.port = var2.getPort();
      if (var3 != null) {
         var7.sslPort = var3.getPort();
      } else {
         var7.sslPort = -1;
      }

      return var7;
   }

   public void addServerAddressChangeListener(ServerNameMapService.ServerAddressChangeListener var1) {
      if (!this._serverAddressChangeListeners.contains(var1)) {
         this._serverAddressChangeListeners.add(var1);
      }

   }

   public void removeServerAddressChangeListener(ServerNameMapService.ServerAddressChangeListener var1) {
      this._serverAddressChangeListeners.remove(var1);
   }

   public void startup() {
      WebServicePersistenceMBean var1 = WebServiceMBeanFactory.getInstance().getWebServicePersistence();
      var1.addPropertyChangeListener(new LogicalStoreListPropertyChangeListener());

      try {
         this._storeListMapLock.writeLock().lock();
         WebServiceLogicalStoreMBean[] var2 = var1.getWebServiceLogicalStores();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WebServiceLogicalStoreMBean var5 = var2[var4];
            if (var5.getPersistenceStrategy().equals("LOCAL_ACCESS_ONLY")) {
               this.handleLogicalStoreAdded(var5, JMSDestinationAvailabilityHelper.getInstance());
            }
         }
      } finally {
         this._storeListMapLock.writeLock().unlock();
      }

      BufferingManager_WLS.setUnitOfOrderQueueFinder(this);
   }

   private void handleLogicalStoreUpdated(String var1, JMSDestinationAvailabilityHelper var2) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Web Services runtime updating LogicalStore " + var1);
      }

      LogicalStoreInfo var3 = (LogicalStoreInfo)this._logicalStoreNameToInfoMap.get(var1);
      this.handleLogicalStoreRemoved(var1);
      this.handleLogicalStoreAdded(var3._mbean, var2);
   }

   private void handleLogicalStoreRemoved(String var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Web Services runtime removing LogicalStore " + var1);
      }

      LogicalStoreInfo var2 = (LogicalStoreInfo)this._logicalStoreNameToInfoMap.get(var1);
      if (var2 != null) {
         if (var2._listener != null) {
            var2._mbean.removePropertyChangeListener(var2._listener);
         }

         String var3 = var2._mbean.getRequestBufferingQueueJndiName();
         this.handleDDJndiNameRemoved(var1, var3);
         var3 = var2._mbean.getResponseBufferingQueueJndiName();
         this.handleDDJndiNameRemoved(var1, var3);
         Iterator var4 = var2.getPhysicalStoreList().iterator();

         while(var4.hasNext()) {
            StoreAndServerNamePair var5 = (StoreAndServerNamePair)var4.next();
            this.handlePhysicalStoreRemoved(var1, var5, false);
         }

         this._logicalStoreNameToInfoMap.remove(var1);
         LogicalStore.removeStore(var1);
      }
   }

   private void handleDDJndiNameRemoved(String var1, String var2) {
      if (var2 != null) {
         List var3 = (List)this._ddJndiNameToLogicalStoreNameListMap.get(var2);
         var3.remove(var1);
         if (var3.size() < 1) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Stopped tracking DD JNDI name: " + var2);
            }

            this._ddJndiNameToLogicalStoreNameListMap.remove(var2);
            DDInfo var4 = (DDInfo)this._ddJndiNameToDDInfoMap.remove(var2);
            this._handles.remove(var4.getRegistrationHandle());
            var4.unregister();
         }
      }

   }

   private void handleLogicalStoreAdded(WebServiceLogicalStoreMBean var1, JMSDestinationAvailabilityHelper var2) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Web Services runtime adding LogicalStore " + var1.getName());
      }

      LogicalStoreInfo var3 = new LogicalStoreInfo(var1);
      this._logicalStoreNameToInfoMap.put(var1.getName(), var3);
      LogicalStore.addStore(var1.getName());
      String var5 = var1.getRequestBufferingQueueJndiName();
      boolean var4 = this.handleDDJndiNameAdded(var1, var2, var5);
      var5 = var1.getResponseBufferingQueueJndiName();
      var4 |= this.handleDDJndiNameAdded(var1, var2, var5);
      if (!var4) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("LogicalStore " + var1.getName() + " has NO request/response buffering queue assigned. Physical store directly specified: " + var1.getPhysicalStoreName());
         }

         if (var1.getPhysicalStoreName() == null || var1.getPhysicalStoreName().length() == 0) {
            throw new IllegalArgumentException("No request/response buffering queue or physical store name specified for logical store: " + var1.getName());
         }

         StoreAndServerNamePair var6 = new StoreAndServerNamePair(var1.getPhysicalStoreName(), _runtimeAccess.getServerName());
         var3.addPhysicalStore(var6);
         this.handlePhysicalStoreAdded(var1.getName(), var6);
      }

      var3._listener = new LogicalStorePropertyChangeListener(var1.getName());
      var1.addPropertyChangeListener(var3._listener);
   }

   private boolean handleDDJndiNameAdded(WebServiceLogicalStoreMBean var1, JMSDestinationAvailabilityHelper var2, String var3) {
      if (var3 != null && var3.length() < 1) {
         var3 = null;
      }

      if (var3 == null) {
         return false;
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("LogicalStore " + var1.getName() + " has buffering queue JNDI: " + var3);
         }

         this.verifyQueueType(var3, var1.getName());
         DDInfo var4 = (DDInfo)this._ddJndiNameToDDInfoMap.get(var3);
         if (var4 == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Started tracking DD JNDI name: " + var3);
            }

            var4 = new DDInfo(var3);
            RegistrationHandle var5 = var2.register((Hashtable)null, var3, this);
            var4.setRegistrationHandle(var5);
            this._handles.add(var5);
            this._ddJndiNameToDDInfoMap.put(var3, var4);
         }

         Object var6 = (List)this._ddJndiNameToLogicalStoreNameListMap.get(var3);
         if (var6 == null) {
            var6 = new ArrayList();
            this._ddJndiNameToLogicalStoreNameListMap.put(var3, var6);
         }

         ((List)var6).add(var1.getName());
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("WSEE runtime registering for JMS destination changes for LogicalStore " + var1.getName() + " and buffering queue JNDI: " + var3);
         }

         return true;
      }
   }

   private void verifyQueueType(String var1, String var2) {
      try {
         InitialContext var3 = new InitialContext();
         var3.lookup(var1);
         if (JMSServerUtilities.findBEDestinationByJNDIName(var1) == null) {
            WseePersistLogger.logImproperBufferingQueueType("Weighted Distributed Destination", var1, var2);
         }
      } catch (NamingException var4) {
      }

   }

   public void shutdown() {
      Iterator var1 = this._handles.iterator();

      while(var1.hasNext()) {
         RegistrationHandle var2 = (RegistrationHandle)var1.next();
         var2.unregister();
      }

      LogicalStore.closeAllStores();
   }

   public List<String> getLocalPhysicalStoresForLogicalStore(String var1) {
      ArrayList var10;
      try {
         this._storeListMapLock.readLock().lock();
         LogicalStoreInfo var2 = (LogicalStoreInfo)this._logicalStoreNameToInfoMap.get(var1);
         ArrayList var3 = new ArrayList();
         if (var2 != null) {
            String var4 = _runtimeAccess.getServerName();
            Iterator var5 = var2.getPhysicalStoreList().iterator();

            while(var5.hasNext()) {
               StoreAndServerNamePair var6 = (StoreAndServerNamePair)var5.next();
               if (var6.getServerName().equals(var4)) {
                  var3.add(var6.getPhysStoreName());
               }
            }
         }

         var10 = var3;
      } finally {
         this._storeListMapLock.readLock().unlock();
      }

      return var10;
   }

   public String getServerNameForPhysicalStore(String var1) {
      String var2;
      try {
         this._storeListMapLock.readLock().lock();
         var2 = (String)this._physicalStoreToServerMap.get(var1);
      } finally {
         this._storeListMapLock.readLock().unlock();
      }

      return var2;
   }

   public void onDestinationsAvailable(String var1, List<DestinationDetail> var2) {
      List var3;
      DDInfo var4;
      try {
         this._storeListMapLock.readLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("WSEE runtime just detected destinations available for queue JNDI: " + var1);
         }

         var3 = (List)this._ddJndiNameToLogicalStoreNameListMap.get(var1);
         var4 = (DDInfo)this._ddJndiNameToDDInfoMap.get(var1);
      } finally {
         this._storeListMapLock.readLock().unlock();
      }

      if (var3 != null && var4 != null) {
         Set var5 = this.getDDMemberInfosFromDestinationChange(var2);
         var4.addMembers(var5);
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();

            LogicalStoreInfo var8;
            try {
               this._storeListMapLock.readLock().lock();
               var8 = (LogicalStoreInfo)this._logicalStoreNameToInfoMap.get(var7);
            } finally {
               this._storeListMapLock.readLock().unlock();
            }

            Iterator var9 = var5.iterator();

            while(var9.hasNext()) {
               DDMemberInfo var10 = (DDMemberInfo)var9.next();
               boolean var11 = false;
               Iterator var12 = var8.getPhysicalStoreList().iterator();

               while(var12.hasNext()) {
                  StoreAndServerNamePair var13 = (StoreAndServerNamePair)var12.next();
                  if (var13.getPhysStoreName().equals(var10.getPair().getPhysStoreName())) {
                     var11 = true;
                  }
               }

               if (!var11) {
                  this.handlePhysicalStoreAdded(var7, var10.getPair());
               }
            }
         }

         try {
            this._storeListMapLock.writeLock().lock();
            this._lastNotificationTimestamp = System.currentTimeMillis();
            this._physicalStoreToServerMapInitializedCondition.signalAll();
         } finally {
            this._storeListMapLock.writeLock().unlock();
         }

         this.notifyServerAddressChange((String)null);
      }
   }

   private void handlePhysicalStoreAdded(String var1, StoreAndServerNamePair var2) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("WSEE runtime just detected new physical store added for logical store " + var1 + ": " + var2);
      }

      try {
         this._storeListMapLock.writeLock().lock();
         if (this._physicalStoreToServerMap.containsKey(var2.getPhysStoreName())) {
            return;
         }

         this._physicalStoreToServerMap.put(var2.getPhysStoreName(), var2.getServerName());
         LogicalStoreInfo var3 = (LogicalStoreInfo)this._logicalStoreNameToInfoMap.get(var1);
         var3.addPhysicalStore(var2);
      } finally {
         this._storeListMapLock.writeLock().unlock();
      }

      if (var2.getServerName().equals(_runtimeAccess.getServerName())) {
         List var11 = LogicalStore.getLogicalStores(var1);
         Iterator var4 = var11.iterator();

         while(var4.hasNext()) {
            LogicalStore var5 = (LogicalStore)var4.next();

            try {
               var5.addPhysicalStore(var2.getPhysStoreName());
            } catch (Exception var9) {
               WseeCoreLogger.logUnexpectedException(var9.toString(), var9);
            }
         }
      }

      this.notifyServerAddressChange((String)null);
   }

   public void onDestinationsUnavailable(String var1, List<DestinationDetail> var2) {
      List var3;
      DDInfo var4;
      try {
         this._storeListMapLock.readLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("WSEE runtime just detected destinations UN-available for queue JNDI: " + var1);
         }

         var3 = (List)this._ddJndiNameToLogicalStoreNameListMap.get(var1);
         var4 = (DDInfo)this._ddJndiNameToDDInfoMap.get(var1);
      } finally {
         this._storeListMapLock.readLock().unlock();
      }

      if (var3 != null && var4 != null) {
         Set var5 = this.getDDMemberInfosFromDestinationChange(var2);
         var4.removeMembers(var5);
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();

            LogicalStoreInfo var8;
            try {
               this._storeListMapLock.readLock().lock();
               var8 = (LogicalStoreInfo)this._logicalStoreNameToInfoMap.get(var7);
            } finally {
               this._storeListMapLock.readLock().unlock();
            }

            Iterator var9 = var5.iterator();

            while(var9.hasNext()) {
               DDMemberInfo var10 = (DDMemberInfo)var9.next();
               boolean var11 = false;
               Iterator var12 = var8._physicalStoreList.iterator();

               while(var12.hasNext()) {
                  StoreAndServerNamePair var13 = (StoreAndServerNamePair)var12.next();
                  if (var13.getPhysStoreName().equals(var10.getPair().getPhysStoreName())) {
                     var11 = true;
                  }
               }

               if (var11) {
                  this.handlePhysicalStoreRemoved(var7, var10.getPair(), true);
               }
            }
         }

         this.notifyServerAddressChange((String)null);
      }
   }

   private void handlePhysicalStoreRemoved(String var1, StoreAndServerNamePair var2, boolean var3) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("WSEE runtime just detected physical store removed for logical store " + var1 + ": " + var2);
      }

      try {
         this._storeListMapLock.writeLock().lock();
         String var4 = (String)this._physicalStoreToServerMap.remove(var2.getPhysStoreName());
         if (var4 == null) {
            return;
         }

         LogicalStoreInfo var5 = (LogicalStoreInfo)this._logicalStoreNameToInfoMap.get(var1);
         var5.removePhysicalStore(var2);
      } finally {
         this._storeListMapLock.writeLock().unlock();
      }

      if (var3 && var2.getServerName().equals(_runtimeAccess.getServerName())) {
         List var12 = LogicalStore.getLogicalStores(var1);
         Iterator var13 = var12.iterator();

         while(var13.hasNext()) {
            LogicalStore var6 = (LogicalStore)var13.next();

            try {
               var6.removePhysicalStore(var2.getPhysStoreName());
            } catch (Exception var10) {
               WseeCoreLogger.logUnexpectedException(var10.toString(), var10);
            }
         }
      }

   }

   public void onFailure(String var1, Exception var2) {
   }

   private Set<DDMemberInfo> getDDMemberInfosFromDestinationChange(List<DestinationDetail> var1) {
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         DestinationDetail var4 = (DestinationDetail)var3.next();
         String var5 = var4.getStoreName();
         if (var5 == null) {
            PersistentStore var6 = PersistentStoreManager.getManager().getDefaultStore();
            var5 = var6 != null ? var6.getName() : null;
         }

         if (var5 != null) {
            String var9 = var4.getWLSServerName() != null ? var4.getWLSServerName() : _runtimeAccess.getServerName();
            StoreAndServerNamePair var7 = new StoreAndServerNamePair(var5, var9);
            DDMemberInfo var8 = new DDMemberInfo(var7, var4);
            var2.add(var8);
         }
      }

      return var2;
   }

   public Map<String, String> getCurrentRoutableIDToServerMap() throws Exception {
      this.ensurePhysicalStoreToServerMapInitialized();

      HashMap var1;
      try {
         this._storeListMapLock.readLock().lock();
         var1 = new HashMap(this._physicalStoreToServerMap);
      } finally {
         this._storeListMapLock.readLock().unlock();
      }

      return var1;
   }

   public void ensurePhysicalStoreToServerMapInitialized() {
      if (!this.needStoreToServerMap()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.info("LogicalStore configuration for this server doesn't require waiting for a store-to-server map to be initialized. Allowing immediate access to LogicalStores");
         }

      } else {
         boolean var1 = false;
         boolean var2 = false;

         try {
            try {
               this._storeListMapLock.readLock().lock();
               var1 = true;
               boolean var3 = false;
               long var4;
               if (this._lastNotificationTimestamp != -1L) {
                  var4 = (System.currentTimeMillis() - this._lastNotificationTimestamp) / 1000L;
                  if (var4 > 20L) {
                     var3 = true;
                     this._storeListMapLock.readLock().unlock();
                     var1 = false;
                     if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Found " + var4 + " seconds elapsed time between last notification and ensurePhysicalStoreToServerMapInitialized call. Returning from ensurePhysicalStoreToServerMapInitialized without waiting.");
                     }

                     return;
                  }
               }

               this._storeListMapLock.readLock().unlock();
               var1 = false;
               this._storeListMapLock.writeLock().lock();
               var2 = true;

               while(!var3) {
                  var4 = this._lastNotificationTimestamp == -1L ? 150L : 20L;
                  if (LOGGER.isLoggable(Level.INFO)) {
                     LOGGER.info("Blocking current thread " + Thread.currentThread() + " for " + var4 + " seconds. Waiting for physical store(s) to be detected for any logical store on this server");
                  }

                  boolean var6 = this._physicalStoreToServerMapInitializedCondition.await(var4, TimeUnit.SECONDS);
                  if (!var6) {
                     var3 = true;
                  }
               }

               if (LOGGER.isLoggable(Level.INFO)) {
                  LOGGER.info("Resuming thread " + Thread.currentThread());
                  return;
               }
            } catch (Exception var10) {
               WseeCoreLogger.logUnexpectedException(var10.toString(), var10);
            }

         } finally {
            if (var1) {
               this._storeListMapLock.readLock().unlock();
            }

            if (var2) {
               this._storeListMapLock.writeLock().unlock();
            }

         }
      }
   }

   private boolean needStoreToServerMap() {
      WebServiceMBean var1 = WebServiceMBeanFactory.getInstance();
      WebServicePersistenceMBean var2 = var1.getWebServicePersistence();
      if (var2 != null && var2.getWebServiceLogicalStores() != null) {
         WebServiceLogicalStoreMBean[] var3 = var2.getWebServiceLogicalStores();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            WebServiceLogicalStoreMBean var6 = var3[var5];
            if ((var6.getRequestBufferingQueueJndiName() != null || var6.getResponseBufferingQueueJndiName() != null) && !var6.getPersistenceStrategy().equals("IN_MEMORY")) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void notifyServerAddressChange(String var1) {
      ServerNameMapService.ServerAddressChangeListener[] var2 = (ServerNameMapService.ServerAddressChangeListener[])this._serverAddressChangeListeners.toArray(new ServerNameMapService.ServerAddressChangeListener[this._serverAddressChangeListeners.size()]);
      ServerNameMapService.ServerAddressChangeListener[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ServerNameMapService.ServerAddressChangeListener var6 = var3[var5];
         var6.serverAddressChanged(var1, this);
      }

   }

   public String findQueueJndiNameForUnitOfOrder(String var1, String var2) {
      String var4 = null;

      DDInfo var3;
      Set var5;
      label90: {
         try {
            var4 = WsUtil.getStoreNameFromRoutableUUID(var2);
            if (var4 != null) {
               this._storeListMapLock.readLock().lock();
               var3 = (DDInfo)this._ddJndiNameToDDInfoMap.get(var1);
               if (var3 != null) {
                  break label90;
               }

               var5 = null;
               return var5;
            }

            var5 = null;
         } finally {
            this._storeListMapLock.readLock().unlock();
         }

         return var5;
      }

      var5 = var3.getMembers();
      Iterator var6 = var5.iterator();

      DDMemberInfo var7;
      do {
         if (!var6.hasNext()) {
            return null;
         }

         var7 = (DDMemberInfo)var6.next();
      } while(!var7.getPair().getPhysStoreName().equals(var4));

      return var7.getDestDetail().getJNDIName();
   }

   static {
      try {
         HTTP_PROTOCOL = ProtocolManager.findProtocol("http");
         HTTPS_PROTOCOL = ProtocolManager.findProtocol("https");
      } catch (Exception var1) {
         WseeCoreLogger.logUnexpectedException(var1.toString(), var1);
         throw new RuntimeException(var1.toString(), var1);
      }

      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      _runtimeAccess = ManagementService.getRuntimeAccess(var0);
   }

   private static class StoreAndServerNamePair {
      private String _physStoreName;
      private String _serverName;

      private StoreAndServerNamePair(String var1, String var2) {
         this._physStoreName = var1;
         this._serverName = var2;
      }

      public String getPhysStoreName() {
         return this._physStoreName;
      }

      public void setPhysStoreName(String var1) {
         this._physStoreName = var1;
      }

      public String getServerName() {
         return this._serverName;
      }

      public void setServerName(String var1) {
         this._serverName = var1;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof StoreAndServerNamePair)) {
            return false;
         } else {
            StoreAndServerNamePair var2 = (StoreAndServerNamePair)var1;
            return this._physStoreName.equals(var2._physStoreName) && this._serverName.equals(var2._serverName);
         }
      }

      public int hashCode() {
         StringBuffer var1 = new StringBuffer(this._physStoreName);
         var1.append(this._serverName);
         return var1.toString().hashCode();
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("Physical store: ").append(this._physStoreName);
         var1.append(" ");
         var1.append("Server: ").append(this._serverName);
         return var1.toString();
      }

      // $FF: synthetic method
      StoreAndServerNamePair(String var1, String var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class LogicalStoreInfo {
      private WebServiceLogicalStoreMBean _mbean;
      private final ReentrantReadWriteLock _physicalStoreListLock = new ReentrantReadWriteLock(false);
      private List<StoreAndServerNamePair> _physicalStoreList;
      public PropertyChangeListener _listener;

      public LogicalStoreInfo(WebServiceLogicalStoreMBean var1) {
         this._mbean = var1;
         this._physicalStoreList = new ArrayList();
      }

      public void addPhysicalStore(StoreAndServerNamePair var1) {
         try {
            this._physicalStoreListLock.writeLock().lock();
            this._physicalStoreList.add(var1);
            if (JMSStoreRoutableIDMapper.LOGGER.isLoggable(Level.FINE)) {
               JMSStoreRoutableIDMapper.LOGGER.fine("Added physical store for server " + var1.getServerName() + " PhysicalStore='" + var1.getPhysStoreName() + "' Current physical store count: " + this._physicalStoreList.size());
            }
         } finally {
            this._physicalStoreListLock.writeLock().unlock();
         }

      }

      public void removePhysicalStore(StoreAndServerNamePair var1) {
         try {
            this._physicalStoreListLock.writeLock().lock();
            this._physicalStoreList.remove(var1);
            if (JMSStoreRoutableIDMapper.LOGGER.isLoggable(Level.FINE)) {
               JMSStoreRoutableIDMapper.LOGGER.fine("Removed physical store for server " + var1.getServerName() + " PhysicalStore='" + var1.getPhysStoreName() + "' Current physical store count: " + this._physicalStoreList.size());
            }
         } finally {
            this._physicalStoreListLock.writeLock().unlock();
         }

      }

      public List<StoreAndServerNamePair> getPhysicalStoreList() {
         ArrayList var1;
         try {
            this._physicalStoreListLock.readLock().lock();
            var1 = new ArrayList(this._physicalStoreList);
         } finally {
            this._physicalStoreListLock.readLock().unlock();
         }

         return var1;
      }
   }

   private static class DDMemberInfo {
      private StoreAndServerNamePair _pair;
      private DestinationDetail _destDetail;

      private DDMemberInfo(StoreAndServerNamePair var1, DestinationDetail var2) {
         this._pair = var1;
         this._destDetail = var2;
      }

      public StoreAndServerNamePair getPair() {
         return this._pair;
      }

      public DestinationDetail getDestDetail() {
         return this._destDetail;
      }

      public int hashCode() {
         return this._pair.hashCode();
      }

      public boolean equals(Object var1) {
         return var1 instanceof DDMemberInfo && this._pair.equals(var1);
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString());
         var1.append(" - ");
         var1.append(this._pair.toString());
         return var1.toString();
      }

      // $FF: synthetic method
      DDMemberInfo(StoreAndServerNamePair var1, DestinationDetail var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class DDInfo {
      private String _jndiName;
      private final ReentrantReadWriteLock _physicalStoreListLock = new ReentrantReadWriteLock(false);
      private Set<DDMemberInfo> _members;
      public RegistrationHandle _registrationHandle;

      public DDInfo(String var1) {
         this._jndiName = var1;
         this._members = new HashSet();
      }

      public String getJndiName() {
         return this._jndiName;
      }

      public Set<DDMemberInfo> getMembers() {
         HashSet var1;
         try {
            this._physicalStoreListLock.readLock().lock();
            var1 = new HashSet(this._members);
         } finally {
            this._physicalStoreListLock.readLock().unlock();
         }

         return var1;
      }

      public void addMembers(Set<DDMemberInfo> var1) {
         try {
            this._physicalStoreListLock.writeLock().lock();
            this._members.addAll(var1);
            if (JMSStoreRoutableIDMapper.LOGGER.isLoggable(Level.FINE)) {
               JMSStoreRoutableIDMapper.LOGGER.fine("DD JNDI Name added members (" + dumpMembers(var1) + ") to make current member list (" + dumpMembers(this._members) + ")");
            }
         } finally {
            this._physicalStoreListLock.writeLock().unlock();
         }

      }

      public void removeMembers(Set<DDMemberInfo> var1) {
         try {
            this._physicalStoreListLock.writeLock().lock();
            this._members.removeAll(var1);
            if (JMSStoreRoutableIDMapper.LOGGER.isLoggable(Level.FINE)) {
               JMSStoreRoutableIDMapper.LOGGER.fine("DD JNDI Name removed members (" + dumpMembers(var1) + ") to make current member list (" + dumpMembers(this._members) + ")");
            }
         } finally {
            this._physicalStoreListLock.writeLock().unlock();
         }

      }

      private static String dumpMembers(Set<DDMemberInfo> var0) {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            DDMemberInfo var3 = (DDMemberInfo)var2.next();
            var1.append(var3.getPair());
            var1.append(",");
         }

         if (var1.length() > 0) {
            var1.deleteCharAt(var1.length() - 1);
         }

         return var1.toString();
      }

      public RegistrationHandle getRegistrationHandle() {
         return this._registrationHandle;
      }

      public void setRegistrationHandle(RegistrationHandle var1) {
         this._registrationHandle = var1;
      }

      public void unregister() {
         if (this._registrationHandle != null) {
            try {
               this._registrationHandle.unregister();
            } catch (Exception var2) {
               if (JMSStoreRoutableIDMapper.LOGGER.isLoggable(Level.SEVERE)) {
                  JMSStoreRoutableIDMapper.LOGGER.log(Level.SEVERE, var2.toString(), var2);
               }

               WseeCoreLogger.logUnexpectedException(var2.toString(), var2);
            }
         }

      }
   }

   private class LogicalStorePropertyChangeListener implements PropertyChangeListener {
      private String _logicalStoreName;

      private LogicalStorePropertyChangeListener(String var2) {
         this._logicalStoreName = var2;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if ("RequestBufferingQueueJndiName".equals(var1.getPropertyName())) {
            try {
               JMSStoreRoutableIDMapper.this._storeListMapLock.writeLock().lock();
               JMSStoreRoutableIDMapper.this.handleLogicalStoreUpdated(this._logicalStoreName, JMSDestinationAvailabilityHelper.getInstance());
            } finally {
               JMSStoreRoutableIDMapper.this._storeListMapLock.writeLock().unlock();
            }
         }

      }

      // $FF: synthetic method
      LogicalStorePropertyChangeListener(String var2, Object var3) {
         this(var2);
      }
   }

   private class LogicalStoreListPropertyChangeListener implements PropertyChangeListener {
      private LogicalStoreListPropertyChangeListener() {
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if ("WebServiceLogicalStores".equals(var1.getPropertyName())) {
            if (JMSStoreRoutableIDMapper.LOGGER.isLoggable(Level.FINE)) {
               JMSStoreRoutableIDMapper.LOGGER.fine("JMSStoreRoutableIDMapper detected a change to the logical stores configured on the local server");
            }

            WebServiceLogicalStoreMBean[] var2 = (WebServiceLogicalStoreMBean[])((WebServiceLogicalStoreMBean[])var1.getNewValue());

            try {
               JMSStoreRoutableIDMapper.this._storeListMapLock.writeLock().lock();
               ArrayList var3 = new ArrayList();
               ArrayList var4 = new ArrayList(JMSStoreRoutableIDMapper.this._logicalStoreNameToInfoMap.keySet());
               WebServiceLogicalStoreMBean[] var5 = var2;
               int var6 = var2.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  WebServiceLogicalStoreMBean var8 = var5[var7];
                  if (var8.getPersistenceStrategy().equals("LOCAL_ACCESS_ONLY")) {
                     var4.remove(var8.getName());
                     if (!JMSStoreRoutableIDMapper.this._logicalStoreNameToInfoMap.containsKey(var8.getName())) {
                        var3.add(var8);
                     }
                  }
               }

               Iterator var12 = var4.iterator();

               while(var12.hasNext()) {
                  String var13 = (String)var12.next();
                  JMSStoreRoutableIDMapper.this.handleLogicalStoreRemoved(var13);
               }

               var12 = var3.iterator();

               while(var12.hasNext()) {
                  WebServiceLogicalStoreMBean var14 = (WebServiceLogicalStoreMBean)var12.next();
                  JMSStoreRoutableIDMapper.this.handleLogicalStoreAdded(var14, JMSDestinationAvailabilityHelper.getInstance());
               }
            } finally {
               JMSStoreRoutableIDMapper.this._storeListMapLock.writeLock().unlock();
            }

         }
      }

      // $FF: synthetic method
      LogicalStoreListPropertyChangeListener(Object var2) {
         this();
      }
   }

   public static enum ChangeType {
      ADD,
      REMOVE,
      UPDATE;
   }
}
