package weblogic.wsee.jaxws.framework.jaxrpc;

import com.sun.xml.ws.api.message.Packet;
import javax.xml.ws.handler.MessageContext;

public abstract class AbstractPropertyConverter implements PropertyConverter {
   public boolean containsKey(Packet var1, MessageContext var2) {
      return true;
   }

   public Object convertToJAXWS(Packet var1, MessageContext var2, Object var3) {
      throw new UnsupportedOperationException();
   }

   public Object remove(Packet var1, MessageContext var2) {
      throw new UnsupportedOperationException();
   }
}
