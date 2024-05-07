package weblogic.wsee.jaxws.transport.http.client;

import com.sun.xml.ws.api.EndpointAddress;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.message.Packet;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOMFeature;
import weblogic.kernel.KernelStatus;
import weblogic.security.utils.KeyStoreConfigurationHelper;
import weblogic.security.utils.KeyStoreInfo;
import weblogic.security.utils.MBeanKeyStoreConfiguration;
import weblogic.wsee.jaxws.proxy.ClientProxyFeature;
import weblogic.wsee.jaxws.proxy.ProxyUtil;
import weblogic.wsee.jaxws.sslclient.PersistentSSLInfo;
import weblogic.wsee.jaxws.sslclient.SSLClientUtil;

public class HttpClientTransport extends com.sun.xml.ws.transport.http.client.HttpClientTransport {
   public static final String JAXWS_TRANSPORT_STREAMING = "jaxws.transport.streaming";
   Packet context = null;
   private static final Logger LOGGER = Logger.getLogger(HttpClientTransport.class.getName());
   private static final HostnameVerifierAdapter byPassAllHostnameVerifierAdapter = new HostnameVerifierAdapter(new HttpClientVerifier());
   private static final boolean excludeProxyAuthHeader = Boolean.getBoolean("weblogic.wsee.jaxws.client.excludeProxyAuthHeader");

   public HttpClientTransport(Packet var1, Map<String, List<String>> var2) {
      super(var1, var2);
   }

   public static void forcePersistentSSLInfoOntoOutgoingMessageIfNotPresent(Map<String, Object> var0) {
      if (KernelStatus.isServer() && !var0.containsKey("weblogic.wsee.jaxws.sslclient.PersistentSSLInfo")) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("HttpClientTransport forcing PersistentSSLInfo onto outgoing Packet to handle an HTTPS connection for delivering a message");
         }

         KeyStoreConfigurationHelper var1 = new KeyStoreConfigurationHelper(MBeanKeyStoreConfiguration.getInstance());
         KeyStoreInfo var2 = var1.getIdentityKeyStore();
         if (var2 == null) {
            throw new WebServiceException("Couldn't get KeyStoreConfigurationHelper");
         } else {
            String var3 = var2.getFileName();
            if (var3 != null && var3.length() != 0) {
               String var4 = var2.getType();
               char[] var5 = var2.getPassPhrase();
               String var6 = var1.getIdentityAlias();
               char[] var7 = var1.getIdentityPrivateKeyPassPhrase();
               if (var6 != null && var6.length() != 0) {
                  if (var7 == null) {
                     throw new WebServiceException("Server Certificate PassPhrase not supplied");
                  } else {
                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("KeyStore File:  " + var3);
                        LOGGER.fine("KeyStore Type:  " + var4);
                        LOGGER.fine("KeyStore Alias: " + var6);
                     }

                     PersistentSSLInfo var8 = new PersistentSSLInfo();
                     var8.setKeyAlias(var6);
                     var8.setKeyPassword(new String(var7));
                     var8.setKeystore(var3);
                     var8.setKeystorePassword(new String(var5));
                     var8.setKeystoreType(var4);
                     var0.put("weblogic.wsee.jaxws.sslclient.PersistentSSLInfo", var8);
                  }
               } else {
                  throw new WebServiceException("Server Certificate Alias not supplied");
               }
            } else {
               throw new WebServiceException("KeyStoreFilename not supplied");
            }
         }
      }
   }

   protected HttpURLConnection openConnection(Packet var1) {
      this.context = var1;
      Object var2 = var1.invocationProperties.get("weblogic.wsee.jaxws.sslclient.PersistentSSLInfo");
      SSLSocketFactory var3 = null;
      if (var2 instanceof PersistentSSLInfo) {
         PersistentSSLInfo var4 = (PersistentSSLInfo)var2;
         var3 = SSLClientUtil.getSSLSocketFactory(var4);
         var1.invocationProperties.put("com.sun.xml.ws.transport.https.client.SSLSocketFactory", var3);
      }

      var2 = var1.invocationProperties.get("weblogic.wsee.jaxws.proxy.PersistentProxyInfo");
      ClientProxyFeature var11 = null;
      if (var2 instanceof ClientProxyFeature) {
         var11 = (ClientProxyFeature)var2;
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("host=" + var11.getProxyHost());
            LOGGER.fine("port=" + var11.getProxyPort());
         }
      }

      HttpURLConnection var5 = null;

      try {
         var5 = this.openConnection(var1.endpointAddress, var11);
      } catch (Exception var10) {
         return null;
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("ignore=" + Boolean.getBoolean("weblogic.security.SSL.ignoreHostnameVerification"));
         LOGGER.fine("vp=" + var1.invocationProperties.get("com.sun.xml.ws.client.http.HostnameVerificationProperty"));
      }

      if (var5 instanceof HttpsURLConnection || var5 instanceof weblogic.net.http.HttpsURLConnection) {
         if (Boolean.getBoolean("weblogic.security.SSL.ignoreHostnameVerification")) {
            var1.invocationProperties.put("com.sun.xml.ws.client.http.HostnameVerificationProperty", "true");
         }

         if (var5 instanceof weblogic.net.http.HttpsURLConnection) {
            weblogic.net.http.HttpsURLConnection var6 = (weblogic.net.http.HttpsURLConnection)var5;
            HostnameVerifier var7 = (HostnameVerifier)this.context.invocationProperties.get("com.sun.xml.ws.transport.https.client.hostname.verifier");
            if (var7 != null) {
               var6.setHostnameVerifier(new HostnameVerifierAdapter(var7));
            } else {
               boolean var8 = false;
               String var9 = (String)this.context.invocationProperties.get("com.sun.xml.ws.client.http.HostnameVerificationProperty");
               if (var9 != null && var9.equalsIgnoreCase("true")) {
                  var8 = true;
               }

               if (var8) {
                  var6.setHostnameVerifier(byPassAllHostnameVerifierAdapter);
               }
            }

            var3 = (SSLSocketFactory)this.context.invocationProperties.get("com.sun.xml.ws.transport.https.client.SSLSocketFactory");
            if (var3 != null && var3 instanceof SSLSocketFactory) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("set (jdk) ssl socketfactory to wls socketfactory");
               }

               var6.setSSLSocketFactory(new MySSLSocketFactory((SSLSocketFactory)var3));
            }
         }
      }

      if (Boolean.parseBoolean(System.getProperty("jaxws.transport.streaming"))) {
         WSBinding var12 = var1.getBinding();
         if (var12 != null) {
            MTOMFeature var13 = (MTOMFeature)var12.getFeature(MTOMFeature.class);
            if (var13 != null && var13.isEnabled()) {
               var5.setChunkedStreamingMode(8192);
            }
         }
      }

      return var5;
   }

   private HttpURLConnection openConnection(EndpointAddress var1, ClientProxyFeature var2) throws MalformedURLException, IOException, URISyntaxException {
      Proxy var3 = ProxyUtil.getProxy(var2, var1.getURI());
      HttpURLConnection var4;
      if (var3 != null && var3 != Proxy.NO_PROXY) {
         var4 = ProxyUtil.openConnection(var1.getURL(), var3);
         if (!excludeProxyAuthHeader) {
            ProxyUtil.setProxyAuthHeader(var4, var2);
         }

         ProxyUtil.setIgnoreSystemNonPorxyHosts(var4, var2);
      } else {
         var4 = (HttpURLConnection)var1.getURL().openConnection();
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("got http connection:" + var4 + "");
      }

      return var4;
   }

   static class MySSLSocketFactory extends weblogic.security.SSL.SSLSocketFactory {
      public MySSLSocketFactory(SSLSocketFactory var1) {
         super(var1);
      }
   }

   static class HostnameVerifierAdapter implements weblogic.security.SSL.HostnameVerifier {
      HostnameVerifier jsseHostNameVerifer;

      public HostnameVerifierAdapter(HostnameVerifier var1) {
         this.jsseHostNameVerifer = var1;
      }

      public boolean verify(String var1, SSLSession var2) {
         return this.jsseHostNameVerifer == null ? false : this.jsseHostNameVerifer.verify(var1, var2);
      }
   }

   private static class HttpClientVerifier implements HostnameVerifier {
      private HttpClientVerifier() {
      }

      public boolean verify(String var1, SSLSession var2) {
         return true;
      }

      // $FF: synthetic method
      HttpClientVerifier(Object var1) {
         this();
      }
   }
}
