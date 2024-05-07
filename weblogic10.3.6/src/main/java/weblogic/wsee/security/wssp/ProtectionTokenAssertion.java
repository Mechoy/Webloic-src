package weblogic.wsee.security.wssp;

public interface ProtectionTokenAssertion {
   SecureConversationTokenAssertion getSecureConversationTokenAssertion();

   X509TokenAssertion getX509TokenAssertion();

   KerberosTokenAssertion getKerberosTokenAssertion();

   SamlTokenAssertion getSamlTokenAssertion();
}
