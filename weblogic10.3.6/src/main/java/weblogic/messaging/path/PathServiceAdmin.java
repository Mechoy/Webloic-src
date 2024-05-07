package weblogic.messaging.path;

import java.security.AccessController;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.PathServiceMBean;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.utils.GenericAdminHandler;
import weblogic.messaging.path.helper.PathHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.ServiceFailureException;
import weblogic.store.PersistentStoreManager;
import weblogic.store.xa.PersistentStoreXA;

public class PathServiceAdmin implements GenericAdminHandler {
   private PathServiceMap pathMap;
   private boolean running;
   private PathServiceRuntimeDelegate psRuntimeDelegate;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public PathServiceAdmin() {
      PathService.getService().setPathServiceAdmin(this);
   }

   public PathServiceMap getPathMap() {
      return this.pathMap;
   }

   public boolean isRunning() {
      return this.running;
   }

   public void prepare(DeploymentMBean var1) throws DeploymentException {
   }

   public synchronized void activate(DeploymentMBean var1) throws DeploymentException {
      if (var1 instanceof PathServiceMBean) {
         PathServiceMBean var2 = (PathServiceMBean)var1;
         PersistentStoreMBean var3 = var2.getPersistentStore();
         PersistentStoreXA var4;
         if (var3 == null) {
            var4 = (PersistentStoreXA)PersistentStoreManager.getManager().getDefaultStore();
         } else {
            var4 = (PersistentStoreXA)PersistentStoreManager.getManager().getStore(var3.getName());
         }

         String var5;
         if (var3 == null) {
            var5 = "DefaultStore";
         } else {
            var5 = var3.getName();
         }

         assert var4 != null;

         this.pathMap = new PathServiceMap(PathHelper.DEFAULT_PATH_SERVICE_JNDI, var4, PathHelper.jmsOrderExceptionAdapter());
         RuntimeAccess var6 = ManagementService.getRuntimeAccess(KERNEL_ID);
         String var7 = var1.getName();

         try {
            this.psRuntimeDelegate = new PathServiceRuntimeDelegate(var7, this.pathMap);
         } catch (ManagementException var10) {
            throw new DeploymentException(var10.getMessage(), var10);
         }

         try {
            this.doJNDIOperation(true, PathHelper.DEFAULT_PATH_SERVICE_JNDI, new AsyncMapImpl(PathHelper.DEFAULT_PATH_SERVICE_JNDI, this.pathMap, PathHelper.jmsOrderExceptionAdapter()));
         } catch (NamingException var9) {
            throw new DeploymentException(var9.getMessage(), var9);
         }

         var6.getServerRuntime().setPathServiceRuntime(this.psRuntimeDelegate);
         PathLogger.logPathStarted(var6.getServer().getName(), PathHelper.DEFAULT_PATH_SERVICE_JNDI, var5);
         this.running = true;
      }

   }

   public synchronized void deactivate(DeploymentMBean var1) throws UndeploymentException {
      if (this.running) {
         this.running = false;
         this.psRuntimeDelegate = null;
         this.pathMap = null;

         try {
            ManagementService.getRuntimeAccess(KERNEL_ID).getServerRuntime().setPathServiceRuntime(this.psRuntimeDelegate);

            try {
               this.doJNDIOperation(false, PathHelper.DEFAULT_PATH_SERVICE_JNDI, (Object)null);
            } catch (NamingException var8) {
               throw new ServiceFailureException(var8.getMessage(), var8);
            } finally {
               PathHelper.manager().unRegister(PathHelper.DEFAULT_PATH_SERVICE_JNDI);
            }

         } catch (ServiceFailureException var10) {
            throw new UndeploymentException(var10);
         }
      }
   }

   public void unprepare(DeploymentMBean var1) throws UndeploymentException {
   }

   private void doJNDIOperation(boolean var1, String var2, Object var3) throws NamingException {
      Environment var4 = new Environment();
      var4.setCreateIntermediateContexts(true);
      var4.setReplicateBindings(true);
      Context var5 = var4.getInitialContext();
      SecurityServiceManager.pushSubject(KERNEL_ID, KERNEL_ID);

      try {
         if (var1) {
            var5.bind(var2, var3);
         } else {
            var5.unbind(var2);
         }
      } finally {
         SecurityServiceManager.popSubject(KERNEL_ID);
      }

   }
}
