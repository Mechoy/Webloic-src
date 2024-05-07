package weblogic.messaging.saf.internal;

import java.util.ArrayList;
import java.util.Comparator;
import weblogic.messaging.kernel.MessageElement;

public final class SAFMessageCursorComparator implements Comparator {
   private SAFCursorKey[] keys;

   public SAFMessageCursorComparator(ArrayList var1) {
      this.keys = new SAFCursorKey[var1.size()];
      var1.toArray(this.keys);
   }

   public int compare(Object var1, Object var2) {
      MessageElement var3 = (MessageElement)var1;
      MessageElement var4 = (MessageElement)var2;

      for(int var5 = 0; var5 < this.keys.length; ++var5) {
         if (this.keys[var5] != null) {
            long var6 = this.keys[var5].compareKey(var3, var4);
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
