package weblogic.deploy.service.internal.transport.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import javax.mail.internet.MimeUtility;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.deploy.common.Debug;
import weblogic.deploy.common.DeploymentObjectInputStream;
import weblogic.deploy.service.internal.transport.DeploymentServiceMessage;
import weblogic.deploy.service.internal.transport.MessageReceiver;
import weblogic.deploy.service.internal.transport.MessageSender;
import weblogic.deploy.service.internal.transport.UnreachableHostException;
import weblogic.deploy.utils.DeploymentServletConstants;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.PropertyService;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.protocol.URLManager;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.StringUtils;

public final class HTTPMessageSender implements MessageSender, DeploymentServletConstants, DeploymentServiceConstants {
   private static final String DEPLOYMENT_APPNAME = "bea_wls_deployment_internal";
   private static final HTTPMessageSender SINGLETON = new HTTPMessageSender();
   private String userName;
   private String password;
   private MessageReceiver loopbackReceiver;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private HTTPMessageSender() {
      if (isDebugEnabled()) {
         debug("Created HTTPMessageSender");
      }

   }

   private static final boolean isDebugEnabled() {
      return Debug.isServiceHttpDebugEnabled();
   }

   private static final void debug(String var0) {
      Debug.serviceHttpDebug(var0);
   }

   private String getUserName() {
      if (this.userName == null) {
         this.userName = ManagementService.getPropertyService(kernelId).getTimestamp1();
      }

      return this.userName;
   }

   private String getUserCredential() {
      if (this.password == null) {
         this.password = ManagementService.getPropertyService(kernelId).getTimestamp2();
      }

      return this.password;
   }

   private static String getAdminUrl() {
      ManagementService.getPropertyService(kernelId);
      return PropertyService.getAdminHttpUrl();
   }

   private static String[] getAllAdminUrls() {
      return PropertyService.getAllAdminHttpUrls();
   }

   public static HTTPMessageSender getMessageSender() {
      return SINGLETON;
   }

   public void sendHeartbeatMessage(DeploymentServiceMessage var1, String var2) throws Exception {
      try {
         String var3 = getURL(var2);
         this.sendMessageToServerURL(var1, var3, var2, false, 60000);
      } catch (UnreachableHostException var4) {
         if (isDebugEnabled()) {
            debug(" UnreachableHost : " + StackTraceUtils.throwable2StackTrace(var4));
         }
      }

   }

   public void sendMessageToTargetServer(DeploymentServiceMessage var1, String var2) throws RemoteException {
      String var3 = getURL(var2);
      this.sendMessageToServerURL(var1, var3, var2, true, 0);
   }

   public void sendMessageToAdminServer(DeploymentServiceMessage var1) throws RemoteException {
      Object var3 = null;
      String var4 = null;
      if (!isAdminServer()) {
         var4 = getAdminUrl();
      }

      try {
         this.sendMessageToServerURL(var1, var4, (String)var3, true, 0);
      } catch (UnreachableHostException var6) {
         this.sendMessageToAdminServerViaAlternateUrls(var4, var1, true);
      }

   }

   private DeploymentServiceMessage sendMessageToAdminServerViaAlternateUrls(String var1, DeploymentServiceMessage var2, boolean var3) throws RemoteException {
      String[] var4 = getAllAdminUrls();
      int var5 = 0;
      Object var6 = null;
      UnreachableHostException var7 = null;

      while(true) {
         String var8;
         do {
            if (var5 >= var4.length) {
               if (var7 != null) {
                  throw var7;
               }

               var8 = DeployerRuntimeLogger.adminUnreachable(StringUtils.join(var4, ","));
               throw new RemoteException(var8);
            }

            var8 = var4[var5];
            ++var5;
         } while(var1 != null && var1.equals(var8));

         try {
            if (isDebugEnabled()) {
               debug("Retrying attempt to send to admin server via alternate url '" + var8);
            }

            return this.sendMessageToServerURL(var2, var8, (String)var6, var3, 0);
         } catch (UnreachableHostException var11) {
            if (var7 != null) {
               var7 = var11;
            }
         } catch (RemoteException var12) {
            String var10 = DeployerRuntimeLogger.altURLFailed(var8);
            throw new RemoteException(var10);
         }
      }
   }

   public DeploymentServiceMessage sendBlockingMessageToAdminServer(DeploymentServiceMessage var1) throws RemoteException {
      Object var3 = null;
      String var4 = null;
      if (!isAdminServer()) {
         var4 = getAdminUrl();
      }

      try {
         return this.sendMessageToServerURL(var1, var4, (String)var3, true, 0);
      } catch (UnreachableHostException var6) {
         return this.sendMessageToAdminServerViaAlternateUrls(var4, var1, true);
      }
   }

   public void setLoopbackReceiver(MessageReceiver var1) {
      if (this.loopbackReceiver == null) {
         this.loopbackReceiver = var1;
      }

      if (isDebugEnabled()) {
         debug("setting loopback messageReceiver to '" + var1 + "'");
      }

   }

   private DeploymentServiceMessage sendMessageToServerURL(DeploymentServiceMessage var1, String var2, String var3, boolean var4, int var5) throws RemoteException {
      boolean var6 = false;
      if (isAdminServer() && (var3 == null || var3.equals(ManagementService.getRuntimeAccess(kernelId).getServerName()))) {
         var6 = true;
         var3 = "loopback admin";
      }

      if (isDebugEnabled()) {
         debug("sending message for id '" + var1.getDeploymentId() + "' to '" + var3 + "' using URL '" + var2 + "' via http");
      }

      if (var6) {
         try {
            if (var4) {
               return this.loopbackReceiver.receiveSynchronousMessage(var1);
            } else {
               this.loopbackReceiver.receiveMessage(var1);
               return null;
            }
         } catch (Exception var27) {
            String var36 = DeployerRuntimeLogger.errorReceivingMessage();
            throw new RemoteException(var36, var27);
         }
      } else {
         URL var7;
         try {
            var7 = new URL(var2 + "/" + "bea_wls_deployment_internal" + "/DeploymentService");
         } catch (MalformedURLException var28) {
            String var9 = DeployerRuntimeLogger.malformedURL(var2);
            throw new RemoteException(var9, var28);
         }

         int var8 = -1;
         HttpURLConnection var37 = null;

         label286: {
            DeploymentServiceMessage var16;
            try {
               var37 = URLManager.createAdminHttpConnection(var7);
               var37.setRequestProperty("wl_request_type", mimeEncode("deployment_svc_msg"));
               if (ManagementService.isRuntimeAccessInitialized()) {
                  ConnectionSigner.signConnection(var37, kernelId);
               } else {
                  var37.setRequestProperty("username", mimeEncode(this.getUserName()));
                  var37.setRequestProperty("password", mimeEncode(this.getUserCredential()));
               }

               var37.setRequestProperty("serverName", mimeEncode(ManagementService.getPropertyService(kernelId).getServerName()));
               var37.setRequestProperty("server_version", PeerInfo.getPeerInfo().getVersionAsString());
               var37.setRequestProperty("isSynchronous", mimeEncode(Boolean.valueOf(var4).toString()));
               var37.setRequestProperty("deployment_request_id", Long.toString(var1.getDeploymentId()));
               var37.setRequestProperty("Content-Type", "application/x-java-serialized-object");
               var37.setDoInput(true);
               var37.setDoOutput(true);
               if (var5 > 0) {
                  var37.setConnectTimeout(var5);
                  var37.setReadTimeout(var5);
               }

               ByteArrayOutputStream var10 = new ByteArrayOutputStream();
               WLObjectOutputStream var38 = new WLObjectOutputStream(var10);
               var38.setReplacer(RemoteObjectReplacer.getReplacer());
               var38.writeObject(var1);
               var38.flush();
               var37.setRequestProperty("Content-Length", "" + var10.size());
               OutputStream var12 = var37.getOutputStream();
               var10.writeTo(var12);
               var12.flush();
               var12.close();
               if (!var4) {
                  break label286;
               }

               var8 = var37.getResponseCode();
               if (var8 != 200) {
                  StringBuffer var39 = new StringBuffer();
                  var39.append(DeployerRuntimeLogger.errorReadingInput());
                  var39.append(" : with response code '").append(var8).append("'");
                  var39.append(" : with response message '");
                  var39.append(var37.getResponseMessage()).append("'");
                  if (isServerStandby(var3)) {
                     throw new ConnectException(var39.toString());
                  }

                  throw new RemoteException(var39.toString());
               }

               String var13 = var37.getHeaderField("server_version");
               String var14 = var13 != null && var13.length() != 0 ? var13 : PeerInfo.getPeerInfo().getVersionAsString();
               if (isDebugEnabled()) {
                  debug("Response Peer Version: " + var14);
               }

               DeploymentObjectInputStream var15 = new DeploymentObjectInputStream(var37.getInputStream(), var14);
               var16 = (DeploymentServiceMessage)var15.readObject();
            } catch (ConnectException var29) {
               throw new UnreachableHostException(var3, var29);
            } catch (UnknownHostException var30) {
               throw new UnreachableHostException(var3, var30);
            } catch (IOException var31) {
               if (var31 instanceof RemoteException) {
                  throw (RemoteException)var31;
               }

               boolean var35 = true;
               if (isDebugEnabled()) {
                  debug("HTTPMessageSender: IOException: " + var31.toString() + " when making a DeploymentServiceMsg request to " + "URL: " + var7.toString());
                  debug("Exception StackTrace: " + StackTraceUtils.throwable2StackTrace(var31));
               }

               String var11 = DeployerRuntimeLogger.errorReadingInput();
               throw new RemoteException(var11, var31);
            } catch (ClassNotFoundException var32) {
               if (isDebugEnabled()) {
                  debug("HTTPMessageSender: ClassNotFoundException: " + var32.toString() + " when making a DeploymentServiceMsg request to " + "URL: " + var7.toString());
               }

               throw new RemoteException(var32.toString(), var32);
            } catch (Throwable var33) {
               if (isDebugEnabled()) {
                  debug("HTTPMessageSender: Throwable: " + StackTraceUtils.throwable2StackTrace(var33) + " when making a DeploymentServiceMsg request to " + "URL: " + var7.toString());
               }

               throw new RemoteException(var33.toString(), var33);
            } finally {
               if (var37 != null) {
                  var37.disconnect();
               }

            }

            return var16;
         }

         if (var8 == 404) {
            Debug.serviceHttpDebug("HTTPMessageSender: HTTP_NOT_FOUND error when making a DeploymentServiceMsg request to URL: " + var7.toString());
         } else if (var8 == 401) {
            Debug.serviceHttpDebug("HTTPMessageSender: HTTP_UNAUTHORIZED error when making a DeploymentServiceMsg request to URL: " + var7.toString());
         } else if (var8 != 500 && var8 != 409) {
            if (var8 == 503) {
               Debug.serviceHttpDebug("HTTPMessageSender: HTTP_UNAVAILABLE error when making a DeploymentServiceMsg request to URL: " + var7.toString());
            }
         } else {
            Debug.serviceHttpDebug("HTTPMessageSender: " + (var8 == 500 ? "HTTP_INTERNAL_ERROR" : "HTTP_CONFLICT") + "error when making a DeploymentServiceMsg request to " + "URL: " + var7.toString());
         }

         return null;
      }
   }

   private static boolean isAdminServer() {
      return ManagementService.getPropertyService(kernelId).isAdminServer();
   }

   private static String getURL(String var0) throws UnreachableHostException {
      try {
         String var1 = URLManager.findAdministrationURL(var0);
         var1 = URLManager.normalizeToHttpProtocol(var1);
         if (var1 == null) {
            throw new UnreachableHostException(var0, (Exception)null);
         } else {
            return var1;
         }
      } catch (UnknownHostException var2) {
         throw new UnreachableHostException(var0, var2);
      }
   }

   private static boolean isServerStandby(String var0) {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      ServerMBean var2 = var1.lookupServer(var0);
      return var2 != null && "STANDBY".equals(var2.getStartupMode());
   }

   private static String mimeDecode(String var0) {
      String var1 = null;

      try {
         var1 = MimeUtility.decodeText(var0);
      } catch (UnsupportedEncodingException var3) {
         var1 = var0;
      }

      return var1;
   }

   private static String mimeEncode(String var0) {
      String var1 = null;

      try {
         var1 = MimeUtility.encodeText(var0, "UTF-8", (String)null);
      } catch (UnsupportedEncodingException var3) {
         var1 = var0;
      }

      return var1;
   }
}
