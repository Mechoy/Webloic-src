package weblogic.application.compiler.flow;

import java.util.Arrays;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.library.Library;
import weblogic.j2ee.J2EELogger;
import weblogic.utils.compiler.ToolFailureException;

public final class CheckUnusedLibrariesFlow extends CompilerFlow {
   public CheckUnusedLibrariesFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      Library[] var1 = this.ctx.getApplicationContext().getLibraryManagerAggregate().getUnreferencedLibraries();
      if (var1.length != 0) {
         Arrays.sort(var1);
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.append(var1[var3].toString());
            if (var3 < var1.length - 1) {
               var2.append(", ");
            }
         }

         J2EELogger.logAppcUnreferencedLibraries(var2.toString());
      }
   }

   public void cleanup() {
   }
}
