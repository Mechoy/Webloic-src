package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.WebServer;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WebServerMBeanImpl extends DeploymentMBeanImpl implements WebServerMBean, Serializable {
   private boolean _AcceptContextPathInGetRealPath;
   private boolean _AuthCookieEnabled;
   private Map _Charsets;
   private boolean _ChunkedTransferDisabled;
   private String _ClientIpHeader;
   private boolean _DebugEnabled;
   private WebAppComponentMBean _DefaultWebApp;
   private String _DefaultWebAppContextRoot;
   private int _FrontendHTTPPort;
   private int _FrontendHTTPSPort;
   private String _FrontendHost;
   private int _HttpsKeepAliveSecs;
   private boolean _KeepAliveEnabled;
   private int _KeepAliveSecs;
   private int _LogFileBufferKBytes;
   private int _LogFileCount;
   private int _LogFileFlushSecs;
   private String _LogFileFormat;
   private boolean _LogFileLimitEnabled;
   private String _LogFileName;
   private int _LogRotationPeriodMins;
   private String _LogRotationTimeBegin;
   private String _LogRotationType;
   private boolean _LogTimeInGMT;
   private boolean _LoggingEnabled;
   private int _MaxLogFileSizeKBytes;
   private int _MaxPostSize;
   private int _MaxPostTimeSecs;
   private String _Name;
   private int _OverloadResponseCode;
   private int _PostTimeoutSecs;
   private boolean _SendServerHeaderEnabled;
   private boolean _SingleSignonDisabled;
   private Map _URLResource;
   private boolean _UseHeaderEncoding;
   private boolean _UseHighestCompatibleHTTPVersion;
   private boolean _WAPEnabled;
   private WebDeploymentMBean[] _WebDeployments;
   private WebServerLogMBean _WebServerLog;
   private String _WorkManagerForRemoteSessionFetching;
   private int _WriteChunkBytes;
   private WebServer _customizer;
   private static SchemaHelper2 _schemaHelper;

   public WebServerMBeanImpl() {
      try {
         this._customizer = new WebServer(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public WebServerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new WebServer(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public WebServerLogMBean getWebServerLog() {
      return this._WebServerLog;
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isWebServerLogSet() {
      return this._isSet(9) || this._isAnythingSet((AbstractDescriptorBean)this.getWebServerLog());
   }

   public void setWebServerLog(WebServerLogMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 9)) {
         this._postCreate(var2);
      }

      WebServerLogMBean var3 = this._WebServerLog;
      this._WebServerLog = var1;
      this._postSet(9, var3, var1);
   }

   public void setLoggingEnabled(boolean var1) {
      boolean var2 = this.isLoggingEnabled();
      this._customizer.setLoggingEnabled(var1);
      this._postSet(10, var2, var1);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public boolean isLoggingEnabled() {
      return this._customizer.isLoggingEnabled();
   }

   public boolean isLoggingEnabledSet() {
      return this._isSet(10);
   }

   public String getLogFileFormat() {
      return this._customizer.getLogFileFormat();
   }

   public boolean isLogFileFormatSet() {
      return this._isSet(11);
   }

   public void setLogFileFormat(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"common", "extended"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("LogFileFormat", var1, var2);
      String var3 = this.getLogFileFormat();
      this._customizer.setLogFileFormat(var1);
      this._postSet(11, var3, var1);
   }

   public boolean getLogTimeInGMT() {
      return this._customizer.getLogTimeInGMT();
   }

   public boolean isLogTimeInGMTSet() {
      return this._isSet(12);
   }

   public void setLogTimeInGMT(boolean var1) {
      boolean var2 = this.getLogTimeInGMT();
      this._customizer.setLogTimeInGMT(var1);
      this._postSet(12, var2, var1);
   }

   public String getLogFileName() {
      return this._customizer.getLogFileName();
   }

   public boolean isLogFileNameSet() {
      return this._isSet(13);
   }

   public void setLogFileName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getLogFileName();
      this._customizer.setLogFileName(var1);
      this._postSet(13, var2, var1);
   }

   public String getFrontendHost() {
      return this._FrontendHost;
   }

   public boolean isFrontendHostSet() {
      return this._isSet(14);
   }

   public void setFrontendHost(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._FrontendHost;
      this._FrontendHost = var1;
      this._postSet(14, var2, var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public int getFrontendHTTPPort() {
      return this._FrontendHTTPPort;
   }

   public boolean isFrontendHTTPPortSet() {
      return this._isSet(15);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setFrontendHTTPPort(int var1) throws InvalidAttributeValueException {
      int var2 = this._FrontendHTTPPort;
      this._FrontendHTTPPort = var1;
      this._postSet(15, var2, var1);
   }

   public int getFrontendHTTPSPort() {
      return this._FrontendHTTPSPort;
   }

   public boolean isFrontendHTTPSPortSet() {
      return this._isSet(16);
   }

   public void setFrontendHTTPSPort(int var1) throws InvalidAttributeValueException {
      int var2 = this._FrontendHTTPSPort;
      this._FrontendHTTPSPort = var1;
      this._postSet(16, var2, var1);
   }

   public void setLogFileBufferKBytes(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LogFileBufferKBytes", (long)var1, 0L, 1024L);
      int var2 = this._LogFileBufferKBytes;
      this._LogFileBufferKBytes = var1;
      this._postSet(17, var2, var1);
   }

   public int getLogFileBufferKBytes() {
      return this._LogFileBufferKBytes;
   }

   public boolean isLogFileBufferKBytesSet() {
      return this._isSet(17);
   }

   public int getMaxLogFileSizeKBytes() {
      return this._MaxLogFileSizeKBytes;
   }

   public boolean isMaxLogFileSizeKBytesSet() {
      return this._isSet(18);
   }

   public void setMaxLogFileSizeKBytes(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MaxLogFileSizeKBytes", var1, 0);
      int var2 = this._MaxLogFileSizeKBytes;
      this._MaxLogFileSizeKBytes = var1;
      this._postSet(18, var2, var1);
   }

   public String getLogRotationType() {
      return this._customizer.getLogRotationType();
   }

   public boolean isLogRotationTypeSet() {
      return this._isSet(19);
   }

   public void setLogRotationType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"size", "date"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("LogRotationType", var1, var2);
      String var3 = this.getLogRotationType();
      this._customizer.setLogRotationType(var1);
      this._postSet(19, var3, var1);
   }

   public int getLogRotationPeriodMins() {
      return this._customizer.getLogRotationPeriodMins();
   }

   public boolean isLogRotationPeriodMinsSet() {
      return this._isSet(20);
   }

   public void setLogRotationPeriodMins(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LogRotationPeriodMins", (long)var1, 1L, 2147483647L);
      int var2 = this.getLogRotationPeriodMins();

      try {
         this._customizer.setLogRotationPeriodMins(var1);
      } catch (DistributedManagementException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(20, var2, var1);
   }

   public int getOverloadResponseCode() {
      return this._OverloadResponseCode;
   }

   public boolean isOverloadResponseCodeSet() {
      return this._isSet(21);
   }

   public void setOverloadResponseCode(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("OverloadResponseCode", (long)var1, 100L, 599L);
      int var2 = this._OverloadResponseCode;
      this._OverloadResponseCode = var1;
      this._postSet(21, var2, var1);
   }

   public int getLogFileFlushSecs() {
      return this._LogFileFlushSecs;
   }

   public boolean isLogFileFlushSecsSet() {
      return this._isSet(22);
   }

   public void setLogFileFlushSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LogFileFlushSecs", (long)var1, 1L, 360L);
      int var2 = this._LogFileFlushSecs;
      this._LogFileFlushSecs = var1;
      this._postSet(22, var2, var1);
   }

   public String getLogRotationTimeBegin() {
      return this._customizer.getLogRotationTimeBegin();
   }

   public boolean isLogRotationTimeBeginSet() {
      return this._isSet(23);
   }

   public void setLogRotationTimeBegin(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LoggingLegalHelper.validateWebServerLogRotationTimeBegin(var1);
      String var2 = this.getLogRotationTimeBegin();
      this._customizer.setLogRotationTimeBegin(var1);
      this._postSet(23, var2, var1);
   }

   public void setKeepAliveEnabled(boolean var1) {
      boolean var2 = this._KeepAliveEnabled;
      this._KeepAliveEnabled = var1;
      this._postSet(24, var2, var1);
   }

   public boolean isKeepAliveEnabled() {
      return this._KeepAliveEnabled;
   }

   public boolean isKeepAliveEnabledSet() {
      return this._isSet(24);
   }

   public int getKeepAliveSecs() {
      return this._KeepAliveSecs;
   }

   public boolean isKeepAliveSecsSet() {
      return this._isSet(25);
   }

   public void setKeepAliveSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("KeepAliveSecs", (long)var1, 5L, 3600L);
      int var2 = this._KeepAliveSecs;
      this._KeepAliveSecs = var1;
      this._postSet(25, var2, var1);
   }

   public int getHttpsKeepAliveSecs() {
      return this._HttpsKeepAliveSecs;
   }

   public boolean isHttpsKeepAliveSecsSet() {
      return this._isSet(26);
   }

   public void setHttpsKeepAliveSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("HttpsKeepAliveSecs", (long)var1, 30L, 360L);
      int var2 = this._HttpsKeepAliveSecs;
      this._HttpsKeepAliveSecs = var1;
      this._postSet(26, var2, var1);
   }

   public void setPostTimeoutSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PostTimeoutSecs", (long)var1, 0L, 120L);
      int var2 = this._PostTimeoutSecs;
      this._PostTimeoutSecs = var1;
      this._postSet(27, var2, var1);
   }

   public int getPostTimeoutSecs() {
      return this._PostTimeoutSecs;
   }

   public boolean isPostTimeoutSecsSet() {
      return this._isSet(27);
   }

   public void setMaxPostTimeSecs(int var1) throws InvalidAttributeValueException {
      int var2 = this._MaxPostTimeSecs;
      this._MaxPostTimeSecs = var1;
      this._postSet(28, var2, var1);
   }

   public int getMaxPostTimeSecs() {
      return this._MaxPostTimeSecs;
   }

   public boolean isMaxPostTimeSecsSet() {
      return this._isSet(28);
   }

   public void setMaxPostSize(int var1) throws InvalidAttributeValueException {
      int var2 = this._MaxPostSize;
      this._MaxPostSize = var1;
      this._postSet(29, var2, var1);
   }

   public int getMaxPostSize() {
      return this._MaxPostSize;
   }

   public boolean isMaxPostSizeSet() {
      return this._isSet(29);
   }

   public void setSendServerHeaderEnabled(boolean var1) {
      boolean var2 = this._SendServerHeaderEnabled;
      this._SendServerHeaderEnabled = var1;
      this._postSet(30, var2, var1);
   }

   public boolean isSendServerHeaderEnabled() {
      return this._SendServerHeaderEnabled;
   }

   public boolean isSendServerHeaderEnabledSet() {
      return this._isSet(30);
   }

   public String getDefaultWebAppContextRoot() {
      return this._DefaultWebAppContextRoot;
   }

   public boolean isDefaultWebAppContextRootSet() {
      return this._isSet(31);
   }

   public void setDefaultWebAppContextRoot(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DefaultWebAppContextRoot;
      this._DefaultWebAppContextRoot = var1;
      this._postSet(31, var2, var1);
   }

   public WebAppComponentMBean getDefaultWebApp() {
      return this._DefaultWebApp;
   }

   public String getDefaultWebAppAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDefaultWebApp();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isDefaultWebAppSet() {
      return this._isSet(32);
   }

   public void setDefaultWebAppAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, WebAppComponentMBean.class, new ReferenceManager.Resolver(this, 32) {
            public void resolveReference(Object var1) {
               try {
                  WebServerMBeanImpl.this.setDefaultWebApp((WebAppComponentMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         WebAppComponentMBean var2 = this._DefaultWebApp;
         this._initializeProperty(32);
         this._postSet(32, var2, this._DefaultWebApp);
      }

   }

   public void setDefaultWebApp(WebAppComponentMBean var1) {
      WebAppComponentMBean var2 = this._DefaultWebApp;
      this._DefaultWebApp = var1;
      this._postSet(32, var2, var1);
   }

   public void setCharsets(Map var1) throws InvalidAttributeValueException {
      Map var2 = this._Charsets;
      this._Charsets = var1;
      this._postSet(33, var2, var1);
   }

   public Map getCharsets() {
      return this._Charsets;
   }

   public String getCharsetsAsString() {
      return StringHelper.objectToString(this.getCharsets());
   }

   public boolean isCharsetsSet() {
      return this._isSet(33);
   }

   public void setCharsetsAsString(String var1) {
      try {
         this.setCharsets(StringHelper.stringToMap(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setURLResource(Map var1) throws InvalidAttributeValueException {
      Map var2 = this._URLResource;
      this._URLResource = var1;
      this._postSet(34, var2, var1);
   }

   public Map getURLResource() {
      return this._URLResource;
   }

   public String getURLResourceAsString() {
      return StringHelper.objectToString(this.getURLResource());
   }

   public boolean isURLResourceSet() {
      return this._isSet(34);
   }

   public void setURLResourceAsString(String var1) {
      try {
         this.setURLResource(StringHelper.stringToMap(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setChunkedTransferDisabled(boolean var1) {
      boolean var2 = this._ChunkedTransferDisabled;
      this._ChunkedTransferDisabled = var1;
      this._postSet(35, var2, var1);
   }

   public boolean isChunkedTransferDisabled() {
      return this._ChunkedTransferDisabled;
   }

   public boolean isChunkedTransferDisabledSet() {
      return this._isSet(35);
   }

   public void setUseHighestCompatibleHTTPVersion(boolean var1) {
      boolean var2 = this._UseHighestCompatibleHTTPVersion;
      this._UseHighestCompatibleHTTPVersion = var1;
      this._postSet(36, var2, var1);
   }

   public boolean isUseHighestCompatibleHTTPVersion() {
      return this._UseHighestCompatibleHTTPVersion;
   }

   public boolean isUseHighestCompatibleHTTPVersionSet() {
      return this._isSet(36);
   }

   public void setUseHeaderEncoding(boolean var1) {
      boolean var2 = this._UseHeaderEncoding;
      this._UseHeaderEncoding = var1;
      this._postSet(37, var2, var1);
   }

   public boolean isUseHeaderEncoding() {
      return this._UseHeaderEncoding;
   }

   public boolean isUseHeaderEncodingSet() {
      return this._isSet(37);
   }

   public void setAuthCookieEnabled(boolean var1) {
      boolean var2 = this._AuthCookieEnabled;
      this._AuthCookieEnabled = var1;
      this._postSet(38, var2, var1);
   }

   public boolean isAuthCookieEnabled() {
      return this._AuthCookieEnabled;
   }

   public boolean isAuthCookieEnabledSet() {
      return this._isSet(38);
   }

   public void setWriteChunkBytes(int var1) throws InvalidAttributeValueException {
      int var2 = this._WriteChunkBytes;
      this._WriteChunkBytes = var1;
      this._postSet(39, var2, var1);
   }

   public int getWriteChunkBytes() {
      return this._WriteChunkBytes;
   }

   public boolean isWriteChunkBytesSet() {
      return this._isSet(39);
   }

   public void setDebugEnabled(boolean var1) {
      boolean var2 = this._DebugEnabled;
      this._DebugEnabled = var1;
      this._postSet(40, var2, var1);
   }

   public boolean isDebugEnabled() {
      return this._DebugEnabled;
   }

   public boolean isDebugEnabledSet() {
      return this._isSet(40);
   }

   public void setWAPEnabled(boolean var1) {
      boolean var2 = this._WAPEnabled;
      this._WAPEnabled = var1;
      this._postSet(41, var2, var1);
   }

   public boolean isWAPEnabled() {
      return this._WAPEnabled;
   }

   public boolean isWAPEnabledSet() {
      return this._isSet(41);
   }

   public void setAcceptContextPathInGetRealPath(boolean var1) {
      boolean var2 = this._AcceptContextPathInGetRealPath;
      this._AcceptContextPathInGetRealPath = var1;
      this._postSet(42, var2, var1);
   }

   public boolean isAcceptContextPathInGetRealPath() {
      return this._AcceptContextPathInGetRealPath;
   }

   public boolean isAcceptContextPathInGetRealPathSet() {
      return this._isSet(42);
   }

   public void setSingleSignonDisabled(boolean var1) {
      boolean var2 = this._SingleSignonDisabled;
      this._SingleSignonDisabled = var1;
      this._postSet(43, var2, var1);
   }

   public boolean isSingleSignonDisabled() {
      return this._SingleSignonDisabled;
   }

   public boolean isSingleSignonDisabledSet() {
      return this._isSet(43);
   }

   public WebDeploymentMBean[] getWebDeployments() {
      return this._WebDeployments;
   }

   public String getWebDeploymentsAsString() {
      return this._getHelper()._serializeKeyList(this.getWebDeployments());
   }

   public boolean isWebDeploymentsSet() {
      return this._isSet(44);
   }

   public void setWebDeploymentsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._WebDeployments);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, WebDeploymentMBean.class, new ReferenceManager.Resolver(this, 44) {
                  public void resolveReference(Object var1) {
                     try {
                        WebServerMBeanImpl.this.addWebDeployment((WebDeploymentMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               WebDeploymentMBean[] var6 = this._WebDeployments;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  WebDeploymentMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeWebDeployment(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         WebDeploymentMBean[] var2 = this._WebDeployments;
         this._initializeProperty(44);
         this._postSet(44, var2, this._WebDeployments);
      }
   }

   public void setWebDeployments(WebDeploymentMBean[] var1) throws DistributedManagementException {
      Object var4 = var1 == null ? new WebDeploymentMBeanImpl[0] : var1;
      var1 = (WebDeploymentMBean[])((WebDeploymentMBean[])this._getHelper()._cleanAndValidateArray(var4, WebDeploymentMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 44, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return WebServerMBeanImpl.this.getWebDeployments();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      WebDeploymentMBean[] var5 = this._WebDeployments;
      this._WebDeployments = var1;
      this._postSet(44, var5, var1);
   }

   public boolean isLogFileLimitEnabled() {
      return this._customizer.isLogFileLimitEnabled();
   }

   public boolean isLogFileLimitEnabledSet() {
      return this._isSet(45);
   }

   public void setLogFileLimitEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isLogFileLimitEnabled();
      this._customizer.setLogFileLimitEnabled(var1);
      this._postSet(45, var2, var1);
   }

   public int getLogFileCount() {
      return this._customizer.getLogFileCount();
   }

   public boolean isLogFileCountSet() {
      return this._isSet(46);
   }

   public void setLogFileCount(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LogFileCount", (long)var1, 1L, 9999L);
      int var2 = this.getLogFileCount();
      this._customizer.setLogFileCount(var1);
      this._postSet(46, var2, var1);
   }

   public boolean addWebDeployment(WebDeploymentMBean var1) throws DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 44)) {
         WebDeploymentMBean[] var2;
         if (this._isSet(44)) {
            var2 = (WebDeploymentMBean[])((WebDeploymentMBean[])this._getHelper()._extendArray(this.getWebDeployments(), WebDeploymentMBean.class, var1));
         } else {
            var2 = new WebDeploymentMBean[]{var1};
         }

         try {
            this.setWebDeployments(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeWebDeployment(WebDeploymentMBean var1) throws DistributedManagementException {
      WebDeploymentMBean[] var2 = this.getWebDeployments();
      WebDeploymentMBean[] var3 = (WebDeploymentMBean[])((WebDeploymentMBean[])this._getHelper()._removeElement(var2, WebDeploymentMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setWebDeployments(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void setWorkManagerForRemoteSessionFetching(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._WorkManagerForRemoteSessionFetching;
      this._WorkManagerForRemoteSessionFetching = var1;
      this._postSet(47, var2, var1);
   }

   public String getWorkManagerForRemoteSessionFetching() {
      return this._WorkManagerForRemoteSessionFetching;
   }

   public boolean isWorkManagerForRemoteSessionFetchingSet() {
      return this._isSet(47);
   }

   public void setClientIpHeader(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ClientIpHeader;
      this._ClientIpHeader = var1;
      this._postSet(48, var2, var1);
   }

   public String getClientIpHeader() {
      return this._ClientIpHeader;
   }

   public boolean isClientIpHeaderSet() {
      return this._isSet(48);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet() || this.isWebServerLogSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 33;
      }

      try {
         switch (var1) {
            case 33:
               this._Charsets = null;
               if (var2) {
                  break;
               }
            case 48:
               this._ClientIpHeader = null;
               if (var2) {
                  break;
               }
            case 32:
               this._DefaultWebApp = null;
               if (var2) {
                  break;
               }
            case 31:
               this._DefaultWebAppContextRoot = null;
               if (var2) {
                  break;
               }
            case 15:
               this._FrontendHTTPPort = 0;
               if (var2) {
                  break;
               }
            case 16:
               this._FrontendHTTPSPort = 0;
               if (var2) {
                  break;
               }
            case 14:
               this._FrontendHost = null;
               if (var2) {
                  break;
               }
            case 26:
               this._HttpsKeepAliveSecs = 60;
               if (var2) {
                  break;
               }
            case 25:
               this._KeepAliveSecs = 30;
               if (var2) {
                  break;
               }
            case 17:
               this._LogFileBufferKBytes = 8;
               if (var2) {
                  break;
               }
            case 46:
               this._customizer.setLogFileCount(7);
               if (var2) {
                  break;
               }
            case 22:
               this._LogFileFlushSecs = 60;
               if (var2) {
                  break;
               }
            case 11:
               this._customizer.setLogFileFormat("common");
               if (var2) {
                  break;
               }
            case 13:
               this._customizer.setLogFileName("logs/access.log");
               if (var2) {
                  break;
               }
            case 20:
               this._customizer.setLogRotationPeriodMins(1440);
               if (var2) {
                  break;
               }
            case 23:
               this._customizer.setLogRotationTimeBegin((String)null);
               if (var2) {
                  break;
               }
            case 19:
               this._customizer.setLogRotationType("size");
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setLogTimeInGMT(false);
               if (var2) {
                  break;
               }
            case 18:
               this._MaxLogFileSizeKBytes = 5000;
               if (var2) {
                  break;
               }
            case 29:
               this._MaxPostSize = -1;
               if (var2) {
                  break;
               }
            case 28:
               this._MaxPostTimeSecs = -1;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 21:
               this._OverloadResponseCode = 503;
               if (var2) {
                  break;
               }
            case 27:
               this._PostTimeoutSecs = 30;
               if (var2) {
                  break;
               }
            case 34:
               this._URLResource = null;
               if (var2) {
                  break;
               }
            case 44:
               this._WebDeployments = new WebDeploymentMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._WebServerLog = new WebServerLogMBeanImpl(this, 9);
               this._postCreate((AbstractDescriptorBean)this._WebServerLog);
               if (var2) {
                  break;
               }
            case 47:
               this._WorkManagerForRemoteSessionFetching = null;
               if (var2) {
                  break;
               }
            case 39:
               this._WriteChunkBytes = 512;
               if (var2) {
                  break;
               }
            case 42:
               this._AcceptContextPathInGetRealPath = false;
               if (var2) {
                  break;
               }
            case 38:
               this._AuthCookieEnabled = true;
               if (var2) {
                  break;
               }
            case 35:
               this._ChunkedTransferDisabled = false;
               if (var2) {
                  break;
               }
            case 40:
               this._DebugEnabled = false;
               if (var2) {
                  break;
               }
            case 24:
               this._KeepAliveEnabled = true;
               if (var2) {
                  break;
               }
            case 45:
               this._customizer.setLogFileLimitEnabled(false);
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setLoggingEnabled(true);
               if (var2) {
                  break;
               }
            case 30:
               this._SendServerHeaderEnabled = false;
               if (var2) {
                  break;
               }
            case 43:
               this._SingleSignonDisabled = false;
               if (var2) {
                  break;
               }
            case 37:
               this._UseHeaderEncoding = false;
               if (var2) {
                  break;
               }
            case 36:
               this._UseHighestCompatibleHTTPVersion = true;
               if (var2) {
                  break;
               }
            case 41:
               this._WAPEnabled = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "WebServer";
   }

   public void putValue(String var1, Object var2) {
      boolean var8;
      if (var1.equals("AcceptContextPathInGetRealPath")) {
         var8 = this._AcceptContextPathInGetRealPath;
         this._AcceptContextPathInGetRealPath = (Boolean)var2;
         this._postSet(42, var8, this._AcceptContextPathInGetRealPath);
      } else if (var1.equals("AuthCookieEnabled")) {
         var8 = this._AuthCookieEnabled;
         this._AuthCookieEnabled = (Boolean)var2;
         this._postSet(38, var8, this._AuthCookieEnabled);
      } else {
         Map var9;
         if (var1.equals("Charsets")) {
            var9 = this._Charsets;
            this._Charsets = (Map)var2;
            this._postSet(33, var9, this._Charsets);
         } else if (var1.equals("ChunkedTransferDisabled")) {
            var8 = this._ChunkedTransferDisabled;
            this._ChunkedTransferDisabled = (Boolean)var2;
            this._postSet(35, var8, this._ChunkedTransferDisabled);
         } else {
            String var5;
            if (var1.equals("ClientIpHeader")) {
               var5 = this._ClientIpHeader;
               this._ClientIpHeader = (String)var2;
               this._postSet(48, var5, this._ClientIpHeader);
            } else if (var1.equals("DebugEnabled")) {
               var8 = this._DebugEnabled;
               this._DebugEnabled = (Boolean)var2;
               this._postSet(40, var8, this._DebugEnabled);
            } else if (var1.equals("DefaultWebApp")) {
               WebAppComponentMBean var10 = this._DefaultWebApp;
               this._DefaultWebApp = (WebAppComponentMBean)var2;
               this._postSet(32, var10, this._DefaultWebApp);
            } else if (var1.equals("DefaultWebAppContextRoot")) {
               var5 = this._DefaultWebAppContextRoot;
               this._DefaultWebAppContextRoot = (String)var2;
               this._postSet(31, var5, this._DefaultWebAppContextRoot);
            } else {
               int var4;
               if (var1.equals("FrontendHTTPPort")) {
                  var4 = this._FrontendHTTPPort;
                  this._FrontendHTTPPort = (Integer)var2;
                  this._postSet(15, var4, this._FrontendHTTPPort);
               } else if (var1.equals("FrontendHTTPSPort")) {
                  var4 = this._FrontendHTTPSPort;
                  this._FrontendHTTPSPort = (Integer)var2;
                  this._postSet(16, var4, this._FrontendHTTPSPort);
               } else if (var1.equals("FrontendHost")) {
                  var5 = this._FrontendHost;
                  this._FrontendHost = (String)var2;
                  this._postSet(14, var5, this._FrontendHost);
               } else if (var1.equals("HttpsKeepAliveSecs")) {
                  var4 = this._HttpsKeepAliveSecs;
                  this._HttpsKeepAliveSecs = (Integer)var2;
                  this._postSet(26, var4, this._HttpsKeepAliveSecs);
               } else if (var1.equals("KeepAliveEnabled")) {
                  var8 = this._KeepAliveEnabled;
                  this._KeepAliveEnabled = (Boolean)var2;
                  this._postSet(24, var8, this._KeepAliveEnabled);
               } else if (var1.equals("KeepAliveSecs")) {
                  var4 = this._KeepAliveSecs;
                  this._KeepAliveSecs = (Integer)var2;
                  this._postSet(25, var4, this._KeepAliveSecs);
               } else if (var1.equals("LogFileBufferKBytes")) {
                  var4 = this._LogFileBufferKBytes;
                  this._LogFileBufferKBytes = (Integer)var2;
                  this._postSet(17, var4, this._LogFileBufferKBytes);
               } else if (var1.equals("LogFileCount")) {
                  var4 = this._LogFileCount;
                  this._LogFileCount = (Integer)var2;
                  this._postSet(46, var4, this._LogFileCount);
               } else if (var1.equals("LogFileFlushSecs")) {
                  var4 = this._LogFileFlushSecs;
                  this._LogFileFlushSecs = (Integer)var2;
                  this._postSet(22, var4, this._LogFileFlushSecs);
               } else if (var1.equals("LogFileFormat")) {
                  var5 = this._LogFileFormat;
                  this._LogFileFormat = (String)var2;
                  this._postSet(11, var5, this._LogFileFormat);
               } else if (var1.equals("LogFileLimitEnabled")) {
                  var8 = this._LogFileLimitEnabled;
                  this._LogFileLimitEnabled = (Boolean)var2;
                  this._postSet(45, var8, this._LogFileLimitEnabled);
               } else if (var1.equals("LogFileName")) {
                  var5 = this._LogFileName;
                  this._LogFileName = (String)var2;
                  this._postSet(13, var5, this._LogFileName);
               } else if (var1.equals("LogRotationPeriodMins")) {
                  var4 = this._LogRotationPeriodMins;
                  this._LogRotationPeriodMins = (Integer)var2;
                  this._postSet(20, var4, this._LogRotationPeriodMins);
               } else if (var1.equals("LogRotationTimeBegin")) {
                  var5 = this._LogRotationTimeBegin;
                  this._LogRotationTimeBegin = (String)var2;
                  this._postSet(23, var5, this._LogRotationTimeBegin);
               } else if (var1.equals("LogRotationType")) {
                  var5 = this._LogRotationType;
                  this._LogRotationType = (String)var2;
                  this._postSet(19, var5, this._LogRotationType);
               } else if (var1.equals("LogTimeInGMT")) {
                  var8 = this._LogTimeInGMT;
                  this._LogTimeInGMT = (Boolean)var2;
                  this._postSet(12, var8, this._LogTimeInGMT);
               } else if (var1.equals("LoggingEnabled")) {
                  var8 = this._LoggingEnabled;
                  this._LoggingEnabled = (Boolean)var2;
                  this._postSet(10, var8, this._LoggingEnabled);
               } else if (var1.equals("MaxLogFileSizeKBytes")) {
                  var4 = this._MaxLogFileSizeKBytes;
                  this._MaxLogFileSizeKBytes = (Integer)var2;
                  this._postSet(18, var4, this._MaxLogFileSizeKBytes);
               } else if (var1.equals("MaxPostSize")) {
                  var4 = this._MaxPostSize;
                  this._MaxPostSize = (Integer)var2;
                  this._postSet(29, var4, this._MaxPostSize);
               } else if (var1.equals("MaxPostTimeSecs")) {
                  var4 = this._MaxPostTimeSecs;
                  this._MaxPostTimeSecs = (Integer)var2;
                  this._postSet(28, var4, this._MaxPostTimeSecs);
               } else if (var1.equals("Name")) {
                  var5 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var5, this._Name);
               } else if (var1.equals("OverloadResponseCode")) {
                  var4 = this._OverloadResponseCode;
                  this._OverloadResponseCode = (Integer)var2;
                  this._postSet(21, var4, this._OverloadResponseCode);
               } else if (var1.equals("PostTimeoutSecs")) {
                  var4 = this._PostTimeoutSecs;
                  this._PostTimeoutSecs = (Integer)var2;
                  this._postSet(27, var4, this._PostTimeoutSecs);
               } else if (var1.equals("SendServerHeaderEnabled")) {
                  var8 = this._SendServerHeaderEnabled;
                  this._SendServerHeaderEnabled = (Boolean)var2;
                  this._postSet(30, var8, this._SendServerHeaderEnabled);
               } else if (var1.equals("SingleSignonDisabled")) {
                  var8 = this._SingleSignonDisabled;
                  this._SingleSignonDisabled = (Boolean)var2;
                  this._postSet(43, var8, this._SingleSignonDisabled);
               } else if (var1.equals("URLResource")) {
                  var9 = this._URLResource;
                  this._URLResource = (Map)var2;
                  this._postSet(34, var9, this._URLResource);
               } else if (var1.equals("UseHeaderEncoding")) {
                  var8 = this._UseHeaderEncoding;
                  this._UseHeaderEncoding = (Boolean)var2;
                  this._postSet(37, var8, this._UseHeaderEncoding);
               } else if (var1.equals("UseHighestCompatibleHTTPVersion")) {
                  var8 = this._UseHighestCompatibleHTTPVersion;
                  this._UseHighestCompatibleHTTPVersion = (Boolean)var2;
                  this._postSet(36, var8, this._UseHighestCompatibleHTTPVersion);
               } else if (var1.equals("WAPEnabled")) {
                  var8 = this._WAPEnabled;
                  this._WAPEnabled = (Boolean)var2;
                  this._postSet(41, var8, this._WAPEnabled);
               } else if (var1.equals("WebDeployments")) {
                  WebDeploymentMBean[] var7 = this._WebDeployments;
                  this._WebDeployments = (WebDeploymentMBean[])((WebDeploymentMBean[])var2);
                  this._postSet(44, var7, this._WebDeployments);
               } else if (var1.equals("WebServerLog")) {
                  WebServerLogMBean var6 = this._WebServerLog;
                  this._WebServerLog = (WebServerLogMBean)var2;
                  this._postSet(9, var6, this._WebServerLog);
               } else if (var1.equals("WorkManagerForRemoteSessionFetching")) {
                  var5 = this._WorkManagerForRemoteSessionFetching;
                  this._WorkManagerForRemoteSessionFetching = (String)var2;
                  this._postSet(47, var5, this._WorkManagerForRemoteSessionFetching);
               } else if (var1.equals("WriteChunkBytes")) {
                  var4 = this._WriteChunkBytes;
                  this._WriteChunkBytes = (Integer)var2;
                  this._postSet(39, var4, this._WriteChunkBytes);
               } else if (var1.equals("customizer")) {
                  WebServer var3 = this._customizer;
                  this._customizer = (WebServer)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AcceptContextPathInGetRealPath")) {
         return new Boolean(this._AcceptContextPathInGetRealPath);
      } else if (var1.equals("AuthCookieEnabled")) {
         return new Boolean(this._AuthCookieEnabled);
      } else if (var1.equals("Charsets")) {
         return this._Charsets;
      } else if (var1.equals("ChunkedTransferDisabled")) {
         return new Boolean(this._ChunkedTransferDisabled);
      } else if (var1.equals("ClientIpHeader")) {
         return this._ClientIpHeader;
      } else if (var1.equals("DebugEnabled")) {
         return new Boolean(this._DebugEnabled);
      } else if (var1.equals("DefaultWebApp")) {
         return this._DefaultWebApp;
      } else if (var1.equals("DefaultWebAppContextRoot")) {
         return this._DefaultWebAppContextRoot;
      } else if (var1.equals("FrontendHTTPPort")) {
         return new Integer(this._FrontendHTTPPort);
      } else if (var1.equals("FrontendHTTPSPort")) {
         return new Integer(this._FrontendHTTPSPort);
      } else if (var1.equals("FrontendHost")) {
         return this._FrontendHost;
      } else if (var1.equals("HttpsKeepAliveSecs")) {
         return new Integer(this._HttpsKeepAliveSecs);
      } else if (var1.equals("KeepAliveEnabled")) {
         return new Boolean(this._KeepAliveEnabled);
      } else if (var1.equals("KeepAliveSecs")) {
         return new Integer(this._KeepAliveSecs);
      } else if (var1.equals("LogFileBufferKBytes")) {
         return new Integer(this._LogFileBufferKBytes);
      } else if (var1.equals("LogFileCount")) {
         return new Integer(this._LogFileCount);
      } else if (var1.equals("LogFileFlushSecs")) {
         return new Integer(this._LogFileFlushSecs);
      } else if (var1.equals("LogFileFormat")) {
         return this._LogFileFormat;
      } else if (var1.equals("LogFileLimitEnabled")) {
         return new Boolean(this._LogFileLimitEnabled);
      } else if (var1.equals("LogFileName")) {
         return this._LogFileName;
      } else if (var1.equals("LogRotationPeriodMins")) {
         return new Integer(this._LogRotationPeriodMins);
      } else if (var1.equals("LogRotationTimeBegin")) {
         return this._LogRotationTimeBegin;
      } else if (var1.equals("LogRotationType")) {
         return this._LogRotationType;
      } else if (var1.equals("LogTimeInGMT")) {
         return new Boolean(this._LogTimeInGMT);
      } else if (var1.equals("LoggingEnabled")) {
         return new Boolean(this._LoggingEnabled);
      } else if (var1.equals("MaxLogFileSizeKBytes")) {
         return new Integer(this._MaxLogFileSizeKBytes);
      } else if (var1.equals("MaxPostSize")) {
         return new Integer(this._MaxPostSize);
      } else if (var1.equals("MaxPostTimeSecs")) {
         return new Integer(this._MaxPostTimeSecs);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("OverloadResponseCode")) {
         return new Integer(this._OverloadResponseCode);
      } else if (var1.equals("PostTimeoutSecs")) {
         return new Integer(this._PostTimeoutSecs);
      } else if (var1.equals("SendServerHeaderEnabled")) {
         return new Boolean(this._SendServerHeaderEnabled);
      } else if (var1.equals("SingleSignonDisabled")) {
         return new Boolean(this._SingleSignonDisabled);
      } else if (var1.equals("URLResource")) {
         return this._URLResource;
      } else if (var1.equals("UseHeaderEncoding")) {
         return new Boolean(this._UseHeaderEncoding);
      } else if (var1.equals("UseHighestCompatibleHTTPVersion")) {
         return new Boolean(this._UseHighestCompatibleHTTPVersion);
      } else if (var1.equals("WAPEnabled")) {
         return new Boolean(this._WAPEnabled);
      } else if (var1.equals("WebDeployments")) {
         return this._WebDeployments;
      } else if (var1.equals("WebServerLog")) {
         return this._WebServerLog;
      } else if (var1.equals("WorkManagerForRemoteSessionFetching")) {
         return this._WorkManagerForRemoteSessionFetching;
      } else if (var1.equals("WriteChunkBytes")) {
         return new Integer(this._WriteChunkBytes);
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 20:
            case 27:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 35:
            case 37:
            case 38:
            case 39:
            default:
               break;
            case 8:
               if (var1.equals("charsets")) {
                  return 33;
               }
               break;
            case 11:
               if (var1.equals("wap-enabled")) {
                  return 41;
               }
               break;
            case 12:
               if (var1.equals("url-resource")) {
                  return 34;
               }
               break;
            case 13:
               if (var1.equals("frontend-host")) {
                  return 14;
               }

               if (var1.equals("log-file-name")) {
                  return 13;
               }

               if (var1.equals("max-post-size")) {
                  return 29;
               }

               if (var1.equals("debug-enabled")) {
                  return 40;
               }
               break;
            case 14:
               if (var1.equals("log-file-count")) {
                  return 46;
               }

               if (var1.equals("log-time-ingmt")) {
                  return 12;
               }

               if (var1.equals("web-deployment")) {
                  return 44;
               }

               if (var1.equals("web-server-log")) {
                  return 9;
               }
               break;
            case 15:
               if (var1.equals("default-web-app")) {
                  return 32;
               }

               if (var1.equals("keep-alive-secs")) {
                  return 25;
               }

               if (var1.equals("log-file-format")) {
                  return 11;
               }

               if (var1.equals("logging-enabled")) {
                  return 10;
               }
               break;
            case 16:
               if (var1.equals("client-ip-header")) {
                  return 48;
               }
               break;
            case 17:
               if (var1.equals("frontendhttp-port")) {
                  return 15;
               }

               if (var1.equals("log-rotation-type")) {
                  return 19;
               }

               if (var1.equals("post-timeout-secs")) {
                  return 27;
               }

               if (var1.equals("write-chunk-bytes")) {
                  return 39;
               }
               break;
            case 18:
               if (var1.equals("frontendhttps-port")) {
                  return 16;
               }

               if (var1.equals("max-post-time-secs")) {
                  return 28;
               }

               if (var1.equals("keep-alive-enabled")) {
                  return 24;
               }
               break;
            case 19:
               if (var1.equals("log-file-flush-secs")) {
                  return 22;
               }

               if (var1.equals("auth-cookie-enabled")) {
                  return 38;
               }

               if (var1.equals("use-header-encoding")) {
                  return 37;
               }
               break;
            case 21:
               if (var1.equals("https-keep-alive-secs")) {
                  return 26;
               }
               break;
            case 22:
               if (var1.equals("log-file-bufferk-bytes")) {
                  return 17;
               }

               if (var1.equals("overload-response-code")) {
                  return 21;
               }

               if (var1.equals("log-file-limit-enabled")) {
                  return 45;
               }

               if (var1.equals("single-signon-disabled")) {
                  return 43;
               }
               break;
            case 23:
               if (var1.equals("log-rotation-time-begin")) {
                  return 23;
               }
               break;
            case 24:
               if (var1.equals("log-rotation-period-mins")) {
                  return 20;
               }

               if (var1.equals("max-log-file-sizek-bytes")) {
                  return 18;
               }
               break;
            case 25:
               if (var1.equals("chunked-transfer-disabled")) {
                  return 35;
               }
               break;
            case 26:
               if (var1.equals("send-server-header-enabled")) {
                  return 30;
               }
               break;
            case 28:
               if (var1.equals("default-web-app-context-root")) {
                  return 31;
               }
               break;
            case 34:
               if (var1.equals("use-highest-compatiblehttp-version")) {
                  return 36;
               }
               break;
            case 36:
               if (var1.equals("accept-context-path-in-get-real-path")) {
                  return 42;
               }
               break;
            case 40:
               if (var1.equals("work-manager-for-remote-session-fetching")) {
                  return 47;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 9:
               return new WebServerLogMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            default:
               return super.getElementName(var1);
            case 9:
               return "web-server-log";
            case 10:
               return "logging-enabled";
            case 11:
               return "log-file-format";
            case 12:
               return "log-time-ingmt";
            case 13:
               return "log-file-name";
            case 14:
               return "frontend-host";
            case 15:
               return "frontendhttp-port";
            case 16:
               return "frontendhttps-port";
            case 17:
               return "log-file-bufferk-bytes";
            case 18:
               return "max-log-file-sizek-bytes";
            case 19:
               return "log-rotation-type";
            case 20:
               return "log-rotation-period-mins";
            case 21:
               return "overload-response-code";
            case 22:
               return "log-file-flush-secs";
            case 23:
               return "log-rotation-time-begin";
            case 24:
               return "keep-alive-enabled";
            case 25:
               return "keep-alive-secs";
            case 26:
               return "https-keep-alive-secs";
            case 27:
               return "post-timeout-secs";
            case 28:
               return "max-post-time-secs";
            case 29:
               return "max-post-size";
            case 30:
               return "send-server-header-enabled";
            case 31:
               return "default-web-app-context-root";
            case 32:
               return "default-web-app";
            case 33:
               return "charsets";
            case 34:
               return "url-resource";
            case 35:
               return "chunked-transfer-disabled";
            case 36:
               return "use-highest-compatiblehttp-version";
            case 37:
               return "use-header-encoding";
            case 38:
               return "auth-cookie-enabled";
            case 39:
               return "write-chunk-bytes";
            case 40:
               return "debug-enabled";
            case 41:
               return "wap-enabled";
            case 42:
               return "accept-context-path-in-get-real-path";
            case 43:
               return "single-signon-disabled";
            case 44:
               return "web-deployment";
            case 45:
               return "log-file-limit-enabled";
            case 46:
               return "log-file-count";
            case 47:
               return "work-manager-for-remote-session-fetching";
            case 48:
               return "client-ip-header";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 44:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 9:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
            case 14:
               return true;
            case 15:
               return true;
            case 16:
               return true;
            case 17:
               return true;
            case 18:
               return true;
            case 19:
               return true;
            case 20:
               return true;
            case 21:
               return true;
            case 22:
               return true;
            case 23:
               return true;
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 33:
            case 34:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            default:
               return super.isConfigurable(var1);
            case 31:
               return true;
            case 32:
               return true;
            case 35:
               return true;
            case 36:
               return true;
            case 37:
               return true;
            case 38:
               return true;
            case 47:
               return true;
            case 48:
               return true;
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private WebServerMBeanImpl bean;

      protected Helper(WebServerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            default:
               return super.getPropertyName(var1);
            case 9:
               return "WebServerLog";
            case 10:
               return "LoggingEnabled";
            case 11:
               return "LogFileFormat";
            case 12:
               return "LogTimeInGMT";
            case 13:
               return "LogFileName";
            case 14:
               return "FrontendHost";
            case 15:
               return "FrontendHTTPPort";
            case 16:
               return "FrontendHTTPSPort";
            case 17:
               return "LogFileBufferKBytes";
            case 18:
               return "MaxLogFileSizeKBytes";
            case 19:
               return "LogRotationType";
            case 20:
               return "LogRotationPeriodMins";
            case 21:
               return "OverloadResponseCode";
            case 22:
               return "LogFileFlushSecs";
            case 23:
               return "LogRotationTimeBegin";
            case 24:
               return "KeepAliveEnabled";
            case 25:
               return "KeepAliveSecs";
            case 26:
               return "HttpsKeepAliveSecs";
            case 27:
               return "PostTimeoutSecs";
            case 28:
               return "MaxPostTimeSecs";
            case 29:
               return "MaxPostSize";
            case 30:
               return "SendServerHeaderEnabled";
            case 31:
               return "DefaultWebAppContextRoot";
            case 32:
               return "DefaultWebApp";
            case 33:
               return "Charsets";
            case 34:
               return "URLResource";
            case 35:
               return "ChunkedTransferDisabled";
            case 36:
               return "UseHighestCompatibleHTTPVersion";
            case 37:
               return "UseHeaderEncoding";
            case 38:
               return "AuthCookieEnabled";
            case 39:
               return "WriteChunkBytes";
            case 40:
               return "DebugEnabled";
            case 41:
               return "WAPEnabled";
            case 42:
               return "AcceptContextPathInGetRealPath";
            case 43:
               return "SingleSignonDisabled";
            case 44:
               return "WebDeployments";
            case 45:
               return "LogFileLimitEnabled";
            case 46:
               return "LogFileCount";
            case 47:
               return "WorkManagerForRemoteSessionFetching";
            case 48:
               return "ClientIpHeader";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Charsets")) {
            return 33;
         } else if (var1.equals("ClientIpHeader")) {
            return 48;
         } else if (var1.equals("DefaultWebApp")) {
            return 32;
         } else if (var1.equals("DefaultWebAppContextRoot")) {
            return 31;
         } else if (var1.equals("FrontendHTTPPort")) {
            return 15;
         } else if (var1.equals("FrontendHTTPSPort")) {
            return 16;
         } else if (var1.equals("FrontendHost")) {
            return 14;
         } else if (var1.equals("HttpsKeepAliveSecs")) {
            return 26;
         } else if (var1.equals("KeepAliveSecs")) {
            return 25;
         } else if (var1.equals("LogFileBufferKBytes")) {
            return 17;
         } else if (var1.equals("LogFileCount")) {
            return 46;
         } else if (var1.equals("LogFileFlushSecs")) {
            return 22;
         } else if (var1.equals("LogFileFormat")) {
            return 11;
         } else if (var1.equals("LogFileName")) {
            return 13;
         } else if (var1.equals("LogRotationPeriodMins")) {
            return 20;
         } else if (var1.equals("LogRotationTimeBegin")) {
            return 23;
         } else if (var1.equals("LogRotationType")) {
            return 19;
         } else if (var1.equals("LogTimeInGMT")) {
            return 12;
         } else if (var1.equals("MaxLogFileSizeKBytes")) {
            return 18;
         } else if (var1.equals("MaxPostSize")) {
            return 29;
         } else if (var1.equals("MaxPostTimeSecs")) {
            return 28;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("OverloadResponseCode")) {
            return 21;
         } else if (var1.equals("PostTimeoutSecs")) {
            return 27;
         } else if (var1.equals("URLResource")) {
            return 34;
         } else if (var1.equals("WebDeployments")) {
            return 44;
         } else if (var1.equals("WebServerLog")) {
            return 9;
         } else if (var1.equals("WorkManagerForRemoteSessionFetching")) {
            return 47;
         } else if (var1.equals("WriteChunkBytes")) {
            return 39;
         } else if (var1.equals("AcceptContextPathInGetRealPath")) {
            return 42;
         } else if (var1.equals("AuthCookieEnabled")) {
            return 38;
         } else if (var1.equals("ChunkedTransferDisabled")) {
            return 35;
         } else if (var1.equals("DebugEnabled")) {
            return 40;
         } else if (var1.equals("KeepAliveEnabled")) {
            return 24;
         } else if (var1.equals("LogFileLimitEnabled")) {
            return 45;
         } else if (var1.equals("LoggingEnabled")) {
            return 10;
         } else if (var1.equals("SendServerHeaderEnabled")) {
            return 30;
         } else if (var1.equals("SingleSignonDisabled")) {
            return 43;
         } else if (var1.equals("UseHeaderEncoding")) {
            return 37;
         } else if (var1.equals("UseHighestCompatibleHTTPVersion")) {
            return 36;
         } else {
            return var1.equals("WAPEnabled") ? 41 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getWebServerLog() != null) {
            var1.add(new ArrayIterator(new WebServerLogMBean[]{this.bean.getWebServerLog()}));
         }

         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isCharsetsSet()) {
               var2.append("Charsets");
               var2.append(String.valueOf(this.bean.getCharsets()));
            }

            if (this.bean.isClientIpHeaderSet()) {
               var2.append("ClientIpHeader");
               var2.append(String.valueOf(this.bean.getClientIpHeader()));
            }

            if (this.bean.isDefaultWebAppSet()) {
               var2.append("DefaultWebApp");
               var2.append(String.valueOf(this.bean.getDefaultWebApp()));
            }

            if (this.bean.isDefaultWebAppContextRootSet()) {
               var2.append("DefaultWebAppContextRoot");
               var2.append(String.valueOf(this.bean.getDefaultWebAppContextRoot()));
            }

            if (this.bean.isFrontendHTTPPortSet()) {
               var2.append("FrontendHTTPPort");
               var2.append(String.valueOf(this.bean.getFrontendHTTPPort()));
            }

            if (this.bean.isFrontendHTTPSPortSet()) {
               var2.append("FrontendHTTPSPort");
               var2.append(String.valueOf(this.bean.getFrontendHTTPSPort()));
            }

            if (this.bean.isFrontendHostSet()) {
               var2.append("FrontendHost");
               var2.append(String.valueOf(this.bean.getFrontendHost()));
            }

            if (this.bean.isHttpsKeepAliveSecsSet()) {
               var2.append("HttpsKeepAliveSecs");
               var2.append(String.valueOf(this.bean.getHttpsKeepAliveSecs()));
            }

            if (this.bean.isKeepAliveSecsSet()) {
               var2.append("KeepAliveSecs");
               var2.append(String.valueOf(this.bean.getKeepAliveSecs()));
            }

            if (this.bean.isLogFileBufferKBytesSet()) {
               var2.append("LogFileBufferKBytes");
               var2.append(String.valueOf(this.bean.getLogFileBufferKBytes()));
            }

            if (this.bean.isLogFileCountSet()) {
               var2.append("LogFileCount");
               var2.append(String.valueOf(this.bean.getLogFileCount()));
            }

            if (this.bean.isLogFileFlushSecsSet()) {
               var2.append("LogFileFlushSecs");
               var2.append(String.valueOf(this.bean.getLogFileFlushSecs()));
            }

            if (this.bean.isLogFileFormatSet()) {
               var2.append("LogFileFormat");
               var2.append(String.valueOf(this.bean.getLogFileFormat()));
            }

            if (this.bean.isLogFileNameSet()) {
               var2.append("LogFileName");
               var2.append(String.valueOf(this.bean.getLogFileName()));
            }

            if (this.bean.isLogRotationPeriodMinsSet()) {
               var2.append("LogRotationPeriodMins");
               var2.append(String.valueOf(this.bean.getLogRotationPeriodMins()));
            }

            if (this.bean.isLogRotationTimeBeginSet()) {
               var2.append("LogRotationTimeBegin");
               var2.append(String.valueOf(this.bean.getLogRotationTimeBegin()));
            }

            if (this.bean.isLogRotationTypeSet()) {
               var2.append("LogRotationType");
               var2.append(String.valueOf(this.bean.getLogRotationType()));
            }

            if (this.bean.isLogTimeInGMTSet()) {
               var2.append("LogTimeInGMT");
               var2.append(String.valueOf(this.bean.getLogTimeInGMT()));
            }

            if (this.bean.isMaxLogFileSizeKBytesSet()) {
               var2.append("MaxLogFileSizeKBytes");
               var2.append(String.valueOf(this.bean.getMaxLogFileSizeKBytes()));
            }

            if (this.bean.isMaxPostSizeSet()) {
               var2.append("MaxPostSize");
               var2.append(String.valueOf(this.bean.getMaxPostSize()));
            }

            if (this.bean.isMaxPostTimeSecsSet()) {
               var2.append("MaxPostTimeSecs");
               var2.append(String.valueOf(this.bean.getMaxPostTimeSecs()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isOverloadResponseCodeSet()) {
               var2.append("OverloadResponseCode");
               var2.append(String.valueOf(this.bean.getOverloadResponseCode()));
            }

            if (this.bean.isPostTimeoutSecsSet()) {
               var2.append("PostTimeoutSecs");
               var2.append(String.valueOf(this.bean.getPostTimeoutSecs()));
            }

            if (this.bean.isURLResourceSet()) {
               var2.append("URLResource");
               var2.append(String.valueOf(this.bean.getURLResource()));
            }

            if (this.bean.isWebDeploymentsSet()) {
               var2.append("WebDeployments");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getWebDeployments())));
            }

            var5 = this.computeChildHashValue(this.bean.getWebServerLog());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isWorkManagerForRemoteSessionFetchingSet()) {
               var2.append("WorkManagerForRemoteSessionFetching");
               var2.append(String.valueOf(this.bean.getWorkManagerForRemoteSessionFetching()));
            }

            if (this.bean.isWriteChunkBytesSet()) {
               var2.append("WriteChunkBytes");
               var2.append(String.valueOf(this.bean.getWriteChunkBytes()));
            }

            if (this.bean.isAcceptContextPathInGetRealPathSet()) {
               var2.append("AcceptContextPathInGetRealPath");
               var2.append(String.valueOf(this.bean.isAcceptContextPathInGetRealPath()));
            }

            if (this.bean.isAuthCookieEnabledSet()) {
               var2.append("AuthCookieEnabled");
               var2.append(String.valueOf(this.bean.isAuthCookieEnabled()));
            }

            if (this.bean.isChunkedTransferDisabledSet()) {
               var2.append("ChunkedTransferDisabled");
               var2.append(String.valueOf(this.bean.isChunkedTransferDisabled()));
            }

            if (this.bean.isDebugEnabledSet()) {
               var2.append("DebugEnabled");
               var2.append(String.valueOf(this.bean.isDebugEnabled()));
            }

            if (this.bean.isKeepAliveEnabledSet()) {
               var2.append("KeepAliveEnabled");
               var2.append(String.valueOf(this.bean.isKeepAliveEnabled()));
            }

            if (this.bean.isLogFileLimitEnabledSet()) {
               var2.append("LogFileLimitEnabled");
               var2.append(String.valueOf(this.bean.isLogFileLimitEnabled()));
            }

            if (this.bean.isLoggingEnabledSet()) {
               var2.append("LoggingEnabled");
               var2.append(String.valueOf(this.bean.isLoggingEnabled()));
            }

            if (this.bean.isSendServerHeaderEnabledSet()) {
               var2.append("SendServerHeaderEnabled");
               var2.append(String.valueOf(this.bean.isSendServerHeaderEnabled()));
            }

            if (this.bean.isSingleSignonDisabledSet()) {
               var2.append("SingleSignonDisabled");
               var2.append(String.valueOf(this.bean.isSingleSignonDisabled()));
            }

            if (this.bean.isUseHeaderEncodingSet()) {
               var2.append("UseHeaderEncoding");
               var2.append(String.valueOf(this.bean.isUseHeaderEncoding()));
            }

            if (this.bean.isUseHighestCompatibleHTTPVersionSet()) {
               var2.append("UseHighestCompatibleHTTPVersion");
               var2.append(String.valueOf(this.bean.isUseHighestCompatibleHTTPVersion()));
            }

            if (this.bean.isWAPEnabledSet()) {
               var2.append("WAPEnabled");
               var2.append(String.valueOf(this.bean.isWAPEnabled()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            WebServerMBeanImpl var2 = (WebServerMBeanImpl)var1;
            this.computeDiff("Charsets", this.bean.getCharsets(), var2.getCharsets(), true);
            this.computeDiff("ClientIpHeader", this.bean.getClientIpHeader(), var2.getClientIpHeader(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DefaultWebApp", this.bean.getDefaultWebApp(), var2.getDefaultWebApp(), true);
            }

            this.computeDiff("DefaultWebAppContextRoot", this.bean.getDefaultWebAppContextRoot(), var2.getDefaultWebAppContextRoot(), true);
            this.computeDiff("FrontendHTTPPort", this.bean.getFrontendHTTPPort(), var2.getFrontendHTTPPort(), true);
            this.computeDiff("FrontendHTTPSPort", this.bean.getFrontendHTTPSPort(), var2.getFrontendHTTPSPort(), true);
            this.computeDiff("FrontendHost", this.bean.getFrontendHost(), var2.getFrontendHost(), true);
            this.computeDiff("HttpsKeepAliveSecs", this.bean.getHttpsKeepAliveSecs(), var2.getHttpsKeepAliveSecs(), true);
            this.computeDiff("KeepAliveSecs", this.bean.getKeepAliveSecs(), var2.getKeepAliveSecs(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogFileBufferKBytes", this.bean.getLogFileBufferKBytes(), var2.getLogFileBufferKBytes(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogFileCount", this.bean.getLogFileCount(), var2.getLogFileCount(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogFileFlushSecs", this.bean.getLogFileFlushSecs(), var2.getLogFileFlushSecs(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogFileFormat", this.bean.getLogFileFormat(), var2.getLogFileFormat(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogFileName", this.bean.getLogFileName(), var2.getLogFileName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogRotationPeriodMins", this.bean.getLogRotationPeriodMins(), var2.getLogRotationPeriodMins(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogRotationTimeBegin", this.bean.getLogRotationTimeBegin(), var2.getLogRotationTimeBegin(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogRotationType", this.bean.getLogRotationType(), var2.getLogRotationType(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogTimeInGMT", this.bean.getLogTimeInGMT(), var2.getLogTimeInGMT(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MaxLogFileSizeKBytes", this.bean.getMaxLogFileSizeKBytes(), var2.getMaxLogFileSizeKBytes(), false);
            }

            this.computeDiff("MaxPostSize", this.bean.getMaxPostSize(), var2.getMaxPostSize(), true);
            this.computeDiff("MaxPostTimeSecs", this.bean.getMaxPostTimeSecs(), var2.getMaxPostTimeSecs(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("OverloadResponseCode", this.bean.getOverloadResponseCode(), var2.getOverloadResponseCode(), true);
            this.computeDiff("PostTimeoutSecs", this.bean.getPostTimeoutSecs(), var2.getPostTimeoutSecs(), true);
            this.computeDiff("URLResource", this.bean.getURLResource(), var2.getURLResource(), true);
            this.computeDiff("WebDeployments", this.bean.getWebDeployments(), var2.getWebDeployments(), true);
            this.computeSubDiff("WebServerLog", this.bean.getWebServerLog(), var2.getWebServerLog());
            this.computeDiff("WorkManagerForRemoteSessionFetching", this.bean.getWorkManagerForRemoteSessionFetching(), var2.getWorkManagerForRemoteSessionFetching(), false);
            this.computeDiff("WriteChunkBytes", this.bean.getWriteChunkBytes(), var2.getWriteChunkBytes(), true);
            this.computeDiff("AcceptContextPathInGetRealPath", this.bean.isAcceptContextPathInGetRealPath(), var2.isAcceptContextPathInGetRealPath(), false);
            this.computeDiff("AuthCookieEnabled", this.bean.isAuthCookieEnabled(), var2.isAuthCookieEnabled(), true);
            this.computeDiff("ChunkedTransferDisabled", this.bean.isChunkedTransferDisabled(), var2.isChunkedTransferDisabled(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DebugEnabled", this.bean.isDebugEnabled(), var2.isDebugEnabled(), false);
            }

            this.computeDiff("KeepAliveEnabled", this.bean.isKeepAliveEnabled(), var2.isKeepAliveEnabled(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LogFileLimitEnabled", this.bean.isLogFileLimitEnabled(), var2.isLogFileLimitEnabled(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LoggingEnabled", this.bean.isLoggingEnabled(), var2.isLoggingEnabled(), false);
            }

            this.computeDiff("SendServerHeaderEnabled", this.bean.isSendServerHeaderEnabled(), var2.isSendServerHeaderEnabled(), true);
            this.computeDiff("SingleSignonDisabled", this.bean.isSingleSignonDisabled(), var2.isSingleSignonDisabled(), true);
            this.computeDiff("UseHeaderEncoding", this.bean.isUseHeaderEncoding(), var2.isUseHeaderEncoding(), true);
            this.computeDiff("UseHighestCompatibleHTTPVersion", this.bean.isUseHighestCompatibleHTTPVersion(), var2.isUseHighestCompatibleHTTPVersion(), true);
            this.computeDiff("WAPEnabled", this.bean.isWAPEnabled(), var2.isWAPEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebServerMBeanImpl var3 = (WebServerMBeanImpl)var1.getSourceBean();
            WebServerMBeanImpl var4 = (WebServerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Charsets")) {
                  var3.setCharsets(var4.getCharsets());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 33);
               } else if (var5.equals("ClientIpHeader")) {
                  var3.setClientIpHeader(var4.getClientIpHeader());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 48);
               } else if (var5.equals("DefaultWebApp")) {
                  var3.setDefaultWebAppAsString(var4.getDefaultWebAppAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 32);
               } else if (var5.equals("DefaultWebAppContextRoot")) {
                  var3.setDefaultWebAppContextRoot(var4.getDefaultWebAppContextRoot());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 31);
               } else if (var5.equals("FrontendHTTPPort")) {
                  var3.setFrontendHTTPPort(var4.getFrontendHTTPPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("FrontendHTTPSPort")) {
                  var3.setFrontendHTTPSPort(var4.getFrontendHTTPSPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("FrontendHost")) {
                  var3.setFrontendHost(var4.getFrontendHost());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("HttpsKeepAliveSecs")) {
                  var3.setHttpsKeepAliveSecs(var4.getHttpsKeepAliveSecs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("KeepAliveSecs")) {
                  var3.setKeepAliveSecs(var4.getKeepAliveSecs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("LogFileBufferKBytes")) {
                  var3.setLogFileBufferKBytes(var4.getLogFileBufferKBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("LogFileCount")) {
                  var3.setLogFileCount(var4.getLogFileCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 46);
               } else if (var5.equals("LogFileFlushSecs")) {
                  var3.setLogFileFlushSecs(var4.getLogFileFlushSecs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("LogFileFormat")) {
                  var3.setLogFileFormat(var4.getLogFileFormat());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("LogFileName")) {
                  var3.setLogFileName(var4.getLogFileName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("LogRotationPeriodMins")) {
                  var3.setLogRotationPeriodMins(var4.getLogRotationPeriodMins());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("LogRotationTimeBegin")) {
                  var3.setLogRotationTimeBegin(var4.getLogRotationTimeBegin());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("LogRotationType")) {
                  var3.setLogRotationType(var4.getLogRotationType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("LogTimeInGMT")) {
                  var3.setLogTimeInGMT(var4.getLogTimeInGMT());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("MaxLogFileSizeKBytes")) {
                  var3.setMaxLogFileSizeKBytes(var4.getMaxLogFileSizeKBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("MaxPostSize")) {
                  var3.setMaxPostSize(var4.getMaxPostSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("MaxPostTimeSecs")) {
                  var3.setMaxPostTimeSecs(var4.getMaxPostTimeSecs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("OverloadResponseCode")) {
                  var3.setOverloadResponseCode(var4.getOverloadResponseCode());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("PostTimeoutSecs")) {
                  var3.setPostTimeoutSecs(var4.getPostTimeoutSecs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("URLResource")) {
                  var3.setURLResource(var4.getURLResource());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 34);
               } else if (var5.equals("WebDeployments")) {
                  var3.setWebDeploymentsAsString(var4.getWebDeploymentsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 44);
               } else if (var5.equals("WebServerLog")) {
                  if (var6 == 2) {
                     var3.setWebServerLog((WebServerLogMBean)this.createCopy((AbstractDescriptorBean)var4.getWebServerLog()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("WebServerLog", var3.getWebServerLog());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("WorkManagerForRemoteSessionFetching")) {
                  var3.setWorkManagerForRemoteSessionFetching(var4.getWorkManagerForRemoteSessionFetching());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 47);
               } else if (var5.equals("WriteChunkBytes")) {
                  var3.setWriteChunkBytes(var4.getWriteChunkBytes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 39);
               } else if (var5.equals("AcceptContextPathInGetRealPath")) {
                  var3.setAcceptContextPathInGetRealPath(var4.isAcceptContextPathInGetRealPath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 42);
               } else if (var5.equals("AuthCookieEnabled")) {
                  var3.setAuthCookieEnabled(var4.isAuthCookieEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 38);
               } else if (var5.equals("ChunkedTransferDisabled")) {
                  var3.setChunkedTransferDisabled(var4.isChunkedTransferDisabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 35);
               } else if (var5.equals("DebugEnabled")) {
                  var3.setDebugEnabled(var4.isDebugEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 40);
               } else if (var5.equals("KeepAliveEnabled")) {
                  var3.setKeepAliveEnabled(var4.isKeepAliveEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("LogFileLimitEnabled")) {
                  var3.setLogFileLimitEnabled(var4.isLogFileLimitEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 45);
               } else if (var5.equals("LoggingEnabled")) {
                  var3.setLoggingEnabled(var4.isLoggingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("SendServerHeaderEnabled")) {
                  var3.setSendServerHeaderEnabled(var4.isSendServerHeaderEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("SingleSignonDisabled")) {
                  var3.setSingleSignonDisabled(var4.isSingleSignonDisabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 43);
               } else if (var5.equals("UseHeaderEncoding")) {
                  var3.setUseHeaderEncoding(var4.isUseHeaderEncoding());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 37);
               } else if (var5.equals("UseHighestCompatibleHTTPVersion")) {
                  var3.setUseHighestCompatibleHTTPVersion(var4.isUseHighestCompatibleHTTPVersion());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 36);
               } else if (var5.equals("WAPEnabled")) {
                  var3.setWAPEnabled(var4.isWAPEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 41);
               } else {
                  super.applyPropertyUpdate(var1, var2);
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            WebServerMBeanImpl var5 = (WebServerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Charsets")) && this.bean.isCharsetsSet()) {
               var5.setCharsets(this.bean.getCharsets());
            }

            if ((var3 == null || !var3.contains("ClientIpHeader")) && this.bean.isClientIpHeaderSet()) {
               var5.setClientIpHeader(this.bean.getClientIpHeader());
            }

            if (var2 && (var3 == null || !var3.contains("DefaultWebApp")) && this.bean.isDefaultWebAppSet()) {
               var5._unSet(var5, 32);
               var5.setDefaultWebAppAsString(this.bean.getDefaultWebAppAsString());
            }

            if ((var3 == null || !var3.contains("DefaultWebAppContextRoot")) && this.bean.isDefaultWebAppContextRootSet()) {
               var5.setDefaultWebAppContextRoot(this.bean.getDefaultWebAppContextRoot());
            }

            if ((var3 == null || !var3.contains("FrontendHTTPPort")) && this.bean.isFrontendHTTPPortSet()) {
               var5.setFrontendHTTPPort(this.bean.getFrontendHTTPPort());
            }

            if ((var3 == null || !var3.contains("FrontendHTTPSPort")) && this.bean.isFrontendHTTPSPortSet()) {
               var5.setFrontendHTTPSPort(this.bean.getFrontendHTTPSPort());
            }

            if ((var3 == null || !var3.contains("FrontendHost")) && this.bean.isFrontendHostSet()) {
               var5.setFrontendHost(this.bean.getFrontendHost());
            }

            if ((var3 == null || !var3.contains("HttpsKeepAliveSecs")) && this.bean.isHttpsKeepAliveSecsSet()) {
               var5.setHttpsKeepAliveSecs(this.bean.getHttpsKeepAliveSecs());
            }

            if ((var3 == null || !var3.contains("KeepAliveSecs")) && this.bean.isKeepAliveSecsSet()) {
               var5.setKeepAliveSecs(this.bean.getKeepAliveSecs());
            }

            if (var2 && (var3 == null || !var3.contains("LogFileBufferKBytes")) && this.bean.isLogFileBufferKBytesSet()) {
               var5.setLogFileBufferKBytes(this.bean.getLogFileBufferKBytes());
            }

            if (var2 && (var3 == null || !var3.contains("LogFileCount")) && this.bean.isLogFileCountSet()) {
               var5.setLogFileCount(this.bean.getLogFileCount());
            }

            if (var2 && (var3 == null || !var3.contains("LogFileFlushSecs")) && this.bean.isLogFileFlushSecsSet()) {
               var5.setLogFileFlushSecs(this.bean.getLogFileFlushSecs());
            }

            if (var2 && (var3 == null || !var3.contains("LogFileFormat")) && this.bean.isLogFileFormatSet()) {
               var5.setLogFileFormat(this.bean.getLogFileFormat());
            }

            if (var2 && (var3 == null || !var3.contains("LogFileName")) && this.bean.isLogFileNameSet()) {
               var5.setLogFileName(this.bean.getLogFileName());
            }

            if (var2 && (var3 == null || !var3.contains("LogRotationPeriodMins")) && this.bean.isLogRotationPeriodMinsSet()) {
               var5.setLogRotationPeriodMins(this.bean.getLogRotationPeriodMins());
            }

            if (var2 && (var3 == null || !var3.contains("LogRotationTimeBegin")) && this.bean.isLogRotationTimeBeginSet()) {
               var5.setLogRotationTimeBegin(this.bean.getLogRotationTimeBegin());
            }

            if (var2 && (var3 == null || !var3.contains("LogRotationType")) && this.bean.isLogRotationTypeSet()) {
               var5.setLogRotationType(this.bean.getLogRotationType());
            }

            if (var2 && (var3 == null || !var3.contains("LogTimeInGMT")) && this.bean.isLogTimeInGMTSet()) {
               var5.setLogTimeInGMT(this.bean.getLogTimeInGMT());
            }

            if (var2 && (var3 == null || !var3.contains("MaxLogFileSizeKBytes")) && this.bean.isMaxLogFileSizeKBytesSet()) {
               var5.setMaxLogFileSizeKBytes(this.bean.getMaxLogFileSizeKBytes());
            }

            if ((var3 == null || !var3.contains("MaxPostSize")) && this.bean.isMaxPostSizeSet()) {
               var5.setMaxPostSize(this.bean.getMaxPostSize());
            }

            if ((var3 == null || !var3.contains("MaxPostTimeSecs")) && this.bean.isMaxPostTimeSecsSet()) {
               var5.setMaxPostTimeSecs(this.bean.getMaxPostTimeSecs());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("OverloadResponseCode")) && this.bean.isOverloadResponseCodeSet()) {
               var5.setOverloadResponseCode(this.bean.getOverloadResponseCode());
            }

            if ((var3 == null || !var3.contains("PostTimeoutSecs")) && this.bean.isPostTimeoutSecsSet()) {
               var5.setPostTimeoutSecs(this.bean.getPostTimeoutSecs());
            }

            if ((var3 == null || !var3.contains("URLResource")) && this.bean.isURLResourceSet()) {
               var5.setURLResource(this.bean.getURLResource());
            }

            if ((var3 == null || !var3.contains("WebDeployments")) && this.bean.isWebDeploymentsSet()) {
               var5._unSet(var5, 44);
               var5.setWebDeploymentsAsString(this.bean.getWebDeploymentsAsString());
            }

            if ((var3 == null || !var3.contains("WebServerLog")) && this.bean.isWebServerLogSet() && !var5._isSet(9)) {
               WebServerLogMBean var4 = this.bean.getWebServerLog();
               var5.setWebServerLog((WebServerLogMBean)null);
               var5.setWebServerLog(var4 == null ? null : (WebServerLogMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("WorkManagerForRemoteSessionFetching")) && this.bean.isWorkManagerForRemoteSessionFetchingSet()) {
               var5.setWorkManagerForRemoteSessionFetching(this.bean.getWorkManagerForRemoteSessionFetching());
            }

            if ((var3 == null || !var3.contains("WriteChunkBytes")) && this.bean.isWriteChunkBytesSet()) {
               var5.setWriteChunkBytes(this.bean.getWriteChunkBytes());
            }

            if ((var3 == null || !var3.contains("AcceptContextPathInGetRealPath")) && this.bean.isAcceptContextPathInGetRealPathSet()) {
               var5.setAcceptContextPathInGetRealPath(this.bean.isAcceptContextPathInGetRealPath());
            }

            if ((var3 == null || !var3.contains("AuthCookieEnabled")) && this.bean.isAuthCookieEnabledSet()) {
               var5.setAuthCookieEnabled(this.bean.isAuthCookieEnabled());
            }

            if ((var3 == null || !var3.contains("ChunkedTransferDisabled")) && this.bean.isChunkedTransferDisabledSet()) {
               var5.setChunkedTransferDisabled(this.bean.isChunkedTransferDisabled());
            }

            if (var2 && (var3 == null || !var3.contains("DebugEnabled")) && this.bean.isDebugEnabledSet()) {
               var5.setDebugEnabled(this.bean.isDebugEnabled());
            }

            if ((var3 == null || !var3.contains("KeepAliveEnabled")) && this.bean.isKeepAliveEnabledSet()) {
               var5.setKeepAliveEnabled(this.bean.isKeepAliveEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("LogFileLimitEnabled")) && this.bean.isLogFileLimitEnabledSet()) {
               var5.setLogFileLimitEnabled(this.bean.isLogFileLimitEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("LoggingEnabled")) && this.bean.isLoggingEnabledSet()) {
               var5.setLoggingEnabled(this.bean.isLoggingEnabled());
            }

            if ((var3 == null || !var3.contains("SendServerHeaderEnabled")) && this.bean.isSendServerHeaderEnabledSet()) {
               var5.setSendServerHeaderEnabled(this.bean.isSendServerHeaderEnabled());
            }

            if ((var3 == null || !var3.contains("SingleSignonDisabled")) && this.bean.isSingleSignonDisabledSet()) {
               var5.setSingleSignonDisabled(this.bean.isSingleSignonDisabled());
            }

            if ((var3 == null || !var3.contains("UseHeaderEncoding")) && this.bean.isUseHeaderEncodingSet()) {
               var5.setUseHeaderEncoding(this.bean.isUseHeaderEncoding());
            }

            if ((var3 == null || !var3.contains("UseHighestCompatibleHTTPVersion")) && this.bean.isUseHighestCompatibleHTTPVersionSet()) {
               var5.setUseHighestCompatibleHTTPVersion(this.bean.isUseHighestCompatibleHTTPVersion());
            }

            if ((var3 == null || !var3.contains("WAPEnabled")) && this.bean.isWAPEnabledSet()) {
               var5.setWAPEnabled(this.bean.isWAPEnabled());
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getDefaultWebApp(), var1, var2);
         this.inferSubTree(this.bean.getWebDeployments(), var1, var2);
         this.inferSubTree(this.bean.getWebServerLog(), var1, var2);
      }
   }
}
