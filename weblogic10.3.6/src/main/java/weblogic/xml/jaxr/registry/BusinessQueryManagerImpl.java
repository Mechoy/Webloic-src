package weblogic.xml.jaxr.registry;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.Key;
import weblogic.xml.jaxr.registry.command.CommandHandler;
import weblogic.xml.jaxr.registry.command.FindAssociationsCommand;
import weblogic.xml.jaxr.registry.command.FindCallerAssociationsCommand;
import weblogic.xml.jaxr.registry.command.FindClassificationSchemesCommand;
import weblogic.xml.jaxr.registry.command.FindConceptsCommand;
import weblogic.xml.jaxr.registry.command.FindOrganizationsCommand;
import weblogic.xml.jaxr.registry.command.FindServiceBindingsCommand;
import weblogic.xml.jaxr.registry.command.FindServicesCommand;

public class BusinessQueryManagerImpl extends QueryManagerImpl implements BusinessQueryManager {
   public BusinessQueryManagerImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public BulkResponse findAssociations(Collection var1, String var2, String var3, Collection var4) throws JAXRException {
      RegistryServiceImpl var5 = (RegistryServiceImpl)this.getRegistryService();
      FindAssociationsCommand var6 = new FindAssociationsCommand(var5, var1, var2, var3, var4);
      BulkResponse var7 = CommandHandler.runBulkQueryCommand(var6);
      return var7;
   }

   public BulkResponse findCallerAssociations(Collection var1, Boolean var2, Boolean var3, Collection var4) throws JAXRException {
      RegistryServiceImpl var5 = (RegistryServiceImpl)this.getRegistryService();
      FindCallerAssociationsCommand var6 = new FindCallerAssociationsCommand(var5, var1, var2, var3, var4);
      BulkResponse var7 = CommandHandler.runBulkQueryCommand(var6);
      return var7;
   }

   public BulkResponse findOrganizations(Collection var1, Collection var2, Collection var3, Collection var4, Collection var5, Collection var6) throws JAXRException {
      this.getLogger().trace("+BusinessQueryManager.findOrganizations()");
      this.getLogger().debug("Find Organizations called with findQualifiers [" + var1 + "] namePatterns [" + var2 + "] classifications [" + var3 + "] specifications [" + var4 + "] externalIdentifiers [" + var5 + "] externalLinks [" + var6 + "]");
      RegistryServiceImpl var7 = (RegistryServiceImpl)this.getRegistryService();
      FindOrganizationsCommand var8 = new FindOrganizationsCommand(var7, var1, var2, var3, var4, var5, var6);
      BulkResponse var9 = CommandHandler.runBulkQueryCommand(var8);
      this.getLogger().debug("Find Organizations will return bulkResponse [" + var9 + "]");
      this.getLogger().trace("-BusinessQueryManager.findOrganizations()");
      return var9;
   }

   public BulkResponse findServices(Key var1, Collection var2, Collection var3, Collection var4, Collection var5) throws JAXRException {
      RegistryServiceImpl var6 = (RegistryServiceImpl)this.getRegistryService();
      FindServicesCommand var7 = new FindServicesCommand(var6, var1, var2, var3, var4, var5);
      BulkResponse var8 = CommandHandler.runBulkQueryCommand(var7);
      return var8;
   }

   public BulkResponse findServiceBindings(Key var1, Collection var2, Collection var3, Collection var4) throws JAXRException {
      RegistryServiceImpl var5 = (RegistryServiceImpl)this.getRegistryService();
      FindServiceBindingsCommand var6 = new FindServiceBindingsCommand(var5, var1, var2, var3, var4);
      BulkResponse var7 = CommandHandler.runBulkQueryCommand(var6);
      return var7;
   }

   public BulkResponse findClassificationSchemes(Collection var1, Collection var2, Collection var3, Collection var4) throws JAXRException {
      RegistryServiceImpl var5 = (RegistryServiceImpl)this.getRegistryService();
      FindClassificationSchemesCommand var6 = new FindClassificationSchemesCommand(var5, var1, var2, var3, var4);
      BulkResponse var7 = CommandHandler.runBulkQueryCommand(var6);
      return var7;
   }

   public ClassificationScheme findClassificationSchemeByName(Collection var1, String var2) throws JAXRException {
      RegistryServiceImpl var3 = (RegistryServiceImpl)this.getRegistryService();
      return var3.getRegistryProxy().findClassificationSchemeByName(var1, var2);
   }

   public BulkResponse findConcepts(Collection var1, Collection var2, Collection var3, Collection var4, Collection var5) throws JAXRException {
      RegistryServiceImpl var6 = (RegistryServiceImpl)this.getRegistryService();
      FindConceptsCommand var7 = new FindConceptsCommand(var6, var1, var2, var3, var4, var5);
      BulkResponse var8 = CommandHandler.runBulkQueryCommand(var7);
      return var8;
   }

   public Concept findConceptByPath(String var1) throws JAXRException {
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      return var2.getRegistryProxy().findConceptByPath(var1);
   }

   public BulkResponse findRegistryPackages(Collection var1, Collection var2, Collection var3, Collection var4) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }
}
