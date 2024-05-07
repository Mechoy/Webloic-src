package weblogic.application.compiler.flow;

import weblogic.application.compiler.BuildtimeApplicationContext;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryReference;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.application.utils.LibraryUtils;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.utils.compiler.ToolFailureException;

public final class ImportLibrariesFlow extends CompilerFlow {
   private final BuildtimeApplicationContext libCtx;
   private LibraryManager libraryManager = null;

   public ImportLibrariesFlow(CompilerCtx var1) {
      super(var1);
      this.libCtx = (BuildtimeApplicationContext)var1.getApplicationContext();
   }

   public void compile() throws ToolFailureException {
      this.libraryManager = new LibraryManager(LibraryUtils.initAppReferencer(this.ctx.getSourceName()));
      this.processLibraries();
   }

   private void processLibraries() throws ToolFailureException {
      if (this.ctx.getWLApplicationDD() != null) {
         LibraryRefBean[] var1 = this.ctx.getWLApplicationDD().getLibraryRefs();
         if (var1 != null && var1.length != 0) {
            this.initAppLibManager(var1);
            if (!this.libraryManager.hasUnresolvedReferences()) {
               this.importLibraries();
            }
         }
      }
   }

   private void importLibraries() throws ToolFailureException {
      try {
         LibraryUtils.importAppLibraries(this.libraryManager, this.libCtx, this.ctx, this.ctx.isVerbose());
      } catch (LoggableLibraryProcessingException var2) {
         throw new ToolFailureException(var2.getLoggable().getMessage(), var2.getCause());
      }
   }

   private void initAppLibManager(LibraryRefBean[] var1) throws ToolFailureException {
      J2EELibraryReference[] var2 = null;

      try {
         var2 = LibraryLoggingUtils.initLibRefs(var1);
      } catch (LoggableLibraryProcessingException var4) {
         throw new ToolFailureException(var4.getLoggable().getMessage(), var4.getCause());
      }

      this.libraryManager.lookup((LibraryReference[])var2);
      this.libCtx.getLibraryManagerAggregate().setAppLevelLibraryManager(this.libraryManager);
   }

   public void cleanup() {
   }
}
