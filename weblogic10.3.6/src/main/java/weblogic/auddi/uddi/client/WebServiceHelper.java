package weblogic.auddi.uddi.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.AccessPoint;
import weblogic.auddi.uddi.datastructure.BindingTemplate;
import weblogic.auddi.uddi.datastructure.BindingTemplates;
import weblogic.auddi.uddi.datastructure.BusinessEntities;
import weblogic.auddi.uddi.datastructure.BusinessEntity;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.BusinessService;
import weblogic.auddi.uddi.datastructure.BusinessServices;
import weblogic.auddi.uddi.datastructure.Description;
import weblogic.auddi.uddi.datastructure.DiscoveryURL;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.OverviewDoc;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModelBag;
import weblogic.auddi.uddi.datastructure.TModelInstanceInfo;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.datastructure.TModels;
import weblogic.auddi.uddi.request.inquiry.FindBindingRequest;
import weblogic.auddi.uddi.request.inquiry.FindServiceRequest;
import weblogic.auddi.uddi.request.publish.SaveBusinessRequest;
import weblogic.auddi.uddi.request.publish.SaveServiceRequest;
import weblogic.auddi.uddi.request.publish.SaveTModelRequest;
import weblogic.auddi.uddi.response.BindingDetailResponse;
import weblogic.auddi.uddi.response.BusinessDetailResponse;
import weblogic.auddi.uddi.response.ServiceDetailResponse;
import weblogic.auddi.uddi.response.ServiceInfo;
import weblogic.auddi.uddi.response.ServiceInfos;
import weblogic.auddi.uddi.response.ServiceListResponse;
import weblogic.auddi.uddi.response.TModelDetailResponse;

public class WebServiceHelper {
   private UDDIProxy uddiProxy;

   public WebServiceHelper(String var1, String var2, String var3, String var4) throws MalformedURLException, UDDIException {
      if (var1 == null && var2 == null) {
         throw new FatalErrorException("At minimum, a uddiInquiryURL or uddiPublishURL is needed to use the UDDI registry");
      } else {
         this.uddiProxy = new UDDIProxy();
         if (var1 != null) {
            this.uddiProxy.setInquiryURL(var1);
         }

         if (var2 != null) {
            this.uddiProxy.setPublishURL(var2);
         }

         this.uddiProxy.setCredential(var3, var4);
      }
   }

   public TModelKey saveInterface(String var1, String var2, String var3) throws UDDIException {
      if (var1 != null && !var1.equals("")) {
         TModel var4 = new TModel();
         var4.setName(new Name(var1));
         if (var2 != null) {
            Description var5 = new Description(var2);
            var4.addDescription(var5);
         }

         if (var3 != null) {
            OverviewDoc var10 = new OverviewDoc();
            var10.setOverviewURL(var3);
            var4.setOverviewDoc(var10);
         }

         SaveTModelRequest var11 = new SaveTModelRequest();
         var11.addTModel(var4);
         TModelDetailResponse var6 = (TModelDetailResponse)this.uddiProxy.execute(var11);
         if (var6 == null) {
            throw new FatalErrorException("Operation failed - no response received from the server");
         } else {
            TModels var7 = var6.getTModels();
            if (var7.size() == 0) {
               throw new FatalErrorException("Operation failed - no response received from the server");
            } else {
               TModel var8 = var7.getFirst();
               TModelKey var9 = var8.getTModelKey();
               return var9;
            }
         }
      } else {
         throw new IllegalArgumentException("Cannot save interface with no name");
      }
   }

   public ServiceKey saveWebService(BusinessKey var1, String var2, String var3, String var4, TModelKey var5) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot save Web Service when no provider is specified");
      } else if (var2 != null && !var2.equals("")) {
         BusinessService var6 = this.getService(var2, var3, var4, var5);
         var6.setBusinessKey(var1);
         SaveServiceRequest var7 = new SaveServiceRequest();
         var7.addBusinessService(var6);
         ServiceDetailResponse var8 = (ServiceDetailResponse)this.uddiProxy.execute(var7);
         if (var8 == null) {
            throw new FatalErrorException("Operation failed - no response received from the server");
         } else {
            BusinessServices var9 = var8.getBusinessServices();
            if (var9.size() == 0) {
               throw new FatalErrorException("Operation failed - no response received from the server");
            } else {
               BusinessService var10 = var9.getFirst();
               ServiceKey var11 = var10.getServiceKey();
               return var11;
            }
         }
      } else {
         throw new IllegalArgumentException("Cannot save Web Service with no name");
      }
   }

   public ServiceKey saveWebService(String var1, String var2, String var3, String var4, TModelKey var5) throws UDDIException {
      if (var1 != null && !var1.equals("")) {
         if (var2 != null && !var2.equals("")) {
            BusinessService var6 = this.getService(var2, var3, var4, var5);
            BusinessEntity var7 = new BusinessEntity();
            var7.addName(new Name(var1));
            var7.addBusinessService(var6);
            SaveBusinessRequest var8 = new SaveBusinessRequest();
            var8.addBusinessEntity(var7);
            BusinessDetailResponse var9 = (BusinessDetailResponse)this.uddiProxy.execute(var8);
            if (var9 == null) {
               throw new FatalErrorException("Operation failed - no response received from the server");
            } else {
               BusinessEntities var10 = var9.getBusinessEntities();
               if (var10.size() == 0) {
                  throw new FatalErrorException("Operation failed - no response received from the server");
               } else {
                  BusinessServices var11 = var10.getFirst().getBusinessServices();
                  if (var11.size() == 0) {
                     throw new FatalErrorException("Operation failed - no response received from the server");
                  } else {
                     BusinessService var12 = var11.getFirst();
                     ServiceKey var13 = var12.getServiceKey();
                     return var13;
                  }
               }
            }
         } else {
            throw new IllegalArgumentException("Cannot save Web Service with no name");
         }
      } else {
         throw new IllegalArgumentException("Cannot save Web Service when no provider is specified");
      }
   }

   private BusinessService getService(String var1, String var2, String var3, TModelKey var4) throws UDDIException {
      BusinessService var5 = new BusinessService();
      var5.addName(new Name(var1));
      if (var2 != null) {
         Description var6 = new Description(var2);
         var5.addDescription(var6);
      }

      BindingTemplate var11 = null;
      if (var3 != null) {
         if (var11 == null) {
            var11 = new BindingTemplate();
         }

         try {
            new URL(var3);
         } catch (MalformedURLException var10) {
            throw new FatalErrorException("AccessPoint value was not a valid URL", var10);
         }

         String var7 = var3.substring(0, 5).toLowerCase();
         String var8 = null;
         if (var7.startsWith("http")) {
            var8 = "http";
         } else if (var7.startsWith("https")) {
            var8 = "https";
         } else {
            if (!var7.startsWith("mailto")) {
               throw new FatalErrorException("Unrecognized accessPoint type");
            }

            var8 = "mailto";
         }

         AccessPoint var9 = new AccessPoint(var8, var3);
         var11.setAccessPoint(var9);
      }

      if (var4 != null) {
         if (var11 == null) {
            var11 = new BindingTemplate();
         }

         TModelInstanceInfo var12 = new TModelInstanceInfo(var4);
         var11.addTModelInstanceInfo(var12);
      }

      if (var11 != null) {
         var5.addBindingTemplate(var11);
      }

      return var5;
   }

   public BusinessKey saveServiceProvider(String var1, String var2, String var3) throws UDDIException {
      if (var1 != null && !var1.equals("")) {
         BusinessEntity var4 = new BusinessEntity();
         var4.addName(new Name(var1));
         Description var5;
         if (var2 != null) {
            var5 = new Description(var2);
            var4.addDescription(var5);
         }

         if (var3 != null) {
            var5 = null;

            try {
               new URL(var3);
            } catch (MalformedURLException var10) {
               throw new FatalErrorException("DiscoveryURL value was not a valid URL", var10);
            }

            String var6 = var3.substring(0, 5).toLowerCase();
            String var7 = null;
            if (var6.startsWith("http")) {
               var7 = "http";
            } else if (var6.startsWith("https")) {
               var7 = "https";
            } else {
               if (!var6.startsWith("mailto")) {
                  throw new FatalErrorException("Unrecognized discoveryURL type");
               }

               var7 = "mailto";
            }

            DiscoveryURL var8 = new DiscoveryURL(var7, var3);
            var4.addDiscoveryURL(var8);
         }

         SaveBusinessRequest var11 = new SaveBusinessRequest();
         var11.addBusinessEntity(var4);
         BusinessDetailResponse var12 = (BusinessDetailResponse)this.uddiProxy.execute(var11);
         if (var12 == null) {
            throw new FatalErrorException("Operation failed - no response received from the server");
         } else {
            BusinessEntities var14 = var12.getBusinessEntities();
            if (var14.size() == 0) {
               throw new FatalErrorException("Operation failed - no response received from the server");
            } else {
               BusinessEntity var13 = var14.getFirst();
               BusinessKey var9 = var13.getBusinessKey();
               return var9;
            }
         }
      } else {
         throw new IllegalArgumentException("Cannot save Web Service Provider without a name");
      }
   }

   public ServiceInfos findWebServiceImplementations(TModelKey var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException("A TModelKey is required for this operation");
      } else {
         FindServiceRequest var2 = new FindServiceRequest();
         TModelBag var3 = new TModelBag();
         var3.add(var1);
         var2.setTModelBag(var3);
         ServiceListResponse var4 = (ServiceListResponse)this.uddiProxy.execute(var2);
         if (var4 == null) {
            throw new FatalErrorException("Operation failed - no response received from the server");
         } else {
            ServiceInfos var5 = var4.getServiceInfos();
            return var5;
         }
      }
   }

   public List findInvocationPoints(TModelKey var1) throws UDDIException {
      ArrayList var2 = new ArrayList();
      ServiceInfos var3 = this.findWebServiceImplementations(var1);
      if (var3 != null && var3.size() != 0) {
         FindBindingRequest var4 = new FindBindingRequest();
         TModelBag var5 = new TModelBag();
         var5.add(var1);
         var4.setTModelBag(var5);

         for(ServiceInfo var6 = var3.getFirst(); var6 != null; var6 = var3.getNext()) {
            var4.setServiceKey(var6.getServiceKey());
            BindingDetailResponse var7 = (BindingDetailResponse)this.uddiProxy.execute(var4);
            if (var7 != null) {
               BindingTemplates var8 = var7.getBindingTemplates();
               if (var8.size() > 0) {
                  for(BindingTemplate var9 = var8.getFirst(); var9 != null; var9 = var8.getNext()) {
                     AccessPoint var10 = var9.getAccessPoint();
                     if (var10 != null) {
                        var2.add(var10.getURL());
                     }
                  }
               }
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public static void setTrustedKeyStore(String var0) {
      UDDIProxy.setTrustedKeyStore(var0);
   }
}
