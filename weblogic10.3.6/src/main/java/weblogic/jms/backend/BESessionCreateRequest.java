package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.messaging.dispatcher.Response;

public final class BESessionCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 1492850028849803175L;
   private static final int EXTVERSION62 = 1;
   private static final int EXTVERSION70 = 2;
   private static final int EXTVERSION90 = 3;
   private static final int EXTVERSION = 3;
   private static final int VERSION_MASK = 255;
   private static final int TRANSACTED_MASK = 256;
   private static final int STOPPED_MASK = 1024;
   private static final int XA_SESSION_MASK = 2048;
   private static final int ACKNOWLEDGE_MODE_MASK = 267386880;
   private static final int ACKNOWLEDGE_MODE_SHIFT = 20;
   private DispatcherId feDispatcherId;
   private JMSID connectionId;
   private JMSID sessionId;
   private JMSID sequencerId;
   private boolean transacted;
   private boolean xaSession;
   private int acknowledgeMode;
   private boolean isStopped;
   private long startStopSequenceNumber;
   private String connectionAddress = null;
   private transient String pushWorkManager;
   private byte clientVersion = 1;

   public BESessionCreateRequest(DispatcherId var1, JMSID var2, JMSID var3, JMSID var4, boolean var5, boolean var6, int var7, boolean var8, long var9, byte var11, String var12, String var13) {
      super((JMSID)null, 13570);
      this.feDispatcherId = var1;
      this.connectionId = var2;
      this.sessionId = var3;
      this.sequencerId = var4;
      this.transacted = var5;
      this.xaSession = var6;
      this.acknowledgeMode = var7;
      this.isStopped = var8;
      this.startStopSequenceNumber = var9;
      this.clientVersion = var11;
      this.connectionAddress = var12;
      this.pushWorkManager = var13;
   }

   public final DispatcherId getFEDispatcherId() {
      return this.feDispatcherId;
   }

   public final JMSID getConnectionId() {
      return this.connectionId;
   }

   public final JMSID getSessionId() {
      return this.sessionId;
   }

   public final String getConnectionAddress() {
      return this.connectionAddress;
   }

   public final JMSID getSequencerId() {
      return this.sequencerId;
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

   public final boolean getIsStopped() {
      return this.isStopped;
   }

   public final long getStartStopSequenceNumber() {
      return this.startStopSequenceNumber;
   }

   public String getPushWorkManager() {
      return this.pushWorkManager;
   }

   public final byte getClientVersion() {
      return this.clientVersion;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public BESessionCreateRequest() {
   }

   private byte getVersion(Object var1) {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_70) < 0) {
            return 1;
         }
      }

      return 3;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = this.getVersion(var1);
      int var3 = var2 | this.acknowledgeMode << 20;
      if (this.transacted) {
         var3 |= 256;
      }

      if (this.xaSession) {
         var3 |= 2048;
      }

      if (this.isStopped) {
         var3 |= 1024;
      }

      var1.writeInt(var3);
      super.writeExternal(var1);
      this.feDispatcherId.writeExternal(var1);
      this.connectionId.writeExternal(var1);
      this.sessionId.writeExternal(var1);
      this.sequencerId.writeExternal(var1);
      var1.writeLong(this.startStopSequenceNumber);
      if (var2 >= 2) {
         var1.writeByte(this.clientVersion);
      }

      if (var2 >= 3) {
         var1.writeUTF(this.connectionAddress);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1 && var3 != 2 && var3 != 3) {
         throw JMSUtilities.versionIOException(var3, 1, 3);
      } else {
         super.readExternal(var1);
         this.feDispatcherId = new DispatcherId();
         this.feDispatcherId.readExternal(var1);
         this.connectionId = new JMSID();
         this.connectionId.readExternal(var1);
         this.sessionId = new JMSID();
         this.sessionId.readExternal(var1);
         this.sequencerId = new JMSID();
         this.sequencerId.readExternal(var1);
         this.startStopSequenceNumber = var1.readLong();
         if (var3 >= 2) {
            this.clientVersion = var1.readByte();
         }

         if (var3 >= 3) {
            this.connectionAddress = var1.readUTF();
         }

         this.acknowledgeMode = var2 >> 20;
         this.transacted = (var2 & 256) != 0;
         this.xaSession = (var2 & 2048) != 0;
         this.isStopped = (var2 & 1024) != 0;
      }
   }
}
