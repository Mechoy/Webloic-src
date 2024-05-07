package weblogic.uddi.client.service;

import weblogic.uddi.client.serialize.dom.AuthTokenDOMBinder;
import weblogic.uddi.client.serialize.dom.BindingDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.BusinessDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.DeleteBindingDOMBinder;
import weblogic.uddi.client.serialize.dom.DeleteBusinessDOMBinder;
import weblogic.uddi.client.serialize.dom.DeleteServiceDOMBinder;
import weblogic.uddi.client.serialize.dom.DeleteTModelDOMBinder;
import weblogic.uddi.client.serialize.dom.DiscardAuthTokenDOMBinder;
import weblogic.uddi.client.serialize.dom.DispositionReportDOMBinder;
import weblogic.uddi.client.serialize.dom.GetAuthTokenDOMBinder;
import weblogic.uddi.client.serialize.dom.GetRegisteredInfoDOMBinder;
import weblogic.uddi.client.serialize.dom.RegisteredInfoDOMBinder;
import weblogic.uddi.client.serialize.dom.SaveBindingDOMBinder;
import weblogic.uddi.client.serialize.dom.SaveBusinessDOMBinder;
import weblogic.uddi.client.serialize.dom.SaveServiceDOMBinder;
import weblogic.uddi.client.serialize.dom.SaveTModelDOMBinder;
import weblogic.uddi.client.serialize.dom.ServiceDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.TModelDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.UDDIExceptionDOMBinder;
import weblogic.uddi.client.serialize.dom.ValidateCategorizationDOMBinder;
import weblogic.uddi.client.structures.exception.UDDIException;
import weblogic.uddi.client.structures.exception.XML_SoapException;
import weblogic.uddi.client.structures.request.DeleteBinding;
import weblogic.uddi.client.structures.request.DeleteBusiness;
import weblogic.uddi.client.structures.request.DeleteService;
import weblogic.uddi.client.structures.request.DeleteTModel;
import weblogic.uddi.client.structures.request.DiscardAuthToken;
import weblogic.uddi.client.structures.request.GetAuthToken;
import weblogic.uddi.client.structures.request.GetRegisteredInfo;
import weblogic.uddi.client.structures.request.SaveBinding;
import weblogic.uddi.client.structures.request.SaveBusiness;
import weblogic.uddi.client.structures.request.SaveService;
import weblogic.uddi.client.structures.request.SaveTModel;
import weblogic.uddi.client.structures.request.ValidateCategorization;
import weblogic.uddi.client.structures.response.AuthToken;
import weblogic.uddi.client.structures.response.BindingDetail;
import weblogic.uddi.client.structures.response.BusinessDetail;
import weblogic.uddi.client.structures.response.DispositionReport;
import weblogic.uddi.client.structures.response.RegisteredInfo;
import weblogic.uddi.client.structures.response.ServiceDetail;
import weblogic.uddi.client.structures.response.TModelDetail;

public class Publish extends UDDIService {
   public Publish() {
      System.setProperty("weblogic.webservice.forceXMLEncoding", "true");
      System.setProperty("weblogic.webservice.i18n.charset", "UTF-8");
      this.URL = new String("https://www-3.ibm.com/services/uddi/testregistry/protect/publishapi");
   }

   public AuthToken getAuthToken(GetAuthToken var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(GetAuthTokenDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         AuthToken var3 = AuthTokenDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public DispositionReport discardAuthToken(DiscardAuthToken var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(DiscardAuthTokenDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         DispositionReport var3 = DispositionReportDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public RegisteredInfo getRegisteredInfo(GetRegisteredInfo var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(GetRegisteredInfoDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         RegisteredInfo var3 = RegisteredInfoDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public DispositionReport validateCategorization(ValidateCategorization var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(ValidateCategorizationDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         DispositionReport var3 = DispositionReportDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public DispositionReport deleteBinding(DeleteBinding var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(DeleteBindingDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         DispositionReport var3 = DispositionReportDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public DispositionReport deleteBusiness(DeleteBusiness var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(DeleteBusinessDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         DispositionReport var3 = DispositionReportDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public DispositionReport deleteService(DeleteService var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(DeleteServiceDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         DispositionReport var3 = DispositionReportDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public DispositionReport deleteTModel(DeleteTModel var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(DeleteTModelDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         DispositionReport var3 = DispositionReportDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public BindingDetail saveBinding(SaveBinding var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(SaveBindingDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         BindingDetail var3 = BindingDetailDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public BusinessDetail saveBusiness(SaveBusiness var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(SaveBusinessDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         BusinessDetail var3 = BusinessDetailDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public ServiceDetail saveService(SaveService var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(SaveServiceDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         ServiceDetail var3 = ServiceDetailDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public TModelDetail saveTModel(SaveTModel var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(SaveTModelDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         TModelDetail var3 = TModelDetailDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }
}
