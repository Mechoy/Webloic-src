package weblogic.wsee.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import weblogic.utils.encoders.BASE64Encoder;

public class MimeHeadersUtil {
   public static final String AUTH_HEADER = "Authorization";
   public static final String PROXY_AUTH_HEADER = "Proxy-Authorization";
   public static final String BASIC_REALM = "Basic";
   public static final String PROXY_SET = "proxySet";
   public static final String PROXY_HOST = "proxyHost";
   public static final String PROXY_PORT = "proxyPort";

   public static String getEncodedAuthToken(Object var0, Object var1) {
      byte[] var2;
      if (var0 instanceof String) {
         var2 = ((String)var0).getBytes();
      } else {
         var2 = (byte[])((byte[])var0);
      }

      byte[] var3;
      if (var1 instanceof String) {
         var3 = ((String)var1).getBytes();
      } else {
         var3 = (byte[])((byte[])var1);
      }

      byte[] var4 = new byte[var2.length + var3.length + 1];
      System.arraycopy(var2, 0, var4, 0, var2.length);
      var4[var2.length] = 58;
      System.arraycopy(var3, 0, var4, var2.length + 1, var3.length);
      BASE64Encoder var5 = new BASE64Encoder();
      return var5.encodeBuffer(var4);
   }

   public static String getBasicAuthHeaderValue(Object var0, Object var1) {
      String var2 = null;
      if (var0 == null || var1 == null) {
         var0 = getSysProp("javax.xml.rpc.security.auth.username");
         var1 = getSysProp("javax.xml.rpc.security.auth.password");
      }

      if (var0 != null && var1 != null) {
         var2 = "Basic " + getEncodedAuthToken(var0, var1);
      }

      return var2;
   }

   public static String getBasicProxyAuthHeaderValue(Object var0, Object var1) {
      String var2 = null;
      if ((var0 == null || var1 == null) && Boolean.getBoolean("proxySet")) {
         var0 = getSysProp("weblogic.webservice.client.proxyusername");
         var1 = getSysProp("weblogic.webservice.client.proxypassword");
      }

      if (var0 != null && var1 != null) {
         var2 = "Basic " + getEncodedAuthToken(var0, var1);
      }

      return var2;
   }

   public static Proxy getProxyFromSysProps() {
      Proxy var0 = null;
      boolean var1 = Boolean.getBoolean("proxySet");
      if (var1) {
         String var2 = System.getProperty("proxyHost");
         String var3 = System.getProperty("proxyPort");
         if (var2 != null && var3 != null) {
            var0 = new Proxy(Type.HTTP, new InetSocketAddress(var2, Integer.parseInt(var3)));
         }
      }

      return var0;
   }

   private static String getSysProp(String var0) {
      String var1 = System.getProperty(var0);
      return var1 != null && var1.length() > 0 ? var1 : null;
   }
}
