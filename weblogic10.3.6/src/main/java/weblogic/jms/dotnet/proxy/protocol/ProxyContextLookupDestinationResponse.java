package weblogic.jms.dotnet.proxy.protocol;

import javax.jms.Destination;
import javax.jms.JMSException;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public class ProxyContextLookupDestinationResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private ProxyDestinationImpl destination;

   public ProxyContextLookupDestinationResponse(Destination var1) throws JMSException {
      this.destination = new ProxyDestinationImpl(var1);
   }

   public final String getName() {
      return this.destination.getName();
   }

   public final ProxyDestinationImpl getDestination() {
      return this.destination;
   }

   public ProxyContextLookupDestinationResponse() {
   }

   public int getMarshalTypeCode() {
      return 6;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      this.destination.marshal(var1);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.destination = new ProxyDestinationImpl();
      this.destination.unmarshal(var1);
   }
}
