package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public class ProxyConsumerCreateResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private long id;

   public ProxyConsumerCreateResponse(long var1) {
      this.id = var1;
   }

   public long getConsumerId() {
      return this.id;
   }

   public ProxyConsumerCreateResponse() {
   }

   public int getMarshalTypeCode() {
      return 14;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      var1.writeLong(this.id);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.id = var1.readLong();
   }
}
