package weblogic.application.internal.library;

import java.io.File;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryFactory;

public class JarLibraryFactory implements LibraryFactory {
   public LibraryDefinition createLibrary(LibraryData var1, File var2) {
      return new JarLibraryDefinition(var1);
   }
}
