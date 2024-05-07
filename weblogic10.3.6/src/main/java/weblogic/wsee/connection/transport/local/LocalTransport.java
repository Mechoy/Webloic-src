package weblogic.wsee.connection.transport.local;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.soap.MimeHeaders;
import weblogic.wsee.connection.ResponseListener;
import weblogic.wsee.connection.transport.ClientTransport;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;

public class LocalTransport implements ClientTransport {
   private static final boolean verbose = Verbose.isVerbose(LocalTransport.class);
   private String serviceUri;
   private WsPort port;

   public String getName() {
      return "local";
   }

   public String getServiceURI() {
      return this.serviceUri;
   }

   public String getEndpointAddress() {
      return this.serviceUri;
   }

   public OutputStream send(MimeHeaders var1) throws IOException {
      throw new Error("NIY");
   }

   public OutputStream sendFault(MimeHeaders var1) throws IOException {
      throw new Error("NIY");
   }

   public InputStream receive(MimeHeaders var1) throws IOException {
      throw new Error("NIY");
   }

   public void confirmOneway() throws IOException {
      throw new Error("NIY");
   }

   public void connect(String var1, TransportInfo var2) throws IOException {
      this.serviceUri = var1;
      WsRegistry var3 = WsRegistry.instance();
      this.port = var3.lookup(this.serviceUri);
      if (this.port == null) {
         throw new IOException("Failed to connect to port: " + var1);
      }
   }

   public void setResponseListener(ResponseListener var1) {
      throw new AssertionError("NIY");
   }

   public boolean isBlocking() {
      return false;
   }

   public void setConnectionTimeout(int var1) {
      throw new Error("NIY");
   }

   public void setReadTimeout(int var1) {
      throw new Error("NIY");
   }
}
