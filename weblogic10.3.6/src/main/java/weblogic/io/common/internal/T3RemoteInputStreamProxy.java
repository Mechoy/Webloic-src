package weblogic.io.common.internal;

import java.io.IOException;
import java.io.InputStream;
import weblogic.common.T3MiscLogger;

public final class T3RemoteInputStreamProxy implements T3RemoteConstants, OneWayInputServer {
   private InputStream is;
   private int bufferSize;
   private int nextBufferNum;
   private int requestedBufferNum;
   private boolean atEOF;
   private OneWayInputClient onewayClient;

   public T3RemoteInputStreamProxy(String var1, InputStream var2, int var3, int var4, OneWayInputClient var5) {
      this.is = var2;
      this.bufferSize = var3;
      this.onewayClient = var5;
      this.nextBufferNum = 0;
      this.requestedBufferNum = -1;
      this.atEOF = false;
      this.read(var4);
   }

   public synchronized void read(int var1) {
      if (var1 > this.requestedBufferNum) {
         for(this.requestedBufferNum = var1; this.nextBufferNum <= this.requestedBufferNum && !this.atEOF; ++this.nextBufferNum) {
            int var2 = 0;

            int var3;
            byte[] var4;
            for(var4 = new byte[this.bufferSize]; var2 < this.bufferSize; var2 += var3) {
               try {
                  var3 = this.is.read(var4, var2, this.bufferSize - var2);
               } catch (IOException var6) {
                  System.out.println("Calling onewayClient's error method.");
                  this.onewayClient.error(var6);
                  return;
               }

               if (var3 == -1) {
                  this.atEOF = true;
                  byte[] var5 = new byte[var2];
                  System.arraycopy(var4, 0, var5, 0, var2);
                  var4 = var5;
                  break;
               }
            }

            this.onewayClient.readResult(this.nextBufferNum, var4);
         }
      }

   }

   public synchronized void skip(long var1) {
      try {
         this.onewayClient.skipResult(this.is.skip(var1));
      } catch (IOException var4) {
         this.onewayClient.error(var4);
      }

   }

   public synchronized void close() {
      try {
         this.is.close();
      } catch (IOException var2) {
         T3MiscLogger.logCloseException(var2);
      }

   }
}
