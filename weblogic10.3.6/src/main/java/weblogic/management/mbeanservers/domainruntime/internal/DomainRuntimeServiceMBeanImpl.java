package weblogic.management.mbeanservers.domainruntime.internal;

import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.jmx.RemoteRuntimeException;
import weblogic.management.mbeanservers.Service;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.internal.DomainServiceImpl;
import weblogic.management.mbeanservers.runtime.RuntimeServiceMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.EditFailedException;
import weblogic.management.provider.EditNotEditorException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.MigratableServiceCoordinatorRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class DomainRuntimeServiceMBeanImpl extends DomainServiceImpl implements DomainRuntimeServiceMBean {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXDomain");
   private final MBeanServerConnectionManager connectionManager;
   private final String serverName;
   private final DomainRuntimeMBean domainRuntime;
   private static final ObjectName RUNTIME_SERVICE;
   private RuntimeServicesManager runtimeServicesManager;
   private DomainAccess domainAccess = null;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   DomainRuntimeServiceMBeanImpl(MBeanServerConnectionManager var1, DomainRuntimeMBean var2) {
      super("DomainRuntimeService", DomainRuntimeServiceMBean.class.getName(), (Service)null);
      this.connectionManager = var1;
      this.domainRuntime = var2;
      this.runtimeServicesManager = new RuntimeServicesManager();
      this.domainAccess = ManagementService.getDomainAccess(kernelId);
      var1.addCallback(new MBeanServerConnectionManager.MBeanServerConnectionListener() {
         public synchronized void connect(String var1, MBeanServerConnection var2) {
            DomainRuntimeServiceMBeanImpl.this.runtimeServicesManager.addConnection(var1, var2);
         }

         public synchronized void disconnect(String var1) {
            DomainRuntimeServiceMBeanImpl.this.runtimeServicesManager.removeConnection(var1);
         }
      });
      RuntimeAccess var3 = ManagementService.getRuntimeAccess(kernelId);
      this.serverName = var3.getServerName();
   }

   public DomainMBean getDomainConfiguration() {
      return DomainRuntimeServiceMBeanImpl.SINGLETON.getInstance().getDomainConfig();
   }

   public DomainMBean getDomainPending() {
      return DomainRuntimeServiceMBeanImpl.SINGLETON.getInstance().getDomainEdit();
   }

   public ServerMBean findServerConfiguration(String var1) {
      RuntimeServiceMBean var2 = this.getRuntimeServiceMBean(var1);
      return var2 == null ? null : var2.getServerConfiguration();
   }

   public DomainMBean findDomainConfiguration(String var1) {
      RuntimeServiceMBean var2 = this.getRuntimeServiceMBean(var1);
      return var2 == null ? null : var2.getDomainConfiguration();
   }

   public String getServerName() {
      return this.serverName;
   }

   public DomainRuntimeMBean getDomainRuntime() {
      return this.domainRuntime;
   }

   public RuntimeMBean[] findRuntimes(DescriptorBean var1) {
      ArrayList var2 = new ArrayList();
      RuntimeServiceMBean[] var3 = this.runtimeServicesManager.getRuntimeServices();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         RuntimeServiceMBean var5 = var3[var4];
         Object var6 = null;

         try {
            var5.findRuntime(var1);
         } catch (RemoteRuntimeException var8) {
            if (debug.isDebugEnabled()) {
               debug.debug("Exception finding runtimes: ", var8);
            }
         }

         if (var6 != null) {
            var2.add(var6);
         }
      }

      return (RuntimeMBean[])((RuntimeMBean[])var2.toArray(new RuntimeMBean[var2.size()]));
   }

   public RuntimeMBean findRuntime(DescriptorBean var1, String var2) {
      RuntimeServiceMBean var3 = this.getRuntimeServiceMBean(var2);
      if (var3 == null) {
         return null;
      } else {
         try {
            return var3.findRuntime(var1);
         } catch (RemoteRuntimeException var5) {
            if (debug.isDebugEnabled()) {
               debug.debug("Exception finding runtime for config and server:", var5);
            }

            return null;
         }
      }
   }

   public DescriptorBean findConfiguration(RuntimeMBean var1) {
      MBeanServerInvocationHandler var2 = (MBeanServerInvocationHandler)Proxy.getInvocationHandler(var1);
      String var3 = this.connectionManager.lookupServerName(var2._getConnection());
      if (var3 == null) {
         return null;
      } else {
         RuntimeServiceMBean var4 = this.getRuntimeServiceMBean(var3);
         if (var4 == null) {
            return null;
         } else {
            try {
               return var4.findConfiguration(var1);
            } catch (RemoteRuntimeException var6) {
               if (debug.isDebugEnabled()) {
                  debug.debug("Exception finding configuration: ", var6);
               }

               return null;
            }
         }
      }
   }

   public ServerRuntimeMBean[] getServerRuntimes() {
      ArrayList var1 = new ArrayList();
      RuntimeServiceMBean[] var2 = this.runtimeServicesManager.getRuntimeServices();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         RuntimeServiceMBean var4 = var2[var3];

         try {
            ServerRuntimeMBean var5 = var4.getServerRuntime();
            if (var5 != null) {
               var1.add(var5);
            }
         } catch (RemoteRuntimeException var6) {
            if (debug.isDebugEnabled()) {
               debug.debug("Exception finding server runtimes: ", var6);
            }
         }
      }

      return (ServerRuntimeMBean[])((ServerRuntimeMBean[])var1.toArray(new ServerRuntimeMBean[var1.size()]));
   }

   public ServerRuntimeMBean lookupServerRuntime(String var1) {
      ServerRuntimeMBean var2 = null;
      if (var1 == null) {
         return var2;
      } else {
         RuntimeServiceMBean var3 = this.runtimeServicesManager.get(var1);
         if (var3 != null) {
            try {
               var2 = var3.getServerRuntime();
            } catch (RemoteRuntimeException var5) {
               if (debug.isDebugEnabled()) {
                  debug.debug("Exception looking up runtime: ", var5);
               }
            }
         }

         return var2;
      }
   }

   public Service findService(String var1, String var2, String var3) {
      if (var3 == null) {
         return (Service)this.domainAccess.findService(var1, var2);
      } else {
         RuntimeServiceMBean var4 = this.getRuntimeServiceMBean(var3);
         return var4 != null ? var4.findService(var1, var2) : null;
      }
   }

   public Service[] getServices(String var1) {
      if (var1 == null) {
         weblogic.management.provider.Service[] var4 = this.domainAccess.getRootServices();
         Service[] var3 = new Service[var4.length];
         System.arraycopy(var4, 0, var3, 0, var4.length);
         return var3;
      } else {
         RuntimeServiceMBean var2 = this.getRuntimeServiceMBean(var1);
         return var2 != null ? var2.getServices() : null;
      }
   }

   private RuntimeServiceMBean getRuntimeServiceMBean(String var1) {
      RuntimeServiceMBean var2 = this.runtimeServicesManager.get(var1);
      return var2;
   }

   public MigratableServiceCoordinatorRuntimeMBean lookupMigratableServiceCoordinatorRuntime() {
      return ManagementService.getDomainAccess(kernelId).getMigratableServiceCoordinatorRuntime();
   }

   public MigratableServiceCoordinatorRuntimeMBean getMigratableServiceCoordinatorRuntime() {
      return ManagementService.getDomainAccess(kernelId).getMigratableServiceCoordinatorRuntime();
   }

   static {
      try {
         RUNTIME_SERVICE = new ObjectName(RuntimeServiceMBean.OBJECT_NAME);
      } catch (MalformedObjectNameException var1) {
         throw new Error(var1);
      }
   }

   private class RuntimeServicesManager {
      private Map runtimeServicesByName = new ConcurrentHashMap();
      private RuntimeServiceMBean[] runtimeServicesArray;
      private boolean runtimeServicesArrayUptodate = true;
      private Map unresolvedConnections = null;

      RuntimeServicesManager() {
         this.unresolvedConnections = new ConcurrentHashMap();
      }

      RuntimeServiceMBean get(String var1) {
         this.checkUnresolved();
         return (RuntimeServiceMBean)this.runtimeServicesByName.get(var1);
      }

      synchronized void addConnection(String var1, MBeanServerConnection var2) {
         if (DomainRuntimeServiceMBeanImpl.debug.isDebugEnabled()) {
            DomainRuntimeServiceMBeanImpl.debug.debug("Added MBeanServerConnection " + var1 + " " + var2);
         }

         this.unresolvedConnections.put(var1, var2);
         this.runtimeServicesArrayUptodate = false;
      }

      synchronized void removeConnection(String var1) {
         if (DomainRuntimeServiceMBeanImpl.debug.isDebugEnabled()) {
            DomainRuntimeServiceMBeanImpl.debug.debug("Removed MBeanServerConnection " + var1);
         }

         this.runtimeServicesByName.remove(var1);
         this.unresolvedConnections.remove(var1);
         Collection var2 = this.runtimeServicesByName.values();
         this.runtimeServicesArray = (RuntimeServiceMBean[])((RuntimeServiceMBean[])var2.toArray(new RuntimeServiceMBean[var2.size()]));
      }

      RuntimeServiceMBean[] getRuntimeServices() {
         this.checkUnresolved();
         return this.runtimeServicesArray;
      }

      private void checkUnresolved() {
         if (!this.runtimeServicesArrayUptodate) {
            this.resolve();
         }

      }

      private synchronized void resolve() {
         if (!this.runtimeServicesArrayUptodate) {
            Iterator var1 = this.unresolvedConnections.entrySet().iterator();

            while(var1.hasNext()) {
               Map.Entry var2 = (Map.Entry)var1.next();
               this.resolveRuntimeServiceProxy((MBeanServerConnection)var2.getValue(), (String)var2.getKey());
            }

            Collection var3 = this.runtimeServicesByName.values();
            this.runtimeServicesArray = (RuntimeServiceMBean[])((RuntimeServiceMBean[])var3.toArray(new RuntimeServiceMBean[var3.size()]));
            this.runtimeServicesArrayUptodate = true;
         }

      }

      private synchronized RuntimeServiceMBean resolveRuntimeServiceProxy(MBeanServerConnection var1, String var2) {
         RuntimeServiceMBean var3;
         try {
            var3 = (RuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var1, DomainRuntimeServiceMBeanImpl.RUNTIME_SERVICE);
         } catch (Throwable var5) {
            if (DomainRuntimeServiceMBeanImpl.debug.isDebugEnabled()) {
               DomainRuntimeServiceMBeanImpl.debug.debug("Exception resolve runtime: ", var5);
            }

            return null;
         }

         this.runtimeServicesByName.put(var2, var3);
         this.unresolvedConnections.remove(var2);
         return var3;
      }
   }

   private static class SINGLETON {
      private DomainMBean domainConfig = null;
      private DomainMBean domainEdit = null;
      private static SINGLETON instance = null;

      public static SINGLETON getInstance() {
         if (instance == null) {
            instance = new SINGLETON();
         }

         return instance;
      }

      private SINGLETON() {
         EditAccess var1 = ManagementServiceRestricted.getEditAccess(DomainRuntimeServiceMBeanImpl.kernelId);

         try {
            this.domainConfig = var1.getCurrentDomainBean();
            this.domainEdit = var1.getDomainBeanWithoutLock();
         } catch (EditFailedException var3) {
            throw new AssertionError(var3);
         } catch (EditNotEditorException var4) {
            throw new AssertionError(var4);
         }
      }

      public DomainMBean getDomainConfig() {
         return this.domainConfig;
      }

      public DomainMBean getDomainEdit() {
         return this.domainEdit;
      }
   }
}
