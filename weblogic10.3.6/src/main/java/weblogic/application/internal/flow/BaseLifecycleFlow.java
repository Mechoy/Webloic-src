package weblogic.application.internal.flow;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;
import weblogic.application.ApplicationVersionLifecycleListener;
import weblogic.application.DeploymentOperationType;
import weblogic.application.WrappedDeploymentException;
import weblogic.application.internal.Flow;
import weblogic.application.internal.FlowContext;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.application.utils.TargetUtils;
import weblogic.j2ee.J2EELogger;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.management.DeploymentException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.GenericClassLoader;

abstract class BaseLifecycleFlow extends BaseFlow implements Flow {
   protected static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean isAppDeployedLocally = true;
   private final PreStartAction preStart = new PreStartAction();
   private final PostStartAction postStart = new PostStartAction();
   private final PreStopAction preStop = new PreStopAction();
   private final PostStopAction postStop = new PostStopAction();

   public BaseLifecycleFlow(FlowContext var1) {
      super(var1);
      this.isAppDeployedLocally = TargetUtils.isDeployedLocally(var1.getBasicDeploymentMBean().getTargets());
   }

   private ApplicationLifecycleEvent createEvent() {
      return new ApplicationLifecycleEvent(this.appCtx, DeploymentOperationType.valueOf(this.appCtx.getDeploymentOperation()), this.appCtx.isStaticDeploymentOperation());
   }

   protected void preStart() throws DeploymentException {
      if (this.isAppDeployedLocally) {
         this.preStart.invoke();
      }

   }

   protected void postStart() throws DeploymentException {
      if (this.isAppDeployedLocally) {
         javaURLContextFactory.pushContext(this.appCtx.getRootContext());

         try {
            this.postStart.invoke();
         } finally {
            javaURLContextFactory.popContext();
         }
      }

   }

   protected void preStop() throws DeploymentException {
      if (this.isAppDeployedLocally) {
         javaURLContextFactory.pushContext(this.appCtx.getRootContext());

         try {
            this.preStop.invoke();
         } finally {
            javaURLContextFactory.popContext();
         }
      }

   }

   protected void postStop() throws DeploymentException {
      if (this.isAppDeployedLocally) {
         this.postStop.invoke();
      }

   }

   private class PostStopAction extends AbstractStopAction implements PrivilegedAction {
      private PostStopAction() {
         super(null);
      }

      public Object run() {
         try {
            this.listener.postStop(BaseLifecycleFlow.this.createEvent());
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }

      // $FF: synthetic method
      PostStopAction(Object var2) {
         this();
      }
   }

   private class PreStopAction extends AbstractStopAction implements PrivilegedAction {
      private PreStopAction() {
         super(null);
      }

      public Object run() {
         try {
            this.listener.preStop(BaseLifecycleFlow.this.createEvent());
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }

      // $FF: synthetic method
      PreStopAction(Object var2) {
         this();
      }
   }

   private abstract class AbstractStopAction extends LifecycleListenerAction implements PrivilegedAction {
      private AbstractStopAction() {
         super(null);
      }

      protected boolean fail() {
         return false;
      }

      protected Iterator getListeners() {
         ArrayList var1 = new ArrayList();
         ApplicationLifecycleListener[] var2 = BaseLifecycleFlow.this.appCtx.getApplicationListeners();

         for(int var3 = var2.length - 1; var3 >= 0; --var3) {
            var1.add(var2[var3]);
         }

         return var1.iterator();
      }

      // $FF: synthetic method
      AbstractStopAction(Object var2) {
         this();
      }
   }

   private class PostStartAction extends AbstractStartAction implements PrivilegedAction {
      private PostStartAction() {
         super(null);
      }

      public Object run() {
         try {
            this.listener.postStart(BaseLifecycleFlow.this.createEvent());
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }

      // $FF: synthetic method
      PostStartAction(Object var2) {
         this();
      }
   }

   private class PreStartAction extends AbstractStartAction implements PrivilegedAction {
      private PreStartAction() {
         super(null);
      }

      public Object run() {
         try {
            this.listener.preStart(BaseLifecycleFlow.this.createEvent());
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }

      // $FF: synthetic method
      PreStartAction(Object var2) {
         this();
      }
   }

   protected class CreateMainClassAction extends BaseAction implements PrivilegedAction {
      private final GenericClassLoader gcl;
      private final String className;

      CreateMainClassAction(GenericClassLoader var2, String var3) {
         super(null);
         this.gcl = var2;
         this.className = var3;
      }

      public Object run() {
         Class var1 = null;

         try {
            var1 = Class.forName(this.className, false, this.gcl);
            return var1 == null ? new DeploymentException("Cannot load ApplicationLifecycleListener class " + this.className) : var1;
         } catch (ClassNotFoundException var3) {
            return new DeploymentException(var3);
         }
      }
   }

   private abstract class AbstractStartAction extends LifecycleListenerAction implements PrivilegedAction {
      private AbstractStartAction() {
         super(null);
      }

      protected boolean fail() {
         return true;
      }

      protected Iterator getListeners() {
         List var1 = Arrays.asList(BaseLifecycleFlow.this.appCtx.getApplicationListeners());
         return var1.iterator();
      }

      // $FF: synthetic method
      AbstractStartAction(Object var2) {
         this();
      }
   }

   private abstract class LifecycleListenerAction implements PrivilegedAction {
      protected ApplicationLifecycleListener listener;

      private LifecycleListenerAction() {
         this.listener = null;
      }

      Object invoke() throws DeploymentException {
         ErrorCollectionException var1 = null;
         Iterator var2 = this.getListeners();

         while(var2.hasNext()) {
            this.listener = (ApplicationLifecycleListener)var2.next();
            AuthenticatedSubject var3 = BaseLifecycleFlow.this.appCtx.getAppListenerIdentity(this.listener);
            if (var3 == null) {
               var3 = BaseLifecycleFlow.this.appCtx.getDeploymentInitiator();
            }

            Object var4 = SecurityServiceManager.runAs(BaseLifecycleFlow.KERNEL_ID, var3, this);
            if (var4 != null) {
               if (this.fail()) {
                  this.throwsException(var4);
               }

               if (var1 == null) {
                  var1 = new ErrorCollectionException();
               }

               var1.addError((Throwable)var4);
            }
         }

         if (var1 != null && !var1.isEmpty()) {
            this.throwsException(var1);
         }

         return null;
      }

      private void throwsException(Object var1) throws DeploymentException {
         if (var1 instanceof DeploymentException) {
            throw (DeploymentException)var1;
         } else if (var1 instanceof Throwable) {
            throw new WrappedDeploymentException((Throwable)var1);
         }
      }

      protected abstract Iterator getListeners();

      protected abstract boolean fail();

      // $FF: synthetic method
      LifecycleListenerAction(Object var2) {
         this();
      }
   }

   protected class CreateListenerAction extends BaseAction implements PrivilegedAction {
      private final GenericClassLoader gcl;
      private final String className;
      private final boolean nonVersionOnly;

      CreateListenerAction(GenericClassLoader var2, String var3, boolean var4) {
         super(null);
         this.gcl = var2;
         this.className = var3;
         this.nonVersionOnly = var4;
      }

      public Object run() {
         Class var1 = null;

         try {
            var1 = Class.forName(this.className, false, this.gcl);
            if (var1 == null) {
               return new DeploymentException("Cannot load ApplicationLifecycleListener class " + this.className);
            } else {
               Object var2 = var1.newInstance();
               if (var2 instanceof ApplicationLifecycleListener) {
                  return var2;
               } else if (this.nonVersionOnly) {
                  return new DeploymentException("ApplicationLifecycleListener " + var1.getName() + " was not an instanceof " + "weblogic.application.ApplicationLifecycleListener");
               } else if (var2 instanceof ApplicationVersionLifecycleListener) {
                  String var3 = BaseLifecycleFlow.this.appCtx.getApplicationId();
                  if (ApplicationVersionUtils.getVersionId(var3) != null) {
                     return var2;
                  } else {
                     J2EELogger.logIgnoreAppVersionListenerForNonVersionApp(ApplicationVersionUtils.getDisplayName(var3), this.className);
                     return null;
                  }
               } else {
                  return new DeploymentException("Application Lifecycle Listener " + var1.getName() + " was not an instanceof " + "weblogic.application.ApplicationLifecycleListener or " + "weblogic.application.ApplicationVersionLifecycleListener");
               }
            }
         } catch (ClassNotFoundException var4) {
            return new DeploymentException(var4);
         } catch (InstantiationException var5) {
            return new DeploymentException(var5);
         } catch (IllegalAccessException var6) {
            return new DeploymentException(var6);
         }
      }
   }

   private abstract class BaseAction implements PrivilegedAction {
      private BaseAction() {
      }

      Object invoke() throws DeploymentException {
         Object var1 = SecurityServiceManager.runAs(BaseLifecycleFlow.KERNEL_ID, BaseLifecycleFlow.this.appCtx.getDeploymentInitiator(), this);
         if (var1 instanceof DeploymentException) {
            throw (DeploymentException)var1;
         } else if (var1 instanceof Throwable) {
            throw new WrappedDeploymentException((Throwable)var1);
         } else {
            return var1;
         }
      }

      // $FF: synthetic method
      BaseAction(Object var2) {
         this();
      }
   }
}
