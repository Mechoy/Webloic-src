package weblogic.auddi.util;

import weblogic.auddi.NestedException;

public class ConnectException extends NestedException {
   public ConnectException(Throwable var1, String var2) {
      super(var1, var2);
   }

   public ConnectException(Throwable var1) {
      this(var1, (String)null);
   }
}
