package weblogic.application.internal;

import java.io.File;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.application.DeploymentManager;
import weblogic.application.MBeanFactory;
import weblogic.application.ModuleListener;
import weblogic.j2ee.J2EELogger;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.collections.ConcurrentHashMap;

public final class DeploymentManagerImpl extends DeploymentManager {
   private static final boolean TIMES = Debug.getCategory("weblogic.application.times").isEnabled();
   private static final ApplicationFactoryManager afm = ApplicationFactoryManager.getApplicationFactoryManager();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final Map<String, Deployment> deployments = new ConcurrentHashMap();
   private final MBeanFactory mbeanFactory = MBeanFactory.getMBeanFactory();
   private List<ModuleListener> listeners = Collections.emptyList();

   private String getId(BasicDeploymentMBean var1) {
      return var1 instanceof AppDeploymentMBean ? ((AppDeploymentMBean)var1).getApplicationIdentifier() : var1.getName();
   }

   public DeploymentManagerImpl() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);

      assert var1 != null;

      assert var1.getServer() != null;

      var1.getServer().getStagingDirectoryName();
   }

   public Deployment createDeployment(BasicDeploymentMBean var1, File var2) throws DeploymentException {
      Iterator var3 = afm.getDeploymentFactories();

      Deployment var5;
      do {
         if (!var3.hasNext()) {
            Loggable var6 = J2EELogger.logInvalidApplicationLoggable(var2.getAbsolutePath());
            throw new DeploymentException(var6.getMessage());
         }

         DeploymentFactory var4 = (DeploymentFactory)var3.next();
         var5 = null;
         if (var1 instanceof AppDeploymentMBean) {
            var5 = var4.createDeployment((AppDeploymentMBean)var1, var2);
         } else {
            var5 = var4.createDeployment((SystemResourceMBean)var1, var2);
         }
      } while(var5 == null);

      Object var7;
      if (TIMES) {
         var7 = this.isBackgroundDeployment(var1) ? new BackgroundDeployment(new DeploymentTimer(var5)) : var5;
      } else {
         var7 = this.isBackgroundDeployment(var1) ? new BackgroundDeployment(var5) : var5;
      }

      var7 = TIMES ? new DeploymentTimer((Deployment)var7) : new DeploymentStateChecker((Deployment)var7);
      this.deployments.put(this.getId(var1), var7);
      return (Deployment)var7;
   }

   public Deployment findDeployment(BasicDeploymentMBean var1) {
      return (Deployment)this.deployments.get(this.getId(var1));
   }

   public Deployment findDeployment(String var1) {
      return (Deployment)this.deployments.get(var1);
   }

   public Deployment removeDeployment(BasicDeploymentMBean var1) {
      return (Deployment)this.deployments.remove(this.getId(var1));
   }

   public Deployment removeDeployment(String var1) {
      return (Deployment)this.deployments.remove(var1);
   }

   public Iterator<Deployment> getDeployments() {
      return this.deployments.values().iterator();
   }

   public MBeanFactory getMBeanFactory() {
      return this.mbeanFactory;
   }

   public synchronized void addModuleListener(ModuleListener var1) {
      ArrayList var2 = new ArrayList(this.listeners.size() + 1);
      var2.addAll(this.listeners);
      var2.add(var1);
      this.listeners = var2;
   }

   public synchronized void removeModuleListener(ModuleListener var1) {
      ArrayList var2 = new ArrayList(this.listeners.size() - 1);
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ModuleListener var4 = (ModuleListener)var3.next();
         if (!var1.equals(var4)) {
            var2.add(var4);
         }
      }

      this.listeners = var2;
   }

   public Iterator<ModuleListener> getModuleListeners() {
      return this.listeners.iterator();
   }

   private boolean isBackgroundDeployment(BasicDeploymentMBean var1) {
      if (!(var1 instanceof AppDeploymentMBean)) {
         return false;
      } else if (!((AppDeploymentMBean)var1).isBackgroundDeployment()) {
         return false;
      } else {
         String var2 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getState();
         return !var2.equals("ADMIN") && !var2.equals("RUNNING");
      }
   }
}
