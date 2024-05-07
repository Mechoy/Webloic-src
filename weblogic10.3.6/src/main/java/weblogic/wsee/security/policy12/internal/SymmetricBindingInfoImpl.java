package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.EncryptionToken;
import weblogic.wsee.security.policy12.assertions.ProtectionToken;
import weblogic.wsee.security.policy12.assertions.SignatureToken;
import weblogic.wsee.security.policy12.assertions.SymmetricBinding;
import weblogic.wsee.security.wssp.EncryptionTokenAssertion;
import weblogic.wsee.security.wssp.ProtectionTokenAssertion;
import weblogic.wsee.security.wssp.SignatureTokenAssertion;
import weblogic.wsee.security.wssp.SymmetricBindingInfo;

public class SymmetricBindingInfoImpl extends SecurityBindingPropertiesAssertionImpl implements SymmetricBindingInfo {
   private SignatureTokenAssertionImpl sigToken;
   private EncryptionTokenAssertionImpl encToken;
   private ProtectionTokenAssertionImpl protectToken;
   private boolean encryptedKeyRequired = false;

   SymmetricBindingInfoImpl(SymmetricBinding var1) {
      super(var1);
      SignatureToken var2 = var1.getSignatureToken();
      if (var2 != null) {
         this.sigToken = new SignatureTokenAssertionImpl(var2);
      }

      EncryptionToken var3 = var1.getEncryptionToken();
      if (var3 != null) {
         this.encToken = new EncryptionTokenAssertionImpl(var3);
      }

      ProtectionToken var4 = var1.getProtectionToken();
      if (var4 != null) {
         this.protectToken = new ProtectionTokenAssertionImpl(var4);
      }

   }

   public SignatureTokenAssertion getSignatureTokenAssertion() {
      return this.sigToken;
   }

   public EncryptionTokenAssertion getEncryptionTokenAssertion() {
      return this.encToken;
   }

   public ProtectionTokenAssertion getProtectionTokenAssertion() {
      return this.protectToken;
   }

   public boolean isEncryptedKeyRequired() {
      return this.encryptedKeyRequired;
   }

   void setEncryptedKeyRequired(boolean var1) {
      this.encryptedKeyRequired = var1;
   }
}
