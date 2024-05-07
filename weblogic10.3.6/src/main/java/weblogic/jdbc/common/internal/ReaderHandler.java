package weblogic.jdbc.common.internal;

import java.io.IOException;
import java.io.Reader;
import java.rmi.RemoteException;

public final class ReaderHandler extends Reader {
   private char[] chars = null;
   private int idx = 0;
   private boolean eof = false;
   private boolean closed = false;
   private ReaderBlockGetter rbg = null;
   private int id = 0;
   private boolean marked = false;

   public void setReaderBlockGetter(ReaderBlockGetter var1, int var2) {
      synchronized(this.lock) {
         this.rbg = var1;
         this.id = var2;
      }
   }

   public int read(char[] var1) throws IOException {
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

   public int read(char[] var1, int var2, int var3) throws IOException {
      if (var3 <= 0) {
         return 0;
      } else {
         synchronized(this.lock) {
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

                     int var7 = Math.min(this.chars.length - this.idx, var6);
                     System.arraycopy(this.chars, this.idx, var1, var2, var7);
                     var5 += var7;
                     this.idx += var7;
                     var2 += var7;
                  }

                  return var5;
               } catch (IOException var9) {
                  return var5;
               }
            }
         }
      }
   }

   public int read() throws IOException {
      synchronized(this.lock) {
         this.checkIfClosed();
         if (this.eof) {
            return -1;
         } else {
            this.refreshCacheIfNeeded();
            return this.eof ? -1 : this.chars[this.idx++] & 255;
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

   public void mark(int var1) throws IOException {
      try {
         if (this.rbg.markSupported(this.id)) {
            this.rbg.mark(this.id, var1);
            this.marked = true;
         }

      } catch (IOException var3) {
         throw new IOException(var3.getMessage());
      }
   }

   public void reset() throws IOException {
      if (this.marked) {
         try {
            this.rbg.reset(this.id);
            this.resetCache();
         } catch (IOException var2) {
            throw new IOException(var2.getMessage());
         }
      } else {
         throw new IOException("Reader was not marked.");
      }
   }

   public boolean markSupported() {
      return this.rbg == null ? false : this.rbg.markSupported(this.id);
   }

   public boolean ready() throws IOException {
      boolean var1 = false;

      try {
         var1 = this.rbg.ready(this.id);
         return var1;
      } catch (IOException var3) {
         throw new IOException(var3.getMessage());
      }
   }

   private void refreshCacheIfNeeded() throws IOException {
      if (this.chars == null || this.idx >= this.chars.length) {
         try {
            this.chars = this.rbg.getBlock(this.id);
            this.idx = 0;
            if (this.chars == null) {
               this.eof = true;
            }
         } catch (RemoteException var2) {
            this.resetCache();
            throw new IOException(var2.toString());
         }
      }

   }

   private void resetCache() {
      this.chars = null;
      this.eof = false;
      this.closed = false;
      this.idx = 0;
   }

   private void checkIfClosed() throws IOException {
      if (this.closed) {
         throw new IOException("Reader already closed");
      }
   }

   public void close() {
      synchronized(this.lock) {
         if (!this.closed) {
            this.closed = true;
            this.rbg.close(this.id);
            this.eof = true;
            this.chars = null;
            this.rbg = null;
         }
      }
   }
}
