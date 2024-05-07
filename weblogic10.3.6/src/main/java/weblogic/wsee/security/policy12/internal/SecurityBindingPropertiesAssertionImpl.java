package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.SecurityBinding;
import weblogic.wsee.security.wssp.SecurityBindingPropertiesAssertion;

public class SecurityBindingPropertiesAssertionImpl implements SecurityBindingPropertiesAssertion {
   private SecurityBindingPropertiesAssertion.AlgorithmSuite as;
   private SecurityBindingPropertiesAssertion.Layout layout;
   private boolean isTimestampRequired;
   private boolean isEncryptBeforeSigning;
   private boolean isSignatureProtectionRequired;
   private boolean isTokenProtectionRequired;
   private boolean isEntireHeaderAndBodySignatureRequired;
   private boolean isTimestampOptional;
   private boolean isEncryptBeforeSigningOptional;
   private boolean isSignatureProtectionOptional;
   private boolean isTokenProtectionOptional;
   private boolean isEntireHeaderAndBodySignatureOptional;

   SecurityBindingPropertiesAssertionImpl(SecurityBinding var1) {
      this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC256_RSA15;
      this.layout = SecurityBindingPropertiesAssertion.Layout.LAX;
      this.isTimestampRequired = false;
      this.isEncryptBeforeSigning = false;
      this.isSignatureProtectionRequired = false;
      this.isTokenProtectionRequired = false;
      this.isEntireHeaderAndBodySignatureRequired = false;
      this.isTimestampOptional = false;
      this.isEncryptBeforeSigningOptional = false;
      this.isSignatureProtectionOptional = false;
      this.isTokenProtectionOptional = false;
      this.isEntireHeaderAndBodySignatureOptional = false;
      this.initAlgorithmSuite(var1);
      this.initLayout(var1);
      if (var1.getIncludeTimestamp() != null) {
         this.isTimestampRequired = true;
         if (var1.getIncludeTimestamp().isOptional()) {
            this.isTimestampOptional = true;
         }
      }

      if (var1.getEncryptBeforeSigning() != null) {
         this.isEncryptBeforeSigning = true;
         if (var1.getEncryptBeforeSigning().isOptional()) {
            this.isEncryptBeforeSigningOptional = true;
         }
      }

      if (var1.getEncryptSignature() != null) {
         this.isSignatureProtectionRequired = true;
         if (var1.getEncryptSignature().isOptional()) {
            this.isSignatureProtectionOptional = true;
         }
      }

      if (var1.getProtectTokens() != null) {
         this.isTokenProtectionRequired = true;
         if (var1.getProtectTokens().isOptional()) {
            this.isTokenProtectionOptional = true;
         }
      }

      if (var1.getOnlySignEntireHeadersAndBody() != null) {
         this.isEntireHeaderAndBodySignatureRequired = true;
         if (var1.getOnlySignEntireHeadersAndBody().isOptional()) {
            this.isEntireHeaderAndBodySignatureOptional = true;
         }
      }

   }

   private void initAlgorithmSuite(SecurityBinding var1) {
      weblogic.wsee.security.policy12.assertions.AlgorithmSuite var2 = var1.getAlgorithmSuite();
      if (var2 != null) {
         if (var2.getBasic256() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC256;
         } else if (var2.getTripleDes() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.TRIPLEDES;
         } else if (var2.getBasic128() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC128;
         } else if (var2.getBasic128Rsa15() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC128_RSA15;
         } else if (var2.getBasic128Sha256() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC128_SHA256;
         } else if (var2.getBasic128Sha256Rsa15() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC128_SHA256_RSA15;
         } else if (var2.getBasic192() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC192;
         } else if (var2.getBasic192Rsa15() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC192_RSA15;
         } else if (var2.getBasic192Sha256() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC192_SHA256;
         } else if (var2.getBasic192Sha256Rsa15() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC192_SHA256_RSA15;
         } else if (var2.getBasic256Rsa15() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC256_RSA15;
         } else if (var2.getBasic256Sha256() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC256_SHA256;
         } else if (var2.getBasic256Sha256Rsa15() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC256_SHA256_RSA15;
         } else if (var2.getTripleDesRsa15() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.TRIPLEDES_RSA15;
         } else if (var2.getTripleDesSha256() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.TRIPLEDES_SHA256;
         } else if (var2.getTripleDesSha256Rsa15() != null) {
            this.as = SecurityBindingPropertiesAssertion.AlgorithmSuite.TRIPLEDES_SHA256_RSA15;
         }
      }

   }

   private void initLayout(SecurityBinding var1) {
      weblogic.wsee.security.policy12.assertions.Layout var2 = var1.getLayout();
      if (var2 != null) {
         if (var2.getLax() != null) {
            this.layout = SecurityBindingPropertiesAssertion.Layout.LAX;
         } else if (var2.getStrict() != null) {
            this.layout = SecurityBindingPropertiesAssertion.Layout.STRICT;
         } else if (var2.getLaxTsFirst() != null) {
            this.layout = SecurityBindingPropertiesAssertion.Layout.LAX_TIMESTAMP_FIRST;
         } else if (var2.getLaxTsLast() != null) {
            this.layout = SecurityBindingPropertiesAssertion.Layout.LAX_TIMESTAMP_LAST;
         }
      }

   }

   public SecurityBindingPropertiesAssertion.AlgorithmSuite getAlgorithm() {
      return this.as;
   }

   public boolean isTimestampRequired() {
      return this.isTimestampRequired;
   }

   public boolean isEncryptBeforeSigning() {
      return this.isEncryptBeforeSigning;
   }

   public boolean isSignBeforeEncrypting() {
      return !this.isEncryptBeforeSigning;
   }

   public boolean isSignatureProtectionRequired() {
      return this.isSignatureProtectionRequired;
   }

   public boolean isTokenProtectionRequired() {
      return this.isTokenProtectionRequired;
   }

   public boolean isEntireHeaderAndBodySignatureRequired() {
      return this.isEntireHeaderAndBodySignatureRequired;
   }

   public SecurityBindingPropertiesAssertion.Layout getLayout() {
      return this.layout;
   }

   public boolean isTimestampOptional() {
      return this.isTimestampOptional;
   }

   public boolean isEncryptBeforeSigningOptional() {
      return this.isEncryptBeforeSigningOptional;
   }

   public boolean isSignatureProtectionOptional() {
      return this.isSignatureProtectionOptional;
   }

   public boolean isTokenProtectionOptional() {
      return this.isTokenProtectionOptional;
   }

   public boolean isEntireHeaderAndBodySignatureOptional() {
      return this.isEntireHeaderAndBodySignatureOptional;
   }
}
