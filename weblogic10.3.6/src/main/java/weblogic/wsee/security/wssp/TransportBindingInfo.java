package weblogic.wsee.security.wssp;

public interface TransportBindingInfo extends SecurityBindingPropertiesAssertion {
   HttpsTokenAssertion getHttpsTokenAssertion();
}
