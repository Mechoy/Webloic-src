package weblogic.wsee.security.wss.plan;

import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.wss.policy.EncryptionPolicy;
import weblogic.wsee.security.wss.policy.GeneralPolicy;
import weblogic.wsee.security.wss.policy.IdentityPolicy;
import weblogic.wsee.security.wss.policy.SignaturePolicy;
import weblogic.wsee.security.wss.policy.TimestampPolicy;

public interface SecurityPolicyPlan {
   String BODY = "Body";
   String HEADER = "Header";
   String TIME_STAMP = "TimeStamp";
   String USERNAME_TOKEN = "UserNameToken";
   String X509_TOKEN = "X509Token";
   String SAML_TOKEN = "SamlToken";
   String WSSC_TOKEN = "SecureConversationTokenToken";
   String KERBEROS_TOKEN = "KerberosToken";
   String ENDORSE_SIGNATURE = "EndoseSignature";
   String ENCRYPT_SIGNATURE = "EncryptSignature";
   String RECIPIENT_TOKEN = "RecipientToken";
   String ELEMENT = "Element";
   String SIGNATURE = "Signature";
   String DERIVED_KEY_TOKEN = "DerivedKeysToken";
   int BASIC_PLAN = 27;
   int SUPPORTING_TOKENS = 0;
   int SIGNED_SUPPORTING_TOKENS = 1;
   int ENDORSING_SUPPORTING_TOKENS = 2;
   int SIGNED_ENDORSING_SUPPORTING_TOKENS = 3;
   int SIGNED_ENCRYPTED_SUPPORTING_TOKENS = 4;
   int ENCRYPTED_SUPPORTING_TOKENS = 5;
   int ENDORSING_ENCRYPTED_SUPPORTING_TOKENS = 6;
   int SIGNED_ENDORSING_ENCRYPTED_SUPPORTING_TOKENS = 7;
   int ACTION_AUTHENTICATION = 1;
   int ACTION_TIMESTAMP = 2;
   int ACTION_ADD_TOKENS = 4;
   int ACTION_ENCRYPTION = 8;
   int ACTION_SIGNATURE = 16;
   int ACTION_REQUEST_ONLY = 32;
   int ACTION_RESPONSE_ONLY = 64;
   int ACTION_SIGNAURE_CONFIRMATION = 128;
   int ACTION_SIGN_AND_ENCRYPT = 256;
   int ACTION_SIGN_AND_ENCRYPT_REQUEST = 288;
   int ACTION_SIGN_AND_ENCRYPT_RESPONSE = 320;
   int ACTION_DERIVED_KEY = 512;
   int ACTION_ENDORSING_SUPPORTING_TOKENS = 1024;
   int ACTION_SIGNED_ENDORSING_SUPPORTING_TOKENS = 3072;
   int ACTION_ENCRYPT_SIGNATURE = 8192;
   int ACTION_ENCRYPT_BEFORE_SIGN = 4096;
   int ACTION_SIGN_AND_ENCRYPT_AND_ENCRYPT_SIGNATURE = 8448;
   int ACTION_SIGN_AND_ENCRYPT_ENDORSING_SUPPORTING_TOKENS_ENCRYPT_SIGNATURE = 9472;

   boolean isRequest();

   void setRequest(boolean var1);

   boolean isHasSecurity();

   void setHasSecurity(boolean var1);

   boolean isHasMessageSecurity();

   void setHasMessageSecurity(boolean var1);

   EncryptionPolicy getEncryptionPolicy();

   void setEncryptionPolicy(EncryptionPolicy var1);

   SignaturePolicy getSigningPolicy();

   void setSigningPolicy(SignaturePolicy var1);

   SignaturePolicy getEndorsingPolicy();

   void setEndorsingPolicy(SignaturePolicy var1);

   int getBuildingPlan();

   void setBuildingPlan(int var1);

   void addActionToBuildingPlan(int var1);

   IdentityPolicy getIdentityPolicy();

   void setIdentityPolicy(IdentityPolicy var1);

   GeneralPolicy getGeneralPolicy();

   void setGeneralPolicy(GeneralPolicy var1);

   TimestampPolicy getTimestampPolicy();

   SecurityToken getPolicyIdToken();

   void setPolicyIdToken(SecurityToken var1);

   GeneralPolicy getGenrealPolicy();

   void setGenrealPolicy(GeneralPolicy var1);

   boolean isX509AuthConditional();

   boolean isSymmeticPlan();

   void setSymmeticPlan(boolean var1);

   boolean isEncryptedKeyRequired();

   void setEncryptedKeyRequired(boolean var1);

   boolean isBodyEmpty();

   PolicyAlternative getPolicyAlternative();

   void setPolicyAlternative(PolicyAlternative var1);

   boolean hasTransportSecuirity();
}
