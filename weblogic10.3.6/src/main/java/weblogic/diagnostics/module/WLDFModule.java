package weblogic.diagnostics.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.FatalModuleException;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorDiff;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.management.DomainDir;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.WLDFSystemResourceMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public class WLDFModule implements Module, UpdateListener, WLDFModuleStates {
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugDiagnosticsModule");
   private String moduleURI = null;
   private WLDFResourceBean wldfResource = null;
   private WLDFSubModule[] subModules;
   private int moduleState = 0;
   private WLDFResourceBean proposedWLDFResource;
   private DescriptorDiff proposedUpdates = null;
   private ApplicationContextInternal appCtxInternal;
   public static final String UPDATE_SYNC = new String("");

   public WLDFModule(String var1) {
      this.moduleURI = var1;
   }

   public String getId() {
      return this.moduleURI;
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_WLDF;
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[0];
   }

   public DescriptorBean[] getDescriptors() {
      return this.wldfResource == null ? new DescriptorBean[0] : new DescriptorBean[]{(DescriptorBean)this.wldfResource};
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.initUsingLoader(var1, var2, var3);
      return var2;
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.initDescriptor(var1, var3);
   }

   public void prepare() throws ModuleException {
      if (DEBUG_LOGGER.isDebugEnabled() && this.moduleState != 1) {
         DEBUG_LOGGER.debug("WLDF Module not in INITIALIZED state during prepare() callback.  State was " + this.moduleState);
      }

      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Inside prepare()");
      }

      try {
         for(int var1 = 0; var1 < this.subModules.length; ++var1) {
            this.subModules[var1].prepare();
         }
      } catch (ModuleException var2) {
         if (var2 instanceof FatalModuleException) {
            throw var2;
         }

         throw new FatalModuleException(var2);
      }

      this.moduleState = 2;
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("WLDFModule PREPARED successfully");
      }

   }

   public void activate() throws ModuleException {
      if (DEBUG_LOGGER.isDebugEnabled() && this.moduleState != 2) {
         DEBUG_LOGGER.debug("WLDF Module not in PREPARED state during activate() callback.  State was " + this.moduleState);
      }

      synchronized(UPDATE_SYNC) {
         try {
            for(int var2 = 0; var2 < this.subModules.length; ++var2) {
               this.subModules[var2].activate();
            }
         } catch (ModuleException var4) {
            if (var4 instanceof FatalModuleException) {
               throw var4;
            }

            throw new FatalModuleException(var4);
         }
      }

      this.moduleState = 3;
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("WLDFModule ACTIVATED successfully");
      }

   }

   public void start() throws ModuleException {
   }

   public void deactivate() throws ModuleException {
      if (DEBUG_LOGGER.isDebugEnabled() && this.moduleState != 3) {
         DEBUG_LOGGER.debug("WLDF Module not in ACTIVATED state during deactivate() callback.  State was " + this.moduleState);
      }

      synchronized(UPDATE_SYNC) {
         for(int var2 = 0; var2 < this.subModules.length; ++var2) {
            this.subModules[var2].deactivate();
         }

         this.moduleState = 2;
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("WLDFModule deactivated successfully");
         }

      }
   }

   public void unprepare() throws ModuleException {
      if (DEBUG_LOGGER.isDebugEnabled() && this.moduleState != 2) {
         DEBUG_LOGGER.debug("WLDF Module not in PREPARED state during unprepare() callback.  State was " + this.moduleState);
      }

      for(int var1 = 0; var1 < this.subModules.length; ++var1) {
         this.subModules[var1].unprepare();
      }

      this.moduleState = 1;
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("WLDFModule unprepared successfully");
      }

   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      if (DEBUG_LOGGER.isDebugEnabled() && this.moduleState != 1) {
         DEBUG_LOGGER.debug("WLDF Module not in INITIALIZED state during destroy() callback.  State was " + this.moduleState);
      }

      for(int var2 = 0; var2 < this.subModules.length; ++var2) {
         this.subModules[var2].destroy();
      }

      this.moduleState = 0;
      var1.removeUpdateListener(this);
   }

   public void remove() throws ModuleException {
   }

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws ModuleException {
   }

   public void forceProductionToAdmin() throws ModuleException {
   }

   public boolean acceptURI(String var1) {
      if (DEBUG_LOGGER.isDebugEnabled() && this.moduleState != 3) {
         DEBUG_LOGGER.debug("WLDF Module not in ACTIVATED state during acceptURI() callback. State was " + this.moduleState);
      }

      return this.moduleURI.equals(var1);
   }

   public void prepareUpdate(String var1) throws ModuleException {
      if (DEBUG_LOGGER.isDebugEnabled() && this.moduleState != 3) {
         DEBUG_LOGGER.debug("WLDF Module not in ACTIVATED state during prepareUpdate() callback.  State was " + this.moduleState);
      }

      try {
         if (this.appCtxInternal.getSystemResourceMBean() != null) {
            var1 = DomainDir.getPathRelativeConfigDir(var1);
         }

         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Preparing update from URI = " + var1);
         }

         this.proposedWLDFResource = this.loadDescriptor(this.appCtxInternal, var1, true);
         Descriptor var2 = ((DescriptorBean)this.wldfResource).getDescriptor();
         Descriptor var3 = ((DescriptorBean)this.proposedWLDFResource).getDescriptor();
         this.proposedUpdates = var2.computeDiff(var3);
      } catch (Exception var4) {
         throw new WLDFModuleException(var4);
      }

      for(int var5 = 0; var5 < this.subModules.length; ++var5) {
         this.subModules[var5].prepareUpdate(this.proposedWLDFResource, this.proposedUpdates);
      }

      this.moduleState = 4;
   }

   public void activateUpdate(String var1) throws ModuleException {
      if (DEBUG_LOGGER.isDebugEnabled() && this.moduleState != 4) {
         DEBUG_LOGGER.debug("WLDF Module not in UPDATE_PENDING state during activateUpdate() callback.  State was " + this.moduleState);
      }

      synchronized(UPDATE_SYNC) {
         int var3 = 0;

         while(true) {
            if (var3 >= this.subModules.length) {
               break;
            }

            this.subModules[var3].activateUpdate(this.proposedWLDFResource, this.proposedUpdates);
            ++var3;
         }
      }

      this.moduleState = 3;
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Activated update from URI = " + var1);
      }

   }

   public void rollbackUpdate(String var1) {
      if (DEBUG_LOGGER.isDebugEnabled() && this.moduleState != 4) {
         DEBUG_LOGGER.debug("WLDF Module not in UPDATE_PENDING state during rollbackUpdate() callback.  State was " + this.moduleState);
      }

      synchronized(UPDATE_SYNC) {
         int var3 = 0;

         while(true) {
            if (var3 >= this.subModules.length) {
               break;
            }

            this.subModules[var3].rollbackUpdate(this.proposedWLDFResource, this.proposedUpdates);
            ++var3;
         }
      }

      this.moduleState = 3;
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Rolled back update from URI = " + var1);
      }

   }

   private void initDescriptor(ApplicationContext var1, UpdateListener.Registration var2) throws ModuleException {
      try {
         ApplicationContextInternal var3 = (ApplicationContextInternal)var1;
         this.appCtxInternal = var3;
         WLDFSystemResourceMBean var4 = (WLDFSystemResourceMBean)var3.getSystemResourceMBean();
         if (var4 != null) {
            this.wldfResource = var4.getWLDFResource();
         } else {
            this.wldfResource = this.loadDescriptor(var3, this.moduleURI, false);
         }

         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Initialized WLDFResource");
         }

         if (var4 != null) {
            this.subModules = SubModuleRegistry.getWLDFSubModules();
         } else {
            this.subModules = SubModuleRegistry.getAppScopedWLDFSubModules();
         }

         for(int var5 = 0; var5 < this.subModules.length; ++var5) {
            this.subModules[var5].init(var1, this.wldfResource);
         }

         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Initialized WLDF Submodules");
         }

         var2.addUpdateListener(this);
         this.moduleState = 1;
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Initialized WLDF Module successfully");
         }

      } catch (Exception var6) {
         throw new ModuleException("Error creating WLDF descriptor from " + this.moduleURI, var6);
      }
   }

   public static DescriptorBean getDescriptorBean(DescriptorManager var0, GenericClassLoader var1, File var2, DeploymentPlanBean var3, String var4, String var5) throws IOException, XMLStreamException {
      WLDFDescriptorLoader var6 = new WLDFDescriptorLoader(var0, var1, var2, var3, var4, var5);
      return var6.loadDescriptorBean();
   }

   public static DescriptorBean getDescriptorBean(VirtualJarFile var0, File var1, DeploymentPlanBean var2, String var3, String var4) throws IOException, XMLStreamException {
      WLDFDescriptorLoader var5 = new WLDFDescriptorLoader(var0, var1, var2, var3, var4);
      return var5.loadDescriptorBean();
   }

   public static DescriptorBean getDescriptorBean(File var0, File var1, DeploymentPlanBean var2, String var3, String var4) throws IOException, XMLStreamException {
      WLDFDescriptorLoader var5 = new WLDFDescriptorLoader(var0, var1, var2, var3, var4);
      return var5.loadDescriptorBean();
   }

   private WLDFResourceBean loadDescriptor(ApplicationContextInternal var1, String var2, boolean var3) throws IOException, XMLStreamException {
      FileInputStream var4 = null;
      VirtualJarFile var5 = null;

      WLDFResourceBean var13;
      try {
         if (var1.getSystemResourceMBean() != null) {
            DescriptorManager var29 = new DescriptorManager();
            var4 = new FileInputStream(var2);
            WLDFDescriptorLoader var30 = new WLDFDescriptorLoader(var4, var29, new ArrayList());
            WLDFResourceBean var31 = (WLDFResourceBean)var30.loadDescriptorBean();
            return var31;
         }

         File var6 = null;
         DeploymentPlanBean var7 = null;
         String var8 = (new File(var1.getAppDeploymentMBean().getAbsoluteSourcePath())).getName();
         var5 = var1.getApplicationFileManager().getVirtualJarFile();
         AppDeploymentMBean var9 = var1.getAppDeploymentMBean();
         var7 = this.getDeploymentPlan(var1, var3);
         if (var9.getPlanDir() != null) {
            var6 = new File(var9.getLocalPlanDir());
         }

         File var11 = this.getExternalDiagnosticDescriptorFile(var7);
         WLDFDescriptorLoader var10;
         if (var11 != null) {
            var10 = new WLDFDescriptorLoader(var11, var6, var7, var8, var2);
         } else {
            var10 = new WLDFDescriptorLoader(var5, var6, var7, var8, var2);
         }

         WLDFResourceBean var12 = this.getWLDFResourceBean(var10);
         if (var12 != null) {
            var13 = var12;
            return var13;
         }

         var13 = (WLDFResourceBean)(new DescriptorManager()).createDescriptorRoot(WLDFResourceBean.class).getRootBean();
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var27) {
         }

         try {
            if (var5 != null) {
               var5.close();
            }
         } catch (IOException var26) {
         }

      }

      return var13;
   }

   private DeploymentPlanBean getDeploymentPlan(ApplicationContextInternal var1, boolean var2) {
      String var3 = var1.getApplicationId();
      AppDeploymentMBean var4 = null;
      if (var2) {
         DomainMBean var5 = var1.getProposedDomain();
         if (var5 != null) {
            var4 = var5.lookupAppDeployment(var3);
         }
      }

      if (var4 == null) {
         var4 = var1.getAppDeploymentMBean();
      }

      return var4.getDeploymentPlanDescriptor();
   }

   private WLDFResourceBean getWLDFResourceBean(WLDFDescriptorLoader var1) throws IOException {
      try {
         return (WLDFResourceBean)var1.loadDescriptorBean();
      } catch (IOException var4) {
         throw var4;
      } catch (Exception var5) {
         IOException var3 = new IOException(var5.getMessage());
         var3.initCause(var5);
         throw var3;
      }
   }

   private File getExternalDiagnosticDescriptorFile(DeploymentPlanBean var1) {
      if (var1 == null) {
         return null;
      } else {
         File var2 = null;
         ModuleOverrideBean var3 = var1.findModuleOverride(this.appCtxInternal.getApplicationFileName());
         if (var3 != null) {
            ModuleDescriptorBean[] var4 = var3.getModuleDescriptors();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               if (var4[var5].isExternal() && var4[var5].getRootElement().equals("wldf-resource")) {
                  var2 = new File(var1.getConfigRoot(), var4[var5].getUri());
                  if (var2.isFile() && var2.exists()) {
                     break;
                  }

                  var2 = null;
               }
            }
         }

         return var2;
      }
   }
}
