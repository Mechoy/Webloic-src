package weblogic.messaging.path;

import java.util.Map;
import weblogic.common.CompletionRequest;

class BasicAsyncMapImpl implements AsyncMap {
   private Map map;

   BasicAsyncMapImpl(Map var1) {
      this.map = var1;
   }

   public void putIfAbsent(Object var1, Object var2, CompletionRequest var3) {
      Object var4;
      try {
         synchronized(this.map) {
            if (!this.map.containsKey(var1)) {
               var4 = this.map.put(var1, var2);
            } else {
               var4 = this.map.get(var1);
            }
         }
      } catch (RuntimeException var8) {
         notifyCaller(var3, var8);
         throw var8;
      } catch (Error var9) {
         notifyCaller(var3, var9);
         throw var9;
      }

      var3.setResult(var4);
   }

   private static void notifyCaller(CompletionRequest var0, Throwable var1) {
      synchronized(var0) {
         if (var0.hasResult()) {
            return;
         }
      }

      var0.setResult(var1);
   }

   public void put(Object var1, Object var2, CompletionRequest var3) {
      Object var4;
      try {
         synchronized(this.map) {
            var4 = this.map.put(var1, var2);
         }
      } catch (RuntimeException var8) {
         notifyCaller(var3, var8);
         throw var8;
      } catch (Error var9) {
         notifyCaller(var3, var9);
         throw var9;
      }

      var3.setResult(var4);
   }

   public void get(Object var1, CompletionRequest var2) {
      Object var3;
      try {
         synchronized(this.map) {
            var3 = this.map.get(var1);
         }
      } catch (RuntimeException var7) {
         notifyCaller(var2, var7);
         throw var7;
      } catch (Error var8) {
         notifyCaller(var2, var8);
         throw var8;
      }

      var2.setResult(var3);
   }

   public void remove(Object var1, Object var2, CompletionRequest var3) {
      Boolean var4;
      try {
         synchronized(this.map) {
            Object var5 = this.map.get(var1);
            if (var5 == var2 || var5 != null && var5.equals(var2)) {
               this.map.remove(var1);
               var4 = Boolean.TRUE;
            } else {
               var4 = Boolean.FALSE;
            }
         }
      } catch (RuntimeException var9) {
         notifyCaller(var3, var9);
         throw var9;
      } catch (Error var10) {
         notifyCaller(var3, var10);
         throw var10;
      }

      var3.setResult(var4);
   }

   public void close(CompletionRequest var1) {
      Boolean var2;
      try {
         synchronized(this.map) {
            this.map.clear();
         }

         var2 = Boolean.TRUE;
      } catch (RuntimeException var6) {
         notifyCaller(var1, var6);
         throw var6;
      } catch (Error var7) {
         notifyCaller(var1, var7);
         throw var7;
      }

      var1.setResult(var2);
   }
}
