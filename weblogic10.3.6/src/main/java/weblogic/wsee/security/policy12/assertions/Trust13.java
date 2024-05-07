package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Trust13 extends Trust10 {
   private static final long serialVersionUID = 8959594568191649869L;
   public static final String TRUST_13 = "Trust13";

   public QName getName() {
      return new QName(this.getNamespace(), "Trust13", "sp");
   }

   public ScopePolicy15 getScopePolicy15() {
      return (ScopePolicy15)this.getNestedAssertion(ScopePolicy15.class);
   }

   public MustSupportInteractiveChallenge getMustSupportInteractiveChallenge() {
      return (MustSupportInteractiveChallenge)this.getNestedAssertion(MustSupportInteractiveChallenge.class);
   }
}
