package weblogic.wsee.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.cluster.spi.RoutableIDMapServiceRegistry;
import weblogic.wsee.jaxws.cluster.spi.StoreRoutableIDMapService;
import weblogic.wsee.monitoring.WseeRuntimeMBeanDelegate;
import weblogic.wsee.reliability2.ReliabilityService;

public class WebServicesRuntime {
   private static final Logger LOGGER = Logger.getLogger(WebServicesRuntime.class.getName());
   private static WebServicesRuntime _instance = new WebServicesRuntime();
   private ReentrantReadWriteLock _runtimeReadyLock = new ReentrantReadWriteLock(false);
   private State _state;
   private boolean _runtimeReady;
   private StoreRoutableIDMapService[] _storeMappers;
   private JMSStoreRoutableIDMapper _jmsStoreRoutableIdMapper;

   public static WebServicesRuntime getInstance() {
      return _instance;
   }

   private WebServicesRuntime() {
      if (KernelStatus.isServer()) {
         this.ctorOnServerLogic();
      } else {
         this.ctorOffServerLogic();
      }

   }

   private void ctorOffServerLogic() {
      this._storeMappers = new StoreRoutableIDMapService[1];
      this._storeMappers[0] = new StandaloneVMRoutableIDMapper();
      this._state = WebServicesRuntime.State.STARTED;
      this._runtimeReady = true;
   }

   private void ctorOnServerLogic() {
      this._state = WebServicesRuntime.State.INITIAL;
      this._storeMappers = new StoreRoutableIDMapService[2];
      this._jmsStoreRoutableIdMapper = new JMSStoreRoutableIDMapper();
      this._storeMappers[0] = this._jmsStoreRoutableIdMapper;
      this._storeMappers[1] = new InMemoryStoreRoutableIDMapper();
      RoutableIDMapServiceRegistry var1 = RoutableIDMapServiceRegistry.getInstance();
      StoreRoutableIDMapService[] var2 = this._storeMappers;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         StoreRoutableIDMapService var5 = var2[var4];
         var1.addMapper(var5);
      }

      this._runtimeReady = false;
   }

   public boolean isStarted() {
      boolean var1;
      try {
         this._runtimeReadyLock.readLock().lock();
         var1 = this._state == WebServicesRuntime.State.STARTED;
      } finally {
         this._runtimeReadyLock.readLock().unlock();
      }

      return var1;
   }

   public boolean ensureReady() {
      try {
         this._runtimeReadyLock.readLock().lock();
         boolean var1 = this._state == WebServicesRuntime.State.STARTED;
         if (Boolean.TRUE.equals(this._runtimeReady) || !var1) {
            boolean var2 = var1;
            return var2;
         }
      } finally {
         this._runtimeReadyLock.readLock().unlock();
      }

      if (KernelStatus.isServer()) {
         try {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("WebServicesRuntime.ensureReady is checking with JMSStoreRoutableIDMapper to see if its runtime is ready");
            }

            this._runtimeReadyLock.writeLock().lock();
            this._jmsStoreRoutableIdMapper.ensurePhysicalStoreToServerMapInitialized();
            this._runtimeReady = true;
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("WebServicesRuntime.ensureReady is done. Runtime is ready");
            }
         } finally {
            this._runtimeReadyLock.writeLock().unlock();
         }
      }

      return true;
   }

   void startup() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Web Services runtime STARTING UP");
      }

      this.setState(WebServicesRuntime.State.STARTING_UP);
      StoreRoutableIDMapService[] var1 = this._storeMappers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         StoreRoutableIDMapService var4 = var1[var3];
         var4.startup();
      }

      if (WseeRuntimeMBeanDelegate.isReliableSecureProfileEnabled()) {
         try {
            ReliabilityService.startup();
         } catch (Exception var5) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
               LOGGER.log(Level.SEVERE, var5.toString(), var5);
            }
         }
      }

      this.setState(WebServicesRuntime.State.STARTED);
   }

   private void setState(State var1) {
      try {
         this._runtimeReadyLock.readLock().lock();
         this._state = var1;
      } finally {
         this._runtimeReadyLock.readLock().unlock();
      }

   }

   void shutdown() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Web Services runtime SHUTTING DOWN");
      }

      this.setState(WebServicesRuntime.State.SHUTTING_DOWN);

      try {
         ReliabilityService.shutdown();
      } catch (Exception var5) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, var5.toString(), var5);
         }
      }

      StoreRoutableIDMapService[] var1 = this._storeMappers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         StoreRoutableIDMapService var4 = var1[var3];
         var4.shutdown();
      }

      this.setState(WebServicesRuntime.State.SHUTDOWN);
   }

   public List<String> getLocalPhysicalStoresForLogicalStore(String var1) {
      Object var2 = null;
      StoreRoutableIDMapService[] var3 = this._storeMappers;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         StoreRoutableIDMapService var6 = var3[var5];
         var2 = var6.getLocalPhysicalStoresForLogicalStore(var1);
         if (!((List)var2).isEmpty()) {
            return (List)var2;
         }
      }

      if (var2 == null) {
         var2 = new ArrayList();
      }

      return (List)var2;
   }

   public String getServerNameForPhysicalStore(String var1) {
      String var2 = null;
      StoreRoutableIDMapService[] var3 = this._storeMappers;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         StoreRoutableIDMapService var6 = var3[var5];
         var2 = var6.getServerNameForPhysicalStore(var1);
         if (var2 != null) {
            return var2;
         }
      }

      return var2;
   }

   private class StandaloneVMRoutableIDMapper implements StoreRoutableIDMapService {
      private StandaloneVMRoutableIDMapper() {
      }

      public void startup() {
      }

      public void shutdown() {
      }

      public List<String> getLocalPhysicalStoresForLogicalStore(String var1) {
         ArrayList var2 = new ArrayList();
         WebServiceLogicalStoreMBean[] var3 = WebServiceMBeanFactory.getInstance().getWebServicePersistence().getWebServiceLogicalStores();
         WebServiceLogicalStoreMBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            WebServiceLogicalStoreMBean var7 = var4[var6];
            if (var7.getName().equals(var1)) {
               String var8 = var7.getPhysicalStoreName();
               if (var8 != null) {
                  var2.add(var8);
               }

               return var2;
            }
         }

         return var2;
      }

      public String getServerNameForPhysicalStore(String var1) {
         return null;
      }

      public Map<String, String> getCurrentRoutableIDToServerMap() throws Exception {
         return new HashMap();
      }

      // $FF: synthetic method
      StandaloneVMRoutableIDMapper(Object var2) {
         this();
      }
   }

   private static enum State {
      INITIAL,
      STARTING_UP,
      STARTED,
      SHUTTING_DOWN,
      SHUTDOWN;
   }
}
