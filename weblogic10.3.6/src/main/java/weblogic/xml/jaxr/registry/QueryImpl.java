package weblogic.xml.jaxr.registry;

import javax.xml.registry.JAXRException;
import javax.xml.registry.Query;

public class QueryImpl extends BaseJAXRObject implements Query {
   private RegistryServiceImpl m_registryServiceImpl;

   public QueryImpl(RegistryServiceImpl var1) {
      this.m_registryServiceImpl = var1;
   }

   public int getType() throws JAXRException {
      this.checkCapability(this.m_registryServiceImpl, 1);
      return 0;
   }

   public String toString() {
      return null;
   }
}
