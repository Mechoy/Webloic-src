package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;
import weblogic.application.library.LibraryManager;

public final class ImportOptionalPackagesFlow extends BaseFlow implements Flow {
   public ImportOptionalPackagesFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() {
      LibraryManager var1 = this.appCtx.getLibraryManagerAggregate().getOptionalPackagesManager();
      var1.addReferences();
      this.appCtx.getRuntime().setOptionalPackageRuntimes(var1.getReferencedLibraryRuntimes());
   }

   public void unprepare() {
      this.appCtx.getLibraryManagerAggregate().getOptionalPackagesManager().removeReferences();
   }
}
