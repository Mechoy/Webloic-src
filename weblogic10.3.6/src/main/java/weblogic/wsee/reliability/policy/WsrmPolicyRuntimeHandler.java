package weblogic.wsee.reliability.policy;

import java.util.StringTokenizer;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicySelectionHelper;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.policy11.DeliveryAssurance;
import weblogic.wsee.reliability.policy11.RM11Assertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.util.Verbose;

public class WsrmPolicyRuntimeHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(WsrmPolicyRuntimeHandler.class);
   public static final String WSRM_POLICY_PROCESSED_PROPERTY = "weblogic.wsee.reliability.WsrmPolicyProcessed";

   public boolean processRequest(MessageContext var1, NormalizedExpression var2) throws PolicyException, JAXRPCException {
      return this.processRequest(var1, var2, (WsrmConstants.RMVersion)null);
   }

   public boolean processRequest(MessageContext var1, NormalizedExpression var2, WsrmConstants.RMVersion var3) throws PolicyException, JAXRPCException {
      if (var1.containsProperty("weblogic.wsee.reliability.WsrmPolicyProcessed")) {
         if (verbose) {
            Verbose.log((Object)"Skipping WS-RM policy processing, as it has already been performed on this message");
         }

         return true;
      } else {
         var1.setProperty("weblogic.wsee.reliability.WsrmPolicyProcessed", "true");
         PolicySelectionHelper var4 = new PolicySelectionHelper(var2, var1);
         if (!var4.hasReliabilityPolicyAssertion()) {
            return false;
         } else {
            PolicyAssertion[] var5 = var4.getReliabilityPolicyAssertion();
            if (var5 == null) {
               return false;
            } else {
               WlMessageContext var6 = WlMessageContext.narrow(var1);
               boolean var7 = false;
               int var8 = 0;

               while(var8 < var5.length) {
                  if ((var3 == null || !var3.isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11) || !(var5[var8] instanceof RM11Assertion)) && (var3 != null || !(var5[var8] instanceof RM11Assertion))) {
                     if ((var3 == null || var3 != WsrmConstants.RMVersion.RM_10 || !(var5[var8] instanceof RMAssertion)) && (var3 != null || !(var5[var8] instanceof RMAssertion))) {
                        ++var8;
                        continue;
                     }

                     this.processRM10Request((RMAssertion)var5[var8], var6);
                     var7 = true;
                     break;
                  }

                  this.processRM11Request((RM11Assertion)var5[var8], var2, var6);
                  var7 = true;
                  break;
               }

               if (!var7) {
                  throw new UnsupportedOperationException("Couldn't find any known RM Assertion");
               } else {
                  return true;
               }
            }
         }
      }
   }

   private void processRM11Request(RM11Assertion var1, NormalizedExpression var2, WlMessageContext var3) throws PolicyException {
      var3.setProperty("weblogic.wsee.wsrm.RMVersion", WsrmConstants.RMVersion.latest());
      if (!var1.getOptional()) {
         var3.setProperty("weblogic.wsee.rm.mandatory", "true");
      }

      this.handleRM11SequenceSTR(var1, var2, var3);
      this.handleRM11SequenceTranportSecurity(var1, var2, var3);
      this.handleRM11DeliveryAssurance(var1, var3);
   }

   private void handleRM11DeliveryAssurance(RM11Assertion var1, WlMessageContext var2) {
      DeliveryAssurance var3 = var1.getDeliveryAssurance();
      if (var3 != null) {
         var2.setProperty("weblogic.wsee.user.defined.qos", "true");
         if (var3.getInOrder() != null) {
            var2.setProperty("weblogic.wsee.qos.inorder", "true");
         }

         if (var3.getExactlyOnce() != null) {
            var2.setProperty("weblogic.wsee.qos.delivery", new Integer(1));
         } else if (var3.getAtMostOnce() != null) {
            var2.setProperty("weblogic.wsee.qos.delivery", new Integer(3));
         } else if (var3.getAtLeastOnce() != null) {
            var2.setProperty("weblogic.wsee.qos.delivery", new Integer(2));
         }

      }
   }

   private void handleRM11SequenceTranportSecurity(RM11Assertion var1, NormalizedExpression var2, WlMessageContext var3) throws PolicyException {
      if (var1.getSequenceTransportSecurity() != null) {
         var3.setProperty("weblogic.wsee.wsrm.SequenceTransportSecurity", "true");
         boolean var4 = false;
         if (!SecurityPolicyAssertionInfoFactory.hasTransportSecurityPolicy(var2)) {
            var4 = true;
         }

         if (var4) {
            throw new PolicyException("SequenceTransportSecurity set in WS-RM policy, but no SSL/TLS (HTTPS) policy is configured to support this on this service");
         }
      }

   }

   private void handleRM11SequenceSTR(RM11Assertion var1, NormalizedExpression var2, WlMessageContext var3) throws PolicyException {
      if (var1.getSequenceSTR() != null) {
         var3.setProperty("weblogic.wsee.wsrm.SequenceSTR", "true");
         if (!SecurityPolicyAssertionInfoFactory.hasWsTrustPolicy(var2)) {
            throw new PolicyException("SequenceSTR set in WS-RM policy, but no WS-Trust policy is configured to support this on this service");
         }
      }

   }

   private void processRM10Request(RMAssertion var1, WlMessageContext var2) throws PolicyException {
      var2.setProperty("weblogic.wsee.wsrm.RMVersion", WsrmConstants.RMVersion.RM_10);
      if (!var1.getOptional()) {
         var2.setProperty("weblogic.wsee.rm.mandatory", "true");
      }

      this.handleRM10InactivityTimeout(var1, var2);
      this.handleRM10BaseRetransmissionInterval(var1, var2);
      this.handleRM10AcknowledgementInterval(var1, var2);
      this.handleRM10ExponentialBackoff(var1, var2);
      this.handleRM10SequenceExpiration(var1, var2);
      this.handleRM10SequenceQOS(var1, var2);
   }

   private void handleRM10SequenceQOS(RMAssertion var1, WlMessageContext var2) throws PolicyException {
      SequenceQOS var3 = var1.getSeqQos();
      if (var3 != null) {
         this.parseRM10QOS(var3, var2);
      }

   }

   private void handleRM10SequenceExpiration(RMAssertion var1, WlMessageContext var2) {
      SequenceExpires var3 = var1.getSeqExpires();
      if (var3 != null) {
         var2.setProperty("weblogic.wsee.wsrm.SequenceExpiration", var3.getExpires());
         if (verbose) {
            Verbose.log((Object)("Sequence expires at " + var3.getExpires()));
         }
      }

   }

   private void handleRM10ExponentialBackoff(RMAssertion var1, WlMessageContext var2) {
      ExponentialBackoff var3 = var1.getExponentialBackoff();
      if (var3 != null) {
         var2.setProperty("weblogic.wsee.wsrm.RetransmissionExponentialBackoff", new Boolean(true));
         if (verbose) {
            Verbose.log((Object)"ExponentialBackoff is set to true");
         }
      }

   }

   private void handleRM10AcknowledgementInterval(RMAssertion var1, WlMessageContext var2) {
      AcknowledgementInterval var3 = var1.getAckInterval();
      if (var3 != null) {
         long var4 = var3.getInterval();
         var2.setProperty("weblogic.wsee.wsrm.AcknowledgementInterval", this.createDurationFromMillis(var4).toString());
         if (verbose) {
            Verbose.log((Object)("AcknowledgementInterval is " + var4 + " msec"));
         }
      }

   }

   private void handleRM10BaseRetransmissionInterval(RMAssertion var1, WlMessageContext var2) {
      BaseRetransmissionInterval var3 = var1.getBaseRetransmissionInterval();
      if (var3 != null) {
         long var4 = var3.getInterval();
         Duration var6 = this.createDurationFromMillis(var4);
         var2.setProperty("weblogic.wsee.wsrm.BaseRetransmissionInterval", var6.toString());
         if (verbose) {
            Verbose.log((Object)("BaseRetransmissionInterval is " + var4 + " msec"));
         }
      }

   }

   private void handleRM10InactivityTimeout(RMAssertion var1, WlMessageContext var2) {
      InactivityTimeout var3 = var1.getInactivityTimeout();
      if (var3 != null) {
         long var4 = var3.getTimeout();
         var2.setProperty("weblogic.wsee.wsrm.InactivityTimeout", this.createDurationFromMillis(var4).toString());
         if (verbose) {
            Verbose.log((Object)("InactivityTimeout is " + var4 + " msec"));
         }
      }

   }

   private Duration createDurationFromMillis(long var1) {
      try {
         return DatatypeFactory.newInstance().newDuration(var1);
      } catch (DatatypeConfigurationException var4) {
         throw new RuntimeException(var4.toString(), var4);
      }
   }

   private void parseRM10QOS(SequenceQOS var1, WlMessageContext var2) throws PolicyException {
      RM10QosInfo var3 = parseRM10QOS(var1);
      if (var3.safQoS >= 0) {
         var2.setProperty("weblogic.wsee.user.defined.qos", "true");
         var2.setProperty("weblogic.wsee.qos.delivery", var3.safQoS);
      }

      if (var3.inOrder) {
         var2.setProperty("weblogic.wsee.qos.inorder", "true");
      }

   }

   public static RM10QosInfo parseRM10QOS(SequenceQOS var0) throws PolicyException {
      RM10QosInfo var1 = new RM10QosInfo();
      if (var0 == null) {
         return var1;
      } else {
         StringTokenizer var2 = new StringTokenizer(var0.getQos());
         boolean var3 = false;
         boolean var4 = false;

         while(var2.hasMoreTokens()) {
            var1.userDefinedQoS = true;
            String var5 = var2.nextToken();
            if ("InOrder".equalsIgnoreCase(var5)) {
               if (var3) {
                  throw new PolicyException("InOrder QOS is set more than once");
               }

               var3 = true;
               var1.inOrder = true;
            } else if ("ExactlyOnce".equalsIgnoreCase(var5)) {
               if (var4) {
                  throw new PolicyException("Delivery QOS is set more than once");
               }

               var4 = true;
               var1.safQoS = new Integer(1);
            } else if ("AtMostOnce".equalsIgnoreCase(var5)) {
               if (var4) {
                  throw new PolicyException("Delivery QOS is set more than once");
               }

               var4 = true;
               var1.safQoS = new Integer(3);
            } else {
               if (!"AtLeastOnce".equalsIgnoreCase(var5)) {
                  throw new PolicyException("Unsupported Quality of Service specified: " + var5);
               }

               if (var4) {
                  throw new PolicyException("Delivery QOS is set more than once");
               }

               var4 = true;
               var1.safQoS = new Integer(2);
            }
         }

         return var1;
      }
   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   public static class RM10QosInfo {
      public boolean userDefinedQoS;
      public boolean inOrder;
      public int safQoS = -1;
   }
}
