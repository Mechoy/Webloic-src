package weblogic.messaging.saf.internal;

import java.util.ArrayList;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.runtime.MessageCursorDelegate;
import weblogic.messaging.runtime.CursorRuntimeImpl;
import weblogic.messaging.runtime.OpenDataConverter;

public final class SAFMessageCursorDelegate extends MessageCursorDelegate {
   public SAFMessageCursorDelegate(CursorRuntimeImpl var1, OpenDataConverter var2, Cursor var3, OpenDataConverter var4, int var5) {
      super(var1, var2, var3, var4, var5);
   }

   public long sort(long var1, String[] var3, Boolean[] var4) {
      this.updateAccessTime();
      Object var5 = null;
      if (var1 != -1L) {
         this.cursorIterator.seek(var1);
      }

      ArrayList var6 = new ArrayList();
      if (var3 != null) {
         for(int var7 = 0; var7 < var3.length; ++var7) {
            boolean var8 = true;
            if (var4 != null && var7 < var4.length && !var4[var7]) {
               var8 = false;
            }

            var6.add(new SAFCursorKey(var3[var7], var8));
         }
      }

      this.cursorIterator.setComparator(new SAFMessageCursorComparator(var6));
      this.cursorIterator.rewind();
      if (var5 != null) {
         this.cursorIterator.seek((MessageElement)var5);
         return this.cursorIterator.getPosition();
      } else {
         return 0L;
      }
   }
}
