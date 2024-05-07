package weblogic.webservice.binding;

import java.io.IOException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPException;

/** @deprecated */
public interface Binding {
   String getReplyTo();

   void setReplyTo(String var1);

   String getSender();

   void setSender(String var1);

   String getDestination();

   void setDestination(String var1);

   void init(BindingInfo var1) throws IOException;

   BindingInfo getBindingInfo();

   void receive(MessageContext var1) throws IOException, SOAPException;

   void send(MessageContext var1) throws IOException, SOAPException;
}
