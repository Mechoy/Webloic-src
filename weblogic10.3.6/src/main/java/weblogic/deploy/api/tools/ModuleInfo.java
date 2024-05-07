package weblogic.deploy.api.tools;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;

public class ModuleInfo {
   protected String name = null;
   protected Set subModules = new HashSet();
   protected ModuleType type;
   protected boolean archived;
   protected ModuleInfo parent;
   protected String[] roots;
   protected String[] webservices;
   protected String[] beans;
   protected String[] subDeployments;

   protected ModuleInfo() {
      this.type = WebLogicModuleType.UNKNOWN;
      this.archived = false;
      this.parent = null;
      this.roots = null;
   }

   protected static ModuleInfo createModuleInfo(WebLogicDeployableObject var0) throws IOException, ConfigurationException {
      return createModuleInfo(var0, (WebLogicDeploymentConfiguration)null, (String)null);
   }

   protected static ModuleInfo createModuleInfo(WebLogicDeployableObject var0, WebLogicDeploymentConfiguration var1, String var2) throws IOException, ConfigurationException {
      ConfigHelper.checkParam("WebLogicDeployableObject", var0);
      return new DeployableObjectInfo(var0, var1, var2);
   }

   private ModuleInfo createModuleInfo(WeblogicModuleBean var1, DescriptorBean var2) {
      if ("JMS".equals(var1.getType())) {
         return new ModuleBeanInfo(var1, WebLogicModuleType.JMS, var2);
      } else if ("JDBC".equals(var1.getType())) {
         return new ModuleBeanInfo(var1, WebLogicModuleType.JDBC, var2);
      } else {
         return "Interception".equals(var1.getType()) ? new ModuleBeanInfo(var1, WebLogicModuleType.INTERCEPT, var2) : null;
      }
   }

   public String getName() {
      return this.name;
   }

   public ModuleInfo[] getSubModules() {
      return (ModuleInfo[])((ModuleInfo[])this.subModules.toArray(new ModuleInfo[0]));
   }

   public ModuleType getType() {
      return this.type;
   }

   public boolean isArchived() {
      return this.archived;
   }

   public boolean isWebService() {
      return this.webservices != null && this.webservices.length > 0;
   }

   public String[] getWebServices() {
      return this.webservices;
   }

   public String[] getContextRoots() {
      return this.roots;
   }

   public String[] getBeans() {
      return this.beans;
   }

   public String[] getSubDeployments() {
      return this.subDeployments;
   }

   protected boolean addModuleInfo(ModuleInfo var1) {
      boolean var2 = this.subModules.add(var1);
      if (var2) {
         var1.setParent(this);
      }

      return var2;
   }

   protected boolean addModuleInfo(WeblogicModuleBean var1, DescriptorBean var2) {
      ModuleInfo var3 = this.createModuleInfo(var1, var2);
      return var3 != null ? this.addModuleInfo(var3) : false;
   }

   protected ModuleInfo getParent() {
      return this.parent;
   }

   private void setParent(ModuleInfo var1) {
      this.parent = var1;
   }

   protected boolean checkIfArchived(WebLogicDeployableObject var1) {
      File var2 = var1.getArchive();
      if (var2.isDirectory()) {
         return false;
      } else {
         return !var2.getName().endsWith(".xml");
      }
   }
}
