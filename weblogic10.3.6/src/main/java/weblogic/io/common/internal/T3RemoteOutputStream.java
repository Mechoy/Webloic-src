package weblogic.io.common.internal;

import java.io.IOException;
import weblogic.common.T3Exception;

public final class T3RemoteOutputStream implements T3RemoteConstants, OneWayOutputClient {
   private int bufferSize;
   private int currentBufferNum;
   private boolean closed;
   private WriteResponse writeRes;
   private CommandResponse flushRes;
   private CommandResponse closeRes;
   private byte[] target;
   private int targetOffset;
   private boolean aborted;
   private String abortError;
   private boolean flushRequired;
   private OneWayOutputServer onewayServer;

   public T3RemoteOutputStream(int var1, int var2) throws T3Exception {
      this.bufferSize = var1;
      if (var1 < 1) {
         throw new T3Exception("bufferSize must be positive");
      } else if (var2 < 0) {
         throw new T3Exception("writeBehind cannot be negative");
      } else {
         this.closed = false;
         this.writeRes = new WriteResponse(var2);
         this.flushRes = new CommandResponse();
         this.closeRes = new CommandResponse();
         this.target = null;
         this.currentBufferNum = 0;
         this.aborted = false;
         this.flushRequired = false;
      }
   }

   public void setOneWayRemote(OneWayOutputServer var1) {
      this.onewayServer = var1;
   }

   public synchronized void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("Attempt to write to closed file");
      } else if (this.aborted) {
         throw new IOException(this.abortError);
      } else if (var3 > var1.length - var2) {
         throw new IOException("Insufficient data for write: " + var3 + " requested, " + (var1.length - var2) + " available");
      } else {
         while(var3 > 0) {
            if (this.target == null) {
               this.target = new byte[this.bufferSize];
               this.targetOffset = 0;
            }

            int var4 = Math.min(this.bufferSize - this.targetOffset, var3);
            System.arraycopy(var1, var2, this.target, this.targetOffset, var4);
            this.targetOffset += var4;
            var2 += var4;
            var3 -= var4;
            if (this.targetOffset == this.bufferSize) {
               this.writeRes.waitAround(this.currentBufferNum);
               this.sendWrite(this.currentBufferNum, this.target);
               this.flushRequired = true;
               this.target = null;
               ++this.currentBufferNum;
            }
         }

      }
   }

   public void write(int var1) throws IOException {
      byte[] var2 = new byte[]{(byte)var1};
      this.write(var2, 0, 1);
   }

   public synchronized void flush() throws IOException {
      if (this.closed) {
         throw new IOException("Attempt to flush closed file");
      } else if (this.aborted) {
         throw new IOException(this.abortError);
      } else {
         if (this.target != null && this.targetOffset != 0) {
            this.writeRes.waitAround(this.currentBufferNum);
            byte[] var1 = new byte[this.targetOffset];
            System.arraycopy(this.target, 0, var1, 0, this.targetOffset);
            this.sendWrite(this.currentBufferNum, var1);
            this.flushRequired = true;
            this.target = null;
            ++this.currentBufferNum;
         }

         if (this.flushRequired) {
            this.sendFlush(this.currentBufferNum - 1);
            this.flushRequired = false;
         }

      }
   }

   public void close() throws IOException {
      if (this.aborted) {
         throw new IOException(this.abortError);
      } else {
         if (!this.closed) {
            this.flush();
            this.closed = true;
            this.sendClose();
            this.writeRes.waitAroundExactly(this.currentBufferNum - 1);
         }

      }
   }

   private void cancel(String var1) {
      this.aborted = true;
      this.abortError = var1;
      this.writeRes.cancel(var1);
      this.flushRes.cancel(var1);
      this.closeRes.cancel(var1);
   }

   private void sendWrite(int var1, byte[] var2) {
      this.onewayServer.write(var1, var2);
   }

   private void sendFlush(int var1) throws IOException {
      this.onewayServer.flush(var1);
      this.flushRes.waitAround();
   }

   private void sendClose() throws IOException {
      this.onewayServer.close();
      this.closeRes.waitAround();
   }

   public void writeResult(int var1) {
      this.writeRes.signal(var1);
   }

   public void flushResult() {
      this.flushRes.signal();
   }

   public void closeResult() {
      this.closeRes.signal();
   }

   public void error(Exception var1) {
      this.cancel(var1.getMessage());
   }
}
