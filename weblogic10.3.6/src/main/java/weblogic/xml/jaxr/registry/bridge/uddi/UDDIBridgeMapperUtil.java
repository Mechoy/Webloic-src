package weblogic.xml.jaxr.registry.bridge.uddi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import javax.xml.registry.RegistryService;
import javax.xml.registry.UnsupportedCapabilityException;
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
import javax.xml.registry.infomodel.PostalAddress;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.Slot;
import javax.xml.registry.infomodel.SpecificationLink;
import javax.xml.registry.infomodel.TelephoneNumber;
import javax.xml.registry.infomodel.User;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.InvalidKeyPassedException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UnsupportedException;
import weblogic.auddi.uddi.datastructure.AccessPoint;
import weblogic.auddi.uddi.datastructure.Address;
import weblogic.auddi.uddi.datastructure.AddressLine;
import weblogic.auddi.uddi.datastructure.AddressLines;
import weblogic.auddi.uddi.datastructure.Addresses;
import weblogic.auddi.uddi.datastructure.AssertionStatusItem;
import weblogic.auddi.uddi.datastructure.AssertionStatusItems;
import weblogic.auddi.uddi.datastructure.AuthorizedName;
import weblogic.auddi.uddi.datastructure.BindingKey;
import weblogic.auddi.uddi.datastructure.BindingTemplate;
import weblogic.auddi.uddi.datastructure.BindingTemplates;
import weblogic.auddi.uddi.datastructure.BusinessEntity;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.BusinessService;
import weblogic.auddi.uddi.datastructure.BusinessServices;
import weblogic.auddi.uddi.datastructure.CategoryBag;
import weblogic.auddi.uddi.datastructure.Contact;
import weblogic.auddi.uddi.datastructure.Contacts;
import weblogic.auddi.uddi.datastructure.Description;
import weblogic.auddi.uddi.datastructure.Descriptions;
import weblogic.auddi.uddi.datastructure.DiscoveryURL;
import weblogic.auddi.uddi.datastructure.DiscoveryURLs;
import weblogic.auddi.uddi.datastructure.Email;
import weblogic.auddi.uddi.datastructure.Emails;
import weblogic.auddi.uddi.datastructure.HostingRedirector;
import weblogic.auddi.uddi.datastructure.IdentifierBag;
import weblogic.auddi.uddi.datastructure.InstanceDetails;
import weblogic.auddi.uddi.datastructure.InstanceParms;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.Language;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.Operator;
import weblogic.auddi.uddi.datastructure.OverviewDoc;
import weblogic.auddi.uddi.datastructure.Phone;
import weblogic.auddi.uddi.datastructure.Phones;
import weblogic.auddi.uddi.datastructure.PublisherAssertion;
import weblogic.auddi.uddi.datastructure.SearchNames;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModelBag;
import weblogic.auddi.uddi.datastructure.TModelInstanceDetails;
import weblogic.auddi.uddi.datastructure.TModelInstanceInfo;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.datastructure.UniqueNames;
import weblogic.auddi.uddi.request.inquiry.FindQualifier;
import weblogic.auddi.uddi.request.inquiry.FindQualifiers;
import weblogic.xml.jaxr.registry.BulkResponseImpl;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.infomodel.LocalizedStringImpl;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;
import weblogic.xml.jaxr.registry.util.ClassificationSchemeHelper;

public class UDDIBridgeMapperUtil {
   private static final String[] urlTypeArray = new String[]{"http", "https", "ftp", "phone", "mailto"};
   private static final String DEFAULT_URL_TYPE = "http";
   private static final String[][] ASSOCIATION_TYPE_MAPPING = new String[][]{{"equivalentTo", "identity"}, {"relatedTo", "peer-peer"}, {"hasChild", "parent-child"}};

   private UDDIBridgeMapperUtil() {
   }

   public static JAXRException mapException(UDDIException var0) {
      var0.printStackTrace();
      return (JAXRException)(var0 instanceof UnsupportedException ? new UnsupportedCapabilityException(var0.getMessage()) : new RegistryException(var0.getMessage()));
   }

   public static void accumulateBulkResponses(BulkResponseImpl var0, BulkResponse[] var1) throws JAXRException {
      if (var0.getCollection().size() == 0) {
         var0.setResponse(new ArrayList(), var0.getExceptions(), var0.isPartialResponse());
      }

      for(int var2 = 0; var2 < var1.length; ++var2) {
         BulkResponse var3 = var1[var2];
         var0.getCollection().addAll(var3.getCollection());
         Collection var4 = var3.getExceptions();
         if (var4 != null) {
            if (var0.getExceptions() == null) {
               var0.setResponse(var0.getCollection(), new ArrayList(), var0.isPartialResponse());
            }

            var0.getExceptions().addAll(var4);
         }

         if (var2 == 0) {
            var0.setStatus(var3.getStatus());
         } else {
            updateStatus(var0, var3);
         }
      }

   }

   public static void accumulateBulkResponses(BulkResponseImpl var0, BulkResponse var1) throws JAXRException {
      if (var1 != null) {
         BulkResponse[] var2 = new BulkResponse[]{var1};
         accumulateBulkResponses(var0, var2);
      }

   }

   public static AccessPoint getAccessPoint(String var0, Collection var1) throws JAXRException {
      AccessPoint var2;
      if (var0 == null) {
         var2 = null;
      } else {
         String var3 = "http";
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Classification var5 = (Classification)var4.next();
            Concept var6 = var5.getConcept();
            if (var6 != null) {
               ClassificationScheme var7 = var6.getClassificationScheme();
               if (var7 != null) {
                  InternationalString var8 = var7.getName();
                  if (var8 != null) {
                     String var9 = var8.getValue();
                     if (var9.equalsIgnoreCase("URLType")) {
                        if (isPredefinedURLType(var6.getValue())) {
                           var3 = var6.getValue();
                        } else {
                           var3 = "other";
                        }
                     }
                  }
               }
            }
         }

         try {
            var2 = new AccessPoint(var3, var0);
         } catch (UDDIException var10) {
            throw mapException(var10);
         }
      }

      return var2;
   }

   private static boolean isPredefinedURLType(String var0) {
      for(int var1 = 0; var1 < urlTypeArray.length; ++var1) {
         if (var0.toLowerCase(Locale.ENGLISH).indexOf(urlTypeArray[var1]) >= 0) {
            return true;
         }
      }

      return false;
   }

   public static boolean isConcept(TModel var0) {
      boolean var1 = UDDITaxonomies.isClassificationScheme(var0);
      return !var1;
   }

   public static Address getAddress(PostalAddress var0, RegistryServiceImpl var1) throws JAXRException {
      try {
         Address var2;
         if (var0 == null) {
            var2 = null;
         } else {
            var2 = new Address();
            var2.addAddressLine(getAddressLine(var0.getStreetNumber()));
            var2.addAddressLine(getAddressLine(var0.getStreet()));
            var2.addAddressLine(getAddressLine(var0.getCity()));
            var2.addAddressLine(getAddressLine(var0.getStateOrProvince()));
            var2.addAddressLine(getAddressLine(var0.getPostalCode()));
            var2.addAddressLine(getAddressLine(var0.getCountry()));
            ClassificationScheme var3 = var0.getPostalScheme();
            if (var3 == null) {
               var3 = var1.getDefaultPostalScheme();
            }

            if (var3 != null) {
               var2.setTModelKey(new TModelKey(var3.getKey().getId()));
            }

            setSemanticEquivalences(var2, var3, var1);
            AddressLines var4 = getAddressLines(var0.getSlot("addressLines"));
            if (var4 != null) {
               for(AddressLine var5 = var4.getFirst(); var5 != null; var5 = var4.getNext()) {
                  var2.addAddressLine(var5);
               }
            }

            var2.setUseType(var0.getType());
            var2.setSortCode(getSortCode(var0.getSlot("sortCode")));
         }

         return var2;
      } catch (UDDIException var6) {
         throw mapException(var6);
      }
   }

   private static void setSemanticEquivalences(Address var0, ClassificationScheme var1, RegistryServiceImpl var2) throws JAXRException {
      ClassificationScheme var3 = ClassificationSchemeHelper.getClassificationSchemeByName("PostalAddressAttributes", var2);
      Collection var4 = var3.getChildrenConcepts();
      AddressLine var5 = var0.getAddressLines().getFirst();

      for(Iterator var6 = var4.iterator(); var6.hasNext(); var5 = var0.getAddressLines().getNext()) {
         Concept var7 = (Concept)var6.next();
         String var8 = var2.getSemanticEquivalent(var7.getKey().getId());
         if (var8 != null) {
            boolean var9 = true;
            if (var1 != null) {
               Iterator var10 = var1.getChildrenConcepts().iterator();

               while(var10.hasNext() && var9) {
                  Concept var11 = (Concept)var10.next();
                  Key var12 = var11.getKey();
                  if (var12 != null && var12.getId().equalsIgnoreCase(var8)) {
                     InternationalString var13 = var11.getName();
                     if (var13 != null) {
                        var5.setKeyName(var13.getValue());
                     }

                     var5.setKeyValue(var11.getValue());
                     var9 = false;
                  }
               }
            }

            if (var9) {
               InternationalString var14 = var7.getName();
               if (var14 != null) {
                  var5.setKeyName(var14.getValue());
               }

               var5.setKeyValue(var7.getValue());
            }
         }
      }

   }

   public static Addresses getAddresses(Collection var0, RegistryServiceImpl var1) throws JAXRException {
      Addresses var2;
      if (var0 != null && var0.size() != 0) {
         var2 = new Addresses();

         try {
            Iterator var3 = var0.iterator();

            while(var3.hasNext()) {
               PostalAddress var4 = (PostalAddress)var3.next();
               var2.add(getAddress(var4, var1));
            }
         } catch (UDDIException var5) {
            throw mapException(var5);
         }
      } else {
         var2 = null;
      }

      return var2;
   }

   public static AddressLine getAddressLine(String var0) {
      AddressLine var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new AddressLine(var0);
      }

      return var1;
   }

   public static AddressLines getAddressLines(Slot var0) throws JAXRException {
      AddressLines var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new AddressLines();

         try {
            Collection var2 = var0.getValues();
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               var1.add(getAddressLine(var4));
            }
         } catch (UDDIException var5) {
            throw mapException(var5);
         }
      }

      return var1;
   }

   public static AuthorizedName getAuthorizedName(Slot var0) throws JAXRException {
      AuthorizedName var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new AuthorizedName(getSlotValue(var0));
      }

      return var1;
   }

   public static BindingKey getBindingKey(Key var0) throws JAXRException {
      BindingKey var1;
      if (var0 == null) {
         var1 = null;
      } else {
         try {
            var1 = new BindingKey(var0.getId());
         } catch (InvalidKeyPassedException var3) {
            throw mapException(var3);
         }
      }

      return var1;
   }

   public static BindingTemplate getBindingTemplate(ServiceBinding var0) throws JAXRException {
      BindingTemplate var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new BindingTemplate();

         try {
            var1.setBindingKey(getBindingKey(var0.getKey()));
            if (var0.getService() != null) {
               var1.setServiceKey(getServiceKey(var0.getService().getKey()));
            }

            var1.setDescriptions(getDescriptions(var0.getDescription()));
            if (var0.getAccessURI() != null) {
               var1.setAccessPoint(getAccessPoint(var0.getAccessURI(), var0.getClassifications()));
            } else {
               var1.setHostingRedirector(getHostingRedirector(var0.getTargetBinding()));
            }

            var1.setTModelInstanceDetails(getTModelInstanceDetails(var0.getSpecificationLinks()));
         } catch (UDDIException var3) {
            throw mapException(var3);
         }
      }

      return var1;
   }

   public static BindingTemplates getBindingTemplates(Collection var0) throws JAXRException {
      BindingTemplates var1;
      if (var0 != null && var0.size() != 0) {
         var1 = new BindingTemplates();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            ServiceBinding var3 = (ServiceBinding)var2.next();
            var1.add(getBindingTemplate(var3));
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public static BusinessEntity getBusinessEntity(Organization var0, RegistryServiceImpl var1) throws JAXRException {
      BusinessEntity var2;
      if (var0 == null) {
         var2 = null;
      } else {
         var2 = new BusinessEntity();
         var2.setBusinessKey(getBusinessKey(var0.getKey()));
         var2.setOperator(getOperatorAsString(var0.getSlot("operator")));
         var2.setDiscoveryURLs(getDiscoveryURLs(var0.getExternalLinks()));
         var2.setNames(getNames(var0.getName()));
         var2.setDescriptions(getDescriptions(var0.getDescription()));
         var2.setContacts(getContacts(var0.getUsers(), var1));
         var2.setBusinessServices(getBusinessServices(var0.getServices()));
         var2.setIdentifierBag(getIdentifierBag(var0.getExternalIdentifiers()));
         var2.setCategoryBag(getCategoryBagFromClassifications(var0.getClassifications()));
      }

      return var2;
   }

   public static BusinessKey getBusinessKey(Key var0) throws JAXRException {
      BusinessKey var1 = null;
      if (var0 == null) {
         var1 = null;
      } else {
         try {
            var1 = new BusinessKey(var0.getId());
         } catch (InvalidKeyPassedException var3) {
            throw mapException(var3);
         }
      }

      return var1;
   }

   public static BusinessService getBusinessService(Service var0) throws JAXRException {
      BusinessService var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new BusinessService();
         var1.setBusinessKey(getBusinessKey(var0.getProvidingOrganization().getKey()));
         var1.setServiceKey(getServiceKey(var0.getKey()));
         var1.setNames(getNames(var0.getName()));
         var1.setDescriptions(getDescriptions(var0.getDescription()));
         var1.setBindingTemplates(getBindingTemplates(var0.getServiceBindings()));
         var1.setCategoryBag(getCategoryBagFromClassifications(var0.getClassifications()));
      }

      return var1;
   }

   public static BusinessServices getBusinessServices(Collection var0) throws JAXRException {
      BusinessServices var1;
      if (var0 != null && var0.size() != 0) {
         var1 = new BusinessServices();

         try {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               Service var3 = (Service)var2.next();
               var1.add(getBusinessService(var3));
            }
         } catch (UDDIException var4) {
            mapException(var4);
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public static CategoryBag getCategoryBagFromClassifications(Collection var0) throws JAXRException {
      CategoryBag var1;
      if (var0 != null && var0.size() != 0) {
         var1 = new CategoryBag();

         try {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               Classification var3 = (Classification)var2.next();
               Concept var4 = var3.getConcept();
               if (var4 != null) {
                  var1.add(getKeyedReference(var4));
               } else {
                  var1.add(getKeyedReference(var3));
               }
            }
         } catch (UDDIException var5) {
            mapException(var5);
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public static Contact getContact(User var0, RegistryServiceImpl var1) throws JAXRException {
      Contact var2;
      if (var0 == null) {
         var2 = null;
      } else {
         var2 = new Contact();
         var2.setUseType(var0.getType());
         var2.setDescriptions(getDescriptions(var0.getDescription()));
         var2.setPersonName(var0.getPersonName().getFullName());
         var2.setPhones(getPhones(var0.getTelephoneNumbers((String)null)));
         var2.setEmails(getEmails(var0.getEmailAddresses()));
         var2.setAddresses(getAddresses(var0.getPostalAddresses(), var1));
      }

      return var2;
   }

   public static Phones getPhones(Collection var0) throws JAXRException {
      Phones var1;
      if (var0 != null && var0.size() != 0) {
         var1 = new Phones();

         try {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               TelephoneNumber var3 = (TelephoneNumber)var2.next();
               var1.add(getPhone(var3));
            }
         } catch (UDDIException var4) {
            throw mapException(var4);
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public static Phone getPhone(TelephoneNumber var0) throws JAXRException {
      Phone var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new Phone(var0.getNumber(), var0.getType());
      }

      return var1;
   }

   public static Emails getEmails(Collection var0) throws JAXRException {
      Emails var1;
      if (var0 != null && var0.size() != 0) {
         var1 = new Emails();

         try {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               EmailAddress var3 = (EmailAddress)var2.next();
               var1.add(getEmail(var3));
            }
         } catch (UDDIException var4) {
            throw mapException(var4);
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public static Email getEmail(EmailAddress var0) throws JAXRException {
      Email var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new Email(var0.getAddress(), var0.getType());
      }

      return var1;
   }

   public static String getSortCode(Slot var0) throws JAXRException {
      String var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = "";
         Collection var2 = var0.getValues();

         for(Iterator var3 = var2.iterator(); var3.hasNext(); var1 = (String)var3.next()) {
         }
      }

      return var1;
   }

   public static ServiceKey getServiceKey(Key var0) throws JAXRException {
      ServiceKey var1;
      if (var0 == null) {
         var1 = null;
      } else {
         try {
            var1 = new ServiceKey(var0.getId());
         } catch (InvalidKeyPassedException var3) {
            throw mapException(var3);
         }
      }

      return var1;
   }

   public static Contacts getContacts(Collection var0, RegistryServiceImpl var1) throws JAXRException {
      Contacts var2;
      if (var0 != null && var0.size() != 0) {
         var2 = new Contacts();

         try {
            Iterator var3 = var0.iterator();

            while(var3.hasNext()) {
               User var4 = (User)var3.next();
               var2.add(getContact(var4, var1));
            }
         } catch (UDDIException var5) {
            throw mapException(var5);
         }
      } else {
         var2 = null;
      }

      return var2;
   }

   public static TModel getTModelFromConcept(Concept var0) throws JAXRException {
      TModel var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new TModel();
         var1.setAuthorizedName(getAuthorizedName(var0.getSlot("authorizedName")));
         CategoryBag var2 = getCategoryBagFromClassifications(var0.getClassifications());
         var2 = addCategorization(var2, "Concept");
         var1.setCategoryBag(var2);
         var1.setDescriptions(getDescriptions(var0.getDescription()));
         var1.setIdentifierBag(getIdentifierBag(var0.getExternalIdentifiers()));
         var1.setName(getName(var0.getName()));
         var1.setOperator(getOperator(var0.getSlot("operator")));
         var1.setOverviewDoc(getOverviewDoc(var0.getExternalLinks()));
         var1.setTModelKey(getTModelKey(var0.getKey()));
      }

      return var1;
   }

   public static TModel getTModelFromClassificationScheme(ClassificationScheme var0) throws JAXRException {
      TModel var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new TModel();
         var1.setAuthorizedName(getAuthorizedName(var0.getSlot("authorizedName")));
         CategoryBag var2 = getCategoryBagFromClassifications(var0.getClassifications());
         var2 = addCategorization(var2, "ClassificationScheme");
         var1.setCategoryBag(var2);
         var1.setDescriptions(getDescriptions(var0.getDescription()));
         var1.setIdentifierBag(getIdentifierBag(var0.getExternalIdentifiers()));
         var1.setName(getName(var0.getName()));
         var1.setOperator(getOperator(var0.getSlot("operator")));
         var1.setOverviewDoc(getOverviewDoc(var0.getExternalLinks()));
         var1.setTModelKey(getTModelKey(var0.getKey()));
      }

      return var1;
   }

   public static Descriptions getDescriptions(InternationalString var0) throws JAXRException {
      Descriptions var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new Descriptions();

         try {
            Iterator var2 = var0.getLocalizedStrings().iterator();

            while(var2.hasNext()) {
               LocalizedString var3 = (LocalizedString)var2.next();
               Locale var4 = var3.getLocale();
               Description var5 = new Description(var3.getValue(), new Language(var4.getLanguage()));
               var1.add(var5);
            }
         } catch (UDDIException var6) {
            throw mapException(var6);
         }
      }

      return var1;
   }

   public static DiscoveryURL getDiscoveryURL(ExternalLink var0) throws JAXRException {
      DiscoveryURL var1;
      if (var0 == null) {
         var1 = null;
      } else {
         try {
            String var2 = var0.getName().getValue();
            var1 = new DiscoveryURL(var2 == null ? "businessEntity" : var2, var0.getExternalURI());
         } catch (UDDIException var3) {
            throw mapException(var3);
         }
      }

      return var1;
   }

   public static UniqueNames getNames(InternationalString var0) throws JAXRException {
      UniqueNames var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new UniqueNames();

         try {
            Iterator var2 = var0.getLocalizedStrings().iterator();

            while(var2.hasNext()) {
               LocalizedString var3 = (LocalizedString)var2.next();
               Locale var4 = var3.getLocale();
               Name var5 = new Name(var3.getValue(), new Language(var4.getLanguage()));
               var1.add(var5);
            }
         } catch (UDDIException var6) {
            throw mapException(var6);
         }
      }

      return var1;
   }

   public static SearchNames getSearchNames(Collection var0) throws JAXRException {
      SearchNames var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new SearchNames();

         Name var4;
         try {
            for(Iterator var2 = var0.iterator(); var2.hasNext(); var1.add(var4)) {
               Object var3 = var2.next();
               var4 = null;
               if (var3 instanceof String) {
                  String var5 = (String)var3;
                  var4 = new Name(var5);
               } else {
                  LocalizedString var8 = (LocalizedString)var3;
                  Locale var6 = var8.getLocale();
                  var4 = new Name(var8.getValue(), new Language(var6.getLanguage()));
               }
            }
         } catch (UDDIException var7) {
            throw mapException(var7);
         }
      }

      return var1;
   }

   public static Name getName(InternationalString var0) throws JAXRException {
      Name var1;
      if (var0 == null) {
         var1 = null;
      } else {
         try {
            var1 = new Name(var0.getValue());
         } catch (UDDIException var3) {
            throw mapException(var3);
         }
      }

      return var1;
   }

   public static DiscoveryURLs getDiscoveryURLs(Collection var0) throws JAXRException {
      DiscoveryURLs var1;
      if (var0 != null && var0.size() != 0) {
         try {
            var1 = new DiscoveryURLs();
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               ExternalLink var3 = (ExternalLink)var2.next();
               var1.add(getDiscoveryURL(var3));
            }
         } catch (UDDIException var4) {
            throw mapException(var4);
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public static HostingRedirector getHostingRedirector(ServiceBinding var0) throws JAXRException {
      HostingRedirector var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new HostingRedirector(getBindingKey(var0.getKey()));
      }

      return var1;
   }

   public static TModelInstanceDetails getTModelInstanceDetails(Collection var0) throws JAXRException {
      TModelInstanceDetails var1;
      if (var0 != null && var0.size() != 0) {
         var1 = new TModelInstanceDetails();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            SpecificationLink var3 = (SpecificationLink)var2.next();
            var1.add(getTModelInstanceInfo(var3));
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public static TModelInstanceInfo getTModelInstanceInfo(SpecificationLink var0) throws JAXRException {
      TModelInstanceInfo var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new TModelInstanceInfo();
         var1.setTModelKey(getTModelKey(var0.getSpecificationObject()));
         var1.setDescriptions(getDescriptions(var0.getUsageDescription()));
         var1.setInstanceDetails(getInstanceDetails(var0));
      }

      return var1;
   }

   public static TModelKey getTModelKey(RegistryObject var0) throws JAXRException {
      TModelKey var1;
      if (var0 == null) {
         var1 = null;
      } else {
         try {
            var1 = new TModelKey(var0.getKey().getId());
         } catch (InvalidKeyPassedException var3) {
            throw mapException(var3);
         } catch (FatalErrorException var4) {
            throw mapException(var4);
         }
      }

      return var1;
   }

   public static TModelKey getTModelKey(Key var0) throws JAXRException {
      TModelKey var1;
      if (var0 == null) {
         var1 = null;
      } else {
         try {
            var1 = new TModelKey(var0.getId());
         } catch (InvalidKeyPassedException var3) {
            throw mapException(var3);
         } catch (FatalErrorException var4) {
            throw mapException(var4);
         }
      }

      return var1;
   }

   public static InstanceDetails getInstanceDetails(SpecificationLink var0) throws JAXRException {
      InstanceDetails var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new InstanceDetails();
         var1.setInstanceParms(getInstanceParms(var0.getUsageParameters()));
         var1.setOverviewDoc(getOverviewDoc(var0.getExternalLinks()));
      }

      return var1;
   }

   public static InstanceParms getInstanceParms(Collection var0) throws JAXRException {
      InstanceParms var1;
      if (var0 != null && var0.size() != 0) {
         try {
            var1 = null;
            Iterator var2 = var0.iterator();
            var1 = new InstanceParms((String)var2.next());
            if (var2.hasNext()) {
               String var3 = JAXRMessages.getMessage("jaxr.uddi.uddiBridgeMapperUtil.invalidUsageParametersNember");
               throw new InvalidRequestException(var3);
            }
         } catch (UDDIException var4) {
            throw mapException(var4);
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public static IdentifierBag getIdentifierBag(Collection var0) throws JAXRException {
      IdentifierBag var1;
      if (var0 != null && var0.size() != 0) {
         var1 = new IdentifierBag();

         try {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               ExternalIdentifier var3 = (ExternalIdentifier)var2.next();
               var1.add(getKeyedReference(var3));
            }
         } catch (UDDIException var4) {
            mapException(var4);
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   private static String getOperatorAsString(Slot var0) throws JAXRException {
      String var1;
      if (var0 == null) {
         var1 = null;
      } else {
         Operator var2 = getOperator(var0);
         if (var2 == null) {
            var1 = null;
         } else {
            var1 = var2.getName();
         }
      }

      return var1;
   }

   private static String getSlotValue(Slot var0) throws JAXRException {
      String var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = "";

         for(Iterator var2 = var0.getValues().iterator(); var2.hasNext(); var1 = (String)var2.next()) {
         }
      }

      return var1;
   }

   public static Operator getOperator(Slot var0) throws JAXRException {
      Operator var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new Operator(getSlotValue(var0));
      }

      return var1;
   }

   public static OverviewDoc getOverviewDoc(Collection var0) throws JAXRException {
      OverviewDoc var1;
      if (var0 != null && var0.size() != 0) {
         var1 = new OverviewDoc();

         try {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               ExternalLink var3 = (ExternalLink)var2.next();
               var1.setDescriptions(getDescriptions(var3.getDescription()));
               var1.setOverviewURL(var3.getExternalURI());
            }
         } catch (UDDIException var4) {
            mapException(var4);
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public static KeyedReference getKeyedReference(Concept var0) throws JAXRException {
      if (var0 == null) {
         throw new JAXRException();
      } else {
         try {
            ClassificationScheme var2 = var0.getClassificationScheme();
            if (var2 == null) {
               throw new JAXRException();
            } else {
               Key var3 = var2.getKey();
               if (var3 == null) {
                  throw new JAXRException();
               } else {
                  TModelKey var4 = new TModelKey(var3.getId());
                  String var5 = null;
                  if (var0.getName() != null) {
                     var5 = var0.getName().getValue();
                  }

                  String var6 = var0.getValue();
                  if (var6 == null) {
                     return null;
                  } else {
                     KeyedReference var1 = new KeyedReference(var4, var5, var6);
                     return var1;
                  }
               }
            }
         } catch (UDDIException var7) {
            throw mapException(var7);
         }
      }
   }

   public static KeyedReference getKeyedReference(Classification var0) throws JAXRException {
      if (var0 == null) {
         throw new JAXRException();
      } else {
         try {
            ClassificationScheme var2 = var0.getClassificationScheme();
            if (var2 == null) {
               throw new JAXRException();
            } else {
               Key var3 = var2.getKey();
               if (var3 == null) {
                  throw new JAXRException();
               } else {
                  TModelKey var4 = new TModelKey(var3.getId());
                  String var5 = null;
                  if (var0.getName() != null) {
                     var5 = var0.getName().getValue();
                  }

                  String var6 = var0.getValue();
                  if (var6 == null) {
                     return null;
                  } else {
                     KeyedReference var1 = new KeyedReference(var4, var5, var6);
                     return var1;
                  }
               }
            }
         } catch (UDDIException var7) {
            throw mapException(var7);
         }
      }
   }

   public static KeyedReference getKeyedReference(ExternalIdentifier var0) throws JAXRException {
      if (var0 == null) {
         return null;
      } else {
         try {
            ClassificationScheme var2 = var0.getIdentificationScheme();
            if (var2 == null) {
               throw new JAXRException();
            } else {
               Key var3 = var2.getKey();
               if (var3 == null) {
                  throw new JAXRException();
               } else {
                  TModelKey var4 = new TModelKey(var3.getId());
                  String var5 = null;
                  if (var0.getName() != null) {
                     var5 = var0.getName().getValue();
                  }

                  String var6 = var0.getValue();
                  if (var6 == null) {
                     return null;
                  } else {
                     KeyedReference var1 = new KeyedReference(var4, var5, var6);
                     return var1;
                  }
               }
            }
         } catch (UDDIException var7) {
            throw mapException(var7);
         }
      }
   }

   public static FindQualifiers getFindQualifiers(Collection var0) throws JAXRException {
      FindQualifiers var1 = new FindQualifiers();

      try {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            FindQualifier var3 = new FindQualifier((String)var2.next());
            var1.add(var3);
         }

         return var1;
      } catch (UDDIException var4) {
         throw mapException(var4);
      }
   }

   public static TModelBag getTModelBagFromSpecifications(Collection var0) throws JAXRException {
      TModelBag var1 = new TModelBag();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         RegistryObject var3 = (RegistryObject)var2.next();
         var1.add(getTModelKey(var3.getKey()));
      }

      return var1;
   }

   public static Collection getKeyedReferencesFromAssociationTypes(Collection var0, RegistryServiceImpl var1) throws JAXRException {
      ArrayList var2 = new ArrayList();
      if (var0 != null) {
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            KeyedReference var5;
            if (var4 instanceof String) {
               var5 = getKeyedReferenceFromAssociationType((String)var4, var1);
            } else {
               var5 = getKeyedReferenceFromAssociationType((Concept)var4);
            }

            if (var5 != null) {
               var2.add(var5);
            }
         }
      }

      return var2;
   }

   public static KeyedReference getKeyedReferenceFromAssociationType(String var0, RegistryServiceImpl var1) throws JAXRException {
      Concept var2 = var1.getBusinessLifeCycleManager().createConcept((RegistryObject)null, var0, var0);
      KeyedReference var3 = getKeyedReferenceFromAssociationType(var2);
      return var3;
   }

   public static KeyedReference getKeyedReferenceFromAssociationType(Concept var0) throws JAXRException {
      try {
         TModelKey var1 = new TModelKey("UUID:807A2C6A-EE22-470D-ADC7-E0424A337C03");
         String var2 = mapAssertionTypeString(false, var0.getValue());
         String var3 = mapAssertionTypeString(false, var0.getName().getValue());
         KeyedReference var4 = new KeyedReference(var1, var3, var2);
         return var4;
      } catch (UDDIException var5) {
         throw mapException(var5);
      }
   }

   public static PublisherAssertion getPublisherAssertionFromAssociationKey(Key var0, RegistryServiceImpl var1) throws JAXRException {
      PublisherAssertion var2 = new PublisherAssertion();
      if (var0 != null) {
         String var3 = var0.getId();
         if (var3 != null) {
            StringTokenizer var4 = new StringTokenizer(var3, ":", false);
            String var5;
            if (var4.countTokens() != 3) {
               var5 = JAXRMessages.getMessage("jaxr.uddi.uddiBridgeMapperUtil.invalidFormatForAssociationKey");
               throw new JAXRException(var5);
            }

            var5 = var4.nextToken();
            String var6 = var4.nextToken();
            String var7 = var4.nextToken();

            try {
               var2.setFromKey(new BusinessKey(var5));
               var2.setToKey(new BusinessKey(var6));
            } catch (InvalidKeyPassedException var9) {
               throw new JAXRException(var9);
            }

            KeyedReference var8 = getKeyedReferenceFromAssociationType(var7, var1);
            if (var8 != null) {
               var2.setKeyedReference(var8);
            }
         }
      }

      return var2;
   }

   public static Collection getPublisherAssertionsFromAssociations(Collection var0) throws JAXRException {
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         try {
            while(var2.hasNext()) {
               Association var3 = (Association)var2.next();
               PublisherAssertion var4 = getPublisherAssertionFromAssociation(var3);
               if (var4 != null) {
                  var1.add(var4);
               }
            }
         } catch (ClassCastException var5) {
            throw new JAXRException(var5);
         }
      }

      return var1;
   }

   public static PublisherAssertion getPublisherAssertionFromAssociation(Association var0) throws JAXRException {
      Concept var1 = var0.getAssociationType();
      Organization var2 = null;
      Organization var3 = null;

      try {
         var2 = (Organization)var0.getSourceObject();
         var3 = (Organization)var0.getTargetObject();
         PublisherAssertion var4 = new PublisherAssertion();
         Key var5 = var2.getKey();
         String var6;
         if (var5 != null) {
            var6 = var5.getId();
            String var7;
            if (var6 != null) {
               var4.setFromKey(new BusinessKey(var6));
               Key var11 = var3.getKey();
               if (var11 != null) {
                  var7 = var11.getId();
                  if (var7 != null) {
                     var4.setToKey(new BusinessKey(var7));
                     if (var1 != null) {
                        KeyedReference var12 = getKeyedReferenceFromAssociationType(var1);
                        if (var12 != null) {
                           var4.setKeyedReference(var12);
                        }

                        return var4;
                     } else {
                        var7 = JAXRMessages.getMessage("jaxr.uddi.uddiBridgeMapperUtil.missingAssociationType");
                        throw new JAXRException(var7);
                     }
                  } else {
                     String var8 = JAXRMessages.getMessage("jaxr.uddi.uddiBridgeMapperUtil.invalidId", new Object[]{"targetKeyId"});
                     throw new JAXRException(var8);
                  }
               } else {
                  var7 = JAXRMessages.getMessage("jaxr.uddi.uddiBridgeMapperUtil.invalidKey", new Object[]{"targetKey"});
                  throw new JAXRException(var7);
               }
            } else {
               var7 = JAXRMessages.getMessage("jaxr.uddi.uddiBridgeMapperUtil.invalidId", new Object[]{"sourceKeyId"});
               throw new JAXRException(var7);
            }
         } else {
            var6 = JAXRMessages.getMessage("jaxr.uddi.uddiBridgeMapperUtil.invalidKey", new Object[]{"sourceKey"});
            throw new JAXRException(var6);
         }
      } catch (ClassCastException var9) {
         throw new JAXRException(var9);
      } catch (InvalidKeyPassedException var10) {
         throw new JAXRException(var10);
      }
   }

   public static Concept getConceptByValue(Collection var0, String var1) throws JAXRException {
      Concept var2 = null;
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Concept var4 = (Concept)var3.next();
         String var5 = var4.getValue();
         if (var5 != null && var5.equalsIgnoreCase(var1)) {
            var2 = var4;
         }
      }

      return var2;
   }

   public static Collection getLocalizedStringsFromDescriptions(Descriptions var0, RegistryService var1) throws JAXRException {
      ArrayList var2 = new ArrayList();

      for(Description var4 = var0.getFirst(); var4 != null; var4 = var0.getNext()) {
         LocalizedStringImpl var3 = new LocalizedStringImpl((RegistryServiceImpl)var1);
         String var5 = var4.getText();
         String var6 = var4.getLang().getLang();
         if (var5 != null) {
            var3.setValue(var5);
         }

         if (var6 != null) {
            Locale var7 = Locale.getDefault();
            String var8 = var7.getLanguage();
            if (var6.equalsIgnoreCase(var8)) {
               var3.setLocale(var7);
            } else {
               Locale var9 = new Locale(var6, "");
               var3.setLocale(var9);
            }
         } else {
            var3.setLocale(Locale.getDefault());
         }

         var2.add(var3);
      }

      return var2;
   }

   public static InternationalString getInternationalStringFromDescriptions(Descriptions var0, RegistryService var1) throws JAXRException {
      InternationalString var2;
      if (var0 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var1.getBusinessLifeCycleManager();
         var2 = var3.createInternationalString();
         var2.addLocalizedStrings(getLocalizedStringsFromDescriptions(var0, var1));
      }

      return var2;
   }

   public static AssertionStatusItems selectAssertionStatusItems(AssertionStatusItems var0, Boolean var1, Boolean var2, Collection var3) throws JAXRException, UDDIException {
      AssertionStatusItems var4 = new AssertionStatusItems();

      for(AssertionStatusItem var5 = var0.getFirst(); var5 != null; var5 = var0.getNext()) {
         boolean var6 = true;
         boolean var7;
         if (var1 != null) {
            if (var5.getFromKeyOwnership()) {
               var7 = var5.isFromKeyAsserted();
            } else {
               var7 = var5.isToKeyAsserted();
            }

            if (var1 != var7) {
               var6 = false;
            }
         }

         if (var6 && var2 != null) {
            if (var5.getToKeyOwnership()) {
               var7 = var5.isFromKeyAsserted();
            } else {
               var7 = var5.isToKeyAsserted();
            }

            if (var2 != var7) {
               var6 = false;
            }
         }

         if (var6 && var3 != null && var3.size() > 0) {
            var6 = false;
            String var10 = var5.getKeyedReference().getValue();
            Iterator var8 = var3.iterator();

            while(var8.hasNext() && !var6) {
               Concept var9 = (Concept)var8.next();
               if (var10.equals(var9.getValue())) {
                  var6 = true;
               }
            }
         }

         if (var6) {
            var4.add(var5);
         }
      }

      return var4;
   }

   public static Concept getAssociationTypeFromAssertion(KeyedReference var0, RegistryServiceImpl var1) throws JAXRException {
      BusinessLifeCycleManager var2 = var1.getBusinessLifeCycleManager();
      String var3 = mapAssertionTypeString(true, var0.getValue());
      String var4 = mapAssertionTypeString(true, var0.getName());
      Concept var5 = var2.createConcept((RegistryObject)null, var4, var3);
      if (var0.getTModelKey() != null) {
         Key var6 = var2.createKey(var0.getTModelKey().getKey());
         var5.setKey(var6);
      }

      return var5;
   }

   private static String mapAssertionTypeString(boolean var0, String var1) {
      String var2 = var1;
      byte var3;
      byte var4;
      if (var0) {
         var3 = 1;
         var4 = 0;
      } else {
         var3 = 0;
         var4 = 1;
      }

      for(int var5 = 0; var5 < ASSOCIATION_TYPE_MAPPING.length; ++var5) {
         String[] var6 = ASSOCIATION_TYPE_MAPPING[var5];
         if (var6[var3].equals(var1)) {
            var2 = var6[var4];
         }
      }

      return var2;
   }

   private static CategoryBag addCategorization(CategoryBag var0, String var1) throws JAXRException {
      boolean var2 = false;
      KeyedReference var3;
      if (var0 == null) {
         var0 = new CategoryBag();
      } else {
         for(var3 = var0.getFirst(); var3 != null; var3 = var0.getNext()) {
            if (var3.getTModelKey().equals("uuid:c1acf26d-9672-4404-9d70-39b756e62ab4")) {
               var2 = true;
               break;
            }
         }
      }

      if (!var2) {
         try {
            String var4 = "uddi-org:types";
            TModelKey var5 = new TModelKey("uuid:c1acf26d-9672-4404-9d70-39b756e62ab4");
            String var6;
            if (var1.equals("ClassificationScheme")) {
               var6 = "categorization";
            } else {
               var6 = "specification";
            }

            var3 = new KeyedReference(var5, var4, var6);
            var0.add(var3);
         } catch (UDDIException var7) {
            throw mapException(var7);
         }
      }

      return var0;
   }

   private static void updateStatus(BulkResponseImpl var0, BulkResponse var1) throws JAXRException {
      int var2 = var0.getStatus();
      int var3 = var1.getStatus();
      switch (var2) {
         case 0:
            if (var3 == 0) {
               var0.setStatus(0);
            } else if (var3 == 2) {
               var0.setStatus(1);
            } else if (var3 == 1) {
               var0.setStatus(1);
            } else if (var3 == 3) {
               var0.setStatus(3);
            }
            break;
         case 1:
            if (var3 == 0) {
               var0.setStatus(1);
            } else if (var3 == 2) {
               var0.setStatus(1);
            } else if (var3 == 1) {
               var0.setStatus(1);
            } else if (var3 == 3) {
               var0.setStatus(3);
            }
            break;
         case 2:
            if (var3 == 0) {
               var0.setStatus(1);
            } else if (var3 == 2) {
               var0.setStatus(2);
            } else if (var3 == 1) {
               var0.setStatus(1);
            } else if (var3 == 3) {
               var0.setStatus(3);
            }
            break;
         case 3:
            var0.setStatus(3);
            break;
         default:
            String var4 = JAXRMessages.getMessage("jaxr.registry.bridge.uddi.UDDIBridgeMapperUtil.unknownStatus", new Object[]{(new Integer(var2)).toString()});
            throw new JAXRException(var4);
      }

   }
}
