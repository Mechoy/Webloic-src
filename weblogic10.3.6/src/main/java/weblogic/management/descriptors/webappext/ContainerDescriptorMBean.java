package weblogic.management.descriptors.webappext;

import weblogic.management.descriptors.WebElementMBean;

public interface ContainerDescriptorMBean extends WebElementMBean {
   boolean isCheckAuthOnForwardEnabled();

   void setCheckAuthOnForwardEnabled(boolean var1);

   /** @deprecated */
   String getRedirectContentType();

   /** @deprecated */
   void setRedirectContentType(String var1);

   /** @deprecated */
   String getRedirectContent();

   /** @deprecated */
   void setRedirectContent(String var1);

   boolean isRedirectWithAbsoluteURLEnabled();

   void setRedirectWithAbsoluteURLEnabled(boolean var1);

   boolean isFilterDispatchedRequestsEnabled();

   void setFilterDispatchedRequestsEnabled(boolean var1);

   boolean isFilterDispatchedRequestsEnabledSet();

   boolean isIndexDirectoryEnabled();

   void setIndexDirectoryEnabled(boolean var1);

   boolean isIndexDirectoryEnabledSet();

   String getIndexDirectorySortBy();

   void setIndexDirectorySortBy(String var1);

   boolean isSessionMonitoringEnabled();

   void setSessionMonitoringEnabled(boolean var1);

   boolean isSessionMonitoringEnabledSet();

   int getServletReloadCheckSecs();

   void setServletReloadCheckSecs(int var1);

   boolean isServletReloadCheckSecsSet();

   int getSingleThreadedServletPoolSize();

   void setSingleThreadedServletPoolSize(int var1);

   boolean isSingleThreadedServletPoolSizeSet();

   boolean isPreferWebInfClasses();

   void setPreferWebInfClasses(boolean var1);

   boolean isPreferWebInfClassesSet();

   void setDefaultMimeType(String var1);

   String getDefaultMimeType();

   void setSaveSessionsEnabled(boolean var1);

   boolean isSaveSessionsEnabledSet();

   boolean isSaveSessionsEnabled();
}
