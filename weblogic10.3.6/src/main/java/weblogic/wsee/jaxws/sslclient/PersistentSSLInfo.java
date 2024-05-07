package weblogic.wsee.jaxws.sslclient;

import java.io.Serializable;
import javax.net.ssl.TrustManagerFactory;

public class PersistentSSLInfo implements Serializable {
   private String keystore;
   private String keystorePassword;
   private String keystoreType = "JKS";
   private String keyAlias;
   private String keyPassword;
   private String trustKeystore;
   private String trustKeystorePassword;
   private String trustKeystoreType = "JKS";
   private String trustKeystoreAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
   private String trustKeystoreProvider = null;

   public String getTrustKeystoreAlgorithm() {
      return this.trustKeystoreAlgorithm;
   }

   public void setTrustKeystoreAlgorithm(String var1) {
      this.trustKeystoreAlgorithm = var1;
   }

   public String getTrustKeystoreProvider() {
      if (this.trustKeystoreProvider == null) {
         try {
            this.trustKeystoreProvider = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).getProvider().getName();
         } catch (Exception var2) {
         }
      }

      return this.trustKeystoreProvider;
   }

   public void setTrustKeystoreProvider(String var1) {
      this.trustKeystoreProvider = var1;
   }

   public String getKeyAlias() {
      return this.keyAlias;
   }

   public void setKeyAlias(String var1) {
      this.keyAlias = var1;
   }

   public String getKeyPassword() {
      return this.keyPassword;
   }

   public void setKeyPassword(String var1) {
      this.keyPassword = var1;
   }

   public String getKeystore() {
      return this.keystore;
   }

   public void setKeystore(String var1) {
      this.keystore = var1;
   }

   public String getKeystorePassword() {
      return this.keystorePassword;
   }

   public void setKeystorePassword(String var1) {
      this.keystorePassword = var1;
   }

   public String getKeystoreType() {
      return this.keystoreType;
   }

   public void setKeystoreType(String var1) {
      this.keystoreType = var1;
   }

   public String getTrustKeystore() {
      return this.trustKeystore;
   }

   public void setTrustKeystore(String var1) {
      this.trustKeystore = var1;
   }

   public String getTrustKeystorePassword() {
      return this.trustKeystorePassword;
   }

   /** @deprecated */
   public void setTrustKeystorePassword(String var1) {
      this.trustKeystorePassword = var1;
   }

   public String getTrustKeystoreType() {
      return this.trustKeystoreType;
   }

   public void setTrustKeystoreType(String var1) {
      this.trustKeystoreType = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("_keystore=").append(this.keystore).append("\n");
      var1.append("_keystorePassword=").append(this.keystorePassword).append("\n");
      var1.append("_keystoreType=").append(this.keystoreType).append("\n");
      var1.append("_keyAlias=").append(this.keyAlias).append("\n");
      var1.append("_keyPassword=").append(this.keyPassword).append("\n");
      var1.append("_trustKeystore=").append(this.trustKeystore).append("\n");
      var1.append("_trustKeystorePassword=").append(this.trustKeystorePassword).append("\n");
      var1.append("_trustKeystoreType=").append(this.trustKeystoreType).append("\n");
      var1.append("_trustKeystoreAlgorithm=").append(this.trustKeystoreAlgorithm).append("\n");
      var1.append("_trustKeystoreProvider=").append(this.getTrustKeystoreProvider()).append("\n");
      return var1.toString();
   }
}
