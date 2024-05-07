package weblogic.wsee.security.wss.policy.wssp;

import java.util.ArrayList;
import java.util.List;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.policy.XBeanUtils;
import weblogic.wsee.security.policy.assertions.IdentityAssertion;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.wss.plan.fact.SecurityTokenFactory;
import weblogic.wsee.security.wss.policy.IdentityPolicy;

public class IdentityPolicyImpl implements IdentityPolicy {
   private List validIdentityTokens = new ArrayList();

   public IdentityPolicyImpl() {
   }

   public IdentityPolicyImpl(IdentityAssertion var1) {
      SecurityTokenType[] var2 = var1.getXbean().getIdentity().getSupportedTokens().getSecurityTokenArray();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         SecurityTokenType var4 = var2[var3];
         this.validIdentityTokens.add(new SecurityToken(XBeanUtils.getElement(var4), (String)null, var4.getTokenType(), var4.getIncludeInMessage()));
      }

   }

   /** @deprecated */
   public void addIdentityToken(SecurityTokenType var1) {
      this.addIdentityToken(SecurityTokenFactory.makeSecurityToken(var1));
   }

   public void addIdentityToken(SecurityToken var1) {
      if (null != var1) {
         this.doAddIdentityToken(var1);
      }

   }

   private void doAddIdentityToken(SecurityToken var1) {
      this.validIdentityTokens.add(var1);
   }

   public List getValidIdentityTokens() {
      return this.validIdentityTokens;
   }

   public boolean isAuthenticationRequired() {
      return 0 != this.validIdentityTokens.size();
   }
}
