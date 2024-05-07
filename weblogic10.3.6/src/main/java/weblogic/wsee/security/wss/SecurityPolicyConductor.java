package weblogic.wsee.security.wss;

import java.util.HashMap;
import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.SecurityBuilder;
import weblogic.xml.crypto.wss11.internal.SecurityValidator;
import weblogic.xml.crypto.wss11.internal.WSS11Context;

public abstract class SecurityPolicyConductor {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyConductor.class);
   private static final boolean debug = false;
   protected SecurityBuilder sbuilder = null;
   protected SecurityValidator sValidator = null;
   protected WSS11Context securityCtx;

   public SecurityPolicyConductor(SecurityBuilder var1, WSS11Context var2) {
      if (null == var1) {
         throw new IllegalArgumentException("Null security builder found");
      } else {
         this.sbuilder = var1;
         this.securityCtx = var2;
      }
   }

   public SecurityPolicyConductor(SecurityBuilder var1, SecurityValidator var2, WSS11Context var3) {
      if (null == var1) {
         throw new IllegalArgumentException("Null security builder found");
      } else {
         this.sbuilder = var1;
         this.sValidator = var2;
         this.securityCtx = var3;
      }
   }

   public SecurityPolicyConductor(SecurityValidator var1, WSS11Context var2) {
      if (null == var1) {
         throw new IllegalArgumentException("Null security builder found");
      } else {
         this.sValidator = var1;
         this.securityCtx = var2;
      }
   }

   public void processRequestOutbound(NormalizedExpression var1, SOAPMessageContext var2) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      if (SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(var1)) {
         this.processRequestOutbound(var1, (NormalizedExpression)null, var2);
      }

   }

   public void processRequestOutbound(NormalizedExpression var1, NormalizedExpression var2, SOAPMessageContext var3) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      this.processMessagePolicy(var1, var2, var3, true);
   }

   public void processResponseOutbound(NormalizedExpression var1, SOAPMessageContext var2) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      if (SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(var1)) {
         this.processResponseOutbound((NormalizedExpression)null, var1, var2);
      }

   }

   public void processResponseOutbound(NormalizedExpression var1, NormalizedExpression var2, SOAPMessageContext var3) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      this.processMessagePolicy(var1, var2, var3, false);
   }

   protected abstract void processMessagePolicy(NormalizedExpression var1, NormalizedExpression var2, SOAPMessageContext var3, boolean var4) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException;

   protected static Map<String, Object> initBlueprintPropertiesFromContext(SOAPMessageContext var0) {
      HashMap var1 = new HashMap();
      var1.put("weblogic.wsee.security.message_age", var0.getProperty("weblogic.wsee.security.message_age"));
      PolicySelectionPreference var2 = PolicySelectionPreference.getPolicySelectionPreference(var0);
      var1.put("weblogic.wsee.policy.selection.preference", var2);
      String var3 = (String)var0.getProperty("weblogic.wsee.policy.compat.preference");
      var1.put("weblogic.wsee.policy.compat.preference", var3);
      var1.put("oracle.contextelement.saml2.AttributeOnly", var0.getProperty("oracle.contextelement.saml2.AttributeOnly"));
      var1.put("weblogic.wsee.security.saml.attributies", var0.getProperty("weblogic.wsee.security.saml.attributies"));
      return var1;
   }
}
