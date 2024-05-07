package weblogic.work.j2ee;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkListener;
import commonj.work.WorkManager;
import weblogic.kernel.Kernel;

public class Test implements WorkListener, Work {
   private WorkItem item;
   private boolean throwException;

   public Test() {
   }

   public Test(boolean var1) {
      this.throwException = var1;
   }

   public void workAccepted(WorkEvent var1) {
      System.out.println("work accepted : " + var1.getType());
   }

   public void workRejected(WorkEvent var1) {
      System.out.println("work rejected : " + var1.getType() + ", workitem : " + (this.item != null ? this.item.getStatus() : -1));
   }

   public void workStarted(WorkEvent var1) {
      System.out.println("work started : " + var1.getType() + ", workitem : " + (this.item != null ? this.item.getStatus() : -1));
   }

   public void workCompleted(WorkEvent var1) {
      System.out.println("work completed : " + var1.getType() + ", workitem : " + this.item.getStatus() + ", exception : " + var1.getException());
   }

   public static void main(String[] var0) {
      Test var1 = new Test();
      Test var2 = new Test(true);
      Kernel.ensureInitialized();
      WorkManager var3 = J2EEWorkManager.getDefault();

      try {
         var1.item = var3.schedule(var1, var1);
         var2.item = var3.schedule(var2, var2);
      } catch (WorkException var5) {
         var5.printStackTrace();
      } catch (IllegalArgumentException var6) {
         var6.printStackTrace();
      }

      while(var2.item.getStatus() != 4) {
      }

   }

   public void run() {
      try {
         System.out.println("started run. sleeping ...");
         Thread.sleep(10000L);
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

      if (this.throwException) {
         throw new RuntimeException("testing testing");
      } else {
         System.out.println("completed run");
      }
   }

   public void release() {
   }

   public boolean isDaemon() {
      return false;
   }
}
