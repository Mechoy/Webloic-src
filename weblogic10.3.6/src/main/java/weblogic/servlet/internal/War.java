package weblogic.servlet.internal;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.SplitDirectoryInfo;
import weblogic.application.io.Archive;
import weblogic.application.io.ClasspathInfo;
import weblogic.application.io.DescriptorFinder;
import weblogic.application.io.Ear;
import weblogic.application.io.ExplodedJar;
import weblogic.application.io.JarCopyFilter;
import weblogic.application.io.ManifestFinder;
import weblogic.application.library.CachableLibMetadata;
import weblogic.application.library.CachableLibMetadataEntry;
import weblogic.application.library.CachableLibMetadataType;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryMetadataCache;
import weblogic.application.library.LibraryProcessingException;
import weblogic.application.utils.CompositeWebAppFinder;
import weblogic.application.utils.PathUtils;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.servlet.security.internal.WebAppSecurity;
import weblogic.servlet.utils.ServletMapping;
import weblogic.servlet.utils.annotation.ClassAnnotationDetector;
import weblogic.utils.FileUtils;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.DelegateFinder;
import weblogic.utils.classloaders.DelegateSource;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.classloaders.NullClassFinder;
import weblogic.utils.classloaders.NullSource;
import weblogic.utils.classloaders.Source;
import weblogic.utils.collections.ArraySet;
import weblogic.utils.collections.SecondChanceCacheMap;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.enumerations.IteratorEnumerator;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.utils.zip.SafeZipFileInputStream;

public class War {
   private static final String WEB_INF_FACES_CONFIG = "/WEB-INF/faces-config.xml";
   private static final String META_INF_FACES_CONFIG = "META-INF/faces-config.xml";
   private static final String BEA_EXTN_DIR = "/WEB-INF/bea-ext";
   static final String WAR_EXTRACT_ROOT = "war";
   private static final String LIBRARY_EXTRACT_ROOT = "libs";
   private static final String EXTENSION_EXTRACT_ROOT = "beaext";
   private static final String CONSOLE_EXTENSION_EXTRACT_ROOT = "console-ext";
   private static final FileFilter DOT_JAR = FileUtils.makeExtensionFilter(".jar");
   private static final FileFilter WEBAPP_EXTN = FileUtils.makeExtensionFilter(new String[]{".jar", ".war"});
   private static final String WEB_INF_LIB = "/WEB-INF/lib";
   private static String WEB_INF_CLASSES = "/WEB-INF/classes";
   private static final boolean DEBUG = false;
   private static final DebugLogger debugLogger = DebugLogger.getDebugLogger("WarExtraction");
   private final Archive archive;
   private CompositeWebAppFinder classfinder;
   private final String uri;
   private List extensions;
   private List beaExtensionRoots;
   private final ServletMapping virtualFinders;
   private List tldURIs;
   private boolean findTldsCalled;
   private Map tldInfo;
   private List facesConfigURIs;
   private Set<String> matchSet;
   private WebAppConfigManager configManager;
   private File extractDir;
   private boolean isConsoleWar;
   private boolean isConsoleHelpWar;
   public static final ClasspathInfo WAR_CLASSPATH_INFO = new ClasspathInfo() {
      private final String[] WEB_INF_CLASSES;
      private final String[] WEB_INF_LIB;

      {
         this.WEB_INF_CLASSES = new String[]{"WEB-INF" + File.separator + "classes"};
         this.WEB_INF_LIB = new String[]{"WEB-INF" + File.separator + "lib"};
      }

      public String[] getClasspathURIs() {
         return this.WEB_INF_CLASSES;
      }

      public String[] getJarURIs() {
         return this.WEB_INF_LIB;
      }
   };

   public War(String var1, File var2, WebAppServletContext var3) throws IOException {
      this.classfinder = new CompositeWebAppFinder();
      this.extensions = new ArrayList();
      this.beaExtensionRoots = new ArrayList();
      this.virtualFinders = newServletMapping();
      this.findTldsCalled = false;
      this.facesConfigURIs = null;
      this.matchSet = new HashSet();
      ApplicationContextInternal var4 = var3.getApplicationContext();
      this.isConsoleWar = "console".equals(var3.getWebAppModule().getId());
      this.isConsoleHelpWar = "consolehelp".equals(var3.getWebAppModule().getId());
      if (!this.isConsoleHelpWar) {
         this.isConsoleHelpWar = "consolehelp.war".equals(var3.getWebAppModule().getId());
      }

      this.uri = var3.getWebAppModule().getId();
      this.configManager = var3.getConfigManager();
      Ear var5 = var4.getEar();
      this.extractDir = var2;
      File var6 = new File(var2, "war");
      var6.mkdirs();
      File[] var7 = null;
      boolean var8 = this.isConsoleWar && var3.isInternalApp();
      if (var5 == null) {
         File var9 = new File(var4.getStagingPath());
         if (!var9.exists()) {
            throw new FileNotFoundException("Unable to find war for uri " + this.uri + " at path " + var9.getAbsolutePath());
         }

         var7 = new File[]{var9};
         this.archive = makeExplodedJar(this.uri, var6, var7, var8);
      } else {
         var7 = var5.getModuleRoots(var1);
         if (var7.length == 0) {
            throw new FileNotFoundException("Unable to find war with uri " + this.uri + " in ear at " + var4.getStagingPath());
         }

         this.archive = makeExplodedJar(this.uri, var6, var7, var8);
      }

      SplitDirectoryInfo var12 = var4.getSplitDirectoryInfo();
      if (var12 != null) {
         String[] var10 = var12.getWebAppClasses(var1);
         if (var10 != null && var10.length > 0) {
            ClasspathClassFinder2 var11 = new ClasspathClassFinder2(StringUtils.join(var10, File.pathSeparator));
            this.classfinder.addFinder(var11);
            this.classfinder.addFinder(new SplitWebInfoClassesDescriptorFinder(this.uri, var11));
         }
      }

      this.init(var7, var2);
   }

   public War(String var1, File var2, VirtualJarFile var3) throws IOException {
      this(var1, var2, var3, false);
   }

   private War(String var1, File var2, VirtualJarFile var3, boolean var4) throws IOException {
      this.classfinder = new CompositeWebAppFinder();
      this.extensions = new ArrayList();
      this.beaExtensionRoots = new ArrayList();
      this.virtualFinders = newServletMapping();
      this.findTldsCalled = false;
      this.facesConfigURIs = null;
      this.matchSet = new HashSet();
      this.uri = var1;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Creating War uri: " + var1 + " extractDir :" + var2 + " VJFDir: " + var3.getDirectory());
      }

      this.extractDir = var2;
      File var5 = new File(var2, "war");
      var5.mkdirs();
      File[] var6 = var3.getRootFiles();
      this.archive = makeExplodedJar(var1, var5, var6, var4);
      this.init(var6, var2);
   }

   public War(String var1) {
      this.classfinder = new CompositeWebAppFinder();
      this.extensions = new ArrayList();
      this.beaExtensionRoots = new ArrayList();
      this.virtualFinders = newServletMapping();
      this.findTldsCalled = false;
      this.facesConfigURIs = null;
      this.matchSet = new HashSet();
      this.uri = var1;
      this.archive = new NoOpArchive();
      this.virtualFinders.put("/", new ResourceFinder(this.getURI() + "#", this.classfinder, this.configManager));
   }

   private void init(File[] var1, File var2) throws IOException {
      this.classfinder.addFinder(this.archive.getClassFinder());

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3] != null) {
            this.classfinder.addFinder(new ManifestFinder.ClassPathFinder(var1[var3]));
         }
      }

      this.processExtensions(var2);
      this.virtualFinders.put("/", new ResourceFinder(this.getURI() + "#", this.classfinder, this.configManager));
   }

   public List getBeaExtensionRoots() {
      return this.beaExtensionRoots;
   }

   boolean hasExtensions() {
      return !this.beaExtensionRoots.isEmpty();
   }

   public ClassFinder getClassFinder() {
      return this.classfinder;
   }

   public void remove() {
      if (this.extensions.size() > 0) {
         Iterator var1 = this.extensions.iterator();

         while(var1.hasNext()) {
            WarExtension var2 = (WarExtension)var1.next();
            var2.remove();
         }

         this.extensions.clear();
      }

      if (this.beaExtensionRoots.size() > 0) {
         this.beaExtensionRoots.clear();
      }

      this.archive.remove();
   }

   public String getURI() {
      return this.uri;
   }

   private List getWebTLDLocations() {
      if (!this.findTldsCalled) {
         ResourceFinder var1 = ((ResourceFinder)this.getResourceFinder("")).getWebResourceFinder();
         ArrayList var2 = new ArrayList();
         findTlds(var1, var2, this.classfinder.getWebappFinder());
         this.tldURIs = var2;
         this.findTldsCalled = true;
      }

      return this.tldURIs;
   }

   private List getWebFacesLocations(String var1) {
      if (this.facesConfigURIs == null) {
         ResourceFinder var2 = ((ResourceFinder)this.getResourceFinder("")).getWebResourceFinder();
         this.facesConfigURIs = findFacesConfigs(var1, var2, this.classfinder.getWebappFinder());
      }

      return this.facesConfigURIs;
   }

   public synchronized void addLibrary(final Library var1, File var2) throws IOException {
      File var3 = computeExtractDir(var2, PathUtils.generateTempPath((String)null, var1.getName(), var1.getSpecificationVersion() + var1.getImplementationVersion()), "libs");
      VirtualJarFile var4 = null;

      try {
         var4 = VirtualJarFactory.createVirtualJar(var1.getLocation());
         final War var5;
         if (var1 instanceof WarLibraryDefinition && ((WarLibraryDefinition)var1).isArchived()) {
            var5 = new War(this.getURI(), var3, var4, true);
         } else {
            var5 = new War(this.getURI(), var3, var4);
         }

         final String var6 = var1.getName();
         final LibraryFinder var7 = new LibraryFinder(var5.getClassFinder(), var1);
         this.extensions.add(new WarExtension() {
            private Map tldInfo = null;

            public String getName() {
               return var6;
            }

            public ClassFinder getClassFinder() throws IOException {
               return var7;
            }

            public void remove() {
               var5.remove();
            }

            public Collection getTagListeners(boolean var1x) {
               return (Collection)(var1x ? this.getAnnotatedTagListeners() : this.getTagListeners());
            }

            public Collection getTagHandlers(boolean var1x) {
               return (Collection)(var1x ? this.getAnnotatedTagHandlers() : this.getTagHandlers());
            }

            private Set getTagListeners() {
               if (this.tldInfo == null) {
                  this.getTldInfo();
               }

               return (Set)this.tldInfo.get("listener-class");
            }

            private Set getTagHandlers() {
               if (this.tldInfo == null) {
                  this.getTldInfo();
               }

               return (Set)this.tldInfo.get("tag-class");
            }

            private Collection getAnnotatedTagListeners() {
               CachableLibMetadataEntry var1x = ((CachableLibMetadata)var1).getCachableEntry(CachableLibMetadataType.TAG_LISTENERS);

               try {
                  return (Collection)LibraryMetadataCache.getInstance().lookupCachedObject(var1x);
               } catch (LibraryProcessingException var3) {
                  return Collections.EMPTY_SET;
               }
            }

            private Collection getAnnotatedTagHandlers() {
               CachableLibMetadataEntry var1x = ((CachableLibMetadata)var1).getCachableEntry(CachableLibMetadataType.TAG_HANDLERS);

               try {
                  return (Collection)LibraryMetadataCache.getInstance().lookupCachedObject(var1x);
               } catch (LibraryProcessingException var3) {
                  return Collections.EMPTY_SET;
               }
            }

            private DescriptorCacheEntry.DescriptorCachableObject getTldCachedObject() throws LibraryProcessingException {
               CachableLibMetadataEntry var1x = ((CachableLibMetadata)var1).getCachableEntry(CachableLibMetadataType.TLD);
               return (DescriptorCacheEntry.DescriptorCachableObject)LibraryMetadataCache.getInstance().lookupCachedObject(var1x);
            }

            private void getTldInfo() {
               if (!(var1 instanceof CachableLibMetadata)) {
                  this.tldInfo = Collections.EMPTY_MAP;
               }

               if (this.tldInfo == null) {
                  try {
                     DescriptorCacheEntry.DescriptorCachableObject var1x = this.getTldCachedObject();
                     this.tldInfo = TldCacheHelper.parseTagLibraries(var1x.getResourceLocation(), (File)var1x.getCacheBaseDir(), var6);
                  } catch (LibraryProcessingException var2) {
                  }
               }

            }

            private DescriptorCacheEntry.DescriptorCachableObject getFacesConfigCachedObject() throws LibraryProcessingException {
               CachableLibMetadataEntry var1x = ((CachableLibMetadata)var1).getCachableEntry(CachableLibMetadataType.FACE_BEANS);
               return (DescriptorCacheEntry.DescriptorCachableObject)LibraryMetadataCache.getInstance().lookupCachedObject(var1x);
            }

            public Set getFacesManagedBeans() {
               if (!(var1 instanceof CachableLibMetadata)) {
                  return Collections.EMPTY_SET;
               } else {
                  Set var1x = null;

                  try {
                     DescriptorCacheEntry.DescriptorCachableObject var2 = this.getFacesConfigCachedObject();
                     var1x = FaceConfigCacheHelper.parseFacesConfigs(var2.getResourceLocation(), (File)var2.getCacheBaseDir(), var6);
                  } catch (LibraryProcessingException var3) {
                  }

                  return var1x == null ? Collections.EMPTY_SET : var1x;
               }
            }

            public List getAnnotatedClasses() {
               if (!(var1 instanceof CachableLibMetadata)) {
                  return Collections.EMPTY_LIST;
               } else {
                  List var1x = null;

                  try {
                     CachableLibMetadataEntry var2 = ((CachableLibMetadata)var1).getCachableEntry(CachableLibMetadataType.ANNOTATED_CLASSES);
                     var1x = (List)LibraryMetadataCache.getInstance().lookupCachedObject(var2);
                  } catch (LibraryProcessingException var3) {
                  }

                  return var1x == null ? Collections.EMPTY_LIST : var1x;
               }
            }
         });
         this.classfinder.addLibraryFinder(var7);
      } finally {
         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var14) {
            }
         }

      }

   }

   public void addVirtualDirectory(String var1, String var2) {
      DescriptorFinder var3 = new DescriptorFinder(this.getURI(), new ClasspathClassFinder2(var1));
      MultiClassFinder var4 = new MultiClassFinder(var3);
      var4.addFinder(this.classfinder);
      this.virtualFinders.put(var2, new ResourceFinder(this.getURI() + "#", var4, this.configManager));
   }

   public ClassFinder getResourceFinder(String var1) {
      var1 = HttpParsing.ensureStartingSlash(var1);
      return (ClassFinder)this.virtualFinders.get(var1);
   }

   WarSource getResourceAsSource(String var1, boolean var2) {
      ResourceFinder var3 = (ResourceFinder)this.getResourceFinder(var1);
      return var2 ? var3.getSource(var1, var2) : (WarSource)var3.getSource(var1);
   }

   void getResourcePaths(String var1, Set var2) {
      Enumeration var3 = this.getResourceFinder(var1).getSources(var1);

      while(true) {
         WarSource[] var6;
         do {
            if (!var3.hasMoreElements()) {
               return;
            }

            Source var4 = (Source)var3.nextElement();
            WarSource var5 = new WarSource(var4);
            var6 = var5.listSources();
         } while(var6 == null);

         for(int var7 = 0; var7 < var6.length; ++var7) {
            String var8 = var1 + var6[var7].getName();
            if (var6[var7].isDirectory()) {
               var8 = var8 + "/";
            }

            var2.add(var8);
         }
      }
   }

   synchronized void addClassPath(String var1) {
      this.classfinder.addFinder(new ClasspathClassFinder2(var1));
   }

   public void closeAllFinders() {
      this.findTldsCalled = false;
      Object[] var1 = this.virtualFinders.values();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         ((ClassFinder)var1[var2]).close();
      }

      if (this.classfinder != null) {
         this.classfinder.close();
      }

   }

   void populateJarMap(Map var1) {
      ClassFinder var2 = this.getResourceFinder("/");
      Enumeration var3 = var2.getSources("/WEB-INF/lib/");

      while(true) {
         File[] var8;
         do {
            File var7;
            do {
               do {
                  URL var5;
                  String var6;
                  do {
                     if (!var3.hasMoreElements()) {
                        return;
                     }

                     Source var4 = (Source)var3.nextElement();
                     var5 = var4.getURL();
                     var6 = var5.getProtocol();
                  } while(!"file".equals(var6));

                  var7 = new File(var5.getPath());
               } while(!var7.isDirectory());
            } while(!var7.exists());

            var8 = var7.listFiles(DOT_JAR);
         } while(var8 == null);

         for(int var9 = 0; var9 < var8.length; ++var9) {
            String var10 = var8[var9].getName();
            var1.put(var10, var8[var9].getAbsolutePath());
         }
      }
   }

   public static void findTlds(ClassFinder var0, List var1, ClassFinder var2) {
      Enumeration var3 = var0.getSources("/WEB-INF/");

      File var7;
      while(var3.hasMoreElements()) {
         Source var4 = (Source)var3.nextElement();
         URL var5 = var4.getURL();
         String var6 = var5.getProtocol();
         if ("file".equals(var6)) {
            var7 = new File(var5.getPath());
            if (var7.isDirectory()) {
               findTLDs(var7, "/WEB-INF", var1);
            }
         }
      }

      String var10 = var2.getClassPath();
      List var11 = getWebInfLibJarFiles(var10, true);
      Iterator var12 = var11.iterator();

      while(var12.hasNext()) {
         var7 = (File)var12.next();
         long var8 = 0L;
         if (debugLogger.isDebugEnabled()) {
            var8 = System.currentTimeMillis();
         }

         findJarTLD(var7, var1);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("**** Processing WEB-INF/lib jar -> :" + var7.getName() + " Elapsed: " + (System.currentTimeMillis() - var8));
         }
      }

   }

   public static List findFacesConfigs(String var0, ClassFinder var1, ClassFinder var2) {
      List var3 = null;
      List var4 = findMetaInfFacesConfigs(var2);
      var3 = addAllIfNotEmpty((List)var3, var4);
      List var5 = findFacesConfigFiles(var0, var1);
      Object var7 = addAllIfNotEmpty((List)var3, var5);
      ResourceLocation var6 = findDefaultFacesConfig(var1);
      if (var6 != null) {
         if (var7 == null) {
            var7 = new ArrayList();
         }

         ((List)var7).add(var6);
      }

      return (List)(var7 == null ? Collections.EMPTY_LIST : var7);
   }

   private static void findTLDs(File var0, String var1, List var2) {
      String[] var3 = var0.list();
      if (var3 != null && var3.length != 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            File var5 = new File(var0, var3[var4]);
            if (var5.isDirectory()) {
               findTLDs(var5, var1 + '/' + var3[var4], var2);
            } else if (var5.isFile() && var3[var4].endsWith(".tld")) {
               var2.add(new ResourceLocation(var5, var1 + '/' + var3[var4], 1));
            }
         }

      }
   }

   private static void findJarTLD(File var0, List var1) {
      JarFile var2 = null;

      try {
         var2 = new JarFile(var0);
         Enumeration var3 = var2.entries();

         while(var3.hasMoreElements()) {
            JarEntry var4 = (JarEntry)var3.nextElement();
            String var5 = var4.getName();
            if (var5.endsWith(".tld")) {
               var1.add(new JarResourceLocation(var0, var5, 1));
            }
         }
      } catch (IOException var15) {
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var14) {
            }
         }

      }

   }

   private static List findMetaInfFacesConfigs(ClassFinder var0) {
      ArrayList var1 = null;
      List var2 = getWebInfLibJarFiles(var0.getClassPath(), false);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         File var4 = (File)var3.next();
         JarFile var5 = null;

         try {
            var5 = new JarFile(var4);
            JarEntry var6 = var5.getJarEntry("META-INF/faces-config.xml");
            if (var6 != null) {
               if (var1 == null) {
                  var1 = new ArrayList();
               }

               var1.add(new JarResourceLocation(var4, "META-INF/faces-config.xml", 2));
            }
         } catch (IOException var16) {
         } finally {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (IOException var15) {
               }
            }

         }
      }

      return var1;
   }

   private static List findFacesConfigFiles(String var0, ClassFinder var1) {
      if (var0 == null) {
         return null;
      } else {
         var0 = var0.trim();
         if (var0.length() == 0) {
            return null;
         } else {
            String[] var2 = var0.split(",");
            ArrayList var3 = null;

            for(int var4 = 0; var4 < var2.length; ++var4) {
               if (!"/WEB-INF/faces-config.xml".equals(var2[var4])) {
                  if (!var2[var4].startsWith("/")) {
                     var2[var4] = "/" + var2[var4];
                  }

                  Source var5 = var1.getSource(var2[var4]);
                  if (var5 != null) {
                     URL var6 = var5.getURL();
                     String var7 = var6.getProtocol();
                     String var8 = var6.getFile();
                     if ("file".equals(var7)) {
                        if (var3 == null) {
                           var3 = new ArrayList();
                        }

                        var3.add(new ResourceLocation(new File(var8), var2[var4], 2));
                     }
                  }
               }
            }

            return var3;
         }
      }
   }

   private static ResourceLocation findDefaultFacesConfig(ClassFinder var0) {
      Source var1 = var0.getSource("/WEB-INF/faces-config.xml");
      if (var1 == null) {
         return null;
      } else {
         URL var2 = var1.getURL();
         String var3 = var2.getProtocol();
         if (!"file".equals(var3)) {
            return null;
         } else {
            String var4 = var2.getFile();
            return new ResourceLocation(new File(var4), "/WEB-INF/faces-config.xml", 2);
         }
      }
   }

   private static List getWebInfLibJarFiles(String var0, boolean var1) {
      if (var0 == null) {
         return Collections.EMPTY_LIST;
      } else {
         String[] var2 = var0.split(File.pathSeparator);
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].endsWith(".jar") && (!var1 || !var2[var4].endsWith("_wl_cls_gen.jar"))) {
               File var5 = new File(var2[var4]);
               if (var2[var4].replace(File.separatorChar, '/').endsWith("WEB-INF/lib/" + var5.getName())) {
                  var3.add(var5);
               }
            }
         }

         return var3;
      }
   }

   private void processExtensions(File var1) throws IOException {
      if (this.isConsoleWar || this.isConsoleHelpWar) {
         long var2 = 0L;
         long var4 = 0L;
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("**** processExtensions, finding console extensions");
            var2 = System.currentTimeMillis();
         }

         ConsoleExtensionManager var6 = ConsoleExtensionManager.getInstance();
         ConsoleExtensionManager.ExtensionDef[] var7 = var6.findExtensions();
         if (debugLogger.isDebugEnabled()) {
            var4 = System.currentTimeMillis();
         }

         if (var7.length > 0) {
            for(int var8 = 0; var8 < var7.length; ++var8) {
               ConsoleExtensionManager.ExtensionDef var9 = var7[var8];
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("**** processExtensions, found extension: " + var9);
               }

               if (var6.shouldIncludeExtension(this.uri, var9)) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("**** processExtensions, archive will be loaded");
                  }

                  File var10 = var9.getFile();
                  File var11 = computeExtractDir(var1, var10, "console-ext");
                  final ArchivedWar var12 = new ArchivedWar(this.uri, var11, var10, WAR_CLASSPATH_INFO);
                  final String var13 = var10.getName();
                  this.extensions.add(new WarExtension() {
                     public String getName() {
                        return var13;
                     }

                     public ClassFinder getClassFinder() throws IOException {
                        return var12.getClassFinder();
                     }

                     public Collection getTagListeners(boolean var1) {
                        return Collections.EMPTY_SET;
                     }

                     public Collection getTagHandlers(boolean var1) {
                        return Collections.EMPTY_SET;
                     }

                     public List getAnnotatedClasses() {
                        return Collections.EMPTY_LIST;
                     }

                     public Set getFacesManagedBeans() {
                        return Collections.EMPTY_SET;
                     }

                     public void remove() {
                        var12.remove();
                     }
                  });
                  this.beaExtensionRoots.addAll(Arrays.asList(var12.getDirs()));
               }
            }
         } else if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("**** processExtensions, No console extensions configured");
         }

         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("**** processExtensions, console extension execution time(ms):" + (var4 - var2));
         }

         var6.release();
      }

      Enumeration var14 = this.classfinder.getSources(this.uri + "#" + "/WEB-INF/bea-ext");

      while(var14.hasMoreElements()) {
         Source var3 = (Source)var14.nextElement();
         URL var16 = var3.getURL();
         if (!"file".equals(var16.getProtocol())) {
            throw new AssertionError("Unknown protocol " + var16.getProtocol());
         }

         String var5 = var16.getPath();
         File var19 = new File(var5);
         if (var19.isDirectory()) {
            this.processExtensionsDir(var19, "beaext", var1);
         }
      }

      if (this.extensions.size() > 0) {
         MultiClassFinder var15 = new MultiClassFinder();

         WarExtension var18;
         for(Iterator var17 = this.extensions.iterator(); var17.hasNext(); var15.addFinder(var18.getClassFinder())) {
            var18 = (WarExtension)var17.next();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Prepending extension: " + var18.getName());
            }
         }

         this.classfinder.addFinderFirst(var15);
      }

   }

   private void processExtensionsDir(File var1, String var2, File var3) throws IOException {
      File[] var4 = var1.listFiles(WEBAPP_EXTN);

      for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
         File var6 = computeExtractDir(var3, var4[var5], var2);
         final ArchivedWar var7 = new ArchivedWar(this.uri, var6, var4[var5], WAR_CLASSPATH_INFO);
         final String var8 = var4[var5].getName();
         this.extensions.add(new WarExtension() {
            public String getName() {
               return var8;
            }

            public ClassFinder getClassFinder() throws IOException {
               return var7.getClassFinder();
            }

            public Collection getTagListeners(boolean var1) {
               return Collections.EMPTY_SET;
            }

            public Collection getTagHandlers(boolean var1) {
               return Collections.EMPTY_SET;
            }

            public List getAnnotatedClasses() {
               return Collections.EMPTY_LIST;
            }

            public Set getFacesManagedBeans() {
               return Collections.EMPTY_SET;
            }

            public void remove() {
               var7.remove();
            }
         });
         this.beaExtensionRoots.addAll(Arrays.asList(var7.getDirs()));
      }

   }

   private Map getWebTldInfo() {
      if (this.tldInfo == null) {
         List var1 = this.getWebTLDLocations();
         this.tldInfo = TldCacheHelper.parseTagLibraries(var1, (File)this.extractDir, this.uri);
      }

      return this.tldInfo;
   }

   public Set getTagClasses(boolean var1, String var2) {
      Set var3 = null;
      Map var4 = this.getWebTldInfo();
      ClassFinder var5 = this.classfinder.getWebappFinder();
      Collection var6 = getWebTagClasses(var5, var4, var1, var2);
      var3 = addAllIfNotEmpty(var3, var6);
      Set var7 = this.getLibTagClasses(var1, var2);
      var3 = addAllIfNotEmpty((Set)var3, var7);
      return var3 == null ? Collections.EMPTY_SET : var3;
   }

   public static Collection getWebTagClasses(ClassFinder var0, Map var1, boolean var2, String var3) {
      Object var4 = null;
      Collection var5 = (Collection)var1.get(var3);
      if (var2) {
         if (var5 != null && !var5.isEmpty()) {
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               Object var7 = var6.next();
               String var8 = (String)var7;
               if (ClassAnnotationDetector.isClassHasAnnotation(var0, var8)) {
                  if (var4 == null) {
                     var4 = new ArraySet();
                  }

                  ((Collection)var4).add(var8);
               }
            }
         }
      } else {
         var4 = var5;
      }

      return (Collection)var4;
   }

   public Set getLibTagClasses(boolean var1, String var2) {
      Set var3 = null;

      Collection var6;
      for(Iterator var4 = this.extensions.iterator(); var4.hasNext(); var3 = addAllIfNotEmpty(var3, var6)) {
         WarExtension var5 = (WarExtension)var4.next();
         var6 = null;
         if (var2.equals("listener-class")) {
            var6 = var5.getTagListeners(var1);
         } else if (var2.equals("tag-class")) {
            var6 = var5.getTagHandlers(var1);
         }
      }

      return var3 == null ? Collections.EMPTY_SET : var3;
   }

   public Set getFacesManagedBeans(String var1, File var2) {
      List var3 = this.getWebFacesLocations(var1);
      Set var4 = null;
      Set var5 = FaceConfigCacheHelper.parseFacesConfigs(var3, (File)var2, this.uri);
      var4 = addAllIfNotEmpty((Set)var4, var5);
      Set var6 = this.getLibManagedBeans();
      var4 = addAllIfNotEmpty((Set)var4, var6);
      return var4 == null ? Collections.EMPTY_SET : var4;
   }

   public Set getLibManagedBeans() {
      Set var1 = null;

      Set var4;
      for(Iterator var2 = this.extensions.iterator(); var2.hasNext(); var1 = addAllIfNotEmpty((Set)var1, var4)) {
         WarExtension var3 = (WarExtension)var2.next();
         var4 = var3.getFacesManagedBeans();
      }

      return var1 == null ? Collections.EMPTY_SET : var1;
   }

   public List getAnnotatedClasses(WebAnnotationProcessor var1) {
      List var2 = null;
      ClassFinder var3 = this.classfinder.getWebappFinder();
      List var4 = var1.getAnnotatedClasses(var3);
      var2 = addAllIfNotEmpty((List)var2, var4);

      List var7;
      for(Iterator var5 = this.extensions.iterator(); var5.hasNext(); var2 = addAllIfNotEmpty((List)var2, var7)) {
         WarExtension var6 = (WarExtension)var5.next();
         var7 = var6.getAnnotatedClasses();
      }

      return var2 == null ? Collections.EMPTY_LIST : var2;
   }

   public static Set addAllIfNotEmpty(Set var0, Collection var1) {
      Object var2 = var0;
      if (var1 != null && !var1.isEmpty()) {
         if (var0 == null) {
            var2 = new ArraySet();
         }

         ((Set)var2).addAll(var1);
      }

      return (Set)var2;
   }

   public static List addAllIfNotEmpty(List var0, Collection var1) {
      Object var2 = var0;
      if (var1 != null && !var1.isEmpty()) {
         if (var0 == null) {
            var2 = new ArrayList();
         }

         ((List)var2).addAll(var1);
      }

      return (List)var2;
   }

   static ExplodedJar makeExplodedJar(String var0, File var1, File[] var2, boolean var3) throws IOException {
      long var4 = 0L;

      try {
         if (var2.length == 1 && !var2[0].isDirectory()) {
            ArchivedWar var11 = new ArchivedWar(var0, var1, var2[0], WAR_CLASSPATH_INFO);
            return var11;
         } else {
            CaseAwareExplodedJar var6 = new CaseAwareExplodedJar(var0, var1, var2, WAR_CLASSPATH_INFO, var3 ? JarCopyFilter.NOCOPY_FILTER : JarCopyFilter.DEFAULT_FILTER);
            return var6;
         }
      } finally {
         ;
      }
   }

   private static ServletMapping newServletMapping() {
      return !Kernel.isServer() ? new ServletMapping() : new ServletMapping(WebAppConfigManager.isCaseInsensitive(), WebAppSecurity.getEnforceStrictURLPattern());
   }

   private static File computeExtractDir(File var0, File var1, String var2) {
      String var3 = var1.getName();
      if (var3.lastIndexOf(46) > 0) {
         var3 = var3.substring(0, var3.lastIndexOf(46));
      }

      return computeExtractDir(var0, var3, var2);
   }

   private static File computeExtractDir(File var0, String var1, String var2) {
      File var3 = new File(var0, var2 + File.separator + var1);
      return var3;
   }

   public void setVirtualMappingPaths(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (var3.endsWith(File.separator)) {
            this.matchSet.add(var3.substring(0, var3.length() - 1));
         } else {
            this.matchSet.add(var3);
         }
      }

   }

   public boolean isKnownVirtualMappingUri(String var1) {
      if (!this.matchSet.isEmpty() && var1 != null) {
         var1 = FileUtils.normalize(var1);

         while(var1.length() > 0) {
            boolean var2 = this.matchSet.contains(var1);
            if (var2) {
               return true;
            }

            int var3 = var1.lastIndexOf(File.separatorChar);
            if (var3 > 0) {
               var1 = var1.substring(0, var3);
            } else {
               var1 = "";
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static class SplitWebInfoClassesDescriptorFinder extends DescriptorFinder {
      private String prefix;
      private ClassFinder delegate;

      public SplitWebInfoClassesDescriptorFinder(String var1, ClassFinder var2) throws IOException {
         super(var1, var2);
         this.prefix = var1 + "#" + War.WEB_INF_CLASSES;
         this.delegate = var2;
      }

      public Source getSource(String var1) {
         return var1 != null && var1.startsWith(this.prefix) ? this.delegate.getSource(this.removePrefix(var1)) : null;
      }

      public Enumeration getSources(String var1) {
         return (Enumeration)(var1 != null && var1.startsWith(this.prefix) ? this.delegate.getSources(this.removePrefix(var1)) : new EmptyEnumerator());
      }

      private String removePrefix(String var1) {
         return var1.substring(this.prefix.length(), var1.length());
      }
   }

   private static class LibraryFinder extends DelegateFinder {
      private final boolean fromArchive;

      public LibraryFinder(ClassFinder var1, Library var2) {
         super(var1);
         boolean var3 = false;
         if (var2 instanceof WarLibraryDefinition) {
            var3 = ((WarLibraryDefinition)var2).isArchived();
         }

         this.fromArchive = var3;
      }

      public Source getSource(String var1) {
         Source var2 = super.getSource(var1);
         return var2 == null ? null : new LibrarySource(var2, this.fromArchive);
      }

      public Enumeration getSources(String var1) {
         Enumeration var2 = super.getSources(var1);
         ArrayList var3 = null;
         if (var2 != null) {
            var3 = new ArrayList();

            while(var2.hasMoreElements()) {
               Source var4 = (Source)var2.nextElement();
               var3.add(new LibrarySource(var4, this.fromArchive));
            }
         }

         return (Enumeration)(var3 == null ? new EmptyEnumerator() : new IteratorEnumerator(var3.iterator()));
      }
   }

   public static class LibrarySource extends DelegateSource {
      private boolean fromArchive;

      public LibrarySource(Source var1, boolean var2) {
         super(var1);
         this.fromArchive = var2;
      }

      public boolean isFromArchive() {
         return this.fromArchive;
      }
   }

   static final class JarResourceLocation extends ResourceLocation {
      private String fragment;

      JarResourceLocation(File var1, String var2, int var3) {
         super(var1, var1.getName() + '/' + var2, var3);
         this.fragment = var2;
      }

      InputStream getInputStream() {
         JarFile var1 = null;

         try {
            var1 = new JarFile(this.location);
            JarEntry var2 = (JarEntry)var1.getEntry(this.fragment);
            if (var2 != null) {
               return new SafeZipFileInputStream(var1, var2);
            }
         } catch (IOException var5) {
            if (HTTPDebugLogger.isEnabled()) {
               if (this.type == 1) {
                  HTTPDebugLogger.debug("[War] Unable to find tld at location: " + this.getLocation());
               } else if (this.type == 2) {
                  HTTPDebugLogger.debug("[War] Unable to find faces config file at location: " + this.getLocation());
               }
            }

            if (var1 != null) {
               try {
                  var1.close();
               } catch (IOException var4) {
               }
            }
         }

         return null;
      }

      String getLocation() {
         return this.location.getAbsolutePath() + "!" + (this.fragment.endsWith("/") ? this.fragment : "/" + this.fragment);
      }
   }

   static class ResourceLocation implements Serializable {
      static final int TYPE_TLD = 1;
      static final int TYPE_FACES_CONFIG = 2;
      protected final int type;
      protected final File location;
      private final String uri;

      ResourceLocation(File var1, String var2, int var3) {
         this.location = var1;
         this.uri = var2;
         this.type = var3;
      }

      InputStream getInputStream() throws IOException {
         return new FileInputStream(this.location);
      }

      String getLocation() {
         return this.location.getAbsolutePath();
      }

      String getURI() {
         return this.uri;
      }

      int getType() {
         return this.type;
      }
   }

   static final class ResourceFinder implements ClassFinder {
      private static final NullSource NULL = new NullSource();
      private final SecondChanceCacheMap cache;
      private ClassFinder delegate;
      private ResourceFinder webappResourceFinder;
      private String prefix;
      private String classpath;
      private WebAppConfigManager configManager;

      ResourceFinder(String var1, ClassFinder var2) {
         this(var1, var2, (WebAppConfigManager)null);
      }

      ResourceFinder(String var1, ClassFinder var2, WebAppConfigManager var3) {
         this.cache = new SecondChanceCacheMap(317);
         this.prefix = var1;
         this.delegate = var2;
         this.configManager = var3;
      }

      public ResourceFinder getWebResourceFinder() {
         if (this.delegate instanceof CompositeWebAppFinder) {
            if (this.webappResourceFinder == null) {
               this.webappResourceFinder = new ResourceFinder(this.prefix, ((CompositeWebAppFinder)this.delegate).getWebappFinder(), this.configManager);
            }

            return this.webappResourceFinder;
         } else {
            return this;
         }
      }

      public Source getSource(String var1) {
         WarSource var2 = (WarSource)this.cache.get(this.prefix + var1);
         int var3 = -1;
         if (this.configManager != null) {
            var3 = this.configManager.getResourceReloadCheckSecs() * 1000;
         }

         if (var2 == null || var3 >= 0 && System.currentTimeMillis() - (long)var3 > var2.getLastChecked()) {
            var2 = this.getSource(var1, true);
         }

         return var2.delegate == NULL ? null : var2;
      }

      private WarSource getSource(String var1, boolean var2) {
         WarSource var3 = null;
         if (var2) {
            Object var4 = this.delegate.getSource(this.prefix + var1);
            if (var4 == null) {
               var4 = NULL;
            }

            var3 = new WarSource((Source)var4);
            this.cache.put(this.prefix + var1, var3);
         }

         return var3;
      }

      public Enumeration getSources(String var1) {
         Enumeration var2 = this.delegate.getSources(this.prefix + var1);
         ArrayList var3 = null;
         if (var2 != null) {
            var3 = new ArrayList();

            while(var2.hasMoreElements()) {
               var3.add(new WarSource((Source)var2.nextElement()));
            }
         }

         return (Enumeration)(var3 == null ? new EmptyEnumerator() : new IteratorEnumerator(var3.iterator()));
      }

      public Source getClassSource(String var1) {
         return this.delegate.getClassSource(var1);
      }

      public String getClassPath() {
         if (this.classpath != null) {
            return this.classpath;
         } else {
            String var1 = null;
            Enumeration var2 = this.delegate.getSources(this.prefix + "/");

            while(var2.hasMoreElements()) {
               Source var3 = (Source)var2.nextElement();
               URL var4 = var3.getURL();
               if (var4 != null) {
                  if (var1 == null) {
                     var1 = var4.getPath() + File.pathSeparator;
                  } else {
                     var1 = var1 + var4.getPath() + File.pathSeparator;
                  }
               }
            }

            this.classpath = var1;
            return this.classpath;
         }
      }

      public ClassFinder getManifestFinder() {
         return this.delegate.getManifestFinder();
      }

      public Enumeration entries() {
         return this.delegate.entries();
      }

      public void clearCache(String var1) {
         if (!var1.startsWith("/")) {
            var1 = "/" + var1;
         }

         this.cache.remove(this.prefix + var1);
      }

      public void close() {
         this.classpath = null;
         this.delegate.close();
      }
   }

   private static class NoOpArchive extends Archive {
      private NoOpArchive() {
      }

      public ClassFinder getClassFinder() throws IOException {
         return NullClassFinder.NULL_FINDER;
      }

      public void remove() {
      }

      // $FF: synthetic method
      NoOpArchive(Object var1) {
         this();
      }
   }

   interface WarExtension {
      String getName();

      ClassFinder getClassFinder() throws IOException;

      Collection getTagListeners(boolean var1);

      Collection getTagHandlers(boolean var1);

      List getAnnotatedClasses();

      Set getFacesManagedBeans();

      void remove();
   }
}
