package weblogic.diagnostics.image;

import java.security.AccessController;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WLDFImageCreationTaskRuntimeMBean;
import weblogic.management.runtime.WLDFImageRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ImageRuntimeMBeanImpl extends RuntimeMBeanDelegate implements WLDFImageRuntimeMBean {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticImage");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static ImageRuntimeMBeanImpl singleton;
   private static ImageManager im;
   private Set tasks = Collections.synchronizedSet(new HashSet());

   public static synchronized ImageRuntimeMBeanImpl getInstance() throws ManagementException {
      if (singleton == null) {
         singleton = new ImageRuntimeMBeanImpl();
      }

      return singleton;
   }

   public ImageRuntimeMBeanImpl() throws ManagementException {
      super("Image", ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getWLDFRuntime());
      im = ImageManager.getInstance();
   }

   public WLDFImageCreationTaskRuntimeMBean captureImage() throws ManagementException {
      try {
         WLDFImageCreationTaskRuntimeMBean var1 = im.captureImage();
         this.tasks.add(var1);
         return var1;
      } catch (ImageAlreadyCapturedException var2) {
         throw new ManagementException(var2);
      } catch (InvalidDestinationDirectoryException var3) {
         throw new ManagementException(var3);
      }
   }

   public WLDFImageCreationTaskRuntimeMBean captureImage(int var1) throws ManagementException {
      try {
         WLDFImageCreationTaskRuntimeMBean var2 = im.captureImage(var1);
         this.tasks.add(var2);
         return var2;
      } catch (ImageAlreadyCapturedException var3) {
         throw new ManagementException(var3);
      } catch (InvalidDestinationDirectoryException var4) {
         throw new ManagementException(var4);
      } catch (InvalidLockoutTimeException var5) {
         throw new ManagementException(var5);
      }
   }

   public WLDFImageCreationTaskRuntimeMBean captureImage(String var1) throws ManagementException {
      try {
         WLDFImageCreationTaskRuntimeMBean var2 = im.captureImage(var1);
         this.tasks.add(var2);
         return var2;
      } catch (ImageAlreadyCapturedException var3) {
         throw new ManagementException(var3);
      } catch (InvalidDestinationDirectoryException var4) {
         throw new ManagementException(var4);
      }
   }

   public WLDFImageCreationTaskRuntimeMBean captureImage(String var1, int var2) throws ManagementException {
      try {
         WLDFImageCreationTaskRuntimeMBean var3 = im.captureImage(var1, var2);
         this.tasks.add(var3);
         return var3;
      } catch (ImageAlreadyCapturedException var4) {
         throw new ManagementException(var4);
      } catch (InvalidDestinationDirectoryException var5) {
         throw new ManagementException(var5);
      } catch (InvalidLockoutTimeException var6) {
         throw new ManagementException(var6);
      }
   }

   public WLDFImageCreationTaskRuntimeMBean[] listImageCaptureTasks() {
      return (WLDFImageCreationTaskRuntimeMBean[])((WLDFImageCreationTaskRuntimeMBean[])this.tasks.toArray(new WLDFImageCreationTaskRuntimeMBean[this.tasks.size()]));
   }

   public void clearCompletedImageCaptureTasks() {
      Iterator var1 = this.tasks.iterator();
      HashSet var2 = new HashSet();

      while(var1.hasNext()) {
         WLDFImageCreationTaskRuntimeMBean var3 = (WLDFImageCreationTaskRuntimeMBean)var1.next();
         if (var3.getStatus().equalsIgnoreCase("Completed")) {
            var2.add(var3);
         }
      }

      var1 = var2.iterator();

      while(var1.hasNext()) {
         this.tasks.remove((WLDFImageCreationTaskRuntimeMBean)var1.next());
      }

   }

   public void resetImageLockout() {
      im.resetImageLockout();
   }
}
