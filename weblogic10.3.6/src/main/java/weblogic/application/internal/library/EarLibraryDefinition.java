package weblogic.application.internal.library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.Type;
import weblogic.application.io.DescriptorFinder;
import weblogic.application.io.Ear;
import weblogic.application.io.ManifestFinder;
import weblogic.application.library.ApplicationLibrary;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryContext;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryProcessingException;
import weblogic.application.library.LibraryReference;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.IOUtils;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.application.utils.LibraryUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.j2ee.descriptor.wl.PreferApplicationPackagesBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class EarLibraryDefinition extends LibraryDefinition implements Library, ApplicationLibrary {
   private final File extractDir;
   private Ear ear = null;
   private VirtualJarFile vjf = null;
   private ApplicationDescriptor parser = null;
   private J2EELibraryReference[] libraryRefs = null;
   private boolean inited = false;
   private List filters;

   public EarLibraryDefinition(LibraryData var1, File var2) {
      super(var1, Type.EAR);
      this.filters = Collections.EMPTY_LIST;
      this.extractDir = var2;
   }

   public void init() throws LibraryProcessingException {
      if (!this.inited) {
         if (this.getAutoRef().length > 0) {
            throw new LibraryProcessingException("ear libraries may not be auto-ref");
         } else {
            try {
               if (this.isArchive(this.getLocation())) {
                  this.ear = new Ear(this.getName(), this.extractDir, this.getLocation());
                  this.setLocation(this.extractDir);
                  if (LibraryUtils.isDebugOn()) {
                     LibraryUtils.debug("Extracted ear into:" + this.extractDir.getAbsolutePath());
                  }
               } else {
                  this.ear = new Ear(this.getName(), this.extractDir, new File[]{this.getLocation()});
               }

               this.initDescriptors();
            } catch (IOException var2) {
               throw new LibraryProcessingException(var2);
            }

            this.inited = true;
         }
      }
   }

   private void initDescriptors() throws IOException, LibraryProcessingException {
      try {
         this.vjf = VirtualJarFactory.createVirtualJar(this.getLocation());
         this.parser = new ApplicationDescriptor(this.vjf);
         WeblogicApplicationBean var1 = this.parser.getWeblogicApplicationDescriptor();
         if (var1 != null) {
            this.libraryRefs = LibraryLoggingUtils.initLibRefs(var1.getLibraryRefs());
            PreferApplicationPackagesBean var2 = var1.getPreferApplicationPackages();
            if (var2 != null) {
               String[] var3 = var2.getPackageNames();
               this.filters = validatePackages(var3);
            }
         }

      } catch (XMLStreamException var4) {
         throw new LibraryProcessingException(var4);
      }
   }

   private boolean isArchive(File var1) {
      return var1.isFile();
   }

   public void cleanup() {
      IOUtils.forceClose(this.vjf);
      if (this.ear != null) {
         this.ear.getClassFinder().close();
      }

   }

   public void remove() {
      if (this.ear != null) {
         if (LibraryUtils.isDebugOn()) {
            LibraryUtils.debug("Calling remove on ear library");
         }

         this.ear.remove();
      }

   }

   public void importLibrary(J2EELibraryReference var1, LibraryContext var2, MultiClassFinder var3) throws LibraryProcessingException {
      LibraryLoggingUtils.checkNoContextRootSet(var1, Type.EAR);
      ModuleBean[] var4 = null;
      WeblogicModuleBean[] var5 = null;
      CustomModuleBean[] var6 = null;

      try {
         ApplicationBean var7 = this.parser.getApplicationDescriptor();
         if (var7 != null) {
            var4 = var7.getModules();
         }

         WeblogicApplicationBean var8 = this.parser.getWeblogicApplicationDescriptor();
         if (var8 != null) {
            var5 = var8.getModules();
         }

         WeblogicExtensionBean var9 = this.parser.getWeblogicExtensionDescriptor();
         if (var9 != null) {
            var6 = var9.getCustomModules();
         }

         ApplicationDescriptor var10 = var2.getApplicationDescriptor();
         LibraryLoggingUtils.mergeDescriptors(var10, this.vjf);
         var2.notifyDescriptorUpdate();
      } catch (IOException var14) {
         LibraryLoggingUtils.errorMerging(var14);
      } catch (XMLStreamException var15) {
         LibraryLoggingUtils.errorMerging(var15);
      }

      int var16;
      if (var4 != null) {
         for(var16 = 0; var16 < var4.length; ++var16) {
            try {
               var2.registerLink(new File(this.getLocation(), EarUtils.reallyGetModuleURI(var4[var16])));
            } catch (IOException var13) {
               throw new LibraryProcessingException(var13);
            }
         }
      }

      if (var5 != null) {
         for(var16 = 0; var16 < var5.length; ++var16) {
            try {
               String var17 = var5[var16].getPath();
               File var19 = new File(this.getLocation(), var17);
               if (var19.isDirectory()) {
                  var2.registerLink(var19.getName(), var19);
               } else {
                  var2.registerLink(var17, var19);
               }
            } catch (IOException var12) {
               throw new LibraryProcessingException(var12);
            }
         }
      }

      if (var6 != null) {
         for(var16 = 0; var16 < var6.length; ++var16) {
            try {
               File var18 = new File(this.getLocation(), var6[var16].getUri());
               if (var18.exists()) {
                  var2.registerLink(var18);
               } else {
                  var2.registerLink(var6[var16].getUri(), this.getLocation());
               }
            } catch (IOException var11) {
               throw new LibraryProcessingException(var11);
            }
         }
      }

      var3.addFinderFirst(new DescriptorFinder(var2.getRefappUri(), new ClasspathClassFinder2(this.getLocation().getAbsolutePath())));
      var3.addFinderFirst(this.getClassFinder());
   }

   public LibraryReference[] getLibraryReferences() {
      return this.libraryRefs;
   }

   private ClassFinder getClassFinder() {
      MultiClassFinder var1 = new MultiClassFinder();
      var1.addFinder(new ManifestFinder.ExtensionListFinder(this.getLocation()));
      var1.addFinder(new ClasspathClassFinder2(this.ear.getClassFinder().getClassPath()));
      return var1;
   }

   public List getFilters() {
      return this.filters;
   }

   private static List validatePackages(String[] var0) {
      if (var0 != null && var0.length != 0) {
         List var1 = Arrays.asList(var0);
         ArrayList var2 = new ArrayList(var1.size());

         String var4;
         for(Iterator var3 = var1.iterator(); var3.hasNext(); var2.add(var4)) {
            var4 = (String)var3.next();
            if (var4.endsWith("*")) {
               var4 = var4.substring(0, var4.length() - 1);
            }

            if (!var4.endsWith(".")) {
               var4 = var4 + ".";
            }
         }

         return var2;
      } else {
         return Collections.EMPTY_LIST;
      }
   }
}
