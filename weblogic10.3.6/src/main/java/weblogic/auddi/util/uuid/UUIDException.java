package weblogic.auddi.util.uuid;

import weblogic.auddi.NestedException;

public class UUIDException extends NestedException {
   public UUIDException(Throwable var1, String var2) {
      super(var1, var2);
   }

   public UUIDException(Throwable var1) {
      super(var1);
   }

   public UUIDException(String var1) {
      super((Throwable)null, var1);
   }
}
