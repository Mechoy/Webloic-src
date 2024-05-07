package weblogic.wsee.deploy;

import weblogic.store.ObjectHandler;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.Verbose;

public class VersioningHelper {
   private WsStorage storage = WsStorageFactory.getStorage("versioncount", (ObjectHandler)null, false, true);
   private static final boolean verbose = Verbose.isVerbose(VersioningHelper.class);
   private static final VersioningHelper singleton = new VersioningHelper();

   public static void updateCount(String var0, String var1, int var2) {
      singleton.updateVersionCount(var0, var1, var2);
   }

   public static long getCount(String var0, String var1) {
      return singleton.getVersionCount(var0, var1);
   }

   public static void removeRecord(String var0, String var1) {
      singleton.removeVersionRecord(var0, var1);
   }

   private VersioningHelper() {
      if (verbose) {
         Verbose.log((Object)("Created version helper storage  class loader = " + this.storage.getClass().getClassLoader()));
      }

   }

   private synchronized void updateVersionCount(String var1, String var2, int var3) {
      String var4 = var1 + "#" + (var2 == null ? "" : var2);
      if (verbose) {
         Verbose.log((Object)("Updating version count for " + var4 + " by " + var3));
      }

      long var5 = this.getVersionCount(var1, var2);
      var5 += (long)var3;
      if (verbose) {
         Verbose.log((Object)("New count = " + var5));
      }

      try {
         if (var5 <= 0L) {
            if (verbose) {
               Verbose.log((Object)("Removing key " + var4 + " from store"));
            }

            this.storage.persistentRemove(var4);
         } else {
            if (verbose) {
               Verbose.log((Object)("Updating key " + var4 + " with count " + var5));
            }

            this.storage.persistentPut(var4, new Long(var5));
         }
      } catch (PersistentStoreException var8) {
         if (verbose) {
            Verbose.log((Object)("Caught " + var8 + " updating instance count of " + var4 + "by " + var3));
         }
      }

   }

   private synchronized long getVersionCount(String var1, String var2) {
      String var3 = var1 + "#" + (var2 == null ? "" : var2);
      if (verbose) {
         Verbose.log((Object)("Getting version count for " + var3));
      }

      try {
         Long var4 = (Long)this.storage.persistentGet(var3);
         long var5 = 0L;
         if (var4 != null) {
            var5 = var4;
            if (var5 <= 0L) {
               var5 = 0L;
            }
         }

         if (verbose) {
            Verbose.log((Object)("Returning count = " + var5 + " for key " + var3));
         }

         return var5;
      } catch (PersistentStoreException var7) {
         if (verbose) {
            Verbose.log((Object)("Caught " + var7 + "getting version count for " + var3));
         }

         return 0L;
      }
   }

   private synchronized void removeVersionRecord(String var1, String var2) {
      String var3 = var1 + "#" + (var2 == null ? "" : var2);

      try {
         if (this.storage.persistentGet(var3) != null) {
            this.storage.persistentRemove(var3);
         }
      } catch (PersistentStoreException var5) {
         if (verbose) {
            Verbose.log((Object)("Caught " + var5 + " removing version record for " + var3));
         }
      }

   }
}
