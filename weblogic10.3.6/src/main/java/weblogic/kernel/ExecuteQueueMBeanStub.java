package weblogic.kernel;

import weblogic.management.configuration.ExecuteQueueMBean;

public final class ExecuteQueueMBeanStub extends MBeanStub implements ExecuteQueueMBean {
   private int queueLength = 65536;
   private int threadPriority = 5;
   private int threadCount = 5;
   private int queueLengthThresholdPercent = 90;
   private int threadsIncrease = 0;
   private int threadsMaximum = 65536;
   private int threadsMinimum = 5;

   public ExecuteQueueMBeanStub() {
      if (Kernel.isServer()) {
         this.threadCount = 15;
      }

   }

   public final String getName() {
      return "default";
   }

   public int getQueueLength() {
      return this.queueLength;
   }

   public void setQueueLength(int var1) {
      this.queueLength = var1;
   }

   public int getThreadPriority() {
      return 5;
   }

   public void setThreadPriority(int var1) {
      this.threadPriority = var1;
   }

   public int getThreadCount() {
      return this.threadCount;
   }

   public void setThreadCount(int var1) {
      this.threadCount = var1;
   }

   public int getQueueLengthThresholdPercent() {
      return this.queueLengthThresholdPercent;
   }

   public void setQueueLengthThresholdPercent(int var1) {
      this.queueLengthThresholdPercent = var1;
   }

   public int getThreadsIncrease() {
      return this.threadsIncrease;
   }

   public void setThreadsIncrease(int var1) {
      this.threadsIncrease = var1;
   }

   public int getThreadsMaximum() {
      return this.threadsMaximum;
   }

   public void setThreadsMaximum(int var1) {
      this.threadsMaximum = var1;
   }

   public int getThreadsMinimum() {
      return this.threadsMinimum;
   }

   public void setThreadsMinimum(int var1) {
      this.threadsMinimum = var1;
   }
}
