package weblogic.ejb.container.pool;

import java.util.Collection;
import java.util.Iterator;
import weblogic.utils.concurrent.atomic.AtomicFactory;
import weblogic.utils.concurrent.atomic.AtomicLong;

public final class MonitoredPool implements Collection, weblogic.utils.collections.Pool {
   private final ShrinkablePool delegate;
   private final AtomicLong accesses = AtomicFactory.createAtomicLong();
   private final AtomicLong misses = AtomicFactory.createAtomicLong();

   public MonitoredPool(ShrinkablePool var1) {
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

   public boolean contains(Object var1) {
      return this.delegate.contains(var1);
   }

   public Object[] toArray() {
      return this.delegate.toArray();
   }

   public Object[] toArray(Object[] var1) {
      return this.delegate.toArray(var1);
   }

   public boolean add(Object var1) {
      return this.delegate.add(var1);
   }

   public boolean remove(Object var1) {
      return this.delegate.remove(var1);
   }

   public boolean containsAll(Collection var1) {
      return this.delegate.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      return this.delegate.addAll(var1);
   }

   public boolean removeAll(Collection var1) {
      return this.delegate.removeAll(var1);
   }

   public boolean retainAll(Collection var1) {
      return this.delegate.retainAll(var1);
   }

   public void clear() {
      this.delegate.clear();
   }

   public int size() {
      return this.delegate.size();
   }

   public Iterator iterator() {
      return this.delegate.iterator();
   }

   public Collection trim(boolean var1) {
      return this.delegate.trim(var1);
   }
}
