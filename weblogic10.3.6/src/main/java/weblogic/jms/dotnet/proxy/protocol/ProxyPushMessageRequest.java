package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyPushMessageRequest extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private static final int _SUGGEST_WINDOW_TURN = 1;
   private long consumerId;
   private ProxyMessageImpl message;
   private boolean suggestWindowTurn;
   private ProxyPushMessageRequest next;

   public ProxyPushMessageRequest(long var1, ProxyMessageImpl var3, boolean var4) {
      this.consumerId = var1;
      this.message = var3;
      this.suggestWindowTurn = var4;
   }

   void setNext(ProxyPushMessageRequest var1) {
      this.next = var1;
   }

   ProxyPushMessageRequest getNext() {
      return this.next;
   }

   public ProxyMessageImpl getMessage() {
      return this.message;
   }

   public ProxyPushMessageRequest() {
   }

   public int getMarshalTypeCode() {
      return 44;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.suggestWindowTurn) {
         this.versionFlags.setBit(1);
      }

      this.versionFlags.marshal(var1);
      var1.writeLong(this.consumerId);
      var1.writeByte(this.message.getType());
      this.message.marshal(var1);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.consumerId = var1.readLong();
      byte var2 = var1.readByte();
      this.message = ProxyMessageImpl.createMessageImpl(var2);
      this.message.unmarshal(var1);
      this.suggestWindowTurn = this.versionFlags.isSet(1);
   }
}
