package weblogic.t3.srvr;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.NestedError;

public final class Scavenger implements TimerListener {
   private static final int SCAVENGE_INTERVAL_SECS = 60;
   private static final Hashtable TRASH = new Hashtable();
   private int passCount = 0;

   public Scavenger() {
      byte var1 = 60;
      TimerManagerFactory.getTimerManagerFactory().getTimerManager("Scavenger").scheduleAtFixedRate(this, 0L, (long)(var1 * 1000));
   }

   public void timerExpired(Timer var1) {
      ++this.passCount;
      Hashtable var2 = (Hashtable)TRASH.clone();
      Enumeration var3 = var2.elements();

      while(var3.hasMoreElements()) {
         Scavengable var4 = (Scavengable)var3.nextElement();

         try {
            var4.scavenge(this.passCount);
         } catch (IOException var6) {
            throw new NestedError("IOException in trigger of " + var1, var6);
         }
      }

   }

   public static void addScavengable(String var0, Scavengable var1) {
      TRASH.put(var0, var1);
   }

   public static void removeScavengable(String var0) {
      TRASH.remove(var0);
   }
}
