package weblogic.application.internal.flow;

import java.io.IOException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.PersistenceUtils;
import weblogic.deployment.EarPersistenceUnitRegistry;
import weblogic.deployment.EnvironmentException;
import weblogic.management.DeploymentException;

public class ParseJpaDescriptorFlow extends BaseFlow {
   public ParseJpaDescriptorFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      try {
         if (this.appCtx.isEar()) {
            PersistenceUtils.addRootPersistenceJars(this.appCtx.getAppClassLoader(), this.appCtx.getApplicationId(), this.appCtx.getApplicationDD());
            EarPersistenceUnitRegistry var1 = new EarPersistenceUnitRegistry(this.appCtx.getAppClassLoader(), this.appCtx);
            this.appCtx.putUserObject(EarPersistenceUnitRegistry.class, var1);
         }

      } catch (EnvironmentException var2) {
         throw new DeploymentException(var2);
      } catch (IOException var3) {
         throw new DeploymentException(var3);
      }
   }
}
