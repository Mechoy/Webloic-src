package weblogic.messaging.dispatcher;

import java.util.HashMap;
import weblogic.messaging.ID;

public abstract class InvocableManager {
   private HashMap[] INVOCABLE_MAPS;
   private String[] INVOCABLE_STRINGS;
   private int[] invocablesHighCount;
   private int[] invocablesTotalCount;
   private Invocable[] managers;
   public int INVOCABLE_TYPE_MASK;
   public int INVOCABLE_METHOD_MASK;
   public static final int DISPATCHER_MANAGER = 0;
   public static final int DSP_HANDSHAKE_HELLO = 15872;
   public static final int DSP_HANDSHAKE_METHOD_MASK = 16776960;

   protected InvocableManager(HashMap[] var1, String[] var2, int[] var3, int[] var4, int var5, int var6, Invocable[] var7) {
      this.INVOCABLE_MAPS = var1;
      this.INVOCABLE_STRINGS = var2;
      this.invocablesHighCount = var3;
      this.invocablesTotalCount = var4;
      this.INVOCABLE_TYPE_MASK = var5;
      this.INVOCABLE_METHOD_MASK = var6;
      this.managers = var7;
   }

   public void invocableAdd(int var1, Invocable var2) throws Exception {
      HashMap var3 = this.INVOCABLE_MAPS[var1];
      synchronized(var3) {
         Invocable var5 = (Invocable)var3.put(var2.getId(), var2);
         if (var5 == null) {
            if (var3.size() > this.invocablesHighCount[var1]) {
               this.invocablesHighCount[var1] = var3.size();
            }

            int var10002 = this.invocablesTotalCount[var1]++;
            return;
         }

         if (var2 == var5) {
            return;
         }

         var3.put(var5.getId(), var5);
      }

      throw new Exception(this.INVOCABLE_STRINGS[var1] + " already exists");
   }

   public Invocable invocableFind(int var1, ID var2) throws Exception {
      HashMap var3 = this.INVOCABLE_MAPS[var1];
      if (var3 != null) {
         synchronized(var3) {
            Invocable var5 = (Invocable)var3.get(var2);
            if (var5 != null) {
               return var5;
            }
         }
      } else if (this.isManager(var1)) {
         return this.managers[var1];
      }

      throw new Exception(this.INVOCABLE_STRINGS[var1] + " not found");
   }

   public Invocable invocableRemove(int var1, ID var2) {
      HashMap var3 = this.INVOCABLE_MAPS[var1];
      synchronized(var3) {
         return (Invocable)var3.remove(var2);
      }
   }

   public HashMap getInvocableMap(int var1) {
      return this.INVOCABLE_MAPS[var1];
   }

   public int getInvocablesCurrentCount(int var1) {
      return this.INVOCABLE_MAPS[var1].size();
   }

   public int getInvocablesHighCount(int var1) {
      return this.invocablesHighCount[var1];
   }

   public int getInvocablesTotalCount(int var1) {
      return this.invocablesTotalCount[var1];
   }

   protected abstract boolean isManager(int var1);

   public void addManager(int var1, Invocable var2) {
      this.managers[var1] = var2;
   }
}
