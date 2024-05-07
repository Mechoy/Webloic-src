package weblogic.wsee.config;

import weblogic.management.configuration.WebServiceBufferingMBean;
import weblogic.management.configuration.WebServiceRequestBufferingQueueMBean;
import weblogic.management.configuration.WebServiceResponseBufferingQueueMBean;

public class WebServiceBufferingMBeanImpl extends DummyConfigurationMBeanImpl implements WebServiceBufferingMBean {
   private WebServiceRequestBufferingQueueMBeanImpl _requestQueue = new WebServiceRequestBufferingQueueMBeanImpl();
   private WebServiceResponseBufferingQueueMBeanImpl _responseQueue = new WebServiceResponseBufferingQueueMBeanImpl();
   private int _retryCount = 3;
   private String _retryDelay = "P0DT30S";

   public WebServiceRequestBufferingQueueMBean getWebServiceRequestBufferingQueue() {
      return this._requestQueue;
   }

   public WebServiceResponseBufferingQueueMBean getWebServiceResponseBufferingQueue() {
      return this._responseQueue;
   }

   public int getRetryCount() {
      return this._retryCount;
   }

   public void setRetryCount(int var1) {
      this._retryCount = var1;
   }

   public String getRetryDelay() {
      return this._retryDelay;
   }

   public void setRetryDelay(String var1) {
      this._retryDelay = var1;
   }
}
