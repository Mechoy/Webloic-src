package weblogic.io.common.internal;

import java.io.IOException;
import weblogic.common.T3Exception;
import weblogic.io.common.T3FileInputStream;

public final class T3FileInputStreamRemote extends T3FileInputStream {
   private static int count = 0;
   private int bufferSize;
   private int readAhead;
   private T3RemoteInputStream ris;

   public T3FileInputStreamRemote(T3FileSystemProxy var1, T3FileRemote var2, int var3, int var4) throws T3Exception {
      this.bufferSize = var3;
      this.readAhead = var4;
      ++count;
      this.ris = new T3RemoteInputStream(var3, var4);
      this.ris.setOneWayRemote(var1.createInputStream(this.ris, var2.getPath(), var3, var4));
   }

   public int bufferSize() {
      return this.bufferSize;
   }

   public int readAhead() {
      return this.readAhead;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      return this.ris.read(var1, var2, var3);
   }

   public int read() throws IOException {
      return this.ris.read();
   }

   public long skip(long var1) throws IOException {
      return this.ris.skip(var1);
   }

   public int available() throws IOException {
      return this.ris.available();
   }

   public void close() throws IOException {
      this.ris.close();
   }
}
