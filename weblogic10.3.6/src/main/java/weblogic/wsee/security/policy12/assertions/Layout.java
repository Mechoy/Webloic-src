package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Layout extends NestedSecurityPolicy12Assertion {
   public static final String LAYOUT = "Layout";

   public QName getName() {
      return new QName(this.getNamespace(), "Layout", "sp");
   }

   public Lax getLax() {
      return (Lax)this.getNestedAssertion(Lax.class);
   }

   public Strict getStrict() {
      return (Strict)this.getNestedAssertion(Strict.class);
   }

   public LaxTsFirst getLaxTsFirst() {
      return (LaxTsFirst)this.getNestedAssertion(LaxTsFirst.class);
   }

   public LaxTsLast getLaxTsLast() {
      return (LaxTsLast)this.getNestedAssertion(LaxTsLast.class);
   }
}
