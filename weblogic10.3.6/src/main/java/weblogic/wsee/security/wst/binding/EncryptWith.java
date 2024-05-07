package weblogic.wsee.security.wst.binding;

public class EncryptWith extends AlgorithmUri {
   public static final String NAME = "EncryptWith";

   public EncryptWith() {
   }

   public EncryptWith(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public String getName() {
      return "EncryptWith";
   }
}
