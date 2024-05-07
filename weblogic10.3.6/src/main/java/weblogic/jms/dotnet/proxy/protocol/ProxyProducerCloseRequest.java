package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyProducerCloseRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;

   public int getMarshalTypeCode() {
      return 22;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
   }
}
