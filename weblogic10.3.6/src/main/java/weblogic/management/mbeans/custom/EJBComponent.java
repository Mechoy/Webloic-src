package weblogic.management.mbeans.custom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.application.ApplicationFileManager;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.J2EEApplicationService;
import weblogic.j2ee.J2EEUtils;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.EJBComponentMBean;
import weblogic.management.descriptors.TopLevelDescriptorMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.security.service.EJBResource;
import weblogic.utils.Debug;
import weblogic.utils.FileUtils;
import weblogic.utils.classloaders.ClasspathClassLoader;
import weblogic.utils.jars.JarFileObject;
import weblogic.utils.jars.VirtualJarFile;

public final class EJBComponent extends Component {
   private static final long serialVersionUID = 5432117546996543348L;
   private List excludedResources = null;
   private List uncheckedResources = null;
   private EJBComponentRuntimeMBean compRTMBean = null;
   private EjbDescriptorBean dd = null;
   private Map remoteMethods = new HashMap();
   private Map homeMethods = new HashMap();
   private Map localMethods = new HashMap();
   private Map localHomeMethods = new HashMap();
   private boolean methodsInitialized = false;

   public EJBComponent(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public EJBComponentRuntimeMBean getEJBComponentRuntime() {
      return this.compRTMBean;
   }

   public void setEJBComponentRuntime(EJBComponentRuntimeMBean var1) {
      this.compRTMBean = var1;
   }

   public List getExcludedEJBResources() {
      if (this.excludedResources == null) {
         this.excludedResources = new ArrayList();
      }

      return this.excludedResources;
   }

   public List getUncheckedEJBResources() {
      if (this.uncheckedResources == null) {
         this.uncheckedResources = new ArrayList();
      }

      return this.uncheckedResources;
   }

   public List getExcludedEJBResources(String var1, String var2, String var3) {
      return !this.hasExcludedEJBResources() ? this.getExcludedEJBResources() : this.getQualifiedList(var1, var2, var3, this.excludedResources);
   }

   public List getUncheckedEJBResources(String var1, String var2, String var3) {
      return !this.hasUncheckedEJBResources() ? this.getUncheckedEJBResources() : this.getQualifiedList(var1, var2, var3, this.uncheckedResources);
   }

   public EjbDescriptorBean getEJBDescriptor() {
      return this.dd;
   }

   public void refreshDDsIfNeeded(String[] var1) {
      if (!this.isConfig()) {
         if (var1 == null || this.containsDD(var1, "ejb-jar.xml") || this.containsDD(var1, "weblogic-ejb-jar.xml") || this.containsDD(var1, "weblogic-rdbms-jar.xml")) {
            if (DEBUG) {
               Debug.say("Resetting Editor and Descriptor Tree");
            }

            this.dd = null;
         }

         this.methodsInitialized = false;
      }
   }

   private void addExcludedEJBResource(EJBResource var1) {
      if (this.excludedResources == null) {
         this.excludedResources = new ArrayList();
      }

      this.excludedResources.add(var1);
   }

   private void addUncheckedEJBResource(EJBResource var1) {
      if (this.uncheckedResources == null) {
         this.uncheckedResources = new ArrayList();
      }

      this.uncheckedResources.add(var1);
   }

   public boolean hasExcludedEJBResources() {
      if (this.excludedResources == null) {
         return false;
      } else {
         return this.excludedResources.size() > 0;
      }
   }

   public boolean hasUncheckedEJBResources() {
      if (this.uncheckedResources == null) {
         return false;
      } else {
         return this.uncheckedResources.size() > 0;
      }
   }

   protected TopLevelDescriptorMBean readDescriptor(VirtualJarFile var1, File var2, String var3) {
      return null;
   }

   protected boolean isPhysicalModule() {
      return true;
   }

   private List getQualifiedList(String var1, String var2, String var3, List var4) {
      ArrayList var5 = new ArrayList();
      if (var4 == null) {
         return var5;
      } else {
         Iterator var6 = var4.iterator();

         while(true) {
            EJBResource var7;
            do {
               do {
                  do {
                     if (!var6.hasNext()) {
                        return var5;
                     }

                     var7 = (EJBResource)var6.next();
                  } while(var1 != null && !var1.equals(var7.getApplicationName()));
               } while(var2 != null && !var2.equals(var7.getModuleName()));
            } while(var3 != null && !var3.equals(var7.getEJBName()));

            var5.add(var7);
         }
      }
   }

   private void initializeMethods() throws IOException, ClassNotFoundException {
      this.remoteMethods.clear();
      this.homeMethods.clear();
      this.localMethods.clear();
      this.localHomeMethods.clear();
      ApplicationMBean var1 = this.getApplication();
      File var2 = new File(var1.getPath());
      File var3 = null;
      ClasspathClassLoader var4 = null;

      try {
         if (var1.getDeploymentType().equals(ApplicationMBean.TYPE_EAR)) {
            var3 = new File(J2EEApplicationService.getTempDir().getCanonicalPath() + File.separator + "dde" + File.separator + var1.getName());
            var3.mkdirs();
            JarFileObject var5 = new JarFileObject(var2);
            var5.extract(var3);
            var2 = var3;
         }

         StringBuffer var16 = new StringBuffer();
         int var8;
         if (var1.isEar()) {
            J2EEUtils.addAppInfToClasspath(var16, var2);
            ApplicationFileManager var17 = ApplicationFileManager.newInstance(var2);
            if (var17.isSplitDirectory()) {
               J2EEUtils.addAppInfToClasspath(var16, var17.getSourcePath());
            }

            ComponentMBean[] var7 = var1.getComponents();

            for(var8 = 0; var8 < var7.length; ++var8) {
               if (var7[var8] instanceof EJBComponentMBean) {
                  File var9 = new File(var2, var7[var8].getURI());
                  if (DEBUG) {
                     Debug.assertion(var9.exists());
                  }

                  var16.append(var9.getPath());
                  var16.append(File.pathSeparator);
                  if (var17.isSplitDirectory()) {
                     var16.append(new File(var17.getSourcePath(), var7[var8].getURI()));
                     var16.append(File.pathSeparator);
                  }
               }
            }
         } else {
            File var6 = new File(var2, this.getComponentMBean().getURI());
            var16.append(var6.getPath());
            if (DEBUG) {
               Debug.assertion(var6.exists());
            }
         }

         var4 = new ClasspathClassLoader(var16.toString());
         EnterpriseBeansBean var18 = this.dd.getEjbJarBean().getEnterpriseBeans();
         if (var18 != null) {
            SessionBeanBean[] var19 = var18.getSessions();

            Class var11;
            for(var8 = 0; var8 < var19.length; ++var8) {
               String var21 = var19[var8].getEjbName();
               Class var10;
               if (var19[var8].getRemote() != null) {
                  var10 = var4.loadClass(var19[var8].getRemote());
                  this.remoteMethods.put(var21, var10.getMethods());
                  var11 = var4.loadClass(var19[var8].getHome());
                  this.homeMethods.put(var21, var11.getMethods());
               }

               if (var19[var8].getLocal() != null) {
                  var10 = var4.loadClass(var19[var8].getLocal());
                  this.localMethods.put(var21, var10.getMethods());
                  var11 = var4.loadClass(var19[var8].getLocalHome());
                  this.localHomeMethods.put(var21, var11.getMethods());
               }
            }

            EntityBeanBean[] var20 = var18.getEntities();

            for(int var22 = 0; var22 < var20.length; ++var22) {
               String var23 = var20[var22].getEjbName();
               Class var12;
               if (var20[var22].getRemote() != null) {
                  var11 = var4.loadClass(var20[var22].getRemote());
                  this.remoteMethods.put(var23, var11.getMethods());
                  var12 = var4.loadClass(var20[var22].getHome());
                  this.homeMethods.put(var23, var12.getMethods());
               }

               if (var20[var22].getLocal() != null) {
                  var11 = var4.loadClass(var20[var22].getLocal());
                  this.localMethods.put(var23, var11.getMethods());
                  var12 = var4.loadClass(var20[var22].getLocalHome());
                  this.localHomeMethods.put(var23, var12.getMethods());
               }
            }
         }
      } finally {
         if (var4 != null) {
            var4.close();
         }

         if (var3 != null) {
            FileUtils.remove(var3);
         }

      }

      this.methodsInitialized = true;
   }
}
