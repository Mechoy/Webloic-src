package weblogic.wsee.connection.transport.local;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import javax.xml.soap.MimeHeaders;
import weblogic.wsee.connection.transport.ServerTransport;
import weblogic.wsee.util.Verbose;

public class LocalDelegateServerTransport implements ServerTransport {
   private static final boolean verbose = Verbose.isVerbose(LocalDelegateServerTransport.class);
   private ServerTransport delegate;

   public LocalDelegateServerTransport(ServerTransport var1) {
      this.delegate = var1;
   }

   public String getServiceURI() {
      return this.delegate.getServiceURI();
   }

   public String getEndpointAddress() {
      return this.delegate.getEndpointAddress();
   }

   public String getName() {
      return "LocalDelegateServerTransport";
   }

   public OutputStream send(MimeHeaders var1) throws IOException {
      return this.delegate.send(var1);
   }

   public OutputStream sendGeneralFault(MimeHeaders var1) throws IOException {
      return this.delegate.sendGeneralFault(var1);
   }

   public OutputStream sendAuthorizationFault(MimeHeaders var1) throws IOException {
      return this.delegate.sendAuthorizationFault(var1);
   }

   public OutputStream sendAuthorizationRequiredFault(MimeHeaders var1) throws IOException {
      return this.delegate.sendAuthorizationRequiredFault(var1);
   }

   public InputStream receive(MimeHeaders var1) throws IOException {
      return this.delegate.receive(var1);
   }

   public boolean isUserInRole(String var1) {
      return this.delegate.isUserInRole(var1);
   }

   public Principal getUserPrincipal() {
      return this.delegate.getUserPrincipal();
   }

   public boolean isReliable() {
      return this.delegate.isReliable();
   }

   public void confirmOneway() throws IOException {
      this.delegate.confirmOneway();
   }
}
