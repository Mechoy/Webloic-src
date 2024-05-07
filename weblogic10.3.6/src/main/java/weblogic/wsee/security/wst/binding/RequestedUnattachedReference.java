package weblogic.wsee.security.wst.binding;

public class RequestedUnattachedReference extends TokenReferenceBase {
   public static final String NAME = "RequestedUnattachedReference";

   public RequestedUnattachedReference() {
   }

   public RequestedUnattachedReference(String var1) {
      this.namespaceUri = var1;
   }

   public String getName() {
      return "RequestedUnattachedReference";
   }
}
