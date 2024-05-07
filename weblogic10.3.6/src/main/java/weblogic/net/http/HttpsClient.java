package weblogic.net.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import weblogic.net.NetLogger;
import weblogic.security.SSL.SSLClientInfo;
import weblogic.security.SSL.SSLSocketFactory;
import weblogic.security.acl.internal.Security;
import weblogic.security.utils.SSLContextWrapper;
import weblogic.security.utils.SSLIOContextTable;
import weblogic.security.utils.SSLSetup;
import weblogic.utils.StringUtils;

final class HttpsClient extends HttpClient {
   static final int MAX_TRIES = 3;
   static final int defaultHTTPSPort = 443;
   private static String SSLProxyHost = null;
   private static int SSLProxyPort = -1;
   private static String proxyAuthStr = null;
   private static Hashtable proxyAuthStrTable = new Hashtable();
   private String proxyAuthStrHeader = null;
   private SSLClientInfo sslInfo;
   private SSLSocketFactory sslSocketFactory;

   public SSLClientInfo getSSLClientInfo() {
      return this.sslInfo;
   }

   public void setSSLClientInfo(SSLClientInfo var1) {
      this.sslInfo = var1;
   }

   Object getClientInfo() {
      return this.getSSLClientInfo();
   }

   public SSLSocketFactory getSSLSocketFactory() {
      return this.sslSocketFactory;
   }

   public void setSSLSocketFactory(SSLSocketFactory var1) {
      this.sslSocketFactory = var1;
   }

   private void resetSSLProperties() {
      try {
         String var1;
         try {
            var1 = System.getProperty("https.proxyPort");
            if (var1 == null) {
               var1 = System.getProperty("ssl.proxyPort");
            }

            if (var1 == null) {
               return;
            }

            SSLProxyPort = Integer.parseInt(var1);
         } catch (NumberFormatException var4) {
            return;
         }

         SSLProxyHost = System.getProperty("https.proxyHost");
         if (SSLProxyHost == null) {
            SSLProxyHost = System.getProperty("ssl.proxyHost");
         }

         var1 = System.getProperty("https.nonProxyHosts");
         if (var1 != null) {
            dontProxy = new weblogic.socket.utils.RegexpPool();
            StringTokenizer var2 = new StringTokenizer(var1, "|", false);
            boolean var3 = false;

            while(var2.hasMoreTokens()) {
               if (!dontProxy.add(var2.nextToken().toLowerCase())) {
                  var3 = true;
               }
            }

            if (var3) {
               NetLogger.logDuplicateExpression("https", var1, (Exception)null);
            }
         }
      } catch (SecurityException var5) {
         SSLProxyPort = -1;
         SSLProxyHost = null;
      }

   }

   private static byte[] getConnectBytes(String var0, int var1, String var2) {
      String var3 = "CONNECT " + var0 + ':' + var1 + " HTTP/1.0";
      if (var2 != null) {
         var3 = var3 + "\r\n" + var2;
      }

      var3 = var3 + "\r\n\r\n";
      return var3.getBytes();
   }

   /** @deprecated */
   public static Socket getLayeredSocketUsingProxy(String var0, int var1) throws IOException {
      int var3 = 0;

      while(true) {
         Socket var2 = new Socket(SSLProxyHost, SSLProxyPort);
         var2.setTcpNoDelay(true);
         OutputStream var4 = var2.getOutputStream();
         var4.write(getConnectBytes(var0, var1, proxyAuthStr));
         DataInputStream var5 = new DataInputStream(var2.getInputStream());
         String var6 = var5.readLine();
         if (var6 != null && var6.length() != 0) {
            String[] var7 = StringUtils.splitCompletely(var6);
            if (var7.length >= 2 && (var7[0].equals("HTTP/1.0") || var7[0].equals("HTTP/1.1"))) {
               if (var7.length < 2 || !var7[0].equals("HTTP/1.0") && !var7[0].equals("HTTP/1.1")) {
                  continue;
               }

               if (var7[1].equals("200")) {
                  while((var6 = var5.readLine()) != null && var6.length() > 0) {
                  }

                  return var2;
               }

               if (!var7[1].equals("407")) {
                  var2.close();
                  throw new ProtocolException("unrecognized response from SSL proxy: '" + var6 + "'");
               }

               if (var3 > 3) {
                  throw new ProtocolException("Server redirected too many times (" + var3 + ")");
               }

               String var8;
               while((var8 = var5.readLine()) != null && var8.length() > 0) {
                  String[] var9 = StringUtils.split(var8, ':');
                  if (var9[0].equalsIgnoreCase("Proxy-Authenticate")) {
                     proxyAuthStr = HttpURLConnection.getAuthInfo(SSLProxyHost, SSLProxyPort, var9[1]);
                     if (proxyAuthStr == null) {
                        throw new HttpUnauthorizedException("Proxy Authentication required (407)");
                     }

                     proxyAuthStr = "Proxy-Authorization: " + proxyAuthStr;
                  }
               }

               ++var3;
               continue;
            }

            var2.close();
            throw new ProtocolException("unrecognized response from SSL proxy: '" + var6 + "'");
         }

         throw new ProtocolException("Empty response is detected from SSL proxy : '" + SSLProxyHost + ":" + SSLProxyPort + "'");
      }
   }

   static String getProxyHost() {
      return SSLProxyHost;
   }

   static int getProxyPort() {
      return SSLProxyPort;
   }

   private HttpsClient(URL var1, Proxy var2, SocketFactory var3, SSLClientInfo var4, SSLSocketFactory var5, boolean var6, int var7, int var8, String var9, boolean var10) throws IOException {
      super(var1, var2, var3, var6, var7, var8, var10);
      this.sslInfo = var4;
      this.sslSocketFactory = var5;
      if (var9 != null) {
         this.proxyAuthStrHeader = "Proxy-Authorization: " + var9;
      }

   }

   protected void finalize() throws Throwable {
      try {
         if (this.serverSocket != null && this.serverSocket instanceof SSLSocket) {
            SSLIOContextTable.removeContext((SSLSocket)this.serverSocket);
         }

         if (this.scavenger != null) {
            this.scavenger.run();
         }

         this.scavenger = null;
      } catch (Throwable var2) {
      }

      super.finalize();
   }

   protected int getDefaultPort() {
      return 443;
   }

   private SocketFactory getInternalSocketFactory() throws IOException {
      if (this.sslSocketFactory == null) {
         if (this.sslInfo == null) {
            this.sslInfo = Security.getThreadSSLClientInfo();
         }

         this.sslSocketFactory = SSLSocketFactory.getInstance(this.sslInfo);
      }

      return this.sslSocketFactory;
   }

   protected void openServer(String var1, int var2) throws IOException {
      InetAddress[] var3 = InetAddress.getAllByName(var1);
      SocketFactory var4 = this.getInternalSocketFactory();
      int var5 = 0;

      while(var5 < var3.length) {
         try {
            if (this.connectTimeout > 0) {
               this.serverSocket = this.openWrappedSSLSocket(var3[var5], var2, this.connectTimeout);
            } else {
               this.serverSocket = var4.createSocket(var3[var5], var2);
            }
            break;
         } catch (ConnectException var7) {
            ++var5;
         }
      }

      if (this.serverSocket == null) {
         throw new ConnectException("Tried all: " + var3.length + " addresses, but could not connect" + " over HTTPS to server: " + var1 + " port: " + var2);
      } else {
         this.serverSocket.setTcpNoDelay(true);
         if (this.readTimeout != -1) {
            this.serverSocket.setSoTimeout(this.readTimeout);
         }

         this.serverOutput = new HttpOutputStream(new BufferedOutputStream(this.serverSocket.getOutputStream()));
         this.serverInput = new BufferedInputStream(this.serverSocket.getInputStream());
      }
   }

   protected String getProtocol() {
      return "https";
   }

   protected synchronized void openServer() throws IOException {
      this.resetSSLProperties();
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkConnect(this.host, this.port);
      }

      if (!this.keepingAlive) {
         if (!this.url.getProtocol().equals(this.getProtocol())) {
            throw new ProtocolException("unsupported protocol: " + this.url.getProtocol());
         } else {
            if (this.instProxy != Proxy.NO_PROXY && (this.ignoreSystemNonPorxyHosts || isProxyAllowed(this.host))) {
               InetSocketAddress var2 = (InetSocketAddress)this.instProxy.address();
               String var3 = var2.getHostName();
               int var4 = var2.getPort();

               try {
                  this.makeConnectionUsingProxy(var3, var4, true);
                  return;
               } catch (IOException var7) {
               }
            }

            if (SSLProxyHost != null && isProxyAllowed(this.host)) {
               try {
                  this.makeConnectionUsingProxy(SSLProxyHost, SSLProxyPort, false);
                  return;
               } catch (IOException var6) {
               }
            }

            this.openServer(this.host, this.port);
         }
      }
   }

   private void makeConnectionUsingProxy(String var1, int var2, boolean var3) throws IOException {
      try {
         SSLContextWrapper var5 = SSLSetup.getSSLContext(this.sslInfo);
         var5.getHostnameVerifier().setProxyMapping(var1, this.host);
         var5.getTrustManager().setProxyMapping(var1, this.host);
         javax.net.ssl.SSLSocketFactory var4 = var5.getSSLSocketFactory();
         if (var3) {
            proxyAuthStr = (String)proxyAuthStrTable.get(this.instProxy);
         }

         if (proxyAuthStr == null) {
            try {
               proxyAuthStr = HttpURLConnection.getAuthInfo(var1, var2, "Basic");
               proxyAuthStr = "Proxy-Authorization: " + proxyAuthStr;
               if (var3) {
                  proxyAuthStrTable.put(this.instProxy, proxyAuthStr);
               }
            } catch (HttpUnauthorizedException var13) {
            }
         }

         int var6 = 0;

         while(true) {
            Socket var7 = this.getSocketFactory().createSocket(var1, var2);
            var7.setTcpNoDelay(true);
            OutputStream var8 = var7.getOutputStream();
            var8.write(getConnectBytes(this.host, this.port, this.proxyAuthStrHeader != null ? this.proxyAuthStrHeader : proxyAuthStr));
            DataInputStream var9 = new DataInputStream(var7.getInputStream());
            String var10 = var9.readLine();
            if (var10 != null && var10.length() != 0) {
               String[] var11 = StringUtils.splitCompletely(var10);
               if (var11.length >= 2 && (var11[0].equals("HTTP/1.0") || var11[0].equals("HTTP/1.1"))) {
                  if (var11[1].equals("200")) {
                     while((var10 = var9.readLine()) != null && var10.length() > 0) {
                     }

                     this.serverSocket = var4.createSocket(var7, this.host, this.port, true);
                     this.serverOutput = new HttpOutputStream(new BufferedOutputStream(this.serverSocket.getOutputStream()));
                     this.serverInput = new BufferedInputStream(this.serverSocket.getInputStream());
                     this.usingProxy = true;
                     return;
                  }

                  if (!var11[1].equals("407")) {
                     var7.close();
                     throw new ProtocolException("Unrecognized response from SSL proxy: '" + var10 + "'");
                  }

                  if (var6 > 3) {
                     throw new ProtocolException("Server redirected too many times (" + var6 + ")");
                  }

                  while((var10 = var9.readLine()) != null && var10.length() > 0) {
                     String[] var12 = StringUtils.split(var10, ':');
                     if (var12[0].equalsIgnoreCase("Proxy-Authenticate")) {
                        proxyAuthStr = HttpURLConnection.getAuthInfo(var1, var2, var12[1]);
                        if (proxyAuthStr == null) {
                           throw new HttpUnauthorizedException("Proxy Authentication required (407)");
                        }

                        proxyAuthStr = "Proxy-Authorization: " + proxyAuthStr;
                        if (var3) {
                           proxyAuthStrTable.put(this.instProxy, proxyAuthStr);
                        }
                     }
                  }

                  var7.close();
                  ++var6;
                  continue;
               }

               var7.close();
               throw new ProtocolException("unrecognized response from SSL proxy: '" + var10 + "'");
            }

            var7.close();
            throw new ProtocolException("Empty response is detected from SSL proxy : '" + var1 + ":" + var2 + "'");
         }
      } catch (IOException var14) {
         NetLogger.logIOException(var1, "" + var2, this.host, "" + this.port, var14);
         throw var14;
      }
   }

   public String getURLFile() {
      String var1 = this.url.getFile();
      if (var1 == null || var1.length() == 0) {
         var1 = "/";
      }

      return var1;
   }

   public SSLSession getSSLSession() {
      if (this.serverSocket != null && this.serverSocket instanceof SSLSocket) {
         SSLSocket var1 = (SSLSocket)this.serverSocket;
         return var1.getSession();
      } else {
         return null;
      }
   }

   static HttpsClient New(URL var0, Proxy var1, SocketFactory var2, SSLClientInfo var3, SSLSocketFactory var4, boolean var5, int var6, int var7, boolean var8, String var9, boolean var10) throws IOException {
      HttpsClient var11 = null;
      if (var8) {
         var11 = (HttpsClient)findInCache(var0, var3, var1);
      }

      if (var11 == null) {
         var11 = new HttpsClient(var0, var1, var2, var3, var4, var5, var6, var7, var9, var10);
         var11.openServer();
      } else {
         var11.url = var0;
         var11.setReadTimeout(var7 < 0 ? 0 : var7);
         var11.setHttp11(var5);
      }

      return var11;
   }

   private Socket openWrappedSSLSocket(InetAddress var1, int var2, int var3) throws IOException {
      Socket var5 = new Socket();
      var5.connect(new InetSocketAddress(var1, var2), var3);
      Socket var4 = this.sslSocketFactory.createSocket(var5, var1.getHostAddress(), var2, true);
      return var4;
   }
}
