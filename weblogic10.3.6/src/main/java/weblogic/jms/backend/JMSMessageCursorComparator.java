package weblogic.jms.backend;

import java.util.ArrayList;
import java.util.Comparator;
import weblogic.messaging.kernel.MessageElement;

public class JMSMessageCursorComparator implements Comparator {
   private BECursorDestinationKey[] keys;

   public JMSMessageCursorComparator(ArrayList var1) {
      this.keys = new BECursorDestinationKey[var1.size()];
      var1.toArray(this.keys);
   }

   public int compare(Object var1, Object var2) {
      MessageElement var3 = (MessageElement)var1;
      MessageElement var4 = (MessageElement)var2;

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
