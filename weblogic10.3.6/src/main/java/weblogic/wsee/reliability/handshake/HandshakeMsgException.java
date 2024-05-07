package weblogic.wsee.reliability.handshake;

import weblogic.wsee.handler.InvocationException;

public final class HandshakeMsgException extends InvocationException {
   private static final long serialVersionUID = -2793739080017692868L;

   public HandshakeMsgException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public HandshakeMsgException(String var1) {
      super(var1);
   }
}
