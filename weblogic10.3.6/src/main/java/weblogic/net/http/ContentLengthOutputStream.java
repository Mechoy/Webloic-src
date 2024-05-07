package weblogic.net.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ProtocolException;

/** @deprecated */
public final class ContentLengthOutputStream extends OutputStream {
   private boolean closed;
   private int count;
   private int clen;
   private OutputStream os;

   public ContentLengthOutputStream(OutputStream var1, int var2) {
      this.os = var1;
      this.clen = var2;
      this.closed = false;
      this.count = 0;
   }

   public synchronized void write(int var1) throws IOException {
      if (this.closed) {
         throw new IOException("stream is closed");
      } else if (this.count + 1 > this.clen) {
         throw new ProtocolException("Exceeding stated content length of " + this.clen);
      } else {
         this.os.write(var1);
         ++this.count;
      }
   }

   public synchronized void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("stream is closed");
      } else if (this.count + var3 > this.clen) {
         throw new ProtocolException("Exceeding stated content length of " + this.clen);
      } else {
         this.os.write(var1, var2, var3);
         this.count += var3;
      }
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public synchronized void close() throws IOException {
      if (!this.closed) {
         if (this.count != this.clen) {
            throw new ProtocolException("Did not meet stated content length of OutputStream:  you wrote " + this.count + " bytes and I was expecting " + " you to write exactly " + this.clen + " bytes!!!");
         } else {
            this.closed = true;
            this.os.flush();
         }
      }
   }
}
