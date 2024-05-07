package weblogic.wsee.wstx.wsat.security;

import java.io.InputStream;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jws.jaxws.ClientPolicyFeature;
import weblogic.jws.jaxws.policy.InputStreamPolicySource;

public abstract class ClientPolicyFeatureBuilder {
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugWSAT");
   private static String POLICY_HOME = "policies/wls_internal/wsat/";
   private static String BUILTIN_POLICY_HOME = "weblogic/wsee/policy/runtime/wls_internal/wsat/";

   public static ClientPolicyFeatureBuilder V10() {
      return ClientPolicyFeatureBuilder.ClientPolicyFeatureBuilderV10Holder.instance;
   }

   public static ClientPolicyFeatureBuilder V11() {
      return ClientPolicyFeatureBuilder.ClientPolicyFeatureBuilderV11Holder.instance;
   }

   abstract String getOutboundPolicy();

   abstract String getInboundPolicy();

   abstract String getIssuedTokenPolicy();

   public ClientPolicyFeature newClientPolicyFeature() {
      ClientPolicyFeature var1 = new ClientPolicyFeature();
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      var1.setEffectivePolicy(new InputStreamPolicySource(new InputStream[]{var2.getResourceAsStream(this.getIssuedTokenPolicy())}));
      if (isExisted(this.getInboundPolicy())) {
         if (logger.isDebugEnabled()) {
            logger.debug("WSAT registration inbound policy " + this.getInboundPolicy() + " will be used as client side policy.");
         }

         var1.setEffectivePolicyForInputMessage(new InputStreamPolicySource(new InputStream[]{var2.getResourceAsStream(this.getInboundPolicy())}));
      }

      if (isExisted(this.getOutboundPolicy())) {
         if (logger.isDebugEnabled()) {
            logger.debug("WSAT registration outbound policy " + this.getOutboundPolicy() + " will be used as client side policy.");
         }

         var1.setEffectivePolicyForOutputMessage(new InputStreamPolicySource(new InputStream[]{var2.getResourceAsStream(this.getOutboundPolicy())}));
      }

      return var1;
   }

   private static boolean isExisted(String var0) {
      return Thread.currentThread().getContextClassLoader().getResourceAsStream(var0) != null;
   }

   private static String getPolicy(String var0) {
      String var1 = null;
      if (isExisted(POLICY_HOME + var0)) {
         return POLICY_HOME + var0;
      } else {
         var1 = BUILTIN_POLICY_HOME + var0;
         if (logger.isDebugEnabled()) {
            logger.debug("policy file " + var0 + " will be loaded from " + var1);
         }

         return var1;
      }
   }

   static class ClientPolicyFeatureBuilder_v11 extends ClientPolicyFeatureBuilder {
      static final String WSAT11_POLICY = "wsat11-registration-policy.xml";
      private static final String WSAT11_INBOUND_POLICY = "wsat11-registration-inbound-policy.xml";
      private static final String WSAT11_OUTBOUND_POLICY = "wsat11-registration-outbound-policy.xml";
      private static String outboundPolicy = ClientPolicyFeatureBuilder.getPolicy("wsat11-registration-outbound-policy.xml");
      private static String inboundPolicy = ClientPolicyFeatureBuilder.getPolicy("wsat11-registration-inbound-policy.xml");
      private static String issuedTokenPolicy = ClientPolicyFeatureBuilder.getPolicy("wsat11-registration-policy.xml");

      String getOutboundPolicy() {
         return outboundPolicy;
      }

      String getInboundPolicy() {
         return inboundPolicy;
      }

      String getIssuedTokenPolicy() {
         return issuedTokenPolicy;
      }
   }

   static class ClientPolicyFeatureBuilder_v10 extends ClientPolicyFeatureBuilder {
      private static final String WSAT_INBOUND_POLICY = "wsat-registration-inbound-policy.xml";
      private static final String WSAT_OUTBOUND_POLICY = "wsat-registration-outbound-policy.xml";
      private static final String WSAT_POLICY = "wsat-registration-policy.xml";
      private static String outboundPolicy = ClientPolicyFeatureBuilder.getPolicy("wsat-registration-outbound-policy.xml");
      private static String inboundPolicy = ClientPolicyFeatureBuilder.getPolicy("wsat-registration-inbound-policy.xml");
      private static String issuedTokenPolicy = ClientPolicyFeatureBuilder.getPolicy("wsat-registration-policy.xml");

      String getOutboundPolicy() {
         return outboundPolicy;
      }

      String getInboundPolicy() {
         return inboundPolicy;
      }

      String getIssuedTokenPolicy() {
         return issuedTokenPolicy;
      }
   }

   private static class ClientPolicyFeatureBuilderV11Holder {
      private static ClientPolicyFeatureBuilder instance = new ClientPolicyFeatureBuilder_v11();
   }

   private static class ClientPolicyFeatureBuilderV10Holder {
      private static ClientPolicyFeatureBuilder instance = new ClientPolicyFeatureBuilder_v10();
   }
}
