package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.Trust10;
import weblogic.wsee.security.policy12.assertions.Trust13;
import weblogic.wsee.security.wssp.WsTrustOptions;

public class WsTrustOptionsImpl implements WsTrustOptions {
   private boolean isWst10;
   private boolean isWst13;
   private boolean isMustSupportClientChallenge;
   private boolean isMustSupportServerChallenge;
   private boolean isClientEntropyRequired;
   private boolean isServerEntropyRequired;
   private boolean isMustSupportIssuedTokens;
   private boolean isScopePolicy15;
   private boolean isMustSupportInteractiveChallenge;

   WsTrustOptionsImpl(Trust10 var1) {
      this.isWst10 = false;
      this.isWst13 = false;
      this.isMustSupportClientChallenge = false;
      this.isMustSupportServerChallenge = false;
      this.isClientEntropyRequired = false;
      this.isServerEntropyRequired = false;
      this.isMustSupportIssuedTokens = false;
      this.isScopePolicy15 = false;
      this.isMustSupportInteractiveChallenge = false;
      this.isWst10 = true;
      this.isMustSupportClientChallenge = var1.getMustSupportClientChallenge() != null;
      this.isMustSupportServerChallenge = var1.getMustSupportServerChallenge() != null;
      this.isClientEntropyRequired = var1.getRequireClientEntropy() != null;
      this.isServerEntropyRequired = var1.getRequireServerEntropy() != null;
      this.isMustSupportIssuedTokens = var1.getMustSupportIssuedTokens() != null;
   }

   WsTrustOptionsImpl(Trust13 var1) {
      this((Trust10)var1);
      this.isWst13 = true;
      this.isWst10 = false;
      this.isScopePolicy15 = var1.getScopePolicy15() != null;
      this.isMustSupportInteractiveChallenge = var1.getMustSupportInteractiveChallenge() != null;
   }

   public boolean isWst10() {
      return this.isWst10;
   }

   public boolean isWst13() {
      return this.isWst13;
   }

   public boolean isMustSupportClientChallenge() {
      return this.isMustSupportClientChallenge;
   }

   public boolean isMustSupportServerChallenge() {
      return this.isMustSupportServerChallenge;
   }

   public boolean isClientEntropyRequired() {
      return this.isClientEntropyRequired;
   }

   public boolean isServerEntropyRequired() {
      return this.isServerEntropyRequired;
   }

   public boolean isMustSupportIssuedTokens() {
      return this.isMustSupportIssuedTokens;
   }

   public boolean isScopePolicy15() {
      return this.isScopePolicy15;
   }

   public boolean isMustSupportInteractiveChallenge() {
      return this.isMustSupportInteractiveChallenge;
   }
}
