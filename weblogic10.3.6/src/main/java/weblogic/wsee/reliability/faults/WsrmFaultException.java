package weblogic.wsee.reliability.faults;

import weblogic.wsee.handler.InvocationException;

public class WsrmFaultException extends InvocationException {
   private static final long serialVersionUID = 1L;
   private transient WsrmFaultMsg _msg = null;

   public WsrmFaultException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public WsrmFaultException(String var1) {
      super(var1);
   }

   public WsrmFaultException(WsrmFaultMsg var1) {
      super(var1.toString());
      this._msg = var1;
   }

   public WsrmFaultMsg getWsrmFaultMsg() {
      return this._msg;
   }

   public String toString() {
      return this._msg != null ? this.getClass().getSimpleName() + ": " + this._msg.toString() : super.toString();
   }
}
