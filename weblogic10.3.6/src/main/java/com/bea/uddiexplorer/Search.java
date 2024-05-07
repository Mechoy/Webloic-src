package com.bea.uddiexplorer;

import java.util.Hashtable;
import java.util.Vector;
import weblogic.logging.LogOutputStream;
import weblogic.uddi.UDDITextFormatter;
import weblogic.uddi.client.service.Inquiry;
import weblogic.uddi.client.service.Publish;
import weblogic.uddi.client.structures.datatypes.Address;
import weblogic.uddi.client.structures.datatypes.AddressLine;
import weblogic.uddi.client.structures.datatypes.BindingTemplate;
import weblogic.uddi.client.structures.datatypes.BusinessEntity;
import weblogic.uddi.client.structures.datatypes.BusinessInfo;
import weblogic.uddi.client.structures.datatypes.BusinessInfos;
import weblogic.uddi.client.structures.datatypes.BusinessServices;
import weblogic.uddi.client.structures.datatypes.CategoryBag;
import weblogic.uddi.client.structures.datatypes.Contact;
import weblogic.uddi.client.structures.datatypes.DiscoveryURL;
import weblogic.uddi.client.structures.datatypes.DiscoveryURLs;
import weblogic.uddi.client.structures.datatypes.FindQualifiers;
import weblogic.uddi.client.structures.datatypes.IdentifierBag;
import weblogic.uddi.client.structures.datatypes.Name;
import weblogic.uddi.client.structures.datatypes.Result;
import weblogic.uddi.client.structures.datatypes.ServiceInfos;
import weblogic.uddi.client.structures.datatypes.TModel;
import weblogic.uddi.client.structures.datatypes.TModelInfo;
import weblogic.uddi.client.structures.datatypes.TModelInstanceDetails;
import weblogic.uddi.client.structures.datatypes.TModelInstanceInfo;
import weblogic.uddi.client.structures.exception.UDDIException;
import weblogic.uddi.client.structures.exception.XML_SoapException;
import weblogic.uddi.client.structures.request.FindBusiness;
import weblogic.uddi.client.structures.request.FindService;
import weblogic.uddi.client.structures.request.FindTModel;
import weblogic.uddi.client.structures.request.GetBindingDetail;
import weblogic.uddi.client.structures.request.GetBusinessDetail;
import weblogic.uddi.client.structures.request.GetServiceDetail;
import weblogic.uddi.client.structures.request.GetTModelDetail;
import weblogic.uddi.client.structures.response.BindingDetail;
import weblogic.uddi.client.structures.response.BusinessDetail;
import weblogic.uddi.client.structures.response.BusinessList;
import weblogic.uddi.client.structures.response.DispositionReport;
import weblogic.uddi.client.structures.response.ServiceDetail;
import weblogic.uddi.client.structures.response.ServiceList;
import weblogic.uddi.client.structures.response.TModelDetail;
import weblogic.uddi.client.structures.response.TModelList;

public class Search {
   private static String LOCALHOST_URL = "t3://localhost:7001";
   private String UDDI_INQUIRY_URL;
   private String UDDI_PUBLISH_URL;
   private String PUBLIC_UDDI_LINKS_FILE;
   private String m_operator;
   private Inquiry m_inquiry;
   private Publish m_publish;
   private LogOutputStream log;
   private String[][] m_operators;
   private String[] m_selectOptions;
   private Hashtable m_categories;
   private Hashtable m_identifiers;

   public Search() {
      this(LOCALHOST_URL);
   }

   public Search(String var1) {
      this.UDDI_INQUIRY_URL = "http://www-3.ibm.com/services/uddi/testregistry/inquiryapi";
      this.UDDI_PUBLISH_URL = "https://www-3.ibm.com:443/services/uddi/testregistry/protect/publishapi";
      this.PUBLIC_UDDI_LINKS_FILE = "PUBLIC_UDDI_LINKS";
      this.log = new LogOutputStream("UDDI Explorer");
      this.m_selectOptions = new String[]{"Business location", "tModel by name", "D-U-N-S number", "Business URL", "GeoWeb taxonomy", "NAICS codes", "SIC codes", "UNSPSC codes", "ISO 3166 Geographic Taxonomy"};

      try {
         System.setProperty("java.protocol.handler.pkgs", "weblogic.net");
         this.loadHashtables();
         this.m_inquiry = new Inquiry();
         this.m_publish = new Publish();
      } catch (Exception var4) {
         String var3 = (new UDDITextFormatter()).uddiExplorerSearchException();
         this.log.error(var3, var4);
      }

   }

   public void setOperator(String var1) {
      try {
         this.m_operator = var1;
         this.setUDDIInquiryURL(var1);
      } catch (Exception var4) {
         String var3 = (new UDDITextFormatter()).uddiExplorerSearchOpException();
         this.log.error(var3, var4);
      }

   }

   public String[][] getOperators() {
      return this.m_operators;
   }

   public String[] getSelectOptions() {
      return this.m_selectOptions;
   }

   public Object getPrivateSearchResponse(String var1, String var2, String var3, String var4) throws UDDIException, XML_SoapException {
      Object var5 = null;
      if (var1.equalsIgnoreCase("listallservices")) {
         var5 = this.getAllServices();
      } else if (var1.equalsIgnoreCase("deptproject")) {
         var5 = this.findBusinessListByName(var2);
      } else if (var1.equalsIgnoreCase("servicename")) {
         var5 = this.findServiceListByName(var3);
      } else if (var1.equalsIgnoreCase("person")) {
      }

      return var5;
   }

   public Object getResponse(String var1, String var2, String var3, String var4, String var5) throws UDDIException, XML_SoapException {
      Object var6 = null;
      if (var1.equalsIgnoreCase("name")) {
         var6 = this.findBusinessListByName(var2);
      } else if (var1.equalsIgnoreCase("key")) {
         try {
            var6 = this.getBusinessDetail(var3);
         } catch (UDDIException var11) {
            DispositionReport var8 = var11.getDispositionReport();

            for(int var9 = 0; var9 < var8.getResultVector().size(); ++var9) {
               Result var10 = (Result)var8.getResultVector().elementAt(var9);
               if (!var10.getErrno().equals("10210")) {
                  throw var11;
               }
            }
         }

         if (var6 == null || ((BusinessDetail)var6).getBusinessEntityVector().size() == 0) {
            var6 = this.getServiceDetail(var3);
         }
      } else if (var1.equalsIgnoreCase("for")) {
         if (var5.equalsIgnoreCase("tModel by name")) {
            var6 = this.findBusinessListByTModelName(var4);
         } else if (var5.equalsIgnoreCase("Business location")) {
            var6 = this.findBusinessListByLocation(var4);
         } else if (var5.equalsIgnoreCase("Business URL")) {
            var6 = this.findBusinessListByBusinessURL(var4);
         } else if (this.isIdentifier(var5)) {
            var6 = this.findBusinessListByIdentifier(var5, var4);
         } else if (this.isCategory(var5)) {
            var6 = this.findBusinessListByCategory(var5, var4);
         }
      }

      return var6;
   }

   private BusinessList getAllBusinesses() throws UDDIException, XML_SoapException {
      return this.findBusinessListByName("%");
   }

   private BusinessServices getAllServices() throws UDDIException, XML_SoapException {
      BusinessServices var1 = new BusinessServices();
      Vector var2 = new Vector();
      BusinessList var3 = this.getAllBusinesses();
      if (var3 != null && var3.getBusinessInfos() != null) {
         Vector var4 = var3.getBusinessInfos().getBusinessInfoVector();
         if (var4 != null && var4.size() > 0) {
            for(int var5 = 0; var5 < var4.size(); ++var5) {
               BusinessInfo var6 = (BusinessInfo)var4.elementAt(var5);
               String var7 = var6.getBusinessKey();
               BusinessDetail var8 = this.getBusinessDetail(var7);
               Vector var9 = var8.getBusinessEntityVector();
               if (var9 != null) {
                  for(int var10 = 0; var10 < var9.size(); ++var10) {
                     BusinessEntity var11 = (BusinessEntity)var9.elementAt(var10);
                     if (var11.getBusinessServices() != null) {
                        Vector var12 = var11.getBusinessServices().getBusinessServiceVector();
                        if (var12 != null && var12.size() > 0) {
                           var2.addAll(var12);
                        }
                     }
                  }

                  var1.setBusinessServiceVector(var2);
               }
            }
         }
      }

      return var1;
   }

   private ServiceInfos findServiceListByName(String var1) throws UDDIException, XML_SoapException {
      ServiceInfos var2 = new ServiceInfos();
      Vector var3 = new Vector();
      BusinessList var4 = this.getAllBusinesses();
      if (var4 != null && var4.getBusinessInfos() != null) {
         Vector var5 = var4.getBusinessInfos().getBusinessInfoVector();
         if (var5 != null && var5.size() > 0) {
            for(int var6 = 0; var6 < var5.size(); ++var6) {
               BusinessInfo var7 = (BusinessInfo)var5.elementAt(var6);
               String var8 = var7.getBusinessKey();
               FindService var9 = new FindService();
               var9.setBusinessKey(var8);
               var9.setName(new Name(var1));
               ServiceList var10 = null;

               try {
                  var10 = this.m_inquiry.findService(var9);
               } catch (Exception var13) {
                  String var12 = (new UDDITextFormatter()).uddiExplorerSearchInfoException();
                  this.log.error(var12, var13);
               }

               if (var10 != null && var10.getServiceInfos() != null) {
                  Vector var11 = var10.getServiceInfos().getServiceInfoVector();
                  if (var11 != null && var11.size() > 0) {
                     var3.addAll(var11);
                  }
               }
            }

            var2.setServiceInfoVector(var3);
         }
      }

      return var2;
   }

   private BusinessList findBusinessListByName(String var1) throws UDDIException, XML_SoapException {
      BusinessList var2 = null;
      FindBusiness var3 = new FindBusiness();
      var3.setName(new Name(var1));
      var2 = this.m_inquiry.findBusiness(var3);
      return var2;
   }

   public TModelDetail findBusinessListByTModelName(String var1) throws UDDIException, XML_SoapException {
      TModelDetail var2 = new TModelDetail();
      FindTModel var3 = new FindTModel();
      var3.setName(new Name(var1));
      TModelList var4 = this.m_inquiry.findTModel(var3);
      if (var4.getTModelInfos() != null && var4.getTModelInfos().getTModelInfoVector().size() > 0) {
         Vector var5 = var4.getTModelInfos().getTModelInfoVector();
         GetTModelDetail var6 = new GetTModelDetail();

         for(int var7 = 0; var7 < var5.size(); ++var7) {
            var6.addTModelKey(((TModelInfo)var5.elementAt(var7)).getTModelKey());
         }

         var2 = this.m_inquiry.getTModelDetail(var6);
      }

      return var2;
   }

   private BusinessList findBusinessListByBusinessURL(String var1) throws UDDIException, XML_SoapException {
      BusinessList var2 = null;
      FindBusiness var3 = new FindBusiness();
      DiscoveryURLs var4 = new DiscoveryURLs();
      DiscoveryURL var5 = new DiscoveryURL(var1);
      var5.setUseType("businessEntity");
      var4.addDiscoveryURL(var5);
      var3.setDiscoveryURLs(var4);
      var2 = this.m_inquiry.findBusiness(var3);
      return var2;
   }

   private BusinessList findBusinessListByLocation(String var1) throws UDDIException, XML_SoapException {
      BusinessList var2 = new BusinessList();
      Vector var3 = new Vector();
      BusinessList var4 = this.getAllBusinesses();

      for(int var5 = 0; var5 < var4.getBusinessInfos().getBusinessInfoVector().size(); ++var5) {
         BusinessInfo var6 = (BusinessInfo)var4.getBusinessInfos().getBusinessInfoVector().elementAt(var5);
         BusinessDetail var7 = this.getBusinessDetail(var6.getBusinessKey());
         if (var7.getBusinessEntityVector().size() > 0) {
            boolean var8 = false;
            BusinessEntity var9 = (BusinessEntity)var7.getBusinessEntityVector().elementAt(0);
            if (var9.getContacts() != null) {
               Vector var10 = var9.getContacts().getContactVector();

               for(int var11 = 0; var11 < var10.size() && !var8; ++var11) {
                  Contact var12 = (Contact)var10.elementAt(var11);
                  Vector var13 = var12.getAddressVector();

                  for(int var14 = 0; var14 < var13.size() && !var8; ++var14) {
                     Address var15 = (Address)var13.elementAt(var14);

                     for(int var16 = 0; var16 < var15.getAddressLineVector().size(); ++var16) {
                        AddressLine var17 = (AddressLine)var15.getAddressLineVector().elementAt(var16);
                        String var18 = var17.getValue().toLowerCase();
                        if (var18.indexOf(var1.toLowerCase()) != -1) {
                           var3.add(var6);
                           var8 = true;
                           break;
                        }
                     }
                  }
               }
            }
         }
      }

      BusinessInfos var19 = new BusinessInfos();
      var19.setBusinessInfoVector(var3);
      var2.setBusinessInfos(var19);
      return var2;
   }

   private BusinessList findBusinessListByCategory(String var1, String var2) throws UDDIException, XML_SoapException {
      BusinessList var3 = null;
      FindBusiness var4 = new FindBusiness();
      CategoryBag var5 = new CategoryBag();
      String var6 = (String)this.m_categories.get(var1);
      if (var6.indexOf("?") == -1) {
         var5.addKeyedReference(var6, var1, var2);
      } else {
         FindQualifiers var7 = new FindQualifiers();
         var7.addFindQualifier("orAllKeys");
         var4.setFindQualifiers(var7);
         var5.addKeyedReference(var6.substring(0, var6.indexOf("?")), var1, var2);
         var5.addKeyedReference(var6.substring(var6.indexOf("?") + 1, var6.length()), var1, var2);
      }

      var4.setCategoryBag(var5);
      var3 = this.m_inquiry.findBusiness(var4);
      return var3;
   }

   private BusinessList findBusinessListByIdentifier(String var1, String var2) throws UDDIException, XML_SoapException {
      BusinessList var3 = null;
      FindBusiness var4 = new FindBusiness();
      IdentifierBag var5 = new IdentifierBag();
      var5.addKeyedReference((String)this.m_identifiers.get(var1), var1, var2);
      var4.setIdentifierBag(var5);
      var3 = this.m_inquiry.findBusiness(var4);
      return var3;
   }

   private void loadHashtables() {
      this.m_categories = new Hashtable();
      this.m_identifiers = new Hashtable();
      this.m_categories.put("GeoWeb taxonomy", "UUID:297AAA47-2DE3-4454-A04A-CF38E889D0C4");
      this.m_categories.put("NAICS codes", "UUID:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2");
      this.m_categories.put("SIC codes", "UUID:70A80F61-77BC-4821-A5E2-2A406ACC35DD");
      this.m_categories.put("UNSPSC codes", "UUID:CD153257-086A-4237-B336-6BDCBDCC6634?UUID:DB77450D-9FA8-45D4-A7BC-04411D14E384");
      this.m_categories.put("ISO 3166 Geographic Taxonomy", "UUID:4E49A8D6-D5A2-4FC2-93A0-0411D8D19E88");
      this.m_identifiers.put("D-U-N-S number", "UUID:8609C81E-EE1F-4D5A-B202-3EB13AD01823");
   }

   private boolean isIdentifier(String var1) {
      return this.m_identifiers.containsKey(var1);
   }

   private boolean isCategory(String var1) {
      return this.m_categories.containsKey(var1);
   }

   public TModelDetail getTModelDetail(String var1) throws UDDIException, XML_SoapException {
      TModelDetail var2 = null;
      GetTModelDetail var3 = new GetTModelDetail();
      var3.addTModelKey(var1);
      var2 = this.m_inquiry.getTModelDetail(var3);
      return var2;
   }

   public BindingDetail getBindingDetail(String var1) throws UDDIException, XML_SoapException {
      BindingDetail var2 = null;
      GetBindingDetail var3 = new GetBindingDetail();
      var3.addBindingKey(var1);
      var2 = this.m_inquiry.getBindingDetail(var3);
      return var2;
   }

   public BusinessDetail getBusinessDetail(String var1) throws UDDIException, XML_SoapException {
      BusinessDetail var2 = null;
      GetBusinessDetail var3 = new GetBusinessDetail();
      var3.addBusinessKey(var1);
      var2 = this.m_inquiry.getBusinessDetail(var3);
      return var2;
   }

   public ServiceDetail getServiceDetail(String var1) throws UDDIException, XML_SoapException {
      ServiceDetail var2 = null;
      GetServiceDetail var3 = new GetServiceDetail();
      var3.addServiceKey(var1);
      var2 = this.m_inquiry.getServiceDetail(var3);
      return var2;
   }

   public Vector getOverviewURLs(String var1) throws UDDIException, XML_SoapException {
      Vector var2 = new Vector();
      BindingDetail var3 = this.getBindingDetail(var1);
      if (var3 != null && var3.getBindingTemplateVector().size() > 0) {
         BindingTemplate var4 = (BindingTemplate)var3.getBindingTemplateVector().elementAt(0);
         TModelInstanceDetails var5 = var4.getTModelInstanceDetails();
         if (var5.getTModelInstanceInfoVector() != null && var5.getTModelInstanceInfoVector().size() > 0) {
            for(int var6 = 0; var6 < var5.getTModelInstanceInfoVector().size(); ++var6) {
               TModelInstanceInfo var7 = (TModelInstanceInfo)var5.getTModelInstanceInfoVector().elementAt(var6);
               String var8 = var7.getTModelKey();
               TModelDetail var9 = this.getTModelDetail(var8);
               if (var9 != null && var9.getTModelVector().size() > 0) {
                  TModel var10 = (TModel)var9.getTModelVector().elementAt(0);
                  if (var10.getOverviewDoc() != null && var10.getOverviewDoc().getOverviewURL() != null) {
                     String var11 = var10.getOverviewDoc().getOverviewURL().getValue();
                     if (var11 != null) {
                        var2.add(var11);
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   public void setUDDIInquiryURL(String var1) {
      this.UDDI_INQUIRY_URL = var1;
      this.m_inquiry.setURL(this.UDDI_INQUIRY_URL);
   }

   public void setUDDIPublishURL(String var1) {
      this.UDDI_PUBLISH_URL = var1;
      this.m_publish.setURL(this.UDDI_INQUIRY_URL);
   }
}
