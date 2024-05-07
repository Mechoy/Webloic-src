package weblogic.iiop.csi;

import weblogic.utils.NestedException;

public class GSSUPDecodeException extends NestedException {
   public GSSUPDecodeException(String var1) {
      super(var1);
   }

   public GSSUPDecodeException() {
      this((String)null);
   }

   public GSSUPDecodeException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
