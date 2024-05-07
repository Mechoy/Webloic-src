package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WebServerMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private WebServerMBeanImpl bean;

   protected WebServerMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WebServerMBeanImpl)var1;
   }

   public WebServerMBeanBinder() {
      super(new WebServerMBeanImpl());
      this.bean = (WebServerMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Charsets")) {
                  this.bean.setCharsetsAsString((String)var2);
               } else if (var1.equals("ClientIpHeader")) {
                  try {
                     this.bean.setClientIpHeader((String)var2);
                  } catch (BeanAlreadyExistsException var41) {
                     System.out.println("Warning: multiple definitions with same name: " + var41.getMessage());
                  }
               } else if (var1.equals("DefaultWebApp")) {
                  this.bean.setDefaultWebAppAsString((String)var2);
               } else if (var1.equals("DefaultWebAppContextRoot")) {
                  try {
                     this.bean.setDefaultWebAppContextRoot((String)var2);
                  } catch (BeanAlreadyExistsException var40) {
                     System.out.println("Warning: multiple definitions with same name: " + var40.getMessage());
                  }
               } else if (var1.equals("FrontendHTTPPort")) {
                  try {
                     this.bean.setFrontendHTTPPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var39) {
                     System.out.println("Warning: multiple definitions with same name: " + var39.getMessage());
                  }
               } else if (var1.equals("FrontendHTTPSPort")) {
                  try {
                     this.bean.setFrontendHTTPSPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var38) {
                     System.out.println("Warning: multiple definitions with same name: " + var38.getMessage());
                  }
               } else if (var1.equals("FrontendHost")) {
                  try {
                     this.bean.setFrontendHost((String)var2);
                  } catch (BeanAlreadyExistsException var37) {
                     System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                  }
               } else if (var1.equals("HttpsKeepAliveSecs")) {
                  try {
                     this.bean.setHttpsKeepAliveSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var36) {
                     System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                  }
               } else if (var1.equals("KeepAliveSecs")) {
                  try {
                     this.bean.setKeepAliveSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                  }
               } else if (var1.equals("LogFileBufferKBytes")) {
                  try {
                     this.bean.setLogFileBufferKBytes(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                  }
               } else if (var1.equals("LogFileCount")) {
                  this.handleDeprecatedProperty("LogFileCount", "9.0.0.0 Use getWebServerLog().getFileCount()");

                  try {
                     this.bean.setLogFileCount(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var33) {
                     System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                  }
               } else if (var1.equals("LogFileFlushSecs")) {
                  try {
                     this.bean.setLogFileFlushSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var32) {
                     System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                  }
               } else if (var1.equals("LogFileFormat")) {
                  this.handleDeprecatedProperty("LogFileFormat", "9.0.0.0 Use getWebServerLog().getLogFileFormat().");

                  try {
                     this.bean.setLogFileFormat((String)var2);
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("LogFileName")) {
                  this.handleDeprecatedProperty("LogFileName", "9.0.0.0 Use getWebServerLog().getFileName()");

                  try {
                     this.bean.setLogFileName((String)var2);
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("LogRotationPeriodMins")) {
                  this.handleDeprecatedProperty("LogRotationPeriodMins", "9.0.0.0 Use getWebServerLog().getFileTimeSpan() (hours)");

                  try {
                     this.bean.setLogRotationPeriodMins(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("LogRotationTimeBegin")) {
                  try {
                     this.bean.setLogRotationTimeBegin((String)var2);
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("LogRotationType")) {
                  this.handleDeprecatedProperty("LogRotationType", "9.0.0.0 Use getWebServerLog().getRotationType()");

                  try {
                     this.bean.setLogRotationType((String)var2);
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("LogTimeInGMT")) {
                  this.handleDeprecatedProperty("LogTimeInGMT", "9.0.0.0 Use getWebServerLog().getLogTimeInGMT().");

                  try {
                     this.bean.setLogTimeInGMT(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("MaxLogFileSizeKBytes")) {
                  this.handleDeprecatedProperty("MaxLogFileSizeKBytes", "9.0.0.0 Use getWebServerLog().getFileMinSize()");

                  try {
                     this.bean.setMaxLogFileSizeKBytes(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("MaxPostSize")) {
                  try {
                     this.bean.setMaxPostSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("MaxPostTimeSecs")) {
                  try {
                     this.bean.setMaxPostTimeSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("OverloadResponseCode")) {
                  try {
                     this.bean.setOverloadResponseCode(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("PostTimeoutSecs")) {
                  try {
                     this.bean.setPostTimeoutSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("URLResource")) {
                  this.bean.setURLResourceAsString((String)var2);
               } else if (var1.equals("WebDeployments")) {
                  this.bean.setWebDeploymentsAsString((String)var2);
               } else if (var1.equals("WebServerLog")) {
                  try {
                     this.bean.setWebServerLog((WebServerLogMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("WorkManagerForRemoteSessionFetching")) {
                  try {
                     this.bean.setWorkManagerForRemoteSessionFetching((String)var2);
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("WriteChunkBytes")) {
                  try {
                     this.bean.setWriteChunkBytes(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("AcceptContextPathInGetRealPath")) {
                  try {
                     this.bean.setAcceptContextPathInGetRealPath(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("AuthCookieEnabled")) {
                  try {
                     this.bean.setAuthCookieEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("ChunkedTransferDisabled")) {
                  try {
                     this.bean.setChunkedTransferDisabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("DebugEnabled")) {
                  this.handleDeprecatedProperty("DebugEnabled", "9.0.0.0 use the ServerDebugMBean");

                  try {
                     this.bean.setDebugEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("KeepAliveEnabled")) {
                  try {
                     this.bean.setKeepAliveEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("LogFileLimitEnabled")) {
                  this.handleDeprecatedProperty("LogFileLimitEnabled", "9.0.0.0 Use getWebServerLog().getNumberOfFilesLimited()");

                  try {
                     this.bean.setLogFileLimitEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("LoggingEnabled")) {
                  this.handleDeprecatedProperty("LoggingEnabled", "9.0.0.0 Use getWebServerLog().isLoggingEnabled().");

                  try {
                     this.bean.setLoggingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("SendServerHeaderEnabled")) {
                  try {
                     this.bean.setSendServerHeaderEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("SingleSignonDisabled")) {
                  try {
                     this.bean.setSingleSignonDisabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("UseHeaderEncoding")) {
                  try {
                     this.bean.setUseHeaderEncoding(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("UseHighestCompatibleHTTPVersion")) {
                  try {
                     this.bean.setUseHighestCompatibleHTTPVersion(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("WAPEnabled")) {
                  try {
                     this.bean.setWAPEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var42) {
         System.out.println(var42 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var42;
      } catch (RuntimeException var43) {
         throw var43;
      } catch (Exception var44) {
         if (var44 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var44);
         } else if (var44 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var44.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var44);
         }
      }
   }
}
