package weblogic.socket.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.StringTokenizer;
import weblogic.common.ProxyAuthenticator;
import weblogic.kernel.KernelStatus;
import weblogic.socket.SocketMuxer;
import weblogic.utils.StringUtils;
import weblogic.utils.encoders.BASE64Encoder;

public final class ProxyUtils {
   private static final int MAX_TRIES = 3;
   private static String proxyHost = null;
   private static int proxyPort = -1;
   private static String SSLProxyHost = null;
   private static int SSLProxyPort = -1;
   private static String proxyAuthStr = null;
   private static String proxyAuthClassName;
   private static RegexpPool sslDontProxy;
   private static RegexpPool dontProxy;

   public static InetAddress getProxyHost() throws UnknownHostException {
      return InetAddress.getByName(proxyHost);
   }

   public static int getProxyPort() {
      return proxyPort;
   }

   public static InetAddress getSSLProxyHost() throws UnknownHostException {
      return InetAddress.getByName(SSLProxyHost);
   }

   public static int getSSLProxyPort() {
      return SSLProxyPort;
   }

   public static String getProxyAuthStr() {
      return proxyAuthStr;
   }

   public static String getProxyAuthClassName() {
      return proxyAuthClassName;
   }

   public static RegexpPool getSSLDontProxy() {
      return sslDontProxy;
   }

   public static RegexpPool getDontProxy() {
      return dontProxy;
   }

   public static void resetSSLProperties() {
      try {
         String var0;
         try {
            var0 = System.getProperty("https.proxyPort");
            if (var0 == null) {
               var0 = System.getProperty("ssl.proxyPort");
            }

            if (var0 == null) {
               return;
            }

            SSLProxyPort = Integer.parseInt(var0);
         } catch (NumberFormatException var2) {
            return;
         }

         SSLProxyHost = System.getProperty("https.proxyHost");
         if (SSLProxyHost == null) {
            SSLProxyHost = System.getProperty("ssl.proxyHost");
         }

         var0 = System.getProperty("https.nonProxyHosts");
         if (var0 != null) {
            sslDontProxy = new RegexpPool();
            StringTokenizer var1 = new StringTokenizer(var0, "|", false);

            while(var1.hasMoreTokens()) {
               if (!sslDontProxy.add(var1.nextToken().toLowerCase(Locale.ENGLISH))) {
               }
            }
         }
      } catch (SecurityException var3) {
         SSLProxyPort = -1;
         SSLProxyHost = null;
      }

   }

   public static synchronized void resetProperties() {
      try {
         proxyHost = System.getProperty("http.proxyHost");
         proxyPort = Integer.getInteger("http.proxyPort", 80);
         if (proxyHost == null) {
            proxyHost = System.getProperty("proxyHost");
            proxyPort = Integer.getInteger("proxyPort", 80);
         }

         if (proxyHost != null && proxyHost.length() == 0) {
            proxyHost = null;
         }

         String var0 = System.getProperty("http.nonProxyHosts");
         if (var0 == null) {
            var0 = System.getProperty("nonProxyHosts");
         }

         if (var0 != null) {
            dontProxy = new RegexpPool();
            StringTokenizer var1 = new StringTokenizer(var0, "|", false);

            while(var1.hasMoreTokens()) {
               if (!dontProxy.add(var1.nextToken().toLowerCase())) {
               }
            }
         }
      } catch (SecurityException var2) {
         proxyHost = null;
         proxyPort = 80;
      }

   }

   public static Socket getProxySocket(Socket var0, String var1, int var2, String var3, int var4) throws IOException {
      int var5 = 0;

      while(true) {
         DataInputStream var8;
         String var9;
         String[] var10;
         do {
            do {
               var0.setTcpNoDelay(true);
               String var6 = null;
               if (proxyAuthStr == null) {
                  var6 = "CONNECT " + var1 + ':' + var2 + " HTTP/1.0\r\n\r\n";
               } else {
                  var6 = "CONNECT " + var1 + ':' + var2 + " HTTP/1.0\r\n" + proxyAuthStr + "\r\n\r\n";
               }

               OutputStream var7 = var0.getOutputStream();
               var7.write(var6.getBytes());
               var8 = new DataInputStream(var0.getInputStream());
               var9 = var8.readLine();
               if (var9 == null) {
                  var0.close();
                  throw new ProtocolException("Empty or no response from proxy");
               }

               var10 = StringUtils.splitCompletely(var9);
            } while(var10.length < 2);
         } while(!var10[0].equals("HTTP/1.0") && !var10[0].equals("HTTP/1.1"));

         if (var10[1].equals("200")) {
            while((var9 = var8.readLine()) != null && var9.length() > 0) {
            }

            return var0;
         }

         if (!var10[1].equals("407")) {
            var0.close();
            throw new ProtocolException("unrecognized response from proxy: '" + var9 + "'");
         }

         if (var5 > 3) {
            throw new ProtocolException("Server redirected too many times (" + var5 + ")");
         }

         String var11;
         while((var11 = var8.readLine()) != null && var11.length() > 0) {
            String[] var12 = StringUtils.split(var11, ':');
            if (var12[0].equalsIgnoreCase("Proxy-Authenticate")) {
               proxyAuthStr = getAuthInfo(var3, var4, var12[1]);
               if (proxyAuthStr == null) {
                  throw new ProtocolException("Proxy Authentication required (407)");
               }

               proxyAuthStr = "Proxy-Authorization: " + proxyAuthStr;
            }
         }

         ++var5;
      }
   }

   public static Socket getClientProxy(String var0, int var1, int var2) throws IOException {
      Socket var3 = SocketMuxer.getMuxer().newSocket(InetAddress.getByName(proxyHost), proxyPort, var2);
      return getProxySocket(var3, var0, var1, proxyHost, proxyPort);
   }

   public static Socket getSSLClientProxy(String var0, int var1, int var2) throws IOException {
      Socket var3 = SocketMuxer.getMuxer().newSocket(InetAddress.getByName(SSLProxyHost), SSLProxyPort, var2);
      return getProxySocket(var3, var0, var1, SSLProxyHost, SSLProxyPort);
   }

   public static Socket getSSLClientProxy(String var0, int var1, String var2, int var3, int var4) throws IOException {
      Socket var5 = SocketMuxer.getMuxer().newSocket(InetAddress.getByName(SSLProxyHost), SSLProxyPort, InetAddress.getByName(var2), var3, var4);
      return getProxySocket(var5, var0, var1, SSLProxyHost, SSLProxyPort);
   }

   public static boolean canProxy(InetAddress var0, boolean var1) {
      String var2 = var0.getHostName().toLowerCase();
      if (var1) {
         if (SSLProxyHost == null) {
            return false;
         } else if (!SSLProxyHost.equals(var2) && !SSLProxyHost.equals(var0.getHostAddress())) {
            if (sslDontProxy == null) {
               return true;
            } else {
               return !sslDontProxy.match(var2);
            }
         } else {
            return false;
         }
      } else if (proxyHost == null) {
         return false;
      } else if (!proxyHost.equals(var2) && !proxyHost.equals(var0.getHostAddress())) {
         if (dontProxy == null) {
            return true;
         } else {
            return !dontProxy.match(var2);
         }
      } else {
         return false;
      }
   }

   public static String getAuthInfo(String var0, int var1, String var2) throws IOException {
      if (proxyAuthClassName != null && var2 != null) {
         ProxyAuthenticator var3 = null;
         String var4 = null;
         String var5 = null;
         String var6 = var2.trim();
         int var7 = var6.indexOf(32);
         if (var7 == -1) {
            var4 = var6;
            var5 = "Login to Proxy";
         } else {
            var4 = var6.substring(0, var7);
            var5 = var6.substring(var7 + 1);
            var7 = var5.indexOf(61);
            if (var7 != -1) {
               var5 = var5.substring(var7 + 1);
            }
         }

         try {
            var3 = (ProxyAuthenticator)Class.forName(proxyAuthClassName).newInstance();
         } catch (Exception var13) {
            throw new ProtocolException("Proxy authenticator " + proxyAuthClassName + " failed: " + var13);
         }

         var3.init(var0, var1, var4, var5);
         String[] var8 = var3.getLoginAndPassword();
         if (var8 != null && var8.length == 2) {
            String var9 = var8[0] + ':' + var8[1];
            byte[] var10 = var9.getBytes();
            BASE64Encoder var11 = new BASE64Encoder();
            String var12 = "Basic " + var11.encodeBuffer(var10);
            return var12;
         } else {
            throw new ProtocolException("Proxy authentication failed");
         }
      } else {
         throw new ProtocolException("Proxy or Server Authentication Required");
      }
   }

   static {
      if (!KernelStatus.isApplet()) {
         resetSSLProperties();
         resetProperties();
         proxyAuthClassName = System.getProperty("weblogic.net.proxyAuthenticatorClassName");
      }

   }
}
