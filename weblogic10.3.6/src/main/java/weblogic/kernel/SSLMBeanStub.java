package weblogic.kernel;

import weblogic.management.configuration.SSLMBean;

final class SSLMBeanStub extends MBeanStub implements SSLMBean {
   private boolean isTwoWaySSLEnabled = false;
   private boolean isAllowUnencryptedNullCipher = false;

   SSLMBeanStub() {
      this.initializeFromSystemProperties("weblogic.ssl.");
   }

   public boolean isUseJava() {
      return true;
   }

   public void setUseJava(boolean var1) {
   }

   public String getMDAcceleration() {
      return "Native/Java";
   }

   public void setMDAcceleration(String var1) {
   }

   public String getRC4Acceleration() {
      return "Native/Java";
   }

   public void setRC4Acceleration(String var1) {
   }

   public String getRSAAcceleration() {
      return "Native/Java";
   }

   public void setRSAAcceleration(String var1) {
   }

   public boolean isEnabled() {
      return false;
   }

   public void setEnabled(boolean var1) {
   }

   public String[] getCiphersuites() {
      return null;
   }

   public void setCiphersuites(String[] var1) {
   }

   public String getCertAuthenticator() {
      return null;
   }

   public void setCertAuthenticator(String var1) {
   }

   public String getTrustedCAFileName() {
      return "trusted-ca.pem";
   }

   public void setTrustedCAFileName(String var1) {
   }

   public int getExportKeyLifespan() {
      return 500;
   }

   public void setExportKeyLifespan(int var1) {
   }

   public boolean isUseServerCerts() {
      return false;
   }

   public void setUseServerCerts(boolean var1) {
   }

   public void setJSSEEnabled(boolean var1) {
   }

   public boolean isJSSEEnabled() {
      return false;
   }

   public boolean isClientCertificateEnforced() {
      return false;
   }

   public void setClientCertificateEnforced(boolean var1) {
   }

   public String getServerCertificateFileName() {
      return "server-cert.der";
   }

   public void setServerCertificateFileName(String var1) {
   }

   public int getListenPort() {
      return 7002;
   }

   public void setListenPort(int var1) {
   }

   public boolean isListenPortEnabled() {
      return false;
   }

   public void setListenPortEnabled(boolean var1) {
   }

   public String getServerCertificateChainFileName() {
      return "server-certchain.pem";
   }

   public void setServerCertificateChainFileName(String var1) {
   }

   public int getCertificateCacheSize() {
      return 3;
   }

   public void setCertificateCacheSize(int var1) {
   }

   public boolean isHandlerEnabled() {
      return true;
   }

   public void setHandlerEnabled(boolean var1) {
   }

   public int getLoginTimeoutMillis() {
      return 25000;
   }

   public void setLoginTimeoutMillis(int var1) {
   }

   public String getServerKeyFileName() {
      return "server-key.der";
   }

   public void setServerKeyFileName(String var1) {
   }

   /** @deprecated */
   public int getPeerValidationEnforced() {
      return 0;
   }

   /** @deprecated */
   public void setPeerValidationEnforced(int var1) {
   }

   public boolean isKeyEncrypted() {
      return false;
   }

   public void setKeyEncrypted(boolean var1) {
   }

   public String getHostnameVerifier() {
      return null;
   }

   public void setHostnameVerifier(String var1) {
   }

   public boolean isHostnameVerificationIgnored() {
      return false;
   }

   public void setHostnameVerificationIgnored(boolean var1) {
   }

   public boolean isTwoWaySSLEnabled() {
      return false;
   }

   public void setTwoWaySSLEnabled(boolean var1) {
      this.isTwoWaySSLEnabled = var1;
   }

   public boolean isAllowUnencryptedNullCipher() {
      return this.isAllowUnencryptedNullCipher;
   }

   public void setAllowUnencryptedNullCipher(boolean var1) {
      this.isAllowUnencryptedNullCipher = var1;
   }

   public String getServerPrivateKeyAlias() {
      return null;
   }

   public void setServerPrivateKeyAlias(String var1) {
   }

   public String getServerPrivateKeyPassPhrase() {
      return null;
   }

   public void setServerPrivateKeyPassPhrase(String var1) {
   }

   public byte[] getServerPrivateKeyPassPhraseEncrypted() {
      return null;
   }

   public void setServerPrivateKeyPassPhraseEncrypted(byte[] var1) {
   }

   public boolean isSSLRejectionLoggingEnabled() {
      return true;
   }

   public void setSSLRejectionLoggingEnabled(boolean var1) {
   }

   public String getIdentityAndTrustLocations() {
      return null;
   }

   public void setIdentityAndTrustLocations(String var1) {
   }

   public String getInboundCertificateValidation() {
      return null;
   }

   public void setInboundCertificateValidation(String var1) {
   }

   public String getOutboundCertificateValidation() {
      return null;
   }

   public void setOutboundCertificateValidation(String var1) {
   }

   public boolean isUseClientCertForOutbound() {
      return false;
   }

   public void setUseClientCertForOutbound(boolean var1) {
   }

   public String getClientCertAlias() {
      return null;
   }

   public void setClientCertAlias(String var1) {
   }

   public String getClientCertPrivateKeyPassPhrase() {
      return null;
   }

   public void setClientCertPrivateKeyPassPhrase(String var1) {
   }

   public byte[] getClientCertPrivateKeyPassPhraseEncrypted() {
      return null;
   }

   public void setClientCertPrivateKeyPassPhraseEncrypted(byte[] var1) {
   }

   public String getOutboundPrivateKeyAlias() {
      return null;
   }

   public String getOutboundPrivateKeyPassPhrase() {
      return null;
   }
}
