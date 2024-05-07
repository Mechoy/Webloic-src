package weblogic.xml.jaxr.registry.provider;

import javax.xml.registry.CapabilityProfile;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.util.JAXRLogger;

public interface ProviderInfo {
   RegistryProxyFactory getRegistryProxyFactory(RegistryServiceImpl var1);

   CapabilityProfile getCapabilityProfile();

   JAXRLogger getLogger();
}
