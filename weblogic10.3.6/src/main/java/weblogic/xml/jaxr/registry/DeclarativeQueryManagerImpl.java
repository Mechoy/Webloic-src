package weblogic.xml.jaxr.registry;

import javax.xml.registry.BulkResponse;
import javax.xml.registry.DeclarativeQueryManager;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.Query;

public class DeclarativeQueryManagerImpl extends QueryManagerImpl implements DeclarativeQueryManager {
   public DeclarativeQueryManagerImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public Query createQuery(int var1, String var2) throws InvalidRequestException, JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }

   public BulkResponse executeQuery(Query var1) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }
}
