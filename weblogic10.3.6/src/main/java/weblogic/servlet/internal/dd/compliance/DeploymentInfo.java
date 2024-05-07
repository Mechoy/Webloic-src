package weblogic.servlet.internal.dd.compliance;

import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.validation.ModuleValidationInfo;

public class DeploymentInfo {
   private WebAppBean bean;
   private WeblogicWebAppBean wlBean;
   private ClassLoader classLoader;
   private ModuleValidationInfo moduleValidationInfo;
   private boolean verbose;
   private boolean isWebServiceModule;

   public DeploymentInfo() {
   }

   public DeploymentInfo(WebAppBean var1, WeblogicWebAppBean var2) {
      this.bean = var1;
      this.wlBean = var2;
   }

   public WebAppBean getWebAppBean() {
      return this.bean;
   }

   public void setWebAppBean(WebAppBean var1) {
      this.bean = var1;
   }

   public WeblogicWebAppBean getWeblogicWebAppBean() {
      return this.wlBean;
   }

   public void setWeblogicWebAppBean(WeblogicWebAppBean var1) {
      this.wlBean = var1;
   }

   public ClassLoader getClassLoader() {
      return this.classLoader;
   }

   public void setClassLoader(ClassLoader var1) {
      this.classLoader = var1;
   }

   public ModuleValidationInfo getModuleValidationInfo() {
      return this.moduleValidationInfo;
   }

   public void setModuleValidationInfo(ModuleValidationInfo var1) {
      this.moduleValidationInfo = var1;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public void setIsWebServiceModule(boolean var1) {
      this.isWebServiceModule = var1;
   }

   public boolean isWebServiceModule() {
      return this.isWebServiceModule;
   }
}
