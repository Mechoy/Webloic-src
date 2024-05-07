package weblogic.wsee.security.wssp;

public interface HttpsTokenAssertion {
   boolean isHttpBasicAuthenticationRequired();

   boolean isClientCertificateRequired();
}
