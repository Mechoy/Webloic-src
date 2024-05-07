package weblogic.xml.jaxr.registry.provider;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.infomodel.KeyImpl;

public interface InternalClassificationSchemes {
   ClassificationScheme getClassificationSchemeByKey(KeyImpl var1, RegistryServiceImpl var2) throws JAXRException;

   ClassificationScheme getClassificationSchemeByName(String var1, RegistryServiceImpl var2) throws JAXRException;

   void validateConcept(Concept var1, RegistryServiceImpl var2) throws JAXRException;
}
