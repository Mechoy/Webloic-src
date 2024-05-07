package weblogic.xml.jaxr.registry.provider;

import weblogic.webservice.async.KernelFeeder;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.util.JAXRLogger;

public class WeblogicEnvironmentHelper extends EnvironmentHelper {
   public Boolean isJ2EEContainer(RegistryServiceImpl var1) {
      return this.isRunningOnServer(var1.getLogger()) ? Boolean.TRUE : Boolean.FALSE;
   }

   private boolean isRunningOnServer(JAXRLogger var1) {
      boolean var2;
      try {
         Class.forName("weblogic.kernel.Kernel");
         KernelFeeder var3 = (KernelFeeder)Class.forName("weblogic.webservice.async.KernelFeederImpl").newInstance();
         var2 = var3.isServer();
      } catch (Throwable var4) {
         var2 = false;
         var1.error(var4);
      }

      return var2;
   }
}
