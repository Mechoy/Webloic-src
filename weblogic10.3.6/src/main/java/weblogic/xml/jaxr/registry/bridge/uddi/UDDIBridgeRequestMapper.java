package weblogic.xml.jaxr.registry.bridge.uddi;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Association;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import weblogic.auddi.uddi.InvalidKeyPassedException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.request.inquiry.FindBindingRequest;
import weblogic.auddi.uddi.request.inquiry.FindBusinessRequest;
import weblogic.auddi.uddi.request.inquiry.FindRelatedBusinessesRequest;
import weblogic.auddi.uddi.request.inquiry.FindServiceRequest;
import weblogic.auddi.uddi.request.inquiry.FindTModelRequest;
import weblogic.auddi.uddi.request.inquiry.GetBusinessDetailRequest;
import weblogic.auddi.uddi.request.inquiry.GetServiceDetailRequest;
import weblogic.auddi.uddi.request.inquiry.GetTModelDetailRequest;
import weblogic.auddi.uddi.request.publish.AddPublisherAssertionsRequest;
import weblogic.auddi.uddi.request.publish.DeleteBindingRequest;
import weblogic.auddi.uddi.request.publish.DeleteBusinessRequest;
import weblogic.auddi.uddi.request.publish.DeletePublisherAssertionsRequest;
import weblogic.auddi.uddi.request.publish.DeleteServiceRequest;
import weblogic.auddi.uddi.request.publish.DeleteTModelRequest;
import weblogic.auddi.uddi.request.publish.GetRegisteredInfoRequest;
import weblogic.auddi.uddi.request.publish.SaveBindingRequest;
import weblogic.auddi.uddi.request.publish.SaveBusinessRequest;
import weblogic.auddi.uddi.request.publish.SaveServiceRequest;
import weblogic.auddi.uddi.request.publish.SaveTModelRequest;
import weblogic.auddi.uddi.request.publish.SetPublisherAssertionsRequest;
import weblogic.xml.jaxr.registry.BaseJAXRObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;

public class UDDIBridgeRequestMapper extends BaseJAXRObject {
   private UDDIBridgeRequestMapper() {
   }

   public static FindBusinessRequest getFindBusinessRequest(Collection var0, Collection var1, Collection var2, Collection var3, Collection var4, Collection var5) throws JAXRException {
      FindBusinessRequest var6 = new FindBusinessRequest();
      if (null != var0) {
         var6.setFindQualifiers(UDDIBridgeMapperUtil.getFindQualifiers(var0));
      }

      if (null != var1) {
         var6.setNames(UDDIBridgeMapperUtil.getSearchNames(var1));
      }

      if (null != var2) {
         var6.setCategoryBag(UDDIBridgeMapperUtil.getCategoryBagFromClassifications(var2));
      }

      if (null != var3) {
         var6.setTModelBag(UDDIBridgeMapperUtil.getTModelBagFromSpecifications(var3));
      }

      if (null != var4) {
         var6.setIdentifierBag(UDDIBridgeMapperUtil.getIdentifierBag(var4));
      }

      if (null != var5) {
         var6.setDiscoveryURLs(UDDIBridgeMapperUtil.getDiscoveryURLs(var5));
      }

      return var6;
   }

   public static FindTModelRequest getFindTModelRequest(Collection var0, Collection var1, Collection var2, Collection var3) throws JAXRException {
      FindTModelRequest var4 = new FindTModelRequest();
      if (null != var0) {
         var4.setFindQualifiers(UDDIBridgeMapperUtil.getFindQualifiers(var0));
      }

      if (null != var1) {
         var4.setNames(UDDIBridgeMapperUtil.getSearchNames(var1));
      }

      if (null != var2) {
         var4.setCategoryBag(UDDIBridgeMapperUtil.getCategoryBagFromClassifications(var2));
      }

      if (null != var3) {
         var4.setIdentifierBag(UDDIBridgeMapperUtil.getIdentifierBag(var3));
      }

      return var4;
   }

   public static FindRelatedBusinessesRequest getFindRelatedBusinessesRequest(Collection var0, String var1, String var2, KeyedReference var3) throws JAXRException {
      FindRelatedBusinessesRequest var4 = new FindRelatedBusinessesRequest();
      if (null != var0) {
         var4.setFindQualifiers(UDDIBridgeMapperUtil.getFindQualifiers(var0));
      }

      try {
         if (null != var1) {
            var4.setBusinessKey(new BusinessKey(var1));
         } else if (null != var2) {
            var4.setBusinessKey(new BusinessKey(var2));
         }
      } catch (InvalidKeyPassedException var6) {
         throw new JAXRException(var6);
      }

      if (null != var3) {
         var4.setKeyedReference(var3);
      }

      return var4;
   }

   public static FindServiceRequest getFindServiceRequest(Key var0, Collection var1, Collection var2, Collection var3, Collection var4) throws JAXRException {
      FindServiceRequest var5 = new FindServiceRequest();
      if (null != var1) {
         var5.setFindQualifiers(UDDIBridgeMapperUtil.getFindQualifiers(var1));
      }

      if (null != var2) {
         var5.setNames(UDDIBridgeMapperUtil.getSearchNames(var2));
      }

      if (null != var3) {
         var5.setCategoryBag(UDDIBridgeMapperUtil.getCategoryBagFromClassifications(var3));
      }

      if (null != var4) {
         var5.setTModelBag(UDDIBridgeMapperUtil.getTModelBagFromSpecifications(var4));
      }

      if (null != var0) {
         var5.setBusinessKey(UDDIBridgeMapperUtil.getBusinessKey(var0));
      }

      return var5;
   }

   public static FindBindingRequest getFindBindingRequest(Key var0, Collection var1, Collection var2) throws JAXRException {
      FindBindingRequest var3 = new FindBindingRequest();
      if (null != var1) {
         var3.setFindQualifiers(UDDIBridgeMapperUtil.getFindQualifiers(var1));
      }

      if (null != var2) {
         var3.setTModelBag(UDDIBridgeMapperUtil.getTModelBagFromSpecifications(var2));
      }

      if (null != var0) {
         var3.setServiceKey(UDDIBridgeMapperUtil.getServiceKey(var0));
      }

      return var3;
   }

   public static SaveBusinessRequest getSaveBusinessRequest(Collection var0, RegistryServiceImpl var1) throws JAXRException {
      try {
         SaveBusinessRequest var2 = new SaveBusinessRequest();
         if (var0 != null) {
            Iterator var3 = var0.iterator();

            while(var3.hasNext()) {
               Organization var4 = (Organization)var3.next();
               var2.addBusinessEntity(UDDIBridgeMapperUtil.getBusinessEntity(var4, var1));
            }
         }

         return var2;
      } catch (UDDIException var5) {
         throw UDDIBridgeMapperUtil.mapException(var5);
      }
   }

   public static SaveServiceRequest getSaveServiceRequest(Collection var0) throws JAXRException {
      try {
         SaveServiceRequest var1 = new SaveServiceRequest();
         if (var0 != null) {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               Service var3 = (Service)var2.next();
               var1.addBusinessService(UDDIBridgeMapperUtil.getBusinessService(var3));
            }
         }

         return var1;
      } catch (UDDIException var4) {
         throw UDDIBridgeMapperUtil.mapException(var4);
      }
   }

   public static GetRegisteredInfoRequest getRegisteredInfoRequest() throws JAXRException {
      return new GetRegisteredInfoRequest();
   }

   public static SaveBindingRequest getSaveBindingsRequest(Collection var0) throws JAXRException {
      try {
         SaveBindingRequest var1 = new SaveBindingRequest();
         if (var0 != null) {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               ServiceBinding var3 = (ServiceBinding)var2.next();
               var1.addBindingTemplate(UDDIBridgeMapperUtil.getBindingTemplate(var3));
            }
         }

         return var1;
      } catch (UDDIException var4) {
         throw UDDIBridgeMapperUtil.mapException(var4);
      }
   }

   public static SaveTModelRequest getSaveTModelFromConcepts(Collection var0) throws JAXRException {
      try {
         SaveTModelRequest var1 = new SaveTModelRequest();
         if (var0 != null) {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               Concept var3 = (Concept)var2.next();
               var1.addTModel(UDDIBridgeMapperUtil.getTModelFromConcept(var3));
            }
         }

         return var1;
      } catch (UDDIException var4) {
         throw UDDIBridgeMapperUtil.mapException(var4);
      }
   }

   public static SaveTModelRequest getSaveTModelFromClassificationSchemes(Collection var0) throws JAXRException {
      try {
         SaveTModelRequest var1 = new SaveTModelRequest();
         TModel var4;
         if (var0 != null) {
            for(Iterator var2 = var0.iterator(); var2.hasNext(); var1.addTModel(var4)) {
               ClassificationScheme var3 = (ClassificationScheme)var2.next();
               var4 = UDDIBridgeMapperUtil.getTModelFromClassificationScheme(var3);
               if (var4.getOverviewDoc() != null && var4.getOverviewDoc().getOverviewURL() != null) {
                  validateURI(var4.getOverviewDoc().getOverviewURL().toString());
               }
            }
         }

         return var1;
      } catch (UDDIException var5) {
         throw UDDIBridgeMapperUtil.mapException(var5);
      }
   }

   public static void validateURI(String var0) throws JAXRException {
      try {
         URL var1 = new URL(var0);
         if (var1.getProtocol().equalsIgnoreCase("http")) {
            String var5;
            String var13;
            String[] var15;
            try {
               HttpURLConnection var12 = (HttpURLConnection)var1.openConnection();
               int var14 = var12.getResponseCode();
               int var16 = var14 / 100;
               if (var16 != 1 && var16 != 2 && var16 != 3) {
                  String[] var6;
                  String var7;
                  if (var16 == 4) {
                     var5 = "jaxr.validation.uri.clientError";
                     var6 = new String[]{var0};
                     var7 = JAXRMessages.getMessage(var5, var6);
                     throw new InvalidRequestException(var7);
                  }

                  if (var16 == 5) {
                     var5 = "jaxr.validation.uri.serverError";
                     var6 = new String[]{var0};
                     var7 = JAXRMessages.getMessage(var5, var6);
                     throw new InvalidRequestException(var7);
                  }

                  var5 = "jaxr.validation.uri.unknownResponse";
                  var6 = new String[]{var0};
                  var7 = JAXRMessages.getMessage(var5, var6);
                  throw new InvalidRequestException(var7);
               }
            } catch (UnknownHostException var8) {
               var13 = "jaxr.validation.uri.unknownHostException";
               var15 = new String[]{var0};
               var5 = JAXRMessages.getMessage(var13, var15);
               throw new InvalidRequestException(var5, var8);
            } catch (ConnectException var9) {
               var13 = "jaxr.validation.uri.connectException";
               var15 = new String[]{var0};
               var5 = JAXRMessages.getMessage(var13, var15);
               throw new InvalidRequestException(var5, var9);
            } catch (IOException var10) {
               var13 = "jaxr.validation.uri.ioException";
               var15 = new String[]{var0};
               var5 = JAXRMessages.getMessage(var13, var15);
               throw new InvalidRequestException(var5, var10);
            }
         }

      } catch (MalformedURLException var11) {
         String var2 = "jaxr.validation.uri.malformed";
         String[] var3 = new String[]{var0};
         String var4 = JAXRMessages.getMessage(var2, var3);
         throw new InvalidRequestException(var4, var11);
      }
   }

   public static SetPublisherAssertionsRequest getSetPublisherAssertions(Collection var0) throws JAXRException {
      SetPublisherAssertionsRequest var1 = new SetPublisherAssertionsRequest();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Association var3 = (Association)var2.next();
            var1.addPublisherAssertion(UDDIBridgeMapperUtil.getPublisherAssertionFromAssociation(var3));
         }
      }

      return var1;
   }

   public static AddPublisherAssertionsRequest getAddPublisherAssertions(Collection var0) throws JAXRException {
      AddPublisherAssertionsRequest var1 = new AddPublisherAssertionsRequest();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Association var3 = (Association)var2.next();
            var1.addPublisherAssertion(UDDIBridgeMapperUtil.getPublisherAssertionFromAssociation(var3));
         }
      }

      return var1;
   }

   public static DeleteBusinessRequest getDeleteBusinessRequest(Collection var0) throws JAXRException {
      DeleteBusinessRequest var1 = new DeleteBusinessRequest();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Key var3 = (Key)var2.next();
            var1.addBusinessKey(UDDIBridgeMapperUtil.getBusinessKey(var3));
         }
      }

      return var1;
   }

   public static DeleteBindingRequest getDeleteBindingRequest(Collection var0) throws JAXRException {
      DeleteBindingRequest var1 = new DeleteBindingRequest();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Key var3 = (Key)var2.next();
            var1.addBindingKey(UDDIBridgeMapperUtil.getBindingKey(var3));
         }
      }

      return var1;
   }

   public static DeleteServiceRequest getDeleteServiceRequest(Collection var0) throws JAXRException {
      DeleteServiceRequest var1 = new DeleteServiceRequest();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Key var3 = (Key)var2.next();
            var1.addServiceKey(UDDIBridgeMapperUtil.getServiceKey(var3));
         }
      }

      return var1;
   }

   public static DeleteTModelRequest getDeleteTModelRequest(Collection var0) throws JAXRException {
      DeleteTModelRequest var1 = new DeleteTModelRequest();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Key var3 = (Key)var2.next();
            var1.addTModelKey(UDDIBridgeMapperUtil.getTModelKey(var3));
         }
      }

      return var1;
   }

   public static DeletePublisherAssertionsRequest getDeletePublisherAssertionsRequest(Collection var0, RegistryServiceImpl var1) throws JAXRException {
      DeletePublisherAssertionsRequest var2 = new DeletePublisherAssertionsRequest();
      if (var0 != null) {
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            Key var4 = (Key)var3.next();

            try {
               var2.addPublisherAssertion(UDDIBridgeMapperUtil.getPublisherAssertionFromAssociationKey(var4, var1));
            } catch (UDDIException var6) {
               throw new JAXRException(var6);
            }
         }
      }

      return var2;
   }

   public static GetBusinessDetailRequest getBusinessDetailRequest(Collection var0) throws JAXRException {
      GetBusinessDetailRequest var1 = new GetBusinessDetailRequest();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            BusinessKey var3 = (BusinessKey)var2.next();
            var1.addBusinessKey(var3);
         }
      }

      return var1;
   }

   public static GetServiceDetailRequest getServiceDetailRequest(Collection var0) throws JAXRException {
      GetServiceDetailRequest var1 = new GetServiceDetailRequest();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            ServiceKey var3 = (ServiceKey)var2.next();
            var1.addServiceKey(var3);
         }
      }

      return var1;
   }

   public static GetTModelDetailRequest getTModelDetailRequest(Collection var0) throws JAXRException {
      GetTModelDetailRequest var1 = new GetTModelDetailRequest();
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            TModelKey var3 = (TModelKey)var2.next();
            var1.addTModelKey(var3);
         }
      }

      return var1;
   }
}
