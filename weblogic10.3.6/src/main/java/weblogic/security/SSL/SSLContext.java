package weblogic.security.SSL;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public final class SSLContext {
   private SSLClientInfo clientInfo;
   private String protocol;

   public static SSLContext getInstance(String var0) {
      return new SSLContext(var0);
   }

   public SSLContext(String var1) {
      this.protocol = var1;
   }

   public SSLContext() {
      this.protocol = "https";
   }

   public void setTrustManager(TrustManager var1) {
      this.getClientInfo().setTrustManager(var1);
   }

   public void setHostnameVerifier(HostnameVerifier var1) {
      this.getClientInfo().setHostnameVerifier(var1);
   }

   /** @deprecated */
   public void setTrustManagerJSSE(TrustManagerJSSE var1) {
      if (var1 != null) {
         this.getClientInfo().setTrustManagerJSSE(var1);
      }

   }

   /** @deprecated */
   public void setHostnameVerifierJSSE(HostnameVerifierJSSE var1) {
      if (var1 != null) {
         this.getClientInfo().setHostnameVerifierJSSE(var1);
      }

   }

   public void loadLocalIdentity(InputStream[] var1) {
      if (var1 != null) {
         this.getClientInfo().setSSLClientCertificate(var1);
      }

   }

   public void loadLocalIdentity(InputStream[] var1, String var2) {
      if (var1 != null) {
         this.getClientInfo().setSSLClientCertificate(var1);
         this.getClientInfo().setSSLClientKeyPassword(var2);
      }

   }

   public void loadLocalIdentity(Certificate[] var1, PrivateKey var2) {
      if (var1 != null && var2 != null) {
         this.getClientInfo().loadLocalIdentity(var1, var2);
      }

   }

   /** @deprecated */
   public SSLSocketFactory getSocketFactoryJSSE() {
      return SSLSocketFactory.getJSSE(this.clientInfo);
   }

   public SSLSocketFactory getSocketFactory() {
      return SSLSocketFactory.getInstance(this.clientInfo);
   }

   public SSLSocketFactory getNioSocketFactory() {
      return SSLNioSocketFactory.getInstance(this.clientInfo);
   }

   public String getProtocol() {
      return this.protocol;
   }

   public String getProvider() {
      return "weblogic.net";
   }

   protected SSLClientInfo getSSLClientInfo() {
      return this.clientInfo;
   }

   protected void setSSLClientInfo(SSLClientInfo var1) {
      this.clientInfo = var1;
   }

   private SSLClientInfo getClientInfo() {
      if (this.clientInfo == null) {
         this.clientInfo = new SSLClientInfo();
      }

      return this.clientInfo;
   }
}
