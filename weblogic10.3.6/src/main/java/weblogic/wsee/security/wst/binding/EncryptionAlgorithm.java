package weblogic.wsee.security.wst.binding;

public class EncryptionAlgorithm extends AlgorithmUri {
   public static final String NAME = "EncryptionAlgorithm";

   public EncryptionAlgorithm() {
   }

   public EncryptionAlgorithm(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public String getName() {
      return "EncryptionAlgorithm";
   }
}
