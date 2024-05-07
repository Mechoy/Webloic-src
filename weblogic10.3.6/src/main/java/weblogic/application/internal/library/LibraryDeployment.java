package weblogic.application.internal.library;

import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextFactory;
import weblogic.application.Deployment;
import weblogic.application.RedeployInfo;
import weblogic.application.internal.RedeployInfoImpl;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryDeploymentException;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.application.utils.LibraryUtils;
import weblogic.deploy.container.DeploymentContext;
import weblogic.management.ManagementException;
import weblogic.management.configuration.LibraryMBean;

public class LibraryDeployment implements Deployment {
   private static final LibraryRegistry libraryRegistry = LibraryRegistry.getRegistry();
   private final LibraryDefinition def;
   private final ApplicationContext appCtx;
   private final LibraryMBean mbean;

   public LibraryDeployment(LibraryDefinition var1, LibraryMBean var2) {
      this.def = var1;
      this.mbean = var2;
      this.appCtx = ApplicationContextFactory.getApplicationContextFactory().newApplicationContext(var2.getName());
   }

   public void prepare(DeploymentContext var1) throws LibraryDeploymentException {
      try {
         LibraryLoggingUtils.initLibraryDefinition(this.def);
         this.registerLibrary();
      } catch (LoggableLibraryProcessingException var3) {
         throw new LibraryDeploymentException(var3.getLoggable().getMessage());
      }

      this.initRuntime();
   }

   private void initRuntime() throws LibraryDeploymentException {
      LibraryRuntimeMBeanImpl var1 = null;

      try {
         var1 = new LibraryRuntimeMBeanImpl(this.def.getLibData(), this.mbean.getApplicationIdentifier(), this.mbean.getAppMBean().getComponents());
      } catch (ManagementException var3) {
         throw new LibraryDeploymentException(var3);
      }

      this.def.setRuntime(var1);
   }

   private void registerLibrary() throws LoggableLibraryProcessingException {
      if (LibraryUtils.isDebugOn()) {
         LibraryUtils.debug("Registering: " + this.def + "(" + this.def.getType() + ")");
      }

      LibraryLoggingUtils.registerLibrary(this.def, true);
      if (LibraryUtils.isDebugOn()) {
         LibraryUtils.debug("Registry has: " + libraryRegistry.toString());
      }

   }

   public void unprepare(DeploymentContext var1) throws LibraryDeploymentException {
      if (LibraryUtils.isDebugOn()) {
         LibraryUtils.debug("unprepare");
      }

      this.unregisterLibrary();
   }

   public void remove(DeploymentContext var1) throws LibraryDeploymentException {
      try {
         LibraryLoggingUtils.errorRemoveLibrary(this.def);
      } catch (LoggableLibraryProcessingException var3) {
         throw new LibraryDeploymentException(var3.getLoggable().getMessage());
      }
   }

   public void unregisterLibrary() throws LibraryDeploymentException {
      try {
         this.def.getRuntimeImpl().unregister();
      } catch (ManagementException var2) {
         throw new LibraryDeploymentException(var2);
      }

      if (LibraryUtils.isDebugOn()) {
         LibraryUtils.debug("Removing from registry...: " + this.def);
      }

      libraryRegistry.remove(this.def);
      if (LibraryUtils.isDebugOn()) {
         LibraryUtils.debug("...Now registry has: " + libraryRegistry.toString());
      }

   }

   public void gracefulProductionToAdmin(DeploymentContext var1) {
      Deployment.AdminModeCallback var2 = null;
      if (var1 != null) {
         var2 = var1.getAdminModeCallback();
      }

      if (var2 != null) {
         var2.completed();
      }

   }

   public void forceProductionToAdmin(DeploymentContext var1) {
      Deployment.AdminModeCallback var2 = null;
      if (var1 != null) {
         var2 = var1.getAdminModeCallback();
      }

      if (var2 != null) {
         var2.completed();
      }

   }

   public RedeployInfo validateRedeploy(DeploymentContext var1) {
      return new RedeployInfoImpl();
   }

   public void activate(DeploymentContext var1) {
   }

   public void deactivate(DeploymentContext var1) {
   }

   public void prepareUpdate(DeploymentContext var1) {
   }

   public void activateUpdate(DeploymentContext var1) {
   }

   public void rollbackUpdate(DeploymentContext var1) {
   }

   public void adminToProduction(DeploymentContext var1) {
   }

   public void stop(DeploymentContext var1) {
   }

   public void start(DeploymentContext var1) {
   }

   public boolean deregisterCallback(int var1) {
      return false;
   }

   public ApplicationContext getApplicationContext() {
      return this.appCtx;
   }
}
