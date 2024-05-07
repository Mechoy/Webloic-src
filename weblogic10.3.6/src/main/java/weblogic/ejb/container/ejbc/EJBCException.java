package weblogic.ejb.container.ejbc;

import weblogic.utils.NestedException;

public class EJBCException extends NestedException {
   public EJBCException() {
   }

   public EJBCException(String var1) {
      super(var1);
   }

   public EJBCException(Throwable var1) {
      super(var1);
   }

   public EJBCException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
