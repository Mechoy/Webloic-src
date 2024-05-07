package weblogic.servlet.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import weblogic.management.runtime.SocketRuntime;
import weblogic.protocol.ServerChannel;
import weblogic.security.service.ContextHandler;
import weblogic.security.utils.SSLCertUtility;
import weblogic.security.utils.SSLCipherUtility;
import weblogic.security.utils.SSLSetup;
import weblogic.servlet.HTTPLogger;
import weblogic.socket.SocketMuxer;
import weblogic.utils.StringUtils;
import weblogic.utils.collections.SecondChanceCacheMap;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.utils.io.UnsyncByteArrayInputStream;

public final class VirtualConnection {
   private static final String X509_CERTIFICATE = "javax.servlet.request.X509Certificate";
   private static final String CIPHER_SUITE = "javax.servlet.request.cipher_suite";
   private static final String KEY_SIZE = "javax.servlet.request.key_size";
   private static final String SSL_SESSION = "weblogic.servlet.request.sslsession";
   private static final String NETWORK_CHANNEL_HTTP_PORT = "weblogic.servlet.network_channel.port";
   private static final String NETWORK_CHANNEL_HTTPS_PORT = "weblogic.servlet.network_channel.sslport";
   private final ServletRequestImpl request;
   private final MuxableSocketHTTP muxableSocket;
   private final boolean internalDispatch;
   private Socket socket;
   private int socketFD = -1;
   private InetAddress proxyHost;
   private InetAddress peer;
   private String remoteAddr;
   private String remoteHost;
   private int remotePort = -1;
   private byte[] x509ProxyClientCert;
   private boolean certExtracted;
   private final ArrayList perimeterAuthClientCert = new ArrayList(5);
   private final ArrayList perimeterAuthClientCertType = new ArrayList(5);
   private Object origCert = null;
   private boolean certsFromProxy;
   private boolean secure;
   private boolean ssl;

   VirtualConnection(ServletRequestImpl var1, MuxableSocketHTTP var2) {
      this.request = var1;
      this.muxableSocket = var2;
      this.internalDispatch = this.muxableSocket == null;
      if (!this.internalDispatch) {
         this.initNetworkChannelPorts(this.muxableSocket.getChannel());
         this.socket = this.muxableSocket.getSocket();
      }

   }

   void init() {
      if (!this.internalDispatch && this.muxableSocket.isHttps()) {
         this.initSSLAttributes((SSLSocket)this.getSocket());
      }

   }

   int getLocalPort() {
      if (this.internalDispatch) {
         return 0;
      } else {
         ServerChannel var1 = this.muxableSocket.getChannel();
         return var1.getPublicInetAddress().getPort();
      }
   }

   String getLocalAddr() {
      if (this.internalDispatch) {
         return null;
      } else {
         ServerChannel var1 = this.muxableSocket.getChannel();
         return var1.getPublicInetAddress().getAddress().getHostAddress();
      }
   }

   String getLocalName() {
      if (this.internalDispatch) {
         return null;
      } else {
         ServerChannel var1 = this.muxableSocket.getChannel();
         return var1.getPublicInetAddress().getHostName();
      }
   }

   public int getSocketFD() {
      return this.socketFD;
   }

   public void setSocketFD(int var1) {
      this.socketFD = var1;
   }

   boolean isSecure() {
      return this.secure;
   }

   public Socket getSocket() {
      return this.muxableSocket.getSocket();
   }

   public SocketRuntime getSocketRuntime() {
      return this.muxableSocket;
   }

   public ServerChannel getChannel() {
      return this.muxableSocket.getChannel();
   }

   public ContextHandler getContextHandler() {
      return this.muxableSocket;
   }

   public ArrayList getPerimeterAuthClientCert() {
      return this.perimeterAuthClientCert;
   }

   public ArrayList getPerimeterAuthClientCertType() {
      return this.perimeterAuthClientCertType;
   }

   public byte[] getX509ProxyClientCert() {
      return this.x509ProxyClientCert;
   }

   void setX509ProxyClientCert(byte[] var1) {
      this.x509ProxyClientCert = var1;
   }

   void reset() {
      this.perimeterAuthClientCert.clear();
      this.perimeterAuthClientCertType.clear();
      this.x509ProxyClientCert = null;
      this.certExtracted = false;
      this.proxyHost = null;
      this.remoteAddr = null;
      this.remoteHost = null;
      this.remotePort = -1;
      this.peer = null;
      this.secure = this.ssl;
      if (this.certsFromProxy) {
         this.certsFromProxy = false;
         this.request.setAttribute("javax.servlet.request.X509Certificate", this.origCert);
      }

   }

   private void initNetworkChannelPorts(ServerChannel var1) {
      if (var1.supportsTLS()) {
         this.request.setAttribute("weblogic.servlet.network_channel.sslport", new Integer(var1.getPublicPort()));
      } else {
         this.request.setAttribute("weblogic.servlet.network_channel.port", new Integer(var1.getPublicPort()));
      }

   }

   private void initSSLAttributes(SSLSocket var1) {
      this.ssl = this.secure = true;
      SSLSession var2 = var1.getSession();
      String var3 = var2.getCipherSuite();
      this.request.setAttribute("weblogic.servlet.request.sslsession", var2);
      this.request.setAttribute("javax.servlet.request.cipher_suite", var3);
      int var4 = SSLCipherUtility.getKeySize(var3);
      if (var4 >= 0) {
         this.request.setAttribute("javax.servlet.request.key_size", new Integer(var4));
      }

      try {
         Certificate[] var5 = var2.getPeerCertificates();
         if (var5 != null) {
            this.request.setAttribute("javax.servlet.request.X509Certificate", SSLCertUtility.toJavaX5092(var5));
         }
      } catch (IOException var6) {
         SSLSetup.info(var6, "Exception processing certificates: " + var6.getMessage());
      }

   }

   void initCerts() {
      if (this.request.getContext().getConfigManager().isClientCertProxyEnabled()) {
         this.initProxyClientCert();
      } else {
         this.setX509ProxyClientCert((byte[])null);
      }

   }

   private void initProxyClientCert() {
      if (!this.certExtracted) {
         this.certExtracted = true;
         if (this.x509ProxyClientCert != null) {
            try {
               UnsyncByteArrayInputStream var1 = new UnsyncByteArrayInputStream(this.x509ProxyClientCert);
               byte[] var2 = (new BASE64Decoder()).decodeBuffer(var1);
               UnsyncByteArrayInputStream var3 = new UnsyncByteArrayInputStream(var2);
               X509Certificate[] var4 = new X509Certificate[1];
               CertificateFactory var5 = CertificateFactory.getInstance("X.509");
               var4[0] = (X509Certificate)var5.generateCertificate(var3);
               this.saveOrigCert();
               this.request.setAttribute("javax.servlet.request.X509Certificate", var4);
            } catch (Exception var6) {
               HTTPLogger.logIgnoringClientCert("WL-Proxy-Client-Cert", var6);
               this.x509ProxyClientCert = null;
            }
         }

      }
   }

   void processProxyHeader(String var1, byte[] var2) {
      int var3 = var1.length();
      switch (var3) {
         case 12:
            if (ServletRequestImpl.eq(var1, "WL-Proxy-SSL", 12)) {
               String var4 = StringUtils.getString(var2);
               if ("true".equalsIgnoreCase(var4)) {
                  if (this.muxableSocket.getHttpServer().isWeblogicPluginEnabled()) {
                     this.secure = true;
                  } else {
                     this.secure = false;
                  }
               } else if ("false".equalsIgnoreCase(var4)) {
                  this.secure = false;
               }
            }
            break;
         case 18:
            if (ServletRequestImpl.eq(var1, "WL-Proxy-Client-IP", 18)) {
               try {
                  this.proxyHost = InetAddress.getByName(StringUtils.getString(var2));
               } catch (UnknownHostException var5) {
                  if (HTTPDebugLogger.isEnabled()) {
                     HTTPDebugLogger.debug("Failed to process the client header WL-Proxy-Client-IP:" + StringUtils.getString(var2), var5);
                  }
               }
            }
            break;
         case 20:
            if (ServletRequestImpl.eq(var1, "WL-Proxy-Client-Cert", 20)) {
               this.setX509ProxyClientCert(var2);
            }
      }

      if (var3 > 16 && ServletRequestImpl.eq(var1, "WL-Proxy-Client-", 16) && var2 != null && var2.length > 0) {
         this.getPerimeterAuthClientCertType().add(var1.substring(16));
         this.getPerimeterAuthClientCert().add(var2);
      }

   }

   private void saveOrigCert() {
      this.certsFromProxy = true;
      this.origCert = this.request.getAttribute("javax.servlet.request.X509Certificate");
   }

   String getRemoteAddr() {
      if (this.remoteAddr != null) {
         return this.remoteAddr;
      } else if (this.internalDispatch) {
         return null;
      } else {
         if (this.muxableSocket.getHttpServer().isWeblogicPluginEnabled()) {
            this.peer = this.proxyHost;
         }

         if (this.peer == null && (this.peer = this.socket.getInetAddress()) == null) {
            return null;
         } else {
            InetAddressCacheRecord var1 = VirtualConnection.InetAddressCacheRecord.getInstance(this.peer);
            this.remoteAddr = var1.getHostAddress();
            return this.remoteAddr;
         }
      }
   }

   String getRemoteHost() {
      if (this.remoteHost != null) {
         return this.remoteHost;
      } else {
         this.getRemoteAddr();
         if (this.remoteAddr == null) {
            return "";
         } else {
            if (this.peer == null) {
               try {
                  this.peer = InetAddress.getByName(this.remoteAddr);
               } catch (UnknownHostException var2) {
                  return this.getRemoteAddr();
               }
            }

            InetAddressCacheRecord var1 = VirtualConnection.InetAddressCacheRecord.getInstance(this.peer);
            this.remoteHost = var1.getHostName();
            return this.remoteHost;
         }
      }
   }

   public int getRemotePort() {
      if (this.remotePort != -1) {
         return this.remotePort;
      } else if (this.internalDispatch) {
         return -1;
      } else {
         if (this.remotePort == -1) {
            this.remotePort = this.socket.getPort();
         }

         return this.remotePort;
      }
   }

   boolean isInternalDispatch() {
      return this.internalDispatch;
   }

   void deliverHasException(IOException var1) {
      SocketMuxer.getMuxer().deliverHasException(this.muxableSocket.getSocketFilter(), var1);
   }

   void requeue() {
      this.muxableSocket.requeue();
   }

   void close() {
      SocketMuxer.getMuxer().deliverEndOfStream(this.muxableSocket.getSocketFilter());
   }

   static final class InetAddressCacheRecord {
      private static final SecondChanceCacheMap cache = new SecondChanceCacheMap(317);
      private final InetAddress address;
      private String remoteHost;
      private String remoteIP;

      static InetAddressCacheRecord getInstance(InetAddress var0) {
         InetAddressCacheRecord var1 = (InetAddressCacheRecord)cache.get(var0);
         if (var1 == null) {
            var1 = new InetAddressCacheRecord(var0);
            cache.put(var0, var1);
         }

         return var1;
      }

      InetAddressCacheRecord(InetAddress var1) {
         this.address = var1;
      }

      String getHostName() {
         if (this.remoteHost == null) {
            this.remoteHost = this.address.getHostName();
         }

         return this.remoteHost;
      }

      String getHostAddress() {
         if (this.remoteIP == null) {
            this.remoteIP = this.address.getHostAddress();
         }

         return this.remoteIP;
      }
   }
}
