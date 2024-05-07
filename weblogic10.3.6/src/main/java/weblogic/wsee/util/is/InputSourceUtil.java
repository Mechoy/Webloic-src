package weblogic.wsee.util.is;

import com.sun.xml.ws.util.JAXWSUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import org.xml.sax.InputSource;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.connection.transport.http.HttpTransportInfo;
import weblogic.wsee.connection.transport.https.DefaultClientSSLAdapter;
import weblogic.wsee.connection.transport.https.HttpsTransportInfo;
import weblogic.wsee.connection.transport.https.SSLAdapter;
import weblogic.wsee.util.MimeHeadersUtil;

public final class InputSourceUtil {
   private InputSourceUtil() {
   }

   public static InputSource loadURL(String var0) throws Exception {
      URLConnection var1 = JAXWSUtils.getEncodedURL(var0).openConnection();
      var1.setUseCaches(false);
      InputSource var2 = new InputSource(var1.getInputStream());
      var2.setSystemId(var0);
      return var2;
   }

   public static InputSource loadURL(TransportInfo var0, String var1) throws IOException {
      Object var2 = null;
      Object var3 = null;
      File var4 = new File(var1);
      if (var4.exists()) {
         var2 = new FileInputStream(var4);
      } else {
         URL var5 = JAXWSUtils.getEncodedURL(var1);
         Proxy var6 = null;
         byte[] var8 = null;
         byte[] var9 = null;
         byte[] var10 = null;
         byte[] var11 = null;
         SSLAdapter var12 = null;
         if (var0 instanceof HttpTransportInfo) {
            HttpTransportInfo var7 = (HttpTransportInfo)var0;
            var6 = var7.getProxy();
            var8 = var7.getUsername();
            var9 = var7.getPassword();
            var10 = var7.getProxyUsername();
            var11 = var7.getProxyPassword();
         } else {
            var6 = MimeHeadersUtil.getProxyFromSysProps();
         }

         if (var0 instanceof HttpsTransportInfo) {
            HttpsTransportInfo var13 = (HttpsTransportInfo)var0;
            var12 = var13.getSSLAdapter();
         }

         String var18;
         if (var5.getProtocol().equals("https")) {
            if (var12 != null) {
               var3 = var12.openConnection(var5, var6, var0);
            } else {
               DefaultClientSSLAdapter var19 = new DefaultClientSSLAdapter();
               var3 = var19.openConnection(var5, var6, var0);
            }
         } else {
            var18 = var5.getProtocol();
            if (var6 != null && !"jar".equalsIgnoreCase(var18) && !"zip".equalsIgnoreCase(var18) && !"classpath".equalsIgnoreCase(var18) && !"bundleresource".equalsIgnoreCase(var18)) {
               var3 = var5.openConnection(var6);
            } else {
               var3 = var5.openConnection();
            }
         }

         var18 = MimeHeadersUtil.getBasicAuthHeaderValue(var8, var9);
         if (var18 != null) {
            ((URLConnection)var3).addRequestProperty("Authorization", var18);
         }

         String var14 = MimeHeadersUtil.getBasicAuthHeaderValue(var10, var11);
         if (var14 != null) {
            ((URLConnection)var3).addRequestProperty("Proxy-Authorization", var14);
         }

         ((URLConnection)var3).setUseCaches(false);
         var2 = ((URLConnection)var3).getInputStream();
      }

      if (var3 instanceof HttpURLConnection) {
         String var15 = ((URLConnection)var3).getContentType();
         if (var15 != null) {
            String var17 = var15.toLowerCase(Locale.ENGLISH);
            if (var17.indexOf("/xml") >= 0 && var17.indexOf("text/plain") >= 0) {
               throw new IOException("Content-type \"" + var15 + "\" is not supported. Expected content-type is \"text/xml\", \"application/xml\" or \"text/plain\".");
            }
         }
      }

      InputSource var16 = new InputSource((InputStream)var2);
      var16.setSystemId(var1);
      return var16;
   }
}
