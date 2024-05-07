package weblogic.security.SSL;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public final class CertCallback {
   SSLClientInfo sslInfo;
   private boolean bSSLNio = false;
   private String srcHost;
   private int srcPort = -1;
   private String destHost;
   private int destPort = -1;

   public CertCallback() {
      this.bSSLNio = false;
      this.sslInfo = null;
   }

   public CertCallback(boolean var1) {
      this.bSSLNio = var1;
      this.sslInfo = null;
   }

   public CertCallback(boolean var1, String var2, int var3, String var4, int var5) {
      this.bSSLNio = var1;
      this.sslInfo = null;
      this.destHost = var4;
      this.destPort = var5;
      this.srcHost = var2;
      this.srcPort = var3;
   }

   public boolean isNioConfigured() {
      return this.bSSLNio;
   }

   public void setSSLClientInfo(Certificate[] var1, PrivateKey var2) {
      if (!this.bSSLNio) {
         this.sslInfo = new SSLClientInfo();
      } else {
         this.sslInfo = new SSLClientInfo(true);
      }

      this.sslInfo.loadLocalIdentity(var1, var2);
   }

   public SSLClientInfo getSSLClientInfo() {
      return this.sslInfo;
   }

   public String getDestinationHost() {
      return this.destHost;
   }

   public int getDestinationPort() {
      return this.destPort;
   }

   public String getSourceHost() {
      return this.srcHost;
   }

   public int getSourcePort() {
      return this.srcPort;
   }
}
