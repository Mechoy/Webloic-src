package weblogic.management.scripting.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;

public class WLSTProcess implements Runnable {
   private boolean isErr;
   private String subname = "";
   private InputStream isem;
   private String proName;
   private String log = null;
   private boolean doDrain;
   private static HashMap processes = new HashMap();

   WLSTProcess(boolean var1, InputStream var2, String var3, boolean var4) {
      this.isErr = var1;
      if (this.isErr) {
         this.subname = var3 + " - STDERROR";
      } else {
         this.subname = var3 + " - STDOut";
      }

      this.isem = var2;
      this.proName = var3;
      this.doDrain = var4;
   }

   WLSTProcess(boolean var1, InputStream var2, String var3, boolean var4, String var5) {
      this.isErr = var1;
      if (this.isErr) {
         this.subname = var3 + " - STDERROR";
      } else {
         this.subname = var3 + " - STDOut";
      }

      this.isem = var2;
      this.proName = var3;
      this.doDrain = var4;
      this.log = var5;
   }

   public void run() {
      try {
         BufferedReader var2 = new BufferedReader(new InputStreamReader(this.isem));
         FileOutputStream var3 = null;
         BufferedOutputStream var4 = null;
         String var6 = "";
         PrintStream var5;
         if (this.log != null) {
            var3 = new FileOutputStream(new File(this.log));
            var4 = new BufferedOutputStream(var3);
            var5 = new PrintStream(var4, true);
         } else {
            var5 = System.out;
            var6 = this.proName + ": ";
         }

         try {
            String var1;
            try {
               while((var1 = var2.readLine()) != null) {
                  if (this.doDrain) {
                     var5.println(var6 + var1);
                  }
               }
            } catch (IOException var29) {
               if (this.doDrain) {
                  var5.println(var6 + "IOException when running WLSTProcess");
                  var29.printStackTrace();
               }
            }
         } finally {
            if (this.doDrain) {
               var5.println(var6 + "Stopped draining " + this.proName);
            }

            if (this.log != null) {
               var5.flush();
               var5.close();

               try {
                  var4.flush();
               } catch (IOException var28) {
               }

               try {
                  var4.close();
               } catch (IOException var27) {
               }

               try {
                  var3.flush();
               } catch (IOException var26) {
               }

               try {
                  var3.close();
               } catch (IOException var25) {
               }
            }

         }
      } catch (Exception var31) {
         System.out.println("Error running the WLST process " + var31);
      }

   }

   static void destroyProcesses(String var0) {
      Iterator var1;
      Process var2;
      if (var0 == null) {
         var1 = processes.keySet().iterator();

         while(var1.hasNext()) {
            var2 = (Process)var1.next();
            var2.destroy();
         }
      } else {
         var1 = processes.keySet().iterator();
         var2 = null;

         while(var1.hasNext()) {
            var2 = (Process)var1.next();
            String var3 = (String)processes.get(var2);
            if (var3.equals(var0)) {
               System.out.println("Killing process " + var3);
               var2.destroy();
               break;
            }
         }

         if (var2 != null) {
            processes.remove(var2);
         }
      }

   }

   static Process getProcess(String var0) {
      try {
         Iterator var1 = processes.keySet().iterator();
         Process var2 = null;

         while(var1.hasNext()) {
            var2 = (Process)var1.next();
            String var3 = (String)processes.get(var2);
            if (var3.equals(var0)) {
               return var2;
            }
         }
      } catch (Exception var4) {
         System.out.println("Error getting the process " + var4);
      }

      return null;
   }

   public static void startIOThreads(Process var0, String var1, boolean var2, String var3) {
      Thread var4 = new Thread(new WLSTProcess(false, var0.getInputStream(), var1, var2, var3));
      var4.setDaemon(true);
      var4.start();
      Thread var5 = new Thread(new WLSTProcess(true, var0.getErrorStream(), var1, var2, var3));
      var5.setDaemon(true);
      var5.start();
      processes.put(var0, var1);
   }

   public static void startIOThreads(Process var0, String var1, boolean var2) {
      Thread var3 = new Thread(new WLSTProcess(false, var0.getInputStream(), var1, var2));
      var3.setDaemon(true);
      var3.start();
      Thread var4 = new Thread(new WLSTProcess(true, var0.getErrorStream(), var1, var2));
      var4.setDaemon(true);
      var4.start();
      processes.put(var0, var1);
   }
}
