package weblogic.net.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import javax.net.SocketFactory;
import weblogic.common.ProxyAuthenticator;
import weblogic.utils.encoders.BASE64Encoder;
import weblogic.utils.http.HttpChunkOutputStream;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.http.HttpReasonPhraseCoder;
import weblogic.utils.io.NullInputStream;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public class HttpURLConnection extends java.net.HttpURLConnection {
   private static String userAgent;
   private static boolean strictPostRedirect = false;
   private static String proxyAuthClassName;
   private static String proxyAuthString = null;
   private static boolean http11 = true;
   private static boolean bufferPostForRetry = false;
   static final boolean debug;
   private static final String acceptString = "text/html, image/gif, image/jpeg, */*; q=.2";
   private static final int MAX_REDIRECTS = 5;
   private static final int MAX_TRIES = 3;
   private static final int MAX_LENGTH_OF_REQUEST_METHOD = 32;
   protected HttpClient http;
   protected Proxy instProxy;
   protected MessageHeader requests;
   protected MessageHeader responses;
   protected InputStream inputStream;
   protected OutputStream streamedPostOS;
   protected UnsyncByteArrayOutputStream bufferedPostOS;
   protected boolean setRequests;
   protected boolean useHttp11;
   private boolean wroteRequests;
   private SocketFactory socketFactory;
   private String redirectCookieStr;
   private static final String COOKIE_SEPARATOR = ";";
   private static boolean sendCookiesRedirect = false;
   private int readTimeout;
   private int connectTimeout;
   private int chunkLength;
   private int fixedContentLength;
   private static int defaultReadTimeout;
   protected static int defaultConnectTimeout;
   protected IOException rememberedException;
   protected boolean ignoreSystemNonPorxyHosts;
   private static SocketFactory defaultSocketFactory;

   public HttpURLConnection(URL var1) {
      this(var1, (Proxy)null);
   }

   public HttpURLConnection(URL var1, Proxy var2) {
      super(var1);
      this.inputStream = null;
      this.streamedPostOS = null;
      this.bufferedPostOS = null;
      this.setRequests = false;
      this.useHttp11 = http11;
      this.wroteRequests = false;
      this.redirectCookieStr = null;
      this.readTimeout = defaultReadTimeout;
      this.connectTimeout = defaultConnectTimeout;
      this.chunkLength = -1;
      this.fixedContentLength = -1;
      this.rememberedException = null;
      this.ignoreSystemNonPorxyHosts = false;
      this.requests = new MessageHeader();
      this.responses = new MessageHeader();
      this.instProxy = var2 == null ? Proxy.NO_PROXY : var2;
   }

   protected String getProtocol() {
      return "http";
   }

   public final void setSocketFactory(SocketFactory var1) {
      this.socketFactory = var1;
   }

   public final SocketFactory getSocketFactory() {
      return this.socketFactory != null ? this.socketFactory : defaultSocketFactory;
   }

   public static void setDefaultSocketFactory(SocketFactory var0) {
      defaultSocketFactory = var0;
   }

   protected synchronized void writeRequests() throws IOException {
      if (!this.wroteRequests) {
         this.wroteRequests = true;
         if (!this.setRequests) {
            this.doSetRequests();
         }

         this.requests.print(this.http.getOutputStream());
         this.http.getOutputStream().flush();
         if (debug) {
            p("wrote request - " + this.requests);
         }

         if (this.http != null) {
            this.http.setLastUsed(System.currentTimeMillis());
         }

         if (this.bufferedPostOS != null) {
            this.bufferedPostOS.writeTo(this.http.getOutputStream());
            this.http.getOutputStream().flush();
         }

      }
   }

   public void setIgnoreSystemNonPorxyHosts(boolean var1) {
      this.ignoreSystemNonPorxyHosts = var1;
   }

   public void connect() throws IOException {
      if (!this.connected) {
         try {
            this.http = HttpClient.New(this.url, this.instProxy, this.getSocketFactory(), this.useHttp11, this.getConnectTimeout(), this.getReadTimeout(), true, this.ignoreSystemNonPorxyHosts);
         } catch (SocketTimeoutException var2) {
            this.rememberedException = var2;
            throw var2;
         }

         this.http.setConnection(this);
         this.connected = true;
         if (debug) {
            p("connected " + this.http + " HTTP/1." + (this.useHttp11 ? "1" : "0"));
         }

      }
   }

   protected HttpClient getHttpClient() throws IOException {
      HttpClient var1 = HttpClient.New(this.url, this.instProxy, this.getSocketFactory(), this.useHttp11, this.getConnectTimeout(), this.getReadTimeout(), false, this.ignoreSystemNonPorxyHosts);
      var1.setConnection(this);
      this.connected = true;
      if (debug) {
         p("new HttpClient=" + var1);
      }

      return var1;
   }

   public synchronized OutputStream getOutputStream() throws IOException {
      try {
         if (!this.doOutput) {
            throw new ProtocolException("cannot write to a URLConnection if doOutput=false - call setDoOutput(true)");
         } else {
            if (this.method.equals("GET")) {
               this.setRequestMethod("POST");
            }

            if (("HEAD".equals(this.method) || "OPTIONS".equals(this.method) || "DELETE".equals(this.method) || "TRACE".equals(this.method)) && this.getProtocol().equals(this.url.getProtocol())) {
               throw new ProtocolException("HTTP method " + this.method + " doesn't support output");
            } else if (this.inputStream != null) {
               throw new ProtocolException("Cannot write output after reading input.");
            } else {
               String var1 = this.getRequestProperty("Content-Length");
               if (var1 == null && this.useHttp11 && this.fixedContentLength > 0) {
                  var1 = Integer.toString(this.fixedContentLength);
                  this.setRequestProperty("Content-Length", var1);
               }

               if (!bufferPostForRetry && var1 != null) {
                  this.connect();
                  this.writeRequests();
                  this.streamedPostOS = new ContentLengthOutputStream(this.http.getOutputStream(), Integer.parseInt(var1));
                  if (debug) {
                     p("using content length streaming. CL=" + var1);
                  }

                  return this.streamedPostOS;
               } else if (!bufferPostForRetry && this.useHttp11 && this.chunkLength > 0) {
                  this.setRequestProperty("Transfer-Encoding", "chunked");
                  this.connect();
                  this.writeRequests();
                  this.streamedPostOS = new HttpChunkOutputStream(this.http.getOutputStream(), this.chunkLength);
                  if (debug) {
                     p("using chunked streaming. ChunkSize=" + this.chunkLength);
                  }

                  return this.streamedPostOS;
               } else {
                  if (this.bufferedPostOS == null) {
                     this.bufferedPostOS = new UnsyncByteArrayOutputStream();
                  } else if (!this.connected) {
                     this.bufferedPostOS.reset();
                  }

                  if (debug) {
                     p("using buffered stream");
                  }

                  return this.bufferedPostOS;
               }
            }
         }
      } catch (RuntimeException var2) {
         this.disconnect();
         throw var2;
      } catch (IOException var3) {
         this.disconnect();
         throw var3;
      }
   }

   protected static String getProxyAuthString() {
      return proxyAuthString;
   }

   public static String getAuthInfo(String var0, int var1, String var2) throws IOException {
      if (debug) {
         p("getAuthInfo(" + var0 + ", " + var1 + ", " + var2 + ") called");
      }

      if (proxyAuthClassName != null && var2 != null) {
         if (debug) {
            p("using ProxyAuthenticator = " + proxyAuthClassName);
         }

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
            throw new HttpUnauthorizedException("Proxy authenticator " + proxyAuthClassName + " failed: " + var13);
         }

         var3.init(var0, var1, var4, var5);
         String[] var8 = var3.getLoginAndPassword();
         if (var8 != null && var8.length == 2) {
            String var9 = var8[0] + ':' + var8[1];
            byte[] var10 = var9.getBytes();
            BASE64Encoder var11 = new BASE64Encoder();
            String var12 = "Basic " + var11.encodeBuffer(var10);
            if (debug) {
               p("getAuthString() returning '" + var12 + "'");
            }

            return var12;
         } else {
            throw new HttpUnauthorizedException("Proxy authentication failed");
         }
      } else {
         throw new HttpUnauthorizedException("Proxy or Server Authentication Required");
      }
   }

   public synchronized InputStream getInputStream() throws IOException {
      if (!this.doInput) {
         throw new ProtocolException("Cannot read from URLConnection if doInput=false (call setDoInput(true))");
      } else if (this.responseCode >= 400 && this.rememberedException != null) {
         throw this.rememberedException;
      } else if (this.inputStream != null) {
         if (this.http != null) {
            this.http.setLastUsed(System.currentTimeMillis());
         }

         return this.inputStream;
      } else {
         if (this.streamedPostOS != null) {
            try {
               this.streamedPostOS.close();
            } catch (IOException var7) {
               this.disconnect();
               throw var7;
            }
         }

         int var1 = 0;
         int var2 = 0;
         byte var3 = 3;

         do {
            String var5;
            try {
               if (debug) {
                  p("########### connecting try " + var2 + " of " + var3);
               }

               this.connect();
               this.writeRequests();
               this.http.parseHTTP(this.responses);
            } catch (HttpUnauthorizedException var8) {
               if (debug) {
                  p("Authentication required for this request");
               }

               this.disconnect();
               this.wroteRequests = false;
               this.setRequests = false;
               this.requests = new MessageHeader();
               var5 = new String("-Authenticate: Basic Realm=WebLogic Realm");
               proxyAuthString = getAuthInfo(this.getURL().getHost(), this.getURL().getPort(), var8.getMessage() + var5);
               continue;
            } catch (ConnectException var9) {
               if (debug) {
                  p("ConnectException " + var9.getMessage());
               }

               throw var9;
            } catch (InterruptedIOException var10) {
               if (debug) {
                  p("InterruptedIOException " + var10.getMessage());
               }

               throw var10;
            } catch (IOException var11) {
               if (debug) {
                  p("IOException " + var11.getMessage());
               }

               if (this.rememberedException != null && this.rememberedException instanceof SocketTimeoutException) {
                  throw var11;
               }

               ++var2;
               if (var2 >= var3) {
                  throw var11;
               }

               this.wroteRequests = false;
               if (this.http != null) {
                  this.http.closeServer();
               }

               try {
                  Thread.sleep((long)(var2 * 100));
               } catch (InterruptedException var6) {
               }

               this.connected = false;
               this.http = this.getHttpClient();
               continue;
            }

            this.inputStream = this.http.getInputStream();
            int var4 = this.getResponseCode();
            if (debug) {
               p("response - " + this.responses);
            }

            if (var4 == 100) {
               this.handleContinueResponse();
            }

            if (this.followRedirect()) {
               this.wroteRequests = false;
               ++var1;
            } else {
               if (this.method.equals("HEAD") || this.method.equals("TRACE")) {
                  this.disconnect();
                  if (this.http != null) {
                     this.http.setLastUsed(System.currentTimeMillis());
                  }

                  return this.inputStream = new NullInputStream();
               }

               if (var4 != 407) {
                  if (var4 >= 400) {
                     var5 = HttpReasonPhraseCoder.getReasonPhrase(var4);
                     throw new FileNotFoundException("Response: '" + var4 + ": " + var5 + "' for url: '" + this.toStringWithoutUserinfo(this.url) + "'");
                  }

                  if (this.http != null) {
                     this.http.setLastUsed(System.currentTimeMillis());
                  }

                  return this.inputStream;
               }

               if (debug) {
                  p("Proxy Authentication required");
               }

               this.disconnect();
               this.wroteRequests = false;
               if (this.instProxy != Proxy.NO_PROXY) {
                  InetSocketAddress var12 = (InetSocketAddress)this.instProxy.address();
                  proxyAuthString = getAuthInfo(var12.getHostName(), var12.getPort(), this.responses.findValue("Proxy-Authenticate"));
               } else {
                  proxyAuthString = getAuthInfo(HttpClient.proxyHost, HttpClient.proxyPort, this.responses.findValue("Proxy-Authenticate"));
               }

               if (proxyAuthString != null) {
                  this.requests.set("Proxy-Authorization", proxyAuthString);
               }
            }
         } while(var1 < 5);

         throw new ProtocolException("Server redirected too many times (" + var1 + ")");
      }
   }

   void writeRequestForAsyncResponse() throws IOException {
      int var1 = 0;

      while(true) {
         try {
            this.connect();
            this.writeRequests();
            return;
         } catch (IOException var5) {
            ++var1;
            if (var1 >= 3) {
               throw var5;
            }

            this.wroteRequests = false;
            if (this.http != null) {
               this.http.closeServer();
            }

            try {
               Thread.sleep((long)(var1 * 100));
            } catch (InterruptedException var4) {
            }

            this.connected = false;
            this.http = this.getHttpClient();
         }
      }
   }

   private void handleContinueResponse() throws IOException {
      this.responseCode = -1;
      this.responses = new MessageHeader();
      this.http.parseHTTP(this.responses);
      this.inputStream = this.http.getInputStream();
      this.http.setLastUsed(System.currentTimeMillis());
      if (debug) {
         p("handled 100 Continue. status=" + this.getResponseCode());
      }

   }

   public InputStream getErrorStream() {
      if (this.http != null) {
         this.http.setLastUsed(System.currentTimeMillis());
      }

      return this.connected && this.responseCode >= 400 ? this.inputStream : null;
   }

   protected boolean followRedirect() throws IOException {
      if (!this.getInstanceFollowRedirects()) {
         return false;
      } else {
         int var1 = this.getResponseCode();
         if (var1 >= 300 && var1 <= 307 && var1 != 304) {
            String var2 = this.getHeaderField("Location");
            if (var2 == null) {
               return false;
            } else {
               URL var3 = new URL(this.getURL(), var2);
               if (!var3.getProtocol().equals(var3.getProtocol())) {
                  return false;
               } else {
                  if (debug) {
                     p("followRedirect Location=" + var2);
                  }

                  this.disconnect();
                  MessageHeader var4 = this.responses;
                  this.responses = new MessageHeader();
                  StringBuffer var5 = new StringBuffer();
                  int var6 = 0;

                  while(true) {
                     String var7 = var4.getKey(var6);
                     String var8 = var4.getValue(var6);
                     if (var7 == null && var8 == null) {
                        if (sendCookiesRedirect && var5.length() != 0) {
                           this.redirectCookieStr = var5.substring(0, var5.length() - ";".length());
                           this.requests.add("Cookie", this.redirectCookieStr);
                        }

                        if (var1 == 305) {
                           this.requests.set(0, this.method + " " + this.http.getURLFile() + " " + this.getHttpVersion(), (String)null);
                           this.connected = true;
                        } else {
                           this.url = new URL(this.url, var2);
                           if (this.method.equals("POST") && !strictPostRedirect) {
                              this.requests = new MessageHeader();
                              this.setRequests = false;
                              this.setRequestMethod("GET");
                              this.bufferedPostOS = null;
                              this.connect();
                           } else {
                              this.connect();
                              this.requests.set(0, this.method + " " + this.http.getURLFile() + " " + this.getHttpVersion(), (String)null);
                              this.requests.set("Host", this.url.getHost() + (this.url.getPort() != -1 && this.url.getPort() != 80 ? ":" + String.valueOf(this.url.getPort()) : ""));
                           }
                        }

                        return true;
                     }

                     if (var7 != null && var8 != null && var7.equalsIgnoreCase("Set-Cookie")) {
                        if (sendCookiesRedirect) {
                           var5.append(var8.split(";", 2)[0]);
                           var5.append(";");
                        } else {
                           this.responses.add(var7, var8);
                        }
                     }

                     ++var6;
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   public void disconnect() {
      this.responseCode = -1;
      if (this.http != null) {
         if (debug) {
            p("disconnect called on " + this.http);
         }

         this.http.closeServer();
         this.http = null;
         this.connected = false;
      }

   }

   boolean isConnected() {
      return this.connected;
   }

   public boolean usingProxy() {
      return this.http != null ? this.http.usingProxy : false;
   }

   public String getHeaderField(String var1) {
      try {
         this.getInputStream();
      } catch (IOException var3) {
         if (var3 instanceof SocketTimeoutException) {
            this.rememberedException = var3;
         }
      }

      return this.responses.findValue(var1);
   }

   public String getHeaderField(int var1) {
      try {
         this.getInputStream();
      } catch (IOException var3) {
      }

      return this.responses.getValue(var1);
   }

   public Map getHeaderFields() {
      try {
         this.getInputStream();
      } catch (IOException var2) {
      }

      return this.responses.getHeaders();
   }

   public String getHeaderFieldKey(int var1) {
      try {
         this.getInputStream();
      } catch (IOException var3) {
      }

      return this.responses.getKey(var1);
   }

   public void addRequestProperty(String var1, String var2) {
      if (this.connected) {
         throw new IllegalAccessError("Already connected");
      } else if (var1 == null) {
         throw new IllegalArgumentException("key is null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("value is null");
      } else {
         if (var1.length() > 0) {
            this.requests.add(var1, var2);
         }

      }
   }

   public Map getRequestProperties() {
      return this.requests.getHeaders();
   }

   public void setRequestProperty(String var1, String var2) {
      if (this.connected) {
         throw new IllegalAccessError("Already connected");
      } else if (var1 == null) {
         throw new IllegalArgumentException("key is null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("value is null");
      } else {
         if (var1.length() > 0) {
            if ("Set-Cookie".equalsIgnoreCase(var1)) {
               this.requests.add(var1, var2);
            } else {
               this.requests.set(var1, var2);
            }
         }

      }
   }

   public void setEmptyRequestProperty(String var1) {
      if (this.connected) {
         throw new IllegalAccessError("Already connected");
      } else {
         if (var1.length() > 0) {
            if ("Set-Cookie".equalsIgnoreCase(var1)) {
               this.requests.add(var1, "");
            } else {
               this.requests.set(var1, "");
            }
         }

      }
   }

   public String getRequestProperty(String var1) {
      return this.requests.findValue(var1);
   }

   String getMethod() {
      return this.method;
   }

   protected void doSetRequests() {
      this.requests.prepend(this.method + " " + this.http.getURLFile() + " " + this.getHttpVersion(), (String)null);
      this.requests.setIfNotSet("User-Agent", userAgent);
      int var1 = this.url.getPort();
      String var2 = this.url.getHost();
      if (var1 != -1 && var1 != 80) {
         var2 = var2 + ":" + var1;
      }

      this.requests.setIfNotSet("Host", var2);
      this.requests.setIfNotSet("Accept", "text/html, image/gif, image/jpeg, */*; q=.2");
      if (proxyAuthString != null) {
         this.requests.set("Proxy-Authorization", proxyAuthString);
      }

      if (sendCookiesRedirect && this.redirectCookieStr != null) {
         this.requests.setIfNotSet("Cookie", this.redirectCookieStr);
      }

      if (this.http.getHttpKeepAliveSet()) {
         if (this.http.usingProxy) {
            this.requests.setIfNotSet("Proxy-Connection", "Keep-Alive");
         } else {
            this.requests.setIfNotSet("Connection", "Keep-Alive");
         }
      }

      if (this.bufferedPostOS != null) {
         synchronized(this.bufferedPostOS) {
            this.requests.setIfNotSet("Content-Type", "application/x-www-form-urlencoded");
            this.requests.set("Content-Length", String.valueOf(this.bufferedPostOS.size()));
         }
      }

      this.setRequests = true;
   }

   /** @deprecated */
   public void setTimeout(int var1) {
      this.setReadTimeout(var1);
   }

   /** @deprecated */
   public int getTimeout() {
      return this.getReadTimeout();
   }

   public void setReadTimeout(int var1) {
      this.readTimeout = var1;
      if (this.http != null) {
         this.http.setReadTimeout(var1);
      }

   }

   public int getReadTimeout() {
      return this.readTimeout;
   }

   public void setConnectTimeout(int var1) {
      this.connectTimeout = var1;
   }

   public int getConnectTimeout() {
      return this.connectTimeout;
   }

   public void setFixedLengthStreamingMode(int var1) {
      this.fixedContentLength = var1;
   }

   public void setChunkedStreamingMode(int var1) {
      this.chunkLength = var1;
   }

   public int getResponseCode() throws IOException {
      if (this.responseCode != -1) {
         return this.responseCode;
      } else if (this.rememberedException != null && this.rememberedException instanceof SocketTimeoutException) {
         throw this.rememberedException;
      } else {
         IOException var1 = null;

         try {
            this.getInputStream();
         } catch (SocketTimeoutException var5) {
            this.rememberedException = var5;
            throw var5;
         } catch (ConnectException var6) {
            throw var6;
         } catch (InterruptedIOException var7) {
            throw var7;
         } catch (IOException var8) {
            var1 = var8;
         }

         String var2 = this.getHeaderField(0);
         if (var2 == null && var1 != null) {
            throw var1;
         } else {
            this.rememberedException = var1;

            try {
               int var3;
               for(var3 = var2.indexOf(32); var2.charAt(var3) == ' '; ++var3) {
               }

               this.responseCode = Integer.parseInt(var2.substring(var3, var3 + 3));
               this.responseMessage = var2.substring(var3 + 4).trim();
               return this.responseCode;
            } catch (Exception var9) {
               return this.responseCode;
            }
         }
      }
   }

   String getHttpVersion() {
      return this.useHttp11 ? "HTTP/1.1" : "HTTP/1.0";
   }

   private String toStringWithoutUserinfo(URL var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var1.getProtocol());
      var2.append(":");
      String var3 = var1.getPort() == -1 ? var1.getHost() : var1.getHost() + ":" + var1.getPort();
      if (var3 != null && var3.length() > 0) {
         var2.append("//");
         var2.append(var3);
      }

      if (var1.getPath() != null) {
         var2.append(var1.getPath());
      }

      if (var1.getQuery() != null) {
         var2.append('?');
         var2.append(var1.getQuery());
      }

      if (var1.getRef() != null) {
         var2.append("#");
         var2.append(var1.getRef());
      }

      return var2.toString();
   }

   public void u11() {
      if (!this.connected) {
         this.useHttp11 = true;
         if (debug) {
            p("will use HTTP/1.1");
         }
      }

   }

   void finish() {
      this.http = null;
   }

   static final void p(String var0) {
      System.out.println("[" + new Date() + "] [" + Thread.currentThread().getName() + "] " + var0);
   }

   private static boolean getBoolean(String var0, boolean var1) {
      String var2 = System.getProperty(var0);
      return var2 != null ? Boolean.valueOf(var2) : var1;
   }

   Socket getSocket() {
      return this.http.getSocket();
   }

   void setInputStream(InputStream var1) {
      this.http.setInputStream(var1);
   }

   public void setMuxableSocket(Object var1) {
      this.http.setMuxableSocket(var1);
   }

   Object getMuxableSocket() {
      return this.http.getMuxableSocket();
   }

   void setScavenger(Runnable var1) {
      this.http.setScavenger(var1);
   }

   public void setRequestMethod(String var1) throws ProtocolException {
      if (this.connected) {
         throw new ProtocolException("Can't reset method: already connected");
      } else if (var1 == null) {
         throw new ProtocolException("Invalid HTTP method: " + var1);
      } else if (var1.length() > 32) {
         throw new ProtocolException("Invalid HTTP method: " + var1 + ", maximum length allowed is " + 32);
      } else if (!HttpParsing.isTokenClean(var1)) {
         throw new ProtocolException("Invalid HTTP method: " + var1 + ", invalid character(s) was found");
      } else {
         this.method = var1;
      }
   }

   static {
      boolean var0 = false;
      userAgent = "Java" + System.getProperty("java.version");

      try {
         userAgent = System.getProperty("http.agent", userAgent);
         defaultReadTimeout = Integer.getInteger("weblogic.http.client.defaultReadTimeout", -1);
         defaultConnectTimeout = Integer.getInteger("weblogic.http.client.defaultConnectTimeout", -1);
         strictPostRedirect = getBoolean("http.strictPostRedirect", strictPostRedirect);
         sendCookiesRedirect = getBoolean("weblogic.http.sendCookiesRedirect", sendCookiesRedirect);
         var0 = getBoolean("http.debug", var0);
         http11 = getBoolean("http.11", http11);
         bufferPostForRetry = getBoolean("http.bufferPostForRetry", bufferPostForRetry);
         proxyAuthClassName = System.getProperty("weblogic.net.proxyAuthenticatorClassName");
      } catch (SecurityException var2) {
         defaultReadTimeout = -1;
         defaultConnectTimeout = -1;
      }

      debug = var0;
   }
}
