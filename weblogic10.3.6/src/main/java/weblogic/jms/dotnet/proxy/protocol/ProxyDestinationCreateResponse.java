package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyDestinationCreateResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private static final int _HAS_DESTINATION = 1;
   private ProxyDestinationImpl destination;

   public ProxyDestinationCreateResponse(ProxyDestinationImpl var1) {
      this.destination = var1;
   }

   public final ProxyDestinationImpl getDestination() {
      return this.destination;
   }

   public ProxyDestinationCreateResponse() {
   }

   public int getMarshalTypeCode() {
      return 21;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.destination != null) {
         this.versionFlags.setBit(1);
      }

      this.versionFlags.marshal(var1);
      this.destination.marshal(var1);
   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      if (var2.isSet(1)) {
         this.destination = new ProxyDestinationImpl();
         this.destination.unmarshal(var1);
      }

   }
}
