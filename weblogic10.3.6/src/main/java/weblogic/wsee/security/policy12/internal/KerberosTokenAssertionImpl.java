package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.KerberosToken;
import weblogic.wsee.security.wssp.KerberosTokenAssertion;

public class KerberosTokenAssertionImpl extends TokenAssertionImpl implements KerberosTokenAssertion {
   private boolean isKeyIdentifierReferenceRequired = false;
   private boolean isWssKerberosV5ApReqToken11 = false;

   KerberosTokenAssertionImpl(KerberosToken var1) {
      super(var1);
      this.isKeyIdentifierReferenceRequired = var1.getRequireKeyIdentifierReference() != null;
      this.isWssKerberosV5ApReqToken11 = var1.getWssKerberosV5ApReqToken11() != null;
   }

   public boolean isKeyIdentifierReferenceRequired() {
      return this.isKeyIdentifierReferenceRequired;
   }

   public boolean isWssKerberosV5ApReqToken11() {
      return this.isWssKerberosV5ApReqToken11;
   }
}
