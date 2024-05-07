package weblogic.net.http;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import weblogic.utils.io.NullInputStream;

public final class KeepAliveStream extends FilterInputStream {
   protected boolean closed = false;
   protected int expected;
   protected int count = 0;
   protected int markedCount = 0;
   protected int markLimit = -1;
   protected HttpClient client;

   public KeepAliveStream(HttpClient var1, InputStream var2, int var3) {
      super((InputStream)(var3 == 0 ? NullInputStream.getInstance() : var2));
      this.expected = var3;
      this.client = var1;
   }

   private final void justRead(int var1) throws IOException {
      if (var1 == -1) {
         if (!this.isMarked()) {
            this.close();
         }

      } else {
         this.count += var1;
         if (this.count > this.markLimit) {
            this.markLimit = -1;
         }

         if (!this.isMarked()) {
            if (this.expected != -1 && this.count >= this.expected) {
               this.close();
            }

         }
      }
   }

   private boolean isMarked() {
      if (this.markLimit < 0) {
         return false;
      } else {
         return this.count <= this.markLimit;
      }
   }

   public int read() throws IOException {
      if (this.closed) {
         return -1;
      } else {
         int var1 = this.in.read();
         if (var1 != -1) {
            this.justRead(1);
         } else {
            this.justRead(var1);
         }

         return var1;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         return -1;
      } else {
         int var4 = this.in.read(var1, var2, var3);
         this.justRead(var4);
         return var4;
      }
   }

   public long skip(long var1) throws IOException {
      if (this.closed) {
         return 0L;
      } else {
         int var3 = (int)var1;
         if (this.expected != -1 && var1 > (long)(this.expected - this.count)) {
            var3 = this.expected - this.count;
         }

         var1 = this.in.skip((long)var3);
         this.justRead((int)var1);
         return var1;
      }
   }

   public int available() throws IOException {
      return this.closed ? 0 : this.in.available();
   }

   public synchronized void close() throws IOException {
      if (!this.closed) {
         boolean var1 = false;

         try {
            if (this.expected == -1) {
               var1 = this.in.read() != -1;
            }

            if (var1 || this.expected > this.count) {
               this.client.setKeepingAlive(false);
               this.in.close();
            }
         } finally {
            this.closed = true;
            HttpClient.finished(this.client);
         }

      }
   }

   public void mark(int var1) {
      if (!this.closed && !this.client.isKeepingAlive()) {
         super.mark(var1);
         this.markedCount = this.count;
         this.markLimit = var1;
      }
   }

   public void reset() throws IOException {
      if (!this.closed) {
         if (this.client.isKeepingAlive()) {
            throw new IOException("mark/reset not supported");
         } else if (!this.isMarked()) {
            throw new IOException("Resetting to an invalid mark");
         } else {
            this.count = this.markedCount;
            super.reset();
         }
      }
   }

   public boolean markSupported() {
      return !this.closed && !this.client.isKeepingAlive() ? super.markSupported() : false;
   }
}
