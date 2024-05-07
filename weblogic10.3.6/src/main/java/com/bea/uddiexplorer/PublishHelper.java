package com.bea.uddiexplorer;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import weblogic.logging.LogOutputStream;
import weblogic.uddi.UDDITextFormatter;
import weblogic.uddi.client.service.Inquiry;
import weblogic.uddi.client.service.Publish;
import weblogic.uddi.client.structures.datatypes.AccessPoint;
import weblogic.uddi.client.structures.datatypes.Address;
import weblogic.uddi.client.structures.datatypes.BindingTemplate;
import weblogic.uddi.client.structures.datatypes.BindingTemplates;
import weblogic.uddi.client.structures.datatypes.BusinessEntity;
import weblogic.uddi.client.structures.datatypes.BusinessInfo;
import weblogic.uddi.client.structures.datatypes.BusinessService;
import weblogic.uddi.client.structures.datatypes.CategoryBag;
import weblogic.uddi.client.structures.datatypes.Contact;
import weblogic.uddi.client.structures.datatypes.Contacts;
import weblogic.uddi.client.structures.datatypes.InstanceDetails;
import weblogic.uddi.client.structures.datatypes.OverviewDoc;
import weblogic.uddi.client.structures.datatypes.Result;
import weblogic.uddi.client.structures.datatypes.TModel;
import weblogic.uddi.client.structures.datatypes.TModelInstanceDetails;
import weblogic.uddi.client.structures.datatypes.TModelInstanceInfo;
import weblogic.uddi.client.structures.exception.UDDIException;
import weblogic.uddi.client.structures.exception.XML_SoapException;
import weblogic.uddi.client.structures.request.DeleteBusiness;
import weblogic.uddi.client.structures.request.DeleteService;
import weblogic.uddi.client.structures.request.GetAuthToken;
import weblogic.uddi.client.structures.request.GetBusinessDetail;
import weblogic.uddi.client.structures.request.GetRegisteredInfo;
import weblogic.uddi.client.structures.request.GetServiceDetail;
import weblogic.uddi.client.structures.request.GetTModelDetail;
import weblogic.uddi.client.structures.request.SaveBusiness;
import weblogic.uddi.client.structures.request.SaveService;
import weblogic.uddi.client.structures.request.SaveTModel;
import weblogic.uddi.client.structures.response.AuthToken;
import weblogic.uddi.client.structures.response.BusinessDetail;
import weblogic.uddi.client.structures.response.DispositionReport;
import weblogic.uddi.client.structures.response.RegisteredInfo;
import weblogic.uddi.client.structures.response.ServiceDetail;
import weblogic.uddi.client.structures.response.TModelDetail;

public class PublishHelper {
   private static String LOCALHOST_URL = "t3://localhost:7001";
   private String UDDI_INQUIRY_URL;
   private String UDDI_PUBLISH_URL;
   private String PUBLIC_UDDI_LINKS_FILE;
   private Inquiry m_inquiry;
   private Publish m_publish;
   private Hashtable m_categories;
   private Hashtable m_identifiers;
   private LogOutputStream log;
   public static int BUSINESS_NAME_LENGTH = 25;
   public static int CONTACT_NAME_LENGTH = 25;
   public static int CONTACT_ADDRESS_LENGTH = 30;
   public static int CONTACT_PHONE_LENGTH = 20;
   public static int CONTACT_EMAIL_LENGTH = 30;
   public static int SERVICE_NAME_LENGTH = 25;
   public static int SERVICE_DESCRIPTION_LENGTH = 35;
   public static int WSDL_LINK_LENGTH = 35;

   public PublishHelper() {
      this(LOCALHOST_URL);
   }

   public PublishHelper(String var1) {
      this.UDDI_INQUIRY_URL = "http://www-3.ibm.com/services/uddi/testregistry/inquiryapi";
      this.UDDI_PUBLISH_URL = "https://www-3.ibm.com:443/services/uddi/testregistry/protect/publishapi";
      this.PUBLIC_UDDI_LINKS_FILE = "PUBLIC_UDDI_LINKS";
      this.log = new LogOutputStream("UDDI Explorer");

      try {
         System.setProperty("weblogic.webservice.i18n.charset", "UTF-8");
         System.setProperty("weblogic.webservice.forceXMLEncoding", "true");
         System.setProperty("java.protocol.handler.pkgs", "weblogic.net");
         this.loadHashtables();
         this.m_inquiry = new Inquiry();
         this.m_publish = new Publish();
      } catch (Exception var4) {
         String var3 = (new UDDITextFormatter()).uddiExplorerPubHelperException();
         this.log.error(var3, var4);
      }

   }

   private void loadHashtables() {
      this.m_categories = new Hashtable();
      this.m_identifiers = new Hashtable();
      this.m_categories.put("GeoWeb taxonomy", "uuid:DB77450D-9FA8-45D4-A7BC-04411D14E384");
      this.m_categories.put("NAICS codes", "uuid:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2");
      this.m_categories.put("SIC codes", "uuid:70A80F61-77BC-4821-A5E2-2A406ACC35DD");
      this.m_categories.put("UNSPSC codes", "uuid:DB77450D-9FA8-45D4-A7BC-04411D14E384");
      this.m_categories.put("ISO 3166 Geographic Taxonomy", "uuid:4e49a8d6-d5a2-4fc2-93a0-0411d8d19e88");
      this.m_identifiers.put("D-U-N-S number", "UUID:8609C81E-EE1F-4D5A-B202-3EB13AD01823");
      this.m_identifiers.put("RealNames Keyword", "uuid:3bb93de3-cf9a-4f4d-b553-6537b012d0e0");
   }

   private boolean isIdentifier(String var1) {
      return this.m_identifiers.containsKey(var1);
   }

   private boolean isCategory(String var1) {
      return this.m_categories.containsKey(var1);
   }

   public void testPublishing(String var1, String var2) {
      GetAuthToken var3 = new GetAuthToken();
      var3.setUserID(var1);
      var3.setCred(var2);

      try {
         this.m_publish.getAuthToken(var3);
      } catch (Exception var6) {
         String var5 = (new UDDITextFormatter()).uddiExplorerPubHelperAuthException();
         this.log.error(var5, var6);
      }

   }

   public void setUDDIInquiryURL(String var1) {
      this.UDDI_INQUIRY_URL = var1;
      this.m_inquiry.setURL(this.UDDI_INQUIRY_URL);
   }

   public void setUDDIPublishURL(String var1) {
      this.UDDI_PUBLISH_URL = var1;
      this.m_publish.setURL(this.UDDI_PUBLISH_URL);
   }

   public AuthToken login(String var1, String var2) throws UDDIException, XML_SoapException {
      GetAuthToken var3 = new GetAuthToken();
      var3.setUserID(var1);
      var3.setCred(var2);
      AuthToken var4 = null;
      var4 = this.m_publish.getAuthToken(var3);
      return var4;
   }

   public RegisteredInfo getRegisteredInfo(AuthToken var1) throws UDDIException, XML_SoapException {
      GetRegisteredInfo var2 = new GetRegisteredInfo();
      var2.setAuthInfo(var1.getAuthInfo());
      RegisteredInfo var3 = null;
      var3 = this.m_publish.getRegisteredInfo(var2);
      return var3;
   }

   public Vector getRegisteredInfoBusinesses(AuthToken var1) throws UDDIException, XML_SoapException {
      Vector var2 = new Vector();
      RegisteredInfo var3 = this.getRegisteredInfo(var1);
      if (var3 != null && var3.getBusinessInfos() != null) {
         var2 = var3.getBusinessInfos().getBusinessInfoVector();
      }

      return var2;
   }

   public BusinessInfo getRegisteredInfoBusiness(AuthToken var1, String var2) throws UDDIException, XML_SoapException {
      BusinessInfo var3 = null;
      RegisteredInfo var4 = this.getRegisteredInfo(var1);
      if (var4 != null && var4.getBusinessInfos() != null) {
         Vector var5 = var4.getBusinessInfos().getBusinessInfoVector();

         for(int var6 = 0; var6 < var5.size(); ++var6) {
            var3 = (BusinessInfo)var5.elementAt(var6);
            if (var3.getBusinessKey().equals(var2)) {
               break;
            }

            var3 = null;
         }
      }

      return var3;
   }

   public Vector getRegisteredInfoServices(AuthToken var1, String var2) throws UDDIException, XML_SoapException {
      Vector var3 = new Vector();
      RegisteredInfo var4 = this.getRegisteredInfo(var1);
      if (var4 != null && var4.getBusinessInfos() != null) {
         Vector var5 = var4.getBusinessInfos().getBusinessInfoVector();

         for(int var6 = 0; var6 < var5.size(); ++var6) {
            BusinessInfo var7 = (BusinessInfo)var5.elementAt(var6);
            if (var7.getBusinessKey().equals(var2)) {
               if (var7.getServiceInfos() != null) {
                  var3 = var7.getServiceInfos().getServiceInfoVector();
               }
               break;
            }
         }
      }

      return var3;
   }

   public BusinessService getRegisteredInfoServiceDetail(AuthToken var1, String var2, String var3) throws UDDIException, XML_SoapException {
      BusinessService var4 = null;
      BusinessInfo var5 = this.getRegisteredInfoBusiness(var1, var2);
      if (var5 != null) {
         GetServiceDetail var6 = new GetServiceDetail();
         var6.addServiceKey(var3);
         ServiceDetail var7 = this.m_inquiry.getServiceDetail(var6);
         var4 = (BusinessService)var7.getBusinessServiceVector().elementAt(0);
      }

      return var4;
   }

   public Vector getRegisteredInfoContacts(AuthToken var1, String var2) throws UDDIException, XML_SoapException {
      Vector var3 = new Vector();
      BusinessDetail var4 = null;
      GetBusinessDetail var5 = new GetBusinessDetail();
      var5.addBusinessKey(var2);
      var4 = this.m_inquiry.getBusinessDetail(var5);
      BusinessEntity var6 = (BusinessEntity)var4.getBusinessEntityVector().elementAt(0);
      if (var6.getContacts() != null) {
         var3 = var6.getContacts().getContactVector();
      }

      return var3;
   }

   public Contact getRegisteredInfoContact(AuthToken var1, String var2, String var3) throws UDDIException, XML_SoapException {
      Contact var4 = null;
      new Vector();
      BusinessDetail var6 = null;
      GetBusinessDetail var7 = new GetBusinessDetail();
      var7.addBusinessKey(var2);
      var6 = this.m_inquiry.getBusinessDetail(var7);
      BusinessEntity var8 = (BusinessEntity)var6.getBusinessEntityVector().elementAt(0);
      Vector var5 = var8.getContacts().getContactVector();

      for(int var9 = 0; var9 < var5.size(); ++var9) {
         Contact var10 = (Contact)var5.elementAt(var9);
         if (var10.getPersonName().getValue().equals(var3)) {
            var4 = var10;
            break;
         }
      }

      return var4;
   }

   public String getServiceWSDL(BusinessService var1) {
      String var2 = "";

      try {
         if (var1.getBindingTemplates() != null && var1.getBindingTemplates().getBindingTemplateVector().size() > 0) {
            BindingTemplate var3 = (BindingTemplate)var1.getBindingTemplates().getBindingTemplateVector().elementAt(0);
            if (var3.getTModelInstanceDetails() != null && var3.getTModelInstanceDetails().getTModelInstanceInfoVector().size() > 0) {
               TModelInstanceInfo var4 = (TModelInstanceInfo)var3.getTModelInstanceDetails().getTModelInstanceInfoVector().elementAt(0);
               if (var4.getInstanceDetails() != null) {
                  OverviewDoc var5 = var4.getInstanceDetails().getOverviewDoc();
                  if (var5 != null && var5.getOverviewURL() != null) {
                     var2 = var5.getOverviewURL().getValue();
                  }
               }
            }
         }
      } catch (NullPointerException var6) {
      }

      return var2;
   }

   public boolean validBusinessName(AuthToken var1, String var2) {
      return true;
   }

   public BusinessDetail saveBusiness(AuthToken var1, String var2) throws UDDIException, XML_SoapException {
      BusinessDetail var3 = null;
      SaveBusiness var4 = new SaveBusiness();
      var4.setAuthInfo(var1.getAuthInfo());
      BusinessEntity var5 = new BusinessEntity();
      var5.setName(var2);
      var5.setBusinessKey("");
      var4.addBusinessEntity(var5);
      var3 = this.m_publish.saveBusiness(var4);
      return var3;
   }

   public BusinessDetail saveBusiness(AuthToken var1, String var2, String var3, String var4, String var5, String var6) throws UDDIException, XML_SoapException {
      BusinessDetail var7 = null;
      if (var2 == null || var2.equals("")) {
         var7 = this.saveBusiness(var1, var3);
         var2 = ((BusinessEntity)var7.getBusinessEntityVector().elementAt(0)).getBusinessKey();
      }

      this.saveService(var1, var2, "", var4, var5, var6);
      GetBusinessDetail var8 = new GetBusinessDetail();
      var8.addBusinessKey(var2);
      var7 = this.m_inquiry.getBusinessDetail(var8);
      return var7;
   }

   public TModelDetail createTModel(AuthToken var1, String var2, String var3) throws UDDIException, XML_SoapException {
      TModelDetail var4 = null;
      GetTModelDetail var5 = new GetTModelDetail();
      var5.addTModelKey("uuid:C1ACF26D-9672-4404-9D70-39B756E62AB4");

      try {
         this.m_inquiry.getTModelDetail(var5);
      } catch (UDDIException var10) {
         SaveTModel var7 = new SaveTModel();
         var7.setAuthInfo(var1.getAuthInfo());
         TModel var8 = new TModel();
         var8.setTModelKey("uuid:C1ACF26D-9672-4404-9D70-39B756E62AB4");
         var8.setName("uddi-org:types");
         var8.addDescription("UDDI Type Taxonomy");
         OverviewDoc var9 = new OverviewDoc();
         var9.setOverviewURL("http://www.uddi.org");
         var8.setOverviewDoc(var9);
         var7.addTModel(var8);
         this.m_publish.saveTModel(var7);
      }

      SaveTModel var6 = new SaveTModel();
      var6.setAuthInfo(var1.getAuthInfo());
      TModel var11 = new TModel();
      var11.setTModelKey("");
      var11.setName(var2);
      if (!StringUtils.isEmpty(var3)) {
         var11.addDescription(var3);
      }

      OverviewDoc var12 = new OverviewDoc();
      var12.setOverviewURL(var2);
      var11.setOverviewDoc(var12);
      CategoryBag var13 = new CategoryBag();
      var13.addKeyedReference("uuid:C1ACF26D-9672-4404-9D70-39B756E62AB4", "uddi-org:types", "wsdlSpec");
      var11.setCategoryBag(var13);
      var6.addTModel(var11);
      var4 = this.m_publish.saveTModel(var6);
      return var4;
   }

   public DispositionReport deleteService(AuthToken var1, String var2) throws UDDIException, XML_SoapException {
      DeleteService var3 = new DeleteService();
      var3.setAuthInfo(var1.getAuthInfo());
      var3.addServiceKey(var2);
      DispositionReport var4 = null;
      var4 = this.m_publish.deleteService(var3);
      return var4;
   }

   public BusinessDetail addContact(AuthToken var1, String var2, String var3, String var4, String var5, String var6, String var7) throws UDDIException, XML_SoapException {
      new Vector();
      BusinessDetail var9 = null;
      if (var2 != null && !var2.equals("")) {
         GetBusinessDetail var10 = new GetBusinessDetail();
         var10.addBusinessKey(var2);
         var9 = this.m_inquiry.getBusinessDetail(var10);
      } else {
         var9 = this.saveBusiness(var1, var3);
      }

      if (var9.getBusinessEntityVector().size() > 0) {
         BusinessEntity var15 = (BusinessEntity)var9.getBusinessEntityVector().elementAt(0);
         if (var15.getContacts() == null) {
            Contacts var11 = new Contacts();
            var15.setContacts(var11);
         }

         Vector var8 = var15.getContacts().getContactVector();
         Contact var16 = new Contact();
         var16.setPersonName(var4);
         Address var12 = new Address();
         var12.addAddressLine(var5);
         var16.addAddress(var12);
         var16.addPhone(var6);
         var16.addEmail(var7);
         var8.add(var16);
         SaveBusiness var13 = new SaveBusiness();
         var13.setAuthInfo(var1.getAuthInfo());
         Vector var14 = new Vector();
         var14.add(var15);
         var13.setBusinessEntityVector(var14);
         var9 = this.m_publish.saveBusiness(var13);
      }

      return var9;
   }

   public BusinessDetail deleteContact(AuthToken var1, String var2, String var3) throws UDDIException, XML_SoapException {
      Vector var4 = new Vector();
      BusinessDetail var5 = null;
      GetBusinessDetail var6 = new GetBusinessDetail();
      var6.addBusinessKey(var2);
      var5 = this.m_inquiry.getBusinessDetail(var6);
      BusinessEntity var7 = (BusinessEntity)var5.getBusinessEntityVector().elementAt(0);
      if (var7.getContacts() != null) {
         var4 = var7.getContacts().getContactVector();
      }

      for(int var8 = 0; var8 < var4.size(); ++var8) {
         Contact var9 = (Contact)var4.elementAt(var8);
         if (var9.getPersonName().getValue().equals(var3)) {
            var4.remove(var9);
         }
      }

      SaveBusiness var10 = new SaveBusiness();
      var10.setAuthInfo(var1.getAuthInfo());
      Vector var11 = new Vector();
      var11.add(var7);
      var10.setBusinessEntityVector(var11);
      var5 = this.m_publish.saveBusiness(var10);
      return var5;
   }

   public BusinessDetail editContact(AuthToken var1, String var2, String var3, String var4, String var5, String var6, String var7) throws UDDIException, XML_SoapException {
      BusinessDetail var8 = null;
      GetBusinessDetail var9 = new GetBusinessDetail();
      var9.addBusinessKey(var2);
      Vector var10 = new Vector();
      BusinessDetail var11 = this.m_inquiry.getBusinessDetail(var9);
      BusinessEntity var12 = (BusinessEntity)var11.getBusinessEntityVector().elementAt(0);
      if (var12.getContacts() != null) {
         var10 = var12.getContacts().getContactVector();
      }

      for(int var13 = 0; var13 < var10.size(); ++var13) {
         Contact var14 = (Contact)var10.elementAt(var13);
         if (var14.getPersonName().getValue().equals(var3)) {
            var14.setPersonName(var4);
            var14.setAddressVector(new Vector());
            Address var15 = new Address();
            var15.addAddressLine(var5);
            var14.addAddress(var15);
            var14.setPhoneVector(new Vector());
            var14.addPhone(var6);
            var14.setEmailVector(new Vector());
            var14.addEmail(var7);
            break;
         }
      }

      SaveBusiness var16 = new SaveBusiness();
      var16.setAuthInfo(var1.getAuthInfo());
      var16.addBusinessEntity(var12);
      var8 = this.m_publish.saveBusiness(var16);
      return var8;
   }

   public BusinessDetail editBusinessName(AuthToken var1, String var2, String var3) throws UDDIException, XML_SoapException {
      BusinessDetail var4 = null;
      GetBusinessDetail var5 = new GetBusinessDetail();
      var5.addBusinessKey(var2);
      BusinessDetail var6 = this.m_inquiry.getBusinessDetail(var5);
      BusinessEntity var7 = (BusinessEntity)var6.getBusinessEntityVector().elementAt(0);
      if (var7 != null) {
         var7.setName(var3);
         SaveBusiness var8 = new SaveBusiness();
         var8.setAuthInfo(var1.getAuthInfo());
         Vector var9 = new Vector();
         var9.add(var7);
         var8.setBusinessEntityVector(var9);
         var4 = this.m_publish.saveBusiness(var8);
      }

      return var4;
   }

   public DispositionReport deleteBusiness(AuthToken var1, String var2) throws UDDIException, XML_SoapException {
      DispositionReport var3 = null;
      DeleteBusiness var4 = new DeleteBusiness();
      var4.setAuthInfo(var1.getAuthInfo());
      var4.addBusinessKey(var2);
      var3 = this.m_publish.deleteBusiness(var4);
      return var3;
   }

   public ServiceDetail saveService(AuthToken var1, String var2, String var3, String var4, String var5, String var6) throws UDDIException, XML_SoapException {
      return this.saveService(var1, var2, var3, var4, "", var5, var6);
   }

   public ServiceDetail saveService(AuthToken var1, String var2, String var3, String var4, String var5, String var6, String var7) throws UDDIException, XML_SoapException {
      ServiceDetail var8 = null;
      SaveService var9 = new SaveService();
      var9.setAuthInfo(var1.getAuthInfo());
      BusinessService var10 = new BusinessService();
      var10.setBusinessKey(var2);
      var10.setServiceKey(var3);
      var10.setName(var4);
      if (!StringUtils.isEmpty(var7)) {
         var10.addDescription(var7);
      }

      TModelDetail var11 = this.createTModel(var1, var6, var7);
      if (var11.getTModelVector().size() > 0) {
         TModel var12 = (TModel)var11.getTModelVector().elementAt(0);
         OverviewDoc var13 = new OverviewDoc();
         if (var12.getOverviewDoc() != null && var12.getOverviewDoc().getOverviewURL() != null) {
            var13.setOverviewURL(var12.getOverviewDoc().getOverviewURL().getValue());
         }

         InstanceDetails var14 = new InstanceDetails();
         var14.setOverviewDoc(var13);
         TModelInstanceInfo var15 = new TModelInstanceInfo();
         var15.setInstanceDetails(var14);
         var15.setTModelKey(var12.getTModelKey());
         TModelInstanceDetails var16 = new TModelInstanceDetails();
         var16.addTModelInstanceInfo(var15);
         AccessPoint var17 = new AccessPoint();
         var17.setURLType("http");
         var17.setValue("http://www.nothing.com");
         if (var6 != null) {
            var17.setValue(var6);
         }

         BindingTemplate var18 = new BindingTemplate();
         var18.setBindingKey(var5);
         var18.setAccessPoint(var17);
         var18.setTModelInstanceDetails(var16);
         BindingTemplates var19 = new BindingTemplates();
         var19.addBindingTemplate(var18);
         var10.setBindingTemplates(var19);
         var9.addBusinessService(var10);
         var8 = this.m_publish.saveService(var9);
      }

      return var8;
   }

   public String dumpError(DispositionReport var1, HttpSession var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append("<p>An error has occurred<BR>");

      for(int var4 = 0; var4 < var1.getResultVector().size(); ++var4) {
         Result var5 = (Result)var1.getResultVector().elementAt(var4);
         String var6 = var5.getErrInfo().getValue();
         String var7 = var5.getErrInfo().getErrCode();
         String var8 = var5.getErrno();
         var3.append(var7 + "(" + var8 + "): " + var6);
         if (StringUtils.equals(var7, "E_authTokenExpired") || StringUtils.equals(var7, "E_authTokenRequired")) {
            var2.setAttribute("authToken", (Object)null);
         }
      }

      return var3.toString();
   }
}
