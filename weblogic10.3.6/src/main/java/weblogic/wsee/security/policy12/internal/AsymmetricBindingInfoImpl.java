package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.AsymmetricBinding;
import weblogic.wsee.security.policy12.assertions.InitiatorEncryptionToken;
import weblogic.wsee.security.policy12.assertions.InitiatorSignatureToken;
import weblogic.wsee.security.policy12.assertions.InitiatorToken;
import weblogic.wsee.security.policy12.assertions.RecipientEncryptionToken;
import weblogic.wsee.security.policy12.assertions.RecipientSignatureToken;
import weblogic.wsee.security.policy12.assertions.RecipientToken;
import weblogic.wsee.security.wssp.AsymmetricBindingInfo;
import weblogic.wsee.security.wssp.InitiatorEncryptionTokenAssertion;
import weblogic.wsee.security.wssp.InitiatorSignatureTokenAssertion;
import weblogic.wsee.security.wssp.InitiatorTokenAssertion;
import weblogic.wsee.security.wssp.RecipientEncryptionTokenAssertion;
import weblogic.wsee.security.wssp.RecipientSignatureTokenAssertion;
import weblogic.wsee.security.wssp.RecipientTokenAssertion;

public class AsymmetricBindingInfoImpl extends SecurityBindingPropertiesAssertionImpl implements AsymmetricBindingInfo {
   RecipientTokenAssertionImpl recTokenImpl;
   RecipientSignatureTokenAssertionImpl rstAsstImpl;
   RecipientEncryptionTokenAssertionImpl retAsstImpl;
   InitiatorTokenAssertionImpl iniTokenImpl;
   InitiatorSignatureTokenAssertionImpl istAsstImpl;
   InitiatorEncryptionTokenAssertionImpl ietAsstImpl;

   AsymmetricBindingInfoImpl(AsymmetricBinding var1) {
      super(var1);
      InitiatorSignatureToken var2 = var1.getInitiatorSignatureToken();
      if (var2 != null) {
         this.istAsstImpl = new InitiatorSignatureTokenAssertionImpl(var2);
      }

      InitiatorEncryptionToken var3 = var1.getInitiatorEncryptionToken();
      if (var3 != null) {
         this.ietAsstImpl = new InitiatorEncryptionTokenAssertionImpl(var3);
      }

      RecipientSignatureToken var4 = var1.getRecipientSignatureToken();
      if (var4 != null) {
         this.rstAsstImpl = new RecipientSignatureTokenAssertionImpl(var4);
      }

      RecipientEncryptionToken var5 = var1.getRecipientEncryptionToken();
      if (var5 != null) {
         this.retAsstImpl = new RecipientEncryptionTokenAssertionImpl(var5);
      }

      InitiatorToken var6 = var1.getInitiatorToken();
      if (var6 != null) {
         this.iniTokenImpl = new InitiatorTokenAssertionImpl(var6);
      }

      RecipientToken var7 = var1.getRecipientToken();
      if (var7 != null) {
         this.recTokenImpl = new RecipientTokenAssertionImpl(var7);
      }

   }

   public InitiatorTokenAssertion getInitiatorTokenAssertion() {
      return this.iniTokenImpl;
   }

   public RecipientTokenAssertion getRecipientTokenAssertion() {
      return this.recTokenImpl;
   }

   public InitiatorSignatureTokenAssertion getInitiatorSignatureTokenAssertion() {
      return this.istAsstImpl;
   }

   public InitiatorEncryptionTokenAssertion getInitiatorEncryptionTokenAssertion() {
      return this.ietAsstImpl;
   }

   public RecipientSignatureTokenAssertion getRecipientSignatureTokenAssertion() {
      return this.rstAsstImpl;
   }

   public RecipientEncryptionTokenAssertion getRecipientEncryptionTokenAssertion() {
      return this.retAsstImpl;
   }
}
