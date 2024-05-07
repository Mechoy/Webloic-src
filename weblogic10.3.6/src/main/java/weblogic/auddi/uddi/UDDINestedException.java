package weblogic.auddi.uddi;

import weblogic.auddi.NestedException;

public class UDDINestedException extends NestedException {
   public UDDINestedException(Exception var1, String var2) {
      super(var1, var2);
   }

   public UDDINestedException(Exception var1) {
      super((Throwable)var1);
   }
}
