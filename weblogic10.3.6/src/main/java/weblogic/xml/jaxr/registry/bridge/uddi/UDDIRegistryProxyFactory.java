package weblogic.xml.jaxr.registry.bridge.uddi;

import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.provider.RegistryProxy;
import weblogic.xml.jaxr.registry.provider.RegistryProxyFactory;

public class UDDIRegistryProxyFactory extends RegistryProxyFactory {
   private RegistryServiceImpl m_registryServiceImpl;

   public UDDIRegistryProxyFactory(RegistryServiceImpl var1) {
      this.m_registryServiceImpl = var1;
   }

   public RegistryProxy createRegistryProxy() throws JAXRException {
      return new UDDIBridgeProxy(this.m_registryServiceImpl);
   }
}
