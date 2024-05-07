package weblogic.application.internal.flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.application.AppClassLoaderManager;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.internal.AppClassLoaderManagerImpl;
import weblogic.application.internal.Flow;
import weblogic.deploy.container.NonFatalDeploymentException;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.wl.ClassloaderStructureBean;
import weblogic.j2ee.descriptor.wl.ModuleRefBean;
import weblogic.management.DeploymentException;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.MultiClassFinder;

public final class InitModulesFlow extends BaseFlow implements Flow {
   private static final AppClassLoaderManagerImpl appClassLoaderManager = (AppClassLoaderManagerImpl)AppClassLoaderManager.getAppClassLoaderManager();
   private static final boolean VERBOSE = false;
   private Set clstructLoaders;
   private List missingModuleRefs;

   public InitModulesFlow(ApplicationContextInternal var1) {
      super(var1);
      this.clstructLoaders = Collections.EMPTY_SET;
      this.missingModuleRefs = new ArrayList();
   }

   private Module findModuleWithUriInternal(Module[] var1, String var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var2.equals(var1[var3].getId())) {
            return var1[var3];
         }
      }

      return null;
   }

   private Module findWebModule(ApplicationBean var1, Module[] var2, String var3) {
      ModuleBean[] var4 = var1.getModules();
      if (var4 == null) {
         return null;
      } else {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getWeb() != null && var4[var5].getWeb().getWebUri().equals(var3)) {
               return this.findModuleWithUriInternal(var2, var4[var5].getWeb().getContextRoot());
            }
         }

         return null;
      }
   }

   private Module findModuleWithUri(Module[] var1, String var2) throws DeploymentException {
      Module var3 = this.findModuleWithUriInternal(var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         var3 = this.findWebModule(this.appCtx.getApplicationDD(), var1, var2);
         if (var3 != null) {
            return var3;
         } else {
            Module[] var4 = this.appCtx.getApplicationModules();
            if (this.findModuleWithUriInternal(var4, var2) == null && this.findWebModule(this.appCtx.getApplicationDD(), var4, var2) == null && this.appCtx.getPartialRedeployURIs() == null) {
               throw new DeploymentException("classloader-structure element in weblogic-application.xml is referencing the module-uri " + var2 + " which does not exist in this application.");
            } else {
               return null;
            }
         }
      }
   }

   private void initClassLoaderStructure(ClassloaderStructureBean var1, GenericClassLoader var2, Map var3, Module[] var4) throws DeploymentException {
      ModuleRefBean[] var5 = var1.getModuleRefs();
      ArrayList var6 = new ArrayList();

      for(int var7 = 0; var7 < var5.length; ++var7) {
         String var8 = var5[var7].getModuleUri();
         Module var9 = this.findModuleWithUri(var4, var8);
         if (var9 != null) {
            if (this.missingModuleRefs.size() > 0 || var6.size() > 0) {
               throw new NonFatalDeploymentException(" The following modules: " + (this.missingModuleRefs.size() > 0 ? this.missingModuleRefs.toString() : "") + (var6.size() > 0 ? var6.toString() : "") + " which module uri " + var8 + " depends on are not initiated in this application.");
            }

            String var10 = var2.getAnnotation().getAnnotationString();
            if (var10 == null || "".equals(var10)) {
               var2.setAnnotation(new Annotation(this.appCtx.getAppDeploymentMBean().getApplicationIdentifier(), var8));
            }

            if (var3.get(var8) != null) {
               throw new DeploymentException("The module-uri " + var8 + " is declared more than once in the classloader-structure." + "  Check your weblogic-application.xml");
            }

            var9.initUsingLoader(this.appCtx, var2, this.appCtx);
            appClassLoaderManager.addModuleLoader(var2, var9.getId());
            var3.put(var8, var9);
            if (!var8.equals(var9.getId())) {
               var3.put(var9.getId(), var9);
            }
         } else {
            Module var15 = this.findModuleWithUri(this.appCtx.getApplicationModules(), var8);
            if (var15 == null) {
               if (this.appCtx.getPartialRedeployURIs() == null) {
                  throw new DeploymentException("classloader-structure element in weblogic-application.xml is referencing the module-uri " + var8 + " which does not exist in this application.");
               }

               var6.add(var8);
            } else {
               GenericClassLoader var11 = appClassLoaderManager.findModuleLoader(this.appCtx.getAppDeploymentMBean().getApplicationIdentifier(), var15.getId());
               if (var11 != null) {
                  this.clstructLoaders.remove(var2);
                  var2 = var11;
               }
            }
         }
      }

      ClassloaderStructureBean[] var12 = var1.getClassloaderStructures();
      if (var12 != null && var12.length != 0) {
         if (var6.size() > 0) {
            this.missingModuleRefs.add(var6);
         }

         for(int var13 = 0; var13 < var12.length; ++var13) {
            GenericClassLoader var14 = new GenericClassLoader(new MultiClassFinder(), var2);
            if (this.clstructLoaders.size() == 0) {
               this.clstructLoaders = new LinkedHashSet();
            }

            this.clstructLoaders.add(var14);
            this.initClassLoaderStructure(var12[var13], var14, var3, var4);
         }

         this.missingModuleRefs.remove(var6);
      }
   }

   private void initRemainingModules(Map var1, Module[] var2) throws DeploymentException {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = var2[var3].getId();
         if (!var1.containsKey(var4)) {
            this.initModule(var2[var3]);
            var1.put(var4, var2[var3]);
         }
      }

   }

   private void initModulesCLStructure(Module[] var1) throws DeploymentException {
      HashMap var2 = new HashMap(var1.length);
      GenericClassLoader var3 = this.appCtx.getAppClassLoader();
      ClassloaderStructureBean var4 = this.appCtx.getWLApplicationDD().getClassloaderStructure();

      try {
         this.initClassLoaderStructure(var4, var3, var2, var1);
         this.initRemainingModules(var2, var1);
      } catch (Throwable var10) {
         this.destroy(var2);
         this.throwAppException(var10);
      } finally {
         this.missingModuleRefs.clear();
      }

   }

   private void destroy(Map var1) throws DeploymentException {
      Module[] var2 = new Module[var1.size()];
      this.destroy((Module[])((Module[])var1.values().toArray(var2)), var2.length);
   }

   private void initModule(Module var1) throws ModuleException {
      GenericClassLoader var2 = var1.init(this.appCtx, this.appCtx.getAppClassLoader(), this.appCtx);
      appClassLoaderManager.addModuleLoader(var2, var1.getId());
   }

   private void initModules(Module[] var1) throws DeploymentException {
      GenericClassLoader var2 = this.appCtx.getAppClassLoader();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         try {
            this.initModule(var1[var3]);
         } catch (Throwable var7) {
            try {
               this.destroy(var3);
            } catch (DeploymentException var6) {
               J2EELogger.logIgnoringUndeploymentError(var6);
            }

            this.throwAppException(var7);
         }
      }

   }

   private boolean hasClassLoaderStructure() {
      return this.appCtx.getWLApplicationDD() != null && this.appCtx.getWLApplicationDD().getClassloaderStructure() != null;
   }

   public void prepare() throws DeploymentException {
      Module[] var1 = this.appCtx.getApplicationModules();
      if (this.appCtx.getStoppedModules() != null) {
         String[] var2 = this.appCtx.getStoppedModules();
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var1.length; ++var4) {
            var3.add(var1[var4]);

            for(int var5 = 0; var5 < var2.length; ++var5) {
               if (var1[var4].getId().equals(var2[var5])) {
                  var3.remove(var1[var4]);
                  break;
               }
            }
         }

         this.appCtx.setApplicationModules((Module[])((Module[])var3.toArray(new Module[var3.size()])));
      }

      GenericClassLoader var6 = this.appCtx.getAppClassLoader();
      if (this.hasClassLoaderStructure()) {
         this.initModulesCLStructure(var1);
      } else {
         this.initModules(var1);
      }

   }

   public void unprepare() throws DeploymentException {
      Module[] var1 = this.appCtx.getApplicationModules();

      try {
         this.destroy(var1, var1.length);
      } finally {
         if (this.clstructLoaders.size() > 0) {
            Iterator var4 = this.clstructLoaders.iterator();

            while(var4.hasNext()) {
               GenericClassLoader var5 = (GenericClassLoader)var4.next();
               var5.close();
            }

            this.clstructLoaders = Collections.EMPTY_SET;
         }

      }

   }

   private void destroy(int var1) throws DeploymentException {
      Module[] var2 = this.appCtx.getApplicationModules();
      this.destroy(var2, var1);
   }

   private void destroy(Module[] var1, int var2) throws DeploymentException {
      ErrorCollectionException var3 = null;

      for(int var4 = var2 - 1; var4 >= 0; --var4) {
         try {
            var1[var4].destroy(this.appCtx);
         } catch (Throwable var6) {
            if (var3 == null) {
               var3 = new ErrorCollectionException();
            }

            var3.addError(var6);
         }
      }

      if (var3 != null) {
         this.throwAppException(var3);
      }

   }

   public void start(String[] var1) throws DeploymentException {
      Module[] var2 = this.appCtx.getStartingModules();
      if (this.hasClassLoaderStructure()) {
         this.initModulesCLStructure(var2);
      } else {
         this.initModules(var2);
      }

   }

   public void stop(String[] var1) throws DeploymentException {
      Module[] var2 = this.appCtx.getStoppingModules();

      try {
         this.destroy(var2, var2.length);
      } catch (DeploymentException var4) {
         J2EELogger.logIgnoringUndeploymentError(var4);
      }

   }
}
