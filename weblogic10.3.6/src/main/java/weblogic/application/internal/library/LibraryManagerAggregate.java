package weblogic.application.internal.library;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryProvider;
import weblogic.application.utils.XMLWriter;

public class LibraryManagerAggregate {
   private static final LibraryRegistry libReg = LibraryRegistry.getRegistry();
   private final Collection<LibraryManager> libraryManagers = new HashSet();
   private final Map<String, LibraryManager> moduleMapping = new HashMap();
   private LibraryManager appLevelLibraryManager = null;
   private LibraryManager optionalPackagesManager = null;

   public void setAppLevelLibraryManager(LibraryManager var1) {
      this.appLevelLibraryManager = var1;
      this.addLibraryManager(var1);
   }

   public void setOptionalPackagesManager(LibraryManager var1) {
      this.optionalPackagesManager = var1;
      this.addLibraryManager(var1);
   }

   public LibraryManager getOptionalPackagesManager() {
      return this.optionalPackagesManager;
   }

   public void addLibraryManager(String var1, LibraryManager var2) {
      this.addLibraryManager(var2);
      this.moduleMapping.put(var1, var2);
   }

   public void removeLibraryManager(String var1, LibraryManager var2) {
      this.libraryManagers.remove(var2);
      this.moduleMapping.remove(var1);
   }

   public LibraryProvider getLibraryProvider(String var1) {
      return (LibraryProvider)(var1 == null ? this.appLevelLibraryManager : (LibraryProvider)this.moduleMapping.get(var1));
   }

   public void addLibraryManager(LibraryManager var1) {
      this.libraryManagers.add(var1);
   }

   public boolean hasUnresolvedRefs() {
      Iterator var1 = this.libraryManagers.iterator();

      LibraryManager var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (LibraryManager)var1.next();
      } while(!var2.hasUnresolvedReferences());

      return true;
   }

   public Library[] getUnreferencedLibraries() {
      Collection var1 = libReg.getAll();
      Iterator var2 = this.libraryManagers.iterator();

      while(var2.hasNext()) {
         LibraryManager var3 = (LibraryManager)var2.next();
         Library[] var4 = var3.getReferencedLibraries();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var1.remove(var4[var5]);
         }
      }

      return (Library[])((Library[])var1.toArray(new Library[var1.size()]));
   }

   public String getUnresolvedRefsError() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.libraryManagers.iterator();

      while(var2.hasNext()) {
         LibraryManager var3 = (LibraryManager)var2.next();
         if (var3.hasUnresolvedReferences()) {
            if (var1.length() > 0) {
               var1.append(" ");
            }

            var3.getUnresolvedReferencesError(var1);
         }
      }

      return var1.toString();
   }

   public void writeDiagnosticImage(XMLWriter var1) {
      Iterator var2 = this.libraryManagers.iterator();

      while(var2.hasNext()) {
         LibraryManager var3 = (LibraryManager)var2.next();
         var3.writeDiagnosticImage(var1);
      }

   }
}
