package weblogic.socket;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import weblogic.kernel.Kernel;
import weblogic.utils.AssertionError;
import weblogic.utils.collections.Stack;
import weblogic.utils.io.Chunk;
import weblogic.utils.net.SocketResetException;

public final class NTSocketMuxer extends ServerSocketMuxer {
   private static final int ARRAY_CHUNK_SIZE = 1024;
   private static ArrayList socketInfos = new ArrayList(1024);
   private static Stack freeIndexes = new Stack(1024);
   private static int nativeArrayCapacity = 1024;

   public NTSocketMuxer() throws IOException {
      System.loadLibrary("wlntio");
      initNative(10000, 1024, Chunk.CHUNK_SIZE);
      String var1 = getBuildTime();
      SocketLogger.logTimeStamp(var1);
      this.init();
   }

   private void init() {
      setDebug(Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail());
      this.initSocketReaderThreads(2, "weblogic.socket.Muxer", "weblogic.NTSocketReaders");

      for(int var1 = 0; var1 < 1024; ++var1) {
         socketInfos.add((Object)null);
         freeIndexes.push(new Integer(var1));
      }

   }

   public void register(MuxableSocket var1) throws IOException {
      NTSocketInfo var2 = null;

      try {
         var2 = new NTSocketInfo(var1);
      } catch (IOException var4) {
         SocketLogger.logRegisterSocketProblem(var1.toString(), var4);
         throw var4;
      }

      var1.setSocketInfo(var2);
      super.register(var1);
   }

   public void read(MuxableSocket var1) {
      this.internalRead(var1, var1.getSocketInfo());
   }

   private void internalRead(MuxableSocket var1, SocketInfo var2) {
      if (this.initiateIO(var2)) {
         try {
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
               SocketLogger.logDebug("initiateNativeIo for: " + var2);
            }

            initiateNativeIo(((NTSocketInfo)var2).nativeIndex);
         } catch (SocketException var4) {
            Object var3 = var4;
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerException()) {
               SocketLogger.logNTMuxerInitiateIOError(var2.toString(), var4);
            }

            this.completeIO(var1, var2);
            if (SocketResetException.isResetException(var4)) {
               var3 = new SocketResetException(var4);
            }

            this.deliverHasException(var1, (Throwable)var3);
         }

      }
   }

   public void processSockets() {
      IoCompletionData var1 = new IoCompletionData();

      while(true) {
         var1.clear();
         MuxableSocket var2 = null;

         try {
            boolean var3 = getIoCompletionResult(var1);
            int var4 = var1.index;
            NTSocketInfo var5 = (NTSocketInfo)socketInfos.get(var4);
            if (var5 == null) {
               SocketLogger.logNTMuxerSocketInfoNotFound("" + var4, var3);
            } else {
               var2 = var5.getMuxableSocket();
               if (this.completeIO(var2, var5)) {
                  if (var3) {
                     if (var1.numAvailableBytes <= 0) {
                        this.deliverEndOfStream(var2);
                     } else {
                        if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
                           SocketLogger.logDebug("data ready for " + var5 + ": " + var1);
                        }

                        if (this.copyDataFromNativeBuffer(var2, var1)) {
                           if (var2.isMessageComplete()) {
                              var5.messageCompleted();
                              if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
                                 SocketLogger.logDebug("dispatch: " + var5);
                              }

                              var2.dispatch();
                           } else {
                              var5.messageInitiated();
                              this.internalRead(var2, var5);
                           }
                        }
                     }
                  } else {
                     this.deliverHasException(var2, new SocketResetException());
                  }
               }
            }
         } catch (ThreadDeath var6) {
            if (Kernel.isServer()) {
               if (!Kernel.isIntentionalShutdown()) {
                  SocketLogger.logThreadDeath(var6);
               }

               throw var6;
            }
         } catch (Throwable var7) {
            SocketLogger.logUncaughtThrowable(var7);
            if (var2 != null) {
               this.deliverHasException(var2, var7);
            }
         }
      }
   }

   private boolean copyDataFromNativeBuffer(MuxableSocket var1, IoCompletionData var2) {
      NTSocketInfo var3 = (NTSocketInfo)var1.getSocketInfo();
      int var4 = var2.numAvailableBytes;
      Object var5 = null;
      boolean var6 = false;
      int var7 = 0;
      int var8 = var3.nativeIndex;

      while(var4 > 0) {
         byte[] var13 = var1.getBuffer();
         int var14 = var1.getBufferOffset();
         int var9 = var13.length - var14;
         if (var9 < 0) {
            throw new AssertionError("Buffer offset > buffer length for socket ms=" + var3);
         }

         int var10 = Math.min(var9, var4);
         copyData(var13, var14, var10, var8, var7);
         var4 -= var10;

         try {
            var1.incrementBufferOffset(var10);
            var7 += var10;
         } catch (MaxMessageSizeExceededException var12) {
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
               SocketLogger.logDebugException("Exception while copying data for " + var3, var12);
            }

            this.deliverHasException(var1, var12);
            return false;
         }
      }

      return true;
   }

   static int add(NTSocketInfo var0) throws IOException {
      int var1 = getNewIoRecord(var0);

      try {
         initIoRecord(var1, var0.fd);
         return var1;
      } catch (IOException var3) {
         freeIoRecord(var1);
         throw var3;
      }
   }

   static void remove(int var0) {
      freeIoRecord(var0);
   }

   private static int getNewIoRecord(NTSocketInfo var0) {
      synchronized(socketInfos) {
         int var2;
         if (!freeIndexes.isEmpty()) {
            var2 = (Integer)freeIndexes.pop();
            socketInfos.set(var2, var0);
            return var2;
         } else {
            var2 = socketInfos.size();
            socketInfos.add(var2, var0);
            if (var2 == nativeArrayCapacity) {
               addIoRecordArrayChunk();
               nativeArrayCapacity += 1024;
            }

            return var2;
         }
      }
   }

   private static void freeIoRecord(int var0) {
      synchronized(socketInfos) {
         socketInfos.set(var0, (Object)null);
         freeIndexes.push(new Integer(var0));
      }
   }

   private static native void initNative(int var0, int var1, int var2) throws IOException;

   private static native void addIoRecordArrayChunk();

   private static native void initIoRecord(int var0, int var1) throws IOException;

   private static native void initiateNativeIo(int var0) throws SocketException;

   private static native boolean getIoCompletionResult(IoCompletionData var0);

   private static native void copyData(byte[] var0, int var1, int var2, int var3, int var4);

   private static native String getBuildTime();

   private static native void setDebug(boolean var0);

   private static final class IoCompletionData {
      private int fd;
      private int index;
      private int numAvailableBytes;

      private IoCompletionData() {
      }

      private void clear() {
         this.fd = -1;
         this.index = -1;
         this.numAvailableBytes = 0;
      }

      public String toString() {
         return this.getClass().getName() + "[fd=" + this.fd + ", index=" + this.index + ", numAvailableBytes=" + this.numAvailableBytes + "]";
      }

      // $FF: synthetic method
      IoCompletionData(Object var1) {
         this();
      }
   }
}
