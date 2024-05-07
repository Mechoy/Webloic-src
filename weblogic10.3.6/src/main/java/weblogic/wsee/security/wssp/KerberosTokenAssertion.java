package weblogic.wsee.security.wssp;

public interface KerberosTokenAssertion extends TokenAssertion {
   boolean isKeyIdentifierReferenceRequired();

   boolean isWssKerberosV5ApReqToken11();
}
