package weblogic.application.internal.library;

import java.io.File;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryFactory;
import weblogic.application.library.LibraryProcessingException;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.EarUtils;
import weblogic.j2ee.J2EELogger;

public class EarLibraryFactory implements LibraryFactory {
   public LibraryDefinition createLibrary(LibraryData var1, File var2) throws LibraryProcessingException {
      if (EarUtils.isEar(var1.getLocation())) {
         if (EarUtils.isSplitDir(var1.getLocation())) {
            throw new LoggableLibraryProcessingException(J2EELogger.logSplitDirNotSupportedForLibrariesLoggable(var1.getLocation().getAbsolutePath()));
         } else {
            return new EarLibraryDefinition(var1, var2);
         }
      } else {
         return null;
      }
   }
}
