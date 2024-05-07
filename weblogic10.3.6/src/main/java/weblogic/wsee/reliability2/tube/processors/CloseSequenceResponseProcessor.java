package weblogic.wsee.reliability2.tube.processors;

import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.handshake.CloseSequenceResponseMsg;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.sequence.SourceOfferSequence;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public class CloseSequenceResponseProcessor extends MessageProcessor {
   public CloseSequenceResponseProcessor(WsrmTubeUtils var1) {
      super(var1);
   }

   public boolean processOutbound(Packet var1, WsrmConstants.RMVersion var2, SequenceIdFactory var3, String var4) throws WsrmException {
      return true;
   }

   public void handleInbound(Packet var1, WsrmConstants.RMVersion var2, WsrmTubeUtils.InboundMessageResult var3) {
      Message var4 = null;
      Message var5 = var1.getMessage();

      try {
         CloseSequenceResponseMsg var7 = new CloseSequenceResponseMsg(var2);
         var7.readMsg(var5.readAsSOAPMessage());
         SourceSequence var6 = (SourceSequence)WsrmTubeUtils.doInitialSetupForAction(WsrmConstants.Element.CLOSE_SEQUENCE_RESPONSE.getElementName(), true, var7.getSequenceId(), var2, var1);
         var3.seq = var6;
         this._tubeUtil.setInboundSequence(var1, var6);
         internalCloseSequenceResponse(var6);
      } catch (Exception var8) {
         var4 = WsrmTubeUtils.handleInboundException(var8, this._addrVersion, this._soapVersion);
      }

      var3.message = var4;
   }

   public static void forceAnonymousCloseSequenceResponse(SourceOfferSequence var0) throws WsrmException {
      internalCloseSequenceResponse(var0);
   }

   private static void internalCloseSequenceResponse(SourceSequence var0) {
      SequenceState var1 = var0.getState();
      synchronized(var0) {
         if (var1.isValidTransition(SequenceState.CLOSED)) {
            var0.setState(SequenceState.CLOSED);
         }
      }
   }
}
