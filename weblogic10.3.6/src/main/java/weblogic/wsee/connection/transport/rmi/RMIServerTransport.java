package weblogic.wsee.connection.transport.rmi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Iterator;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import weblogic.wsee.connection.transport.ServerTransport;
import weblogic.wsee.util.Verbose;

public class RMIServerTransport implements ServerTransport {
   private static final boolean verbose = Verbose.isVerbose(RMIServerTransport.class);
   protected String uri;
   protected OutputStream os;

   public RMIServerTransport(String var1, OutputStream var2) {
      this.uri = var1;
      this.os = var2;
   }

   public String getServiceURI() {
      return this.uri;
   }

   public String getEndpointAddress() {
      return null;
   }

   public String getName() {
      return "RMIServerTransport";
   }

   public OutputStream send(MimeHeaders var1) throws IOException {
      assert var1 != null;

      if (verbose) {
         Verbose.log((Object)"Sending from RMIServerTransport");
         Iterator var2 = var1.getAllHeaders();

         while(var2.hasNext()) {
            MimeHeader var3 = (MimeHeader)var2.next();
            Verbose.log((Object)("Name: " + var3.getName() + " Value: " + var3.getValue()));
         }
      }

      return this.os;
   }

   public OutputStream sendGeneralFault(MimeHeaders var1) throws IOException {
      return this.send(var1);
   }

   public OutputStream sendAuthorizationFault(MimeHeaders var1) throws IOException {
      return this.send(var1);
   }

   public OutputStream sendAuthorizationRequiredFault(MimeHeaders var1) throws IOException {
      return this.send(var1);
   }

   public InputStream receive(MimeHeaders var1) throws IOException {
      throw new IOException("RMIServerTransport receive should not be called");
   }

   public boolean isUserInRole(String var1) {
      throw new Error("NYI");
   }

   public Principal getUserPrincipal() {
      return null;
   }

   public boolean isReliable() {
      return false;
   }

   public void confirmOneway() throws IOException {
      if (verbose) {
         Verbose.log((Object)"confirming status of oneway");
      }

   }
}
