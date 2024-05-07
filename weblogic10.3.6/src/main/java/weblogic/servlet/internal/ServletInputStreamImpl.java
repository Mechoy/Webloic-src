package weblogic.servlet.internal;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletInputStream;
import weblogic.utils.http.HttpChunkInputStream;
import weblogic.utils.io.Chunk;

public final class ServletInputStreamImpl extends ServletInputStream {
   private InputStream in = this;
   private int nread;
   private boolean isClosed = false;

   public ServletInputStreamImpl(InputStream var1) {
      this.in = var1;
      this.nread = 0;
   }

   int getBytesRead() {
      return this.nread;
   }

   boolean isClosed() {
      return this.isClosed;
   }

   void ensureChunkedConsumed() throws IOException {
      if (this.in instanceof HttpChunkInputStream) {
         HttpChunkInputStream var1 = (HttpChunkInputStream)this.in;
         if (var1.hasUnconsumedChunk()) {
            var1.skipAllChunk();
         }
      }

   }

   public int available() throws IOException {
      return this.in.available();
   }

   public void close() throws IOException {
      this.in.close();
      this.isClosed = true;
   }

   public void mark(int var1) {
      if (!this.in.markSupported()) {
         this.in = new FilterInputStream(this.in) {
            private int pos = -1;
            private int size = -1;
            private byte[] markBuffer;
            private boolean markSet = false;

            public long skip(long var1) throws IOException {
               if (var1 <= 0L) {
                  return 0L;
               } else {
                  long var3 = var1;
                  int var5;
                  if (this.markSet && this.size > this.pos) {
                     var5 = this.size - this.pos;
                     if ((long)var5 >= var1) {
                        this.pos = (int)((long)this.pos + var1);
                        return var1;
                     }

                     this.markSet = false;
                     var3 = var1 - (long)var5;
                  }

                  Chunk var6 = Chunk.getChunk();

                  long var7;
                  try {
                     while(var3 > 0L) {
                        var5 = this.read(var6.buf, 0, (int)Math.min((long)Chunk.CHUNK_SIZE, var3));
                        if (var5 < 0) {
                           break;
                        }

                        var3 -= (long)var5;
                     }

                     var7 = var1 - var3;
                  } finally {
                     Chunk.releaseChunk(var6);
                  }

                  return var7;
               }
            }

            public void mark(int var1) {
               if (this.pos != this.size) {
                  int var2 = this.size - this.pos;
                  if (this.markBuffer != null && this.markBuffer.length < var1 + var2) {
                     byte[] var3 = new byte[var1 + var2];
                     System.arraycopy(this.markBuffer, this.pos, var3, 0, var2);
                     this.markBuffer = var3;
                  } else {
                     System.arraycopy(this.markBuffer, this.pos, this.markBuffer, 0, var2);
                  }

                  this.size = var2;
               } else {
                  if (this.markBuffer == null || this.markBuffer.length < var1) {
                     this.markBuffer = new byte[var1];
                  }

                  this.size = 0;
               }

               this.markSet = true;
               this.pos = 0;
            }

            public int available() throws IOException {
               return super.available() + (this.size - this.pos);
            }

            public boolean markSupported() {
               return true;
            }

            public int read() throws IOException {
               if (!this.markSet) {
                  return this.in.read();
               } else {
                  if (this.pos == this.size) {
                     int var1 = this.in.read();
                     if (var1 == -1) {
                        return -1;
                     }

                     if (this.size == this.markBuffer.length) {
                        this.markSet = false;
                        return var1;
                     }

                     this.markBuffer[this.size++] = (byte)var1;
                  }

                  return this.markBuffer[this.pos++] & 255;
               }
            }

            public int read(byte[] var1, int var2, int var3) throws IOException {
               if (!this.markSet) {
                  return this.in.read(var1, var2, var3);
               } else {
                  boolean var4 = false;
                  int var5 = this.size - this.pos;
                  int var6 = var3 - var5;
                  if (var6 > 0) {
                     byte[] var7 = new byte[var6];
                     int var8 = this.in.read(var7, 0, var6);
                     if (var8 == -1) {
                        if (var5 == 0) {
                           return -1;
                        }

                        var8 = 0;
                     }

                     if (this.size + var8 > this.markBuffer.length) {
                        this.markSet = false;
                        System.arraycopy(this.markBuffer, this.pos, var1, var2, var5);
                        System.arraycopy(var7, 0, var1, var2 + var5, var8);
                        return var5 + var8;
                     }

                     System.arraycopy(var7, 0, this.markBuffer, this.size, var8);
                     this.size += var8;
                  }

                  int var9 = Math.min(this.size - this.pos, var3);
                  System.arraycopy(this.markBuffer, this.pos, var1, var2, var9);
                  this.pos += var9;
                  return var9;
               }
            }

            public void reset() {
               this.pos = 0;
            }
         };
      }

      this.in.mark(var1);
   }

   public boolean markSupported() {
      return true;
   }

   public int read() throws IOException {
      int var1 = this.in.read();
      if (var1 > 0) {
         ++this.nread;
      }

      return var1;
   }

   public int read(byte[] var1) throws IOException {
      int var2 = this.in.read(var1);
      if (var2 > 0) {
         this.nread += var2;
      }

      return var2;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.in.read(var1, var2, var3);
      if (var4 > 0) {
         this.nread += var4;
      }

      return var4;
   }

   public void reset() throws IOException {
      this.in.reset();
      this.nread = 0;
   }

   public long skip(long var1) throws IOException {
      return this.in.skip(var1);
   }
}
