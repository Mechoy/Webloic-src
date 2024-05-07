package weblogic.wsee.security.wss;

import java.util.HashMap;
import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.wss.plan.SecurityMessageInspector;
import weblogic.wsee.security.wss.plan.SecurityPolicyOutline;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.policy.SecurityPolicyInspectionException;
import weblogic.wsee.security.wss.sps.SmartPolicySelector;
import weblogic.wsee.security.wss.sps.SmartSecurityPolicyBlueprint;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.SecurityBuilderImpl;
import weblogic.xml.crypto.wss11.internal.SecurityValidator;
import weblogic.xml.crypto.wss11.internal.WSS11Context;

public class SecurityPolicyInspector extends SecurityPolicyConductor {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyInspector.class);
   private static final boolean debug = false;

   public SecurityPolicyInspector(SecurityValidator var1, WSS11Context var2) {
      super(new SecurityBuilderImpl(var2), var1, var2);
   }

   protected static Map<String, Object> initBlueprintPropertiesFromContext(SOAPMessageContext var0) {
      HashMap var1 = new HashMap();
      var1.put("weblogic.wsee.security.message_age", var0.getProperty("weblogic.wsee.security.message_age"));
      if (null != var0.getProperty("weblogic.wsee.policy.compat.preference")) {
         var1.put("weblogic.wsee.policy.compat.preference", var0.getProperty("weblogic.wsee.policy.compat.preference"));
      }

      var1.put("weblogic.wsee.policy.selection.preference", new PolicySelectionPreference("ValidationOnly"));
      var1.put("oracle.contextelement.saml2.AttributeOnly", var0.getProperty("oracle.contextelement.saml2.AttributeOnly"));
      var1.put("weblogic.wsee.security.saml.attributies", var0.getProperty("weblogic.wsee.security.saml.attributies"));
      return var1;
   }

   protected void processMessagePolicy(NormalizedExpression var1, NormalizedExpression var2, SOAPMessageContext var3, boolean var4) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      Map var5 = initBlueprintPropertiesFromContext(var3);
      SmartPolicySelector var6;
      if (var4) {
         var6 = new SmartPolicySelector(this.sValidator, this.sbuilder, var1, var2, var5, var4, (WSS11Context)null);
      } else {
         var6 = new SmartPolicySelector(this.sValidator, this.sbuilder, var2, var1, var5, var4, (WSS11Context)null);
      }

      SecurityPolicyOutline var7 = null;
      Object var8 = this.securityCtx.getPolicyOutline();
      if (var8 != null && var8 instanceof SecurityPolicyOutline) {
         var7 = (SecurityPolicyOutline)var8;
      }

      SmartSecurityPolicyBlueprint[] var9 = var6.getSmartPolicyBlueprint(var7);
      if (null == var9) {
         if (0 != var6.getReasonCode()) {
            String var14 = var6.tellMeWhy();
            throw new SecurityPolicyException(var14);
         } else {
            if (verbose) {
               Verbose.log((Object)"No policy aletrnative has been selected");
            }

            throw new SecurityPolicyException("No security policy found!");
         }
      } else {
         SecurityPolicyInspectionException var10 = null;
         int var11 = 0;

         while(var11 < var9.length) {
            try {
               this.inspectMessageByPolicy(var9[var11], var3);
               this.securityCtx.setRequestPolicyIdx(var9[var11].getPolicyLocationIdx());
               this.securityCtx.setPolicyOutline(var9[var11].getSecurityPolicyBlueprint());
               return;
            } catch (SecurityPolicyInspectionException var13) {
               var10 = var13;
               ++var11;
            }
         }

         if (null == var10) {
            throw new SecurityPolicyInspectionException("Unknown error???");
         } else {
            throw var10;
         }
      }
   }

   private void inspectMessageByPolicy(SmartSecurityPolicyBlueprint var1, SOAPMessageContext var2) throws SecurityPolicyInspectionException, SecurityPolicyArchitectureException, WSSecurityException {
      SecurityMessageInspector var3 = new SecurityMessageInspector(var2, this.securityCtx);
      boolean var4 = "true".equalsIgnoreCase((String)var2.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME"));
      var3.inspectWssMessage(var1, var4);
   }
}
