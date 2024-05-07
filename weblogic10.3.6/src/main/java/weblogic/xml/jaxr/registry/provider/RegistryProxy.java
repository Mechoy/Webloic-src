package weblogic.xml.jaxr.registry.provider;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Association;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.RegistryObject;

public interface RegistryProxy {
   void setCredentials(String var1, String var2) throws JAXRException;

   void closeConnection() throws JAXRException;

   InternalClassificationSchemes getInternalClassificationSchemes() throws JAXRException;

   String getRegistryObjectOwner(RegistryObject var1) throws JAXRException;

   void setRegistryObjectOwner(RegistryObject var1, String var2) throws JAXRException;

   String makeRegistrySpecificRequest(String var1) throws JAXRException;

   BulkResponse saveOrganizations(Collection var1) throws JAXRException;

   BulkResponse saveServices(Collection var1) throws JAXRException;

   BulkResponse saveServiceBindings(Collection var1) throws JAXRException;

   BulkResponse saveConcepts(Collection var1) throws JAXRException;

   BulkResponse saveClassificationSchemes(Collection var1) throws JAXRException;

   BulkResponse saveAssociations(Collection var1, boolean var2) throws JAXRException;

   BulkResponse deleteOrganizations(Collection var1) throws JAXRException;

   BulkResponse deleteServices(Collection var1) throws JAXRException;

   BulkResponse deleteServiceBindings(Collection var1) throws JAXRException;

   BulkResponse deleteConcepts(Collection var1) throws JAXRException;

   BulkResponse deleteClassificationSchemes(Collection var1) throws JAXRException;

   BulkResponse deleteAssociations(Collection var1) throws JAXRException;

   void confirmAssociation(Association var1) throws JAXRException, InvalidRequestException;

   void unConfirmAssociation(Association var1) throws JAXRException, InvalidRequestException;

   BulkResponse deprecateObjects(Collection var1) throws JAXRException;

   BulkResponse unDeprecateObjects(Collection var1) throws JAXRException;

   BulkResponse deleteObjects(Collection var1) throws JAXRException;

   BulkResponse findAssociations(Collection var1, String var2, String var3, Collection var4) throws JAXRException;

   BulkResponse findCallerAssociations(Collection var1, Boolean var2, Boolean var3, Collection var4) throws JAXRException;

   BulkResponse findOrganizations(Collection var1, Collection var2, Collection var3, Collection var4, Collection var5, Collection var6) throws JAXRException;

   BulkResponse findServices(Key var1, Collection var2, Collection var3, Collection var4, Collection var5) throws JAXRException;

   BulkResponse findServiceBindings(Key var1, Collection var2, Collection var3, Collection var4) throws JAXRException;

   BulkResponse findClassificationSchemes(Collection var1, Collection var2, Collection var3, Collection var4) throws JAXRException;

   ClassificationScheme findClassificationSchemeByName(Collection var1, String var2) throws JAXRException;

   BulkResponse findConcepts(Collection var1, Collection var2, Collection var3, Collection var4, Collection var5) throws JAXRException;

   Concept findConceptByPath(String var1) throws JAXRException;

   BulkResponse findRegistryPackages(Collection var1, Collection var2, Collection var3, Collection var4) throws JAXRException;

   RegistryObject getRegistryObject(String var1, String var2) throws JAXRException;

   RegistryObject getRegistryObject(String var1) throws JAXRException;

   BulkResponse getRegistryObjects(Collection var1) throws JAXRException;

   BulkResponse getRegistryObjects(Collection var1, String var2) throws JAXRException;

   BulkResponse getRegistryObjects() throws JAXRException;

   BulkResponse getRegistryObjects(String var1) throws JAXRException;
}
