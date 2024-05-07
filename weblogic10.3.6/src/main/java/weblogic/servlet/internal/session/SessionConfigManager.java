package weblogic.servlet.internal.session;

import java.util.ArrayList;
import java.util.HashMap;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.SessionConfigBean;
import weblogic.j2ee.descriptor.wl.ContainerDescriptorBean;
import weblogic.j2ee.descriptor.wl.SessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.internal.WebAppModule;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebComponentBeanUpdateListener;

public final class SessionConfigManager implements SessionConstants {
   private final WebAppModule module;
   private final WebAppServletContext servletContext;
   private String persistentStoreType = "memory";
   private String sessionPersistentStoreDir = "session_db";
   private String sessionPersistentStoreTable = "wl_servlet_sessions";
   private String maxInactiveIntervalColumnName = "wl_max_inactive_interval";
   private String cookieName = "JSESSIONID";
   private String cookiePath = "/";
   private String cookiePersistentStoreCookieName = "WLCOOKIE";
   private int jdbcConnectionTimeoutSecs = 120;
   private int idLength = 52;
   private String sessionPersistentStorePool;
   private String sessionPersistentDataSourceJNDIName;
   private int sessionPersistentFlushInterval;
   private int sessionPersistentFlushThreshold;
   private int sessionPersistentQueueTimeout;
   private String cookieDomain;
   private String cookieComment;
   private boolean sessionTrackingEnabled = true;
   private boolean sessionCookiesEnabled = true;
   private boolean urlRewritingEnabled = true;
   private boolean cacheSessionCookieEnabled = true;
   private boolean sessionSharingEnabled;
   private boolean invalidateOnRelogin = false;
   private boolean encodeSessionIdInQueryParamsEnabled;
   private boolean cookieSecure;
   private boolean cookieHttpOnly = true;
   private boolean cleanupSessionFilesEnabled;
   private boolean saveSessionsOnRedeploy;
   private boolean debugEnabled = false;
   private int cookieMaxAgeSecs = -1;
   private int sessionTimeoutSecs = 3600;
   private boolean isTimeoutSecsSet = false;
   private int maxInMemorySessions = -1;
   private int cacheSize = 1024;
   private String monitoringAttributeName = null;
   private int invalidationIntervalSecs = 60;
   private final BeanUpdateListener sessionBeanListener;
   private String wlsAuthCookieName = "_WL_AUTHCOOKIE_JSESSIONID";
   public static final HashMap SESSION_ELEMENTS_MAP = new HashMap();

   SessionConfigManager(SessionDescriptorBean var1, WebAppServletContext var2) {
      this.servletContext = var2;
      this.module = var2.getWebAppModule();
      this.initWithSessionConfigBean();
      this.initWithSessionDescriptorBean(var1);
      this.sessionBeanListener = this.createBeanUpdateListener();
   }

   public String getPersistentStoreDir() {
      return this.sessionPersistentStoreDir;
   }

   public void setPersistentStoreDir(String var1) {
      this.sessionPersistentStoreDir = var1;
   }

   public int getJDBCConnectionTimeoutSecs() {
      return this.jdbcConnectionTimeoutSecs;
   }

   public void setJDBCConnectionTimeoutSecs(int var1) {
      this.jdbcConnectionTimeoutSecs = var1;
   }

   public String getPersistentStorePool() {
      return this.sessionPersistentStorePool;
   }

   public void setPersistentStorePool(String var1) {
      this.sessionPersistentStorePool = var1;
   }

   public String getPersistentDataSourceJNDIName() {
      return this.sessionPersistentDataSourceJNDIName;
   }

   public void setPersistentDataSourceJNDIName(String var1) {
      this.sessionPersistentDataSourceJNDIName = var1;
   }

   public int getPersistentSessionFlushInterval() {
      return this.sessionPersistentFlushInterval;
   }

   public void setPersistentSessionFlushInterval(int var1) {
      this.sessionPersistentFlushInterval = var1;
   }

   public int getPersistentAsyncQueueTimeout() {
      return this.sessionPersistentQueueTimeout;
   }

   public void setPersistentAsyncQueueTimeout(int var1) {
      this.sessionPersistentQueueTimeout = var1;
   }

   public int getPersistentSessionFlushThreshold() {
      return this.sessionPersistentFlushThreshold;
   }

   public void setPersistentSessionFlushThreshold(int var1) {
      this.sessionPersistentFlushThreshold = var1;
   }

   public String getPersistentStoreTable() {
      return this.sessionPersistentStoreTable;
   }

   public void setPersistentStoreTable(String var1) {
      this.sessionPersistentStoreTable = var1;
   }

   public String getMaxInactiveIntervalColumnName() {
      return this.maxInactiveIntervalColumnName;
   }

   public void setMaxInactiveIntervalColumnName(String var1) {
      this.maxInactiveIntervalColumnName = var1;
   }

   public boolean isCleanupSessionFilesEnabled() {
      return this.cleanupSessionFilesEnabled;
   }

   public void setCleanupSessionFilesEnabled(boolean var1) {
      this.cleanupSessionFilesEnabled = var1;
   }

   public boolean isSaveSessionsOnRedeployEnabled() {
      return this.saveSessionsOnRedeploy;
   }

   public void setSaveSessionsOnRedeployEnabled(boolean var1) {
      this.saveSessionsOnRedeploy = var1;
   }

   boolean isDebugEnabled() {
      return this.debugEnabled;
   }

   public boolean isCookieSecure() {
      return this.cookieSecure;
   }

   public void setCookieSecure(boolean var1) {
      this.cookieSecure = var1;
   }

   public boolean isCookieHttpOnly() {
      return this.cookieHttpOnly;
   }

   public void setCookieHttpOnly(boolean var1) {
      this.cookieHttpOnly = var1;
   }

   public String getCookieComment() {
      return this.cookieComment;
   }

   public void setCookieComment(String var1) {
      this.cookieComment = var1;
   }

   public String getCookieName() {
      return this.cookieName;
   }

   public void setCookieName(String var1) {
      if (var1 == null) {
         var1 = "JSESSIONID";
      }

      this.cookieName = var1;
   }

   public String getWLSAuthCookieName() {
      return this.wlsAuthCookieName;
   }

   public String getCookiePersistentStoreCookieName() {
      return this.cookiePersistentStoreCookieName;
   }

   public void setCookiePersistentStoreCookieName(String var1) {
      if (var1 == null) {
         var1 = "WLCOOKIE";
      }

      this.cookiePersistentStoreCookieName = var1;
   }

   public int getCookieMaxAgeSecs() {
      return this.cookieMaxAgeSecs;
   }

   public String getCookieDomain() {
      return this.cookieDomain;
   }

   public void setCookieDomain(String var1) {
      this.cookieDomain = var1;
   }

   public String getCookiePath() {
      return this.cookiePath;
   }

   public void setCookiePath(String var1) {
      if (var1 == null) {
         var1 = "/";
      }

      this.cookiePath = var1;
   }

   public int getSessionTimeoutSecs() {
      return this.sessionTimeoutSecs;
   }

   public int getMaxInMemorySessions() {
      return this.maxInMemorySessions;
   }

   public int getCacheSize() {
      return this.cacheSize;
   }

   public int getIDLength() {
      return this.idLength;
   }

   public void setIDLength(int var1) {
      this.idLength = var1;
   }

   String getMonitoringAttributeName() {
      return this.monitoringAttributeName;
   }

   public boolean isMonitoringEnabled() {
      if (this.module != null && this.module.getWlWebAppBean() != null) {
         WeblogicWebAppBean var1 = this.module.getWlWebAppBean();
         ContainerDescriptorBean var2 = (ContainerDescriptorBean)DescriptorUtils.getFirstChildOrDefaultBean(var1, var1.getContainerDescriptors(), "ContainerDescriptor");
         return var2.isSessionMonitoringEnabled();
      } else {
         return false;
      }
   }

   public boolean isUrlRewritingEnabled() {
      return this.urlRewritingEnabled;
   }

   public void setUrlRewritingEnabled(boolean var1) {
      this.urlRewritingEnabled = var1;
   }

   public boolean isEncodeSessionIdInQueryParamsEnabled() {
      return this.encodeSessionIdInQueryParamsEnabled;
   }

   public boolean isCacheSessionCookieEnabled() {
      return this.cacheSessionCookieEnabled;
   }

   public boolean isSessionTrackingEnabled() {
      return this.sessionTrackingEnabled;
   }

   public boolean isSessionCookiesEnabled() {
      return this.sessionCookiesEnabled;
   }

   public boolean isSessionSharingEnabled() {
      return this.sessionSharingEnabled;
   }

   public boolean isInvalidateOnRelogin() {
      return this.invalidateOnRelogin;
   }

   public int getInvalidationIntervalSecs() {
      return this.invalidationIntervalSecs;
   }

   public String getPersistentStoreType() {
      return this.persistentStoreType;
   }

   public boolean isSamePersistentStore(SessionConfigManager var1) {
      if (var1 == null) {
         return false;
      } else if (!this.persistentStoreType.equals(var1.getPersistentStoreType())) {
         return false;
      } else if (this.persistentStoreType.equals("file")) {
         return this.sessionPersistentStoreDir.equals(var1.sessionPersistentStoreDir);
      } else if (!this.persistentStoreType.equals("jdbc")) {
         return true;
      } else {
         return this.sessionPersistentStorePool.equals(var1.sessionPersistentStorePool) && this.sessionPersistentStoreTable.equals(var1.sessionPersistentStoreTable);
      }
   }

   public BeanUpdateListener getBeanUpdateListener() {
      return this.sessionBeanListener;
   }

   private void initWithSessionConfigBean() {
      SessionConfigBean[] var1 = null;
      if (this.module != null && this.module.getWebAppBean() != null) {
         var1 = this.module.getWebAppBean().getSessionConfigs();
      }

      if (var1 != null && var1.length > 0) {
         int var2 = var1[0].getSessionTimeout();
         if (var2 == 0) {
            var2 = -1;
         }

         if (var2 != 60) {
            this.sessionTimeoutSecs = var2 * 60;
            this.isTimeoutSecsSet = true;
         }
      }

   }

   private void initWithSessionDescriptorBean(SessionDescriptorBean var1) {
      if (var1 != null) {
         this.sessionCookiesEnabled = var1.isCookiesEnabled();
         this.urlRewritingEnabled = var1.isUrlRewritingEnabled();
         this.encodeSessionIdInQueryParamsEnabled = var1.isEncodeSessionIdInQueryParams();
         this.sessionTrackingEnabled = var1.isTrackingEnabled();
         this.cacheSessionCookieEnabled = var1.isHttpProxyCachingOfCookies();
         this.idLength = var1.getIdLength();
         this.cookieComment = var1.getCookieComment();
         this.cookieDomain = var1.getCookieDomain();
         this.cookieName = var1.getCookieName();
         this.cookiePath = var1.getCookiePath();
         this.jdbcConnectionTimeoutSecs = var1.getJdbcConnectionTimeoutSecs();
         this.cookiePersistentStoreCookieName = var1.getPersistentStoreCookieName();
         this.sessionPersistentDataSourceJNDIName = var1.getPersistentDataSourceJNDIName();
         this.sessionPersistentFlushInterval = var1.getPersistentSessionFlushInterval();
         this.sessionPersistentFlushThreshold = var1.getPersistentSessionFlushThreshold();
         this.sessionPersistentQueueTimeout = var1.getPersistentAsyncQueueTimeout();
         this.sessionPersistentStoreDir = var1.getPersistentStoreDir();
         this.sessionPersistentStorePool = var1.getPersistentStorePool();
         this.sessionPersistentStoreTable = var1.getPersistentStoreTable();
         this.maxInactiveIntervalColumnName = var1.getJdbcColumnNameMaxInactiveInterval();
         this.cookieSecure = var1.isCookieSecure();
         this.cookieHttpOnly = var1.isCookieHttpOnly();
         this.persistentStoreType = var1.getPersistentStoreType();
         this.sessionSharingEnabled = var1.isSharingEnabled();
         this.invalidateOnRelogin = var1.isInvalidateOnRelogin();
         if (this.module != null && this.module.getWlWebAppBean() != null) {
            WeblogicWebAppBean var2 = this.module.getWlWebAppBean();
            SessionDescriptorBean[] var3 = var2.getSessionDescriptors();
            if (var3 != null && var3.length > 0) {
               var1 = var3[0];
            }
         }

         this.debugEnabled = var1.isDebugEnabled();
         this.cookieMaxAgeSecs = var1.getCookieMaxAgeSecs();
         if (!this.isTimeoutSecsSet) {
            this.sessionTimeoutSecs = var1.getTimeoutSecs();
         }

         this.maxInMemorySessions = var1.getMaxInMemorySessions();
         this.cacheSize = var1.getCacheSize();
         this.monitoringAttributeName = var1.getMonitoringAttributeName();
         this.invalidationIntervalSecs = var1.getInvalidationIntervalSecs();
         if (var1.getCookiePath() != null && !"/".equals(var1.getCookiePath())) {
            this.cookiePath = var1.getCookiePath();
         }

         if (this.cookieName != null) {
            this.wlsAuthCookieName = "_WL_AUTHCOOKIE_" + this.cookieName;
         }

      }
   }

   private BeanUpdateListener createBeanUpdateListener() {
      return new WebComponentBeanUpdateListener() {
         protected void handlePropertyRemove(BeanUpdateEvent.PropertyUpdate var1) {
            String var2 = var1.getPropertyName();
            if ("InvalidationIntervalSecs".equals(var2)) {
               SessionConfigManager.this.invalidationIntervalSecs = 60;
               SessionConfigManager.this.servletContext.getSessionContext().setInvalidationIntervalSecs(60);
            } else if ("DebugEnabled".equals(var2)) {
               SessionConfigManager.this.debugEnabled = false;
            } else if ("CookieMaxAgeSecs".equals(var2)) {
               SessionConfigManager.this.cookieMaxAgeSecs = -1;
            } else if ("TimeoutSecs".equals(var2) && !SessionConfigManager.this.isTimeoutSecsSet) {
               SessionConfigManager.this.sessionTimeoutSecs = 3600;
            } else if ("MaxInMemorySessions".equals(var2)) {
               SessionConfigManager.this.maxInMemorySessions = -1;
            } else if ("CacheSize".equals(var2)) {
               SessionConfigManager.this.cacheSize = 1024;
            } else if ("MonitoringAttributeName".equals(var2)) {
               SessionConfigManager.this.monitoringAttributeName = null;
            }

         }

         protected void handlePropertyChange(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) {
            SessionDescriptorBean var3 = (SessionDescriptorBean)var2;
            String var4 = var1.getPropertyName();
            if ("InvalidationIntervalSecs".equals(var4)) {
               SessionConfigManager.this.invalidationIntervalSecs = var3.getInvalidationIntervalSecs();
               SessionConfigManager.this.servletContext.getSessionContext().setInvalidationIntervalSecs(var3.getInvalidationIntervalSecs());
            } else if ("DebugEnabled".equals(var4)) {
               SessionConfigManager.this.debugEnabled = var3.isDebugEnabled();
            } else if ("CookieMaxAgeSecs".equals(var4)) {
               SessionConfigManager.this.cookieMaxAgeSecs = var3.getCookieMaxAgeSecs();
            } else if ("TimeoutSecs".equals(var4) && !SessionConfigManager.this.isTimeoutSecsSet) {
               SessionConfigManager.this.sessionTimeoutSecs = var3.getTimeoutSecs();
            } else if ("MaxInMemorySessions".equals(var4)) {
               SessionConfigManager.this.maxInMemorySessions = var3.getMaxInMemorySessions();
            } else if ("CacheSize".equals(var4)) {
               SessionConfigManager.this.cacheSize = var3.getCacheSize();
            } else if ("MonitoringAttributeName".equals(var4)) {
               SessionConfigManager.this.monitoringAttributeName = var3.getMonitoringAttributeName();
            }

         }

         protected void handleBeanAdd(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) {
            if (var2 instanceof SessionDescriptorBean && var1 != null && "SessionDescriptors".equals(var1.getPropertyName())) {
               SessionDescriptorBean var3 = (SessionDescriptorBean)var2;
               ((DescriptorBean)var3).addBeanUpdateListener(this);
               if (!SessionConfigManager.this.isTimeoutSecsSet) {
                  SessionConfigManager.this.sessionTimeoutSecs = var3.getTimeoutSecs();
                  SessionConfigManager.this.isTimeoutSecsSet = true;
               }

               SessionConfigManager.this.invalidationIntervalSecs = var3.getInvalidationIntervalSecs();
               SessionConfigManager.this.servletContext.getSessionContext().setInvalidationIntervalSecs(SessionConfigManager.this.invalidationIntervalSecs);
               SessionConfigManager.this.debugEnabled = var3.isDebugEnabled();
               SessionConfigManager.this.maxInMemorySessions = var3.getMaxInMemorySessions();
               SessionConfigManager.this.cookieMaxAgeSecs = var3.getCookieMaxAgeSecs();
               SessionConfigManager.this.monitoringAttributeName = var3.getMonitoringAttributeName();
            }
         }

         protected void handleBeanRemove(BeanUpdateEvent.PropertyUpdate var1) {
            if (var1 != null && "SessionDescriptors".equals(var1.getPropertyName()) && var1.getRemovedObject() instanceof SessionDescriptorBean) {
               SessionDescriptorBean var2 = (SessionDescriptorBean)var1.getRemovedObject();
               ((DescriptorBean)var2).removeBeanUpdateListener(this);
               if (!SessionConfigManager.this.isTimeoutSecsSet) {
                  SessionConfigManager.this.sessionTimeoutSecs = 3600;
                  SessionConfigManager.this.isTimeoutSecsSet = true;
               }

               SessionConfigManager.this.invalidationIntervalSecs = 60;
               SessionConfigManager.this.servletContext.getSessionContext().setInvalidationIntervalSecs(SessionConfigManager.this.invalidationIntervalSecs);
               SessionConfigManager.this.debugEnabled = false;
               SessionConfigManager.this.maxInMemorySessions = -1;
               SessionConfigManager.this.cookieMaxAgeSecs = -1;
               SessionConfigManager.this.monitoringAttributeName = null;
            }
         }

         protected void prepareBeanAdd(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) throws BeanUpdateRejectedException {
            if (var2 instanceof SessionDescriptorBean) {
               SessionDescriptorBean var3 = (SessionDescriptorBean)var2;
               SessionDescriptorBean var4 = (SessionDescriptorBean)DescriptorUtils.getFirstChildOrDefaultBean(SessionConfigManager.this.module.getWlWebAppBean(), SessionConfigManager.this.module.getWlWebAppBean().getSessionDescriptors(), "SessionDescriptor");
               ArrayList var5 = new ArrayList();
               computeChange("id-length", var3.getIdLength(), var4.getIdLength(), var5);
               computeChange("tracking-enabled", var3.isTrackingEnabled(), var4.isTrackingEnabled(), var5);
               computeChange("cache-size", var3.getCacheSize(), var4.getCacheSize(), var5);
               computeChange("max-in-memory-sessions", var3.getMaxInMemorySessions(), var4.getMaxInMemorySessions(), var5);
               computeChange("cookies-enabled", var3.isCookiesEnabled(), var4.isCookiesEnabled(), var5);
               computeChange("cookie-name", var3.getCookieName(), var4.getCookieName(), var5);
               computeChange("cookie-path", var3.getCookiePath(), var4.getCookiePath(), var5);
               computeChange("cookie-domain", var3.getCookieDomain(), var4.getCookieDomain(), var5);
               computeChange("cookie-comment", var3.getCookieComment(), var4.getCookieComment(), var5);
               computeChange("cookie-secure", var3.isCookieSecure(), var4.isCookieSecure(), var5);
               computeChange("persistent-store-type", var3.getPersistentStoreType(), var4.getPersistentStoreType(), var5);
               computeChange("persistent-store-cookie-name", var3.getPersistentStoreCookieName(), var4.getPersistentStoreCookieName(), var5);
               computeChange("persistent-store-dir", var3.getPersistentStoreDir(), var4.getPersistentStoreDir(), var5);
               computeChange("persistent-store-pool", var3.getPersistentStorePool(), var4.getPersistentStorePool(), var5);
               computeChange("persistent-data-source-jndi-name", var3.getPersistentDataSourceJNDIName(), var4.getPersistentDataSourceJNDIName(), var5);
               computeChange("persistent-session-flush-interval", var3.getPersistentSessionFlushInterval(), var4.getPersistentSessionFlushInterval(), var5);
               computeChange("persistent-session-flush-threshold", var3.getPersistentSessionFlushThreshold(), var4.getPersistentSessionFlushThreshold(), var5);
               computeChange("persistent-async-queue-timeout", var3.getPersistentAsyncQueueTimeout(), var4.getPersistentAsyncQueueTimeout(), var5);
               computeChange("persistent-store-table", var3.getPersistentStoreTable(), var4.getPersistentStoreTable(), var5);
               computeChange("jdbc-column-name-max-inactive-interval", var3.getJdbcColumnNameMaxInactiveInterval(), var4.getJdbcColumnNameMaxInactiveInterval(), var5);
               computeChange("jdbc-connection-timeout-secs", var3.getJdbcConnectionTimeoutSecs(), var4.getJdbcConnectionTimeoutSecs(), var5);
               computeChange("url-rewriting-enabled", var3.isUrlRewritingEnabled(), var4.isUrlRewritingEnabled(), var5);
               computeChange("http-proxy-caching-of-cookies", var3.isHttpProxyCachingOfCookies(), var4.isHttpProxyCachingOfCookies(), var5);
               computeChange("encode-session-id-in-query-params", var3.isEncodeSessionIdInQueryParams(), var4.isEncodeSessionIdInQueryParams(), var5);
               computeChange("sharing-enabled", var3.isSharingEnabled(), var4.isSharingEnabled(), var5);
               computeChange("cookie-http-only", var3.isCookieHttpOnly(), var4.isCookieHttpOnly(), var5);
               if (!var5.isEmpty()) {
                  throw new BeanUpdateRejectedException("Non-Dynamic property in \"session-descriptor\" is/are specified in deployment plan: '" + getChangedPropertyNames(var5) + "'");
               }
            }
         }
      };
   }

   static {
      SESSION_ELEMENTS_MAP.put("PersistentStoreType".toLowerCase(), "persistent-store-type");
      SESSION_ELEMENTS_MAP.put("CookiesEnabled".toLowerCase(), "cookies-enabled");
      SESSION_ELEMENTS_MAP.put("URLRewritingEnabled".toLowerCase(), "url-rewriting-enabled");
      SESSION_ELEMENTS_MAP.put("EncodeSessionIdInQueryParams".toLowerCase(), "encode-session-id-in-query-params");
      SESSION_ELEMENTS_MAP.put("TrackingEnabled".toLowerCase(), "tracking-enabled");
      SESSION_ELEMENTS_MAP.put("CacheSessionCookie".toLowerCase(), "http-proxy-caching-of-cookies");
      SESSION_ELEMENTS_MAP.put("IDLength".toLowerCase(), "id-length");
      SESSION_ELEMENTS_MAP.put("CookieComment".toLowerCase(), "cookie-comment");
      SESSION_ELEMENTS_MAP.put("CookieDomain".toLowerCase(), "cookie-domain");
      SESSION_ELEMENTS_MAP.put("CookieMaxAgeSecs".toLowerCase(), "cookie-max-age-secs");
      SESSION_ELEMENTS_MAP.put("CookieName".toLowerCase(), "cookie-name");
      SESSION_ELEMENTS_MAP.put("CookiePath".toLowerCase(), "cookie-path");
      SESSION_ELEMENTS_MAP.put("InvalidationIntervalSecs".toLowerCase(), "invalidation-interval-secs");
      SESSION_ELEMENTS_MAP.put("PersistentStoreCookieName".toLowerCase(), "persistent-store-cookie-name");
      SESSION_ELEMENTS_MAP.put("PersistentStoreDir".toLowerCase(), "persistent-store-dir");
      SESSION_ELEMENTS_MAP.put("PersistentStorePool".toLowerCase(), "persistent-store-pool");
      SESSION_ELEMENTS_MAP.put("PersistentStoreTable".toLowerCase(), "persistent-store-table");
      SESSION_ELEMENTS_MAP.put("SessionDebuggable".toLowerCase(), "debug-enabled");
      SESSION_ELEMENTS_MAP.put("ConsoleMainAttribute".toLowerCase(), "monitoring-attribute-name");
      SESSION_ELEMENTS_MAP.put("TimeoutSecs".toLowerCase(), "timeout-secs");
      SESSION_ELEMENTS_MAP.put("CookieSecure".toLowerCase(), "cookie-secure");
      SESSION_ELEMENTS_MAP.put("JDBCColumnName_MaxInactiveInterval".toLowerCase(), "jdbc-column-name-max-inactive-interval");
      SESSION_ELEMENTS_MAP.put("CacheSize".toLowerCase(), "cache-size");
      SESSION_ELEMENTS_MAP.put("JDBCConnectionTimeoutSecs".toLowerCase(), "jdbc-connection-timeout-secs");
      SESSION_ELEMENTS_MAP.put("SharingEnabled".toLowerCase(), "sharing-enabled");
      SESSION_ELEMENTS_MAP.put("PersistentDataSourceJNDIName".toLowerCase(), "persistent-data-source-jndi-name");
      SESSION_ELEMENTS_MAP.put("PersistentSessionFlushInterval".toLowerCase(), "persistent-session-flush-interval");
      SESSION_ELEMENTS_MAP.put("PersistentSessionFlushThreshold".toLowerCase(), "persistent-session-flush-threshold");
      SESSION_ELEMENTS_MAP.put("PersistentAsyncQueueTimeout".toLowerCase(), "persistent-async-queue-timeout");
   }
}
