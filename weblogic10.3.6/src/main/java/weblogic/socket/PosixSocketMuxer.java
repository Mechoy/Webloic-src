package weblogic.socket;

import java.io.IOException;
import java.net.SocketException;
import weblogic.kernel.Kernel;
import weblogic.utils.NestedError;
import weblogic.utils.net.SocketResetException;

final class PosixSocketMuxer extends ServerSocketMuxer {
   private final PosixSocketInfo.FdStruct[] fdStructs;
   private final int maxNumberOfFds;
   private int numberOfFds = 0;
   private final Object pollLock = new Object() {
   };

   public PosixSocketMuxer() throws IOException {
      try {
         System.loadLibrary("muxer");
         initNative();
         setDebug(Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail());
      } catch (IncompatibleVMException var2) {
         throw var2;
      } catch (UnsatisfiedLinkError var3) {
         throw var3;
      } catch (Throwable var4) {
         SocketLogger.logInitPerf();
         throw new NestedError("Could not initialize POSIX Performance Pack", var4);
      }

      SocketLogger.logFdLimit(getSoftFdLimit(), getHardFdLimit());
      this.maxNumberOfFds = getCurrentFdLimit();
      SocketLogger.logFdCurrent(this.maxNumberOfFds);
      SocketLogger.logTimeStamp(getBuildTime());
      this.initSocketReaderThreads(3, "weblogic.socket.Muxer", "weblogic.PosixSocketReaders");
      this.fdStructs = new PosixSocketInfo.FdStruct[this.maxNumberOfFds * 2];
   }

   public void register(MuxableSocket var1) throws IOException {
      var1.setSocketInfo(new PosixSocketInfo(var1));
      super.register(var1);
   }

   public void read(MuxableSocket var1) {
      SocketInfo var2 = var1.getSocketInfo();
      if (this.initiateIO(var2)) {
         PosixSocketInfo.FdStruct var3 = ((PosixSocketInfo)var2).fdStruct;
         var3.status = 0;
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
            SocketLogger.logDebug("initiate read for: " + var3);
         }

         synchronized(this.fdStructs) {
            if (this.numberOfFds == this.maxNumberOfFds * 2) {
               SocketLogger.logPosixMuxerMaxFdExceededError(this.maxNumberOfFds);
               return;
            }

            this.fdStructs[this.numberOfFds++] = var3;
            this.fdStructs.notify();
         }

         wakeupPoll();
      }
   }

   protected void processSockets() {
      PosixSocketInfo.FdStruct[] var1 = new PosixSocketInfo.FdStruct[this.maxNumberOfFds * 2];
      int var2 = 0;

      while(true) {
         while(true) {
            try {
               synchronized(this.pollLock) {
                  int var3;
                  synchronized(this.fdStructs) {
                     while(true) {
                        if (this.numberOfFds != 0) {
                           var3 = this.numberOfFds;
                           break;
                        }

                        this.fdStructs.wait();
                     }
                  }

                  if (!poll(this.fdStructs, var3)) {
                     continue;
                  }

                  synchronized(this.fdStructs) {
                     int var6 = 0;

                     while(var6 < var3) {
                        PosixSocketInfo.FdStruct var7 = this.fdStructs[var6];
                        if (var7.status == 0) {
                           ++var6;
                           if (var6 != this.numberOfFds) {
                              continue;
                           }
                           break;
                        } else {
                           var1[var2++] = var7;
                           if (var6 != --this.numberOfFds) {
                              this.fdStructs[var6] = this.fdStructs[this.numberOfFds];
                              this.fdStructs[this.numberOfFds] = null;
                           } else {
                              this.fdStructs[var6] = null;
                              break;
                           }
                        }
                     }
                  }
               }

               for(int var4 = 0; var4 < var2; ++var4) {
                  PosixSocketInfo.FdStruct var5 = var1[var4];
                  var1[var4] = null;
                  PosixSocketInfo var16 = var5.info;
                  MuxableSocket var17 = var16.getMuxableSocket();
                  if (this.completeIO(var17, var16)) {
                     if (var5.status == 1) {
                        try {
                           this.readReadySocket(var17, var16, 0L);
                        } catch (Throwable var10) {
                           this.deliverHasException(var16.getMuxableSocket(), var10);
                        }
                     } else if (var5.status == 2) {
                        this.deliverHasException(var17, new SocketException("Error in poll for fd=" + var16.fd + ", revents=" + var5.revents));
                     } else if (var5.status == 3) {
                        this.deliverHasException(var17, new SocketResetException("Error in poll for fd=" + var16.fd + ", revents=" + var5.revents));
                     }
                  }
               }

               var2 = 0;
            } catch (ThreadDeath var14) {
               throw var14;
            } catch (Throwable var15) {
               SocketLogger.logUncaughtThrowable(var15);
            }
         }
      }
   }

   private static native void initNative() throws IncompatibleVMException;

   private static native boolean poll(PosixSocketInfo.FdStruct[] var0, int var1) throws IOException;

   private static native void wakeupPoll();

   private static native int getSoftFdLimit();

   private static native int getHardFdLimit();

   private static native int getCurrentFdLimit();

   private static native String getBuildTime();

   private static native void setDebug(boolean var0);
}
