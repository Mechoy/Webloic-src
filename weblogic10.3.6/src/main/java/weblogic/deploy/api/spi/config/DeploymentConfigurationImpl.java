package weblogic.deploy.api.spi.config;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.model.J2eeApplicationObject;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.DConfigBeanRoot;
import javax.enterprise.deploy.spi.exceptions.BeanNotFoundException;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ClassLoaderControl;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.internal.utils.InstallDir;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.model.internal.DDBeanRootImpl;
import weblogic.deploy.api.model.sca.ScaApplicationObject;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.WebLogicDConfigBeanRoot;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.descriptor.internal.DescriptorHelper;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.EntityDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;

public class DeploymentConfigurationImpl implements WebLogicDeploymentConfiguration, PropertyChangeListener {
   private static final boolean debug = Debug.isDebug("config");
   private DeployableObject dObject;
   private ArrayList dObjects = new ArrayList();
   private Map rootMap = new HashMap();
   private Map ddBeanRoots = new HashMap();
   private DeploymentPlanBean plan = null;
   private String appName;
   private InstallDir installDir = null;
   private boolean planRestored = false;
   private boolean extDDWrite = false;
   private DescriptorHelper ddhelper = null;

   public void close() {
      this.unregisterListener(this.plan);
      this.dObject = null;
      this.dObjects.clear();
      this.rootMap.clear();
      Iterator var1 = this.ddBeanRoots.values().iterator();

      while(var1.hasNext()) {
         WebLogicDConfigBeanRoot var2 = (WebLogicDConfigBeanRoot)var1.next();
         if (var2 != null) {
            var2.close();
         }
      }

      this.ddBeanRoots.clear();
      this.plan = null;
      this.appName = null;
      this.installDir.resetInstallDir((File)null);
      this.installDir = null;
   }

   public DeploymentConfigurationImpl(DeployableObject var1) throws InvalidModuleException, IOException {
      ConfigHelper.checkParam("DeployableObject", var1);
      DescriptorSupportManager.flush();
      this.dObject = var1;
      this.initialize();
      this.plan = this.buildPlan();
   }

   public DeployableObject getDeployableObject() {
      return this.dObject;
   }

   private WebLogicDConfigBeanRoot findRoot(DDBeanRoot var1) {
      Iterator var2 = this.ddBeanRoots.entrySet().iterator();

      Map.Entry var3;
      DDBeanRoot var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Map.Entry)var2.next();
         var4 = (DDBeanRoot)var3.getKey();
      } while(!var4.equals(var1));

      return (WebLogicDConfigBeanRoot)var3.getValue();
   }

   public DConfigBeanRoot getDConfigBeanRoot(DDBeanRoot var1) throws ConfigurationException {
      ConfigHelper.checkParam("DDBeanRoot", var1);
      WebLogicDConfigBeanRoot var2 = this.findRoot(var1);
      if (var2 == null) {
         DescriptorSupport var5 = null;
         Iterator var6 = this.rootMap.entrySet().iterator();
         String var7 = this.getRootTag(var1);

         while(var6.hasNext()) {
            Map.Entry var8 = (Map.Entry)var6.next();
            DeployableObjectKey var9 = (DeployableObjectKey)var8.getKey();
            String var3 = var9.getName();
            DDBeanRoot var4 = (DDBeanRoot)var8.getValue();
            if (var4 != null && var4.equals(var1)) {
               ConfigurationException var11;
               try {
                  var5 = DescriptorSupportManager.getForTag(var7);
                  if (var5 == null) {
                     throw new AssertionError("Inconsistent results from DescriptorSupport");
                  }

                  var2 = this.initDConfig(var1, var3, var5);
                  if (var2 != null) {
                     DeployableObjectKey var10 = new DeployableObjectKey(var3, var1.getDeployableObject().getType(), var9.getContextRoot());
                     this.rootMap.put(var10, var1);
                     this.ddBeanRoots.put(var1, var2);
                     this.addToPlan(var2);
                     this.collectSecondaryConfig(var2);
                  }
                  break;
               } catch (InvalidModuleException var12) {
                  var11 = new ConfigurationException(var12.toString());
                  var11.initCause(var12);
                  throw var11;
               } catch (IOException var13) {
                  var11 = new ConfigurationException(SPIDeployerLogger.parseError(var3, var5.getConfigURI(), var13.getMessage()));
                  var11.initCause(var13);
                  throw var11;
               }
            }
         }

         if (var2 == null) {
            throw new AssertionError("DDBeanRoot not part of any DeployableObject");
         }
      }

      return var2;
   }

   public String getModuleName(DDBeanRoot var1) {
      ConfigHelper.checkParam("DDBeanRoot", var1);
      Iterator var4 = this.rootMap.entrySet().iterator();

      DDBeanRoot var2;
      String var3;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         Map.Entry var5 = (Map.Entry)var4.next();
         DeployableObjectKey var6 = (DeployableObjectKey)var5.getKey();
         var3 = var6.getName();
         var2 = (DDBeanRoot)var5.getValue();
      } while(!var1.equals(var2));

      return var3;
   }

   public void removeDConfigBean(DConfigBeanRoot var1) throws BeanNotFoundException {
      if (var1 == null) {
         throw new BeanNotFoundException(SPIDeployerLogger.nullDCB());
      } else {
         Iterator var2 = this.ddBeanRoots.entrySet().iterator();

         Map.Entry var3;
         for(var3 = null; var2.hasNext(); var3 = null) {
            var3 = (Map.Entry)var2.next();
            if (var1.equals(var3.getValue())) {
               break;
            }
         }

         if (var3 == null) {
            this.removeSecondaryDConfigBean(var1);
         }

         ((BasicDConfigBeanRoot)var1).removeAllChildren();
         var3.setValue((Object)null);
         this.removeFromPlan((BasicDConfigBeanRoot)var1);
         SPIDeployerLogger.logRemoveDCB(this.getModuleName((DDBeanRoot)var3.getKey()));
      }
   }

   public DConfigBeanRoot restoreDConfigBean(InputStream var1, DDBeanRoot var2) throws ConfigurationException {
      ConfigHelper.checkParam("InputStream", var1);
      ConfigHelper.checkParam("DDBeanRoot", var2);
      WebLogicDConfigBeanRoot var3 = null;
      WebLogicDConfigBeanRoot var4 = this.findRoot(var2);
      DeploymentPlanBean var5 = this.plan;

      try {
         String var9 = null;
         DescriptorSupport var6;
         if (var4 != null) {
            var6 = var4.getDescriptorSupport();
         } else {
            var9 = this.getRootTag(var2);
            var6 = DescriptorSupportManager.getForTag(var9);
         }

         if (var6 == null) {
            throw new ConfigurationException(SPIDeployerLogger.badDDBean(var9));
         } else {
            var3 = this.constructDCB(var2, var1, var6.getDConfigClassName(), this.getModuleName(var2), var6);
            this.ddBeanRoots.put(var2, var3);
            this.removeFromPlan((BasicDConfigBeanRoot)var4);
            this.addToPlan(var3);
            SPIDeployerLogger.logRestoreDCB(this.getModuleName(var2));
            return var3;
         }
      } catch (Throwable var8) {
         ConfigurationException var7;
         if (var8 instanceof ConfigurationException) {
            var7 = (ConfigurationException)var8;
         } else {
            var7 = new ConfigurationException(var8.toString());
            var7.initCause(var8);
         }

         this.ddBeanRoots.put(var2, var4);
         this.plan = var5;
         throw var7;
      }
   }

   public void saveDConfigBean(OutputStream var1, DConfigBeanRoot var2) throws ConfigurationException {
      ConfigHelper.checkParam("OuputStream", var1);
      ConfigHelper.checkParam("DConfigBeanRoot", var2);

      try {
         this.toXML(((BasicDConfigBean)var2).getDescriptorBean(), var1);
         SPIDeployerLogger.logSaveDCB(this.getModuleName((DDBeanRoot)var2.getDDBean()));
      } catch (IOException var5) {
         ConfigurationException var4 = new ConfigurationException(SPIDeployerLogger.marshalError(((BasicDConfigBeanRoot)var2).getName(), var5.toString()));
         var4.initCause(var5);
         throw var4;
      }
   }

   public void restore(InputStream var1) throws ConfigurationException {
      ConfigHelper.checkParam("InputStream", var1);
      this.restore(this.parsePlan(var1));
   }

   public void restore(DeploymentPlanBean var1) throws ConfigurationException {
      ConfigHelper.checkParam("DeploymentPlanBean", var1);
      if (debug) {
         Debug.say("start restore new plan");
      }

      Map var2 = this.ddBeanRoots;
      DeploymentPlanBean var3 = this.plan;
      String var4 = this.appName;
      InstallDir var5 = this.installDir;
      ArrayList var6 = this.dObjects;

      try {
         this.ddBeanRoots = new HashMap();
         this.dObjects = new ArrayList();
         this.plan = var1;
         this.planRestored = true;
         this.initialize();
         SPIDeployerLogger.logRestore(this.plan.getApplicationName());
      } catch (Throwable var13) {
         ConfigurationException var8;
         if (var13 instanceof ConfigurationException) {
            var8 = (ConfigurationException)var13;
         } else {
            var8 = new ConfigurationException(SPIDeployerLogger.restoreError(var13.getMessage()));
            var8.initCause(var13);
         }

         this.dObjects = var6;
         this.ddBeanRoots = var2;
         this.plan = var3;
         this.appName = var4;
         this.installDir = var5;
         throw var8;
      } finally {
         this.unregisterListener(var3);
      }

   }

   private void unregisterListener(DeploymentPlanBean var1) {
      if (var1 != null) {
         this.getDescriptorHelper().removePropertyChangeListener((DescriptorBean)var1, this);
      }

   }

   public void save(OutputStream var1) throws ConfigurationException {
      ConfigHelper.checkParam("OutputStream", var1);

      try {
         this.writeConfig();
         if (debug) {
            Debug.say("Saving following plan: ");
            this.toXML((DescriptorBean)this.plan, System.out);
         }

         this.toXML((DescriptorBean)this.plan, var1);
         SPIDeployerLogger.logSave(this.appName);
      } catch (IOException var3) {
         throw new ConfigurationException(SPIDeployerLogger.marshalPlanError(var3.toString()));
      }
   }

   private boolean hasExternalDDs() {
      ModuleOverrideBean[] var1 = this.plan.getModuleOverrides();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         ModuleOverrideBean var3 = var1[var2];
         ModuleDescriptorBean[] var4 = var3.getModuleDescriptors();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            ModuleDescriptorBean var6 = var4[var5];
            if (var6.isExternal()) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isPlanRestored() {
      return this.planRestored;
   }

   public InstallDir getInstallDir() {
      return this.installDir;
   }

   public void export(int var1) throws IllegalArgumentException {
      Iterator var2 = this.ddBeanRoots.values().iterator();

      while(true) {
         WebLogicDConfigBeanRoot var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (WebLogicDConfigBeanRoot)var2.next();
         } while(var3 == null);

         var3.export(var1);
         DConfigBean[] var4 = (DConfigBean[])var3.getSecondaryDescriptors();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            WebLogicDConfigBeanRoot var6 = (WebLogicDConfigBeanRoot)var4[var5];
            if (var6 != null) {
               var6.export(var1);
            }
         }
      }
   }

   public DeploymentPlanBean getPlan() {
      return this.plan;
   }

   public void addToPlan(WebLogicDConfigBeanRoot var1) {
      String var3 = var1.getDConfigName();
      ModuleOverrideBean var2 = this.findModuleOverride(var3);
      if (var2 == null) {
         if (debug) {
            Debug.say("creating module override for " + var3);
         }

         var2 = this.plan.createModuleOverride();
         var2.setModuleName(var3);
         var2.setModuleType(var1.getDDBean().getRoot().getType().toString());
      }

      String var4 = var1.getDescriptorSupport().getConfigURI();
      if (debug) {
         Debug.say("creating module override descriptor for " + var1 + " at " + var4);
      }

      this.addDescriptor(var2, var4, var1.getDescriptorSupport().getConfigTag(), var1.isExternal());
      var4 = var1.getDescriptorSupport().getBaseURI();
      if (debug) {
         Debug.say("creating module override descriptor for " + var1 + " at " + var4);
      }

      this.addDescriptor(var2, var4, var1.getDescriptorSupport().getBaseTag(), false);
   }

   private void addDescriptor(ModuleOverrideBean var1, String var2, String var3, boolean var4) {
      ModuleDescriptorBean var5 = this.findModuleDescriptor(var1, var2);
      if (var5 == null) {
         var5 = var1.createModuleDescriptor();
         var5.setRootElement(var3);
         var5.setUri(var2);
         var5.setExternal(var4);
      }

   }

   private ModuleOverrideBean findModuleOverride(String var1) {
      ModuleOverrideBean[] var2 = this.plan.getModuleOverrides();
      if (var2 == null) {
         return null;
      } else {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            ModuleOverrideBean var3 = var2[var4];
            if (var1.equals(var3.getModuleName())) {
               return var3;
            }
         }

         return null;
      }
   }

   private ModuleDescriptorBean findModuleDescriptor(ModuleOverrideBean var1, String var2) {
      if (var1 == null) {
         return null;
      } else {
         ModuleDescriptorBean[] var3 = var1.getModuleDescriptors();
         if (var3 == null) {
            return null;
         } else {
            for(int var5 = 0; var5 < var3.length; ++var5) {
               ModuleDescriptorBean var4 = var3[var5];
               if (var2.equals(var4.getUri())) {
                  return var4;
               }
            }

            return null;
         }
      }
   }

   private void removeFromPlan(BasicDConfigBeanRoot var1) {
      String var4 = var1.getDConfigName();
      ModuleOverrideBean var3 = this.findModuleOverride(var4);
      if (var3 != null) {
         String var5 = var1.getDescriptorSupport().getConfigURI();
         ModuleDescriptorBean var2 = this.findModuleDescriptor(var3, var5);
         if (var2 != null) {
            var3.destroyModuleDescriptor(var2);
         }

         var5 = var1.getDescriptorSupport().getBaseURI();
         var2 = this.findModuleDescriptor(var3, var5);
         if (var2 != null) {
            var3.destroyModuleDescriptor(var2);
         }

         Iterator var6 = Arrays.asList((Object[])var1.getSecondaryDescriptors()).iterator();

         while(var6.hasNext()) {
            this.removeFromPlan((BasicDConfigBeanRoot)var6.next());
         }

      }
   }

   private boolean writeConfig() throws ConfigurationException {
      boolean var2 = false;
      Iterator var3 = this.ddBeanRoots.values().iterator();

      while(var3.hasNext()) {
         try {
            WebLogicDConfigBeanRoot var1 = (WebLogicDConfigBeanRoot)var3.next();
            if (var1 != null) {
               if (this.writeConfigToFile(var1)) {
                  var2 = true;
               }

               if (this.writeSecondaryConfig(var1)) {
                  var2 = true;
               }
            }
         } catch (IOException var5) {
            throw new SPIConfigurationException(var5.toString(), var5);
         }
      }

      return var2;
   }

   private boolean writeSecondaryConfig(WebLogicDConfigBeanRoot var1) throws IOException {
      boolean var2 = false;
      Iterator var3 = Arrays.asList((Object[])var1.getSecondaryDescriptors()).iterator();

      while(var3.hasNext()) {
         if (this.writeConfigToFile((WebLogicDConfigBeanRoot)var3.next())) {
            var2 = true;
         }
      }

      return var2;
   }

   private boolean writeConfigToFile(WebLogicDConfigBeanRoot var1) throws IOException {
      if (var1 == null) {
         return false;
      } else {
         if (debug) {
            Debug.say("hasdd: " + var1.hasDD() + ", is external:" + var1.isExternal() + ", is modified:" + var1.isModified());
         }

         if ((!var1.hasDD() || var1.isExternal()) && var1.isModified()) {
            String var2 = var1.getDescriptorSupport().getConfigURI();
            if (debug) {
               Debug.say("writing dd for module, " + var1.getDConfigName() + ", with uri, " + var2);
            }

            try {
               ((BasicDConfigBeanRoot)var1).clearUnmodifiedElementsFromDescriptor();
               File var3 = this.writeDDToPlanDir(var1, var2, var1.getDescriptorBean());
               ((BasicDConfigBeanRoot)var1).restoreUnmodifiedElementsToDescriptor();
               ModuleDescriptorBean var4 = this.findModuleDescriptor(this.findModuleOverride(var1.getDConfigName()), var2);
               if (var4 != null) {
                  var4.setHashCode(Long.toString(System.currentTimeMillis()));
               }

               SPIDeployerLogger.logSaveDD(var3.getPath());
            } catch (IOException var6) {
               IOException var5 = new IOException(SPIDeployerLogger.marshalError(((BasicDConfigBeanRoot)var1).getName(), var6.toString()));
               var5.initCause(var6);
               throw var5;
            }
         } else if (debug) {
            SPIDeployerLogger.logNoSave(var1.getDConfigName(), var1.hasDD(), !var1.isModified());
         }

         return var1.isExternal();
      }
   }

   private WebLogicDConfigBeanRoot constructDCB(DDBeanRoot var1, Object var2, String var3, String var4, DescriptorSupport var5) throws Throwable {
      ClassLoaderControl var6 = this.pushClassLoader(var1.getDeployableObject());

      WebLogicDConfigBeanRoot var18;
      try {
         Class var7 = Class.forName(var3, false, Thread.currentThread().getContextClassLoader());
         if (debug) {
            Debug.say("name : " + var4);
            Debug.say("Loaded class: " + var7.getName());
            Debug.say("this class loader: " + var7.getClassLoader());

            for(ClassLoader var16 = var7.getClassLoader().getParent(); var16 != null; var16 = var16.getParent()) {
               Debug.say("parent context loader: " + var16);
            }

            URL var9 = Thread.currentThread().getContextClassLoader().getResource("binding.jar");
            if (var9 != null) {
               Debug.say("binding url: " + var9.toString());
            }
         }

         Constructor var17;
         if (var2 != null) {
            if (var2 instanceof DescriptorBean) {
               var17 = var7.getDeclaredConstructor(DDBeanRoot.class, WebLogicDeploymentConfiguration.class, DescriptorBean.class, String.class, DescriptorSupport.class);
            } else {
               var17 = var7.getDeclaredConstructor(DDBeanRoot.class, WebLogicDeploymentConfiguration.class, Object.class, String.class, DescriptorSupport.class);
            }

            var18 = (WebLogicDConfigBeanRoot)var17.newInstance(var1, this, var2, var4, var5);
            return var18;
         }

         var17 = var7.getDeclaredConstructor(DDBeanRoot.class, WebLogicDeploymentConfiguration.class, String.class, DescriptorSupport.class);
         var18 = (WebLogicDConfigBeanRoot)var17.newInstance(var1, this, var4, var5);
      } catch (Throwable var14) {
         Throwable var8 = var14.getCause();
         if (var8 == null) {
            throw var14;
         }

         throw var8;
      } finally {
         this.popClassLoader(var6);
      }

      return var18;
   }

   private void popClassLoader(ClassLoaderControl var1) {
      if (var1 != null) {
         var1.restoreClassloader();
      }

   }

   private ClassLoaderControl pushClassLoader(DeployableObject var1) throws IOException {
      if (!(var1 instanceof WebLogicDeployableObject)) {
         return null;
      } else {
         Thread var2 = Thread.currentThread();
         ClassLoaderControl var3 = new ClassLoaderControl((WebLogicDeployableObject)var1);
         var2.setContextClassLoader(var3.getClassLoader());
         return var3;
      }
   }

   private DeploymentPlanBean buildPlan() {
      if (debug) {
         Debug.say("building deployment plan");
      }

      this.plan = (DeploymentPlanBean)DescriptorParser.getWLSEditableDescriptorBean(DeploymentPlanBean.class);
      this.plan.setApplicationName(this.appName);
      this.plan.setConfigRoot(this.installDir.getConfigDir().getPath());
      Iterator var4 = this.rootMap.entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry var5 = (Map.Entry)var4.next();
         DeployableObjectKey var6 = (DeployableObjectKey)var5.getKey();
         String var3 = var6.getName();
         DDBeanRoot var2 = (DDBeanRoot)this.rootMap.get(var6);
         WebLogicDConfigBeanRoot var1 = (WebLogicDConfigBeanRoot)this.ddBeanRoots.get(var2);
         if (var1 != null) {
            this.addToPlan(var1);
         }
      }

      return this.plan;
   }

   private void toXML(DescriptorBean var1, OutputStream var2) throws IOException {
      (new EditableDescriptorManager()).writeDescriptorAsXML(var1.getDescriptor(), var2);
   }

   WebLogicDConfigBeanRoot initDConfig(DDBeanRoot var1, String var2, DescriptorSupport var3) throws InvalidModuleException, IOException {
      WebLogicDConfigBeanRoot var4 = null;
      String var5 = null;

      try {
         String var6 = var3.getConfigURI();
         DeployableObject var23 = var1.getDeployableObject();
         if (debug) {
            Debug.say("looking for " + var2 + " on dobj: " + var23);
         }

         if (var23 instanceof WebLogicDeployableObject) {
            boolean var8 = false;
            WebLogicDeployableObject var9 = (WebLogicDeployableObject)var23;
            if (var9.hasDDBean(var6)) {
               DDBeanRootImpl var10 = (DDBeanRootImpl)var9.getDDBeanRoot(var6);
               if (debug) {
                  Debug.say("Getting DConfig information from ddroot :\n" + var10 + " of type: " + var10.getType());
               }

               DescriptorBean var11 = var10.getDescriptorBean();
               var5 = this.convertImplToDConfig(var11);
               if (debug) {
                  Debug.say("will create new " + var10.getType() + " dcb, " + var2 + ", of class, " + var5 + " with existing bean");
               }

               var4 = this.constructDCB(var1, var11, var5, var2, var3);
               InputStream var12 = null;

               try {
                  var12 = var23.getEntry(var6);
                  if (var12 == null) {
                     var8 = true;
                  }
               } finally {
                  if (var12 != null) {
                     try {
                        var12.close();
                     } catch (IOException var20) {
                     }
                  }

               }
            } else {
               var5 = var3.getDConfigClassName();
               if (debug) {
                  Debug.say("will create new dcb, " + var2 + ", of class, " + var5);
               }

               var4 = this.constructDCB(var1, (Object)null, var5, var2, var3);
               var8 = true;
            }

            var4.setExternal(var8);
            if (var4.getDescriptorBean() != null) {
               ((BasicDConfigBeanRoot)var4).registerAsListener(var4.getDescriptorBean());
            }
         }

         return var4;
      } catch (Throwable var22) {
         if (var22 instanceof InvalidModuleException) {
            throw (InvalidModuleException)var22;
         } else if (var22 instanceof IOException) {
            throw (IOException)var22;
         } else {
            if (debug) {
               var22.printStackTrace();
            }

            InvalidModuleException var7 = new InvalidModuleException(SPIDeployerLogger.invalidClass(var5, var22.toString()));
            var7.initCause(var22);
            throw var7;
         }
      }
   }

   private String convertImplToDConfig(DescriptorBean var1) {
      String var2 = var1.toString();
      if (var2.indexOf("Impl") != -1) {
         var2 = var2.substring(0, var2.indexOf("Impl"));
      }

      return var2 + "DConfig";
   }

   private String getNameFromUri(String var1) {
      return var1;
   }

   private void initEAR() {
      DeployableObject[] var1 = ((J2eeApplicationObject)this.dObject).getDeployableObjects();
      String[] var2 = ((J2eeApplicationObject)this.dObject).getModuleUris();
      if (var1 != null && var2 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.dObjects.add(var1[var3]);
            if (debug) {
               Debug.say("initing roots for " + this.getNameFromUri(var2[var3]) + " with uri " + var2[var3]);
            }

            DDBeanRoot var4 = var1[var3].getDDBeanRoot();
            ModuleType var5 = var4.getDeployableObject().getType();
            DeployableObjectKey var6 = new DeployableObjectKey(this.getNameFromUri(var2[var3]), var5);
            if (var5 == ModuleType.WAR) {
               DeployableObject var7 = var4.getDeployableObject();
               if (var7 instanceof WebLogicDeployableObject) {
                  WebLogicDeployableObject var8 = (WebLogicDeployableObject)var7;
                  var6.setContextRoot(var8.getContextRoot());
               }
            }

            this.rootMap.put(var6, var4);
            this.ddBeanRoots.put(var4, (Object)null);
         }

      }
   }

   private void initSCA() {
      ScaApplicationObject var1 = (ScaApplicationObject)this.dObject;
      DeployableObject[] var2 = var1.getDeployableObjects();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] instanceof WebLogicDeployableObject) {
               WebLogicDeployableObject var4 = (WebLogicDeployableObject)var2[var3];
               ModuleType var5 = var4.getType();
               if (var5 == ModuleType.WAR || var5 == ModuleType.EJB) {
                  this.dObjects.add(var4);
                  String var6 = var4.getUri();
                  DeployableObjectKey var7 = new DeployableObjectKey(this.getNameFromUri(var6), var5);
                  if (var5 == ModuleType.WAR) {
                     var7.setContextRoot(var4.getContextRoot());
                  }

                  DDBeanRoot var8 = var4.getDDBeanRoot();
                  this.rootMap.put(var7, var8);
                  this.ddBeanRoots.put(var8, (Object)null);
               }
            }
         }

      }
   }

   private void initialize() throws IOException, InvalidModuleException {
      File var1 = null;
      this.appName = null;
      WebLogicDeployableObject var3;
      if (this.plan != null) {
         ModuleOverrideBean var2 = this.plan.findRootModule();
         if (var2 != null) {
            this.appName = var2.getModuleName();
         } else if (this.dObject instanceof WebLogicDeployableObject) {
            var3 = (WebLogicDeployableObject)this.dObject;
            this.appName = var3.getArchive().getName();
         } else {
            this.appName = this.plan.getApplicationName();
         }

         var1 = ConfigHelper.getAppRootFromPlan(this.plan);
      }

      if (var1 == null && this.dObject instanceof WebLogicDeployableObject) {
         WebLogicDeployableObject var5 = (WebLogicDeployableObject)this.dObject;
         this.appName = var5.getArchive().getName();
         var1 = var5.getInstallDirPath();
      }

      if (this.appName == null) {
         this.appName = "MyApp";
      }

      if (debug) {
         Debug.say("initing roots with " + this.appName);
      }

      if (this.dObject instanceof ScaApplicationObject) {
         this.initSCA();
      } else {
         this.dObjects.add(this.dObject);
         DeployableObjectKey var6 = new DeployableObjectKey(this.appName, this.dObject.getType());
         if (this.dObject.getType() == ModuleType.WAR && this.dObject instanceof WebLogicDeployableObject) {
            var3 = (WebLogicDeployableObject)this.dObject;
            var6.setContextRoot(var3.getContextRoot());
         }

         this.rootMap.put(var6, this.dObject.getDDBeanRoot());
         this.ddBeanRoots.put(this.dObject.getDDBeanRoot(), (Object)null);
         if (this.dObject instanceof J2eeApplicationObject) {
            this.initEAR();
         }
      }

      this.installDir = new InstallDir(this.appName, var1);
      this.initPlanArtifacts();
      if (this.plan != null) {
         try {
            this.genDConfigBeans();
         } catch (ConfigurationException var4) {
            if (debug) {
               var4.printStackTrace();
            }

            InvalidModuleException var7 = new InvalidModuleException(var4.getMessage());
            var7.initCause(var4);
            throw var7;
         }
      }

   }

   private void initPlanArtifacts() {
      if (this.plan != null) {
         ConfigHelper.initPlanDirFromPlan(this.plan, this.installDir);
         this.registerListener(this.plan);
      }

   }

   private void registerListener(DeploymentPlanBean var1) {
      if (var1 != null) {
         this.getDescriptorHelper().addPropertyChangeListener((DescriptorBean)var1, this);
      }

   }

   private DescriptorHelper getDescriptorHelper() {
      if (this.ddhelper == null) {
         this.ddhelper = DescriptorHelper.getInstance();
      }

      return this.ddhelper;
   }

   private DeploymentPlanBean parsePlan(InputStream var1) throws ConfigurationException {
      try {
         return DescriptorParser.parseDeploymentPlan(var1);
      } catch (IOException var5) {
         Throwable var3 = ManagementException.unWrapExceptions(var5);
         ConfigurationException var4 = new ConfigurationException(SPIDeployerLogger.badPlan(var3.getMessage()));
         var4.initCause(var3);
         throw var4;
      }
   }

   private void genDConfigBeans() throws ConfigurationException {
      Iterator var1 = this.ddBeanRoots.keySet().iterator();

      while(var1.hasNext()) {
         DDBeanRoot var2 = (DDBeanRoot)var1.next();
         DConfigBeanRoot var3 = this.getDConfigBeanRoot(var2);
         this.collectSecondaryConfig((WebLogicDConfigBeanRoot)var3);
      }

   }

   private void collectSecondaryConfig(WebLogicDConfigBeanRoot var1) throws ConfigurationException {
      DeployableObject var2 = var1.getDDBean().getRoot().getDeployableObject();
      if (debug) {
         Debug.say("checking for secondary dd's for " + var2.getType().toString() + ":" + var1.getDConfigName());
      }

      DescriptorSupport var3 = null;
      DescriptorSupport[] var4 = DescriptorSupportManager.getForModuleType(var2.getType());

      int var5;
      for(var5 = 0; var5 < var4.length; ++var5) {
         var3 = var4[var5];
         if (!var3.isPrimary()) {
            if (debug) {
               Debug.say("found secondary dd candidate: " + var3.toString());
            }

            if (var3.getBaseTag().equals("weblogic-rdbms-jar")) {
               this.collectSecondaryEjbConfig(var1, var2);
            } else if (this.filterByType(var3)) {
               this.addSecondaryModule(var2, var1, var3, false);
            }
         }
      }

      this.collectWlsModules(var1, var2);
      if (ModuleType.EJB.equals(var2.getType()) || ModuleType.WAR.equals(var2.getType())) {
         var4 = DescriptorSupportManager.getForModuleType((ModuleType)WebLogicModuleType.WSEE);

         for(var5 = 0; var5 < var4.length; ++var5) {
            var3 = var4[var5];
            if ((ModuleType.EJB.equals(var2.getType()) && var3.getConfigURI().startsWith("META-INF") || ModuleType.WAR.equals(var2.getType()) && var3.getConfigURI().startsWith("WEB-INF")) && this.checkForDepenedentDD(var2, var3)) {
               this.addSecondaryModule(var2, var1, var3, false);
            }
         }
      }

      if (ModuleType.EAR.equals(var2.getType()) || ModuleType.WAR.equals(var2.getType())) {
         var4 = DescriptorSupportManager.getForModuleType((ModuleType)WebLogicModuleType.CONFIG);

         for(var5 = 0; var5 < var4.length; ++var5) {
            var3 = var4[var5];
            if (debug) {
               Debug.say("Adding secondary module to wdo : " + var2 + " at " + var3.getBaseURI());
            }

            this.addSecondaryModule(var2, var1, var3, false);
         }
      }

      this.collectPersistenceDescriptors(var1);
   }

   private void collectPersistenceDescriptors(WebLogicDConfigBeanRoot var1) throws ConfigurationException {
      WebLogicDeployableObject var2 = (WebLogicDeployableObject)var1.getDDBean().getRoot().getDeployableObject();
      ArrayList var3 = Collections.list(var2.getDDResourceEntries("META-INF/persistence.xml"));
      ArrayList var4 = Collections.list(var2.getDDResourceEntries("META-INF/persistence-configuration.xml"));
      if (!var3.isEmpty()) {
         Iterator var8 = var3.iterator();

         while(var8.hasNext()) {
            String var5 = (String)var8.next();
            String var6 = this.getPersistenceConfigURI(var5);
            DescriptorSupport var7 = this.getDescriptorSupportforPersistenceArchive(var2.getType(), var5, var6);
            this.addSecondaryModule(var2, var1, var7, false);
         }

      }
   }

   private DescriptorSupport getDescriptorSupportforPersistenceArchive(ModuleType var1, String var2, String var3) {
      return new DescriptorSupport(var1, "persistence", "persistence-configuration", "http://java.sun.com/xml/ns/persistence", "http://bea.com/ns/weblogic/950/persistence", var2, var3, "weblogic.j2ee.descriptor.PersistenceBean", "kodo.jdbc.conf.descriptor.PersistenceConfigurationBean", "kodo.jdbc.conf.descriptor.PersistenceConfigurationBeanDConfig", false);
   }

   private String getPersistenceConfigURI(String var1) {
      String var2 = var1.substring(0, var1.length() - "META-INF/persistence.xml".length());
      return var2 + "META-INF/persistence-configuration.xml";
   }

   private boolean checkForDepenedentDD(DeployableObject var1, DescriptorSupport var2) {
      if (var1 instanceof WebLogicDeployableObject) {
         WebLogicDeployableObject var3 = (WebLogicDeployableObject)var1;

         try {
            if (var2.getConfigURI().equals("META-INF/weblogic-webservices-policy.xml")) {
               return var3.hasDDBean("META-INF/weblogic-webservices.xml");
            }

            if (var2.getConfigURI().equals("WEB-INF/weblogic-webservices-policy.xml")) {
               return var3.hasDDBean("WEB-INF/weblogic-webservices.xml");
            }
         } catch (FileNotFoundException var5) {
         }
      }

      return true;
   }

   private void safeClose(InputStream var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (IOException var3) {
         }
      }

   }

   private void addSecondaryModule(DeployableObject var1, WebLogicDConfigBeanRoot var2, DescriptorSupport var3, boolean var4) throws ConfigurationException {
      String var7 = var3.getBaseURI();

      try {
         if (debug) {
            Debug.say("linking secondary dd at " + var7 + " on " + var1);
         }

         DDBeanRoot var6 = this.getOrCreateDDBeanRoot(var1, var2, var7);
         DConfigBean var5 = var2.getDConfigBean(var6, var3);
         if (var5 != null) {
            this.addToPlan((WebLogicDConfigBeanRoot)var5);
         }
      } catch (FileNotFoundException var10) {
         if (var4) {
            Loggable var9 = SPIDeployerLogger.logMissingDDLoggable(var7, var3.getModuleType().toString());
            throw new ConfigurationException(var9.getMessage());
         }

         if (debug) {
            Debug.say("no dd found or created for " + var7 + ": " + var10.toString());
         }
      } catch (DDBeanCreateException var11) {
         throw new ConfigurationException(SPIDeployerLogger.getDDBeanCreateError(var7, var11.getMessage()));
      }

   }

   private DDBeanRoot getOrCreateDDBeanRoot(DeployableObject var1, WebLogicDConfigBeanRoot var2, String var3) throws FileNotFoundException, DDBeanCreateException {
      DDBeanRoot var4 = null;

      try {
         var4 = var1.getDDBeanRoot(var3);
      } catch (FileNotFoundException var11) {
         if (debug) {
            Debug.say("Could not get the DDBean root for " + var1 + " at uri: " + var3);
         }

         DescriptorSupport var6 = DescriptorSupportManager.getForceWriteDS(var3);
         if (var6 != null) {
            if (debug) {
               Debug.say("Creating dd for " + var3);
            }

            try {
               String var7 = var6.getStandardClassName();
               var7 = var7.substring(0, var7.length() - 4);
               DescriptorBean var8 = DescriptorParser.getWLSEditableDescriptorBean(Class.forName(var7));
               this.writeDDToPlanDir(var2, var3, var8);
               var4 = var1.getDDBeanRoot(var3);
            } catch (ClassNotFoundException var9) {
               throw new FileNotFoundException(var3);
            } catch (IOException var10) {
               if (var10 instanceof FileNotFoundException) {
                  throw (FileNotFoundException)var10;
               }

               throw new FileNotFoundException(var3);
            }
         }
      }

      if (var4 == null) {
         throw new FileNotFoundException(var3);
      } else {
         return var4;
      }
   }

   private File writeDDToPlanDir(WebLogicDConfigBeanRoot var1, String var2, DescriptorBean var3) throws IOException {
      FileOutputStream var4 = null;

      File var5;
      try {
         if (this.dObject == var1.getDDBean().getRoot().getDeployableObject()) {
            var5 = this.installDir.getAppDDFile(var2);
         } else {
            var5 = this.installDir.getDDFile(var1.getDConfigName(), var2);
         }

         var4 = this.installDir.getOutputStream(var5);
         this.toXML(var3, var4);
         this.extDDWrite = true;
      } finally {
         if (var4 != null) {
            var4.close();
         }

      }

      return var5;
   }

   private void collectWlsModules(WebLogicDConfigBeanRoot var1, DeployableObject var2) throws ConfigurationException {
      DescriptorSupport var3 = DescriptorSupportManager.getForModuleType((ModuleType)WebLogicModuleType.WLDF)[0];
      this.addSecondaryModule(var2, var1, var3, false);
      if (var2 instanceof J2eeApplicationObject) {
         WeblogicModuleBean[] var4 = ((WeblogicApplicationBean)var1.getDescriptorBean()).getModules();
         var3 = null;
         if (var4 == null) {
            return;
         }

         for(int var8 = 0; var8 < var4.length; ++var8) {
            WeblogicModuleBean var5 = var4[var8];
            String var7 = var5.getType();
            String var6 = var5.getPath();
            if ("JDBC".equals(var7)) {
               var3 = DescriptorSupportManager.getForTag("jdbc-data-source");
            } else if ("JMS".equals(var7)) {
               var3 = DescriptorSupportManager.getForTag("weblogic-jms");
            } else {
               if (!"Interception".equals(var7)) {
                  throw new AssertionError("ERROR: A module of unknown type " + var7 + " was found");
               }

               var3 = DescriptorSupportManager.getForTag("weblogic-interception");
            }

            var3.setBaseURI(var6);
            var3.setConfigURI(var6);
            if (var6 != null) {
               this.addSecondaryModule(var2, var1, var3, true);
            }
         }
      }

   }

   private void collectSecondaryEjbConfig(WebLogicDConfigBeanRoot var1, DeployableObject var2) throws ConfigurationException {
      WeblogicEjbJarBean var3 = (WeblogicEjbJarBean)var1.getDescriptorBean();
      WeblogicEnterpriseBeanBean[] var4 = var3.getWeblogicEnterpriseBeans();
      if (var4 != null) {
         for(int var8 = 0; var8 < var4.length; ++var8) {
            DescriptorSupport var9 = null;
            EntityDescriptorBean var5 = var4[var8].getEntityDescriptor();
            if (var5 != null) {
               PersistenceBean var6 = var5.getPersistence();
               if (var6 != null) {
                  PersistenceUseBean var7 = var6.getPersistenceUse();
                  if (var7 != null) {
                     String var10 = var7.getTypeIdentifier();
                     if (debug) {
                        Debug.say("checking pu: " + var10);
                     }

                     if ("WebLogic_CMP_RDBMS".equals(var10)) {
                        var9 = DescriptorSupportManager.getForSecondaryTag("weblogic-rdbms-jar")[0];
                     }

                     if (var9 != null && var7.getTypeStorage() != null) {
                        if (debug) {
                           Debug.say("changing TypeStorage to " + var7.getTypeStorage());
                        }

                        var9.setBaseURI(var7.getTypeStorage());
                        var9.setConfigURI(var7.getTypeStorage());
                        if ("5.1.0".equals(var7.getTypeVersion())) {
                           var9.setBaseNameSpace("http://www.bea.com/ns/weblogic/60");
                           var9.setConfigNameSpace("http://www.bea.com/ns/weblogic/60");
                           var9.setStandardClassName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBeanImpl");
                           var9.setConfigClassName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBeanImpl");
                           var9.setDConfigClassName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBeanDConfig");
                        }

                        DescriptorSupportManager.add(var9);
                        this.addSecondaryModule(var2, var1, var9, true);
                     }
                  }
               }
            }
         }

      }
   }

   private boolean filterByType(DescriptorSupport var1) {
      return var1.getModuleType().getValue() != WebLogicModuleType.CONFIG.getValue();
   }

   private void removeSecondaryDConfigBean(DConfigBean var1) throws BeanNotFoundException {
   }

   String getRootTag(DDBeanRoot var1) throws ConfigurationException {
      String var2 = var1.getXpath();
      if (!"/".equals(var2)) {
         throw new IllegalArgumentException(SPIDeployerLogger.badRootBean(var1.toString()));
      } else {
         DescriptorSupport[] var3 = DescriptorSupportManager.getForModuleType(var1.getType());
         if (var3 != null && var3.length != 0) {
            if (var3.length == 1) {
               return var3[0].getBaseTag();
            } else {
               for(int var5 = 0; var5 < var3.length; ++var5) {
                  String var6 = ConfigHelper.getNSPrefix(var1, var3[var5].getBaseNameSpace());
                  DDBean[] var4 = var1.getChildBean(ConfigHelper.applyNamespace(var6, var3[var5].getBaseTag()));
                  if (var4 != null) {
                     return var3[var5].getBaseTag();
                  }
               }

               if (var1.getType() == ModuleType.EJB) {
                  return DescriptorSupportManager.EJB_DESC_SUPPORT.getBaseTag();
               } else {
                  WebLogicDeployableObject var7 = (WebLogicDeployableObject)var1.getDeployableObject();
                  throw new ConfigurationException(SPIDeployerLogger.noTagFound(var1.getType().toString(), var7.getUri(), var1.getFilename()));
               }
            }
         } else {
            throw new ConfigurationException(SPIDeployerLogger.noTagRegistered(var1.getType().toString()));
         }
      }
   }

   public void propertyChange(PropertyChangeEvent var1) {
      String var2 = var1.getPropertyName();
      if ("ConfigRoot".equals(var2)) {
         File var3 = null;
         if (this.plan.getConfigRoot() != null) {
            var3 = (new File(this.plan.getConfigRoot())).getAbsoluteFile();
            var3.mkdirs();
         }

         if (var3 != null && (!var3.exists() || !var3.isDirectory())) {
            if (debug) {
               Debug.say("Ignoring invalid change to config root: " + this.plan.getConfigRoot());
            }
         } else {
            this.installDir.setConfigDir(var3);
         }
      }

   }
}
