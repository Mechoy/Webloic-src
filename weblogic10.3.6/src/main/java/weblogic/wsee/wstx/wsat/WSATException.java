package weblogic.wsee.wstx.wsat;

import javax.transaction.xa.XAException;

public class WSATException extends Exception {
   private static final long serialVersionUID = 8473938065230309413L;
   public int errorCode;

   public WSATException(String var1) {
      super(var1);
   }

   public WSATException(Exception var1) {
      super(var1);
   }

   public WSATException(String var1, XAException var2) {
      super(var1, var2);
      this.errorCode = var2.errorCode;
   }
}
