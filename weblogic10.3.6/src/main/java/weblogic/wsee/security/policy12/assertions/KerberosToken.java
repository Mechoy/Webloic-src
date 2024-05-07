package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class KerberosToken extends Token {
   public static final String KERBEROS_TOKEN = "KerberosToken";

   public QName getName() {
      return new QName(this.getNamespace(), "KerberosToken", "sp");
   }

   public RequireKeyIdentifierReference getRequireKeyIdentifierReference() {
      return (RequireKeyIdentifierReference)this.getNestedAssertion(RequireKeyIdentifierReference.class);
   }

   public WssKerberosV5ApReqToken11 getWssKerberosV5ApReqToken11() {
      return (WssKerberosV5ApReqToken11)this.getNestedAssertion(WssKerberosV5ApReqToken11.class);
   }

   public WssGssKerberosV5ApReqToken11 getWssGssKerberosV5ApReqToken11() {
      return (WssGssKerberosV5ApReqToken11)this.getNestedAssertion(WssGssKerberosV5ApReqToken11.class);
   }
}
