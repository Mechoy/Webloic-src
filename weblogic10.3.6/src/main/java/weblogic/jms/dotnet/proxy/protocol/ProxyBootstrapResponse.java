package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyBootstrapResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private long serviceId;

   public ProxyBootstrapResponse(long var1) {
      this.serviceId = var1;
   }

   public long getServiceId() {
      return this.serviceId;
   }

   public ProxyBootstrapResponse() {
   }

   public int getMarshalTypeCode() {
      return 42;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      var1.writeLong(this.serviceId);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.serviceId = var1.readLong();
   }
}
