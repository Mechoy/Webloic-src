package weblogic.diagnostics.image;

import java.io.OutputStream;

class ImageSourceWork implements Runnable {
   private ImageSource imageSource;
   private boolean finished;
   private long startTime;
   private long imageSourceElapsedTime;
   private OutputStream imageOutputStream;
   private Exception failureException;

   ImageSourceWork(ImageSource var1, OutputStream var2) {
      this.imageSource = var1;
      this.imageOutputStream = var2;
   }

   ImageSource getImageSource() {
      return this.imageSource;
   }

   OutputStream getOutputStream() {
      return this.imageOutputStream;
   }

   long getImageSourceElapsedTime() {
      return this.imageSourceElapsedTime;
   }

   boolean isFinished() {
      return this.finished;
   }

   Exception getFailureException() {
      return this.failureException;
   }

   public void run() {
      try {
         this.startTime = System.currentTimeMillis();
         this.imageSource.createDiagnosticImage(this.imageOutputStream);
      } catch (Exception var6) {
         this.failureException = var6;
      } finally {
         this.imageSourceElapsedTime = System.currentTimeMillis() - this.startTime;
         this.finished = true;
      }

   }
}
