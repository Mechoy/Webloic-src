package weblogic.jndi.internal.SSL;

import java.io.InputStream;
import java.net.SocketException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import javax.net.ssl.SSLSocketFactory;
import weblogic.rjvm.RJVMEnvironment;
import weblogic.security.SSL.TrustManager;

public final class ClientSSLProxyImpl implements SSLProxy {
   public SSLSocketFactory getSSLSocketFactory() throws SocketException {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public boolean isEmpty() {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public void setRootCAfingerprints(byte[][] var1) {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public void setRootCAfingerprints(String var1) {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public byte[][] getRootCAfingerprints() {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public void setExpectedName(String var1) {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public String getExpectedName() {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public InputStream[] getSSLClientCertificate() {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public void setSSLClientCertificate(InputStream[] var1) {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public void setSSLClientKeyPassword(String var1) {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public String getSSLClientKeyPassword() {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public void setTrustManager(TrustManager var1) {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public TrustManager getTrustManager() {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public void loadLocalIdentity(Certificate[] var1, PrivateKey var2) {
      throw new UnsupportedOperationException("This method is not supported on the standalone WebLogic client");
   }

   public boolean isClientCertAvailable() {
      return RJVMEnvironment.getEnvironment().getSSLContext() != null;
   }

   public boolean isLocalIdentitySet() {
      return RJVMEnvironment.getEnvironment().getSSLContext() != null;
   }
}
