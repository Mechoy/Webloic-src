package weblogic.xml.jaxr.registry.infomodel;

import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Association;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.RegistryObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;

public class AssociationImpl extends RegistryObjectImpl implements Association {
   private RegistryObject m_sourceObject;
   private RegistryObject m_targetObject;
   private ConceptImpl m_associationType;
   private boolean m_confirmedBySourceOwner;
   private boolean m_confirmedByTargetOwner;
   private static final long serialVersionUID = -1L;

   public AssociationImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
   }

   public AssociationImpl(Association var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      if (var1 != null) {
         this.m_sourceObject = new RegistryObjectImpl(var1.getSourceObject(), var2);
         this.m_targetObject = new RegistryObjectImpl(var1.getTargetObject(), var2);
         this.m_associationType = new ConceptImpl(var1.getAssociationType(), var2);
         this.m_confirmedBySourceOwner = var1.isConfirmedBySourceOwner();
         this.m_confirmedByTargetOwner = var1.isConfirmedByTargetOwner();
      }

   }

   public RegistryObject getSourceObject() throws JAXRException {
      return this.m_sourceObject;
   }

   public void setSourceObject(RegistryObject var1) throws JAXRException {
      this.m_sourceObject = var1;
      this.createAndSetKey();
   }

   public RegistryObject getTargetObject() throws JAXRException {
      return this.m_targetObject;
   }

   public void setTargetObject(RegistryObject var1) throws JAXRException {
      this.m_targetObject = var1;
      this.createAndSetKey();
   }

   public Concept getAssociationType() throws JAXRException {
      return this.m_associationType;
   }

   public void setAssociationType(Concept var1) throws JAXRException {
      this.m_associationType = (ConceptImpl)var1;
      this.createAndSetKey();
   }

   public boolean isExtramural() throws JAXRException {
      String var2;
      if (this.m_sourceObject != null && this.m_targetObject != null) {
         var2 = this.getRegistryObjectOwner(this.m_sourceObject);
         String var3 = this.getRegistryObjectOwner(this.m_targetObject);
         boolean var1;
         if (var2 == null && var3 == null) {
            var1 = false;
         } else if (var2 != null) {
            if (var2.equals(var3)) {
               var1 = false;
            } else {
               var1 = true;
            }
         } else {
            var1 = true;
         }

         return var1;
      } else {
         var2 = JAXRMessages.getMessage("jaxr.registry.registryServiceImpl.invalidSourceORTarget");
         throw new InvalidRequestException(var2);
      }
   }

   public boolean isIntramural() throws JAXRException {
      String var2;
      if (this.m_sourceObject != null && this.m_targetObject != null) {
         var2 = this.getRegistryObjectOwner(this.m_sourceObject);
         String var3 = this.getRegistryObjectOwner(this.m_targetObject);
         String var4 = this.getRegistryObjectOwner(this);
         boolean var1;
         if (var4 == null) {
            if (var2 == null && var3 == null) {
               var1 = true;
            } else {
               var1 = false;
            }
         } else if (var2 != null) {
            if (var2.equals(var3) && var2.equals(var4)) {
               var1 = true;
            } else {
               var1 = false;
            }
         } else {
            var1 = false;
         }

         return var1;
      } else {
         var2 = JAXRMessages.getMessage("jaxr.registry.registryServiceImpl.invalidSourceORTarget");
         throw new InvalidRequestException(var2);
      }
   }

   public boolean isConfirmedBySourceOwner() throws JAXRException {
      if (this.m_sourceObject != null && this.m_targetObject != null) {
         if (this.haveSameOwner(this.m_sourceObject, this.m_targetObject)) {
            this.m_confirmedBySourceOwner = true;
         }

         return this.m_confirmedBySourceOwner;
      } else {
         String var1 = JAXRMessages.getMessage("jaxr.registry.registryServiceImpl.invalidSourceORTarget");
         throw new InvalidRequestException(var1);
      }
   }

   public void setConfirmedBySourceOwner(boolean var1) throws JAXRException {
      this.m_confirmedBySourceOwner = var1;
   }

   public boolean isConfirmedByTargetOwner() throws JAXRException {
      if (this.m_sourceObject != null && this.m_targetObject != null) {
         if (this.haveSameOwner(this.m_sourceObject, this.m_targetObject)) {
            this.m_confirmedByTargetOwner = true;
         }

         return this.m_confirmedByTargetOwner;
      } else {
         String var1 = JAXRMessages.getMessage("jaxr.registry.registryServiceImpl.invalidSourceORTarget");
         throw new InvalidRequestException(var1);
      }
   }

   public void setConfirmedByTargetOwner(boolean var1) throws JAXRException {
      this.m_confirmedByTargetOwner = var1;
   }

   public boolean isConfirmed() throws JAXRException {
      boolean var1;
      if (this.isIntramural()) {
         var1 = true;
      } else if (this.isConfirmedBySourceOwner() && this.isConfirmedByTargetOwner()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_sourceObject, this.m_targetObject, this.m_associationType, new Boolean(this.m_confirmedBySourceOwner), new Boolean(this.m_confirmedByTargetOwner)};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_sourceObject", "m_targetObject", "m_associationType", "m_confirmedBySourceOwner", "m_confirmedByTargetOwner"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private String getRegistryObjectOwner(RegistryObject var1) throws JAXRException {
      return this.getRegistryService().getRegistryProxy().getRegistryObjectOwner(var1);
   }

   private boolean haveSameOwner(RegistryObject var1, RegistryObject var2) throws JAXRException {
      boolean var3 = false;
      String var4 = this.getRegistryObjectOwner(var1);
      if (var4 == null) {
         var4 = this.getRegistryService().getCurrentUser();
      }

      String var5 = this.getRegistryObjectOwner(var2);
      if (var5 == null) {
         var5 = this.getRegistryService().getCurrentUser();
      }

      if (var4 == null) {
         if (var5 == null) {
            var3 = true;
         }
      } else if (var4.equals(var5)) {
         var3 = true;
      }

      return var3;
   }

   private void createAndSetKey() throws JAXRException {
      String var1 = null;
      String var2 = null;
      String var3 = null;
      if (this.m_sourceObject != null && this.m_sourceObject.getKey() != null) {
         var1 = this.m_sourceObject.getKey().getId();
      }

      if (this.m_targetObject != null && this.m_targetObject.getKey() != null) {
         var2 = this.m_targetObject.getKey().getId();
      }

      if (this.m_associationType != null) {
         var3 = this.m_associationType.getValue();
      }

      StringBuffer var4 = new StringBuffer(400);
      var4.append(var1);
      var4.append(":");
      var4.append(var2);
      var4.append(":");
      var4.append(var3);
      KeyImpl var5 = new KeyImpl(var4.toString(), this.getRegistryService());
      this.setKey(var5);
   }
}
