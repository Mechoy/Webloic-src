package weblogic.wsee.security.wst.binding;

public class ComputedKeyAlgorithm extends AlgorithmUri {
   public static final String NAME = "ComputedKeyAlgorithm";
   private String uri;

   public ComputedKeyAlgorithm() {
   }

   public ComputedKeyAlgorithm(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public String getName() {
      return "ComputedKeyAlgorithm";
   }
}
