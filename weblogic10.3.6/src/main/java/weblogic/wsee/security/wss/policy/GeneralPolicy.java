package weblogic.wsee.security.wss.policy;

import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.wssp.HttpsTokenAssertion;
import weblogic.wsee.security.wssp.WsTrustOptions;
import weblogic.wsee.security.wssp.Wss10Options;
import weblogic.wsee.security.wssp.Wss11Options;

public interface GeneralPolicy {
   int REF_THUMBPRINT = 1;
   int REF_KEY_IDENTIFIER = 2;
   int REF_ISSUER_SERIAL = 4;
   int REF_EMBEDDED_TOKEN = 8;
   int REF_EXTERNAL_URI = 16;
   int REF_DIRECT = 32;
   int REF_KERBEROS_V5 = 64;
   int REF_KERBEROS_GSS_V5 = 128;

   String getLayout();

   void setLayout(String var1);

   void setLayoutToStrict();

   void setLayoutToLaxTimestampFirst();

   void setLayoutToLaxTimestampLast();

   void setLayoutToLax();

   boolean isWss11();

   void setWss11On();

   void setWss11OptionsAssertion(Wss11Options var1);

   void setWss10OptionsAssertion(Wss10Options var1);

   boolean isRequireSignatureConfirmation();

   String[] getSignatureValues();

   void setSignatureValues(String[] var1);

   int getMustSupportedTypeCode();

   void setRequireSignatureConfirmation(boolean var1);

   boolean isEncryptBeforeSigning();

   void setEncryptBeforeSigning(boolean var1);

   WsTrustOptions getTrustOptions();

   void setTrustOptions(WsTrustOptions var1);

   boolean hasTrustOptions();

   boolean isWssc13();

   boolean isCompatMSFT();

   void setCompatMSFT(boolean var1);

   PolicySelectionPreference getPreference();

   void setPreference(PolicySelectionPreference var1);

   boolean isOptionalSignatureConfirmation();

   void setOptionalSignatureConfirmation(boolean var1);

   void setHttpsAssertion(HttpsTokenAssertion var1);

   boolean isHTTPsRequired();

   boolean isClientCertificateRequired();

   boolean isHttpBasicAuthenticationRequired();
}
