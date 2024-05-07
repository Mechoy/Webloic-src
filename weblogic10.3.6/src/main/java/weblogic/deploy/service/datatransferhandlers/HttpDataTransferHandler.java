package weblogic.deploy.service.datatransferhandlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.mail.internet.MimeUtility;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.DataTransferHandler;
import weblogic.deploy.service.DataTransferRequest;
import weblogic.deploy.service.MultiDataStream;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.transport.http.DeploymentServiceServlet;
import weblogic.deploy.utils.DeploymentServletConstants;
import weblogic.logging.Loggable;
import weblogic.management.provider.ManagementService;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.protocol.URLManager;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public class HttpDataTransferHandler implements DataTransferHandler, DeploymentServletConstants {
   private static final AuthenticatedSubject KERNE_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean gotSecret = false;
   private String userName;
   private String password;

   protected HttpDataTransferHandler() {
   }

   public final String getType() {
      return "HTTP";
   }

   public MultiDataStream getDataAsStream(DataTransferRequest var1) throws IOException {
      DataHandlerManager.validateRequestType(var1);
      HttpURLConnection var2 = null;

      MultiDataStream var9;
      try {
         var2 = this.createURLConnection();
         var2.setRequestProperty("deployment_request_id", Long.toString(var1.getRequestId()));
         UnsyncByteArrayOutputStream var3 = new UnsyncByteArrayOutputStream();
         WLObjectOutputStream var20 = new WLObjectOutputStream(var3);
         var20.setReplacer(RemoteObjectReplacer.getReplacer());
         var20.writeObject(var1);
         var20.flush();
         var2.setRequestProperty("Content-Length", "" + var3.size());
         var2.connect();
         OutputStream var5 = var2.getOutputStream();
         var3.writeTo(var5);
         var5.flush();
         int var6 = var2.getResponseCode();
         String var7 = var2.getContentType();
         if (var6 != 200 || var7 == null) {
            Loggable var21 = DeploymentServiceLogger.logExceptionWhileGettingDataAsStreamLoggable(var1.getRequestId(), var2.getHeaderField("ErrorMsg"));
            throw new IOException(var21.getMessage());
         }

         MultipartParser var8 = new MultipartParser(var2, (String)null);
         var9 = var8.getMultiDataStream();
      } catch (IOException var18) {
         StringBuffer var4 = new StringBuffer();
         var4.append("Error occurred while while downloading file for request '");
         var4.append(var1);
         var4.append("'. ");
         var4.append("Underlying error is: ");
         var4.append(var18.toString());
         if (Debug.isServiceTransportDebugEnabled()) {
            Debug.serviceHttpDebug(var4.toString());
         }

         throw var18;
      } finally {
         if (var2 != null) {
            try {
               var2.disconnect();
            } catch (Exception var17) {
            }
         }

      }

      return var9;
   }

   private HttpURLConnection createURLConnection() throws IOException {
      URL var1 = DeploymentServiceServlet.getURL();
      HttpURLConnection var2 = URLManager.createAdminHttpConnection(var1);
      var2.setRequestProperty("wl_request_type", mimeEncode("data_transfer_request"));
      if (ManagementService.isRuntimeAccessInitialized()) {
         ConnectionSigner.signConnection(var2, KERNE_ID);
      } else {
         var2.setRequestProperty("username", mimeEncode(this.getUserName()));
         var2.setRequestProperty("password", mimeEncode(this.getUserCredential()));
      }

      var2.setRequestProperty("serverName", mimeEncode(ManagementService.getPropertyService(KERNE_ID).getServerName()));
      var2.setRequestProperty("server_version", PeerInfo.getPeerInfo().getVersionAsString());
      var2.setRequestMethod("POST");
      var2.setDoOutput(true);
      return var2;
   }

   private String getUserName() {
      this.initSecret();
      return this.userName;
   }

   private String getUserCredential() {
      this.initSecret();
      return this.password;
   }

   private void initSecret() {
      if (!this.gotSecret) {
         synchronized(this) {
            if (this.gotSecret) {
               return;
            }

            PrivilegedAction var2 = new PrivilegedAction() {
               public Object run() {
                  HttpDataTransferHandler.this.userName = ManagementService.getPropertyService(HttpDataTransferHandler.KERNE_ID).getTimestamp1();
                  HttpDataTransferHandler.this.password = ManagementService.getPropertyService(HttpDataTransferHandler.KERNE_ID).getTimestamp2();
                  return null;
               }
            };
            SecurityServiceManager.runAs(KERNE_ID, KERNE_ID, var2);
         }

         this.gotSecret = true;
      }

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
