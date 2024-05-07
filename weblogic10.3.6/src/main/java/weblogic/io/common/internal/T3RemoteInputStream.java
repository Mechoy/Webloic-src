package weblogic.io.common.internal;

import java.io.IOException;
import weblogic.common.T3Exception;

public final class T3RemoteInputStream implements T3RemoteConstants, OneWayInputClient {
   private int bufferSize;
   private int readAhead;
   private int currentBufferNum;
   private int nextBufferNum;
   private boolean closed;
   private WaitHashtable buffers;
   private SkipResponse skipRes;
   private byte[] source;
   private int sourceOffset;
   private String abortError;
   private boolean stopReadAhead;
   private OneWayInputServer onewayServer;
   private byte[] tmpb = new byte[1];

   public T3RemoteInputStream(int var1, int var2) throws T3Exception {
      this.bufferSize = var1;
      this.readAhead = var2;
      if (var1 < 1) {
         throw new T3Exception("bufferSize must be positive");
      } else if (var2 < 0) {
         throw new T3Exception("readAhead cannot be negative");
      } else {
         this.closed = false;
         this.skipRes = new SkipResponse();
         this.buffers = new WaitHashtable(2 * (var2 + 1), 1.0F);
         this.source = null;
         this.currentBufferNum = 0;
         this.stopReadAhead = false;
         this.nextBufferNum = var2 + 1;
      }
   }

   public void setOneWayRemote(OneWayInputServer var1) {
      this.onewayServer = var1;
   }

   public synchronized int read(byte[] var1, int var2, int var3) throws IOException {
      int var5 = 0;
      if (this.closed) {
         throw new IOException("Attempt to read from closed file");
      } else if (var3 > var1.length - var2) {
         throw new IOException("Insufficient space for read: " + var3 + " requested, " + (var1.length - var2) + " available");
      } else {
         while(var3 > 0) {
            if (this.source == null) {
               this.source = (byte[])((byte[])this.buffers.removeWait(new Integer(this.currentBufferNum)));
               if (this.source == null) {
                  if (this.abortError == null) {
                     this.abortError = this.buffers.getError();
                  }

                  throw new IOException(this.abortError);
               }

               this.sourceOffset = 0;
            }

            if (this.source.length == 0) {
               if (var5 == 0) {
                  var5 = -1;
               }
               break;
            }

            int var4 = Math.min(this.source.length - this.sourceOffset, var3);
            System.arraycopy(this.source, this.sourceOffset, var1, var2, var4);
            var2 += var4;
            this.sourceOffset += var4;
            var5 += var4;
            var3 -= var4;
            if (this.sourceOffset == this.source.length) {
               this.source = null;
               ++this.currentBufferNum;
               if (!this.stopReadAhead) {
                  this.sendRead(this.nextBufferNum);
                  ++this.nextBufferNum;
               }
            }
         }

         return var5;
      }
   }

   public synchronized int read() throws IOException {
      int var1 = this.read(this.tmpb, 0, 1);
      return var1 == -1 ? -1 : this.tmpb[0] & 255;
   }

   public synchronized long skip(long var1) throws IOException {
      int var4 = 0;
      if (this.closed) {
         throw new IOException("Attempt to skip in closed file");
      } else {
         int var5 = this.currentBufferNum;

         while(var1 > 0L && this.currentBufferNum < this.nextBufferNum) {
            if (this.source == null) {
               this.source = (byte[])((byte[])this.buffers.removeWait(new Integer(this.currentBufferNum)));
               if (this.source == null) {
                  throw new IOException(this.abortError);
               }

               this.sourceOffset = 0;
            }

            if (this.source.length == 0) {
               break;
            }

            int var3 = (int)Math.min((long)(this.source.length - this.sourceOffset), var1);
            this.sourceOffset += var3;
            var4 += var3;
            var1 -= (long)var3;
            if (this.sourceOffset == this.source.length) {
               this.source = null;
               ++this.currentBufferNum;
            }
         }

         if (var1 > 0L) {
            var1 = this.sendSkip(var1);
            var4 = (int)((long)var4 + var1);
         }

         if (var5 < this.currentBufferNum && !this.stopReadAhead) {
            this.nextBufferNum = this.currentBufferNum + this.readAhead + 1;
            this.sendRead(this.nextBufferNum - 1);
         }

         return (long)var4;
      }
   }

   public synchronized int available() throws IOException {
      int var1 = 0;
      if (this.closed) {
         throw new IOException("Attempt to check availability of closed file");
      } else {
         if (this.source != null) {
            var1 = this.source.length - this.sourceOffset;
         }

         int var2 = this.currentBufferNum + 1;

         while(true) {
            byte[] var3 = (byte[])((byte[])this.buffers.get(new Integer(var2)));
            if (var3 == null) {
               return var1;
            }

            var1 += var3.length;
            ++var2;
         }
      }
   }

   public synchronized void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         if (this.source == null || this.source.length > 0) {
            while(this.currentBufferNum < this.nextBufferNum) {
               this.source = (byte[])((byte[])this.buffers.removeWait(new Integer(this.currentBufferNum)));
               if (this.source == null || this.source.length == 0) {
                  break;
               }

               ++this.currentBufferNum;
            }
         }

         this.sendClose();
      }

   }

   private void readCallback(int var1, byte[] var2) {
      this.buffers.put(new Integer(var1), var2);
      if (var2.length < this.bufferSize) {
         this.stopReadAhead = true;
         if (var2.length > 0) {
            this.buffers.put(new Integer(var1 + 1), new byte[0]);
         }
      }

   }

   private void cancel(String var1) {
      this.stopReadAhead = true;
      this.abortError = var1;
      this.buffers.cancel(var1);
      this.skipRes.cancel(var1);
   }

   private long sendSkip(long var1) throws IOException {
      this.onewayServer.skip(var1);

      try {
         Object var3 = this.skipRes.waitAround();
         if (var3 != null) {
            return (Long)var3;
         } else {
            throw new IOException("Error waiting for result from skip");
         }
      } catch (IOException var4) {
         throw var4;
      }
   }

   private void sendRead(int var1) {
      this.onewayServer.read(var1);
   }

   private void sendClose() {
      this.onewayServer.close();
   }

   public void readResult(int var1, byte[] var2) {
      this.readCallback(var1, var2);
   }

   public void skipResult(long var1) {
      this.skipRes.signal(new Long(var1));
   }

   public void error(Exception var1) {
      String var2 = var1.getMessage();
      this.cancel(var2);
   }
}
