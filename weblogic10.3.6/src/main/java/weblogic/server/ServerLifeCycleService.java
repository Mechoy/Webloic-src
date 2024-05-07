package weblogic.server;

import java.security.AccessController;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.CoherenceServerMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.SecurityHelper;
import weblogic.management.provider.DomainAccessSettable;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.CoherenceServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ServerLifeCycleService extends AbstractServerService {
   private static ServerLifeCycleService singleton;
   private Map<String, ServerLifeCycleRuntime> instances;
   private Map<String, CoherenceServerLifeCycleRuntime> instancesCoh;
   private boolean started;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public ServerLifeCycleService() {
      if (singleton != null) {
         throw new AssertionError("ServeruLifecycleServices can be initialized only once");
      } else {
         singleton = this;
      }
   }

   public static final ServerLifeCycleService getInstance() {
      SecurityHelper.assertIfNotKernel();
      if (singleton == null) {
         throw new AssertionError("ServeruLifecycleServices Not Yet Initialized");
      } else {
         return singleton;
      }
   }

   public final ServerLifeCycleRuntimeMBean[] getServerLifecycleRuntimes() {
      return (ServerLifeCycleRuntimeMBean[])this.instances.values().toArray(new ServerLifeCycleRuntimeMBean[this.instances.size()]);
   }

   public final ServerLifeCycleRuntimeMBean lookupServerLifecycleRuntime(String var1) {
      return (ServerLifeCycleRuntimeMBean)this.instances.get(var1);
   }

   public final CoherenceServerLifeCycleRuntimeMBean[] getCoherenceServerLifecycleRuntimes() {
      return (CoherenceServerLifeCycleRuntimeMBean[])this.instancesCoh.values().toArray(new CoherenceServerLifeCycleRuntimeMBean[this.instancesCoh.size()]);
   }

   public final CoherenceServerLifeCycleRuntimeMBean lookupCoherenceServerLifecycleRuntime(String var1) {
      return (CoherenceServerLifeCycleRuntimeMBean)this.instancesCoh.get(var1);
   }

   public void start() throws ServiceFailureException {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(KERNEL_ID);
      if (var1.isAdminServer()) {
         this.instances = Collections.synchronizedMap(new HashMap());
         ((DomainAccessSettable)ManagementService.getDomainAccess(KERNEL_ID)).setLifecycleService(this);
         DomainMBean var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain();
         ServerMBean[] var3 = var2.getServers();
         ServerLifeCycleRuntime.cleanupStore(var3);

         for(int var4 = 0; var4 < var3.length; ++var4) {
            try {
               this.createServerLifeCycleRuntime(var3[var4]);
            } catch (ManagementException var6) {
               throw new Error("Unexpected exception creating server lifecycle", var6);
            }
         }

         var2.addBeanUpdateListener(this.createBeanUpdateListener());
         this.startCoherenceServers(var2);
         this.started = true;
      }
   }

   private void startCoherenceServers(DomainMBean var1) {
      this.instancesCoh = Collections.synchronizedMap(new HashMap());
      CoherenceServerMBean[] var2 = var1.getCoherenceServers();
      CoherenceServerMBean[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         CoherenceServerMBean var6 = var3[var5];

         try {
            this.createCoherenceServerLifeCycleRuntime(var6);
         } catch (ManagementException var8) {
            throw new Error("Unexpected exception creating coherence server lifecycle", var8);
         }
      }

   }

   private BeanUpdateListener createBeanUpdateListener() {
      return new BeanUpdateListener() {
         public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
         }

         public void activateUpdate(BeanUpdateEvent var1) {
            BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               BeanUpdateEvent.PropertyUpdate var4 = var2[var3];
               String var5;
               ServerMBean var6;
               CoherenceServerMBean var10;
               switch (var4.getUpdateType()) {
                  case 2:
                     var5 = var4.getPropertyName();
                     if ("Servers".equals(var5)) {
                        var6 = (ServerMBean)var4.getAddedObject();

                        try {
                           ServerLifeCycleService.this.createServerLifeCycleRuntime(var6);
                        } catch (ManagementException var9) {
                           throw new Error("Unexpected exception creating server lifecycle", var9);
                        }
                     } else if ("CoherenceServers".equals(var5)) {
                        var10 = (CoherenceServerMBean)var4.getAddedObject();

                        try {
                           ServerLifeCycleService.this.createCoherenceServerLifeCycleRuntime(var10);
                        } catch (ManagementException var8) {
                           throw new Error("Unexpected exception creating server lifecycle", var8);
                        }
                     }
                     break;
                  case 3:
                     var5 = var4.getPropertyName();
                     if ("Servers".equals(var5)) {
                        var6 = (ServerMBean)var4.getRemovedObject();
                        ServerLifeCycleService.this.destroyServerLifecycleRuntime(var6.getName());
                     } else if ("CoherenceServers".equals(var5)) {
                        var10 = (CoherenceServerMBean)var4.getRemovedObject();
                        ServerLifeCycleService.this.destroyCoherenceServerLifecycleRuntime(var10.getName());
                     }
               }
            }

         }

         public void rollbackUpdate(BeanUpdateEvent var1) {
         }
      };
   }

   private void createServerLifeCycleRuntime(ServerMBean var1) throws ManagementException {
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(KERNEL_ID);
      ServerLifeCycleRuntime var3 = new ServerLifeCycleRuntime(var1);
      this.instances.put(var1.getName(), var3);
   }

   private void createCoherenceServerLifeCycleRuntime(CoherenceServerMBean var1) throws ManagementException {
      CoherenceServerLifeCycleRuntime var2 = new CoherenceServerLifeCycleRuntime(var1);
      this.instancesCoh.put(var1.getName(), var2);
   }

   private void destroyServerLifecycleRuntime(String var1) {
      ServerLifeCycleRuntime var2 = (ServerLifeCycleRuntime)this.instances.get(var1);
      var2.cleanup();
      this.instances.remove(var1);
      ManagementService.getDomainAccess(KERNEL_ID).unregister(var2);
   }

   private void destroyCoherenceServerLifecycleRuntime(String var1) {
      CoherenceServerLifeCycleRuntime var2 = (CoherenceServerLifeCycleRuntime)this.instancesCoh.get(var1);
      var2.cleanup();
      this.instancesCoh.remove(var1);
      ManagementService.getDomainAccess(KERNEL_ID).unregister(var2);
   }

   public void stop() throws ServiceFailureException {
   }

   public void halt() throws ServiceFailureException {
   }

   public static boolean isStarted() {
      return singleton != null ? singleton.started : false;
   }
}
