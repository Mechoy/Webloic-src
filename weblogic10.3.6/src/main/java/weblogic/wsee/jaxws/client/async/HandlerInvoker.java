package weblogic.wsee.jaxws.client.async;

import com.sun.xml.ws.api.message.Packet;

public interface HandlerInvoker {
   Packet handleResponse(Packet var1);

   Packet handleException(Throwable var1);
}
