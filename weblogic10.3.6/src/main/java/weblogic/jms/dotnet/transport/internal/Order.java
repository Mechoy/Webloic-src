package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.TransportExecutable;

class Order implements TransportExecutable {
   private ExecutableWrapper first;
   private ExecutableWrapper last;
   private final long ordering;
   private final ThreadPoolWrapper poolWrapper;

   private Order(ThreadPoolWrapper var1, long var2) {
      this.poolWrapper = var1;
      this.ordering = var2;
   }

   private synchronized void add(ExecutableWrapper var1) {
      if (this.first == null) {
         this.first = var1;
         this.last = var1;
      } else {
         this.last.next = var1;
         this.last = var1;
      }
   }

   private synchronized ExecutableWrapper remove() {
      if (this.first == null) {
         return null;
      } else {
         ExecutableWrapper var1 = this.first;
         this.first = this.first.next;
         if (this.first == null) {
            this.last = null;
         }

         return var1;
      }
   }

   private synchronized boolean isEmpty() {
      return this.first == null;
   }

   static void schedule(ThreadPoolWrapper var0, TransportExecutable var1, long var2) {
      ExecutableWrapper var4 = new ExecutableWrapper(var1);
      Order var5;
      synchronized(var0.getLock()) {
         var5 = var0.getOrder(var2);
         if (var5 != null) {
            var5.add(var4);
            return;
         }

         var5 = new Order(var0, var2);
         var5.add(var4);
         var0.putOrder(var2, var5);
      }

      var0.schedule(var5);
   }

   public void execute() {
      for(int var1 = 0; var1 < 10; ++var1) {
         ExecutableWrapper var2 = this.remove();
         var2.getTask().execute();
         if (this.isEmpty()) {
            synchronized(this.poolWrapper.getLock()) {
               if (this.isEmpty()) {
                  this.poolWrapper.removeOrder(this.ordering);
                  return;
               }
            }
         }
      }

      this.poolWrapper.schedule(this);
   }
}
