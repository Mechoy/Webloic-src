package weblogic.xml.jaxr.registry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.activation.DataHandler;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.LifeCycleManager;
import javax.xml.registry.RegistryService;
import javax.xml.registry.UnexpectedObjectException;
import javax.xml.registry.UnsupportedCapabilityException;
import javax.xml.registry.infomodel.Association;
import javax.xml.registry.infomodel.Classification;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.EmailAddress;
import javax.xml.registry.infomodel.ExternalIdentifier;
import javax.xml.registry.infomodel.ExternalLink;
import javax.xml.registry.infomodel.ExtrinsicObject;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.LocalizedString;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.PersonName;
import javax.xml.registry.infomodel.PostalAddress;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.RegistryPackage;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.Slot;
import javax.xml.registry.infomodel.SpecificationLink;
import javax.xml.registry.infomodel.TelephoneNumber;
import javax.xml.registry.infomodel.User;
import weblogic.xml.jaxr.registry.bridge.uddi.UDDIBridgeMapperUtil;
import weblogic.xml.jaxr.registry.infomodel.AssociationImpl;
import weblogic.xml.jaxr.registry.infomodel.AuditableEventImpl;
import weblogic.xml.jaxr.registry.infomodel.BaseInfoModelObject;
import weblogic.xml.jaxr.registry.infomodel.ClassificationImpl;
import weblogic.xml.jaxr.registry.infomodel.ClassificationSchemeImpl;
import weblogic.xml.jaxr.registry.infomodel.ConceptImpl;
import weblogic.xml.jaxr.registry.infomodel.EmailAddressImpl;
import weblogic.xml.jaxr.registry.infomodel.ExternalIdentifierImpl;
import weblogic.xml.jaxr.registry.infomodel.ExternalLinkImpl;
import weblogic.xml.jaxr.registry.infomodel.ExtrinsicObjectImpl;
import weblogic.xml.jaxr.registry.infomodel.InternationalStringImpl;
import weblogic.xml.jaxr.registry.infomodel.KeyImpl;
import weblogic.xml.jaxr.registry.infomodel.LocalizedStringImpl;
import weblogic.xml.jaxr.registry.infomodel.OrganizationImpl;
import weblogic.xml.jaxr.registry.infomodel.PersonNameImpl;
import weblogic.xml.jaxr.registry.infomodel.PostalAddressImpl;
import weblogic.xml.jaxr.registry.infomodel.RegistryEntryImpl;
import weblogic.xml.jaxr.registry.infomodel.RegistryPackageImpl;
import weblogic.xml.jaxr.registry.infomodel.ServiceBindingImpl;
import weblogic.xml.jaxr.registry.infomodel.ServiceImpl;
import weblogic.xml.jaxr.registry.infomodel.SlotImpl;
import weblogic.xml.jaxr.registry.infomodel.SpecificationLinkImpl;
import weblogic.xml.jaxr.registry.infomodel.TelephoneNumberImpl;
import weblogic.xml.jaxr.registry.infomodel.UserImpl;
import weblogic.xml.jaxr.registry.infomodel.VersionableImpl;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;
import weblogic.xml.jaxr.registry.util.JAXRLogger;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public class LifeCycleManagerImpl extends BaseJAXRObject implements LifeCycleManager {
   private RegistryServiceImpl m_registryServiceImpl;
   private Map m_objects;
   private Map m_capabilities;

   public LifeCycleManagerImpl(RegistryServiceImpl var1) {
      this.m_registryServiceImpl = var1;
      if (this.m_objects == null) {
         this.m_objects = getAllObjects();
      }

      if (this.m_capabilities == null) {
         this.m_capabilities = this.getCapabilityLevels();
      }

   }

   public Object createObject(String var1) throws JAXRException, InvalidRequestException {
      String var3;
      try {
         Class var11 = (Class)this.m_objects.get(var1);
         if (var11 == null) {
            String var12 = JAXRMessages.getMessage("jaxr.registry.createObject.unknownType", new Object[]{var1});
            throw new InvalidRequestException(var12);
         } else {
            int var4 = this.m_registryServiceImpl.getCapabilityProfile().getCapabilityLevel();
            int var5 = (Integer)this.m_capabilities.get(var1);
            if (var5 > var4) {
               String var13 = JAXRMessages.getMessage("jaxr.registry.invalidClassCapability", new Object[]{var1, new Integer(var4), new Integer(var5)});
               throw new UnsupportedCapabilityException(var13);
            } else {
               Object var2;
               if (BaseInfoModelObject.class.isAssignableFrom(var11)) {
                  Constructor var6 = var11.getConstructor(this.m_registryServiceImpl.getClass());
                  var2 = var6.newInstance(this.m_registryServiceImpl);
               } else {
                  var2 = var11.newInstance();
               }

               return var2;
            }
         }
      } catch (InstantiationException var7) {
         var3 = JAXRMessages.getMessage("jaxr.registry.lifeCycleManagerImplException", new Object[]{"InstantiationException"});
         throw new JAXRException(var3, var7);
      } catch (IllegalAccessException var8) {
         var3 = JAXRMessages.getMessage("jaxr.registry.lifeCycleManagerImplException", new Object[]{"IllegalAccessException"});
         throw new JAXRException(var3, var8);
      } catch (NoSuchMethodException var9) {
         var3 = JAXRMessages.getMessage("jaxr.registry.lifeCycleManagerImplException", new Object[]{"NoSuchMethodException"});
         throw new JAXRException(var3, var9);
      } catch (InvocationTargetException var10) {
         var3 = JAXRMessages.getMessage("jaxr.registry.lifeCycleManagerImplException", new Object[]{"InvocationTargetException"});
         throw new JAXRException(var3, var10);
      }
   }

   public Association createAssociation(RegistryObject var1, Concept var2) throws JAXRException {
      AssociationImpl var3 = new AssociationImpl(this.m_registryServiceImpl);
      var3.setTargetObject(var1);
      var3.setAssociationType(var2);
      return var3;
   }

   public Classification createClassification(ClassificationScheme var1, String var2, String var3) throws JAXRException {
      ClassificationImpl var4 = new ClassificationImpl(this.m_registryServiceImpl);
      var4.setClassificationScheme(var1);
      var4.setName(new InternationalStringImpl(var2, this.m_registryServiceImpl));
      var4.setValue(var3);
      return var4;
   }

   public Classification createClassification(ClassificationScheme var1, InternationalString var2, String var3) throws JAXRException {
      ClassificationImpl var4 = new ClassificationImpl(this.m_registryServiceImpl);
      var4.setClassificationScheme(var1);
      var4.setName(var2);
      var4.setValue(var3);
      return var4;
   }

   public Classification createClassification(Concept var1) throws JAXRException, InvalidRequestException {
      if (var1.getClassificationScheme() != null) {
         ClassificationImpl var3 = new ClassificationImpl(this.m_registryServiceImpl);
         var3.setConcept(var1);
         return var3;
      } else {
         String var2 = JAXRMessages.getMessage("jaxr.registry.invalidConcept");
         throw new InvalidRequestException(var2);
      }
   }

   public ClassificationScheme createClassificationScheme(String var1, String var2) throws JAXRException, InvalidRequestException {
      ClassificationSchemeImpl var3 = new ClassificationSchemeImpl(this.m_registryServiceImpl);
      var3.setName(new InternationalStringImpl(var1, this.m_registryServiceImpl));
      var3.setDescription(new InternationalStringImpl(var2, this.m_registryServiceImpl));
      return var3;
   }

   public ClassificationScheme createClassificationScheme(InternationalString var1, InternationalString var2) throws JAXRException, InvalidRequestException {
      ClassificationSchemeImpl var3 = new ClassificationSchemeImpl(this.m_registryServiceImpl);
      var3.setName(var1);
      var3.setDescription(var2);
      return var3;
   }

   public ClassificationScheme createClassificationScheme(Concept var1) throws JAXRException, InvalidRequestException {
      if (var1.getClassificationScheme() == null && var1.getParentConcept() == null) {
         ClassificationSchemeImpl var2 = new ClassificationSchemeImpl(var1, this.m_registryServiceImpl);
         return var2;
      } else {
         throw new InvalidRequestException();
      }
   }

   public Concept createConcept(RegistryObject var1, String var2, String var3) throws JAXRException {
      ConceptImpl var4 = new ConceptImpl(var1, new InternationalStringImpl(var2, this.m_registryServiceImpl), var3, this.m_registryServiceImpl);
      return var4;
   }

   public Concept createConcept(RegistryObject var1, InternationalString var2, String var3) throws JAXRException {
      ConceptImpl var4 = new ConceptImpl(var1, var2, var3, this.m_registryServiceImpl);
      return var4;
   }

   public EmailAddress createEmailAddress(String var1) throws JAXRException {
      EmailAddressImpl var2 = new EmailAddressImpl(this.m_registryServiceImpl);
      var2.setAddress(var1);
      return var2;
   }

   public EmailAddress createEmailAddress(String var1, String var2) throws JAXRException {
      EmailAddressImpl var3 = new EmailAddressImpl(this.m_registryServiceImpl);
      var3.setAddress(var1);
      var3.setType(var2);
      return var3;
   }

   public ExternalIdentifier createExternalIdentifier(ClassificationScheme var1, String var2, String var3) throws JAXRException {
      return this.createExternalIdentifier(var1, (InternationalString)(new InternationalStringImpl(var2, this.m_registryServiceImpl)), var3);
   }

   public ExternalIdentifier createExternalIdentifier(ClassificationScheme var1, InternationalString var2, String var3) throws JAXRException {
      ExternalIdentifierImpl var4 = new ExternalIdentifierImpl(this.m_registryServiceImpl);
      var4.setIdentificationScheme(var1);
      var4.setName(var2);
      var4.setValue(var3);
      return var4;
   }

   public ExternalLink createExternalLink(String var1, String var2) throws JAXRException {
      return this.createExternalLink(var1, (InternationalString)(new InternationalStringImpl(var2, this.m_registryServiceImpl)));
   }

   public ExternalLink createExternalLink(String var1, InternationalString var2) throws JAXRException {
      ExternalLinkImpl var3 = new ExternalLinkImpl(this.m_registryServiceImpl);
      var3.setDescription(var2);
      var3.setExternalURI(var1, false);
      return var3;
   }

   public ExtrinsicObject createExtrinsicObject(DataHandler var1) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }

   public InternationalString createInternationalString() throws JAXRException {
      return new InternationalStringImpl(this.m_registryServiceImpl);
   }

   public InternationalString createInternationalString(String var1) throws JAXRException {
      return new InternationalStringImpl(var1, this.m_registryServiceImpl);
   }

   public InternationalString createInternationalString(Locale var1, String var2) throws JAXRException {
      return new InternationalStringImpl(var1, var2, this.m_registryServiceImpl);
   }

   public Key createKey(String var1) throws JAXRException {
      return new KeyImpl(var1, this.m_registryServiceImpl);
   }

   public LocalizedString createLocalizedString(Locale var1, String var2) throws JAXRException {
      JAXRUtil.verifyNotNull(var2, String.class);
      if (var1 == null) {
         var1 = Locale.getDefault();
      }

      return new LocalizedStringImpl(var1, var2, this.m_registryServiceImpl);
   }

   public LocalizedString createLocalizedString(Locale var1, String var2, String var3) throws JAXRException {
      JAXRUtil.verifyNotNull(var2, String.class);
      JAXRUtil.verifyNotNull(var3, String.class);
      if (var1 == null) {
         var1 = Locale.getDefault();
      }

      return new LocalizedStringImpl(var1, var2, var3, this.m_registryServiceImpl);
   }

   public Organization createOrganization(String var1) throws JAXRException {
      return new OrganizationImpl(new InternationalStringImpl(var1, this.m_registryServiceImpl), this.m_registryServiceImpl);
   }

   public Organization createOrganization(InternationalString var1) throws JAXRException {
      return new OrganizationImpl(var1, this.m_registryServiceImpl);
   }

   public PersonName createPersonName(String var1, String var2, String var3) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }

   public PersonName createPersonName(String var1) throws JAXRException {
      PersonNameImpl var2 = new PersonNameImpl(this.m_registryServiceImpl);
      var2.setFullName(var1);
      return var2;
   }

   public PostalAddress createPostalAddress(String var1, String var2, String var3, String var4, String var5, String var6, String var7) throws JAXRException {
      PostalAddressImpl var8 = new PostalAddressImpl(this.m_registryServiceImpl);
      var8.setCity(var3);
      var8.setCountry(var5);
      var8.setPostalCode(var6);
      var8.setStateOrProvince(var4);
      var8.setStreet(var2);
      var8.setStreetNumber(var1);
      var8.setType(var7);
      return var8;
   }

   public RegistryPackage createRegistryPackage(String var1) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }

   public RegistryPackage createRegistryPackage(InternationalString var1) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }

   public Service createService(String var1) throws JAXRException {
      return this.createService((InternationalString)(new InternationalStringImpl(var1, this.m_registryServiceImpl)));
   }

   public Service createService(InternationalString var1) throws JAXRException {
      ServiceImpl var2 = new ServiceImpl(this.m_registryServiceImpl);
      var2.setName(var1);
      return var2;
   }

   public ServiceBinding createServiceBinding() throws JAXRException {
      return new ServiceBindingImpl(this.m_registryServiceImpl);
   }

   public Slot createSlot(String var1, String var2, String var3) throws JAXRException {
      SlotImpl var4 = new SlotImpl(this.m_registryServiceImpl);
      var4.setName(var1);
      var4.setSlotType(var3);
      ArrayList var5 = new ArrayList();
      var5.add(var2);
      var4.setValues(var5);
      return var4;
   }

   public Slot createSlot(String var1, Collection var2, String var3) throws JAXRException {
      SlotImpl var4 = new SlotImpl(this.m_registryServiceImpl);
      var4.setName(var1);
      var4.setSlotType(var3);
      var4.setValues(var2);
      return var4;
   }

   public SpecificationLink createSpecificationLink() throws JAXRException {
      SpecificationLinkImpl var1 = new SpecificationLinkImpl(this.m_registryServiceImpl);
      return var1;
   }

   public TelephoneNumber createTelephoneNumber() throws JAXRException {
      return new TelephoneNumberImpl(this.m_registryServiceImpl);
   }

   public User createUser() throws JAXRException {
      return new UserImpl(this.m_registryServiceImpl);
   }

   public BulkResponse saveObjects(Collection var1) throws JAXRException {
      BulkResponseImpl var2 = new BulkResponseImpl(this.m_registryServiceImpl);
      BusinessLifeCycleManagerImpl var4 = new BusinessLifeCycleManagerImpl(this.m_registryServiceImpl);

      BulkResponse var3;
      for(Iterator var5 = var1.iterator(); var5.hasNext(); UDDIBridgeMapperUtil.accumulateBulkResponses(var2, var3)) {
         ArrayList var6 = new ArrayList();
         Object var7 = var5.next();
         var6.add(var7);
         if (var7 instanceof Association) {
            var3 = var4.saveAssociations(var6, false);
         } else if (var7 instanceof ClassificationScheme) {
            var3 = var4.saveClassificationSchemes(var6);
         } else if (var7 instanceof Concept) {
            var3 = var4.saveConcepts(var6);
         } else if (var7 instanceof Organization) {
            var3 = var4.saveOrganizations(var6);
         } else if (var7 instanceof ServiceBinding) {
            var3 = var4.saveServiceBindings(var6);
         } else {
            if (!(var7 instanceof Service)) {
               String var8 = JAXRMessages.getMessage("jaxr.infomodel.collection.invalidRegistryObjectType");
               throw new UnexpectedObjectException(var8);
            }

            var3 = var4.saveServices(var6);
         }
      }

      return var2;
   }

   public BulkResponse deprecateObjects(Collection var1) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }

   public BulkResponse unDeprecateObjects(Collection var1) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }

   public BulkResponse deleteObjects(Collection var1) throws JAXRException {
      this.checkCapability(this.getRegistryService(), 1);
      return null;
   }

   public BulkResponse deleteObjects(Collection var1, String var2) throws JAXRException {
      BusinessLifeCycleManagerImpl var4 = new BusinessLifeCycleManagerImpl(this.m_registryServiceImpl);
      BulkResponse var3;
      if (var2.equals("Association")) {
         var3 = var4.deleteAssociations(var1);
      } else if (var2.equals("ClassificationScheme")) {
         var3 = var4.deleteClassificationSchemes(var1);
      } else if (var2.equals("Concept")) {
         var3 = var4.deleteConcepts(var1);
      } else if (var2.equals("Organization")) {
         var3 = var4.deleteOrganizations(var1);
      } else if (var2.equals("ServiceBinding")) {
         var3 = var4.deleteServiceBindings(var1);
      } else {
         if (!var2.equals("Service")) {
            String var5 = JAXRMessages.getMessage("jaxr.infomodel.collection.invalidRegistryObjectType");
            throw new UnexpectedObjectException(var5);
         }

         var3 = var4.deleteServices(var1);
      }

      return var3;
   }

   public RegistryService getRegistryService() throws JAXRException {
      return this.m_registryServiceImpl;
   }

   protected JAXRLogger getLogger() {
      return this.m_registryServiceImpl.getLogger();
   }

   private static Map getAllObjects() {
      HashMap var0 = new HashMap();
      var0.put("Association", AssociationImpl.class);
      var0.put("AuditableEvent", AuditableEventImpl.class);
      var0.put("Classification", ClassificationImpl.class);
      var0.put("ClassificationScheme", ClassificationSchemeImpl.class);
      var0.put("Concept", ConceptImpl.class);
      var0.put("EmailAddress", EmailAddressImpl.class);
      var0.put("ExternalLink", ExternalLinkImpl.class);
      var0.put("ExternalIdentifier", ExternalIdentifierImpl.class);
      var0.put("ExtrinsicObject", ExtrinsicObjectImpl.class);
      var0.put("InternationalString", InternationalStringImpl.class);
      var0.put("Key", KeyImpl.class);
      var0.put("LocalizedString", LocalizedStringImpl.class);
      var0.put("Organization", OrganizationImpl.class);
      var0.put("PersonName", PersonNameImpl.class);
      var0.put("PostalAddress", PostalAddressImpl.class);
      var0.put("RegistryEntry", RegistryEntryImpl.class);
      var0.put("RegistryPackage", RegistryPackageImpl.class);
      var0.put("Service", ServiceImpl.class);
      var0.put("ServiceBinding", ServiceBindingImpl.class);
      var0.put("Slot", SlotImpl.class);
      var0.put("SpecificationLink", SpecificationLinkImpl.class);
      var0.put("TelephoneNumber", TelephoneNumberImpl.class);
      var0.put("User", UserImpl.class);
      var0.put("Versionable", VersionableImpl.class);
      return var0;
   }

   private Map getCapabilityLevels() {
      Integer var1 = new Integer(0);
      Integer var2 = new Integer(1);
      HashMap var3 = new HashMap();
      var3.put("Association", var1);
      var3.put("AuditableEvent", var2);
      var3.put("Classification", var1);
      var3.put("ClassificationScheme", var1);
      var3.put("Concept", var1);
      var3.put("EmailAddress", var1);
      var3.put("ExternalLink", var1);
      var3.put("ExternalIdentifier", var1);
      var3.put("ExtrinsicObject", var2);
      var3.put("InternationalString", var1);
      var3.put("Key", var1);
      var3.put("LocalizedString", var1);
      var3.put("Organization", var1);
      var3.put("PersonName", var1);
      var3.put("PostalAddress", var1);
      var3.put("RegistryEntry", var2);
      var3.put("RegistryPackage", var2);
      var3.put("Service", var1);
      var3.put("ServiceBinding", var1);
      var3.put("Slot", var1);
      var3.put("SpecificationLink", var1);
      var3.put("TelephoneNumber", var1);
      var3.put("User", var1);
      var3.put("Versionable", var2);
      return var3;
   }
}
