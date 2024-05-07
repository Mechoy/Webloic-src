package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.TransportExecutable;
import weblogic.jms.dotnet.transport.TransportThreadPool;
import weblogic.utils.collections.NumericKeyHashMap;

class ThreadPoolWrapper {
   static final ThreadPoolWrapper DIRECT = new ThreadPoolWrapper(new DirectThreadPool());
   private final TransportThreadPool poolActual;
   private final OrderLock lock = new OrderLock();
   private NumericKeyHashMap orderings = new NumericKeyHashMap();

   ThreadPoolWrapper(TransportThreadPool var1) {
      this.poolActual = var1;
   }

   void schedule(TransportExecutable var1) {
      this.poolActual.schedule(var1);
   }

   void schedule(TransportExecutable var1, long var2) {
      if (var2 == -1L) {
         this.poolActual.schedule(var1);
      } else {
         Order.schedule(this, var1, var2);
      }

   }

   OrderLock getLock() {
      return this.lock;
   }

   void removeOrder(long var1) {
      this.orderings.remove(var1);
   }

   Order getOrder(long var1) {
      return (Order)this.orderings.get(var1);
   }

   void putOrder(long var1, Order var3) {
      this.orderings.put(var1, var3);
   }
}
