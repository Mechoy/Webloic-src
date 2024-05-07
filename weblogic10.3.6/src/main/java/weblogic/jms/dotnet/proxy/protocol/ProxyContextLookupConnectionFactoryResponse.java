package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public class ProxyContextLookupConnectionFactoryResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private long connectionFactoryId;

   public ProxyContextLookupConnectionFactoryResponse(long var1) {
      this.connectionFactoryId = var1;
   }

   public final long getConnectionFactoryId() {
      return this.connectionFactoryId;
   }

   public ProxyContextLookupConnectionFactoryResponse() {
   }

   public int getMarshalTypeCode() {
      return 4;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      var1.writeLong(this.connectionFactoryId);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.connectionFactoryId = var1.readLong();
   }
}
