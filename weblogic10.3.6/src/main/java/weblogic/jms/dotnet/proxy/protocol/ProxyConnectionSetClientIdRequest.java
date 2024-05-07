package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyConnectionSetClientIdRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;
   private static final int _HAS_CLIENT_ID = 1;
   private String clientId;

   public ProxyConnectionSetClientIdRequest(String var1) {
      this.clientId = var1;
   }

   public final String getClientId() {
      return this.clientId;
   }

   public ProxyConnectionSetClientIdRequest() {
   }

   public int getMarshalTypeCode() {
      return 11;
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      if (this.versionFlags.isSet(1)) {
         this.clientId = var1.readString();
      }

   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.clientId != null) {
         this.versionFlags.setBit(1);
      }

      this.versionFlags.marshal(var1);
      if (this.versionFlags.isSet(1)) {
         var1.writeString(this.clientId);
      }

   }
}
