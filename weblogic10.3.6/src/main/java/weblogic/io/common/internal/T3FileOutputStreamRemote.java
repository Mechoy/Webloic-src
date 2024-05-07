package weblogic.io.common.internal;

import java.io.IOException;
import weblogic.common.T3Exception;
import weblogic.io.common.T3FileOutputStream;

public final class T3FileOutputStreamRemote extends T3FileOutputStream {
   private static int count = 0;
   private int bufferSize;
   private int writeBehind;
   private T3RemoteOutputStream ros;

   public T3FileOutputStreamRemote(T3FileSystemProxy var1, T3FileRemote var2, int var3, int var4) throws T3Exception {
      this.bufferSize = var3;
      this.writeBehind = var4;
      ++count;
      this.ros = new T3RemoteOutputStream(var3, var4);
      this.ros.setOneWayRemote(var1.createOutputStream(this.ros, var2.getPath(), var3));
   }

   public int bufferSize() {
      return this.bufferSize;
   }

   public int writeBehind() {
      return this.writeBehind;
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.ros.write(var1, var2, var3);
   }

   public void write(int var1) throws IOException {
      this.ros.write(var1);
   }

   public void flush() throws IOException {
      this.ros.flush();
   }

   public void close() throws IOException {
      this.ros.close();
   }
}
