package weblogic.management.deploy.classdeployment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.common.T3ShutdownDef;
import weblogic.common.T3StartupDef;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.DeploymentType;
import weblogic.deploy.internal.TargetHelper;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.ClassDeploymentMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ShutdownClassMBean;
import weblogic.management.configuration.StartupClassMBean;
import weblogic.management.internal.DeploymentHandler;
import weblogic.management.internal.DeploymentHandlerContext;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.ServiceFailureException;
import weblogic.t3.srvr.T3Srvr;
import weblogic.utils.AssertionError;
import weblogic.utils.StringUtils;
import weblogic.utils.TypeConversionUtils;

public class ClassDeploymentManager implements DeploymentHandler {
   private Set loadAfterAppAdminState;
   private Set loadBeforeAppActivation;
   private Set loadAfterAppsRunning;
   private Set shutdownClasses;
   private boolean startupComplete;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final Class[] MAIN_SIGNATURE = new Class[]{String[].class};

   public ClassDeploymentManager() {
      this.loadAfterAppAdminState = Collections.synchronizedSet(new TreeSet(DeploymentType.DEPLOYMENT_HANDLER.getComparator()));
      this.loadBeforeAppActivation = Collections.synchronizedSet(new TreeSet(DeploymentType.DEPLOYMENT_HANDLER.getComparator()));
      this.loadAfterAppsRunning = Collections.synchronizedSet(new TreeSet(DeploymentType.DEPLOYMENT_HANDLER.getComparator()));
      this.shutdownClasses = Collections.synchronizedSet(new TreeSet(DeploymentType.DEPLOYMENT_HANDLER.getComparator()));
      this.startupComplete = false;
   }

   public static ClassDeploymentManager getInstance() {
      return ClassDeploymentManager.Initializer.SINGLETON;
   }

   public void prepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      if (var1 instanceof StartupClassMBean) {
         if (this.startupComplete) {
            return;
         }

         StartupClassMBean var3 = (StartupClassMBean)var1;
         if (var3.getLoadBeforeAppDeployments()) {
            return;
         }

         if (var3.getLoadBeforeAppActivation()) {
            this.loadBeforeAppActivation.add(var3);
         } else if (var3.getLoadAfterAppsRunning()) {
            this.loadAfterAppsRunning.add(var3);
         } else {
            this.loadAfterAppAdminState.add(var3);
         }
      }

      if (var1 instanceof ShutdownClassMBean) {
         this.shutdownClasses.add(var1);
      }

   }

   public void activateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
   }

   public void deactivateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      ServerRuntimeMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      if (var3.getStateVal() != 4 && var3.getStateVal() != 7) {
         if (var1 instanceof ShutdownClassMBean) {
            this.shutdownClasses.remove(var1);
         }

      }
   }

   public void unprepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
   }

   void runStartupsBeforeAppDeployments() throws ServiceFailureException {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      StartupClassMBean[] var2 = var1.getStartupClasses();
      if (var2 != null) {
         int var3 = 0;

         for(int var4 = var2.length; var3 < var4; ++var3) {
            if (var2[var3].getLoadBeforeAppDeployments() && TargetHelper.isTargetedLocally(var2[var3])) {
               this.invokeClassDeployment(var2[var3]);
            }
         }

      }
   }

   public void runStartupsBeforeAppActivation() throws ServiceFailureException {
      this.invokeTMDeployment();
      this.invokeClassDeployments(this.loadBeforeAppActivation);
      this.startupComplete = true;
   }

   void runStartupsAfterAppAdminState() throws ServiceFailureException {
      this.invokeClassDeployments(this.loadAfterAppAdminState);
   }

   void runStartupsAfterAppsRunning() throws ServiceFailureException {
      this.invokeClassDeployments(this.loadAfterAppsRunning);
   }

   void runShutdownClasses() throws ServiceFailureException {
      this.invokeClassDeployments(this.shutdownClasses);
   }

   public void invokeTMDeployment() throws ServiceFailureException {
      try {
         this.invokeClass("JTAStartupClass", "weblogic.transaction.internal.StartupClass", (String)null);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void invokeClassDeployments(Collection var1) throws ServiceFailureException {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.invokeClassDeployment((ClassDeploymentMBean)var2.next());
      }

      var1.clear();
   }

   protected void invokeClassDeployment(final ClassDeploymentMBean var1) throws ServiceFailureException {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("deploying ClassDeploymentMBean: " + var1.getName() + ", classname: " + var1.getClassName() + ", arguments: " + var1.getArguments() + ", kernal id: " + kernelId);
      }

      Object var2 = SecurityServiceManager.runAs(kernelId, kernelId, new PrivilegedAction() {
         public Object run() {
            ServiceFailureException var4;
            try {
               Object var2;
               try {
                  ApplicationVersionUtils.setCurrentAdminMode(true);
                  ClassDeploymentManager.this.invokeClass(var1.getName(), var1.getClassName(), var1.getArguments());
                  return null;
               } catch (Exception var9) {
                  var2 = var9;
                  if (var9 instanceof InvocationTargetException) {
                     var2 = ((InvocationTargetException)var9).getTargetException();
                  }

                  if (!(var1 instanceof StartupClassMBean)) {
                     if (var9 instanceof InvocationTargetException) {
                        var2 = ((InvocationTargetException)var9).getTargetException();
                     }

                     T3SrvrLogger.logFailInvokeShutdownClass(var1.getName(), (Throwable)var2);
                     return null;
                  }

                  StartupClassMBean var3 = (StartupClassMBean)var1;
                  if (!var3.getFailureIsFatal()) {
                     T3SrvrLogger.logFailInvokeStartupClass(var1.getName(), (Throwable)var2);
                     return null;
                  }
               }

               var4 = new ServiceFailureException("Can't start server due to classDeployment class failure " + var1.getName(), (Throwable)var2);
            } finally {
               ApplicationVersionUtils.unsetCurrentAdminMode();
            }

            return var4;
         }
      });
      if (var2 != null) {
         throw (ServiceFailureException)var2;
      }
   }

   private void invokeClass(String var1, String var2, String var3) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
      Class var4 = Class.forName(var2, true, Thread.currentThread().getContextClassLoader());
      if (T3StartupDef.class.isAssignableFrom(var4)) {
         this.invokeStartup(var1, var4, var3);
      } else if (T3ShutdownDef.class.isAssignableFrom(var4)) {
         invokeShutdown(var1, var4, var3);
      } else {
         this.invokeMain(var4, var3);
      }
   }

   private final void invokeStartup(String var1, Class var2, String var3) throws InstantiationException, IllegalAccessException, InvocationTargetException {
      T3StartupDef var4 = (T3StartupDef)var2.newInstance();
      Hashtable var5 = new Hashtable();
      TypeConversionUtils.stringToDictionary(var3, var5);
      var4.setServices(T3Srvr.getT3Srvr().getT3Services());
      T3SrvrLogger.logInvokingStartup(var2.getName(), var3);

      try {
         String var6 = var4.startup(var1, var5);
         T3SrvrLogger.logStartupClassReports(var2.getName(), var6);
      } catch (Exception var7) {
         throw new InvocationTargetException(var7);
      }
   }

   private static final void invokeShutdown(String var0, Class var1, String var2) throws InstantiationException, IllegalAccessException, InvocationTargetException {
      T3ShutdownDef var3 = (T3ShutdownDef)var1.newInstance();
      Hashtable var4 = new Hashtable();
      TypeConversionUtils.stringToDictionary(var2, var4);
      var3.setServices(T3Srvr.getT3Srvr().getT3Services());
      T3SrvrLogger.logInvokingShutdown(var1.getName(), var2);

      try {
         String var5 = var3.shutdown(var0, var4);
         T3SrvrLogger.logShutdownClassReports(var1.getName(), var5);
      } catch (Exception var6) {
         throw new InvocationTargetException(var6);
      }
   }

   private void invokeMain(Class var1, String var2) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
      Object[] var3 = null;
      String[] var4;
      if (var2 != null) {
         var4 = StringUtils.splitCompletely(var2, " ");
         var3 = new Object[]{var4};
      } else {
         var3 = new Object[]{new String[0]};
      }

      var4 = null;
      int var5 = var1.getModifiers();
      if (!Modifier.isPublic(var5)) {
         throw new IllegalAccessException(var1.getName() + " is not a public class");
      } else if (Modifier.isInterface(var5)) {
         throw new IllegalAccessException(var1.getName() + " is an interface, not a public class");
      } else {
         Method var10;
         try {
            var10 = var1.getMethod("main", MAIN_SIGNATURE);
         } catch (NoSuchMethodException var9) {
            throw new NoSuchMethodException(var1.getName() + " does not define 'public static void main(String[])'");
         }

         int var6 = var10.getModifiers();
         if (Modifier.isStatic(var6) && Modifier.isPublic(var6)) {
            try {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("invoking main on: " + var1.getName() + ", with " + var2);
               }

               T3SrvrLogger.logInvokingClass(var1.getName(), var2);
               var10.invoke((Object)null, var3);
            } catch (IllegalArgumentException var8) {
               throw new AssertionError("Should never occur", var8);
            }
         } else {
            throw new IllegalAccessException(var1.getName() + ".main(String[]) must must be a public static method");
         }
      }
   }

   private static class Initializer {
      static final ClassDeploymentManager SINGLETON = new ClassDeploymentManager();
   }
}
