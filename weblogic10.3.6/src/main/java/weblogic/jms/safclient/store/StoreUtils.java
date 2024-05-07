package weblogic.jms.safclient.store;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import javax.jms.JMSException;
import weblogic.store.PersistentStoreException;
import weblogic.store.StoreWritePolicy;
import weblogic.store.io.file.FileStoreIO;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.store.xa.internal.PersistentStoreXAImpl;

public final class StoreUtils {
   private static final String STORE_PFX = "SAFSTORE";
   private static final String STORE_APX = "V";
   private static int storeNum;
   private static Object lock = new Object();
   private static HashMap stores = new HashMap();

   private static PersistentStoreXA makeStore(String var0, String var1) throws JMSException {
      if (var0 != null && var1 != null) {
         String var2 = var0 + "_RESOURCE";

         try {
            FileStoreIO var3 = new FileStoreIO(var0, var1, true);
            return new PersistentStoreXAImpl(var0, var3, var2);
         } catch (PersistentStoreException var4) {
            throw new JMSException(var4.getMessage());
         }
      } else {
         throw new JMSException("Name and directory name may not be null");
      }
   }

   private static int getStoreNumber(File var0) {
      if (var0.exists() && var0.isDirectory()) {
         String[] var1 = var0.list(new FileFilter());
         if (var1 != null && var1.length > 0) {
            int var2 = 0;

            Integer var6;
            while(true) {
               if (var2 >= var1.length) {
                  synchronized(lock) {
                     return storeNum++;
                  }
               }

               String var3 = var1[var2];
               int var4 = var3.indexOf(86);
               if (var4 > "SAFSTORE".length()) {
                  String var5 = var3.substring("SAFSTORE".length(), "SAFSTORE".length() + (var4 - "SAFSTORE".length()));

                  try {
                     var6 = new Integer(var5);
                     break;
                  } catch (NumberFormatException var15) {
                  }
               }

               ++var2;
            }

            int var7 = var6;
            synchronized(lock) {
               if (storeNum <= var7) {
                  storeNum = var7 + 1;
               }

               return var7;
            }
         } else {
            synchronized(lock) {
               return storeNum++;
            }
         }
      } else {
         synchronized(lock) {
            return storeNum++;
         }
      }
   }

   public static void initStores(File var0, File var1, String var2) throws JMSException {
      String var3 = "SAFSTORE" + getStoreNumber(var1) + "V";
      PersistentStoreXA var4 = makeStore(var3, var1.toString());

      try {
         HashMap var5 = new HashMap();
         var5.put("SynchronousWritePolicy", StoreWritePolicy.getPolicy(var2));
         var4.open(var5);
      } catch (PersistentStoreException var8) {
         throw new weblogic.jms.common.JMSException(var8);
      }

      synchronized(stores) {
         stores.put(var0, var4);
      }
   }

   public static PersistentStoreXA getStore(File var0) {
      synchronized(stores) {
         return (PersistentStoreXA)stores.get(var0);
      }
   }

   public static void removeStore(File var0) {
      synchronized(stores) {
         stores.remove(var0);
      }
   }

   private static class FileFilter implements FilenameFilter {
      private FileFilter() {
      }

      public boolean accept(File var1, String var2) {
         if (var2 == null) {
            return false;
         } else {
            return var2.startsWith("SAFSTORE");
         }
      }

      // $FF: synthetic method
      FileFilter(Object var1) {
         this();
      }
   }
}
