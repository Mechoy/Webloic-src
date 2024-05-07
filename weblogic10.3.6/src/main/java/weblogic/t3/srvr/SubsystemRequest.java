package weblogic.t3.srvr;

import weblogic.management.runtime.ServerStates;
import weblogic.server.ServerService;
import weblogic.server.ServiceFailureException;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

final class SubsystemRequest extends WorkAdapter implements ServerStates {
   private ServerService slc;
   private Throwable t;
   private boolean done;
   private final boolean concurrent;
   private int action;

   private SubsystemRequest(ServerService var1, boolean var2) {
      this.slc = var1;
      this.concurrent = var2;
   }

   SubsystemRequest(ServerService var1) {
      this(var1, false);
   }

   SubsystemRequest() {
      this((ServerService)null, false);
   }

   void setRequest(ServerService var1) {
      this.slc = var1;
   }

   public void start(long var1) throws ServiceFailureException {
      this.action(6, var1);
   }

   public void run() {
      try {
         switch (this.action) {
            case 3:
               this.slc.halt();
               break;
            case 4:
               this.slc.stop();
               break;
            case 5:
            default:
               throw new ServiceFailureException("Unknown ServerLifeCycle action");
            case 6:
               this.slc.start();
         }

         this.notify(true);
      } catch (Throwable var2) {
         this.notify(var2);
      }

   }

   private void action(int var1, long var2) throws ServiceFailureException {
      try {
         this.action = var1;
         WorkManagerFactory.getInstance().getSystem().schedule(this);
         if (!this.concurrent) {
            this.rendezvouz(var2);
         }
      } finally {
         this.t = null;
         this.done = false;
      }

   }

   private synchronized void rendezvouz(long var1) throws ServiceFailureException {
      long var3 = System.currentTimeMillis();

      while(this.notDone()) {
         try {
            if (var1 != 0L && System.currentTimeMillis() - var3 >= var1) {
               this.slc.halt();
               throw ServerServicesManager.STARTUP_TIMED_OUT;
            }

            this.wait(var1);
         } catch (InterruptedException var6) {
         }
      }

      if (this.t != null) {
         if (this.t instanceof ServiceFailureException) {
            throw (ServiceFailureException)this.t;
         }

         if (this.t instanceof RuntimeException) {
            throw (RuntimeException)this.t;
         }

         if (this.t instanceof Error) {
            throw (Error)this.t;
         }
      }

   }

   synchronized void notify(Throwable var1) {
      this.t = var1;
      this.notify();
   }

   private synchronized void notify(boolean var1) {
      this.done = true;
      this.notify();
   }

   private final boolean notDone() {
      return !this.done && this.t == null;
   }
}
