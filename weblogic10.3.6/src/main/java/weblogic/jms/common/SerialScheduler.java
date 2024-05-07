package weblogic.jms.common;

import java.util.LinkedList;
import weblogic.work.WorkManagerFactory;

public class SerialScheduler implements Runnable {
   private boolean running = false;
   private boolean drain = false;
   private LinkedList schedList = new LinkedList();
   private Throwable firstThrowable;

   public void run() {
      int var1 = 0;

      while(true) {
         Runnable var2 = null;
         synchronized(this.schedList) {
            if (!this.drain && var1 == 100) {
               if (WorkManagerFactory.getInstance().getDefault().scheduleIfBusy(this)) {
                  return;
               }

               var1 = 0;
            }

            if (this.schedList.size() != 0) {
               var2 = (Runnable)this.schedList.removeFirst();
            }

            if (var2 == null) {
               this.running = false;
               this.drain = false;
               this.schedList.notifyAll();
               return;
            }
         }

         try {
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("Calling out to " + var2);
            }

            var2.run();
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("Back from " + var2);
            }
         } catch (Throwable var5) {
            if (this.firstThrowable == null) {
               this.firstThrowable = var5;
            }

            var5.printStackTrace();
         }

         ++var1;
      }
   }

   public void schedule(Runnable var1) {
      synchronized(this.schedList) {
         this.schedList.add(var1);
         if (!this.running) {
            this.running = true;
            WorkManagerFactory.getInstance().getDefault().schedule(this);
         }

      }
   }

   public void drain() {
      synchronized(this.schedList) {
         if (this.schedList.size() != 0) {
            this.drain = true;
            if (!this.running) {
               this.running = true;
               WorkManagerFactory.getInstance().getDefault().schedule(this);
            }

         }
      }
   }

   public Throwable waitForComplete() {
      synchronized(this.schedList) {
         while(this.schedList.size() > 0 || this.running) {
            try {
               this.schedList.wait();
            } catch (InterruptedException var4) {
               throw new RuntimeException(var4);
            }
         }

         Throwable var2 = this.firstThrowable;
         this.firstThrowable = null;
         return var2;
      }
   }
}
