package weblogic.wsee.config;

import weblogic.management.configuration.WebServiceBufferingMBean;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.management.configuration.WebServicePersistenceMBean;
import weblogic.management.configuration.WebServiceReliabilityMBean;

public class WebServiceMBeanImpl extends DummyConfigurationMBeanImpl implements WebServiceMBean {
   private WebServicePersistenceMBeanImpl _persistence = new WebServicePersistenceMBeanImpl();
   private WebServiceBufferingMBeanImpl _buffering = new WebServiceBufferingMBeanImpl();
   private WebServiceReliabilityMBeanImpl _reliability = new WebServiceReliabilityMBeanImpl();

   public void setJmsConnectionFactory(String var1) {
      throw new IllegalStateException("Not implemented");
   }

   public String getJmsConnectionFactory() {
      throw new IllegalStateException("Not implemented");
   }

   public void setMessagingQueue(String var1) {
      throw new IllegalStateException("Not implemented");
   }

   public String getMessagingQueue() {
      throw new IllegalStateException("Not implemented");
   }

   public void setMessagingQueueMDBRunAsPrincipalName(String var1) {
      throw new IllegalStateException("Not implemented");
   }

   public String getMessagingQueueMDBRunAsPrincipalName() {
      throw new IllegalStateException("Not implemented");
   }

   public void setCallbackQueue(String var1) {
      throw new IllegalStateException("Not implemented");
   }

   public String getCallbackQueue() {
      throw new IllegalStateException("Not implemented");
   }

   public void setCallbackQueueMDBRunAsPrincipalName(String var1) {
      throw new IllegalStateException("Not implemented");
   }

   public String getCallbackQueueMDBRunAsPrincipalName() {
      throw new IllegalStateException("Not implemented");
   }

   public WebServicePersistenceMBean getWebServicePersistence() {
      return this._persistence;
   }

   public WebServiceBufferingMBean getWebServiceBuffering() {
      return this._buffering;
   }

   public WebServiceReliabilityMBean getWebServiceReliability() {
      return this._reliability;
   }
}
