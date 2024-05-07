package weblogic.nodemanager.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

class WindowsProcessControl extends ProcessControl {
   WindowsProcessControl() throws UnsatisfiedLinkError {
      System.loadLibrary("nodemanager");
   }

   public String getProcessId() {
      return String.valueOf(this.getProcessId0());
   }

   public boolean killProcess(String var1) {
      try {
         return this.killProcess0(Integer.parseInt(var1));
      } catch (NumberFormatException var3) {
         throw new IllegalArgumentException("Invalid pid format");
      }
   }

   public boolean isProcessAlive(String var1) {
      try {
         return this.isProcessAlive0(Integer.parseInt(var1));
      } catch (NumberFormatException var3) {
         throw new IllegalArgumentException("Invalid pid format");
      }
   }

   public String createProcess(String[] var1, Map var2, File var3, File var4) throws IOException {
      String var5 = null;
      String var6 = null;
      String var7 = null;
      if (var1.length < 1) {
         throw new IndexOutOfBoundsException();
      } else {
         var1[0] = (new File(var1[0])).getPath();
         if (var3 != null) {
            var5 = var3.getAbsolutePath();
         }

         if (var4 != null) {
            var6 = var4.getAbsolutePath();
         }

         if (var2 != null) {
            var7 = this.getEnvironmentBlock(var2);
         }

         int var8 = this.createProcess0((String)null, this.getCommandLine(var1), var7, var5, var6);
         return String.valueOf(var8);
      }
   }

   private String getCommandLine(String[] var1) {
      StringBuffer var2 = new StringBuffer(256);
      var1[0] = (new File(var1[0])).getPath();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var3 > 0) {
            var2.append(' ');
         }

         String var4 = var1[var3];
         if ((var4.indexOf(32) >= 0 || var4.indexOf(9) >= 0) && var4.indexOf(34) < 0) {
            var2.append('"').append(var4).append('"');
         } else {
            var2.append(var4);
         }
      }

      return var2.toString();
   }

   private String getEnvironmentBlock(Map var1) {
      ArrayList var2 = new ArrayList(var1.entrySet());
      Collections.sort(var2, new Comparator() {
         public int compare(Object var1, Object var2) {
            Map.Entry var3 = (Map.Entry)var1;
            Map.Entry var4 = (Map.Entry)var2;
            return ((String)var3.getKey()).compareToIgnoreCase((String)var4.getKey());
         }
      });
      StringBuffer var3 = new StringBuffer(var2.size() * 32);
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Map.Entry var5 = (Map.Entry)var4.next();
         var3.append((String)var5.getKey()).append('=').append((String)var5.getValue()).append('\u0000');
      }

      var3.append('\u0000');
      return var3.toString();
   }

   private native int getProcessId0();

   private native boolean killProcess0(int var1);

   private native boolean isProcessAlive0(int var1);

   private native int createProcess0(String var1, String var2, String var3, String var4, String var5) throws IOException;
}
