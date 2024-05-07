package weblogic.jms.dotnet.proxy.protocol;

import javax.jms.JMSException;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.dotnet.transport.TransportError;

public final class ProxyPushExceptionRequest extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private TransportError error;

   public ProxyPushExceptionRequest(JMSException var1) {
      this.error = new TransportError(var1);
   }

   public TransportError getTransportError() {
      return this.error;
   }

   public ProxyPushExceptionRequest() {
   }

   public int getMarshalTypeCode() {
      return 43;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      this.error.marshal(var1);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.error = new TransportError();
      this.error.unmarshal(var1);
   }
}
