package weblogic.deploy.service.internal;

import weblogic.deploy.common.Debug;
import weblogic.utils.UnsyncCircularQueue;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public abstract class RequestManager {
   private ServiceRequestQueue incomingRequestQueue = null;

   public RequestManager(String var1) {
      this.incomingRequestQueue = new ServiceRequestQueue(var1);
   }

   public final synchronized void scheduleNextRequest() {
      this.serviceNextRequest();
   }

   public final synchronized void addRequest(ServiceRequest var1) {
      this.incomingRequestQueue.add(var1);
      this.serviceNextRequest();
   }

   protected final void debug(String var1) {
      Debug.serviceDebug(var1);
   }

   protected final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   private void serviceNextRequest() {
      ServiceRequest var1 = this.incomingRequestQueue.getNextQueuedRequest();
      if (var1 != null) {
         if (WorkManagerFactory.isInitialized()) {
            WorkManager var2 = WorkManagerFactory.getInstance().getSystem();
            var2.schedule(var1);
         } else {
            var1.run();
         }
      }

   }

   private class ServiceRequestQueue {
      private UnsyncCircularQueue q = null;
      private String queueName;

      ServiceRequestQueue(String var2) {
         this.q = new UnsyncCircularQueue();
         this.queueName = var2;
      }

      void add(ServiceRequest var1) {
         this.q.put(var1);
         if (RequestManager.this.isDebugEnabled()) {
            RequestManager.this.debug("Adding request '" + var1.toString() + "' to '" + this.queueName + "' queue whose size is now '" + this.size() + "'");
         }

      }

      ServiceRequest getNextQueuedRequest() {
         if (this.q.empty()) {
            return null;
         } else {
            ServiceRequest var1 = (ServiceRequest)this.q.get();
            if (RequestManager.this.isDebugEnabled()) {
               RequestManager.this.debug("ServiceRequest on queue '" + this.queueName + "' returning request '" + var1.toString() + "' to be serviced - queue's size now is '" + this.size() + "'");
            }

            return var1;
         }
      }

      String getName() {
         return this.queueName;
      }

      int size() {
         return this.q.size();
      }
   }
}
