package weblogic.jms.dotnet.proxy.protocol;

import javax.jms.JMSException;
import javax.jms.Message;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyHdrMessageImpl extends ProxyMessageImpl {
   public ProxyHdrMessageImpl() {
   }

   public ProxyHdrMessageImpl(Message var1) throws JMSException {
      super(var1);
   }

   public byte getType() {
      return 2;
   }

   public void populateJMSMessage(Message var1) throws JMSException {
      super.populateJMSMessage(var1);
   }

   public int getMarshalTypeCode() {
      return 48;
   }

   public void marshal(MarshalWriter var1) {
      super.marshal(var1);
   }

   public void unmarshal(MarshalReader var1) {
      super.unmarshal(var1);
   }
}
