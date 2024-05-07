package weblogic.servlet.proxy;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import weblogic.servlet.FutureServletResponse;
import weblogic.utils.Debug;
import weblogic.utils.io.DataIO;
import weblogic.work.WorkAdapter;

final class ProxyRequest extends WorkAdapter {
   private static final boolean DEBUG = true;
   private static final String CONTENT_LENGTH_HEADER = "Content-Length: ";
   private static final int CONTENT_LENGTH_HEADER_INDEX = "Content-Length: ".length();
   private final SocketConnResource con;
   private final HttpServletRequest request;
   private final FutureServletResponse response;
   private final byte[] bytes;
   private final PrintStream out;
   private final PushbackInputStream in;
   private final Cookie[] cookies;

   ProxyRequest(SocketConnResource var1, HttpServletRequest var2, FutureServletResponse var3) throws IOException {
      this(var1, var2, var3, (Cookie[])null);
   }

   ProxyRequest(SocketConnResource var1, HttpServletRequest var2, FutureServletResponse var3, Cookie[] var4) throws IOException {
      this.con = var1;
      this.request = var2;
      this.response = var3;
      this.bytes = this.createRequest();
      this.out = var1.getOutputStream();
      this.in = var1.getInputStream();
      this.cookies = var4;
   }

   byte[] getBackendRequest() {
      return this.bytes;
   }

   public void run() {
      this.out.write(this.bytes, 0, this.bytes.length);
      StringBuilder var1 = new StringBuilder();
      String var2 = null;
      int var3 = -1;
      boolean var4 = false;

      try {
         var2 = ProxyUtils.readHTTPHeader(this.in);

         while(var2 != null) {
            var1.append(var2);
            if (var2.length() == 0) {
               break;
            }

            if (var3 == -1) {
               int var5 = var2.indexOf("Content-Length: ");
               if (var5 > -1) {
                  var3 = Integer.parseInt(var2.substring(CONTENT_LENGTH_HEADER_INDEX).trim());
               }
            }
         }

         byte[] var9 = var1.toString().getBytes();
         if (var3 > 0) {
            this.sendResponseWithBody(var3, var9);
            return;
         }

         this.response.getOutputStream().write(var9);

         for(int var6 = ProxyUtils.readChunkSize(this.in); var6 > 0; var6 = ProxyUtils.readChunkSize(this.in)) {
            byte[] var7 = new byte[var6 + 2];
            this.in.read(var7, 0, var6 + 2);
            this.sendResponse(var7);
         }

         this.response.send();
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   private void sendResponseWithBody(int var1, byte[] var2) throws IOException {
      int var3 = var2.length + var1;
      byte[] var4 = new byte[var3];
      System.arraycopy(var2, 0, var4, 0, var2.length);
      this.in.read(var4, var2.length, var1);
      this.sendResponse(var4);
      this.response.send();
   }

   private void sendResponse(byte[] var1) {
      try {
         this.response.getOutputStream().write(var1);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   private byte[] createRequest() {
      if (this.request.getMethod() == "GET") {
         return this.createGetRequest();
      } else if (this.request.getMethod() == "POST") {
         return this.createPostRequest();
      } else {
         return this.request.getMethod() == "HEAD" ? this.createHeadRequest() : null;
      }
   }

   private byte[] createHeadRequest() {
      StringBuilder var1 = new StringBuilder();
      var1.append("HEAD ");
      var1.append(this.request.getRequestURI());
      var1.append("HTTP/1.1\r\n");
      var1.append("Content-Length: 0\r\n");
      var1.append("Connection: Keep-Alive\r\n");
      var1.append(this.con.getHost());
      var1.append("\r\n\r\n");
      Debug.say("HEAD REQUEST " + var1.toString());
      return var1.toString().getBytes();
   }

   private byte[] createGetRequest() {
      StringBuilder var1 = new StringBuilder();
      var1.append("GET ");
      var1.append(this.request.getRequestURI());
      var1.append(" HTTP/1.1\r\n");
      var1.append(" Content-Length: 0\r\n");
      Cookie[] var2 = this.request.getCookies();
      if (var2 != null) {
         this.addCookies(var1, var2);
      }

      var1.append("Connection: Keep-Alive\r\n");
      var1.append(this.con.getHost());
      var1.append("\r\n\r\n");
      Debug.say("GET REQUEST " + var1.toString());
      return var1.toString().getBytes();
   }

   private void addCookies(StringBuilder var1, Cookie[] var2) {
      var1.append("Cookie: ");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.append(var2[var3].getName());
         var1.append("=");
         var1.append(var2[var3].getValue());
         var1.append(";");
      }

      var1.append("\r\n");
   }

   private byte[] createPostRequest() {
      int var1 = this.request.getContentLength();
      StringBuilder var2 = new StringBuilder();
      var2.append("POST ");
      var2.append(this.request.getRequestURI());
      var2.append(" HTTP/1.1\r\n");
      var2.append("Content-Length: ");
      var2.append(var1);
      var2.append("\r\n");
      Cookie[] var3 = this.request.getCookies();
      if (var3 != null) {
         this.addCookies(var2, var3);
      }

      var2.append("Connection: Keep-Alive\r\n");
      var2.append(this.con.getHost());
      var2.append("\r\n\r\n");
      Debug.say("POST REQUEST" + var2.toString());
      byte[] var4 = var2.toString().getBytes();
      byte[] var5 = new byte[var1 + var4.length];
      System.arraycopy(var4, 0, var5, 0, var4.length);

      try {
         DataIO.readFully(this.request.getInputStream(), var5, var4.length, var1);
         return var5;
      } catch (IOException var7) {
         throw new AssertionError("Unexpected exception");
      }
   }
}
