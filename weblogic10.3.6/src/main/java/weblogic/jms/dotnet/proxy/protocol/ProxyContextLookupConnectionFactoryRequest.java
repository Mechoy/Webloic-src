package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyContextLookupConnectionFactoryRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;
   private String jndiName;

   public ProxyContextLookupConnectionFactoryRequest(String var1) {
      this.jndiName = var1;
   }

   public String getJNDIName() {
      return this.jndiName;
   }

   public ProxyContextLookupConnectionFactoryRequest() {
   }

   public int getMarshalTypeCode() {
      return 3;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      var1.writeString(this.jndiName);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.jndiName = var1.readString();
   }
}
