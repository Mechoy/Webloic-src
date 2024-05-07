package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public class ProxyConsumerReceiveResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private static final int _HAS_MESSAGE = 1;
   private ProxyMessageImpl message;

   public ProxyConsumerReceiveResponse(ProxyMessageImpl var1) {
      this.message = var1;
   }

   public final ProxyMessageImpl getMessage() {
      return this.message;
   }

   public ProxyConsumerReceiveResponse() {
   }

   public int getMarshalTypeCode() {
      return 17;
   }

   public void marshal(MarshalWriter var1) {
      MarshalBitMask var2 = new MarshalBitMask(1);
      if (this.message != null) {
         var2.setBit(1);
      }

      var2.marshal(var1);
      if (this.message != null) {
         var1.writeByte(this.message.getType());
         this.message.marshal(var1);
      }

   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      if (var2.isSet(1)) {
         byte var3 = var1.readByte();
         this.message = ProxyMessageImpl.createMessageImpl(var3);
         this.message.unmarshal(var1);
      }

   }
}
