package weblogic.wsee.security.wssc.sct;

import javax.xml.namespace.QName;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.wss.policy.ClaimsBuilder;

public class SCTClaims {
   private static final String POLICY_URI = "http://www.bea.com/wls90/security/policy";
   private static final QName TOKEN_LIFETIME = new QName("http://www.bea.com/wls90/security/policy", "TokenLifeTime");
   private long lifetime;

   private SCTClaims(Node var1, Long var2) {
      this(var2);
      if (var1 != null) {
         String var3 = ClaimsBuilder.getClaimFromElt(var1, TOKEN_LIFETIME);
         if (var3 != null && var3.length() > 0) {
            try {
               var2 = Long.parseLong(var3) * 1000L;
            } catch (NumberFormatException var5) {
            }
         }
      }

   }

   private SCTClaims(Long var1) {
      this.lifetime = -1L;
      if (var1 != null) {
         this.lifetime = var1;
      }

   }

   public static SCTClaims newInstance(ContextHandler var0) {
      Long var1 = (Long)var0.getValue("weblogic.wsee.wssc.sct.lifetime");
      Node var2 = (Node)var0.getValue("weblogic.xml.crypto.wss.policy.Claims");
      return new SCTClaims(var2, var1);
   }

   public long getTokenLifetime() {
      return this.lifetime;
   }
}
