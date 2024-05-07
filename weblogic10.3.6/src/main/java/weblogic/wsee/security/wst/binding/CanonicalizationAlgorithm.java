package weblogic.wsee.security.wst.binding;

public class CanonicalizationAlgorithm extends AlgorithmUri {
   public static final String NAME = "CanonicalizationAlgorithm";

   public CanonicalizationAlgorithm() {
   }

   public CanonicalizationAlgorithm(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public String getName() {
      return "CanonicalizationAlgorithm";
   }
}
