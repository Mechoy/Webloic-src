package weblogic.xml.jaxr.registry.infomodel;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.EmailAddress;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class EmailAddressImpl extends BaseInfoModelObject implements EmailAddress {
   private static final long serialVersionUID = -1L;
   private String m_address;
   private String m_type;

   public EmailAddressImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public EmailAddressImpl(EmailAddress var1, RegistryServiceImpl var2) throws JAXRException {
      super(var2);
      if (var1 != null) {
         this.m_address = var1.getAddress();
         this.m_type = var1.getType();
      }

   }

   public String getAddress() throws JAXRException {
      return this.m_address;
   }

   public void setAddress(String var1) throws JAXRException {
      this.m_address = var1;
   }

   public String getType() throws JAXRException {
      return this.m_type;
   }

   public void setType(String var1) throws JAXRException {
      this.m_type = var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_address, this.m_type};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_address", "m_type"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
