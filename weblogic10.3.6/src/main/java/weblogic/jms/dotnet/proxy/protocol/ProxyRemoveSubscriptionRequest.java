package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyRemoveSubscriptionRequest extends ProxyRequest {
   private String name;
   private static final int EXTVERSION = 1;

   public ProxyRemoveSubscriptionRequest(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public ProxyRemoveSubscriptionRequest() {
   }

   public int getMarshalTypeCode() {
      return 27;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      var1.writeString(this.name);
   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      this.name = var1.readString();
   }
}
