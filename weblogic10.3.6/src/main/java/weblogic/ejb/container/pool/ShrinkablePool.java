package weblogic.ejb.container.pool;

import java.util.ArrayList;
import java.util.Collection;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.utils.collections.StackPool;

public final class ShrinkablePool extends StackPool implements weblogic.utils.collections.Pool {
   private static final DebugLogger debugLogger;
   private int watermark;
   private int initialObjectsInPool;

   public ShrinkablePool(int var1, int var2) {
      super(var1);
      this.initialObjectsInPool = var2;
      this.watermark = this.initialObjectsInPool;
   }

   public synchronized Object remove() {
      if (this.getPointer() > 0) {
         Object var1 = this.decrementPointerAndGetValue();
         int var2 = this.getPointer();
         this.setValueAt(var2, (Object)null);
         if (var2 < this.watermark) {
            this.watermark = var2;
         }

         return var1;
      } else {
         return null;
      }
   }

   synchronized Collection trim(boolean var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("trimAndResetMark entered.  initialObjectsInPool = " + this.initialObjectsInPool + ", pointer = " + this.getPointer() + ", watermark = " + this.watermark);
      }

      if (this.getPointer() <= this.initialObjectsInPool) {
         return null;
      } else {
         int var2 = this.getPointer();
         int var3 = this.initialObjectsInPool;
         if (var1) {
            var3 = var2 - this.watermark;
         }

         if (var3 < this.initialObjectsInPool) {
            var3 = this.initialObjectsInPool;
         }

         ArrayList var4 = new ArrayList();

         for(int var5 = var3; var5 < var2; ++var5) {
            var4.add(this.getValueAt(var5));
            this.setValueAt(var5, (Object)null);
         }

         this.setPointer(var3);
         this.watermark = var3;
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
}
