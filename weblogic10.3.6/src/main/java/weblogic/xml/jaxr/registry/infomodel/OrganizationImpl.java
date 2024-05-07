package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.PostalAddress;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.TelephoneNumber;
import javax.xml.registry.infomodel.User;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public class OrganizationImpl extends RegistryObjectImpl implements Organization {
   private static final long serialVersionUID = -1L;
   private User m_primaryContact;
   private ArrayList m_services = new ArrayList();
   private ArrayList m_telephoneNumbers = new ArrayList();
   private ArrayList m_users = new ArrayList();

   public OrganizationImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
      var1.getRegistryProxy().setRegistryObjectOwner(this, var1.getCurrentUser());
   }

   public OrganizationImpl(Organization var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      var2.getRegistryProxy().setRegistryObjectOwner(this, var2.getCurrentUser());
      if (var1 != null) {
         if (var1.getPrimaryContact() != null) {
            this.m_primaryContact = new UserImpl(var1.getPrimaryContact(), var2);
         }

         this.duplicateServices(var1.getServices());
         this.duplcateTelephoneNumbers(var1.getTelephoneNumbers((String)null));
         this.duplcateUsers(var1.getUsers());
      }

   }

   public OrganizationImpl(InternationalString var1, RegistryServiceImpl var2) throws JAXRException {
      super(var1, var2);
      var2.getRegistryProxy().setRegistryObjectOwner(this, var2.getCurrentUser());
   }

   public PostalAddress getPostalAddress() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void setPostalAddress(PostalAddress var1) throws JAXRException {
      this.checkCapability(1);
   }

   public User getPrimaryContact() throws JAXRException {
      return null == this.m_primaryContact ? this.getRegistryService().getBusinessLifeCycleManager().createUser() : this.m_primaryContact;
   }

   public void setPrimaryContact(User var1) throws JAXRException {
      this.m_primaryContact = var1;
      if (!this.m_users.contains(var1)) {
         this.addUser(this.m_primaryContact);
      }

   }

   public void addUser(User var1) throws JAXRException {
      if (var1 != null) {
         this.m_users.add(var1);
         ((UserImpl)var1).setOrganization(this);
      }

   }

   public void addUsers(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            UserImpl var3 = (UserImpl)var2.next();
            this.addUser(var3);
         }
      }

   }

   public void removeUser(User var1) throws JAXRException {
      if (var1 != null) {
         this.m_users.remove(var1);
      }

   }

   public void removeUsers(Collection var1) throws JAXRException {
      if (var1 != null) {
         this.m_users.removeAll(var1);
      }

   }

   public Collection getUsers() throws JAXRException {
      return this.m_users;
   }

   public Collection getTelephoneNumbers(String var1) throws JAXRException {
      if (null == var1) {
         return this.m_telephoneNumbers;
      } else {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < this.m_telephoneNumbers.size(); ++var3) {
            TelephoneNumber var4 = (TelephoneNumber)this.m_telephoneNumbers.get(var3);
            if (var1.equals(var4.getType())) {
               var2.add(var4);
            }
         }

         return var2;
      }
   }

   public void setTelephoneNumbers(Collection var1) throws JAXRException {
      if (var1 != null) {
         this.m_telephoneNumbers.addAll(var1);
      }

   }

   public void addService(Service var1) throws JAXRException {
      if (null != var1) {
         if (var1.getProvidingOrganization() == null) {
            ((ServiceImpl)var1).setProvidingOrganization(this);
         }

         this.m_services.add(var1);
      }

   }

   public void addServices(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Service.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Service var3 = (Service)var2.next();
         this.addService(var3);
      }

   }

   public void removeService(Service var1) throws JAXRException {
      this.m_services.remove(var1);
   }

   public void removeServices(Collection var1) throws JAXRException {
      this.m_services.removeAll(var1);
   }

   public Collection getServices() throws JAXRException {
      return this.m_services;
   }

   public void addChildOrganization(Organization var1) throws JAXRException {
      this.checkCapability(1);
   }

   public void addChildOrganizations(Collection var1) throws JAXRException {
      this.checkCapability(1);
   }

   public void removeChildOrganization(Organization var1) throws JAXRException {
      this.checkCapability(1);
   }

   public void removeChildOrganizations(Collection var1) throws JAXRException {
      this.checkCapability(1);
   }

   public int getChildOrganizationCount() throws JAXRException {
      this.checkCapability(1);
      return 0;
   }

   public Collection getChildOrganizations() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public Collection getDescendantOrganizations() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public Organization getParentOrganization() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public Organization getRootOrganization() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_primaryContact, this.m_services, this.m_telephoneNumbers, this.m_users};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_primaryContact", "m_services", "m_telephoneNumbers", "m_users"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private void duplicateServices(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Service var3 = (Service)var2.next();
            ServiceImpl var4 = new ServiceImpl(var3, this.getRegistryService());
            this.m_services.add(var4);
         }
      }

   }

   private void duplcateTelephoneNumbers(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            TelephoneNumber var3 = (TelephoneNumber)var2.next();
            TelephoneNumberImpl var4 = new TelephoneNumberImpl(var3, this.getRegistryService());
            this.m_telephoneNumbers.add(var4);
         }
      }

   }

   private void duplcateUsers(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            User var3 = (User)var2.next();
            UserImpl var4 = new UserImpl(var3, this.getRegistryService());
            this.m_telephoneNumbers.add(var4);
         }
      }

   }
}
