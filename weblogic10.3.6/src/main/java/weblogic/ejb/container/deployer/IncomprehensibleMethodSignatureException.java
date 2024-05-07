package weblogic.ejb.container.deployer;

import weblogic.utils.NestedException;

public final class IncomprehensibleMethodSignatureException extends NestedException {
   private static final long serialVersionUID = -8474212152431635624L;

   public IncomprehensibleMethodSignatureException() {
   }

   public IncomprehensibleMethodSignatureException(String var1) {
      super(var1);
   }

   public IncomprehensibleMethodSignatureException(Throwable var1) {
      super(var1);
   }

   public IncomprehensibleMethodSignatureException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
