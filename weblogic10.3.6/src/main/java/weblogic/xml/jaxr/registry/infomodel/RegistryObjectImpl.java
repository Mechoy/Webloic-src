package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.JAXRException;
import javax.xml.registry.LifeCycleManager;
import javax.xml.registry.infomodel.Association;
import javax.xml.registry.infomodel.Classification;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.ExternalIdentifier;
import javax.xml.registry.infomodel.ExternalLink;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.RegistryObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public class RegistryObjectImpl extends ExtensibleObjectImpl implements RegistryObject {
   private static final long serialVersionUID = -1L;
   private InternationalString m_name;
   private InternationalString m_description;
   private Key m_key;
   private OrganizationImpl m_submittingOrganization;
   private ArrayList m_classifications = new ArrayList();
   private ArrayList m_associations = new ArrayList();
   private ArrayList m_externalIdentifiers = new ArrayList();
   private ArrayList m_externalLinks = new ArrayList();

   public RegistryObjectImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
   }

   public RegistryObjectImpl(RegistryObject var1, RegistryServiceImpl var2) throws JAXRException {
      super(var1, var2);
      if (var1 != null) {
         this.m_name = new InternationalStringImpl(var1.getName(), var2);
         this.m_description = new InternationalStringImpl(var1.getDescription(), var2);
         if (var1.getKey() == null) {
            this.m_key = new KeyImpl(var2);
         } else {
            this.m_key = new KeyImpl(var1.getKey(), var2);
         }

         this.m_submittingOrganization = new OrganizationImpl(var1.getSubmittingOrganization(), var2);
         this.duplicateClassifications(var1.getClassifications());
         this.duplicateAssociations(var1.getAssociations());
         this.duplicateExternalIdentifiers(var1.getExternalIdentifiers());
         this.duplicateExternalLinks(var1.getExternalLinks());
      }

   }

   public RegistryObjectImpl(InternationalString var1, RegistryServiceImpl var2) {
      super(var2);
      this.m_name = var1;
   }

   public Key getKey() throws JAXRException {
      return this.m_key;
   }

   public InternationalString getDescription() throws JAXRException {
      return this.m_description != null ? this.m_description : this.getEmptyInternationalString();
   }

   public void setDescription(InternationalString var1) throws JAXRException {
      this.m_description = var1;
   }

   public InternationalString getName() {
      return this.m_name == null ? this.getEmptyInternationalString() : this.m_name;
   }

   public void setName(InternationalString var1) throws JAXRException {
      this.m_name = var1;
   }

   public void setKey(Key var1) throws JAXRException {
      this.m_key = var1;
   }

   public String toXML() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void addClassification(Classification var1) throws JAXRException {
      if (null != var1) {
         var1.setClassifiedObject(this);
         this.m_classifications.add(var1);
      }

   }

   public void addClassifications(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Classification.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Classification var3 = (Classification)var2.next();
         this.addClassification(var3);
      }

   }

   public void removeClassification(Classification var1) throws JAXRException {
      if (null != var1) {
         this.m_classifications.remove(var1);
      }

   }

   public void removeClassifications(Collection var1) throws JAXRException {
      if (null != var1) {
         this.m_classifications.removeAll(var1);
      }

   }

   public void setClassifications(Collection var1) throws JAXRException {
      if (null != var1) {
         this.m_classifications.clear();
         this.addClassifications(var1);
      }

   }

   public Collection getClassifications() throws JAXRException {
      return this.m_classifications;
   }

   public Collection getAuditTrail() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void addAssociation(Association var1) throws JAXRException {
      if (null != var1) {
         var1.setSourceObject(this);
         this.m_associations.add(var1);
      }

   }

   public void addAssociations(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Association.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Association var3 = (Association)var2.next();
         this.addAssociation(var3);
      }

   }

   public void removeAssociation(Association var1) throws JAXRException {
      if (null != var1) {
         this.m_associations.remove(var1);
      }

   }

   public void removeAssociations(Collection var1) throws JAXRException {
      if (null != var1) {
         this.m_associations.removeAll(var1);
      }

   }

   public void setAssociations(Collection var1) throws JAXRException {
      if (null != var1) {
         this.m_associations.clear();
         this.addAssociations(var1);
      }

   }

   public Collection getAssociations() throws JAXRException {
      return this.m_associations;
   }

   public Collection getAssociatedObjects() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void addExternalIdentifier(ExternalIdentifier var1) throws JAXRException {
      if (null != var1) {
         ((ExternalIdentifierImpl)var1).setRegistryObject(this);
         this.m_externalIdentifiers.add(var1);
      }

   }

   public void addExternalIdentifiers(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, ExternalIdentifier.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ExternalIdentifier var3 = (ExternalIdentifier)var2.next();
         this.addExternalIdentifier(var3);
      }

   }

   public void removeExternalIdentifier(ExternalIdentifier var1) throws JAXRException {
      if (null != var1) {
         this.m_externalIdentifiers.remove(var1);
      }

   }

   public void removeExternalIdentifiers(Collection var1) throws JAXRException {
      if (null != var1) {
         this.m_externalIdentifiers.removeAll(var1);
      }

   }

   public void setExternalIdentifiers(Collection var1) throws JAXRException {
      if (null != var1) {
         this.m_externalIdentifiers.clear();
         this.addExternalIdentifiers(var1);
      }

   }

   public Collection getExternalIdentifiers() throws JAXRException {
      return this.m_externalIdentifiers;
   }

   public void addExternalLink(ExternalLink var1) throws JAXRException {
      if (null != var1) {
         var1.setName(new InternationalStringImpl("businessEntityExt", this.getRegistryService()));
         this.m_externalLinks.add(var1);
         ((ExternalLinkImpl)var1).addLinkedObject(this);
      }

   }

   public void addExternalLinks(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, ExternalLink.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ExternalLink var3 = (ExternalLink)var2.next();
         this.addExternalLink(var3);
      }

   }

   public void removeExternalLink(ExternalLink var1) throws JAXRException {
      if (null != var1) {
         this.m_externalLinks.remove(var1);
      }

   }

   public void removeExternalLinks(Collection var1) throws JAXRException {
      if (null != var1) {
         this.m_externalLinks.removeAll(var1);
      }

   }

   public void setExternalLinks(Collection var1) throws JAXRException {
      if (null != var1) {
         this.m_externalLinks.clear();
         this.addExternalLinks(var1);
      }

   }

   public Collection getExternalLinks() throws JAXRException {
      return this.m_externalLinks;
   }

   public Concept getObjectType() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public Organization getSubmittingOrganization() throws JAXRException {
      return this.m_submittingOrganization;
   }

   public void setSubmittingOrganization(Organization var1) throws JAXRException {
      this.m_submittingOrganization = (OrganizationImpl)var1;
   }

   public Collection getRegistryPackages() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public LifeCycleManager getLifeCycleManager() throws JAXRException {
      return this.getRegistryService().getBusinessLifeCycleManager();
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_name, this.m_description, this.m_key, this.getName(this.m_submittingOrganization), this.m_classifications, this.m_associations, this.m_externalIdentifiers, this.m_externalLinks};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_name", "m_description", "m_key", "m_submittingOrganization.Name", "m_classifications", "m_associations", "m_externalIdentifiers", "m_externalLinks"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private void duplicateClassifications(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Classification var3 = (Classification)var2.next();
            ClassificationImpl var4 = new ClassificationImpl(var3, this.getRegistryService());
            this.m_classifications.add(var4);
         }
      }

   }

   private void duplicateAssociations(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Association var3 = (Association)var2.next();
            AssociationImpl var4 = new AssociationImpl(var3, this.getRegistryService());
            this.m_associations.add(var4);
         }
      }

   }

   private void duplicateExternalIdentifiers(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ExternalIdentifier var3 = (ExternalIdentifier)var2.next();
            ExternalIdentifierImpl var4 = new ExternalIdentifierImpl(var3, this.getRegistryService());
            this.m_externalIdentifiers.add(var4);
         }
      }

   }

   private void duplicateExternalLinks(Collection var1) throws JAXRException {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ExternalLink var3 = (ExternalLink)var2.next();
            ExternalLinkImpl var4 = new ExternalLinkImpl(var3, this.getRegistryService());
            this.m_externalIdentifiers.add(var4);
         }
      }

   }
}
