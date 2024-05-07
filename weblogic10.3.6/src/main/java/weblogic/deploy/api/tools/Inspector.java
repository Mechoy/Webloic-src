package weblogic.deploy.api.tools;

import java.io.File;
import java.io.IOException;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.deploy.api.model.WebLogicDeployableObject;

public class Inspector {
   private File app;
   private ModuleInfo info;
   private WebLogicDeployableObject dObject;

   public Inspector(File var1) {
      this.app = var1;
   }

   public ModuleInfo getModuleInfo() throws DeploymentManagerCreationException, IOException, InvalidModuleException, ConfigurationException {
      if (this.info != null) {
         return this.info;
      } else {
         this.setup();
         this.info = ModuleInfo.createModuleInfo(this.dObject);
         return this.info;
      }
   }

   private void setup() throws InvalidModuleException, IOException {
      this.dObject = WebLogicDeployableObject.createLazyDeployableObject(this.app, (File)null);
   }

   public void close() {
      if (this.dObject != null) {
         this.dObject.close();
      }

   }
}
