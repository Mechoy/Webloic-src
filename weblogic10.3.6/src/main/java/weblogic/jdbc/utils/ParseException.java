package weblogic.jdbc.utils;

import weblogic.utils.NestedException;

public class ParseException extends NestedException {
   public ParseException() {
   }

   public ParseException(String var1) {
      super(var1);
   }

   public ParseException(Throwable var1) {
      super(var1);
   }

   public ParseException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
