package weblogic.management.deploy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.beans.factory.InvalidTargetException;
import weblogic.deploy.internal.TargetHelper;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WebDeploymentMBean;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;

/** @deprecated */
public class DeploymentData implements Serializable {
   private static final long serialVersionUID = -1065644178659248034L;
   public static final int UNKNOWN = 0;
   public static final int CLUSTER = 1;
   public static final int SERVER = 2;
   public static final int VIRTUALHOST = 3;
   public static final int JMSSERVER = 4;
   public static final int SAFAGENT = 5;
   private String[] files;
   private boolean[] isDirectory;
   private HashMap targets = null;
   private boolean deleteFlag = false;
   private boolean isNameFromSource = false;
   private boolean isNewApp = false;
   private boolean isActionFromDeployer = false;
   private String plan = null;
   private String root = null;
   private String config = null;
   private DeploymentOptions deployOpts = new DeploymentOptions();
   private ArrayList globaltargets = new ArrayList();
   private Map moduleTargets = new HashMap();
   private Map allSubModuleTargets = new HashMap();
   public static final String STANDALONE_MODULE = "_the_standalone_module";
   private boolean standaloneModule = false;
   private boolean earmodule = false;
   private boolean planUpdate = false;
   private boolean targetsFromConfig = false;
   private String intendedState;
   private boolean remote = false;
   private boolean thinClient = false;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean enforceClusterConstraints = false;
   private int timeOut = 3600000;
   private String appDD = null;
   private String webDD = null;
   private String deploymentPrincipalName = null;

   public DeploymentData copy() {
      DeploymentData var1 = new DeploymentData();
      var1.setFile(this.files);
      var1.isDirectory = this.isDirectory;
      var1.deleteFlag = this.deleteFlag;
      var1.isNameFromSource = this.isNameFromSource;
      var1.isNewApp = this.isNewApp;
      var1.isActionFromDeployer = this.isActionFromDeployer;
      var1.plan = this.plan;
      var1.root = this.root;
      var1.config = this.config;
      var1.deployOpts = this.deployOpts;
      var1.globaltargets.addAll(this.globaltargets);
      var1.moduleTargets.putAll(this.moduleTargets);
      var1.allSubModuleTargets.putAll(this.allSubModuleTargets);
      var1.standaloneModule = this.standaloneModule;
      var1.earmodule = this.earmodule;
      var1.planUpdate = this.planUpdate;
      var1.targetsFromConfig = this.targetsFromConfig;
      var1.intendedState = this.intendedState;
      var1.remote = this.remote;
      var1.thinClient = this.thinClient;
      return var1;
   }

   public boolean isTargetsFromConfig() {
      return this.targetsFromConfig;
   }

   public void setTargetsFromConfig(boolean var1) {
      this.targetsFromConfig = var1;
   }

   public DeploymentData() {
      this.files = null;
   }

   public DeploymentData(String[] var1) {
      this.files = var1;
   }

   public boolean usesNonExclusiveLock() {
      return this.getDeploymentOptions() != null ? this.getDeploymentOptions().usesNonExclusiveLock() : false;
   }

   public boolean isPlanUpdate() {
      return this.planUpdate;
   }

   public void setPlanUpdate(boolean var1) {
      this.planUpdate = var1;
   }

   public boolean hasTargets() {
      return !this.globaltargets.isEmpty() || !this.moduleTargets.isEmpty() || !this.allSubModuleTargets.isEmpty();
   }

   public String[] getGlobalTargets() {
      return (String[])((String[])this.globaltargets.toArray(new String[this.globaltargets.size()]));
   }

   public boolean hasGlobalTarget(String var1) {
      return this.globaltargets.contains(var1);
   }

   public void addGlobalTarget(String var1) {
      if (!this.hasGlobalTarget(var1)) {
         this.globaltargets.add(var1);
      }

   }

   public void removeGlobalTarget(String var1) {
      if (this.hasGlobalTarget(var1)) {
         this.globaltargets.remove(var1);
      }

   }

   public void addGlobalTargets(String[] var1) {
      if (var1 != null && var1.length != 0) {
         this.addGlobalTargets(Arrays.asList(var1));
      }
   }

   public void addGlobalTargets(List var1) {
      if (var1 != null && !var1.isEmpty()) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            this.addGlobalTarget((String)var2.next());
         }

      }
   }

   public void setGlobalTargets(String[] var1) {
      this.globaltargets = new ArrayList(var1.length);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.globaltargets.add(var1[var2]);
      }

   }

   public void addOrUpdateModuleTargets(Map var1) {
      if (var1 != null && !var1.isEmpty()) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            this.addModuleTargets((String)var3.getKey(), (String[])((String[])var3.getValue()));
         }

      }
   }

   public Map getAllModuleTargets() {
      return this.moduleTargets;
   }

   public boolean hasModuleTargets() {
      return !this.moduleTargets.isEmpty();
   }

   public String[] getModuleTargets(String var1) {
      return (String[])((String[])this.moduleTargets.get(var1));
   }

   public void addModuleTarget(String var1, String var2) {
      if (var2 != null && var1 != null) {
         String[] var3 = (String[])((String[])this.moduleTargets.get(var1));
         String[] var4;
         if (var3 == null) {
            var4 = new String[]{var2};
         } else {
            var4 = this.merge(var3, new String[]{var2});
         }

         this.moduleTargets.put(var1, var4);
      } else {
         throw new IllegalArgumentException("Parameters must not be null");
      }
   }

   public void addModuleTargets(String var1, String[] var2) {
      if (var2 != null && var2.length != 0 && var1 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.addModuleTarget(var1, var2[var3]);
         }

      } else {
         throw new IllegalArgumentException("No module targets to add.");
      }
   }

   public boolean isStandaloneModule() {
      return this.standaloneModule;
   }

   public void addSubModuleTarget(String var1, String var2, String[] var3) throws IllegalArgumentException {
      if (var3 != null && var3.length != 0 && var2 != null) {
         var1 = this.validateModuleName(var1);
         Object var4 = (Map)this.allSubModuleTargets.get(var1);
         if (var4 == null) {
            var4 = new HashMap();
            ((Map)var4).put(var2, var3);
         } else {
            String[] var5 = (String[])((String[])((Map)var4).get(var2));
            if (var5 == null) {
               ((Map)var4).put(var2, var3);
            } else {
               ((Map)var4).put(var2, this.merge(var5, var3));
            }
         }

         this.allSubModuleTargets.put(var1, var4);
      } else {
         throw new IllegalArgumentException("Parameters must not be null");
      }
   }

   public boolean isSubModuleTargeted(String var1, String var2) {
      var1 = this.validateModuleName(var1);
      Map var3 = (Map)this.allSubModuleTargets.get(var1);
      if (var3 == null) {
         return false;
      } else {
         String[] var4 = (String[])((String[])var3.get(var2));
         return var4 != null;
      }
   }

   public void addOrUpdateSubModuleTargetsFor(String var1, Map var2) {
      if (var2 != null && !var2.isEmpty()) {
         Iterator var3 = var2.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            String var5 = (String)var4.getKey();
            String[] var6 = (String[])((String[])var4.getValue());
            this.addSubModuleTarget(var1, var5, var6);
         }

      }
   }

   public void addOrUpdateSubModuleTargets(Map var1) {
      if (var1 != null && !var1.isEmpty()) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            String var4 = (String)var3.getKey();
            Map var5 = (Map)var3.getValue();
            this.addOrUpdateSubModuleTargetsFor(var4, var5);
         }

      }
   }

   public Set getAllTargetedServers(Set var1) throws InvalidTargetException {
      HashSet var2 = new HashSet();
      DomainMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      TargetMBean[] var4 = TargetHelper.lookupTargetMBeans(var3, (String[])((String[])var1.toArray(new String[var1.size()])));

      for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
         var2.addAll(var4[var5].getServerNames());
      }

      return var2;
   }

   public Set getAllTargetedServers(Set var1, DomainMBean var2) throws InvalidTargetException {
      HashSet var3 = new HashSet();
      DomainMBean var4 = ManagementService.getRuntimeAccess(kernelId).getDomain();

      TargetMBean[] var5;
      try {
         var5 = TargetHelper.lookupTargetMBeans(var2, (String[])((String[])var1.toArray(new String[var1.size()])));
      } catch (InvalidTargetException var7) {
         var5 = TargetHelper.lookupTargetMBeans(var4, (String[])((String[])var1.toArray(new String[var1.size()])));
      }

      for(int var6 = 0; var5 != null && var6 < var5.length; ++var6) {
         var3.addAll(var5[var6].getServerNames());
      }

      return var3;
   }

   public Set getAllLogicalTargets() {
      HashSet var1 = new HashSet();
      var1.addAll(this.globaltargets);
      Iterator var2 = this.moduleTargets.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String[] var4 = (String[])((String[])this.moduleTargets.get(var3));
         var1.addAll(Arrays.asList((Object[])var4));
      }

      var2 = this.allSubModuleTargets.values().iterator();

      while(var2.hasNext()) {
         Map var6 = (Map)var2.next();
         Iterator var7 = var6.values().iterator();

         while(var7.hasNext()) {
            String[] var5 = (String[])((String[])var7.next());
            var1.addAll(Arrays.asList((Object[])var5));
         }
      }

      return var1;
   }

   private String[] merge(String[] var1, String[] var2) {
      HashSet var3 = new HashSet(Arrays.asList(var1));
      var3.addAll(Arrays.asList(var2));
      String[] var4 = new String[var3.size()];
      var4 = (String[])((String[])var3.toArray(var4));
      return var4;
   }

   private String validateModuleName(String var1) throws IllegalArgumentException {
      if (var1 == null) {
         if (this.earmodule) {
            throw new IllegalArgumentException(DeployerRuntimeLogger.logAppSubModuleTargetErr());
         }

         this.standaloneModule = true;
         var1 = "_the_standalone_module";
      } else if (this.standaloneModule) {
         var1 = "_the_standalone_module";
      } else {
         this.earmodule = true;
      }

      return var1;
   }

   public Map getSubModuleTargets(String var1) {
      return (Map)this.allSubModuleTargets.get(var1);
   }

   public Map getAllSubModuleTargets() {
      return this.allSubModuleTargets;
   }

   public boolean hasSubModuleTargets() {
      return !this.allSubModuleTargets.isEmpty();
   }

   public void addTargetsFromConfig(BasicDeploymentMBean var1) {
      this.addGlobalTargets(var1.getTargets());
      SubDeploymentMBean[] var2 = var1.getSubDeployments();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         TargetMBean[] var4 = var2[var3].getTargets();
         if (var4 != null && var4.length > 0) {
            this.addModuleTargets(var2[var3].getName(), this.getNames(var4));
         }

         this.addSubModTargetsFromConfig(var2[var3].getSubDeployments(), var2[var3].getName());
      }

      this.setTargetsFromConfig(true);
   }

   private void addSubModTargetsFromConfig(TargetInfoMBean[] var1, String var2) {
      for(int var3 = 0; var1 != null && var3 < var1.length; ++var3) {
         TargetMBean[] var4 = var1[var3].getTargets();
         if (var4 != null && var4.length > 0) {
            this.addSubModuleTarget(var2, var1[var3].getName(), this.getNames(var4));
         }
      }

   }

   private String[] getNames(TargetMBean[] var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = var1[var3].getName();
         }

         return var2;
      }
   }

   private void addGlobalTargets(TargetMBean[] var1) {
      for(int var2 = 0; var1 != null && var2 < var1.length; ++var2) {
         TargetMBean var3 = var1[var2];
         this.addGlobalTarget(var3.getName());
      }

   }

   public String getSecurityModel() {
      return this.deployOpts.getSecurityModel();
   }

   public void setSecurityModel(String var1) throws IllegalArgumentException {
      this.deployOpts.setSecurityModel(var1);
   }

   public boolean isSecurityValidationEnabled() {
      return this.deployOpts.isSecurityValidationEnabled();
   }

   public void setSecurityValidationEnabled(boolean var1) {
      this.deployOpts.setSecurityValidationEnabled(var1);
   }

   public boolean isLibrary() {
      return this.deployOpts.isLibrary();
   }

   public void setLibrary(boolean var1) {
      this.deployOpts.setLibrary(var1);
   }

   public void setFile(String[] var1) {
      this.files = var1;
   }

   public void addFiles(String[] var1) {
      HashSet var2 = new HashSet();
      var2.addAll(Arrays.asList(this.files));

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.add(var1[var3]);
      }

      this.files = new String[var2.size()];
      this.files = (String[])((String[])var2.toArray(this.files));
   }

   public void setDelete(boolean var1) {
      this.deleteFlag = var1;
   }

   public boolean getDelete() {
      return this.deleteFlag;
   }

   public void setClusterConstraints(boolean var1) {
      this.enforceClusterConstraints = var1;
   }

   public boolean getClusterConstraints() {
      return this.enforceClusterConstraints;
   }

   public int getTimeOut() {
      return this.timeOut;
   }

   public void setTimeOut(int var1) {
      this.timeOut = var1;
   }

   public void setAltDescriptorPath(String var1) {
      this.appDD = var1;
   }

   public String getAltDescriptorPath() {
      if (this.appDD == null && this.getDeploymentOptions() != null) {
         this.appDD = this.getDeploymentOptions().getAltDD();
      }

      return this.appDD;
   }

   public void setAltWLSDescriptorPath(String var1) {
      this.webDD = var1;
   }

   public String getAltWLSDescriptorPath() {
      if (this.webDD == null && this.getDeploymentOptions() != null) {
         this.webDD = this.getDeploymentOptions().getAltWlsDD();
      }

      return this.webDD;
   }

   public void setDeploymentPrincipalName(String var1) {
      this.deploymentPrincipalName = var1;
   }

   public String getDeploymentPrincipalName() {
      if (this.deploymentPrincipalName == null && this.getDeploymentOptions() != null) {
         this.deploymentPrincipalName = this.getDeploymentOptions().getDeploymentPrincipalName();
      }

      return this.deploymentPrincipalName;
   }

   public String[] getFiles() {
      return this.files;
   }

   public boolean hasFiles() {
      return this.files != null && this.files.length > 0;
   }

   public int getTargetType(String var1) {
      return TargetHelper.getTypeForTarget(var1);
   }

   public void setTargetType(String var1, int var2) {
   }

   public boolean getIsNameFromSource() {
      return this.isNameFromSource;
   }

   public void setIsNameFromSource(boolean var1) {
      this.isNameFromSource = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Delete Files:" + this.getDelete() + "\n");
      var1.append("Timeout :" + this.getTimeOut() + "\n");
      var1.append("Targets: \n");
      if (this.globaltargets != null) {
         Iterator var2 = this.globaltargets.iterator();

         while(var2.hasNext()) {
            var1.append((String)var2.next());
            var1.append("\n");
         }
      }

      var1.append(this.getModuleTargetsAsString());
      var1.append(this.getSubModuleTargetsAsString());
      var1.append("Files: \n");
      String[] var4 = this.getFiles();
      if (var4 != null) {
         for(int var3 = 0; var3 < var4.length; ++var3) {
            var1.append(var4[var3]);
            if (this.isDirectory != null) {
               var1.append(" - " + (this.isDirectory[var3] ? "Directory" : "File"));
            }

            var1.append("\n");
         }
      } else {
         var1.append("null\n");
      }

      var1.append("Deployment Plan: ");
      var1.append(this.plan);
      var1.append("\n");
      var1.append("App root: ").append(this.root).append("\n");
      var1.append("App config: ").append(this.config).append("\n");
      var1.append("Deployment Options: ").append(this.deployOpts).append("\n");
      return var1.toString();
   }

   private String getSubModuleTargetsAsString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("SubModuleTargets=");
      var1.append("{");
      if (this.allSubModuleTargets.isEmpty()) {
         var1.append("}");
      } else {
         Iterator var2 = this.allSubModuleTargets.entrySet().iterator();

         while(var2.hasNext()) {
            var1.append("\n");
            Map.Entry var3 = (Map.Entry)var2.next();
            var1.append(var3.getKey()).append("=");
            var1.append(toStringOfModuleTargetsOrSubmoduleTargetsMap((Map)var3.getValue())).append(", ");
         }
      }

      var1.append("\n").append("}").append("\n");
      return var1.toString();
   }

   private String getModuleTargetsAsString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ModuleTargets=");
      var1.append(toStringOfModuleTargetsOrSubmoduleTargetsMap(this.moduleTargets));
      var1.append("\n");
      return var1.toString();
   }

   private static String toStringOfModuleTargetsOrSubmoduleTargetsMap(Map var0) {
      StringBuffer var1 = new StringBuffer();
      if (var0.isEmpty()) {
         var1.append(var0);
      } else {
         Iterator var2 = var0.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            var1.append(var3.getKey()).append("=");
            var1.append(getStringArrayAsString((String[])((String[])var3.getValue())));
            var1.append(", ");
         }
      }

      return var1.toString();
   }

   private static String getStringArrayAsString(String[] var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append("{");
      if (var0 != null && var0.length != 0) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var2 != 0) {
               var1.append(", ");
            }

            var1.append(var0[var2]);
         }

         var1.append("}");
         return var1.toString();
      } else {
         var1.append("}");
         return var1.toString();
      }
   }

   public boolean isNewApplication() {
      return this.isNewApp;
   }

   public void setNewApp(boolean var1) {
      this.isNewApp = var1;
   }

   public boolean isActionFromDeployer() {
      return this.isActionFromDeployer;
   }

   public void setActionFromDeployer(boolean var1) {
      this.isActionFromDeployer = var1;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this.globaltargets == null) {
         this.globaltargets = new ArrayList();
      }

      if (this.moduleTargets == null) {
         this.moduleTargets = new HashMap();
      }

      if (this.allSubModuleTargets == null) {
         this.allSubModuleTargets = new HashMap();
      }

      if (this.deployOpts == null) {
         this.deployOpts = new DeploymentOptions();
      }

      if (this.targets != null && !this.targets.isEmpty()) {
         this.populateFromTargetInfos();
      }

   }

   private void populateFromTargetInfos() {
      Debug.assertion(this.targets != null);
      Iterator var1 = this.targets.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         TargetInfo var3 = (TargetInfo)this.targets.get(var2);
         String[] var4 = var3.getModules();
         if (var4 == null) {
            this.addGlobalTarget(var2);
         } else {
            this.addTargetToModules(var4, var2);
         }
      }

   }

   public String getDeploymentPlan() {
      return this.plan;
   }

   public void setDeploymentPlan(String var1) {
      this.plan = var1;
   }

   public String getConfigDirectory() {
      return this.config;
   }

   public void setConfigDirectory(String var1) {
      this.config = var1;
   }

   public String getRootDirectory() {
      return this.root;
   }

   public void setRootDirectory(String var1) {
      this.root = var1;
   }

   public DeploymentOptions getDeploymentOptions() {
      return this.deployOpts;
   }

   public void setDeploymentOptions(DeploymentOptions var1) {
      if (var1 == null) {
         var1 = new DeploymentOptions();
      }

      this.deployOpts = var1;
   }

   /** @deprecated */
   public String[] getTargets() {
      if (this.globaltargets.isEmpty() && this.moduleTargets.isEmpty()) {
         return null;
      } else {
         HashSet var1 = new HashSet();
         var1.addAll(this.globaltargets);
         if (!this.moduleTargets.isEmpty()) {
            String[] var2 = this.getModules();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               var1.addAll(Arrays.asList((Object[])this.getModuleTargets(var2[var3])));
            }
         }

         ArrayList var4 = new ArrayList(var1);
         return (String[])((String[])var4.toArray(new String[var4.size()]));
      }
   }

   /** @deprecated */
   public void addTarget(String var1, String[] var2) {
      if (var2 == null) {
         this.addGlobalTarget(var1);
      } else {
         this.addTargetToModules(var2, var1);
      }

   }

   private void addTargetToModules(String[] var1, String var2) {
      if (var1 != null && var1.length != 0 && var2 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.addModuleTargets(var1[var3], new String[]{var2});
         }

      } else {
         throw new IllegalArgumentException("Parameters must be non null");
      }
   }

   /** @deprecated */
   public String[] getModulesForTarget(String var1) {
      if (this.globaltargets.contains(var1)) {
         return null;
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.moduleTargets.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            String[] var5 = (String[])((String[])this.moduleTargets.get(var4));
            if (Arrays.asList((Object[])var5).contains(var1)) {
               var2.add(var4);
            }
         }

         return (String[])((String[])var2.toArray(new String[0]));
      }
   }

   /** @deprecated */
   public void addModuleToTarget(String var1, String var2) {
      if (this.globaltargets.contains(var1)) {
         this.globaltargets.remove(var1);
         this.addModuleTargets(var2, new String[]{var1});
      }

   }

   /** @deprecated */
   public void addTargetsForComponent(ApplicationMBean var1, String var2) throws IllegalArgumentException {
      ComponentMBean[] var3 = var1.getComponents();
      if (var1 != null && var3 != null) {
         if (var2 == null) {
            throw new IllegalArgumentException("No component provided");
         } else {
            for(int var5 = 0; var5 < var3.length; ++var5) {
               ComponentMBean var4 = var3[var5];
               if (var4.getName().equals(var2)) {
                  TargetMBean[] var6 = var4.getTargets();

                  for(int var7 = 0; var7 < var6.length; ++var7) {
                     this.addModuleTargets(var4.getName(), new String[]{var6[var7].getName()});
                  }

                  if (var4 instanceof WebDeploymentMBean) {
                     VirtualHostMBean[] var9 = ((WebDeploymentMBean)var4).getVirtualHosts();

                     for(int var8 = 0; var8 < var9.length; ++var8) {
                        this.addModuleTargets(var4.getName(), new String[]{var9[var8].getName()});
                     }
                  }
                  break;
               }
            }

         }
      } else {
         throw new IllegalArgumentException("No application provided");
      }
   }

   /** @deprecated */
   public Set getTargetsForModule(String var1) {
      if (this.globaltargets.isEmpty() && this.moduleTargets.isEmpty()) {
         return new HashSet(0);
      } else {
         String[] var2 = this.getModuleTargets(var1);
         return var2 == null ? new HashSet(this.globaltargets) : new HashSet(Arrays.asList((Object[])var2));
      }
   }

   /** @deprecated */
   public String[] getModules() {
      return (String[])((String[])this.moduleTargets.keySet().toArray(new String[this.moduleTargets.size()]));
   }

   void setAllSubModuleTargets(Map var1) {
      this.allSubModuleTargets = var1;
   }

   public void setIntendedState(String var1) {
      this.intendedState = var1;
   }

   public String getIntendedState() {
      return this.intendedState;
   }

   public void setRemote(boolean var1) {
      this.remote = var1;
   }

   public boolean isRemote() {
      return this.remote;
   }

   public boolean hasNoTargets() {
      String[] var1 = this.getGlobalTargets();
      if (var1 != null && var1.length > 0) {
         return false;
      } else {
         return !this.hasModuleTargets() && !this.hasSubModuleTargets();
      }
   }

   public void removeCommonTargets(DeploymentData var1, boolean var2) {
      this.removeCommonGlobalTargets(var1, var2);
      this.removeCommonModuleTargets(var1, var2);
      this.removeCommonSubModuleTargets(var1, var2);
   }

   public void removeCommonGlobalTargets(DeploymentData var1, boolean var2) {
      String[] var3 = this.getGlobalTargets();
      if (var3 != null && var3.length != 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var1.hasGlobalTarget(var3[var4])) {
               if (var2) {
                  this.removeGlobalTarget(var3[var4]);
               }

               var1.removeGlobalTarget(var3[var4]);
            }
         }

      }
   }

   public void removeCommonModuleTargets(DeploymentData var1, boolean var2) {
      if (this.hasModuleTargets() && var1.hasModuleTargets()) {
         HashMap var3 = new HashMap(this.getAllModuleTargets());
         Iterator var4 = var3.keySet().iterator();

         while(true) {
            String var5;
            String[] var6;
            do {
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  var5 = (String)var4.next();
                  var6 = this.getModuleTargets(var5);
               } while(var6 == null);
            } while(var6.length <= 0);

            for(int var7 = 0; var7 < var6.length; ++var7) {
               boolean var8 = var1.removeModuleTarget(var5, var6[var7]);
               if (var8 && var2) {
                  this.removeModuleTarget(var5, var6[var7]);
               }
            }
         }
      }
   }

   public void removeCommonSubModuleTargets(DeploymentData var1, boolean var2) {
      if (this.hasSubModuleTargets() && var1.hasSubModuleTargets()) {
         HashMap var3 = new HashMap(this.getAllSubModuleTargets());
         Iterator var4 = var3.keySet().iterator();

         label45:
         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            HashMap var6 = new HashMap((Map)var3.get(var5));
            Iterator var7 = var6.keySet().iterator();

            while(true) {
               String var8;
               String[] var9;
               do {
                  do {
                     if (!var7.hasNext()) {
                        continue label45;
                     }

                     var8 = (String)var7.next();
                     var9 = (String[])((String[])var6.get(var8));
                  } while(var9 == null);
               } while(var9.length <= 0);

               for(int var10 = 0; var10 < var9.length; ++var10) {
                  boolean var11 = var1.removeSubModuleTarget(var5, var8, var9[var10]);
                  if (var11 && var2) {
                     this.removeSubModuleTarget(var5, var8, var9[var10]);
                  }
               }
            }
         }

      }
   }

   public boolean removeModuleTarget(String var1, String var2) {
      if (var2 != null && var1 != null) {
         String[] var3 = (String[])((String[])this.moduleTargets.get(var1));
         if (var3 != null && var3.length != 0) {
            ArrayList var4 = new ArrayList(Arrays.asList(var3));
            int var5 = var4.size();
            var4.remove(var2);
            int var6 = var4.size();
            if (var5 != var6) {
               var3 = new String[var4.size()];
               if (var3.length == 0) {
                  this.moduleTargets.remove(var1);
               } else {
                  var3 = (String[])((String[])var4.toArray(var3));
                  this.moduleTargets.put(var1, var3);
               }

               return true;
            } else {
               return false;
            }
         } else {
            this.moduleTargets.remove(var1);
            return false;
         }
      } else {
         throw new IllegalArgumentException("Parameters must not be null");
      }
   }

   public boolean removeSubModuleTarget(String var1, String var2, String var3) {
      if (var1 != null && var2 != null && var3 != null) {
         Map var4 = (Map)this.allSubModuleTargets.get(var1);
         if (var4 != null && !var4.isEmpty()) {
            String[] var5 = (String[])((String[])var4.get(var2));
            if (var5 != null && var5.length != 0) {
               ArrayList var6 = new ArrayList(Arrays.asList(var5));
               int var7 = var6.size();
               var6.remove(var3);
               int var8 = var6.size();
               if (var7 != var8) {
                  var5 = new String[var6.size()];
                  if (var5.length == 0) {
                     var4.remove(var2);
                     if (var4.isEmpty()) {
                        this.allSubModuleTargets.remove(var1);
                     }
                  } else {
                     var5 = (String[])((String[])var6.toArray(var5));
                     var4.put(var2, var5);
                     this.allSubModuleTargets.put(var1, var4);
                  }

                  return true;
               } else {
                  return false;
               }
            } else {
               var4.remove(var2);
               return false;
            }
         } else {
            this.allSubModuleTargets.remove(var1);
            return false;
         }
      } else {
         throw new IllegalArgumentException("Parameters must not be null");
      }
   }

   public void setThinClient(boolean var1) {
      this.thinClient = var1;
   }

   public boolean isThinClient() {
      return this.thinClient;
   }

   class TargetInfo implements Serializable {
      static final long serialVersionUID = -5379313404023125526L;
      private String target;
      private HashSet modules = null;
      private int type = 0;

      TargetInfo(String var2, String[] var3) {
         this.target = var2;
         if (var3 != null) {
            this.modules = new HashSet(var3.length);

            for(int var4 = 0; var4 < var3.length; ++var4) {
               this.modules.add(var3[var4]);
            }
         }

      }

      String getTarget() {
         return this.target;
      }

      String[] getModules() {
         String[] var1 = null;
         if (this.modules != null) {
            var1 = (String[])((String[])this.modules.toArray(new String[0]));
         }

         return var1;
      }

      void addModule(String var1) {
         if (this.modules == null) {
            this.modules = new HashSet();
         }

         if (var1 != null) {
            this.modules.add(var1);
         }

      }

      int getType() {
         return this.type;
      }

      void setType(int var1) {
         this.type = var1;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(this.getTarget());
         var1.append(": (Modules: ");
         if (this.modules != null) {
            Iterator var2 = this.modules.iterator();

            while(var2.hasNext()) {
               var1.append((String)var2.next());
               var1.append(" ");
            }
         } else {
            var1.append("null");
         }

         var1.append(")\n");
         return var1.toString();
      }
   }
}
