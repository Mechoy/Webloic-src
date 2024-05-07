package weblogic.wsee.security.wssp;

public interface SecurityBindingPropertiesAssertion {
   AlgorithmSuite getAlgorithm();

   boolean isTimestampRequired();

   boolean isEncryptBeforeSigning();

   boolean isSignBeforeEncrypting();

   boolean isSignatureProtectionRequired();

   boolean isTokenProtectionRequired();

   boolean isEntireHeaderAndBodySignatureRequired();

   Layout getLayout();

   boolean isTimestampOptional();

   boolean isEncryptBeforeSigningOptional();

   boolean isSignatureProtectionOptional();

   boolean isTokenProtectionOptional();

   boolean isEntireHeaderAndBodySignatureOptional();

   public static enum Layout {
      LAX,
      STRICT,
      LAX_TIMESTAMP_FIRST,
      LAX_TIMESTAMP_LAST;
   }

   public static enum AlgorithmSuite {
      BASIC256,
      BASIC192,
      BASIC128,
      TRIPLEDES,
      BASIC256_RSA15,
      BASIC192_RSA15,
      BASIC128_RSA15,
      TRIPLEDES_RSA15,
      BASIC256_SHA256,
      BASIC192_SHA256,
      BASIC128_SHA256,
      TRIPLEDES_SHA256,
      BASIC256_SHA256_RSA15,
      BASIC192_SHA256_RSA15,
      BASIC128_SHA256_RSA15,
      TRIPLEDES_SHA256_RSA15;
   }
}
