package weblogic.entitlement.engine;

public class UnregisteredPredicateException extends RuntimeException {
   private String predicateName;

   public UnregisteredPredicateException(String var1) {
      super("Unregistered predicate: " + var1);
      this.predicateName = var1;
   }

   public String getPredicateName() {
      return this.predicateName;
   }
}
