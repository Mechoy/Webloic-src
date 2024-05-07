package weblogic.deploy.event;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import weblogic.application.ApplicationException;
import weblogic.application.ApplicationVersionLifecycleEvent;
import weblogic.application.ApplicationVersionLifecycleListener;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class ApplicationVersionLifecycleListenerAdapter implements VetoableDeploymentListener, DeploymentEventListener {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final AuthenticatedSubject ANONYMOUS_ID = SubjectUtils.getAnonymousSubject();
   private String appId;
   private ApplicationVersionLifecycleListener[] appListeners;

   public ApplicationVersionLifecycleListenerAdapter(String var1, ApplicationVersionLifecycleListener[] var2) throws DeploymentException {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("AppVersionLifecycleAdapter create for " + var1 + ", listeners=" + Arrays.asList(var2));
      }

      this.appId = var1;
      this.appListeners = var2;
      if (var2.length > 0) {
         this.registerDeploymentEventListeners(ApplicationVersionUtils.getApplicationName(var1));
      } else if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("AppVersionLifecycleAdapter no listeners for " + var1);
      }

   }

   private void registerDeploymentEventListeners(String var1) throws DeploymentException {
      if (var1 != null) {
         DeploymentEventManager.addVetoableDeploymentListener(var1, this);
         DeploymentEventManager.addDeploymentEventListener(var1, this);
      } else if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("AppVersionLifecycleAdapter null appName for " + this.appId);
      }

   }

   public String toString() {
      return "ApplicationVersionLifecycleListenerAdapter[" + this.appId + "]";
   }

   public void cleanup() {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("AppVersionLifecycleAdapter cleanup for " + this.appId);
      }

      this.unregisterDeploymentEventListeners();
   }

   private void unregisterDeploymentEventListeners() {
      DeploymentEventManager.removeVetoableDeploymentListener(this);
      DeploymentEventManager.removeDeploymentEventListener(this);
   }

   public void vetoableApplicationActivate(VetoableDeploymentEvent var1) throws DeploymentVetoException {
   }

   public void vetoableApplicationDeploy(VetoableDeploymentEvent var1) throws DeploymentVetoException {
      this.invokeAppLifecycleListeners(ApplicationVersionLifecycleListenerAdapter.ApplicationLifecycleAction.PRE_DEPLOY, var1, true, false);
   }

   public void vetoableApplicationUndeploy(VetoableDeploymentEvent var1) throws DeploymentVetoException {
      this.invokeAppLifecycleListeners(ApplicationVersionLifecycleListenerAdapter.ApplicationLifecycleAction.PRE_UNDEPLOY, var1, true, true);
   }

   public void applicationActivated(DeploymentEvent var1) {
   }

   public void applicationDeployed(DeploymentEvent var1) {
      try {
         this.invokeAppLifecycleListeners(ApplicationVersionLifecycleListenerAdapter.ApplicationLifecycleAction.POST_DEPLOY, var1, false, false);
      } catch (DeploymentVetoException var3) {
      }

   }

   public void applicationRedeployed(DeploymentEvent var1) {
      try {
         this.invokeAppLifecycleListeners(ApplicationVersionLifecycleListenerAdapter.ApplicationLifecycleAction.POST_DEPLOY, var1, false, false);
      } catch (DeploymentVetoException var3) {
      }

   }

   public void applicationDeleted(DeploymentEvent var1) {
      try {
         this.invokeAppLifecycleListeners(ApplicationVersionLifecycleListenerAdapter.ApplicationLifecycleAction.POST_DELETE, var1, false, true);
      } catch (DeploymentVetoException var3) {
      }

   }

   private void invokeAppLifecycleListeners(final ApplicationLifecycleAction var1, final BaseDeploymentEvent var2, final boolean var3, final boolean var4) throws DeploymentVetoException {
      Object var5 = SecurityServiceManager.runAs(KERNEL_ID, ANONYMOUS_ID, new PrivilegedAction() {
         public Object run() {
            Throwable var1x = null;
            Iterator var2x = ApplicationVersionLifecycleListenerAdapter.this.getIterator(var4);

            while(var2x.hasNext()) {
               try {
                  ApplicationVersionLifecycleListener var3x = (ApplicationVersionLifecycleListener)var2x.next();
                  ApplicationVersionLifecycleEvent var6 = ApplicationVersionLifecycleListenerAdapter.this.getAppLifecycleEvent(var2);
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("AppVersionLifecycleAdapter invoke " + var1 + " for " + var3x + ", " + var2);
                  }

                  if (var3x != null && var2 != null) {
                     var1.invoke(var3x, var6);
                  }
               } catch (Throwable var5) {
                  if (var3) {
                     return var5;
                  }

                  Throwable var4x = null;
                  if (var1x != null && var5.getCause() != null) {
                     var4x = new Throwable(var5.getMessage());
                     var4x.initCause(var1x);
                  } else {
                     var4x = new Throwable(var5);
                  }

                  var1x = var4x;
               }
            }

            return var1x;
         }
      });
      if (var5 instanceof Throwable) {
         if (var3) {
            throw new DeploymentVetoException((Throwable)var5);
         } else {
            throw new ApplicationLifecycleException((Throwable)var5);
         }
      }
   }

   private Iterator getIterator(boolean var1) {
      return var1 ? new Iterator() {
         private ListIterator listIter;

         {
            this.listIter = Arrays.asList(ApplicationVersionLifecycleListenerAdapter.this.appListeners).listIterator(ApplicationVersionLifecycleListenerAdapter.this.appListeners.length);
         }

         public boolean hasNext() {
            return this.listIter.hasPrevious();
         }

         public Object next() {
            return this.listIter.previous();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      } : Arrays.asList(this.appListeners).iterator();
   }

   private ApplicationVersionLifecycleEvent getAppLifecycleEvent(BaseDeploymentEvent var1) {
      AppDeploymentMBean var2 = var1.getAppDeployment();
      return new ApplicationVersionLifecycleEvent(this.appId, var2.getName());
   }

   public class ApplicationLifecycleException extends RuntimeException {
      public ApplicationLifecycleException(Throwable var2) {
         super(DeploymentServiceLogger.logAppListenerExceptionLoggable().getMessage(), var2);
      }
   }

   private abstract static class ApplicationLifecycleAction {
      static final ApplicationLifecycleAction PRE_DEPLOY = new ApplicationLifecycleAction("preDeploy") {
         void invoke(ApplicationVersionLifecycleListener var1, ApplicationVersionLifecycleEvent var2) throws ApplicationException {
            var1.preDeploy(var2);
         }
      };
      static final ApplicationLifecycleAction POST_DEPLOY = new ApplicationLifecycleAction("postDeploy") {
         void invoke(ApplicationVersionLifecycleListener var1, ApplicationVersionLifecycleEvent var2) throws ApplicationException {
            var1.postDeploy(var2);
         }
      };
      static final ApplicationLifecycleAction PRE_UNDEPLOY = new ApplicationLifecycleAction("preUndeploy") {
         void invoke(ApplicationVersionLifecycleListener var1, ApplicationVersionLifecycleEvent var2) throws ApplicationException {
            var1.preUndeploy(var2);
         }
      };
      static final ApplicationLifecycleAction POST_DELETE = new ApplicationLifecycleAction("postDelete") {
         void invoke(ApplicationVersionLifecycleListener var1, ApplicationVersionLifecycleEvent var2) throws ApplicationException {
            var1.postDelete(var2);
         }
      };
      private String name;

      ApplicationLifecycleAction(String var1) {
         this.name = var1;
      }

      public String toString() {
         return this.name;
      }

      String getName() {
         return this.name;
      }

      abstract void invoke(ApplicationVersionLifecycleListener var1, ApplicationVersionLifecycleEvent var2) throws ApplicationException;
   }
}
