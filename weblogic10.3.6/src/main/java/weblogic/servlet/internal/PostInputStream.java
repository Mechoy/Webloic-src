package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import weblogic.servlet.HTTPLogger;

final class PostInputStream extends InputStream {
   private static final int SIZE = 512;
   private byte[] buf;
   private InputStream in;
   private int pos;
   private int count;
   private int contentLen;
   private boolean isNotChunkTransfer = true;
   private int nread = 0;
   private long timeReading = 0L;
   private int maxPostTime = -1;
   private int maxPostSize = -1;
   private MuxableSocketHTTP ms;
   private boolean timedOut = false;

   PostInputStream(MuxableSocketHTTP var1, int var2, byte[] var3, int var4, int var5) {
      this.ms = var1;
      this.in = var1.getSocketInputStream();
      this.contentLen = var2;
      if (this.contentLen < 0) {
         this.isNotChunkTransfer = false;
      }

      int var6 = var5 - var4;
      if (var6 > 0) {
         this.buf = new byte[Math.max(var6, 512)];
         System.arraycopy(var3, var4, this.buf, 0, var6);
         this.count = var6;
      } else {
         this.buf = new byte[512];
      }

      this.maxPostTime = var1.getHttpServer().getMaxPostTimeSecs() * 1000;
      this.maxPostSize = var1.getHttpServer().getMaxPostSize();
   }

   public String toString() {
      return super.toString() + " - contentLen: '" + this.contentLen + "', nread: '" + this.nread + "', pos: '" + this.pos + "', count: '" + this.count + "'" + "', timedOut: '" + this.timedOut + "'";
   }

   private void complain() throws ProtocolException {
      ProtocolException var1 = new ProtocolException("EOF after reading only: '" + this.nread + "' of: '" + this.contentLen + "' promised bytes, out of which at least: '" + this.count + "' were already buffered");
      this.in = null;
      this.buf = null;
      this.nread = this.contentLen = 0;
      throw var1;
   }

   private void checkPostSize() throws IOException {
      if (this.maxPostSize > 0 && (this.count >= this.maxPostSize || this.nread >= this.maxPostSize)) {
         HTTPLogger.logPOSTSizeExceeded(this.maxPostSize);
         throw new IOException("MaxPostSize exceeded");
      }
   }

   private void checkPostTime() throws PostTimeoutException {
      if (this.maxPostTime >= 0 && this.timeReading > (long)this.maxPostTime) {
         HTTPLogger.logPOSTTimeExceeded(this.maxPostTime / 1000);
         this.nread = 0;
         throw new PostTimeoutException("Attempt to read for more thanthe max POST read time: " + this.maxPostTime / 1000 + "' seconds");
      }
   }

   public int read() throws IOException {
      this.checkTimedOut();

      try {
         if (this.isNotChunkTransfer && this.nread == this.contentLen) {
            return -1;
         } else {
            if (this.pos == this.count) {
               this.checkPostTime();
               this.pos = 0;
               long var1 = System.currentTimeMillis();
               if (this.isNotChunkTransfer) {
                  this.count = this.in.read(this.buf, 0, Math.min(this.contentLen - this.nread, this.buf.length));
                  if (this.count < 0) {
                     this.complain();
                  }
               } else {
                  this.count = this.in.read(this.buf, 0, this.buf.length);
                  if (this.count == -1) {
                     return -1;
                  }
               }

               this.timeReading += System.currentTimeMillis() - var1;
            }

            ++this.nread;
            this.ms.incrementBytesReceivedCount(1L);
            return this.buf[this.pos++] & 255;
         }
      } catch (SocketTimeoutException var3) {
         this.setTimedOut(true);
         throw var3;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      this.checkTimedOut();

      try {
         int var4;
         if (this.isNotChunkTransfer) {
            var4 = this.contentLen - this.nread;
            if (var4 == 0) {
               return -1;
            } else {
               if (var3 > var4) {
                  var3 = var4;
               }

               var4 = this.count - this.pos;
               if (var4 == 0) {
                  this.checkPostTime();
                  long var9 = System.currentTimeMillis();
                  int var7 = this.in.read(var1, var2, var3);
                  this.timeReading += System.currentTimeMillis() - var9;
                  if (this.isNotChunkTransfer && var7 < 0) {
                     this.complain();
                  }

                  this.ms.incrementBytesReceivedCount((long)var7);
                  this.nread += var7;
                  return var7;
               } else {
                  if (var3 > var4) {
                     var3 = var4;
                  }

                  System.arraycopy(this.buf, this.pos, var1, var2, var3);
                  this.pos += var3;
                  this.nread += var3;
                  return var3;
               }
            }
         } else {
            for(var4 = var3; var4 > 0; --var4) {
               int var5 = this.read();
               if (var5 < 0) {
                  return -1;
               }

               var1[var2++] = (byte)var5;
            }

            return var3;
         }
      } catch (SocketTimeoutException var8) {
         this.setTimedOut(true);
         throw var8;
      }
   }

   public int available() throws IOException {
      int var1 = this.count - this.pos;
      int var2 = var1 + this.in.available();
      return this.isNotChunkTransfer ? Math.min(var2, this.contentLen) : var2;
   }

   private void skipUnreadBody() throws IOException {
      if (this.isNotChunkTransfer && this.nread < this.contentLen) {
         this.skip((long)(this.contentLen - this.nread));
      }

   }

   public void close() throws IOException {
      this.skipUnreadBody();
   }

   private void checkTimedOut() throws SocketTimeoutException {
      if (this.timedOut) {
         throw new SocketTimeoutException("read is alrady timed out");
      }
   }

   private void setTimedOut(boolean var1) {
      this.timedOut = var1;
   }
}
