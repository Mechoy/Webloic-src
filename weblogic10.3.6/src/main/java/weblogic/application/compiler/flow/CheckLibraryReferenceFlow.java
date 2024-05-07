package weblogic.application.compiler.flow;

import weblogic.application.compiler.BuildtimeApplicationContext;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.utils.compiler.ToolFailureException;

public final class CheckLibraryReferenceFlow extends CompilerFlow {
   private final boolean isError;

   public CheckLibraryReferenceFlow(CompilerCtx var1) {
      this(var1, true);
   }

   public CheckLibraryReferenceFlow(CompilerCtx var1, boolean var2) {
      super(var1);
      this.isError = var2;
   }

   public void compile() throws ToolFailureException {
      BuildtimeApplicationContext var1 = (BuildtimeApplicationContext)this.ctx.getApplicationContext();

      try {
         LibraryLoggingUtils.verifyLibraryReferences(var1.getLibraryManagerAggregate(), this.isError);
      } catch (LoggableLibraryProcessingException var3) {
         if (this.isError) {
            throw new ToolFailureException(var3.getLoggable().getMessage(), var3);
         }

         var3.getLoggable().log();
      }

   }

   public void cleanup() {
   }
}
