package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.Wss11;
import weblogic.wsee.security.wssp.Wss11Options;

public class Wss11OptionsImpl extends Wss10OptionsImpl implements Wss11Options {
   private Wss11 wss11;

   Wss11OptionsImpl(Wss11 var1) {
      super(var1);
      this.wss11 = var1;
   }

   public boolean isMustSupportThumbprintReference() {
      return this.wss11.getMustSupportRefThumbprint() != null;
   }

   public boolean isMustSupportEncryptedKeyReference() {
      return this.wss11.getMustSupportRefEncryptedKey() != null;
   }

   public boolean isSignatureConfirmationRequired() {
      return this.wss11.getRequireSignatureConfirmation() != null;
   }

   public boolean isMustSupportThumbprintReferenceOptional() {
      return this.wss11.getMustSupportRefThumbprint() != null ? this.wss11.getMustSupportRefThumbprint().isOptional() : true;
   }

   public boolean isMustSupportEncryptedKeyReferenceOptional() {
      return this.wss11.getMustSupportRefEncryptedKey() != null ? this.wss11.getMustSupportRefEncryptedKey().isOptional() : true;
   }

   public boolean isSignatureConfirmationRequiredOptional() {
      return this.wss11.getRequireSignatureConfirmation() != null ? this.wss11.getRequireSignatureConfirmation().isOptional() : true;
   }
}
