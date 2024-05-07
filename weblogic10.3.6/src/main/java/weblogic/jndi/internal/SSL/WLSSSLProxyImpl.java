package weblogic.jndi.internal.SSL;

import java.io.InputStream;
import java.net.SocketException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import javax.net.ssl.SSLSocketFactory;
import weblogic.security.SSL.SSLClientInfo;
import weblogic.security.SSL.TrustManager;
import weblogic.security.acl.internal.Security;

public final class WLSSSLProxyImpl implements SSLProxy {
   private SSLClientInfo findOrCreateSSLClientInfo() {
      return Security.getThreadSSLClientInfo();
   }

   public SSLSocketFactory getSSLSocketFactory() throws SocketException {
      return this.findOrCreateSSLClientInfo().getSSLSocketFactory();
   }

   public boolean isEmpty() {
      return this.findOrCreateSSLClientInfo().isEmpty();
   }

   public void setRootCAfingerprints(byte[][] var1) {
      this.findOrCreateSSLClientInfo().setRootCAfingerprints(var1);
   }

   public void setRootCAfingerprints(String var1) {
      this.findOrCreateSSLClientInfo().setRootCAfingerprints(var1);
   }

   public byte[][] getRootCAfingerprints() {
      return this.findOrCreateSSLClientInfo().getRootCAfingerprints();
   }

   public void setExpectedName(String var1) {
      this.findOrCreateSSLClientInfo().setExpectedName(var1);
   }

   public String getExpectedName() {
      return this.findOrCreateSSLClientInfo().getExpectedName();
   }

   public InputStream[] getSSLClientCertificate() {
      return this.findOrCreateSSLClientInfo().getSSLClientCertificate();
   }

   public void setSSLClientCertificate(InputStream[] var1) {
      this.findOrCreateSSLClientInfo().setSSLClientCertificate(var1);
   }

   public void setSSLClientKeyPassword(String var1) {
      this.findOrCreateSSLClientInfo().setSSLClientKeyPassword(var1);
   }

   public String getSSLClientKeyPassword() {
      return this.findOrCreateSSLClientInfo().getSSLClientKeyPassword();
   }

   public void setTrustManager(TrustManager var1) {
      this.findOrCreateSSLClientInfo().setTrustManager(var1);
   }

   public TrustManager getTrustManager() {
      return this.findOrCreateSSLClientInfo().getTrustManager();
   }

   public void loadLocalIdentity(Certificate[] var1, PrivateKey var2) {
      this.findOrCreateSSLClientInfo().loadLocalIdentity(var1, var2);
   }

   public boolean isClientCertAvailable() {
      return this.findOrCreateSSLClientInfo().isClientCertAvailable();
   }

   public boolean isLocalIdentitySet() {
      return this.findOrCreateSSLClientInfo().isLocalIdentitySet();
   }
}
