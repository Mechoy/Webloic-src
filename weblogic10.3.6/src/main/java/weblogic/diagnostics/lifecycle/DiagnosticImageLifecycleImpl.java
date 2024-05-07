package weblogic.diagnostics.lifecycle;

import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.image.ImageRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.runtime.WLDFImageCreationTaskRuntimeMBean;
import weblogic.t3.srvr.ServerRuntime;

public class DiagnosticImageLifecycleImpl implements DiagnosticComponentLifecycle {
   private static DiagnosticImageLifecycleImpl singleton = new DiagnosticImageLifecycleImpl();

   public static final DiagnosticComponentLifecycle getInstance() {
      return singleton;
   }

   public int getStatus() {
      return 4;
   }

   public void initialize() throws DiagnosticComponentLifecycleException {
      try {
         ServerRuntime.theOne().getWLDFRuntime().setWLDFImageRuntime(ImageRuntimeMBeanImpl.getInstance());
      } catch (ManagementException var2) {
         throw new DiagnosticComponentLifecycleException(var2);
      }
   }

   public void enable() throws DiagnosticComponentLifecycleException {
   }

   public void disable() throws DiagnosticComponentLifecycleException {
      if (Boolean.getBoolean("weblogic.diagnostics.image.CaptureOnShutdown")) {
         try {
            WLDFImageCreationTaskRuntimeMBean var1 = ImageManager.getInstance().captureImage();

            try {
               Thread.sleep(2000L);
            } catch (InterruptedException var4) {
            }

            while(var1.isRunning()) {
               try {
                  Thread.sleep(1000L);
               } catch (InterruptedException var3) {
               }
            }
         } catch (Exception var5) {
         }
      }

   }
}
