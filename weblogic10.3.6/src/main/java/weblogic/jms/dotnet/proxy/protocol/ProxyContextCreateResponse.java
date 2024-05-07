package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public class ProxyContextCreateResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private long ctxId;

   public ProxyContextCreateResponse(long var1) {
      this.ctxId = var1;
   }

   public long getContextId() {
      return this.ctxId;
   }

   public ProxyContextCreateResponse() {
   }

   public int getMarshalTypeCode() {
      return 2;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      var1.writeLong(this.ctxId);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.ctxId = var1.readLong();
   }
}
