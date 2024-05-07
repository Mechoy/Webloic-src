package weblogic.wsee.connection;

import java.io.IOException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.connection.transport.Transport;

public interface Connection {
   void send(MessageContext var1) throws IOException;

   void receive(MessageContext var1) throws IOException;

   void setTransport(Transport var1);

   Transport getTransport();
}
