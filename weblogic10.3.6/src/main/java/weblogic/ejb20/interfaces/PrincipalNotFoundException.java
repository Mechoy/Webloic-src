package weblogic.ejb20.interfaces;

import weblogic.utils.NestedException;

public final class PrincipalNotFoundException extends NestedException {
   private static final long serialVersionUID = 8767040552998421219L;

   public PrincipalNotFoundException() {
   }

   public PrincipalNotFoundException(String var1) {
      super(var1);
   }

   public PrincipalNotFoundException(Throwable var1) {
      super(var1);
   }

   public PrincipalNotFoundException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
