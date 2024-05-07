package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.deployment.EarPersistenceUnitRegistry;
import weblogic.deployment.EnvironmentException;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.management.DeploymentException;

public class InitJpaFlow extends BaseFlow {
   public InitJpaFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      if (this.appCtx.isEar()) {
         try {
            EarPersistenceUnitRegistry var1 = (EarPersistenceUnitRegistry)this.appCtx.getUserObject(EarPersistenceUnitRegistry.class);
            if (var1 != null) {
               var1.initialize();
            }
         } catch (EnvironmentException var2) {
            throw new DeploymentException(var2);
         }
      }

   }

   public void unprepare() throws DeploymentException {
      if (this.appCtx.isEar()) {
         PersistenceUnitRegistry var1 = (PersistenceUnitRegistry)this.appCtx.getUserObject(EarPersistenceUnitRegistry.class);
         if (var1 != null) {
            var1.close();
            var1 = null;
         }
      }

   }
}
