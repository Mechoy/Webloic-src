package weblogic.ejb.spi;

import java.io.IOException;
import weblogic.application.Type;
import weblogic.application.library.ApplicationLibrary;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryConstants;
import weblogic.application.library.LibraryContext;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.MultiClassFinder;

public class EJBLibraryDefinition extends LibraryDefinition implements Library, ApplicationLibrary {
   private static String singletonAutoRefLibName = null;

   public EJBLibraryDefinition(LibraryData var1) {
      super(var1, Type.EJB);
   }

   public void importLibrary(J2EELibraryReference var1, LibraryContext var2, MultiClassFinder var3) throws LibraryProcessingException {
      LibraryLoggingUtils.checkNoContextRootSet(var1, Type.EJB);
      this.addEJB(var2, this.getLocation().getName());

      try {
         var2.registerLink(this.getLocation());
      } catch (IOException var5) {
         throw new LibraryProcessingException(var5);
      }
   }

   private ClassFinder getClassFinder() {
      return new ClasspathClassFinder2(this.getLocation().getAbsolutePath());
   }

   private void addEJB(LibraryContext var1, String var2) throws LibraryProcessingException {
      ApplicationBean var3 = var1.getApplicationDD();
      var3.createModule().setEjb(var2);
      LibraryLoggingUtils.updateDescriptor(var1.getApplicationDescriptor(), var3);
   }

   public void init() throws LibraryProcessingException {
      LibraryConstants.AutoReferrer[] var1 = this.getAutoRef();
      if (var1.length > 0) {
         LibraryConstants.AutoReferrer[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            LibraryConstants.AutoReferrer var5 = var2[var4];
            if (var5 != LibraryConstants.AutoReferrer.EJBApp) {
               throw new LibraryProcessingException("Unsupported Auto-Ref value: " + var5);
            }
         }

         if (singletonAutoRefLibName == null) {
            singletonAutoRefLibName = this.getName();
         } else if (!singletonAutoRefLibName.equals(this.getName())) {
            throw new LibraryProcessingException("Only one ejb auto reference library allowed. " + singletonAutoRefLibName + " is already deployed as an auto reference library");
         }
      }

   }

   public void cleanup() throws LibraryProcessingException {
      LibraryConstants.AutoReferrer[] var1 = this.getAutoRef();
      if (var1.length > 0 && singletonAutoRefLibName != null && singletonAutoRefLibName.equals(this.getName())) {
         singletonAutoRefLibName = null;
      }

   }
}
