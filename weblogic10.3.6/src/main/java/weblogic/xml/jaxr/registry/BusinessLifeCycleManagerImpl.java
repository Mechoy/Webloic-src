package weblogic.xml.jaxr.registry;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Association;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import weblogic.xml.jaxr.registry.command.CommandHandler;
import weblogic.xml.jaxr.registry.command.DeleteAssociationsCommand;
import weblogic.xml.jaxr.registry.command.DeleteClassificationSchemesCommand;
import weblogic.xml.jaxr.registry.command.DeleteConceptsCommand;
import weblogic.xml.jaxr.registry.command.DeleteOrganizationsCommand;
import weblogic.xml.jaxr.registry.command.DeleteServiceBindingsCommand;
import weblogic.xml.jaxr.registry.command.DeleteServicesCommand;
import weblogic.xml.jaxr.registry.command.SaveAssociationsCommand;
import weblogic.xml.jaxr.registry.command.SaveClassificationSchemesCommand;
import weblogic.xml.jaxr.registry.command.SaveConceptsCommand;
import weblogic.xml.jaxr.registry.command.SaveOrganizationsCommand;
import weblogic.xml.jaxr.registry.command.SaveServiceBindingsCommand;
import weblogic.xml.jaxr.registry.command.SaveServicesCommand;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public class BusinessLifeCycleManagerImpl extends LifeCycleManagerImpl implements BusinessLifeCycleManager {
   public BusinessLifeCycleManagerImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public BulkResponse saveOrganizations(Collection var1) throws JAXRException {
      this.getLogger().trace("+BusinessLifeCycleManager.saveOrganizations()");
      this.getLogger().debug("Save Organizations called with organizations [" + var1 + "]");
      JAXRUtil.verifyCollectionContent(var1, Organization.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      SaveOrganizationsCommand var3 = new SaveOrganizationsCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      this.getLogger().debug("Save Organizations will return bulkResponse [" + var4 + "]");
      this.getLogger().trace("-BusinessLifeCycleManager.saveOrganizations()");
      return var4;
   }

   public BulkResponse saveServices(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Service.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      SaveServicesCommand var3 = new SaveServicesCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      return var4;
   }

   public BulkResponse saveServiceBindings(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, ServiceBinding.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      SaveServiceBindingsCommand var3 = new SaveServiceBindingsCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      return var4;
   }

   public BulkResponse saveConcepts(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Concept.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      SaveConceptsCommand var3 = new SaveConceptsCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      return var4;
   }

   public BulkResponse saveClassificationSchemes(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, ClassificationScheme.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      SaveClassificationSchemesCommand var3 = new SaveClassificationSchemesCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      return var4;
   }

   public BulkResponse saveAssociations(Collection var1, boolean var2) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Association.class);
      RegistryServiceImpl var3 = (RegistryServiceImpl)this.getRegistryService();
      SaveAssociationsCommand var4 = new SaveAssociationsCommand(var3, var1, var2);
      BulkResponse var5 = CommandHandler.runBulkQueryCommand(var4);
      return var5;
   }

   public BulkResponse deleteOrganizations(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Key.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      DeleteOrganizationsCommand var3 = new DeleteOrganizationsCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      return var4;
   }

   public BulkResponse deleteServices(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Key.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      DeleteServicesCommand var3 = new DeleteServicesCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      return var4;
   }

   public BulkResponse deleteServiceBindings(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Key.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      DeleteServiceBindingsCommand var3 = new DeleteServiceBindingsCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      return var4;
   }

   public BulkResponse deleteConcepts(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Key.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      DeleteConceptsCommand var3 = new DeleteConceptsCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      return var4;
   }

   public BulkResponse deleteClassificationSchemes(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Key.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      DeleteClassificationSchemesCommand var3 = new DeleteClassificationSchemesCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      return var4;
   }

   public BulkResponse deleteAssociations(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Key.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      DeleteAssociationsCommand var3 = new DeleteAssociationsCommand(var2, var1);
      BulkResponse var4 = CommandHandler.runBulkQueryCommand(var3);
      return var4;
   }

   public void confirmAssociation(Association var1) throws JAXRException, InvalidRequestException {
      JAXRUtil.verifyNotNull(var1, Association.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      var2.getRegistryProxy().confirmAssociation(var1);
   }

   public void unConfirmAssociation(Association var1) throws JAXRException, InvalidRequestException {
      JAXRUtil.verifyNotNull(var1, Association.class);
      RegistryServiceImpl var2 = (RegistryServiceImpl)this.getRegistryService();
      var2.getRegistryProxy().unConfirmAssociation(var1);
   }
}
