package weblogic.jms.backend;

import java.util.Comparator;
import java.util.List;
import weblogic.jms.common.MessageImpl;

public final class BEMessageComparator implements Comparator {
   private BEDestinationKey[] keys;

   BEMessageComparator(List var1) {
      this.keys = new BEDestinationKey[var1.size()];
      var1.toArray(this.keys);
   }

   boolean isDefault() {
      return this.keys.length == 1 && this.keys[0].isDefault();
   }

   public int compare(Object var1, Object var2) {
      MessageImpl var3 = (MessageImpl)var1;
      MessageImpl var4 = (MessageImpl)var2;

      for(int var5 = 0; var5 < this.keys.length; ++var5) {
         if (this.keys[var5] != null) {
            long var6 = this.keys[var5].compareKey(var3, var4, false);
            if (var6 < 0L) {
               return -1;
            }

            if (var6 > 0L) {
               return 1;
            }
         }
      }

      return 0;
   }
}
