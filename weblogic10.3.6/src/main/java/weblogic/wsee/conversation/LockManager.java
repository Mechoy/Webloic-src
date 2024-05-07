package weblogic.wsee.conversation;

import java.util.HashMap;
import java.util.concurrent.Semaphore;
import weblogic.wsee.util.Verbose;

public class LockManager {
   private static LockManager instance = new LockManager();
   private final HashMap<String, Lock> locks = new HashMap();
   private static final boolean verbose = Verbose.isVerbose(LockManager.class);

   private LockManager() {
   }

   public static LockManager getInstance() {
      return instance;
   }

   public Lock lock(String var1) throws InterruptedException {
      if (verbose) {
         Verbose.log((Object)("Attempting to lock conversation " + var1));
      }

      Lock var2;
      synchronized(this.locks) {
         var2 = (Lock)this.locks.get(var1);
         if (var2 == null) {
            var2 = new Lock(var1);
            this.locks.put(var1, var2);
         }
      }

      var2.s.acquire();
      if (verbose) {
         Verbose.log((Object)("Successfully locked conversation " + var1));
      }

      return var2;
   }

   public void destroy(String var1) {
      synchronized(this.locks) {
         this.locks.remove(var1);
      }
   }

   public static void main(String[] var0) throws Exception {
      final LockManager var1 = new LockManager();
      Runnable var2 = new Runnable() {
         private int count = 0;

         public void run() {
            try {
               while(true) {
                  Lock var1x = var1.lock("1");
                  System.out.println("lock free");
                  Class var2 = Runnable.class;
                  synchronized(Runnable.class) {
                     ++this.count;
                     if (this.count > 1) {
                        throw new IllegalStateException();
                     }
                  }

                  Thread.sleep(5L);
                  var2 = Runnable.class;
                  synchronized(Runnable.class) {
                     --this.count;
                     var1x.release();
                     System.out.println("released");
                  }
               }
            } catch (InterruptedException var7) {
               var7.printStackTrace();
            }
         }
      };
      Thread var3 = new Thread(var2);
      Thread var4 = new Thread(var2);
      Thread var5 = new Thread(var2);
      var3.start();
      var4.start();
      var5.start();
   }

   public class Lock {
      private String id;
      private Semaphore s = new Semaphore(1, true);

      public Lock() {
      }

      public Lock(String var2) {
         this.id = var2;
      }

      public synchronized void release() {
         if (LockManager.verbose) {
            Verbose.log((Object)("Releasing lock on conversation " + this.id));
         }

         this.s.release();
      }
   }
}
