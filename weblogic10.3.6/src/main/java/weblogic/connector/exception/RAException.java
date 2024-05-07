package weblogic.connector.exception;

import weblogic.utils.ErrorCollectionException;

public class RAException extends ErrorCollectionException {
   public RAException() {
   }

   public RAException(String var1) {
      super(var1);
   }

   public RAException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public RAException(Throwable var1) {
      super(var1);
   }
}
