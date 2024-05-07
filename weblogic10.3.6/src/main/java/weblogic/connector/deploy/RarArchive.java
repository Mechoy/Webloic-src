package weblogic.connector.deploy;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import weblogic.application.io.ClasspathInfo;
import weblogic.application.io.ExplodedJar;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.application.utils.PathUtils;
import weblogic.connector.ConnectorLogger;
import weblogic.connector.common.Debug;
import weblogic.utils.FileUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class RarArchive {
   private String tempPath;
   private String applicationId;
   private String applicationName;
   private VirtualJarFile originalVj;
   private VirtualJarFile tempVj;
   private File rootTempDir;
   private String classpath;
   private volatile boolean initialized = false;
   private ExplodedJar explodedJarFile = null;
   private static ClasspathInfo RARClasspathInfo = new ClasspathInfo() {
      public String[] getClasspathURIs() {
         return new String[0];
      }

      public String[] getJarURIs() {
         return new String[0];
      }
   };

   public RarArchive(String var1, String var2, VirtualJarFile var3) {
      this.applicationId = var1;
      this.applicationName = var2;
      this.originalVj = var3;
   }

   public VirtualJarFile getVirtualJarFile() {
      return this.originalVj;
   }

   public void remove() {
      if (this.rootTempDir == null) {
         this.generateTempPath();
      }

      if (this.rootTempDir.exists()) {
         File var1 = this.rootTempDir.getParentFile();
         if (this.explodedJarFile != null) {
            this.explodedJarFile.remove();
         }

         if (Debug.isDeploymentEnabled()) {
            Debug.deployment("Remove temporary directory" + this.rootTempDir.getAbsolutePath());
         }

         if (var1 != null && var1.list().length == 0) {
            FileUtils.remove(var1);
         }
      }

   }

   public String getClassPath() {
      this.extractIfNeed();
      this.computerClassPath();
      return this.classpath;
   }

   private void computerClassPath() {
      if (this.classpath == null) {
         String var1 = this.tempVj.getName();
         if (this.tempVj.isDirectory()) {
            ArrayList var3 = new ArrayList();
            Iterator var4 = this.tempVj.entries();

            while(var4.hasNext()) {
               ZipEntry var2 = (ZipEntry)var4.next();
               if (var2.getName().endsWith(".jar")) {
                  var3.add(var2.getName());
               }
            }

            this.classpath = ClassPathUtil.computeClasspath(var1, (List)var3);
         } else {
            this.classpath = var1;
         }
      }

   }

   private boolean initializeTempDir() {
      if (this.originalVj.isDirectory()) {
         return false;
      } else {
         this.generateTempPath();
         if (!this.rootTempDir.exists() && !this.rootTempDir.mkdirs()) {
            if (Debug.isDeploymentEnabled()) {
               Debug.deployment(ConnectorLogger.logCannotExtractRARtoTempDir(this.originalVj.getName()));
            }

            return false;
         } else {
            return true;
         }
      }
   }

   private void generateTempPath() {
      String var1 = ApplicationVersionUtils.replaceDelimiter(this.applicationId, '_');
      String var2 = ApplicationVersionUtils.replaceDelimiter(this.applicationName, '_');
      this.tempPath = PathUtils.generateTempPath((String)null, var1, var2);
      this.rootTempDir = PathUtils.getAppTempDir(this.tempPath);
   }

   private void extractIfNeed() {
      if (!this.initialized) {
         try {
            synchronized(this) {
               if (!this.initialized) {
                  if (!this.initializeTempDir()) {
                     this.tempVj = this.originalVj;
                  } else {
                     this.explodedJarFile = new ExplodedJar(this.applicationId, this.rootTempDir, new File(this.originalVj.getName()), RARClasspathInfo);
                     this.tempVj = VirtualJarFactory.createVirtualJar(this.rootTempDir);
                  }

                  this.initialized = true;
               }
            }
         } catch (Exception var4) {
            if (Debug.isDeploymentEnabled()) {
               Debug.deployment(ConnectorLogger.logCannotExtractRARtoTempDir(this.originalVj.getName()), var4);
            }

            this.tempVj = this.originalVj;
         }

      }
   }
}
