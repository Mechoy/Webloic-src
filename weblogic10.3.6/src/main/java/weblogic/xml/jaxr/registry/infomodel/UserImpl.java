package weblogic.xml.jaxr.registry.infomodel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.EmailAddress;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.PersonName;
import javax.xml.registry.infomodel.PostalAddress;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.TelephoneNumber;
import javax.xml.registry.infomodel.User;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class UserImpl extends RegistryObjectImpl implements User {
   private static final long serialVersionUID = -1L;
   private Organization m_organization;
   private PersonName m_personName;
   private String m_type;
   private ArrayList m_emailAddresses = new ArrayList();
   private ArrayList m_postalAddresses = new ArrayList();
   private ArrayList m_telephoneNumbers = new ArrayList();

   public UserImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
   }

   public UserImpl(User var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      if (var1 != null) {
         this.m_organization = new OrganizationImpl(var1.getOrganization(), var2);
         this.m_personName = new PersonNameImpl(var1.getPersonName(), var2);
         this.m_type = var1.getType();
         this.duplcateEmailAddresses(var1.getEmailAddresses());
         this.duplcatePostalAddresses(var1.getPostalAddresses());
         this.duplcateTelephoneNumbers(var1.getTelephoneNumbers((String)null));
      }

   }

   public Organization getOrganization() throws JAXRException {
      return this.m_organization;
   }

   public void setOrganization(Organization var1) throws JAXRException {
      this.m_organization = var1;
   }

   public PersonName getPersonName() throws JAXRException {
      return this.m_personName;
   }

   public void setPersonName(PersonName var1) throws JAXRException {
      this.m_personName = var1;
   }

   public Collection getPostalAddresses() throws JAXRException {
      return this.m_postalAddresses;
   }

   public void setPostalAddresses(Collection var1) throws JAXRException {
      this.m_postalAddresses.clear();
      if (null != var1) {
         this.m_postalAddresses.addAll(var1);
      }

   }

   public URL getUrl() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void setUrl(URL var1) throws JAXRException {
      this.checkCapability(1);
   }

   public Collection getTelephoneNumbers(String var1) throws JAXRException {
      if (var1 == null) {
         return this.m_telephoneNumbers;
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.m_telephoneNumbers.iterator();

         while(var3.hasNext()) {
            TelephoneNumber var4 = (TelephoneNumber)var3.next();
            if (var4.getType().equals(var1)) {
               var2.add(var4);
            }
         }

         return var2;
      }
   }

   public void setTelephoneNumbers(Collection var1) throws JAXRException {
      this.m_telephoneNumbers.clear();
      if (var1 != null) {
         this.m_telephoneNumbers.addAll(var1);
      }

   }

   public Collection getEmailAddresses() throws JAXRException {
      return this.m_emailAddresses;
   }

   public void setEmailAddresses(Collection var1) throws JAXRException {
      this.m_emailAddresses.clear();
      if (var1 != null) {
         this.m_emailAddresses.addAll(var1);
      }

   }

   public String getType() throws JAXRException {
      return this.m_type;
   }

   public void setType(String var1) throws JAXRException {
      this.m_type = var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.getName(this.m_organization), this.m_personName, this.m_type, this.m_emailAddresses, this.m_postalAddresses, this.m_telephoneNumbers};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_organization.Name", "m_personName", "m_type", "m_emailAddresses", "m_postalAddresses", "m_telephoneNumbers"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private void duplcateEmailAddresses(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            EmailAddress var3 = (EmailAddress)var2.next();
            EmailAddressImpl var4 = new EmailAddressImpl(var3, this.getRegistryService());
            this.m_emailAddresses.add(var4);
         }
      }

   }

   private void duplcatePostalAddresses(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            PostalAddress var3 = (PostalAddress)var2.next();
            PostalAddressImpl var4 = new PostalAddressImpl(var3, this.getRegistryService());
            this.m_postalAddresses.add(var4);
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
}
