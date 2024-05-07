package weblogic.xml.registry;

import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.XmlBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.GenericClassLoader;

public class XMLModule implements Module {
   private static boolean debug = false;
   private AppDeploymentMBean deployableMBean;
   private ApplicationContext appCtx;
   private XmlBean xmlDD;
   private final String WEBLOGIC_APPLICATION_DESCRIPTOR = "weblogic-application.xml";
   private final String REGISTRY_DIR = "lib/xml/registry";
   private String[] changedFiles = null;
   private boolean loadDescriptorEnabled = false;
   private boolean cleanUp = false;
   private boolean cleanUpCache = false;
   private boolean reInitialize = true;
   private ClassLoader classLoader;

   public XMLModule(XmlBean var1) {
      this.xmlDD = var1;
   }

   public String getId() {
      return "weblogic.xml.registry.XMLModule";
   }

   public String getType() {
      return null;
   }

   public DescriptorBean[] getDescriptors() {
      return this.xmlDD != null ? new DescriptorBean[]{(DescriptorBean)this.xmlDD} : new DescriptorBean[0];
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[0];
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.init(var1, var2, var3);
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.classLoader = var2;
      this.appCtx = var1;
      this.deployableMBean = var1.getAppDeploymentMBean();
      return var2;
   }

   public void setDelta(String[] var1, long[] var2) {
      this.changedFiles = var1;
      if (debug) {
         Debug.say("ChangedFiles is " + var1);
      }

      if (var1 == null) {
         this.reInitialize = true;
      } else {
         if (debug) {
            Debug.say("Changed Files length is : " + var1.length);
         }

         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var1[var3].endsWith("weblogic-application.xml")) {
               if (debug) {
                  Debug.say("----> weblogic-application.xml descriptor has changes");
               }

               this.reInitialize = true;
               this.cleanUp = true;
            }

            if (var1[var3].startsWith("lib/xml/registry")) {
               this.cleanUpCache = true;
            }
         }

      }
   }

   public void prepare() throws ModuleException {
      if (this.cleanUp) {
         try {
            XMLRegistry.cleanUpAppScopedXMLRegistry(this.deployableMBean.getName());
         } catch (XMLRegistryException var4) {
            throw new ModuleException(var4.getMessage());
         }
      }

      if (this.reInitialize) {
         if (debug) {
            Debug.say("Preparing XMLModule ------>>>>>> \n ");
         }

         try {
            String var1 = ((ApplicationContextInternal)this.appCtx).getStagingPath();
            if (debug) {
               Debug.say("---------> Path is " + var1);
            }

            XMLRegistry.initializeAppScopedXMLRegistry(this.xmlDD, this.deployableMBean, var1);
         } catch (XMLRegistryException var3) {
            throw new ModuleException(var3.getMessage());
         }
      }

      if (!this.cleanUp && this.cleanUpCache) {
         try {
            XMLRegistry var5 = XMLRegistry.getXMLRegistry(this.appCtx.getApplicationId());
            var5.cleanUpCache(this.changedFiles);
         } catch (XMLRegistryException var2) {
            throw new ModuleException(var2.getMessage());
         }
      }

      this.cleanUp = false;
      this.cleanUpCache = false;
      this.reInitialize = false;
   }

   public void unprepare() throws IllegalStateException, ModuleException {
      try {
         if (debug) {
            Debug.say("----> Rolling Back XML Module ");
         }

         XMLRegistry.cleanUpAppScopedXMLRegistry(this.deployableMBean.getName());
      } catch (XMLRegistryException var2) {
         throw new ModuleException(var2);
      } catch (Exception var3) {
         throw new ModuleException(var3);
      }
   }

   public void activate() {
   }

   public void start() {
   }

   public void destroy(UpdateListener.Registration var1) {
   }

   public void deactivate() throws IllegalStateException, ModuleException {
   }

   public void remove() throws IllegalStateException, ModuleException {
   }

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) {
   }

   public void forceProductionToAdmin() {
   }
}
