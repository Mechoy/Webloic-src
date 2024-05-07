package weblogic.metadata.management;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorDiff;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.DescriptorUpdateFailedException;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.WebBean;
import weblogic.j2ee.descriptor.wl.AnnotationOverridesBean;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.ModuleProviderBean;
import weblogic.logging.NonCatalogLogger;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.utils.classloaders.GenericClassLoader;

public class AnnotationOverridesModule implements Module, UpdateListener {
   private static final String ROOT_ELEMENT_NAME = "annotation-overrides";
   private static final String PLAN_UPDATE_URI = "META-INF/annotation-overrides.xml";
   private static final String PLAN_DIR_NAME = "plan";
   private static final String PLAN_FILENAME = "Plan.xml";
   private ModuleProviderBean _provider;
   private GenericClassLoader _classloader;
   private Map<String, DescriptorBean> _descriptorMap = new HashMap();
   private String _appName;
   private CustomModuleBean _customModuleBean;
   private DescriptorManager _descriptorManager;
   private ApplicationContextInternal _applicationContext;
   private static NonCatalogLogger _logger = new NonCatalogLogger("AnnotationOverridesModule");
   private long _overrideVersion = 0L;
   private Map<String, List<DescriptorBean>> _pendingUpdateDescriptors;
   private Map<String, String> _webUriMapping;

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[0];
   }

   public void adminToProduction() {
   }

   public void forceProductionToAdmin() throws ModuleException {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws ModuleException {
   }

   public AnnotationOverridesModule(ModuleProviderBean var1, CustomModuleBean var2) {
      this._provider = var1;
      this._customModuleBean = var2;
      this._descriptorManager = new DescriptorManager();
      this._pendingUpdateDescriptors = new HashMap();
   }

   public String getId() {
      return this._customModuleBean.getUri();
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_CONFIG;
   }

   public DescriptorBean[] getDescriptors() {
      return new DescriptorBean[0];
   }

   private String getLogPrefix() {
      return "[ _appName: " + this._appName + " ][ uri: " + this._customModuleBean.getUri() + " ] ";
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this._applicationContext = (ApplicationContextInternal)var1;
      this._appName = this._applicationContext.getApplicationId();
      var3.addUpdateListener(this);
      this._classloader = var2;
      this._webUriMapping = new HashMap();
      ApplicationBean var4 = this._applicationContext.getApplicationDD();
      if (var4 != null) {
         ModuleBean[] var5 = var4.getModules();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            ModuleBean var8 = var5[var7];
            WebBean var9 = var8.getWeb();
            if (var9 != null) {
               String var10 = var9.getContextRoot();
               String var11 = var9.getWebUri();
               this._webUriMapping.put(var11, var10);
            }
         }
      }

      return var2;
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.init(var1, var2, var3);
   }

   private Map<String, DescriptorBean> getAnnotationOverrideDescriptors() throws ModuleException {
      HashMap var1 = new HashMap();

      try {
         DeploymentPlanBean var2 = this.getDeploymentPlanBean();
         if (var2 == null) {
            _logger.debug(this.getLogPrefix() + ":No Deployment Plan");
         } else {
            String var3 = var2.getConfigRoot();
            _logger.info("ConfigRoot is " + var3);
            StringBuilder var4 = new StringBuilder();
            ModuleOverrideBean[] var5 = var2.getModuleOverrides();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               ModuleOverrideBean var7 = var5[var6];
               String var8 = var7.getModuleName();
               String var9 = var7.getModuleType();
               boolean var10 = var2.rootModule(var8);
               ModuleDescriptorBean[] var11 = var7.getModuleDescriptors();

               for(int var12 = 0; var12 < var11.length; ++var12) {
                  ModuleDescriptorBean var13 = var11[var12];
                  String var14 = var13.getUri();
                  String var15 = var13.getRootElement();
                  if (var15.equals("annotation-overrides")) {
                     String var16 = null;
                     String var17 = var8;
                     var4.delete(0, var4.length());
                     var4.append(var3);
                     if (var10) {
                        var17 = "__WLS ROOT MODULE URI__";
                        var4.append(File.separatorChar);
                        var4.append(var14);
                     } else {
                        var4.append(File.separatorChar);
                        var4.append(var8);
                        var4.append(File.separatorChar);
                        var4.append(var14);
                     }

                     var16 = var4.toString();
                     FileInputStream var18 = null;

                     try {
                        File var19 = new File(var16);
                        if (var19.exists()) {
                           if (!var19.canRead()) {
                              _logger.debug(this.getLogPrefix() + ": unable to load anootation override for " + var17 + " due to file permission");
                           } else {
                              var18 = new FileInputStream(var19);
                              DescriptorBean var20 = this._descriptorManager.createDescriptor(var18).getRootBean();
                              var1.put(var17, var20);
                              if (this._webUriMapping.containsKey(var17)) {
                                 String var21 = (String)this._webUriMapping.get(var17);
                                 var1.put(var21, var20);
                              }
                           }
                        }
                     } catch (Exception var26) {
                        throw new ModuleException(var26);
                     } finally {
                        if (var18 != null) {
                           var18.close();
                        }

                     }
                  }
               }
            }
         }

         return var1;
      } catch (Exception var28) {
         throw new ModuleException(var28);
      }
   }

   public void prepare() throws ModuleException {
      _logger.info(this.getLogPrefix() + ":prepare()");
      this._descriptorMap = this.getAnnotationOverrideDescriptors();
   }

   private DeploymentPlanBean getDeploymentPlanBean() throws FileNotFoundException, IOException, XMLStreamException {
      AppDeploymentMBean var1 = this._applicationContext.getAppDeploymentMBean();

      assert var1 != null;

      DeploymentPlanBean var2 = var1.getDeploymentPlanDescriptor();
      if (var2 == null) {
         String var3 = this.getConfigDir(var1);
         if (var3 == null) {
            return null;
         }

         File var4 = new File(var3 + File.separator + "Plan.xml");
         if (var4.exists() && var4.canRead()) {
            var2 = (new DeploymentPlanDescriptorLoader(var4)).getDeploymentPlanBean();
            var2.setConfigRoot(var3);
         }
      }

      return var2;
   }

   private String getConfigDir(AppDeploymentMBean var1) {
      String var2 = null;
      String var3 = var1.getSourcePath();
      File var4 = new File(var3);
      boolean var5 = var4.isDirectory();
      File var6;
      if (var5) {
         var6 = var4.getParentFile();
         if (var6 != null) {
            var2 = var6.getAbsolutePath();
         }
      } else {
         var6 = var4.getParentFile();
         if (var6 != null) {
            File var7 = var6.getParentFile();
            if (var7 != null) {
               var2 = var7.getAbsolutePath();
            }
         }
      }

      return var2 != null ? var2 + File.separator + "plan" : null;
   }

   public void activate() throws ModuleException {
      _logger.info(this.getLogPrefix() + ":activate()");
      Map var1 = this._applicationContext.getApplicationParameters();
      if (var1 == Collections.EMPTY_MAP || var1 == null) {
         this._applicationContext.setApplicationParameters(new HashMap());
      }

      this._applicationContext.getApplicationParameters().put("weblogic.metadata.management.AnnotationOverrideDescriptors", this._descriptorMap);
      this._applicationContext.getApplicationParameters().put("weblogic.metadata.management.AnnotationOverrideDescriptorsVersionID", this._overrideVersion);
   }

   public boolean acceptURI(String var1) {
      return var1 != null && var1.endsWith("META-INF/annotation-overrides.xml");
   }

   public void prepareUpdate(String var1) throws ModuleException {
      _logger.info(this.getLogPrefix() + ":prepareUpdate()");
      LinkedList var2 = new LinkedList();
      this._pendingUpdateDescriptors.put(var1, var2);
      String var3 = getModuleNameFromURI(var1);
      if (var3 != null && var3.length() > 0) {
         Map var4 = this.getAnnotationOverrideDescriptors();
         DescriptorBean var5 = (DescriptorBean)var4.get(var3);
         DescriptorBean var6 = (DescriptorBean)this._descriptorMap.get(var3);
         if (var5 != null && var6 != null) {
            try {
               AnnotationOverridesBean var7 = (AnnotationOverridesBean)var5;
               AnnotationOverridesBean var8 = (AnnotationOverridesBean)var6;
               DescriptorDiff var9 = var6.getDescriptor().computeDiff(var5.getDescriptor());
               if (var9.size() > 0) {
                  _logger.info(this.getLogPrefix() + ":[moduleName:" + var3 + "] had diffs, # of diffs = " + var9.size());
                  var7.setUpdateCount(var8.getUpdateCount() + 1L);
                  var6.getDescriptor().prepareUpdate(var5.getDescriptor());
                  var2.add(var6);
               }

            } catch (DescriptorUpdateRejectedException var10) {
               _logger.error(this.getLogPrefix() + ":prepareUpdate()", var10);
               this._pendingUpdateDescriptors.remove(var1);
               throw new ModuleException("Failed on module:" + var3, var10);
            }
         }
      } else {
         throw new ModuleException(this.getLogPrefix() + " Unexpected uri format: " + var1);
      }
   }

   public void activateUpdate(String var1) throws ModuleException {
      _logger.info(this.getLogPrefix() + ":activateUpdate()");
      boolean var2 = false;
      List var3 = (List)this._pendingUpdateDescriptors.remove(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         DescriptorBean var5 = (DescriptorBean)var4.next();

         try {
            var5.getDescriptor().activateUpdate();
         } catch (DescriptorUpdateFailedException var7) {
            _logger.error(this.getLogPrefix() + ":activateUpdate()" + var7);
            var2 = true;
         }
      }

      if (!var2) {
         this._applicationContext.getApplicationParameters().put("weblogic.metadata.management.AnnotationOverrideDescriptorsVersionID", ++this._overrideVersion);
      }

   }

   public void start() throws ModuleException {
   }

   public void deactivate() throws ModuleException {
   }

   public void unprepare() throws ModuleException {
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      var1.removeUpdateListener(this);
   }

   public void rollbackUpdate(String var1) {
   }

   public void remove() throws ModuleException {
   }

   private static String getModuleNameFromURI(String var0) {
      if (var0 != null && var0.length() > 0) {
         if (var0.equals("META-INF/annotation-overrides.xml")) {
            return "__WLS ROOT MODULE URI__";
         }

         int var1 = var0.indexOf("META-INF/annotation-overrides.xml");
         if (var1 > 0) {
            return var0.substring(0, var1 - 1);
         }
      }

      return null;
   }

   public static void main(String[] var0) throws Exception {
      DescriptorManager var1 = new DescriptorManager();
      FileInputStream var2 = null;
      FileOutputStream var3 = null;

      try {
         var2 = new FileInputStream("D:/Development/oamApp/plan/OAMPgFlowWeb/META-INF/annotation-overrides.xml");
         Descriptor var4 = var1.createDescriptor(var2);
         AnnotationOverridesBean var5 = (AnnotationOverridesBean)var4.getRootBean();
         var3 = new FileOutputStream("D:/temp/anno-ovr.xml");
         var1.writeDescriptorAsXML(var4, new BufferedOutputStream(var3));
      } catch (Exception var9) {
         throw var9;
      } finally {
         if (var2 != null) {
            var2.close();
         }

         if (var3 != null) {
            var3.flush();
         }

      }

   }
}
