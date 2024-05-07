package weblogic.security.internal.encryption;

import weblogic.utils.NestedRuntimeException;

public class EncryptionServiceException extends NestedRuntimeException {
   public EncryptionServiceException() {
   }

   public EncryptionServiceException(String var1) {
      super(var1);
   }

   public EncryptionServiceException(Throwable var1) {
      super(var1);
   }

   public EncryptionServiceException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
