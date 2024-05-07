package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyContextCreateRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;
   private PrimitiveMap env;

   public ProxyContextCreateRequest(PrimitiveMap var1) {
      this.env = var1;
   }

   public PrimitiveMap getEnvironment() {
      return this.env;
   }

   public ProxyContextCreateRequest() {
   }

   public int getMarshalTypeCode() {
      return 1;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      this.env.marshal(var1);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.env = new PrimitiveMap();
      this.env.unmarshal(var1);
   }
}
