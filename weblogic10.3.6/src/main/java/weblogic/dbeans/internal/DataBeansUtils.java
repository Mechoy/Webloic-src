package weblogic.dbeans.internal;

import weblogic.dbeans.DataBeansException;
import weblogic.ejb.container.InternalException;
import weblogic.utils.StackTraceUtils;

public class DataBeansUtils {
   public static void throwDataBeansException(InternalException var0) throws DataBeansException {
      throwDataBeansException("DataBeans Exception: ", var0);
   }

   public static void throwDataBeansException(String var0, Throwable var1) throws DataBeansException {
      if (var1 instanceof InternalException) {
         InternalException var2 = (InternalException)var1;
         if (var2.detail == null) {
            throw new DataBeansException(var2.getMessage() + ": " + StackTraceUtils.throwable2StackTrace(var2));
         }

         var1 = var2.detail;
         var0 = var2.getMessage();
      }

      if (var1 instanceof DataBeansException) {
         throw (DataBeansException)var1;
      } else {
         throw new DataBeansException((Exception)var1);
      }
   }
}
