package weblogic.deploy.internal.targetserver;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.Deployment;
import weblogic.application.internal.DeploymentStateChecker;
import weblogic.application.utils.TargetUtils;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.common.Debug;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeployerRuntimeTextTextFormatter;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.AssertionError;
import weblogic.utils.NestedThrowable;

public class DeployHelper {
   private static final String CONFIG_DIR_PREFIX;
   private static final AuthenticatedSubject kernelId;

   public static String getTaskName(int var0) {
      return getTaskName(var0, (Locale)null);
   }

   public static String getTaskName(int var0, Locale var1) {
      String var2 = null;
      DeployerRuntimeTextTextFormatter var3 = null;
      if (var1 == null) {
         var3 = DeployerRuntimeTextTextFormatter.getInstance();
      } else {
         var3 = DeployerRuntimeTextTextFormatter.getInstance(var1);
      }

      switch (var0) {
         case 1:
            var2 = var3.messageActivate();
            break;
         case 2:
            var2 = var3.messagePrepare();
            break;
         case 3:
            var2 = var3.messageDeactivate();
            break;
         case 4:
            var2 = var3.messageRemove();
            break;
         case 5:
            var2 = var3.messageUnprepare();
            break;
         case 6:
            var2 = var3.messageDistribute();
            break;
         case 7:
            var2 = var3.messageStart();
            break;
         case 8:
            var2 = var3.messageStop();
            break;
         case 9:
            var2 = var3.messageRedeploy();
            break;
         case 10:
            var2 = var3.messageUpdate();
            break;
         case 11:
            var2 = var3.messageDeploy();
         case 12:
         default:
            break;
         case 13:
            var2 = var3.messageRetire();
      }

      return var2;
   }

   public static String getStagingMode(String var0, AppDeploymentMBean var1) {
      String var2 = var1.getStagingMode();
      if (var2 == null || var2.length() == 0) {
         var2 = getServerStagingMode(var0);
      }

      return var2;
   }

   public static String getServerStagingMode(String var0) {
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
      ServerMBean var3 = var2.getDomain().lookupServer(var0);
      String var1 = var3.getStagingMode();
      return var1 != null && var1.length() != 0 ? var1 : determineDefaultStagingMode(var0);
   }

   public static String determineDefaultStagingMode(String var0) {
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
      if (null == var2) {
         return ServerMBean.DEFAULT_STAGE;
      } else {
         String var1;
         if (DeployHelper.AdminServer.adminServerName != null && !DeployHelper.AdminServer.adminServerName.equals(var0)) {
            var1 = "stage";
         } else {
            var1 = "nostage";
         }

         return var1;
      }
   }

   public static DeploymentException convertThrowable(Throwable var0) {
      if (Debug.isDeploymentDebugEnabled()) {
         var0.printStackTrace();
      }

      String var1 = SlaveDeployerLogger.logUnexpectedThrowableLoggable().getMessage();
      return handleException(var0, var1);
   }

   public static BasicDeployment createDeployment(BasicDeploymentMBean var0) {
      return (BasicDeployment)(var0 instanceof AppDeploymentMBean ? new AppDeployment((AppDeploymentMBean)var0) : new SystemResourceDeployment((SystemResourceMBean)var0));
   }

   public static void logAndThrow(Loggable var0) throws DeploymentException {
      var0.log();
      throw new DeploymentException(var0.getMessage());
   }

   public static void throwUnexpected(Throwable var0) throws DeploymentException {
      throw convertThrowable(var0);
   }

   public static void debug(String var0) {
      Debug.deploymentDebug(var0);
   }

   public static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   public static boolean isModuleType(TargetInfoMBean var0, ModuleType var1) {
      return var1 != null && var1.toString().equals(var0.getModuleType());
   }

   public static String getSourcePath(BasicDeploymentMBean var0) {
      String var1 = null;
      if (var0 instanceof AppDeploymentMBean) {
         var1 = ((AppDeploymentMBean)var0).getAbsoluteSourcePath();
      } else {
         if (!(var0 instanceof SystemResourceMBean)) {
            throw new AssertionError("DeploymentMBean should be either AppDeployment/SystemResourceMBean");
         }

         var1 = CONFIG_DIR_PREFIX + ((SystemResourceMBean)var0).getDescriptorFileName();
      }

      return var1;
   }

   public static int getState(Deployment var0) {
      if (var0 instanceof AppContainerInvoker) {
         return getState(((AppContainerInvoker)var0).getDelegate());
      } else if (var0 instanceof DeploymentStateChecker) {
         return ((DeploymentStateChecker)var0).getState();
      } else {
         throw new java.lang.AssertionError("Deployment must be an instanceof DeploymentStateChecker. Got - " + var0.getClass());
      }
   }

   public static boolean isPreparedState(Deployment var0) {
      return getState(var0) == 1;
   }

   public static boolean isAdminState(Deployment var0) {
      return getState(var0) == 2;
   }

   public static boolean isActiveState(Deployment var0) {
      return getState(var0) == 3;
   }

   public static String getStagingModeFromOptions(DeploymentData var0) {
      String var1 = null;
      DeploymentOptions var2 = var0.getDeploymentOptions();
      if (var2 != null) {
         var1 = var2.getStageMode();
      }

      return var1;
   }

   public static boolean isOkToTransition(AppDeploymentMBean var0, ServerMBean var1, String var2) {
      if (var0.isInternalApp()) {
         return true;
      } else {
         Set var3 = getAllTargets(var0);
         String var4 = null;
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            TargetMBean var6 = (TargetMBean)var5.next();
            if (TargetUtils.isDeployedLocally(new TargetMBean[]{var6})) {
               String var7 = AppRuntimeStateManager.getManager().getIntendedState(var0.getName(), var6.getName());
               if (var4 == null) {
                  var4 = var7;
               }

               if (less(var4, var7)) {
                  var4 = var7;
               }
            }
         }

         if (var4 == null) {
            try {
               AppRuntimeStateManager.getManager().setState(var0.getName(), (String[])((String[])getAllTargetNames(var3).toArray(new String[0])), "STATE_ACTIVE");
            } catch (ManagementException var8) {
            }

            return true;
         } else if (!var2.equals(var4) && less(var4, var2)) {
            return false;
         } else {
            return true;
         }
      }
   }

   private static Set getAllTargetNames(Set var0) {
      HashSet var1 = new HashSet(var0.size());
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         TargetMBean var3 = (TargetMBean)var2.next();
         var1.add(var3.getName());
      }

      return var1;
   }

   public static Set getAllTargetNames(BasicDeploymentMBean var0) {
      Set var1 = getAllTargets(var0);
      HashSet var2 = new HashSet(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         TargetMBean var4 = (TargetMBean)var3.next();
         var2.add(var4.getName());
      }

      return var2;
   }

   public static Set getAllTargets(BasicDeploymentMBean var0) {
      HashSet var1 = new HashSet();
      addFromMbean(var0, var1);
      SubDeploymentMBean[] var2 = var0.getSubDeployments();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         addFromMbean(var2[var3], var1);
         SubDeploymentMBean[] var4 = var0.getSubDeployments();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            addFromMbean(var4[var5], var1);
         }
      }

      return var1;
   }

   private static void addFromMbean(TargetInfoMBean var0, Set var1) {
      TargetMBean[] var2 = var0.getTargets();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.add(var2[var3]);
      }

   }

   public static boolean less(String var0, String var1) {
      if (var0 != null && var1 != null) {
         for(int var2 = 0; var2 < AppRuntimeStateRuntimeMBean.appStateDefs.length; ++var2) {
            if (var0.equals(AppRuntimeStateRuntimeMBean.appStateDefs[var2])) {
               return true;
            }

            if (var1.equals(AppRuntimeStateRuntimeMBean.appStateDefs[var2])) {
               return false;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static DeploymentException handleException(Throwable var0, String var1) {
      if (var0 instanceof DeploymentException) {
         return (DeploymentException)var0;
      } else if (var0 instanceof ManagementException) {
         ManagementException var2 = (ManagementException)var0;
         Throwable var3 = var2.getNested();
         DeploymentException var4 = var3 != null ? new DeploymentException(var2.getMessage(), var3) : new DeploymentException(var2.getMessage());
         var4.setStackTrace(var2.getStackTrace());
         return var4;
      } else {
         assertForNullMessage(var1);
         return new DeploymentException(var1, getWrappedException(var0));
      }
   }

   private static void assertForNullMessage(String var0) {
      if (var0 == null || var0.length() == 0) {
         throw new AssertionError(" ************* CONSTRUCTING EXCEPTION WITH NULL MESSAGE ********** ");
      }
   }

   private static Throwable getWrappedException(Throwable var0) {
      Throwable var1;
      if (var0 instanceof NestedThrowable) {
         var1 = ((NestedThrowable)var0).getNested();
      } else {
         var1 = var0.getCause();
      }

      return var1 == null ? var0 : getWrappedException(var1);
   }

   public static DeploymentContextImpl createDeploymentContext(BasicDeploymentMBean var0) throws DeploymentException {
      try {
         String var1 = null;
         if (var0 != null) {
            var1 = var0.getDeploymentPrincipalName();
         }

         if (var1 != null) {
            PrincipalAuthenticator var2 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, "weblogicDEFAULT", ServiceType.AUTHENTICATION);
            AuthenticatedSubject var3 = var2.impersonateIdentity(var1, (ContextHandler)null);
            return new DeploymentContextImpl(var3);
         } else {
            return new DeploymentContextImpl(SubjectUtils.getAnonymousSubject());
         }
      } catch (Exception var4) {
         throw new DeploymentException(var4);
      }
   }

   private static boolean isInternalClass(Object var0) {
      String var1 = var0.getClass().getPackage().getName();
      String[] var2 = new String[]{"java.", "javax.", "weblogic."};

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var1.startsWith(var2[var3])) {
            return true;
         }
      }

      return false;
   }

   private static Throwable handleUnTranferableCause(Throwable var0, int var1) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
      if (var1 == 10) {
         return var0;
      } else {
         if (var0.getCause() != null) {
            Throwable var2 = var0.getCause();
            if (!isInternalClass(var2)) {
               Constructor var6 = var0.getClass().getConstructor(String.class);
               Throwable var7 = (Throwable)var6.newInstance(var0.getMessage() + ":" + var2.getClass().getCanonicalName() + ":" + var2.getMessage());
               var7.setStackTrace(var2.getStackTrace());
               return var7;
            }

            Throwable var3 = handleUnTranferableCause(var2, var1 + 1);
            if (var3 != var2) {
               Constructor var4 = var0.getClass().getConstructor(String.class);
               Throwable var5 = (Throwable)var4.newInstance(var0.getMessage());
               var5.initCause(var3);
               var5.setStackTrace(var0.getStackTrace());
               return var5;
            }
         }

         return var0;
      }
   }

   public static DeploymentException convertThrowableForTransfer(Throwable var0) {
      if (Debug.isDeploymentDebugEnabled()) {
         var0.printStackTrace();
      }

      if (var0 instanceof DeploymentException) {
         try {
            if (var0.getCause() != null) {
               return (DeploymentException)handleUnTranferableCause(var0, 0);
            }

            return (DeploymentException)var0;
         } catch (Exception var2) {
         }
      }

      String var1 = SlaveDeployerLogger.logUnexpectedThrowableLoggable().getMessage();
      return handleException(var0, var1);
   }

   static {
      CONFIG_DIR_PREFIX = DomainDir.getRootDir() + File.separator + "config" + File.separator;
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   static class AdminServer {
      static String adminServerName;

      static {
         adminServerName = ManagementService.getRuntimeAccess(DeployHelper.kernelId).getAdminServerName();
      }
   }
}
