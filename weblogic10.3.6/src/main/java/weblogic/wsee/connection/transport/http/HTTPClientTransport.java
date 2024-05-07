package weblogic.wsee.connection.transport.http;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Locale;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;
import javax.net.ssl.SSLSession;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import weblogic.net.http.HttpsURLConnection;
import weblogic.utils.io.Chunk;
import weblogic.wsee.connection.ResponseListener;
import weblogic.wsee.connection.transport.ClientTransport;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.connection.transport.TransportUtil;
import weblogic.wsee.util.AccessException;
import weblogic.wsee.util.MimeHeadersUtil;
import weblogic.wsee.util.MultiByteConverterUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class HTTPClientTransport implements ClientTransport {
   private static final String JAXRPC_USER_AGENT = "Oracle JAX-RPC 1.1";
   private URL url;
   private HttpURLConnection connection;
   private static final boolean verbose = Verbose.isVerbose(HTTPClientTransport.class);
   private int readTimeout = -1;
   private int connectionTimeout = -1;

   public String getName() {
      return "http";
   }

   public String getServiceURI() {
      return this.connection.getURL().getPath();
   }

   public String getEndpointAddress() {
      return this.connection.getURL().toExternalForm();
   }

   public byte[] getSSLSessionId() {
      if (!(this.connection instanceof HttpsURLConnection)) {
         return null;
      } else {
         HttpsURLConnection var1 = (HttpsURLConnection)this.connection;
         SSLSession var2 = var1.getSSLSession();
         return var2 == null ? null : var2.getId();
      }
   }

   public void connect(String var1, TransportInfo var2) throws IOException {
      if (MultiByteConverterUtil.hasNonAsciiOrSpace(var1)) {
         if (!MultiByteConverterUtil.isEncodedUrl(var1)) {
            String var3 = MultiByteConverterUtil.encodeMultiByteURL(var1);
            this.url = new URL(var3);
         } else {
            this.url = new URL(var1);
         }
      } else {
         this.url = new URL(var1);
      }

      Proxy var5 = this.getProxy(var2);
      this.connection = this.openConnection(this.url, var5, var2);
      this.connection.setDoOutput(true);
      this.connection.setDoInput(true);
      this.connection.setRequestMethod("POST");
      if (this.connectionTimeout != -1) {
         this.connection.setConnectTimeout(this.connectionTimeout);
      }

      if (this.readTimeout != -1) {
         this.connection.setReadTimeout(this.readTimeout);
      }

      if (var2 instanceof HttpTransportInfo) {
         int var4 = ((HttpTransportInfo)var2).getChunkedStreamingMode();
         if (var4 > 0) {
            this.connection.setChunkedStreamingMode(var4);
         }
      }

      this.connection.setRequestProperty("User-Agent", "Oracle JAX-RPC 1.1");
   }

   public void disconnect() {
      if (this.connection != null) {
         this.connection.disconnect();
      }

   }

   private Proxy getProxy(TransportInfo var1) {
      Proxy var2;
      if (var1 != null && var1 instanceof HttpTransportInfo) {
         var2 = ((HttpTransportInfo)var1).getProxy();
      } else {
         var2 = MimeHeadersUtil.getProxyFromSysProps();
      }

      if (var2 == null && System.getProperty("proxySet") == null) {
         try {
            var2 = this.chooseProxy(this.url.toURI());
         } catch (URISyntaxException var4) {
         }
      }

      return var2;
   }

   private Proxy chooseProxy(URI var1) {
      ProxySelector var2 = (ProxySelector)AccessController.doPrivileged(new PrivilegedAction<ProxySelector>() {
         public ProxySelector run() {
            return ProxySelector.getDefault();
         }
      });
      if (var2 == null) {
         return Proxy.NO_PROXY;
      } else if (!var2.getClass().getName().equals("sun.net.spi.DefaultProxySelector")) {
         return null;
      } else {
         Iterator var3 = var2.select(var1).iterator();
         return var3.hasNext() ? (Proxy)var3.next() : Proxy.NO_PROXY;
      }
   }

   protected HttpURLConnection openConnection(URL var1, Proxy var2, TransportInfo var3) throws IOException {
      return var2 == null ? (HttpURLConnection)var1.openConnection() : (HttpURLConnection)var1.openConnection(var2);
   }

   public OutputStream send(MimeHeaders var1) throws IOException {
      if (verbose) {
         Verbose.say("   ** S T A R T   R E Q U E S T **");
         Verbose.say("POST " + this.url);
      }

      Iterator var2 = var1.getAllHeaders();

      while(var2.hasNext()) {
         MimeHeader var3 = (MimeHeader)var2.next();
         if (verbose) {
            Verbose.say(var3.getName() + ": " + var3.getValue());
         }

         if (var3.getName().equalsIgnoreCase("User-Agent")) {
            this.connection.setRequestProperty(var3.getName(), "Oracle JAX-RPC 1.1");
         } else {
            this.connection.addRequestProperty(var3.getName(), var3.getValue());
         }
      }

      OutputStream var4 = this.connection.getOutputStream();
      return (OutputStream)(verbose ? new TeeOutputStream(var4, Chunk.CHUNK_SIZE) : var4);
   }

   public OutputStream sendFault(MimeHeaders var1) throws IOException {
      return this.send(var1);
   }

   public InputStream receive(MimeHeaders var1) throws IOException {
      InputStream var2;
      if (verbose) {
         var2 = TransportUtil.dumpHttpInput(this.connection);
      } else {
         var2 = TransportUtil.getInputStream(this.connection);
      }

      if (!this.validContentType(this.connection.getContentType())) {
         this.handleErrorResponse(false);
      }

      this.readMimeHeaders(var1);
      return var2;
   }

   private boolean validContentType(String var1) {
      if (var1 == null) {
         return false;
      } else {
         try {
            var1 = (new ContentType(var1)).getBaseType();
         } catch (ParseException var3) {
            return false;
         }

         var1 = var1.trim().toLowerCase(Locale.ENGLISH);
         return "text/xml".startsWith(var1) || "application/soap+xml".startsWith(var1) || "multipart/related".startsWith(var1);
      }
   }

   private void readMimeHeaders(MimeHeaders var1) {
      int var2 = 1;

      String var3;
      while((var3 = this.connection.getHeaderFieldKey(var2)) != null) {
         String var4 = this.connection.getHeaderField(var2++);
         if (var4 != null && var4.length() != 0) {
            var1.addHeader(var3, var4);
         }
      }

   }

   private void handleErrorResponse(boolean var1) throws IOException {
      int var2 = this.connection.getResponseCode();
      if (!var1) {
         this.dumpErrorStream(var2);
         this.disconnect();
      }

      String var3 = " Set -Dweblogic.wsee.verbose=weblogic.wsee.connection.transport.http.HttpClientTransport to see the full response stream.";
      switch (var2) {
         case 301:
         case 302:
            throw new IOException("Redirection not supported: The server at " + this.connection.getURL() + " returned a " + var2 + " response code indicating this resource has moved.");
         case 400:
            throw new IOException("The server at url: " + this.connection.getURL() + " returned a 400 error code (received a bad " + "request with malformed syntax)." + var3);
         case 401:
            throw new AccessException("The server at " + this.connection.getURL() + " returned a 401 error code (Unauthorized).  Please check that" + " username and password are set correctly and that you have" + " permission to access the requested method.");
         case 403:
            throw new AccessException("The server at " + this.connection.getURL() + " returned a 403 error code (Forbidden).  Please ensure that your " + "URL is correct and that the correct protocol is in use.");
         case 404:
            throw new IOException("The server at " + this.connection.getURL() + " returned a 404 error code (Not Found).  Please ensure that your" + " URL is correct, and the web service has deployed " + "without error.");
         case 407:
            throw new AccessException("The server at " + this.connection.getURL() + " returned a 407 error code (Proxy Authentication Required).  Please ensure that you provide valid " + ".");
         case 500:
            throw new IOException("The server at " + this.connection.getURL() + " returned a 500 error code (Internal Server Error).  Please ensure" + " that your URL is correct, and the web service has deployed." + var3);
         default:
            throw new IOException("Received a response from url: " + this.connection.getURL() + " with HTTP status " + var2 + " and SOAP " + "content-type: " + this.connection.getContentType() + "." + var3);
      }
   }

   private void dumpErrorStream(int var1) throws IOException {
      InputStream var2 = this.connection.getErrorStream();
      byte[] var3 = new byte[200];
      if (verbose) {
         Verbose.log((Object)("Error stream with status code " + var1));
      }

      if (var2 != null) {
         while(true) {
            int var4;
            do {
               if ((var4 = var2.read(var3)) <= 0) {
                  if (verbose) {
                     Verbose.getOut().println();
                  }

                  return;
               }
            } while(!verbose);

            for(int var5 = 0; var5 < var4; ++var5) {
               Verbose.getOut().print((char)var3[var5]);
            }
         }
      }
   }

   public boolean isBlocking() {
      return true;
   }

   public void setConnectionTimeout(int var1) {
      this.connectionTimeout = var1;
   }

   public void setReadTimeout(int var1) {
      this.readTimeout = var1;
   }

   public void setResponseListener(ResponseListener var1) {
      throw new UnsupportedOperationException("Blocking transports do not support setResponseListener()");
   }

   public void confirmOneway() throws IOException {
      this.confirmOneway(false);
   }

   public void confirmOneway(boolean var1) throws IOException {
      if (verbose) {
         Verbose.log((Object)("confirming status of oneway with code " + this.connection.getResponseCode()));
      }

      if (this.connection.getResponseCode() != 202 && this.connection.getResponseCode() != 200) {
         this.handleErrorResponse(var1);
      }

      if (this.connection.getResponseCode() == 202 || this.connection.getResponseCode() == 200 && this.connection.getContentLength() == 0) {
         this.disconnect();
      }

      if (!var1) {
         if (this.connection.getResponseCode() == 200 && this.connection.getContentLength() == -1) {
            InputStream var2 = null;

            try {
               var2 = this.connection.getInputStream();
            } catch (IOException var4) {
            }

            if (var2 == null || var2.read() == -1) {
               this.disconnect();
            }
         }

      }
   }

   public int getResponseCode() throws IOException {
      return this.connection.getResponseCode();
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("url", this.url);
      var1.end();
   }

   private static class TeeOutputStream extends BufferedOutputStream {
      public TeeOutputStream(OutputStream var1, int var2) {
         super(var1, var2);
         if (HTTPClientTransport.verbose) {
            Verbose.say("");
         }

      }

      public void write(int var1) throws IOException {
         Verbose.getOut().write(var1);
         super.write(var1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         Verbose.getOut().write(var1, var2, var3);
         super.write(var1, var2, var3);
      }

      public void close() throws IOException {
         super.close();
         if (HTTPClientTransport.verbose) {
            Verbose.say("\n   ** E N D   R E Q U E S T **");
         }

      }
   }
}
