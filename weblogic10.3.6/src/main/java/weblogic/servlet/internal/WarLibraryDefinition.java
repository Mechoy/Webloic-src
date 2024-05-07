package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import weblogic.application.Type;
import weblogic.application.io.ExplodedJar;
import weblogic.application.io.JarCopyFilter;
import weblogic.application.library.ApplicationLibrary;
import weblogic.application.library.CachableLibMetadataEntry;
import weblogic.application.library.CachableLibMetadataType;
import weblogic.application.library.IllegalSpecVersionTypeException;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryConstants;
import weblogic.application.library.LibraryContext;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryMetadataCache;
import weblogic.application.library.LibraryProcessingException;
import weblogic.application.library.LibraryReference;
import weblogic.application.library.LibraryReferenceFactory;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WebBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.utils.WarUtils;
import weblogic.servlet.utils.annotation.ClassAnnotationDetector;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.collections.ArraySet;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class WarLibraryDefinition extends LibraryDefinition implements Library, ApplicationLibrary {
   private LibraryReference[] libraryRefs = null;
   private final File extractDir;
   private final boolean archived;
   private Map cacheEntries;
   private static String singletonAutoRefLibName = null;
   private final String ctxRootFromLocation;
   private ExplodedJar jar;
   private String jsfConfigFiles = null;
   private boolean isReferenceJSFLib = false;
   private Map tldInfo = null;
   private boolean isAnnotationEnabled = false;

   public WarLibraryDefinition(LibraryData var1, File var2) {
      super(var1, Type.WAR);
      this.extractDir = var2;
      this.ctxRootFromLocation = this.getLocation().getName();
      this.archived = !this.getLocation().isDirectory();
      this.cacheEntries = new HashMap();
      this.cacheEntries.put(CachableLibMetadataType.ANNOTATED_CLASSES, new AnnotatedClassesCacheEntry(this));
      this.cacheEntries.put(CachableLibMetadataType.TLD, new TldCacheEntry(this));
      this.cacheEntries.put(CachableLibMetadataType.TAG_HANDLERS, new AnnotatedTagHandlersCacheEntry(this));
      this.cacheEntries.put(CachableLibMetadataType.TAG_LISTENERS, new AnnotatedTagListenersCacheEntry(this));
      this.cacheEntries.put(CachableLibMetadataType.FACE_BEANS, new FaceConfigCacheEntry(this));
   }

   public Collection getAnnotatedTagListeners() throws LibraryProcessingException {
      return this.getAnnotatedTagClasses("listener-class");
   }

   public Collection getAnnotatedTagHandlers() throws LibraryProcessingException {
      return this.getAnnotatedTagClasses("tag-class");
   }

   public boolean isArchived() {
      return this.archived;
   }

   public File getLibTempDir() {
      return this.extractDir;
   }

   public List getTldLocations() throws LibraryProcessingException {
      ClassFinder var1 = null;
      War.ResourceFinder var2 = null;
      ArrayList var3 = new ArrayList();

      try {
         var1 = this.getClassFinder();
         var2 = new War.ResourceFinder("wld#", var1);
         War.findTlds(var2, var3, var1);
      } catch (Exception var9) {
         throw new LibraryProcessingException(var9);
      } finally {
         if (var1 != null) {
            var1.close();
         }

         if (var2 != null) {
            var2.close();
         }

      }

      return var3;
   }

   public List getFacesConfigLocations() throws LibraryProcessingException {
      if (!this.isReferenceJSFLib) {
         return Collections.EMPTY_LIST;
      } else {
         ClassFinder var1 = null;
         War.ResourceFinder var2 = null;

         List var3;
         try {
            var1 = this.getClassFinder();
            var2 = new War.ResourceFinder("wld#", var1);
            var3 = War.findFacesConfigs(this.jsfConfigFiles, var2, var1);
         } catch (Exception var8) {
            throw new LibraryProcessingException(var8);
         } finally {
            if (var1 != null) {
               var1.close();
            }

            if (var2 != null) {
               var2.close();
            }

         }

         return var3;
      }
   }

   public List getAnnotatedClasses() throws LibraryProcessingException {
      if (!this.isAnnotationEnabled) {
         return Collections.EMPTY_LIST;
      } else {
         List var1 = null;
         ClassFinder var2 = null;

         try {
            var2 = this.getClassFinder();
            String var3 = "weblogic.servlet.internal.WebAnnotationProcessorImpl";
            WebAnnotationProcessor var4 = (WebAnnotationProcessor)Class.forName(var3).newInstance();
            var1 = var4.getAnnotatedClasses(var2);
         } catch (Exception var9) {
            throw new LibraryProcessingException(var9);
         } finally {
            if (var2 != null) {
               var2.close();
            }

         }

         return var1;
      }
   }

   private ClassFinder getClassFinder() throws IOException {
      if (this.archived) {
         return this.jar.getClassFinder();
      } else {
         ExplodedJar var1 = new ExplodedJar("wld", this.extractDir, new File[]{this.getLocation()}, War.WAR_CLASSPATH_INFO, JarCopyFilter.NOCOPY_FILTER);
         return var1.getClassFinder();
      }
   }

   public void init() throws LibraryProcessingException {
      LibraryConstants.AutoReferrer[] var1 = this.getAutoRef();
      if (var1.length > 0) {
         LibraryConstants.AutoReferrer[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            LibraryConstants.AutoReferrer var5 = var2[var4];
            if (var5 != LibraryConstants.AutoReferrer.WebApp) {
               throw new LibraryProcessingException("Unsupported Auto-Ref value: " + var5);
            }
         }

         if (singletonAutoRefLibName == null) {
            singletonAutoRefLibName = this.getName();
         } else if (!singletonAutoRefLibName.equals(this.getName())) {
            throw new LibraryProcessingException("Only one web auto reference library allowed. " + singletonAutoRefLibName + " is already deployed as an auto reference library");
         }
      }

      if (this.jar == null && this.archived) {
         try {
            this.jar = new ExplodedJar("wld", this.extractDir, this.getLocation(), War.WAR_CLASSPATH_INFO);
            this.setLocation(this.extractDir);
         } catch (IOException var14) {
            throw new LibraryProcessingException(var14);
         } finally {
            if (this.jar != null) {
               try {
                  this.jar.getClassFinder().close();
               } catch (IOException var13) {
               }
            }

         }
      }

      this.initDescriptors();
      LibraryMetadataCache.getInstance().initLibraryCache(this);
   }

   public void importLibrary(J2EELibraryReference var1, LibraryContext var2, MultiClassFinder var3) throws LibraryProcessingException {
      String var4 = var1.getContextRoot() == null ? this.ctxRootFromLocation : var1.getContextRoot();
      this.addWebModule(var2, this.getLocation().getName(), var4);

      try {
         var2.registerLink(this.getLocation());
      } catch (IOException var6) {
         throw new LibraryProcessingException(var6);
      }
   }

   public LibraryReference[] getLibraryReferences() {
      return this.libraryRefs;
   }

   public void cleanup() throws LibraryProcessingException {
      LibraryConstants.AutoReferrer[] var1 = this.getAutoRef();
      if (var1.length > 0 && singletonAutoRefLibName != null && singletonAutoRefLibName.equals(this.getName())) {
         singletonAutoRefLibName = null;
      }

   }

   public void remove() throws LibraryProcessingException {
      if (this.jar != null) {
         this.jar.remove();
         this.jar = null;
      }

      LibraryMetadataCache.getInstance().clearLibraryCache(this);
      this.cacheEntries.clear();
   }

   public CachableLibMetadataEntry[] findAllCachableEntry() {
      return (CachableLibMetadataEntry[])((CachableLibMetadataEntry[])this.cacheEntries.values().toArray(new CachableLibMetadataEntry[this.cacheEntries.size()]));
   }

   public CachableLibMetadataEntry getCachableEntry(CachableLibMetadataType var1) {
      return (CachableLibMetadataEntry)this.cacheEntries.get(var1);
   }

   private void addWebModule(LibraryContext var1, String var2, String var3) throws LibraryProcessingException {
      ApplicationBean var4 = var1.getApplicationDD();
      WebBean var5 = var4.createModule().createWeb();
      var5.setWebUri(var2);
      var5.setContextRoot(var3);
      LibraryLoggingUtils.updateDescriptor(var1.getApplicationDescriptor(), var4);
   }

   private void initDescriptors() throws LibraryProcessingException {
      VirtualJarFile var1 = null;

      try {
         var1 = VirtualJarFactory.createVirtualJar(this.getLocation());
         WebAppDescriptor var2 = new WebAppDescriptor(var1);
         WebAppBean var3 = var2.getWebAppBean();
         WeblogicWebAppBean var4 = var2.getWeblogicWebAppBean();
         this.isAnnotationEnabled = WarUtils.isAnnotationEnabled(var3);
         this.jsfConfigFiles = WarUtils.getFacesConfigFiles(var3);
         this.isReferenceJSFLib = WarUtils.isJsfApplication(var3, var4);
         if (var4 != null) {
            this.libraryRefs = LibraryReferenceFactory.getWebLibReference(var4.getLibraryRefs());
         }
      } catch (IOException var15) {
         throw new LibraryProcessingException(var15);
      } catch (XMLStreamException var16) {
         throw new LibraryProcessingException(var16);
      } catch (IllegalSpecVersionTypeException var17) {
         throw new LibraryProcessingException(HTTPLogger.logIllegalWebLibSpecVersionRefLoggable(this.getLocation().getName(), var17.getSpecVersion()).getMessage());
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var14) {
            }
         }

      }

   }

   private Map getTldInfo() throws LibraryProcessingException {
      if (this.tldInfo == null) {
         CachableLibMetadataEntry var1 = this.getCachableEntry(CachableLibMetadataType.TLD);
         DescriptorCacheEntry.DescriptorCachableObject var2 = (DescriptorCacheEntry.DescriptorCachableObject)LibraryMetadataCache.getInstance().lookupCachedObject(var1);
         this.tldInfo = TldCacheHelper.parseTagLibraries(var2.getResourceLocation(), (File)var2.getCacheBaseDir(), this.getName());
      }

      return this.tldInfo;
   }

   private Collection getAnnotatedTagClasses(String var1) throws LibraryProcessingException {
      if (!this.isAnnotationEnabled) {
         return Collections.EMPTY_SET;
      } else {
         Object var2 = Collections.EMPTY_SET;
         ClassFinder var3 = null;

         try {
            var3 = this.getClassFinder();
            Map var4 = this.getTldInfo();
            if (var4 == Collections.EMPTY_MAP) {
               Set var14 = Collections.EMPTY_SET;
               return var14;
            }

            Collection var5 = (Collection)var4.get(var1);
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               Object var7 = var6.next();
               if (ClassAnnotationDetector.isClassHasAnnotation(var3, (String)var7)) {
                  if (var2 == Collections.EMPTY_SET) {
                     var2 = new ArraySet();
                  }

                  ((Collection)var2).add(var7);
               }
            }
         } catch (IOException var12) {
            throw new LibraryProcessingException(var12);
         } finally {
            if (var3 != null) {
               var3.close();
            }

         }

         return (Collection)var2;
      }
   }

   static class Noop extends WarLibraryDefinition {
      Noop(LibraryData var1, File var2) {
         super(var1, var2);
      }

      public void importLibrary(J2EELibraryReference var1, LibraryContext var2, MultiClassFinder var3) {
      }
   }
}
