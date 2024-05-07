package weblogic.messaging.saf.common;

import java.util.ArrayList;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFResult;
import weblogic.messaging.saf.internal.AgentImpl;
import weblogic.messaging.saf.internal.ConversationReassembler;
import weblogic.messaging.saf.internal.ReceivingAgentImpl;

public final class AgentDeliverRequest {
   private final SAFRequest request;
   private final SAFConversationInfo conversationInfo;
   private final boolean syncAck;
   private Object result;
   private Throwable throwableResponse;
   private int numWaiting;

   public AgentDeliverRequest(SAFConversationInfo var1, SAFRequest var2, boolean var3) {
      this.conversationInfo = var1;
      this.request = var2;
      this.syncAck = var3;
   }

   public SAFRequest getRequest() {
      return this.request;
   }

   public final boolean isSyncAck() {
      return this.syncAck;
   }

   public SAFConversationInfo getConversationInfo() {
      return this.conversationInfo;
   }

   public AgentDeliverResponse finishDeliver(AgentImpl var1) throws SAFException {
      ConversationReassembler var2 = ((ReceivingAgentImpl)var1).getConversation(this.getConversationInfo());
      if (var2 == null) {
         return this.processDeliverFailure(6);
      } else if (var2.isClosed()) {
         return this.processDeliverFailure(5);
      } else {
         ((SAFRequestImpl)this.request).setAgentRequest(this);
         var2.addMessage(this.request);
         return !this.syncAck ? new AgentDeliverResponse((SAFResultImpl)null) : this.getResult();
      }
   }

   private AgentDeliverResponse processDeliverFailure(int var1) throws SAFException {
      if (this.syncAck) {
         ArrayList var2 = new ArrayList();
         var2.add(new Long(0L));
         var2.add(new Long(0L));
         return new AgentDeliverResponse(new SAFResultImpl(this.getConversationInfo(), var2, var1, "Cannot synchronously deliver message to Endpoint: " + SAFResult.description[var1]));
      } else {
         throw new SAFException("Cannot asynchronously deliver message to Endpoint for async : " + SAFResult.description[var1]);
      }
   }

   public final synchronized void notifyResult(AgentDeliverResponse var1) {
      this.result = var1;
      if (this.numWaiting > 0) {
         this.notify();
      }

   }

   private synchronized AgentDeliverResponse getResult() throws SAFException {
      while(this.result == null && this.throwableResponse == null) {
         this.sleepTillNotified();
      }

      if (this.throwableResponse != null) {
         if (this.throwableResponse instanceof RuntimeException) {
            throw (RuntimeException)this.throwableResponse;
         } else if (this.throwableResponse instanceof SAFException) {
            throw (SAFException)this.throwableResponse;
         } else {
            throw new SAFException(this.throwableResponse.getMessage(), this.throwableResponse);
         }
      } else {
         return (AgentDeliverResponse)this.result;
      }
   }

   private void sleepTillNotified() {
      if (SAFDebug.SAFVerbose.isDebugEnabled()) {
         SAFDebug.SAFVerbose.debug(this + " is in sleepTillNotify()");
      }

      try {
         ++this.numWaiting;
         this.wait();
         if (SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFVerbose.debug(this + " is waken up from sleepTillNotify()");
         }
      } catch (InterruptedException var9) {
      } finally {
         --this.numWaiting;
         if (this.numWaiting > 0) {
            this.notify();
         }

         Object var4 = null;
         Object var5 = null;
         boolean var6 = false;
         if (var6) {
            if (var4 != null) {
               throw var4;
            }

            if (var5 != null) {
               throw var5;
            }
         }

      }

   }
}
