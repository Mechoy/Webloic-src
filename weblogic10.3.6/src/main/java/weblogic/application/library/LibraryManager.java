package weblogic.application.library;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import weblogic.application.Type;
import weblogic.application.internal.library.BasicLibraryData;
import weblogic.application.internal.library.LibraryRegistry;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.application.utils.LibraryUtils;
import weblogic.application.utils.XMLWriter;
import weblogic.management.runtime.LibraryRuntimeMBean;

public class LibraryManager implements LibraryProvider {
   private static final LibraryRegistry libReg = LibraryRegistry.getRegistry();
   private final List libraries;
   private final List resolvedRefs;
   private final Collection unresolvedRefs;
   private final Collection allRefs;
   private final Set libRefs;
   private final LibraryReferencer referencer;
   private final List<LibraryDefinition> autoRefLibs;

   public LibraryManager(LibraryReferencer var1) {
      this(var1, (LibraryReference[])null);
   }

   public LibraryManager(LibraryReferencer var1, LibraryReference[] var2) {
      this.libraries = new ArrayList();
      this.resolvedRefs = new ArrayList();
      this.unresolvedRefs = new ArrayList();
      this.allRefs = new HashSet();
      this.libRefs = new HashSet();
      this.autoRefLibs = new ArrayList();
      if (var1 == null) {
         throw new IllegalArgumentException("null referencer not allowed");
      } else {
         this.lookup(var2);
         this.referencer = var1;
      }
   }

   public void lookup(LibraryReference[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.lookup(var1[var2]);
         }

      }
   }

   private void lookup(LibraryReference var1) {
      if (var1 != null) {
         this.findReferencedLibraries(var1);
      }
   }

   private boolean isDuplicateRef(LibraryReference var1) {
      if (this.allRefs.contains(var1)) {
         return true;
      } else {
         this.allRefs.add(var1);
         return false;
      }
   }

   public Library[] getUnreferencedLibraries() {
      Collection var1 = libReg.getAll();
      Iterator var2 = this.libraries.iterator();

      while(var2.hasNext()) {
         var1.remove(var2.next());
      }

      return (Library[])((Library[])var1.toArray(new Library[var1.size()]));
   }

   public boolean hasReferencedLibraries() {
      return !this.libraries.isEmpty();
   }

   public Library[] getReferencedLibraries() {
      return (Library[])((Library[])this.libraries.toArray(new Library[this.libraries.size()]));
   }

   public Library[] getAutoReferencedLibraries() {
      return (Library[])this.autoRefLibs.toArray(new Library[this.autoRefLibs.size()]);
   }

   public LibraryReferencer getReferencer() {
      return this.referencer;
   }

   public void lookupAndAddAutoReferences(Type var1, LibraryConstants.AutoReferrer var2) {
      Collection var3 = libReg.getAll();
      TreeSet var4 = new TreeSet();
      Iterator var5 = var3.iterator();

      while(true) {
         LibraryDefinition var6;
         boolean var7;
         do {
            do {
               if (!var5.hasNext()) {
                  try {
                     var5 = var4.iterator();

                     while(var5.hasNext()) {
                        String var11 = (String)var5.next();
                        BasicLibraryData var12 = new BasicLibraryData(var11, (String)null, (String)null, var1);
                        LibraryDefinition var13 = libReg.lookup(new J2EELibraryReference(var12, false, (String)null));
                        this.autoRefLibs.add(var13);
                        var13.getRuntimeImpl().addReference(this.referencer);
                     }
                  } catch (IllegalSpecVersionTypeException var10) {
                  }

                  return;
               }

               var6 = (LibraryDefinition)var5.next();
               var7 = false;
               LibraryConstants.AutoReferrer[] var8 = var6.getAutoRef();

               for(int var9 = 0; !var7 && var9 < var8.length; ++var9) {
                  var7 = var8[var9] == var2;
               }
            } while(!var7);
         } while(var1 != null && !var1.equals(var6.getType()));

         if (this.libraries.contains(var6)) {
            if (LibraryUtils.isDebugOn()) {
               LibraryUtils.debug("The auto-ref library: " + var6.getName() + " is explicitly referred");
            }
         } else {
            var4.add(var6.getName());
         }
      }
   }

   public void initializeReferencedLibraries() throws LoggableLibraryProcessingException {
      LibraryDefinition var2;
      for(Iterator var1 = this.libraries.iterator(); var1.hasNext(); LibraryLoggingUtils.initLibraryDefinition(var2)) {
         var2 = (LibraryDefinition)var1.next();
         if (LibraryUtils.isDebugOn()) {
            LibraryUtils.debug("Calling init on " + var2);
         }
      }

   }

   public LibraryReference[] getLibraryReferences() {
      return (LibraryReference[])((LibraryReference[])this.resolvedRefs.toArray(new LibraryReference[this.resolvedRefs.size()]));
   }

   public LibraryRuntimeMBean[] getReferencedLibraryRuntimes() {
      LibraryRuntimeMBean[] var1 = new LibraryRuntimeMBean[this.libraries.size()];
      int var2 = this.libraries.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3] = ((Library)this.libraries.get(var3)).getRuntime();
      }

      return var1;
   }

   public boolean hasUnresolvedReferences() {
      return !this.unresolvedRefs.isEmpty();
   }

   public LibraryReference[] getUnresolvedReferences() {
      return (LibraryReference[])((LibraryReference[])this.unresolvedRefs.toArray(new LibraryReference[this.unresolvedRefs.size()]));
   }

   public void resetUnresolvedReferences() {
      this.unresolvedRefs.clear();
   }

   public String getUnresolvedReferencesAsString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.unresolvedRefs.iterator();

      while(var2.hasNext()) {
         LibraryReference var3 = (LibraryReference)var2.next();
         var1.append("[").append(var3.toString()).append("]");
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      return var1.toString();
   }

   public void getUnresolvedReferencesError(StringBuffer var1) {
      var1.append(this.referencer.getUnresolvedError()).append(" ").append(this.getUnresolvedReferencesAsString());
   }

   public String getUnresolvedReferencesError() {
      StringBuffer var1 = new StringBuffer();
      this.getUnresolvedReferencesError(var1);
      return var1.toString();
   }

   private void registerUnresolvedLibraryRef(LibraryReference var1) {
      this.unresolvedRefs.add(var1);
   }

   private void registerLibrary(LibraryReference var1, Library var2) {
      if (!this.libRefs.add(var1.getCompositeEntry(var2))) {
         if (LibraryUtils.isDebugOn()) {
            LibraryUtils.debug("Ignoroning entry --> " + var2.getLocation());
         }

      } else {
         this.resolvedRefs.add(var1);
         this.libraries.add(var2);
      }
   }

   private void findReferencedLibraries(LibraryReference var1) {
      if (this.isDuplicateRef(var1)) {
         if (LibraryUtils.isDebugOn()) {
            LibraryUtils.debug("Ignoring duplicate reference: " + var1);
         }

      } else {
         LibraryDefinition var2 = libReg.lookup(var1);
         if (LibraryUtils.isDebugOn()) {
            LibraryUtils.debug("Referenced library is " + var2);
         }

         if (var2 == null) {
            this.registerUnresolvedLibraryRef(var1);
         } else {
            if (LibraryUtils.isDebugOn()) {
               LibraryUtils.debug("Adding library to result list: " + var2);
            }

            this.registerLibrary(var1, var2);
            LibraryReference[] var3 = var2.getLibraryReferences();
            if (var3 != null) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  if (LibraryUtils.isDebugOn()) {
                     LibraryUtils.debug("Found library reference " + var3[var4]);
                  }

                  this.findReferencedLibraries(var3[var4]);
               }
            } else if (LibraryUtils.isDebugOn()) {
               LibraryUtils.debug("This library does not reference other libraries");
            }

         }
      }
   }

   public void addReferences() {
      Iterator var1 = this.libraries.iterator();

      while(var1.hasNext()) {
         LibraryDefinition var2 = (LibraryDefinition)var1.next();
         var2.getRuntimeImpl().addReference(this.referencer);
      }

   }

   public void removeReferences() {
      Iterator var1;
      LibraryDefinition var2;
      for(var1 = this.libraries.iterator(); var1.hasNext(); var2.getRuntimeImpl().removeReference(this.referencer)) {
         var2 = (LibraryDefinition)var1.next();
         if (LibraryUtils.isDebugOn()) {
            LibraryUtils.debug("For library: " + var2.getName());
         }

         if (LibraryUtils.isDebugOn()) {
            LibraryUtils.debug("removing its ref to:" + this.referencer.getReferencerName());
         }

         if (LibraryUtils.isDebugOn()) {
            LibraryUtils.debug("library runtime is: " + var2.getRuntimeImpl());
         }
      }

      var1 = this.autoRefLibs.iterator();

      while(var1.hasNext()) {
         var2 = (LibraryDefinition)var1.next();
         var2.getRuntimeImpl().removeReference(this.referencer);
      }

      this.autoRefLibs.clear();
      this.libraries.clear();
      this.libRefs.clear();
   }

   public File[] getOptionalPackages(LibraryReference[] var1) {
      if (var1 == null) {
         return null;
      } else {
         ArrayList var2 = new ArrayList(var1.length);

         for(int var3 = 0; var3 < var1.length; ++var3) {
            LibraryDefinition var4 = libReg.lookup(var1[var3]);
            if (var4 != null) {
               var2.add(var4.getLocation());
            }

            if (!this.isDuplicateRef(var1[var3])) {
               if (var4 == null) {
                  this.registerUnresolvedLibraryRef(var1[var3]);
               } else {
                  this.registerLibrary(var1[var3], var4);
               }
            }
         }

         if (var2.isEmpty()) {
            return null;
         } else {
            return (File[])((File[])var2.toArray(new File[var2.size()]));
         }
      }
   }

   public void writeDiagnosticImage(XMLWriter var1) {
      if (!this.libraries.isEmpty()) {
         String var2 = this.referencer.getReferencerName();
         if (var2 == null) {
            var2 = "app";
         }

         var1.addElement("referencer");
         var1.addElement("name", var2);
         int var3 = this.libraries.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            Library var5 = (Library)this.libraries.get(var4);
            LibraryReference var6 = (LibraryReference)this.resolvedRefs.get(var4);
            var1.addElement("reference", var6.toString());
            var1.addElement("library", var5.toString());
         }

         var1.closeElement();
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.libraries.isEmpty()) {
         var1.append("No libraries available\n");
      } else {
         var1.append("Available libraries ").append(this.libraries.toString());
      }

      if (this.unresolvedRefs.isEmpty()) {
         return var1.toString();
      } else {
         var1.append("\n").append("Unresolved references ").append(this.unresolvedRefs.toString());
         return var1.toString();
      }
   }
}
