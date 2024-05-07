package weblogic.t3.srvr;

import weblogic.utils.NestedException;

final class FatalStartupException extends NestedException {
   private static final long serialVersionUID = -87776756165832496L;

   public FatalStartupException() {
   }

   public FatalStartupException(String var1) {
      super(var1);
   }

   public FatalStartupException(Throwable var1) {
      super(var1);
   }

   public FatalStartupException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
