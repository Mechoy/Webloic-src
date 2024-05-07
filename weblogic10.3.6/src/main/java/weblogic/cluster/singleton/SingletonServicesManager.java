package weblogic.cluster.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterService;
import weblogic.cluster.migration.MigratableGroup;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SingletonServiceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServiceFailureException;
import weblogic.work.InheritableThreadContext;

public final class SingletonServicesManager implements RemoteSingletonServicesControl, BeanUpdateListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final SingletonServicesManager THE_ONE = new SingletonServicesManager();
   private LeaseManager manager;
   private static final boolean DEBUG = SingletonServicesDebugLogger.isDebugEnabled();
   private final HashMap nameToServiceMap = new HashMap();
   private final HashMap internalServiceMap = new HashMap();
   private final HashMap nameToContextMap = new HashMap();
   private final HashMap constructedClassesMap = new HashMap();
   private final ArrayList activeServices = new ArrayList();
   private ClusterMBean myCluster;
   private ServerMBean myserver;
   private boolean started = false;

   public void start() throws ServiceFailureException {
      this.myserver = ManagementService.getRuntimeAccess(kernelId).getServer();
      this.myCluster = this.myserver.getCluster();
      this.manager = ClusterService.getServices().getDefaultLeaseManager("service");
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      var1.addBeanUpdateListener(getInstance());
      this.started = true;
      this.startSingletonServices();

      try {
         ServerHelper.exportObject(this, ClusterService.getClusterService().getHeartbeatTimeoutMillis());
      } catch (RemoteException var3) {
         throw new ServiceFailureException(var3);
      }
   }

   public void stop() {
      Iterator var1 = ((ArrayList)this.activeServices.clone()).iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();

         try {
            this.deactivateService(var2);
         } catch (Exception var4) {
         }
      }

   }

   public synchronized String[] getActiveServiceNames() {
      Object[] var1 = this.activeServices.toArray();
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = (String)var1[var3];
      }

      return var2;
   }

   Object[] getRegisteredSingletonServices() {
      return this.nameToServiceMap.keySet().toArray();
   }

   Object[] getInternalSingletonServices() {
      return this.internalServiceMap.keySet().toArray();
   }

   public static SingletonServicesManager getInstance() {
      return THE_ONE;
   }

   public void startSingletonServices() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      SingletonServiceMBean[] var2 = var1.getSingletonServices();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         try {
            if ((var2[var3].getCluster() == null || var2[var3].getCluster().getName().equals(this.myCluster.getName())) && var2[var3].isCandidate(this.myserver)) {
               this.addConfiguredService(var2[var3].getName(), constructSingletonService(var2[var3].getClassName(), (ClassLoader)null));
            }
         } catch (DeploymentException var5) {
            if (DEBUG) {
               this.p("Couldn't deploy " + var2[var3].getClassName(), var5);
            }
         }
      }

   }

   public synchronized SingletonService getService(String var1) {
      return (SingletonService)this.nameToServiceMap.get(var1);
   }

   public boolean isServiceActive(String var1) {
      boolean var2 = false;
      SingletonService var3 = this.getService(var1);
      if (var3 != null && this.activeServices.contains(var1)) {
         var2 = true;
      }

      return var2;
   }

   public boolean isServiceRegistered(String var1) {
      boolean var2 = false;
      SingletonService var3 = this.getService(var1);
      if (var3 != null) {
         var2 = true;
      }

      return var2;
   }

   public synchronized void activateService(String var1) throws RemoteException {
      if (ManagementService.getRuntimeAccess(kernelId).getServerRuntime().isShuttingDown()) {
         String var16 = "Service named " + var1 + " cannot be activated as the server is shutting down";
         if (DEBUG) {
            this.p(var16);
         }

         throw new RemoteException(var16);
      } else {
         if (DEBUG) {
            this.p("Activating Singleton Service: " + var1);
         }

         SingletonService var2 = (SingletonService)this.nameToServiceMap.get(var1);
         if (var2 == null) {
            throw new RemoteException("No service named " + var1 + " found on this server.");
         } else {
            try {
               if (!(var2 instanceof MigratableGroup) && !this.manager.tryAcquire(var1)) {
                  if (DEBUG) {
                     this.p("Could not acquire lease for " + var1 + ", stopping activation.");
                  }

                  throw new RemoteException("Could not start service " + var1 + " because could not claim lease");
               }
            } catch (LeasingException var15) {
               if (DEBUG) {
                  this.p("Could not acquire lease for " + var1 + ", stopping activation.");
               }

               throw new RemoteException("Could not start service because could not claim lease." + var15, var15);
            }

            InheritableThreadContext var3 = null;

            try {
               if (this.nameToContextMap.get(var1) != null) {
                  var3 = (InheritableThreadContext)this.nameToContextMap.get(var1);
                  if (DEBUG) {
                     this.p("Setting up thread context for activation of " + var1);
                  }

                  var3.push();
               }

               var2.activate();
               if (DEBUG) {
                  this.p(var1 + " successfully activated.");
               }

               ClusterLogger.logActivatedSingletonService(var1);
               this.activeServices.add(var1);
            } catch (Throwable var13) {
               if (DEBUG) {
                  this.p("Failed to activate service " + var1 + " - " + var13.getMessage());
               }

               try {
                  this.manager.release(var1);
               } catch (LeasingException var12) {
                  throw new RemoteException("Error trying to release lease because of failed activation: " + var12, var12);
               }

               throw new RemoteException("Error trying to activate service " + var1 + ": " + var13, var13);
            } finally {
               if (var3 != null) {
                  var3.pop();
               }

            }

         }
      }
   }

   public synchronized void deactivateService(String var1) throws RemoteException {
      if (DEBUG) {
         this.p("Deactivating Singleton Service: " + var1);
      }

      SingletonService var2 = (SingletonService)this.nameToServiceMap.get(var1);
      if (var2 == null) {
         throw new RemoteException("No service named " + var1 + " found on this server.");
      } else {
         if (!(var2 instanceof MigratableGroup) && !this.internalServiceMap.containsKey(var1)) {
            try {
               this.manager.release(var1);
            } catch (LeasingException var9) {
               if (DEBUG) {
                  this.p("Could not release lease for " + var1 + ". Ignoring and continuing " + "with the deactivation, since we do not own the lease.");
               }
            }
         }

         InheritableThreadContext var3 = null;

         try {
            if (this.nameToContextMap.get(var1) != null) {
               var3 = (InheritableThreadContext)this.nameToContextMap.get(var1);
               if (DEBUG) {
                  this.p("Setting up thread context for deactivation of " + var1);
               }

               var3.push();
            }

            var2.deactivate();
            if (DEBUG) {
               this.p(var1 + " successfully deactivated.");
            }

            ClusterLogger.logDeactivatedSingletonService(var1);
            this.activeServices.remove(var1);
         } finally {
            if (var3 != null) {
               var3.pop();
            }

         }

      }
   }

   public synchronized void restartService(String var1) throws RemoteException {
      SingletonService var2 = (SingletonService)this.nameToServiceMap.get(var1);
      if (var2 == null) {
         throw new RemoteException("No service named " + var1 + " found on this server.");
      } else if (!(var2 instanceof MigratableGroup)) {
         throw new RemoteException("Only migratable targets can be restarted.");
      } else {
         InheritableThreadContext var3 = null;

         try {
            if (this.nameToContextMap.get(var1) != null) {
               var3 = (InheritableThreadContext)this.nameToContextMap.get(var1);
               if (DEBUG) {
                  this.p("Setting up thread context for deactivation of " + var1);
               }

               var3.push();
            }

            ((MigratableGroup)var2).restart();
            if (DEBUG) {
               this.p(var1 + " successfully restarted.");
            }
         } finally {
            if (var3 != null) {
               var3.pop();
            }

         }

      }
   }

   public synchronized void addConfiguredService(String var1, SingletonService var2) throws IllegalArgumentException {
      if (!this.started) {
         throw new IllegalArgumentException("Cannot add Singleton Service " + var1 + " as SingletonServicesManager not started. " + " Check if MigrationBasis for cluster is configured.");
      } else if (this.getService(var1) != null) {
         throw new IllegalArgumentException(var1 + " has been registered as a singleton service already.");
      } else {
         if (DEBUG) {
            this.p("Registering " + var1 + " on this server.");
         }

         ClusterLogger.logRegisteredSingletonService(var1);
         this.nameToServiceMap.put(var1, var2);
      }
   }

   public synchronized void addConfiguredService(String var1, SingletonService var2, InheritableThreadContext var3) throws IllegalArgumentException {
      this.addConfiguredService(var1, var2);
      this.nameToContextMap.put(var1, var3);
   }

   public synchronized void add(String var1, SingletonService var2) throws IllegalArgumentException {
      this.addConfiguredService(var1, var2);
      this.internalServiceMap.put(var1, var2);
      if (SingletonServicesBatchManager.theOne() != null && SingletonServicesBatchManager.theOne().hasStarted()) {
         SingletonServicesBatchManager.theOne().startService(var1);
      }

   }

   public synchronized void add(String var1, SingletonService var2, InheritableThreadContext var3) throws IllegalArgumentException {
      this.add(var1, var2);
      this.nameToContextMap.put(var1, var3);
   }

   public synchronized void remove(String var1) {
      if (DEBUG) {
         this.p("De-registering " + var1 + " on this server.");
      }

      try {
         this.deactivateService(var1);
      } catch (RemoteException var3) {
      }

      this.nameToServiceMap.remove(var1);
      this.nameToContextMap.remove(var1);
      ClusterLogger.logUnregisteredSingletonService(var1);
   }

   public static SingletonService constructSingletonService(String var0, ClassLoader var1) throws DeploymentException {
      try {
         Class var2 = null;
         if (var1 != null) {
            var2 = var1.loadClass(var0);
         } else {
            var2 = Class.forName(var0, true, Thread.currentThread().getContextClassLoader());
         }

         if (var2 == null) {
            throw new ClassNotFoundException();
         } else {
            Constructor var3 = var2.getDeclaredConstructor();
            SingletonService var4 = (SingletonService)var3.newInstance();
            return new SingletonServiceWrapper(var4);
         }
      } catch (ClassNotFoundException var5) {
         throw new DeploymentException("Could not find specified class: " + var0, var5);
      } catch (IllegalAccessException var6) {
         throw new DeploymentException("No permission to construct specified class: " + var0, var6);
      } catch (NoSuchMethodException var7) {
         throw new DeploymentException("Could not find constructor for: " + var0, var7);
      } catch (InvocationTargetException var8) {
         throw new DeploymentException("Could not construct specified class: " + var0, var8);
      } catch (InstantiationException var9) {
         throw new DeploymentException("Could not construct specified class: " + var0, var9);
      }
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getAddedObject() instanceof SingletonServiceMBean) {
            this.prepareSingletonBean((SingletonServiceMBean)var2[var3].getAddedObject());
         } else if (var2[var3].getRemovedObject() instanceof SingletonServiceMBean) {
            this.checkDeactivateSingletonBean((SingletonServiceMBean)var2[var3].getRemovedObject());
         }
      }

   }

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getAddedObject() instanceof SingletonServiceMBean) {
            this.activateSingletonBean((SingletonServiceMBean)var2[var3].getAddedObject());
         } else if (var2[var3].getRemovedObject() instanceof SingletonServiceMBean) {
            this.deactivateSingletonBean((SingletonServiceMBean)var2[var3].getRemovedObject());
         }
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   private void prepareSingletonBean(SingletonServiceMBean var1) throws BeanUpdateRejectedException {
      if (var1.getCluster() == null || var1.getCluster().getName().equals(this.myCluster.getName())) {
         if (this.getService(var1.getName()) != null) {
            throw new BeanUpdateRejectedException(var1.getName() + " has already been deployed.");
         } else {
            if (DEBUG) {
               this.p("Preparing " + var1 + " for deployment on this server.");
            }

            String var3 = var1.getClassName();

            try {
               SingletonService var4 = constructSingletonService(var3, (ClassLoader)null);
               this.constructedClassesMap.put(var3, var4);
            } catch (DeploymentException var5) {
               throw new BeanUpdateRejectedException("Error updating bean: " + var5, var5);
            }
         }
      }
   }

   private void activateSingletonBean(SingletonServiceMBean var1) throws BeanUpdateFailedException {
      if (var1.getCluster() == null || var1.getCluster().getName().equals(this.myCluster.getName())) {
         if (!this.isServiceActive(var1.getName())) {
            if (DEBUG) {
               this.p("Activating " + var1);
            }

            SingletonService var2 = (SingletonService)this.constructedClassesMap.get(var1.getClassName());

            try {
               if (var1.isCandidate(this.myserver)) {
                  this.addConfiguredService(var1.getName(), var2);
               }

            } catch (IllegalArgumentException var4) {
               throw new BeanUpdateFailedException("Could not activate bean: " + var4, var4);
            }
         }
      }
   }

   private void checkDeactivateSingletonBean(SingletonServiceMBean var1) {
   }

   private void deactivateSingletonBean(SingletonServiceMBean var1) {
      if (DEBUG) {
         this.p("Deactivating " + var1);
      }

      this.remove(var1.getName());
      if (var1.getCluster() == null || var1.getCluster().getName().equals(this.myCluster.getName())) {
         this.constructedClassesMap.remove(var1.getClassName());
      }
   }

   public void addActiveService(String var1) {
      SingletonService var2 = this.getService(var1);
      if (var2 != null && !this.activeServices.contains(var1)) {
         this.activeServices.add(var1);
      }
   }

   public void removeActiveService(String var1) {
      this.activeServices.remove(var1);
   }

   private void p(Object var1) {
      SingletonServicesDebugLogger.debug("SingletonServicesManager " + var1.toString());
   }

   private void p(Object var1, Exception var2) {
      SingletonServicesDebugLogger.debug("SingletonServicesManager " + var1.toString(), var2);
   }

   public abstract static class Util {
      private static final String DELIMITER = "#";

      public static String getAppscopedSingletonServiceName(String var0, String var1) {
         return var1 != null && var1.length() != 0 ? var0 + "#" + var1 : var0;
      }
   }
}
