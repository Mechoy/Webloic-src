package weblogic.jdbc.utils;

import weblogic.utils.NestedException;

public class JDBCDriverInfoException extends NestedException {
   public JDBCDriverInfoException() {
   }

   public JDBCDriverInfoException(String var1) {
      super(var1);
   }

   public JDBCDriverInfoException(Throwable var1) {
      super(var1);
   }

   public JDBCDriverInfoException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
