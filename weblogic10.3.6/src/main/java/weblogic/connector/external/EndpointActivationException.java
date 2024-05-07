package weblogic.connector.external;

import javax.resource.ResourceException;

public class EndpointActivationException extends ResourceException {
   private boolean changeable;

   public EndpointActivationException(String var1, boolean var2) {
      super(var1);
      this.changeable = var2;
   }

   public EndpointActivationException(String var1, Throwable var2, boolean var3) {
      super(var1, var2);
      this.changeable = var3;
   }

   public boolean isChangeable() {
      return this.changeable;
   }
}
