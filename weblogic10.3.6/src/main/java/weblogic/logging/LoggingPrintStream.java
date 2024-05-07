package weblogic.logging;

import java.io.OutputStream;
import java.io.PrintStream;

public class LoggingPrintStream extends PrintStream {
   public LoggingPrintStream(OutputStream var1) {
      super(var1);
   }

   public void println() {
      super.println();
      this.flush();
   }

   public void println(boolean var1) {
      super.println(var1);
      this.flush();
   }

   public void println(char var1) {
      super.println(var1);
      this.flush();
   }

   public void println(int var1) {
      super.println(var1);
      this.flush();
   }

   public void println(long var1) {
      super.println(var1);
      this.flush();
   }

   public void println(float var1) {
      super.println(var1);
      this.flush();
   }

   public void println(double var1) {
      super.println(var1);
      this.flush();
   }

   public void println(char[] var1) {
      super.println(var1);
      this.flush();
   }

   public void println(String var1) {
      super.println(var1);
      this.flush();
   }

   public void println(Object var1) {
      super.println(var1);
      this.flush();
   }
}
