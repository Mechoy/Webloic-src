package weblogic.wsee.security.policy12.internal;

import java.util.ArrayList;
import java.util.List;
import weblogic.wsee.security.policy12.assertions.AbstractSupportingTokens;
import weblogic.wsee.security.policy12.assertions.EncryptedSupportingTokens;
import weblogic.wsee.security.policy12.assertions.EndorsingSupportingTokens;
import weblogic.wsee.security.policy12.assertions.IssuedToken;
import weblogic.wsee.security.policy12.assertions.KerberosToken;
import weblogic.wsee.security.policy12.assertions.SamlToken;
import weblogic.wsee.security.policy12.assertions.SecureConversationToken;
import weblogic.wsee.security.policy12.assertions.SignedEncryptedSupportingTokens;
import weblogic.wsee.security.policy12.assertions.SignedEndorsingSupportingTokens;
import weblogic.wsee.security.policy12.assertions.SignedSupportingTokens;
import weblogic.wsee.security.policy12.assertions.SupportingTokens;
import weblogic.wsee.security.policy12.assertions.UsernameToken;
import weblogic.wsee.security.policy12.assertions.X509Token;
import weblogic.wsee.security.wssp.SamlTokenAssertion;
import weblogic.wsee.security.wssp.SupportingTokensAssertion;
import weblogic.wsee.security.wssp.TokenAssertion;

public class SupportingTokensAssertionImpl implements SupportingTokensAssertion {
   private List<TokenAssertion> supportingTokens = new ArrayList();
   private List<TokenAssertion> encryptedSupportingTokens = new ArrayList();
   private List<TokenAssertion> signedSupportingTokens = new ArrayList();
   private List<TokenAssertion> signedEncryptedSupportingTokens = new ArrayList();
   private List<TokenAssertion> endorsingSupportingTokens = new ArrayList();
   private List<TokenAssertion> signedEndorsingSupportingTokens = new ArrayList();
   private SupportingTokensAssertion.SecurityInfo siOfSupportingTokens = new SupportingTokensAssertion.SecurityInfo();
   private SupportingTokensAssertion.SecurityInfo siOfEncryptedSupportingTokens = new SupportingTokensAssertion.SecurityInfo();
   private SupportingTokensAssertion.SecurityInfo siOfSignedSupportingTokens = new SupportingTokensAssertion.SecurityInfo();
   private SupportingTokensAssertion.SecurityInfo siOfSignedEncryptedSupportingTokens = new SupportingTokensAssertion.SecurityInfo();
   private SupportingTokensAssertion.SecurityInfo siOfEndorsingSupportingTokens = new SupportingTokensAssertion.SecurityInfo();
   private SupportingTokensAssertion.SecurityInfo siOfSignedEndorsingSupportingTokens = new SupportingTokensAssertion.SecurityInfo();
   private boolean verifySamlConfirmation = false;

   SupportingTokensAssertionImpl() {
   }

   void initSupportingTokens(SupportingTokens var1) {
      addTokensNoSaml(var1, this.supportingTokens);
      addSamlTokens(var1, this.supportingTokens, SamlTokenAssertion.ConfirmationMethod.BEARER);
      this.siOfSupportingTokens.init(var1);
   }

   void initSignedSupportingTokens(SignedSupportingTokens var1) {
      addTokensNoSaml(var1, this.signedSupportingTokens);
      if (addSamlTokens(var1, this.signedSupportingTokens, SamlTokenAssertion.ConfirmationMethod.SENDER_VOUCHES)) {
         this.verifySamlConfirmation = true;
      }

      this.siOfSignedSupportingTokens.init(var1);
   }

   void initSignedEncryptedSupportingTokens(SignedEncryptedSupportingTokens var1) {
      addTokensNoSaml(var1, this.signedEncryptedSupportingTokens);
      if (addSamlTokens(var1, this.signedEncryptedSupportingTokens, SamlTokenAssertion.ConfirmationMethod.SENDER_VOUCHES)) {
         this.verifySamlConfirmation = true;
      }

      this.siOfSignedEncryptedSupportingTokens.init(var1);
   }

   void initEncryptedSupportingTokens(EncryptedSupportingTokens var1) {
      addTokensNoSaml(var1, this.encryptedSupportingTokens);
      addSamlTokens(var1, this.encryptedSupportingTokens, SamlTokenAssertion.ConfirmationMethod.BEARER);
      this.siOfEncryptedSupportingTokens.init(var1);
   }

   void initEndorsingSupportingTokens(EndorsingSupportingTokens var1) {
      addTokensNoSaml(var1, this.endorsingSupportingTokens);
      addSamlTokens(var1, this.endorsingSupportingTokens, SamlTokenAssertion.ConfirmationMethod.HOLDER_OF_KEY);
      this.siOfEndorsingSupportingTokens.init(var1);
   }

   void initSignedEndorsingSupportingTokens(SignedEndorsingSupportingTokens var1) {
      addTokensNoSaml(var1, this.signedEndorsingSupportingTokens);
      addSamlTokens(var1, this.signedEndorsingSupportingTokens, SamlTokenAssertion.ConfirmationMethod.HOLDER_OF_KEY);
      this.siOfSignedEndorsingSupportingTokens.init(var1);
   }

   private static void addTokensNoSaml(AbstractSupportingTokens var0, List<TokenAssertion> var1) {
      UsernameToken var2 = var0.getUsernameToken();
      if (var2 != null) {
         UsernameTokenAssertionImpl var3 = new UsernameTokenAssertionImpl(var2);
         if (var0.isOptional()) {
            var3.setOptional(var0.getOptional());
         }

         var1.add(var3);
      }

      X509Token var8 = var0.getX509Token();
      if (var8 != null) {
         X509TokenAssertionImpl var4 = new X509TokenAssertionImpl(var8);
         if (var0.isOptional()) {
            var4.setOptional(var0.getOptional());
         }

         var1.add(var4);
      }

      SecureConversationToken var9 = var0.getSecureConversationToken();
      if (var9 != null) {
         SecureConversationTokenAssertionImpl var5 = new SecureConversationTokenAssertionImpl(var9);
         if (var0.isOptional()) {
            var5.setOptional(var9.isOptional());
         }

         var1.add(var5);
      }

      KerberosToken var10 = var0.getKerberosToken();
      if (var10 != null) {
         KerberosTokenAssertionImpl var6 = new KerberosTokenAssertionImpl(var10);
         if (var10.isOptional()) {
            var6.setOptional(true);
         }

         var1.add(var6);
      }

      IssuedToken var11 = var0.getIssuedToken();
      if (var11 != null) {
         IssuedTokenAssertionImpl var7 = new IssuedTokenAssertionImpl(var11);
         if (var11.isOptional()) {
            var7.setOptional(true);
         }

         var1.add(var7);
      }

   }

   private static boolean addSamlTokens(AbstractSupportingTokens var0, List<TokenAssertion> var1, SamlTokenAssertion.ConfirmationMethod var2) {
      SamlToken var3 = var0.getSamlToken();
      if (var3 == null) {
         return false;
      } else {
         SamlTokenAssertionImpl var4 = new SamlTokenAssertionImpl(var3);
         if (var0.isOptional()) {
            var4.setOptional(var0.getOptional());
         }

         var4.setSubjectConfirmationMethod(var2);
         var1.add(var4);
         return true;
      }
   }

   public boolean hasSupportingTokens() {
      return this.supportingTokens.size() > 0;
   }

   public List<TokenAssertion> getSupportingTokens() {
      return this.supportingTokens;
   }

   public SupportingTokensAssertion.SecurityInfo getSecurityInfoOfSupportingTokens() {
      return this.siOfSupportingTokens;
   }

   public boolean hasSignedSupportingTokens() {
      return this.signedSupportingTokens.size() > 0;
   }

   public List<TokenAssertion> getSignedSupportingTokens() {
      return this.signedSupportingTokens;
   }

   public SupportingTokensAssertion.SecurityInfo getSecurityInfoOfSignedSupportingTokens() {
      return this.siOfSignedSupportingTokens;
   }

   public boolean hasSignedEncryptedSupportingTokens() {
      return this.signedEncryptedSupportingTokens.size() > 0;
   }

   public List<TokenAssertion> getSignedEncryptedSupportingTokens() {
      return this.signedEncryptedSupportingTokens;
   }

   public SupportingTokensAssertion.SecurityInfo getSecurityInfoOfSignedEncryptedSupportingTokens() {
      return this.siOfSignedEncryptedSupportingTokens;
   }

   public boolean hasEncryptedSupportingTokens() {
      return this.encryptedSupportingTokens.size() > 0;
   }

   public List<TokenAssertion> getEncryptedSupportingTokens() {
      return this.encryptedSupportingTokens;
   }

   public SupportingTokensAssertion.SecurityInfo getSecurityInfoOfEncryptedSupportingTokens() {
      return this.siOfEncryptedSupportingTokens;
   }

   public boolean hasEndorsingSupportingTokens() {
      return this.endorsingSupportingTokens.size() > 0;
   }

   public List<TokenAssertion> getEndorsingSupportingTokens() {
      return this.endorsingSupportingTokens;
   }

   public SupportingTokensAssertion.SecurityInfo getSecurityInfoOfEndorsingSupportingTokens() {
      return this.siOfEndorsingSupportingTokens;
   }

   public boolean hasSignedEndorsingSupportingTokens() {
      return this.signedEndorsingSupportingTokens.size() > 0;
   }

   public List<TokenAssertion> getSignedEndorsingSupportingTokens() {
      return this.signedEndorsingSupportingTokens;
   }

   public SupportingTokensAssertion.SecurityInfo getSecurityInfoOfSignedEndorsingSupportingTokens() {
      return this.siOfSignedEndorsingSupportingTokens;
   }

   /** @deprecated */
   public boolean isEncryptedBodyRequired() {
      SupportingTokensAssertion.SecurityInfo[] var1 = new SupportingTokensAssertion.SecurityInfo[]{this.siOfSupportingTokens, this.siOfEncryptedSupportingTokens, this.siOfSignedSupportingTokens, this.siOfSignedEncryptedSupportingTokens, this.siOfEndorsingSupportingTokens, this.siOfSignedEndorsingSupportingTokens};

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].isEncryptedBodyRequired()) {
            return true;
         }
      }

      return false;
   }

   /** @deprecated */
   public boolean isEncryptedBodyOptional() {
      SupportingTokensAssertion.SecurityInfo[] var1 = new SupportingTokensAssertion.SecurityInfo[]{this.siOfSupportingTokens, this.siOfEncryptedSupportingTokens, this.siOfSignedSupportingTokens, this.siOfSignedEncryptedSupportingTokens, this.siOfEndorsingSupportingTokens, this.siOfSignedEndorsingSupportingTokens};

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].isEncryptedBodyOptional()) {
            return true;
         }
      }

      return false;
   }

   public boolean isVerifySamlConfirmation() {
      return this.verifySamlConfirmation;
   }
}
