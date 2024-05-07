package weblogic.wsee.jaxws.framework.jaxrpc;

import com.sun.xml.ws.api.message.Packet;
import javax.xml.ws.handler.MessageContext;

public interface PropertyConverter {
   Object convertToJAXRPC(Packet var1, MessageContext var2);

   Object convertToJAXWS(Packet var1, MessageContext var2, Object var3);

   boolean containsKey(Packet var1, MessageContext var2);

   Object remove(Packet var1, MessageContext var2);
}
