package weblogic.wsee.config;

import weblogic.management.configuration.WebServiceLogicalStoreMBean;

public class WebServiceLogicalStoreMBeanImpl extends DummyConfigurationMBeanImpl implements WebServiceLogicalStoreMBean {
   private String _persistenceStrategy = "LOCAL_ACCESS_ONLY";
   private String _cleanerInterval = "PT10M";
   private String _defaultMaximumObjectLifetime = "P1D";
   private String _requestBufferingQueueJndiName = null;
   private String _responseBufferingQueueJndiName = null;
   private String _physicalStoreName = "WseeFileStore";

   public WebServiceLogicalStoreMBeanImpl(String var1) {
      super(var1);
   }

   public String getPersistenceStrategy() {
      return this._persistenceStrategy != null ? this._persistenceStrategy : "LOCAL_ACCESS_ONLY";
   }

   public void setPersistenceStrategy(String var1) {
      if (var1 == null) {
         var1 = "LOCAL_ACCESS_ONLY";
      }

      this._persistenceStrategy = var1;
   }

   public void setCleanerInterval(String var1) {
      this._cleanerInterval = var1;
   }

   public String getCleanerInterval() {
      return this._cleanerInterval;
   }

   public void setDefaultMaximumObjectLifetime(String var1) {
      this._defaultMaximumObjectLifetime = var1;
   }

   public String getDefaultMaximumObjectLifetime() {
      return this._defaultMaximumObjectLifetime;
   }

   public String getRequestBufferingQueueJndiName() {
      return this._requestBufferingQueueJndiName;
   }

   public void setRequestBufferingQueueJndiName(String var1) {
      this._requestBufferingQueueJndiName = var1;
   }

   public String getResponseBufferingQueueJndiName() {
      return this._responseBufferingQueueJndiName;
   }

   public void setResponseBufferingQueueJndiName(String var1) {
      this._responseBufferingQueueJndiName = var1;
   }

   public void setPhysicalStoreName(String var1) {
      this._physicalStoreName = var1;
   }

   public String getPhysicalStoreName() {
      return this._physicalStoreName;
   }
}
