package weblogic.entitlement.util;

import java.util.Iterator;
import java.util.Map;
import weblogic.utils.collections.SecondChanceCacheMap;

public class SecondChanceCache extends SecondChanceCacheMap implements Cache {
   public SecondChanceCache(int var1) {
      super(var1);
   }

   public int getMaximumSize() {
      return super.getCapacity();
   }

   public void setMaximumSize(int var1) {
      throw new UnsupportedOperationException(this.getClass().getName() + ".setMaximumSize(int)");
   }

   public Object lookup(Object var1) {
      throw new UnsupportedOperationException(this.getClass().getName() + ".lookup(Object)");
   }

   public void putOff(Object var1) {
      throw new UnsupportedOperationException(this.getClass().getName() + ".putOff(Object)");
   }

   public Map.Entry remove() {
      throw new UnsupportedOperationException(this.getClass().getName() + ".remove()");
   }

   public Iterator iterator() {
      return this.entrySet().iterator();
   }
}
