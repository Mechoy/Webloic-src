package weblogic.xml.jaxr.registry.infomodel;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.TelephoneNumber;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class TelephoneNumberImpl extends BaseInfoModelObject implements TelephoneNumber {
   private static final long serialVersionUID = -1L;
   private String m_number;
   private String m_type;

   public TelephoneNumberImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public TelephoneNumberImpl(TelephoneNumber var1, RegistryServiceImpl var2) throws JAXRException {
      super(var2);
      if (var1 != null) {
         this.m_number = var1.getNumber();
         this.m_type = var1.getType();
      }

   }

   public String getCountryCode() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public String getAreaCode() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public String getNumber() throws JAXRException {
      return this.m_number == null ? "" : this.m_number;
   }

   public String getExtension() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public String getUrl() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public String getType() throws JAXRException {
      return this.m_type;
   }

   public void setCountryCode(String var1) throws JAXRException {
      this.checkCapability(1);
   }

   public void setAreaCode(String var1) throws JAXRException {
      this.checkCapability(1);
   }

   public void setNumber(String var1) throws JAXRException {
      this.m_number = var1;
   }

   public void setExtension(String var1) throws JAXRException {
      this.checkCapability(1);
   }

   public void setUrl(String var1) throws JAXRException {
      this.checkCapability(1);
   }

   public void setType(String var1) throws JAXRException {
      this.m_type = var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_number, this.m_type};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_number", "m_type"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
