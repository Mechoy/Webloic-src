package weblogic.servlet.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class UnsynchronizedPrintWriter extends PrintWriter {
   public UnsynchronizedPrintWriter(Writer var1) {
      super(var1);
   }

   public UnsynchronizedPrintWriter(Writer var1, boolean var2) {
      super(var1, var2);
   }

   public void flush() {
      try {
         this.out.flush();
      } catch (IOException var2) {
         this.setError();
      }

   }

   public void write(int var1) {
      try {
         this.out.write(var1);
      } catch (IOException var3) {
         this.setError();
      }

   }

   public void write(char[] var1, int var2, int var3) {
      try {
         this.out.write(var1, var2, var3);
      } catch (IOException var5) {
         this.setError();
      }

   }

   public void write(String var1, int var2, int var3) {
      try {
         this.out.write(var1, var2, var3);
      } catch (IOException var5) {
         this.setError();
      }

   }

   public void println() {
      try {
         this.out.write("\r\n");
      } catch (IOException var2) {
         this.setError();
      }

   }

   public void println(boolean var1) {
      super.print(var1);
      this.println();
   }

   public void println(char var1) {
      super.print(var1);
      this.println();
   }

   public void println(int var1) {
      super.print(var1);
      this.println();
   }

   public void println(long var1) {
      super.print(var1);
      this.println();
   }

   public void println(float var1) {
      super.print(var1);
      this.println();
   }

   public void println(double var1) {
      super.print(var1);
      this.println();
   }

   public void println(char[] var1) {
      super.print(var1);
      this.println();
   }

   public void println(String var1) {
      super.print(var1);
      this.println();
   }

   public void println(Object var1) {
      String var2 = String.valueOf(var1);
      this.print(var2);
      this.println();
   }

   public void close() {
      try {
         this.out.close();
      } catch (IOException var2) {
         this.setError();
      }

   }
}
