package weblogic.diagnostics.image;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

class ImageBuilder {
   ImageManager imageManager = ImageManager.getInstance();
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticImage");
   private WorkManager workManager = WorkManagerFactory.getInstance().find("ImageWorkManager");
   private ZipOutputStream zipFile;
   private ImageRequester imageRequester;
   private ImageSummary imageSummary;
   private String imageDirectory;
   private String archiveName;
   private long imageCreationTime;
   private boolean cancelRequest = false;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   ImageBuilder(String var1, long var2, ImageRequester var4) {
      this.imageDirectory = var1;
      this.imageCreationTime = var2;
      this.imageRequester = var4;
   }

   void requestImageCaptureCancel() {
      this.cancelRequest = true;
   }

   String getArchiveName() {
      return this.archiveName;
   }

   synchronized void buildImage(TaskRuntimeMBeanImpl var1) {
      long var2 = System.currentTimeMillis();

      try {
         this.cancelRequest = false;
         Map var10 = this.imageManager.getInternalImageSources();
         this.createImageSourceArchive();
         synchronized(var10) {
            Set var12 = var10.keySet();
            Iterator var13 = var12.iterator();

            while(!this.cancelRequest && var13.hasNext()) {
               String var14 = (String)var13.next();
               ImageSource var15 = (ImageSource)var10.get(var14);
               long var6 = System.currentTimeMillis();
               this.addImageSourceToArchive(var14, var15);
               long var8 = System.currentTimeMillis() - var6;
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Added image source " + var14 + " to archive: " + this.archiveName + " in " + var8 + " ms.");
               }
            }

            if (this.cancelRequest) {
               this.imageSummary.captureCancelled();
            }
         }
      } catch (IOException var61) {
         this.logBuilderError(var1, var61);
      } finally {
         try {
            long var4 = System.currentTimeMillis() - var2;
            this.imageSummary.setImageCreationElapsedTime(var4);
            this.closeImageSourceArchive();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Successfully created archive: " + this.archiveName + " in " + var4 + " ms.");
            }
         } catch (Exception var58) {
            this.logBuilderError(var1, var58);
         } finally {
            var1.setEndTime(System.currentTimeMillis());
         }

      }

   }

   private String getUniqueDiagnosticImageName() {
      StringBuffer var1 = new StringBuffer();
      String var2 = "_";
      SimpleDateFormat var3 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
      var1.append("diagnostic_image_");
      var1.append(ManagementService.getRuntimeAccess(kernelId).getServer().getName() + var2);
      var1.append(var3.format(new Date(this.imageCreationTime)));
      String var4 = var1.toString();
      File var5 = new File(this.imageDirectory, var4 + ".zip");
      if (var5.exists()) {
         var4 = this.incrementName(var4);
      }

      return this.imageDirectory + File.separator + var4 + ".zip";
   }

   private void createImageSourceArchive() throws IOException {
      this.archiveName = this.getUniqueDiagnosticImageName();
      BufferedOutputStream var1 = new BufferedOutputStream(new FileOutputStream(this.archiveName));
      this.zipFile = new ZipOutputStream(var1);
      this.imageSummary = new ImageSummary(this.imageCreationTime, this.imageRequester, this.archiveName);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Creating image source archive: " + this.archiveName);
      }

   }

   private void closeImageSourceArchive() throws IOException, ImageSourceCreationException {
      String var1 = "image.summary";
      ZipEntry var2 = new ZipEntry(var1);

      try {
         synchronized(this.zipFile) {
            this.zipFile.putNextEntry(var2);
         }

         this.imageSummary.createDiagnosticImage(this.zipFile);
      } catch (Exception var17) {
         DiagnosticsLogger.logDiagnosticImageSourceCreationException(var1, var17);
      } finally {
         synchronized(this.zipFile) {
            this.zipFile.closeEntry();
            this.zipFile.close();
         }
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Closing image source archive: " + this.archiveName);
      }

   }

   private void addImageSourceToArchive(String var1, ImageSource var2) throws IOException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Adding image source to archive: " + var1);
      }

      String var3;
      if (var1.equals("FlightRecorder")) {
         var3 = ".jfr";
      } else {
         var3 = ".img";
      }

      String var4 = var1 + var3;
      ZipEntry var5 = new ZipEntry(var4);
      synchronized(this.zipFile) {
         this.zipFile.putNextEntry(var5);
      }

      ImageSourceOutputStream var6 = new ImageSourceOutputStream(this.zipFile);
      ImageSourceWork var7 = new ImageSourceWork(var2, var6);
      this.workManager.schedule(var7);
      this.handleImageSourceWork(var7);
      synchronized(this.zipFile) {
         this.zipFile.closeEntry();
         this.zipFile.flush();
      }

      if (var7.getFailureException() == null) {
         this.imageSummary.addSuccessfulImageSource(var1, var7.getImageSourceElapsedTime());
      } else {
         this.imageSummary.addFailedImageSource(var1, var7.getFailureException());
         DiagnosticsLogger.logDiagnosticImageSourceCreationException(var1, var7.getFailureException());
      }

   }

   private String incrementName(String var1) {
      File var2 = new File(this.imageDirectory);
      String var3 = "_";
      int var4 = 0;
      ImageFilenameFilter var5 = new ImageFilenameFilter(var1 + var3);
      String[] var6 = var2.list(var5);
      if (var6.length > 0) {
         Arrays.sort(var6);
         String var7 = var6[var6.length - 1];
         var7 = var7.split("[.]")[0];
         String[] var8 = var7.split(var3);
         var4 = Integer.parseInt(var8[var8.length - 1]);
      }

      ++var4;
      return var1 + var3 + var4;
   }

   private void handleImageSourceWork(ImageSourceWork var1) {
      boolean var2 = false;

      for(long var3 = 60000L; !var1.isFinished() && var3 > 0L; var3 -= 500L) {
         this.sleep(500L);
      }

      if (!var1.isFinished()) {
         var1.getImageSource().timeoutImageCreation();
         this.sleep(30000L);
         var2 = true;
      }

      ImageSourceOutputStream var5 = (ImageSourceOutputStream)var1.getOutputStream();
      var5.close();
   }

   private void sleep(long var1) {
      try {
         long var3 = System.currentTimeMillis() + var1;
         synchronized(this) {
            while(true) {
               if (System.currentTimeMillis() >= var3) {
                  break;
               }

               this.wait(var1);
            }
         }
      } catch (InterruptedException var8) {
         UnexpectedExceptionHandler.handle("ImageBuilder interrupted.", var8);
      }

   }

   private void logBuilderError(TaskRuntimeMBeanImpl var1, Exception var2) {
      DiagnosticsLogger.logDiagnosticImageCreationError(var2);
      var1.setError(var2);
   }
}
