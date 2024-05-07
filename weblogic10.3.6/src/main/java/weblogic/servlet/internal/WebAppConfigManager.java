package weblogic.servlet.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import weblogic.application.ApplicationContextInternal;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.MimeMappingBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.CharsetMappingBean;
import weblogic.j2ee.descriptor.wl.CharsetParamsBean;
import weblogic.j2ee.descriptor.wl.ContainerDescriptorBean;
import weblogic.j2ee.descriptor.wl.InputCharsetBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.management.DomainDir;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.WebAppComponentMBean;
import weblogic.management.configuration.WebAppContainerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.security.RealmMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.HTTPLogger;
import weblogic.utils.StringUtils;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.http.HttpConstants;
import weblogic.utils.http.HttpParsing;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class WebAppConfigManager {
   private static final String WL_USE_VM_ENCODING = "webapp.encoding.usevmdefault";
   private static final String WL_DEFAULT_ENCODING = "webapp.encoding.default";
   private static final boolean WIN_32;
   static final AuthenticatedSubject KERNEL_ID;
   private static final ServerMBean servermbean;
   private static final ClusterMBean clustermbean;
   private static final WebAppContainerMBean webAppContainer;
   private static final int DEFAULT_RESOURCE_RELOAD_CHECK_SECONDS;
   private static final int DEFAULT_SERVLET_RELOAD_CHECK_SECONDS;
   private final WebAppServletContext context;
   private final ConcurrentHashMap mimeMapping = new ConcurrentHashMap();
   private WebAppComponentMBean compMBean = null;
   private boolean checkAuthOnForwardEnabled;
   private boolean filterDispatchedRequestsEnabled = true;
   private boolean preferWebInfClasses;
   private boolean clientCertProxyEnabled;
   private boolean redirectWithAbsoluteURLEnabled = true;
   private boolean encodingEnabled;
   private int singleThreadedServletPoolSize = 5;
   private String authRealmName = "WebLogic Server";
   private String defaultMimeType;
   private String defaultEncoding = "ISO-8859-1";
   private String dispatchPolicy = "weblogic.kernel.Default";
   private WorkManager workManager;
   private boolean reloginEnabled;
   private boolean allowAllRoles;
   private boolean nativeIOEnabled;
   private boolean rtexprvalueJspParamName;
   private boolean isJSPCompilerBackwardsCompatible;
   private static final boolean caseInsensitive;
   private final HashMap inputEncodingMap = new HashMap();
   private CharsetMap charsetMap;
   private LocaleEncodingMap localeEncodingMap = new LocaleEncodingMap();
   private int defaultServletReloadCheckSecs;
   private int servletReloadCheckSecs;
   private int resourceReloadCheckSecs;
   private boolean servletReloadCheckSecsSet = false;
   private boolean optimisticSerialization;
   private boolean retainOriginalURL;
   private boolean servletAuthFromURL;
   private boolean indexDirectoryEnabled = false;
   private boolean indexDirectoryEnabledSet = false;
   private String indexDirectorySortBy = "NAME";
   private boolean showArchivedRealPathEnabledSet = false;
   private boolean showArchivedRealPathEnabled = false;
   private boolean accessLoggingDisabled = false;
   private boolean accessLoggingDisabledSet = false;
   private boolean preferForwardQueryStringSet = false;
   private boolean preferForwardQueryString = false;
   private long minimumNativeFileSize = 4096L;
   private String tempDir = null;
   private boolean disableImplicitServletMappings = false;
   private boolean requireAdminTraffic = false;
   private final BeanUpdateListener containerBeanListener;

   WebAppConfigManager(WebAppServletContext var1) {
      this.context = var1;
      this.defaultServletReloadCheckSecs = DEFAULT_SERVLET_RELOAD_CHECK_SECONDS;
      this.servletReloadCheckSecs = DEFAULT_SERVLET_RELOAD_CHECK_SECONDS;
      this.resourceReloadCheckSecs = DEFAULT_RESOURCE_RELOAD_CHECK_SECONDS;
      this.containerBeanListener = this.createBeanUpdateListener();
   }

   BeanUpdateListener getBeanUpdateListener() {
      return this.containerBeanListener;
   }

   void init() {
      HttpServer var1 = this.context.getServer();
      this.initMimeMapping();
      this.initEncoding(this.context.getApplicationContext());
      this.initCharsetMap(var1.getMBean().getCharsets());
      this.initPluginSecurity();
      this.initSwitches();
      this.initWorkManager();
      this.initAccessLoggingDiasbled();
   }

   private void initSwitches() {
      this.reloginEnabled = webAppContainer.isReloginEnabled();
      this.allowAllRoles = webAppContainer.isAllowAllRoles();
      if (webAppContainer.isSet("FilterDispatchedRequestsEnabled")) {
         this.filterDispatchedRequestsEnabled = webAppContainer.isFilterDispatchedRequestsEnabled();
      } else {
         this.filterDispatchedRequestsEnabled = this.isOldDescriptor();
      }

      this.optimisticSerialization = Boolean.getBoolean("weblogic.servlet.optimisticSerialization") || webAppContainer.isOptimisticSerialization();
      this.retainOriginalURL = webAppContainer.isRetainOriginalURL();
      this.servletAuthFromURL = webAppContainer.isServletAuthenticationFormURL();
      this.rtexprvalueJspParamName = webAppContainer.isRtexprvalueJspParamName();
      this.isJSPCompilerBackwardsCompatible = webAppContainer.isJSPCompilerBackwardsCompatible();
   }

   boolean isOldDescriptor() {
      WebAppModule var1 = this.context.getWebAppModule();
      if (var1 == null) {
         return false;
      } else {
         WebAppBean var2 = var1.getWebAppBean();
         if (var2 == null) {
            return false;
         } else {
            String var3 = var2.getVersion();
            if (var3 == null) {
               return true;
            } else {
               try {
                  float var4 = Float.parseFloat(var3);
                  return (double)var4 < 2.4;
               } catch (NumberFormatException var5) {
                  return true;
               }
            }
         }
      }
   }

   private void initPluginSecurity() {
      this.clientCertProxyEnabled = webAppContainer.isClientCertProxyEnabled();
      if (clustermbean != null && clustermbean.isSet("ClientCertProxyEnabled")) {
         this.clientCertProxyEnabled = clustermbean.isClientCertProxyEnabled();
      }

      if (servermbean.isSet("ClientCertProxyEnabled")) {
         this.clientCertProxyEnabled = servermbean.isClientCertProxyEnabled();
      }

   }

   private void initEncoding(ApplicationContextInternal var1) {
      String var2 = null;
      if ("true".equalsIgnoreCase(var1.getApplicationParameter("webapp.encoding.usevmdefault"))) {
         var2 = System.getProperty("file.encoding");
      } else {
         var2 = var1.getApplicationParameter("webapp.encoding.default");
      }

      if (var2 != null) {
         if (Charset.isSupported(var2)) {
            this.defaultEncoding = var2;
            this.encodingEnabled = true;
         } else {
            HTTPLogger.logUnsupportedEncoding(this.context.getLogContext(), var2, new UnsupportedEncodingException("Unsupported encoding " + var2));
         }
      }
   }

   private void initCharsetMap(Map var1) {
      this.charsetMap = new CharsetMap(var1);
   }

   private void initAccessLoggingDiasbled() {
      this.accessLoggingDisabled = this.context.isInternalApp();
   }

   void addInputEncoding(String var1, String var2) {
      var1 = HttpParsing.ensureStartingSlash(var1);
      var1 = StringUtils.endsWith(var1, '*') ? var1.substring(0, var1.length() - 1) : var1;
      var1 = StringUtils.endsWith(var1, '/') ? var1.substring(0, var1.length() - 1) : var1;
      this.inputEncodingMap.put(var1, var2);
   }

   void registerMBean(WebAppComponentMBean var1) {
      this.compMBean = var1;
      this.singleThreadedServletPoolSize = var1.getSingleThreadedServletPoolSize();
      this.preferWebInfClasses = var1.isPreferWebInfClasses();
      if (var1.isSessionURLRewritingEnabledSet()) {
         this.context.getSessionContext().getConfigMgr().setUrlRewritingEnabled(var1.isSessionURLRewritingEnabled());
      }

      this.authRealmName = var1.getAuthRealmName();
   }

   void registerContainerDescriptors(WeblogicWebAppBean var1) {
      if (var1 != null) {
         ContainerDescriptorBean var2 = (ContainerDescriptorBean)DescriptorUtils.getFirstChildOrDefaultBean(var1, var1.getContainerDescriptors(), "ContainerDescriptor");
         this.checkAuthOnForwardEnabled = var2.getCheckAuthOnForward() != null;
         this.redirectWithAbsoluteURLEnabled = var2.isRedirectWithAbsoluteUrl();
         this.nativeIOEnabled = var2.isNativeIOEnabled();
         DescriptorBean var3 = (DescriptorBean)var2;
         if (var3.isSet("FilterDispatchedRequestsEnabled")) {
            this.filterDispatchedRequestsEnabled = var2.isFilterDispatchedRequestsEnabled();
         }

         if (var3.isSet("PreferWebInfClasses")) {
            this.preferWebInfClasses = var2.isPreferWebInfClasses();
         }

         if (var3.isSet("SingleThreadedServletPoolSize")) {
            this.singleThreadedServletPoolSize = var2.getSingleThreadedServletPoolSize();
         }

         if (var3.isSet("ClientCertProxyEnabled")) {
            this.clientCertProxyEnabled = var2.isClientCertProxyEnabled();
         }

         if (var3.isSet("ReloginEnabled")) {
            this.reloginEnabled = var2.isReloginEnabled();
         }

         if (var3.isSet("AllowAllRoles")) {
            this.allowAllRoles = var2.isAllowAllRoles();
         }

         if (var3.isSet("OptimisticSerialization")) {
            this.optimisticSerialization = var2.isOptimisticSerialization();
         }

         if (var3.isSet("RetainOriginalURL")) {
            this.retainOriginalURL = var2.isRetainOriginalURL();
         }

         if (var2.getDefaultMimeType() != null && !var2.getDefaultMimeType().equals("")) {
            this.defaultMimeType = var2.getDefaultMimeType();
         }

         if (var2.isServletReloadCheckSecsSet()) {
            this.servletReloadCheckSecsSet = true;
            this.servletReloadCheckSecs = var2.getServletReloadCheckSecs();
         }

         this.resourceReloadCheckSecs = var2.getResourceReloadCheckSecs();
         if (var2.isIndexDirectoryEnabledSet()) {
            this.indexDirectoryEnabledSet = true;
            this.indexDirectoryEnabled = var2.isIndexDirectoryEnabled();
         }

         this.indexDirectorySortBy = var2.getIndexDirectorySortBy();
         if (var2.isShowArchivedRealPathEnabledSet()) {
            this.showArchivedRealPathEnabledSet = true;
            this.showArchivedRealPathEnabled = var2.isShowArchivedRealPathEnabled();
         }

         if (var2.isAccessLoggingDisabledSet()) {
            this.accessLoggingDisabledSet = true;
            this.accessLoggingDisabled = var2.isAccessLoggingDisabled();
         }

         if (var2.isPreferForwardQueryStringSet()) {
            this.preferForwardQueryStringSet = true;
            this.preferForwardQueryString = var2.isPreferForwardQueryString();
         }

         this.minimumNativeFileSize = var2.getMinimumNativeFileSize();
         this.tempDir = var2.getTempDir();
         this.disableImplicitServletMappings = var2.isDisableImplicitServletMappings();
         this.requireAdminTraffic = var2.isRequireAdminTraffic();
      }
   }

   void registerCharsetParams(WeblogicWebAppBean var1) {
      CharsetParamsBean var2 = (CharsetParamsBean)DescriptorUtils.getFirstChildOrDefaultBean(var1, var1.getCharsetParams(), "CharsetParams");
      if (var2 != null) {
         InputCharsetBean[] var3 = var2.getInputCharsets();
         if (var3 != null) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               String var5 = var3[var4].getResourcePath();
               String var6 = var3[var4].getJavaCharsetName();
               if (var5 != null && var6 != null) {
                  this.addInputEncoding(var5, var6);
               }
            }
         }

         CharsetMappingBean[] var7 = var2.getCharsetMappings();
         if (var7 != null) {
            CharsetMap var8 = this.getCharsetMap();

            for(int var9 = 0; var9 < var7.length; ++var9) {
               var8.addMapping(var7[var9]);
            }
         }
      }

   }

   public CharsetMap getCharsetMap() {
      return this.charsetMap;
   }

   public LocaleEncodingMap getLocaleEncodingMap() {
      return this.localeEncodingMap;
   }

   HashMap getInputEncodings() {
      return this.inputEncodingMap;
   }

   public String getDefaultEncoding() {
      return this.defaultEncoding;
   }

   public boolean useDefaultEncoding() {
      return this.encodingEnabled;
   }

   public boolean isRedirectWithAbsoluteURLEnabled() {
      return this.redirectWithAbsoluteURLEnabled;
   }

   public boolean isShowArchivedRealPathEnabled() {
      if (this.showArchivedRealPathEnabledSet) {
         return this.showArchivedRealPathEnabled;
      } else {
         return webAppContainer != null ? webAppContainer.isShowArchivedRealPathEnabled() : false;
      }
   }

   public boolean isCheckAuthOnForwardEnabled() {
      return this.checkAuthOnForwardEnabled;
   }

   public boolean isIndexDirectoryEnabled() {
      if (this.indexDirectoryEnabledSet) {
         return this.indexDirectoryEnabled;
      } else {
         return this.compMBean != null ? this.compMBean.isIndexDirectoryEnabled() : false;
      }
   }

   public String getIndexDirectorySortBy() {
      return this.indexDirectorySortBy;
   }

   public static boolean isCaseInsensitive() {
      return caseInsensitive;
   }

   public boolean isReloginEnabled() {
      return this.reloginEnabled;
   }

   public boolean isAllowAllRoles() {
      return this.allowAllRoles;
   }

   public boolean isNativeIOEnabled() {
      return this.nativeIOEnabled;
   }

   boolean isOptimisticSerialization() {
      return this.optimisticSerialization;
   }

   public boolean isRetainOriginalURL() {
      return this.retainOriginalURL;
   }

   public boolean isServletAuthFromURL() {
      return this.servletAuthFromURL;
   }

   boolean isRtexprvalueJspParamName() {
      return this.rtexprvalueJspParamName;
   }

   boolean isJSPCompilerBackwardsCompatible() {
      return this.isJSPCompilerBackwardsCompatible;
   }

   public boolean isAccessLoggingDisabled() {
      return this.accessLoggingDisabled;
   }

   public long getMinimumNativeFileSize() {
      return this.minimumNativeFileSize;
   }

   public boolean isPreferForwardQueryString() {
      return this.preferForwardQueryString;
   }

   boolean isRequireAdminTraffic() {
      return this.requireAdminTraffic;
   }

   public boolean isFilterDispatchedRequestsEnabled() {
      return this.filterDispatchedRequestsEnabled;
   }

   public int getServletReloadCheckSecs() {
      WebAppModule var1 = this.context.getWebAppModule();
      if (var1 != null && !var1.isServletReloadAllowed()) {
         return -1;
      } else if (this.servletReloadCheckSecsSet) {
         return this.servletReloadCheckSecs;
      } else {
         return webAppContainer.isServletReloadCheckSecsSet() ? webAppContainer.getServletReloadCheckSecs() : this.defaultServletReloadCheckSecs;
      }
   }

   public int getResourceReloadCheckSecs() {
      return this.context.getJSPManager().isPageCheckSecondsSet() ? this.context.getJSPManager().getPageCheckSeconds() : this.resourceReloadCheckSecs;
   }

   void setServletReloadCheckSecs(int var1) {
      this.defaultServletReloadCheckSecs = var1;
   }

   public int getSingleThreadedServletPoolSize() {
      return this.singleThreadedServletPoolSize;
   }

   void setSingleThreadedServletPoolSize(int var1) {
      this.singleThreadedServletPoolSize = var1;
   }

   String getTempDir() {
      return this.tempDir;
   }

   boolean getPreferWebInfClasses() {
      return this.preferWebInfClasses;
   }

   void setPreferWebInfClasses(boolean var1) {
      this.preferWebInfClasses = var1;
   }

   String getDefaultMimeType() {
      return this.defaultMimeType;
   }

   public String getAuthRealmName() {
      return this.authRealmName;
   }

   void setAuthRealmName(String var1) {
      this.authRealmName = var1;
   }

   public boolean isClientCertProxyEnabled() {
      return this.clientCertProxyEnabled;
   }

   void setClientCertProxyEnabled(boolean var1) {
      this.clientCertProxyEnabled = var1;
   }

   public String getDispatchPolicy() {
      return this.dispatchPolicy;
   }

   private void initWorkManager() {
      this.workManager = WorkManagerFactory.getInstance().find("weblogic.kernel.Default", this.context.getApplicationId(), (String)null);
   }

   WorkManager getWorkManager() {
      return this.workManager;
   }

   void setDispatchPolicy(String var1) {
      if (var1 == null) {
         this.dispatchPolicy = "weblogic.kernel.Default";
      } else {
         this.dispatchPolicy = var1;
      }

      this.workManager = WorkManagerFactory.getInstance().find(this.dispatchPolicy, this.context.getApplicationId(), this.context.getId());
   }

   boolean isImplicitServletMappingDisabled() {
      return this.disableImplicitServletMappings;
   }

   private void initMimeMapping() {
      for(int var1 = 0; var1 < HttpConstants.DEFAULT_MIME_MAPPINGS.length; ++var1) {
         this.addMimeMapping(HttpConstants.DEFAULT_MIME_MAPPINGS[var1][0], HttpConstants.DEFAULT_MIME_MAPPINGS[var1][1]);
      }

      if (webAppContainer.getMimeMappingFile() != null) {
         String var5 = webAppContainer.getMimeMappingFile();
         var5 = var5.replace('\\', File.separatorChar);
         var5 = var5.replace('/', File.separatorChar);
         if (!WebAppServletContext.isAbsoluteFilePath(var5)) {
            var5 = DomainDir.getRootDir() + File.separatorChar + var5;
         }

         this.addMimeMappings(new File(var5));
      }

      if (this.compMBean != null) {
         Map var7 = this.compMBean.getMimeTypes();
         if (var7 != null) {
            Iterator var2 = var7.keySet().iterator();

            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               String var4 = (String)var7.get(var3);
               this.addMimeMapping(var3, var4);
            }
         }
      }

      WebAppModule var9 = this.context.getWebAppModule();
      if (var9 != null && var9.getWebAppBean() != null) {
         MimeMappingBean[] var6 = var9.getWebAppBean().getMimeMappings();
         if (var6 != null) {
            for(int var8 = 0; var8 < var6.length; ++var8) {
               MimeMappingBean var10 = var6[var8];
               this.addMimeMapping(var10.getExtension(), var10.getMimeType());
            }
         }
      }

   }

   private void addMimeMappings(File var1) {
      if (var1.exists()) {
         FileInputStream var2 = null;

         try {
            var2 = new FileInputStream(var1);
            Properties var3 = new Properties();
            var3.load(var2);
            Enumeration var4 = var3.propertyNames();

            while(var4.hasMoreElements()) {
               String var5 = (String)var4.nextElement();
               String var6 = var3.getProperty(var5);
               if (var5 != null && var5.length() > 0) {
                  this.addMimeMapping(var5, var6);
               }
            }
         } catch (IOException var16) {
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("Failed to load mime-mappings from domain", var16);
            }
         } finally {
            try {
               if (var2 != null) {
                  var2.close();
               }
            } catch (IOException var15) {
            }

         }

      }
   }

   void addMimeMapping(String var1, String var2) {
      var1 = var1.trim().toLowerCase();
      if (var2 == null) {
         this.mimeMapping.remove(var1);
      } else {
         var2 = var2.trim();
         if (var2.length() == 0) {
            this.mimeMapping.remove(var1);
         } else {
            this.mimeMapping.put(var1, var2);
         }
      }
   }

   String getMimeType(String var1) {
      int var2 = var1.lastIndexOf(46);
      String var3;
      if (var2 != -1) {
         var3 = var1.substring(var2 + 1).toLowerCase();
      } else {
         var3 = var1;
      }

      String var4 = (String)this.mimeMapping.get(var3);
      return var4 == null ? this.getDefaultMimeType() : var4;
   }

   static File getConsoleExtensionDirectory() {
      DomainMBean var0 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain();
      String var1 = var0.getConsoleExtensionDirectory();
      if (var1 != null) {
         File var2 = new File(var0.getRootDirectory(), var1);
         if (var2.isDirectory()) {
            return var2;
         }
      }

      return null;
   }

   public static String getRealmAuthMethods() {
      DomainMBean var0 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain();
      RealmMBean var1 = var0.getSecurityConfiguration().getDefaultRealm();
      return var1 != null ? var1.getAuthMethods() : null;
   }

   static File getDocrootFile(String var0, WebAppServletContext var1) throws IOException {
      if (var0 == null) {
         if (var1.getLogContext() != null) {
            HTTPLogger.logNullDocumentRoot(var1.getLogContext());
         }

         throw new IOException("Document root is null.");
      } else {
         var0 = var0.replace('/', File.separatorChar);
         File var2 = new File(var0);
         String var3 = var0;
         if (!var2.isAbsolute() && !var2.exists()) {
            ClusterMBean var4 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getCluster();
            if (var4 != null) {
               var3 = var0 + var4.getName();
            }

            if (var3 == null) {
               var3 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getName();
            }

            var3 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getRootDirectory() + File.separator + var3 + File.separator + var0;
            var2 = new File(var3);
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug(var1.getLogContext() + ": document root doesn't exist for " + var1.getLogContext() + " looking under relative path: " + var2.getAbsolutePath());
            }
         }

         if (!var2.exists()) {
            if (var1.getLogContext() != null) {
               HTTPLogger.logNoDocRoot(var1.getLogContext(), var3);
            }

            throw new IOException("document root (" + var3 + ") does not exist.");
         } else {
            return var2;
         }
      }
   }

   private static int getDefaultReloadSecs() {
      return HttpServer.isProductionModeEnabled() ? -1 : 1;
   }

   private BeanUpdateListener createBeanUpdateListener() {
      return new WebComponentBeanUpdateListener() {
         protected void handlePropertyRemove(BeanUpdateEvent.PropertyUpdate var1) {
            String var2 = var1.getPropertyName();
            if ("ServletReloadCheckSecs".equals(var2)) {
               WebAppConfigManager.this.servletReloadCheckSecs = WebAppConfigManager.DEFAULT_SERVLET_RELOAD_CHECK_SECONDS;
               WebAppConfigManager.this.servletReloadCheckSecsSet = false;
            } else if ("ResourceReloadCheckSecs".equals(var2)) {
               WebAppConfigManager.this.resourceReloadCheckSecs = WebAppConfigManager.DEFAULT_RESOURCE_RELOAD_CHECK_SECONDS;
            } else if ("IndexDirectoryEnabled".equals(var2)) {
               WebAppConfigManager.this.indexDirectoryEnabledSet = false;
               WebAppConfigManager.this.indexDirectoryEnabled = false;
            } else if ("IndexDirectorySortBy".equals(var2)) {
               WebAppConfigManager.this.indexDirectorySortBy = "NAME";
            } else if ("ShowArchivedRealPathEnabled".equals(var2)) {
               WebAppConfigManager.this.showArchivedRealPathEnabledSet = false;
               WebAppConfigManager.this.showArchivedRealPathEnabled = false;
            } else if ("MinimumNativeFileSize".equals(var2)) {
               WebAppConfigManager.this.minimumNativeFileSize = 4096L;
            } else if ("TempDir".equals(var2)) {
               WebAppConfigManager.this.tempDir = null;
            } else if ("DisableImplicitServletMappings".equals(var2)) {
               WebAppConfigManager.this.disableImplicitServletMappings = false;
            } else if ("RequireAdminTraffic".equals(var2)) {
               WebAppConfigManager.this.requireAdminTraffic = false;
            } else if ("AccessLoggingDisabled".equals(var2)) {
               WebAppConfigManager.this.accessLoggingDisabledSet = false;
               WebAppConfigManager.this.accessLoggingDisabled = WebAppConfigManager.this.context.isInternalApp();
            }

         }

         protected void handlePropertyChange(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) {
            ContainerDescriptorBean var3 = (ContainerDescriptorBean)var2;
            String var4 = var1.getPropertyName();
            if ("ServletReloadCheckSecs".equals(var4)) {
               WebAppConfigManager.this.servletReloadCheckSecs = var3.getServletReloadCheckSecs();
               WebAppConfigManager.this.servletReloadCheckSecsSet = true;
            } else if ("ResourceReloadCheckSecs".equals(var4)) {
               WebAppConfigManager.this.resourceReloadCheckSecs = var3.getResourceReloadCheckSecs();
            } else if ("IndexDirectoryEnabled".equals(var4)) {
               WebAppConfigManager.this.indexDirectoryEnabledSet = true;
               WebAppConfigManager.this.indexDirectoryEnabled = var3.isIndexDirectoryEnabled();
            } else if ("IndexDirectorySortBy".equals(var4)) {
               WebAppConfigManager.this.indexDirectorySortBy = var3.getIndexDirectorySortBy();
            } else if ("ShowArchivedRealPathEnabled".equals(var4)) {
               WebAppConfigManager.this.showArchivedRealPathEnabledSet = true;
               WebAppConfigManager.this.showArchivedRealPathEnabled = var3.isShowArchivedRealPathEnabled();
            } else if ("MinimumNativeFileSize".equals(var4)) {
               WebAppConfigManager.this.minimumNativeFileSize = var3.getMinimumNativeFileSize();
            } else if ("TempDir".equals(var4)) {
               WebAppConfigManager.this.tempDir = var3.getTempDir();
            } else if ("DisableImplicitServletMappings".equals(var4)) {
               WebAppConfigManager.this.disableImplicitServletMappings = var3.isDisableImplicitServletMappings();
            } else if ("RequireAdminTraffic".equals(var4)) {
               WebAppConfigManager.this.requireAdminTraffic = var3.isRequireAdminTraffic();
            } else if ("AccessLoggingDisabled".equals(var4)) {
               WebAppConfigManager.this.accessLoggingDisabledSet = true;
               WebAppConfigManager.this.accessLoggingDisabled = var3.isAccessLoggingDisabled();
            }

         }

         protected void prepareBeanAdd(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) throws BeanUpdateRejectedException {
            if (var2 instanceof ContainerDescriptorBean) {
               WeblogicWebAppBean var3 = WebAppConfigManager.this.context.getWebAppModule().getWlWebAppBean();
               ContainerDescriptorBean var4 = (ContainerDescriptorBean)var2;
               ContainerDescriptorBean var5 = (ContainerDescriptorBean)DescriptorUtils.getFirstChildOrDefaultBean(var3, var3.getContainerDescriptors(), "ContainerDescriptor");
               ArrayList var6 = new ArrayList();
               computeChange("check-auth-on-forward", var4.getCheckAuthOnForward(), var5.getCheckAuthOnForward(), var6);
               computeChange("filter-dispatched-requests-enabled", var4.isFilterDispatchedRequestsEnabled(), var5.isFilterDispatchedRequestsEnabled(), var6);
               computeChange("redirect-content-type", var4.getRedirectContentType(), var5.getRedirectContentType(), var6);
               computeChange("redirect-content", var4.getRedirectContent(), var5.getRedirectContent(), var6);
               computeChange("redirect-with-absolute-url", var4.isRedirectWithAbsoluteUrl(), var5.isRedirectWithAbsoluteUrl(), var6);
               computeChange("single-threaded-servlet-pool-size", var4.getSingleThreadedServletPoolSize(), var5.getSingleThreadedServletPoolSize(), var6);
               computeChange("save-sessions-enabled", var4.isSaveSessionsEnabled(), var5.isSaveSessionsEnabled(), var6);
               computeChange("prefer-web-inf-classes", var4.isPreferWebInfClasses(), var5.isPreferWebInfClasses(), var6);
               computeChange("default-mime-type", var4.getDefaultMimeType(), var5.getDefaultMimeType(), var6);
               computeChange("relogin-enabled", var4.isReloginEnabled(), var5.isReloginEnabled(), var6);
               computeChange("allow-all-roles", var4.isAllowAllRoles(), var5.isAllowAllRoles(), var6);
               computeChange("client-cert-proxy-enabled", var4.isClientCertProxyEnabled(), var5.isClientCertProxyEnabled(), var6);
               computeChange("native-io-enabled", var4.isNativeIOEnabled(), var5.isNativeIOEnabled(), var6);
               computeChange("disable-implicit-servlet-mappings", var4.isDisableImplicitServletMappings(), var5.isDisableImplicitServletMappings(), var6);
               computeChange("temp-dir", var4.getTempDir(), var5.getTempDir(), var6);
               computeChange("optimistic-serialization", var4.isOptimisticSerialization(), var5.isOptimisticSerialization(), var6);
               computeChange("retain-original-url", var4.isRetainOriginalURL(), var5.isRetainOriginalURL(), var6);
               if (!var6.isEmpty()) {
                  throw new BeanUpdateRejectedException("Non-Dynamic property in \"container-descriptor\" is/are specified in deployment plan: '" + getChangedPropertyNames(var6) + "'");
               }
            }
         }

         protected void handleBeanAdd(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) {
            if (var2 instanceof ContainerDescriptorBean && var1 != null && "ContainerDescriptors".equals(var1.getPropertyName())) {
               ContainerDescriptorBean var3 = (ContainerDescriptorBean)var2;
               ((DescriptorBean)var3).addBeanUpdateListener(this);
               if (var3.isServletReloadCheckSecsSet()) {
                  WebAppConfigManager.this.servletReloadCheckSecs = var3.getServletReloadCheckSecs();
                  WebAppConfigManager.this.servletReloadCheckSecsSet = true;
               }

               WebAppConfigManager.this.resourceReloadCheckSecs = var3.getResourceReloadCheckSecs();
               if (var3.isIndexDirectoryEnabledSet()) {
                  WebAppConfigManager.this.indexDirectoryEnabledSet = true;
                  WebAppConfigManager.this.indexDirectoryEnabled = var3.isIndexDirectoryEnabled();
               }

               WebAppConfigManager.this.indexDirectorySortBy = var3.getIndexDirectorySortBy();
               if (var3.isShowArchivedRealPathEnabledSet()) {
                  WebAppConfigManager.this.showArchivedRealPathEnabledSet = true;
                  WebAppConfigManager.this.showArchivedRealPathEnabled = var3.isShowArchivedRealPathEnabled();
               }

               WebAppConfigManager.this.minimumNativeFileSize = var3.getMinimumNativeFileSize();
               WebAppConfigManager.this.tempDir = var3.getTempDir();
               WebAppConfigManager.this.disableImplicitServletMappings = var3.isDisableImplicitServletMappings();
               WebAppConfigManager.this.requireAdminTraffic = var3.isRequireAdminTraffic();
               if (var3.isAccessLoggingDisabledSet()) {
                  WebAppConfigManager.this.accessLoggingDisabledSet = true;
                  WebAppConfigManager.this.accessLoggingDisabled = var3.isAccessLoggingDisabled();
               }

            }
         }

         protected void handleBeanRemove(BeanUpdateEvent.PropertyUpdate var1) {
            if (var1 != null && "ContainerDescriptors".equals(var1.getPropertyName()) && var1.getRemovedObject() instanceof ContainerDescriptorBean) {
               ContainerDescriptorBean var2 = (ContainerDescriptorBean)var1.getRemovedObject();
               ((DescriptorBean)var2).removeBeanUpdateListener(this);
               WebAppConfigManager.this.servletReloadCheckSecs = WebAppConfigManager.DEFAULT_SERVLET_RELOAD_CHECK_SECONDS;
               WebAppConfigManager.this.servletReloadCheckSecsSet = false;
               WebAppConfigManager.this.resourceReloadCheckSecs = WebAppConfigManager.DEFAULT_RESOURCE_RELOAD_CHECK_SECONDS;
               WebAppConfigManager.this.indexDirectoryEnabledSet = false;
               WebAppConfigManager.this.indexDirectoryEnabled = false;
               WebAppConfigManager.this.indexDirectorySortBy = "NAME";
               WebAppConfigManager.this.showArchivedRealPathEnabledSet = false;
               WebAppConfigManager.this.showArchivedRealPathEnabled = false;
               WebAppConfigManager.this.minimumNativeFileSize = 4096L;
               WebAppConfigManager.this.tempDir = null;
               WebAppConfigManager.this.disableImplicitServletMappings = false;
               WebAppConfigManager.this.requireAdminTraffic = false;
               WebAppConfigManager.this.accessLoggingDisabledSet = false;
               WebAppConfigManager.this.accessLoggingDisabled = WebAppConfigManager.this.context.isInternalApp();
            }
         }
      };
   }

   static {
      WIN_32 = System.getProperty("os.name", "unknown").toLowerCase(Locale.ENGLISH).indexOf("windows") >= 0;
      KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      servermbean = ManagementService.getRuntimeAccess(KERNEL_ID).getServer();
      clustermbean = servermbean.getCluster();
      webAppContainer = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().getWebAppContainer();
      DEFAULT_RESOURCE_RELOAD_CHECK_SECONDS = getDefaultReloadSecs();
      DEFAULT_SERVLET_RELOAD_CHECK_SECONDS = getDefaultReloadSecs();
      caseInsensitive = SecurityServiceManager.areWebAppFilesCaseInsensitive() && WIN_32;
   }
}
