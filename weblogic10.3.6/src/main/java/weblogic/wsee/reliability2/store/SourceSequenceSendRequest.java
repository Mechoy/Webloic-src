package weblogic.wsee.reliability2.store;

import com.sun.xml.ws.api.message.Packet;
import java.io.Serializable;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.persistence.PersistentMessage;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.sequence.SourceMessageInfo;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.sender.api.AbstractSendRequest;

public class SourceSequenceSendRequest extends AbstractSendRequest {
   private static final long serialVersionUID = 1L;
   private SourceMessageInfo _msgInfo;
   private PersistentMessage _payload;

   public SourceSequenceSendRequest(SourceMessageInfo var1) {
      super(var1.getMessageId());
      this._msgInfo = var1;
      this.createPayload();
   }

   private void createPayload() {
      Packet var1 = this._msgInfo.getRequestPacket();
      this._payload = PersistentMessageFactory.getInstance().createMessageFromPacket(this._msgInfo.getMessageId(), var1);
   }

   public String getConversationName() {
      return this._msgInfo.getSequenceId();
   }

   public void setConversationName(String var1) {
   }

   public long getSequenceNumber() {
      return this._msgInfo.getMessageNum();
   }

   public void setSequenceNumber(long var1) {
   }

   public long getTimestamp() {
      return this._msgInfo.getTimestamp();
   }

   public void setTimestamp(long var1) {
   }

   public Serializable getPayload() {
      return this._payload;
   }

   public String getMessageId() {
      return this._msgInfo.getMessageId();
   }

   public String getObjectId() {
      return this.getMessageId();
   }

   public boolean hasExplicitExpiration() {
      SourceSequence var1 = this.getSequence();
      return var1 == null || var1.hasExplicitExpiration();
   }

   private SourceSequence getSequence() {
      try {
         return SourceSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.RM_11, this._msgInfo.getSequenceId(), true);
      } catch (Exception var2) {
         WseeRmLogger.logUnexpectedException(var2.toString(), var2);
         throw new RuntimeException(var2.toString(), var2);
      }
   }

   public boolean isExpired() {
      SourceSequence var1 = this.getSequence();
      return var1 == null || var1.isExpired();
   }

   public SourceMessageInfo getMsgInfo() {
      return this._msgInfo;
   }
}
