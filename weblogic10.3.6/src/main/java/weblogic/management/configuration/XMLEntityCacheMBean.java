package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.runtime.EntityCacheCumulativeRuntimeMBean;
import weblogic.management.runtime.EntityCacheCurrentStateRuntimeMBean;

public interface XMLEntityCacheMBean extends ConfigurationMBean {
   String getCacheLocation();

   void setCacheLocation(String var1) throws InvalidAttributeValueException;

   int getCacheMemorySize();

   void setCacheMemorySize(int var1) throws InvalidAttributeValueException;

   int getCacheDiskSize();

   void setCacheDiskSize(int var1) throws InvalidAttributeValueException;

   int getCacheTimeoutInterval();

   void setCacheTimeoutInterval(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   EntityCacheCurrentStateRuntimeMBean getEntityCacheCurrentRuntime();

   void setEntityCacheCurrentRuntime(EntityCacheCurrentStateRuntimeMBean var1);

   /** @deprecated */
   EntityCacheCumulativeRuntimeMBean getEntityCacheSessionRuntime();

   void setEntityCacheSessionRuntime(EntityCacheCumulativeRuntimeMBean var1);

   /** @deprecated */
   EntityCacheCumulativeRuntimeMBean getEntityCacheHistoricalRuntime();

   void setEntityCacheHistoricalRuntime(EntityCacheCumulativeRuntimeMBean var1);

   int getMaxSize();

   void setMaxSize(int var1);
}
