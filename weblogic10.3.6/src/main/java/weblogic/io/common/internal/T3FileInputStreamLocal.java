package weblogic.io.common.internal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import weblogic.common.T3Exception;
import weblogic.io.common.T3FileInputStream;

public final class T3FileInputStreamLocal extends T3FileInputStream {
   private FileInputStream is;

   public T3FileInputStreamLocal(T3FileLocal var1) throws T3Exception {
      try {
         this.is = new FileInputStream(var1.getPath());
      } catch (FileNotFoundException var3) {
         throw new T3Exception(var3.toString(), var3);
      }
   }

   public int bufferSize() {
      return 0;
   }

   public int readAhead() {
      return 0;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      return this.is.read(var1, var2, var3);
   }

   public int read() throws IOException {
      return this.is.read();
   }

   public long skip(long var1) throws IOException {
      return this.is.skip(var1);
   }

   public int available() throws IOException {
      return this.is.available();
   }

   public void close() throws IOException {
      this.is.close();
   }
}
