package weblogic.wsee.config;

import java.util.HashMap;
import java.util.Map;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.management.configuration.WebServicePersistenceMBean;
import weblogic.management.configuration.WebServicePhysicalStoreMBean;

public class WebServicePersistenceMBeanImpl extends DummyConfigurationMBeanImpl implements WebServicePersistenceMBean {
   private String _logicalStoreName;
   private Map<String, WebServiceLogicalStoreMBeanImpl> _logicalStoreMap = new HashMap();
   private Map<String, WebServicePhysicalStoreMBeanImpl> _physicalStoreMap = new HashMap();

   public WebServicePersistenceMBeanImpl() {
      WebServiceLogicalStoreMBeanImpl var1 = new WebServiceLogicalStoreMBeanImpl("WseeStore");
      this._logicalStoreMap.put(var1.getName(), var1);
      this._logicalStoreName = var1.getName();
      WebServicePhysicalStoreMBeanImpl var2 = new WebServicePhysicalStoreMBeanImpl("WseeFileStore");
      this._physicalStoreMap.put(var2.getName(), var2);
   }

   public String getDefaultLogicalStoreName() {
      return this._logicalStoreName;
   }

   public void setDefaultLogicalStoreName(String var1) {
      this._logicalStoreName = var1;
   }

   public WebServiceLogicalStoreMBean createWebServiceLogicalStore(String var1) {
      WebServiceLogicalStoreMBeanImpl var2 = new WebServiceLogicalStoreMBeanImpl(var1);
      this._logicalStoreMap.put(var1, var2);
      return var2;
   }

   public void destroyWebServiceLogicalStore(WebServiceLogicalStoreMBean var1) {
      this._logicalStoreMap.remove(var1.getName());
   }

   public WebServiceLogicalStoreMBean[] getWebServiceLogicalStores() {
      return (WebServiceLogicalStoreMBean[])this._logicalStoreMap.values().toArray(new WebServiceLogicalStoreMBean[0]);
   }

   public WebServiceLogicalStoreMBean lookupWebServiceLogicalStore(String var1) {
      return (WebServiceLogicalStoreMBean)this._logicalStoreMap.get(var1);
   }

   public WebServiceLogicalStoreMBean getLogicalStore(String var1) {
      return (WebServiceLogicalStoreMBean)this._logicalStoreMap.get(var1);
   }

   public WebServicePhysicalStoreMBean createWebServicePhysicalStore(String var1) {
      WebServicePhysicalStoreMBeanImpl var2 = new WebServicePhysicalStoreMBeanImpl(var1);
      this._physicalStoreMap.put(var1, var2);
      return var2;
   }

   public void destroyWebServicePhysicalStore(WebServicePhysicalStoreMBean var1) {
      this._physicalStoreMap.remove(var1.getName());
   }

   public WebServicePhysicalStoreMBean[] getWebServicePhysicalStores() {
      return (WebServicePhysicalStoreMBean[])this._physicalStoreMap.values().toArray(new WebServicePhysicalStoreMBean[0]);
   }

   public WebServicePhysicalStoreMBean lookupWebServicePhysicalStore(String var1) {
      return (WebServicePhysicalStoreMBean)this._physicalStoreMap.get(var1);
   }
}
