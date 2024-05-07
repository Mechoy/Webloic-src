package weblogic.xml.jaxr.registry.bridge.uddi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.Association;
import javax.xml.registry.infomodel.Classification;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.EmailAddress;
import javax.xml.registry.infomodel.ExternalIdentifier;
import javax.xml.registry.infomodel.ExternalLink;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.LocalizedString;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.PersonName;
import javax.xml.registry.infomodel.PostalAddress;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.Slot;
import javax.xml.registry.infomodel.SpecificationLink;
import javax.xml.registry.infomodel.TelephoneNumber;
import javax.xml.registry.infomodel.User;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.Address;
import weblogic.auddi.uddi.datastructure.AddressLine;
import weblogic.auddi.uddi.datastructure.AddressLines;
import weblogic.auddi.uddi.datastructure.Addresses;
import weblogic.auddi.uddi.datastructure.AssertionStatusItem;
import weblogic.auddi.uddi.datastructure.AssertionStatusItems;
import weblogic.auddi.uddi.datastructure.BindingTemplate;
import weblogic.auddi.uddi.datastructure.BindingTemplates;
import weblogic.auddi.uddi.datastructure.BusinessEntities;
import weblogic.auddi.uddi.datastructure.BusinessEntity;
import weblogic.auddi.uddi.datastructure.BusinessService;
import weblogic.auddi.uddi.datastructure.BusinessServices;
import weblogic.auddi.uddi.datastructure.CategoryBag;
import weblogic.auddi.uddi.datastructure.Contact;
import weblogic.auddi.uddi.datastructure.Contacts;
import weblogic.auddi.uddi.datastructure.DiscoveryURL;
import weblogic.auddi.uddi.datastructure.DiscoveryURLs;
import weblogic.auddi.uddi.datastructure.Email;
import weblogic.auddi.uddi.datastructure.Emails;
import weblogic.auddi.uddi.datastructure.HostingRedirector;
import weblogic.auddi.uddi.datastructure.IdentifierBag;
import weblogic.auddi.uddi.datastructure.InstanceDetails;
import weblogic.auddi.uddi.datastructure.InstanceParms;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.Names;
import weblogic.auddi.uddi.datastructure.OverviewDoc;
import weblogic.auddi.uddi.datastructure.Phone;
import weblogic.auddi.uddi.datastructure.Phones;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModelInstanceDetails;
import weblogic.auddi.uddi.datastructure.TModelInstanceInfo;
import weblogic.auddi.uddi.datastructure.TModels;
import weblogic.auddi.uddi.datastructure.UDDIKey;
import weblogic.auddi.uddi.request.publish.DeleteBindingRequest;
import weblogic.auddi.uddi.request.publish.DeleteBusinessRequest;
import weblogic.auddi.uddi.request.publish.DeleteServiceRequest;
import weblogic.auddi.uddi.request.publish.DeleteTModelRequest;
import weblogic.auddi.uddi.response.BindingDetailResponse;
import weblogic.auddi.uddi.response.BusinessDetailResponse;
import weblogic.auddi.uddi.response.BusinessInfo;
import weblogic.auddi.uddi.response.BusinessInfos;
import weblogic.auddi.uddi.response.RelatedBusinessInfo;
import weblogic.auddi.uddi.response.RelatedBusinessInfos;
import weblogic.auddi.uddi.response.RelatedBusinessListResponse;
import weblogic.auddi.uddi.response.ServiceDetailResponse;
import weblogic.auddi.uddi.response.SharedRelationships;
import weblogic.auddi.uddi.response.TModelDetailResponse;
import weblogic.xml.jaxr.registry.BaseJAXRObject;
import weblogic.xml.jaxr.registry.BulkResponseImpl;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.infomodel.AssociationImpl;
import weblogic.xml.jaxr.registry.infomodel.ExternalIdentifierImpl;
import weblogic.xml.jaxr.registry.infomodel.ExternalLinkImpl;
import weblogic.xml.jaxr.registry.infomodel.InternationalStringImpl;
import weblogic.xml.jaxr.registry.infomodel.KeyImpl;
import weblogic.xml.jaxr.registry.infomodel.UserImpl;
import weblogic.xml.jaxr.registry.provider.InternalClassificationSchemes;
import weblogic.xml.jaxr.registry.util.ClassificationSchemeHelper;

public class UDDIBridgeResponseMapper extends BaseJAXRObject {
   public static final String SLOT_TYPE_STRING = "String";

   private UDDIBridgeResponseMapper() {
   }

   public static BulkResponse getBulkResponseForOrganizations(RegistryServiceImpl var0, BusinessEntities var1) throws JAXRException {
      Collection var2 = getOrganizationsFromBusinessEntities(var0, var1);
      BulkResponseImpl var3 = getBulkResponseFromCollection(var2, var0);
      return var3;
   }

   public static BulkResponse getBulkResponseForAssociations(RegistryServiceImpl var0, RelatedBusinessListResponse var1, boolean var2, Organization var3) throws JAXRException, UDDIException {
      ArrayList var4 = new ArrayList();
      RelatedBusinessInfos var5 = var1.getRelatedBusinessInfos();
      if (var5.size() > 0) {
         for(RelatedBusinessInfo var6 = var5.getFirst(); var6 != null; var6 = var5.getNext()) {
            BusinessEntity var7 = new BusinessEntity(var6.getBusinessKey());
            Names var8 = var6.getNames();

            for(Name var9 = var8.getFirst(); var9 != null; var9 = var8.getNext()) {
               var7.addName(var9);
            }

            var7.setDescriptions(var6.getDescriptions());
            ArrayList var17 = new ArrayList();
            List var10 = var6.getSharedRelationships();
            Iterator var11 = var10.iterator();

            while(var11.hasNext()) {
               SharedRelationships var12 = (SharedRelationships)var11.next();

               for(KeyedReference var13 = var12.getFirst(); var13 != null; var13 = var12.getNext()) {
                  Concept var14 = UDDIBridgeMapperUtil.getAssociationTypeFromAssertion(var13, var0);
                  var17.add(var14);
               }
            }

            Organization var18;
            Organization var19;
            if (var2) {
               var18 = getOrganization(var0, var7);
               var19 = var3;
            } else {
               var18 = var3;
               var19 = getOrganization(var0, var7);
            }

            if (var17.size() == 0) {
               AssociationImpl var21 = (AssociationImpl)var0.getBusinessLifeCycleManager().createAssociation(var19, (Concept)null);
               var21.setSourceObject(var18);
               var21.setConfirmedBySourceOwner(true);
               var21.setConfirmedByTargetOwner(true);
               var4.add(var21);
            } else {
               Iterator var20 = var17.iterator();

               while(var20.hasNext()) {
                  AssociationImpl var22 = (AssociationImpl)var0.getBusinessLifeCycleManager().createAssociation(var19, (Concept)null);
                  var22.setSourceObject(var18);
                  var22.setConfirmedBySourceOwner(true);
                  var22.setConfirmedByTargetOwner(true);
                  Concept var15 = (Concept)var20.next();
                  var22.setAssociationType(var15);
                  var4.add(var22);
               }
            }
         }
      }

      BulkResponseImpl var16 = getBulkResponseFromCollection(var4, var0);
      return var16;
   }

   public static BulkResponse getBulkResponseForClassificationSchemes(RegistryServiceImpl var0, TModels var1) throws JAXRException {
      Collection var2 = getClassificationSchemes(var0, var1);
      BulkResponseImpl var3 = getBulkResponseFromCollection(var2, var0);
      return var3;
   }

   public static BulkResponse getBulkResponseForConcepts(RegistryServiceImpl var0, TModels var1) throws JAXRException {
      Collection var2 = getConcepts(var0, var1);
      BulkResponseImpl var3 = getBulkResponseFromCollection(var2, var0);
      return var3;
   }

   public static BulkResponse getBulkResponseForConceptsOrClassificationSchemes(RegistryServiceImpl var0, TModelDetailResponse var1) throws JAXRException {
      ArrayList var2 = new ArrayList();
      TModels var3 = var1.getTModels();

      for(TModel var4 = var3.getFirst(); var4 != null; var4 = var3.getNext()) {
         if (UDDIBridgeMapperUtil.isConcept(var4)) {
            var2.add(getConcept(var0, var4));
         } else {
            var2.add(getClassificationScheme(var0, var4));
         }
      }

      BulkResponseImpl var5 = getBulkResponseFromCollection(var2, var0);
      return var5;
   }

   public static BulkResponse getBulkResponseForServiceBindings(RegistryServiceImpl var0, BindingDetailResponse var1) throws JAXRException {
      BindingTemplates var2 = var1.getBindingTemplates();
      Collection var3 = getServiceBindings(var0, var2);
      BulkResponseImpl var4 = getBulkResponseFromCollection(var3, var0);
      return var4;
   }

   public static BulkResponse getBulkResponseForServiceBindings(RegistryServiceImpl var0, BindingDetailResponse var1, Collection var2) throws JAXRException {
      BindingTemplates var3 = var1.getBindingTemplates();
      Collection var4 = getServiceBindings(var0, var3, var2);
      BulkResponseImpl var5 = getBulkResponseFromCollection(var4, var0);
      return var5;
   }

   public static BulkResponse getBulkResponseForServicesFromServiceDetailResponse(RegistryServiceImpl var0, ServiceDetailResponse var1) throws JAXRException {
      BusinessServices var2 = var1.getBusinessServices();
      Collection var3 = getServices(var0, var2);
      BulkResponseImpl var4 = getBulkResponseFromCollection(var3, var0);
      return var4;
   }

   public static BulkResponse getBulkResponseForKeysFromServiceDetailResponse(RegistryServiceImpl var0, ServiceDetailResponse var1) throws JAXRException {
      BusinessServices var2 = var1.getBusinessServices();
      Collection var3 = getKeys(var0, (BusinessServices)var2);
      BulkResponseImpl var4 = getBulkResponseFromCollection(var3, var0);
      return var4;
   }

   public static BulkResponse getBulkResponseForKeysFromDeleteBusinessRequest(RegistryServiceImpl var0, DeleteBusinessRequest var1) throws JAXRException {
      Collection var2 = getKeys(var0, (ArrayList)var1.getBusinessKeys());
      BulkResponseImpl var3 = getBulkResponseFromCollection(var2, var0);
      return var3;
   }

   public static BulkResponse getBulkResponseForKeys(RegistryServiceImpl var0, Collection var1) throws JAXRException {
      BulkResponseImpl var2 = getBulkResponseFromCollection(var1, var0);
      return var2;
   }

   public static BulkResponse getBulkResponseForKeysFromDeleteServiceRequest(RegistryServiceImpl var0, DeleteServiceRequest var1) throws JAXRException {
      ArrayList var2 = new ArrayList();

      for(ServiceKey var3 = var1.getFirst(); null != var3; var3 = var1.getNext()) {
         var2.add(var3);
      }

      Collection var4 = getKeys(var0, (ArrayList)var2);
      BulkResponseImpl var5 = getBulkResponseFromCollection(var4, var0);
      return var5;
   }

   public static BulkResponse getBulkResponseForKeysFromDeleteBindingRequest(RegistryServiceImpl var0, DeleteBindingRequest var1) throws JAXRException {
      Collection var2 = getKeys(var0, (ArrayList)var1.getBindingKeys());
      BulkResponseImpl var3 = getBulkResponseFromCollection(var2, var0);
      return var3;
   }

   public static BulkResponse getBulkResponseForKeysFromDeleteTModelRequest(RegistryServiceImpl var0, DeleteTModelRequest var1) throws JAXRException {
      Collection var2 = getKeys(var0, (ArrayList)var1.getTModelKeys());
      BulkResponseImpl var3 = getBulkResponseFromCollection(var2, var0);
      return var3;
   }

   public static BulkResponse getBulkResponseForKeysFromBusinessDetailResponse(RegistryServiceImpl var0, BusinessDetailResponse var1) throws JAXRException {
      BusinessEntities var2 = var1.getBusinessEntities();
      Collection var3 = getKeys(var0, (BusinessEntities)var2);
      BulkResponseImpl var4 = getBulkResponseFromCollection(var3, var0);
      return var4;
   }

   public static BulkResponse getBulkResponseForKeysFromBindingDetailResponse(RegistryServiceImpl var0, BindingDetailResponse var1) throws JAXRException {
      BindingTemplates var2 = var1.getBindingTemplates();
      Collection var3 = getKeys(var0, (BindingTemplates)var2);
      BulkResponseImpl var4 = getBulkResponseFromCollection(var3, var0);
      return var4;
   }

   public static BulkResponse getBulkResponseForKeysFromTModelDetailResponse(RegistryServiceImpl var0, TModelDetailResponse var1) throws JAXRException {
      TModels var2 = var1.getTModels();
      Collection var3 = getKeys(var0, (TModels)var2);
      BulkResponseImpl var4 = getBulkResponseFromCollection(var3, var0);
      return var4;
   }

   public static Collection getKeys(RegistryService var0, BusinessServices var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(BusinessService var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getKey(var0, var3.getServiceKey()));
         }
      }

      return (Collection)var2;
   }

   public static Collection getKeys(RegistryService var0, BusinessInfos var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(BusinessInfo var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getKey(var0, var3.getBusinessKey()));
         }
      }

      return (Collection)var2;
   }

   public static Collection getKeys(RegistryService var0, BusinessEntities var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(BusinessEntity var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getKey(var0, var3.getBusinessKey()));
         }
      }

      return (Collection)var2;
   }

   public static Collection getKeys(RegistryService var0, ArrayList var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            ((Collection)var2).add(getKey(var0, (UDDIKey)var1.get(var3)));
         }
      }

      return (Collection)var2;
   }

   public static Collection getKeys(RegistryService var0, BindingTemplates var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(BindingTemplate var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getKey(var0, var3.getBindingKey()));
         }
      }

      return (Collection)var2;
   }

   public static Collection getKeys(RegistryService var0, TModels var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(TModel var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getKey(var0, var3.getTModelKey()));
         }
      }

      return (Collection)var2;
   }

   public static Collection getKeys(RegistryService var0, DeleteServiceRequest var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(ServiceKey var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getKey(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static Collection getOrganizationsFromBusinessEntities(RegistryServiceImpl var0, BusinessEntities var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(BusinessEntity var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getOrganization(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static Organization getOrganization(RegistryServiceImpl var0, BusinessEntity var1) throws JAXRException {
      Organization var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createOrganization(getInternationalString(var0, var1.getNames()));
         if (var1.getBusinessKey() != null) {
            var2.setKey(getKey(var0, var1.getBusinessKey()));
         }

         if (var1.getAuthorizedName() != null) {
            var2.addSlot(getSlot(var0, "authorizedName", var1.getAuthorizedName().getName(), "String"));
         }

         if (var1.getOperator() != null) {
            var2.addSlot(getSlot(var0, "operator", var1.getOperator(), "String"));
         }

         if (var1.getDiscoveryURLs() != null) {
            var2.setExternalLinks(getExternalLinksFromDiscoveryURLs(var0, var1.getDiscoveryURLs()));
         }

         if (var1.getDescriptions() != null) {
            var2.setDescription(UDDIBridgeMapperUtil.getInternationalStringFromDescriptions(var1.getDescriptions(), var0));
         }

         if (var1.getContacts() != null) {
            setUsers(var0, var1.getContacts(), var2);
         }

         if (var1.getBusinessServices() != null) {
            var2.addServices(getServices(var0, var1.getBusinessServices()));
         }

         if (var1.getIdentifierBag() != null) {
            var2.addExternalIdentifiers(getExtenalIdentifiersFromIdentifierBag(var0, var1.getIdentifierBag()));
         }

         if (var1.getCategoryBag() != null) {
            Key var4 = var2.getKey();
            var2.addClassifications(getClassificationsFromCategoryBag(var0, var1.getCategoryBag(), var4));
         }
      }

      return var2;
   }

   public static Key getKey(RegistryService var0, UDDIKey var1) throws JAXRException {
      Key var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createKey(var1.getKey());
      }

      return var2;
   }

   public static User getPrimaryContact(RegistryServiceImpl var0, Contacts var1, Organization var2) throws JAXRException {
      User var3;
      if (var1 == null) {
         var3 = null;
      } else {
         var3 = getUser(var0, var1.getFirst());
         ((UserImpl)var3).setSubmittingOrganization(var2);
      }

      return var3;
   }

   public static void setUsers(RegistryServiceImpl var0, Contacts var1, Organization var2) throws JAXRException {
      if (var1 != null) {
         Contact var3 = var1.getFirst();
         UserImpl var4 = (UserImpl)getUser(var0, var3);
         var4.setSubmittingOrganization(var2);
         var2.setPrimaryContact(var4);

         for(var3 = var1.getNext(); var3 != null; var3 = var1.getNext()) {
            UserImpl var5 = (UserImpl)getUser(var0, var3);
            var5.setSubmittingOrganization(var2);
            var2.addUser(var5);
         }
      }

   }

   public static Collection getServices(RegistryService var0, BusinessServices var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(BusinessService var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getServiceFromBusinessService(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static Collection getExtenalIdentifiersFromIdentifierBag(RegistryService var0, IdentifierBag var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(KeyedReference var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getExtenalIdentifierFromKeyedReference(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static Collection getClassificationsFromCategoryBag(RegistryService var0, CategoryBag var1, Key var2) throws JAXRException {
      Object var3;
      if (var1 == null) {
         var3 = Collections.EMPTY_LIST;
      } else {
         var3 = new ArrayList();

         for(KeyedReference var4 = var1.getFirst(); var4 != null; var4 = var1.getNext()) {
            ((Collection)var3).add(getClassificationFromKeyedReference(var0, var4, var2));
         }
      }

      return (Collection)var3;
   }

   public static User getUser(RegistryServiceImpl var0, Contact var1) throws JAXRException {
      User var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createUser();
         if (null != var1.getUseType()) {
            var2.setType(var1.getUseType());
         }

         if (null != var1.getDescriptions()) {
            var2.setDescription(UDDIBridgeMapperUtil.getInternationalStringFromDescriptions(var1.getDescriptions(), var0));
         }

         if (null != var1.getPersonName()) {
            var2.setPersonName(getPersonName(var0, var1.getPersonName()));
         }

         if (null != var1.getPhones()) {
            var2.setTelephoneNumbers(getTelephoneNumbers(var0, var1.getPhones()));
         }

         if (null != var1.getEmails()) {
            var2.setEmailAddresses(getEmailAddresses(var0, var1.getEmails()));
         }

         if (null != var1.getAddresses()) {
            var2.setPostalAddresses(getPostalAddresses(var0, var1.getAddresses()));
         }
      }

      return var2;
   }

   public static PersonName getPersonName(RegistryService var0, String var1) throws JAXRException {
      PersonName var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createPersonName(var1);
      }

      return var2;
   }

   public static Collection getEmailAddresses(RegistryService var0, Emails var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(Email var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getEmailAddress(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static EmailAddress getEmailAddress(RegistryService var0, Email var1) throws JAXRException {
      EmailAddress var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createEmailAddress(var1.getEmail(), var1.getUseType());
      }

      return var2;
   }

   public static Collection getTelephoneNumbers(RegistryService var0, Phones var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(Phone var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getTelephoneNumber(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static TelephoneNumber getTelephoneNumber(RegistryService var0, Phone var1) throws JAXRException {
      TelephoneNumber var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createTelephoneNumber();
         if (null != var1.getPhoneNumber()) {
            var2.setNumber(var1.getPhoneNumber());
         }

         if (null != var1.getUseType()) {
            var2.setType(var1.getUseType());
         }
      }

      return var2;
   }

   public static Collection getPostalAddresses(RegistryServiceImpl var0, Addresses var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(Address var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getPostalAddress(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static PostalAddress getPostalAddress(RegistryServiceImpl var0, Address var1) throws JAXRException {
      PostalAddress var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = (PostalAddress)var3.createObject("PostalAddress");
         if (var1.getTModelKey() != null) {
            String var4 = var1.getTModelKey().getKey();
            ClassificationScheme var5 = (ClassificationScheme)var0.getBusinessQueryManager().getRegistryObject(var4, "ClassificationScheme");
            if (var5 != null) {
               var2.setPostalScheme(var5);
            }
         }

         ArrayList var15 = new ArrayList();
         AddressLines var16 = var1.getAddressLines();
         ClassificationScheme var6 = ClassificationSchemeHelper.getClassificationSchemeByName("PostalAddressAttributes", var0);

         for(AddressLine var7 = var16.getFirst(); var7 != null; var7 = var16.getNext()) {
            boolean var8 = false;
            ClassificationScheme var9 = var2.getPostalScheme();
            String var10 = var7.getKeyValue();
            if (var9 != null && var10 != null) {
               Iterator var11 = var9.getChildrenConcepts().iterator();

               while(var11.hasNext()) {
                  Concept var12 = (Concept)var11.next();
                  Key var13 = var12.getKey();
                  if (var10.equals(var12.getValue()) && var13 != null) {
                     Iterator var14 = var6.getChildrenConcepts().iterator();
                     if (isSemanticMatch((Concept)var14.next(), var13.getId(), var0)) {
                        var2.setStreetNumber(var7.getLine());
                        var8 = true;
                     } else if (isSemanticMatch((Concept)var14.next(), var13.getId(), var0)) {
                        var2.setStreet(var7.getLine());
                        var8 = true;
                     } else if (isSemanticMatch((Concept)var14.next(), var13.getId(), var0)) {
                        var2.setCity(var7.getLine());
                        var8 = true;
                     } else if (isSemanticMatch((Concept)var14.next(), var13.getId(), var0)) {
                        var2.setStateOrProvince(var7.getLine());
                        var8 = true;
                     } else if (isSemanticMatch((Concept)var14.next(), var13.getId(), var0)) {
                        var2.setPostalCode(var7.getLine());
                        var8 = true;
                     } else if (isSemanticMatch((Concept)var14.next(), var13.getId(), var0)) {
                        var2.setCountry(var7.getLine());
                        var8 = true;
                     } else {
                        var8 = false;
                     }
                  }
               }
            }

            if (!var8) {
               var15.add(var7);
            }
         }

         BusinessLifeCycleManager var17 = var0.getBusinessLifeCycleManager();
         Slot var20 = (Slot)var17.createObject("Slot");
         var20.setName("addressLines");
         var20.setSlotType((String)null);
         ArrayList var18 = new ArrayList();
         Iterator var19 = var15.iterator();

         while(var19.hasNext()) {
            AddressLine var21 = (AddressLine)var19.next();
            var18.add(var21);
         }

         var20.setValues(var18);
         if (null != var1.getSortCode()) {
            var2.addSlot(getSlot(var0, "sortCode", var1.getSortCode(), (String)null));
         }

         if (null != var1.getUseType()) {
            var2.setType(var1.getUseType());
         }
      }

      return var2;
   }

   public static Slot getSlot(RegistryService var0, String var1, String var2, String var3) throws JAXRException {
      BusinessLifeCycleManager var4 = var0.getBusinessLifeCycleManager();
      Slot var5 = var4.createSlot(var1, var2, var3);
      return var5;
   }

   public static Service getServiceFromBusinessService(RegistryService var0, BusinessService var1) throws JAXRException {
      Service var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = (Service)var3.createObject("Service");
         Key var4;
         if (var1.getBusinessKey() != null) {
            var4 = var3.createKey(var1.getBusinessKey().getKey());
            Organization var5 = (Organization)var3.createObject("Organization");
            var2.setProvidingOrganization(var5);
            var2.getProvidingOrganization().setKey(var4);
         }

         var4 = null;
         if (var1.getServiceKey() != null) {
            var4 = var3.createKey(var1.getServiceKey().getKey());
         }

         var2.setKey(var4);
         if (null != var1.getNames()) {
            var2.setName(getInternationalString(var0, var1.getNames()));
         }

         if (null != var1.getDescriptions()) {
            var2.setDescription(UDDIBridgeMapperUtil.getInternationalStringFromDescriptions(var1.getDescriptions(), var0));
         }

         if (null != var1.getBindingTemplates()) {
            var2.addServiceBindings(getServiceBindings(var0, var1.getBindingTemplates()));
         }

         if (null != var1.getCategoryBag()) {
            Key var6 = var2.getKey();
            var2.addClassifications(getClassificationsFromCategoryBag(var0, var1.getCategoryBag(), var6));
         }
      }

      return var2;
   }

   public static Collection getServiceBindings(RegistryService var0, BindingTemplates var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(BindingTemplate var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getServiceBinding(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static Collection getServiceBindings(RegistryService var0, BindingTemplates var1, Collection var2) throws JAXRException {
      Object var3;
      if (var1 == null) {
         var3 = Collections.EMPTY_LIST;
      } else {
         var3 = new ArrayList();

         for(BindingTemplate var4 = var1.getFirst(); var4 != null; var4 = var1.getNext()) {
            ((Collection)var3).add(getServiceBinding(var0, var4, var2));
         }
      }

      return (Collection)var3;
   }

   public static Collection getConcepts(RegistryService var0, TModels var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(TModel var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getConcept(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static Collection getClassificationSchemes(RegistryService var0, TModels var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(TModel var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getClassificationScheme(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static Concept getConcept(RegistryService var0, TModel var1) throws JAXRException {
      Concept var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createConcept((RegistryObject)null, var1.getName().getName(), (String)null);
         if (null != var1.getTModelKey()) {
            var2.setKey(var3.createKey(var1.getTModelKey().getKey()));
         }

         Slot var4;
         if (null != var1.getAuthorizedName()) {
            var4 = var3.createSlot("authorizedName", var1.getAuthorizedName().getName(), "authorizedName");
            var2.addSlot(var4);
         }

         if (null != var1.getOperator()) {
            var4 = var3.createSlot("operator", var1.getOperator().getName(), "operator");
            var2.addSlot(var4);
         }

         if (null != var1.getDescriptions()) {
            var2.setDescription(UDDIBridgeMapperUtil.getInternationalStringFromDescriptions(var1.getDescriptions(), var0));
         }

         if (null != var1.getOverviewDoc()) {
            var2.addExternalLink(getExternalLinkFromOverviewDoc(var0, var1.getOverviewDoc()));
         }

         if (null != var1.getCategoryBag()) {
            Key var5 = var2.getKey();
            var2.setClassifications(getClassificationsFromCategoryBag(var0, var1.getCategoryBag(), var5));
         }

         if (null != var1.getIdentifierBag()) {
            var2.setExternalIdentifiers(getExtenalIdentifiersFromIdentifierBag(var0, var1.getIdentifierBag()));
         }
      }

      return var2;
   }

   public static ClassificationScheme getClassificationScheme(RegistryService var0, TModel var1) throws JAXRException {
      ClassificationScheme var2;
      if (var1 == null) {
         var2 = null;
      } else {
         RegistryServiceImpl var3 = (RegistryServiceImpl)var0;
         BusinessLifeCycleManager var4 = var0.getBusinessLifeCycleManager();
         KeyImpl var5 = (KeyImpl)var4.createKey(var1.getTModelKey().getKey());
         InternalClassificationSchemes var6 = var3.getRegistryProxy().getInternalClassificationSchemes();
         var2 = var6.getClassificationSchemeByKey(var5, var3);
         if (var2 == null) {
            var2 = var4.createClassificationScheme(var1.getName().getName(), "");
            var2.setKey(var5);
         }

         Slot var7;
         if (null != var1.getAuthorizedName()) {
            var7 = var4.createSlot("authorizedName", var1.getAuthorizedName().getName(), "authorizedName");
            var2.addSlot(var7);
         }

         if (null != var1.getOperator()) {
            var7 = var4.createSlot("operator", var1.getOperator().getName(), "operator");
            var2.addSlot(var7);
         }

         if (null != var1.getDescriptions()) {
            var2.setDescription(UDDIBridgeMapperUtil.getInternationalStringFromDescriptions(var1.getDescriptions(), var0));
         }

         if (null != var1.getOverviewDoc()) {
            var2.addExternalLink(getExternalLinkFromOverviewDoc(var0, var1.getOverviewDoc()));
         }

         if (null != var1.getCategoryBag()) {
            Key var8 = var2.getKey();
            var2.setClassifications(getClassificationsFromCategoryBag(var0, var1.getCategoryBag(), var8));
         }

         if (null != var1.getIdentifierBag()) {
            var2.setExternalIdentifiers(getExtenalIdentifiersFromIdentifierBag(var0, var1.getIdentifierBag()));
         }
      }

      return var2;
   }

   public static ServiceBinding getServiceBinding(RegistryService var0, BindingTemplate var1) throws JAXRException {
      ServiceBinding var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createServiceBinding();
         var2.setKey(getKey(var0, var1.getBindingKey()));
         var2.getService().setKey(getKey(var0, var1.getServiceKey()));
         if (null != var1.getDescriptions()) {
            var2.setDescription(UDDIBridgeMapperUtil.getInternationalStringFromDescriptions(var1.getDescriptions(), var0));
         }

         if (null != var1.getAccessPoint()) {
            var2.setAccessURI(var1.getAccessPoint().getURL());
         }

         if (null != var1.getHostingRedirector()) {
            var2.setTargetBinding(getTargetBinding(var0, var1.getHostingRedirector()));
         }

         if (null != var1.getTModelInstanceDetails()) {
            var2.addSpecificationLinks(getSpecificationLinks(var0, var1.getTModelInstanceDetails()));
         }

         var2.setValidateURI(false);
      }

      return var2;
   }

   public static ServiceBinding getServiceBinding(RegistryService var0, BindingTemplate var1, Collection var2) throws JAXRException {
      ServiceBinding var3;
      if (var1 == null) {
         var3 = null;
      } else {
         BusinessLifeCycleManager var4 = var0.getBusinessLifeCycleManager();
         var3 = var4.createServiceBinding();
         var3.setKey(getKey(var0, var1.getBindingKey()));
         var3.getService().setKey(getKey(var0, var1.getServiceKey()));
         if (var2 != null) {
            Iterator var5 = var2.iterator();

            while(var5.hasNext()) {
               Service var6 = (Service)var5.next();
               if (var6.getKey().equals(var3.getService().getKey())) {
                  var6.addServiceBinding(var3);
                  break;
               }
            }
         }

         if (null != var1.getDescriptions()) {
            var3.setDescription(UDDIBridgeMapperUtil.getInternationalStringFromDescriptions(var1.getDescriptions(), var0));
         }

         if (null != var1.getAccessPoint()) {
            var3.setAccessURI(var1.getAccessPoint().getURL());
         }

         if (null != var1.getHostingRedirector()) {
            var3.setTargetBinding(getTargetBinding(var0, var1.getHostingRedirector()));
         }

         if (null != var1.getTModelInstanceDetails()) {
            var3.addSpecificationLinks(getSpecificationLinks(var0, var1.getTModelInstanceDetails()));
         }

         var3.setValidateURI(false);
      }

      return var3;
   }

   public static ServiceBinding getTargetBinding(RegistryService var0, HostingRedirector var1) throws JAXRException {
      ServiceBinding var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createServiceBinding();
         if (null != var1.getBindingKey()) {
            var2.setKey(getKey(var0, var1.getBindingKey()));
         }
      }

      return var2;
   }

   public static Collection getSpecificationLinks(RegistryService var0, TModelInstanceDetails var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(TModelInstanceInfo var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getSpecificationLink(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static SpecificationLink getSpecificationLink(RegistryService var0, TModelInstanceInfo var1) throws JAXRException {
      SpecificationLink var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createSpecificationLink();
         Concept var4 = (Concept)var3.createObject("Concept");
         if (null != var1.getTModelKey()) {
            Key var5 = var3.createKey(var1.getTModelKey().getKey());
            var4.setKey(var5);
         }

         if (null != var1.getDescriptions()) {
            var4.setDescription(UDDIBridgeMapperUtil.getInternationalStringFromDescriptions(var1.getDescriptions(), var0));
         }

         var2.setSpecificationObject(var4);
         InstanceDetails var6 = var1.getInstanceDetails();
         if (var6 != null) {
            if (null != var6.getDescriptions()) {
               var2.setUsageDescription(UDDIBridgeMapperUtil.getInternationalStringFromDescriptions(var6.getDescriptions(), var0));
            }

            if (null != var6.getInstanceParms()) {
               var2.setUsageParameters(getUsageParameters(var6.getInstanceParms()));
            }

            if (null != var6.getOverviewDoc()) {
               var2.addExternalLink(getExternalLinkFromOverviewDoc(var0, var6.getOverviewDoc()));
            }
         }
      }

      return var2;
   }

   public static Collection getUsageParameters(InstanceParms var0) throws JAXRException {
      Object var1;
      if (var0 == null) {
         var1 = Collections.EMPTY_LIST;
      } else {
         var1 = new ArrayList();
         ((Collection)var1).add(var0.toString());
      }

      return (Collection)var1;
   }

   public static ExternalLink getExternalLinkFromOverviewDoc(RegistryService var0, OverviewDoc var1) throws JAXRException {
      ExternalLink var2 = null;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         if (var1.getOverviewURL() != null) {
            String var4 = var1.getOverviewURL().toString();
            InternationalString var5 = UDDIBridgeMapperUtil.getInternationalStringFromDescriptions(var1.getDescriptions(), var0);
            var2 = var3.createExternalLink(var4, var5);
            String var6 = var4 + ":" + ((RegistryServiceImpl)var0).getSequenceId();
            var2.setKey(new KeyImpl(var6, (RegistryServiceImpl)var0));
            var2.setValidateURI(false);
         }
      }

      return var2;
   }

   public static Collection getExternalLinksFromDiscoveryURLs(RegistryService var0, DiscoveryURLs var1) throws JAXRException {
      Object var2;
      if (var1 == null) {
         var2 = Collections.EMPTY_LIST;
      } else {
         var2 = new ArrayList();

         for(DiscoveryURL var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
            ((Collection)var2).add(getExternalLink(var0, var3));
         }
      }

      return (Collection)var2;
   }

   public static ExternalLink getExternalLink(RegistryService var0, DiscoveryURL var1) throws JAXRException {
      ExternalLinkImpl var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = (ExternalLinkImpl)var3.createObject("ExternalLink");
         if (null != var1.getUrl()) {
            var2.setExternalURI(var1.getUrl(), false);
         }

         if (null != var1.getUseType()) {
            var2.setName(var3.createInternationalString(var1.getUseType()));
         }
      }

      return var2;
   }

   public static InternationalString getInternationalString(RegistryService var0, Names var1) throws JAXRException {
      InternationalString var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createInternationalString();

         for(Name var4 = var1.getFirst(); var4 != null; var4 = var1.getNext()) {
            Locale var5 = null;
            Locale var6 = Locale.getDefault();
            if (null == var4.getLang()) {
               var5 = var6;
            } else if (var6.getLanguage().equals(var4.getLang().toString())) {
               var5 = var6;
            } else {
               var5 = new Locale(var4.getLang().toString(), "");
            }

            LocalizedString var7 = var3.createLocalizedString(var5, var4.getName());
            var2.addLocalizedString(var7);
         }
      }

      return var2;
   }

   public static ExternalIdentifier getExtenalIdentifierFromKeyedReference(RegistryService var0, KeyedReference var1) throws JAXRException {
      ExternalIdentifierImpl var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         ClassificationScheme var4 = (ClassificationScheme)var3.createObject("ClassificationScheme");
         if (null != var1.getTModelKey()) {
            var4.setKey(var3.createKey(var1.getTModelKey().getKey()));
         }

         if (null != var1.getName()) {
            var4.setName(new InternationalStringImpl(var1.getName(), (RegistryServiceImpl)var0));
         }

         var2 = (ExternalIdentifierImpl)var3.createExternalIdentifier(var4, var1.getName(), var1.getValue());
         var2.createAndSetKey((RegistryServiceImpl)var0);
      }

      return var2;
   }

   public static Classification getClassificationFromKeyedReference(RegistryService var0, KeyedReference var1, Key var2) throws JAXRException {
      Classification var3;
      if (var1 == null) {
         var3 = null;
      } else {
         BusinessLifeCycleManager var4 = var0.getBusinessLifeCycleManager();
         ClassificationScheme var5 = (ClassificationScheme)var4.createObject("ClassificationScheme");
         if (null != var1.getTModelKey()) {
            var5.setKey(var4.createKey(var1.getTModelKey().getKey()));
         }

         var3 = var4.createClassification(var5, var1.getName(), var1.getValue());
         String var6 = var2.getId() + ":" + var1.getTModelKey().getKey();
         if (var3.isExternal()) {
            var6 = var6 + ":" + var3.getValue();
         }

         var3.setKey(new KeyImpl(var6, (RegistryServiceImpl)var0));
      }

      return var3;
   }

   public static BulkResponse getBulkResponseForAssociations(RegistryServiceImpl var0, AssertionStatusItems var1) throws JAXRException {
      ArrayList var2 = new ArrayList();

      for(AssertionStatusItem var3 = var1.getFirst(); var3 != null; var3 = var1.getNext()) {
         Association var4 = getAssociation(var0, var3);
         var2.add(var4);
      }

      BulkResponseImpl var5 = getBulkResponseFromCollection(var2, var0);
      return var5;
   }

   public static Association getAssociation(RegistryServiceImpl var0, AssertionStatusItem var1) throws JAXRException {
      Concept var2 = UDDIBridgeMapperUtil.getAssociationTypeFromAssertion(var1.getKeyedReference(), var0);
      AssociationImpl var3 = (AssociationImpl)var0.getBusinessLifeCycleManager().createAssociation((RegistryObject)null, var2);
      String var4 = null;
      if (var1.getCompletionStatus() != null) {
         var4 = var1.getCompletionStatus().getCompletionStatus();
      }

      if ("status:complete".equals(var4)) {
         var3.setConfirmedBySourceOwner(true);
         var3.setConfirmedByTargetOwner(true);
      } else if ("status:fromKey_incomplete".equals(var4)) {
         var3.setConfirmedBySourceOwner(false);
         var3.setConfirmedByTargetOwner(true);
      } else if ("status:toKey_incomplete".equals(var4)) {
         var3.setConfirmedBySourceOwner(true);
         var3.setConfirmedByTargetOwner(false);
      } else {
         var3.setConfirmedBySourceOwner(false);
         var3.setConfirmedByTargetOwner(false);
      }

      return var3;
   }

   static BulkResponseImpl getBulkResponseFromCollection(Collection var0, RegistryServiceImpl var1) throws JAXRException {
      BulkResponseImpl var2 = new BulkResponseImpl(var1);
      var2.setResponse(var0, (Collection)null, false);
      return var2;
   }

   static BulkResponseImpl getBulkResponseFromExceptions(JAXRException var0, RegistryServiceImpl var1) throws JAXRException {
      BulkResponseImpl var2 = new BulkResponseImpl(var1);
      ArrayList var3 = new ArrayList();
      var3.add(var0);
      var2.setResponse(Collections.EMPTY_LIST, var3, false);
      return var2;
   }

   private static boolean isSemanticMatch(Concept var0, String var1, RegistryServiceImpl var2) throws JAXRException {
      String var3 = var2.getSemanticEquivalent(var0.getKey().getId());
      return var3 != null && var3.equals(var1);
   }
}
