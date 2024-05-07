package weblogic.ejb.spi;

import java.io.File;
import java.io.IOException;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryFactory;
import weblogic.application.library.LibraryProcessingException;

public final class EJBLibraryFactory implements LibraryFactory {
   public LibraryDefinition createLibrary(LibraryData var1, File var2) throws LibraryProcessingException {
      try {
         return EJBJarUtils.isEJB(var1.getLocation()) ? new EJBLibraryDefinition(var1) : null;
      } catch (IOException var4) {
         throw new LibraryProcessingException(var4);
      }
   }
}
