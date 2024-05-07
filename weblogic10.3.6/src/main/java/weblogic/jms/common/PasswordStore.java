package weblogic.jms.common;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;

public final class PasswordStore {
   private long currentHandle;
   private HashMap store;
   private char[] key;
   private static final char[] DEFAULT_KEY = new char[]{'B', 'E', 'A', '0', '1'};

   public PasswordStore() {
      this(DEFAULT_KEY);
   }

   public PasswordStore(char[] var1) {
      this.currentHandle = Long.MIN_VALUE;
      this.store = new HashMap();
      if (var1 == null || var1.length <= 0) {
         var1 = DEFAULT_KEY;
      }

      this.key = new char[var1.length];
      System.arraycopy(var1, 0, this.key, 0, var1.length);
   }

   public Object storePassword(Object var1) throws GeneralSecurityException {
      StoreData var2;
      if (var1 == null) {
         var2 = new StoreData(2, var1);
      } else {
         String var3;
         if (var1 instanceof String) {
            var3 = SecHelper.encryptString(this.key, (String)var1);
            var2 = new StoreData(0, var3);
         } else if (var1 instanceof char[]) {
            var3 = SecHelper.encryptPassword(this.key, (char[])((char[])var1));
            var2 = new StoreData(1, var3);
         } else {
            var2 = new StoreData(2, var1);
         }
      }

      synchronized(this.store) {
         Long var4 = new Long((long)(this.currentHandle++));
         this.store.put(var4, var2);
         return var4;
      }
   }

   public Object retrievePassword(Object var1) throws GeneralSecurityException, IOException {
      if (var1 == null) {
         return null;
      } else if (!(var1 instanceof Long)) {
         throw new GeneralSecurityException("Invalid handle type: " + var1.getClass().getName());
      } else {
         Long var2 = (Long)var1;
         StoreData var3;
         synchronized(this.store) {
            if (!this.store.containsKey(var2)) {
               return null;
            }

            var3 = (StoreData)this.store.get(var2);
         }

         if (var3.getType() == 1) {
            return SecHelper.decryptString(this.key, (String)var3.getData());
         } else {
            return var3.getType() == 0 ? new String(SecHelper.decryptString(this.key, (String)var3.getData())) : var3.getData();
         }
      }
   }

   public void removePassword(Object var1) {
      if (var1 != null) {
         if (var1 instanceof Long) {
            synchronized(this.store) {
               this.store.remove(var1);
            }
         }
      }
   }

   private static class StoreData {
      private static final int TYPE_STRING = 0;
      private static final int TYPE_CHARARRAY = 1;
      private static final int TYPE_OTHER = 2;
      private int type;
      private Object data;

      private StoreData(int var1, Object var2) {
         this.type = var1;
         this.data = var2;
      }

      private int getType() {
         return this.type;
      }

      private Object getData() {
         return this.data;
      }

      // $FF: synthetic method
      StoreData(int var1, Object var2, Object var3) {
         this(var1, var2);
      }
   }
}
