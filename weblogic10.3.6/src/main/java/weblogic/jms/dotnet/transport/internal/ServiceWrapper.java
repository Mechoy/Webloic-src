package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.Service;
import weblogic.jms.dotnet.transport.TransportError;

class ServiceWrapper {
   private final ThreadPoolWrapper threadPool;
   private final Service service;
   private final long serviceID;
   private boolean closed;

   ServiceWrapper(long var1, Service var3, ThreadPoolWrapper var4) {
      this.service = var3;
      this.threadPool = var4;
      this.serviceID = var1;
   }

   Service getService() {
      return this.service;
   }

   synchronized void invoke(ReceivedOneWayImpl var1, long var2) {
      if (!this.closed) {
         this.threadPool.schedule(new ServiceExecuteOneWay(this, var1), var2);
      }
   }

   synchronized void invoke(ReceivedTwoWayImpl var1, long var2) {
      if (!this.closed) {
         this.threadPool.schedule(new ServiceExecuteTwoWay(this, var1), var2);
      }
   }

   synchronized void shutdown(TransportError var1) {
      if (!this.closed) {
         this.closed = true;
         if (var1 != null) {
            this.threadPool.schedule(new ServiceExecutePeerGone(this, var1));
         } else {
            this.threadPool.schedule(new ServiceExecuteShutdown(this));
         }

      }
   }

   synchronized void unregister() {
      if (!this.closed) {
         this.closed = true;
         this.threadPool.schedule(new ServiceExecuteUnregister(this));
      }
   }

   public synchronized String toString() {
      return "ServiceWrapper:<" + this.serviceID + "," + this.service + ">";
   }
}
