package weblogic.jdbc.common.internal;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

public final class InputStreamHandler extends InputStream {
   private byte[] bytes = null;
   private int idx = 0;
   private boolean eof = false;
   private boolean closed = false;
   private BlockGetter bg = null;
   private int id = 0;
   private boolean marked = false;

   public void setBlockGetter(BlockGetter var1, int var2) {
      synchronized(this) {
         this.bg = var1;
         this.id = var2;
      }
   }

   public int read() throws IOException {
      synchronized(this) {
         this.checkIfClosed();
         if (this.eof) {
            return -1;
         } else {
            this.refreshCacheIfNeeded();
            return this.eof ? -1 : this.bytes[this.idx++] & 255;
         }
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (var2 >= 0 && var2 <= var1.length && var3 >= 0 && var2 + var3 <= var1.length && var2 + var3 >= 0) {
         if (var3 == 0) {
            return 0;
         } else {
            synchronized(this) {
               this.checkIfClosed();
               if (this.eof) {
                  return -1;
               } else {
                  int var5 = 0;

                  try {
                     for(int var6 = var3 - var5; var6 > 0; var6 = var3 - var5) {
                        this.refreshCacheIfNeeded();
                        if (this.eof) {
                           int var10000 = var5;
                           return var10000;
                        }

                        int var7 = Math.min(this.bytes.length - this.idx, var6);
                        System.arraycopy(this.bytes, this.idx, var1, var2, var7);
                        var5 += var7;
                        this.idx += var7;
                        var2 += var7;
                     }
                  } catch (IOException var9) {
                  }

                  return var5;
               }
            }
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public int read(byte[] var1) throws IOException {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         boolean var2 = true;

         try {
            int var5 = this.read(var1, 0, var1.length);
            return var5;
         } catch (IOException var4) {
            throw new IOException(var4.getMessage());
         }
      }
   }

   public long skip(long var1) throws IOException {
      if (var1 <= 0L) {
         return 0L;
      } else {
         this.checkIfClosed();
         int var3 = 0;

         try {
            if (!this.eof) {
               while((long)var3 < var1 && this.read() != -1) {
                  ++var3;
               }
            }
         } catch (IOException var5) {
            throw new IOException(var5.getMessage());
         }

         return (long)var3;
      }
   }

   public int available() throws IOException {
      boolean var1 = false;

      try {
         int var4 = this.bg.available(this.id);
         return var4;
      } catch (IOException var3) {
         throw new IOException(var3.getMessage());
      }
   }

   public void mark(int var1) {
      if (this.bg.markSupported(this.id)) {
         this.bg.mark(this.id, var1);
         this.marked = true;
      }

   }

   public void reset() throws IOException {
      if (this.marked) {
         try {
            this.bg.reset(this.id);
            this.resetCache();
         } catch (IOException var2) {
            throw new IOException(var2.getMessage());
         }
      } else {
         throw new IOException("Stream was not marked.");
      }
   }

   public boolean markSupported() {
      return this.bg.markSupported(this.id);
   }

   private void refreshCacheIfNeeded() throws IOException {
      if (this.bytes == null || this.idx >= this.bytes.length) {
         try {
            this.bytes = this.bg.getBlock(this.id);
            this.idx = 0;
            if (this.bytes == null) {
               this.eof = true;
            }
         } catch (RemoteException var2) {
            this.resetCache();
            throw new IOException(var2.toString());
         }
      }

   }

   private void resetCache() {
      this.bytes = null;
      this.eof = false;
      this.closed = false;
      this.idx = 0;
   }

   private void checkIfClosed() throws IOException {
      if (this.closed) {
         throw new IOException("Stream already closed");
      }
   }

   public void close() {
      synchronized(this) {
         if (!this.closed) {
            this.closed = true;
            this.bg.close(this.id);
            this.eof = true;
            this.bytes = null;
            this.bg = null;
         }
      }
   }
}
