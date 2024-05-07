package weblogic.socket;

import java.io.IOException;
import weblogic.kernel.ExecuteThread;
import weblogic.kernel.ExecuteThreadManager;
import weblogic.kernel.Kernel;
import weblogic.rjvm.HeartbeatMonitor;
import weblogic.socket.internal.SocketEnvironment;
import weblogic.socket.utils.DynaQueue;
import weblogic.socket.utils.QueueFullException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.utils.concurrent.Latch;
import weblogic.work.WorkManagerFactory;

final class JavaSocketMuxer extends ServerSocketMuxer {
   private static final boolean ASSERT = false;
   private static final int QUEUE_BLOCK_SIZE = 25;
   private static final int SOCKET_WAIT_TIMEOUT = 2000;
   private static final String CLIENT_SOCKET_READERS_QUEUE_NAME = "weblogic.JavaSocketReaders";
   private final Latch warningLock = new Latch();
   private final DynaQueue sockQueue = new DynaQueue("SockMuxQ", 25);
   private int numSocketReaders = 0;
   private int maxSocketReaders = -1;
   private int curSoTimeoutMillis = -1;
   private ExecuteThreadManager socketReaderQueue = null;
   private ExecuteThreadManager clientExecuteQueue;
   private int numClientSocketReaders = 0;
   private static final int MIN_CLIENT_EXECUTE_THREAD_COUNT = 0;
   private static final int MAX_CLIENT_EXECUTE_THREAD_COUNT = 15;
   private static final boolean jsse = SocketEnvironment.getSocketEnvironment().isJSSE();
   private static final int MAX_SLEEP_SUM = 1000;
   private static final int SLEEP_MULTIPLE = 100;
   private int prevNum = 0;
   private int curNum = 1;

   protected JavaSocketMuxer() throws IOException {
      this.curSoTimeoutMillis = config.getSocketReaderTimeoutMaxMillis();
      this.init();
   }

   private void init() {
      ExecuteThreadManager var1 = Kernel.getExecuteThreadManager("weblogic.socket.Muxer");
      if (var1 != null && var1.getName().equalsIgnoreCase("weblogic.socket.Muxer")) {
         this.socketReaderQueue = var1;
         this.maxSocketReaders = var1.getExecuteThreadCount();
         SocketLogger.logAllocSocketReaders(this.maxSocketReaders);

         for(int var2 = 0; var2 < this.maxSocketReaders; ++var2) {
            Kernel.execute(new SocketReaderRequest(), "weblogic.socket.Muxer");
         }
      }

   }

   private int getMaxSocketReaders() {
      if (this.maxSocketReaders == -1) {
         int var1 = config.getThreadPoolPercentSocketReaders();
         int var2 = var1 * config.getThreadPoolSize() / 100;
         this.maxSocketReaders = Math.max(2, var2);
      }

      return this.maxSocketReaders;
   }

   public void read(MuxableSocket var1) {
      if (this.initiateIO(var1.getSocketInfo())) {
         this.internalRead(var1, false);
      }
   }

   private void internalRead(MuxableSocket var1, Boolean var2) {
      try {
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
            SocketLogger.logDebug("internalRead for: " + var1.getSocketInfo());
         }

         if (var1 instanceof ClientSSLFilterImpl && jsse) {
            if (var2) {
               int var3 = this.calculateSleepTime() * 100;

               try {
                  Thread.sleep(0L, var3);
               } catch (InterruptedException var5) {
               }
            } else {
               this.resetSleepTime();
            }
         }

         this.sockQueue.put(var1);
      } catch (QueueFullException var6) {
         SocketLogger.logSocketQueueFull(var6);
         this.closeSocket(var1);
      }

   }

   private int calculateSleepTime() {
      if (this.curNum >= 1000) {
         return 1000;
      } else {
         this.curNum += this.prevNum;
         this.prevNum = this.curNum - this.prevNum;
         return this.curNum >= 1000 ? 1000 : this.curNum;
      }
   }

   private void resetSleepTime() {
      this.prevNum = 0;
      this.curNum = 1;
   }

   protected void handleReadTimeout(MuxableSocket var1) {
      this.internalRead(var1, true);
   }

   protected void readCompleted(MuxableSocket var1) {
      this.completeIO(var1, var1.getSocketInfo());
   }

   public void register(MuxableSocket var1) throws IOException {
      var1.setSocketInfo(new SocketInfo(var1));
      synchronized(this.sockets) {
         super.register(var1);
         if (this.socketReaderQueue == null) {
            int var3 = this.getNumSockets();
            if (var3 > this.numSocketReaders + this.numClientSocketReaders) {
               if (this.numSocketReaders < this.getMaxSocketReaders()) {
                  WorkManagerFactory.getInstance().getSystem().schedule(new SocketReaderRequest());
                  ++this.numSocketReaders;
                  if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
                     SocketLogger.logDebug("Starting socket reader: '" + this.numSocketReaders + "', " + "sockets: '" + var3 + "'");
                  }
               } else if (!Kernel.isServer() && this.createClientThread()) {
                  Kernel.execute(new SocketReaderRequest(), "weblogic.JavaSocketReaders");
               } else if (this.warningLock.tryLock()) {
                  SocketLogger.logSocketConfig(var3, this.getMaxSocketReaders());
               }
            }

         }
      }
   }

   private boolean createClientThread() {
      if (this.numClientSocketReaders == 15) {
         return false;
      } else {
         if (this.clientExecuteQueue == null) {
            this.createClientExecuteQueue();
         }

         if (++this.numClientSocketReaders > this.clientExecuteQueue.getExecuteThreadCount()) {
            this.clientExecuteQueue.setThreadCount(this.numClientSocketReaders);
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
               SocketLogger.logDebug("Created thread in extra client execute queue, total number of extra client threads: " + this.numClientSocketReaders);
            }
         }

         return true;
      }
   }

   private void createClientExecuteQueue() {
      Kernel.addExecuteQueue("weblogic.JavaSocketReaders", 0, 0, 15);
      this.clientExecuteQueue = Kernel.getExecuteThreadManager("weblogic.JavaSocketReaders");
   }

   private boolean shouldBreakProcessSockets(boolean var1) {
      if (this.socketReaderQueue != null) {
         return false;
      } else {
         synchronized(this.sockets) {
            if (this.numSocketReaders + this.numClientSocketReaders > this.getNumSockets()) {
               if (var1) {
                  --this.numClientSocketReaders;
               } else {
                  --this.numSocketReaders;
               }

               if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
                  SocketLogger.logDebug("Decrementing socket reader: " + this.numSocketReaders + ", client socket reader: " + this.numClientSocketReaders + ", sockets: " + this.getNumSockets());
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   private boolean isClientExecuteThread() {
      return !Kernel.isServer() && ((ExecuteThread)Thread.currentThread()).getExecuteThreadManager().getName().equalsIgnoreCase("weblogic.JavaSocketReaders");
   }

   protected void processSockets() {
      boolean var1 = this.isClientExecuteThread();

      while(!this.shouldBreakProcessSockets(var1)) {
         MuxableSocket var2 = null;
         SocketInfo var3 = null;

         try {
            for(var2 = (MuxableSocket)this.sockQueue.get(); var2 == null; var2 = (MuxableSocket)this.sockQueue.getW(2000)) {
               if (this.shouldBreakProcessSockets(var1)) {
                  return;
               }
            }

            var3 = var2.getSocketInfo();
            var2.setSoTimeout(this.getSoTimeout());
            this.readReadySocket(var2, var3, (long)this.getSoTimeout());
         } catch (ThreadDeath var5) {
            if (Kernel.isServer()) {
               if (!Kernel.isIntentionalShutdown()) {
                  SocketLogger.logThreadDeath(var5);
               }

               throw var5;
            }

            if (var2 != null && !var2.getSocketInfo().markedClose) {
               this.internalRead(var2, false);
            }
         } catch (Throwable var6) {
            this.deliverHasException(var3.getMuxableSocket(), var6);
         }
      }

   }

   private int getSoTimeout() {
      return this.curSoTimeoutMillis;
   }

   private void updateSoTimeout() {
      int var1 = this.getNumSockets();
      int var2 = this.numSocketReaders;
      int var3 = config.getSocketReaderTimeoutMinMillis();
      int var4 = config.getSocketReaderTimeoutMaxMillis();
      if (var2 != 0 && var1 != 0) {
         int var5 = HeartbeatMonitor.periodLengthMillis();
         this.curSoTimeoutMillis = var5 * var2 / var1;
         this.curSoTimeoutMillis = Math.min(this.curSoTimeoutMillis, var4);
         this.curSoTimeoutMillis = Math.max(this.curSoTimeoutMillis, var3);
      } else {
         this.curSoTimeoutMillis = var4;
      }

   }

   protected TimerListener createTimeoutTrigger() {
      return new JavaTimerListenerImpl();
   }

   protected class JavaTimerListenerImpl extends SocketMuxer.TimerListenerImpl {
      protected JavaTimerListenerImpl() {
         super();
      }

      public void timerExpired(Timer var1) {
         super.timerExpired(var1);
         JavaSocketMuxer.this.updateSoTimeout();
      }
   }
}
