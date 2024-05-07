package weblogic.wsee.reliability;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.j2ee.descriptor.wl.ReliabilityConfigBean;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;

public class WsrmServerConfigHandler extends WsrmHandler {
   private static final boolean verbose = Verbose.isVerbose(WsrmServerConfigHandler.class);

   public boolean handleRequest(MessageContext var1) {
      if (!(var1 instanceof WlMessageContext)) {
         return true;
      } else {
         WlMessageContext var2 = (WlMessageContext)var1;
         WsPort var3 = var2.getDispatcher().getWsPort();
         PortComponentBean var4 = var3.getPortComponent();
         ReliabilityConfigBean var5 = var4.getReliabilityConfig();
         if (var5 == null) {
            return true;
         } else {
            this.handleInactivityTimeout(var5, var2);
            this.handleAcknowledgementInterval(var5, var2);
            this.handleSequenceExpiration(var5, var2);
            this.handleBufferRetryCount(var5, var2);
            this.handleBufferRetryDelay(var5, var2);
            this.handleBaseRetransmissionInterval(var5, var2);
            this.handleRetransmissionExponentialBackoff(var5, var2);
            return true;
         }
      }
   }

   private void handleInactivityTimeout(ReliabilityConfigBean var1, WlMessageContext var2) {
      String var3 = var1.getInactivityTimeout();
      if (var3 != null) {
         var2.setProperty("weblogic.wsee.wsrm.InactivityTimeout", var3);
         if (verbose) {
            Verbose.say("InactivityTimeout is " + var3);
         }
      }

   }

   private void handleAcknowledgementInterval(ReliabilityConfigBean var1, WlMessageContext var2) {
      String var3 = var1.getAcknowledgementInterval();
      if (var3 != null) {
         var2.setProperty("weblogic.wsee.wsrm.AcknowledgementInterval", var3);
         if (verbose) {
            Verbose.say("AcknowledgementInterval is " + var3);
         }
      }

   }

   private void handleSequenceExpiration(ReliabilityConfigBean var1, WlMessageContext var2) {
      String var3 = var1.getSequenceExpiration();
      if (var3 != null) {
         var2.setProperty("weblogic.wsee.wsrm.SequenceExpiration", var3);
         if (verbose) {
            Verbose.log((Object)("Sequence expires at " + var3));
         }
      }

   }

   private void handleBufferRetryCount(ReliabilityConfigBean var1, WlMessageContext var2) {
      int var3 = var1.getBufferRetryCount();
      var2.setProperty("weblogic.wsee.wsrm.RetryCount", Long.toString((long)var3));
   }

   private void handleBufferRetryDelay(ReliabilityConfigBean var1, WlMessageContext var2) {
      String var3 = var1.getBufferRetryDelay();
      if (var3 != null) {
         try {
            Duration var4 = DatatypeFactory.newInstance().newDuration(var3);
            StringBuffer var5 = new StringBuffer();
            var5.append(var4.getYears()).append("y");
            var5.append(var4.getMonths()).append("m");
            var5.append(var4.getDays()).append("d");
            var5.append(var4.getHours()).append("h");
            var5.append(var4.getMinutes()).append("m");
            var5.append(var4.getSeconds()).append("s");
            weblogic.wsee.jws.container.Duration var6 = new weblogic.wsee.jws.container.Duration(var5.toString());
            var2.setProperty("weblogic.wsee.wsrm.RetryDelay", var6.toString());
            if (verbose) {
               Verbose.log((Object)("BufferRetryDelay is " + var6.toString() + " msec"));
            }
         } catch (Exception var7) {
            throw new JAXRPCException(var7.toString(), var7);
         }
      }

   }

   private void handleBaseRetransmissionInterval(ReliabilityConfigBean var1, WlMessageContext var2) {
      String var3 = var1.getBaseRetransmissionInterval();
      if (var3 != null) {
         var2.setProperty("weblogic.wsee.wsrm.BaseRetransmissionInterval", var3);
         if (verbose) {
            Verbose.log((Object)("BaseRetransmission is " + var3));
         }
      }

   }

   private void handleRetransmissionExponentialBackoff(ReliabilityConfigBean var1, WlMessageContext var2) {
      boolean var3 = var1.getRetransmissionExponentialBackoff();
      if (var3) {
         var2.setProperty("weblogic.wsee.wsrm.RetransmissionExponentialBackoff", var3);
      } else {
         var2.removeProperty("weblogic.wsee.wsrm.RetransmissionExponentialBackoff");
      }

      if (verbose) {
         Verbose.log((Object)("RetransmissionExponentialBackoff is " + var3));
      }

   }
}
