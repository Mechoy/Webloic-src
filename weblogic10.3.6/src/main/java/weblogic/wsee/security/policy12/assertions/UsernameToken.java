package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class UsernameToken extends Token {
   private static final long serialVersionUID = 7862148842812691308L;
   public static final String USERNAME_TOKEN = "UsernameToken";

   public QName getName() {
      return new QName(this.getNamespace(), "UsernameToken", "sp");
   }

   public NoPassword getNoPassword() {
      return (NoPassword)this.getNestedAssertion(NoPassword.class);
   }

   public HashPassword getHashPassword() {
      return (HashPassword)this.getNestedAssertion(HashPassword.class);
   }

   public Nonce getNonce() {
      return (Nonce)this.getNestedAssertion(Nonce.class);
   }

   public Created getCreated() {
      return (Created)this.getNestedAssertion(Created.class);
   }

   public WssUsernameToken10 getWssUsernameToken10() {
      return (WssUsernameToken10)this.getNestedAssertion(WssUsernameToken10.class);
   }

   public WssUsernameToken11 getWssUsernameToken11() {
      return (WssUsernameToken11)this.getNestedAssertion(WssUsernameToken11.class);
   }
}
