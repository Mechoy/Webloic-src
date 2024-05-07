package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxySessionCreateRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;
   private static final int _IS_TRANSACTED = 1;
   private static final int _IS_XA_SESSION = 2;
   private boolean transacted;
   private boolean xaSession;
   private int acknowledgeMode;
   private long sessionMsgListenerServiceId;

   public ProxySessionCreateRequest(boolean var1, boolean var2, int var3, long var4) {
      this.transacted = var1;
      this.xaSession = var2;
      this.acknowledgeMode = var3;
      this.sessionMsgListenerServiceId = var4;
   }

   public final long getSessionMsgListenerServiceId() {
      return this.sessionMsgListenerServiceId;
   }

   public final boolean getTransacted() {
      return this.transacted;
   }

   public final boolean getXASession() {
      return this.xaSession;
   }

   public final int getAcknowledgeMode() {
      return this.acknowledgeMode;
   }

   public ProxySessionCreateRequest() {
   }

   public int getMarshalTypeCode() {
      return 30;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.transacted) {
         this.versionFlags.setBit(1);
      }

      if (this.xaSession) {
         this.versionFlags.setBit(2);
      }

      this.versionFlags.marshal(var1);
      var1.writeInt(this.acknowledgeMode);
      var1.writeLong(this.sessionMsgListenerServiceId);
   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      if (this.versionFlags.isSet(1)) {
         this.transacted = true;
      }

      if (this.versionFlags.isSet(2)) {
         this.xaSession = true;
      }

      this.acknowledgeMode = var1.readInt();
      this.sessionMsgListenerServiceId = var1.readLong();
   }
}
