package weblogic.auddi.uddi.datastructure;

import java.util.Hashtable;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.UnsupportedException;
import weblogic.auddi.uddi.request.inquiry.FindBindingRequestHandler;
import weblogic.auddi.uddi.request.inquiry.FindBusinessRequestHandler;
import weblogic.auddi.uddi.request.inquiry.FindQualifierHandler;
import weblogic.auddi.uddi.request.inquiry.FindQualifiersHandler;
import weblogic.auddi.uddi.request.inquiry.FindRelatedBusinessesRequestHandler;
import weblogic.auddi.uddi.request.inquiry.FindServiceRequestHandler;
import weblogic.auddi.uddi.request.inquiry.FindTModelRequestHandler;
import weblogic.auddi.uddi.request.inquiry.GetBindingDetailRequestHandler;
import weblogic.auddi.uddi.request.inquiry.GetBusinessDetailExtRequestHandler;
import weblogic.auddi.uddi.request.inquiry.GetBusinessDetailRequestHandler;
import weblogic.auddi.uddi.request.inquiry.GetServiceDetailRequestHandler;
import weblogic.auddi.uddi.request.inquiry.GetTModelDetailRequestHandler;
import weblogic.auddi.uddi.request.publish.AddPublisherAssertionsRequestHandler;
import weblogic.auddi.uddi.request.publish.DeleteBindingRequestHandler;
import weblogic.auddi.uddi.request.publish.DeleteBusinessRequestHandler;
import weblogic.auddi.uddi.request.publish.DeletePublisherAssertionsRequestHandler;
import weblogic.auddi.uddi.request.publish.DeleteServiceRequestHandler;
import weblogic.auddi.uddi.request.publish.DeleteTModelRequestHandler;
import weblogic.auddi.uddi.request.publish.DiscardAuthTokenRequestHandler;
import weblogic.auddi.uddi.request.publish.GetAssertionStatusReportRequestHandler;
import weblogic.auddi.uddi.request.publish.GetAuthTokenRequestHandler;
import weblogic.auddi.uddi.request.publish.GetPublisherAssertionsRequestHandler;
import weblogic.auddi.uddi.request.publish.GetRegisteredInfoRequestHandler;
import weblogic.auddi.uddi.request.publish.SaveBindingRequestHandler;
import weblogic.auddi.uddi.request.publish.SaveBusinessRequestHandler;
import weblogic.auddi.uddi.request.publish.SaveServiceRequestHandler;
import weblogic.auddi.uddi.request.publish.SaveTModelRequestHandler;
import weblogic.auddi.uddi.request.publish.SetPublisherAssertionsRequestHandler;
import weblogic.auddi.uddi.response.AssertionStatusReportResponseHandler;
import weblogic.auddi.uddi.response.AuthTokenResponseHandler;
import weblogic.auddi.uddi.response.BindingDetailResponseHandler;
import weblogic.auddi.uddi.response.BusinessDetailExtResponseHandler;
import weblogic.auddi.uddi.response.BusinessDetailResponseHandler;
import weblogic.auddi.uddi.response.BusinessInfoHandler;
import weblogic.auddi.uddi.response.BusinessInfosHandler;
import weblogic.auddi.uddi.response.BusinessListResponseHandler;
import weblogic.auddi.uddi.response.DispositionReportResponseHandler;
import weblogic.auddi.uddi.response.ErrInfoHandler;
import weblogic.auddi.uddi.response.ErrorResponseHandler;
import weblogic.auddi.uddi.response.FaultResponseHandler;
import weblogic.auddi.uddi.response.PublisherAssertionsResponseHandler;
import weblogic.auddi.uddi.response.RegisteredInfoResponseHandler;
import weblogic.auddi.uddi.response.RelatedBusinessInfoHandler;
import weblogic.auddi.uddi.response.RelatedBusinessInfosHandler;
import weblogic.auddi.uddi.response.RelatedBusinessListResponseHandler;
import weblogic.auddi.uddi.response.ResultHandler;
import weblogic.auddi.uddi.response.ServiceDetailResponseHandler;
import weblogic.auddi.uddi.response.ServiceInfoHandler;
import weblogic.auddi.uddi.response.ServiceInfosHandler;
import weblogic.auddi.uddi.response.ServiceListResponseHandler;
import weblogic.auddi.uddi.response.SharedRelationshipsHandler;
import weblogic.auddi.uddi.response.TModelDetailResponseHandler;
import weblogic.auddi.uddi.response.TModelInfoHandler;
import weblogic.auddi.uddi.response.TModelInfosHandler;
import weblogic.auddi.uddi.response.TModelListResponseHandler;
import weblogic.auddi.util.Logger;

public class UDDIXMLHandlerMaker {
   private static UDDIXMLHandlerMaker _instance = null;
   protected Hashtable _lookuptable = null;

   public static UDDIXMLHandlerMaker getInstance() throws UDDIException {
      if (_instance == null) {
         Class var0 = UDDIXMLHandlerMaker.class;
         synchronized(UDDIXMLHandlerMaker.class) {
            if (_instance == null) {
               String var1 = "weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMakerDev";

               try {
                  _instance = (UDDIXMLHandlerMaker)Class.forName(var1).newInstance();
               } catch (Exception var6) {
                  Logger.debug("could not find : " + var1 + " trying the other");
                  var1 = "weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker";

                  try {
                     _instance = (UDDIXMLHandlerMaker)Class.forName(var1).newInstance();
                  } catch (Exception var5) {
                     throw new FatalErrorException(UDDIMessages.get("error.fatalError.instantiation", var1), var5);
                  }
               }
            }
         }
      }

      return _instance;
   }

   protected UDDIXMLHandlerMaker() {
      this._lookuptable = new Hashtable();
      this._lookuptable.put("accessPoint", AccessPointHandler.class);
      this._lookuptable.put("authInfo", AuthInfoHandler.class);
      this._lookuptable.put("authToken", AuthTokenResponseHandler.class);
      this._lookuptable.put("authorizedName", AuthorizedNameHandler.class);
      this._lookuptable.put("bindingDetail", BindingDetailResponseHandler.class);
      this._lookuptable.put("bindingKey", BindingKeyHandler.class);
      this._lookuptable.put("bindingTemplate", BindingTemplateHandler.class);
      this._lookuptable.put("bindingTemplates", BindingTemplatesHandler.class);
      this._lookuptable.put("businessDetail", BusinessDetailResponseHandler.class);
      this._lookuptable.put("businessDetailExt", BusinessDetailExtResponseHandler.class);
      this._lookuptable.put("businessKey", BusinessKeyHandler.class);
      this._lookuptable.put("bindingKey", BindingKeyHandler.class);
      this._lookuptable.put("businessEntityExt", BusinessEntityExtHandler.class);
      this._lookuptable.put("businessInfo", BusinessInfoHandler.class);
      this._lookuptable.put("businessInfos", BusinessInfosHandler.class);
      this._lookuptable.put("businessList", BusinessListResponseHandler.class);
      this._lookuptable.put("businessServices", BusinessServicesHandler.class);
      this._lookuptable.put("businessService", BusinessServiceHandler.class);
      this._lookuptable.put("categoryBag", CategoryBagHandler.class);
      this._lookuptable.put("description", DescriptionHandler.class);
      this._lookuptable.put("discoveryURL", DiscoveryURLHandler.class);
      this._lookuptable.put("discoveryURLs", DiscoveryURLsHandler.class);
      this._lookuptable.put("dispositionReport", DispositionReportResponseHandler.class);
      this._lookuptable.put("errInfo", ErrInfoHandler.class);
      this._lookuptable.put("find_binding", FindBindingRequestHandler.class);
      this._lookuptable.put("find_business", FindBusinessRequestHandler.class);
      this._lookuptable.put("find_service", FindServiceRequestHandler.class);
      this._lookuptable.put("find_tModel", FindTModelRequestHandler.class);
      this._lookuptable.put("find_qualifiers", FindQualifiersHandler.class);
      this._lookuptable.put("findQualifier", FindQualifierHandler.class);
      this._lookuptable.put("findQualifiers", FindQualifiersHandler.class);
      this._lookuptable.put("find_qualifier", FindQualifierHandler.class);
      this._lookuptable.put("identifier_bag", IdentifierBagHandler.class);
      this._lookuptable.put("keyedReference", KeyedReferenceHandler.class);
      this._lookuptable.put("get_bindingDetail", GetBindingDetailRequestHandler.class);
      this._lookuptable.put("get_businessDetail", GetBusinessDetailRequestHandler.class);
      this._lookuptable.put("get_businessDetailExt", GetBusinessDetailExtRequestHandler.class);
      this._lookuptable.put("get_serviceDetail", GetServiceDetailRequestHandler.class);
      this._lookuptable.put("get_tModelDetail", GetTModelDetailRequestHandler.class);
      this._lookuptable.put("delete_binding", DeleteBindingRequestHandler.class);
      this._lookuptable.put("delete_business", DeleteBusinessRequestHandler.class);
      this._lookuptable.put("delete_service", DeleteServiceRequestHandler.class);
      this._lookuptable.put("delete_tModel", DeleteTModelRequestHandler.class);
      this._lookuptable.put("discard_authToken", DiscardAuthTokenRequestHandler.class);
      this._lookuptable.put("get_authToken", GetAuthTokenRequestHandler.class);
      this._lookuptable.put("get_registeredInfo", GetRegisteredInfoRequestHandler.class);
      this._lookuptable.put("hostingRedirector", HostingRedirectorHandler.class);
      this._lookuptable.put("identifierBag", IdentifierBagHandler.class);
      this._lookuptable.put("name", NameHandler.class);
      this._lookuptable.put("searchname", SearchNameHandler.class);
      this._lookuptable.put("operator", OperatorHandler.class);
      this._lookuptable.put("registeredInfo", RegisteredInfoResponseHandler.class);
      this._lookuptable.put("result", ResultHandler.class);
      this._lookuptable.put("serviceDetail", ServiceDetailResponseHandler.class);
      this._lookuptable.put("serviceInfo", ServiceInfoHandler.class);
      this._lookuptable.put("serviceInfos", ServiceInfosHandler.class);
      this._lookuptable.put("serviceKey", ServiceKeyHandler.class);
      this._lookuptable.put("serviceList", ServiceListResponseHandler.class);
      this._lookuptable.put("save_binding", SaveBindingRequestHandler.class);
      this._lookuptable.put("save_business", SaveBusinessRequestHandler.class);
      this._lookuptable.put("save_service", SaveServiceRequestHandler.class);
      this._lookuptable.put("save_tModel", SaveTModelRequestHandler.class);
      this._lookuptable.put("phone", PhoneHandler.class);
      this._lookuptable.put("email", EmailHandler.class);
      this._lookuptable.put("address", AddressHandler.class);
      this._lookuptable.put("addressLine", AddressLineHandler.class);
      this._lookuptable.put("contact", ContactHandler.class);
      this._lookuptable.put("contacts", ContactsHandler.class);
      this._lookuptable.put("instanceParms", InstanceParmsHandler.class);
      this._lookuptable.put("tModel", TModelHandler.class);
      this._lookuptable.put("tModel_bag", TModelBagHandler.class);
      this._lookuptable.put("tModelBag", TModelBagHandler.class);
      this._lookuptable.put("tModelExt", TModelExtHandler.class);
      this._lookuptable.put("tModelDetail", TModelDetailResponseHandler.class);
      this._lookuptable.put("tModelInfo", TModelInfoHandler.class);
      this._lookuptable.put("tModelInfos", TModelInfosHandler.class);
      this._lookuptable.put("tModelInstanceDetails", TModelInstanceDetailsHandler.class);
      this._lookuptable.put("tModelInstanceInfo", TModelInstanceInfoHandler.class);
      this._lookuptable.put("instanceDetails", InstanceDetailsHandler.class);
      this._lookuptable.put("overviewDoc", OverviewDocHandler.class);
      this._lookuptable.put("tModelKey", TModelKeyHandler.class);
      this._lookuptable.put("tModelList", TModelListResponseHandler.class);
      this._lookuptable.put("businessEntity", BusinessEntityHandler.class);
      this._lookuptable.put("businessDetail", BusinessDetailResponseHandler.class);
      this._lookuptable.put("businessDetailExt", BusinessDetailExtResponseHandler.class);
      this._lookuptable.put("bindingDetail", BindingDetailResponseHandler.class);
      this._lookuptable.put("serviceDetail", ServiceDetailResponseHandler.class);
      this._lookuptable.put("tModelDetail", TModelDetailResponseHandler.class);
      this._lookuptable.put("publisherAssertion", PublisherAssertionHandler.class);
      this._lookuptable.put("publisherAssertions", PublisherAssertionsResponseHandler.class);
      this._lookuptable.put("add_publisherAssertions", AddPublisherAssertionsRequestHandler.class);
      this._lookuptable.put("delete_publisherAssertions", DeletePublisherAssertionsRequestHandler.class);
      this._lookuptable.put("get_publisherAssertions", GetPublisherAssertionsRequestHandler.class);
      this._lookuptable.put("set_publisherAssertions", SetPublisherAssertionsRequestHandler.class);
      this._lookuptable.put("find_relatedBusinesses", FindRelatedBusinessesRequestHandler.class);
      this._lookuptable.put("relatedBusinessInfo", RelatedBusinessInfoHandler.class);
      this._lookuptable.put("relatedBusinessInfos", RelatedBusinessInfosHandler.class);
      this._lookuptable.put("sharedRelationships", SharedRelationshipsHandler.class);
      this._lookuptable.put("get_assertionStatusReport", GetAssertionStatusReportRequestHandler.class);
      this._lookuptable.put("assertionStatusItem", AssertionStatusItemHandler.class);
      this._lookuptable.put("completionStatus", CompletionStatusHandler.class);
      this._lookuptable.put("Fault", FaultResponseHandler.class);
      this._lookuptable.put("relatedBusinessList", RelatedBusinessListResponseHandler.class);
      this._lookuptable.put("relatedBusinessesList", RelatedBusinessListResponseHandler.class);
      this._lookuptable.put("assertionStatusReport", AssertionStatusReportResponseHandler.class);
      this._lookuptable.put("Error", ErrorResponseHandler.class);
   }

   public UDDIXMLHandler makeHandler(String var1) throws UDDIException {
      Class var2 = (Class)this._lookuptable.get(var1);
      if (var2 == null) {
         throw new UnsupportedException(UDDIMessages.get("error.unsupported.api", var1));
      } else {
         try {
            UDDIXMLHandler var3 = (UDDIXMLHandler)var2.newInstance();
            return var3;
         } catch (InstantiationException var4) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.instantiation", var2.getName()), var4);
         } catch (IllegalAccessException var5) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.illegalAccess", var2.getName()), var5);
         }
      }
   }
}
