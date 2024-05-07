package weblogic.xml.jaxr.registry.infomodel;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.PersonName;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class PersonNameImpl extends BaseInfoModelObject implements PersonName {
   private static final long serialVersionUID = -1L;
   private String m_fullName;

   public PersonNameImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public PersonNameImpl(PersonName var1, RegistryServiceImpl var2) throws JAXRException {
      super(var2);
      if (var1 != null) {
         this.m_fullName = var1.getFullName();
      }

   }

   public String getLastName() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void setLastName(String var1) throws JAXRException {
      this.checkCapability(1);
   }

   public String getFirstName() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void setFirstName(String var1) throws JAXRException {
      this.checkCapability(1);
   }

   public String getMiddleName() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void setMiddleName(String var1) throws JAXRException {
      this.checkCapability(1);
   }

   public String getFullName() throws JAXRException {
      return this.m_fullName == null ? "" : this.m_fullName;
   }

   public void setFullName(String var1) throws JAXRException {
      this.m_fullName = var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_fullName};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_fullName"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
