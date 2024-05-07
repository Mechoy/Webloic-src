package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.SpecificationLink;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;

public class SpecificationLinkImpl extends RegistryObjectImpl implements SpecificationLink {
   private static final long serialVersionUID = -1L;
   private Concept m_registryObject;
   private InternationalString m_usageDescription;
   private ArrayList m_usageParameters = new ArrayList();
   private ServiceBinding m_serviceBinding;

   public SpecificationLinkImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
   }

   public SpecificationLinkImpl(SpecificationLink var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      if (var1 != null) {
         this.m_registryObject = new ConceptImpl((Concept)var1.getSpecificationObject(), var2);
         this.m_usageDescription = new InternationalStringImpl(var1.getUsageDescription(), var2);
         this.m_usageParameters = new ArrayList(var1.getUsageParameters());
         this.duplicateAncestorKeys(var1, var2);
      }

   }

   public RegistryObject getSpecificationObject() throws JAXRException {
      return this.m_registryObject;
   }

   public void setSpecificationObject(RegistryObject var1) throws JAXRException {
      if (var1 instanceof Concept) {
         this.m_registryObject = (Concept)var1;
      } else {
         String var2 = JAXRMessages.getMessage("jaxr.infomodle.SpecificationLinkImpl.invalidSpecificationObject");
         throw new JAXRException(var2);
      }
   }

   public InternationalString getUsageDescription() throws JAXRException {
      return this.m_usageDescription == null ? this.getEmptyInternationalString() : this.m_usageDescription;
   }

   public void setUsageDescription(InternationalString var1) throws JAXRException {
      this.m_usageDescription = var1;
   }

   public Collection getUsageParameters() throws JAXRException {
      return this.m_usageParameters;
   }

   public void setUsageParameters(Collection var1) throws JAXRException {
      this.m_usageParameters = new ArrayList();
      if (null != var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            if (!(var2.next() instanceof String)) {
               String var3 = JAXRMessages.getMessage("jaxr.infomodle.SpecificationLinkImpl.invalidUsageParameters");
               throw new JAXRException(var3);
            }
         }

         this.m_usageParameters = new ArrayList(var1);
      }

   }

   public ServiceBinding getServiceBinding() throws JAXRException {
      return this.m_serviceBinding;
   }

   public void setServiceBinding(ServiceBinding var1) throws JAXRException {
      this.m_serviceBinding = var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_registryObject, this.m_usageDescription, this.m_usageParameters, this.getName(this.m_serviceBinding)};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_registryObject", "m_usageDescription", "m_usageParameters", "m_serviceBinding"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private void duplicateAncestorKeys(SpecificationLink var1, RegistryServiceImpl var2) throws JAXRException {
      ServiceBinding var3 = var1.getServiceBinding();
      if (var3 != null) {
         this.m_serviceBinding = new ServiceBindingImpl(var2);
         if (var3.getKey() != null) {
            KeyImpl var4 = new KeyImpl(var3.getKey(), var2);
            this.m_serviceBinding.setKey(var4);
            Service var5 = var3.getService();
            if (var5 != null) {
               ServiceImpl var6 = new ServiceImpl(var2);
               if (var5.getKey() != null) {
                  var4 = new KeyImpl(var5.getKey(), var2);
                  var6.setKey(var4);
                  Organization var7 = var5.getProvidingOrganization();
                  if (var7 != null) {
                     OrganizationImpl var8 = new OrganizationImpl(var2);
                     if (var7.getKey() != null) {
                        var4 = new KeyImpl(var7.getKey(), var2);
                        var8.setKey(var4);
                     }

                     var5.setProvidingOrganization(var8);
                  }
               }

               ((ServiceBindingImpl)this.m_serviceBinding).setService(var6);
            }
         }
      }

   }
}
