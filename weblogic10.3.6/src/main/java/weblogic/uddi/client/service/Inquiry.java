package weblogic.uddi.client.service;

import weblogic.uddi.client.serialize.dom.BindingDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.BusinessDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.BusinessDetailExtDOMBinder;
import weblogic.uddi.client.serialize.dom.BusinessListDOMBinder;
import weblogic.uddi.client.serialize.dom.FindBindingDOMBinder;
import weblogic.uddi.client.serialize.dom.FindBusinessDOMBinder;
import weblogic.uddi.client.serialize.dom.FindServiceDOMBinder;
import weblogic.uddi.client.serialize.dom.FindTModelDOMBinder;
import weblogic.uddi.client.serialize.dom.GetBindingDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.GetBusinessDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.GetBusinessDetailExtDOMBinder;
import weblogic.uddi.client.serialize.dom.GetServiceDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.GetTModelDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.ServiceDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.ServiceListDOMBinder;
import weblogic.uddi.client.serialize.dom.TModelDetailDOMBinder;
import weblogic.uddi.client.serialize.dom.TModelListDOMBinder;
import weblogic.uddi.client.serialize.dom.UDDIExceptionDOMBinder;
import weblogic.uddi.client.structures.exception.UDDIException;
import weblogic.uddi.client.structures.exception.XML_SoapException;
import weblogic.uddi.client.structures.request.FindBinding;
import weblogic.uddi.client.structures.request.FindBusiness;
import weblogic.uddi.client.structures.request.FindService;
import weblogic.uddi.client.structures.request.FindTModel;
import weblogic.uddi.client.structures.request.GetBindingDetail;
import weblogic.uddi.client.structures.request.GetBusinessDetail;
import weblogic.uddi.client.structures.request.GetBusinessDetailExt;
import weblogic.uddi.client.structures.request.GetServiceDetail;
import weblogic.uddi.client.structures.request.GetTModelDetail;
import weblogic.uddi.client.structures.response.BindingDetail;
import weblogic.uddi.client.structures.response.BusinessDetail;
import weblogic.uddi.client.structures.response.BusinessDetailExt;
import weblogic.uddi.client.structures.response.BusinessList;
import weblogic.uddi.client.structures.response.ServiceDetail;
import weblogic.uddi.client.structures.response.ServiceList;
import weblogic.uddi.client.structures.response.TModelDetail;
import weblogic.uddi.client.structures.response.TModelList;

public class Inquiry extends UDDIService {
   public Inquiry() {
      System.setProperty("weblogic.webservice.forceXMLEncoding", "true");
      System.setProperty("weblogic.webservice.i18n.charset", "UTF-8");
      this.URL = new String("http://www-3.ibm.com/services/uddi/testregistry/inquiryapi");
   }

   public BindingDetail findBinding(FindBinding var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(FindBindingDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         BindingDetail var3 = BindingDetailDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public BusinessList findBusiness(FindBusiness var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(FindBusinessDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         BusinessList var3 = BusinessListDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public ServiceList findService(FindService var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(FindServiceDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         ServiceList var3 = ServiceListDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public TModelList findTModel(FindTModel var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(FindTModelDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         TModelList var3 = TModelListDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public BindingDetail getBindingDetail(GetBindingDetail var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(GetBindingDetailDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         BindingDetail var3 = BindingDetailDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public BusinessDetail getBusinessDetail(GetBusinessDetail var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(GetBusinessDetailDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         BusinessDetail var3 = BusinessDetailDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public BusinessDetailExt getBusinessDetailExt(GetBusinessDetailExt var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(GetBusinessDetailExtDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         BusinessDetailExt var3 = BusinessDetailExtDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public ServiceDetail getServiceDetail(GetServiceDetail var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(GetServiceDetailDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         ServiceDetail var3 = ServiceDetailDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }

   public TModelDetail getTModelDetail(GetTModelDetail var1) throws UDDIException, XML_SoapException {
      UDDISoapMessage var2 = new UDDISoapMessage();
      var2.sendMessage(GetTModelDetailDOMBinder.toDOM(var1, var2.createDOMDoc()), this.getURL());
      if (var2.isFault()) {
         throw UDDIExceptionDOMBinder.fromDOM(var2.getResult());
      } else {
         TModelDetail var3 = TModelDetailDOMBinder.fromDOM(var2.getResult());
         return var3;
      }
   }
}
