package weblogic.application.internal.flow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.login.LoginException;
import weblogic.application.ApplicationException;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;
import weblogic.application.ApplicationVersionLifecycleListener;
import weblogic.application.event.ApplicationEventManager;
import weblogic.application.internal.Flow;
import weblogic.application.internal.FlowContext;
import weblogic.deploy.event.ApplicationVersionLifecycleListenerAdapter;
import weblogic.j2ee.J2EEApplicationService;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.ListenerBean;
import weblogic.j2ee.descriptor.wl.ShutdownBean;
import weblogic.j2ee.descriptor.wl.StartupBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;

public final class HeadLifecycleFlow extends BaseLifecycleFlow implements Flow {
   private static final ApplicationLifecycleListener[] EMPTY_LISTENERS = new ApplicationLifecycleListener[0];
   private PrincipalAuthenticator pa = null;

   public HeadLifecycleFlow(FlowContext var1) {
      super(var1);
   }

   private void addListenerJarToLoader(GenericClassLoader var1, String var2) throws DeploymentException {
      URL var3 = var1.getResource(this.appCtx.getApplicationId() + "#" + var2);
      if (var3 == null) {
         Loggable var4 = J2EELogger.logUnabletoFindLifecycleJarLoggable(this.appCtx.getApplicationId(), var2);
         throw new DeploymentException(var4.getMessage());
      } else {
         var1.addClassFinder(new ClasspathClassFinder2(var3.getFile()));
      }
   }

   private Class createMainClass(String var1, String var2) throws DeploymentException {
      GenericClassLoader var3 = this.appCtx.getAppClassLoader();
      if (var2 != null) {
         this.addListenerJarToLoader(var3, var2);
      }

      BaseLifecycleFlow.CreateMainClassAction var4 = new BaseLifecycleFlow.CreateMainClassAction(var3, var1);
      return (Class)var4.invoke();
   }

   private ApplicationLifecycleListener createNonVersionedListener(String var1, String var2) throws DeploymentException {
      return (ApplicationLifecycleListener)this.createListener(var1, var2, true);
   }

   private Object createListener(String var1, String var2) throws DeploymentException {
      return this.createListener(var1, var2, false);
   }

   private Object createListener(String var1, String var2, boolean var3) throws DeploymentException {
      GenericClassLoader var4 = this.appCtx.getAppClassLoader();
      if (var2 != null) {
         this.addListenerJarToLoader(var4, var2);
      }

      BaseLifecycleFlow.CreateListenerAction var5 = new BaseLifecycleFlow.CreateListenerAction(var4, var1, var3);
      return var5.invoke();
   }

   private ApplicationLifecycleListener createStartupListener(String var1, String var2) throws DeploymentException {
      return new MainListener(this.createMainClass(var1, var2)) {
         public void preStart(ApplicationLifecycleEvent var1) throws ApplicationException {
            this.invokeMain();
         }
      };
   }

   private ApplicationLifecycleListener createShutdownListener(String var1, String var2) throws DeploymentException {
      return new MainListener(this.createMainClass(var1, var2)) {
         public void postStop(ApplicationLifecycleEvent var1) throws ApplicationException {
            this.invokeMain();
         }
      };
   }

   private ApplicationLifecycleListener[] createListeners() throws DeploymentException {
      WeblogicApplicationBean var1 = this.appCtx.getWLApplicationDD();
      List var2 = ApplicationEventManager.getInstance().createListeners(this.appCtx);
      List var3 = J2EEApplicationService.getApplicationLifecycleListeners();
      if (var1 == null && var3.isEmpty()) {
         return var2.size() == 0 ? EMPTY_LISTENERS : (ApplicationLifecycleListener[])var2.toArray(new ApplicationLifecycleListener[0]);
      } else {
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         var4.addAll(var3);
         if (var1 != null) {
            ListenerBean[] var6 = var1.getListeners();
            int var8;
            if (var6 != null) {
               HashMap var7 = null;

               for(var8 = 0; var8 < var6.length; ++var8) {
                  Object var9 = this.createListener(var6[var8].getListenerClass(), var6[var8].getListenerUri());
                  if (var9 instanceof ApplicationLifecycleListener) {
                     var4.add(var9);
                  } else if (var9 instanceof ApplicationVersionLifecycleListener) {
                     var5.add(var9);
                  }

                  if (var6[var8].getRunAsPrincipalName() != null) {
                     AuthenticatedSubject var10 = this.getRunAsIdentity(var6[var8]);
                     if (var10 != null) {
                        if (var7 == null) {
                           var7 = new HashMap();
                        }

                        var7.put(var9, var10);
                     }
                  }
               }

               this.appCtx.setAppListenerIdentityMappings((Map)(var7 != null ? var7 : Collections.EMPTY_MAP));
               if (var5.size() > 0) {
                  ApplicationVersionLifecycleListener[] var13 = new ApplicationVersionLifecycleListener[var5.size()];
                  var5.toArray(var13);
                  this.appCtx.setApplicationVersionListenerAdapter(new ApplicationVersionLifecycleListenerAdapter(this.appCtx.getApplicationId(), var13));
               }
            }

            StartupBean[] var12 = var1.getStartups();
            if (var12 != null) {
               for(var8 = 0; var8 < var12.length; ++var8) {
                  var4.add(this.createStartupListener(var12[var8].getStartupClass(), var12[var8].getStartupUri()));
               }
            }

            ShutdownBean[] var15 = var1.getShutdowns();
            if (var15 != null) {
               for(int var14 = 0; var14 < var15.length; ++var14) {
                  var4.add(this.createShutdownListener(var15[var14].getShutdownClass(), var15[var14].getShutdownUri()));
               }
            }
         }

         var4.addAll(var2);
         ApplicationLifecycleListener[] var11 = new ApplicationLifecycleListener[var4.size()];
         return (ApplicationLifecycleListener[])((ApplicationLifecycleListener[])var4.toArray(var11));
      }
   }

   private AuthenticatedSubject getRunAsIdentity(ListenerBean var1) throws DeploymentException {
      String var2 = var1.getRunAsPrincipalName();
      AuthenticatedSubject var3 = null;

      try {
         var3 = this.getPrincipalAuthenticator().impersonateIdentity(var2);
      } catch (LoginException var6) {
         Loggable var5 = J2EELogger.logRunAsPrincipalNotFoundLoggable(this.appCtx.getApplicationId(), var1.getListenerClass(), var2);
         throw new DeploymentException(var5.getMessage());
      }

      this.checkDeployUserPrivileges(var1, var3);
      return var3;
   }

   private void checkDeployUserPrivileges(ListenerBean var1, AuthenticatedSubject var2) throws DeploymentException {
      if (SubjectUtils.isUserAnAdministrator(var2)) {
         AuthenticatedSubject var3 = this.appCtx.getDeploymentInitiator();
         if (var3 != null && (!this.appCtx.isStaticDeploymentOperation() || !SubjectUtils.isUserAnonymous(var3)) && !SubjectUtils.isUserAnAdministrator(var3)) {
            Loggable var4 = J2EELogger.logAttemptToBumpUpPrivilegesWithRunAsLoggable(this.appCtx.getApplicationId(), var1.getListenerClass(), var1.getRunAsPrincipalName());
            throw new DeploymentException(var4.getMessage());
         }
      }

   }

   private PrincipalAuthenticator getPrincipalAuthenticator() {
      if (this.pa != null) {
         return this.pa;
      } else {
         String var1 = this.appCtx.getApplicationSecurityRealmName();
         if (var1 == null) {
            var1 = "weblogicDEFAULT";
         }

         this.pa = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNEL_ID, var1, ServiceType.AUTHENTICATION);
         return this.pa;
      }
   }

   public void prepare() throws DeploymentException {
      this.appCtx.setApplicationListeners(this.createListeners());
      this.preStart();
   }

   public void unprepare() throws DeploymentException {
      try {
         this.postStop();
      } finally {
         this.appCtx.setApplicationListeners((ApplicationLifecycleListener[])null);
         this.appCtx.setApplicationVersionListenerAdapter((ApplicationVersionLifecycleListenerAdapter)null);
      }

   }

   private abstract static class MainListener extends ApplicationLifecycleListener {
      private static final Class[] MAIN_SIGNATURE = new Class[]{String[].class};
      private static final Object[] MAIN_ARGS = new Object[]{new String[0]};
      private final Class mainClass;
      protected Method mainMethod;

      MainListener(Class var1) throws DeploymentException {
         this.mainClass = var1;

         try {
            this.mainMethod = var1.getMethod("main", MAIN_SIGNATURE);
         } catch (NoSuchMethodException var3) {
            throw new DeploymentException(var3);
         }
      }

      protected void invokeMain() throws ApplicationException {
         try {
            this.mainMethod.invoke((Object)null, MAIN_ARGS);
         } catch (IllegalAccessException var3) {
            throw new AssertionError(var3);
         } catch (IllegalArgumentException var4) {
            throw new AssertionError(var4);
         } catch (InvocationTargetException var5) {
            Object var2 = var5.getTargetException();
            if (var2 == null) {
               var2 = var5.getCause();
            }

            if (var2 == null) {
               var2 = var5;
            }

            throw new ApplicationException((Throwable)var2);
         }
      }
   }
}
