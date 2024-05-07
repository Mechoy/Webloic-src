package weblogic.wsee.ws.init;

import weblogic.wsee.util.ToStringWriter;

public class WsDeploymentException extends Exception {
   private static final long serialVersionUID = -7032383091303057526L;

   public WsDeploymentException(String var1) {
      super(var1);
   }

   public WsDeploymentException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public WsDeploymentException(Throwable var1) {
      super(var1);
   }

   public String toString() {
      return super.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
