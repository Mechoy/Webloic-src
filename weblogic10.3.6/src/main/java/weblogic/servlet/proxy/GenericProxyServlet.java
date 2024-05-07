package weblogic.servlet.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.version;
import weblogic.security.SSL.SSLContext;
import weblogic.security.SSL.SSLSocketFactory;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.servlet.internal.ChunkInput;
import weblogic.servlet.internal.NakedTimerListenerBase;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.timers.Timer;
import weblogic.utils.StringUtils;
import weblogic.utils.encoders.BASE64Encoder;
import weblogic.utils.io.Chunk;

abstract class GenericProxyServlet extends HttpServlet {
   public static final String EOL = "\r\n";
   private static final String HEADER_SEPARATOR = ": ";
   private static final String HEADER_KEEP_ALIVE = "Connection: Keep-Alive\r\n";
   public static final String WEBLOGIC_BRIDGE_CONFIG_INFO = "__WebLogicBridgeConfig";
   private WebAppServletContext servletContext;
   public static final int MAX_POST_IN_MEMORY;
   protected String destHost;
   protected int destPort;
   protected String pathTrim;
   protected String pathPrepend;
   protected String defaultFileName;
   protected String trimExt;
   protected boolean verbose;
   protected boolean debugConfigInfo;
   protected boolean keepAliveEnabled;
   protected int keepAliveSecs;
   protected boolean isSecureProxy;
   protected String keyStore;
   protected String keyStoreType;
   protected String privateKeyAlias;
   protected String keyStorePasswordProperties;
   protected int socketTimeout;
   protected int maxPostSize = -1;
   protected boolean wlProxySSL;
   protected boolean wlProxySSLPassThrough = false;
   protected boolean wlProxyPassThrough = false;
   protected String cookieName;
   protected String wlCookieName;
   protected String logFileName;
   protected String httpVersion;
   protected boolean fileCaching;
   protected ProxyConnectionPool connPool;
   protected boolean inited = false;
   protected Object syncObj = new Object();
   private PrintStream out;
   private EncryptionService es = null;
   private ClearOrEncryptedService ces = null;
   private String encryptedKeyStorePasswd = null;
   private String encryptedPrivateKeyPasswd = null;

   public void init(ServletConfig var1) throws ServletException {
      if (!this.inited) {
         super.init(var1);
         this.servletContext = (WebAppServletContext)var1.getServletContext();
         this.destHost = this.getInitParameter("WebLogicHost");
         String var2 = this.getInitParameter("WebLogicPort");
         if (var2 != null) {
            this.destPort = Integer.parseInt(var2);
         }

         this.pathTrim = this.getInitParameter("PathTrim");
         if (this.pathTrim == null) {
            this.pathTrim = this.getInitParameter("pathTrim");
         }

         if (this.pathTrim != null && this.pathTrim.charAt(0) != '/') {
            this.pathTrim = "/" + this.pathTrim;
         }

         this.defaultFileName = this.getInitParameter("DefaultFileName");
         if (this.defaultFileName != null && this.defaultFileName.charAt(0) != '/') {
            this.defaultFileName = "/" + this.defaultFileName;
         }

         this.pathPrepend = this.getInitParameter("PathPrepend");
         if (this.pathPrepend == null) {
            this.pathPrepend = this.getInitParameter("pathPrepend");
         }

         if (this.pathPrepend != null && this.pathPrepend.charAt(0) != '/') {
            this.pathPrepend = "/" + this.pathPrepend;
         }

         this.trimExt = this.getInitParameter("trimExt");
         if (this.trimExt == null) {
            this.trimExt = this.getInitParameter("TrimExt");
         }

         if (this.trimExt != null && this.trimExt.charAt(0) != '.') {
            this.trimExt = "." + this.trimExt;
         }

         var2 = this.getInitParameter("FileCaching");
         this.fileCaching = isTrue(var2, true);
         var2 = this.getInitParameter("Debug");
         if (var2 == null) {
            var2 = this.getInitParameter("verbose");
         }

         this.verbose = isTrue(var2, false);
         var2 = this.getInitParameter("DebugConfigInfo");
         this.debugConfigInfo = isTrue(var2, false);
         var2 = this.getInitParameter("KeepAliveEnabled");
         this.keepAliveEnabled = isTrue(var2, true);
         var2 = this.getInitParameter("KeepAliveSecs");
         if (var2 == null) {
            this.keepAliveSecs = 20;
         } else {
            this.keepAliveSecs = Integer.parseInt(var2);
         }

         var2 = this.getInitParameter("WLIOTimeoutSecs");
         if (var2 == null) {
            this.socketTimeout = 300;
         } else {
            this.socketTimeout = Integer.parseInt(var2);
         }

         if (var2 == null) {
            var2 = this.getInitParameter("HungServerRecoverSecs");
            if (var2 != null) {
               this.socketTimeout = Integer.parseInt(var2);
            }
         }

         var2 = this.getInitParameter("SecureProxy");
         this.isSecureProxy = isTrue(var2, false);
         if (this.isSecureProxy) {
            this.keyStore = this.getInitParameter("KeyStore");
            this.keyStoreType = this.getInitParameter("KeyStoreType");
            this.privateKeyAlias = this.getInitParameter("PrivateKeyAlias");
            this.keyStorePasswordProperties = this.getInitParameter("KeyStorePasswordProperties");
            if (this.keyStorePasswordProperties != null) {
               this.es = SerializedSystemIni.getExistingEncryptionService();
               this.ces = new ClearOrEncryptedService(this.es);
               InputStream var3 = this.servletContext.getResourceAsStream(this.keyStorePasswordProperties);

               try {
                  Properties var4 = new Properties();
                  var4.load(var3);
                  this.encryptedKeyStorePasswd = var4.getProperty("KeyStorePassword");
                  this.encryptedPrivateKeyPasswd = var4.getProperty("PrivateKeyPassword");
               } catch (IOException var6) {
                  throw new ServletException("Cannot load keyStorePasswordProperties");
               }
            }
         }

         var2 = this.getInitParameter("MaxPostSize");
         if (var2 != null) {
            this.maxPostSize = Integer.parseInt(var2);
         }

         this.cookieName = this.getInitParameter("CookieName");
         if (this.cookieName == null) {
            this.cookieName = this.getInitParameter("cookieName");
         }

         this.wlCookieName = this.getInitParameter("WLCookieName");
         if (this.wlCookieName == null) {
            this.wlCookieName = this.cookieName;
         }

         if (this.wlCookieName == null) {
            this.wlCookieName = "JSESSIONID";
         }

         this.logFileName = this.getInitParameter("WLLogFile");
         File var7;
         if (this.logFileName != null) {
            var7 = new File(this.logFileName);
         } else {
            String var8 = System.getProperty("os.name");
            if (var8 != null && var8.startsWith("Windows")) {
               var7 = new File("c:/temp/wlproxy.log");
            } else {
               var7 = new File("/tmp/wlproxy.log");
            }

            var7.getParentFile().mkdirs();
         }

         try {
            this.out = new PrintStream(new FileOutputStream(var7, true));
         } catch (FileNotFoundException var5) {
            throw new ServletException("Cannot open file: " + var7.getAbsolutePath(), var5);
         }

         var2 = this.getInitParameter("WLProxySSL");
         this.wlProxySSL = isTrue(var2, false);
         var2 = this.getInitParameter("WLProxySSLPassThrough");
         this.wlProxySSLPassThrough = isTrue(var2, false);
         var2 = this.getInitParameter("WLProxyPassThrough");
         this.wlProxyPassThrough = isTrue(var2, false);
         this.initConnectionPool();
         if (this.cookieName != null) {
            this.trace("Warning: CookieName is deprecated and replaced by WLCookieName");
         }

         if (this.verbose) {
            this.trace("GenericProxyServelt: init()");
         }

         this.inited = true;
      }
   }

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getQueryString();
      if (var3 != null && this.debugConfigInfo && var3.equals("__WebLogicBridgeConfig")) {
         this.printConfigInfo(var2.getWriter());
      } else {
         int var4 = 1;
         ProxyConnection var5 = null;
         PostDataCache var6 = new PostDataCache();
         var6.readPostData(var1);
         if (var6.error) {
            if (this.verbose) {
               this.trace("Failed to read the post data.");
            }

            var6.release();
            throw new IOException("Invalid Post");
         } else {
            try {
               while(true) {
                  CharArrayWriter var8;
                  try {
                     if (var4 != 0) {
                        var5 = this.connPool.getProxyConnection(this.destHost, this.destPort, this.isSecureProxy, this.socketTimeout);
                     } else {
                        var5 = this.connPool.getNewProxyConnection(this.destHost, this.destPort, this.isSecureProxy, this.socketTimeout);
                     }

                     this.sendRequest(var1, var5, var6);
                     this.sendResponse(var1, var2, var5);
                     this.connPool.requeue(var5);
                     break;
                  } catch (HalfOpenSocketRetryException var14) {
                     this.connPool.remove(var5);
                     if (this.verbose) {
                        var8 = new CharArrayWriter();
                        var14.printStackTrace(new PrintWriter(var8));
                        this.trace(var8.toString());
                     }

                     if (var4-- <= 0) {
                        if (!var2.isCommitted()) {
                           var2.sendError(503, "Unable to connect to server");
                        }
                        break;
                     }

                     if (this.verbose) {
                        this.trace("Doing retry.");
                     }
                  } catch (WriteClientIOException var15) {
                     this.connPool.remove(var5);
                     break;
                  } catch (IOException var16) {
                     if (var5 != null) {
                        this.connPool.remove(var5);
                        if (!var2.isCommitted()) {
                           var2.sendError(500, "Internal Server Error");
                        }
                     } else {
                        var2.sendError(503, "Unable to connect to server");
                     }

                     if (this.verbose) {
                        var8 = new CharArrayWriter();
                        var16.printStackTrace(new PrintWriter(var8));
                        this.trace(var8.toString());
                     }
                     break;
                  }
               }
            } finally {
               var6.release();
            }

         }
      }
   }

   public void destroy() {
      if (this.connPool != null) {
         synchronized(this.syncObj) {
            if (this.connPool != null) {
               this.connPool.destroy();
               this.connPool = null;
            }
         }
      }
   }

   protected static boolean isTrue(String var0, boolean var1) {
      if (var0 == null) {
         return var1;
      } else {
         return var0.equalsIgnoreCase("ON") || var0.equalsIgnoreCase("true");
      }
   }

   protected void trace(String var1) {
      StringBuilder var2 = new StringBuilder(512);
      Date var3 = new Date(System.currentTimeMillis());
      var2.append("<");
      var2.append(var3.toString());
      var2.append(">");
      var2.append(' ').append('<');
      var2.append(Thread.currentThread().getName());
      var2.append('>');
      var2.append(": ");
      var2.append(var1);
      String var4 = var2.toString();
      this.out.println(var4);
      this.out.flush();
   }

   protected void printConfigInfo(PrintWriter var1) {
      var1.write("<HTML><TITLE>WEBLOGIC PROXY DEBUG INFO</TITLE>");
      var1.write("<FONT FACE=\"Tahoma\">");
      var1.write("<BODY>Query String: __WebLogicBridgeConfig");
      var1.write("<BR><BR><B>WebLogicHost:</B> <FONT COLOR=\"#0000ff\">" + this.destHost + "</FONT>");
      var1.write("<BR><B>WebLogicPort:</B> <FONT COLOR=\"#0000ff\">" + this.destPort + "</FONT>");
      if (this.cookieName != null) {
         var1.write("<BR><B>CookieName: </B><font color=#0000ff> deprecated</font>");
      }

      var1.write("<BR><B>WLCookieName: </B>" + this.wlCookieName);
      var1.write("<BR><B>Debug: </B>" + this.verbose);
      var1.write("<BR><B>DebugConfigInfo: </B>" + this.debugConfigInfo);
      var1.write("<BR><B>DefaultFileName: </B>" + this.defaultFileName);
      var1.write("<BR><B>FileCaching: </B>" + this.fileCaching);
      var1.write("<BR><B>WLIOTimeoutSecs: </B>" + this.socketTimeout);
      var1.write("<BR><B>KeepAliveEnabled: </B>" + this.keepAliveEnabled);
      var1.write("<BR><B>KeepAliveSecs: </B>" + this.keepAliveSecs);
      var1.write("<BR><B>MaxPostSize: </B>" + this.maxPostSize);
      var1.write("<BR><B>PathPrepend: </B>" + this.pathPrepend);
      var1.write("<BR><B>PathTrim: </B>" + this.pathTrim);
      var1.write("<BR><B>TrimExt: </B>" + this.trimExt);
      var1.write("<BR><B>SecureProxy: </B>" + this.isSecureProxy);
      if (this.isSecureProxy) {
         var1.write("<BR><B>KeyStore: </B>" + this.keyStore);
         var1.write("<BR><B>KeyStoreType: </B>" + this.keyStoreType);
         var1.write("<BR><B>PrivateKeyAlias: </B>" + this.privateKeyAlias);
         var1.write("<BR><B>KeyStorePasswordProperties: </B>" + this.keyStorePasswordProperties);
      }

      var1.write("<BR><B>WLLogFile: </B>" + this.logFileName);
      var1.write("<BR><B>WLProxySSL: </B>" + this.wlProxySSL);
      var1.write("<BR><B>WLProxySSLPassThrough: </B>" + this.wlProxySSLPassThrough);
      var1.write("<BR><B>WLProxyPassThrough: </B>" + this.wlProxyPassThrough);
      var1.write("<BR>_____________________________________________________");
      var1.write("<BR><BR>Last Modified: " + version.getBuildVersion());
      var1.write("</BODY></HTML>");
      var1.close();
   }

   protected void initConnectionPool() {
      if (this.connPool == null) {
         synchronized(this.syncObj) {
            if (this.connPool == null) {
               this.connPool = new ProxyConnectionPool(50);
            }
         }
      }
   }

   public void sendRequest(HttpServletRequest var1, ProxyConnection var2) throws IOException {
      this.sendRequest(var1, var2, (PostDataCache)null);
   }

   private void sendRequest(HttpServletRequest var1, ProxyConnection var2, PostDataCache var3) throws IOException {
      String var4 = this.resolveRequest(var1);
      BufferedOutputStream var5 = new BufferedOutputStream(var2.getSocket().getOutputStream());
      PrintStream var6 = new PrintStream(var5);
      InputStream var7 = null;
      if (var3 != null) {
         var7 = var3.getInputStream();
      }

      Chunk var8 = null;

      try {
         var6.print(var4);
         this.sendRequestHeaders(var1, var6, (Object)null, (Object)null);
         int var9 = var1.getContentLength();
         Object var10 = var7 != null ? var7 : var1.getInputStream();
         if (var9 > 0) {
            var8 = Chunk.getChunk();

            int var11;
            while((var11 = ((InputStream)var10).read(var8.buf, 0, var8.buf.length)) != -1) {
               try {
                  var5.write(var8.buf, 0, var11);
                  var5.flush();
               } catch (SocketException var21) {
                  if (var3 != null && var3.type != 0) {
                     throw new HalfOpenSocketRetryException(var21);
                  }
               } catch (IOException var22) {
                  throw var22;
               }

               var9 -= var11;
               if (var9 < 1) {
                  break;
               }
            }

            if (var9 != 0) {
               throw new IOException("Failed to read " + var1.getContentLength() + " bytes from the inputStream");
            }
         } else if (var9 == -1) {
            String var24 = var1.getHeader("Transfer-Encoding");
            if (var24 != null && var24.equalsIgnoreCase("Chunked")) {
               throw new IOException("Can't process chunked request.");
            }
         }
      } finally {
         if (var8 != null) {
            Chunk.releaseChunk(var8);
         }

         if (var7 != null) {
            try {
               var7.close();
            } catch (IOException var20) {
            }
         }

      }

   }

   protected void sendRequestHeaders(HttpServletRequest var1, PrintStream var2, Object var3, Object var4) {
      if (this.verbose) {
         this.trace("In-bound headers: ");
      }

      StringBuilder var5 = new StringBuilder(256);
      Enumeration var6 = var1.getHeaderNames();
      HashSet var7 = new HashSet();

      while(true) {
         String var8;
         do {
            do {
               if (!var6.hasMoreElements()) {
                  var2.print("Connection: Keep-Alive\r\n");
                  this.addRequestHeaders(var1, var2, var3, var4);
                  var2.print("\r\n");
                  var2.flush();
                  return;
               }

               var8 = (String)var6.nextElement();
            } while(this.ignoreHeader(var8));
         } while(var7.contains(var8));

         var7.add(var8);
         Enumeration var9 = var1.getHeaders(var8);

         while(var9.hasMoreElements()) {
            var5.append(var8).append(": ").append((String)var9.nextElement());
            if (this.verbose) {
               this.trace(var5.toString());
            }

            var5.append("\r\n");
            var2.print(var5.toString());
            var5.delete(0, var5.length());
         }
      }
   }

   private boolean ignoreHeader(String var1) {
      if ("Expect".equalsIgnoreCase(var1)) {
         return true;
      } else if ("Transfer-Encoding".equalsIgnoreCase(var1)) {
         return true;
      } else if ("Connection".equalsIgnoreCase(var1)) {
         return true;
      } else if (("Proxy-Remote-User".equalsIgnoreCase(var1) || "Proxy-Auth-Type".equalsIgnoreCase(var1)) && !this.wlProxyPassThrough) {
         return true;
      } else {
         return "WL-Proxy-SSL".equalsIgnoreCase(var1);
      }
   }

   protected void addRequestHeaders(HttpServletRequest var1, PrintStream var2, Object var3, Object var4) {
      StringBuilder var5 = new StringBuilder(21);
      var5.append("WL-Proxy-SSL").append(": ");
      if ("https".equalsIgnoreCase(var1.getScheme())) {
         var5.append("true");
      } else if (this.wlProxySSLPassThrough) {
         String var6 = var1.getHeader("WL-Proxy-SSL");
         if (var6 == null) {
            var5.append(this.wlProxySSL ? "true" : "false");
         } else {
            var5.append(isTrue(var6, false) ? "true" : "false");
         }
      } else {
         var5.append(this.wlProxySSL ? "true" : "false");
      }

      var5.append("\r\n");
      var2.print(var5.toString());
      if (this.pathTrim != null) {
         var2.print("WL-PATH-TRIM: " + this.pathTrim);
         var2.print("\r\n");
      }

      if (this.pathPrepend != null) {
         var2.print("WL-PATH-PREPEND: " + this.pathPrepend);
         var2.print("\r\n");
      }

      var2.print("X-WebLogic-KeepAliveSecs: " + (this.keepAliveSecs + 10));
      var2.print("\r\n");
      X509Certificate[] var12 = (X509Certificate[])((X509Certificate[])var1.getAttribute("javax.servlet.request.X509Certificate"));
      if (var12 != null) {
         var2.print("WL-Proxy-Client-Cert: ");
         ByteArrayOutputStream var7 = new ByteArrayOutputStream();

         CharArrayWriter var9;
         try {
            for(int var8 = 0; var8 < var12.length; ++var8) {
               var7.write(var12[var8].getEncoded());
            }

            byte[] var13 = var7.toByteArray();
            (new BASE64Encoder()).encodeBuffer(new ByteArrayInputStream(var13), var2);
         } catch (CertificateEncodingException var10) {
            if (this.verbose) {
               var9 = new CharArrayWriter();
               var10.printStackTrace(new PrintWriter(var9));
               this.trace("Error while dumping ssl certificate to ByteArrayOutputStream : " + var9.toString());
            }
         } catch (IOException var11) {
            if (this.verbose) {
               var9 = new CharArrayWriter();
               var11.printStackTrace(new PrintWriter(var9));
               this.trace("Error while encoding ssl certificate : " + var9.toString());
            }
         }

         var2.print("\r\n");
      }

      var2.print("WL-Proxy-Client-IP: " + var1.getRemoteAddr());
      var2.print("\r\n");
   }

   protected int readStatus(HttpServletRequest var1, HttpServletResponse var2, DataInputStream var3) throws IOException {
      return this.readStatus(var1, var2, var3, false);
   }

   protected int readStatus(HttpServletRequest var1, HttpServletResponse var2, DataInputStream var3, boolean var4) throws IOException {
      String var5 = null;

      try {
         var5 = var3.readLine();
      } catch (SocketException var9) {
         if (var4) {
            throw new HalfOpenSocketRetryException(var9);
         }
      } catch (IOException var10) {
         throw var10;
      }

      if (var5 == null) {
         if (var4) {
            throw new HalfOpenSocketRetryException("status line is null");
         } else {
            throw new IOException("status line is null");
         }
      } else {
         if (this.verbose) {
            this.trace(var5);
         }

         try {
            String[] var7 = StringUtils.splitCompletely(var5, " ");
            int var6 = Integer.parseInt(var7[1]);
            var2.setStatus(var6);
            return var6;
         } catch (IndexOutOfBoundsException var8) {
            throw new IOException("malformed status line");
         }
      }
   }

   public void sendResponse(HttpServletRequest var1, HttpServletResponse var2, ProxyConnection var3) throws IOException {
      DataInputStream var4 = new DataInputStream(new BufferedInputStream(var3.getSocket().getInputStream(), 100));
      int var5 = this.readStatus(var1, var2, var4, var3.canRecycle());
      int var6 = this.sendResponseHeaders(var2, var4, var3, (Object)null);
      if (var5 == 100) {
         var5 = this.readStatus(var1, var2, var4);
         var6 = this.sendResponseHeaders(var2, var4, var3, (Object)null);
      }

      if (var5 != 204 && var5 != 304 && var6 != 0 && !"HEAD".equalsIgnoreCase(var1.getMethod())) {
         ServletOutputStream var7 = var2.getOutputStream();
         if (var6 == -9999) {
            if (ChunkInput.readCTE((OutputStream)var7, var4) == -1) {
               var3.setCanRecycle(false);
            }
         } else {
            this.readAndWriteResponseData(var4, var7, var6);
         }

      }
   }

   void readAndWriteResponseData(DataInputStream var1, OutputStream var2, int var3) throws IOException {
      boolean var4 = true;
      Chunk var5 = Chunk.getChunk();

      try {
         int var6;
         if (var3 > 0) {
            int var7 = var3;

            while((var6 = var1.read(var5.buf, 0, Math.min(var7, var5.buf.length))) != -1) {
               if (var4) {
                  try {
                     var2.write(var5.buf, 0, var6);
                  } catch (IOException var14) {
                     var4 = false;
                     throw new WriteClientIOException("Error in writing to client");
                  }
               }

               var7 -= var6;
               if (var7 < 1) {
                  break;
               }
            }

            if (var7 != 0) {
               throw new IOException("Failed to read " + var3 + " bytes from the inputStream");
            }
         } else {
            while((var6 = var1.read(var5.buf)) != -1) {
               if (var4) {
                  try {
                     var2.write(var5.buf, 0, var6);
                     var2.flush();
                  } catch (IOException var13) {
                     var4 = false;
                     throw new WriteClientIOException("Error in writing to client");
                  }
               }
            }
         }
      } finally {
         Chunk.releaseChunk(var5);
      }

   }

   protected int sendResponseHeaders(HttpServletResponse var1, DataInputStream var2, ProxyConnection var3, Object var4) throws IOException {
      int var6 = -1;
      boolean var7 = false;
      if (this.verbose) {
         this.trace("Out-bound headers: ");
      }

      String var5;
      while((var5 = var2.readLine()) != null && var5.length() > 0) {
         var2.mark(1);
         int var8 = var2.read();
         var2.reset();
         if (var8 == 32 || var8 == 9) {
            do {
               do {
                  var5 = var5 + "\r\n" + var2.readLine();
                  var2.mark(1);
                  var8 = var2.read();
                  var2.reset();
               } while(var8 == 32);
            } while(var8 == 9);
         }

         String[] var9 = StringUtils.split(var5, ':');
         String var10 = var9[0].trim();
         String var11 = var9[1].trim();
         if (this.verbose) {
            this.trace(var10 + ": " + var11);
         }

         if (var10.equalsIgnoreCase("Set-Cookie")) {
            var1.addHeader(var10, var11);
         } else if (var10.equalsIgnoreCase("Transfer-Encoding") && var11.equalsIgnoreCase("chunked")) {
            var6 = -9999;
         } else if (var10.equalsIgnoreCase("Content-Length")) {
            if (var6 != -9999) {
               var6 = Integer.parseInt(var11);
            }

            if (var1 instanceof ServletResponseImpl) {
               ((ServletResponseImpl)var1).setHeaderInternal(var10, var11);
            } else {
               var1.setHeader(var10, var11);
            }
         } else if (var10.equalsIgnoreCase("connection")) {
            if (var11.equalsIgnoreCase("close")) {
               var3.setCanRecycle(false);
               ServletResponseImpl var14 = null;
               if (var1 instanceof ServletResponseImpl) {
                  var14 = (ServletResponseImpl)var1;
               } else {
                  var14 = ServletResponseImpl.getOriginalResponse(var1);
               }

               var14.disableKeepAlive();
            } else {
               var7 = true;
               var3.setCanRecycle(true);
            }
         } else {
            if (var10.equalsIgnoreCase("X-WebLogic-KeepAliveSecs")) {
               try {
                  int var12 = Integer.parseInt(var11);
                  if (this.keepAliveSecs > var12 - 10) {
                     this.keepAliveSecs = var12 - 10;
                  }
                  continue;
               } catch (Exception var13) {
               }
            }

            this.addResponseHeaders(var1, var10, var11, var4);
         }
      }

      return var6;
   }

   protected void addResponseHeaders(HttpServletResponse var1, String var2, String var3, Object var4) {
      if (var1 instanceof ServletResponseImpl) {
         ((ServletResponseImpl)var1).addHeaderInternal(var2, var3);
      } else {
         var1.addHeader(var2, var3);
      }

   }

   protected String resolveRequest(HttpServletRequest var1) {
      String var2 = (String)var1.getAttribute("javax.servlet.include.request_uri");
      if (var2 == null) {
         var2 = var1.getRequestURI();
      }

      int var3;
      int var4;
      if (this.trimExt != null) {
         var3 = var2.indexOf(this.trimExt);
         if (var3 > -1) {
            var4 = var2.toLowerCase().indexOf(";" + this.wlCookieName.toLowerCase() + "=");
            if (var4 != -1) {
               var2 = var2.substring(0, var3) + var2.substring(var4);
            } else {
               var2 = var2.substring(0, var3);
            }
         }
      }

      if (this.pathTrim != null) {
         var3 = var2.indexOf(this.pathTrim);
         if (var3 > -1) {
            var4 = this.pathTrim.length();
            var2 = var2.substring(0, var3) + var2.substring(var3 + var4);
         }
      }

      if ((var2.length() == 0 || var2.equals("/")) && this.defaultFileName != null) {
         var2 = var2 + this.defaultFileName;
      }

      var2 = this.pathPrepend == null ? var2 : this.pathPrepend + var2;
      String var8 = var1.getQueryString();
      ServletRequestImpl var9 = ServletRequestImpl.getOriginalRequest(var1);
      String var5 = var9.getSessionHelper().getEncodedSessionID();
      if (var5 != null) {
         var2 = var2 + ";" + this.wlCookieName + "=" + var5;
      }

      if (var8 != null && !var8.equals("")) {
         var2 = var2 + "?" + var8;
      }

      StringBuilder var6 = new StringBuilder(256);
      var6.append(var1.getMethod());
      var6.append(" ");
      var6.append(var2);
      var6.append(" ");
      if (this.httpVersion != null) {
         var6.append("HTTP/");
         var6.append(this.httpVersion);
      } else {
         var6.append(var1.getProtocol());
      }

      var6.append("\r\n");
      String var7 = var6.toString();
      if (this.verbose) {
         this.trace("===New Request===" + var7);
      }

      return var7;
   }

   protected Chunk readPostDataToMemory(HttpServletRequest var1, int var2) throws IOException {
      Chunk var3 = Chunk.getChunk();

      try {
         Chunk.chunk(var3, var1.getInputStream(), var2);
      } catch (IOException var5) {
         Chunk.releaseChunks(var3);
         throw var5;
      }

      if (this.verbose) {
         StringBuilder var4 = new StringBuilder(100);
         var4.append("Declared content-length = ");
         var4.append(var2);
         var4.append("; Actually read ");
         var4.append(Chunk.size(var3));
         this.trace(var4.toString());
      }

      if (var2 != Chunk.size(var3)) {
         Chunk.releaseChunks(var3);
         return null;
      } else {
         return var3;
      }
   }

   protected File readPostDataToFile(HttpServletRequest var1, int var2) throws IOException {
      File var3 = null;
      FileOutputStream var4 = null;
      Chunk var5 = Chunk.getChunk();

      try {
         var3 = File.createTempFile("proxy", (String)null, (File)null);
         ServletInputStream var6;
         if (var3 == null) {
            var6 = null;
            return var6;
         } else {
            var4 = new FileOutputStream(var3);

            int var7;
            for(var6 = var1.getInputStream(); var2 > 0; var2 -= var7) {
               var7 = var6.read(var5.buf, 0, var5.buf.length);
               if (var7 < 0) {
                  StringBuilder var8;
                  if (this.verbose) {
                     var8 = new StringBuilder(100);
                     var8.append("Declared content-length = ");
                     var8.append(var2);
                     var8.append("; Actually read ");
                     var8.append(var7);
                     this.trace(var8.toString());
                  }

                  try {
                     if (var4 != null) {
                        var4.close();
                     }

                     if (var3 != null) {
                        var3.delete();
                     }
                  } catch (Exception var15) {
                  }

                  var8 = null;
                  return var8;
               }

               var4.write(var5.buf, 0, var7);
            }

            var4.flush();
            var4.close();
            return var3;
         }
      } catch (IOException var16) {
         try {
            if (var4 != null) {
               var4.close();
            }

            if (var3 != null) {
               var3.delete();
            }
         } catch (Exception var14) {
         }

         throw var16;
      } finally {
         Chunk.releaseChunk(var5);
      }
   }

   static {
      MAX_POST_IN_MEMORY = Chunk.CHUNK_SIZE;
   }

   class PostDataCache {
      protected static final int NO_CACHE = 0;
      protected static final int MEMORY_CACHE = 1;
      protected static final int FILE_CACHE = 2;
      protected int type = 0;
      protected boolean error;
      private Chunk postData;
      private File postDataFile;
      private boolean alreadyRead;

      public void readPostData(HttpServletRequest var1) {
         if (!this.alreadyRead) {
            int var2 = var1.getContentLength();

            try {
               if (GenericProxyServlet.this.maxPostSize > 0 && var2 > GenericProxyServlet.this.maxPostSize) {
                  if (GenericProxyServlet.this.verbose) {
                     GenericProxyServlet.this.trace("Content Length exceeded the MaxPostSize: " + GenericProxyServlet.this.maxPostSize);
                  }

                  this.error = true;
               } else if (var2 > 0 && var2 <= GenericProxyServlet.MAX_POST_IN_MEMORY) {
                  this.type = 1;
                  this.postData = GenericProxyServlet.this.readPostDataToMemory(var1, var2);
                  if (this.postData == null) {
                     this.error = true;
                  }
               } else if (var2 > GenericProxyServlet.MAX_POST_IN_MEMORY && GenericProxyServlet.this.fileCaching) {
                  this.type = 2;
                  this.postDataFile = GenericProxyServlet.this.readPostDataToFile(var1, var2);
                  if (this.postDataFile == null) {
                     this.error = true;
                  }
               }
            } catch (IOException var8) {
               this.error = true;
               if (GenericProxyServlet.this.verbose) {
                  CharArrayWriter var4 = new CharArrayWriter();
                  var8.printStackTrace(new PrintWriter(var4));
                  GenericProxyServlet.this.trace("Failed to read post data: " + var4.toString());
               }
            } finally {
               this.alreadyRead = true;
            }
         }

      }

      public InputStream getInputStream() throws IOException {
         if (this.error) {
            return null;
         } else {
            Object var1 = null;
            switch (this.type) {
               case 0:
               default:
                  break;
               case 1:
                  var1 = new ByteArrayInputStream(this.postData.buf, 0, this.postData.end);
                  break;
               case 2:
                  var1 = new FileInputStream(this.postDataFile);
            }

            return (InputStream)var1;
         }
      }

      public void release() {
         switch (this.type) {
            case 0:
            default:
               break;
            case 1:
               Chunk.releaseChunks(this.postData);
               break;
            case 2:
               if (this.postDataFile != null) {
                  if (GenericProxyServlet.this.verbose) {
                     GenericProxyServlet.this.trace("Remove temp file: " + this.postDataFile.getAbsolutePath());
                  }

                  this.postDataFile.delete();
               }
         }

      }
   }

   class ProxyConnectionPool extends NakedTimerListenerBase {
      private Timer timer;
      private ArrayList pool;

      ProxyConnectionPool(int var2) {
         super("ProxyConnectionPool", GenericProxyServlet.this.servletContext);
         this.pool = new ArrayList(var2);
         this.startTimer();
      }

      public void destroy() {
         if (GenericProxyServlet.this.verbose) {
            GenericProxyServlet.this.trace("Destroy the connection pool");
         }

         this.stopTimer();
         synchronized(this.pool) {
            int var2 = this.pool.size() - 1;

            while(true) {
               if (var2 < 0) {
                  break;
               }

               ProxyConnection var3 = (ProxyConnection)this.pool.get(var2);
               var3.close();
               this.pool.remove(var2);
               --var2;
            }
         }

         GenericProxyServlet.this.out.close();
      }

      public void remove(ProxyConnection var1) {
         if (var1 != null) {
            var1.close();
            if (GenericProxyServlet.this.keepAliveEnabled) {
               if (GenericProxyServlet.this.verbose) {
                  GenericProxyServlet.this.trace("Remove connection from pool: " + var1);
               }

               synchronized(this.pool) {
                  this.pool.remove(var1);
               }
            }

         }
      }

      public void requeue(ProxyConnection var1) {
         if (!var1.canRecycle()) {
            if (GenericProxyServlet.this.verbose) {
               GenericProxyServlet.this.trace("Close connection: " + var1);
            }

            this.remove(var1);
         } else {
            if (GenericProxyServlet.this.keepAliveEnabled) {
               synchronized(this.pool) {
                  if (GenericProxyServlet.this.verbose) {
                     GenericProxyServlet.this.trace("Requeue connection: " + var1);
                  }

                  var1.setLastUsed(System.currentTimeMillis());
                  this.pool.add(var1);
               }
            } else {
               var1.close();
            }

         }
      }

      ProxyConnection getNewProxyConnection(String var1, int var2, boolean var3, int var4) throws IOException {
         ProxyConnection var5 = GenericProxyServlet.this.new ProxyConnection(var1, var2, var3, var4);
         if (GenericProxyServlet.this.verbose) {
            GenericProxyServlet.this.trace("Create connection: " + var5);
         }

         return var5;
      }

      public ProxyConnection getProxyConnection(String var1, int var2, boolean var3, int var4) throws IOException {
         if (GenericProxyServlet.this.keepAliveEnabled) {
            synchronized(this.pool) {
               long var6 = 0L;
               long var8 = 0L;

               for(int var10 = 0; var10 < this.pool.size(); ++var10) {
                  ProxyConnection var11 = (ProxyConnection)this.pool.get(var10);
                  if (var2 == var11.getPort() && var1.equals(var11.getHost())) {
                     this.pool.remove(var10);
                     --var10;
                     var6 = System.currentTimeMillis();
                     var8 = var6 - var11.getLastUsed();
                     if (var8 <= (long)var11.getKeepAliveMilliSecs()) {
                        if (GenericProxyServlet.this.verbose) {
                           GenericProxyServlet.this.trace("Returning recycled connection: " + var11);
                        }

                        return var11;
                     }

                     if (GenericProxyServlet.this.verbose) {
                        GenericProxyServlet.this.trace("Remove idle for '" + var8 / 1000L + "' secs: " + var11);
                     }

                     var11.close();
                  }
               }
            }
         }

         return this.getNewProxyConnection(var1, var2, var3, var4);
      }

      public ProxyConnection getProxyConnection(String var1, int var2, boolean var3) throws IOException {
         return this.getProxyConnection(var1, var2, var3, 300);
      }

      public void timerExpired(Timer var1) {
         if (GenericProxyServlet.this.keepAliveEnabled) {
            synchronized(this.pool) {
               long var3 = System.currentTimeMillis();

               for(int var5 = this.pool.size() - 1; var5 >= 0; --var5) {
                  ProxyConnection var6 = (ProxyConnection)this.pool.get(var5);
                  long var7 = var6.getLastUsed();
                  long var9 = var3 - var7;
                  if (var9 > (long)var6.getKeepAliveMilliSecs()) {
                     if (GenericProxyServlet.this.verbose) {
                        GenericProxyServlet.this.trace("Trigger remove idle for '" + var9 / 1000L + "' secs: " + var6);
                     }

                     var6.close();
                     this.pool.remove(var5);
                  }
               }

            }
         }
      }

      private void stopTimer() {
         this.timer.cancel();
         this.timerManager.stop();
      }

      private void startTimer() {
         this.timer = this.timerManager.schedule(this, 0L, 10000L);
      }
   }

   class ProxyConnection {
      private Socket sock;
      private boolean isSecure;
      private int timeout;
      private String host;
      private int port;
      private boolean closed;
      private long lastUsed;
      private boolean canRecycle;
      private int keep_alive_millisecs;

      public ProxyConnection(String var2, int var3, boolean var4, int var5) throws IOException {
         this.closed = true;
         this.lastUsed = 0L;
         this.canRecycle = true;
         this.host = var2;
         this.port = var3;
         this.isSecure = var4;
         this.timeout = var5 * 1000;
         this.setKeepAliveMilliSecs(GenericProxyServlet.this.keepAliveSecs * 1000);
         if (var4) {
            try {
               SSLContext var6 = SSLContext.getInstance("https");
               if (GenericProxyServlet.this.keyStore != null && GenericProxyServlet.this.privateKeyAlias != null && GenericProxyServlet.this.es != null) {
                  KeyStore var7 = GenericProxyServlet.this.keyStoreType == null ? KeyStore.getInstance(KeyStore.getDefaultType()) : KeyStore.getInstance(GenericProxyServlet.this.keyStoreType);
                  String var8 = null;
                  if (GenericProxyServlet.this.encryptedKeyStorePasswd != null) {
                     var8 = GenericProxyServlet.this.ces.decrypt(GenericProxyServlet.this.encryptedKeyStorePasswd);
                  }

                  var7.load(GenericProxyServlet.this.servletContext.getResourceAsStream(GenericProxyServlet.this.keyStore), var8 != null ? var8.toCharArray() : null);
                  String var9 = null;
                  if (GenericProxyServlet.this.encryptedPrivateKeyPasswd != null) {
                     var9 = GenericProxyServlet.this.ces.decrypt(GenericProxyServlet.this.encryptedPrivateKeyPasswd);
                  }

                  PrivateKey var10 = (PrivateKey)var7.getKey(GenericProxyServlet.this.privateKeyAlias, var9 != null ? var9.toCharArray() : null);
                  Certificate[] var11 = var7.getCertificateChain(GenericProxyServlet.this.privateKeyAlias);
                  var6.loadLocalIdentity(var11, var10);
               }

               SSLSocketFactory var13 = var6.getSocketFactory();
               this.sock = var13.createSocket(var2, var3);
            } catch (Exception var12) {
               throw (IOException)(new IOException(var12.getMessage())).initCause(var12);
            }
         } else {
            this.sock = new Socket(var2, var3);
         }

         this.sock.setSoTimeout(this.timeout);
         this.sock.setTcpNoDelay(true);
         this.closed = false;
      }

      public ProxyConnection(String var2, int var3, boolean var4) throws IOException {
         this(var2, var3, var4, 300);
      }

      public void close() {
         if (!this.closed) {
            try {
               this.sock.close();
               this.closed = true;
            } catch (IOException var2) {
            }

         }
      }

      public Socket getSocket() {
         return this.sock;
      }

      public int getTimeout() {
         return this.timeout;
      }

      public void setTimeout(int var1) {
         this.timeout = var1 * 1000;

         try {
            this.sock.setSoTimeout(this.timeout);
         } catch (IOException var3) {
         }

      }

      public String getHost() {
         return this.host;
      }

      public int getPort() {
         return this.port;
      }

      public void setLastUsed(long var1) {
         this.lastUsed = var1;
      }

      public long getLastUsed() {
         return this.lastUsed;
      }

      public void setCanRecycle(boolean var1) {
         this.canRecycle = var1;
      }

      public boolean canRecycle() {
         return this.canRecycle;
      }

      public void setKeepAliveMilliSecs(int var1) {
         this.keep_alive_millisecs = var1;
      }

      public int getKeepAliveMilliSecs() {
         return this.keep_alive_millisecs;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(256);
         var1.append("ProxyConnection");
         var1.append("(isSecureProxy=");
         var1.append(this.isSecure);
         var1.append("):  ");
         var1.append(this.host);
         var1.append(":");
         var1.append(this.port);
         var1.append(", keep-alive='");
         var1.append(this.keep_alive_millisecs / 1000);
         var1.append("'secs");
         return var1.toString();
      }
   }
}
