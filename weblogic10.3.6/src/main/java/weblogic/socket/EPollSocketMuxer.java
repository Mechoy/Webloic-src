package weblogic.socket;

import java.io.IOException;
import jrockit.ext.epoll.EPoll;
import jrockit.ext.epoll.EPollEventBuffer;
import weblogic.kernel.Kernel;
import weblogic.utils.Debug;
import weblogic.utils.net.SocketResetException;

final class EPollSocketMuxer extends ServerSocketMuxer {
   private static Object pollBufferLock = new String("PollBufferLock");
   private Object pollLock = new String("PollLock");
   private static int[] freeRecords;
   private static int firstFree = -1;
   private static EPollSocketInfo[] sockRecords;
   private static int nRecords;
   private static EPoll epoll;
   private static final boolean debug = false;

   public EPollSocketMuxer() throws IOException {
      Debug.assertion(Kernel.isServer());
      byte var1 = 100;
      epoll = new EPoll();
      this.initSocketReaderThreads(3, "weblogic.socket.Muxer", "weblogic.EPollSocketReaders");
      sockRecords = new EPollSocketInfo[var1];
      freeRecords = new int[var1];
   }

   public void register(MuxableSocket var1) throws IOException {
      EPollSocketInfo var2 = new EPollSocketInfo(var1);
      var1.setSocketInfo(var2);
      super.register(var1);
   }

   public void read(MuxableSocket var1) {
      SocketInfo var2 = var1.getSocketInfo();
      if (this.initiateIO(var2)) {
         EPollSocketInfo var3 = (EPollSocketInfo)var2;
         synchronized(var3) {
            try {
               int var5 = var3.getIndex();
               if (var5 == -1) {
                  var5 = add(var3);
                  epoll.epollAddOneshot(var3.getFD(), var5);
               } else {
                  epoll.epollEnableOneshot(var3.getFD(), var5);
               }
            } catch (IOException var7) {
               this.deliverHasException(var1, var7);
            }

         }
      }
   }

   private static int add(EPollSocketInfo var0) {
      synchronized(pollBufferLock) {
         int var1 = firstFree;
         if (var1 == -1) {
            var1 = nRecords++;
            if (var1 == sockRecords.length) {
               int[] var3 = new int[freeRecords.length * 2];
               EPollSocketInfo[] var4 = new EPollSocketInfo[sockRecords.length * 2];
               System.arraycopy(sockRecords, 0, var4, 0, sockRecords.length);
               System.arraycopy(freeRecords, 0, var3, 0, freeRecords.length);
               freeRecords = var3;
               sockRecords = var4;
            }
         } else {
            firstFree = freeRecords[var1];
         }

         sockRecords[var1] = var0;
         var0.setIndex(var1);
         return var1;
      }
   }

   static void remove(int var0, int var1) {
      synchronized(pollBufferLock) {
         freeRecords[var0] = firstFree;
         sockRecords[var0] = null;
         firstFree = var0;
      }
   }

   protected void processSockets() {
      int var1 = 0;
      int var2 = 128;
      EPollEventBuffer var3 = EPollEventBuffer.allocEventBuffer(var2);

      while(true) {
         int var4;
         while(true) {
            synchronized(this.pollLock) {
               try {
                  var4 = epoll.epollWait(var3);
                  break;
               } catch (ThreadDeath var10) {
                  throw var10;
               } catch (Throwable var11) {
                  SocketLogger.logUncaughtThrowable(var11);
               }
            }
         }

         for(int var5 = 0; var5 < var4; ++var5) {
            int var6 = var3.getID(var5);
            int var7 = var3.getEvent(var5);
            EPollSocketInfo var8 = sockRecords[var6];
            if (var8 == null) {
               SocketLogger.logSocketInfoNotFound(var6, var7);
            } else {
               this.dataReceived(var7, var8);
            }
         }

         if (var4 == var3.getSize() && var2 < 2048) {
            ++var1;
            if (var1 > 8) {
               var1 = 0;
               var2 = var3.getSize() * 2;
               EPollEventBuffer.freeEventBuffer(var3);
               var3 = EPollEventBuffer.allocEventBuffer(var2);
            }
         } else {
            var1 = 0;
         }

         try {
            if (DELAYPOLLWAKEUP > 0L) {
               Thread.sleep(DELAYPOLLWAKEUP);
            }
         } catch (InterruptedException var9) {
            Thread.interrupted();
         }
      }
   }

   private void dataReceived(int var1, EPollSocketInfo var2) {
      MuxableSocket var3 = var2.getMuxableSocket();
      if (this.completeIO(var3, var2)) {
         if (var1 == 1) {
            try {
               this.readReadySocket(var3, var2, 0L);
            } catch (Throwable var5) {
               this.deliverHasException(var3, var5);
            }
         } else {
            this.deliverHasException(var3, new SocketResetException("Error detected on fd " + var2.fd + " revents=" + var1));
         }

      }
   }

   private static void p(String var0) {
      SocketLogger.logDebug(Thread.currentThread().getName() + " + + + + + " + var0);
   }

   protected void cancelIo(MuxableSocket var1) {
      try {
         epoll.epollRemove(((NativeSocketInfo)var1.getSocketInfo()).fd);
      } catch (IOException var3) {
      }

      super.cancelIo(var1);
      this.cleanupSocket(var1, var1.getSocketInfo());
   }
}
