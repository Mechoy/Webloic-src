package weblogic.wsee.jaxws.proxy;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.connection.transport.https.HttpsTransportInfo;
import weblogic.wsee.util.MimeHeadersUtil;
import weblogic.wsee.util.Verbose;

public class ProxyUtil {
   private static final boolean verbose = Verbose.isVerbose(ProxyUtil.class);

   private static Proxy getProxy(ClientProxyFeature var0) {
      try {
         InetSocketAddress var1 = new InetSocketAddress(var0.getProxyHost(), var0.getProxyPort());
         return new Proxy(var0.getType(), var1);
      } catch (Throwable var2) {
         throw new RuntimeException("Bad setting for proxy connection", var2);
      }
   }

   public static Proxy getProxy(ClientProxyFeature var0, URI var1) {
      Proxy var2 = null;
      if (var0 != null) {
         if (var0.getProxyPort() < 0) {
            return Proxy.NO_PROXY;
         }

         var2 = getProxy(var0);
      } else {
         var2 = MimeHeadersUtil.getProxyFromSysProps();
         if (var2 == null && var1 != null) {
            var2 = chooseProxy(var1);
         }
      }

      return var2;
   }

   private static Proxy chooseProxy(URI var0) {
      ProxySelector var1 = (ProxySelector)AccessController.doPrivileged(new PrivilegedAction<ProxySelector>() {
         public ProxySelector run() {
            return ProxySelector.getDefault();
         }
      });
      if (var1 == null) {
         return Proxy.NO_PROXY;
      } else if (!var1.getClass().getName().equals("sun.net.spi.DefaultProxySelector")) {
         return null;
      } else {
         Iterator var2 = var1.select(var0).iterator();
         return var2.hasNext() ? (Proxy)var2.next() : Proxy.NO_PROXY;
      }
   }

   public static void setIgnoreSystemNonPorxyHosts(HttpURLConnection var0, ClientProxyFeature var1) {
      if (var1 != null && var0 instanceof weblogic.net.http.HttpURLConnection) {
         weblogic.net.http.HttpURLConnection var2 = (weblogic.net.http.HttpURLConnection)var0;
         var2.setIgnoreSystemNonPorxyHosts(var1.isIgnoreSystemNonProxyHosts());
      }

   }

   public static void setProxyAuthHeader(HttpURLConnection var0, ClientProxyFeature var1) {
      if (var0 != null) {
         String var2 = "";
         if (var1 == null) {
            var2 = MimeHeadersUtil.getBasicProxyAuthHeaderValue((Object)null, (Object)null);
         } else {
            var2 = MimeHeadersUtil.getBasicProxyAuthHeaderValue(var1.getProxyUserName(), var1.getProxyPassword());
         }

         if (var2 == null) {
            var2 = "";
         }

         var0.addRequestProperty("Proxy-Authorization", var2);
      }
   }

   public static HttpURLConnection openConnection(URL var0, Proxy var1) throws MalformedURLException, IOException, URISyntaxException {
      URL var2 = null;
      if (Boolean.getBoolean("UseSunHttpHandler")) {
         var2 = var0;
      } else {
         var2 = new URL(var0, var0.toString(), ProxyUtil.WLSURLStreamHandlerSet.getHttpURLStreamHandler(var0.getProtocol()));
      }

      HttpURLConnection var3;
      if (var1 != null) {
         var3 = (HttpURLConnection)var2.openConnection(var1);
      } else {
         var3 = (HttpURLConnection)var2.openConnection();
      }

      if (verbose) {
         Verbose.log((Object)("got sslConnection:" + var3));
      }

      return var3;
   }

   public static TransportInfo toTransportInfo(WebServiceFeature... var0) {
      if (var0 == null) {
         return null;
      } else {
         HttpsTransportInfo var1 = new HttpsTransportInfo();
         WebServiceFeature[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WebServiceFeature var5 = var2[var4];
            if (var5 instanceof ClientProxyFeature) {
               ClientProxyFeature var6 = (ClientProxyFeature)var5;
               var1.setProxy(getProxy(var6));
               var1.setProxyUsername(var6.getProxyUserName() == null ? null : var6.getProxyUserName().getBytes());
               var1.setProxyPassword(var6.getProxyPassword() == null ? null : var6.getProxyPassword().getBytes());
            }
         }

         return var1;
      }
   }

   private static class WLSURLStreamHandlerSet {
      private static final String WLS_HANDLER_PKG = "weblogic.net";
      private static URLStreamHandler httpURLStreamHandler = null;
      private static URLStreamHandler httpsURLStreamHandler = null;

      public static synchronized URLStreamHandler getHttpURLStreamHandler(String var0) throws MalformedURLException {
         if ("https".equalsIgnoreCase(var0)) {
            if (httpsURLStreamHandler == null) {
               httpsURLStreamHandler = newInstance("https");
            }

            return httpsURLStreamHandler;
         } else if ("http".equalsIgnoreCase(var0)) {
            if (httpURLStreamHandler == null) {
               httpURLStreamHandler = newInstance("http");
            }

            return httpURLStreamHandler;
         } else {
            throw new MalformedURLException("Unsupported protocol:" + var0);
         }
      }

      private static URLStreamHandler newInstance(String var0) throws WebServiceException {
         String var1 = "weblogic.net." + var0 + ".Handler";

         Class var2;
         try {
            var2 = Class.forName(var1);
         } catch (ClassNotFoundException var5) {
            throw new WebServiceException("Unable to find URLStreamHandler: " + var1);
         }

         try {
            return (URLStreamHandler)var2.newInstance();
         } catch (Exception var4) {
            throw new WebServiceException("Unable to instantiate URLStreamHandler: " + var1, var4);
         }
      }
   }
}
