package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;
import weblogic.application.library.LibraryManager;
import weblogic.application.utils.LibraryUtils;

public final class OptionalPackageReferencerFlow extends BaseFlow implements Flow {
   public OptionalPackageReferencerFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() {
      this.appCtx.getLibraryManagerAggregate().setOptionalPackagesManager(new LibraryManager(LibraryUtils.initOptPackReferencer(this.appCtx)));
   }
}
