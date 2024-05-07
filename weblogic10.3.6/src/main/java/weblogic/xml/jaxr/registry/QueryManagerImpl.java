package weblogic.xml.jaxr.registry;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import javax.xml.registry.QueryManager;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.RegistryObject;
import weblogic.xml.jaxr.registry.util.JAXRLogger;

public class QueryManagerImpl extends BaseJAXRObject implements QueryManager {
   private RegistryServiceImpl m_registryServiceImpl;

   public QueryManagerImpl(RegistryServiceImpl var1) {
      this.m_registryServiceImpl = var1;
   }

   public RegistryObject getRegistryObject(String var1, String var2) throws JAXRException {
      return this.m_registryServiceImpl.getRegistryProxy().getRegistryObject(var1, var2);
   }

   public RegistryObject getRegistryObject(String var1) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }

   public BulkResponse getRegistryObjects(Collection var1) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }

   public BulkResponse getRegistryObjects(Collection var1, String var2) throws JAXRException {
      return this.m_registryServiceImpl.getRegistryProxy().getRegistryObjects(var1, var2);
   }

   public BulkResponse getRegistryObjects() throws JAXRException {
      return this.m_registryServiceImpl.getRegistryProxy().getRegistryObjects();
   }

   public BulkResponse getRegistryObjects(String var1) throws JAXRException {
      return this.m_registryServiceImpl.getRegistryProxy().getRegistryObjects(var1);
   }

   public RegistryService getRegistryService() throws JAXRException {
      return this.m_registryServiceImpl;
   }

   protected JAXRLogger getLogger() {
      return this.m_registryServiceImpl.getLogger();
   }
}
