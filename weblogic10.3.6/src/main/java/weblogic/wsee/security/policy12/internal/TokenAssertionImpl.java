package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.Token;
import weblogic.wsee.security.wssp.TokenAssertion;

public class TokenAssertionImpl implements TokenAssertion {
   private boolean requireDerivedKey = false;
   private boolean derivedKeyOptional = false;
   private boolean requireExplicitDerivedKey = false;
   private boolean requireImplicitDerivedKey = false;
   private TokenAssertion.TokenInclusion tokenInclusion;
   private String issuer;
   private String issuerName;
   private boolean optional;
   private boolean requireExternalReference;
   private boolean requireInternalReference;

   TokenAssertionImpl(Token var1) {
      this.tokenInclusion = TokenAssertion.TokenInclusion.ALWAYS;
      this.issuer = null;
      this.issuerName = null;
      this.optional = false;
      this.requireExternalReference = false;
      this.requireInternalReference = false;
      if (var1.getRequireDerivedKeys() != null) {
         this.requireDerivedKey = true;
         this.derivedKeyOptional = var1.getRequireDerivedKeys().isOptional();
      }

      this.requireExternalReference = var1.getRequireExternalReference() != null;
      this.requireInternalReference = var1.getRequireInternalReference() != null;
      this.requireImplicitDerivedKey = var1.getRequireImplicitDerivedKeys() != null;
      this.requireExplicitDerivedKey = var1.getRequireExplicitDerivedKeys() != null;
      this.issuer = var1.getIssuerAddress();
      this.issuerName = var1.getIssuerName();
      this.optional = var1.isOptional();
      if (var1.getTokenInclusion() != null) {
         String var2 = var1.getTokenInclusion().getInclusion();
         if (!var2.endsWith("/IncludeToken/AlwaysToRecipient") && !var2.endsWith("/AlwaysToRecipient")) {
            if (var2.endsWith("/IncludeToken/Never")) {
               this.tokenInclusion = TokenAssertion.TokenInclusion.NEVER;
            } else if (var2.endsWith("/IncludeToken/Always")) {
               this.tokenInclusion = TokenAssertion.TokenInclusion.ALWAYS;
            } else if (var2.endsWith("/IncludeToken/AlwaysToInitiator")) {
               this.tokenInclusion = TokenAssertion.TokenInclusion.TO_INITIATOR_ONLY;
            } else if (var2.endsWith("/IncludeToken/Once")) {
               this.tokenInclusion = TokenAssertion.TokenInclusion.ONCE;
            }
         } else {
            this.tokenInclusion = TokenAssertion.TokenInclusion.TO_RECIPIENT_ONLY;
         }

      }
   }

   public TokenAssertion.TokenInclusion getTokenInclusion() {
      return this.tokenInclusion;
   }

   public boolean requireDerivedKey() {
      return this.requireDerivedKey;
   }

   public boolean requireExplicitDerivedKey() {
      return this.requireExplicitDerivedKey;
   }

   public boolean requireImplicitDerivedKey() {
      return this.requireImplicitDerivedKey;
   }

   public String getIssuer() {
      return this.issuer;
   }

   public String getIssuerName() {
      return this.issuerName;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public void setOptional(boolean var1) {
      this.optional = var1;
   }

   public boolean isDerivedKeyOptional() {
      return this.derivedKeyOptional;
   }

   public void setDerivedKeyOptional(boolean var1) {
      this.derivedKeyOptional = var1;
   }

   public boolean isRequireInternalReference() {
      return this.requireInternalReference;
   }

   public boolean isRequireExternalReference() {
      return this.requireExternalReference;
   }
}
