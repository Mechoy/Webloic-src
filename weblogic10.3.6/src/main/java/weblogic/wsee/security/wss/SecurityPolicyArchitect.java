package weblogic.wsee.security.wss;

import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.wss.plan.SecurityMessageArchitect;
import weblogic.wsee.security.wss.plan.SecurityPolicyBlueprint;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.sps.SmartPolicySelector;
import weblogic.wsee.security.wss.sps.SmartSecurityPolicyBlueprint;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.SecurityBuilder;
import weblogic.xml.crypto.wss11.internal.SecurityValidator;
import weblogic.xml.crypto.wss11.internal.WSS11Context;

public class SecurityPolicyArchitect extends SecurityPolicyConductor {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyArchitect.class);
   private static final boolean debug = false;

   public SecurityPolicyArchitect(SecurityBuilder var1, WSS11Context var2) {
      super(var1, var2);
   }

   public SecurityPolicyArchitect(SecurityBuilder var1, SecurityValidator var2, WSS11Context var3) {
      super(var1, var2, var3);
   }

   protected void processMessagePolicy(NormalizedExpression var1, NormalizedExpression var2, SOAPMessageContext var3, boolean var4) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      Map var5 = initBlueprintPropertiesFromContext(var3);
      if (verbose) {
         if (var1 == null) {
            Verbose.say("Request policy is null");
         } else {
            Verbose.say("Request policy:" + var1.toString());
         }

         if (var2 == null) {
            Verbose.say("No Normalized Response Policy");
         } else {
            Verbose.say("Response policy:" + var2.toString());
         }
      }

      SmartPolicySelector var6;
      if (var4) {
         var6 = new SmartPolicySelector(this.sbuilder, var1, var2, var5, var4, this.securityCtx);
      } else {
         if (this.securityCtx.hasDerivedKey()) {
            var5.put("DerivedKeysToken", "DerivedKeysToken");
         }

         var6 = new SmartPolicySelector(this.sbuilder, var2, var1, var5, var4, this.securityCtx);
      }

      SmartSecurityPolicyBlueprint[] var7 = var6.getSmartPolicyBlueprint();
      if (null != var7 && 0 != var7.length) {
         if (1 == var7.length) {
            this.processOutbound(var7[0].getSecurityPolicyBlueprint(), var3);
            this.securityCtx.setRequestPolicyIdx(0);
         } else {
            SecurityPolicyException var8 = null;
            if (var4) {
               int[] var17 = var6.getPolicyPriorityIndex();
               int var19 = 0;

               while(var19 < var7.length) {
                  try {
                     this.processOutbound(var7[var17[var19]].getSecurityPolicyBlueprint(), var3);
                     this.securityCtx.setRequestPolicyIdx(var17[var19]);
                     return;
                  } catch (SecurityPolicyException var14) {
                     Verbose.log((Object)("Policy alternative [" + var17[var19] + "] got problems, error =" + var14.getMessage()));
                     if (null == var8) {
                        var8 = var14;
                     }

                     ++var19;
                  }
               }
            } else {
               int var9 = this.securityCtx.getRequestPolicyIdx();
               Verbose.log((Object)("Use the privous selected one for response idx=" + var9));

               int var11;
               try {
                  boolean var10 = false;

                  for(var11 = 0; var11 < var7.length; ++var11) {
                     if (var7[var11].getPolicyLocationIdx() == var9) {
                        this.processOutbound(var7[var11].getSecurityPolicyBlueprint(), var3);
                        return;
                     }
                  }
               } catch (SecurityPolicyException var16) {
                  Verbose.log((Object)("Policy alternative [" + var9 + "] got problems, error =" + var16.getMessage()));
                  var8 = var16;
               }

               int[] var18 = var6.getPolicyPriorityIndex();

               for(var11 = 0; var11 < var7.length; ++var11) {
                  int var12 = var18[var11];
                  if (var9 != var12) {
                     try {
                        this.processOutbound(var7[var12].getSecurityPolicyBlueprint(), var3);
                        return;
                     } catch (SecurityPolicyException var15) {
                        Verbose.log((Object)("Policy alternative [" + var12 + "] got problems, error =" + var15.getMessage()));
                     }
                  }
               }
            }

            throw var8;
         }
      } else if (0 != var6.getReasonCode()) {
         throw new SecurityPolicyException(var6.tellMeWhy());
      } else {
         if (verbose) {
            Verbose.log((Object)"No policy aletrnative has been selected");
         }

         throw new SecurityPolicyException("No good policy aletrnative found");
      }
   }

   protected void processOutbound(SecurityPolicyBlueprint var1, SOAPMessageContext var2) throws PolicyException, WSSecurityException, SecurityPolicyArchitectureException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      this.securityCtx.setPolicyOutline(var1);
      SecurityMessageArchitect var3 = new SecurityMessageArchitect(this.securityCtx);
      var3.buildWssMessage(var2, var1);
   }
}
