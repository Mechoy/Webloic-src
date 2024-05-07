package weblogic.messaging.saf.internal;

import java.util.List;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFTransport;
import weblogic.messaging.saf.common.AgentDeliverRequest;
import weblogic.messaging.saf.common.AgentDeliverResponse;
import weblogic.messaging.saf.common.SAFRequestImpl;
import weblogic.messaging.saf.common.SAFResultImpl;

public class WSQOSHandler extends QOSHandler {
   private long lastAsyncAckNumHigh;

   public WSQOSHandler(SAFConversationInfo var1, SAFTransport var2, long var3) {
      super(var1, var2);
   }

   private static boolean isDeliverSync(SAFRequest var0) {
      AgentDeliverRequest var1 = ((SAFRequestImpl)var0).getAgentRequest();
      return var1 != null && var1.isSyncAck();
   }

   private final void sendSyncNack(SAFRequest var1) {
      this.setResult();
      AgentDeliverRequest var2 = ((SAFRequestImpl)var1).getAgentRequest();
      var2.notifyResult(new AgentDeliverResponse(this.result));
   }

   protected void sendNack() {
      if (isDeliverSync(this.currentSAFRequest)) {
         this.sendSyncNack(this.currentSAFRequest);
      } else {
         this.sendResult();
      }
   }

   protected void sendAck() {
      MessageReference var1 = this.sequenceNumberLowMRef;

      long var2;
      for(var2 = -1L; var1 != null; var1 = var1.getNext()) {
         if (isDeliverSync(var1.getMessage())) {
            var2 = this.sendSyncAck(var1);
         }
      }

      if (var2 < this.lastAsyncAckNumHigh && this.lastAsyncAckNumHigh != 0L) {
         this.sendAsyncAck();
      }

      this.sequenceNumberLowMRef = this.sequenceNumberHighMRef = null;
      this.lastAsyncAckNumHigh = 0L;
   }

   private long sendSyncAck(MessageReference var1) {
      SAFRequestImpl var2 = (SAFRequestImpl)var1.getMessage();
      AgentDeliverRequest var3 = var2.getAgentRequest();
      long var4 = var2.getSequenceNumber();
      List var6 = this.sequence.getAllSequenceNumberRanges();
      AgentDeliverResponse var7 = new AgentDeliverResponse(new SAFResultImpl(var3.getConversationInfo(), var6, 0, (String)null));
      var3.notifyResult(var7);
      return var4;
   }

   private void sendAsyncAck() {
      this.sequenceNumberLow = this.sequenceNumberLow < this.lastAsyncAckNumHigh ? this.sequenceNumberLow : this.lastAsyncAckNumHigh;
      this.sendAckInternal();
   }

   protected final void update(MessageReference var1, int var2) {
      long var3 = var1.getSequenceNumber();
      this.result.setResultCode(var2);
      if (var2 == 0) {
         if (this.sequenceNumberLowMRef == null) {
            this.sequenceNumberLowMRef = var1;
            this.sequenceNumberHighMRef = var1;
         } else {
            this.sequenceNumberHighMRef.setNext(var1);
            this.sequenceNumberHighMRef = var1;
         }

         if (var3 >= this.sequenceNumberHigh) {
            this.sequenceNumberHigh = var3;
         }

         if (var3 <= this.sequenceNumberLow) {
            this.sequenceNumberLow = var3;
         }

         if (!isDeliverSync(var1.getMessage()) && var3 >= this.lastAsyncAckNumHigh) {
            this.lastAsyncAckNumHigh = var3;
         }

      }
   }
}
