package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RelToken extends Token {
   public static final String REL_TOKEN = "RelToken";

   public QName getName() {
      return new QName(this.getNamespace(), "RelToken", "sp");
   }

   public RequireKeyIdentifierReference getRequireKeyIdentifierReference() {
      return (RequireKeyIdentifierReference)this.getNestedAssertion(RequireKeyIdentifierReference.class);
   }

   public WssRelV10Token10 getWssRelV10Token10() {
      return (WssRelV10Token10)this.getNestedAssertion(WssRelV10Token10.class);
   }

   public WssRelV20Token10 getWssRelV20Token10() {
      return (WssRelV20Token10)this.getNestedAssertion(WssRelV20Token10.class);
   }

   public WssRelV10Token11 getWssRelV10Token11() {
      return (WssRelV10Token11)this.getNestedAssertion(WssRelV10Token11.class);
   }

   public WssRelV20Token11 getWssRelV20Token11() {
      return (WssRelV20Token11)this.getNestedAssertion(WssRelV20Token11.class);
   }
}
