package weblogic.wsee.reliability;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFResult;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.reliability.faults.InvalidAckFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultMsgFactory;
import weblogic.wsee.util.Verbose;

public class WsrmReceivingSide {
   private static final boolean verbose = Verbose.isVerbose(WsrmReceivingSide.class);

   public static void sendResult(SAFResult var0) {
      if (var0.isSuccessful()) {
         acknowledge(var0);
      } else {
         fault(var0);
      }

   }

   private static void fault(SAFResult var0) {
      SAFConversationInfo var1 = var0.getConversationInfo();

      assert var1 != null;

      WsrmSequenceContext var2 = (WsrmSequenceContext)var1.getContext();

      assert var2 != null;

      EndpointReference var3 = var2.getAcksTo();

      assert var3 != null;

      SequenceFaultMsgFactory var4 = SequenceFaultMsgFactory.getInstance();
      int var5 = var0.getResultCode();
      QName var6 = var4.getSAFResultCodeMapping(var5);
      if (var6 == null) {
         if (verbose) {
            Verbose.log((Object)("Fault " + var5 + " -- '" + var0.getDescription() + "' is not handled by WSRM, but will be sent back to RM Source anyway"));
            SAFException var12 = var0.getSAFException();
            Verbose.logException(var12);

            try {
               WsrmSequenceSender.sendFault(var1.getConversationName(), (WsrmSequenceContext)var1.getContext(), var3, (Exception)var12, var2.isSoap12());
            } catch (SAFException var10) {
               if (verbose) {
                  Verbose.logException(var10);
               }
            }
         }

      } else {
         SequenceFaultMsg var7 = var4.createSequenceFaultMsg(var6, var2.getRmVersion());
         List var8 = var0.getSequenceNumbers();
         if (var7 instanceof InvalidAckFaultMsg) {
            InvalidAckFaultMsg var9 = (InvalidAckFaultMsg)var7;
            var9.setSequenceId(var1.getConversationName());
            var9.acknowledgeMessages((Long)var8.get(0), (Long)var8.get(1));
         } else if (var7 != null) {
            var7.setSequenceId(var1.getConversationName());
         }

         try {
            WsrmSequenceSender.sendFault(var1.getConversationName(), (WsrmSequenceContext)var1.getContext(), var3, var7, var2.isSoap12());
         } catch (SAFException var11) {
            if (verbose) {
               Verbose.logException(var11);
            }
         }

      }
   }

   private static void acknowledge(SAFResult var0) {
      SAFConversationInfo var1 = var0.getConversationInfo();

      assert var1 != null;

      WsrmSequenceContext var2 = (WsrmSequenceContext)var1.getContext();

      assert var2 != null;

      EndpointReference var3 = var2.getAcksTo();
      List var4 = var0.getSequenceNumbers();
      ArrayList var5 = new ArrayList(var4);

      try {
         WsrmSequenceSender.acknowledge(var1, var5, var3, var2, var2.isSoap12());
      } catch (SAFException var7) {
         if (verbose) {
            Verbose.logException(var7);
         }
      }

   }
}
