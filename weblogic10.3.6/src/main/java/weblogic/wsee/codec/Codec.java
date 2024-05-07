package weblogic.wsee.codec;

import java.util.Collection;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.wsdl.WsdlBindingMessage;

public interface Codec {
   String FORCE_v91_XMLBEAN_MARSHALLING = "FORCE_v91_XMLBEAN_MARSHALLING";

   MessageContext createContext();

   void encode(MessageContext var1, WsdlBindingMessage var2, WsMethod var3, Map var4) throws CodecException;

   void decode(MessageContext var1, WsdlBindingMessage var2, WsMethod var3, Map var4) throws CodecException;

   QName getOperation(MessageContext var1) throws CodecException;

   void decodeFault(MessageContext var1, Collection<? extends WsdlBindingMessage> var2, WsMethod var3) throws Throwable;

   boolean encodeFault(MessageContext var1, WsMethod var2, Throwable var3) throws CodecException;
}
