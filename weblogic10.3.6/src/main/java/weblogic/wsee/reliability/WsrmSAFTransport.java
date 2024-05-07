package weblogic.wsee.reliability;

import java.io.Externalizable;
import weblogic.messaging.saf.SAFConversationHandle;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFErrorAwareTransport;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFResult;
import weblogic.messaging.saf.SAFTransportException;
import weblogic.wsee.util.AccessException;
import weblogic.wsee.util.Verbose;

public final class WsrmSAFTransport implements SAFErrorAwareTransport {
   private int type = 2;
   private static final boolean verbose = Verbose.isVerbose(WsrmSAFTransport.class);

   public void sendResult(SAFResult var1) {
      assert var1 != null;

      WsrmReceivingSide.sendResult(var1);
   }

   public int getType() {
      return this.type;
   }

   public boolean isGapsAllowed() {
      return false;
   }

   public Externalizable send(SAFConversationInfo var1, SAFRequest var2) throws SAFTransportException {
      long var3 = System.nanoTime();
      if (verbose) {
         Verbose.say(var3 + " Entering WsrmSAFTransport.send(SAFConversationInfo, SAFRequest)");
         Verbose.say(var3 + " SAFRequest.getSequenceNumber: " + var2.getSequenceNumber());
         Verbose.say(var3 + " SAFRequest.getMessageId: " + var2.getMessageId());
      }

      assert var1 != null;

      assert var2 != null;

      return WsrmSequenceSender.send(var1, var2);
   }

   public SAFConversationHandle createConversation(SAFConversationInfo var1) throws SAFTransportException {
      assert var1 != null;

      return WsrmSequenceSender.createSequence(var1);
   }

   public void terminateConversation(SAFConversationInfo var1) throws SAFTransportException {
      assert var1 != null;

      WsrmSequenceSender.terminateSequence(var1, true);
   }

   public Externalizable createSecurityToken(SAFConversationInfo var1) throws SAFTransportException {
      assert var1 != null;

      return WsrmSequenceSender.createSecurityToken(var1);
   }

   public boolean isPermanentError(Throwable var1) {
      long var2 = System.nanoTime();
      boolean var4 = false;

      for(Throwable var5 = var1; var5 != null && !var4; var5 = var5.getCause()) {
         var4 = var5 instanceof WsrmPermanentTransportException || var5 instanceof AccessException;
      }

      if (verbose) {
         Verbose.say(var2 + " Inside WsrmSAFTransport.isPermanentError(Throwable), Throwable is:\n");
         Verbose.logException(var1);
         Verbose.say("\n" + var2 + " Exiting WsrmSAFTransport.isPermanentError(Throwable) with boolean: " + var4);
      }

      return var4;
   }
}
