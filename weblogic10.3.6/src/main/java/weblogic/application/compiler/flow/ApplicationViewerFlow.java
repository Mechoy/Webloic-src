package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.enterprise.deploy.shared.ModuleType;
import javax.xml.stream.XMLStreamException;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.application.compiler.WLSModule;
import weblogic.application.utils.PersistenceUtils;
import weblogic.deploy.api.model.EditableDeployableObject;
import weblogic.deploy.api.model.EditableJ2eeApplicationObject;
import weblogic.deploy.api.model.WebLogicDeployableObjectFactory;
import weblogic.deployment.PersistenceUnitViewer;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.diagnostics.module.WLDFModule;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.compiler.ToolFailureException;

public class ApplicationViewerFlow extends ModuleViewerFlow {
   private static Map supportedURIs = new HashMap();

   public ApplicationViewerFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      WebLogicDeployableObjectFactory var1 = this.ctx.getObjectFactory();
      EARModule[] var2 = this.ctx.getModules();
      if (debug) {
         this.say("No. of regular modules found " + var2.length);
      }

      if (var1 != null) {
         ArrayList var4 = new ArrayList();

         EditableJ2eeApplicationObject var3;
         try {
            var3 = var1.createApplicationObject();
            var3.setVirtualJarFile(this.ctx.getVSource());
            var3.setRootBean((DescriptorBean)this.ctx.getApplicationDescriptor().getApplicationDescriptor());
            var3.addRootBean("META-INF/application.xml", (DescriptorBean)this.ctx.getApplicationDescriptor().getApplicationDescriptor(), (ModuleType)null);
            var3.addRootBean("META-INF/weblogic-application.xml", (DescriptorBean)this.ctx.getApplicationDescriptor().getWeblogicApplicationDescriptor(), (ModuleType)null);
            var3.addRootBean("META-INF/weblogic-extension.xml", (DescriptorBean)this.ctx.getApplicationDescriptor().getWeblogicExtensionDescriptor(), (ModuleType)null);
         } catch (XMLStreamException var7) {
            throw new ToolFailureException("Unable to create Application Object", var7);
         } catch (IOException var8) {
            throw new ToolFailureException("Unable to create Application Object", var8);
         }

         try {
            for(int var5 = 0; var5 < var2.length; ++var5) {
               if (var2[var5] instanceof WLSModule) {
                  this.addWLSModuleDescriptor(var3, (WLSModule)var2[var5]);
               } else {
                  var4.add(this.createDeployableObject(var1, var2[var5]));
               }
            }
         } catch (IOException var10) {
            throw new ToolFailureException("Unable to create deployable object", var10);
         }

         try {
            this.addCustomeModuleDescriptors(var3);
            this.addPersistenceDescriptors(var3);
            var3.setClassLoader(this.ctx.getApplicationContext().getAppClassLoader());
            var3.setResourceFinder(new ApplicationResourceFinder(this.ctx.getEar().getURI(), this.ctx.getApplicationContext().getAppClassLoader().getClassFinder()));
            this.ctx.setDeployableApplication(var3);
            Iterator var11 = var4.iterator();

            while(var11.hasNext()) {
               EditableDeployableObject var6 = (EditableDeployableObject)var11.next();
               var3.addDeployableObject(var6);
            }

            this.addDiagnosticDescriptor(var3);
         } catch (IOException var9) {
            throw new ToolFailureException("Unable to create Application Object", var9);
         }
      }

   }

   public void cleanup() {
   }

   private void addCustomeModuleDescriptors(EditableJ2eeApplicationObject var1) {
      EARModule[] var2 = this.ctx.getCustomModules();
      if (var2 != null) {
         if (debug) {
            this.say("No. of custom modules found " + var2.length);
         }

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (debug) {
               this.say("Adding descriptor for CustomModule " + var2[var3].getURI());
            }

            this.addModuleDescriptor(var1, var2[var3]);
         }

      }
   }

   private void addWLSModuleDescriptor(EditableJ2eeApplicationObject var1, WLSModule var2) {
      if (debug) {
         this.say("Adding descriptor for WLSModule " + var2.getURI());
      }

      this.addModuleDescriptor(var1, var2);
   }

   private void addModuleDescriptor(EditableJ2eeApplicationObject var1, EARModule var2) {
      String[] var3 = var2.getDescriptorUris();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         DescriptorBean var5 = var2.getRootBean(var3[var4]);
         if (var5 != null) {
            if (debug) {
               this.say("Adding descriptor " + var3[var4]);
            }

            var1.addRootBean(var3[var4], var5, var2.getModuleType());
         }
      }

   }

   private void addPersistenceDescriptors(EditableJ2eeApplicationObject var1) throws ToolFailureException, IOException {
      Object var2 = this.ctx.getPerViewer();
      if (var2 == null) {
         PersistenceUtils.addRootPersistenceJars(this.ctx.getApplicationContext().getAppClassLoader(), this.ctx.getApplicationContext().getApplicationId(), this.ctx.getApplicationDD());
         var2 = new PersistenceUnitViewer.ResourceViewer(this.ctx.getApplicationContext().getAppClassLoader(), this.ctx.getApplicationContext().getApplicationId(), this.ctx.getConfigDir(), this.ctx.getPlanBean());
         ((PersistenceUnitViewer)var2).loadDescriptors();
      }

      Iterator var3 = ((PersistenceUnitViewer)var2).getDescriptorURIs();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (debug) {
            this.say("Adding uri " + var4);
         }

         Descriptor var5 = ((PersistenceUnitViewer)var2).getDescriptor(var4);
         var1.addRootBean(var4, var5.getRootBean(), (ModuleType)null);
      }

   }

   private void addDiagnosticDescriptor(EditableJ2eeApplicationObject var1) {
      File var2 = null;
      DeploymentPlanBean var3 = this.ctx.getPlanBean();
      File var4 = this.ctx.getConfigDir();
      if (var3 != null) {
         ModuleOverrideBean[] var5 = var3.getModuleOverrides();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].getModuleName().equals(this.ctx.getEar().getURI()) && var5[var6].getModuleType().equals(var1.getType().toString())) {
               ModuleDescriptorBean[] var7 = var5[var6].getModuleDescriptors();

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  if (var7[var8].isExternal() && var7[var8].getRootElement().equals("wldf-resource")) {
                     var2 = new File(var4, var7[var8].getUri());
                     if (var2.isFile() && var2.exists()) {
                        break;
                     }

                     var2 = null;
                  }
               }
            }

            if (var2 != null) {
               break;
            }
         }
      }

      try {
         DescriptorBean var10 = var2 != null ? WLDFModule.getDescriptorBean(var2, this.ctx.getConfigDir(), this.ctx.getPlanBean(), this.ctx.getEar().getURI(), "META-INF/weblogic-diagnostics.xml") : WLDFModule.getDescriptorBean(this.ctx.getVSource(), this.ctx.getConfigDir(), this.ctx.getPlanBean(), this.ctx.getEar().getURI(), "META-INF/weblogic-diagnostics.xml");
         if (var10 != null) {
            var1.addRootBean("META-INF/weblogic-diagnostics.xml", var10, (ModuleType)null);
         } else {
            var1.addRootBean("META-INF/weblogic-diagnostics.xml", (new DescriptorManager()).createDescriptorRoot(WLDFResourceBean.class).getRootBean(), (ModuleType)null);
         }
      } catch (Exception var9) {
      }

   }

   public static boolean isSupportedURI(ModuleType var0, String var1) {
      if (var0 != null && var1 != null) {
         Set var2 = (Set)supportedURIs.get(var0);
         if (var2 != null) {
            return var2.contains(var1);
         }
      }

      return false;
   }

   static {
      HashSet var0 = new HashSet();
      var0.add("META-INF/application.xml");
      var0.add("META-INF/weblogic-application.xml");
      var0.add("META-INF/weblogic-extension.xml");
      var0.add("META-INF/persistence.xml");
      var0.add("META-INF/persistence-configuration.xml");
      supportedURIs.put(ModuleType.EAR, var0);
      HashSet var1 = new HashSet();
      var1.add("WEB-INF/web.xml");
      var1.add("WEB-INF/weblogic.xml");
      var1.add("WEB-INF/weblogic-extension.xml");
      var1.add("META-INF/persistence.xml");
      var1.add("META-INF/persistence-configuration.xml");
      var1.add("WEB-INF/webservices.xml");
      var1.add("WEB-INF/weblogic-webservices.xml");
      supportedURIs.put(ModuleType.WAR, var1);
      HashSet var2 = new HashSet();
      var2.add("META-INF/ejb-jar.xml");
      var2.add("META-INF/weblogic-ejb-jar.xml");
      var2.add("META-INF/persistence.xml");
      var2.add("META-INF/persistence-configuration.xml");
      var2.add("META-INF/webservices.xml");
      var2.add("META-INF/weblogic-webservices.xml");
      supportedURIs.put(ModuleType.EJB, var2);
   }

   public static class ApplicationResourceFinder implements ClassFinder {
      private ClassFinder delegate;
      private String prefix;

      public ApplicationResourceFinder(String var1, ClassFinder var2) {
         this.prefix = var1 + "#";
         this.delegate = var2;
      }

      public Source getSource(String var1) {
         return this.delegate.getSource(this.prefix + var1);
      }

      public Enumeration getSources(String var1) {
         return this.delegate.getSources(this.prefix + var1);
      }

      public Source getClassSource(String var1) {
         return this.delegate.getClassSource(var1);
      }

      public String getClassPath() {
         return this.delegate.getClassPath();
      }

      public ClassFinder getManifestFinder() {
         return this.delegate.getManifestFinder();
      }

      public Enumeration entries() {
         return this.delegate.entries();
      }

      public void close() {
         this.delegate.close();
      }
   }
}
