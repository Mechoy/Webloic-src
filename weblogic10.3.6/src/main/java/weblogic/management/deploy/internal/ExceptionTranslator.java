package weblogic.management.deploy.internal;

public class ExceptionTranslator {
   static RuntimeException translateException(Throwable var0) {
      RuntimeException var1 = null;
      if (var0.getCause() != null) {
         var1 = new RuntimeException(var0.getMessage(), translateException(var0.getCause()));
      } else {
         var1 = new RuntimeException(var0.getMessage());
      }

      var1.setStackTrace(var0.getStackTrace());
      return var1;
   }
}
