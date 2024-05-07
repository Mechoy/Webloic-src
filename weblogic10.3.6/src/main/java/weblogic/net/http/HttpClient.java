package weblogic.net.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import javax.net.SocketFactory;
import weblogic.net.NetLogger;
import weblogic.socket.WeblogicSocketFactory;
import weblogic.utils.http.HttpChunkInputStream;

class HttpClient {
   public static final String HTTP_10 = "HTTP/1.0";
   public static final String HTTP_11 = "HTTP/1.1";
   private static final int httpPortNumber = 80;
   protected Proxy instProxy;
   static String proxyHost = null;
   static int proxyPort = 80;
   private static KeepAliveCache kac = new KeepAliveCache();
   protected InputStream kas;
   protected Socket serverSocket;
   protected HttpOutputStream serverOutput;
   protected InputStream serverInput;
   protected boolean usingHttp11;
   private SocketFactory socketFactory;
   public boolean usingProxy = false;
   protected String host;
   protected int port;
   protected int numReq = 0;
   private static boolean keepAliveProp = true;
   boolean keepingAlive = false;
   protected int readTimeout = -1;
   protected int connectTimeout = -1;
   protected URL url;
   protected HttpURLConnection connection;
   protected boolean ignoreSystemNonPorxyHosts = false;
   protected static weblogic.socket.utils.RegexpPool dontProxy;
   long lastUsed = System.currentTimeMillis();
   protected Object muxableSocket;
   protected Runnable scavenger;

   private static void resetProperties() {
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
            dontProxy = new weblogic.socket.utils.RegexpPool();
            StringTokenizer var1 = new StringTokenizer(var0, "|", false);
            boolean var2 = false;

            while(var1.hasMoreTokens()) {
               if (!dontProxy.add(var1.nextToken().toLowerCase())) {
                  var2 = true;
               }
            }

            if (var2) {
               NetLogger.logDuplicateExpression("http", var0, (Exception)null);
            }
         }
      } catch (SecurityException var3) {
         proxyHost = null;
         proxyPort = 80;
      }

   }

   protected HttpClient(URL var1, Proxy var2, SocketFactory var3, boolean var4, int var5, int var6, boolean var7) throws IOException {
      this.url = var1;
      this.port = var1.getPort();
      this.host = var1.getHost();
      this.setSocketFactory(var3);
      if (this.port == -1) {
         this.port = this.getDefaultPort();
      }

      this.setReadTimeout(var6);
      this.connectTimeout = var5;
      this.instProxy = var2 == null ? Proxy.NO_PROXY : var2;
      this.numReq = 0;
      this.usingHttp11 = var4;
      this.ignoreSystemNonPorxyHosts = var7;
   }

   public void setHttp11(boolean var1) {
      this.usingHttp11 = var1;
   }

   public String toString() {
      return super.toString() + "[url:" + this.url + " sock:" + this.serverSocket + " requests:" + this.numReq + " ka:" + this.keepingAlive + "]";
   }

   public boolean getHttpKeepAliveSet() {
      return keepAliveProp;
   }

   public final boolean isKeepingAlive() {
      return this.getHttpKeepAliveSet() && this.keepingAlive;
   }

   void setKeepingAlive(boolean var1) {
      this.keepingAlive = var1;
   }

   public InputStream getInputStream() {
      return this.kas != null ? this.kas : this.serverInput;
   }

   public HttpOutputStream getOutputStream() {
      return this.serverOutput;
   }

   Proxy getProxy() {
      return this.instProxy;
   }

   URL getURL() {
      return this.url;
   }

   Object getClientInfo() {
      return null;
   }

   public SocketFactory getSocketFactory() {
      return this.socketFactory == null ? SocketFactory.getDefault() : this.socketFactory;
   }

   public void setSocketFactory(SocketFactory var1) {
      this.socketFactory = var1;
   }

   protected int getDefaultPort() {
      return 80;
   }

   protected static HttpClient findInCache(URL var0, Object var1, Proxy var2) {
      return kac.get(var0, var1, var2);
   }

   static HttpClient New(URL var0, Proxy var1, SocketFactory var2, boolean var3, int var4, int var5, boolean var6, boolean var7) throws IOException {
      HttpClient var8 = null;
      if (var6) {
         var8 = findInCache(var0, (Object)null, var1);
      }

      if (var8 == null) {
         var8 = new HttpClient(var0, var1, var2, var3, var4, var5, var7);
         var8.openServer();
      } else {
         var8.url = var0;
         var8.setReadTimeout(var5 < 0 ? 0 : var5);
         var8.setHttp11(var3);
      }

      return var8;
   }

   public static void finished(HttpClient var0) {
      if (HttpURLConnection.debug) {
         HttpURLConnection.p("Closing " + var0);
      }

      if (var0.isKeepingAlive()) {
         ++var0.numReq;
         var0.kas = null;
         var0.usingHttp11 = false;
         if (var0.connection != null) {
            var0.connection.finish();
            var0.connection = null;
         }

         kac.put(var0);
      } else {
         var0.closeServer();
      }

   }

   public void setLastUsed(long var1) {
      this.lastUsed = var1;
   }

   public long getLastUsed() {
      return this.lastUsed;
   }

   protected void openServer(String var1, int var2) throws IOException {
      this.resetAsyncState();
      InetAddress[] var3 = InetAddress.getAllByName(var1);
      int var4 = 0;

      while(var4 < var3.length) {
         try {
            if (this.connectTimeout > 0) {
               SocketFactory var5 = this.getSocketFactory();
               if (var5 instanceof WeblogicSocketFactory) {
                  this.serverSocket = ((WeblogicSocketFactory)var5).createSocket(var3[var4], var2, this.connectTimeout);
               } else {
                  this.serverSocket = this.getSocketFactory().createSocket();
                  this.serverSocket.connect(new InetSocketAddress(var3[var4], var2), this.connectTimeout);
               }
            } else {
               this.serverSocket = this.getSocketFactory().createSocket(var3[var4], var2);
            }
            break;
         } catch (ConnectException var6) {
            ++var4;
         }
      }

      if (this.serverSocket == null) {
         throw new ConnectException("Tried all: '" + var3.length + "' addresses, but could not connect" + " over HTTP to server: '" + var1 + "', port: '" + var2 + "'");
      } else {
         this.serverSocket.setTcpNoDelay(true);
         if (this.readTimeout > -1) {
            this.serverSocket.setSoTimeout(this.readTimeout);
         }

         this.serverOutput = new HttpOutputStream(new BufferedOutputStream(this.serverSocket.getOutputStream()));
         this.serverInput = new BufferedInputStream(this.serverSocket.getInputStream());
      }
   }

   protected String getProtocol() {
      return "http";
   }

   protected static boolean isProxyAllowed(String var0) {
      boolean var1 = true;
      if (dontProxy != null) {
         if (dontProxy.match(var0)) {
            var1 = false;
         } else {
            try {
               InetAddress var2 = InetAddress.getByName(var0);
               String var3 = var2.getHostName();
               if (dontProxy.match(var3)) {
                  var1 = false;
               }
            } catch (UnknownHostException var4) {
            }
         }
      }

      return var1;
   }

   protected synchronized void openServer() throws IOException {
      resetProperties();
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkConnect(this.host, this.port);
      }

      if (!this.isKeepingAlive()) {
         String var2 = this.url.getProtocol();
         if (!this.getProtocol().equals(var2)) {
            throw new ProtocolException("Unsupported protocol: " + var2 + "'");
         } else if (this.instProxy != Proxy.NO_PROXY && (this.ignoreSystemNonPorxyHosts || isProxyAllowed(this.host))) {
            InetSocketAddress var3 = (InetSocketAddress)this.instProxy.address();

            try {
               this.openServer(var3.getHostName(), var3.getPort());
               this.usingProxy = true;
            } catch (IOException var5) {
               NetLogger.logIOException(var3.getHostName(), "" + var3.getPort(), this.host, "" + this.port, var5);
               throw var5;
            }
         } else {
            if (proxyHost != null && isProxyAllowed(this.host)) {
               try {
                  this.openServer(proxyHost, proxyPort);
                  this.usingProxy = true;
                  return;
               } catch (IOException var6) {
                  NetLogger.logIOException(proxyHost, "" + proxyPort, this.host, "" + this.port, var6);
               }
            }

            this.openServer(this.host, this.port);
         }
      }
   }

   public String getURLFile() {
      String var1;
      if (this.usingProxy) {
         var1 = this.url.getProtocol() + "://" + this.url.getHost();
         if (this.url.getPort() != -1) {
            var1 = var1 + ":" + this.url.getPort();
         }

         return var1 + this.url.getFile();
      } else {
         var1 = this.url.getFile();
         if (var1 == null || var1.length() == 0) {
            var1 = "/";
         }

         return var1;
      }
   }

   public void setReadTimeout(int var1) {
      this.readTimeout = var1;
      if (this.serverSocket != null && this.readTimeout > -1) {
         try {
            this.serverSocket.setSoTimeout(this.readTimeout);
         } catch (SocketException var3) {
         }
      }

   }

   void setConnection(HttpURLConnection var1) {
      this.connection = var1;
   }

   void parseHTTP(MessageHeader var1) throws IOException {
      this.keepingAlive = false;

      try {
         var1.parseHeader(this.serverInput);
      } catch (InterruptedIOException var8) {
         this.resetAsyncState();
         throw var8;
      }

      String var2 = var1.getValue(0);
      boolean var3 = var2 != null && var2.startsWith("HTTP/1.1");
      int var4 = -1;

      try {
         var4 = Integer.parseInt(var1.findValue("content-length"));
      } catch (Exception var7) {
      }

      if (this.usingHttp11) {
         this.keepingAlive = true;
      }

      String var5 = null;
      if (this.usingProxy) {
         var5 = var1.findValue("Proxy-Connection");
      }

      if (var5 == null) {
         var5 = var1.findValue("Connection");
      }

      if (var5 != null) {
         var5 = var5.toLowerCase();
         if (var5.equals("keep-alive")) {
            if (var4 > 0) {
               this.keepingAlive = true;
            }
         } else if (var5.equals("close")) {
            this.keepingAlive = false;
         }
      } else if (!var3) {
         this.keepingAlive = false;
      }

      this.kas = this.serverInput;
      if (this.usingHttp11) {
         String var6 = var1.findValue("Transfer-Encoding");
         if (var6 != null && var6.equalsIgnoreCase("chunked")) {
            this.kas = new HttpChunkInputStream(this.serverInput);
            var4 = -1;
         } else if (var4 == -1) {
            this.keepingAlive = false;
         }
      }

      this.kas = new KeepAliveStream(this, this.kas, var4);
   }

   void closeServer() {
      try {
         this.keepingAlive = false;
         this.serverSocket.close();
         this.resetAsyncState();
      } catch (Exception var2) {
      }

      if (HttpURLConnection.debug) {
         HttpURLConnection.p("Closed " + this);
      }

   }

   protected void finalize() throws Throwable {
      this.resetAsyncState();

      try {
         if (this.serverSocket != null) {
            this.serverSocket.close();
         }
      } catch (IOException var2) {
      }

      super.finalize();
   }

   Socket getSocket() {
      return this.serverSocket;
   }

   void setInputStream(InputStream var1) {
      this.serverInput = var1;
   }

   void setMuxableSocket(Object var1) {
      this.muxableSocket = var1;
   }

   Object getMuxableSocket() {
      return this.muxableSocket;
   }

   private void resetAsyncState() {
      if (this.scavenger != null) {
         try {
            this.scavenger.run();
         } catch (Throwable var2) {
         }
      }

      this.muxableSocket = null;
      this.scavenger = null;
   }

   void setScavenger(Runnable var1) {
      this.scavenger = var1;
   }

   static {
      try {
         String var0 = System.getProperty("http.keepAlive");
         if (var0 != null) {
            keepAliveProp = Boolean.valueOf(var0);
         }
      } catch (SecurityException var1) {
      }

   }
}
