package weblogic.xml.jaxr.registry.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.Key;
import weblogic.xml.jaxr.registry.BaseJAXRObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;

public class ClassificationSchemeHelper extends BaseJAXRObject {
   private static Map s_jaxrSchemes;
   public static final String OBJECT_TYPE = "ObjectType";
   public static final String PHONE_TYPE = "PhoneType";
   public static final String ASSOCIATION_TYPE = "AssociationType";
   public static final String URL_TYPE = "URLType";
   public static final String POSTAL_ADDRESS_ATTR = "PostalAddressAttributes";

   private ClassificationSchemeHelper() {
   }

   public static ClassificationScheme getClassificationSchemeByName(String var0, RegistryServiceImpl var1) throws JAXRException {
      ClassificationScheme var2 = getJAXRClassificationScheme(var0, var1);
      if (var2 == null) {
         var2 = var1.getRegistryProxy().getInternalClassificationSchemes().getClassificationSchemeByName(var0, var1);
      }

      return var2;
   }

   public static void validateConcept(Concept var0, RegistryServiceImpl var1) throws JAXRException {
      if (var0 != null && var0.getClassificationScheme() != null) {
         String var2 = var0.getClassificationScheme().getName().getValue();
         ClassificationScheme var3 = getJAXRClassificationScheme(var2, var1);
         if (var3 != null) {
            boolean var4 = false;
            String var5 = var0.getValue();
            Iterator var6 = var3.getDescendantConcepts().iterator();

            while(var6.hasNext() && !var4) {
               Concept var7 = (Concept)var6.next();
               if (var5.equals(var7.getValue())) {
                  var4 = true;
               }
            }

            if (!var4) {
               Object[] var9 = new Object[]{var0.getValue(), var2};
               String var10 = "jaxr.validation.scheme.invalidValue";
               String var8 = JAXRMessages.getMessage(var10, var9);
               throw new InvalidRequestException(var8);
            }
         } else {
            var1.getRegistryProxy().getInternalClassificationSchemes().validateConcept(var0, var1);
         }
      }

   }

   private static ClassificationScheme getJAXRClassificationScheme(String var0, RegistryServiceImpl var1) throws JAXRException {
      if (s_jaxrSchemes == null) {
         loadSchemes(var1);
      }

      ClassificationScheme var2 = (ClassificationScheme)s_jaxrSchemes.get(var0);
      return var2;
   }

   private static void loadSchemes(RegistryServiceImpl var0) throws JAXRException {
      s_jaxrSchemes = new HashMap();
      getAssociationTypeScheme(var0);
      getObjectTypeScheme(var0);
      getPhoneTypeScheme(var0);
      getPostalAddressAttributesScheme(var0);
      getURLTypeScheme(var0);
   }

   private static ClassificationScheme getObjectTypeScheme(RegistryServiceImpl var0) throws JAXRException {
      ClassificationScheme var1 = (ClassificationScheme)s_jaxrSchemes.get("ObjectType");
      if (var1 == null) {
         String[] var2 = new String[]{"ExternalLink", "Package", "ExternalId", "Association", "Classification", "Concept", "AuditableEvent", "User", "Organization", "CPA", "CPP", "Service", "ServiceBinding", "Process", "WSDL", "ExtrinsicObject", "Organization", "User", "ExternalIdentifier"};
         var1 = createSchemeWithConcepts("ObjectType", var2, var0);
         s_jaxrSchemes.put(var1.getName().getValue(), var1);
      }

      return var1;
   }

   private static ClassificationScheme getPhoneTypeScheme(RegistryServiceImpl var0) throws JAXRException {
      ClassificationScheme var1 = (ClassificationScheme)s_jaxrSchemes.get("PhoneType");
      if (var1 == null) {
         String[] var2 = new String[]{"OfficePhone", "HomePhone", "MobilePhone", "Beeper", "FAX"};
         var1 = createSchemeWithConcepts("PhoneType", var2, var0);
         s_jaxrSchemes.put(var1.getName().getValue(), var1);
      }

      return var1;
   }

   private static ClassificationScheme getAssociationTypeScheme(RegistryServiceImpl var0) throws JAXRException {
      ClassificationScheme var1 = (ClassificationScheme)s_jaxrSchemes.get("AssociationType");
      if (var1 == null) {
         String[] var2 = new String[]{"RelatedTo", "ExternallyLinks", "Contains", "Extends", "Implements", "InstanceOf", "Supersedes", "Uses", "HasMember", "EquivalentTo", "HasChild", "HasParent", "Replaces", "ResponsibleFor", "SubmitterOf"};
         var1 = createSchemeWithConcepts("AssociationType", var2, var0);
         s_jaxrSchemes.put(var1.getName().getValue(), var1);
      }

      return var1;
   }

   private static ClassificationScheme getURLTypeScheme(RegistryServiceImpl var0) throws JAXRException {
      ClassificationScheme var1 = (ClassificationScheme)s_jaxrSchemes.get("URLType");
      if (var1 == null) {
         String[] var2 = new String[]{"HTTP", "HTTPS", "SMTP", "FAX", "PHONE", "OTHER"};
         var1 = createSchemeWithConcepts("URLType", var2, var0);
         s_jaxrSchemes.put(var1.getName().getValue(), var1);
      }

      return var1;
   }

   private static ClassificationScheme getPostalAddressAttributesScheme(RegistryServiceImpl var0) throws JAXRException {
      ClassificationScheme var1 = (ClassificationScheme)s_jaxrSchemes.get("PostalAddressAttributes");
      if (var1 == null) {
         String[] var2 = new String[]{"StreetNumber", "Street", "City", "State", "PostalCode", "Country"};
         var1 = createSchemeWithConcepts("PostalAddressAttributes", var2, var0);
         s_jaxrSchemes.put(var1.getName().getValue(), var1);
      }

      return var1;
   }

   private static ClassificationScheme createSchemeWithConcepts(String var0, String[] var1, RegistryServiceImpl var2) throws JAXRException {
      BusinessLifeCycleManager var4 = var2.getBusinessLifeCycleManager();
      ClassificationScheme var3 = var4.createClassificationScheme(var0, (String)null);
      String var5 = var0.substring(0, 1).toLowerCase() + var0.substring(1);
      var3.setKey(var4.createKey(var5));

      for(int var6 = 0; var6 < var1.length; ++var6) {
         Concept var7 = var4.createConcept(var3, var1[var6], var1[var6]);
         Key var8 = var4.createKey(var5 + "/" + var1[var6]);
         var7.setKey(var8);
         var3.addChildConcept(var7);
      }

      return var3;
   }
}
