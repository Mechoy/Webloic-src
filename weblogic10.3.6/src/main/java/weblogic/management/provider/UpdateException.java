package weblogic.management.provider;

import weblogic.management.ManagementException;

public class UpdateException extends ManagementException {
   public UpdateException(String var1, Throwable var2, boolean var3) {
      super(var1, var2, var3);
   }

   public UpdateException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public UpdateException(Throwable var1) {
      super(var1);
   }

   public UpdateException(String var1) {
      super(var1);
   }
}
