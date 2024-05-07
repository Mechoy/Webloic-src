package weblogic.xml.jaxr.registry.infomodel;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.PostalAddress;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class PostalAddressImpl extends ExtensibleObjectImpl implements PostalAddress {
   private static final long serialVersionUID = -1L;
   private ClassificationScheme m_postalScheme;
   private String m_city;
   private String m_country;
   private String m_postalCode;
   private String m_stateOrProvince;
   private String m_street;
   private String m_streetNumber;
   private String m_addressType;

   public PostalAddressImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public PostalAddressImpl(PostalAddress var1, RegistryServiceImpl var2) throws JAXRException {
      super(var1, var2);
      if (var1 != null) {
         this.m_postalScheme = new ClassificationSchemeImpl(var1.getPostalScheme(), var2);
         this.m_city = var1.getCity();
         this.m_country = var1.getCountry();
         this.m_postalCode = var1.getPostalCode();
         this.m_stateOrProvince = var1.getStateOrProvince();
         this.m_street = var1.getStreet();
         this.m_streetNumber = var1.getStreetNumber();
         this.m_addressType = var1.getType();
      }

   }

   public String getStreet() throws JAXRException {
      return this.m_street == null ? "" : this.m_street;
   }

   public void setStreet(String var1) throws JAXRException {
      this.m_street = var1;
   }

   public String getStreetNumber() throws JAXRException {
      return this.m_streetNumber == null ? "" : this.m_streetNumber;
   }

   public void setStreetNumber(String var1) throws JAXRException {
      this.m_streetNumber = var1;
   }

   public String getCity() throws JAXRException {
      return this.m_city == null ? "" : this.m_city;
   }

   public void setCity(String var1) throws JAXRException {
      this.m_city = var1;
   }

   public String getStateOrProvince() throws JAXRException {
      return this.m_stateOrProvince == null ? "" : this.m_stateOrProvince;
   }

   public void setStateOrProvince(String var1) throws JAXRException {
      this.m_stateOrProvince = var1;
   }

   public String getPostalCode() throws JAXRException {
      return this.m_postalCode == null ? "" : this.m_postalCode;
   }

   public void setPostalCode(String var1) throws JAXRException {
      this.m_postalCode = var1;
   }

   public String getCountry() throws JAXRException {
      return this.m_country == null ? "" : this.m_country;
   }

   public void setCountry(String var1) throws JAXRException {
      this.m_country = var1;
   }

   public String getType() throws JAXRException {
      return this.m_addressType;
   }

   public void setType(String var1) throws JAXRException {
      this.m_addressType = var1;
   }

   public void setPostalScheme(ClassificationScheme var1) throws JAXRException {
      this.m_postalScheme = var1;
   }

   public ClassificationScheme getPostalScheme() throws JAXRException {
      return this.m_postalScheme;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_postalScheme, this.m_city, this.m_country, this.m_postalCode, this.m_stateOrProvince, this.m_street, this.m_streetNumber, this.m_addressType};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_postalScheme", "m_city", "m_country", "m_postalCode", "m_stateOrProvince", "m_street", "m_streetNumber", "m_addressType"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
