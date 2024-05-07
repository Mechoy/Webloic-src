package weblogic.xml.jaxr.registry.provider;

import weblogic.xml.jaxr.registry.BaseJAXRObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public abstract class EnvironmentHelper extends BaseJAXRObject {
   public static EnvironmentHelper getInstance() {
      return new WeblogicEnvironmentHelper();
   }

   public abstract Boolean isJ2EEContainer(RegistryServiceImpl var1);
}
