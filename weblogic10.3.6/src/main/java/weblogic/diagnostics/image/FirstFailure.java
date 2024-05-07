package weblogic.diagnostics.image;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.management.runtime.TaskRuntimeMBean;
import weblogic.management.runtime.WLDFImageCreationTaskRuntimeMBean;

public class FirstFailure {
   private static ImageManager imageManager = ImageManager.getInstance();
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticImage");

   public static TaskRuntimeMBean fail() {
      WLDFImageCreationTaskRuntimeMBean var0 = null;

      try {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("FirstFailure invoked.");
         }

         var0 = imageManager.captureImage();
      } catch (ImageAlreadyCapturedException var2) {
         DiagnosticsLogger.logDiagnosticImageAlreadyCaptured();
      } catch (InvalidDestinationDirectoryException var3) {
         DiagnosticsLogger.logDiagnosticImageDirectoryAccessError(var3.getMessage());
      }

      return var0;
   }
}
