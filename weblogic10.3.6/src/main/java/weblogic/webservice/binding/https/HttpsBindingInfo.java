package weblogic.webservice.binding.https;

import java.net.Socket;
import weblogic.webservice.binding.BindingInfo;
import weblogic.webservice.client.SSLAdapter;
import weblogic.webservice.client.SSLAdapterFactory;

/** @deprecated */
public class HttpsBindingInfo extends BindingInfo {
   private SSLAdapter sslAdapter = SSLAdapterFactory.getDefaultFactory().getSSLAdapter();
   private boolean pooling = false;
   private Socket secureSocket = null;
   private String secureHost = null;
   private int securePort = 0;
   private long secureTimeOK = 0L;
   private long secureTimeKO = 15000L;
   private static final String HTTPS_SOCKET_SHARING = "https.sharedsocket";
   private boolean socketSharingEnabled = Boolean.getBoolean("https.sharedsocket");

   public String getTransport() {
      return "https";
   }

   public SSLAdapter getSSLAdapter() {
      return this.sslAdapter;
   }

   public void setSSLAdapter(SSLAdapter var1) {
      this.sslAdapter = var1;
      this.setSSLSecureSocket((Socket)null, (String)null, 0);
   }

   /** @deprecated */
   public void setSSLSocketPooling(boolean var1) {
      this.pooling = var1;
      if (!this.pooling) {
         this.setSSLSecureSocket((Socket)null, (String)null, 0);
      }

   }

   /** @deprecated */
   public boolean getSSLSocketPooling() {
      return this.pooling;
   }

   void setSSLSecureSocket(Socket var1, String var2, int var3) {
      if (var1 == null && this.secureSocket != null) {
         try {
            this.secureSocket.close();
         } catch (Exception var5) {
         }
      }

      this.secureSocket = var1;
      this.secureHost = var2;
      this.securePort = var3;
      this.secureTimeOK = System.currentTimeMillis() + this.secureTimeKO;
   }

   Socket getSSLSecureSocket(String var1, int var2) {
      if (this.secureSocket != null) {
         if (this.secureHost.equals(var1) && this.securePort == var2 && this.secureSocket.isConnected() && this.secureSocket.isBound() && !this.secureSocket.isClosed() && !this.secureSocket.isInputShutdown() && !this.secureSocket.isOutputShutdown() && this.secureTimeOK > System.currentTimeMillis()) {
            this.secureTimeOK = System.currentTimeMillis() + this.secureTimeKO;
         } else {
            this.closeSharedSocket();
         }
      }

      return this.secureSocket;
   }

   void setSSLSecureSocketTimeOut() {
      this.secureTimeOK = System.currentTimeMillis() + this.secureTimeKO;
   }

   public void closeSharedSocket() {
      try {
         this.secureSocket.close();
      } catch (Exception var2) {
      }

      this.setSSLSecureSocket((Socket)null, (String)null, 0);
   }

   public void setSharedSocketTimeout(long var1) {
      this.secureTimeKO = var1 * 1000L;
   }

   public long getSharedSocketTimeout() {
      return this.secureTimeKO / 1000L;
   }

   public void setSocketSharing(boolean var1) {
      this.socketSharingEnabled = var1;
      if (!this.socketSharingEnabled) {
         this.setSSLSecureSocket((Socket)null, (String)null, 0);
      }

   }

   public boolean getSocketSharing() {
      return this.socketSharingEnabled;
   }
}
