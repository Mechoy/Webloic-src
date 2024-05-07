package weblogic.wsee.message;

import weblogic.wsee.handler.InvocationException;

public class MsgHeaderException extends InvocationException {
   public MsgHeaderException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public MsgHeaderException(String var1) {
      super(var1);
   }
}
