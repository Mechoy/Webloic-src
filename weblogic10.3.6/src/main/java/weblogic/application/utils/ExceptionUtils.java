package weblogic.application.utils;

import weblogic.application.ModuleException;
import weblogic.application.WrappedDeploymentException;
import weblogic.management.DeploymentException;
import weblogic.utils.ErrorCollectionException;

public class ExceptionUtils {
   public static void throwDeploymentException(Throwable var0) throws DeploymentException {
      if (var0 instanceof DeploymentException) {
         throw (DeploymentException)var0;
      } else if (var0 instanceof ErrorCollectionException && ((ErrorCollectionException)var0).size() == 1) {
         ErrorCollectionException var1 = (ErrorCollectionException)var0;
         throwDeploymentException((Throwable)var1.getErrors().next());
      } else {
         throw new WrappedDeploymentException(var0);
      }
   }

   public static void throwModuleException(Throwable var0) throws ModuleException {
      if (var0 instanceof ModuleException) {
         throw (ModuleException)var0;
      } else if (var0 instanceof ErrorCollectionException && ((ErrorCollectionException)var0).size() == 1) {
         ErrorCollectionException var1 = (ErrorCollectionException)var0;
         throwModuleException((Throwable)var1.getErrors().next());
      } else {
         throw new WrappedDeploymentException(var0);
      }
   }
}
