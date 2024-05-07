package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Wss11 extends Wss10 {
   public static final String WSS_11 = "Wss11";

   public QName getName() {
      return new QName(this.getNamespace(), "Wss11", "sp");
   }

   public MustSupportRefThumbprint getMustSupportRefThumbprint() {
      return (MustSupportRefThumbprint)this.getNestedAssertion(MustSupportRefThumbprint.class);
   }

   public MustSupportRefEncryptedKey getMustSupportRefEncryptedKey() {
      return (MustSupportRefEncryptedKey)this.getNestedAssertion(MustSupportRefEncryptedKey.class);
   }

   public RequireSignatureConfirmation getRequireSignatureConfirmation() {
      return (RequireSignatureConfirmation)this.getNestedAssertion(RequireSignatureConfirmation.class);
   }
}
