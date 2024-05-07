package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public class ServiceImpl extends RegistryEntryImpl implements Service {
   private static final long serialVersionUID = -1L;
   private ArrayList m_serviceBindings = new ArrayList();
   private Organization m_providingOrganization;

   public ServiceImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
   }

   public ServiceImpl(Service var1, RegistryServiceImpl var2) throws JAXRException {
      super(var1, var2);
      if (var1 != null) {
         this.duplicateServiceBindings(var1.getServiceBindings());
         this.m_providingOrganization = new OrganizationImpl(var1.getProvidingOrganization(), var2);
      }

   }

   public Organization getProvidingOrganization() throws JAXRException {
      return this.m_providingOrganization;
   }

   public void setProvidingOrganization(Organization var1) throws JAXRException {
      this.m_providingOrganization = var1;
   }

   public void addServiceBinding(ServiceBinding var1) throws JAXRException {
      if (var1 != null) {
         ((ServiceBindingImpl)var1).setService(this);
         this.m_serviceBindings.add(var1);
      }

   }

   public void addServiceBindings(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, ServiceBinding.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ServiceBinding var3 = (ServiceBinding)var2.next();
         this.addServiceBinding(var3);
      }

   }

   public void removeServiceBinding(ServiceBinding var1) throws JAXRException {
      if (null != var1) {
         this.m_serviceBindings.remove(var1);
      }

   }

   public void removeServiceBindings(Collection var1) throws JAXRException {
      if (null != var1) {
         this.m_serviceBindings.removeAll(var1);
      }

   }

   public Collection getServiceBindings() throws JAXRException {
      return this.m_serviceBindings;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_serviceBindings, this.getName(this.m_providingOrganization)};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_serviceBindings", "m_providingOrganization.Name"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private void duplicateServiceBindings(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ServiceBinding var3 = (ServiceBinding)var2.next();
            ServiceBindingImpl var4 = new ServiceBindingImpl(var3, this.getRegistryService());
            this.m_serviceBindings.add(var4);
         }
      }

   }
}
