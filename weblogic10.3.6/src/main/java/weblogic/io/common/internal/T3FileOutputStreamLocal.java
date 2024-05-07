package weblogic.io.common.internal;

import java.io.FileOutputStream;
import java.io.IOException;
import weblogic.common.T3Exception;
import weblogic.io.common.T3FileOutputStream;

public final class T3FileOutputStreamLocal extends T3FileOutputStream {
   private FileOutputStream os;

   public T3FileOutputStreamLocal(T3FileLocal var1) throws T3Exception {
      try {
         this.os = new FileOutputStream(var1.getPath());
      } catch (IOException var3) {
         throw new T3Exception(var3.toString(), var3);
      }
   }

   public int bufferSize() {
      return 0;
   }

   public int writeBehind() {
      return 0;
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.os.write(var1, var2, var3);
   }

   public void write(int var1) throws IOException {
      this.os.write(var1);
   }

   public void flush() throws IOException {
      this.os.flush();
   }

   public void close() throws IOException {
      this.os.close();
   }
}
