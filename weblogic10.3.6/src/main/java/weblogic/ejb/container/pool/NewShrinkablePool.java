package weblogic.ejb.container.pool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;

public class NewShrinkablePool {
   private static final DebugLogger debugLogger;
   private AtomicReference head = new AtomicReference();
   private final int initialObjectsInPool;
   private AtomicInteger capacity;
   private AtomicInteger currentSize;
   private AtomicInteger watermark;

   public NewShrinkablePool(int var1, int var2) {
      this.capacity = new AtomicInteger(var1);
      this.initialObjectsInPool = var2;
      this.watermark = new AtomicInteger(var2);
      this.currentSize = new AtomicInteger(0);
   }

   public void setCapacity(int var1) {
      this.capacity.set(var1);
   }

   public int getCapacity() {
      return this.capacity.get();
   }

   public boolean add(Object var1) {
      Node var2 = new Node(var1);

      while(this.currentSize.get() < this.capacity.get()) {
         Node var3 = (Node)this.head.get();
         var2.next = var3;
         if (this.head.compareAndSet(var3, var2)) {
            this.currentSize.getAndIncrement();
            return true;
         }
      }

      return false;
   }

   public Object remove() {
      Node var1;
      Node var2;
      do {
         var1 = (Node)this.head.get();
         if (var1 == null) {
            return null;
         }

         var2 = var1.next;
      } while(!this.head.compareAndSet(var1, var2));

      int var3 = this.currentSize.decrementAndGet();
      if (var3 < this.watermark.get()) {
         this.watermark.set(var3);
      }

      return var1.item;
   }

   public int size() {
      return this.currentSize.get();
   }

   public Collection trim(int var1) {
      ArrayList var2 = new ArrayList();
      if (var1 > 0) {
         for(int var3 = 0; var3 < var1 && this.currentSize.get() > this.initialObjectsInPool; ++var3) {
            Object var4 = this.remove();
            if (var4 == null) {
               break;
            }

            var2.add(var4);
         }
      }

      return var2;
   }

   public boolean isEmpty() {
      return this.head.get() == null;
   }

   Collection trim(boolean var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("trimAndResetMark entered.  initialObjectsInPool = " + this.initialObjectsInPool + ", currentSize = " + this.currentSize.get() + ", watermark = " + this.watermark.get());
      }

      if (this.currentSize.get() <= this.initialObjectsInPool) {
         return null;
      } else {
         int var2 = this.currentSize.get();
         int var3 = this.initialObjectsInPool;
         if (var1) {
            var3 = var2 - this.watermark.get();
         }

         if (var3 < this.initialObjectsInPool) {
            var3 = this.initialObjectsInPool;
         }

         List var4 = (List)this.trim(var2 - var3);
         this.watermark.set(var3);
         if (debugLogger.isDebugEnabled()) {
            debug("trimAndResetMark exiting. new pointer = " + var3);
         }

         return var4;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[FencedPool] " + var0);
   }

   static {
      debugLogger = EJBDebugService.poolingLogger;
   }

   static class Node {
      final Object item;
      Node next;

      public Node(Object var1) {
         this.item = var1;
      }
   }
}
