package weblogic.xml.jaxr.registry.provider;

import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.BaseJAXRObject;

public abstract class RegistryProxyFactory extends BaseJAXRObject {
   public abstract RegistryProxy createRegistryProxy() throws JAXRException;
}
