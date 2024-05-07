package weblogic.t3.srvr;

import java.io.DataInputStream;
import java.io.IOException;
import weblogic.kernel.T3SrvrLogger;

final class T3SrvrConsole {
   public void processCommands() {
      Runtime var1 = Runtime.getRuntime();
      DataInputStream var2 = new DataInputStream(System.in);
      String var3 = null;

      while(true) {
         while(true) {
            while(true) {
               while(true) {
                  while(true) {
                     do {
                        do {
                           try {
                              var3 = var2.readLine();
                           } catch (IOException var5) {
                              T3SrvrLogger.logReadCommandIOException(var5);
                           }
                        } while(var3 == null);
                     } while(var3.trim().length() == 0);

                     T3SrvrLogger.logReadWhichCommand(var3);
                     if (!var3.equalsIgnoreCase("GC")) {
                        if (!var3.equalsIgnoreCase("PROFOFF")) {
                           if (!var3.equalsIgnoreCase("PROFON")) {
                              if (var3.equalsIgnoreCase("SHUT")) {
                                 try {
                                    T3Srvr.getT3Srvr().requestShutdownFromConsole();
                                    return;
                                 } catch (SecurityException var6) {
                                    T3SrvrLogger.logConsoleShutSecurityException(var6);
                                 }
                              } else if (var3.equalsIgnoreCase("KILL")) {
                                 var1.exit(0);
                              } else {
                                 T3SrvrLogger.logConsoleNoSuchCommand(var3);
                              }
                           } else {
                              T3SrvrLogger.logConsoleProfilingEnabled();
                              var1.traceInstructions(true);
                              var1.traceMethodCalls(true);
                           }
                        } else {
                           T3SrvrLogger.logConsoleProfilingDisabled();
                           var1.traceInstructions(false);
                           var1.traceMethodCalls(false);
                        }
                     } else {
                        this.doGarbageCollection();
                     }
                  }
               }
            }
         }
      }
   }

   private void doGarbageCollection() {
      Runtime var1 = Runtime.getRuntime();
      long var2 = var1.totalMemory();
      long var4 = var1.freeMemory();
      T3SrvrLogger.logConsoleGCBefore(var4, var2, 100L * var4 / var2);
      var1.gc();
      var1.runFinalization();
      var2 = var1.totalMemory();
      var4 = var1.freeMemory();
      T3SrvrLogger.logConsoleGCAfter(var4, var2, 100L * var4 / var2);
   }
}
