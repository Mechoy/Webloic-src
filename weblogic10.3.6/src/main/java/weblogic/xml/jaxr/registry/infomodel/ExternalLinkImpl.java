package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ExternalLink;
import javax.xml.registry.infomodel.RegistryObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class ExternalLinkImpl extends RegistryObjectImpl implements ExternalLink {
   private static final long serialVersionUID = -1L;
   private URIValidatorImpl m_validator;
   private ArrayList m_registryObjects = new ArrayList();
   private String m_externalURI;

   public ExternalLinkImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
      this.m_validator = new URIValidatorImpl(var1);
   }

   public ExternalLinkImpl(ExternalLink var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      if (var1 != null) {
         this.m_validator = new URIValidatorImpl(var2);
         this.setValidateURI(var1.getValidateURI());
         this.duplicateParentKeys(var1, var2);
         this.setExternalURI(var1.getExternalURI());
      }

   }

   public void setValidateURI(boolean var1) throws JAXRException {
      this.m_validator.setValidateURI(var1);
   }

   public boolean getValidateURI() throws JAXRException {
      return this.m_validator.getValidateURI();
   }

   public Collection getLinkedObjects() throws JAXRException {
      return this.m_registryObjects;
   }

   public void addLinkedObject(RegistryObject var1) throws JAXRException {
      this.m_registryObjects.add(var1);
   }

   public String getExternalURI() throws JAXRException {
      return this.m_externalURI;
   }

   public void setExternalURI(String var1) throws JAXRException {
      this.setExternalURI(var1, false);
   }

   public void setExternalURI(String var1, boolean var2) throws JAXRException {
      if (var2) {
         this.m_validator.validate(var1);
      }

      this.m_externalURI = var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_validator, this.getNames(this.m_registryObjects), this.m_externalURI};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_validator", "m_registryObjects.Names", "m_externalURI"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private void duplicateParentKeys(ExternalLink var1, RegistryServiceImpl var2) throws JAXRException {
      Collection var3 = var1.getLinkedObjects();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         RegistryObject var5 = (RegistryObject)var4.next();
         if (var5 != null) {
            RegistryObjectImpl var6 = new RegistryObjectImpl(var2);
            if (var5.getKey() != null) {
               KeyImpl var7 = new KeyImpl(var5.getKey(), var2);
               var6.setKey(var7);
            }

            this.m_registryObjects.add(var6);
         }
      }

   }
}
