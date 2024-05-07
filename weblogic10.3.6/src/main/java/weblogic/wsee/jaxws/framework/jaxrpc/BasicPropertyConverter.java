package weblogic.wsee.jaxws.framework.jaxrpc;

import com.sun.xml.ws.api.message.Packet;
import javax.xml.ws.handler.MessageContext;

public class BasicPropertyConverter implements PropertyConverter {
   private String rpcKey;
   private String wsKey;

   public BasicPropertyConverter(String var1, String var2) {
      this.rpcKey = var1;
      this.wsKey = var2;
   }

   public String getJAXRPCKey() {
      return this.rpcKey;
   }

   public String getJAXWSKey() {
      return this.wsKey;
   }

   public Object convertToJAXRPC(Packet var1, MessageContext var2) {
      return var2.get(this.wsKey);
   }

   public Object convertToJAXWS(Packet var1, MessageContext var2, Object var3) {
      return var2.put(this.wsKey, var3);
   }

   public boolean containsKey(Packet var1, MessageContext var2) {
      return var2.containsKey(this.wsKey);
   }

   public Object remove(Packet var1, MessageContext var2) {
      return var2.remove(this.wsKey);
   }
}
