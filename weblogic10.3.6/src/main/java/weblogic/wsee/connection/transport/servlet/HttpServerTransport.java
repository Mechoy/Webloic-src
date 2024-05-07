package weblogic.wsee.connection.transport.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import weblogic.wsee.connection.transport.ServerTransport;
import weblogic.wsee.connection.transport.TransportUtil;
import weblogic.wsee.util.ServletDebugUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.VerboseOutputStream;

public class HttpServerTransport implements ServerTransport {
   private HttpServletRequest request;
   private HttpServletResponse response;
   private String serviceURI;
   private static final boolean verbose = Verbose.isVerbose(HttpServerTransport.class);

   public HttpServerTransport(HttpServletRequest var1, HttpServletResponse var2) {
      this.request = var1;
      this.response = var2;
      this.serviceURI = var1.getContextPath() + var1.getServletPath();
   }

   public String getServiceURI() {
      return this.serviceURI;
   }

   public String getEndpointAddress() {
      StringBuffer var1 = new StringBuffer(this.request.getScheme());
      var1.append("://");
      var1.append(this.request.getServerName());
      var1.append(":");
      var1.append(this.request.getServerPort());
      var1.append(this.serviceURI);
      return var1.toString();
   }

   public HttpServletRequest getRequest() {
      return this.request;
   }

   public HttpServletResponse getResponse() {
      return this.response;
   }

   public String getName() {
      return "HttpServerTransport";
   }

   public OutputStream send(MimeHeaders var1) throws IOException {
      assert var1 != null;

      if (verbose) {
         Verbose.say("     ** S T A R T   R E S P O N S E **");
      }

      Iterator var2 = var1.getAllHeaders();

      while(var2.hasNext()) {
         MimeHeader var3 = (MimeHeader)var2.next();
         this.response.addHeader(var3.getName(), var3.getValue());
         if (verbose) {
            Verbose.say(var3.getName() + ": " + var3.getValue());
         }
      }

      if (verbose) {
         ServletDebugUtil.printResponse(this.response);
         return new VerboseOutputStream(this.response.getOutputStream());
      } else {
         return this.response.getOutputStream();
      }
   }

   public OutputStream sendGeneralFault(MimeHeaders var1) throws IOException {
      this.response.setStatus(500);
      return this.send(var1);
   }

   public OutputStream sendAuthorizationFault(MimeHeaders var1) throws IOException {
      this.response.setStatus(403);
      return this.send(var1);
   }

   public OutputStream sendAuthorizationRequiredFault(MimeHeaders var1) throws IOException {
      this.response.setStatus(401);
      return this.send(var1);
   }

   public InputStream receive(MimeHeaders var1) throws IOException {
      assert var1 != null;

      Enumeration var2 = this.request.getHeaderNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         String var4 = this.request.getHeader(var3);
         var1.addHeader(var3, var4);
      }

      Object var5 = this.request.getInputStream();
      if (verbose) {
         Verbose.say("     ** S T A R T   R E Q U E S T **");
         ServletDebugUtil.printRequest(this.request);
         var5 = TransportUtil.dumpInput((InputStream)var5);
         Verbose.say("");
         Verbose.say("     ** E N D   R E Q U E S T **");
      }

      return (InputStream)var5;
   }

   public boolean isUserInRole(String var1) {
      return this.request.isUserInRole(var1);
   }

   public Principal getUserPrincipal() {
      return this.request.getUserPrincipal();
   }

   public boolean isReliable() {
      return false;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   public void confirmOneway() throws IOException {
      if (verbose) {
         Verbose.log((Object)("confirming status of oneway @ " + (new Date()).toString()));
      }

      this.response.setStatus(202);
      this.response.setContentLength(0);
      this.response.flushBuffer();
   }
}
