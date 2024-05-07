package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public class ProxyConnectionCreateResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private static final int _HAS_CLIENT_ID = 1;
   private long connectionId;
   private String clientId;
   private int acknowledgePolicy;
   private ProxyConnectionMetaDataImpl metadata;

   public ProxyConnectionCreateResponse(long var1, String var3, int var4, ProxyConnectionMetaDataImpl var5) {
      this.connectionId = var1;
      this.clientId = var3;
      this.acknowledgePolicy = var4;
      this.metadata = var5;
   }

   public long getConnectionId() {
      return this.connectionId;
   }

   public ProxyConnectionCreateResponse() {
   }

   public int getMarshalTypeCode() {
      return 10;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.clientId != null) {
         this.versionFlags.setBit(1);
      }

      this.versionFlags.marshal(var1);
      var1.writeLong(this.connectionId);
      if (this.clientId != null) {
         var1.writeString(this.clientId);
      }

      var1.writeInt(this.acknowledgePolicy);
      this.metadata.marshal(var1);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.connectionId = var1.readLong();
      if (this.versionFlags.isSet(1)) {
         this.clientId = var1.readString();
      }

      this.acknowledgePolicy = var1.readInt();
      this.metadata = new ProxyConnectionMetaDataImpl();
      this.metadata.unmarshal(var1);
   }
}
