package weblogic.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import weblogic.application.library.LibraryFactory;

public final class ApplicationFactoryManager {
   private DeploymentFactory lastDeploymentFactory;
   private LibraryFactory defaultLibFactory;
   private List<DeploymentFactory> deploymentFactories = Collections.emptyList();
   private List<ModuleFactory> moduleFactories = Collections.emptyList();
   private List<WeblogicModuleFactory> weblogicModuleFactories = Collections.emptyList();
   private List<WebLogicApplicationModuleFactory> wlappModuleFactories = Collections.emptyList();
   private List<LibraryFactory> libraryFactories = Collections.emptyList();
   private List<ExtensionModuleFactory> wlExtensionModuleFactories = Collections.emptyList();
   private List<ComponentMBeanFactory> compFactories = null;
   private List<AppDeploymentExtensionFactory> appExtFactories = Collections.emptyList();
   private static final String[] compMBeanFactories = new String[]{"weblogic.servlet.internal.WarDeploymentFactory", "weblogic.connector.deploy.ConnectorDeploymentFactory", "weblogic.application.internal.EarDeploymentFactory", "weblogic.ejb.container.deployer.EJBDeploymentFactory"};
   private static final ApplicationFactoryManager theOne = new ApplicationFactoryManager();

   private ApplicationFactoryManager() {
   }

   public static ApplicationFactoryManager getApplicationFactoryManager() {
      return theOne;
   }

   public static ApplicationFactoryManager getEmptyApplicationFactoryManager() {
      return new ApplicationFactoryManager();
   }

   public synchronized void addLastDeploymentFactory(DeploymentFactory var1) {
      if (this.lastDeploymentFactory != null) {
         throw new AssertionError("Attempt to add " + var1.getClass().getName() + " as the last DeploymentFactory, " + "but " + this.lastDeploymentFactory.getClass().getName() + " has already used this hack.");
      } else {
         this.lastDeploymentFactory = var1;
         ArrayList var2 = new ArrayList(this.deploymentFactories.size() + 1);
         var2.addAll(this.deploymentFactories);
         var2.add(this.lastDeploymentFactory);
         this.deploymentFactories = var2;
      }
   }

   public synchronized void addDeploymentFactory(DeploymentFactory var1) {
      ArrayList var2 = new ArrayList(this.deploymentFactories.size() + 1);
      var2.addAll(this.deploymentFactories);
      if (this.lastDeploymentFactory != null) {
         var2.add(this.deploymentFactories.size() - 1, var1);
      } else {
         var2.add(var1);
      }

      this.deploymentFactories = var2;
   }

   public synchronized void addDeploymentFactoryFirst(DeploymentFactory var1) {
      ArrayList var2 = new ArrayList(this.deploymentFactories.size() + 1);
      var2.add(var1);
      var2.addAll(this.deploymentFactories);
      this.deploymentFactories = var2;
   }

   public Iterator<DeploymentFactory> getDeploymentFactories() {
      return this.deploymentFactories.iterator();
   }

   public synchronized void addModuleFactory(ModuleFactory var1) {
      ArrayList var2 = new ArrayList(this.moduleFactories.size() + 1);
      var2.addAll(this.moduleFactories);
      var2.add(var1);
      this.moduleFactories = var2;
   }

   public Iterator<ModuleFactory> getModuleFactories() {
      return this.moduleFactories.iterator();
   }

   public synchronized Iterator<ComponentMBeanFactory> getComponentMBeanFactories() {
      if (this.compFactories == null) {
         this.initCompFactories();
      }

      return this.compFactories.iterator();
   }

   private synchronized void initCompFactories() {
      ComponentMBeanFactory[] var1 = new ComponentMBeanFactory[compMBeanFactories.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         try {
            var1[var2] = (ComponentMBeanFactory)Class.forName(compMBeanFactories[var2]).newInstance();
         } catch (Exception var4) {
            throw new AssertionError(var4);
         }
      }

      this.compFactories = Arrays.asList(var1);
   }

   public synchronized void addWblogicModuleFactory(WeblogicModuleFactory var1) {
      ArrayList var2 = new ArrayList(this.weblogicModuleFactories.size() + 1);
      var2.addAll(this.weblogicModuleFactories);
      var2.add(var1);
      this.weblogicModuleFactories = var2;
   }

   public Iterator<WeblogicModuleFactory> getWeblogicModuleFactories() {
      return this.weblogicModuleFactories.iterator();
   }

   public synchronized void addWLAppModuleFactory(WebLogicApplicationModuleFactory var1) {
      ArrayList var2 = new ArrayList(this.wlappModuleFactories.size() + 1);
      var2.addAll(this.wlappModuleFactories);
      var2.add(var1);
      this.wlappModuleFactories = var2;
   }

   public Iterator<WebLogicApplicationModuleFactory> getWLAppModuleFactories() {
      return this.wlappModuleFactories.iterator();
   }

   public synchronized void addDefaultLibraryFactory(LibraryFactory var1) {
      if (this.defaultLibFactory != null) {
         throw new AssertionError("Attempt to add " + var1.getClass().getName() + " as the default " + "LibraryFactory, but " + this.defaultLibFactory.getClass().getName() + " has already used this feature.");
      } else {
         this.addLibraryFactory(var1);
         this.defaultLibFactory = var1;
      }
   }

   public synchronized void addLibraryFactory(LibraryFactory var1) {
      ArrayList var2 = new ArrayList(this.libraryFactories.size() + 1);
      var2.addAll(this.libraryFactories);
      if (this.defaultLibFactory != null) {
         var2.add(this.libraryFactories.size() - 1, var1);
      } else {
         var2.add(var1);
      }

      this.libraryFactories = var2;
   }

   public Iterator<LibraryFactory> getLibraryFactories() {
      return this.libraryFactories.iterator();
   }

   public synchronized void addWebLogicExtensionModuleFactory(ExtensionModuleFactory var1) {
      ArrayList var2 = new ArrayList(this.wlExtensionModuleFactories.size() + 1);
      var2.addAll(this.wlExtensionModuleFactories);
      var2.add(var1);
      this.wlExtensionModuleFactories = var2;
   }

   public Iterator<ExtensionModuleFactory> getWebLogicExtenstionModuleFactories() {
      return this.wlExtensionModuleFactories.iterator();
   }

   public synchronized void addAppDeploymentExtensionFactory(AppDeploymentExtensionFactory var1) {
      ArrayList var2 = new ArrayList(this.appExtFactories.size() + 1);
      var2.addAll(this.appExtFactories);
      var2.add(var1);
      this.appExtFactories = var2;
   }

   public Iterator<AppDeploymentExtensionFactory> getAppExtensionFactories() {
      return this.appExtFactories.iterator();
   }
}
