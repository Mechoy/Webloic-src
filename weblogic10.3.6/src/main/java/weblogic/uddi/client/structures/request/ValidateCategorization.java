package weblogic.uddi.client.structures.request;

import weblogic.uddi.client.structures.datatypes.BusinessEntity;
import weblogic.uddi.client.structures.datatypes.BusinessService;
import weblogic.uddi.client.structures.datatypes.KeyValue;
import weblogic.uddi.client.structures.datatypes.TModel;
import weblogic.uddi.client.structures.datatypes.TModelKey;

public class ValidateCategorization extends Request {
   private TModelKey tModelKey = null;
   private KeyValue keyValue = null;
   private BusinessEntity businessEntity = null;
   private BusinessService businessService = null;
   private TModel tModel = null;

   public void setTModelKey(TModelKey var1) {
      this.tModelKey = var1;
   }

   public TModelKey getTModelKey() {
      return this.tModelKey;
   }

   public void setKeyValue(KeyValue var1) {
      this.keyValue = var1;
   }

   public KeyValue getKeyValue() {
      return this.keyValue;
   }

   public void setBusinessEntity(BusinessEntity var1) {
      this.businessService = null;
      this.tModel = null;
      this.businessEntity = var1;
   }

   public BusinessEntity getBusinessEntity() {
      return this.businessEntity;
   }

   public void setBusinessService(BusinessService var1) {
      this.businessEntity = null;
      this.tModel = null;
      this.businessService = var1;
   }

   public BusinessService getBusinessService() {
      return this.businessService;
   }

   public void setTModel(TModel var1) {
      this.businessEntity = null;
      this.businessService = null;
      this.tModel = var1;
   }

   public TModel getTModel() {
      return this.tModel;
   }
}
