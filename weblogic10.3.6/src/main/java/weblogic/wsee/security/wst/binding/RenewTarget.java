package weblogic.wsee.security.wst.binding;

public class RenewTarget extends TokenReferenceBase {
   public static final String NAME = "RenewTarget";

   public RenewTarget() {
   }

   public RenewTarget(String var1) {
      this.namespaceUri = var1;
   }

   public String getName() {
      return "RenewTarget";
   }
}
