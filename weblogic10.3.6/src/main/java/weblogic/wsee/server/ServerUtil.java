package weblogic.wsee.server;

import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import weblogic.jws.BufferQueue;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.WebServerMBean;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.URLManager;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.util.StringUtil;

public class ServerUtil {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String JMS_PROXY_HOST = "weblogic.jms.proxyHost";
   private static final String JMS_PROXY_PORT = "weblogic.jms.proxyPort";
   private static final String HTTP_PROXY_HOST = "http.proxyHost";
   private static final String HTTP_PROXY_PORT = "http.proxyPort";
   private static final String HTTPS_PROXY_HOST = "https.proxyHost";
   private static final String HTTPS_PROXY_PORT = "https.proxyPort";
   private static final int PROTOCOL_JMS = 0;
   private static final int PROTOCOL_HTTP = 1;
   private static final int PROTOCOL_HTTPS = 2;
   private static ServerURLEntry[] SERVER_URLS = new ServerURLEntry[3];
   public static final boolean useReqHostAlways;

   public static String getCallbackServerURL(String var0, String var1, String var2, String var3) throws ServerURLNotFoundException {
      return getServerURL(var0);
   }

   public static String getServerURL(String var0) throws ServerURLNotFoundException {
      if (var0 == null) {
         throw new ServerURLNotFoundException("No protocol was provided");
      } else {
         byte var1 = -1;
         if (var0.equals("jms")) {
            var1 = 0;
         } else if (var0.equals("http")) {
            var1 = 1;
         } else if (var0.equals("https")) {
            var1 = 2;
         }

         if (var1 == -1) {
            throw new ServerURLNotFoundException("Protocol " + var0 + " is not supported");
         } else {
            ServerURLEntry var2 = SERVER_URLS[var1];
            if (var2.isExpired()) {
               synchronized(var2) {
                  if (var2.isExpired()) {
                     var2.setValue(getServerURL(var1));
                  }
               }
            }

            return var2.getValue();
         }
      }
   }

   public static String getServerURL(int var0) throws ServerURLNotFoundException {
      if (var0 == 0) {
         return getJMSServerURL();
      } else if (var0 == 1) {
         return getHTTPServerURL(false);
      } else {
         return var0 == 2 ? getHTTPServerURL(true) : null;
      }
   }

   public static String getHTTPServerURL(boolean var0, HttpServletRequest var1) throws ServerURLNotFoundException {
      String var2 = null;
      if (useReqHostAlways) {
         var2 = formURL(var1.getScheme(), var1.getServerName(), var1.getServerPort());
      }

      if (var2 == null) {
         var2 = getWseeProxyChannelURL(var0 ? "https" : "http");
      }

      if (var2 == null) {
         var2 = getFrontEndServerAddress(var0);
      }

      if (var2 == null) {
         var2 = getClusterAddress(var0 ? "https" : "http");
      }

      if (var2 == null) {
         var2 = formURL(var1.getScheme(), var1.getServerName(), var1.getServerPort());
      }

      if (var2 == null) {
         throw new ServerURLNotFoundException("Cannot resolve URL for protocol http/https");
      } else {
         return var2;
      }
   }

   public static String getHTTPServerURL(boolean var0) throws ServerURLNotFoundException {
      String var1 = getWseeProxyChannelURL(var0 ? "https" : "http");
      if (var1 == null) {
         var1 = getFrontEndServerAddress(var0);
      }

      if (var1 == null) {
         var1 = getClusterAddress(var0 ? "https" : "http");
      }

      if (var1 == null) {
         var1 = getLocalServerPublicURL(var0 ? "https" : "http");
      }

      if (var1 == null) {
         throw new ServerURLNotFoundException("Cannot resolve URL for protocol http/https");
      } else {
         return var1;
      }
   }

   public static String getJMSServerURL() {
      String var0 = getClusterAddress("t3");
      if (var0 == null) {
         var0 = getLocalServerPublicURL("t3");
      }

      String var1 = swapProtocol(var0, "jms");
      if (var1 == null) {
         throw new ServerURLNotFoundException("Cannot resolve URL for protocol jms");
      } else {
         return var1;
      }
   }

   private static String getFirstURL(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf(44);
         if (var1 <= 0) {
            return var0;
         } else {
            String var2 = var0.substring(0, var1);
            int var3 = var0.lastIndexOf(58);
            return var2.indexOf(58, 6) < 0 && var3 > 0 ? var2 + var0.substring(var3) : var2;
         }
      }
   }

   private static String getProtocol(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf(58);
         return var1 <= 0 ? null : var0.substring(0, var1);
      }
   }

   private static String getSystemPropURL(String var0) {
      String var1 = var0.toLowerCase(Locale.ENGLISH);
      String var2 = null;
      String var3 = null;
      String var4 = null;
      if (var1.equals("jms")) {
         var3 = System.getProperty("weblogic.jms.proxyHost");
         var4 = System.getProperty("weblogic.jms.proxyPort");
         if (var3 != null && var4 != null) {
            var2 = "jms://" + var3 + ":" + var4;
         }
      }

      if (var1.equals("http")) {
         var3 = System.getProperty("http.proxyHost");
         var4 = System.getProperty("http.proxyPort");
         if (var3 != null && var4 != null) {
            var2 = "http://" + var3 + ":" + var4;
         }
      }

      if (var1.equals("https")) {
         var3 = System.getProperty("https.proxyHost");
         var4 = System.getProperty("https.proxyPort");
         if (var3 != null && var4 != null) {
            var2 = "https://" + var3 + ":" + var4;
         }
      }

      return var2;
   }

   private static String getWseeProxyChannelURL(String var0) {
      String var1 = System.getProperty("weblogic.wsee.proxy.address");
      if (var1 != null) {
         return var1;
      } else {
         String var2 = var0.toLowerCase(Locale.ENGLISH);
         ServerChannel var3 = ServerChannelManager.findLocalServerChannel("weblogic-wsee-proxy-channel-" + var2);
         if (var3 != null) {
            String var4 = var3.getPublicAddress();
            if (var4 == null) {
               throw new RuntimeException("Public address for the proxy channel is not set");
            } else if (!var0.equalsIgnoreCase(var3.getProtocolPrefix())) {
               throw new RuntimeException("weblogic-wsee-proxy-channel-" + var2 + " should have the protocol be " + var2);
            } else {
               return formURL(var2, var4, var3.getPublicPort());
            }
         } else {
            return null;
         }
      }
   }

   public static String swapProtocol(String var0, String var1) {
      if (var0 != null && var1 != null) {
         int var2 = var0.indexOf("://");
         return var2 <= 0 ? null : var1 + var0.substring(var2, var0.length());
      } else {
         return null;
      }
   }

   public static String getServerName() {
      return ManagementService.getRuntimeAccess(kernelId).getServerName();
   }

   public static String getDomainName() {
      return ManagementService.getRuntimeAccess(kernelId).getDomainName();
   }

   public static String getLocalServerPublicURL(String var0) throws ServerURLNotFoundException {
      String var1 = getServerName();
      Protocol var2 = ProtocolManager.getProtocolByName(var0);

      try {
         return URLManager.findURL(var1, var2);
      } catch (UnknownHostException var4) {
         throw new ServerURLNotFoundException(var4);
      }
   }

   private static final String getFrontEndServerAddress(boolean var0) {
      String var1 = null;
      int var2 = 0;
      ServerMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServer();
      ClusterMBean var4 = var3.getCluster();
      if (var4 != null) {
         var1 = var4.getFrontendHost();
         if (var1 != null) {
            if (var0) {
               var2 = var4.getFrontendHTTPSPort();
               if (var2 == 0) {
                  var2 = var4.getFrontendHTTPPort();
                  var0 = false;
               }
            } else {
               var2 = var4.getFrontendHTTPPort();
               if (var2 == 0) {
                  var2 = var4.getFrontendHTTPSPort();
                  var0 = true;
               }
            }
         }
      }

      if (var1 == null) {
         WebServerMBean var5 = var3.getWebServer();
         if (var5 != null) {
            var1 = var5.getFrontendHost();
            if (var1 != null) {
               if (var0) {
                  var2 = var5.getFrontendHTTPSPort();
                  if (var2 == 0) {
                     var2 = var5.getFrontendHTTPPort();
                     var0 = false;
                  }
               } else {
                  var2 = var5.getFrontendHTTPPort();
                  if (var2 == 0) {
                     var2 = var5.getFrontendHTTPSPort();
                     var0 = true;
                  }
               }
            }
         }
      }

      if (var1 != null) {
         if (var2 == 0) {
            throw new IllegalArgumentException("FrontendHTTPPort or FrontendHTTPSPort must be set for " + var1);
         } else {
            return var0 ? formURL("https", var1, var2) : formURL("http", var1, var2);
         }
      } else {
         return null;
      }
   }

   private static String formURL(String var0, String var1, int var2) {
      return var0 + "://" + var1 + ":" + var2;
   }

   private static String getClusterAddress(String var0) {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      ClusterMBean var2 = var1.getCluster();
      if (var2 == null) {
         return null;
      } else {
         String var3 = var2.getClusterAddress();
         if (var3 != null && var3.length() != 0) {
            ServerChannel var4 = ServerChannelManager.findLocalServerChannel(ProtocolManager.getProtocolByName(var0));
            if (var4 == null) {
               return null;
            } else {
               String var5 = ChannelHelper.createClusterURL(var4);
               return var5 != null ? getFirstURL(var5) : null;
            }
         } else {
            throw new IllegalArgumentException("Cluster address must be set when clustering is enabled.");
         }
      }
   }

   public static WebServiceMBean getWebServiceMBean() {
      ServerMBean var0 = ManagementService.getRuntimeAccess(kernelId).getServer();
      return var0.getWebService();
   }

   public static String getCallbackQueue() {
      WebServiceMBean var0 = getWebServiceMBean();
      return var0 != null ? var0.getCallbackQueue() : "weblogic.wsee.DefaultCallbackQueue";
   }

   public static QueueInfo getCallbackQueueInfo() {
      String var0 = getCallbackQueue();
      WebServiceMBean var1 = getWebServiceMBean();
      String var2 = var1 != null ? var1.getCallbackQueueMDBRunAsPrincipalName() : null;
      return new QueueInfo(var0, var2);
   }

   public static String getMessagingQueue() {
      WebServiceMBean var0 = getWebServiceMBean();
      return var0 != null ? var0.getMessagingQueue() : "weblogic.wsee.DefaultQueue";
   }

   public static QueueInfo getMessagingQueueInfo() {
      String var0 = getMessagingQueue();
      WebServiceMBean var1 = getWebServiceMBean();
      String var2 = var1 != null ? var1.getMessagingQueueMDBRunAsPrincipalName() : null;
      return new QueueInfo(var0, var2);
   }

   public static QueueInfo getBufferQueueInfo(String var0) {
      QueueInfo var1 = getMessagingQueueInfo();
      if (var0 != null && !StringUtil.isEmpty(var0)) {
         return var0.equals("@weblogic.wsee.CallbackQueue@") ? getCallbackQueueInfo() : new QueueInfo(var0, var1.getMdbRunAsPrincipalName());
      } else {
         return var1;
      }
   }

   public static QueueInfo getBufferQueueInfo(Class var0) {
      QueueInfo var1 = getMessagingQueueInfo();
      if (var0 != null) {
         BufferQueue var2 = (BufferQueue)var0.getAnnotation(BufferQueue.class);
         if (var2 != null && !StringUtil.isEmpty(var2.name())) {
            return var2.name().equals("@weblogic.wsee.CallbackQueue@") ? getCallbackQueueInfo() : new QueueInfo(var2.name(), var1.getMdbRunAsPrincipalName());
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static String getJmsConnectionFactory() {
      WebServiceMBean var0 = getWebServiceMBean();
      return var0 != null ? var0.getJmsConnectionFactory() : "weblogic.jms.XAConnectionFactory";
   }

   static {
      for(int var0 = 0; var0 < SERVER_URLS.length; ++var0) {
         SERVER_URLS[var0] = new ServerURLEntry();
      }

      useReqHostAlways = Boolean.getBoolean("weblogic.wsee.useRequestHost");
   }

   private static class ServerURLEntry {
      private long _cacheTime;
      private String value;
      private static final int MAX_TIME = 3000;

      public ServerURLEntry() {
      }

      public String getValue() {
         return this.value;
      }

      public void setValue(String var1) {
         this.value = var1;
         this._cacheTime = System.currentTimeMillis();
      }

      public boolean isExpired() {
         return System.currentTimeMillis() - this._cacheTime > 3000L;
      }
   }

   public static class QueueInfo {
      private String queueName;
      private String mdbRunAsPrincipalName;

      public QueueInfo(String var1, String var2) {
         this.queueName = var1;
         this.mdbRunAsPrincipalName = var2;
      }

      public String getQueueName() {
         return this.queueName;
      }

      public String getMdbRunAsPrincipalName() {
         return this.mdbRunAsPrincipalName;
      }
   }
}
