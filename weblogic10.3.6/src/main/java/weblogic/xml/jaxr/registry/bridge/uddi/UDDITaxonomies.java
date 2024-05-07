package weblogic.xml.jaxr.registry.bridge.uddi;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.RegistryObject;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.CategoryBag;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.util.NodeItem;
import weblogic.auddi.uddi.util.StandardCategories;
import weblogic.auddi.uddi.util.XMLToTree;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.infomodel.KeyImpl;
import weblogic.xml.jaxr.registry.provider.InternalClassificationSchemes;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;

public class UDDITaxonomies implements InternalClassificationSchemes {
   public static final String ISO3166_ID = "uuid:4e49a8d6-d5a2-4fc2-93a0-0411d8d19e88";
   public static final String NAICS_ID = "uuid:c0b9fe13-179f-413d-8a5b-5004db8e5bb2";
   public static final String UNSPC_ID = "uuid:db77450d-9fa8-45d4-a7bc-04411d14e384";
   public static final String UDDI_ORG_TYPES_ID = "uuid:c1acf26d-9672-4404-9d70-39b756e62ab4";
   private static Map s_xmlToTreeMap;
   private static Map s_classificationSchemeMap;

   UDDITaxonomies() {
   }

   public ClassificationScheme getClassificationSchemeByKey(KeyImpl var1, RegistryServiceImpl var2) throws JAXRException {
      try {
         ClassificationScheme var3 = getCachedClassificationScheme(var1);
         if (var3 == null) {
            XMLToTree var4 = getXMLToTree(var1);
            if (var4 != null) {
               var3 = getClassificationScheme(var4, var2);
               cacheClassificationScheme(var3);
            }
         }

         return var3;
      } catch (UDDIException var5) {
         throw UDDIBridgeMapperUtil.mapException(var5);
      }
   }

   public ClassificationScheme getClassificationSchemeByName(String var1, RegistryServiceImpl var2) throws JAXRException {
      try {
         ClassificationScheme var3 = null;
         XMLToTree[] var4 = getAllXMLToTrees();

         for(int var5 = 0; var5 < var4.length && var3 == null; ++var5) {
            XMLToTree var6 = var4[var5];
            String var7 = var6.getRoot().getUserObject().toString();
            if (var7.equalsIgnoreCase(var1)) {
               String var8 = var6.getKey();
               KeyImpl var9 = (KeyImpl)var2.getBusinessLifeCycleManager().createKey(var8);
               var3 = this.getClassificationSchemeByKey(var9, var2);
            }
         }

         return var3;
      } catch (UDDIException var10) {
         throw UDDIBridgeMapperUtil.mapException(var10);
      }
   }

   public void validateConcept(Concept var1, RegistryServiceImpl var2) throws JAXRException {
      try {
         if (var1 != null && var1.getClassificationScheme() != null) {
            KeyImpl var3 = (KeyImpl)var1.getClassificationScheme().getKey();
            if (var3 != null) {
               XMLToTree var4 = getXMLToTree(var3);
               if (var4 != null && !var4.hasValue(var1.getValue())) {
                  Object[] var5 = new Object[]{var1.getValue(), var3};
                  String var6 = "jaxr.validation.taxonomy.category.invalidValue";
                  String var7 = JAXRMessages.getMessage(var6, var5);
                  throw new InvalidRequestException(var7);
               }
            }
         }

      } catch (UDDIException var8) {
         throw UDDIBridgeMapperUtil.mapException(var8);
      }
   }

   public static boolean isClassificationScheme(TModel var0) {
      boolean var1 = false;
      if (var0 != null && var0.getCategoryBag() != null) {
         CategoryBag var2 = var0.getCategoryBag();

         for(KeyedReference var3 = var2.getFirst(); var3 != null && !var1; var3 = var2.getNext()) {
            TModelKey var4 = var3.getTModelKey();
            if (var4 != null && var4.getKey().equalsIgnoreCase("uuid:c1acf26d-9672-4404-9d70-39b756e62ab4")) {
               var1 = var1 || "identifier".equalsIgnoreCase(var3.getValue());
               var1 = var1 || "namespace".equalsIgnoreCase(var3.getValue());
               var1 = var1 || "categorization".equalsIgnoreCase(var3.getValue());
               var1 = var1 || "postalAddress".equalsIgnoreCase(var3.getValue());
            }
         }
      }

      return var1;
   }

   private static void cacheClassificationScheme(ClassificationScheme var0) throws JAXRException {
      if (s_classificationSchemeMap == null) {
         s_classificationSchemeMap = new HashMap();
      }

      s_classificationSchemeMap.put(var0.getKey(), var0);
   }

   private static ClassificationScheme getCachedClassificationScheme(KeyImpl var0) {
      ClassificationScheme var1;
      if (s_classificationSchemeMap == null) {
         var1 = null;
      } else {
         var1 = (ClassificationScheme)s_classificationSchemeMap.get(var0);
      }

      return var1;
   }

   private static XMLToTree[] getAllXMLToTrees() throws UDDIException, JAXRException {
      if (s_xmlToTreeMap == null) {
         loadTaxonomies();
      }

      XMLToTree[] var0 = (XMLToTree[])((XMLToTree[])s_xmlToTreeMap.values().toArray(new XMLToTree[0]));
      return var0;
   }

   private static XMLToTree getXMLToTree(KeyImpl var0) throws UDDIException, JAXRException {
      if (s_xmlToTreeMap == null) {
         loadTaxonomies();
      }

      XMLToTree var1 = (XMLToTree)s_xmlToTreeMap.get(var0.getId().toLowerCase());
      return var1;
   }

   private static void loadTaxonomies() throws UDDIException, JAXRException {
      s_xmlToTreeMap = new HashMap();
      StandardCategories var0 = StandardCategories.getInstance();
      Map var1 = var0.getItems();
      s_xmlToTreeMap.putAll(var1);
   }

   private static ClassificationScheme getClassificationScheme(XMLToTree var0, RegistryService var1) throws JAXRException {
      String var2 = var0.getRoot().getUserObject().toString();
      ClassificationScheme var3 = var1.getBusinessLifeCycleManager().createClassificationScheme(var2, (String)null);
      Key var4 = getKey(var1, var0.getKey());
      var3.setKey(var4);
      Enumeration var5 = var0.getRoot().children();

      while(var5.hasMoreElements()) {
         DefaultMutableTreeNode var6 = (DefaultMutableTreeNode)var5.nextElement();
         Concept var7 = getConcept(var6, var3, var3, var1);
         var3.addChildConcept(var7);
      }

      return var3;
   }

   private static Concept getConcept(DefaultMutableTreeNode var0, ClassificationScheme var1, RegistryObject var2, RegistryService var3) throws JAXRException {
      NodeItem var4 = (NodeItem)var0.getUserObject();
      String var5 = var4.getName();
      String var6 = var4.getValue();
      Concept var7 = var3.getBusinessLifeCycleManager().createConcept(var2, var5, var6);
      Key var8 = var3.getBusinessLifeCycleManager().createKey(var4.getKey());
      var7.setKey(var8);
      Enumeration var9 = var0.children();

      while(var9.hasMoreElements()) {
         DefaultMutableTreeNode var10 = (DefaultMutableTreeNode)var9.nextElement();
         Concept var11 = getConcept(var10, var1, var7, var3);
         var7.addChildConcept(var11);
      }

      return var7;
   }

   private static Key getKey(RegistryService var0, String var1) throws JAXRException {
      Key var2;
      if (var1 == null) {
         var2 = null;
      } else {
         BusinessLifeCycleManager var3 = var0.getBusinessLifeCycleManager();
         var2 = var3.createKey(var1);
      }

      return var2;
   }
}
