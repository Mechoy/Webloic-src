package weblogic.wsee.security.policy12.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.security.policy12.assertions.BootstrapPolicy;
import weblogic.wsee.security.policy12.assertions.SecureConversationToken;
import weblogic.wsee.security.wssp.SecureConversationTokenAssertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;

public class SecureConversationTokenAssertionImpl extends SecurityContextTokenAssertionImpl implements SecureConversationTokenAssertion {
   private static final String WS_SX_SCT_TOKEN_TYPE_V13 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct";
   private static final String WS_SX_DK_TOKEN_TYPE_V13 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";
   private static final String[] WSSC_TOKEN_TYPES_V2005 = new String[]{"http://schemas.xmlsoap.org/ws/2005/02/sc/sct", "http://schemas.xmlsoap.org/ws/2005/02/sc/dk"};
   private static final String[] WS_SX_WSSC_TOKEN_TYPES_V13 = new String[]{"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct", "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk"};
   SecureConversationToken secToken;
   private boolean is200502 = false;
   private boolean isWSSC13 = false;

   SecureConversationTokenAssertionImpl(SecureConversationToken var1) {
      super(var1);
      this.secToken = var1;
      this.is200502 = var1.isSC200502SecurityContextToken();
      this.isWSSC13 = !this.is200502;
   }

   public String getIssuerForSecurityContextToken() {
      return this.secToken.getIssuer();
   }

   public SecurityPolicyAssertionInfo[] getBootstrapPolicy() {
      ArrayList var1 = new ArrayList();
      BootstrapPolicy var2 = this.secToken.getBootstrapPolicy();
      if (var2 != null) {
         NormalizedExpression var3 = var2.getNestedPolicy();
         Set var4 = var3.getPolicyAlternatives();
         if (var4 != null) {
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               SecurityPolicyAssertionInfo var6 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo((PolicyAlternative)var5.next());
               if (var6 != null) {
                  var1.add(var6);
               }
            }
         }
      }

      return (SecurityPolicyAssertionInfo[])var1.toArray(new SecurityPolicyAssertionInfo[0]);
   }

   public boolean isSC200502SecurityContextToken() {
      return this.is200502;
   }

   public boolean isWSSC13SecurityContextToken() {
      return this.isWSSC13;
   }

   public NormalizedExpression getNormalizedBootstrapPolicy() {
      return this.secToken.getNormalizedBootstrapPolicy();
   }

   public String getSctTokenType() {
      if (this.isSC200502SecurityContextToken()) {
         return "http://schemas.xmlsoap.org/ws/2005/02/sc/sct";
      } else {
         return this.isWSSC13SecurityContextToken() ? "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct" : "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct";
      }
   }

   public String getDkTokenType() {
      if (this.isSC200502SecurityContextToken()) {
         return "http://schemas.xmlsoap.org/ws/2005/02/sc/dk";
      } else {
         return this.isWSSC13SecurityContextToken() ? "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk" : "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";
      }
   }

   public String[] getTokenType() {
      if (this.isSC200502SecurityContextToken()) {
         return WSSC_TOKEN_TYPES_V2005;
      } else {
         return this.isWSSC13SecurityContextToken() ? WS_SX_WSSC_TOKEN_TYPES_V13 : WS_SX_WSSC_TOKEN_TYPES_V13;
      }
   }
}
