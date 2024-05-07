package weblogic.application;

import weblogic.utils.ErrorCollectionException;

public class ApplicationException extends ErrorCollectionException {
   public ApplicationException() {
   }

   public ApplicationException(String var1) {
      super(var1);
   }

   public ApplicationException(Throwable var1) {
      super(var1);
   }

   public ApplicationException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
