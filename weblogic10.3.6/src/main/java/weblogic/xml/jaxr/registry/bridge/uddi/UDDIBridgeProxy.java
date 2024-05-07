package weblogic.xml.jaxr.registry.bridge.uddi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Association;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.LocalizedString;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.Slot;
import weblogic.auddi.soap.SOAPWrapper;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.client.UDDIProxy;
import weblogic.auddi.uddi.datastructure.AssertionStatusItem;
import weblogic.auddi.uddi.datastructure.AssertionStatusItems;
import weblogic.auddi.uddi.datastructure.BindingKey;
import weblogic.auddi.uddi.datastructure.BindingTemplate;
import weblogic.auddi.uddi.datastructure.BindingTemplates;
import weblogic.auddi.uddi.datastructure.BusinessEntities;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.datastructure.TModels;
import weblogic.auddi.uddi.request.UDDIRequest;
import weblogic.auddi.uddi.request.inquiry.FindBindingRequest;
import weblogic.auddi.uddi.request.inquiry.FindBusinessRequest;
import weblogic.auddi.uddi.request.inquiry.FindRelatedBusinessesRequest;
import weblogic.auddi.uddi.request.inquiry.FindServiceRequest;
import weblogic.auddi.uddi.request.inquiry.FindTModelRequest;
import weblogic.auddi.uddi.request.inquiry.GetBindingDetailRequest;
import weblogic.auddi.uddi.request.inquiry.GetBusinessDetailRequest;
import weblogic.auddi.uddi.request.inquiry.GetServiceDetailRequest;
import weblogic.auddi.uddi.request.inquiry.GetTModelDetailRequest;
import weblogic.auddi.uddi.request.publish.DeleteBindingRequest;
import weblogic.auddi.uddi.request.publish.DeleteBusinessRequest;
import weblogic.auddi.uddi.request.publish.DeletePublisherAssertionsRequest;
import weblogic.auddi.uddi.request.publish.DeleteServiceRequest;
import weblogic.auddi.uddi.request.publish.DeleteTModelRequest;
import weblogic.auddi.uddi.request.publish.GetAssertionStatusReportRequest;
import weblogic.auddi.uddi.request.publish.GetRegisteredInfoRequest;
import weblogic.auddi.uddi.request.publish.SaveBindingRequest;
import weblogic.auddi.uddi.request.publish.SaveBusinessRequest;
import weblogic.auddi.uddi.request.publish.SaveServiceRequest;
import weblogic.auddi.uddi.request.publish.SaveTModelRequest;
import weblogic.auddi.uddi.request.publish.UDDIPublishRequest;
import weblogic.auddi.uddi.response.AssertionStatusReportResponse;
import weblogic.auddi.uddi.response.BindingDetailResponse;
import weblogic.auddi.uddi.response.BusinessDetailResponse;
import weblogic.auddi.uddi.response.BusinessInfo;
import weblogic.auddi.uddi.response.BusinessInfos;
import weblogic.auddi.uddi.response.BusinessListResponse;
import weblogic.auddi.uddi.response.RegisteredInfoResponse;
import weblogic.auddi.uddi.response.RelatedBusinessListResponse;
import weblogic.auddi.uddi.response.ServiceDetailResponse;
import weblogic.auddi.uddi.response.ServiceInfo;
import weblogic.auddi.uddi.response.ServiceInfos;
import weblogic.auddi.uddi.response.ServiceListResponse;
import weblogic.auddi.uddi.response.TModelDetailResponse;
import weblogic.auddi.uddi.response.TModelInfo;
import weblogic.auddi.uddi.response.TModelInfos;
import weblogic.auddi.uddi.response.TModelListResponse;
import weblogic.auddi.uddi.response.UDDIResponse;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;
import weblogic.xml.jaxr.registry.BulkResponseImpl;
import weblogic.xml.jaxr.registry.BusinessLifeCycleManagerImpl;
import weblogic.xml.jaxr.registry.ConnectionImpl;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.provider.BaseRegistryProxy;
import weblogic.xml.jaxr.registry.provider.InternalClassificationSchemes;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;
import weblogic.xml.jaxr.registry.util.ClassificationSchemeHelper;

public class UDDIBridgeProxy extends BaseRegistryProxy {
   private UDDIProxy m_uddiProxy;
   private RegistryServiceImpl m_registryServiceImpl;
   private UDDITaxonomies m_helper;
   private int m_maxRows;

   public UDDIBridgeProxy(RegistryServiceImpl var1) throws JAXRException {
      Logger.debug("+UDDIBridgeProxy.CTOR");
      this.m_registryServiceImpl = var1;
      this.m_helper = new UDDITaxonomies();
      ConnectionImpl var2 = var1.getConnectionImpl();
      URL var3 = var2.getQueryURL();
      URL var4 = var2.getPublishURL();
      this.m_uddiProxy = new UDDIProxy();
      Integer var5 = var2.getMaxRows();
      if (var5 != null) {
         this.m_maxRows = var5;
      } else {
         this.m_maxRows = Integer.MIN_VALUE;
      }

      try {
         initializeAuddiClient();
      } catch (UDDIException var8) {
         throw UDDIBridgeMapperUtil.mapException(var8);
      } catch (IOException var9) {
         throw new InvalidRequestException(var9);
      }

      try {
         this.m_uddiProxy.setInquiryURL(var3.toExternalForm());
         this.m_uddiProxy.setPublishURL(var4.toExternalForm());
      } catch (MalformedURLException var7) {
         throw new InvalidRequestException(var7);
      }

      Logger.debug("-UDDIBridgeProxy.CTOR");
   }

   public void setCredentials(String var1, String var2) throws JAXRException {
      this.m_uddiProxy.setCredential(var1, var2);
   }

   public void closeConnection() throws JAXRException {
      this.m_uddiProxy = null;
   }

   public InternalClassificationSchemes getInternalClassificationSchemes() throws JAXRException {
      return this.m_helper;
   }

   public String getRegistryObjectOwner(RegistryObject var1) throws JAXRException {
      String var2;
      if (var1 == null) {
         var2 = null;
      } else {
         Slot var3 = var1.getSlot("authorizedName");
         if (var3 == null) {
            var2 = null;
         } else {
            Collection var4 = var3.getValues();
            var2 = null;

            for(Iterator var5 = var4.iterator(); var5.hasNext() && var2 == null; var2 = (String)var5.next()) {
            }
         }
      }

      return var2;
   }

   public void setRegistryObjectOwner(RegistryObject var1, String var2) throws JAXRException {
      BusinessLifeCycleManagerImpl var3 = (BusinessLifeCycleManagerImpl)this.m_registryServiceImpl.getBusinessLifeCycleManager();
      Slot var4 = var3.createSlot("authorizedName", var2, "authorizedName");
      var1.addSlot(var4);
   }

   public String makeRegistrySpecificRequest(String var1) throws JAXRException {
      UDDIResponse var2;
      try {
         String var3 = SOAPWrapper.makeSOAPString(var1);
         var2 = this.m_uddiProxy.processRequestString(var3);
      } catch (UDDIException var4) {
         throw UDDIBridgeMapperUtil.mapException(var4);
      }

      return var2.toXML();
   }

   public BulkResponse saveOrganizations(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            SaveBusinessRequest var2 = UDDIBridgeRequestMapper.getSaveBusinessRequest(var1, UDDIBridgeProxy.this.m_registryServiceImpl);
            return var2;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            BusinessDetailResponse var3 = (BusinessDetailResponse)var2;
            BulkResponse var4 = UDDIBridgeResponseMapper.getBulkResponseForKeysFromBusinessDetailResponse(UDDIBridgeProxy.this.m_registryServiceImpl, var3);
            return var4;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   public BulkResponse saveServices(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            SaveServiceRequest var2 = UDDIBridgeRequestMapper.getSaveServiceRequest(var1);
            return var2;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            ServiceDetailResponse var3 = (ServiceDetailResponse)var2;
            BulkResponse var4 = UDDIBridgeResponseMapper.getBulkResponseForKeysFromServiceDetailResponse(UDDIBridgeProxy.this.m_registryServiceImpl, var3);
            return var4;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   public BulkResponse saveServiceBindings(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            SaveBindingRequest var2 = UDDIBridgeRequestMapper.getSaveBindingsRequest(var1);
            return var2;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            BindingDetailResponse var3 = (BindingDetailResponse)var2;
            BulkResponse var4 = UDDIBridgeResponseMapper.getBulkResponseForKeysFromBindingDetailResponse(UDDIBridgeProxy.this.m_registryServiceImpl, var3);
            return var4;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   public BulkResponse saveConcepts(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            SaveTModelRequest var2 = UDDIBridgeRequestMapper.getSaveTModelFromConcepts(var1);
            return var2;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            TModelDetailResponse var3 = (TModelDetailResponse)var2;
            BulkResponse var4 = UDDIBridgeResponseMapper.getBulkResponseForKeysFromTModelDetailResponse(UDDIBridgeProxy.this.m_registryServiceImpl, var3);
            return var4;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   public BulkResponse saveClassificationSchemes(Collection var1) throws JAXRException {
      Logger.debug("+UDDIBridgeProxy.saveClassificationSchemes()");
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            SaveTModelRequest var2 = UDDIBridgeRequestMapper.getSaveTModelFromClassificationSchemes(var1);
            return var2;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            TModelDetailResponse var3 = (TModelDetailResponse)var2;
            BulkResponse var4 = UDDIBridgeResponseMapper.getBulkResponseForKeysFromTModelDetailResponse(UDDIBridgeProxy.this.m_registryServiceImpl, var3);
            return var4;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      Logger.debug("-UDDIBridgeProxy.saveClassificationSchemes()");
      return var2;
   }

   public BulkResponse saveAssociations(Collection var1, boolean var2) throws JAXRException {
      return var2 ? this.setAssociations(var1) : this.addAssociations(var1);
   }

   public BulkResponse deleteOrganizations(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         DeleteBusinessRequest request = null;

         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            this.request = UDDIBridgeRequestMapper.getDeleteBusinessRequest(var1);
            return this.request;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            BulkResponse var3 = UDDIBridgeResponseMapper.getBulkResponseForKeysFromDeleteBusinessRequest(UDDIBridgeProxy.this.m_registryServiceImpl, this.request);
            return var3;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   public BulkResponse deleteServices(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         DeleteServiceRequest request = null;

         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            this.request = UDDIBridgeRequestMapper.getDeleteServiceRequest(var1);
            return this.request;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            BulkResponse var3 = UDDIBridgeResponseMapper.getBulkResponseForKeysFromDeleteServiceRequest(UDDIBridgeProxy.this.m_registryServiceImpl, this.request);
            return var3;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   public BulkResponse deleteServiceBindings(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         DeleteBindingRequest request = null;

         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            this.request = UDDIBridgeRequestMapper.getDeleteBindingRequest(var1);
            return this.request;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            BulkResponse var3 = UDDIBridgeResponseMapper.getBulkResponseForKeysFromDeleteBindingRequest(UDDIBridgeProxy.this.m_registryServiceImpl, this.request);
            return var3;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   public BulkResponse deleteConcepts(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         DeleteTModelRequest request = null;

         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            this.request = UDDIBridgeRequestMapper.getDeleteTModelRequest(var1);
            return this.request;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            BulkResponse var3 = UDDIBridgeResponseMapper.getBulkResponseForKeysFromDeleteTModelRequest(UDDIBridgeProxy.this.m_registryServiceImpl, this.request);
            return var3;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   public BulkResponse deleteClassificationSchemes(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         DeleteTModelRequest request = null;

         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            this.request = UDDIBridgeRequestMapper.getDeleteTModelRequest(var1);
            return this.request;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            BulkResponse var3 = UDDIBridgeResponseMapper.getBulkResponseForKeysFromDeleteTModelRequest(UDDIBridgeProxy.this.m_registryServiceImpl, this.request);
            return var3;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   public BulkResponse deleteAssociations(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         DeletePublisherAssertionsRequest request = null;
         Collection keysForReturn;

         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            this.request = UDDIBridgeRequestMapper.getDeletePublisherAssertionsRequest(var1, UDDIBridgeProxy.this.m_registryServiceImpl);
            this.keysForReturn = var1;
            return this.request;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            BulkResponse var3 = UDDIBridgeResponseMapper.getBulkResponseForKeys(UDDIBridgeProxy.this.m_registryServiceImpl, this.keysForReturn);
            return var3;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   public void confirmAssociation(Association var1) throws JAXRException, InvalidRequestException {
      if (!this.isIntramural(var1) && !var1.isConfirmed()) {
         ArrayList var2 = new ArrayList();
         var2.add(var1);
         this.addAssociations(var2);
      }

   }

   public void unConfirmAssociation(Association var1) throws JAXRException, InvalidRequestException {
      if (var1.isExtramural() && var1.isConfirmed()) {
         ArrayList var2 = new ArrayList();
         var2.add(var1.getKey());
         this.deleteAssociations(var2);
      }

   }

   public BulkResponse findAssociations(Collection var1, String var2, String var3, Collection var4) throws JAXRException {
      try {
         Object var5;
         if ((var1 == null || var1.size() == 0) && var2 == null && var3 == null && (var4 == null || var4.size() == 0)) {
            var5 = new BulkResponseImpl(this.m_registryServiceImpl);
         } else {
            if (null == var2 && null == var3) {
               String var14 = JAXRMessages.getMessage("jaxr.bridge.uddiBridgeProxy.findAssociations.invalidObjectId");
               throw new JAXRException(var14);
            }

            boolean var8;
            if (var2 == null) {
               var8 = true;
            } else {
               var8 = false;
            }

            FindRelatedBusinessesRequest var6;
            RelatedBusinessListResponse var7;
            if (null == var4) {
               var6 = UDDIBridgeRequestMapper.getFindRelatedBusinessesRequest(var1, var2, var3, (KeyedReference)null);
               this.setMaxRows(var6);
               var7 = (RelatedBusinessListResponse)this.m_uddiProxy.execute(var6);
               Organization var9 = (Organization)this.getRegistryObject(var7.getBusinessKey().getKey(), "Organization");
               var5 = UDDIBridgeResponseMapper.getBulkResponseForAssociations(this.m_registryServiceImpl, var7, var8, var9);
            } else {
               var5 = new BulkResponseImpl(this.m_registryServiceImpl);
               Collection var15 = UDDIBridgeMapperUtil.getKeyedReferencesFromAssociationTypes(var4, this.m_registryServiceImpl);
               Iterator var10 = var15.iterator();

               while(var10.hasNext()) {
                  var6 = UDDIBridgeRequestMapper.getFindRelatedBusinessesRequest(var1, var2, var3, (KeyedReference)var10.next());
                  this.setMaxRows(var6);
                  var7 = (RelatedBusinessListResponse)this.m_uddiProxy.execute(var6);
                  Organization var11 = (Organization)this.getRegistryObject(var7.getBusinessKey().getKey(), "Organization");
                  BulkResponse var12 = UDDIBridgeResponseMapper.getBulkResponseForAssociations(this.m_registryServiceImpl, var7, var8, var11);
                  UDDIBridgeMapperUtil.accumulateBulkResponses((BulkResponseImpl)var5, var12);
               }
            }
         }

         return (BulkResponse)var5;
      } catch (UDDIException var13) {
         throw UDDIBridgeMapperUtil.mapException(var13);
      }
   }

   public BulkResponse findCallerAssociations(Collection var1, Boolean var2, Boolean var3, Collection var4) throws JAXRException {
      try {
         BulkResponseImpl var5;
         if (var1 != null && var1.size() != 0 || var2 != null || var3 != null || var4 != null && var4.size() != 0) {
            GetAssertionStatusReportRequest var6 = new GetAssertionStatusReportRequest();
            AssertionStatusReportResponse var7 = (AssertionStatusReportResponse)this.m_uddiProxy.execute(var6);
            AssertionStatusItems var8 = UDDIBridgeMapperUtil.selectAssertionStatusItems(var7.getAssertionStatusItems(), var2, var3, var4);
            ArrayList var9 = new ArrayList();

            for(AssertionStatusItem var10 = var8.getFirst(); var10 != null; var10 = var8.getNext()) {
               Association var11 = UDDIBridgeResponseMapper.getAssociation(this.m_registryServiceImpl, var10);
               Organization var12 = (Organization)this.getRegistryObject(var10.getFromKey().getKey(), "Organization");
               Organization var13 = (Organization)this.getRegistryObject(var10.getToKey().getKey(), "Organization");
               var11.setSourceObject(var12);
               var11.setTargetObject(var13);
               var9.add(var11);
            }

            var5 = UDDIBridgeResponseMapper.getBulkResponseFromCollection(var9, this.m_registryServiceImpl);
         } else {
            var5 = new BulkResponseImpl(this.m_registryServiceImpl);
         }

         return var5;
      } catch (UDDIException var14) {
         throw UDDIBridgeMapperUtil.mapException(var14);
      }
   }

   public BulkResponse findOrganizations(Collection var1, Collection var2, Collection var3, Collection var4, Collection var5, Collection var6) throws JAXRException {
      try {
         FindBusinessRequest var7 = UDDIBridgeRequestMapper.getFindBusinessRequest(var1, var2, var3, var4, var5, var6);
         this.setMaxRows(var7);
         BusinessListResponse var8 = (BusinessListResponse)this.m_uddiProxy.execute(var7);
         BusinessInfos var9 = var8.getBusinessInfos();
         BusinessEntities var10 = this.getBusinessEntitiesFromBusinessInfos(var9);
         return UDDIBridgeResponseMapper.getBulkResponseForOrganizations(this.m_registryServiceImpl, var10);
      } catch (UDDIException var11) {
         throw UDDIBridgeMapperUtil.mapException(var11);
      }
   }

   public BulkResponse findServices(Key var1, Collection var2, Collection var3, Collection var4, Collection var5) throws JAXRException {
      try {
         FindServiceRequest var6 = UDDIBridgeRequestMapper.getFindServiceRequest(var1, var2, var3, var4, var5);
         this.setMaxRows(var6);
         ServiceListResponse var7 = (ServiceListResponse)this.m_uddiProxy.execute(var6);
         ServiceInfos var8 = var7.getServiceInfos();
         return this.getServicesFromServiceInfos(var8);
      } catch (UDDIException var9) {
         throw UDDIBridgeMapperUtil.mapException(var9);
      }
   }

   public BulkResponse findServiceBindings(Key var1, Collection var2, Collection var3, Collection var4) throws JAXRException {
      BulkResponse var8;
      try {
         Logger.debug("+UDDIBridgeProxy.findServiceBindings");
         FindBindingRequest var5 = UDDIBridgeRequestMapper.getFindBindingRequest(var1, var2, var4);
         this.setMaxRows(var5);
         BindingDetailResponse var6 = (BindingDetailResponse)this.m_uddiProxy.execute(var5);
         Collection var7 = this.findServicesByBindingDetailResponse(var6);
         var8 = UDDIBridgeResponseMapper.getBulkResponseForServiceBindings(this.m_registryServiceImpl, var6, var7);
      } catch (UDDIException var12) {
         throw UDDIBridgeMapperUtil.mapException(var12);
      } finally {
         Logger.debug("-UDDIBridgeProxy.findServiceBindings");
      }

      return var8;
   }

   private Collection findServicesByBindingDetailResponse(BindingDetailResponse var1) throws JAXRException {
      Collection var13;
      try {
         Logger.debug("+UDDIBridgeProxy.findServicesByBindingDetailResponse");
         Collection var2 = null;
         BindingTemplates var3 = var1.getBindingTemplates();
         if (var3 != null) {
            ArrayList var4 = new ArrayList();
            BindingTemplate var5 = var3.getFirst();

            while(true) {
               if (var5 == null) {
                  GetServiceDetailRequest var12 = UDDIBridgeRequestMapper.getServiceDetailRequest(var4);
                  ServiceDetailResponse var6 = (ServiceDetailResponse)this.m_uddiProxy.execute(var12);
                  var2 = UDDIBridgeResponseMapper.getServices(this.m_registryServiceImpl, var6.getBusinessServices());
                  UDDIBridgeResponseMapper.getBulkResponseFromCollection(var2, this.m_registryServiceImpl);
                  break;
               }

               var4.add(var5.getServiceKey());
               var5 = var3.getNext();
            }
         }

         var13 = var2;
      } catch (UDDIException var10) {
         throw UDDIBridgeMapperUtil.mapException(var10);
      } finally {
         Logger.debug("-UDDIBridgeProxy.findServicesByBindingDetailResponse");
      }

      return var13;
   }

   public BulkResponse findClassificationSchemes(Collection var1, Collection var2, Collection var3, Collection var4) throws JAXRException {
      try {
         FindTModelRequest var6 = UDDIBridgeRequestMapper.getFindTModelRequest(var1, var2, var3, (Collection)null);
         this.setMaxRows(var6);
         TModelListResponse var7 = (TModelListResponse)this.m_uddiProxy.execute(var6);
         TModelInfos var8 = var7.getTModelInfos();
         TModels var9 = this.getTModelsFromTModelInfos(var8);
         BulkResponseImpl var5 = (BulkResponseImpl)UDDIBridgeResponseMapper.getBulkResponseForClassificationSchemes(this.m_registryServiceImpl, var9);
         if (var2 != null && var2.size() > 0) {
            Iterator var10 = var2.iterator();

            while(var10.hasNext()) {
               Object var12 = var10.next();
               String var11;
               if (var12 instanceof String) {
                  var11 = (String)var12;
               } else if (var12 instanceof LocalizedString) {
                  var11 = ((LocalizedString)var12).getValue();
               } else {
                  var11 = null;
               }

               if (var11 != null) {
                  ClassificationScheme var13 = ClassificationSchemeHelper.getClassificationSchemeByName(var11, this.m_registryServiceImpl);
                  if (var13 != null) {
                     ArrayList var14 = new ArrayList();
                     var14.add(var13);
                     BulkResponseImpl var15 = UDDIBridgeResponseMapper.getBulkResponseFromCollection(var14, this.m_registryServiceImpl);
                     UDDIBridgeMapperUtil.accumulateBulkResponses(var5, (BulkResponse)var15);
                  }
               }
            }
         }

         return var5;
      } catch (UDDIException var16) {
         throw UDDIBridgeMapperUtil.mapException(var16);
      }
   }

   public ClassificationScheme findClassificationSchemeByName(Collection var1, String var2) throws JAXRException {
      try {
         ClassificationScheme var3 = ClassificationSchemeHelper.getClassificationSchemeByName(var2, this.m_registryServiceImpl);
         if (var3 == null) {
            ArrayList var4 = new ArrayList();
            var4.add(var2);
            FindTModelRequest var5 = UDDIBridgeRequestMapper.getFindTModelRequest(var1, var4, (Collection)null, (Collection)null);
            this.setMaxRows(var5);
            TModelListResponse var6 = (TModelListResponse)this.m_uddiProxy.execute(var5);
            TModelInfos var7 = var6.getTModelInfos();
            TModels var8 = this.getTModelsFromTModelInfos(var7);
            TModel var9 = var8.getFirst();
            TModel var10 = var8.getNext();
            if (var10 != null) {
               String var11 = JAXRMessages.getMessage("jaxr.registry.registryServiceImpl.MultipleMatchesFound");
               throw new InvalidRequestException(var11);
            }

            var3 = UDDIBridgeResponseMapper.getClassificationScheme(this.m_registryServiceImpl, var9);
         }

         return var3;
      } catch (UDDIException var12) {
         throw UDDIBridgeMapperUtil.mapException(var12);
      }
   }

   public BulkResponse findConcepts(Collection var1, Collection var2, Collection var3, Collection var4, Collection var5) throws JAXRException {
      try {
         FindTModelRequest var6 = UDDIBridgeRequestMapper.getFindTModelRequest(var1, var2, var3, var4);
         this.setMaxRows(var6);
         TModelListResponse var7 = (TModelListResponse)this.m_uddiProxy.execute(var6);
         TModelInfos var8 = var7.getTModelInfos();
         TModels var9 = this.getTModelsFromTModelInfos(var8);
         return UDDIBridgeResponseMapper.getBulkResponseForConcepts(this.m_registryServiceImpl, var9);
      } catch (UDDIException var10) {
         throw UDDIBridgeMapperUtil.mapException(var10);
      }
   }

   public Concept findConceptByPath(String var1) throws JAXRException {
      ArrayList var3 = new ArrayList();
      if (var1 == null) {
         String var14 = JAXRMessages.getMessage("jaxr.bridge.uddiBridgeProxy.findConceptByPath.invalidPath");
         throw new JAXRException(var14);
      } else {
         StringTokenizer var4 = new StringTokenizer(var1, "/", true);

         while(var4.hasMoreTokens()) {
            String var5 = var4.nextToken();
            var3.add(var5);
         }

         Concept var13 = null;
         int var15 = var3.size();
         if (var15 > 2) {
            String var6 = (String)var3.get(0);
            if (var6.equals("/")) {
               String var7 = (String)var3.get(1);
               ClassificationScheme var8 = ClassificationSchemeHelper.getClassificationSchemeByName(var7, this.m_registryServiceImpl);
               if (var8 != null) {
                  Collection var9 = var8.getChildrenConcepts();

                  for(int var10 = 2; var10 < var15; var10 += 2) {
                     var6 = (String)var3.get(var10);
                     int var11 = var10 + 1;
                     if (var6.equals("/") && var11 < var15) {
                        String var12 = (String)var3.get(var11);
                        if (var9 != null) {
                           var13 = UDDIBridgeMapperUtil.getConceptByValue(var9, var12);
                        }
                     }
                  }
               }
            }
         }

         return var13;
      }
   }

   public BulkResponse findRegistryPackages(Collection var1, Collection var2, Collection var3, Collection var4) throws JAXRException {
      this.checkCapability(this.m_registryServiceImpl, 1);
      return null;
   }

   public BulkResponse deprecateObjects(Collection var1) throws JAXRException {
      this.checkCapability(this.m_registryServiceImpl, 1);
      return null;
   }

   public BulkResponse unDeprecateObjects(Collection var1) throws JAXRException {
      this.checkCapability(this.m_registryServiceImpl, 1);
      return null;
   }

   public BulkResponse deleteObjects(Collection var1) throws JAXRException {
      this.checkCapability(this.m_registryServiceImpl, 1);
      return null;
   }

   public RegistryObject getRegistryObject(String var1, String var2) throws JAXRException {
      BusinessLifeCycleManager var3 = this.m_registryServiceImpl.getBusinessLifeCycleManager();
      Key var4 = var3.createKey(var1);
      ArrayList var5 = new ArrayList();
      var5.add(var4);
      BulkResponse var6 = this.getRegistryObjects(var5, var2);
      Collection var7 = var6.getCollection();
      Iterator var8 = var7.iterator();
      RegistryObject var9 = (RegistryObject)var8.next();
      return var9;
   }

   public RegistryObject getRegistryObject(String var1) throws JAXRException {
      this.checkCapability(this.m_registryServiceImpl, 1);
      return null;
   }

   public BulkResponse getRegistryObjects(Collection var1) throws JAXRException {
      this.checkCapability(this.m_registryServiceImpl, 1);
      return null;
   }

   public BulkResponse getRegistryObjects(Collection var1, String var2) throws JAXRException {
      try {
         new BulkResponseImpl(this.m_registryServiceImpl);
         BulkResponseImpl var3;
         Iterator var5;
         Key var6;
         if ("Organization".equals(var2)) {
            GetBusinessDetailRequest var4 = new GetBusinessDetailRequest();
            var5 = var1.iterator();

            while(var5.hasNext()) {
               var6 = (Key)var5.next();
               BusinessKey var7 = new BusinessKey(var6.getId());
               var4.addBusinessKey(var7);
            }

            BusinessDetailResponse var12 = (BusinessDetailResponse)this.m_uddiProxy.execute(var4);
            var3 = (BulkResponseImpl)UDDIBridgeResponseMapper.getBulkResponseForOrganizations(this.m_registryServiceImpl, var12.getBusinessEntities());
         } else if (!"ClassificationScheme".equals(var2) && !"Concept".equals(var2)) {
            if ("Service".equals(var2)) {
               GetServiceDetailRequest var10 = new GetServiceDetailRequest();
               var5 = var1.iterator();

               while(var5.hasNext()) {
                  var6 = (Key)var5.next();
                  ServiceKey var18 = new ServiceKey(var6.getId());
                  var10.addServiceKey(var18);
               }

               ServiceDetailResponse var15 = (ServiceDetailResponse)this.m_uddiProxy.execute(var10);
               var3 = (BulkResponseImpl)UDDIBridgeResponseMapper.getBulkResponseForServicesFromServiceDetailResponse(this.m_registryServiceImpl, var15);
            } else {
               if (!"ServiceBinding".equals(var2)) {
                  String var13 = JAXRMessages.getMessage("jaxr.bridge.uddiBridgeProxy.getRegistryObjects.invalidObjectType");
                  throw new JAXRException(var13);
               }

               GetBindingDetailRequest var11 = new GetBindingDetailRequest();
               var5 = var1.iterator();

               while(var5.hasNext()) {
                  var6 = (Key)var5.next();
                  BindingKey var19 = new BindingKey(var6.getId());
                  var11.addBindingKey(var19);
               }

               BindingDetailResponse var17 = (BindingDetailResponse)this.m_uddiProxy.execute(var11);
               var3 = (BulkResponseImpl)UDDIBridgeResponseMapper.getBulkResponseForServiceBindings(this.m_registryServiceImpl, var17);
            }
         } else {
            GetTModelDetailRequest var9 = new GetTModelDetailRequest();
            var5 = var1.iterator();

            while(var5.hasNext()) {
               var6 = (Key)var5.next();
               TModelKey var16 = new TModelKey(var6.getId());
               var9.addTModelKey(var16);
            }

            TModelDetailResponse var14 = (TModelDetailResponse)this.m_uddiProxy.execute(var9);
            var3 = (BulkResponseImpl)UDDIBridgeResponseMapper.getBulkResponseForConceptsOrClassificationSchemes(this.m_registryServiceImpl, var14);
         }

         return var3;
      } catch (UDDIException var8) {
         throw UDDIBridgeMapperUtil.mapException(var8);
      }
   }

   public BulkResponse getRegistryObjects() throws JAXRException {
      return this.getRegistryObjectsOfCallers((Collection)null, (String)null);
   }

   public BulkResponse getRegistryObjects(String var1) throws JAXRException {
      return this.getRegistryObjectsOfCallers((Collection)null, var1);
   }

   public BusinessEntities getBusinessEntitiesFromBusinessInfos(BusinessInfos var1) throws JAXRException {
      try {
         BusinessEntities var2;
         if (var1 != null && var1.size() > 0) {
            ArrayList var3 = new ArrayList();

            for(BusinessInfo var4 = var1.getFirst(); var4 != null; var4 = var1.getNext()) {
               var3.add(var4.getBusinessKey());
            }

            GetBusinessDetailRequest var7 = UDDIBridgeRequestMapper.getBusinessDetailRequest(var3);
            BusinessDetailResponse var5 = (BusinessDetailResponse)this.m_uddiProxy.execute(var7);
            var2 = var5.getBusinessEntities();
         } else {
            var2 = new BusinessEntities();
         }

         return var2;
      } catch (UDDIException var6) {
         throw UDDIBridgeMapperUtil.mapException(var6);
      }
   }

   public BulkResponse getServicesFromServiceInfos(ServiceInfos var1) throws JAXRException {
      try {
         Object var2;
         if (var1 != null && var1.size() > 0) {
            ArrayList var7 = new ArrayList();

            for(ServiceInfo var4 = var1.getFirst(); var4 != null; var4 = var1.getNext()) {
               var7.add(var4.getServiceKey());
            }

            GetServiceDetailRequest var8 = UDDIBridgeRequestMapper.getServiceDetailRequest(var7);
            ServiceDetailResponse var5 = (ServiceDetailResponse)this.m_uddiProxy.execute(var8);
            var2 = UDDIBridgeResponseMapper.getBulkResponseForServicesFromServiceDetailResponse(this.m_registryServiceImpl, var5);
         } else {
            BulkResponseImpl var3 = new BulkResponseImpl(this.m_registryServiceImpl);
            var3.setResponse(Collections.EMPTY_LIST, (Collection)null, false);
            var2 = var3;
         }

         return (BulkResponse)var2;
      } catch (UDDIException var6) {
         throw UDDIBridgeMapperUtil.mapException(var6);
      }
   }

   public TModels getTModelsFromTModelInfos(TModelInfos var1) throws JAXRException {
      try {
         ArrayList var3 = new ArrayList();

         for(TModelInfo var4 = var1.getFirst(); var4 != null; var4 = var1.getNext()) {
            var3.add(var4.getKey());
         }

         TModels var2;
         if (var3.size() > 0) {
            GetTModelDetailRequest var7 = UDDIBridgeRequestMapper.getTModelDetailRequest(var3);
            TModelDetailResponse var5 = (TModelDetailResponse)this.m_uddiProxy.execute(var7);
            var2 = var5.getTModels();
         } else {
            var2 = new TModels();
         }

         return var2;
      } catch (UDDIException var6) {
         throw UDDIBridgeMapperUtil.mapException(var6);
      }
   }

   private BulkResponse getRegistryObjectsOfCallers(Collection var1, String var2) throws JAXRException {
      try {
         GetRegisteredInfoRequest var4 = UDDIBridgeRequestMapper.getRegisteredInfoRequest();
         RegisteredInfoResponse var5 = (RegisteredInfoResponse)this.m_uddiProxy.execute(var4);
         Object var6 = new ArrayList();
         BusinessInfos var7 = var5.getBusinessInfos();
         TModelInfos var8 = var5.getTModelInfos();
         BusinessEntities var9;
         TModels var10;
         TModels var11;
         Collection var13;
         if (null != var2) {
            if (var2.equals("Organization")) {
               var9 = this.getBusinessEntitiesFromBusinessInfos(var7);
               var6 = UDDIBridgeResponseMapper.getOrganizationsFromBusinessEntities(this.m_registryServiceImpl, var9);
            } else if (!var2.equals("Concept") && !var2.equals("ClassificationScheme")) {
               Collection var18;
               Iterator var20;
               Organization var22;
               if (var2.equals("Service")) {
                  var9 = this.getBusinessEntitiesFromBusinessInfos(var7);
                  var18 = UDDIBridgeResponseMapper.getOrganizationsFromBusinessEntities(this.m_registryServiceImpl, var9);
                  var20 = var18.iterator();

                  while(var20.hasNext()) {
                     var22 = (Organization)var20.next();
                     ((Collection)var6).addAll(var22.getServices());
                  }
               } else {
                  if (!var2.equals("ServiceBinding")) {
                     String var19 = JAXRMessages.getMessage("jaxr.bridge.uddiBridgeProxy.getRegistryObjects.invalidObjectType");
                     throw new JAXRException(var19);
                  }

                  var9 = this.getBusinessEntitiesFromBusinessInfos(var7);
                  var18 = UDDIBridgeResponseMapper.getOrganizationsFromBusinessEntities(this.m_registryServiceImpl, var9);
                  var20 = var18.iterator();

                  while(var20.hasNext()) {
                     var22 = (Organization)var20.next();
                     var13 = var22.getServices();
                     Iterator var14 = var13.iterator();

                     while(var20.hasNext()) {
                        Service var15 = (Service)var14.next();
                        ((Collection)var6).addAll(var15.getServiceBindings());
                     }
                  }
               }
            } else {
               TModels var17 = this.getTModelsFromTModelInfos(var8);
               var10 = new TModels();
               var11 = new TModels();

               for(TModel var12 = var17.getFirst(); var12 != null; var12 = var17.getNext()) {
                  if (UDDIBridgeMapperUtil.isConcept(var12)) {
                     var10.add(var12);
                  } else {
                     var11.add(var12);
                  }
               }

               if (var2.equals("ClassificationScheme")) {
                  var6 = UDDIBridgeResponseMapper.getClassificationSchemes(this.m_registryServiceImpl, var11);
               } else {
                  if (!var2.equals("Concept")) {
                     String var21 = JAXRMessages.getMessage("jaxr.bridge.uddiBridgeProxy.getRegistryObjects.invalidObjectType");
                     throw new JAXRException(var21);
                  }

                  var6 = UDDIBridgeResponseMapper.getConcepts(this.m_registryServiceImpl, var10);
               }
            }
         } else {
            var9 = this.getBusinessEntitiesFromBusinessInfos(var7);
            var6 = UDDIBridgeResponseMapper.getOrganizationsFromBusinessEntities(this.m_registryServiceImpl, var9);
            var10 = this.getTModelsFromTModelInfos(var8);
            var11 = new TModels();
            TModels var24 = new TModels();

            for(TModel var23 = var10.getFirst(); var23 != null; var23 = var10.getNext()) {
               if (UDDIBridgeMapperUtil.isConcept(var23)) {
                  var11.add(var23);
               } else {
                  var24.add(var23);
               }
            }

            var13 = UDDIBridgeResponseMapper.getClassificationSchemes(this.m_registryServiceImpl, var24);
            ((Collection)var6).addAll(var13);
            var13 = UDDIBridgeResponseMapper.getConcepts(this.m_registryServiceImpl, var11);
            ((Collection)var6).addAll(var13);
         }

         if (null != var1) {
            var6 = selectWantedRegistryObjects((Collection)var6, var1);
         }

         BulkResponseImpl var3 = UDDIBridgeResponseMapper.getBulkResponseFromCollection((Collection)var6, this.m_registryServiceImpl);
         return var3;
      } catch (UDDIException var16) {
         throw UDDIBridgeMapperUtil.mapException(var16);
      }
   }

   private BulkResponse setAssociations(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         UDDIPublishRequest request = null;
         Collection keysForReturn = null;

         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            this.request = UDDIBridgeRequestMapper.getSetPublisherAssertions(var1);
            ArrayList var2 = new ArrayList();
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               Association var4 = (Association)var3.next();
               var2.add(var4.getKey());
            }

            this.keysForReturn = var2;
            return this.request;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            BulkResponseImpl var3 = UDDIBridgeResponseMapper.getBulkResponseFromCollection(this.keysForReturn, UDDIBridgeProxy.this.m_registryServiceImpl);
            return var3;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   private BulkResponse addAssociations(Collection var1) throws JAXRException {
      PartialCommitHelper.JaxrAPIMapper var3 = new PartialCommitHelper.JaxrAPIMapper() {
         UDDIPublishRequest request = null;
         Collection keysForReturn = null;

         public UDDIRequest getUDDIRequest(Collection var1) throws JAXRException {
            this.request = UDDIBridgeRequestMapper.getAddPublisherAssertions(var1);
            ArrayList var2 = new ArrayList();
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               Association var4 = (Association)var3.next();
               var2.add(var4.getKey());
            }

            this.keysForReturn = var2;
            return this.request;
         }

         public BulkResponse getBulkResponse(RegistryServiceImpl var1, UDDIResponse var2) throws JAXRException {
            BulkResponseImpl var3 = UDDIBridgeResponseMapper.getBulkResponseFromCollection(this.keysForReturn, UDDIBridgeProxy.this.m_registryServiceImpl);
            return var3;
         }
      };
      BulkResponse var2 = PartialCommitHelper.handlePartialCommit(var3, var1, this.m_uddiProxy, this.m_registryServiceImpl);
      return var2;
   }

   private static Collection selectWantedRegistryObjects(Collection var0, Collection var1) throws JAXRException {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var0.iterator();

      while(true) {
         while(var3.hasNext()) {
            RegistryObject var4 = (RegistryObject)var3.next();
            String var5 = var4.getKey().getId();
            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               String var7 = ((Key)var6.next()).getId();
               if (var5.equals(var7)) {
                  var2.add(var4);
                  break;
               }
            }
         }

         return var2;
      }
   }

   private void setMaxRows(UDDIRequest var1) {
      if (this.m_maxRows != Integer.MIN_VALUE) {
         var1.setMaxRows(this.m_maxRows);
      }

   }

   private static void initializeAuddiClient() throws IOException, UDDIException {
      PropertyManager.setRuntimeProperty("uddi.schema.resource", "/weblogic/auddi/uddi/resources/uddi_v2.xsd");
      PropertyManager.setRuntimeProperty("soap.schema.resource", "/weblogic/auddi/uddi/resources/soap-envelope.xml");
      PropertyManager.setRuntimeProperty("xml.schema.resource", "/weblogic/auddi/uddi/resources/xml.xml");
   }

   private boolean isIntramural(Association var1) throws JAXRException {
      RegistryObject var3 = var1.getSourceObject();
      RegistryObject var4 = var1.getTargetObject();
      String var5;
      if (var3 != null && var4 != null) {
         var5 = this.getRegistryObjectOwner(var3);
         String var6 = this.getRegistryObjectOwner(var4);
         String var7 = this.getRegistryObjectOwner(var1);
         boolean var2;
         if (var7 == null) {
            if (var5 == null && var6 == null) {
               var2 = true;
            } else {
               var2 = false;
            }
         } else if (var5 != null) {
            if (var5.equals(var6) && var5.equals(var7)) {
               var2 = true;
            } else {
               var2 = false;
            }
         } else {
            var2 = false;
         }

         return var2;
      } else {
         var5 = JAXRMessages.getMessage("jaxr.registry.registryServiceImpl.invalidSourceORTarget");
         throw new InvalidRequestException(var5);
      }
   }
}
