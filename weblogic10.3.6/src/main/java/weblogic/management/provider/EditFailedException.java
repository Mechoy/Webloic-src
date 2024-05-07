package weblogic.management.provider;

import weblogic.management.ManagementException;

public class EditFailedException extends ManagementException {
   public EditFailedException(String var1, Throwable var2, boolean var3) {
      super(var1, var2, var3);
   }

   public EditFailedException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public EditFailedException(Throwable var1) {
      super(var1);
   }

   public EditFailedException(String var1) {
      super(var1);
   }
}
