package weblogic.xml.jaxr.registry.infomodel;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.ExternalIdentifier;
import javax.xml.registry.infomodel.RegistryObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class ExternalIdentifierImpl extends RegistryObjectImpl implements ExternalIdentifier {
   private static final long serialVersionUID = -1L;
   private String m_value;
   private RegistryObject m_registryObject;
   private ClassificationSchemeImpl m_identificationScheme;

   public ExternalIdentifierImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
   }

   public ExternalIdentifierImpl(ExternalIdentifier var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      if (var1 != null) {
         this.m_value = var1.getValue();
         this.duplicateParentKey(var1, var2);
         this.m_identificationScheme = new ClassificationSchemeImpl(var1.getIdentificationScheme(), var2);
      }

   }

   public void createAndSetKey(RegistryServiceImpl var1) throws JAXRException {
      String var2 = null;
      String var3 = null;
      if (this.m_registryObject != null) {
         var2 = this.m_registryObject.getKey().getId();
      }

      if (this.m_identificationScheme != null) {
         var3 = this.m_identificationScheme.getKey().getId();
      }

      StringBuffer var4 = new StringBuffer(400);
      var4.append(var2);
      var4.append(":");
      var4.append(var3);
      var4.append(":");
      var4.append(this.m_value);
      KeyImpl var5 = new KeyImpl(var4.toString(), var1);
      this.setKey(var5);
   }

   public RegistryObject getRegistryObject() throws JAXRException {
      return this.m_registryObject;
   }

   public void setRegistryObject(RegistryObject var1) throws JAXRException {
      this.m_registryObject = var1;
   }

   public String getValue() throws JAXRException {
      return this.m_value;
   }

   public void setValue(String var1) throws JAXRException {
      this.m_value = var1;
   }

   public ClassificationScheme getIdentificationScheme() throws JAXRException {
      return this.m_identificationScheme;
   }

   public void setIdentificationScheme(ClassificationScheme var1) throws JAXRException {
      this.m_identificationScheme = (ClassificationSchemeImpl)var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_value, this.getName(this.m_registryObject), this.m_identificationScheme};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_value", "m_registryObject", "m_identificationScheme"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private void duplicateParentKey(ExternalIdentifier var1, RegistryServiceImpl var2) throws JAXRException {
      RegistryObject var3 = var1.getRegistryObject();
      if (var3 != null) {
         this.m_registryObject = new RegistryObjectImpl(var2);
         if (var3.getKey() != null) {
            KeyImpl var4 = new KeyImpl(var3.getKey(), var2);
            this.m_registryObject.setKey(var4);
         }
      }

   }
}
