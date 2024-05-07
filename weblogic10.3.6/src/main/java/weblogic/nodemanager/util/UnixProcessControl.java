package weblogic.nodemanager.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class UnixProcessControl extends ProcessControl {
   private static final int REAPER_INTERVAL_MS = 10000;
   private boolean initialized;
   private Set<Integer> childPIDs;
   private Timer reaperTimer;

   UnixProcessControl() throws UnsatisfiedLinkError {
      System.loadLibrary("nodemanager");
      this.childPIDs = new HashSet();
      this.reaperTimer = new Timer("NM Reaper", true);
      this.reaperTimer.schedule(new ReaperTask(), 10000L, 10000L);
   }

   public String getProcessId() {
      return String.valueOf(this.getProcessId0());
   }

   public boolean changeFileOwnership(File var1, String var2, String var3) {
      if (var2 == null && var3 == null) {
         return true;
      } else {
         return var1 != null ? this.changeFileOwnership0(var1.getAbsolutePath(), var2, var3) : false;
      }
   }

   public boolean killProcess(String var1) {
      try {
         return this.killProcess0(Integer.parseInt(var1));
      } catch (NumberFormatException var3) {
         throw new IllegalArgumentException("Invalid pid format: " + var1);
      }
   }

   public boolean isProcessAlive(String var1) {
      int var2 = Integer.parseInt(var1);
      synchronized(this.childPIDs) {
         if (this.childPIDs.contains(var2)) {
            int var4 = this.waitNonBlocking0(var2);
            if (var4 == var2) {
               this.childPIDs.remove(var2);
               return false;
            }
         }
      }

      try {
         return this.isProcessAlive0(Integer.parseInt(var1));
      } catch (NumberFormatException var6) {
         throw new IllegalArgumentException("Invalid pid format: " + var1);
      }
   }

   public String createProcess(String[] var1, Map var2, File var3, File var4) throws IOException {
      String var5 = null;
      String var6 = null;
      byte[] var7 = null;
      if (var1.length < 1) {
         throw new IndexOutOfBoundsException();
      } else {
         if (var3 != null) {
            var5 = var3.getAbsolutePath();
         }

         if (var4 != null) {
            var6 = var4.getAbsolutePath();
         }

         if (var2 != null) {
            var7 = this.getEnvironmentBlock(var2);
         }

         int var8 = this.createProcess0(this.getArgumentBlock(var1), var1.length, var7, var2 != null ? var2.size() : 0, var5, var6);
         if (var8 <= 0) {
            throw new IOException("Invalid pid [" + var8 + "] returned while creating process for " + Arrays.toString(var1));
         } else {
            this.addChild(var8);
            return String.valueOf(var8);
         }
      }
   }

   private byte[] getArgumentBlock(String[] var1) {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2 += var1[var3].getBytes().length;
      }

      ByteBuffer var5 = ByteBuffer.wrap(new byte[var2]);

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var5.put(var1[var4].getBytes());
         var5.put((byte)0);
      }

      return var5.array();
   }

   private byte[] getEnvironmentBlock(Map var1) {
      int var2 = var1.size() * 2;

      Map.Entry var4;
      for(Iterator var3 = var1.entrySet().iterator(); var3.hasNext(); var2 += ((String)var4.getValue()).getBytes().length) {
         var4 = (Map.Entry)var3.next();
         var2 += ((String)var4.getKey()).getBytes().length;
      }

      ByteBuffer var6 = ByteBuffer.wrap(new byte[var2]);
      Iterator var7 = var1.entrySet().iterator();

      while(var7.hasNext()) {
         Map.Entry var5 = (Map.Entry)var7.next();
         var6.put(((String)var5.getKey()).getBytes());
         var6.put((byte)61);
         var6.put(((String)var5.getValue()).getBytes());
         var6.put((byte)0);
      }

      return var6.array();
   }

   private void addChild(int var1) {
      synchronized(this.childPIDs) {
         this.childPIDs.add(var1);
      }
   }

   private native int getProcessId0();

   private native boolean killProcess0(int var1);

   private native boolean isProcessAlive0(int var1);

   private native boolean changeFileOwnership0(String var1, String var2, String var3);

   private native int createProcess0(byte[] var1, int var2, byte[] var3, int var4, String var5, String var6) throws IOException;

   private native int waitNonBlocking0(int var1);

   class ReaperTask extends TimerTask {
      public void run() {
         synchronized(UnixProcessControl.this.childPIDs) {
            if (UnixProcessControl.this.childPIDs.size() != 0) {
               Iterator var2 = UnixProcessControl.this.childPIDs.iterator();

               while(var2.hasNext()) {
                  Integer var3 = (Integer)var2.next();
                  int var4 = UnixProcessControl.this.waitNonBlocking0(var3);
                  if (var4 != 0 && var4 != -1 && var4 == var3) {
                     var2.remove();
                  }
               }

            }
         }
      }
   }
}
