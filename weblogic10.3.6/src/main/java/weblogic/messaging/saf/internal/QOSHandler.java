package weblogic.messaging.saf.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFConversationNotAvailException;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFResult;
import weblogic.messaging.saf.SAFTransport;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.messaging.saf.common.SAFResultImpl;

public abstract class QOSHandler {
   protected long sequenceNumberLow = 1L;
   protected long sequenceNumberHigh = 1L;
   protected MessageReference sequenceNumberLowMRef;
   protected MessageReference sequenceNumberHighMRef;
   protected SAFResultImpl result = new SAFResultImpl();
   protected SAFRequest currentSAFRequest;
   protected SAFTransport transport;
   protected SAFConversationInfo conversationInfo;
   protected Sequence sequence;

   public QOSHandler(SAFConversationInfo var1, SAFTransport var2) {
      this.conversationInfo = var1;
      this.transport = var2;
   }

   void preProcess(MessageReference var1) {
      this.result = new SAFResultImpl();
      this.currentSAFRequest = var1.getMessage();
      this.sequenceNumberHigh = var1.getSequenceNumber();
      this.sequenceNumberLow = var1.getSequenceNumber();
   }

   void setCurrentSAFRequest(SAFRequest var1) {
      this.currentSAFRequest = var1;
   }

   void setSAFException(SAFException var1) {
      this.result.setSAFException(var1);
   }

   static QOSHandler getQOSHandler(SAFConversationInfo var0, SAFTransport var1, long var2) {
      switch (var0.getDestinationType()) {
         case 2:
         default:
            return new WSQOSHandler(var0, var1, var2);
      }
   }

   void setSequence(Sequence var1) {
      this.sequence = var1;
   }

   protected abstract void update(MessageReference var1, int var2);

   protected abstract void sendAck();

   protected abstract void sendNack();

   protected final void sendAckInternal() {
      this.sendResult();
      if (SAFDebug.SAFVerbose.isDebugEnabled()) {
         List var1 = this.result.getSequenceNumbers();
         String var2 = null;

         long var4;
         long var6;
         for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 = var2 + "< sequenceNumberLow=" + var4 + " sequenceNumberHigh=" + var6 + "> ") {
            var4 = (Long)var3.next();
            var6 = (Long)var3.next();
         }

         SAFDebug.SAFVerbose.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
         SAFDebug.SAFVerbose.debug(" == execute() sendResult: Successfully sent  ack conversationInfo" + this.conversationInfo + var2);
         SAFDebug.SAFVerbose.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
      }

   }

   protected void sendResult() {
      this.setResult();
      this.transport.sendResult(this.result);
   }

   protected final SAFResult getResult() {
      return this.result;
   }

   protected void setResult() {
      if (this.result.getResultCode() == 0) {
         this.result.setSequenceNumbers(this.sequence.getAllSequenceNumberRanges());
      } else {
         long var1 = this.currentSAFRequest.getSequenceNumber();
         ArrayList var3 = new ArrayList();
         Long var4 = new Long(var1);
         var3.add(var4);
         var3.add(var4);
         this.result.setSequenceNumbers(var3);
      }

      this.result.setConversationInfo(this.conversationInfo);
   }

   public int handleEndpointDeliveryFailure(Throwable var1, SAFRequest var2) {
      SAFException var3 = null;
      if (var1 instanceof SAFException) {
         var3 = (SAFException)var1;
         if (var1 instanceof SAFConversationNotAvailException) {
            this.result.setResultCode(5);
         } else {
            this.result.setResultCode(3);
         }
      } else {
         this.result.setResultCode(12);
         var3 = new SAFException(var1.getMessage(), var1);
      }

      this.result.setSAFException(var3);
      if (SAFDebug.SAFVerbose.isDebugEnabled()) {
         var1.printStackTrace();
      }

      return this.result.getResultCode();
   }
}
