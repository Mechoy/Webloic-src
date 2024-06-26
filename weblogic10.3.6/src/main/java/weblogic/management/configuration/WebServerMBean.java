package weblogic.management.configuration;

import java.util.Map;
import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

public interface WebServerMBean extends DeploymentMBean {
   String DEFAULT_LOG_FILE_NAME = "logs/access.log";
   String TIME_FORMAT = "MM-dd-yyyy-k:mm:ss";

   WebServerLogMBean getWebServerLog();

   /** @deprecated */
   void setLoggingEnabled(boolean var1);

   /** @deprecated */
   boolean isLoggingEnabled();

   /** @deprecated */
   String getLogFileFormat();

   /** @deprecated */
   void setLogFileFormat(String var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean getLogTimeInGMT();

   /** @deprecated */
   void setLogTimeInGMT(boolean var1);

   /** @deprecated */
   String getLogFileName();

   /** @deprecated */
   void setLogFileName(String var1) throws InvalidAttributeValueException;

   String getFrontendHost();

   void setFrontendHost(String var1) throws InvalidAttributeValueException;

   int getFrontendHTTPPort();

   void setFrontendHTTPPort(int var1) throws InvalidAttributeValueException;

   int getFrontendHTTPSPort();

   void setFrontendHTTPSPort(int var1) throws InvalidAttributeValueException;

   void setLogFileBufferKBytes(int var1) throws InvalidAttributeValueException;

   int getLogFileBufferKBytes();

   /** @deprecated */
   int getMaxLogFileSizeKBytes();

   /** @deprecated */
   void setMaxLogFileSizeKBytes(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getLogRotationType();

   /** @deprecated */
   void setLogRotationType(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getLogRotationPeriodMins();

   /** @deprecated */
   void setLogRotationPeriodMins(int var1) throws InvalidAttributeValueException;

   int getOverloadResponseCode();

   void setOverloadResponseCode(int var1);

   int getLogFileFlushSecs();

   void setLogFileFlushSecs(int var1) throws InvalidAttributeValueException;

   String getLogRotationTimeBegin();

   void setLogRotationTimeBegin(String var1) throws InvalidAttributeValueException;

   void setKeepAliveEnabled(boolean var1);

   boolean isKeepAliveEnabled();

   int getKeepAliveSecs();

   void setKeepAliveSecs(int var1) throws InvalidAttributeValueException;

   int getHttpsKeepAliveSecs();

   void setHttpsKeepAliveSecs(int var1) throws InvalidAttributeValueException;

   void setPostTimeoutSecs(int var1) throws InvalidAttributeValueException;

   int getPostTimeoutSecs();

   boolean isPostTimeoutSecsSet();

   void setMaxPostTimeSecs(int var1) throws InvalidAttributeValueException;

   int getMaxPostTimeSecs();

   boolean isMaxPostTimeSecsSet();

   void setMaxPostSize(int var1) throws InvalidAttributeValueException;

   int getMaxPostSize();

   boolean isMaxPostSizeSet();

   void setSendServerHeaderEnabled(boolean var1);

   boolean isSendServerHeaderEnabled();

   String getDefaultWebAppContextRoot();

   void setDefaultWebAppContextRoot(String var1);

   WebAppComponentMBean getDefaultWebApp();

   void setDefaultWebApp(WebAppComponentMBean var1);

   void setCharsets(Map var1) throws InvalidAttributeValueException;

   Map getCharsets();

   void setURLResource(Map var1) throws InvalidAttributeValueException;

   Map getURLResource();

   void setChunkedTransferDisabled(boolean var1);

   boolean isChunkedTransferDisabled();

   void setUseHighestCompatibleHTTPVersion(boolean var1);

   boolean isUseHighestCompatibleHTTPVersion();

   void setUseHeaderEncoding(boolean var1);

   boolean isUseHeaderEncoding();

   void setAuthCookieEnabled(boolean var1);

   boolean isAuthCookieEnabled();

   void setWriteChunkBytes(int var1) throws InvalidAttributeValueException;

   int getWriteChunkBytes();

   /** @deprecated */
   void setDebugEnabled(boolean var1);

   /** @deprecated */
   boolean isDebugEnabled();

   void setWAPEnabled(boolean var1);

   boolean isWAPEnabled();

   void setAcceptContextPathInGetRealPath(boolean var1);

   boolean isAcceptContextPathInGetRealPath();

   void setSingleSignonDisabled(boolean var1);

   boolean isSingleSignonDisabled();

   WebDeploymentMBean[] getWebDeployments();

   void setWebDeployments(WebDeploymentMBean[] var1) throws DistributedManagementException;

   /** @deprecated */
   boolean isLogFileLimitEnabled();

   /** @deprecated */
   void setLogFileLimitEnabled(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getLogFileCount();

   /** @deprecated */
   void setLogFileCount(int var1) throws InvalidAttributeValueException;

   boolean addWebDeployment(WebDeploymentMBean var1) throws DistributedManagementException;

   boolean removeWebDeployment(WebDeploymentMBean var1) throws DistributedManagementException;

   void setWorkManagerForRemoteSessionFetching(String var1);

   String getWorkManagerForRemoteSessionFetching();

   void setClientIpHeader(String var1);

   String getClientIpHeader();
}
