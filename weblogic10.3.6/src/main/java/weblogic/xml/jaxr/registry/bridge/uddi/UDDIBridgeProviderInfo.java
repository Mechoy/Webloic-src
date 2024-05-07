package weblogic.xml.jaxr.registry.bridge.uddi;

import javax.xml.registry.CapabilityProfile;
import weblogic.xml.jaxr.registry.BaseJAXRObject;
import weblogic.xml.jaxr.registry.CapabilityProfileImpl;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.provider.ProviderInfo;
import weblogic.xml.jaxr.registry.provider.RegistryProxyFactory;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;
import weblogic.xml.jaxr.registry.util.JAXRLogger;

public class UDDIBridgeProviderInfo extends BaseJAXRObject implements ProviderInfo {
   public RegistryProxyFactory getRegistryProxyFactory(RegistryServiceImpl var1) {
      return new UDDIRegistryProxyFactory(var1);
   }

   public CapabilityProfile getCapabilityProfile() {
      String var1 = JAXRMessages.getMessage("jaxr.specifications.version");
      byte var2 = 0;
      return new CapabilityProfileImpl(var1, var2);
   }

   public JAXRLogger getLogger() {
      return UDDIBridgeLogger.getInstance();
   }
}
