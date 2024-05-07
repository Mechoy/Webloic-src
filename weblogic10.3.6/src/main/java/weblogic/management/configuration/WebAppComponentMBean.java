package weblogic.management.configuration;

import java.util.Map;
import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

/** @deprecated */
public interface WebAppComponentMBean extends ComponentMBean, WebDeploymentMBean {
   String[] INDEX_FILES = new String[]{"index.html", "index.htm", "index.jsp"};

   /** @deprecated */
   int getSessionCookieMaxAgeSecs();

   /** @deprecated */
   void setSessionCookieMaxAgeSecs(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getSessionInvalidationIntervalSecs();

   /** @deprecated */
   void setSessionInvalidationIntervalSecs(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getSessionJDBCConnectionTimeoutSecs();

   /** @deprecated */
   void setSessionJDBCConnectionTimeoutSecs(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getSessionTimeoutSecs();

   /** @deprecated */
   void setSessionTimeoutSecs(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getMimeTypeDefault();

   /** @deprecated */
   void setMimeTypeDefault(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   Map getMimeTypes();

   /** @deprecated */
   void setMimeTypes(Map var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getDocumentRoot();

   /** @deprecated */
   void setDocumentRoot(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getDefaultServlet();

   /** @deprecated */
   void setDefaultServlet(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isIndexDirectoryEnabled();

   /** @deprecated */
   void setIndexDirectoryEnabled(boolean var1);

   /** @deprecated */
   String[] getIndexFiles();

   /** @deprecated */
   void setIndexFiles(String[] var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean addIndexFile(String var1);

   /** @deprecated */
   boolean removeIndexFile(String var1);

   /** @deprecated */
   String getServletClasspath();

   /** @deprecated */
   void setServletClasspath(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isServletExtensionCaseSensitive();

   /** @deprecated */
   void setServletExtensionCaseSensitive(boolean var1);

   /** @deprecated */
   int getServletReloadCheckSecs();

   /** @deprecated */
   void setServletReloadCheckSecs(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getSingleThreadedServletPoolSize();

   /** @deprecated */
   void setSingleThreadedServletPoolSize(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String[] getServlets();

   /** @deprecated */
   String getAuthRealmName();

   /** @deprecated */
   void setAuthRealmName(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getAuthFilter();

   /** @deprecated */
   void setAuthFilter(String var1);

   /** @deprecated */
   boolean isDebugEnabled();

   /** @deprecated */
   void setDebugEnabled(boolean var1);

   /** @deprecated */
   boolean isSessionURLRewritingEnabled();

   /** @deprecated */
   boolean isSessionURLRewritingEnabledSet();

   /** @deprecated */
   void setSessionURLRewritingEnabled(boolean var1);

   /** @deprecated */
   int getSessionIDLength();

   /** @deprecated */
   void setSessionIDLength(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getSessionCacheSize();

   /** @deprecated */
   void setSessionCacheSize(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isSessionCookiesEnabled();

   /** @deprecated */
   void setSessionCookiesEnabled(boolean var1);

   /** @deprecated */
   boolean isSessionTrackingEnabled();

   /** @deprecated */
   void setSessionTrackingEnabled(boolean var1);

   /** @deprecated */
   String getSessionCookieComment();

   /** @deprecated */
   void setSessionCookieComment(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getSessionCookieDomain();

   /** @deprecated */
   void setSessionCookieDomain(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getSessionCookieName();

   /** @deprecated */
   void setSessionCookieName(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getSessionCookiePath();

   /** @deprecated */
   void setSessionCookiePath(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getSessionPersistentStoreDir();

   /** @deprecated */
   void setSessionPersistentStoreDir(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getSessionPersistentStorePool();

   /** @deprecated */
   void setSessionPersistentStorePool(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getSessionPersistentStoreTable();

   /** @deprecated */
   void setSessionPersistentStoreTable(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isSessionPersistentStoreShared();

   /** @deprecated */
   void setSessionPersistentStoreShared(boolean var1);

   /** @deprecated */
   String getSessionPersistentStoreType();

   /** @deprecated */
   void setSessionPersistentStoreType(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getSessionPersistentStoreCookieName();

   /** @deprecated */
   void setSessionPersistentStoreCookieName(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getSessionSwapIntervalSecs();

   /** @deprecated */
   void setSessionSwapIntervalSecs(int var1) throws InvalidAttributeValueException;

   void setSessionDebuggable(boolean var1);

   boolean isSessionDebuggable();

   /** @deprecated */
   void setCleanupSessionFilesEnabled(boolean var1);

   /** @deprecated */
   boolean isCleanupSessionFilesEnabled();

   /** @deprecated */
   String getContextPath();

   /** @deprecated */
   void setContextPath(String var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   String getSessionMainAttribute();

   /** @deprecated */
   void setSessionMainAttribute(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isSessionMonitoringEnabled();

   /** @deprecated */
   void setSessionMonitoringEnabled(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isPreferWebInfClasses();

   /** @deprecated */
   void setPreferWebInfClasses(boolean var1);
}
