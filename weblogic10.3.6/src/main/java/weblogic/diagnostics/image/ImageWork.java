package weblogic.diagnostics.image;

import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.management.ManagementException;
import weblogic.management.runtime.WLDFImageCreationTaskRuntimeMBean;

class ImageWork implements Runnable {
   private TaskRuntimeMBeanImpl imageWatcher;
   private String destination;
   private int lockoutMinutes;
   ImageRequester imageRequester;

   ImageWork(String var1, int var2, ImageRequester var3) {
      this.destination = var1;
      this.lockoutMinutes = var2;
      this.imageRequester = var3;

      try {
         this.imageWatcher = new TaskRuntimeMBeanImpl();
      } catch (ManagementException var5) {
         UnexpectedExceptionHandler.handle("Problem registering TaskRuntimeMBean.", var5);
      }

   }

   WLDFImageCreationTaskRuntimeMBean getImageWatcher() {
      return this.imageWatcher;
   }

   public void run() {
      long var1 = System.currentTimeMillis();
      ImageBuilder var3 = new ImageBuilder(this.destination, var1, this.imageRequester);
      this.imageWatcher.setImageBuilder(var3);
      this.imageWatcher.setBeginTime(var1);
      var3.buildImage(this.imageWatcher);
   }
}
