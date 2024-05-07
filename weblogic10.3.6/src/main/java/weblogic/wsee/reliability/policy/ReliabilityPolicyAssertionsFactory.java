package weblogic.wsee.reliability.policy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyAssertionFactory;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.policy11.RM11Assertion;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.xml.dom.DOMProcessingException;

public class ReliabilityPolicyAssertionsFactory extends PolicyAssertionFactory {
   public static final String RM_POLICY_NS_URI = "http://schemas.xmlsoap.org/ws/2004/03/rm";

   public PolicyAssertion createAssertion(Node var1) throws PolicyException {
      assert 1 == var1.getNodeType();

      Element var2 = (Element)var1;

      try {
         if (DOMUtils.equalsQName(var2, RMAssertion.RM_ASSERTION)) {
            return this.createRM10Assertion(var2);
         } else {
            return DOMUtils.equalsQName(var2, RM11Assertion.NAME) ? this.createRM11Assertion(var2) : null;
         }
      } catch (DOMProcessingException var4) {
         throw new PolicyException(var4);
      }
   }

   private PolicyAssertion createRM10Assertion(Element var1) throws DOMProcessingException, PolicyException {
      RMAssertion var2 = new RMAssertion();
      Element var3 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, InactivityTimeout.INACTIVITY_TIMEOUT.getNamespaceURI(), InactivityTimeout.INACTIVITY_TIMEOUT.getLocalPart());
      if (var3 != null) {
         String var4 = this.getRequiredAttributeValue(var3, InactivityTimeout.INACTIVITY_TIMEOUT, InactivityTimeout.MILLISECONDS_ATTRIBUTE);
         long var5 = Long.parseLong(var4);
         if (var5 <= 0L) {
            throw new PolicyException("Value of attribute '" + InactivityTimeout.MILLISECONDS_ATTRIBUTE + "' must be positive");
         }

         var2.setInactivityTimeout(new InactivityTimeout(var5));
      }

      Element var10 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, AcknowledgementInterval.ACKNOWLEDGEMENT_INTERVAL.getNamespaceURI(), AcknowledgementInterval.ACKNOWLEDGEMENT_INTERVAL.getLocalPart());
      if (var10 != null) {
         String var11 = this.getRequiredAttributeValue(var10, AcknowledgementInterval.ACKNOWLEDGEMENT_INTERVAL, AcknowledgementInterval.MILLISECONDS_ATTRIBUTE);
         long var6 = Long.parseLong(var11);
         if (var6 <= 0L) {
            throw new PolicyException("Value of attribute '" + AcknowledgementInterval.MILLISECONDS_ATTRIBUTE + "' must be positive");
         }

         var2.setAckInterval(new AcknowledgementInterval(var6));
      }

      Element var12 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, BaseRetransmissionInterval.BASE_RETRANSMISSION_INTERVAL.getNamespaceURI(), BaseRetransmissionInterval.BASE_RETRANSMISSION_INTERVAL.getLocalPart());
      if (var12 != null) {
         String var13 = this.getRequiredAttributeValue(var12, BaseRetransmissionInterval.BASE_RETRANSMISSION_INTERVAL, BaseRetransmissionInterval.MILLISECONDS_ATTRIBUTE);
         long var7 = Long.parseLong(var13);
         if (var7 <= 0L) {
            throw new PolicyException("Value of attribute '" + BaseRetransmissionInterval.MILLISECONDS_ATTRIBUTE + "' must be positive");
         }

         var2.setBaseRetransmissionInterval(new BaseRetransmissionInterval(var7));
      }

      Element var14 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, ExponentialBackoff.EXPONENTIAL_BACKOFF.getNamespaceURI(), ExponentialBackoff.EXPONENTIAL_BACKOFF.getLocalPart());
      if (var14 != null) {
         var2.setExponentialBackoff(new ExponentialBackoff());
      }

      Element var15 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, SequenceExpires.SEQUENCE_EXPIRES.getNamespaceURI(), SequenceExpires.SEQUENCE_EXPIRES.getLocalPart());
      if (var15 != null) {
         String var8 = this.getRequiredAttributeValue(var15, SequenceExpires.SEQUENCE_EXPIRES, SequenceExpires.EXPIRES_ATTRIBUTE);
         var2.setSeqExpires(new SequenceExpires(var8));
      }

      Element var16 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, SequenceQOS.SEQUENCE_QOS.getNamespaceURI(), SequenceQOS.SEQUENCE_QOS.getLocalPart());
      if (var16 != null) {
         String var9 = this.getRequiredAttributeValue(var16, SequenceQOS.SEQUENCE_QOS, SequenceQOS.QOS_ATTRIBUTE);
         var2.setSeqQos(new SequenceQOS(var9));
      }

      return var2;
   }

   private PolicyAssertion createRM11Assertion(Element var1) throws DOMProcessingException, PolicyException {
      RM11Assertion var2 = new RM11Assertion();
      var2.read(var1);
      return var2;
   }

   public static boolean hasRMPolicy(WsPort var0, PolicyServer var1, Map var2) throws PolicyException {
      boolean var3 = false;
      Iterator var4 = var0.getEndpoint().getMethods();

      while(var4.hasNext()) {
         WsMethod var5 = (WsMethod)var4.next();
         NormalizedExpression var6 = PolicyContext.getRequestEffectivePolicy(var0, var5, var1, var2);
         if (hasRMPolicy(var6)) {
            var3 = true;
         }
      }

      return var3;
   }

   public static boolean hasRMPolicy(NormalizedExpression var0) {
      return var0.containsPolicyAssertion(RMAssertion.class) || var0.containsPolicyAssertion(RM11Assertion.class);
   }

   public static List<WsrmConstants.RMVersion> getRMPolicyVersions(NormalizedExpression var0) {
      ArrayList var1 = new ArrayList();
      if (var0.containsPolicyAssertion(RMAssertion.class)) {
         var1.add(WsrmConstants.RMVersion.RM_10);
      }

      if (var0.containsPolicyAssertion(RM11Assertion.class)) {
         var1.add(WsrmConstants.RMVersion.RM_11);
      }

      return var1;
   }

   public static RM11Assertion copyRM11Assertion(RM11Assertion var0) {
      RM11Assertion var1 = new RM11Assertion();
      var1.setOptional(var0.isOptional());
      var1.setDeliveryAssurance(var0.getDeliveryAssurance());
      var1.setPolicyNamespaceUri(var0.getPolicyNamespaceUri());
      var1.setPolicySubject(var0.getPolicySubject());
      var1.setSequenceSTR(var0.getSequenceSTR());
      var1.setSequenceTransportSecurity(var0.getSequenceTransportSecurity());
      return var1;
   }

   public static RMAssertion copyRMAssertion(RMAssertion var0) {
      RMAssertion var1 = new RMAssertion();
      var1.setAckInterval(var0.getAckInterval());
      var1.setBaseRetransmissionInterval(var0.getBaseRetransmissionInterval());
      var1.setExponentialBackoff(var0.getExponentialBackoff());
      var1.setInactivityTimeout(var0.getInactivityTimeout());
      var1.setOptional(var0.isOptional());
      var1.setPolicyNamespaceUri(var0.getPolicyNamespaceUri());
      var1.setPolicySubject(var0.getPolicySubject());
      var1.setSeqExpires(var0.getSeqExpires());
      var1.setSeqQos(var0.getSeqQos());
      return var1;
   }

   public static boolean hasSSLTLSPolicy(NormalizedExpression var0) {
      RM11Assertion var1 = (RM11Assertion)var0.getPolicyAssertion(RM11Assertion.class);
      if (var1 != null) {
         return var1.getSequenceTransportSecurity() != null;
      } else {
         return false;
      }
   }

   private String getRequiredAttributeValue(Element var1, QName var2, QName var3) throws PolicyException {
      String var4 = DOMUtils.getAttributeValueAsString(var1, var3);
      if (var4 == null) {
         throw new PolicyException("Required attribute '" + var3 + "' is missing from the '" + var2 + "' assertion");
      } else {
         return var4;
      }
   }

   static {
      registerAssertion(RMAssertion.RM_ASSERTION, RMAssertion.class.getName());
      registerAssertion(RM11Assertion.NAME, RM11Assertion.class.getName());
   }
}
