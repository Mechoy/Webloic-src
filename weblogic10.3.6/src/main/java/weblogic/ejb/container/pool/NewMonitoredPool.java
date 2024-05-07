package weblogic.ejb.container.pool;

import java.util.Collection;
import weblogic.utils.concurrent.atomic.AtomicFactory;
import weblogic.utils.concurrent.atomic.AtomicLong;

public final class NewMonitoredPool {
   private final NewShrinkablePool delegate;
   private final AtomicLong accesses = AtomicFactory.createAtomicLong();
   private final AtomicLong misses = AtomicFactory.createAtomicLong();

   public NewMonitoredPool(NewShrinkablePool var1) {
      this.delegate = var1;
   }

   public int getFreeCount() {
      return this.size();
   }

   public long getAccessCount() {
      return this.accesses.get();
   }

   public long getMissCount() {
      return this.misses.get();
   }

   public void setCapacity(int var1) {
      this.delegate.setCapacity(var1);
   }

   public int getCapacity() {
      return this.delegate.getCapacity();
   }

   public Object remove() {
      this.accesses.incrementAndGet();
      Object var1 = this.delegate.remove();
      if (var1 == null) {
         this.misses.incrementAndGet();
      }

      return var1;
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public boolean add(Object var1) {
      return this.delegate.add(var1);
   }

   public int size() {
      return this.delegate.size();
   }

   public Collection trim(boolean var1) {
      return this.delegate.trim(var1);
   }

   public Collection trim(int var1) {
      return this.delegate.trim(var1);
   }
}
