package weblogic.security.internal;

import weblogic.utils.NestedRuntimeException;

class SerializedSystemIniException extends NestedRuntimeException {
   public SerializedSystemIniException() {
   }

   public SerializedSystemIniException(String var1) {
      super(var1);
   }

   public SerializedSystemIniException(Throwable var1) {
      super(var1);
   }

   public SerializedSystemIniException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
