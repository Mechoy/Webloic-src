package weblogic.ejb.container.manager;

import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb20.manager.SimpleKey;

public final class SimpleKeyGenerator implements KeyGenerator {
   private long base_id;
   private long count;

   public void setup(BeanInfo var1) {
      long var2 = (long)System.identityHashCode(this);
      this.base_id = var2 << 32;
   }

   public Object nextKey() {
      long var1;
      synchronized(this) {
         var1 = (long)(this.count++);
      }

      return new SimpleKey(this.base_id ^ var1);
   }
}
