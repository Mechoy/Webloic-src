package weblogic.application.internal;

import java.io.File;
import java.util.jar.Attributes;
import weblogic.application.ApplicationAccess;
import weblogic.application.library.LibraryReference;
import weblogic.application.library.LibraryReferenceFactory;
import weblogic.utils.OptionalPackageProvider;

public class OptionalPackageProviderImpl extends OptionalPackageProvider {
   private final ApplicationAccess appAccess = ApplicationAccess.getApplicationAccess();

   protected FlowContext getflowContext() {
      return (FlowContext)this.appAccess.getCurrentApplicationContext();
   }

   protected String getModuleName() {
      return this.appAccess.getCurrentModuleName();
   }

   public File[] getOptionalPackages(String var1, Attributes var2) {
      FlowContext var3 = this.getflowContext();
      if (var3 == null) {
         return null;
      } else {
         String var4 = this.getModuleName();
         if (var4 != null) {
            var1 = var4 + " at " + var1;
         }

         LibraryReference[] var5 = LibraryReferenceFactory.getOptPackReference(var1, var2);
         return var5 == null ? null : var3.getLibraryManagerAggregate().getOptionalPackagesManager().getOptionalPackages(var5);
      }
   }
}
