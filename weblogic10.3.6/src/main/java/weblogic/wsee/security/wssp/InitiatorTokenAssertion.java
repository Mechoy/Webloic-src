package weblogic.wsee.security.wssp;

public interface InitiatorTokenAssertion {
   X509TokenAssertion getX509TokenAssertion();

   SamlTokenAssertion getSamlTokenAssertion();
}
