package weblogic.socket;

import java.io.IOException;
import weblogic.kernel.Kernel;
import weblogic.utils.Debug;
import weblogic.utils.NestedError;
import weblogic.utils.net.SocketResetException;

final class DevPollSocketMuxer extends ServerSocketMuxer {
   private final DevPollSocketInfo[] sockRecords;
   private final int maxNumberOfFds;
   private final Object pollLock = new String("pollLock");
   private final Object pollSetLock = new String("pollSetLock");
   private static final boolean debug = Debug.getCategory("weblogic.socket.DevPollSocketMuxer").isEnabled();
   static final byte FD_ADD = 2;
   static final byte FD_REM = 4;

   public DevPollSocketMuxer() throws IOException {
      Debug.assertion(Kernel.isServer());
      System.loadLibrary("muxer");

      try {
         this.maxNumberOfFds = initDevPoll() + 1;
      } catch (IOException var2) {
         SocketLogger.logInitPerf();
         throw new NestedError("Could not initialize /dev/poll Performance Pack. Ensure that /dev/poll device exists and is initialized.", var2);
      }

      SocketLogger.logFdCurrent(this.maxNumberOfFds);
      SocketLogger.logTimeStamp(getBuildTime());
      this.initSocketReaderThreads(3, "weblogic.socket.Muxer", "weblogic.DevPollSocketReaders");
      this.sockRecords = new DevPollSocketInfo[this.maxNumberOfFds];
   }

   public void register(MuxableSocket var1) throws IOException {
      DevPollSocketInfo var2 = new DevPollSocketInfo(var1);
      var1.setSocketInfo(var2);
      super.register(var1);
      if (debug) {
         p("Registered ms=" + var1 + " info=" + var2);
      }

   }

   public void read(MuxableSocket var1) {
      SocketInfo var2 = var1.getSocketInfo();
      if (this.initiateIO(var2)) {
         DevPollSocketInfo var3 = (DevPollSocketInfo)var2;
         int var4 = var3.fd;
         synchronized(var3) {
            this.sockRecords[var4] = var3;
            synchronized(this.pollSetLock) {
               try {
                  editPollSet(var4, (byte)2);
               } catch (IOException var10) {
                  throw new NestedError("Error adding FD to pollset " + var3, var10);
               }
            }

            if (debug) {
               p("Read ms=" + var1 + " info=" + var1.getSocketInfo());
            }

         }
      }
   }

   protected void processSockets() {
      while(true) {
         int[][] var1 = (int[][])null;
         synchronized(this.pollLock) {
            try {
               if (debug) {
                  p("processSockets about to poll ........... ");
               }

               var1 = doPoll();
            } catch (ThreadDeath var10) {
               throw var10;
            } catch (Throwable var11) {
               SocketLogger.logUncaughtThrowable(var11);
               continue;
            }

            if (debug) {
               p("processSockets poll returned " + var1);
            }

            if (var1 == null) {
               continue;
            }
         }

         for(int var2 = 0; var2 < var1[0].length; ++var2) {
            int var3 = var1[0][var2];
            if (var3 != 0) {
               int var4 = var1[1][var2];
               DevPollSocketInfo var5 = this.sockRecords[var3];
               if (var5 == null) {
                  SocketLogger.logSocketInfoNotFound(var3, var4);
               } else {
                  MuxableSocket var6 = var5.getMuxableSocket();
                  if (this.completeIO(var6, var5)) {
                     if (var4 == 0) {
                        try {
                           if (debug) {
                              p("processSockets dispatching " + var5);
                           }

                           this.readReadySocket(var6, var5, 0L);
                        } catch (Throwable var9) {
                           if (debug) {
                              p("processSockets dispatching exception " + var5);
                           }

                           this.deliverHasException(var6, var9);
                        }
                     } else {
                        if (debug) {
                           p("processSockets exception " + var5);
                        }

                        this.deliverHasException(var6, new SocketResetException("Error detected on fd " + var5.fd + " revents=" + var4));
                     }
                  }
               }
            }
         }

         try {
            if (DELAYPOLLWAKEUP > 0L) {
               Thread.sleep(DELAYPOLLWAKEUP);
            }
         } catch (InterruptedException var8) {
            Thread.interrupted();
         }
      }
   }

   void cleanupSocket(MuxableSocket var1, SocketInfo var2) {
      this.sockRecords[var2.getFD()] = null;
      super.cleanupSocket(var1, var2);
   }

   private static void p(String var0) {
      SocketLogger.logDebug(Thread.currentThread().getName() + " + + + + + " + var0);
   }

   protected void cancelIo(MuxableSocket var1) {
      super.cancelIo(var1);
      this.cleanupSocket(var1, var1.getSocketInfo());
   }

   private static native int initDevPoll() throws IOException;

   private static native int[][] doPoll() throws IOException;

   static native void editPollSet(int var0, byte var1) throws IOException;

   private static native String getBuildTime();
}
