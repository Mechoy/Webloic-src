package weblogic.net.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.UnknownProtocolException;
import weblogic.security.SecurityLogger;
import weblogic.security.SSL.HostnameVerifier;
import weblogic.security.SSL.HostnameVerifierJSSE;
import weblogic.security.SSL.SSLClientInfo;
import weblogic.security.SSL.SSLSocketFactory;
import weblogic.security.SSL.TrustManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.Security;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.SSLContextManager;

public class HttpsURLConnection extends HttpURLConnection {
   private SSLSocketFactory sslSocketFactory;
   private SSLClientInfo sslinfo;
   private SSLSession sslSession;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public HttpsURLConnection(URL var1, SSLClientInfo var2) {
      super(var1);
      this.sslSession = null;
      this.sslinfo = var2 != null ? var2 : Security.getThreadSSLClientInfo();
   }

   public HttpsURLConnection(URL var1) {
      this(var1, (Proxy)null);
   }

   public HttpsURLConnection(URL var1, Proxy var2) {
      this(var1, (SSLClientInfo)null);
      this.instProxy = var2 == null ? Proxy.NO_PROXY : var2;
   }

   public void loadLocalIdentity(InputStream[] var1) {
      this.setSSLClientCertificate(var1);
   }

   private SSLClientInfo getSSLInfo() {
      if (this.sslinfo == null) {
         this.sslinfo = new SSLClientInfo();
      }

      return this.sslinfo;
   }

   public void loadLocalIdentity(InputStream var1, InputStream var2, char[] var3) {
      this.getSSLInfo().loadLocalIdentity(var1, var2, var3);
   }

   public void loadLocalIdentity(Certificate[] var1, PrivateKey var2) {
      this.getSSLInfo().loadLocalIdentity(var1, var2);
   }

   public void setSSLClientCertificate(InputStream[] var1) {
      if (var1 != null) {
         this.getSSLInfo().setSSLClientCertificate(var1);
         this.updateFactoryClientInfo();
      }

   }

   public void setTrustManager(TrustManager var1) {
      this.getSSLInfo().setTrustManager(var1);
      this.updateFactoryClientInfo();
   }

   public TrustManager getTrustManager() {
      return this.sslinfo != null ? this.sslinfo.getTrustManager() : Security.getThreadSSLClientInfo().getTrustManager();
   }

   public void setHostnameVerifier(HostnameVerifier var1) {
      this.getSSLInfo().setHostnameVerifier(var1);
      this.updateFactoryClientInfo();
   }

   public HostnameVerifier getHostnameVerifier() {
      return this.sslinfo != null ? this.sslinfo.getHostnameVerifier() : Security.getThreadSSLClientInfo().getHostnameVerifier();
   }

   /** @deprecated */
   public void setHostnameVerifierJSSE(HostnameVerifierJSSE var1) {
      if (var1 != null) {
         this.getSSLInfo().setHostnameVerifierJSSE(var1);
         this.updateFactoryClientInfo();
      }

   }

   /** @deprecated */
   public HostnameVerifierJSSE getHostnameVerifierJSSE() {
      return this.sslinfo != null ? this.sslinfo.getHostnameVerifierJSSE() : getDefaultHostnameVerifierJSSE();
   }

   /** @deprecated */
   public static HostnameVerifierJSSE getDefaultHostnameVerifierJSSE() {
      return Security.getThreadSSLClientInfo().getHostnameVerifierJSSE();
   }

   public void setSSLSocketFactory(SSLSocketFactory var1) {
      this.sslSocketFactory = var1;
   }

   public SSLSocketFactory getSSLSocketFactory() {
      return this.sslSocketFactory != null ? this.sslSocketFactory : getDefaultSSLSocketFactory();
   }

   public static SSLSocketFactory getDefaultSSLSocketFactory() {
      return (SSLSocketFactory)SSLSocketFactory.getDefault();
   }

   public String getCipherSuite() {
      return this.sslSession != null ? this.sslSession.getCipherSuite() : null;
   }

   public X509Certificate[] getServerCertificateChain() throws SSLPeerUnverifiedException {
      return this.sslSession != null ? this.sslSession.getPeerCertificateChain() : null;
   }

   public SSLSession getSSLSession() {
      return this.sslSession;
   }

   public void connect() throws IOException {
      if (!this.connected) {
         this.checkClientSSLInfo();

         try {
            this.http = HttpsClient.New(this.url, this.instProxy, this.getSocketFactory(), this.sslinfo, this.sslSocketFactory, this.useHttp11, this.getConnectTimeout(), this.getReadTimeout(), true, this.getRequestProperty("Proxy-Authorization"), this.ignoreSystemNonPorxyHosts);
         } catch (SocketTimeoutException var2) {
            this.rememberedException = var2;
            throw var2;
         }

         this.http.setConnection(this);
         this.connected = true;
         HttpsClient var1 = (HttpsClient)this.http;
         if (this.sslSocketFactory == null) {
            this.setSSLSocketFactory(var1.getSSLSocketFactory());
         }

         this.sslSession = var1.getSSLSession();
      }
   }

   private void checkClientSSLInfo() {
      if (KernelStatus.isServer()) {
         if (this.sslinfo != null) {
            java.security.cert.X509Certificate[] var1 = this.sslinfo.getClientLocalIdentityCert();
            if (var1 != null && var1.length > 0) {
               return;
            }
         }

         RuntimeAccess var8 = ManagementService.getRuntimeAccess(kernelId);
         if (var8 != null) {
            SSLMBean var2 = var8.getServer().getSSL();
            if (var2 != null) {
               if (var2.isUseServerCerts()) {
                  if (debug) {
                     SecurityLogger.logUsingServerCerts();
                  }

                  try {
                     ServerChannel var9 = ProtocolManager.findProtocol(this.getProtocol()).getHandler().getDefaultServerChannel();
                     SSLClientInfo var3 = SSLContextManager.getChannelSSLClientInfo(var9, kernelId);
                     this.sslinfo = var3;
                  } catch (ConfigurationException var4) {
                     SecurityLogger.logCantUseServerCerts();
                  } catch (CertificateException var5) {
                     SecurityLogger.logCantUseServerCerts();
                  } catch (UnknownProtocolException var6) {
                     SecurityLogger.logCantUseServerCerts();
                  } catch (Exception var7) {
                     SecurityLogger.logCantUseServerCerts();
                  }

               }
            }
         }
      }
   }

   public void disconnect() {
      this.sslSession = null;
      super.disconnect();
   }

   protected String getProtocol() {
      return "https";
   }

   protected HttpClient getHttpClient() throws IOException {
      HttpsClient var1 = HttpsClient.New(this.url, this.instProxy, this.getSocketFactory(), this.sslinfo, this.sslSocketFactory, this.useHttp11, this.getConnectTimeout(), this.getReadTimeout(), false, this.getRequestProperty("Proxy-Authorization"), this.ignoreSystemNonPorxyHosts);
      var1.setConnection(this);
      this.connected = true;
      return var1;
   }

   public SSLClientInfo getSSLClientInfo() {
      return this.sslinfo;
   }

   private void updateFactoryClientInfo() {
      if (this.sslSocketFactory != null) {
         this.sslSocketFactory.setSSLClientInfo(this.sslinfo);
      }

   }
}
