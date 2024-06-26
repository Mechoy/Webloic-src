package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class ContainerDescriptorBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(ContainerDescriptorBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("CheckAuthOnForward", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getCheckAuthOnForward", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("FilterDispatchedRequestsEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isFilterDispatchedRequestsEnabled", "setFilterDispatchedRequestsEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RedirectContentType", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getRedirectContentType", "setRedirectContentType");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RedirectContent", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getRedirectContent", "setRedirectContent");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RedirectWithAbsoluteUrl", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isRedirectWithAbsoluteUrl", "setRedirectWithAbsoluteUrl");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("IndexDirectoryEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isIndexDirectoryEnabled", "setIndexDirectoryEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("IndexDirectoryEnabledSet", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isIndexDirectoryEnabledSet", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("IndexDirectorySortBy", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getIndexDirectorySortBy", "setIndexDirectorySortBy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ServletReloadCheckSecs", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getServletReloadCheckSecs", "setServletReloadCheckSecs");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ServletReloadCheckSecsSet", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isServletReloadCheckSecsSet", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ResourceReloadCheckSecs", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getResourceReloadCheckSecs", "setResourceReloadCheckSecs");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SingleThreadedServletPoolSize", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getSingleThreadedServletPoolSize", "setSingleThreadedServletPoolSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SessionMonitoringEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isSessionMonitoringEnabled", "setSessionMonitoringEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SaveSessionsEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isSaveSessionsEnabled", "setSaveSessionsEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PreferWebInfClasses", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isPreferWebInfClasses", "setPreferWebInfClasses");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PreferApplicationPackages", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getPreferApplicationPackages", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PreferApplicationResources", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getPreferApplicationResources", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DefaultMimeType", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getDefaultMimeType", "setDefaultMimeType");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ReloginEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isReloginEnabled", "setReloginEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("AllowAllRoles", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isAllowAllRoles", "setAllowAllRoles");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ClientCertProxyEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isClientCertProxyEnabled", "setClientCertProxyEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("NativeIOEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isNativeIOEnabled", "setNativeIOEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MinimumNativeFileSize", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getMinimumNativeFileSize", "setMinimumNativeFileSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DisableImplicitServletMappings", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isDisableImplicitServletMappings", "setDisableImplicitServletMappings");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TempDir", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getTempDir", "setTempDir");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("OptimisticSerialization", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isOptimisticSerialization", "setOptimisticSerialization");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RetainOriginalURL", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isRetainOriginalURL", "setRetainOriginalURL");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "getId", "setId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ShowArchivedRealPathEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isShowArchivedRealPathEnabled", "setShowArchivedRealPathEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ShowArchivedRealPathEnabledSet", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isShowArchivedRealPathEnabledSet", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RequireAdminTraffic", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isRequireAdminTraffic", "setRequireAdminTraffic");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("AccessLoggingDisabled", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isAccessLoggingDisabled", "setAccessLoggingDisabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("AccessLoggingDisabledSet", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isAccessLoggingDisabledSet", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PreferForwardQueryString", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isPreferForwardQueryString", "setPreferForwardQueryString");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PreferForwardQueryStringSet", Class.forName("weblogic.j2ee.descriptor.wl.ContainerDescriptorBeanDConfig"), "isPreferForwardQueryStringSet", (String)null);
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
            throw new AssertionError("Failed to create PropertyDescriptors for ContainerDescriptorBeanDConfigBeanInfo");
         }
      }
   }
}
