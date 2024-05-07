package weblogic.deploy.service.datatransferhandlers;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import weblogic.application.utils.EarUtils;
import weblogic.deploy.common.Debug;
import weblogic.deploy.common.DeploymentConstants;
import weblogic.deploy.service.FileDataStream;
import weblogic.deploy.service.MultiDataStream;
import weblogic.j2ee.J2EEUtils;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class SourceCache implements DeploymentConstants {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String CONFIG_DIR_PREFIX;
   private String appName = null;
   private File tempDirectory;
   private File rootDirectory;
   private String sourcePath = null;
   private String planPath = null;
   private String planDir = null;
   private String altDDPath = null;
   private String altWLDDPath = null;
   private boolean isSrcArchive = false;
   private boolean isSystemResource = false;
   private boolean isStandaloneModule = false;
   private HashMap jarCache = new HashMap();
   private static HashMap sourceCacheMap;

   public SourceCache(BasicDeploymentMBean var1, File var2) throws IOException {
      this.appName = var1.getName();
      this.tempDirectory = new File(var2, "_WL_TEMP_APP_DOWNLOADS" + File.separator + this.appName);
      boolean var3 = this.tempDirectory.exists();
      if (!var3) {
         var3 = this.tempDirectory.mkdirs();
      }

      if (!var3) {
         throw new IOException("Could not create temp location for application : " + this.appName);
      } else {
         if (var1 instanceof AppDeploymentMBean) {
            AppDeploymentMBean var4 = (AppDeploymentMBean)var1;
            this.sourcePath = var4.getAbsoluteSourcePath();
            this.rootDirectory = new File(this.sourcePath);
            this.planPath = var4.getAbsolutePlanPath();
            this.planDir = var4.getAbsolutePlanDir();
            this.altDDPath = var4.getAltDescriptorPath();
            this.altWLDDPath = var4.getAltWLSDescriptorPath();
            File var5 = new File(this.sourcePath);
            if (!var5.exists()) {
               throw new IOException("Invalid source path : " + var5.getAbsolutePath());
            }

            if (var5.isFile()) {
               if (J2EEUtils.isValidArchiveName(var5.getName())) {
                  this.isSrcArchive = true;
               } else if (J2EEUtils.isValidWLSModuleName(var5.getName())) {
                  this.isStandaloneModule = true;
               }
            }
         } else {
            this.sourcePath = CONFIG_DIR_PREFIX + ((SystemResourceMBean)var1).getDescriptorFileName();
            this.rootDirectory = new File(DomainDir.getConfigDir());
            this.isSystemResource = true;
         }

      }
   }

   public MultiDataStream getDataLocations(List var1, boolean var2, BasicDeploymentMBean var3) throws IOException {
      return this.isSystemResource ? this.getSystemResourceDataLocations(var1) : this.getAppDataLocations(var1, var2, (AppDeploymentMBean)var3);
   }

   public MultiDataStream getAppDataLocationsForModuleIds(List var1, AppDeploymentMBean var2) throws IOException {
      if (var1 != null && !var1.isEmpty()) {
         if (this.isSrcArchive) {
            String var10 = DeployerRuntimeLogger.logPartialRedeployOfArchiveLoggable(this.appName).getMessage();
            throw new IOException(var10);
         } else {
            String[] var3 = new String[var1.size()];
            var3 = (String[])((String[])var1.toArray(var3));
            File[] var4 = EarUtils.getAppFiles(var3, var2);
            if (var4 != null && var4.length != 0) {
               MultiDataStream var5 = DataStreamFactory.createMultiDataStream();
               ArrayList var6 = new ArrayList();

               for(int var7 = 0; var7 < var4.length; ++var7) {
                  if (verifyIfFileIsIn(var4[var7], this.rootDirectory)) {
                     String var8 = getFilePathRelativeTo(var4[var7], this.rootDirectory);
                     File var9 = new File(this.rootDirectory, var8);
                     if (var9.isDirectory()) {
                        var5.addFileDataStream(var8, new File(this.getOrCreateJarForURI(var8)), true);
                     } else {
                        var5.addFileDataStream(var8, var9, J2EEUtils.isValidArchiveName(var9.getName()));
                     }
                  } else {
                     var6.add(var4[var7]);
                  }
               }

               if (!var6.isEmpty()) {
                  var5.addFileDataStream(this.getTempDescsFilePath(), true);
               }

               return var5;
            } else {
               throw new IOException("Container did not find file locations for moduleIds '" + var1 + "'");
            }
         }
      } else {
         throw new IOException("Empty ModuleIds list");
      }
   }

   public String toString() {
      return "SourceCache(" + this.appName + ")";
   }

   public void close() {
      synchronized(this.jarCache) {
         Iterator var2 = this.jarCache.values().iterator();
         if (var2 != null && var2.hasNext()) {
            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               if (var3 != null) {
                  File var4 = new File(var3);
                  if (var4.exists()) {
                     var4.delete();
                  }
               }
            }

            this.jarCache.clear();
         }
      }
   }

   public static void invalidateCache(BasicDeploymentMBean var0) {
      SourceCache var1 = null;
      synchronized(sourceCacheMap) {
         var1 = (SourceCache)sourceCacheMap.remove(var0.getName());
      }

      if (var1 != null) {
         var1.close();
      }

   }

   public static void updateDescriptorsInCache(AppDeploymentMBean var0) {
      SourceCache var1 = null;
      synchronized(sourceCacheMap) {
         var1 = (SourceCache)sourceCacheMap.get(var0.getName());
      }

      if (var1 != null) {
         var1.updateDescriptors(var0);
      }

   }

   static SourceCache getSourceCache(BasicDeploymentMBean var0) throws IOException {
      synchronized(sourceCacheMap) {
         SourceCache var2 = (SourceCache)sourceCacheMap.get(var0.getName());
         if (var2 == null) {
            if (isDebugEnabled()) {
               debugSay("Creating new source cache for " + var0);
            }

            File var3 = new File(DomainDir.getTempDirForServer(ManagementService.getPropertyService(kernelId).getServerName()));
            var2 = new SourceCache(var0, var3);
            sourceCacheMap.put(var0.getName(), var2);
         }

         return var2;
      }
   }

   private MultiDataStream getAppDataLocations(List var1, boolean var2, AppDeploymentMBean var3) throws IOException {
      boolean var4 = var1 == null || var1.isEmpty() || this.isStandaloneModule;
      MultiDataStream var5 = DataStreamFactory.createMultiDataStream();
      if (var4) {
         var5.addDataStream(this.getTempSrcLocation());
         String var10 = this.getTempDescsFilePath();
         if (var10 != null) {
            var5.addFileDataStream(var10, true);
         }

         return var5;
      } else {
         if (isDebugEnabled()) {
            debugSay(" +++ isPlanUpdate: " + var2);
            debugSay(" +++ uris passed : " + var1);
         }

         if (var2) {
            var5.addFileDataStream(this.getTempDescsFilePath(), true);
            return var5;
         } else {
            String[] var6 = new String[var1.size()];
            var6 = (String[])((String[])var1.toArray(var6));
            validateDelta(var6, var3);
            Iterator var7 = var1.iterator();

            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               File var9 = new File(this.rootDirectory, var8);
               if (var9.isDirectory()) {
                  var5.addFileDataStream(var8, new File(this.getOrCreateJarForURI(var8)), true);
               } else {
                  var5.addFileDataStream(var8, var9, J2EEUtils.isValidArchiveName(var9.getName()));
               }
            }

            return var5;
         }
      }
   }

   private static boolean isDebugEnabled() {
      return Debug.isServiceHttpDebugEnabled();
   }

   private static void debugSay(String var0) {
      Debug.serviceHttpDebug(" +++ " + var0);
   }

   private MultiDataStream getSystemResourceDataLocations(List var1) throws IOException {
      weblogic.utils.Debug.assertion(this.isSystemResource);
      MultiDataStream var2 = DataStreamFactory.createMultiDataStream();
      File var3 = new File(this.sourcePath);
      weblogic.utils.Debug.assertion(var3.exists());
      var2.addFileDataStream(getRootPath(this.rootDirectory, var3) + var3.getName(), var3, false);
      return var2;
   }

   private String getOrCreateJarForURI(String var1) throws IOException {
      if (var1 != null && var1.length() != 0) {
         File var2 = this.rootDirectory;
         File var3 = new File(var2, var1);
         String var4 = null;
         synchronized(this.jarCache) {
            var4 = (String)this.jarCache.get(var1);
            File var6;
            if (var4 != null) {
               var6 = new File(var4);
               if (var6.exists() && var6.lastModified() >= FileUtils.getLastModified(var3)) {
                  return var4;
               }
            }

            this.jarCache.remove(var1);
            var6 = File.createTempFile("wl_comp", ".jar", this.tempDirectory);
            var6.deleteOnExit();
            JarFileUtils.createJarFileFromDirectory(var6, var3, false);
            var4 = var6.getAbsolutePath();
            this.jarCache.put(var1, var4);
            return var4;
         }
      } else {
         throw new AssertionError("URI cannot be null");
      }
   }

   private FileDataStream getTempSrcLocation() throws IOException {
      File var1 = new File(this.sourcePath);
      if (this.isSrcArchive) {
         if (isDebugEnabled()) {
            debugSay("originalSrcFile : " + var1.getAbsolutePath());
         }

         return DataStreamFactory.createFileDataStream(var1.getName(), var1, true);
      } else if (!var1.isDirectory()) {
         if (isDebugEnabled()) {
            debugSay("originalSrcFile : " + var1.getAbsolutePath());
         }

         return DataStreamFactory.createFileDataStream("wl_app_src.jar", var1, false);
      } else {
         synchronized(this.jarCache) {
            String var3 = (String)this.jarCache.get("wl_app_src.jar");
            File var4;
            if (var3 != null && var3.length() != 0) {
               var4 = new File(var3);
               if (var4.exists()) {
                  long var5 = var4.lastModified();
                  long var7 = FileUtils.getLastModified(var1);
                  if (var5 >= var7) {
                     if (isDebugEnabled()) {
                        debugSay("srcPath '" + this.sourcePath + "' not modified." + " returning : " + var3);
                     }

                     return DataStreamFactory.createFileDataStream(var4, true);
                  }
               }
            }

            if (isDebugEnabled()) {
               debugSay("srcPath '" + this.sourcePath + "' modified. re-constructing temp jar");
            }

            var4 = new File(this.tempDirectory, "wl_app_src.jar");
            JarFileUtils.createJarFileFromDirectory(var4, var1, false);
            var4.deleteOnExit();
            var3 = var4.getAbsolutePath();
            this.jarCache.put("wl_app_src.jar", var3);
            return DataStreamFactory.createFileDataStream(var4, true);
         }
      }
   }

   private String getTempDescsFilePath() throws IOException {
      if (this.planDir == null && this.planPath == null && this.altDDPath == null && this.altWLDDPath == null) {
         return null;
      } else {
         synchronized(this.jarCache) {
            String var2 = (String)this.jarCache.get("wl_app_desc.jar");
            if (var2 != null && var2.length() != 0 && !this.areDescsModified(var2)) {
               if (isDebugEnabled()) {
                  debugSay("descriptors not modified. returning : " + var2);
               }

               return var2;
            } else {
               if (isDebugEnabled()) {
                  debugSay("descriptors modified. Re-constructing temp jar for descs");
               }

               File var3 = FileUtils.createTempDir("DESC_DIR", this.tempDirectory);
               File var4 = new File(var3, "plan");
               if (this.planDir != null && this.planDir.length() != 0) {
                  FileUtils.copyPreserveTimestamps(new File(this.planDir), var4);
               }

               if (this.planPath != null && this.planPath.length() != 0) {
                  boolean var5 = var4.exists();
                  if (!var5) {
                     var5 = var4.mkdirs();
                  }

                  if (!var5) {
                     throw new IOException("Could not create temp location for planDir");
                  }

                  FileUtils.copyPreserveTimestamps(new File(this.planPath), var4);
               }

               if (this.altDDPath != null && this.altDDPath.length() != 0) {
                  FileUtils.copyPreserveTimestamps(new File(this.altDDPath), var3);
               }

               if (this.altWLDDPath != null && this.altWLDDPath.length() != 0) {
                  FileUtils.copyPreserveTimestamps(new File(this.altWLDDPath), var3);
               }

               File[] var9 = var3.listFiles();
               if (var9 != null && var9.length != 0) {
                  File var6 = new File(this.tempDirectory, "wl_app_desc.jar");
                  JarFileUtils.createJarFileFromDirectory(var6, var3, false);
                  var6.deleteOnExit();
                  FileUtils.remove(var3);
                  var2 = var6.getAbsolutePath();
                  this.jarCache.put("wl_app_desc.jar", var2);
                  return var2;
               } else {
                  return null;
               }
            }
         }
      }
   }

   private boolean areFilesPartOfSrc(List var1) throws IOException {
      VirtualJarFile var2 = null;

      try {
         var2 = VirtualJarFactory.createVirtualJar(new File(this.sourcePath));
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Iterator var5 = var2.getEntries(var4);
            if (var5.hasNext()) {
               boolean var6 = true;
               return var6;
            }
         }
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var14) {
            }
         }

      }

      return false;
   }

   private void updateDescriptors(AppDeploymentMBean var1) {
      weblogic.utils.Debug.assertion(!this.isSystemResource);
      this.planPath = var1.getAbsolutePlanPath();
      this.planDir = var1.getAbsolutePlanDir();
      this.altDDPath = var1.getAltDescriptorPath();
      this.altWLDDPath = var1.getAltWLSDescriptorPath();
      synchronized(this.jarCache) {
         String var3 = (String)this.jarCache.remove("wl_app_desc.jar");
         if (var3 != null) {
            File var4 = new File(var3);
            if (var4.exists()) {
               var4.delete();
            }
         }

      }
   }

   private boolean areDescsModified(String var1) {
      if (this.planDir == null && this.planPath == null && this.altDDPath == null && this.altWLDDPath == null) {
         return false;
      } else if (var1 == null) {
         return true;
      } else {
         File var2 = new File(var1);
         if (!var2.exists()) {
            return true;
         } else {
            long var3 = FileUtils.getLastModified(var2);
            long var5 = this.getLastModifiedForDescs();
            return var5 > var3;
         }
      }
   }

   private long getLastModifiedForDescs() {
      long[] var1 = new long[]{getLastModifiedForPath(this.planDir), getLastModifiedForPath(this.planPath), getLastModifiedForPath(this.altDDPath), getLastModifiedForPath(this.altWLDDPath)};
      long var2 = 0L;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         long var5 = var1[var4];
         if (var5 > var2) {
            var2 = var5;
         }
      }

      return var2;
   }

   private static long getLastModifiedForPath(String var0) {
      long var1 = 0L;
      if (var0 != null) {
         File var3 = new File(var0);
         if (var3.exists()) {
            var1 = FileUtils.getLastModified(var3);
         }
      }

      return var1;
   }

   private static String getRootPath(File var0, File var1) throws IOException {
      String var2 = var0.getCanonicalPath();
      if (isDebugEnabled()) {
         debugSay(" +++ baseLocation : " + var2);
      }

      String var3 = var1.getCanonicalPath();
      if (isDebugEnabled()) {
         debugSay(" +++ givenURI : " + var3);
      }

      int var4 = var3.indexOf(var2);
      if (var4 == -1) {
         throw new AssertionError("uri '" + var3 + "' is not sub dir of" + "'" + var2 + "'");
      } else {
         String var5 = var3.substring(var2.length() + 1, var3.indexOf(var1.getName()));
         if (isDebugEnabled()) {
            debugSay(" +++ rootPath : " + var5);
         }

         return var5;
      }
   }

   private static String getFilePathRelativeTo(File var0, File var1) throws IOException {
      if (var0 != null && var0.exists()) {
         if (var1 != null && var1.exists()) {
            String var2 = var0.getCanonicalPath();
            String var3 = var1.getCanonicalPath();
            if (!var2.startsWith(var3)) {
               throw new IOException("SourcePath '" + var2 + "' does not start with '" + var3 + "'");
            } else {
               String var4 = var2.substring(var3.length() + 1);
               return var4;
            }
         } else {
            throw new IOException("Root directory is null or doesnot exist");
         }
      } else {
         throw new IOException("Source is null or doesnot exist");
      }
   }

   private static boolean verifyIfFileIsIn(File var0, File var1) throws IOException {
      if (var0 != null && var0.exists()) {
         if (var1 != null && var1.exists()) {
            if (!var1.isDirectory()) {
               throw new IOException("Root File is not a directory");
            } else {
               try {
                  File var2 = var1.getCanonicalFile();

                  File var4;
                  for(File var3 = var0; (var4 = var3.getParentFile()) != null; var3 = var3.getParentFile()) {
                     var4 = var4.getCanonicalFile();
                     if (var4.equals(var2)) {
                        return true;
                     }
                  }

                  return false;
               } catch (IOException var5) {
                  return false;
               }
            }
         } else {
            throw new IOException("Root directory is null or doesnot exist");
         }
      } else {
         throw new IOException("Source is null or doesnot exist");
      }
   }

   public static File[] validateDelta(String[] var0, AppDeploymentMBean var1) throws IOException {
      if (var0 == null) {
         return null;
      } else {
         File var2 = new File(var1.getAbsoluteSourcePath());
         if (!var2.isDirectory()) {
            Loggable var7 = DeployerRuntimeLogger.logPartialRedeployOfArchiveLoggable(var1.getName());
            throw new IOException(var7.getMessage());
         } else {
            ArrayList var3 = new ArrayList(var0.length);

            for(int var4 = 0; var4 < var0.length; ++var4) {
               File var5 = new File(var2, var0[var4]);
               if (!var5.exists()) {
                  Loggable var6 = DeployerRuntimeLogger.invalidDeltaLoggable(var0[var4], var1.getApplicationName());
                  throw new IOException(var6.getMessage());
               }

               var3.add(var5);
            }

            return (File[])((File[])var3.toArray(new File[var3.size()]));
         }
      }
   }

   static {
      CONFIG_DIR_PREFIX = DomainDir.getRootDir() + File.separator + "config" + File.separator;
      sourceCacheMap = new HashMap();
   }
}
