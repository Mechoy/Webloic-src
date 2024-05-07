package weblogic.ejb.container;

import java.security.AccessController;
import java.util.Set;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.EJBComponentMBean;
import weblogic.management.configuration.EJBContainerMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class ReadConfig {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static Set m_ejbContainerMBeans = null;

   public static String getJavaCompilerPreClassPath(EJBComponentMBean var0) {
      String var1 = null;
      if (var0 != null) {
         var1 = var0.getJavaCompilerPreClassPath();
      }

      if (null == var1 && null != getEJBContainerMBean()) {
         var1 = getEJBContainerMBean().getJavaCompilerPreClassPath();
      }

      if (null == var1 && null != getServerMBean()) {
         var1 = getServerMBean().getJavaCompilerPreClassPath();
      }

      return var1;
   }

   public static String getJavaCompilerPostClassPath(EJBComponentMBean var0) {
      String var1 = null;
      if (var0 != null) {
         var1 = var0.getJavaCompilerPostClassPath();
      }

      if (null == var1 && null != getEJBContainerMBean()) {
         var1 = getEJBContainerMBean().getJavaCompilerPostClassPath();
      }

      if (null == var1 && null != getServerMBean()) {
         var1 = getServerMBean().getJavaCompilerPostClassPath();
      }

      return var1;
   }

   public static String getJavaCompiler(EJBComponentMBean var0) {
      String var1 = null;
      if (var0 != null) {
         var1 = var0.getJavaCompiler();
      }

      if (null == var1 && null != getEJBContainerMBean()) {
         var1 = getEJBContainerMBean().getJavaCompiler();
      }

      if (var1 == null) {
         var1 = "jdt";
      }

      if (null == var1 && null != getServerMBean()) {
         var1 = getServerMBean().getJavaCompiler();
         String var2 = getServerMBean().getExtraEjbcOptions();
         if (null == var2 && "javac".equalsIgnoreCase(var1)) {
            var1 = null;
         }
      }

      return var1;
   }

   public static String getExtraEjbcOptions(EJBComponentMBean var0) {
      String var1 = null;
      if (var0 != null) {
         var1 = var0.getExtraEjbcOptions();
      }

      if (null == var1 && null != getEJBContainerMBean()) {
         var1 = getEJBContainerMBean().getExtraEjbcOptions();
      }

      if (null == var1 && null != getServerMBean()) {
         var1 = getServerMBean().getExtraEjbcOptions();
      }

      return var1;
   }

   public static boolean isVerboseEJBDeploymentEnabled() {
      return isVerboseEJBDeploymentEnabled((EJBComponentMBean)null);
   }

   public static boolean isVerboseEJBDeploymentEnabled(EJBComponentMBean var0) {
      String var1 = null;
      if (null != var0) {
         var1 = var0.getVerboseEJBDeploymentEnabled();
      }

      if (null == var1 && null != getEJBContainerMBean()) {
         var1 = getEJBContainerMBean().getVerboseEJBDeploymentEnabled();
      }

      if (null == var1 && null != getServerMBean()) {
         var1 = getServerMBean().getVerboseEJBDeploymentEnabled();
      }

      return "true".equalsIgnoreCase(var1);
   }

   public static boolean getForceGeneration(EJBComponentMBean var0) {
      if (null != var0 && var0.getApplication().isInternalApp()) {
         return false;
      } else {
         boolean var1 = false;
         boolean var2 = false;
         if (null != var0) {
            var1 = var0.getForceGeneration();
            var2 = var0.isSet("ForceGeneration");
         }

         if (!var2 && null != getEJBContainerMBean()) {
            var1 = getEJBContainerMBean().getForceGeneration();
         }

         return var1;
      }
   }

   private static EJBContainerMBean getEJBContainerMBean() {
      return Kernel.isServer() ? ManagementService.getRuntimeAccess(kernelId).getDomain().getEJBContainer() : null;
   }

   private static ServerMBean getServerMBean() {
      return Kernel.isServer() ? ManagementService.getRuntimeAccess(kernelId).getServer() : null;
   }
}
