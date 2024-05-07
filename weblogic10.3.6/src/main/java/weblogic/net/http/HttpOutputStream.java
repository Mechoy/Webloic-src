package weblogic.net.http;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/** @deprecated */
public final class HttpOutputStream extends FilterOutputStream {
   public HttpOutputStream(OutputStream var1) {
      super(var1);
   }

   public void write(int var1) throws IOException {
      this.out.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.out.write(var1, var2, var3);
   }

   public void write(byte[] var1) throws IOException {
      this.out.write(var1, 0, var1.length);
   }

   public void print(String var1) throws IOException {
      int var2 = var1.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.write(var1.charAt(var3));
      }

   }
}
