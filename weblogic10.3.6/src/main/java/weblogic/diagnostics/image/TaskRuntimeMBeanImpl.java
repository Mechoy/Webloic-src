package weblogic.diagnostics.image;

import java.io.PrintWriter;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.TaskRuntimeMBean;
import weblogic.management.runtime.WLDFImageCreationTaskRuntimeMBean;

public class TaskRuntimeMBeanImpl extends RuntimeMBeanDelegate implements WLDFImageCreationTaskRuntimeMBean {
   private int imageStatus = 0;
   private long imageCaptureStartTime;
   private long imageCaptureEndTime;
   private boolean systemTask;
   private Exception imageError;
   private static int count = 0;
   private ImageBuilder imageBuilder;

   public TaskRuntimeMBeanImpl() throws ManagementException {
      super("DiagnosticImageCaptureTaskRuntime_" + getCount());
   }

   synchronized void setError(Exception var1) {
      this.imageError = var1;
      this.imageStatus = 3;
   }

   synchronized void setBeginTime(long var1) {
      this.imageCaptureStartTime = var1;
      this.imageStatus = 1;
   }

   synchronized void setEndTime(long var1) {
      this.imageCaptureEndTime = var1;
      this.imageStatus = 2;
   }

   void setImageBuilder(ImageBuilder var1) {
      this.imageBuilder = var1;
   }

   public String getDescription() {
      String var1 = this.imageBuilder != null ? this.imageBuilder.getArchiveName() : null;
      return var1 == null ? "Diagnostic image request pending execution." : var1;
   }

   public synchronized String getStatus() {
      return ImageConstants.CAPTURE_STATES[this.imageStatus];
   }

   public synchronized boolean isRunning() {
      return this.imageStatus == 1;
   }

   public synchronized long getBeginTime() {
      return this.imageCaptureStartTime;
   }

   public synchronized long getEndTime() {
      return this.imageCaptureEndTime;
   }

   public TaskRuntimeMBean[] getSubTasks() {
      return null;
   }

   public TaskRuntimeMBean getParentTask() {
      return null;
   }

   public void cancel() {
      if (this.isRunning()) {
         this.imageBuilder.requestImageCaptureCancel();
      }

   }

   public synchronized Exception getError() {
      return this.imageError;
   }

   public boolean isSystemTask() {
      return this.systemTask;
   }

   public void setSystemTask(boolean var1) {
      this.systemTask = var1;
   }

   public void printLog(PrintWriter var1) {
   }

   private static synchronized int getCount() {
      return ++count;
   }
}
