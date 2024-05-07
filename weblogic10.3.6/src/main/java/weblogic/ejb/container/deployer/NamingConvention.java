package weblogic.ejb.container.deployer;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.utils.ClassUtils;

public final class NamingConvention {
   private static final DebugLogger debugLogger;
   private String beanClassName;
   private String beanRootName;
   private String beanPackageName;
   private String ejbName;
   private String generatedBaseName;
   private String interceptorClassName;
   private String interceptorPackageName;

   public NamingConvention(String var1) {
      this.interceptorClassName = var1;
   }

   public NamingConvention(String var1, String var2) {
      this.beanClassName = var1;
      this.ejbName = var2;
   }

   private String unique() {
      String var1 = makeLegalFileName(this.getEJBName() + this.getSimpleBeanClassName());
      return "_" + Long.toString((long)Math.abs(var1.hashCode()), 36) + "_";
   }

   private String uniqueForInterceptor() {
      String var1 = makeLegalFileName(this.getSimpleInterceptorClassName());
      return "_" + Long.toString((long)Math.abs(var1.hashCode()), 36) + "_";
   }

   public String getSimpleBeanClassName() {
      return tail(this.beanClassName);
   }

   public String getSimpleInterceptorClassName() {
      return tail(this.interceptorClassName);
   }

   public String getBeanClassName() {
      return this.beanClassName;
   }

   public String getGeneratedBaseName() {
      assert this.getEJBName() != null;

      if (this.generatedBaseName != null) {
         return this.generatedBaseName;
      } else {
         if (this.getEJBName().length() <= 26) {
            this.generatedBaseName = makeLegalFileName(this.getEJBName() + this.unique());
         } else {
            this.generatedBaseName = this.getSimpleBeanClassName() + this.unique();
         }

         return this.generatedBaseName;
      }
   }

   public String getGeneratedBaseInterceptorName() {
      return this.getSimpleInterceptorClassName() + this.uniqueForInterceptor();
   }

   public String getSimpleHomeClassName() {
      return this.getGeneratedBaseName() + "HomeImpl";
   }

   public String getHomeClassName() {
      return this.getBeanPackagePrefix() + this.getSimpleHomeClassName();
   }

   public String getSimpleLocalHomeClassName() {
      return this.getGeneratedBaseName() + "LocalHomeImpl";
   }

   public String getLocalHomeClassName() {
      return this.getBeanPackagePrefix() + this.getSimpleLocalHomeClassName();
   }

   public String getHomeImplClassName(boolean var1) {
      return var1 ? "weblogic.ejb.container.internal.StatefulEJBHomeImpl" : "weblogic.ejb.container.internal.StatelessEJBHomeImpl";
   }

   public String getLocalHomeImplClassName(boolean var1) {
      return var1 ? "weblogic.ejb.container.internal.StatefulEJBLocalHomeImpl" : "weblogic.ejb.container.internal.StatelessEJBLocalHomeImpl";
   }

   public String getSimpleEJBObjectClassName() {
      return this.getGeneratedBaseName() + "EOImpl";
   }

   public String getEJBObjectClassName() {
      return this.getBeanPackagePrefix() + this.getSimpleEJBObjectClassName();
   }

   public String getSimpleEJBLocalObjectClassName() {
      return this.getGeneratedBaseName() + "ELOImpl";
   }

   public String getEJBLocalObjectClassName() {
      return this.getBeanPackagePrefix() + this.getSimpleEJBLocalObjectClassName();
   }

   public String getSimpleLocalBusinessImplClassName(Class<?> var1) {
      return this.getGeneratedBaseName() + var1.getSimpleName() + "Impl";
   }

   public String getLocalBusinessImplClassName(Class<?> var1) {
      return this.getBeanPackagePrefix() + this.getSimpleLocalBusinessImplClassName(var1);
   }

   public String getSimpleInterceptorImplClassName() {
      return this.getGeneratedBaseInterceptorName() + "Impl";
   }

   public String getInterceptorImplClassName() {
      return this.getInterceptorPackagePrefix() + this.getSimpleInterceptorImplClassName();
   }

   public String getSimpleRemoteBusinessImplClassName(Class<?> var1) {
      return this.getGeneratedBaseName() + var1.getSimpleName() + "Impl";
   }

   public String getRemoteBusinessImplClassName(Class<?> var1) {
      return this.getBeanPackagePrefix() + this.getSimpleLocalBusinessImplClassName(var1);
   }

   public String getSimpleRemoteBusinessIntfClassName(Class<?> var1) {
      return this.getGeneratedBaseName() + var1.getSimpleName() + "RIntf";
   }

   public String getRemoteBusinessIntfClassName(Class<?> var1) {
      String var2 = head(var1.getName());
      if (var2.length() > 0) {
         var2 = var2 + ".";
      }

      return var2 + this.getSimpleRemoteBusinessIntfClassName(var1);
   }

   public String getSimpleRemoteBusinessWrapperClassName(Class<?> var1) {
      return this.getGeneratedBaseName() + var1.getSimpleName() + "Wrap";
   }

   public String getRemoteBusinessWrapperClassName(Class<?> var1) {
      return this.getBeanPackagePrefix() + this.getSimpleRemoteBusinessWrapperClassName(var1);
   }

   public String getSimpleWsHomeClassName() {
      return this.getGeneratedBaseName() + "WSLocalHomeImpl";
   }

   public String getWsHomeClassName() {
      return this.getBeanPackagePrefix() + this.getSimpleWsHomeClassName();
   }

   public String getSimpleWsObjectClassName() {
      return this.getGeneratedBaseName() + "WSOImpl";
   }

   public String getWsObjectClassName() {
      return this.getBeanPackagePrefix() + this.getSimpleWsObjectClassName();
   }

   public String getSimpleMdLocalObjectClassName() {
      return this.getGeneratedBaseName() + "MDOImpl";
   }

   public String getMdLocalObjectClassName(boolean var1) {
      return var1 ? "weblogic.ejb.container.internal.MessageDrivenLocalObject" : this.getBeanPackagePrefix() + this.getSimpleMdLocalObjectClassName();
   }

   public String getSimpleCmpBeanClassName(String var1) {
      return this.getGeneratedBaseName() + "_" + var1;
   }

   private static String makeLegalFileName(String var0) {
      return ClassUtils.makeLegalName(var0);
   }

   public String getCmpBeanClassName(String var1) {
      return this.getBeanPackagePrefix() + this.getSimpleCmpBeanClassName(var1);
   }

   public String getGeneratedBeanClassName() {
      return this.getBeanPackagePrefix() + this.getSimpleGeneratedBeanClassName();
   }

   public String getSimpleGeneratedBeanClassName() {
      return this.getGeneratedBaseName() + "Impl";
   }

   public String getGeneratedBeanInterfaceName() {
      return this.getBeanPackagePrefix() + this.getSimpleGeneratedBeanInterfaceName();
   }

   public String getSimpleGeneratedBeanInterfaceName() {
      return this.getGeneratedBaseName() + "Intf";
   }

   public String getEJBName() {
      return this.ejbName;
   }

   public String beanRoot() {
      if (this.beanRootName == null) {
         this.beanRootName = tail(this.beanClassName);
         if (this.beanRootName.endsWith("Bean")) {
            int var1 = this.beanRootName.length();
            this.beanRootName = this.beanRootName.substring(0, var1 - 4);
         }
      }

      return this.beanRootName;
   }

   public String getInterceptorPackageName() {
      if (this.interceptorPackageName == null) {
         this.interceptorPackageName = head(this.interceptorClassName);
      }

      return this.interceptorPackageName;
   }

   public String getInterceptorPackagePrefix() {
      String var1 = this.getInterceptorPackageName();
      String var2 = "";
      if (var1 != null && var1.length() > 0) {
         var2 = var1 + ".";
      }

      return var2;
   }

   public String getBeanPackageName() {
      if (this.beanPackageName == null) {
         this.beanPackageName = head(this.beanClassName);
      }

      return this.beanPackageName;
   }

   public String getBeanPackagePrefix() {
      String var1 = this.getBeanPackageName();
      String var2 = "";
      if (var1 != null && var1.length() > 0) {
         var2 = var1 + ".";
      }

      return var2;
   }

   private static void debug(String var0) {
      debugLogger.debug("[NamingConvention] " + var0);
   }

   static String head(String var0) {
      int var1 = var0.lastIndexOf(".");
      return var1 == -1 ? "" : var0.substring(0, var1);
   }

   public static String tail(String var0) {
      int var1 = var0.lastIndexOf(".");
      return var0.substring(var1 + 1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("BeanClassName: " + this.beanClassName + "\n");
      var1.append("BeanRootName: " + this.beanRootName + "\n");
      var1.append("BeanPackageName: " + this.beanPackageName + "\n");
      var1.append("EJBName: " + this.ejbName + "\n");
      return var1.toString();
   }

   public static void main(String[] var0) {
      NamingConvention var1 = new NamingConvention(var0[0], (String)null);
      System.out.println("beanClass(" + var1.getBeanClassName() + ")");
      System.out.println("homeImplClass(" + var1.getHomeClassName() + ")");
   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
   }
}
