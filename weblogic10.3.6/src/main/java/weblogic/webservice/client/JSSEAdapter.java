package weblogic.webservice.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.StringTokenizer;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import weblogic.net.http.RegexpPool;

/** @deprecated */
public class JSSEAdapter implements SSLAdapter {
   public static final String STRICT_CHECKING_DEFAULT = "weblogic.webservice.client.ssl.strictcertchecking";
   public static final String VERBOSE_PROPERTY = "weblogic.webservice.client.verbose";
   public static final String TRUSTED_CERTS = "weblogic.webservice.client.ssl.trustedcertfile";
   public static final String ENFORCE_CONSTRAINTS = "weblogic.security.SSL.enforceConstraints";
   private static final String HTTPS_PROXY_HOST = "weblogic.webservice.transport.https.proxy.host";
   private static final String HTTPS_PROXY_PORT = "weblogic.webservice.transport.https.proxy.port";
   private static final String HTTPS_NON_PROXY_HOSTS = "weblogic.webservice.transport.https.proxy.nonProxyHosts";
   private static final String DEFAULT_HTTPS_PROXY_HOST = "https.proxyHost";
   private static final String DEFAULT_HTTPS_PROXY_PORT = "https.proxyPort";
   private SSLSocketFactory _factory = null;
   SSLSocketFactory factory;
   private static String proxyHost = getStringProp("weblogic.webservice.transport.https.proxy.host", (String)null);
   private static String proxyPort = getStringProp("weblogic.webservice.transport.https.proxy.port", (String)null);
   private static RegexpPool dontProxy;
   private static final javax.net.ssl.TrustManager trustingManager = new NullTrustManager((1)null);
   private static javax.net.ssl.TrustManager strictManager = null;
   private static KeyManager localKeyManager = null;
   private static SSLContext context = null;
   private static final HostnameVerifier nonverifier = new NullVerifier((1)null);
   private HostnameVerifier verifier;
   protected static boolean verbose = false;
   private static boolean strictCheckingDefault = true;
   protected boolean strictCertChecking;
   protected static String trustedCertFile = null;
   private String[] enabledCiphers;
   private InputStream myKeystream;
   private char[] myPassword;
   boolean proxyEnabled;
   private static String defaultProxyHost = null;
   private static int defaultProxyPort = 8080;

   private static String getStringProp(String var0, String var1) {
      try {
         return System.getProperty(var0);
      } catch (SecurityException var3) {
         return var1;
      }
   }

   private static boolean getBooleanProp(String var0, boolean var1) {
      try {
         return Boolean.getBoolean(var0);
      } catch (SecurityException var3) {
         return var1;
      }
   }

   public JSSEAdapter() {
      this.factory = this._factory;
      this.verifier = null;
      this.strictCertChecking = true;
      this.enabledCiphers = null;
      this.myKeystream = null;
      this.myPassword = null;
      this.proxyEnabled = false;
      this.setStrictChecking(this.getStrictCheckingDefault());
      if (proxyHost != null && proxyPort != null) {
         this.enableProxy(proxyHost, proxyPort);
      }

   }

   public JSSEAdapter(boolean var1) {
      this.factory = this._factory;
      this.verifier = null;
      this.strictCertChecking = true;
      this.enabledCiphers = null;
      this.myKeystream = null;
      this.myPassword = null;
      this.proxyEnabled = false;
      this.setStrictChecking(var1);
      if (proxyHost != null && proxyPort != null) {
         this.enableProxy(proxyHost, proxyPort);
      }

   }

   public void enableProxy(String var1, String var2) {
      this.factory = new SSLTunnelSocketFactory(this._factory, var1, var2);
      this.proxyEnabled = true;
   }

   public void disableProxy() {
      this.factory = this._factory;
      this.proxyEnabled = false;
   }

   public Socket createSocket(String var1, int var2) throws IOException {
      SSLSocket var3 = null;

      try {
         if (proxyHost == null || dontProxy != null && dontProxy.match(var1.toLowerCase())) {
            var3 = (SSLSocket)this.getSocketFactory().createSocket(var1, var2);
         } else {
            var3 = (SSLSocket)this.createProxySocket(var1, var2);
         }

         if (this.enabledCiphers != null) {
            var3.setEnabledCipherSuites(this.enabledCiphers);
         }

         if (verbose) {
            System.out.println("JSSEAdapter connecting to:" + var1 + " port:" + var2 + " socket:" + var3);
         }

         return var3;
      } catch (ClassCastException var5) {
         throw new IOException("JSSEAdapter unable to create SSLSocket instance");
      }
   }

   /** @deprecated */
   public Socket createProxySocket(String var1, int var2) throws IOException {
      Socket var3 = SSLUtil.doTunnelHandshake(proxyHost, Integer.valueOf(proxyPort), var1, var2);
      return this.factory.createSocket(var3, var1, var2, false);
   }

   public URLConnection openConnection(URL var1) throws IOException {
      URLConnection var2 = var1.openConnection();

      try {
         HttpsURLConnection var3 = (HttpsURLConnection)var2;
         var3.setSSLSocketFactory(this.factory);
      } catch (ClassCastException var4) {
      } catch (Throwable var5) {
         System.out.println("JSSEAdapter exception: " + var5.getMessage());
      }

      return var2;
   }

   public static void setStrictCheckingDefault(boolean var0) {
      if (verbose) {
         System.out.println("JSSEAdapter set default cert checking to: " + (var0 ? "STRICT." : "ACCEPTING."));
      }

      strictCheckingDefault = var0;
   }

   protected final boolean getStrictCheckingDefault() {
      return strictCheckingDefault;
   }

   public final void setStrictChecking(boolean var1) {
      if (this.adapterUsed()) {
         throw new IllegalArgumentException("JSSEAdapter cannot change certificate checking once the adapter has been used.");
      } else {
         this._setStrictChecking(var1);
         if (var1) {
            this.setHostnameVerifier(this.verifier);
         } else {
            this.setHostnameVerifier(nonverifier);
         }

      }
   }

   public final void setHostnameVerifier(HostnameVerifier var1) {
      this.verifier = var1;
      if (var1 != null) {
         HttpsURLConnection.setDefaultHostnameVerifier(var1);
      } else if (verbose) {
         System.out.println("JSSEAdapter using the default JSSE HostnameVerifier.");
      }

   }

   public void setVerbose(boolean var1) {
      verbose = var1;
   }

   public final void setTrustManager(javax.net.ssl.TrustManager var1) {
      if (this.adapterUsed()) {
         throw new IllegalArgumentException("JSSEAdapter cannot change trust manager once the adapter has been used");
      } else {
         try {
            X509TrustManager[] var2 = new X509TrustManager[]{(X509TrustManager)var1};
            if (this.myKeystream != null) {
               File var3 = File.createTempFile("wsjsse", ".fix");
               var3.deleteOnExit();
               FileOutputStream var4 = new FileOutputStream(var3);
               KeyStore var5 = KeyStore.getInstance("JKS");
               var5.load(this.myKeystream, this.myPassword);
               var5.store(var4, this.myPassword);
               var4.close();
               System.setProperty("javax.net.ssl.keyStore", var3.getAbsolutePath());
               System.setProperty("javax.net.ssl.keyStorePassword", new String(this.myPassword));
               if (verbose) {
                  System.out.println("JSSEAdapter setting KeyManager to: " + var3.getName());
               }
            } else {
               context.init((KeyManager[])null, var2, new SecureRandom());
               if (verbose) {
                  System.out.println("JSSEAdapter KeyManager null");
               }
            }
         } catch (Exception var6) {
            if (verbose) {
               System.out.println("JSSEAdapter failure in getInstance.");
               var6.printStackTrace();
            }

            throw new IllegalArgumentException("JSSEAdapter failed to obtain SSLContext.");
         }

         if (verbose) {
            System.out.println("JSSEAdapter setTrustManager to " + var1);
         }

      }
   }

   public final void loadLocalIdentity(InputStream var1, char[] var2) throws Exception {
      if (this.adapterUsed()) {
         throw new IllegalArgumentException("JSSEAdapter cannot load identities once the adapter has been used.");
      } else {
         this.myKeystream = var1;
         this.myPassword = var2;
         if (verbose) {
            System.out.println("JSSEAdapter Loaded local identity from keystore: " + var1);
         }

      }
   }

   protected final void _setStrictChecking(boolean var1) {
      if (this.adapterUsed()) {
         throw new IllegalArgumentException("JSSEAdapter cannot change strict checking once the adapter has been used");
      } else {
         if (var1) {
            if (verbose) {
               System.out.println("JSSEAdapter enabling strict checking on adapter " + this);
            }

            this.setTrustManager(strictManager);
         } else {
            if (verbose) {
               System.out.println("JSSEAdapter disabling strict checking on adapter " + this);
            }

            this.setTrustManager(trustingManager);
         }

         this.strictCertChecking = var1;
      }
   }

   private void resetFactory() {
      this._factory = null;
   }

   protected SSLSocketFactory getSocketFactory() {
      if (this.factory == null) {
         this.factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
         if (verbose) {
            System.out.println("JSSEAdaptger got new socketfactory: " + this.factory);
         }
      }

      return this.factory;
   }

   public void setSocketFactory(SSLSocketFactory var1) {
      this._factory = var1;
      if (!this.proxyEnabled) {
         this.factory = var1;
      } else {
         ((SSLTunnelSocketFactory)var1).setDelegateFactory(var1);
      }

   }

   protected boolean adapterUsed() {
      return this.factory != null;
   }

   /** @deprecated */
   public void setProxy(String var1, int var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Must provide a proxy hostname");
      } else {
         proxyHost = var1;
         proxyPort = Integer.toString(var2);
      }
   }

   /** @deprecated */
   public void setProxyAuth(String var1, String var2) {
      if (!this.proxyEnabled) {
         throw new IllegalStateException("Cannot set proxy username/password until proxy has been enabled.");
      } else if (var1 != null && var2 != null) {
         ((SSLTunnelSocketFactory)this.factory).setProxyAuth(var1, var2);
      } else {
         throw new IllegalArgumentException("Cannot set username or password to null");
      }
   }

   public String toString() {
      return "<JSSEAdapter name=\"" + super.toString() + "\" proxyEnabled=" + this.proxyEnabled + " proxyHost=" + proxyHost + " proxyPort=" + proxyPort + ">\n" + this.factory + "\n</JSSEAdapter>";
   }

   protected final SSLContext getContext() {
      if (context == null) {
         try {
            context = SSLContext.getInstance("SSL");
         } catch (Exception var2) {
            if (verbose) {
               System.out.println("JSSEAdapter failure in getInstance.");
               var2.printStackTrace();
            }

            throw new IllegalArgumentException("JSSEAdapter failed to obtain SSLContext.");
         }
      }

      return context;
   }

   public static void main(String[] var0) {
      try {
         JSSEAdapter var1 = new JSSEAdapter();
         var1.setVerbose(true);
         System.out.println("-----------------------------\nraw adapter:\n" + var1);
         var1.disableProxy();
         System.out.println("-----------------------------\nProxy Disabled:\n" + var1);

         try {
            var1.setStrictChecking(true);
            System.out.println("** success -- setStrictChecking true");
         } catch (Exception var9) {
            System.out.println("Caught exception when setStrictChecking true: " + var9);
         }

         try {
            System.out.println("** success -- Got socket: " + var1.createSocket("127.0.0.1", 7002));
         } catch (IOException var8) {
            System.out.println("Caught exception when creating socket: " + var8);
         }

         try {
            var1.setProxyAuth("pete", "password");
            System.out.println("** failure -- set proxy auth when proxy should be disabled");
         } catch (IllegalStateException var7) {
            System.out.println("** success -- cauth IllegalStateException trying to set proxy when proxy disabled");
         }

         var1.enableProxy("127.0.0.1", "3128");
         System.out.println("-----------------------------\nProxy Enabled:\n" + var1);

         try {
            Socket var2 = var1.createSocket("127.0.0.1", 7002);
            System.out.println("** success -- Got socket: " + var2);
         } catch (IOException var6) {
            System.out.println("Caught exception when creating socket: " + var6);
         }

         try {
            var1.setProxyAuth("pete", (String)null);
            System.out.println("** failure -- set proxy auth when proxy should be disabled");
         } catch (IllegalArgumentException var5) {
            System.out.println("** success -- cauth IllegalArgumentException trying to set password to null");
         }

         try {
            var1.setProxyAuth((String)null, "pete");
            System.out.println("** failure -- set proxy auth when proxy should be disabled");
         } catch (IllegalArgumentException var4) {
            System.out.println("** success -- cauth IllegalArgumentException trying to set user to null");
         }

         var1.disableProxy();

         try {
            System.out.println("** success -- Got socket: " + var1.createSocket("127.0.0.1", 7002));
         } catch (IOException var3) {
            System.out.println("Caught exception when creating socket: " + var3);
         }
      } catch (Throwable var10) {
         System.out.println("Caught exception:" + var10.getMessage());
         var10.printStackTrace();
      }

   }

   static {
      try {
         verbose = getBooleanProp("weblogic.webservice.client.verbose", false);
         if (verbose) {
            System.out.println("JSSEAdapter verbose output enabled");
         }

         if (!getBooleanProp("weblogic.webservice.client.ssl.strictcertchecking", true)) {
            strictCheckingDefault = false;
            if (verbose) {
               System.out.println("JSSEAdapter strict cert checking disabled by default");
            }
         }

         trustedCertFile = getStringProp("weblogic.webservice.client.ssl.trustedcertfile", (String)null);
         if (trustedCertFile != null && verbose) {
            System.out.println("JSSEAdapter trusted certificates will be loaded from " + trustedCertFile);
         }

         defaultProxyHost = getStringProp("weblogic.webservice.transport.https.proxy.host", (String)null);
         String var0 = getStringProp("weblogic.webservice.transport.https.proxy.port", (String)null);
         if (var0 != null) {
            defaultProxyPort = Integer.parseInt(var0);
         }

         String var1 = getStringProp("weblogic.webservice.transport.https.proxy.nonProxyHosts", (String)null);
         if (var1 != null) {
            dontProxy = new RegexpPool();
            StringTokenizer var2 = new StringTokenizer(var1, "|", false);

            while(var2.hasMoreTokens()) {
               dontProxy.add(var2.nextToken().toLowerCase());
            }
         }

         context = SSLContext.getInstance("SSL");
         TrustManagerFactory var4 = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
         var4.init((KeyStore)null);
         strictManager = var4.getTrustManagers()[0];
      } catch (Throwable var3) {
         if (verbose) {
            System.out.println("JSSEAdapter error: " + var3.getMessage());
            var3.printStackTrace();
         }
      }

   }
}
