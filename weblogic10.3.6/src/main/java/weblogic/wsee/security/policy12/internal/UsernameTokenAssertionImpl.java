package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.UsernameToken;
import weblogic.wsee.security.wssp.UsernameTokenAssertion;

public class UsernameTokenAssertionImpl extends TokenAssertionImpl implements UsernameTokenAssertion {
   private boolean noPasswordRequried = false;
   private boolean isHashPasswordRequired = false;
   private boolean isCreatedRequired = false;
   private boolean isNonceRequired = false;
   private UsernameTokenAssertion.TokenType tokenType;

   UsernameTokenAssertionImpl(UsernameToken var1) {
      super(var1);
      this.tokenType = UsernameTokenAssertion.TokenType.WSS_UT_10;
      this.noPasswordRequried = var1.getNoPassword() != null;
      this.isHashPasswordRequired = var1.getHashPassword() != null;
      if (this.isHashPasswordRequired) {
         this.isNonceRequired = true;
         this.isCreatedRequired = true;
      } else {
         this.isNonceRequired = var1.getNonce() != null;
         this.isCreatedRequired = var1.getCreated() != null;
      }

      if (var1.getWssUsernameToken11() != null) {
         this.tokenType = UsernameTokenAssertion.TokenType.WSS_UT_11;
      }

   }

   public boolean noPasswordRequried() {
      return this.noPasswordRequried;
   }

   public boolean isHashPasswordRequired() {
      return this.isHashPasswordRequired;
   }

   public boolean isCreatedRequired() {
      return this.isCreatedRequired;
   }

   public boolean isNonceRequired() {
      return this.isNonceRequired;
   }

   public UsernameTokenAssertion.TokenType getUsernameTokenType() {
      return this.tokenType;
   }
}
