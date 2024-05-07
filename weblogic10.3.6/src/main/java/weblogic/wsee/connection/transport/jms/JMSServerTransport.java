package weblogic.wsee.connection.transport.jms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import javax.xml.soap.MimeHeaders;
import weblogic.wsee.connection.transport.ServerTransport;
import weblogic.wsee.util.Verbose;

public class JMSServerTransport implements ServerTransport {
   private static final boolean verbose = Verbose.isVerbose(JMSServerTransport.class);
   protected String uri;

   public JMSServerTransport(String var1) {
      this.uri = var1;
   }

   public String getEndpointAddress() {
      return null;
   }

   public String getServiceURI() {
      return this.uri;
   }

   public String getName() {
      return "JMSServerTransport";
   }

   public OutputStream send(MimeHeaders var1) throws IOException {
      throw new IOException("JMSServerTransport.send() should not be called, it is a one way transport");
   }

   public OutputStream sendGeneralFault(MimeHeaders var1) throws IOException {
      throw new IOException("JMSServerTransport.sendGeneralFault() should not be called, it is a one way transport");
   }

   public OutputStream sendAuthorizationFault(MimeHeaders var1) throws IOException {
      throw new IOException("JMSServerTransport.sendAuthorizationFault() should not be called, it is a one way transport");
   }

   public OutputStream sendAuthorizationRequiredFault(MimeHeaders var1) throws IOException {
      throw new IOException("JMSServerTransport.sendAuthorizationRequiredFault() should not be called, it is a one way transport");
   }

   public InputStream receive(MimeHeaders var1) throws IOException {
      throw new IOException("JMSServerTransport.receive() should not be called");
   }

   public boolean isUserInRole(String var1) {
      throw new Error("NYI");
   }

   public Principal getUserPrincipal() {
      return null;
   }

   public boolean isReliable() {
      return true;
   }

   public void confirmOneway() throws IOException {
      if (verbose) {
         Verbose.log((Object)"confirming status of oneway");
      }

   }
}
