package weblogic.deployment;

import java.net.MalformedURLException;
import java.util.Map;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.EarUtils;
import weblogic.utils.classloaders.GenericClassLoader;

public class EarPersistenceUnitRegistry extends AbstractPersistenceUnitRegistry {
   private Map persistenceUnitDescriptors;
   private Map persistenceConfigDescriptors;

   public EarPersistenceUnitRegistry(GenericClassLoader var1, ApplicationContextInternal var2) throws EnvironmentException, MalformedURLException {
      super(var1, var2.getApplicationId(), EarUtils.getConfigDir(var2), var2.findDeploymentPlan(), var2);

      try {
         this.loadPersistenceDescriptors(true);
      } catch (EnvironmentException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new EnvironmentException(var5);
      }
   }

   public void initialize() throws EnvironmentException {
      super.storeDescriptors(this.persistenceUnitDescriptors, this.persistenceConfigDescriptors);
   }

   protected void storeDescriptors(Map var1, Map var2) {
      this.persistenceUnitDescriptors = var1;
      this.persistenceConfigDescriptors = var2;
   }

   public PersistenceUnitInfoImpl getPersistenceUnit(String var1) throws IllegalArgumentException {
      if (var1.startsWith("../")) {
         int var2 = var1.indexOf("#");
         if (var2 < 0) {
            return null;
         }

         var1 = var1.substring(var2 + 1);
      }

      return (PersistenceUnitInfoImpl)this.persistenceUnits.get(var1);
   }
}
