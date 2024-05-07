package weblogic.health;

import java.util.ArrayList;
import java.util.Iterator;
import weblogic.platform.GCListener;
import weblogic.platform.GarbageCollectionEvent;
import weblogic.platform.VM;

public final class LowMemoryNotificationService implements GCListener {
   private static final boolean DEBUG = false;
   private static final int MINOR_GC_COUNT_THRESHOLD = 5;
   private static final MemoryEvent LOW_MEMORY_EVENT = new MemoryEvent(1);
   private static final MemoryEvent OK_MEMORY_EVENT = new MemoryEvent(0);
   private static final ArrayList list = new ArrayList();
   private static boolean initialized;
   private final int lowThreshold;
   private final int highThreshold;
   private boolean lowMemoryThresholdReached;
   private int consecutiveMinorGCCount;

   private LowMemoryNotificationService(int var1, int var2) {
      this.lowThreshold = var1;
      this.highThreshold = var2;
   }

   public static synchronized void addMemoryListener(MemoryListener var0) {
      list.add(var0);
   }

   public static synchronized void removeMemoryListener(MemoryListener var0) {
      list.remove(var0);
   }

   public static synchronized void initialize(int var0, int var1) {
      if (!initialized) {
         VM.getVM().addGCListener(new LowMemoryNotificationService(var0, var1));
         initialized = true;
      }
   }

   private static final synchronized void sendMemoryEvent(MemoryEvent var0) {
      Iterator var1 = list.iterator();

      while(var1.hasNext()) {
         MemoryListener var2 = (MemoryListener)var1.next();
         var2.memoryChanged(var0);
      }

   }

   public void onGarbageCollection(GarbageCollectionEvent var1) {
      if (var1 != null) {
         long var2 = Runtime.getRuntime().freeMemory();
         long var4 = Runtime.getRuntime().totalMemory();
         int var6 = (int)(var2 * 100L / var4);
         synchronized(this) {
            if (var6 < this.lowThreshold && !this.lowMemoryThresholdReached && this.acceptGCEvent(var1)) {
               this.lowMemoryThresholdReached = true;
               sendMemoryEvent(LOW_MEMORY_EVENT);
            } else {
               if (var6 > this.highThreshold) {
                  this.consecutiveMinorGCCount = 0;
                  if (!this.lowMemoryThresholdReached) {
                     return;
                  }

                  this.lowMemoryThresholdReached = false;
                  sendMemoryEvent(OK_MEMORY_EVENT);
               }

            }
         }
      }
   }

   private boolean acceptGCEvent(GarbageCollectionEvent var1) {
      if (var1.getEventType() != 0 && this.consecutiveMinorGCCount < 5) {
         ++this.consecutiveMinorGCCount;
         return false;
      } else {
         this.consecutiveMinorGCCount = 0;
         return true;
      }
   }

   private static void debug(String var0) {
      System.out.println("[LowMemoryNotificationService] " + var0);
   }
}
