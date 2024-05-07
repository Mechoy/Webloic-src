package weblogic.management.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.AccessController;
import java.util.Properties;
import java.util.StringTokenizer;
import weblogic.Home;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class OCMService extends AbstractServerService {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationRuntime");
   private static boolean DEBUG;
   private static final AuthenticatedSubject KERNEL_ID;
   private static final String OCM_DIR;
   private static final String FILE_NAME = "domainlocation.properties";
   private static final String DOMAIN_SEPARATOR = ",";
   private static final long LOCK_TIMEOUT_MILLIS = 30000L;

   public void start() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain();
      if (!var1.isOCMEnabled()) {
         if (DEBUG) {
            debug("OCM is disabled since domainMBean.isOCMEnabled()=" + var1.isOCMEnabled());
         }

      } else {
         if (DEBUG) {
            debug("started");
         }

         if (!ManagementService.getRuntimeAccess(KERNEL_ID).isAdminServer()) {
            if (DEBUG) {
               debug("Production mode but not an admin server. quit");
            }

         } else {
            WorkManager var2 = WorkManagerFactory.getInstance().getDefault();
            if (DEBUG) {
               debug("scheduling the OCMRunnable ...");
            }

            var2.schedule(new OCMRunnable());
         }
      }
   }

   private static void recordDomainRoot(File var0, String var1, String var2) throws IOException {
      FileLock var3 = null;
      Properties var4 = new Properties();
      RandomAccessFile var5 = null;

      try {
         var5 = new RandomAccessFile(var0, "rws");
         var3 = lock(var0, var5);
         if (var3 == null) {
            if (DEBUG) {
               debug("unable to get lock on " + var0.getAbsolutePath());
            }

            return;
         }

         if (DEBUG) {
            debug(var0 + " is locked with " + var3);
         }

         load(var4, var5);
         if (DEBUG) {
            debug("loaded properties [" + var4 + "]");
         }

         if (DEBUG) {
            debug("machineName=" + var1 + ", currentDomainRoot=" + var2);
         }

         if (var1 != null && var2 != null) {
            String var6 = var4.getProperty(var1);
            if (DEBUG) {
               debug("domainRoots=" + var6);
            }

            if (!isPresent(var6, var2)) {
               if (var6 == null) {
                  var6 = var2;
               } else {
                  var6 = var6 + "," + var2;
               }

               var4.setProperty(var1, var6);
               if (DEBUG) {
                  debug("storing updated domainRoots=" + var6);
               }

               store(var4, var5);
               return;
            }

            return;
         }
      } finally {
         try {
            if (var3 != null) {
               var3.release();
            }

            if (var5 != null) {
               var5.close();
            }
         } catch (IOException var16) {
            if (DEBUG) {
               var16.printStackTrace();
            }
         }

      }

   }

   private static boolean isPresent(String var0, String var1) {
      if (var1 != null && var0 != null) {
         var1 = var1.trim();
         StringTokenizer var2 = new StringTokenizer(var0, ",");

         String var3;
         do {
            if (!var2.hasMoreElements()) {
               if (DEBUG) {
                  debug("found no match!");
               }

               return false;
            }

            var3 = (String)var2.nextElement();
            if (var3 != null) {
               var3 = var3.trim();
            }

            if (DEBUG) {
               debug("comparing root=" + var3 + " with " + var1 + " with result " + var1.equalsIgnoreCase(var3));
            }
         } while(!var1.equalsIgnoreCase(var3));

         return true;
      } else {
         return false;
      }
   }

   private static FileLock lock(File var0, RandomAccessFile var1) throws IOException {
      FileChannel var2 = var1.getChannel();
      if (DEBUG) {
         debug("attempting to lock " + var0);
      }

      return Utils.getFileLock(var2, 30000L);
   }

   private static void store(Properties var0, RandomAccessFile var1) throws IOException {
      var1.seek(0L);
      RandomAccessFileOutputStream var2 = new RandomAccessFileOutputStream(var1);

      try {
         var0.store(var2, "");
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var9) {
            }
         }

      }

   }

   private static void load(Properties var0, RandomAccessFile var1) throws IOException {
      RandomAccessFileInputStream var2 = new RandomAccessFileInputStream(var1);

      try {
         var0.load(var2);
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var9) {
            }
         }

      }

   }

   private static boolean create(File var0) {
      try {
         if (var0.createNewFile()) {
            return true;
         }
      } catch (IOException var2) {
      }

      return var0.exists();
   }

   private static void debug(String var0) {
      debugLogger.debug("[OCMService] " + var0);
   }

   static {
      DEBUG = debugLogger.isDebugEnabled();
      KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      OCM_DIR = "utils" + File.separator + "ccr";
   }

   private static class RandomAccessFileOutputStream extends OutputStream {
      RandomAccessFile raf;

      RandomAccessFileOutputStream(RandomAccessFile var1) {
         this.raf = var1;
      }

      public void write(int var1) throws IOException {
         this.raf.write(var1);
      }
   }

   private static class RandomAccessFileInputStream extends InputStream {
      RandomAccessFile raf;

      RandomAccessFileInputStream(RandomAccessFile var1) {
         this.raf = var1;
      }

      public int read() throws IOException {
         return this.raf.read();
      }
   }

   private static class OCMRunnable implements Runnable {
      private OCMRunnable() {
      }

      public void run() {
         if (OCMService.DEBUG) {
            OCMService.debug("OCMRunnable has started");
         }

         String var1 = Home.getMiddlewareHomePath();
         if (OCMService.DEBUG) {
            OCMService.debug("home=" + var1);
         }

         if (var1 != null) {
            var1 = var1 + File.separator + OCMService.OCM_DIR;
         }

         File var2 = new File(var1);
         if (!var2.exists()) {
            if (OCMService.DEBUG) {
               OCMService.debug(var2.getAbsolutePath() + " does not exist!");
            }

         } else {
            File var3 = new File(var2, "domainlocation.properties");
            if (!var3.exists() && !OCMService.create(var3)) {
               if (OCMService.DEBUG) {
                  OCMService.debug(var3.getAbsolutePath() + " failed to create !");
               }

            } else {
               try {
                  String var4 = InetAddress.getLocalHost().getHostName();
                  String var5 = DomainDir.getRootDir();
                  OCMService.recordDomainRoot(var3, var4, var5);
               } catch (IOException var6) {
                  if (OCMService.DEBUG) {
                     OCMService.debug("failed to record current domain root! abort!");
                     var6.printStackTrace();
                  }
               }

            }
         }
      }

      // $FF: synthetic method
      OCMRunnable(Object var1) {
         this();
      }
   }
}
