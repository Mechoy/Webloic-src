package weblogic.application.library;

import weblogic.utils.classloaders.MultiClassFinder;

public interface ApplicationLibrary {
   void importLibrary(J2EELibraryReference var1, LibraryContext var2, MultiClassFinder var3) throws LibraryProcessingException;
}
