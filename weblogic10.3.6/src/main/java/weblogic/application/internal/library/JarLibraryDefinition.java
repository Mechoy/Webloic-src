package weblogic.application.internal.library;

import java.io.IOException;
import weblogic.application.Type;
import weblogic.application.library.ApplicationLibrary;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryContext;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.JarClassFinder;
import weblogic.utils.classloaders.MultiClassFinder;

public class JarLibraryDefinition extends LibraryDefinition implements Library, ApplicationLibrary {
   public JarLibraryDefinition(LibraryData var1) {
      super(var1, Type.JAR);
   }

   public void importLibrary(J2EELibraryReference var1, LibraryContext var2, MultiClassFinder var3) throws LibraryProcessingException {
      LibraryLoggingUtils.checkNoContextRootSet(var1, Type.JAR);

      try {
         var3.addFinderFirst(this.getClassFinder());
      } catch (IOException var5) {
         throw new LibraryProcessingException(var5);
      }
   }

   private ClassFinder getClassFinder() throws IOException {
      return new JarClassFinder(this.getLocation());
   }

   public void init() throws LibraryProcessingException {
      if (this.getAutoRef().length > 0) {
         throw new LibraryProcessingException("jar libraries may not be auto-ref");
      }
   }
}
