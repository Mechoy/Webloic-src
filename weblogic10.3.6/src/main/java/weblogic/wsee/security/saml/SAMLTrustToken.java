package weblogic.wsee.security.saml;

import weblogic.wsee.security.wst.framework.TrustCredential;
import weblogic.wsee.security.wst.framework.TrustToken;

public class SAMLTrustToken extends SAMLTokenImpl implements TrustToken {
   SAMLTrustCredential cred;

   public SAMLTrustToken(SAMLTrustCredential var1) {
      super(var1.getCredential());
      this.cred = var1;
   }

   public TrustCredential getTrustCredential() {
      return this.cred;
   }
}
