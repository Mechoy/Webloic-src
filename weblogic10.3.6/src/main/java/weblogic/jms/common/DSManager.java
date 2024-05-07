package weblogic.jms.common;

import java.util.HashMap;

public final class DSManager {
   private static DSManager dsManager;
   private final HashMap dsMap = new HashMap();

   private DSManager() {
   }

   public static synchronized DSManager manager() {
      if (dsManager != null) {
         return dsManager;
      } else {
         dsManager = new DSManager();
         return dsManager;
      }
   }

   public synchronized void add(DurableSubscription var1) {
      String var2 = var1.getName();
      this.dsMap.put(var2, var1);
   }

   public synchronized DurableSubscription lookup(String var1) {
      return (DurableSubscription)this.dsMap.get(var1);
   }

   public synchronized void remove(DurableSubscription var1) {
      String var2 = var1.getName();
      this.dsMap.remove(var2);
   }
}
