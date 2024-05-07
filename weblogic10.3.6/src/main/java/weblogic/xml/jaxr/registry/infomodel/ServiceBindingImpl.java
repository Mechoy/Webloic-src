package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.SpecificationLink;
import javax.xml.registry.infomodel.URIValidator;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public class ServiceBindingImpl extends RegistryObjectImpl implements ServiceBinding {
   private static final long serialVersionUID = -1L;
   private URIValidator m_validator;
   private String m_accessURI;
   private ServiceBinding m_targetBinding;
   private Service m_parentService;
   private ArrayList m_specificationLinks = new ArrayList();

   public ServiceBindingImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
      this.m_validator = new URIValidatorImpl(var1);
      this.m_parentService = new ServiceImpl(var1);
   }

   public ServiceBindingImpl(ServiceBinding var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      if (var1 != null) {
         this.m_validator = new URIValidatorImpl(var2);
         this.setValidateURI(var1.getValidateURI());
         this.m_accessURI = var1.getAccessURI();
         this.m_targetBinding = new ServiceBindingImpl(var1.getTargetBinding(), var2);
         this.duplicateAncestorKeys(var1, var2);
         this.duplicateSpecificationLinks(var1.getSpecificationLinks());
      }

   }

   public void setValidateURI(boolean var1) throws JAXRException {
      this.m_validator.setValidateURI(var1);
   }

   public boolean getValidateURI() throws JAXRException {
      return this.m_validator.getValidateURI();
   }

   public String getAccessURI() throws JAXRException {
      return this.m_accessURI;
   }

   public void setAccessURI(String var1) throws JAXRException {
      if (this.m_targetBinding != null) {
         String var2 = JAXRMessages.getMessage("jaxr.infomodel.ServiceBindingImpl.InvalidRequest", new Object[]{"targetBinding"});
         throw new InvalidRequestException(var2);
      } else {
         this.m_accessURI = var1;
      }
   }

   public ServiceBinding getTargetBinding() throws JAXRException {
      return this.m_targetBinding;
   }

   public void setTargetBinding(ServiceBinding var1) throws JAXRException {
      if (this.m_accessURI != null) {
         String var2 = JAXRMessages.getMessage("jaxr.infomodel.ServiceBindingImpl.InvalidRequest", new Object[]{"accessURI"});
         throw new InvalidRequestException(var2);
      } else {
         this.m_targetBinding = var1;
      }
   }

   public Service getService() throws JAXRException {
      return this.m_parentService;
   }

   void setService(Service var1) throws JAXRException {
      this.m_parentService = var1;
   }

   public void addSpecificationLink(SpecificationLink var1) throws JAXRException {
      if (var1 != null) {
         ((SpecificationLinkImpl)var1).setServiceBinding(this);
         this.m_specificationLinks.add(var1);
      }

   }

   public void addSpecificationLinks(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, SpecificationLink.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         SpecificationLink var3 = (SpecificationLink)var2.next();
         this.addSpecificationLink(var3);
      }

   }

   public void removeSpecificationLink(SpecificationLink var1) throws JAXRException {
      if (var1 != null) {
         this.m_specificationLinks.remove(var1);
      }

   }

   public void removeSpecificationLinks(Collection var1) throws JAXRException {
      if (var1 != null) {
         this.m_specificationLinks.removeAll(var1);
      }

   }

   public Collection getSpecificationLinks() throws JAXRException {
      return this.m_specificationLinks;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_validator, this.m_accessURI, this.m_targetBinding, this.getName(this.m_parentService), this.m_specificationLinks};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_validator", "m_accessURI", "m_targetBinding", "m_parentService.Name", "m_specificationLinks"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private void duplicateSpecificationLinks(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            SpecificationLink var3 = (SpecificationLink)var2.next();
            SpecificationLinkImpl var4 = new SpecificationLinkImpl(var3, this.getRegistryService());
            this.m_specificationLinks.add(var4);
         }
      }

   }

   private void duplicateAncestorKeys(ServiceBinding var1, RegistryServiceImpl var2) throws JAXRException {
      Service var3 = var1.getService();
      if (var3 != null) {
         this.m_parentService = new ServiceImpl(var2);
         if (var3.getKey() != null) {
            KeyImpl var4 = new KeyImpl(var3.getKey(), var2);
            this.m_parentService.setKey(var4);
            Organization var5 = var3.getProvidingOrganization();
            if (var5 != null) {
               OrganizationImpl var6 = new OrganizationImpl(var2);
               if (var5.getKey() != null) {
                  var4 = new KeyImpl(var5.getKey(), var2);
                  var6.setKey(var4);
               }

               this.m_parentService.setProvidingOrganization(var6);
            }
         }
      }

   }
}
