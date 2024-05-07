package weblogic.management.utils;

import weblogic.utils.NestedException;

public final class InvalidPredicateException extends NestedException {
   public InvalidPredicateException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public InvalidPredicateException(Throwable var1) {
      super(var1);
   }

   public InvalidPredicateException(String var1) {
      super(var1);
   }
}
