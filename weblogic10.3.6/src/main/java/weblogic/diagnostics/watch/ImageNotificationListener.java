package weblogic.diagnostics.watch;

import com.bea.diagnostics.notifications.InvalidNotificationException;
import com.bea.diagnostics.notifications.Notification;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.image.ImageAlreadyCapturedException;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.image.InvalidDestinationDirectoryException;
import weblogic.diagnostics.image.InvalidLockoutTimeException;
import weblogic.diagnostics.type.DiagnosticRuntimeException;

final class ImageNotificationListener extends WatchNotificationListenerCommon implements WatchNotificationListener {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private String imageLocation;
   private int imageLockoutMinutes;

   ImageNotificationListener(String var1) throws InvalidNotificationException, NotificationCreateException {
      this(var1, (String)null, -1);
   }

   ImageNotificationListener(String var1, String var2, int var3) throws InvalidNotificationException, NotificationCreateException {
      super(var1);
      this.imageLocation = var2;
      this.imageLockoutMinutes = var3;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Created image notification for location: " + var2 + " lockout: " + var3);
      }

   }

   ImageNotificationListener(String var1, String var2) throws InvalidNotificationException, NotificationCreateException {
      this(var1, var2, -1);
   }

   String getImageLocation() {
      return this.imageLocation;
   }

   int getImageLockoutMinutes() {
      return this.imageLockoutMinutes;
   }

   void setImageLocation(String var1) {
      this.imageLocation = var1;
   }

   void setImageLockoutMinutes(int var1) {
      this.imageLockoutMinutes = var1;
   }

   public void processWatchNotification(Notification var1) {
      ImageManager var2 = ImageManager.getInstance();

      try {
         try {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Handle image notification for location: " + this.imageLocation + " lockout: " + this.imageLockoutMinutes);
               debugLogger.debug("Watch notification: " + var1);
            }

            if (this.imageLocation != null && this.imageLockoutMinutes >= 0) {
               var2.captureImage(this.imageLocation, this.imageLockoutMinutes);
            } else if (this.imageLocation != null) {
               var2.captureImage(this.imageLocation);
            } else if (this.imageLockoutMinutes >= 0) {
               var2.captureImage(this.imageLockoutMinutes);
            } else {
               var2.captureImage();
            }

            this.getWatchRuntime().incrementTotalDIMGNotificationsPerformed();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Capture image started successfully.");
            }

         } catch (InvalidLockoutTimeException var4) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Capture image failed due to invalid lockout time ", var4);
            }

            DiagnosticsLogger.logInvalidNotificationLockoutMinutes("" + this.imageLockoutMinutes);
            throw var4;
         } catch (InvalidDestinationDirectoryException var5) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Capture image failed due to invalid destination ", var5);
            }

            DiagnosticsLogger.logInvalidNotificationImageLocation(this.imageLocation, "" + var5);
            throw var5;
         } catch (ImageAlreadyCapturedException var6) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Capture image failed due to already captured exception ", var6);
            }

            DiagnosticsLogger.logNotificationImageAlreadyCaptured("" + var6);
            throw var6;
         } catch (Exception var7) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Capture image failed with exception ", var7);
            }

            DiagnosticsLogger.logErrorInNotification(var7);
            throw var7;
         }
      } catch (Throwable var8) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Incrementing failed notifications ", var8);
         }

         this.getWatchRuntime().incrementTotalFailedDIMGNotifications();
         throw new DiagnosticRuntimeException(var8);
      }
   }

   public String toString() {
      return "ImageNotificationListener - image directory: " + this.imageLocation + " image lockout minutes: " + this.imageLockoutMinutes + " isEnabled: " + this.isEnabled();
   }
}
