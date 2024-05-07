package weblogic.io.common.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public final class DiscardPipe extends OutputStream {
   public void write(int var1) throws IOException {
   }

   public void write(byte[] var1) throws IOException {
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
   }

   public PrintStream getPrintStream() {
      return new PrintStream(this);
   }
}
