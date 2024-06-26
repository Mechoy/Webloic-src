package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class SessionDescriptorBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(SessionDescriptorBeanDConfig.class);
   static PropertyDescriptor[] pds = null;

   public BeanDescriptor getBeanDescriptor() {
      return this.bd;
   }

   public PropertyDescriptor[] getPropertyDescriptors() {
      if (pds != null) {
         return pds;
      } else {
         ArrayList var2 = new ArrayList();

         try {
            PropertyDescriptor var1 = new PropertyDescriptor("TimeoutSecs", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getTimeoutSecs", "setTimeoutSecs");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("InvalidationIntervalSecs", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getInvalidationIntervalSecs", "setInvalidationIntervalSecs");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DebugEnabled", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "isDebugEnabled", "setDebugEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("IdLength", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getIdLength", "setIdLength");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TrackingEnabled", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "isTrackingEnabled", "setTrackingEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CacheSize", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getCacheSize", "setCacheSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxInMemorySessions", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getMaxInMemorySessions", "setMaxInMemorySessions");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("CookiesEnabled", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "isCookiesEnabled", "setCookiesEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CookieName", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getCookieName", "setCookieName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CookiePath", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getCookiePath", "setCookiePath");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CookieDomain", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getCookieDomain", "setCookieDomain");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CookieComment", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getCookieComment", "setCookieComment");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CookieSecure", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "isCookieSecure", "setCookieSecure");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CookieMaxAgeSecs", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getCookieMaxAgeSecs", "setCookieMaxAgeSecs");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("CookieHttpOnly", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "isCookieHttpOnly", "setCookieHttpOnly");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentStoreType", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getPersistentStoreType", "setPersistentStoreType");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentStoreCookieName", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getPersistentStoreCookieName", "setPersistentStoreCookieName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentStoreDir", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getPersistentStoreDir", "setPersistentStoreDir");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentStorePool", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getPersistentStorePool", "setPersistentStorePool");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentDataSourceJNDIName", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getPersistentDataSourceJNDIName", "setPersistentDataSourceJNDIName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentSessionFlushInterval", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getPersistentSessionFlushInterval", "setPersistentSessionFlushInterval");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentSessionFlushThreshold", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getPersistentSessionFlushThreshold", "setPersistentSessionFlushThreshold");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentAsyncQueueTimeout", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getPersistentAsyncQueueTimeout", "setPersistentAsyncQueueTimeout");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentStoreTable", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getPersistentStoreTable", "setPersistentStoreTable");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("JdbcColumnNameMaxInactiveInterval", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getJdbcColumnNameMaxInactiveInterval", "setJdbcColumnNameMaxInactiveInterval");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("JdbcConnectionTimeoutSecs", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getJdbcConnectionTimeoutSecs", "setJdbcConnectionTimeoutSecs");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("UrlRewritingEnabled", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "isUrlRewritingEnabled", "setUrlRewritingEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("HttpProxyCachingOfCookies", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "isHttpProxyCachingOfCookies", "setHttpProxyCachingOfCookies");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("EncodeSessionIdInQueryParams", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "isEncodeSessionIdInQueryParams", "setEncodeSessionIdInQueryParams");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MonitoringAttributeName", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getMonitoringAttributeName", "setMonitoringAttributeName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SharingEnabled", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "isSharingEnabled", "setSharingEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("InvalidateOnRelogin", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "isInvalidateOnRelogin", "setInvalidateOnRelogin");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.SessionDescriptorBeanDConfig"), "getId", "setId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for SessionDescriptorBeanDConfigBeanInfo");
         }
      }
   }
}
