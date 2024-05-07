package weblogic.wsee.connection.local;

import java.io.IOException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.util.Verbose;

public class LoopbackConnection implements Connection {
   private static final boolean verbose = Verbose.isVerbose(LoopbackConnection.class);
   private SOAPMessage message;
   private Transport transport;

   LoopbackConnection(SOAPMessage var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("message can not be null");
      } else {
         this.message = var1;
      }
   }

   SOAPMessage getMessage() {
      return this.message;
   }

   public void send(MessageContext var1) throws IOException {
      if (!(var1 instanceof SOAPMessageContext)) {
         throw new ConnectionException("context is not a soap message context");
      } else {
         SOAPMessageContext var2 = (SOAPMessageContext)var1;
         this.message = var2.getMessage();
      }
   }

   public void receive(MessageContext var1) throws IOException {
      if (!(var1 instanceof SOAPMessageContext)) {
         throw new ConnectionException("context is not a soap message context");
      } else {
         SOAPMessageContext var2 = (SOAPMessageContext)var1;
         var2.setMessage(this.message);
      }
   }

   public void setTransport(Transport var1) {
      this.transport = var1;
   }

   public Transport getTransport() {
      return this.transport;
   }
}
