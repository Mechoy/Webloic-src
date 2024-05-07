package weblogic.deployment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.ModuleLocationInfo;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.IOUtils;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.ejb.container.deployer.EJBModule;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public class ModulePersistenceUnitRegistry extends AbstractPersistenceUnitRegistry {
   private EarPersistenceUnitRegistry parent;

   public ModulePersistenceUnitRegistry(GenericClassLoader var1, ApplicationContextInternal var2, Module var3, boolean var4) throws EnvironmentException, MalformedURLException {
      super(var1, var3.getId(), EarUtils.getConfigDir(var2), var2.findDeploymentPlan(), var2);
      this.parent = (EarPersistenceUnitRegistry)var2.getUserObject(EarPersistenceUnitRegistry.class);

      try {
         File var5 = this.getOutputFile(var2, var3);
         URL var6 = var5.toURL();
         if (var3.getType().equals(WebLogicModuleType.MODULETYPE_WAR)) {
            this.loadPersistenceDescriptors(var4);
         } else {
            VirtualJarFile var7 = null;

            try {
               if (var3 instanceof EJBModule) {
                  var7 = var2.getApplicationFileManager().getVirtualJarFile(var3.getId());
               } else {
                  var7 = var2.getApplicationFileManager().getVirtualJarFile(((ModuleLocationInfo)var3).getModuleURI());
               }

               this.loadPersistenceDescriptor(var7, var4, var5);
            } finally {
               IOUtils.forceClose(var7);
            }
         }

      } catch (EnvironmentException var14) {
         throw var14;
      } catch (Exception var15) {
         throw new EnvironmentException(var15);
      }
   }

   protected File getOutputFile(ApplicationContextInternal var1, Module var2) {
      String var3 = var1.getOutputPath();
      if (var1.isEar()) {
         var3 = var3 + System.getProperty("file.separator");
         return var2 instanceof EJBModule ? var1.getApplicationFileManager().getOutputPath(var2.getId()) : var1.getApplicationFileManager().getOutputPath(((ModuleLocationInfo)var2).getModuleURI());
      } else {
         return new File(var3);
      }
   }

   public PersistenceUnitInfoImpl getPersistenceUnit(String var1) throws IllegalArgumentException {
      PersistenceUnitInfoImpl var2 = (PersistenceUnitInfoImpl)this.persistenceUnits.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         if (this.parent != null) {
            var2 = this.parent.getPersistenceUnit(var1);
         }

         if (var2 == null) {
            throw new IllegalArgumentException("No persistence unit named '" + var1 + "' is available in scope " + this.getScopeName() + ". Available persistence units: " + this.getPersistenceUnitNames());
         } else {
            return var2;
         }
      }
   }

   public void close() {
      super.close();
      if (this.parent != null) {
         this.parent.close();
      }

   }

   public Collection getPersistenceUnitNames() {
      Collection var1 = super.getPersistenceUnitNames();
      if (this.parent != null) {
         var1.addAll(this.parent.getPersistenceUnitNames());
      }

      return var1;
   }

   void putPersistenceUnit(PersistenceUnitInfoImpl var1) throws EnvironmentException {
      if (var1.getPersistenceUnitName().startsWith("../")) {
         throw new IllegalArgumentException("'" + var1.getPersistenceUnitName() + "' is not a valid persistence unit name. A persistence unit name " + "cannot start with '../'.");
      } else {
         super.putPersistenceUnit(var1);
      }
   }

   public String getQualifiedName() {
      return this.parent == null ? this.getScopeName() : this.parent.getQualifiedName() + "#" + this.getScopeName();
   }
}
