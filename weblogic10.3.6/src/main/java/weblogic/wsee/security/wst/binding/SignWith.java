package weblogic.wsee.security.wst.binding;

public class SignWith extends AlgorithmUri {
   public static final String NAME = "SignWith";

   public SignWith() {
   }

   public SignWith(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public String getName() {
      return "SignWith";
   }
}
