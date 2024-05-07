package weblogic.wsee.mc.exception;

import weblogic.wsee.handler.InvocationException;

public class McFaultException extends InvocationException {
   public McFaultException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public McFaultException(String var1) {
      super(var1);
   }
}
