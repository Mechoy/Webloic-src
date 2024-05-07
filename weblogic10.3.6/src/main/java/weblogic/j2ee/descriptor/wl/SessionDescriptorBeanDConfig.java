package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class SessionDescriptorBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private SessionDescriptorBean beanTreeNode;

   public SessionDescriptorBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (SessionDescriptorBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      return null;
   }

   public String keyPropertyValue() {
      return null;
   }

   public void initKeyPropertyValue(String var1) {
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public int getTimeoutSecs() {
      return this.beanTreeNode.getTimeoutSecs();
   }

   public void setTimeoutSecs(int var1) {
      this.beanTreeNode.setTimeoutSecs(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TimeoutSecs", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getInvalidationIntervalSecs() {
      return this.beanTreeNode.getInvalidationIntervalSecs();
   }

   public void setInvalidationIntervalSecs(int var1) {
      this.beanTreeNode.setInvalidationIntervalSecs(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InvalidationIntervalSecs", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isDebugEnabled() {
      return this.beanTreeNode.isDebugEnabled();
   }

   public void setDebugEnabled(boolean var1) {
      this.beanTreeNode.setDebugEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DebugEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getIdLength() {
      return this.beanTreeNode.getIdLength();
   }

   public void setIdLength(int var1) {
      this.beanTreeNode.setIdLength(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IdLength", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isTrackingEnabled() {
      return this.beanTreeNode.isTrackingEnabled();
   }

   public void setTrackingEnabled(boolean var1) {
      this.beanTreeNode.setTrackingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TrackingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getCacheSize() {
      return this.beanTreeNode.getCacheSize();
   }

   public void setCacheSize(int var1) {
      this.beanTreeNode.setCacheSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CacheSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaxInMemorySessions() {
      return this.beanTreeNode.getMaxInMemorySessions();
   }

   public void setMaxInMemorySessions(int var1) {
      this.beanTreeNode.setMaxInMemorySessions(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxInMemorySessions", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isCookiesEnabled() {
      return this.beanTreeNode.isCookiesEnabled();
   }

   public void setCookiesEnabled(boolean var1) {
      this.beanTreeNode.setCookiesEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CookiesEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getCookieName() {
      return this.beanTreeNode.getCookieName();
   }

   public void setCookieName(String var1) {
      this.beanTreeNode.setCookieName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CookieName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getCookiePath() {
      return this.beanTreeNode.getCookiePath();
   }

   public void setCookiePath(String var1) {
      this.beanTreeNode.setCookiePath(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CookiePath", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getCookieDomain() {
      return this.beanTreeNode.getCookieDomain();
   }

   public void setCookieDomain(String var1) {
      this.beanTreeNode.setCookieDomain(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CookieDomain", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getCookieComment() {
      return this.beanTreeNode.getCookieComment();
   }

   public void setCookieComment(String var1) {
      this.beanTreeNode.setCookieComment(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CookieComment", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isCookieSecure() {
      return this.beanTreeNode.isCookieSecure();
   }

   public void setCookieSecure(boolean var1) {
      this.beanTreeNode.setCookieSecure(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CookieSecure", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getCookieMaxAgeSecs() {
      return this.beanTreeNode.getCookieMaxAgeSecs();
   }

   public void setCookieMaxAgeSecs(int var1) {
      this.beanTreeNode.setCookieMaxAgeSecs(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CookieMaxAgeSecs", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isCookieHttpOnly() {
      return this.beanTreeNode.isCookieHttpOnly();
   }

   public void setCookieHttpOnly(boolean var1) {
      this.beanTreeNode.setCookieHttpOnly(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CookieHttpOnly", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPersistentStoreType() {
      return this.beanTreeNode.getPersistentStoreType();
   }

   public void setPersistentStoreType(String var1) {
      this.beanTreeNode.setPersistentStoreType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PersistentStoreType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPersistentStoreCookieName() {
      return this.beanTreeNode.getPersistentStoreCookieName();
   }

   public void setPersistentStoreCookieName(String var1) {
      this.beanTreeNode.setPersistentStoreCookieName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PersistentStoreCookieName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPersistentStoreDir() {
      return this.beanTreeNode.getPersistentStoreDir();
   }

   public void setPersistentStoreDir(String var1) {
      this.beanTreeNode.setPersistentStoreDir(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PersistentStoreDir", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPersistentStorePool() {
      return this.beanTreeNode.getPersistentStorePool();
   }

   public void setPersistentStorePool(String var1) {
      this.beanTreeNode.setPersistentStorePool(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PersistentStorePool", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPersistentDataSourceJNDIName() {
      return this.beanTreeNode.getPersistentDataSourceJNDIName();
   }

   public void setPersistentDataSourceJNDIName(String var1) {
      this.beanTreeNode.setPersistentDataSourceJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PersistentDataSourceJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getPersistentSessionFlushInterval() {
      return this.beanTreeNode.getPersistentSessionFlushInterval();
   }

   public void setPersistentSessionFlushInterval(int var1) {
      this.beanTreeNode.setPersistentSessionFlushInterval(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PersistentSessionFlushInterval", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getPersistentSessionFlushThreshold() {
      return this.beanTreeNode.getPersistentSessionFlushThreshold();
   }

   public void setPersistentSessionFlushThreshold(int var1) {
      this.beanTreeNode.setPersistentSessionFlushThreshold(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PersistentSessionFlushThreshold", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getPersistentAsyncQueueTimeout() {
      return this.beanTreeNode.getPersistentAsyncQueueTimeout();
   }

   public void setPersistentAsyncQueueTimeout(int var1) {
      this.beanTreeNode.setPersistentAsyncQueueTimeout(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PersistentAsyncQueueTimeout", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPersistentStoreTable() {
      return this.beanTreeNode.getPersistentStoreTable();
   }

   public void setPersistentStoreTable(String var1) {
      this.beanTreeNode.setPersistentStoreTable(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PersistentStoreTable", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getJdbcColumnNameMaxInactiveInterval() {
      return this.beanTreeNode.getJdbcColumnNameMaxInactiveInterval();
   }

   public void setJdbcColumnNameMaxInactiveInterval(String var1) {
      this.beanTreeNode.setJdbcColumnNameMaxInactiveInterval(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JdbcColumnNameMaxInactiveInterval", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getJdbcConnectionTimeoutSecs() {
      return this.beanTreeNode.getJdbcConnectionTimeoutSecs();
   }

   public void setJdbcConnectionTimeoutSecs(int var1) {
      this.beanTreeNode.setJdbcConnectionTimeoutSecs(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JdbcConnectionTimeoutSecs", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isUrlRewritingEnabled() {
      return this.beanTreeNode.isUrlRewritingEnabled();
   }

   public void setUrlRewritingEnabled(boolean var1) {
      this.beanTreeNode.setUrlRewritingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UrlRewritingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isHttpProxyCachingOfCookies() {
      return this.beanTreeNode.isHttpProxyCachingOfCookies();
   }

   public void setHttpProxyCachingOfCookies(boolean var1) {
      this.beanTreeNode.setHttpProxyCachingOfCookies(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HttpProxyCachingOfCookies", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isEncodeSessionIdInQueryParams() {
      return this.beanTreeNode.isEncodeSessionIdInQueryParams();
   }

   public void setEncodeSessionIdInQueryParams(boolean var1) {
      this.beanTreeNode.setEncodeSessionIdInQueryParams(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EncodeSessionIdInQueryParams", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getMonitoringAttributeName() {
      return this.beanTreeNode.getMonitoringAttributeName();
   }

   public void setMonitoringAttributeName(String var1) {
      this.beanTreeNode.setMonitoringAttributeName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MonitoringAttributeName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isSharingEnabled() {
      return this.beanTreeNode.isSharingEnabled();
   }

   public void setSharingEnabled(boolean var1) {
      this.beanTreeNode.setSharingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SharingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isInvalidateOnRelogin() {
      return this.beanTreeNode.isInvalidateOnRelogin();
   }

   public void setInvalidateOnRelogin(boolean var1) {
      this.beanTreeNode.setInvalidateOnRelogin(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InvalidateOnRelogin", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getId() {
      return this.beanTreeNode.getId();
   }

   public void setId(String var1) {
      this.beanTreeNode.setId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Id", (Object)null, (Object)null));
      this.setModified(true);
   }
}
