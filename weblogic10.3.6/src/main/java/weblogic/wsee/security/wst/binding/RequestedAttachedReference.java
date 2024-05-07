package weblogic.wsee.security.wst.binding;

public class RequestedAttachedReference extends TokenReferenceBase {
   public static final String NAME = "RequestedAttachedReference";

   public RequestedAttachedReference() {
   }

   public RequestedAttachedReference(String var1) {
      this.namespaceUri = var1;
   }

   public String getName() {
      return "RequestedAttachedReference";
   }
}
