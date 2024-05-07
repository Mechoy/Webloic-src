package weblogic.diagnostics.image;

import weblogic.diagnostics.type.DiagnosticException;

public class ImageSourceNotFoundException extends DiagnosticException {
   public ImageSourceNotFoundException() {
   }

   public ImageSourceNotFoundException(String var1) {
      super(var1);
   }

   public ImageSourceNotFoundException(Throwable var1) {
      super(var1);
   }

   public ImageSourceNotFoundException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
