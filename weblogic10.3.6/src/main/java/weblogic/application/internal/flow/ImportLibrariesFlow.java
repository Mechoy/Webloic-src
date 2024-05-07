package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.internal.Flow;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.application.utils.LibraryUtils;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.DeploymentException;

public final class ImportLibrariesFlow extends BaseFlow implements Flow {
   private static final ApplicationFactoryManager afm = ApplicationFactoryManager.getApplicationFactoryManager();
   private LibraryManager mgr = null;

   public ImportLibrariesFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      WeblogicApplicationBean var1 = this.appCtx.getWLApplicationDD();
      if (var1 != null) {
         LibraryRefBean[] var2 = var1.getLibraryRefs();
         if (var2 != null && var2.length != 0) {
            J2EELibraryReference[] var3;
            try {
               var3 = LibraryLoggingUtils.initLibRefs(var2);
            } catch (LoggableLibraryProcessingException var6) {
               throw new DeploymentException(var6.getLoggable().getMessage());
            }

            this.mgr = new LibraryManager(LibraryUtils.initAppReferencer((ApplicationContextInternal)this.appCtx), var3);
            this.appCtx.getLibraryManagerAggregate().setAppLevelLibraryManager(this.mgr);
            if (!this.mgr.hasUnresolvedReferences()) {
               try {
                  LibraryUtils.importAppLibraries(this.mgr, this.appCtx, this.appCtx, true);
               } catch (LoggableLibraryProcessingException var5) {
                  throw new DeploymentException(var5.getLoggable().getMessage());
               }

               this.mgr.addReferences();
               this.appCtx.getRuntime().setLibraryRuntimes(this.mgr.getReferencedLibraryRuntimes());
            }
         }
      }
   }

   public void unprepare() {
      if (this.mgr != null) {
         this.mgr.removeReferences();
      }

   }
}
