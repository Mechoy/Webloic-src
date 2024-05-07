package weblogic.io.common.internal;

import java.io.IOException;
import java.io.OutputStream;
import weblogic.common.T3MiscLogger;

public final class T3RemoteOutputStreamProxy implements T3RemoteConstants, OneWayOutputServer {
   private OutputStream os;
   private int nextBufferNum;
   private OneWayOutputClient onewayClient;
   private static final int SERVER_TIMEOUT_MILLSECS = 120000;

   public T3RemoteOutputStreamProxy(String var1, OutputStream var2, int var3, OneWayOutputClient var4) {
      this.os = var2;
      this.onewayClient = var4;
      this.nextBufferNum = 0;
   }

   public synchronized void write(int var1, byte[] var2) {
      while(true) {
         if (var1 != this.nextBufferNum) {
            int var3 = this.nextBufferNum;

            try {
               this.wait(120000L);
            } catch (InterruptedException var5) {
            }

            if (var3 != this.nextBufferNum) {
               continue;
            }

            T3MiscLogger.logWriteTimed(this.nextBufferNum, var1);
            return;
         }

         try {
            this.os.write(var2);
         } catch (IOException var6) {
            this.onewayClient.error(var6);
         }

         this.onewayClient.writeResult(this.nextBufferNum);
         ++this.nextBufferNum;
         this.notifyAll();
         return;
      }
   }

   public synchronized void flush(int var1) {
      while(true) {
         if (this.nextBufferNum <= var1) {
            int var2 = this.nextBufferNum;

            try {
               this.wait(120000L);
            } catch (InterruptedException var4) {
            }

            if (var2 != this.nextBufferNum) {
               continue;
            }

            T3MiscLogger.logFlushTimed(this.nextBufferNum, var1);
            return;
         }

         try {
            this.os.flush();
         } catch (IOException var5) {
            this.onewayClient.error(var5);
         }

         this.onewayClient.flushResult();
         return;
      }
   }

   public synchronized void close() {
      try {
         this.os.close();
         this.onewayClient.closeResult();
      } catch (IOException var2) {
         T3MiscLogger.logCloseException(var2);
      }

   }
}
