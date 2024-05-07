package weblogic.ejb.container.interfaces;

import weblogic.utils.NestedException;

public final class NoSuchRoleException extends NestedException {
   private static final long serialVersionUID = 7729973007125942759L;

   public NoSuchRoleException() {
   }

   public NoSuchRoleException(String var1) {
      super(var1);
   }

   public NoSuchRoleException(Throwable var1) {
      super(var1);
   }

   public NoSuchRoleException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
