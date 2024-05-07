package weblogic.diagnostics.image;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.security.AccessController;
import java.text.Collator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.flightrecorder.FlightRecorderManager;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.instrumentation.gathering.DataGatheringManager;
import weblogic.management.DomainDir;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.WLDFServerDiagnosticMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.WLDFImageCreationTaskRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class ImageManager implements PropertyChangeListener {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticImage");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final ImageManager imageManager = new ImageManager();
   private String destinationDirectory = null;
   private String absoluteDestPath = null;
   private int imageTimeout = 0;
   private Map imageSources = new Hashtable();
   private long lockoutExpiration = -1L;
   private WorkManager workManager;
   private ServerMBean serverBean;
   private Set<WLDFImageCreationTaskRuntimeMBean> activeTasks = new HashSet();

   private ImageManager() {
      String var1 = "ImageWorkManager";
      byte var2 = 2;
      byte var3 = -1;
      this.workManager = WorkManagerFactory.getInstance().findOrCreate(var1, var2, var3);
      this.serverBean = ManagementService.getRuntimeAccess(kernelId).getServer();
      WLDFServerDiagnosticMBean var4 = this.serverBean.getServerDiagnosticConfig();
      this.initFromConfiguration(var4);
      this.isValidDestination(this.destinationDirectory);
      this.registerImageSource("JVM", new JVMSource());
      DataGatheringManager.initialize();
      if (FlightRecorderManager.isRecordingPossible()) {
         this.registerImageSource("FlightRecorder", new FlightRecorderSource(this));
      } else if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Flight recording is not possible, not registering the image source");
      }

   }

   public static ImageManager getInstance() {
      return imageManager;
   }

   public ImageSource registerImageSource(String var1, ImageSource var2) {
      if (var1 != null && var2 != null) {
         ImageSource var3 = (ImageSource)this.imageSources.put(var1, var2);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Registered image source: " + var1);
         }

         return var3;
      } else {
         throw new IllegalArgumentException("Name or source null.");
      }
   }

   public ImageSource unregisterImageSource(String var1) throws ImageSourceNotFoundException {
      if (var1 == null) {
         throw new IllegalArgumentException("ImageSource name null.");
      } else {
         ImageSource var2 = (ImageSource)this.imageSources.remove(var1);
         if (var2 == null) {
            throw new ImageSourceNotFoundException("Image source not found: " + var1);
         } else {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Unregistered image source: " + var1);
            }

            return var2;
         }
      }
   }

   public String[] getImageSources() {
      synchronized(this.imageSources) {
         Set var2 = this.imageSources.keySet();
         return (String[])((String[])var2.toArray(new String[var2.size()]));
      }
   }

   public WLDFImageCreationTaskRuntimeMBean captureImage() throws ImageAlreadyCapturedException, InvalidDestinationDirectoryException {
      return this.captureImage(this.getDestinationDirectory());
   }

   public WLDFImageCreationTaskRuntimeMBean captureImage(String var1) throws ImageAlreadyCapturedException, InvalidDestinationDirectoryException {
      return this.createImageWork(var1, this.imageTimeout);
   }

   public WLDFImageCreationTaskRuntimeMBean captureImage(int var1) throws InvalidLockoutTimeException, ImageAlreadyCapturedException, InvalidDestinationDirectoryException {
      return this.captureImage(this.getDestinationDirectory(), var1);
   }

   public WLDFImageCreationTaskRuntimeMBean captureImage(String var1, int var2) throws InvalidLockoutTimeException, ImageAlreadyCapturedException, InvalidDestinationDirectoryException {
      if (var2 < 0) {
         DiagnosticsLogger.logDiagnosticImageLockoutBelow(var2);
         throw new InvalidLockoutTimeException("Specified lock time less than minimum.");
      } else if (var2 > 1440) {
         DiagnosticsLogger.logDiagnosticImageLockoutAbove(var2);
         throw new InvalidLockoutTimeException("Specified lock time greater than maximum.");
      } else {
         this.purgeCompletedTasks();
         WLDFImageCreationTaskRuntimeMBean var3 = this.createImageWork(var1, var2);
         this.activeTasks.add(var3);
         return var3;
      }
   }

   public boolean tasksInProgress() {
      this.purgeCompletedTasks();
      return !this.activeTasks.isEmpty();
   }

   public void setDestinationDirectory(String var1) throws InvalidDestinationDirectoryException {
      if (!this.isValidDestination(var1)) {
         DiagnosticsLogger.logDiagnosticImageDirectoryAccessError(var1);
         throw new InvalidDestinationDirectoryException(var1);
      } else {
         this.destinationDirectory = var1;
      }
   }

   public String getDestinationDirectory() {
      return this.destinationDirectory;
   }

   public void resetImageLockout() {
      this.lockoutExpiration = -1L;
   }

   Map getInternalImageSources() {
      return this.imageSources;
   }

   private void initFromConfiguration(WLDFServerDiagnosticMBean var1) {
      String var2 = var1.getImageDir();
      this.imageTimeout = var1.getImageTimeout();
      File var3 = new File(var2);
      if (!var3.isAbsolute()) {
         File var4 = new File(DomainDir.getPathRelativeServerDir(this.serverBean.getName(), var2));
         this.destinationDirectory = var4.getAbsolutePath();
      } else {
         this.destinationDirectory = var2;
      }

   }

   private synchronized WLDFImageCreationTaskRuntimeMBean createImageWork(String var1, int var2) throws ImageAlreadyCapturedException, InvalidDestinationDirectoryException {
      if (this.isThrottled()) {
         DiagnosticsLogger.logDiagnosticImageAlreadyCaptured();
         long var6 = this.lockoutExpiration - System.currentTimeMillis();
         String var5 = "Image captures are throttled for another " + var6 + " milliseconds due to lockout specification.";
         throw new ImageAlreadyCapturedException(var5);
      } else if (!this.isValidDestination(var1)) {
         DiagnosticsLogger.logDiagnosticImageDirectoryAccessError(var1);
         throw new InvalidDestinationDirectoryException(var1);
      } else {
         DiagnosticsLogger.logDiagnosticImageCaptureRequest(this.absoluteDestPath, var2);
         ImageRequester var3 = new ImageRequester(new Exception());
         ImageWork var4 = new ImageWork(this.absoluteDestPath, var2, var3);
         this.workManager.schedule(var4);
         if (var2 != 0) {
            this.createLockout(var2);
         }

         return var4.getImageWatcher();
      }
   }

   private boolean isThrottled() {
      boolean var1 = false;
      if (this.lockoutExpiration != -1L) {
         var1 = System.currentTimeMillis() <= this.lockoutExpiration;
         if (!var1) {
            this.lockoutExpiration = -1L;
         }
      }

      return var1;
   }

   private void createLockout(int var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Creating lockout for image captures (minutes): " + var1);
      }

      long var2 = (long)(var1 * 60 * 1000);
      this.lockoutExpiration = System.currentTimeMillis() + var2;
   }

   private boolean isValidDestination(String var1) {
      if (var1 == null) {
         return false;
      } else {
         File var2 = new File(var1);
         if (!var2.isAbsolute()) {
            ServerMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServer();
            File var4 = new File(DomainDir.getPathRelativeServerDir(var3.getName(), var1));
            this.absoluteDestPath = var4.getAbsolutePath();
         } else {
            this.absoluteDestPath = var1;
         }

         File var5 = new File(this.absoluteDestPath);
         if (!var5.exists()) {
            boolean var6 = var5.mkdirs();
            if (!var6) {
               DiagnosticsLogger.logDiagnosticImageDirectoryCreationError(this.absoluteDestPath);
               return false;
            }
         }

         return var5.canWrite();
      }
   }

   public void propertyChange(PropertyChangeEvent var1) {
      this.attributesChanged(var1.getSource());
   }

   private void attributesChanged(Object var1) {
      if (var1 instanceof WLDFServerDiagnosticMBean) {
         WLDFServerDiagnosticMBean var2 = (WLDFServerDiagnosticMBean)var1;
         this.initFromConfiguration(var2);
      }

   }

   public String[] getAvailableCapturedImages() {
      String[] var1 = new String[0];
      if (this.isValidDestination(this.destinationDirectory)) {
         File var2 = new File(this.absoluteDestPath);
         File[] var3 = var2.listFiles(new FileFilter() {
            public boolean accept(File var1) {
               return var1.getName().endsWith(".zip");
            }
         });
         if (var3 != null && var3.length > 0) {
            var1 = new String[var3.length];

            for(int var4 = 0; var4 < var3.length; ++var4) {
               var1[var4] = var3[var4].getName();
            }

            if (var3.length > 1) {
               Arrays.sort(var1, Collator.getInstance());
            }
         }
      }

      return var1;
   }

   public File findImageFile(String var1) {
      File var2 = new File(var1);
      if (var2.isAbsolute()) {
         throw new IllegalArgumentException("Invalid image file requested");
      } else {
         File var3 = new File(this.getDestinationDirectory() + File.separatorChar + var2.getName());
         return var3;
      }
   }

   private void purgeCompletedTasks() {
      Iterator var1 = this.activeTasks.iterator();

      while(var1.hasNext()) {
         WLDFImageCreationTaskRuntimeMBean var2 = (WLDFImageCreationTaskRuntimeMBean)var1.next();
         if (!var2.isRunning()) {
            var1.remove();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Purging completed image task " + var2.getName());
            }
         } else if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Image task " + var2.getName() + " still running");
         }
      }

   }
}
