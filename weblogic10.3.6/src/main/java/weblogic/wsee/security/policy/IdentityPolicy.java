package weblogic.wsee.security.policy;

import java.util.ArrayList;
import java.util.List;
import weblogic.wsee.security.policy.assertions.IdentityAssertion;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;

public class IdentityPolicy {
   private List validIdentityTokens = new ArrayList();

   public IdentityPolicy(IdentityAssertion var1) {
      SecurityTokenType[] var2 = var1.getXbean().getIdentity().getSupportedTokens().getSecurityTokenArray();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         SecurityTokenType var4 = var2[var3];
         this.validIdentityTokens.add(new SecurityToken(XBeanUtils.getElement(var4), (String)null, var4.getTokenType(), var4.getIncludeInMessage()));
      }

   }

   public List getValidIdentityTokens() {
      return this.validIdentityTokens;
   }
}
