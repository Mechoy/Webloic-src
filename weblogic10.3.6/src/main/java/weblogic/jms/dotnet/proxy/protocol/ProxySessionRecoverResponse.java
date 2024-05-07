package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxySessionRecoverResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private static final int _HAS_GENERATION = 1;
   private int generation = 0;

   public ProxySessionRecoverResponse(int var1) {
      this.generation = var1;
   }

   public final long getGeneration() {
      return (long)this.generation;
   }

   public ProxySessionRecoverResponse() {
   }

   public int getMarshalTypeCode() {
      return 33;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.generation != 0) {
         this.versionFlags.setBit(1);
      }

      this.versionFlags.marshal(var1);
      if (this.generation != 0) {
         var1.writeInt(this.generation);
      }

   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      if (var2.isSet(1)) {
         this.generation = var1.readInt();
      }

   }
}
