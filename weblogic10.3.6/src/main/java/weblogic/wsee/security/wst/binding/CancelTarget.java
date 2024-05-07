package weblogic.wsee.security.wst.binding;

public class CancelTarget extends TokenReferenceBase {
   public static final String NAME = "CancelTarget";

   public CancelTarget() {
   }

   public CancelTarget(String var1) {
      this.namespaceUri = var1;
   }

   public String getName() {
      return "CancelTarget";
   }
}
