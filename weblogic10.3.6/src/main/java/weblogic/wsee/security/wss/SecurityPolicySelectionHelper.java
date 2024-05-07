package weblogic.wsee.security.wss;

import java.util.HashMap;
import java.util.Map;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPException;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.sps.SmartPolicySelector;
import weblogic.wsee.security.wss.sps.SmartSecurityPolicyBlueprint;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.WSS11Context;

public class SecurityPolicySelectionHelper {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicySelectionHelper.class);
   private static final boolean debug = false;
   private SmartPolicySelector sps;
   private SmartSecurityPolicyBlueprint[] blueprints;
   private PolicySelectionPreference preference;
   private NormalizedExpression normalizedPolicy;
   public static final int NONE = 0;
   public static final int OPTIONAL = 1;
   public static final int MUST = 2;
   public static final int ERROR = -1;

   public SecurityPolicySelectionHelper(NormalizedExpression var1, boolean var2) throws WSSecurityException, SecurityPolicyArchitectureException {
      this(var1, (Map)null, var2, (WSS11Context)null);
   }

   public SecurityPolicySelectionHelper(NormalizedExpression var1, MessageContext var2, boolean var3) throws WSSecurityException, SecurityPolicyArchitectureException {
      this.sps = null;
      this.blueprints = null;
      this.preference = null;
      this.normalizedPolicy = null;
      HashMap var4 = new HashMap();
      var4.put("weblogic.wsee.policy.selection.preference", PolicySelectionPreference.getPolicySelectionPreference(var2));
      this.init(var1, var4, var3, (WSS11Context)null);
   }

   public SecurityPolicySelectionHelper(NormalizedExpression var1, PolicySelectionPreference var2, boolean var3) throws WSSecurityException, SecurityPolicyArchitectureException {
      this.sps = null;
      this.blueprints = null;
      this.preference = null;
      this.normalizedPolicy = null;
      HashMap var4 = new HashMap();
      var4.put("weblogic.wsee.policy.selection.preference", var2);
      this.init(var1, var4, var3, (WSS11Context)null);
   }

   public SecurityPolicySelectionHelper(NormalizedExpression var1, Map<String, Object> var2, boolean var3, WSS11Context var4) throws WSSecurityException, SecurityPolicyArchitectureException {
      this.sps = null;
      this.blueprints = null;
      this.preference = null;
      this.normalizedPolicy = null;
      if (null == var2) {
         var2 = new HashMap();
      }

      this.init(var1, (Map)var2, var3, var4);
   }

   private void init(NormalizedExpression var1, Map<String, Object> var2, boolean var3, WSS11Context var4) throws WSSecurityException, SecurityPolicyArchitectureException {
      if (null != var1) {
         if (null == var4) {
            SoapMessageContext var5 = new SoapMessageContext(true);

            try {
               var4 = new WSS11Context(var5.getMessage().getSOAPBody().getParentNode());
            } catch (SOAPException var7) {
               throw new WSSecurityException(var7);
            }
         }

         this.normalizedPolicy = var1;
         this.sps = new SmartPolicySelector(var1, var2, var3, var4);
         if (null != this.sps) {
            this.blueprints = this.sps.getSmartPolicyBlueprint();
         }

      }
   }

   public PolicyAlternative getFirstPolicyAlternative(PolicySelectionPreference var1) {
      PolicyAlternative var2 = this.sps.getFirstPolicyAlternative(var1);
      return var2;
   }

   public NormalizedExpression generateSortedecurityNormalizedExpression(PolicySelectionPreference var1) {
      if (!this.sps.hasSecurityPolicyAlternative()) {
         return null;
      } else if (this.sps.getPolicyAlternativeCount() != 1 && this.getAlternatives() != 1) {
         SmartSecurityPolicyBlueprint[] var2 = this.sps.getSmartPolicyBlueprint();
         int[] var3 = this.sps.getPolicyPriorityIndex(var1);
         NormalizedExpression var4 = new NormalizedExpression();

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4.addAlternative(var2[var3[var5]].getPolicyAlternative());
         }

         return var4;
      } else {
         return this.normalizedPolicy;
      }
   }

   public NormalizedExpression generateSortedecurityNormalizedExpression() {
      if (!this.sps.hasSecurityPolicyAlternative()) {
         return null;
      } else if (this.sps.getPolicyAlternativeCount() != 1 && this.getAlternatives() != 1) {
         SmartSecurityPolicyBlueprint[] var1 = this.sps.getSmartPolicyBlueprint();
         int[] var2 = this.sps.getPolicyPriorityIndex();
         NormalizedExpression var3 = new NormalizedExpression();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3.addAlternative(var1[var2[var4]].getPolicyAlternative());
         }

         return var3;
      } else {
         return this.normalizedPolicy;
      }
   }

   public PolicySelectionPreference getPolicySelectionPreference() {
      return this.preference;
   }

   public void setPolicySelectionPreference(PolicySelectionPreference var1) {
      if (null == var1) {
         this.preference = new PolicySelectionPreference();
      } else {
         this.preference = var1;
      }

   }

   public int getAlternatives() {
      return this.blueprints == null ? 0 : this.blueprints.length;
   }

   private boolean noPolicy() {
      return this.blueprints == null || this.blueprints.length == 0;
   }

   public int digitalSignatureKeyPairRequirement() {
      if (this.noPolicy()) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.blueprints.length; ++var2) {
            if (this.blueprints[var2].isSignatureRequired()) {
               ++var1;
            }
         }

         if (var1 == 0) {
            return 0;
         } else if (var1 == this.blueprints.length) {
            return 2;
         } else {
            return 1;
         }
      }
   }

   public int encryptionKeyPairRequirement() {
      if (this.noPolicy()) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.blueprints.length; ++var2) {
            if (this.blueprints[var2].isEncryptionRequired()) {
               ++var1;
            }
         }

         if (var1 == 0) {
            return 0;
         } else if (var1 == this.blueprints.length) {
            return 2;
         } else {
            return 1;
         }
      }
   }

   public int samlTokenRequirement() {
      if (this.noPolicy()) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.blueprints.length; ++var2) {
            if (this.blueprints[var2].isSamlTokenAuth()) {
               ++var1;
            }
         }

         if (var1 == 0) {
            return 0;
         } else if (var1 == this.blueprints.length) {
            return 2;
         } else {
            return 1;
         }
      }
   }

   public int usernameTokenRequirement() {
      if (this.noPolicy()) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.blueprints.length; ++var2) {
            if (this.blueprints[var2].isUsernameTokenAuth()) {
               ++var1;
            }
         }

         if (var1 == 0) {
            return 0;
         } else if (var1 == this.blueprints.length) {
            return 2;
         } else {
            return 1;
         }
      }
   }

   public int secureConversationRequirement() {
      if (this.noPolicy()) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.blueprints.length; ++var2) {
            if (this.blueprints[var2].isSecureConversation()) {
               ++var1;
            }
         }

         if (var1 == 0) {
            return 0;
         } else if (var1 == this.blueprints.length) {
            return 2;
         } else {
            return 1;
         }
      }
   }

   public int secureConversation13Requirement() {
      if (this.noPolicy()) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.blueprints.length; ++var2) {
            if (this.blueprints[var2].isSecureConversation() && this.blueprints[var2].isWssc13()) {
               ++var1;
            }
         }

         if (var1 == 0) {
            return 0;
         } else if (var1 == this.blueprints.length) {
            return 2;
         } else {
            return 1;
         }
      }
   }
}
