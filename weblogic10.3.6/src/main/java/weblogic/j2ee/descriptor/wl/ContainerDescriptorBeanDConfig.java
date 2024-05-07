package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.EmptyBean;

public class ContainerDescriptorBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ContainerDescriptorBean beanTreeNode;

   public ContainerDescriptorBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ContainerDescriptorBean)var2;
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

   public EmptyBean getCheckAuthOnForward() {
      return this.beanTreeNode.getCheckAuthOnForward();
   }

   public boolean isFilterDispatchedRequestsEnabled() {
      return this.beanTreeNode.isFilterDispatchedRequestsEnabled();
   }

   public void setFilterDispatchedRequestsEnabled(boolean var1) {
      this.beanTreeNode.setFilterDispatchedRequestsEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FilterDispatchedRequestsEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getRedirectContentType() {
      return this.beanTreeNode.getRedirectContentType();
   }

   public void setRedirectContentType(String var1) {
      this.beanTreeNode.setRedirectContentType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RedirectContentType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getRedirectContent() {
      return this.beanTreeNode.getRedirectContent();
   }

   public void setRedirectContent(String var1) {
      this.beanTreeNode.setRedirectContent(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RedirectContent", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isRedirectWithAbsoluteUrl() {
      return this.beanTreeNode.isRedirectWithAbsoluteUrl();
   }

   public void setRedirectWithAbsoluteUrl(boolean var1) {
      this.beanTreeNode.setRedirectWithAbsoluteUrl(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RedirectWithAbsoluteUrl", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isIndexDirectoryEnabled() {
      return this.beanTreeNode.isIndexDirectoryEnabled();
   }

   public void setIndexDirectoryEnabled(boolean var1) {
      this.beanTreeNode.setIndexDirectoryEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IndexDirectoryEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isIndexDirectoryEnabledSet() {
      return this.beanTreeNode.isIndexDirectoryEnabledSet();
   }

   public String getIndexDirectorySortBy() {
      return this.beanTreeNode.getIndexDirectorySortBy();
   }

   public void setIndexDirectorySortBy(String var1) {
      this.beanTreeNode.setIndexDirectorySortBy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IndexDirectorySortBy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getServletReloadCheckSecs() {
      return this.beanTreeNode.getServletReloadCheckSecs();
   }

   public void setServletReloadCheckSecs(int var1) {
      this.beanTreeNode.setServletReloadCheckSecs(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ServletReloadCheckSecs", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isServletReloadCheckSecsSet() {
      return this.beanTreeNode.isServletReloadCheckSecsSet();
   }

   public int getResourceReloadCheckSecs() {
      return this.beanTreeNode.getResourceReloadCheckSecs();
   }

   public void setResourceReloadCheckSecs(int var1) {
      this.beanTreeNode.setResourceReloadCheckSecs(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ResourceReloadCheckSecs", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getSingleThreadedServletPoolSize() {
      return this.beanTreeNode.getSingleThreadedServletPoolSize();
   }

   public void setSingleThreadedServletPoolSize(int var1) {
      this.beanTreeNode.setSingleThreadedServletPoolSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SingleThreadedServletPoolSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isSessionMonitoringEnabled() {
      return this.beanTreeNode.isSessionMonitoringEnabled();
   }

   public void setSessionMonitoringEnabled(boolean var1) {
      this.beanTreeNode.setSessionMonitoringEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SessionMonitoringEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isSaveSessionsEnabled() {
      return this.beanTreeNode.isSaveSessionsEnabled();
   }

   public void setSaveSessionsEnabled(boolean var1) {
      this.beanTreeNode.setSaveSessionsEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SaveSessionsEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isPreferWebInfClasses() {
      return this.beanTreeNode.isPreferWebInfClasses();
   }

   public void setPreferWebInfClasses(boolean var1) {
      this.beanTreeNode.setPreferWebInfClasses(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PreferWebInfClasses", (Object)null, (Object)null));
      this.setModified(true);
   }

   public PreferApplicationPackagesBean getPreferApplicationPackages() {
      return this.beanTreeNode.getPreferApplicationPackages();
   }

   public PreferApplicationResourcesBean getPreferApplicationResources() {
      return this.beanTreeNode.getPreferApplicationResources();
   }

   public String getDefaultMimeType() {
      return this.beanTreeNode.getDefaultMimeType();
   }

   public void setDefaultMimeType(String var1) {
      this.beanTreeNode.setDefaultMimeType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultMimeType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isReloginEnabled() {
      return this.beanTreeNode.isReloginEnabled();
   }

   public void setReloginEnabled(boolean var1) {
      this.beanTreeNode.setReloginEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ReloginEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isAllowAllRoles() {
      return this.beanTreeNode.isAllowAllRoles();
   }

   public void setAllowAllRoles(boolean var1) {
      this.beanTreeNode.setAllowAllRoles(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AllowAllRoles", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isClientCertProxyEnabled() {
      return this.beanTreeNode.isClientCertProxyEnabled();
   }

   public void setClientCertProxyEnabled(boolean var1) {
      this.beanTreeNode.setClientCertProxyEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ClientCertProxyEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isNativeIOEnabled() {
      return this.beanTreeNode.isNativeIOEnabled();
   }

   public void setNativeIOEnabled(boolean var1) {
      this.beanTreeNode.setNativeIOEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "NativeIOEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public long getMinimumNativeFileSize() {
      return this.beanTreeNode.getMinimumNativeFileSize();
   }

   public void setMinimumNativeFileSize(long var1) {
      this.beanTreeNode.setMinimumNativeFileSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MinimumNativeFileSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isDisableImplicitServletMappings() {
      return this.beanTreeNode.isDisableImplicitServletMappings();
   }

   public void setDisableImplicitServletMappings(boolean var1) {
      this.beanTreeNode.setDisableImplicitServletMappings(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DisableImplicitServletMappings", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getTempDir() {
      return this.beanTreeNode.getTempDir();
   }

   public void setTempDir(String var1) {
      this.beanTreeNode.setTempDir(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TempDir", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isOptimisticSerialization() {
      return this.beanTreeNode.isOptimisticSerialization();
   }

   public void setOptimisticSerialization(boolean var1) {
      this.beanTreeNode.setOptimisticSerialization(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OptimisticSerialization", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isRetainOriginalURL() {
      return this.beanTreeNode.isRetainOriginalURL();
   }

   public void setRetainOriginalURL(boolean var1) {
      this.beanTreeNode.setRetainOriginalURL(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RetainOriginalURL", (Object)null, (Object)null));
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

   public boolean isShowArchivedRealPathEnabled() {
      return this.beanTreeNode.isShowArchivedRealPathEnabled();
   }

   public void setShowArchivedRealPathEnabled(boolean var1) {
      this.beanTreeNode.setShowArchivedRealPathEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ShowArchivedRealPathEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isShowArchivedRealPathEnabledSet() {
      return this.beanTreeNode.isShowArchivedRealPathEnabledSet();
   }

   public boolean isRequireAdminTraffic() {
      return this.beanTreeNode.isRequireAdminTraffic();
   }

   public void setRequireAdminTraffic(boolean var1) {
      this.beanTreeNode.setRequireAdminTraffic(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RequireAdminTraffic", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isAccessLoggingDisabled() {
      return this.beanTreeNode.isAccessLoggingDisabled();
   }

   public void setAccessLoggingDisabled(boolean var1) {
      this.beanTreeNode.setAccessLoggingDisabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AccessLoggingDisabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isAccessLoggingDisabledSet() {
      return this.beanTreeNode.isAccessLoggingDisabledSet();
   }

   public boolean isPreferForwardQueryString() {
      return this.beanTreeNode.isPreferForwardQueryString();
   }

   public void setPreferForwardQueryString(boolean var1) {
      this.beanTreeNode.setPreferForwardQueryString(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PreferForwardQueryString", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isPreferForwardQueryStringSet() {
      return this.beanTreeNode.isPreferForwardQueryStringSet();
   }
}
